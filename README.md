# 接口自动化测试项目 (interface-autotest)

## 项目简介

interface-autotest项目是一个基于Java的接口自动化测试框架，主要用于测试系统中的各种API接口。项目使用TestNG作为测试框架，支持数据驱动测试和测试报告生成，并集成了钉钉报警功能。

SendMessageTools 是一个用于自动化测试结果通知的工具，主要功能是从测试报告中提取测试结果数据，然后将测试结果发送到钉钉群，实现测试结果的实时通知。该工具与 Jenkins CI/CD 流程集成，可以在测试执行完成后自动发送通知。

## 项目结构

```
dddd-autotest/
├── interface-autotest/           # 接口自动化测试主模块
│   ├── src/
│   │   └── main/
│   │       ├── java/com/dddd/qa/zybh/
│   │       │   ├── ApiTest/      # 各业务模块测试用例
│   │       │   ├── Constant/     # 全局常量和配置
│   │       │   ├── utils/        # 工具类
│   │       │   └── others/       # 其他辅助类
│   │       └── resources/        # 配置文件和测试数据
│   ├── target/                   # 编译输出目录
│   ├── pom.xml                   # Maven项目配置文件
│   └── suite.xml                 # TestNG测试套件配置
├── SendMessageTools/             # 测试结果通知工具
│   ├── src/
│   ├── target/
│   ├── pom.xml
│   └── README.md
├── test-output/                  # TestNG测试报告
└── pom.xml                       # 根项目配置文件
```

## 主要功能

### interface-autotest
1. **接口测试自动化**：支持对多种REST API的自动化测试
2. **多环境支持**：支持测试环境、预发布环境和生产环境的配置切换
3. **测试报告**：使用Allure生成详细的测试报告
4. **异常监控**：集成钉钉报警功能，在测试失败时发送通知
5. **数据库交互**：支持与数据库的交互验证
6. **数据驱动**：支持数据驱动的测试方法
7. **模块化测试**：按照业务模块划分测试用例，便于维护和执行

### SendMessageTools
1. **测试结果提取**：从 Allure 测试报告的 JSON 文件中提取测试结果数据
2. **钉钉通知**：将测试结果发送到指定的钉钉群
3. **结果统计**：统计测试用例的总数、通过数、失败数、跳过数等数据
4. **时间统计**：记录测试执行时间和持续时间
5. **安全签名**：支持钉钉机器人安全签名，确保消息安全发送

## 测试模块

项目覆盖的主要测试模块包括：

### 福利商城接口测试 (FuliMallTest)
- 商品购买流程测试
- 退款流程测试
- 线下收款API测试
- 员工提货券测试

### 福利运营平台接口测试 (FuliOpTest)
- 品牌审核管理
- 商品价格策略管理
- 退款管理
- 提货券管理

### 自建供应商平台接口测试 (FuliSelfSupplierTest)
- 供应商申请审批
- 商品管理（上下架、价格调整、库存调整）
- 订单管理

### 员工PC端接口测试 (YGPCTest)
- 审批流程测试
- 积分发放测试

### 智采企业平台接口测试 (SettingTest)
- 企业员工登录测试
- 积分管理测试

### 其他测试模块
- 活动列表接口测试
- 数据同步测试
- 第三方接口测试

## 技术栈

- **编程语言**: Java 8
- **测试框架**: TestNG
- **构建工具**: Maven
- **HTTP客户端**: Hutool HTTP工具
- **JSON处理**: FastJSON, Hutool JSON
- **测试报告**: Allure
- **持续集成**: Jenkins
- **通知服务**: 钉钉机器人

## 环境准备

- JDK 1.8+
- Maven 3.6+
- TestNG
- Jenkins (可选，用于 CI/CD 集成)

## 配置文件

项目使用不同的properties文件来配置不同环境的参数：
- `test.properties`：测试环境配置
- `pre.properties`：预发布环境配置
- `prod-huika.properties`：慧卡生产环境配置
- `prod-fuli.properties`：福利生产环境配置

要切换环境，需要修改`Common.java`中的配置文件路径：
```java
InputStream in = Common.class.getClassLoader().getResourceAsStream("prod-huika.properties");
// 切换到测试环境
// InputStream in = Common.class.getClassLoader().getResourceAsStream("test.properties");
```

## 运行测试

### Maven命令行运行
1. 运行所有测试：
```bash
mvn clean test
```

2. 运行特定测试套件：
```bash
mvn clean test -DsuiteXmlFile=src/main/resources/suite.xml
```

3. 运行特定测试类：
```bash
mvn clean test -Dtest=com.dddd.qa.zybh.ApiTest.FuliMallTest.RefundTest
```

### IDE运行
可以直接在IDE中运行测试类或测试方法，支持调试模式。

### 测试套件配置
项目通过`suite.xml`文件配置测试套件，可以根据需要启用或禁用特定测试模块。

## 测试报告

### Allure报告
测试执行完成后，Allure报告将生成在`target/site/allure-maven-plugin`目录下。打开`index.html`文件可查看详细报告。

### TestNG报告
TestNG原生报告生成在`test-output`目录下，包含HTML格式的详细测试执行结果。

## 钉钉报警配置

在`Common.java`中配置钉钉机器人webhook URL：
```java
public static String privateDingtalkUrl = STATIC_PROPERTIES.getProperty("private.dingtalk.alert.url");
```

### 数据提取
工具会从以下路径的 JSON 文件中提取测试结果数据：
- summary.json：包含测试用例的统计信息（总数、通过数、失败数等）
- executor.json：包含构建信息（构建名称、URL、报告链接等）

### 消息内容
发送到钉钉的消息包含以下信息：
- 测试开始时间
- 项目名称
- 测试用例统计（总数、通过数、失败数、异常数、跳过数）
- 测试总耗时
- Jenkins 构建链接
- Allure 报告链接

### 钉钉机器人集成
工具使用钉钉机器人的 Webhook 接口发送消息，支持：
- 安全签名（使用 HmacSHA256 算法）
- @指定人员或@所有人
- 自定义消息内容和格式

## Jenkins集成流程

1. 从代码仓库拉取最新代码
2. 执行Maven构建和测试命令
3. 生成Allure测试报告
4. 运行SendMessageTools发送测试结果通知
5. 归档测试报告和构建产物

## 开发规范

### 测试类命名规范
- 测试类名以`Test`结尾
- 包名按照业务模块划分，如`FuliMallTest`、`FuliOpTest`等

### 测试方法命名规范
- 使用有意义的方法名描述测试场景
- 使用`@Test`注解的`description`属性详细描述测试场景

### 常量和配置
- 全局常量统一在`Constant`包下管理
- 环境配置通过properties文件管理
- URL路径和接口地址统一配置

### 日志记录
- 使用SLF4J记录测试执行过程
- 关键步骤和结果必须记录日志
- 错误信息需详细记录便于排查

## 注意事项

- 在使用生产环境配置时请谨慎操作，避免影响线上业务
- 运行测试前请确认配置文件中的参数是否正确
- 使用敏感接口测试时，建议使用测试账号和测试数据 
- 确保 Jenkins 有权限访问指定的文件路径
- 确保钉钉机器人的 token 和 secret 配置正确
- 注意钉钉机器人的消息发送频率限制
- 打包前请更新配置中的文件路径为实际路径

## 贡献者

- 张时超(zaiyebuhui)


