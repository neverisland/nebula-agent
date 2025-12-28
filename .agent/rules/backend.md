---
trigger: always_on
---

---
description: 后端 Java Spring Boot 开发规范与最佳实践 (详细版)
globs: backend/**/*.java
---

# 后端开发规范 (Backend Rules)

## 1. 全局目录说明 (Global Directories)
- **`backend/`**: 后端 Java 项目根目录。
- **`frontend/`**: 前端 Vue 项目根目录。
- **`data/`**: 数据库脚本目录。
    - **规则**: 所有的数据库建表语句 (`.sql`) 必须存放在此目录。如果涉及数据库表结构的变更（新增表、修改字段），**必须**同步更新此目录下的 SQL 文件，保持与生产环境一致。
- **`docs/api/`**: API 接口文档目录。

## 2. 技术栈 (Tech Stack)
- **语言**: Java 21
- **框架**: Spring Boot 3.5.7
- **ORM**: MyBatis（非 MyBatis-Plus）
- **权限**: Sa-Token
- **工具**: Lombok, Hutool
- **Maven 本地仓库**: `D:\Maven-Repository`
    - 编译时需指定：`mvn clean compile -Dmaven.repo.local=D:\Maven-Repository -DskipTests`

## 3. 架构分层与职责 (Architecture & Responsibilities)
遵循 DDD (领域驱动设计) 思想与 MVC 模式结合的分层架构，调用链路如下：
`Controller` -> `Facade` -> `Service` -> `Repository` -> `Mapper` -> `Database`

### 3.1 Controller 层 (接口层)
- **位置**: `cn.yang.nebula.agent.business.*.controller`
- **职责**:
    - 接收 HTTP 请求，定义 URL 路径 (`@RequestMapping`)。
    - 参数校验 (`@Validated`, `@RequestBody`, `@RequestParam`)。
    - 调用 `Facade` 层接口处理业务。
    - 封装并返回统一响应对象 `ResultVo<T>`。
    - **禁止**: 编写复杂业务逻辑，禁止直接调用 `Repository` 或 `Mapper`。

### 3.2 Facade 层 (门面层)
- **位置**: `cn.yang.nebula.agent.business.*.facade`
- **职责**:
    - 定义业务接口 (`interface`)。
    - 对外暴露业务能力，屏蔽内部实现细节。
    - 编排多个 Service 的原子能力（如果需要）。

### 3.3 Service 层 (业务层)
- **位置**: `cn.yang.nebula.agent.business.*.service`
- **职责**:
    - 实现 `Facade` 接口。
    - 核心业务逻辑实现。
    - 事务控制 (`@Transactional`)。
    - 调用 `Repository` 进行数据持久化。
    - 抛出业务异常 (`BusinessException`)。

### 3.4 Repository 层 (仓储层)
- **位置**: `cn.yang.nebula.agent.business.*.repository`
- **职责**:
    - 封装数据访问细节。
    - 调用 `Mapper` 或直接操作数据库。
    - 处理数据缓存 (Redis)。

### 3.5 Entity / DO (领域对象)
- **位置**: `cn.yang.nebula.agent.business.*.dal` (或 `entity`)
- **规范**:
    - 必须继承 `BaseEntity`。
    - 使用 Lombok (`@Data`)。
    - 实现 `Serializable` 接口。
    - **严禁**: 手动设置 `createTime` 和 `updateTime`，由数据库默认值管理。

## 4. 编码规范 (Coding Standards)

### 4.1 命名规范
- **类名**: PascalCase (e.g., `UserController`).
- **方法名**: camelCase (e.g., `getUserById`).
- **接口实现**: `Service` 实现 `Facade` 接口。
- **入参对象**: `XxxxDto`（Parameter Object），用于 Controller 入参/Service 调用入参。
- **出参对象**: `XxxxVo`（Response Object），用于 Controller 出参/Service 返回对象。
- **前后端统一**: 前端 TypeScript 类型定义也应遵循 `Dto`/`Vo` 结尾的入参/出参命名，便于对齐契约。

