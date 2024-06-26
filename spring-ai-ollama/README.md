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

#### Chat 属性

`spring.ai.ollama` 前缀的属性，可让您配置 Ollama Chat 客户端的连接。

| 属性  | 描述 | 默认值  |
| ------------ | ------------ | ------------ |
| `spring.ai.ollama.base-url` | Ollama API 服务器运行的基本 URL. | `http://localhost:11434`  |

`spring.ai.ollama.chat.options` 前缀的属性，可让您配置 Ollama Chat 客户端的连接。

这些`options` 属性基于 [Ollama 有效参数和值](https://github.com/jmorganca/ollama/blob/main/docs/modelfile.md#valid-parameters-and-values "Ollama 有效参数和值") 以及 [Ollama 类型](link:https://github.com/jmorganca/ollama/blob/main/api/types.go "Ollama 类型")。默认值基于：[Ollama type defaults](https://github.com/ollama/ollama/blob/b538dc3858014f94b099730a592751a5454cab0a/api/types.go#L364 "Ollama type defaults")。


| 属性  | 描述 | 默认值  |
| ------------ | ------------ | ------------ |
| `spring.ai.ollama.chat.enabled`      | 启用 Ollama 聊天客户端. | true  |
| `spring.ai.ollama.chat.options.model`  | [支持的模型名称](https://github.com/ollama/ollama?tab=readme-ov-file#model-library "支持的模型名称"). | mistral  |
| `spring.ai.ollama.chat.options.numa`              | 是否使用NUMA。                                          | false  |
| `spring.ai.ollama.chat.options.num-ctx`           | 设置用于生成下一个标记的上下文窗口的大小。 | 2048  |
| `spring.ai.ollama.chat.options.num-batch`         | ???                                                             | 512  |
| `spring.ai.ollama.chat.options.num-gqa`           | Transformer 层中 GQA 组的数量。某些型号需要，例如：llama2:70b 为 8。 | 1  |
| `spring.ai.ollama.chat.options.num-gpu`           | 要发送到 GPU 的层数。在 macOS 上，默认为 1 启用金属支持，0 禁用。这里的1表示 `NumGPU` 应该动态设置 | -1  |
| `spring.ai.ollama.chat.options.main-gpu`          | ???                                                             | -  |
| `spring.ai.ollama.chat.options.low-vram`          | ???                                                             | false  |
| `spring.ai.ollama.chat.options.f16-kv`            | ???                                                             | true  |
| `spring.ai.ollama.chat.options.logits-all`        | ???                                                             | -  |
| `spring.ai.ollama.chat.options.vocab-only`        | ???                                                             | -  |
| `spring.ai.ollama.chat.options.use-mmap`          | ???                                                             | true  |
| `spring.ai.ollama.chat.options.use-mlock`         | ???                                                             | false  |
| `spring.ai.ollama.chat.options.embedding-only`    | ???                                                             | false  |
| `spring.ai.ollama.chat.options.rope-frequency-base` | ???                                                           | 10000.0  |
| `spring.ai.ollama.chat.options.rope-frequency-scale` | ???                                                          | 1.0  |
| `spring.ai.ollama.chat.options.num-thread`        | 设置计算期间使用的线程数。默认情况下，Ollama 将检测到这一点以获得最佳性能。建议将此值设置为系统具有的物理 CPU 核心数（而不是逻辑核心数）。 0 = 让运行时决定 | 0  |
| `spring.ai.ollama.chat.options.num-keep`          | ???                                                             | 0  |
| `spring.ai.ollama.chat.options.seed`              | 设置用于生成的随机数种子。将其设置为特定数字将使模型为相同的提示生成相同的文本。  | -1  |
| `spring.ai.ollama.chat.options.num-predict`       | 生成文本时要预测的最大标记数。 （-1 = 无限生成，-2 = 填充上下文） | -1  |
| `spring.ai.ollama.chat.options.top-k`             | 减少产生废话的可能性。较高的值（例如，100）将给出更多样化的答案，而较低的值（例如，10）将更加保守。  | 40  |
| `spring.ai.ollama.chat.options.top-p`             | 与 top-k 一起工作。较高的值（例如，0.95）将导致更加多样化的文本，而较低的值（例如，0.5）将生成更加集中和保守的文本。  | 0.9  |
| `spring.ai.ollama.chat.options.tfs-z`             | Tail-free 用于减少输出中不太可能的标记的影响。较高的值（例如，2.0）将更多地减少影响，而值 1.0 将禁用此设置。 | 1.0  |
| `spring.ai.ollama.chat.options.typical-p`         | ???                                                             | 1.0  |
| `spring.ai.ollama.chat.options.repeat-last-n`     | 设置模型回溯多远以防止重复。 （默认值：64，0 = 禁用，-1 = num_ctx）   | 64  |
| `spring.ai.ollama.chat.options.temperature`       | 模型的温度。提高温度将使模型的答案更有创意。 | 0.8  |
| `spring.ai.ollama.chat.options.repeat-penalty`    | 设置惩罚重复的强度。较高的值（例如，1.5）将更强烈地惩罚重复，而较低的值（例如，0.9）将更宽松。 | 1.1  |
| `spring.ai.ollama.chat.options.presence-penalty`  | ???                                                             | 0.0  |
| `spring.ai.ollama.chat.options.frequency-penalty` | ???                                                             | 0.0  |
| `spring.ai.ollama.chat.options.mirostat`          | 启用 Mirostat 采样以控制困惑度。 （默认值：0、0 = 禁用、1 = Mirostat、2 = Mirostat 2.0） | 0  |
| `spring.ai.ollama.chat.options.mirostat-tau`      | 影响算法对生成文本反馈的响应速度。较低的学习率将导致调整速度较慢，而较高的学习率将使算法更具响应性。 | 5.0  |
| `spring.ai.ollama.chat.options.mirostat-eta`      | 控制输出的一致性和多样性之间的平衡。较低的值将导致文本更加集中和连贯。 | 0.1  |
| `spring.ai.ollama.chat.options.penalize-newline`  | ???                                                           | true  |
| `spring.ai.ollama.chat.options.stop`              | 设置要使用的停止序列。当遇到这种模式时，LLM 将停止生成文本并返回。可以通过在模型文件中指定多个单独的停止参数来设置多个停止模式。 | -  |

**提示**: 所有 `spring.ai.ollama.chat.options` 前缀的属性， 可以在运行期间通过添加特定请求参数到 `Prompt` 调用 实现覆盖.

### 聊天选项

[OllamaOptions.java](https://github.com/spring-projects/spring-ai/blob/main/models/spring-ai-ollama/src/main/java/org/springframework/ai/ollama/api/OllamaOptions.java "OllamaOptions.java") 提供模型配置，例如：要使用的模型、温度等。

启动时，可以使用 `OllamaChatClient(api, options)` 构造函数 或 `spring.ai.ollama.chat.options.*` 属性配置默认选项。

在运行时，您可以通过向调用添加新的、特定于请求的选项来覆盖默认选项Prompt。例如，要覆盖特定请求的默认型号和温度：

```java
ChatResponse response = chatClient.call(
    new Prompt(
        "Generate the names of 5 famous pirates.",
        OllamaOptions.create()
            .withModel("llama2")
            .withTemperature(0.4)
    ));
```

**提示**: 除了特定于模型的 [OllamaOptions](https://github.com/spring-projects/spring-ai/blob/main/models/spring-ai-ollama/src/main/java/org/springframework/ai/ollama/api/OllamaOptions.java "OllamaOptions") 之外，您还可以使用通过 [ChatOptionsBuilder#builder() ](https://github.com/spring-projects/spring-ai/blob/main/spring-ai-core/src/main/java/org/springframework/ai/chat/ChatOptionsBuilder.java "ChatOptionsBuilder#builder() ") 创建的可移植 [ChatOptions](https://github.com/spring-projects/spring-ai/blob/main/spring-ai-core/src/main/java/org/springframework/ai/chat/ChatOptions.java "ChatOptions")  实例。

### Sample Controller （自动配置）

[创建](https://start.spring.io/ "创建") 一个新的 Spring Boot 项目并将其添加 `spring-ai-ollama-spring-boot-starter` 到您的 pom（或 gradle）依赖项中。

在 `src/main/resources`目录下添加一个`application.properties`文件，以启用和配置 OpenAi Chat 客户端：

```properties
spring.ai.ollama.base-url=http://localhost:11434
spring.ai.ollama.chat.options.model=mistral
spring.ai.ollama.chat.options.temperature=0.7
```

**提示**: 将 `base-url` 替换为您的 Ollama 服务器 URL。

这将创建一个可以注入到您的类中的 `OllamaChatClient` 实现。下面是一个`@Controller`使用聊天客户端生成文本的简单类的示例。

```java
@RestController
public class ChatController {

    private final OllamaChatClient chatClient;

    @Autowired
    public ChatController(OllamaChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/ai/generate")
    public Map generate(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        return Map.of("generation", chatClient.call(message));
    }

    @GetMapping("/ai/generateStream")
	public Flux<ChatResponse> generateStream(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        Prompt prompt = new Prompt(new UserMessage(message));
        return chatClient.stream(prompt);
    }

}
```

#### 手动配置

如果你不想使用 Spring Boot 自动装配，你可以在你的应用里手动初始化 `OllamaChatClient`，[OllamaChatClient](https://github.com/spring-projects/spring-ai/blob/main/models/spring-ai-ollama/src/main/java/org/springframework/ai/ollama/OllamaChatClient.java "OllamaChatClient") 实现 `ChatClient` 和 `StreamingChatClient`， 并使用低级 OpenAi Api 客户端连接到 Ollama 服务。

添加 `spring-ai-ollama` 依赖到你的项目 Maven `pom.xml` 文件:

```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-ollama</artifactId>
</dependency>
```

或者，在你的 Gradle 构建文件 `build.gradle` 中添加：

```groovy
dependencies {
    implementation 'org.springframework.ai:spring-ai-ollama'
}
```

接下来，创建一个 `OllamaChatClient` 实例并将其用于文本生成请求：

```java
var ollamaApi = new OllamaApi();

var chatClient = new OllamaChatClient(ollamaApi).withModel(MODEL)
        .withDefaultOptions(OllamaOptions.create()
                .withModel(OllamaOptions.DEFAULT_MODEL)
                .withTemperature(0.9f));

ChatResponse response = chatClient.call(
    new Prompt("Generate the names of 5 famous pirates."));

// Or with streaming responses
Flux<ChatResponse> response = chatClient.stream(
    new Prompt("Generate the names of 5 famous pirates."));
```

- `OllamaOptions` 提供聊天请求的配置信息.

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