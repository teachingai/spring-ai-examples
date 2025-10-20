#!/bin/bash

# Spring AI Ollama Generation 测试运行脚本

echo "开始运行 Spring AI Ollama Generation 测试..."

# 检查是否在正确的目录
if [ ! -f "pom.xml" ]; then
    echo "错误：请在项目根目录运行此脚本"
    exit 1
fi

# 运行测试
echo "运行单元测试..."
mvn test -Dtest=OllamaGenerationApiTest -Dspring.profiles.active=test

echo "测试完成！"
