## spring-ai-ollama-voice-edgetts

> 基于 [Spring Boot 3.x](https://docs.spring.io/spring-boot/index.html) 、[Spring AI](https://docs.spring.io/spring-ai/reference/index.html)、[Ollama](https://ollama.com/) 和 [Edge-TTS](https://github.com/rany2/edge-tts) 的 Text-To-Speech (TTS) 功能示例。

整合 Edge-TTS 与 本地 Ollama 服务器并输出 Text-To-Speech (TTS) 音频响应

下面是离线模式下2种工具的简单组合：

- 大型语言模型：在离线模式下运行 [Ollama](https://ollama.com/) 本地模型
- 文本转语音：在离线模式下运行 [Edge-TTS](https://github.com/rany2/edge-tts) 本地模型

### 先决条件

您首先需要在本地计算机上运行 Ollama。请参阅官方 [Ollama 项目自述文件](https://github.com/ollama/ollama "Ollama 项目自述文件")，开始在本地计算机上运行模型。

Edge-TTS 不提供API功能，需要安装 Python 环境 和 Edge-TTS 模块，请参阅官方 [Edge-TTS](https://github.com/rany2/edge-tts) 项目进行调用。

#### 添加存储库和 BOM

Spring AI 工件发布在 `Spring Milestone` 和 `Snapshot` 存储库中。请参阅存储库部分将这些存储库添加到您的构建系统中。

为了帮助进行依赖管理，Spring AI 提供了 BOM（物料清单），以确保在整个项目中使用一致的 Spring AI 版本。请参阅[依赖管理](https://docs.spring.io/spring-ai/reference/getting-started.html#dependency-management "依赖管理")部分将 Spring AI BOM 添加到您的构建系统。

#### 自动配置

Spring AI 为 Ollama 聊天客户端提供 Spring Boot 自动配置。要启用它，请将以下依赖项添加到项目的 Maven `pom.xml` 文件中：

```xml
<dependency>
   <groupId>org.springframework.ai</groupId>
   <artifactId>spring-ai-ollama-spring-boot-starter</artifactId>
</dependency>
```

或者，在你的 Gradle 构建文件 `build.gradle` 中添加：

```groovy
dependencies {
    implementation 'org.springframework.ai:spring-ai-ollama-spring-boot-starter'
}
```

### Ollama Chat 模型

Ollama Chat 模型是一个基于大型语言模型的对话系统，支持对话问答、文本生成等功能。

#### Qwen2

> Qwen2 是阿里巴巴集团推出的全新系列大型语言模型。<br/>
Qwen2基于29 种语言的数据进行训练，包括英语和中文。

**注意**: 安装 `ollama run qwen2` 将下载一个 4.4GB 的 docker 镜像。

它有 4 种参数大小：0.5B、1.5B、7B、72B。
在 7B 和 72B 模型中，上下文长度已扩展至128k 个 token。

| Models       | Qwen2-0.5B | Qwen2-1.5B | Qwen2-7B | Qwen2-72B |
|--------------|------------|------------|----------|-----------|
| Params       | 0.49B      | 1.54B      | 7.07B    | 72.71B    |
| Non-Emb Params | 0.35B    | 1.31B      | 5.98B    | 70.21B    |
| GQA          | True       | True       | True     | True      |
| Tie Embedding | True      | True       | False    | False     |
| Context Length | 32K      | 32K        | 128K     | 128K      |

- 文档地址：https://ollama.com/library/qwen2

#### Llama 3

> Meta Llama 3 是 Meta Inc. 开发的一系列模型，是最新推出的先进模型，具有8B和70B两种参数大小（预训练或指令调整）。<br/>
Llama 3 指令调整模型针对对话/聊天用例进行了微调和优化，并且在常见基准测试中胜过许多可用的开源聊天模型。

**注意**: 安装 `ollama run llama3` 将下载一个 4.7GB 的 docker 镜像。

##### 模型变体

Instruct 是针对聊天/对话场景进行了fine-tuned。

```shell
ollama run llama3
ollama run llama3:70b
```

Pre-trained 的是基础模型。

```shell
ollama run llama3:text
ollama run llama3:70b-text
```

- 文档地址：https://ollama.com/library/llama3

### Text-To-Speech (TTS)

#### Edge-TTS

> **Edge-TTS** 是由微软推出的文本转语音Python库，通过微软 `Azure Cognitive Services` 转化文本为自然语音。适合需要语音功能的开发者，GitHub上超3000星。作为国内付费TTS服务的替代品，Edge-TTS支持40多种语言和300种声音，提供优质的语音输出，满足不同开发需求。

- GitHub：https://github.com/rany2/edge-tts

##### 安装部署

首先，安装 Edge-TTS 库：

```shell
pip install edge-tts
```

使用 `edge-tts -h` 命令查看使用帮助：

```shell
PS C:\Windows\system32> edge-tts -h
usage: edge-tts [-h] [-t TEXT] [-f FILE] [-v VOICE] [-l] [--rate RATE] [--volume VOLUME] [--pitch PITCH]
                [--words-in-cue WORDS_IN_CUE] [--write-media WRITE_MEDIA] [--write-subtitles WRITE_SUBTITLES]
                [--proxy PROXY]

Microsoft Edge TTS

options:
  -h, --help            show this help message and exit
  -t TEXT, --text TEXT  what TTS will say
  -f FILE, --file FILE  same as --text but read from file
  -v VOICE, --voice VOICE
                        voice for TTS. Default: en-US-AriaNeural
  -l, --list-voices     lists available voices and exits
  --rate RATE           set TTS rate. Default +0%.
  --volume VOLUME       set TTS volume. Default +0%.
  --pitch PITCH         set TTS pitch. Default +0Hz.
  --words-in-cue WORDS_IN_CUE
                        number of words in a subtitle cue. Default: 10.
  --write-media WRITE_MEDIA
                        send media output to file instead of stdout
  --write-subtitles WRITE_SUBTITLES
                        send subtitle output to provided file instead of stderr
  --proxy PROXY         use a proxy for TTS and voice list.
```

然后，进行简单的测试：

```shell
edge-tts --text "hello world" --write-media  E:/hello.mp3
```

添加参数后再次测试，使用 `zh-CN-XiaoyiNeural` 语音：

```shell
edge-tts --voice "zh-CN-XiaoyiNeural" --text "你好，有什么可以帮助你的吗?" --write-media E:/hello2.mp3
```

### Spring AI + Edge-TTS Audio Speech 功能扩展

> Spring AI 并未提供 `Ollama` 整合 `Edge-TTS` 文本转语音模型实现将对话内容转语音的能力，为了实验这块的能力，这里扩展了 Spring AI 对  [Edge-TTS](https://github.com/rany2/edge-tts) 的支持，以实现后面的边对话边转语音的功能 。

- EdgeTtsNativeAudioApi 实现了 通过 Java 调用 `Edge-TTS` 命令的方式，将对话内容转为语音。

```java
public ResponseEntity<SpeechResponse> createSpeech(SpeechRequest speechRequest) {

        Assert.notNull(speechRequest, "The request body can not be null.");
        Assert.isTrue(speechRequest.stream() == 0, "Request must set the steam property to 0.");
        MultiValueMap body = ApiUtils.toMultiValueMap(speechRequest);
        return this.restClient.post()
                .uri("/tts")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .toEntity(SpeechResponse.class);
    }
```

- EdgeTtsAudioApi 实现了 `Edge-TTS` 的 `API` 接口，用于将对话内容转为语音。

https://www.bilibili.com/read/cv34776791/
https://azure.microsoft.com/zh-cn/products/ai-services/text-to-speech/


```java
public ResponseEntity<SpeechResponse> createSpeech(SpeechRequest speechRequest) {

        Assert.notNull(speechRequest, "The request body can not be null.");
        Assert.isTrue(speechRequest.stream() == 0, "Request must set the steam property to 0.");
        MultiValueMap body = ApiUtils.toMultiValueMap(speechRequest);
        return this.restClient.post()
                .uri("/tts")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .toEntity(SpeechResponse.class);
    }
```

### 相关项目：

- https://github.com/lyz1810/edge-tts
- https://github.com/WhiteMagic2014/tts-edge-java
- https://github.com/bravekingzhang/text2video
- Java Audio Stack: https://github.com/bowbahdoe/java-audio-stack

```xml
<!-- https://mvnrepository.com/artifact/org.jlayer/jlayer -->
<dependency>
    <groupId>org.jlayer</groupId>
    <artifactId>jlayer</artifactId>
    <version>1.0.0</version>
</dependency>
<!-- https://mvnrepository.com/artifact/dev.mccue/jlayer -->
<dependency>
    <groupId>dev.mccue</groupId>
    <artifactId>jlayer</artifactId>
    <version>2024.04.19</version>
</dependency>
<!-- https://mvnrepository.com/artifact/dev.mccue/jlayer-decoder -->
<dependency>
    <groupId>dev.mccue</groupId>
    <artifactId>jlayer-decoder</artifactId>
    <version>2024.04.19</version>
</dependency>
<!-- https://mvnrepository.com/artifact/dev.mccue/jlayer-converter -->
<dependency>
    <groupId>dev.mccue</groupId>
    <artifactId>jlayer-converter</artifactId>
    <version>2024.04.19</version>
</dependency>
<!-- https://mvnrepository.com/artifact/dev.mccue/jlayer-player -->
<dependency>
    <groupId>dev.mccue</groupId>
    <artifactId>jlayer-player</artifactId>
    <version>2024.04.19</version>
</dependency>
```

- https://github.com/pdudits/soundlibs

```xml
<!-- https://mvnrepository.com/artifact/com.googlecode.soundlibs/tritonus-share -->
<dependency>
    <groupId>com.googlecode.soundlibs</groupId>
    <artifactId>tritonus-share</artifactId>
    <version>0.3.7.4</version>
</dependency>
<!-- https://mvnrepository.com/artifact/com.googlecode.soundlibs/tritonus-all -->
<dependency>
    <groupId>com.googlecode.soundlibs</groupId>
    <artifactId>tritonus-all</artifactId>
    <version>0.3.7.2</version>
</dependency>
<!-- https://mvnrepository.com/artifact/com.googlecode.soundlibs/jlayer -->
<dependency>
    <groupId>com.googlecode.soundlibs</groupId>
    <artifactId>jlayer</artifactId>
    <version>1.0.1.4</version>
</dependency>
<!-- https://mvnrepository.com/artifact/com.googlecode.soundlibs/mp3spi -->
<dependency>
    <groupId>com.googlecode.soundlibs</groupId>
    <artifactId>mp3spi</artifactId>
    <version>1.9.5.3</version>
</dependency>
<dependency>
    <groupId>com.googlecode.soundlibs</groupId>
    <artifactId>jorbis</artifactId>
    <version>0.0.17.4</version>
</dependency>
<dependency>
    <groupId>com.googlecode.soundlibs</groupId>
    <artifactId>vorbisspi</artifactId>
    <version>1.0.3.3</version>
</dependency>
<dependency>
    <groupId>com.googlecode.soundlibs</groupId>
    <artifactId>basicplayer</artifactId>
    <version>3.0.0.0</version>
</dependency>
```
