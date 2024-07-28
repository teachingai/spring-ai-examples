## spring-ai-ollama-function-calling

### 项目介绍

> 基于 [Spring Boot 3.x](https://docs.spring.io/spring-boot/index.html) 、[Spring AI](https://docs.spring.io/spring-ai/reference/index.html) 和 [Ollama](https://ollama.com/) 的 Function calling 功能示例。

### 先决条件

您首先需要在本地计算机上运行 Ollama。请参阅官方 [Ollama 项目自述文件](https://github.com/ollama/ollama "Ollama 项目自述文件")，开始在本地计算机上运行模型。

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

### Function Calling 知识点

- Ollama 自定义模型
- Function Calling 原理
- ChatGLM-4-9B 模型
- LoRA、QLoRA 微调
  https://blog.csdn.net/spiderwower/article/details/138755776

### Function Calling 原理

#### 什么是函数调用？

函数调用是 OpenAI 的 GPT-4-0613 和 GPT-3.5 Turbo-0613 模型中的一项新功能。这些 AI 模型经过训练，可以根据用户的提示检测函数调用的需求，并以结构化的调用请求（而不是常规文本）进行响应。

函数调用允许大语言模型与其他系统交互，从而使 LLMs 能够回答它们原本无法回答的问题，例如需要实时信息或训练集中未包含的数据的问题。换句话说，函数调用提供了另一种方法来教 AI 模型如何与外部世界交互。

##### Spring AI 函数调用流程

![](/function_calling.png)
 
Spring AI 目前支持以下 AI 模型的函数调用

- OpenAI：请参阅 [Open AI函数调用文档](https://docs.spring.io/spring-ai/reference/api/chat/functions/openai-chat-functions.html)。
- VertexAI Gemini：请参阅 [Vertex AI Gemini 函数调用文档](https://docs.spring.io/spring-ai/reference/api/chat/functions/vertexai-gemini-chat-functions.html)。
- Azure OpenAI：请参阅 [Azure OpenAI 函数调用文档](https://docs.spring.io/spring-ai/reference/api/chat/functions/azure-open-ai-chat-functions.html)。
- Mistral AI：请参阅 [Mistral AI 功能调用文档](https://docs.spring.io/spring-ai/reference/api/chat/functions/mistralai-chat-functions.html)。
- Anthropic Claude：请参阅 [Anthropic Claude 函数调用文档](https://docs.spring.io/spring-ai/reference/api/chat/functions/anthropic-chat-functions.html)。
- MiniMax：请参阅 [MiniMax函数调用文档](https://docs.spring.io/spring-ai/reference/api/chat/functions/minimax-chat-functions.html)。
- ZhiPu AI：请参阅 [ZhiPu AI功能调用文档](https://docs.spring.io/spring-ai/reference/api/chat/functions/zhipuai-chat-functions.html)。

### Function Calling 模型

Ollama 已有的 Chat 模型中，部分模型对`Function Calling` 的支持较好，以下是我挑选几个用来测试的模型：

#### Qwen2

> Qwen2 是阿里巴巴集团推出的全新系列大型语言模型。
Qwen2基于29 种语言的数据进行训练，包括英语和中文。

- 文档地址：https://ollama.com/library/qwen2

它有 4 种参数大小：0.5B、1.5B、7B、72B。
在 7B 和 72B 模型中，上下文长度已扩展至128k 个 token。

| Models       | Qwen2-0.5B | Qwen2-1.5B | Qwen2-7B | Qwen2-72B |
|--------------|------------|------------|----------|-----------|
| Params       | 0.49B      | 1.54B      | 7.07B    | 72.71B    |
| Non-Emb Params | 0.35B    | 1.31B      | 5.98B    | 70.21B    |
| GQA          | True       | True       | True     | True      |
| Tie Embedding | True      | True       | False    | False     |
| Context Length | 32K      | 32K        | 128K     | 128K      |

**注意**: 安装 `ollama run qwen2` 将下载一个 4.4GB 的 docker 镜像。

#### GLM-4

> GLM-4-9B 是智谱 AI 推出的最新一代预训练模型 GLM-4 系列中的开源版本。在语义、数学、推理、代码和知识等多方面的数据集测评中，GLM-4-9B 及其人类偏好对齐的版本 GLM-4-9B-Chat 均表现出超越 Llama-3-8B 的卓越性能。除了能进行多轮对话，GLM-4-9B-Chat 还具备网页浏览、代码执行、自定义工具调用（Function Call）和长文本推理（支持最大 128K 上下文）等高级功能。本代模型增加了多语言支持，支持包括日语，韩语，德语在内的 26 种语言。我们还推出了支持 1M 上下文长度（约 200 万中文字符）的 GLM-4-9B-Chat-1M 模型和基于 GLM-4-9B 的多模态模型 GLM-4V-9B。GLM-4V-9B 具备 1120 * 1120 高分辨率下的中英双语多轮对话能力，在中英文综合能力、感知推理、文字识别、图表理解等多方面多模态评测中，GLM-4V-9B 表现出超越 GPT-4-turbo-2024-04-09、Gemini1.0 Pro、Qwen-VL-Max 和 Claude 3 Opus 的卓越性能。

- 文档地址：https://ollama.com/library/glm4

```shell
ollama run glm4
```

#### Mistral

> Mistral 是一个 7B 参数模型，使用 Apache 许可证发布。它可用于指令（指令跟踪）和文本完成。

Mistral AI 团队指出，Mistral 7B：

- 在所有基准测试中均优于 Llama 2 13B
- 在许多基准测试中均优于 Llama 1 34B
- 代码性能接近 CodeLlama 7B，同时仍能很好地完成英语任务

##### 版本

| 标签             | 日期             | 备注                     |
|-----------------|-----------------|------------------------|
| `v0.3` `latest` | 2024 年 5 月 22 日 | Mistral 7B 的新版本支持函数调用。 |
| `v0.2`          | 2024 年 3 月 23 日 | Mistral 7B 的小版本        |
| `v0.1`          | 2023 年 9 月 27 日 | 初始发行                   |

##### Function Calling

> Mistral 0.3 支持使用 Ollama 的原始模式进行函数调用。

- Example raw prompt
```
[AVAILABLE_TOOLS] [{"type": "function", "function": {"name": "get_current_weather", "description": "Get the current weather", "parameters": {"type": "object", "properties": {"location": {"type": "string", "description": "The city and state, e.g. San Francisco, CA"}, "format": {"type": "string", "enum": ["celsius", "fahrenheit"], "description": "The temperature unit to use. Infer this from the users location."}}, "required": ["location", "format"]}}}][/AVAILABLE_TOOLS][INST] What is the weather like today in San Francisco [/INST]
```
- Example response
```
[TOOL_CALLS] [{"name": "get_current_weather", "arguments": {"location": "San Francisco, CA", "format": "celsius"}}]
```

文档地址：https://ollama.com/library/mistral

```shell
ollama run mistral
```

#### Mixtral

> Mixtral 大型语言模型 (LLM) 是一组预先训练的生成稀疏混合专家。

该模型有 2 种参数大小：

- mixtral:8x22b
- mixtral:8x7b

混合式 8x22b

```shell
ollama run mixtral:8x22b
```

> Mixtral 8x22B 为 AI 社区树立了新的性能和效率标准。它是一种稀疏混合专家 (SMoE) 模型，仅使用 141B 中的 39B 个活动参数，就其规模而言，具有无与伦比的成本效益。

Mixtral 8x22B 具有以下优势：

- 精通英语、法语、意大利语、德语和西班牙语
- 它具有强大的数学和编码能力
- 它本身具有`函数调用`功能
- 64K 标记上下文窗口允许从大型文档中精确地调用信息
