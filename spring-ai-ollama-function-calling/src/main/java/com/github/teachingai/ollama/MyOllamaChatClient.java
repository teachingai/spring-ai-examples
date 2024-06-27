package com.github.teachingai.ollama;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.Generation;
import org.springframework.ai.chat.StreamingChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.metadata.ChatGenerationMetadata;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.ModelOptionsUtils;
import org.springframework.ai.model.function.AbstractFunctionCallSupport;
import org.springframework.ai.model.function.FunctionCallbackContext;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.ollama.metadata.OllamaChatResponseMetadata;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;

import java.util.Base64;
import java.util.List;
import java.util.Objects;

public class MyOllamaChatClient extends AbstractFunctionCallSupport<MyOllamaApi.Message, MyOllamaApi.ChatRequest, ResponseEntity<MyOllamaApi.ChatResponse>> implements ChatClient, StreamingChatClient {

    /**
     * Low-level Ollama API library.
     */
    private final MyOllamaApi chatApi;

    /**
     * Default options to be used for all chat requests.
     */
    private OllamaChatOptions defaultOptions;

    public MyOllamaChatClient(MyOllamaApi chatApi) {
        this(chatApi, OllamaChatOptions.builder().withModel(OllamaOptions.DEFAULT_MODEL).build());
    }

    public MyOllamaChatClient(MyOllamaApi chatApi, OllamaChatOptions defaultOptions) {
        this(chatApi, defaultOptions, null);
    }

    public MyOllamaChatClient(MyOllamaApi chatApi, OllamaChatOptions defaultOptions, FunctionCallbackContext functionCallbackContext) {
        super(functionCallbackContext);
        Assert.notNull(chatApi, "MyOllamaApi must not be null");
        Assert.notNull(defaultOptions, "DefaultOptions must not be null");
        this.chatApi = chatApi;
        this.defaultOptions = defaultOptions;
    }

    /**
     * @deprecated Use {@link OllamaOptions#setModel} instead.
     */
    @Deprecated
    public MyOllamaChatClient withModel(String model) {
        this.defaultOptions.setModel(model);
        return this;
    }

    /**
     * @deprecated Use {@link OllamaOptions} constructor instead.
     */
    public MyOllamaChatClient withDefaultOptions(OllamaChatOptions options) {
        this.defaultOptions = options;
        return this;
    }

    @Override
    public ChatResponse call(Prompt prompt) {

        MyOllamaApi.ChatResponse response = this.chatApi.chat(ollamaChatRequest(prompt, false));

        var generator = new Generation(response.message().content());
        if (response.promptEvalCount() != null && response.evalCount() != null) {
            generator = generator.withGenerationMetadata(ChatGenerationMetadata.from("unknown", null));
        }
        return new ChatResponse(List.of(generator), MyOllamaChatResponseMetadata.from(response));
    }

    @Override
    public Flux<ChatResponse> stream(Prompt prompt) {

        Flux<MyOllamaApi.ChatResponse> response = this.chatApi.streamingChat(ollamaChatRequest(prompt, true));

        return response.map(chunk -> {
            Generation generation = (chunk.message() != null) ? new Generation(chunk.message().content())
                    : new Generation("");
            if (Boolean.TRUE.equals(chunk.done())) {
                generation = generation.withGenerationMetadata(ChatGenerationMetadata.from("unknown", null));
            }
            return new ChatResponse(List.of(generation), MyOllamaChatResponseMetadata.from(chunk));
        });
    }

