# 接口自动化测试项目 (interface-autotest)

## 项目简介
##### interface-autotest项目是一个基于Java的接口自动化测试框架，主要用于测试系统中的各种API接口。项目使用TestNG作为测试框架，支持数据驱动测试和测试报告生成，并集成了钉钉报警功能。

## 项目结构
```
interface-autotest/
├── allure-results/          # 测试报告结果目录
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── dddd/
│       │           └── qa/
│       │               └── zybh/
│       │                   ├── ApiTest/                  # 测试用例
│       │                   │   ├── ActivityListTest/     # 活动列表测试
│       │                   │   ├── AiTest/               # AI相关测试
│       │                   │   ├── DestinationTest/      # 目标相关测试
│       │                   │   ├── FuliMallTest/         # 福利商城测试
│       │                   │   ├── FuliOpTest/           # 福利运营平台测试
│       │                   │   ├── FuliSelfSupplierTest/ # 自建供应商平台测试
│       │                   │   ├── ProductTest/          # 产品相关测试
│       │                   │   ├── SettingTest/          # 设置相关测试
│       │                   │   ├── SourceTest/           # 源相关测试
│       │                   │   └── YGPCTest/             # 员工PC端测试
│       │                   ├── Constant/                 # 常量定义
│       │                   │   ├── Common.java           # 通用常量
│       │                   │   ├── Config.java           # 配置常量
│       │                   │   ├── DelayListener.java    # 延迟监听器
│       │                   │   ├── CommonUtil.java       # 通用工具类
│       │                   │   └── JDBCTest.java         # 数据库测试
│       │                   ├── utils/                    # 工具类
│       │                   │   ├── DateUtil.java         # 日期工具
│       │                   │   ├── DingTalkRobotUtil.java # 钉钉机器人工具
│       │                   │   ├── ErrorEnum.java        # 错误枚举
│       │                   │   ├── GetCaseUtil.java      # 用例获取工具
│       │                   │   ├── JdbcUtil.java         # 数据库工具
│       │                   │   ├── LoginResponse.java    # 登录响应模型
│       │                   │   ├── LoginUtil.java        # 登录工具
│       │                   │   ├── OrderData.java        # 订单数据模型
│       │                   │   └── ResponseData.java     # 响应数据模型
│       │                   ├── BaseTest.java             # 测试基类
│       │                   └── Testweeks.java            # 周期性测试
│       └── resources/
│           ├── suite.xml                  # TestNG测试套件配置
│           ├── test.properties            # 测试环境配置
│           ├── pre.properties             # 预发布环境配置
│           ├── prod-huika.properties      # 慧卡生产环境配置
│           ├── prod-fuli.properties       # 福粒生产环境配置
│           ├── dddd/                      # 其他资源目录
│           └── test-dddd/                 # 测试资源目录
├── target/                  # 编译输出目录
└── pom.xml                  # Maven项目配置文件
```

## 主要功能
1. **接口测试自动化**：支持对多种REST API的自动化测试
2. **多环境支持**：支持测试环境、预发布环境和生产环境的配置切换
3. **测试报告**：使用Allure生成详细的测试报告
4. **异常监控**：集成钉钉报警功能，在测试失败时发送通知
5. **数据库交互**：支持与数据库的交互验证
6. **数据驱动**：支持数据驱动的测试方法

## 测试模块
项目覆盖的主要测试模块包括：
- 福利商城接口测试
- 福利运营平台接口测试
- 自建供应商平台接口测试
- 员工PC端接口测试
- 登录认证接口测试
- 产品相关接口测试
- 活动列表接口测试
- 提货券相关接口测试

## 如何使用

### 环境准备
- JDK 1.8+
- Maven 3.6+
- TestNG

### 配置文件
项目使用不同的properties文件来配置不同环境的参数：
- `test.properties`：测试环境配置
- `pre.properties`：预发布环境配置
- `prod-huika.properties`：会卡生产环境配置
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

## 贡献者
- zhangsc

## 注意事项
- 在使用生产环境配置时请谨慎操作，避免影响线上业务
- 运行测试前请确认配置文件中的参数是否正确
- 使用敏感接口测试时，建议使用测试账号和测试数据 
- BatchImportEmployeeJumpLogin、CursorTest、CreateCardsTest、MembershipCardTest使用test环境数据