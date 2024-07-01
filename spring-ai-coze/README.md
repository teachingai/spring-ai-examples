## spring-ai-coze

### 项目介绍

> 基于 Spring Boot 3.x 、Spring AI 和 [Coze](https://ollama.com/)、[Whisper](https://github.com/openai/whisper) 的 Text-To-Speech (TTS) 功能示例。

整合 Whisper 音频转录 与 本地 Ollama 服务器并输出 Speech-To-Text (STT) 语音识别

下面是离线模式下2种工具的简单组合：

- 大型语言模型：在离线模式下运行 [Ollama](https://ollama.com/) 本地模型
- 语音识别：在离线模式下运行 [Whisper](https://github.com/openai/whisper) 本地模型


### 先决条件

你需要在[Coze 平台](https://www.coze.cn/)上创建一个账号，并创建一个机器人，获取机器人的API Key。

#### 自动配置

Spring AI 为 Coze 聊天客户端提供 Spring Boot 自动配置。要启用它，请将以下依赖项添加到项目的 Maven `pom.xml` 文件中：

```xml
<dependency>
   <groupId>org.springframework.ai</groupId>
   <artifactId>spring-ai-coze-spring-boot-starter</artifactId>
</dependency>
```

或者，在你的 Gradle 构建文件 `build.gradle` 中添加：

```groovy
dependencies {
    implementation 'org.springframework.ai:spring-ai-coze-spring-boot-starter'
}
```

https://www.coze.cn/docs/developer_guides/chat


### Coze Chat 模型

> 扣子是新一代 AI 应用开发平台。无论你是否有编程基础，都可以在扣子上快速搭建基于大模型的各类 Bot，并将 Bot 发布到各个社交平台、通讯软件或部署到网站等其他渠道。

更多信息请参考官方文档：https://www.coze.cn/docs/guides/welcome




