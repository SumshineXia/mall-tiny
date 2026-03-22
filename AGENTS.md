# mall-tiny 代码库规范

## 项目概述

这是一个基于 Spring Boot + MyBatis Plus 的商城管理系统后端项目，使用 Maven 构建，支持 JWT 认证和 Redis 缓存。

## 构建和测试命令

```bash
# 清理并编译项目
mvn clean compile

# 运行测试（默认跳过测试，需设置 skipTests=false）
mvn test -DskipTests=false

# 运行单个测试类
mvn test -Dtest=ClassName

# 运行单个测试方法
mvn test -Dtest=ClassName#methodName

# 打包项目
mvn clean package

# 跳过测试打包
mvn clean package -DskipTests

# 运行应用
mvn spring-boot:run

# 生成 Docker 镜像
mvn docker:build
```

## 项目结构

```
com.macro.mall.tiny
├── common/           # 通用组件
│   ├── api/         # API 响应封装
│   ├── config/      # 公共配置
│   ├── domain/      # 通用领域对象
│   ├── exception/   # 异常处理
│   └── service/     # 公共服务
├── config/          # Spring 配置类
├── domain/          # 领域模型
├── generator/       # 代码生成器
├── modules/         # 业务模块
│   └── ums/         # 用户管理模块
│       ├── controller/
│       ├── service/
│       ├── model/
│       └── dto/
└── security/        # 安全相关
```

## 代码风格规范

### 命名约定

- **类名**：大驼峰，如 `UmsAdminController`、`CommonResult`
- **方法名**：小驼峰，动词开头，如 `getUserById`、`updatePassword`
- **变量名**：小驼峰，如 `userId`、`userName`
- **常量名**：全大写下划线分隔，如 `MAX_SIZE`
- **包名**：全小写，如 `com.macro.mall.tiny.modules.ums`
- **DTO 后缀**：以 `Param` 或 `Dto` 结尾，如 `UmsAdminParam`、`UpdateAdminPasswordParam`

### 导入规范

- 按以下顺序组织导入：
  1. JDK 标准库（java.*）
  2. 扩展库（javax.*）
  3. 第三方库（cn.*, org.*, com.*）
  4. 项目内部包（com.macro.mall.tiny.*）
- 同一组按字母顺序排列
- 使用 IDE 的自动导入优化功能

### 注解使用

- **Lombok**：优先使用 `@Data`、`@EqualsAndHashCode` 简化代码
- **Controller**：
  - `@Controller` 类级别注解
  - `@RequestMapping` 或 `@GetMapping/@PostMapping` 方法级别
  - `@ResponseBody` 返回 JSON 数据
  - `@Api` 和 `@ApiOperation`（Swagger v2）或 `@Tag`（Swagger v3）
- **Service**：
  - 接口使用 `@Transactional` 标记事务方法
  - 继承 `IService<Entity>`
- **Model**：
  - `@TableName` 指定数据库表名
  - `@TableId` 指定主键
  - `@ApiModel` 和 `@ApiModelProperty`（Swagger）

### Controller 规范

```java
@Controller
@Api(tags = "UmsAdminController")
@RequestMapping("/admin")
public class UmsAdminController {

    @Autowired
    private UmsAdminService adminService;

    @ApiOperation(value = "获取用户信息")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<UmsAdmin> getItem(@PathVariable Long id) {
        UmsAdmin admin = adminService.getById(id);
        return CommonResult.success(admin);
    }
}
```

### Service 规范

```java
public interface UmsAdminService extends IService<UmsAdmin> {

    /**
     * 根据用户名获取后台管理员
     * @param username 用户名
     * @return 后台管理员对象
     */
    UmsAdmin getAdminByUsername(String username);

    /**
     * 注册功能
     * @param umsAdminParam 注册参数
     * @return 注册成功的管理员对象
     */
    UmsAdmin register(UmsAdminParam umsAdminParam);
}
```

### Model 规范

```java
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ums_admin")
@ApiModel(value = "UmsAdmin对象", description = "后台用户表")
public class UmsAdmin implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户名")
    private String username;
}
```

### API 响应规范

所有 API 必须使用 `CommonResult` 包装：

```java
// 成功响应
return CommonResult.success(data);

// 失败响应
return CommonResult.failed("错误信息");

// 验证失败
return CommonResult.validateFailed("验证失败信息");

// 未授权
return CommonResult.unauthorized(null);
```

### 异常处理

- 使用全局异常处理器（在 common/exception 中）
- 抛出业务异常时提供清晰的错误信息
- 返回适当的 HTTP 状态码

### 配置管理

- 配置文件位置：`src/main/resources/application.yml`
- 使用 `@Value` 注入配置值
- 敏感信息使用环境变量或配置中心

### 数据库操作

- 优先使用 MyBatis Plus 的内置方法（如 `getById`、`list`、`page`）
- 复杂查询使用 XML 映射文件或 QueryWrapper
- 使用 `@Transactional` 保证事务一致性

### 注释规范

- 类注释：描述类的功能和作者
- 方法注释：说明功能、参数、返回值
- 复杂逻辑：添加行内注释
- 注释使用中文

### 测试规范

- 测试类放在 `src/test/java` 对应包下
- 测试方法以 `test` 开头或使用 `@Test` 注解
- 测试命名描述测试场景，如 `testGetAdminById_shouldReturnCorrectAdmin`
- 使用 `@SpringBootTest` 集成测试
- Mock 外部依赖使用 `@MockBean`

### 安全规范

- 密码加密存储
- 使用 JWT 进行身份认证
- 敏感操作添加权限校验
- 避免 SQL 注入（使用参数化查询）

## 工具和插件

- **Lombok**：减少样板代码
- **MyBatis Plus Generator**：代码生成
- **Swagger**：API 文档生成
- **Hutool**：Java 工具包
- **Druid**：数据库连接池

## 注意事项

- 默认 `skipTests=true`，测试时需显式设置 `-DskipTests=false`
- Java 版本为 1.8，注意兼容性
- 使用阿里云 Maven 镜像加速依赖下载
- Redis 和 MySQL 需要先启动才能运行完整功能
