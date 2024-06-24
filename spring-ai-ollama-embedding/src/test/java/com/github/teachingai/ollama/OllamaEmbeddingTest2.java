package com.github.teachingai.ollama;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.OllamaEmbeddingClient;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.ParagraphPdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
