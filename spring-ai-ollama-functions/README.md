## spring-ai-ollama-functions

### 项目介绍

> 基于 Spring Boot 3.x 、Spring AI 和 [Ollama](https://ollama.com/) 的 Function calling 功能示例。

### 知识点

- Function calling 原理
- Ollama 支持 Function calling 模型

### 项目结构

```
spring-ai-ollama-functions
├── src/main/java/com/github/teachingai/ollama
│   ├── controller
│   │   └── ChatController.java
│   ├── model
│   │   └── ChatRequest.java
│   └── service
│       └── ChatService.java
├── src/main/resources
│   ├── application.properties
│   └── static
│       └── index.html
├── .gitignore
├── pom.xml
└── README.md
```

### Ollama Function calling 模型

#### 1、Mistral

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

##### Function calling

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

#### 2、Mixtral

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