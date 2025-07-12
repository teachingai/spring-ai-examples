## spring-ai-ollama-prompt

> 基于 [Spring Boot 3.x](https://docs.spring.io/spring-boot/index.html) 、[Spring AI](https://docs.spring.io/spring-ai/reference/index.html)、[Ollama](https://ollama.com/) 和 `Ollama Chat 模型` 的 Prompt Engineering 功能示例。


### 先决条件

您首先需要在本地计算机上运行 Ollama。请参阅官方 [Ollama 项目自述文件](https://github.com/ollama/ollama "Ollama 项目自述文件")，开始在本地计算机上运行模型。

然后，您需要在本地计算机上运行 Ollama Chat 模型。

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

#### Llama 3

> Meta Llama 3 是 Meta Inc. 开发的一系列模型，是最新推出的先进模型，具有8B和70B两种参数大小（预训练或指令调整）。<br/>
Llama 3 指令调整模型针对对话/聊天用例进行了微调和优化，并且在常见基准测试中胜过许多可用的开源聊天模型。

- 文档地址：https://ollama.com/library/llama3

##### 模型变体

Instruct 是针对`聊天/对话场景`进行了fine-tuned。

```shell
ollama run llama3
ollama run llama3:70b
```

Pre-trained 的是基础模型。

```shell
ollama run llama3:text
ollama run llama3:70b-text
```

**注意**: 安装 `ollama run llama3` 将下载一个 4.7GB 的 docker 镜像。

#### Google Gemma 2

> Google 的 Gemma 2 型号有两种尺寸：9B 和 27B，采用全新的架构设计，可实现一流的性能和效率。

- 文档地址：https://ollama.com/library/gemma2

##### 一流的性能

Gemma 2 拥有 270 亿个参数，其性能在基准测试中超越了比其规模大两倍的模型。这一突破性的效率为开放模型领域树立了新标准。

##### 两种尺寸：9B 和 27B 参数

- 9B 参数：`ollama run gemma2`
- 27B 参数：`ollama run gemma2:27b`

#### DeepSeek-V2

> DeepSeek-V2 是一种强大的混合专家 (MoE) 语言模型，具有经济的训练和高效的推理特点。

- 文档地址：https://ollama.com/library/deepseek-v2

**注**：此模型为中英文双语。

##### 该模型有两种尺寸：

- 16B Lite：`ollama run deepseek-v2:16b`
- 236B：`ollama run deepseek-v2:236b`

#### GLM-4

> GLM-4-9B 是智谱 AI 推出的最新一代预训练模型 GLM-4 系列中的开源版本。在语义、数学、推理、代码和知识等多方面的数据集测评中，GLM-4-9B 及其人类偏好对齐的版本 GLM-4-9B-Chat 均表现出超越 Llama-3-8B 的卓越性能。除了能进行多轮对话，GLM-4-9B-Chat 还具备网页浏览、代码执行、自定义工具调用（Function Call）和长文本推理（支持最大 128K 上下文）等高级功能。本代模型增加了多语言支持，支持包括日语，韩语，德语在内的 26 种语言。我们还推出了支持 1M 上下文长度（约 200 万中文字符）的 GLM-4-9B-Chat-1M 模型和基于 GLM-4-9B 的多模态模型 GLM-4V-9B。GLM-4V-9B 具备 1120 * 1120 高分辨率下的中英双语多轮对话能力，在中英文综合能力、感知推理、文字识别、图表理解等多方面多模态评测中，GLM-4V-9B 表现出超越 GPT-4-turbo-2024-04-09、Gemini1.0 Pro、Qwen-VL-Max 和 Claude 3 Opus 的卓越性能。

- 文档地址：https://ollama.com/library/glm4

```shell
ollama run glm4
```

#### Google Gemma

> Gemma 是 Google 及其 DeepMind 团队开发的一种新开放模型。其灵感来自 Google 的 Gemini 模型。

Gemma 有两种2b尺寸7b：

- `ollama run gemma:2b`
- `ollama run gemma:7b`（默认）

文档地址：https://ollama.com/library/gemma

```shell
ollama run gemma:2b
```