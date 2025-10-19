## spring-ai-ollama-tts-unified

> 基于 [Spring Boot 3.x](https://docs.spring.io/spring-boot/index.html) 、[Spring AI](https://docs.spring.io/spring-ai/reference/index.html)、[Ollama](https://ollama.com/) 和 [UnifiedTTS](https://unifiedtts.com/) 的 Text-To-Speech (TTS) 功能示例。

整合 ChatTTS 与 本地 Ollama 服务器并输出 Text-To-Speech (TTS) 音频响应

下面是离线模式下2种工具的简单组合：

- 大型语言模型：在离线模式下运行 [Ollama](https://ollama.com/) 本地模型
- 文本转语音：在离线模式下运行 [MARS5-TTS](https://github.com/camb-ai/mars5-tts) 本地模型

### 先决条件

您首先需要在本地计算机上运行 Ollama。请参阅官方 [Ollama 项目自述文件](https://github.com/ollama/ollama "Ollama 项目自述文件")，开始在本地计算机上运行模型。

ChatTTS 不提供API功能，还需要再本地计算机上运行 ChatTTS-ui，请参阅官方 [ChatTTS-ui](https://github.com/jianchang512/ChatTTS-ui) 项目进行调用。

#### 添加存储库和 BOM

Spring AI 工件发布在 `Spring Milestone` 和 `Snapshot` 存储库中。请参阅存储库部分将这些存储库添加到您的构建系统中。

为了帮助进行依赖管理，Spring AI 提供了 BOM（物料清单），以确保在整个项目中使用一致的 Spring AI 版本。请参阅[依赖管理](https://docs.spring.io/spring-ai/reference/getting-started.html#dependency-management "依赖管理")部分将 Spring AI BOM 添加到您的构建系统。

#### 自动配置

Spring AI 为 Ollama 聊天客户端提供 Spring Boot 自动配置。要启用它，请将以下依赖项添加到项目的 Maven `pom.xml` 文件中：

```xml
<dependency>
   <groupId>org.springframework.ai</groupId>
   <artifactId>spring-ai-starter-model-ollama</artifactId>
</dependency>
```

或者，在你的 Gradle 构建文件 `build.gradle` 中添加：

```groovy
dependencies {
    implementation 'org.springframework.ai:spring-ai-ollama-spring-boot-starter'
}
```

### Ollama Chat 模型

Ollama Chat 模型是一个基于大型语言模型的对话系统，支持对话问答、文本生成等功能。

#### Qwen2

> Qwen2 是阿里巴巴集团推出的全新系列大型语言模型。<br/>
Qwen2基于29 种语言的数据进行训练，包括英语和中文。

**注意**: 安装 `ollama run qwen2` 将下载一个 4.4GB 的 docker 镜像。

它有 4 种参数大小：0.5B、1.5B、7B、72B。
在 7B 和 72B 模型中，上下文长度已扩展至128k 个 token。

| Models       | Qwen2-0.5B | Qwen2-1.5B | Qwen2-7B | Qwen2-72B |
|--------------|------------|------------|----------|-----------|
| Params       | 0.49B      | 1.54B      | 7.07B    | 72.71B    |
| Non-Emb Params | 0.35B    | 1.31B      | 5.98B    | 70.21B    |
| GQA          | True       | True       | True     | True      |
| Tie Embedding | True      | True       | False    | False     |
| Context Length | 32K      | 32K        | 128K     | 128K      |

- 文档地址：https://ollama.com/library/qwen2

#### Llama 3

> Meta Llama 3 是 Meta Inc. 开发的一系列模型，是最新推出的先进模型，具有8B和70B两种参数大小（预训练或指令调整）。<br/>
Llama 3 指令调整模型针对对话/聊天用例进行了微调和优化，并且在常见基准测试中胜过许多可用的开源聊天模型。

**注意**: 安装 `ollama run llama3` 将下载一个 4.7GB 的 docker 镜像。

##### 模型变体

Instruct 是针对聊天/对话场景进行了fine-tuned。

```shell
ollama run llama3
ollama run llama3:70b
```

Pre-trained 的是基础模型。

```shell
ollama run llama3:text
ollama run llama3:70b-text
```

- 文档地址：https://ollama.com/library/llama3

### Text-To-Speech (TTS)

#### UnifiedTTS

>  **UnifiedTTS 是提供一站式文本转语音（TTS）服务，统一的 API 接口平台。**整合多种主流的 TTS 服务，包括 Microsoft Azure、MiniMax、阿里云和 ElevenLabs 等。开发者无需分别研究不同供应商的 API 文档，只需通过 UnifiedTTS 的单一接口，即可接入这些服务。支持多种语言，包括中文、英文、日文和韩文等，满足全球业务的需求。开发者可以根据业务需求灵活切换不同的语音服务，简单更改参数可在不同供应商模型之间切换，选择最适合的语音和风格。标准化了速度、音量、音调等参数，自动转换为对应供应商的格式。提供统一的账户管理和性能监控功能，开发者可以实时监控供应商的响应速度和质量，获取详细的使用统计和性能报告。

##### UnifiedTTS 的主要功能

- 统一接口：一个接口连接所有 TTS 服务，无需研究各供应商 API 文档，节省开发时间和集成成本。
- 统一参数：提供标准化参数，自动转换为对应供应商格式，解决不同 TTS 接口参数不一致问题。
- 统一管理：无需注册多个供应商账户，一站式管理 API 密钥和账单。
- 多语言支持：支持中文、英文、日文、韩文等多种语言，满足全球业务需求。
- 灵活切换：更改参数即可在不同供应商模型间切换，提供多种语音和风格选项。
- 性能监控：实时监控供应商响应速度和质量，提供使用统计和性能报告。

##### UnifiedTTS 的官网地址

官网地址：https://unifiedtts.com/

##### 如何使用UnifiedTTS

- 一键登录：访问 UnifiedTTS 官网，免费注册账号并登录，可获得试用积分。
- 获取 API 密钥：在用户仪表盘中生成专属的 API 密钥，用于后续的 API 调用。
- 开始调用：使用 UnifiedTTS 提供的 API，结合获取的 API 密钥，开始进行语音合成调用。

##### UnifiedTTS相关的人工智能知识

- **自然语言处理（NLP）**：自然语言处理是 TTS 技术的关键部分，使计算机能理解人类语言的结构和含义。通过语法分析、语义理解等技术，NLP 能将文本转换为语音合成引擎可以处理的形式，生成自然流畅的语音输出。
- **语音合成技术**：语音合成技术利用深度学习和神经网络模型，将文本转换为高质量的语音。这些模型通过学习大量的语音数据，能生成接近人类自然语音的合成语音，确保语音的自然度和可理解性。
- **多语言模型**：为了支持多种语言的语音合成，需要构建多语言模型。这些模型能处理不同语言的语音特征和发音规则，涉及语言学知识和跨语言的语音合成技术，实现高质量的多语言语音输出。
- **实时性能优化**：为了确保在不同网络条件下快速响应，TTS 系统需要进行实时性能优化。包括优化数据传输、减少延迟以及提高系统的响应速度，提供流畅的语音交互体验。
- **机器学习与数据训练**：机器学习是 TTS 技术的核心，通过大量的语音数据训练模型，可以提高语音合成的准确性和自然度。数据训练涉及数据收集、标注和模型优化等多个步骤，确保生成的语音质量。

### Spring AI + MARS5-TTS Audio Speech 功能扩展

> Spring AI 并未提供 `Ollama` 整合 `MARS5-TTS` 文本转语音模型实现将对话内容转语音的能力，为了实验这块的能力，这里扩展了 Spring AI 对 [MARS5-TTS](https://github.com/camb-ai/mars5-tts) 的支持，以实现后面的边对话边转语音的功能 。

- Mars5TtsAudioApi 实现了 `MARS5-TTS` 的 `API` 接口，用于将对话内容转为语音。

```java
public ResponseEntity<SpeechResponse> createSpeech(SpeechRequest speechRequest) {

        Assert.notNull(speechRequest, "The request body can not be null.");
        Assert.isTrue(speechRequest.stream() == 0, "Request must set the steam property to 0.");
        MultiValueMap body = ApiUtils.toMultiValueMap(speechRequest);
        return this.restClient.post()
                .uri("/tts")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .toEntity(SpeechResponse.class);
    }
```



### 参考项目：

- https://github.com/hkgood/Ollama_ChatTTS 
- https://github.com/maudoin/ollama-voice
- https://github.com/coqui-ai/tts
- https://github.com/lucataco/cog-xtts-v2
