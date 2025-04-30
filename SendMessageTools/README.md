# 测试结果通知工具 (SendMessageTools)

## 项目简介
SendMessageTools 是一个用于自动化测试结果通知的工具，主要功能是从测试报告中提取测试结果数据，然后将测试结果发送到钉钉群，实现测试结果的实时通知。该工具与 Jenkins CI/CD 流程集成，可以在测试执行完成后自动发送通知。

## 项目结构
```
SendMessageTools/
├── src/
│   └── main/
│       └── java/
│           └── Dingtalk/
│               └── SendResultToDingtalk.java  # 主要实现类，负责发送测试结果到钉钉
├── META-INF/
├── target/                  # 编译输出目录
└── pom.xml                  # Maven项目配置文件
```

## 主要功能
1. **测试结果提取**：从 Allure 测试报告的 JSON 文件中提取测试结果数据
2. **钉钉通知**：将测试结果发送到指定的钉钉群
3. **结果统计**：统计测试用例的总数、通过数、失败数、跳过数等数据
4. **时间统计**：记录测试执行时间和持续时间
5. **安全签名**：支持钉钉机器人安全签名，确保消息安全发送

## 核心功能说明

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

## 如何使用

### 环境准备
- JDK 1.8+
- Maven 3.6+
- Jenkins (可选，用于 CI/CD 集成)

### 配置
主要配置项在 `SendResultToDingtalk.java` 中：

1. **文件路径配置**
```java
// Allure 报告 JSON 文件路径
public static String pathname1 = "/path/to/allure-report/widgets/summary.json";
public static String pathname2 = "/path/to/allure-results/executor.json";
```

2. **钉钉配置**
```java
// 钉钉机器人 Webhook URL 和加签密钥
static String dingUrl = "https://oapi.dingtalk.com/robot/send?access_token=YOUR_TOKEN";
static String secret = "YOUR_SECRET";
```

### 打包和运行
1. 使用 Maven 打包：
```bash
mvn clean package
```

2. 运行 JAR 文件：
```bash
java -jar target/SendMessageTools-1.0-SNAPSHOT.jar
```

3. Jenkins 集成：
   在 Jenkins 构建后步骤中添加执行 shell/batch 命令：
```bash
java -jar /path/to/SendMessageTools-1.0-SNAPSHOT.jar
```

## Jenkins 集成流程
1. 执行自动化测试 (interface-autotest)
2. 生成 Allure 报告
3. 运行 SendMessageTools 工具提取测试结果
4. 发送通知到钉钉群

## 自定义消息格式
可以在 `SendResultToDingtalk.java` 的 `readSummaryJsonSendDingTalk()` 方法中修改消息内容格式：

```java
String content = title + "\n"
        + "项目名称：" + buildName + "\n"
        + "总用例数量：" + total + "\n"
        + "成功用例数：" + passed + "\n"
        + ...
```

## 贡献者
- zhangsc

## 注意事项
- 确保 Jenkins 有权限访问指定的文件路径
- 确保钉钉机器人的 token 和 secret 配置正确
- 注意钉钉机器人的消息发送频率限制
- 打包前请更新配置中的文件路径为实际路径 