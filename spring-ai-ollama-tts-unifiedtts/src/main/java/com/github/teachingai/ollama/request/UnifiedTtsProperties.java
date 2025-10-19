package com.github.teachingai.ollama.request;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * UnifiedTTS 配置属性。
 *
 * <p>通过 {@code unifiedtts.*} 前缀读取：
 * <ul>
 *   <li>{@code unifiedtts.host} 服务地址，默认 {@code https://unifiedtts.com}</li>
 *   <li>{@code unifiedtts.api-key} 访问密钥，必填</li>
 * </ul>
 */
@Data
@ConfigurationProperties(prefix = "unified-tts")
public class UnifiedTtsProperties {

    private String host;
    private String apiKey;

}