## spring-ai-ollama-embedding

### 项目介绍

> 基于 Spring Boot 3.x 、Spring AI 和 [Ollama](https://ollama.com/) 的 Embedding 功能示例。

### 知识点

- Embedding 模型
- Ollama 本地模型

### 项目结构

```
spring-ai-ollama-embedding
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

### Ollama Embedding 模型

> 基于 Ollama 学习 Embedding ，需要支持 Embedding 的模型。 以下我们选择了几个支持 Embedding 的模型。

**注意：当启动 Ollama 之后，Windows会有托盘图标，此时已经启动了 Ollama 的服务，访问 Embedding 时不需要运行 `ollama run gemma` ，只有访问 chat 时才需要启动一个大模型。**

#### Qwen2

> Qwen2 是阿里巴巴集团推出的全新系列大型语言模型。<br/>
Qwen2基于29 种语言的数据进行训练，包括英语和中文。

它有 4 种参数大小：0.5B、1.5B、7B、72B。
在 7B 和 72B 模型中，上下文长度已扩展至128k 个 token。

| Models       | Qwen2-0.5B | Qwen2-1.5B | Qwen2-7B | Qwen2-72B |
|--------------|------------|------------|----------|-----------|
| Params       | 0.49B      | 1.54B      | 7.07B    | 72.71B    |
| Non-Emb Params | 0.35B    | 1.31B      | 5.98B    | 70.21B    |
| GQA          | True       | True       | True     | True      |
| Tie Embedding | True      | True       | False    | False     |
| Context Length | 32K      | 32K        | 128K     | 128K      |

文档地址：https://ollama.com/library/qwen2

#### gemma

> Gemma 是 Google 及其 DeepMind 团队开发的一种新开放模型。其灵感来自 Google 的 Gemini 模型。

Gemma 有两种2b尺寸7b：

- ollama run gemma:2b
- ollama run gemma:7b（默认）

文档地址：https://ollama.com/library/gemma

```shell
ollama pull gemma:7b
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

### Embedding 示例 1：PDF 解析

#### 1.1、基于 spring-ai-pdf-document-reader 的 PDF文档解析

```java
import com.alibaba.fastjson2.JSONObject;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.ParagraphPdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;

import java.util.List;

/**
* 该示例用于学习文档解析
  */
  public class OllamaEmbeddingTest1 {

  /**
    * 下面代码依赖 spring-ai-pdf-document-reader 和 pdfbox（3.0.2）
      */
      public static void main(String[] args) {

      /**
        * 1、解析 llama2.pdf
          */
          ParagraphPdfDocumentReader pdfReader = new ParagraphPdfDocumentReader("classpath:/llama2.pdf",
          PdfDocumentReaderConfig.builder()
          .withPageTopMargin(0)
          .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
          .withNumberOfTopTextLinesToDelete(0)
          .build())
          .withPagesPerDocument(1)
          .build());
          /**
        * 2、读取并处理PDF文档以提取段落。
          */
          List<Document> documents = pdfReader.get();
          for (Document document : documents) {
          System.out.println( JSONObject.of( "id", document.getId(),"content", document.getContent(), "metadata", document.getMetadata()));
          }
          }

}
```

#### 1.2、基于 spring-ai-tika-document-reader 的 PDF文档解析

```java
import com.alibaba.fastjson2.JSONObject;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.List;

/**
 * 该示例用于学习文档解析
 */
public class OllamaEmbeddingTest2 {

    /**
     * 下面代码依赖 spring-ai-tika-document-reader 和 pdfbox（2.0.31）
     */
    public static void main(String[] args) {

        /**
         * 1、解析 llama2.pdf
         */
        Resource resource = new ClassPathResource("llama2.pdf");
        ExtractedTextFormatter formatter = ExtractedTextFormatter.builder()
                .withLeftAlignment(Boolean.TRUE)
                .withNumberOfBottomTextLinesToDelete(0)
                .withNumberOfTopTextLinesToDelete(0)
                .withNumberOfTopPagesToSkipBeforeDelete(0)
                .build();
        TikaDocumentReader documentReader = new TikaDocumentReader(resource, formatter);
        /**
         * 2、读取并处理PDF文档以提取段落。
         */
        List<Document> documents = documentReader.get();
        for (Document document : documents) {
            String content = document.getContent();
            // 尝试使用正则表达式将内容分割成段落
            String[] paragraphs = content.split("\\n\\s*\\n");
            for (String paragraph : paragraphs) {
                System.out.println( JSONObject.of( "id", document.getId(),"content", paragraph, "metadata", document.getMetadata()));
            }
        }
    }

}
```

### Embedding 示例 2：PDF 解析和嵌入

```java
import com.alibaba.fastjson2.JSONObject;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.OllamaEmbeddingClient;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.ParagraphPdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;

import java.util.List;

/**
 * 该示例用于学习文档解析和嵌入
 */
public class OllamaEmbeddingTest3 {

    /**
     * 下面代码依赖 spring-ai-pdf-document-reader 和 pdfbox（3.0.2）
     */
    public static void main(String[] args) {

        /**
         * gemma:7b ：https://ollama.com/library/gemma
         * mxbai-embed-large ：https://ollama.com/library/mxbai-embed-large
         * nomic-embed-text ：https://ollama.com/library/nomic-embed-text
         * snowflake-arctic-embed ：https://ollama.com/library/snowflake-arctic-embed
         */
        var ollamaApi = new OllamaApi();
        var embeddingClient = new OllamaEmbeddingClient(ollamaApi)
                .withDefaultOptions(OllamaOptions.create().withModel("nomic-embed-text"));

        /**
         * 1、解析 llama2.pdf
         */
        ParagraphPdfDocumentReader pdfReader = new ParagraphPdfDocumentReader("classpath:/llama2.pdf",
                PdfDocumentReaderConfig.builder()
                        .withPageTopMargin(0)
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
                                .withNumberOfTopTextLinesToDelete(0)
                                .build())
                        .withPagesPerDocument(1)
                        .build());
        /**
         * 2、读取并处理PDF文档以提取段落。
         */
        List<Document> documents = pdfReader.get();
        for (Document document : documents) {
            System.out.println( JSONObject.of( "id", document.getId(), "embedding", embeddingClient.embed(document),"content", document.getContent(), "metadata", document.getMetadata()));
        }
    }

}
```

### Embedding 示例 3：PDF 解析、嵌入、简单的相似度搜索

#### 1、基于 `SimpleVectorStore` 的简单文本嵌入和相似度搜索

```java
import com.alibaba.fastjson2.JSONObject;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.OllamaEmbeddingClient;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;

import java.util.List;
import java.util.Scanner;

/**
 * 该示例用于学习 文本嵌入、简单的相似度搜索
 */
public class OllamaEmbeddingTest4 {

    /**
     * 下面代码依赖 spring-ai-pdf-document-reader 和 pdfbox（3.0.2）
     */
    public static void main(String[] args) {

        /**
         * gemma:7b ：https://ollama.com/library/gemma
         * mxbai-embed-large ：https://ollama.com/library/mxbai-embed-large
         * nomic-embed-text ：https://ollama.com/library/nomic-embed-text
         * snowflake-arctic-embed ：https://ollama.com/library/snowflake-arctic-embed
         */
        var ollamaApi = new OllamaApi();
        var embeddingClient = new OllamaEmbeddingClient(ollamaApi)
                .withDefaultOptions(OllamaOptions.create().withModel("nomic-embed-text"));
        /**
         * 1、简单的文本嵌入
         */
        VectorStore vectorStore = new SimpleVectorStore(embeddingClient);
        // 将嵌入存储在 VectorStore
        vectorStore.add(List.of(
                new Document("床前明月光，疑是地上霜。举头望明月，低头思故乡。"),
                new Document("李白乘舟将欲行，忽闻岸上踏歌声。桃花潭水深千尺，不及汪伦送我情。"),
                new Document("日照香炉生紫烟，遥看瀑布挂前川。飞流直下三千尺，疑是银河落九天。"),
                new Document("独在异乡为异客，每逢佳节倍思亲。遥知兄弟登高处，遍插茱萸少一人。")
        ));
        /**
         * 2、简单的相似度搜索
         */
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("请输入关键词: ");
            String query = scanner.nextLine();
            if (query.equals("exit")) {
                break;
            }
            System.out.print("Embedding Query: " + embeddingClient.embed(query));
            // Retrieve embeddings
            SearchRequest request = SearchRequest.query(query).withTopK(1).withSimilarityThreshold(0.6);
            List<Document> similarDocuments  = vectorStore.similaritySearch(request);
            System.out.println("查询结果: ");
            for (Document document : similarDocuments ) {
                System.out.println( JSONObject.of( "id", document.getId(), "content", document.getContent(), "embedding", document.getEmbedding(),"metadata", document.getMetadata()));
            }
        }
    }

}

```

#### 2、基于 `SimpleVectorStore` 的 PDF 解析、入和相似度搜索

```java
import com.alibaba.fastjson2.JSONObject;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.OllamaEmbeddingClient;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;

import java.util.List;
import java.util.Scanner;

/**
 * 该示例用于学习 文本嵌入、简单的相似度搜索
 */
public class OllamaEmbeddingTest4 {

    /**
     * 下面代码依赖 spring-ai-pdf-document-reader 和 pdfbox（3.0.2）
     */
    public static void main(String[] args) {

        /**
         * qwen2:7b ：https://ollama.com/library/qwen2
         * gemma:7b ：https://ollama.com/library/gemma
         * mxbai-embed-large ：https://ollama.com/library/mxbai-embed-large
         * nomic-embed-text ：https://ollama.com/library/nomic-embed-text
         * snowflake-arctic-embed ：https://ollama.com/library/snowflake-arctic-embed
         */
        var ollamaApi = new OllamaApi();
        var embeddingClient = new OllamaEmbeddingClient(ollamaApi)
                .withDefaultOptions(OllamaOptions.create().withModel("gemma:7b"));
        /**
         * 1、简单的文本嵌入
         */
        VectorStore vectorStore = new SimpleVectorStore(embeddingClient);
        // 将嵌入存储在 VectorStore
        vectorStore.add(List.of(
                new Document("床前明月光，疑是地上霜。举头望明月，低头思故乡。"),
                new Document("李白乘舟将欲行，忽闻岸上踏歌声。桃花潭水深千尺，不及汪伦送我情。"),
                new Document("日照香炉生紫烟，遥看瀑布挂前川。飞流直下三千尺，疑是银河落九天。"),
                new Document("独在异乡为异客，每逢佳节倍思亲。遥知兄弟登高处，遍插茱萸少一人。")
        ));
        /**
         * 2、简单的相似度搜索
         */
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("请输入关键词: ");
            String query = scanner.nextLine();
            if (query.equals("exit")) {
                break;
            }
            System.out.print("Embedding Query: " + embeddingClient.embed(query));
            // Retrieve embeddings
            SearchRequest request = SearchRequest.query(query).withTopK(1).withSimilarityThreshold(0.6);
            List<Document> similarDocuments  = vectorStore.similaritySearch(request);
            System.out.println("查询结果: ");
            for (Document document : similarDocuments ) {
                System.out.println( JSONObject.of( "id", document.getId(), "content", document.getContent(), "embedding", document.getEmbedding(),"metadata", document.getMetadata()));
            }
        }
    }

}
```

输入关键词：how many parameters does llama 2 have?

