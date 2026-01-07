# 微服务项目演示

这是一个基于 Spring Cloud 构建的微服务项目演示，旨在展示微服务架构中的服务注册与发现、API 网关、用户认证与授权、以及产品服务等核心组件的集成与协作。

## 项目架构

本项目采用典型的微服务架构，主要包含以下核心服务和组件：

*   **API 网关 (Gateway Service)**: 基于 Spring Cloud Gateway 构建，负责请求路由、负载均衡、认证授权（通过集成 JWT 和 LDAP）以及统一的 API 入口。所有外部请求都将通过网关转发到后端相应的微服务。
*   **用户服务 (User Service)**: 负责用户管理、认证（支持 LDAP 和 OAuth2/GitHub 登录）和授权。它与 MySQL 数据库交互存储用户信息，并与 OpenLDAP 进行用户身份验证。
*   **产品服务 (Product Service)**: 负责产品信息的管理。它与 MySQL 数据库交互存储产品数据。
*   **Nacos**: 作为服务注册中心和配置中心，负责微服务的注册与发现，以及动态配置管理。
*   **MySQL**: 关系型数据库，用于存储用户和产品数据。
*   **OpenLDAP**: 轻量级目录访问协议服务，用于集中管理用户身份信息，支持用户服务进行 LDAP 认证。
*   **phpLDAPadmin**: OpenLDAP 的 Web 管理界面，方便管理 LDAP 服务器中的用户和组。

### 架构图

![项目架构图](https://private-us-east-1.manuscdn.com/sessionFile/O3BW8zId3Qz7o3NnuxTqZZ/sandbox/PfSIU9ggIyY2Pi2263ofFe-images_1767763353675_na1fn_L2hvbWUvdWJ1bnR1L21pY3Jvc2VydmljZS1kZW1vL2FyY2hpdGVjdHVyZQ.png?Policy=eyJTdGF0ZW1lbnQiOlt7IlJlc291cmNlIjoiaHR0cHM6Ly9wcml2YXRlLXVzLWVhc3QtMS5tYW51c2Nkbi5jb20vc2Vzc2lvbkZpbGUvTzNCVzh6SWQzUXo3bzNObnV4VHFaWi9zYW5kYm94L1BmU0lVOWdnSXlZMlBpMjI2M29mRmUtaW1hZ2VzXzE3Njc3NjMzNTM2NzVfbmExZm5fTDJodmJXVXZkV0oxYm5SMUwyMXBZM0p2YzJWeWRtbGpaUzFrWlcxdkwyRnlZMmhwZEdWamRIVnlaUS5wbmciLCJDb25kaXRpb24iOnsiRGF0ZUxlc3NUaGFuIjp7IkFXUzpFcG9jaFRpbWUiOjE3OTg3NjE2MDB9fX1dfQ__&Key-Pair-Id=K2HSFNDJXOU9YS&Signature=DJXiNXtNW1SUx3YiIL9FcvpGn~2q8Xc1WFryPk1wqH4kvBGaRHh8-l1GTg4kD~NLhrjKtf5NotjL~BjaPINMJGhfsdII9~9oi5SZtpf7kyOcju2VSWzsRq8WJEwtgYmRV4MPSIrDal~hW-~P~6IFp2bkHEVN7T4WZLvZ-Xtg-5RFAMWhQ0WMDqEYP06PYeT1~~1GndbFWZqxdQX-umqkGAYTvhlqdagL2y9aLSLX-yCye5G6JwEOppQyFJkH8Dkcqeo8P0A75cBsTk27dsrj2ZTgeCG1lhFq4jSEgtlVls~-th3LB9LjmChJnSgylZdHhWASTLfBBj~CBNXZ7c3q3w__ )

## 中间件与技术栈版本

本项目使用的主要中间件和技术栈及其版本如下：

| 中间件/技术栈 | 版本 | 备注 |
| :--- | :--- | :--- |
| **Spring Boot** | 3.3.2 | 微服务框架核心 |
| **Spring Cloud** | 2023.0.1 | 微服务治理框架 |
| **Spring Cloud Alibaba** | 2023.0.1.2 | 阿里巴巴微服务组件，包含 Nacos 集成 |
| **Java** | 17 | 开发语言 |
| **MySQL** | 8.0 | 数据库 |
| **Nacos** | v2.4.0.1 | 服务注册与发现、配置中心 |
| **OpenLDAP** | 1.5.0 | LDAP 服务 |
| **phpLDAPadmin** | 0.9.0 | OpenLDAP Web 管理界面 |
| **MyBatis-Plus** | 3.5.5 | 持久层框架，简化 MyBatis 操作 |
| **Knife4j** | 4.4.0 | API 文档工具，集成 Swagger/OpenAPI |
| **JJWT** | 0.12.3 | Java JWT (JSON Web Token) 库，用于认证授权 |

## 模块结构

项目包含以下主要模块：

*   `common`: 公共模块，包含全局异常处理、JWT 工具类、角色注解等。
*   `gateway-service`: API 网关服务，负责请求路由、认证过滤。
*   `user-service`: 用户服务，提供用户注册、登录、认证（LDAP/OAuth2）等功能。
*   `product-service`: 产品服务，提供产品信息的增删改查功能。

## 快速启动

1.  确保已安装 Docker 和 Docker Compose。
2.  在项目根目录下执行以下命令启动所有服务：
    ```bash
    mvn clean install
    docker-compose up --build
    ```
3.  服务启动后，可以通过 `http://localhost:7573` 访问 API 网关 。

## 测试指南

### 1. 启动环境
```bash
mvn clean install
docker-compose up -d --build
```

### 2. 获取 Access Token
使用预设用户登录：
- **USER**: `user_1` / `user_1`
- **EDITOR**: `editor_1` / `editor_1`
- **ADMIN**: `adm_1` / `adm_1`

```bash
# 1. 数据库登录 (示例：editor_1)
curl -X POST "http://localhost:7573/user/auth/login?username=editor_1&password=editor_1"

# 2. LDAP 登录 (示例：ldap_editor_1 )
# 注意：首次启动 LDAP 容器后，需要几秒钟时间初始化。如果此命令失败，请稍后重试。
# 预设的 LDAP 用户见 docker-compose.yml 同目录下的 ldap-users.ldif 文件。
curl -X POST "http://localhost:7573/user/auth/ldap-login?username=ldap_editor_1&password=ldap_editor_1"

# 3. GitHub 登录
# a. 在项目user-service里面填入GitHub OAuth App 凭证：
#    GITHUB_CLIENT_ID=your_github_client_id
#    GITHUB_CLIENT_SECRET=your_github_client_secret
#    GATEWAY_URL=http://110.41.64.197:7573  公网网关端口
# b. 在浏览器中访问此 URL 以启动 GitHub 登录流程：
    http://110.41.64.197:31300/user/login.html
# d. 授权后 ，页面将直接返回包含 JWT Token 的 JSON 响应。
```
记录从上述任一方法中获取的 `token`。

### 3. 测试产品接口
将 `{TOKEN}` 替换为上一步获取的值。

#### 浏览产品 (需要 USER 角色)
```bash
curl -H "Authorization: Bearer {TOKEN}" http://localhost:7573/product/products
```

#### 添加产品 (需要 EDITOR 角色 )
```bash
curl -X POST -H "Authorization: Bearer {TOKEN}" \
     -H "Content-Type: application/json" \
     -d '{"name": "New Product"}' \
     http://localhost:7573/product/products
```

### 4. 接口文档
- 聚合服务文档: `http://localhost:7573/doc.html`
