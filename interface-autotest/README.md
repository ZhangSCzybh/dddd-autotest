# 接口自动化测试项目 (interface-autotest)

## 项目简介
`interface-autotest` 是一个基于 Java 的接口自动化测试框架，主要用于测试叮叮当当（DDingDDang）生态系统的各类 API 接口，包括智采运营平台、福粒商城、员工 PC 端等多个业务线。项目采用 TestNG 作为测试框架，实现了数据驱动测试、多环境配置管理、Allure 报告生成以及钉钉实时报警等功能。

## 项目特性
- ✅ **多业务线覆盖**：涵盖智采运营、福粒商城、员工 PC、供应商平台等多个业务线的接口测试
- 🌐 **多环境支持**：支持测试、预发布、生产等不同环境的无缝切换
- 📊 **可视化报告**：集成 Allure 报告，提供详细的测试结果分析
- 🔔 **实时报警**：集成钉钉机器人，在测试失败时及时通知相关人员
- 🛠️ **数据驱动**：支持通过 JSON、CSV 等格式进行数据驱动测试
- 🗄️ **数据库验证**：提供数据库连接工具，支持后端数据验证

## 项目架构

```
interface-autotest/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── dddd/
│       │           └── qa/
│       │               └── zybh/
│       │                   ├── ApiTest/                  # 各业务线测试用例
│       │                   │   ├── AiTest/               # AI 相关接口测试
│       │                   │   ├── FuliMallTest/         # 福粒商城接口测试
│       │                   │   ├── FuliOpTest/           # 福粒运营平台接口测试
│       │                   │   ├── FuliSelfSupplierTest/ # 自建供应商平台接口测试
│       │                   │   ├── ProductTest/          # 产品相关接口测试
│       │                   │   ├── SettingTest/          # 设置相关接口测试
│       │                   │   ├── SourceTest/           # 源数据接口测试
│       │                   │   └── YGPCTest/             # 员工 PC 端接口测试
│       │                   ├── Constant/                 # 常量及配置管理
│       │                   │   ├── Common.java           # 环境配置加载
│       │                   │   ├── Config.java           # 报警消息模板及业务常量
│       │                   │   └── CommonUtil.java       # 通用工具类
│       │                   ├── utils/                    # 工具类集合
│       │                   │   ├── AccessTokenClient.java # 访问令牌管理
│       │                   │   ├── DateUtil.java         # 日期时间处理
│       │                   │   ├── DingTalkRobotUtil.java # 钉钉机器人工具
│       │                   │   ├── ErrorEnum.java        # 错误码枚举
│       │                   │   ├── GetCaseUtil.java      # 用例获取工具
│       │                   │   ├── JDBCUtil.java         # 数据库连接工具
│       │                   │   ├── LoginUtil.java        # 登录工具类
│       │                   │   ├── OrderData.java        # 订单数据模型
│       │                   │   └── ResponseData.java     # 响应数据模型
│       │                   └── BaseTest.java             # 测试基类
│       └── resources/                                    # 配置文件及测试数据
│           ├── dddd/                                     # 生产环境测试数据
│           ├── pre-dev-dddd/                            # 预发布环境测试数据
│           ├── source/                                  # 源数据文件
│           ├── *.properties                             # 多环境配置文件
│           └── *testng.xml                              # TestNG 测试套件配置
├── target/                                               # Maven 编译输出目录
└── pom.xml                                               # Maven 项目配置文件
```

## 快速开始

### 环境要求
- **JDK**: 1.8 或更高版本
- **Maven**: 3.6 或更高版本
- **Git**: 版本控制工具
- **Allure**: 2.13+ (用于生成测试报告)

### 安装依赖
```bash
# 克隆项目
git clone <repository-url>

# 进入项目目录
cd interface-autotest

# 下载依赖
mvn clean compile
```

### 环境配置
项目支持多环境配置，通过修改 `src/main/java/com/dddd/qa/zybh/Constant/Common.java` 中的配置文件来切换环境：

```java
// 默认使用生产环境配置
InputStream in = Common.class.getClassLoader().getResourceAsStream("prod-huika.properties");
// 切换到测试环境，取消注释下面这行并注释上面一行
// InputStream in = Common.class.getClassLoader().getResourceAsStream("pre-dev.properties");
```

主要配置文件：
- `prod-huika.properties`: 慧卡生产环境
- `pre-dev.properties`: 预发布环境
- `test.properties`: 测试环境

### 运行测试

#### 1. 运行所有测试
```bash
mvn clean test
```

#### 2. 运行特定测试套件
```bash
# 运行生产环境回归测试
mvn clean test -DsuiteXmlFile=src/main/resources/prod_huika_regression_testng.xml

# 运行预发布环境测试
mvn clean test -DsuiteXmlFile=src/main/resources/pre_dev_testng.xml
```

