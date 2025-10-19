package com.github.teachingai.ollama.request;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

/**
 * UnifiedTTS 服务封装。
 *
 * <p>使用 Spring Boot 3.5 推荐的阻塞式客户端 {@link RestClient} 调用
 * UnifiedTTS 的同步合成接口（/api/v1/common/tts-sync）。
 *
 * <p>提供两种用法：
 * <ul>
 *   <li>{@link #synthesize(UnifiedTtsRequest)} 返回 JSON 响应（含 audio_url）</li>
 *   <li>{@link #synthesizeToFile(UnifiedTtsRequest, Path)} 根据 audio_url 下载并写入文件</li>
 * </ul>
 */
@Service
public class UnifiedTtsService {

    private final RestClient restClient;
    private final UnifiedTtsProperties properties;

    public UnifiedTtsService(UnifiedTtsProperties properties, RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl(properties.getHost())
                .build();
        this.properties = properties;
    }

    /**
     * 调用 UnifiedTTS 同步 TTS 接口，返回 JSON 响应。
     *
     * <p>请求头：
     * <ul>
     *   <li>Content-Type: application/json</li>
     *   <li>X-API-Key: 来自配置的 API Key</li>
     *   <li>Accept: application/json</li>
     * </ul>
     *
     * @param request 模型、音色、文本、速度/音调/音量、输出格式等参数
     * @return 响应对象，包含 {@code data.audio_url}
     * @throws IllegalStateException 当服务端返回非 2xx、无内容、或 {@code success=false} 时抛出
     */
    public UnifiedTtsResponse synthesize(UnifiedTtsRequest request) {
        ResponseEntity<UnifiedTtsResponse> response = restClient
                .post()
                .uri("/api/v1/common/tts-sync")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-API-Key", properties.getApiKey())
                .body(request)
                .retrieve()
                .toEntity(UnifiedTtsResponse.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        }
        throw new IllegalStateException("UnifiedTTS synthesize failed: " + response.getStatusCode());
    }

    /**
     * 调用合成并将音频写入指定文件。
     *
     * <p>若输出路径的父目录不存在，会自动创建；失败时抛出运行时异常。
     *
     * @param request TTS 请求参数
     * @param outputPath 目标文件路径（例如 output.mp3）
     * @return 实际写入的文件路径
     */
    public Path synthesizeToFile(UnifiedTtsRequest request, Path outputPath) {
        UnifiedTtsResponse resp = synthesize(request);
        if (resp.getData() == null || resp.getData().getAudioUrl() == null) {
            throw new IllegalStateException("UnifiedTTS response missing audio_url");
        }
        byte[] data = fetchAudio(resp.getData().getAudioUrl());
        try {
            if (outputPath.getParent() != null) {
                Files.createDirectories(outputPath.getParent());
            }
            Files.write(outputPath, data);
            return outputPath;
        } catch (IOException e) {
            throw new RuntimeException("Failed to write TTS output to file: " + outputPath, e);
        }
    }

    /**
     * 根据返回的 {@code audio_url} 下载音频字节。
     *
     * <p>作为可覆写的保护方法，方便单元测试替换真实下载行为。
     */
    protected byte[] fetchAudio(String audioUrl) {
        ResponseEntity<byte[]> response = restClient
                .get()
                .uri(audioUrl)
                .accept(MediaType.APPLICATION_OCTET_STREAM, MediaType.valueOf("audio/mpeg"), MediaType.valueOf("audio/mp3"))
                .retrieve()
                .toEntity(byte[].class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        }
        throw new IllegalStateException("Download audio failed: " + response.getStatusCode());
    }
}