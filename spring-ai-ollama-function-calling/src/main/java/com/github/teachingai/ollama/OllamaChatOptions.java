package com.github.teachingai.ollama;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.teachingai.ollama.request.ApiRequest;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.ai.model.function.FunctionCallingOptions;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OllamaChatOptions extends OllamaOptions implements FunctionCallingOptions, ChatOptions {


    /**
     * A list of tools the model may call. Currently, only functions are supported as a
     * tool. Use this to provide a list of functions the model may generate JSON inputs
     * for.
     */
    @NestedConfigurationProperty
    private @JsonProperty("tools") List<ApiRequest.FunctionTool> tools;

    /**
     * Controls which (if any) function is called by the model. none means the model will
     * not call a function and instead generates a message. auto means the model can pick
     * between generating a message or calling a function.
     */
    @NestedConfigurationProperty
    private @JsonProperty("tool_choice") ApiRequest.ChatCompletionRequest.ToolChoice toolChoice;

    /**
     * MistralAI Tool Function Callbacks to register with the ChatClient. For Prompt
     * Options the functionCallbacks are automatically enabled for the duration of the
     * prompt execution. For Default Options the functionCallbacks are registered but
     * disabled by default. Use the enableFunctions to set the functions from the registry
     * to be used by the ChatClient chat completion requests.
     */
    @NestedConfigurationProperty
    @JsonIgnore
    private List<FunctionCallback> functionCallbacks = new ArrayList<>();

    /**
     * List of functions, identified by their names, to configure for function calling in
     * the chat completion requests. Functions with those names must exist in the
     * functionCallbacks registry. The {@link #functionCallbacks} from the PromptOptions
     * are automatically enabled for the duration of the prompt execution.
     *
     * Note that function enabled with the default options are enabled for all chat
     * completion requests. This could impact the token count and the billing. If the
     * functions is set in a prompt options, then the enabled functions are only active
     * for the duration of this prompt execution.
     */
    @NestedConfigurationProperty
    @JsonIgnore
    private Set<String> functions = new HashSet<>();


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final OllamaChatOptions options = new OllamaChatOptions();

        public Builder withModel(String model) {
            this.options.setModel(model);
            return this;
        }

        public Builder withFormat(String format) {
            this.options.setFormat(format);
            return this;
        }

        public Builder withKeepAlive(String keepAlive) {
            this.options.setKeepAlive(keepAlive);
            return this;
        }

        public Builder withUseNUMA(Boolean useNUMA) {
            this.options.setUseNUMA(useNUMA);
            return this;
        }

        public Builder withNumCtx(Integer numCtx) {
            this.options.setNumCtx(numCtx);
            return this;
        }

        public Builder withNumBatch(Integer numBatch) {
            this.options.setNumBatch(numBatch);
            return this;
        }

        public Builder withNumGQA(Integer numGQA) {
            this.options.setNumGQA(numGQA);
            return this;
        }

        public Builder withNumGPU(Integer numGPU) {
            this.options.setNumGPU(numGPU);
            return this;
        }

        public Builder withMainGPU(Integer mainGPU) {
            this.options.setMainGPU(mainGPU);
            return this;
        }

        public Builder withLowVRAM(Boolean lowVRAM) {
            this.options.setLowVRAM(lowVRAM);
            return this;
        }

        public Builder withF16KV(Boolean f16KV) {
            this.options.setF16KV(f16KV);
            return this;
        }

        public Builder withLogitsAll(Boolean logitsAll) {
            this.options.setLogitsAll(logitsAll);
            return this;
        }

        public Builder withVocabOnly(Boolean vocabOnly) {
            this.options.setVocabOnly(vocabOnly);
            return this;
        }

        public Builder withUseMMap(Boolean useMMap) {
            this.options.setUseMMap(useMMap);
            return this;
        }

        public Builder withUseMLock(Boolean useMLock) {
            this.options.setUseMLock(useMLock);
            return this;
        }

        public Builder withRopeFrequencyBase(Float ropeFrequencyBase) {
            this.options.setRopeFrequencyBase(ropeFrequencyBase);
            return this;
        }

        public Builder withRopeFrequencyScale(Float ropeFrequencyScale) {
            this.options.setRopeFrequencyScale(ropeFrequencyScale);
            return this;
        }

        public Builder withNumThread(Integer numThread) {
            this.options.setNumThread(numThread);
            return this;
        }

        public Builder withNumKeep(Integer numKeep) {
            this.options.setNumKeep(numKeep);
            return this;
        }

        public Builder withSeed(Integer seed) {
            this.options.setSeed(seed);
            return this;
        }


        public Builder withNumPredict(Integer numPredict) {
            this.options.setNumPredict(numPredict);
            return this;
        }

        public Builder withTopK(Integer topK) {
            this.options.setTopK(topK);
            return this;
        }

        public Builder withTopP(Float topP) {
            this.options.setTopP(topP);
            return this;
        }

        public Builder withTfsZ(Float tfsZ) {
            this.options.setTfsZ(tfsZ);
            return this;
        }

        public Builder withTypicalP(Float typicalP) {
            this.options.setTypicalP(typicalP);
            return this;
        }

        public Builder withRepeatLastN(Integer repeatLastN) {
            this.options.setRepeatLastN(repeatLastN);
            return this;
        }

        public Builder withTemperature(Float temperature) {
            this.options.setTemperature(temperature);
            return this;
        }

        public Builder withRepeatPenalty(Float repeatPenalty) {
            this.options.setRepeatPenalty(repeatPenalty);
            return this;
        }

        public Builder withPresencePenalty(Float presencePenalty) {
            this.options.setPresencePenalty(presencePenalty);
            return this;
        }

        public Builder withFrequencyPenalty(Float frequencyPenalty) {
            this.options.setFrequencyPenalty(frequencyPenalty);
            return this;
        }

        public Builder withMirostat(Integer mirostat) {
            this.options.setMirostat(mirostat);
            return this;
        }

        public Builder withMirostatTau(Float mirostatTau) {
            this.options.setMirostatTau(mirostatTau);
            return this;
        }

        public Builder withMirostatEta(Float mirostatEta) {
            this.options.setMirostatEta(mirostatEta);
            return this;
        }

        public Builder withPenalizeNewline(Boolean penalizeNewline) {
            this.options.setPenalizeNewline(penalizeNewline);
            return this;
        }

        public Builder withStop(List<String> stop) {
            this.options.setStop(stop);
            return this;
        }

        public Builder withTools(List<ApiRequest.FunctionTool> tools) {
            this.options.tools = tools;
            return this;
        }

        public Builder withToolChoice(ApiRequest.ChatCompletionRequest.ToolChoice toolChoice) {
            this.options.toolChoice = toolChoice;
            return this;
        }

        public Builder withFunctionCallbacks(List<FunctionCallback> functionCallbacks) {
            this.options.functionCallbacks = functionCallbacks;
            return this;
        }

        public Builder withFunctions(Set<String> functionNames) {
            Assert.notNull(functionNames, "Function names must not be null");
            this.options.functions = functionNames;
            return this;
        }

        public Builder withFunction(String functionName) {
            Assert.hasText(functionName, "Function name must not be empty");
            this.options.functions.add(functionName);
            return this;
        }

        public OllamaChatOptions build() {
            return this.options;
        }

    }

    @Override
    public List<FunctionCallback> getFunctionCallbacks() {
        return this.functionCallbacks;
    }

    @Override
    public void setFunctionCallbacks(List<FunctionCallback> functionCallbacks) {
        Assert.notNull(functionCallbacks, "FunctionCallbacks must not be null");
        this.functionCallbacks = functionCallbacks;
    }

    @Override
    public Set<String> getFunctions() {
        return this.functions;
    }

    @Override
    public void setFunctions(Set<String> functions) {
        Assert.notNull(functions, "Function must not be null");
        this.functions = functions;
    }
}
