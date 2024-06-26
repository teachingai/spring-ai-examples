# spring-ai-ollama

> 通过 Ollama，我们可以在本地运行各种大型语言模型 (LLM) 并从中生成文本。 Spring AI 通过 `OllamaChatClient` 支持 文本生成。

### 先决条件

您首先需要在本地计算机上运行 Ollama。请参阅官方 [Ollama 项目自述文件](https://github.com/ollama/ollama "Ollama 项目自述文件")，开始在本地计算机上运行模型。

**注意**: 安装 `ollama run llama2` 将下载一个 4GB 的 docker 镜像。

#### 添加存储库和 BOM

Spring AI 工件发布在 `Spring Milestone` 和 `Snapshot` 存储库中。请参阅存储库部分将这些存储库添加到您的构建系统中。

为了帮助进行依赖管理，Spring AI 提供了 BOM（物料清单），以确保在整个项目中使用一致的 Spring AI 版本。请参阅[依赖管理](https://docs.spring.io/spring-ai/reference/getting-started.html#dependency-management "依赖管理")部分将 Spring AI BOM 添加到您的构建系统。

#### 自动配置

Spring AI 为 Ollama 聊天客户端提供 Spring Boot 自动配置。要启用它，请将以下依赖项添加到项目的 Maven `pom.xml` 文件中：

```xml
<dependency>
   <groupId>io.springboot.ai</groupId>
   <artifactId>spring-ai-ollama-spring-boot-starter</artifactId>
</dependency>
```

或者，在你的 Gradle 构建文件 `build.gradle` 中添加：

```groovy
dependencies {
    implementation 'io.springboot.ai:spring-ai-ollama-spring-boot-starter'
}
```

### Low-level OllamaApi Client

[OllamaApi](https://github.com/spring-projects/spring-ai/blob/main/models/spring-ai-ollama/src/main/java/org/springframework/ai/ollama/api/OllamaApi.java "OllamaApi") 为 [Ollama Chat Completion API](https://github.com/ollama/ollama/blob/main/docs/api.md#generate-a-chat-completion "Ollama Chat Completion API") 提供了轻量级 Java 客户端。

以下类图说明了 `OllamaApi` 聊天接口和构建块：

![](/ollama_chat_api.png)

以下是如何以编程方式使用 api 的简单片段：

```java
OllamaApi ollamaApi = new OllamaApi("YOUR_HOST:YOUR_PORT");

// Sync request
var request = ChatRequest.builder("orca-mini")
    .withStream(false) // not streaming
    .withMessages(List.of(
            Message.builder(Role.SYSTEM)
                .withContent("You are a geography teacher. You are talking to a student.")
                .build(),
            Message.builder(Role.USER)
                .withContent("What is the capital of Bulgaria and what is the size? "
                        + "What is the national anthem?")
                .build()))
    .withOptions(OllamaOptions.create().withTemperature(0.9f))
    .build();

ChatResponse response = ollamaApi.chat(request);

// Streaming request
var request2 = ChatRequest.builder("orca-mini")
    .withStream(true) // streaming
    .withMessages(List.of(Message.builder(Role.USER)
        .withContent("What is the capital of Bulgaria and what is the size? " + "What is the national anthem?")
        .build()))
    .withOptions(OllamaOptions.create().withTemperature(0.9f).toMap())
    .build();

Flux<ChatResponse> streamingResponse = ollamaApi.streamingChat(request2);
```

### Ollama Chat Model

> Qwen2 是阿里巴巴集团推出的全新系列大型语言模型。<br/>
Qwen2基于29 种语言的数据进行训练，包括英语和中文。
它有 4 种参数大小：0.5B、1.5B、7B、72B。
在 7B 和 72B 模型中，上下文长度已扩展至128k 个 token。

- 文档地址：https://ollama.com/library/qwen2

![](/models.png)

通过文档，我们可以可知 `qwen2:0.5b` 和 `qwen2:1.5b` 模型支持 Embedding 。