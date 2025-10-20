# Spring AI Ollama Generation 测试说明

## 测试概述

本项目为 `GenerationController` 编写了完整的 Spring Boot 集成测试，包括：

### 测试功能
1. **HTTP 接口测试** - 测试 `/ai/generate` 和 `/ai/generateStream` 端点
2. **直接控制器测试** - 直接调用控制器方法进行测试
3. **多语言支持测试** - 测试中文和英文消息处理
4. **流式响应测试** - 测试流式文本生成功能

### 测试方法

#### 1. HTTP 接口测试
- `testGenerate_Success()` - 测试成功的文本生成请求
- `testGenerate_WithDefaultMessage()` - 测试使用默认消息的请求
- `testGenerate_EmptyMessage()` - 测试空消息的请求
- `testGenerateStream_Success()` - 测试流式文本生成请求
- `testGenerateStream_WithDefaultMessage()` - 测试使用默认消息的流式请求
- `testGenerateStream_EmptyMessage()` - 测试空消息的流式请求

#### 2. 直接控制器测试
- `testGenerate_DirectControllerCall()` - 直接测试控制器方法
- `testGenerateStream_DirectControllerCall()` - 直接测试流式控制器方法

#### 3. 多语言测试
- `testGenerate_ChineseMessage()` - 测试中文消息
- `testGenerate_EnglishMessage()` - 测试英文消息
- `testGenerateStream_LongMessage()` - 测试长消息的流式生成

## 运行测试

### 前提条件
1. 确保 Ollama 服务正在运行（可选，如果服务不可用，测试会优雅地处理异常）
2. 确保项目依赖已正确安装

### 运行方式

#### 方式一：使用 Maven 命令
```bash
cd spring-ai-ollama-generation
mvn test -Dtest=OllamaGenerationApiTest -Dspring.profiles.active=test
```

#### 方式二：使用提供的脚本
```bash
cd spring-ai-ollama-generation
chmod +x run-tests.sh
./run-tests.sh
```

#### 方式三：在 IDE 中运行
1. 在 IDE 中打开 `OllamaGenerationApiTest.java`
2. 右键点击类名或方法名
3. 选择 "Run Test" 或 "Debug Test"

## 测试配置

### 测试环境配置
测试使用 `application-test.properties` 配置文件，包含：
- Ollama 服务配置（默认 localhost:11434）
- 日志级别配置
- 超时设置

### 测试特性
- 使用 `@SpringBootTest` 进行完整的 Spring 上下文测试
- 使用 `@AutoConfigureWebMvc` 进行 Web 层测试
- 使用 `@ActiveProfiles("test")` 激活测试配置
- 支持 MockMvc 进行 HTTP 请求模拟

## 注意事项

1. **Ollama 服务依赖**：如果 Ollama 服务不可用，某些测试可能会失败，这是正常行为
2. **网络连接**：测试需要网络连接来访问 Ollama 服务
3. **超时设置**：测试配置了 30 秒超时，如果响应时间过长可能会超时
4. **资源清理**：测试会自动清理资源，无需手动干预

## 故障排除

### 常见问题
1. **Spring Boot 配置错误**：确保主配置类 `SpringAiOllamaApplication` 存在
2. **Ollama 服务不可用**：检查 Ollama 服务是否正在运行
3. **端口冲突**：确保 11434 端口没有被其他服务占用
4. **依赖问题**：确保所有 Maven 依赖已正确下载

### 调试建议
1. 查看测试日志输出
2. 检查 Ollama 服务状态
3. 验证网络连接
4. 检查配置文件设置
