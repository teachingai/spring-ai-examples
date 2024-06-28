## spring-ai-ollama-voice-chattts

### 项目介绍

> 基于 Spring Boot 3.x 、Spring AI 和 [Ollama](https://ollama.com/)、[ChatTTS](https://chattts.com/) 的 Text-To-Speech (TTS) 功能示例。

整合 ChatTTS 与 本地 Ollama 服务器并输出 Text-To-Speech (TTS) 音频响应

下面是离线模式下2种工具的简单组合：

- 大型语言模式：在离线模式下运行 [Ollama](https://ollama.com/) 本地模型
- 离线文本转语音：在离线模式下运行 [ChatTTS](https://github.com/2noise/ChatTTS) 本地模型

### 先决条件

您首先需要在本地计算机上运行 Ollama。请参阅官方 [Ollama 项目自述文件](https://github.com/ollama/ollama "Ollama 项目自述文件")，开始在本地计算机上运行模型。

ChatTTS 自身不提供API功能，为了能使用API 方式调用 ChatTTS，可通过 [ChatTTS-ui](https://github.com/jianchang512/ChatTTS-ui) 项目进行调用。

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


参考项目：

- https://github.com/hkgood/Ollama_ChatTTS 
- https://github.com/maudoin/ollama-voice
- https://github.com/coqui-ai/tts
- https://github.com/lucataco/cog-xtts-v2
