package com.github.teachingai.ollama;

import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.Generation;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;

import java.util.List;

public class Ollama_Prompt_Test2 {

    public static void main(String[] args) {

        /**
         * qwen2:7b ：https://ollama.com/library/qwen2
         * gemma2:9b ：https://ollama.com/library/gemma2
         * llama3:8b ：https://ollama.com/library/llama3
         * mistral ：https://ollama.com/library/mistral
         */
        var ollamaApi = new OllamaApi();
        var chatModel = new OllamaChatModel(ollamaApi);

        // 系统提示消息
        SystemMessage systemMessage = new SystemMessage("你的任务是识别用户对手机流量套餐产品的选择条件。\n" +
                        "每种流量套餐产品包含三个属性：名称，月费价格，月流量。\n" +
                        "根据用户输入，识别用户在上述三种属性上的倾向。" +
                        "以JSON格式输出。");

        String input_text = "办个100G以上的套餐";

        UserMessage userMessage = new UserMessage(input_text);

        List<Message> messages  = List.of(systemMessage, userMessage);

        Prompt prompt = new Prompt(messages, OllamaOptions.create()
                .withModel("qwen2:7b")
                .withFormat("json")
                .withTemperature(0f));

        ChatResponse resp = chatModel.call(prompt);

        for (Generation generation : resp.getResults()) {
            System.out.println(generation.getOutput().getContent());
        }

    }

}