### 4.2 注释规范 (Mandatory)
- **强制规则**: 所有类、接口、方法必须包含 Javadoc 注释。
- **类注释示例**:
    ```java
    /**
     * 用户业务实现类
     * 负责处理用户相关的核心业务逻辑，如注册、登录、信息修改等
     *
     * @author 未见清海
     */
    @Service
    public class UserService implements UserFacade { ... }
    ```
- **方法注释示例**:
    ```java
    /**
     * 分页查询用户列表
     * 根据传入的查询条件（如用户名、手机号）进行模糊查询，并支持分页
     *
     * @param query 分页查询对象，包含页码、页大小及过滤条件
     * @return 分页结果对象，包含当前页数据列表及总记录数
     * @throws BusinessException 当查询参数为空时抛出
     */
    @Override
    public PageResult<UserPageDto> pageData(UserPageQuery query) { ... }
    ```

### 4.3 数据持久层框架规范 (MyBatis)
- **使用 MyBatis**（不是 MyBatis-Plus）
- **所有 Mapper 接口必须配套创建 XML 映射文件**
    - Mapper 接口位置: `cn.yang.nebula.agent.business.*.mapper`
    - XML 文件位置: `backend/src/main/resources/mapper/*/*.xml`
- **禁止使用 MyBatis-Plus API**:
    - ❌ 禁止: `LambdaQueryWrapper`, `BaseMapper`, `selectList()`, `selectOne()`
    - ✅ 正确: 在 Mapper 接口中定义方法，在 XML 中编写 SQL
- **示例**:
    ```java
    // Mapper 接口
    @Mapper
    public interface UserRoleMapper {
        List<UserRoleDo> selectByUserId(@Param("userId") String userId);
        int deleteByUserId(@Param("userId") String userId);
        int insert(UserRoleDo userRoleDo);
        int batchInsert(@Param("list") List<UserRoleDo> list);
    }
    ```
    ```xml
    <!-- UserRoleMapper.xml -->
    <select id="selectByUserId" resultType="...UserRoleDo">
        SELECT id, user_id, role_id
        FROM user_role 
        WHERE user_id = #{userId}
    </select>
    
    <delete id="deleteByUserId">
        DELETE FROM user_role WHERE user_id = #{userId}
    </delete>
    
    <insert id="batchInsert">
        INSERT INTO user_role (id, user_id, role_id)
        VALUES
        <foreach collection="list" item="item" separator=",">
            (#{item.id}, #{item.userId}, #{item.roleId})
        </foreach>
    </insert>
    ```

### 4.4 Import 规范 (强制)
- **禁止使用全限定类名**（如 `java.util.List`）
- **必须在类顶部使用 import 语句引入所有类型**
- **错误示例** ❌:
    ```java
    public class User {
        private java.util.List<String> roles;  // 错误！
        private java.util.Map<String, Object> data;  // 错误！
    }
    ```
- **正确示例** ✅:
    ```java
    import java.util.List;
    import java.util.Map;
    
    public class User {
        private List<String> roles;  // 正确
        private Map<String, Object> data;  // 正确
    }
    ```

### 4.5 异常处理
- 业务逻辑错误抛出 `BusinessException`。
- 必须指定错误码枚举 `ErrorStatusCodeEnum`。

## 5. 常用工具类 (Common Utils)
- **Bean 转换**: `cn.yang.common.data.structure.utils.bean.BeanConvertUtils`
- **判空**: `org.springframework.util.ObjectUtils`, `org.springframework.util.CollectionUtils`
- **集合**: `java.util.List`, `java.util.Map`, `java.util.stream.Collectors`

## 6. 注意事项 (Notes)
1.  **API 文档**: 修改 Controller 后，务必运行 Cursor 生成/更新 `docs/api` 下的文档。
2.  **相对路径**: 引用文件时使用相对路径。
3.  **事务**: 涉及多表修改务必添加 `@Transactional`。
4.  **MyBatis**: 所有数据库操作必须通过 XML 映射实现，禁止使用 MyBatis-Plus。
5.  **编译验证**: 完成代码修改后，**必须**执行编译验证，确保没有语法错误或依赖问题。
    - 使用 IDEA 中的 Build → Build Project 或 Rebuild Project
    - 或使用命令行：`mvn clean compile -Dmaven.repo.local=D:\Maven-Repository -DskipTests`
    - 编译成功后才能提交代码或继续下一步工作
