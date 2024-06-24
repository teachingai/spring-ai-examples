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

> Qwen2 是阿里巴巴集团推出的全新系列大型语言模型。<br/>
Qwen2基于29 种语言的数据进行训练，包括英语和中文。
它有 4 种参数大小：0.5B、1.5B、7B、72B。
在 7B 和 72B 模型中，上下文长度已扩展至128k 个 token。

- 文档地址：https://ollama.com/library/qwen2

![](/models.png)

通过文档，我们可以可知 `qwen2:0.5b` 和 `qwen2:1.5b` 模型支持 Embedding 。

```shell
ollama run qwen2:1.5b
```


### Embedding 示例

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
