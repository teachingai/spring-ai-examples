package com.github.teachingai.ollama;

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
