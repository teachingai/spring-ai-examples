package com.github.teachingai.ollama.service.impl;

import com.github.teachingai.ollama.service.IEmbeddingService;
import jakarta.servlet.http.Part;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public List<Map> embedding(MultipartFile file) {
        ExtractedTextFormatter formatter = ExtractedTextFormatter.defaults();
        TikaDocumentReader documentReader = new TikaDocumentReader(file.getResource(), formatter);
        List<Document> documents = documentReader.get();
        List<Map> mapList = new ArrayList<>();
        for (Document document : documents) {
            mapList.add(Map.of( "id", document.getId(),"embeddings", embeddingClient.embed(document), "content", document.getContent(), "metadata", document.getMetadata()));
        }
        return mapList;
    }

    @Override
    public List<Map> embedding(Part file) throws IOException {
        ExtractedTextFormatter formatter = ExtractedTextFormatter.defaults();
        TikaDocumentReader documentReader = new TikaDocumentReader(new InputStreamResource(file.getInputStream()), formatter);
        List<Document> documents = documentReader.get();
        List<Map> mapList = new ArrayList<>();
        for (Document document : documents) {
            mapList.add(Map.of( "id", document.getId(),"embeddings", embeddingClient.embed(document), "content", document.getContent(), "metadata", document.getMetadata()));
        }
        return mapList;
    }

}
