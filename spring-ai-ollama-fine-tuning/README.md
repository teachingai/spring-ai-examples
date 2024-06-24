## spring-ai-ollama-fine-tuning

### 项目介绍

> 基于 Spring Boot 3.x 、Spring AI 和 [Ollama](https://ollama.com/) 整合使用 LORA 微调后的本地 ChatGLM-4-9B 大语言模型提供的智能对话能力，支持对话问答、文本生成等功能示例。


### 知识点 

- ChatGLM-4-9B 模型
- LoRA、QLoRA 微调
  https://blog.csdn.net/spiderwower/article/details/138755776
- Ollama 自定义模型

### 项目结构

```
spring-ai-ollama-fine-tuning
├── src/main/java/com/github/teachingai/ollama
│   ├── controller
│   │   └── ChatController.java
│   ├── model
│   │   └── ChatRequest.java
│   └── service
│       └── ChatService.java
├── src/main/resources
│   ├── application.properties
│   └── static
│       └── index.html
├── .gitignore
├── pom.xml
└── README.md
```




