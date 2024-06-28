## spring-ai-ollama-voice-assistant

### 项目介绍

> 基于 Spring Boot 3.x 、Spring AI 和 [Ollama](https://ollama.com/)、[Whisper](https://github.com/openai/whisper) 、 [ChatTTS](https://github.com/2noise/ChatTTS) 的 语音助手 示例。

整合 Whisper 音频转录、ChatTTS 与 本地 Ollama 服务器并输出 Text-To-Speech (TTS) 音频响应

下面是离线模式下3种工具的简单组合：

- 大型语言模型：在离线模式下运行 [Ollama](https://ollama.com/) 本地模型
- 语音识别：在离线模式下运行 [Whisper](https://github.com/openai/whisper) 本地模型
- 文本转语音：在离线模式下运行 [ChatTTS](https://github.com/2noise/ChatTTS) 本地模型
- 语言克隆：在离线模式下运行 [OpenVoice](https://github.com/myshell-ai/OpenVoice) EmotiVoice(rom NetEase)

### 先决条件

您首先需要在本地计算机上运行 Ollama。请参阅官方 [Ollama 项目自述文件](https://github.com/ollama/ollama "Ollama 项目自述文件")，开始在本地计算机上运行模型。

Whisper 依赖 GPU 运行，因此在运行之前需要通过 `pip install` 安装 Cuda 。

```shell
pip install cuda
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

#### ChatTTS

> ChatTTS 是专为对话场景设计的语音生成模型，专门用于大型语言模型 (LLM) 助手的对话任务，以及对话式音频和视频介绍等应用。它支持中英文，通过使用约 10 万小时的中英文数据进行训练，ChatTTS 在语音合成方面表现出很高的质量和自然度。

- Hugging Face：https://huggingface.co/2Noise/ChatTTS
- GitHub：https://github.com/2noise/ChatTTS

#### ChatTTS webUI & API

> 一个简单的本地网页界面，在网页使用 ChatTTS 将文字合成为语音，支持中英文、数字混杂，并提供API接口。

界面预览

![](./ChatTTS-ui.png)

- GitHub：https://github.com/jianchang512/ChatTTS-ui

##### 安装部署

Windows 预打包版

- 从 Releases 中下载压缩包，解压后双击 app.exe 即可使用
- 某些安全软件可能报毒，请退出或使用源码部署
- 英伟达显卡大于4G显存，并安装了CUDA11.8+后，将启用GPU加速

Linux 下容器部署
安装

拉取项目仓库

在任意路径下克隆项目，例如：

git clone https://github.com/jianchang512/ChatTTS-ui.git chat-tts-ui

启动 Runner

进入到项目目录：

cd chat-tts-ui

启动容器并查看初始化日志：

gpu版本
docker compose -f docker-compose.gpu.yaml up -d

cpu版本    
docker compose -f docker-compose.cpu.yaml up -d

docker compose logs -f --no-log-prefix

    访问 ChatTTS WebUI

    启动:['0.0.0.0', '9966']，也即，访问部署设备的 IP:9966 即可，例如：
        本机：http://127.0.0.1:9966
        服务器: http://192.168.1.100:9966

更新

Get the latest code from the main branch:

git checkout main
git pull origin main

Go to the next step and update to the latest image:

docker compose down

gpu版本
docker compose -f docker-compose.gpu.yaml up -d --build

cpu版本
docker compose -f docker-compose.cpu.yaml up -d --build

docker compose logs -f --no-log-prefix


### Speech-To-Text (ASR) 

#### Whisper

> Whisper 是一种用于自动语音识别 (ASR) 和语音翻译的预训练模型。Whisper 模型基于 68 万小时的标记数据进行训练，表现出强大的泛化能力，可适用于许多数据集和领域，而无需进行微调。

- Hugging Face：https://huggingface.co/openai/whisper-large-v3
- GitHub：https://github.com/openai/whisper
- Blog：https://openai.com/blog/whisper/


下载耳语 模型并将其放在whisper子文件夹中（例如 https://openaipublic.azureedge.net/main/whisper/models/e5b1a55b89c1367dacf97e3e19bfd829a01529dbfdeefa8caeb59b3f1b81dadb/large-v3.pt）

配置assistant.yaml设置。（默认情况下，设置为使用法语和 ollama mistral模型...）

跑步assistant.py
 

#### Whisper API（方案1）

> 此开源项目使用经过微调的 `Whisper ASR 模型` 提供了一个自托管 API，用于将语音转录为文本。此 API 允许您通过 HTTP 请求轻松将音频文件转换为文本。非常适合为您的应用程序添加语音识别功能。

主要特征：

- 使用经过微调的 Whisper 模型实现准确的语音识别
- 用于音频文件转录的简单 HTTP API
- 使用 API 密钥进行用户级访问以管理使用情况
- 为您自己的语音转录服务提供自托管代码
- 量化模型优化，实现快速高效的推理
- 开源实现定制化和透明化

GitHub：https://github.com/innovatorved/whisper.api

##### 安装

要安装必要的依赖项，请运行以下命令：

```shell
# Install ffmpeg for Audio Processing
sudo apt install ffmpeg

# Install Python Package
pip install -r requirements.txt
```

##### 运行项目

要运行该项目，请使用以下命令：

```shell
uvicorn app.main:app --reload
```

##### 获取你的Token

要获取您的令牌，请使用以下命令：

```shell
curl -X 'POST' \
'https://innovatorved-whisper-api.hf.space/api/v1/users/get_token' \
-H 'accept: application/json' \
-H 'Content-Type: application/json' \
-d '{
"email": "example@domain.com",
"password": "password"
}'
```

##### 转录文件的示例

要上传文件并转录，请使用以下命令：注意：该令牌是虚拟令牌，不起作用。请使用管理员提供的令牌。

有以下型号可供选择：

- tiny.en
- tiny.en.q5
- base.en.q5

```shell
# Modify the token and audioFilePath
curl -X 'POST' \
'http://localhost:8000/api/v1/transcribe/?model=tiny.en.q5' \
-H 'accept: application/json' \
-H 'Authentication: e9b7658aa93342c492fa64153849c68b8md9uBmaqCwKq4VcgkuBD0G54FmsE8JT' \
-H 'Content-Type: multipart/form-data' \
-F 'file=@audioFilePath.wav;type=audio/wav'
```

#### whisper.cpp（方案2）

##### Java JNI bindings for Whisper

此软件包为 whisper.cpp 提供 Java JNI 绑定。它们已在以下平台上测试：

* <strike>Darwin (OS X) 12.6 on x64_64</strike>
* Ubuntu on x86_64
* Windows on x86_64

“低级” 绑定位于 `WhisperCppJnaLibrary`. 最简单的用法如下:

JNA 将尝试从以下位置加载 `whispercpp` 共享库：

- jna.library.path
- jna.platform.library
- ~/Library/Frameworks
- /Library/Frameworks
- /System/Library/Frameworks
- classpath

```java
import io.github.ggerganov.whispercpp.WhisperCpp;

public class Example {

    public static void main(String[] args) {
        WhisperCpp whisper = new WhisperCpp();
        // By default, models are loaded from ~/.cache/whisper/ and are usually named "ggml-${name}.bin"
        // or you can provide the absolute path to the model file.
        long context = whisper.initContext("base.en");
        try {
            var whisperParams = whisper.getFullDefaultParams(WhisperSamplingStrategy.WHISPER_SAMPLING_GREEDY);
            // custom configuration if required
            whisperParams.temperature_inc = 0f;
            
            var samples = readAudio(); // divide each value by 32767.0f
            whisper.fullTranscribe(whisperParams, samples);
            
            int segmentCount = whisper.getTextSegmentCount(context);
            for (int i = 0; i < segmentCount; i++) {
                String text = whisper.getTextSegment(context, i);
                System.out.println(segment.getText());
            }
        } finally {
             whisper.freeContext(context);
        }
     }
}
```

##### Building & Testing

In order to build, you need to have the JDK 8 or higher installed. Run the tests with:

```bash
git clone https://github.com/ggerganov/whisper.cpp.git
cd whisper.cpp/bindings/java

./gradlew build
```

您需要在 [JNA library path](https://java-native-access.github.io/jna/4.2.1/com/sun/jna/NativeLibrary.html) 中包含 `whisper` 库。在 Windows 上，dll 包含在 jar 中，您可以更新它：

```bash
copy /y ..\..\build\bin\Release\whisper.dll build\generated\resources\main\win32-x86-64\whisper.dll
```
https://mvnrepository.com/artifact/io.github.ggerganov/whispercpp

### Voice Cloning

#### EmotiVoice

- Github：https://github.com/netease-youdao/EmotiVoice

> EmotiVoice是一个强大的开源TTS引擎，完全免费，支持中英文双语，包含2000多种不同的音色，以及特色的情感合成功能，支持合成包含快乐、兴奋、悲伤、愤怒等广泛情感的语音。

## 快速入门

### EmotiVoice Docker镜像

尝试EmotiVoice最简单的方法是运行docker镜像。你需要一台带有NVidia GPU的机器。先按照[Linux](https://www.server-world.info/en/note?os=Ubuntu_22.04&p=nvidia&f=2)和[Windows WSL2](https://zhuanlan.zhihu.com/p/653173679)平台的说明安装NVidia容器工具包。然后可以直接运行EmotiVoice镜像：

```sh
docker run -dp 127.0.0.1:8501:8501 syq163/emoti-voice:latest
```

现在打开浏览器，导航到 http://localhost:8501 ，就可以体验`EmotiVoice`强大的TTS功能。从2024年的docker镜像版本开始，通过 `http://localhost:8000/` 可以使用类 `OpenAI TTS` 的API功能。

 
#### OpenVoice

- Github：https://github.com/myshell-ai/OpenVoice

##### OpenVoice V1

OpenVoice 的优势有三方面：

1. 精准的音色克隆。OpenVoice 可以精准地克隆参考音色并生成多种语言和口音的语音。
2. 灵活的语音风格控制。OpenVoice 可以对语音风格（如情绪和口音）以及节奏、停顿和语调等其他风格参数进行精细控制。
3. 零样本跨语言语音克隆。 生成的语音的语言和参考语音的语言都不需要出现在海量说话人多语言训练数据集中。

##### OpenVoice V2

2024 年 4 月，我们发布了 OpenVoice V2，它包含 V1 中的所有功能，并且具有：

1. 更好的音频质量。OpenVoice V2 采用了不同的训练策略，可以提供更好的音频质量。
2. 原生多语言支持。OpenVoice V2 原生支持英语、西班牙语、法语、中文、日语和韩语。
3. 免费商业使用。 自 2024 年 4 月起，V2 和 V1 均根据 MIT 许可发布。可免费用于商业用途。


参考项目：
- 
- https://github.com/hkgood/Ollama_ChatTTS
- https://github.com/maudoin/ollama-voice
- https://github.com/vndee/local-talking-llm
- https://github.com/brucevoin/talk-to-llm
- https://github.com/matatonic/openedai-speech
