# Spring AI 入门实践

> 基于 Spring Boot 3.4.x 和 Spring AI 1.0.1 的完整学习指南

## 📚 文章目录

### 第一阶段：基础入门

| 序号 | 文章标题 | 状态 | 对应模块 | 预计字数 |
|------|----------|------|----------|----------|
| 1 | [Spring AI 环境准备与快速开始](./spring-ai-quick-start.md) | ✅ 已完成 | - | 5000+ |
| 2 | Spring AI 文本生成（Text Generation API） | 🔄 编写中 | spring-ai-ollama-generation | 4000+ |
| 3 | Spring AI 多轮对话（Chat Completion API） | ⏳ 待编写 | spring-ai-ollama-chat | 4500+ |
| 4 | Spring AI 提示工程（Prompt Engineering） | ⏳ 待编写 | spring-ai-ollama-prompt | 5000+ |
| 5 | Spring AI 工具调用（Tool Calling） | ⏳ 待编写 | spring-ai-ollama-function-calling | 4500+ |

### 第二阶段：核心功能

| 序号 | 文章标题 | 状态 | 对应模块 | 预计字数 |
|------|----------|------|----------|----------|
| 6 | Spring AI 文本嵌入（Embeddings） | ⏳ 待编写 | spring-ai-ollama-embedding | 4000+ |
| 7 | Spring AI 图片生成（Image Generation API） | ⏳ 待编写 | spring-ai-stabilityai | 4500+ |
| 8 | Spring AI 向量数据库集成（Vector Databases） | ⏳ 待编写 | spring-ai-ollama-rag-* | 6000+ |
| 9 | Spring AI 检索增强生成（RAG） | ⏳ 待编写 | spring-ai-ollama-rag-chroma | 5500+ |
| 10 | Spring AI 智能体（Agents） | ⏳ 待编写 | spring-ai-ollama-agents | 5000+ |

### 第三阶段：高级应用

| 序号 | 文章标题 | 状态 | 对应模块 | 预计字数 |
|------|----------|------|----------|----------|
| 11 | Spring AI 模型上下文协议（MCP） | ⏳ 待编写 | spring-ai-ollama-mcp-* | 4500+ |
| 12 | Spring AI 语音处理集成 | ⏳ 待编写 | spring-ai-ollama-voice-* | 5000+ |
| 13 | Spring AI 监控与可观察性 | ⏳ 待编写 | spring-ai-ollama-prometheus | 4000+ |
| 14 | Spring AI 模型微调（Fine-tuning） | ⏳ 待编写 | spring-ai-ollama-fine-tuning | 4500+ |
| 15 | Spring AI 应用实战案例 | ⏳ 待编写 | spring-ai-sql, spring-ai-naming | 6000+ |

## 🎯 学习目标

### 知识目标
- 掌握 Spring AI 核心概念和 API
- 理解 AI 应用开发的最佳实践
- 学会集成各种 AI 模型和服务
- 掌握 RAG 和向量数据库的使用

### 技能目标
- 能够独立开发 Spring AI 应用
- 掌握多模态 AI 能力集成
- 学会构建智能代理和工具调用
- 具备 AI 应用性能优化能力

### 项目目标
- 完成 15 篇高质量技术文章
- 构建完整的 AI 应用示例
- 形成可复用的开发模板
- 建立最佳实践指南

## 📖 学习路径

### 基础阶段（第1-5篇）
**目标**: 掌握 Spring AI 基础概念和核心 API

**学习重点**:
- Spring AI 环境搭建
- 文本生成和对话 API
- 提示工程和工具调用
- 基础项目结构

**实践项目**: 简单的聊天机器人

### 进阶阶段（第6-10篇）
**目标**: 深入理解 AI 应用核心功能

**学习重点**:
- 向量嵌入和相似度计算
- 图像生成和处理
- 向量数据库集成
- RAG 检索增强
- 智能代理开发

**实践项目**: 智能文档助手

### 高级阶段（第11-15篇）
**目标**: 掌握高级 AI 应用开发

