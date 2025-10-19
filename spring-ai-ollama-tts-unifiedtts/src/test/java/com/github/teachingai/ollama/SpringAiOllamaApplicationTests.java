package com.github.teachingai.ollama;

import com.github.teachingai.ollama.UnifiedTtsAudioSpeechModel;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringAiOllamaApplicationTests {

    UnifiedTtsAudioSpeechModel unifiedTtsAudioSpeechModel;
	@Test
	void contextLoads() {

        unifiedTtsAudioSpeechModel.call("hello world");

	}

}
