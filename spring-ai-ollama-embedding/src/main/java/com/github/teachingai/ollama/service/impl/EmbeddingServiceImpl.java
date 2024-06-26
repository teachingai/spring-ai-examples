package com.github.teachingai.ollama.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.github.teachingai.ollama.service.IEmbeddingService;
import jakarta.servlet.http.Part;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.ParagraphPdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Embedding Service Implementation
 */
@Service
public class EmbeddingServiceImpl implements IEmbeddingService {

    private final EmbeddingClient embeddingClient;

    @Autowired
    public EmbeddingServiceImpl(EmbeddingClient embeddingClient) {
        this.embeddingClient = embeddingClient;
    }

    @Override
    public Map embedding(String text) {
        return Map.of("embeddings", embeddingClient.embed(text));
    }

    @Override
    public List<Map> embedding(MultipartFile file) throws IOException {
        // 1、加载文件为 Resource
        InputStreamResource resource = new InputStreamResource(file.getInputStream());
        // 2、调用 embedding 方法
        return this.embedding(resource);
    }

    @Override
    public List<Map> embedding(Part file) throws IOException {
        // 1、加载文件为 Resource
        InputStreamResource resource = new InputStreamResource(file.getInputStream());
        // 2、调用 embedding 方法
        return this.embedding(resource);
    }

    protected List<Map> embedding(Resource resource) throws IOException {
        /**
         * 1、解析 llama2.pdf
         */
        ParagraphPdfDocumentReader pdfReader = new ParagraphPdfDocumentReader(resource,
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
        List<Map> mapList = new ArrayList<>();
        for (Document document : documents) {
            mapList.add(JSONObject.of( "id", document.getId(), "embedding", embeddingClient.embed(document),"content", document.getContent(), "metadata", document.getMetadata()));
        }
        return mapList;
    }

}