#### 3. 运行单个测试类
```bash
mvn clean test -Dtest=YourTestClass
```

#### 4. 生成 Allure 报告
```bash
# 执行测试
mvn clean test

# 生成报告
mvn allure:serve
# 或者生成静态报告
mvn allure:report
```

## 测试模块详情

### AI测试 (AiTest)
- Cursor自动编译登录接口测试

### 福利HR平台测试 (FuliHrTest)
- 批量新增员工测试
- 跳转登录商城测试

### 福利商城平台测试 (FuliMallTest)
- 线下收银dev测试
- 线下收银prd付款码测试
- 提货券领取流程测试
- 会员卡领取测试
- 商品下单、退换货流程测试

### 福利运营平台测试 (FuliOpTest)
- 品牌审核流程测试
- 会员卡创建与发放流程测试
- 平台供应商商品价格策略测试
- sgnet商品同步测试
- 退换货管理售后流程测试
- 对账单（供应商）驳回流程测试
- 自建供应商商品价格策略测试
- 提货券创建销售流程测试

### 供应商管理平台&供应商平台 (FuliSelfSupplierTest)
- 品牌审核流程测试
- 删除自建供应商平台测试
- 邀请供应商流程测试
- 无交易量商品测试
- 平台供应商发起对账流程测试
- 自建供应商商品价格策略测试
- 自建供应商商品销售状态测试
- 自建供应商商品库存测试


### 员工 PC 端测试 (YGPCTest)
- 员工审批流程测试

### 数据源测试 (SourceTest)
- 其他脚本类处理工具

## 钉钉报警配置

系统集成了钉钉机器人报警功能，可在测试失败时及时通知相关人员：

1. 在钉钉群中添加自定义机器人
2. 获取 webhook URL
3. 在对应环境的 `.properties` 文件中配置：
```properties
private.dingtalk.alert.url=https://oapi.dingtalk.com/robot/send?access_token=your-access-token
```

## 开发规范

### 测试用例编写
- 继承 `BaseTest` 类
- 使用 TestNG 注解 (`@Test`, `@BeforeMethod`, `@AfterMethod`)
- 合理使用数据驱动测试
- 添加必要的断言验证

### 代码提交
- 提交前确保所有测试通过
- 遵循团队代码规范
- 编写清晰的提交信息

## 常见问题

### 1. 如何切换测试环境？
修改 `Common.java` 文件中的配置文件路径，然后重新编译项目。

### 2. 测试报告在哪里查看？
Allure 报告默认生成在 `target/allure-results/` 目录下，可通过 `mvn allure:serve` 命令启动本地服务器查看。

### 3. 如何添加新的测试用例？
- 创建新的测试类，继承 `BaseTest`
- 添加相应的测试方法，使用 `@Test` 注解
- 如果需要新配置项，在 `.properties` 文件中添加
- 更新测试数据文件（如 JSON、CSV）

### 4. 如何处理数据库验证？
使用 `JDBCUtil.java` 提供的工具方法进行数据库连接和查询验证。

### 5. 如何运行特定的测试方法？
可以通过在测试方法上添加 `@Test(groups = "groupName")` 注解来分组测试，然后在 testng.xml 中指定运行特定分组。

### 6. 如何调试测试失败的问题？
- 查看 Allure 报告中的详细日志
- 检查测试执行过程中的 HTTP 请求和响应
- 确认环境配置是否正确
- 检查数据库状态和测试数据

### 7. 如何处理登录认证？
系统提供了 `LoginUtil.java` 工具类来处理不同平台的登录认证，包括智采、福粒、员工PC等平台的登录方式。

### 8. 如何添加自定义断言？
可以在测试方法中使用 TestNG 的断言方法，如 `Assert.assertEquals()`、`Assert.assertTrue()` 等，也可以使用 Hamcrest 匹配器进行更灵活的断言。

### 9. 如何处理异步请求？
对于异步请求，可以使用适当的等待机制，如 Thread.sleep() 或更优雅的显式等待方式，确保请求完成后再进行验证。

## 项目维护

**负责人**: 张时超(zaiyebuhui)

**联系方式**: 
- 钉钉: 17858803001
- 邮箱: [17858803001@163.com]

## 注意事项
- 🔐 生产环境配置包含敏感信息，请妥善保管
- ⚙️ 运行测试前请确认环境配置正确
- ⚠️ 避免在生产环境中执行可能影响业务的测试
- 👤 敏感接口测试请使用专用测试账号
- 📋 部分测试用例（如 BatchImportEmployeeJumpLogin、CursorTest 等）固定使用测试环境数据