**学习重点**:
- MCP 模型上下文协议
- 语音处理集成
- 监控和可观察性
- 模型微调
- 完整应用架构

**实践项目**: 企业级 AI 平台

## 🛠️ 技术栈

### 核心框架
- **Spring Boot 3.4.5** - 应用框架
- **Spring AI 1.0.1** - AI 集成框架
- **Java 17** - 编程语言

### AI 模型
- **Ollama** - 本地 AI 模型
- **OpenAI** - GPT 系列模型
- **Anthropic** - Claude 系列模型
- **Azure OpenAI** - 微软云 AI 服务

### 向量数据库
- **Chroma** - 轻量级向量数据库
- **Elasticsearch** - 搜索引擎
- **MongoDB** - 文档数据库
- **Neo4j** - 图数据库
- **Redis** - 内存数据库

### 开发工具
- **Maven** - 构建工具
- **IntelliJ IDEA** - IDE
- **Docker** - 容器化
- **Prometheus** - 监控
- **Langfuse** - 日志追踪

## 📁 项目结构

```
spring-ai-examples/
├── docs/                           # 文档目录
│   ├── README.md                   # 本文档
│   ├── spring-ai-quick-start.md    # 第1篇：环境准备
│   ├── spring-ai-text-generation.md # 第2篇：文本生成
│   └── ...                         # 其他文章
├── spring-ai-ollama-generation/    # 文本生成示例
├── spring-ai-ollama-chat/          # 对话功能示例
├── spring-ai-ollama-prompt/        # 提示工程示例
├── spring-ai-ollama-function-calling/ # 工具调用示例
├── spring-ai-ollama-embedding/     # 向量嵌入示例
├── spring-ai-ollama-rag-*/         # RAG 示例（多个）
├── spring-ai-ollama-agents/        # 智能代理示例
└── ...                             # 其他模块
```

## 🚀 快速开始

### 1. 环境准备
```bash
# 检查 Java 版本
java -version

# 检查 Maven 版本
mvn -version

# 安装 Ollama（可选）
curl -fsSL https://ollama.com/install.sh | sh
```

### 2. 克隆项目
```bash
git clone <your-repo-url>
cd spring-ai-examples
```

### 3. 运行示例
```bash
# 运行文本生成示例
cd spring-ai-ollama-generation
mvn spring-boot:run

# 运行对话示例
cd spring-ai-ollama-chat
mvn spring-boot:run
```

### 4. 阅读文档
按照文章顺序阅读，每篇文章都包含：
- 理论基础
- 代码示例
- 运行测试
- 最佳实践

## 📝 写作规范

### 文章结构
每篇文章应包含以下部分：
1. **引言** - 背景和意义
2. **理论基础** - 核心概念
3. **环境准备** - 依赖和配置
4. **代码实现** - 详细示例
5. **运行测试** - 验证结果
6. **最佳实践** - 经验总结
7. **扩展阅读** - 相关资源

### 代码规范
- 使用 Java 17+ 语法
- 遵循 Spring Boot 最佳实践
- 包含完整的错误处理
- 提供详细的注释

### 图片和图表
- 使用 Mermaid 绘制架构图
- 提供代码运行截图
- 包含性能测试图表

## 🤝 贡献指南

### 如何贡献
1. Fork 项目
2. 创建特性分支
3. 提交更改
4. 推送到分支
5. 创建 Pull Request

### 写作要求
- 文章字数：3000-6000 字
- 代码示例：完整可运行
- 图片质量：清晰易懂
- 技术准确性：经过验证

## 📞 联系方式

- **项目地址**: [GitHub Repository]
- **文档地址**: [Documentation Site]
- **问题反馈**: [Issues Page]

## 📄 许可证

本项目采用 MIT 许可证，详见 [LICENSE](../LICENSE) 文件。

---

**开始您的 Spring AI 学习之旅吧！** 🎉

> 建议按照文章顺序学习，每篇文章都建立在前一篇的基础上，确保学习效果最佳。

