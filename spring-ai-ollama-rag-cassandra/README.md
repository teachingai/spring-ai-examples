## spring-ai-ollama-rag-cassandra

> 基于 [Spring Boot 3.x](https://docs.spring.io/spring-boot/index.html) 、[Spring AI](https://docs.spring.io/spring-ai/reference/index.html)、[Ollama](https://ollama.com/) 和 [Cassandra](https://cassandra.apache.org/) 的 检索增强生成（Retrieval-augmented Generation）功能示例。

### 先决条件

- 您首先需要在本地计算机上运行 Ollama。请参阅官方 [Ollama 项目自述文件](https://github.com/ollama/ollama "Ollama 项目自述文件")，开始在本地计算机上运行模型。
- 其次，您需要安装并运行 Cassandra 数据库。请参阅 [Cassandra 官方网站](https://cassandra.apache.org/ "Cassandra 官方网站")，了解如何安装和运行 Cassandra。

#### 添加存储库和 BOM

Spring AI 工件发布在 `Spring Milestone` 和 `Snapshot` 存储库中。请参阅存储库部分将这些存储库添加到您的构建系统中。

为了帮助进行依赖管理，Spring AI 提供了 BOM（物料清单），以确保在整个项目中使用一致的 Spring AI 版本。请参阅[依赖管理](https://docs.spring.io/spring-ai/reference/getting-started.html#dependency-management "依赖管理")部分将 Spring AI BOM 添加到您的构建系统。

#### 自动配置

##### Spring AI

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

###### Cassandra For Testcontainers

Testcontainers 为 Cassandra 提供了 Java 依赖。要启用它，请将以下依赖项添加到项目的 Maven `pom.xml` 文件中：

```xml
<dependency>
  <groupId>org.testcontainers</groupId>
  <artifactId>cassandra</artifactId>
  <scope>test</scope>
</dependency>
```

或者，在你的 Gradle 构建文件 `build.gradle` 中添加：

```groovy
dependencies {
    implementation 'org.springframework.ai:spring-ai-ollama-spring-boot-starter'
}
```

示例代码：

```java
var cassandra = new CassandraContainer<>(DockerImageName.parse("cassandra:3.11.2"));
cassandra.start();
```

### Ollama Embedding 模型

> 基于 Ollama 学习 Embedding ，需要支持 Embedding 的模型。 以下我们选择了几个专用的 Embedding 的模型。

**注意：当启动 Ollama 之后，Windows会有托盘图标，此时已经启动了 Ollama 的服务，访问 Embedding 时不需要运行 `shaw/dmeta-embedding-zh` ，只有访问 chat 时才需要启动一个大模型。**

#### shaw/dmeta-embedding-zh

> Dmeta-embedding 是一款跨领域、跨任务、开箱即用的中文 Embedding 模型，适用于搜索、问答、智能客服、LLM+RAG 等各种业务场景，支持使用 Transformers/Sentence-Transformers/Langchain 等工具加载推理。

- Huggingface：https://huggingface.co/DMetaSoul/Dmeta-embedding-zh
- 文档地址：https://ollama.com/shaw/dmeta-embedding-zh

优势特点如下：

- 多任务、场景泛化性能优异，目前已取得 MTEB 中文榜单第二成绩（2024.01.25）
- 模型参数大小仅 400MB，对比参数量超过 GB 级模型，可以极大降低推理成本
- 支持上下文窗口长度达到 1024，对于长文本检索、RAG 等场景更适配
  该模型有 4 个不通的版本：

- [dmeta-embedding-zh](https://ollama.com/shaw/dmeta-embedding-zh)：`shaw/dmeta-embedding-zh` 是一个参数量只有400M、适用于多种场景的中文Embedding模型，在MTEB基准上取得了优异成绩，尤其适合语义检索、RAG等LLM应用。
- [dmeta-embedding-zh-q4](https://ollama.com/shaw/dmeta-embedding-zh-q4)：`shaw/dmeta-embedding-zh` 的 Q4_K_M 量化版本
- [dmeta-embedding-zh-small](https://ollama.com/shaw/dmeta-embedding-zh-small)：`shaw/dmeta-embedding-zh-small` 是比 `shaw/dmeta-embedding-zh` 更轻量化的模型，参数不足300M，推理速度提升30%。
- [dmeta-embedding-zh-small-q4](https://ollama.com/shaw/dmeta-embedding-zh-small-q4)：`shaw/dmeta-embedding-zh-small` 的 Q4_K_M 量化版本

```shell 
ollama pull shaw/dmeta-embedding-zh
```

#### mxbai-embed-large

> 截至 2024 年 3 月，该模型在 MTEB 上创下了 Bert-large 尺寸模型的 SOTA 性能记录。它的表现优于 OpenAIs `text-embedding-3-large` 模型等商业模型，并且与其尺寸 20 倍的模型的性能相当。
`mxbai-embed-large`在没有 MTEB 数据重叠的情况下进行训练，这表明该模型在多个领域、任务和文本长度方面具有很好的泛化能力。

文档地址：https://ollama.com/library/mxbai-embed-large

```shell
ollama pull mxbai-embed-large
```

#### nomic-embed-text

> nomic-embed-text 是一个大上下文长度文本编码器，超越了 OpenAI `text-embedding-ada-002`，并且`text-embedding-3-small`在短上下文和长上下文任务上表现优异。

文档地址：https://ollama.com/library/nomic-embed-text

```shell
ollama pull nomic-embed-text
```

#### snowflake-arctic-embed

> snowflake-arctic-embed 是一套文本嵌入模型，专注于创建针对性能优化的高质量检索模型。

这些模型利用现有的开源文本表示模型（例如 bert-base-uncased）进行训练，并在多阶段管道中进行训练以优化其检索性能。

该模型有 5 种参数大小：

- snowflake-arctic-embed:335m（默认）
- snowflake-arctic-embed:137m
- snowflake-arctic-embed:110m
- snowflake-arctic-embed:33m
- snowflake-arctic-embed:22m

文档地址：https://ollama.com/library/snowflake-arctic-embed

```shell
ollama pull snowflake-arctic-embed
```

### Cassandra 向量数据库

> 本示例使用 Cassandra 向量数据库存储 Embedding 向量。




### RAG 示例




