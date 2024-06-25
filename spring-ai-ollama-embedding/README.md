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


#### Qwen2

> Qwen2 是阿里巴巴集团推出的全新系列大型语言模型。<br/>
Qwen2基于29 种语言的数据进行训练，包括英语和中文。
它有 4 种参数大小：0.5B、1.5B、7B、72B。
在 7B 和 72B 模型中，上下文长度已扩展至128k 个 token。

![](/models.png)

通过文档，我们可以可知 `qwen2:0.5b` 和 `qwen2:1.5b` 模型支持 Embedding 。

文档地址：https://ollama.com/library/qwen2

#### gemma

> Gemma 是 Google 及其 DeepMind 团队开发的一种新开放模型。其灵感来自 Google 的 Gemini 模型。

Gemma 有两种2b尺寸7b：

- ollama run gemma:2b
- ollama run gemma:7b（默认）

文档地址：https://ollama.com/library/gemma

#### mxbai-embed-large

> 截至 2024 年 3 月，该模型在 MTEB 上创下了 Bert-large 尺寸模型的 SOTA 性能记录。它的表现优于 OpenAIs `text-embedding-3-large` 模型等商业模型，并且与其尺寸 20 倍的模型的性能相当。
`mxbai-embed-large`在没有 MTEB 数据重叠的情况下进行训练，这表明该模型在多个领域、任务和文本长度方面具有很好的泛化能力。

文档地址：https://ollama.com/library/mxbai-embed-large

#### nomic-embed-text

> nomic-embed-text 是一个大上下文长度文本编码器，超越了OpenAI `text-embedding-ada-002`，并且`text-embedding-3-small`在短上下文和长上下文任务上表现优异。

文档地址：https://ollama.com/library/nomic-embed-text

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
ollama run qwen2:1.5b
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


```java


```java
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.OllamaEmbeddingClient;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;

import java.util.List;
import java.util.Scanner;

public class OllamaEmbeddingTest {

    public static void main(String[] args) {

        var ollamaApi = new OllamaApi();
        //指定使用的模型
        var embeddingClient = new OllamaEmbeddingClient(ollamaApi)
                .withDefaultOptions(OllamaOptions.create().withModel("qwen2:1.5b"));

        // 测试数据
        VectorStore vectorStore = new SimpleVectorStore(embeddingClient);
        vectorStore.add(List.of(
                new Document("白日依山尽，黄河入海流。欲穷千里目，更上一层楼。"),
                new Document("青山依旧在，几度夕阳红。白发渔樵江渚上，惯看秋月春风。"),
                new Document("一片孤城万仞山，羌笛何须怨杨柳。春风不度玉门关。"),
                new Document("危楼高百尺，手可摘星辰。不敢高声语，恐惊天上人。")
        ));
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("请输入关键词: ");
            String query = scanner.nextLine();
            if (query.equals("exit")) {
                break;
            }
            System.out.print("Embedding 结果: " + embeddingClient.embed(query));
            List<Document> documents = vectorStore.similaritySearch(SearchRequest.query(query));
            System.out.println("查询结果: ");
            for (Document doc : documents) {
                System.out.println(doc.getEmbedding());
                System.out.println(doc.getContent());
            }
        }
    }

}
```
