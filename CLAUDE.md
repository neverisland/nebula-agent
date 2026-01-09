# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述

Nebula Agent (星云智能) 是一个全栈应用，采用 Java Spring Boot 后端和 Vue 3 前端。项目遵循领域驱动设计（DDD）原则，前后端清晰分离。

## 构建和开发命令

### 后端 (Java Spring Boot)
- **编译**: `mvn clean compile -s D:\Software\Maven\conf\settings.xml -DskipTests`
- **运行测试**: `mvn test -s D:\Software\Maven\conf\settings.xml`
- **打包**: `mvn clean package -s D:\Software\Maven\conf\settings.xml`
- **Maven 本地仓库**: `D:\Maven-Repository`

### 前端 (Vue 3 + Vite)
- **开发服务器**: `cd frontend && npm run dev`
- **构建**: `cd frontend && npm run build`
- **类型检查**: `cd frontend && vue-tsc --noEmit`
- **预览生产构建**: `cd frontend && npm run preview`

## 架构设计

### 后端架构 (DDD + MVC)

后端遵循严格的分层架构，调用链路如下：
```
Controller → Facade → Service → Repository → Mapper → Database
```

**各层职责：**
- **Controller** (`business.*.controller`): HTTP 端点，参数校验，调用 Facade
- **Facade** (`business.*.facade`): 业务接口定义，编排 Service
- **Service** (`business.*.service`): 核心业务逻辑实现，操作 Entity 对象
- **Repository** (`business.*.repository`): 数据访问层，处理 Entity ↔ DO 转换、缓存、事务
- **Entity** (`business.*.entity`): 业务逻辑中使用的领域对象
- **DO** (`business.*.dal`): 数据库持久化对象，不能离开 Repository 层
- **Mapper** (`business.*.mapper`): MyBatis 接口，XML 映射文件在 `resources/mapper/`

**关键架构规则：**
- Service 层只能操作 Entity 对象，不能操作 DO 对象
- Repository 层入参为 Entity/String id，出参为 Entity
- DO 对象不能离开 Repository 层
- 所有数据库操作使用 MyBatis XML 映射（禁止使用 MyBatis-Plus）

### 前端架构 (Vue 3)

- **框架**: Vue 3 Composition API（推荐使用选项式 API）
- **状态管理**: Pinia
- **UI 组件库**: Ant Design Vue
- **HTTP 客户端**: Axios

**目录结构：**
- `frontend/src/api/`: API 接口定义（对应后端 Controller）
- `frontend/src/views/`: 页面组件，按业务模块组织
- `frontend/src/components/`: 可复用组件
- `frontend/src/type/`: TypeScript 类型定义（每个类型一个文件）
- `frontend/src/utils/`: 工具函数
- `frontend/src/store/`: Pinia 状态管理

### 数据流和命名规范

**后端：**
- 入参对象: `XxxxPo` (Parameter Object)
- 出参对象: `XxxxVo` (Value Object)
- 数据库对象: `XxxxDo` (Data Object)
- 领域对象: Entity 类

**前端：**
- 入参对象: `XxxxPo`（必须与后端一致）
- 出参对象: `XxxxVo`（必须与后端一致）
- 每个类型必须在 `frontend/src/type/` 中单独定义一个文件

### API 文档工作流

API 文档位于 `docs/api/`，作为前后端协作的契约：

1. 后端开发者修改 Controller → 更新 `docs/api/{ControllerName}.md`
2. 前端开发者阅读 API 文档 → 在 `frontend/src/api/` 中实现 → 在 `frontend/src/type/` 中定义类型

## 关键开发规则

### 后端 (Java)

1. **仅使用 MyBatis**: 使用 MyBatis XML 映射。禁止使用 MyBatis-Plus API，如 `LambdaQueryWrapper`、`BaseMapper`、`selectList()` 等。
2. **Import 语句**: 始终在文件顶部使用 import 语句。禁止在代码中使用全限定类名（如 `java.util.List`）。
3. **Javadoc 必需**: 所有类、接口和方法必须有 Javadoc 注释，说明用途、参数和返回值。
4. **禁止 Map 参数**: 不能使用 `Map<Object,Object>` 作为参数或返回值。必须定义明确字段的类型。
5. **事务**: 在 Repository 层修改多表的方法上添加 `@Transactional`。
6. **数据库脚本**: 所有表结构变更必须同步到 `data/` 目录的 SQL 文件。
7. **编译验证**: 代码修改后，运行 `mvn clean compile -s D:\Software\Maven\conf\settings.xml -DskipTests` 验证编译。

### 前端 (Vue 3 + TypeScript)

1. **类型定义**: 每个 API 调用必须有明确的 TypeScript 类型。每个类型一个文件，位于 `frontend/src/type/`。
2. **CRUD 分离**: 禁止在一个页面实现所有 CRUD 操作。列表、新增、编辑、详情使用独立页面/弹窗。
3. **API 封装**: 所有 API 调用必须在 `frontend/src/api/` 中。禁止在组件中直接调用 axios。
4. **JSDoc 注释**: 所有函数必须添加 JSDoc 注释，说明用途、参数和返回值。
5. **表单校验**: 使用 Ant Design Vue Form 组件，校验规则在 script 中定义。
6. **编译验证**: 代码修改后，运行 `npm run build` 确保 TypeScript 编译成功。

### 常用工具类

**后端：**
- Bean 转换: `cn.yang.common.data.structure.utils.bean.BeanConvertUtils`
- 判空检查: `org.springframework.util.ObjectUtils`、`org.springframework.util.CollectionUtils`
- 业务异常: `cn.yang.common.data.structure.exception.BusinessException` 配合 `cn.yang.nebula.agent.enums.ErrorStatusCodeEnum`

**前端：**
- HTTP 客户端: `frontend/src/utils/request` 中的 Axios 实例
- 通用类型: `ResultVo<T>`、`PageResult<T>`

## 技术栈

**后端：**
- Java 21
- Spring Boot 3.5.7
- MyBatis 3.0.5
- Sa-Token 1.41.0（权限认证）
- Redis（缓存）
- MySQL（数据库）
- Redisson（分布式锁）
- Aliyun OSS（文件存储）

**前端：**
- Vue 3.3.11
- TypeScript 5.6.3
- Vite 6.3.4
- Ant Design Vue 4.1.1
- Pinia 2.1.7
- Axios 1.8.2

## 业务模块

代码库按业务领域组织：
- **authentication**: 登录、注册、认证逻辑
- **user**: 用户管理、角色、权限
- **file**: 文件管理、文件库、文件空间、文件分享
- **sliding**: 滑块验证组件

每个业务模块遵循相同的分层结构：controller → facade → service → repository → mapper。