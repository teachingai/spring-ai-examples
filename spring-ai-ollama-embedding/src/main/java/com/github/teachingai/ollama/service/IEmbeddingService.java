package com.github.teachingai.ollama.service;

import jakarta.servlet.http.Part;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IEmbeddingService {

    public Map embedding(String text);

    public List<Map> embedding(MultipartFile file);

    public List<Map> embedding(Part file) throws IOException;

}
