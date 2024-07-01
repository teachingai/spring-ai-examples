package com.github.teachingai.ollama;

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
                .withDefaultOptions(OllamaOptions.create().withModel("mxbai-embed-large"));
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
