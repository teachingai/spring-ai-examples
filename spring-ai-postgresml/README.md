# Spring AI PostgresML 项目

本项目演示了如何使用 Spring AI 与 PostgresML 进行集成，并提供了 JdbcTemplate 的完整配置和使用示例。

## 功能特性

- Spring AI PostgresML 嵌入模型集成
- JdbcTemplate 数据库操作支持
- REST API 接口
- 事务管理
- 连接池配置

## 环境要求

- Java 17+
- Spring Boot 3.x
- PostgreSQL 数据库
- PostgresML 扩展

## 快速开始

### 1. 数据库配置

在 `application.properties` 中配置数据库连接信息：

```properties
# 数据库连接配置
spring.datasource.url=jdbc:postgresql://localhost:5432/postgresml
spring.datasource.username=postgres
spring.datasource.password=password
spring.datasource.driver-class-name=org.postgresql.Driver
```

### 2. 启动应用

```bash
mvn spring-boot:run
```

### 3. API 接口

#### 测试数据库连接
```bash
GET /api/jdbc/test-connection
```

#### 获取所有表名
```bash
GET /api/jdbc/tables
```

#### 执行自定义查询
```bash
POST /api/jdbc/query
Content-Type: application/json

{
  "sql": "SELECT * FROM your_table LIMIT 10"
}
```

#### 执行更新操作
```bash
POST /api/jdbc/update
Content-Type: application/json

{
  "sql": "UPDATE your_table SET column = ? WHERE id = ?",
  "args": ["new_value", 1]
}
```

## 配置说明

### JdbcTemplate 配置

项目包含以下 JdbcTemplate 相关配置：

- **JdbcConfig**: 配置类，初始化 JdbcTemplate 和事务管理器
- **JdbcExampleService**: 服务类，提供数据库操作示例
- **JdbcController**: 控制器，提供 REST API 接口

### 连接池配置

使用 HikariCP 连接池，默认配置：

- 最大连接数：10
- 最小空闲连接：5
- 连接超时：30秒
- 空闲超时：10分钟
- 最大生命周期：30分钟

## 注意事项

1. 确保 PostgreSQL 数据库已安装并运行
2. 确保 PostgresML 扩展已正确安装
3. 根据实际环境修改数据库连接配置
4. 生产环境中请使用更安全的密码和连接配置
