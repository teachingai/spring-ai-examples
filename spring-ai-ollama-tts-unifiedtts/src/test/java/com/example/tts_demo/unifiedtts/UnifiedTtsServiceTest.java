package com.example.tts_demo.unifiedtts;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.github.teachingai.ollama.request.UnifiedTtsRequest;
import com.github.teachingai.ollama.request.UnifiedTtsResponse;
import com.github.teachingai.ollama.request.UnifiedTtsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UnifiedTtsServiceTest {

    @Autowired
    private UnifiedTtsService unifiedTtsService;

    @Test
    void testRealSynthesizeAndDownloadToFile() throws Exception {
        UnifiedTtsRequest req = new UnifiedTtsRequest(
            "edge-tts",
            "en-US-JennyNeural",
            "Hello, this is a test of text to speech synthesis.",
            1.0,
            1.0,
            1.0,
            "mp3"
        );

        // 调用真实接口，断言返回结构
        UnifiedTtsResponse resp = unifiedTtsService.synthesize(req);
        assertNotNull(resp);
        assertTrue(resp.isSuccess(), "Response should be success");
        assertNotNull(resp.getData(), "Response data should not be null");
        assertNotNull(resp.getData().getAudioUrl(), "audio_url should be present");

        // 在当前工程目录下生成测试结果目录并写入文件
        Path projectDir = Paths.get(System.getProperty("user.dir"));
        Path resultDir = projectDir.resolve("test-result");
        Files.createDirectories(resultDir);
        Path out = resultDir.resolve(System.currentTimeMillis() + ".mp3");
        Path written = unifiedTtsService.synthesizeToFile(req, out);
        System.out.println("UnifiedTTS test output: " + written.toAbsolutePath());
        assertTrue(Files.exists(written), "Output file should exist");
        assertTrue(Files.size(written) > 0, "Output file size should be > 0");
    }
}