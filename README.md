# DDDD-Autotest

## 一、interface-autotest：api接口自动化测试项目

### 1、Common-->修改配置文件
#### 1.1 test.properties 智采test-福粒pre环境配置
#### 1.2 pre.properties 智采pre-慧卡dev环境配置
#### 1.3 prod-huika.properties 慧卡生产环境配置
#### 1.4 prod-fuli.properties 福粒生产环境配置

### 2、AddProductTest：新建商品、spu上架、sku开启销售
#### 2.1 supplierData：商品数量、供应商id、供应商名称
#### 2.2 addProduct：创建商品
#### 2.3 updateState：批量上架spu/开启销售sku

### 3、PurchaseProducts：生产环境商品下单、供应商发货（仅支持prod-huika）
#### 3.1 selectArrayByDay：根据星期选择不同的sku和json文件
#### 3.2 staffFuliTokenFromCSV：商品下单：账号编号、token、地址id配置
#### 3.3 supplierTokenFromCSV：自建供应商发货：token参数
#### 3.4 purchaseGoods：商品下单
#### 3.5 supplierOrderDelivery：自建供应商订单发货
#### 3.6 sendEmployeePoints：智采员工发放积分


## 二、ui-autotest：ui自动化测试项目   //todo
## 三、SendMessageTools：发送测试结果到钉钉群的工具

## 四、AI编译器：cursor编译器，集成claude-3.7-sonnet模型
### 1、CursorTest：AI生成接口测试用例。智采运营平台pre环境-登录接口
#### 1.1 testAdminLogin：系统用户登录测试
#### 1.2 testWrongPassword：错误密码登录测试
#### 1.3 testEmptyParams：空参数登录测试
#### 1.4 testSpecialCharParams：特殊字符参数登录测试
#### 1.5 testMissingParams：缺少必要参数登录测试