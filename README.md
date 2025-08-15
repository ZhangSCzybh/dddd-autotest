# 接口自动化测试项目 (interface-autotest)

## 项目简介
##### interface-autotest项目是一个基于Java的接口自动化测试框架，主要用于测试系统中的各种API接口。项目使用TestNG作为测试框架，支持数据驱动测试和测试报告生成，并集成了钉钉报警功能。SendMessageTools 是一个用于自动化测试结果通知的工具，主要功能是从测试报告中提取测试结果数据，然后将测试结果发送到钉钉群，实现测试结果的实时通知。该工具与 Jenkins CI/CD 流程集成，可以在测试执行完成后自动发送通知。

## 主要功能
### interface-autotest
1. **接口测试自动化**：支持对多种REST API的自动化测试
2. **多环境支持**：支持测试环境、预发布环境和生产环境的配置切换
3. **测试报告**：使用Allure生成详细的测试报告
4. **异常监控**：集成钉钉报警功能，在测试失败时发送通知
5. **数据库交互**：支持与数据库的交互验证
6. **数据驱动**：支持数据驱动的测试方法
### SendMessageTools
1. **测试结果提取**：从 Allure 测试报告的 JSON 文件中提取测试结果数据
2. **钉钉通知**：将测试结果发送到指定的钉钉群
3. **结果统计**：统计测试用例的总数、通过数、失败数、跳过数等数据
4. **时间统计**：记录测试执行时间和持续时间
5. **安全签名**：支持钉钉机器人安全签名，确保消息安全发送

## 测试模块
项目覆盖的主要测试模块包括：
- 福利商城接口测试
- 福利运营平台接口测试
- 自建供应商平台接口测试
- 员工PC端接口测试
- 智采企业平台接口测试
- 登录认证接口测试
- 数据相关接口测试
- 提货券相关接口测试
- 活动列表接口测试

## 如何使用

### 环境准备
- JDK 1.8+
- Maven 3.6+
- TestNG
- Jenkins (可选，用于 CI/CD 集成)

### 配置文件
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

### 运行测试
1. 使用Maven命令行运行测试：
```bash
mvn clean test
```

2. 使用TestNG配置文件运行特定测试套件：
```bash
mvn clean test -DsuiteXmlFile=src/main/resources/suite.xml
```

3. 生成Allure报告：
```bash
mvn allure:report
```

### 测试报告
测试执行完成后，Allure报告将生成在`target/site/allure-maven-plugin`目录下。打开`index.html`文件可查看详细报告。

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