    /**
     * Package access for testing.
     */
    MyOllamaApi.ChatRequest ollamaChatRequest(Prompt prompt, boolean stream) {

        List<MyOllamaApi.Message> ollamaMessages = prompt.getInstructions()
                .stream()
                .filter(message -> message.getMessageType() == MessageType.USER
                        || message.getMessageType() == MessageType.ASSISTANT
                        || message.getMessageType() == MessageType.SYSTEM)
                .map(m -> {
                    var messageBuilder = MyOllamaApi.Message.builder(toRole(m)).withContent(m.getContent());

                    if (!CollectionUtils.isEmpty(m.getMedia())) {
                        messageBuilder
                                .withImages(m.getMedia().stream().map(media -> this.fromMediaData(media.getData())).toList());
                    }
                    return messageBuilder.build();
                })
                .toList();

        // runtime options
        OllamaOptions runtimeOptions = null;
        if (prompt.getOptions() != null) {
            if (prompt.getOptions() instanceof ChatOptions runtimeChatOptions) {
                runtimeOptions = ModelOptionsUtils.copyToTarget(runtimeChatOptions, ChatOptions.class,
                        OllamaOptions.class);
            }
            else {
                throw new IllegalArgumentException("Prompt options are not of type ChatOptions: "
                        + prompt.getOptions().getClass().getSimpleName());
            }
        }

        OllamaOptions mergedOptions = ModelOptionsUtils.merge(runtimeOptions, this.defaultOptions, OllamaOptions.class);

        // Override the model.
        if (!StringUtils.hasText(mergedOptions.getModel())) {
            throw new IllegalArgumentException("Model is not set!");
        }

        String model = mergedOptions.getModel();
        MyOllamaApi.ChatRequest.Builder requestBuilder = MyOllamaApi.ChatRequest.builder(model)
                .withStream(stream)
                .withMessages(ollamaMessages)
                .withOptions(mergedOptions);

        if (mergedOptions.getFormat() != null) {
            requestBuilder.withFormat(mergedOptions.getFormat());
        }

        if (mergedOptions.getKeepAlive() != null) {
            requestBuilder.withKeepAlive(mergedOptions.getKeepAlive());
        }

        return requestBuilder.build();
    }

    private String fromMediaData(Object mediaData) {
        if (mediaData instanceof byte[] bytes) {
            return Base64.getEncoder().encodeToString(bytes);
        }
        else if (mediaData instanceof String text) {
            return text;
        }
        else {
            throw new IllegalArgumentException("Unsupported media data type: " + mediaData.getClass().getSimpleName());
        }

    }

    private MyOllamaApi.Message.Role toRole(Message message) {

        switch (message.getMessageType()) {
            case USER:
                return MyOllamaApi.Message.Role.USER;
            case ASSISTANT:
                return MyOllamaApi.Message.Role.ASSISTANT;
            case SYSTEM:
                return MyOllamaApi.Message.Role.SYSTEM;
            default:
                throw new IllegalArgumentException("Unsupported message type: " + message.getMessageType());
        }
    }

    @Override
    protected MyOllamaApi.ChatRequest doCreateToolResponseRequest(MyOllamaApi.ChatRequest previousRequest, MyOllamaApi.Message responseMessage, List<MyOllamaApi.Message> conversationHistory) {
        return previousRequest;
    }

    @Override
    protected List<MyOllamaApi.Message> doGetUserMessages(MyOllamaApi.ChatRequest request) {
        return request.messages();
    }

    @Override
    protected MyOllamaApi.Message doGetToolResponseMessage(ResponseEntity<MyOllamaApi.ChatResponse> response) {
        return response.getBody().message();
    }

    @Override
    protected ResponseEntity<MyOllamaApi.ChatResponse> doChatCompletion(MyOllamaApi.ChatRequest request) {
        return ResponseEntity.ofNullable(this.chatApi.chat(request));
    }

    @Override
    protected boolean isToolFunctionCall(ResponseEntity<MyOllamaApi.ChatResponse> chatCompletion) {

        var body = chatCompletion.getBody();
        if (body == null) {
            return false;
        }
        return Boolean.TRUE;
/*
        var choices = body.choices();
        if (CollectionUtils.isEmpty(choices)) {
            return false;
        }

        return !CollectionUtils.isEmpty(choices.get(0).message().toolCalls());*/
    }
}
