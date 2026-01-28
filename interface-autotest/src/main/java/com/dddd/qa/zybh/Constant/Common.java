package com.dddd.qa.zybh.Constant;

import org.checkerframework.checker.units.qual.C;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;


public class Common {
    private static final Properties STATIC_PROPERTIES = new Properties();

    static {
        try {
            //InputStream in = Common.class.getClassLoader().getResourceAsStream("pre-dev.properties");
            InputStream in = Common.class.getClassLoader().getResourceAsStream("prod-huika.properties");
            //字节流是无法读取中文的，否则会乱码。所以采取reader把inputStream转换成reader用字符流来读取中文
            BufferedReader bf = new BufferedReader(new InputStreamReader(in));
            if (null != bf) {
                STATIC_PROPERTIES.load(bf);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }



    //不同环境切换**** 修改15行的配置文件
    public static String profiesEnv = STATIC_PROPERTIES.getProperty("profiles.active");

    //钉钉机器人
    public static String privateDingtalkUrl = STATIC_PROPERTIES.getProperty("private.dingtalk.alert.url");

    //jenkins绝对路径
    public static String jenkinsUrl = STATIC_PROPERTIES.getProperty("jenkins.url");
    //数据库地址
    public static String SqlUrl = STATIC_PROPERTIES.getProperty("url");

    //智采企业hr
    public static String zhicaiHrUrl = STATIC_PROPERTIES.getProperty("zhicai.hr.url");
    //商城 MallUrl
    public static String MallUrl = STATIC_PROPERTIES.getProperty("mall.url");
    //供应商平台
    public static String SupplierUrl = STATIC_PROPERTIES.getProperty("supplier.url");
    //福粒运营平台 OpUrl
    public static String OpUrl = STATIC_PROPERTIES.getProperty("op.url");
    //智采员工平台
    public static String zhicaiYgUrl = STATIC_PROPERTIES.getProperty("zhicai.yg.url");
    //自建供应商平台
    public static String SelfsupplierUrl = STATIC_PROPERTIES.getProperty("selfsupplier.url");
    //慧卡福粒Hr平台
    public static String HrUrl = STATIC_PROPERTIES.getProperty("hr.url");
    //openapi url
    public static String OpenapiUrl = STATIC_PROPERTIES.getProperty("openapi.url");


    public static String loginOPUri = STATIC_PROPERTIES.getProperty("login.op.uri");
    public static String loginDDingDDangUri = STATIC_PROPERTIES.getProperty("login.ddingddang.uri");
    public static String loginDDingDDangYGPCUri = STATIC_PROPERTIES.getProperty("login.ddingddang.ygpc.uri");
    public static String loginDDingDDangInfo = STATIC_PROPERTIES.getProperty("login.DDingDDang.info");
    public static String loginDDingDDangYGPCInfo = STATIC_PROPERTIES.getProperty("login.DDingDDang.ygpc.info");
    public static String loginOPInfo = STATIC_PROPERTIES.getProperty("login.op.info");
    public static String loginSupplierInfo = STATIC_PROPERTIES.getProperty("login.supplier.info");
    public static String loginHrInfo = STATIC_PROPERTIES.getProperty("login.hr.info");
    public static String loginHrUri = STATIC_PROPERTIES.getProperty("login.hr.uri");


    //员工pc账号密码
    public static String loginDDingDDangYGPCInfo1 = STATIC_PROPERTIES.getProperty("login.DDingDDang.ygpc.info1");
    public static String loginDDingDDangYGPCInfo2 = STATIC_PROPERTIES.getProperty("login.DDingDDang.ygpc.info2");
    public static String loginDDingDDangYGPCInfo3 = STATIC_PROPERTIES.getProperty("login.DDingDDang.ygpc.info3");
    public static String loginDDingDDangYGPCInfo4 = STATIC_PROPERTIES.getProperty("login.DDingDDang.ygpc.info4");
    public static String loginDDingDDangYGPCInfo5 = STATIC_PROPERTIES.getProperty("login.DDingDDang.ygpc.info5");


    //自建供应商平台账号密码
    public static String loginSelfSupplierInfo1 = STATIC_PROPERTIES.getProperty("login.Selfsupplier.info1");
    public static String loginSelfSupplierInfo2 = STATIC_PROPERTIES.getProperty("login.Selfsupplier.info2");
    public static String loginSelfSupplierInfo3 = STATIC_PROPERTIES.getProperty("login.Selfsupplier.info3");
    public static String loginSelfSupplierInfo4 = STATIC_PROPERTIES.getProperty("login.Selfsupplier.info4");


    public static String sendEmployeePointsUri = STATIC_PROPERTIES.getProperty("send.employee.points.uri");

    public static String addCartUri = STATIC_PROPERTIES.getProperty("addcart.uri");
    public static String submitOrderUri = STATIC_PROPERTIES.getProperty("submit.order.uri");
    public static String comfirmOrderUri = STATIC_PROPERTIES.getProperty("confirm.order.uri");
    public static String supplierOrderUri = STATIC_PROPERTIES.getProperty("supplier.order.uri");
    public static String supplierOrderShipUri = STATIC_PROPERTIES.getProperty("supplier.order.ship.uri");

    public static String fuliOpAddProductUri = STATIC_PROPERTIES.getProperty("fuli.op.addproduct.uri");
    public static String fuliOpSkuListUri = STATIC_PROPERTIES.getProperty("fuli.op.skulist.uri");
    public static String fuliOpUpdateSpuStateUri = STATIC_PROPERTIES.getProperty("fuli.op.updateSpuState.uri");
    public static String fuliOpUpdateSkuStateUri = STATIC_PROPERTIES.getProperty("fuli.op.updateSkuState.uri");

    public static String checkMallEmployeePointsUri = STATIC_PROPERTIES.getProperty("check.mall.employee.points.uri");
    public static String checkZcEmployeePointsUri = STATIC_PROPERTIES.getProperty("check.zc.employee.points.uri");
    public static String checkZcPCEmployeePointsUri = STATIC_PROPERTIES.getProperty("check.zc.ygpc.employee.points.uri");


    //用来存储cookies的变量
    public static String Cookies;
    public static String DDingDDangToken;
    public static String DDingDDangPCToken;//智采员工pc
    public static String fuliOperationPlatformToken; //福利运营
    public static String SelfsupplierToken;//自建供应商平台
    public static String supplierToken;//供应商平台
    public static String FuliHrToken;//福粒Hr平台
    public static String zhicaiHrToken  = STATIC_PROPERTIES.getProperty("zhicai.hr.token"); //不需要，从员工pc获取了

    public static String mallToken; //商城再也不会17858803001员工token
    public static String jumpMallToken1;//智采员工pc跳商城
    public static String jumpMallToken2;//智采员工pc跳商城
    public static String jumpMallToken3;//智采员工pc跳商城
    public static String jumpMallToken4;//智采员工pc跳商城
    public static String jumpMallToken5;//智采员工pc跳商城

    public static String SelfsupplierToken1;//自建供应商平台
    public static String SelfsupplierToken2;//自建供应商平台
    public static String SelfsupplierToken3;//自建供应商平台
    public static String SelfsupplierToken4;//自建供应商平台


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static String applySelfSupplierInfo = STATIC_PROPERTIES.getProperty("apply.selfsupplier.info");
    public static String exchangeVoucherCard = STATIC_PROPERTIES.getProperty("exchange.voucher.card");
    public static String createVoucherInfo = STATIC_PROPERTIES.getProperty("create.voucher.info");
    public static String ORDER_PROD_DETAILS = STATIC_PROPERTIES.getProperty("refund.order.detail.info");
    public static String sgnetSyncSkuInfo = STATIC_PROPERTIES.getProperty("sgnet.sync.sku.info");
    public static String mallAddressInfo = STATIC_PROPERTIES.getProperty("mall.address.info");
    public static String mallTokenInfo = STATIC_PROPERTIES.getProperty("mall.token.info");
    public static String ygpcEmployeePointsParameters = STATIC_PROPERTIES.getProperty("ygpc.employee.points.parameters");


    public static String agentId = STATIC_PROPERTIES.getProperty("agent.id");
    public static String enterprId = STATIC_PROPERTIES.getProperty("enterpr.id");
    public static String relatedEnterprId = STATIC_PROPERTIES.getProperty("relatedEnterpr.id");
    public static Integer employeeId = Integer.valueOf(STATIC_PROPERTIES.getProperty("employee.id"));
    public static Integer enterprId2 = Integer.valueOf(STATIC_PROPERTIES.getProperty("enterpr.id"));

    public static String USER_RECEIVE_ADDR_ID = STATIC_PROPERTIES.getProperty("address.id");
    public static String FULI_PLATFORM_SKU = STATIC_PROPERTIES.getProperty("skucode");
    public static String SKU_ID = STATIC_PROPERTIES.getProperty("sku.update.id");
    public static String OP_SKU_ID = STATIC_PROPERTIES.getProperty("op.sku.update.id");
    public static Integer supplierId = Integer.valueOf(STATIC_PROPERTIES.getProperty("supplier.id"));

    public static String cursorTestLoginName = STATIC_PROPERTIES.getProperty("cursor.loginname");
    public static String cursorTestPassword = STATIC_PROPERTIES.getProperty("cursor.password");

    public static String picListInfo = STATIC_PROPERTIES.getProperty("pic.list.info");//福利dev虞信品的账号，电子钱包线下收银


    public static String loginMallInfo1 = STATIC_PROPERTIES.getProperty("login.mall.info1");//福利dev虞信品的账号，电子钱包线下收银




    //创建商品
    public static String brandId = STATIC_PROPERTIES.getProperty("goods.brandid");
    public static String brandName = STATIC_PROPERTIES.getProperty("goods.brandname");
    public static String categoryId = STATIC_PROPERTIES.getProperty("goods.categoryid");
    public static String categoryId2 = STATIC_PROPERTIES.getProperty("goods.categoryid2");
    public static String categoryId3 = STATIC_PROPERTIES.getProperty("goods.categoryid3");

    //下单--生产环境sku列表
    public static String[] array1 = {"9147169", "9147305", "9147417", "9147178","9147190","9147226","9147249","9147260","9147176","9147339","9147175","9147276","9147285","9147289","9147293"};
    public static String[] array2 = {"9147167", "9147172", "9147194", "9147221","9147420","9147423","9147173","9147205","9147224","9147232","9147251","9147254","9147211","9147346","9147354","9147268","9147283","9147300"};
    public static String[] array3 = {"9147309", "9147239", "9147343", "9147346","9147268","9147274","9147280","9147301","9147304"};
    public static String[] array4 = {"9147325", "9147417", "9147418", "9147422","9147424","9147253","9147254","9147255","9147257","9147262","9147357","9147292","9147295","9147298","9147302"};
    public static String[] array5 = {"9147305", "9147309", "9147419", "9147215","9147240","9147248","9147347","9147357","9147269","9147277","9147297"};

    public static String MonOrderDetailInfo = STATIC_PROPERTIES.getProperty("mon.order.detail.info");
    public static String TueOrderDetailInfo = STATIC_PROPERTIES.getProperty("tue.order.detail.info");
    public static String WedOrderDetailInfo = STATIC_PROPERTIES.getProperty("wed.order.detail.info");
    public static String ThuOrderDetailInfo = STATIC_PROPERTIES.getProperty("thu.order.detail.info");
    public static String FriOrderDetailInfo = STATIC_PROPERTIES.getProperty("fri.order.detail.info");

    public static String addressIdInfo1 = STATIC_PROPERTIES.getProperty("address.id.info1");
    public static String addressIdInfo2 = STATIC_PROPERTIES.getProperty("address.id.info2");
    public static String addressIdInfo3 = STATIC_PROPERTIES.getProperty("address.id.info3");
    public static String addressIdInfo4 = STATIC_PROPERTIES.getProperty("address.id.info4");
    public static String addressIdInfo5 = STATIC_PROPERTIES.getProperty("address.id.info5");

    public static String employeeIdInfo1 = STATIC_PROPERTIES.getProperty("employee.id.info1");
    public static String employeeIdInfo2 = STATIC_PROPERTIES.getProperty("employee.id.info2");
    public static String employeeIdInfo3 = STATIC_PROPERTIES.getProperty("employee.id.info3");
    public static String employeeIdInfo4 = STATIC_PROPERTIES.getProperty("employee.id.info4");
    public static String employeeIdInfo5 = STATIC_PROPERTIES.getProperty("employee.id.info5");

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //运营平台提货券模块
    public static String addVoucherUri = STATIC_PROPERTIES.getProperty("add.voucher.uri");
    public static String voucherlistUri = STATIC_PROPERTIES.getProperty("add.voucher.uri");
    public static String salesVoucherUri = STATIC_PROPERTIES.getProperty("sales.voucher.uri");
    public static String grantVoucherUri = STATIC_PROPERTIES.getProperty("grant.voucher.uri");
    //商城端提货券模块
    public static String vouchersCardListUri = STATIC_PROPERTIES.getProperty("vouchers.cardList.uri");
    public static String vouchersOrdersubmitUri = STATIC_PROPERTIES.getProperty("vouchers.ordersubmit.uri");



    //自建供应商平台
    public static String enterpriseSelfsupplierCodeuri = STATIC_PROPERTIES.getProperty("enterprise.selfsupplier.code.uri");
    public static String enterpriseSelfsupplierLoginuri = STATIC_PROPERTIES.getProperty("enterprise.selfsupplier.login.uri");
    public static String applySelfSupplierUri = STATIC_PROPERTIES.getProperty("apply.selfsupplier.uri");
    public static String supplierRegisterApplyUri = STATIC_PROPERTIES.getProperty("supplier.register.apply.uri");
    public static String supplierRegisterSelectlistUri = STATIC_PROPERTIES.getProperty("supplier.register.selectlist.uri");
    public static String supplierlistUri = STATIC_PROPERTIES.getProperty("supplier.list.uri");
    public static String supplierdelSupplierInfoUri = STATIC_PROPERTIES.getProperty("supplier.delsupplierinfo.uri");
    public static String queryNoOrderSkuPagesUri = STATIC_PROPERTIES.getProperty("query.no.order.sku.pages.uri");
    public static String supplierSkuUpdatePriceUri = STATIC_PROPERTIES.getProperty("supplier.sku.updateprice.uri");


    //员工pc--我的审批--列表
    public static String approvalPendingUri = STATIC_PROPERTIES.getProperty("approval.pending.uri");
    public static String approvalProcessHandleUri = STATIC_PROPERTIES.getProperty("approval.processhandle.uri");

    //员工pc--跳转登录
    public static String openMallCodeUri = STATIC_PROPERTIES.getProperty("open.mall.code.uri");
    public static String jumpMallLoginUri = STATIC_PROPERTIES.getProperty("jump.mall.login.uri");


    //商品下单--售后
    public static String mallOrderListUri = STATIC_PROPERTIES.getProperty("mall.order.list.uri");
    public static String mallOrderRefundApplyUri = STATIC_PROPERTIES.getProperty("mall.order.refund.apply.uri");
    public static String opAdminRefundListUri = STATIC_PROPERTIES.getProperty("op.admin.refund.list.uri");
    public static String opAdminRefundAuditUri = STATIC_PROPERTIES.getProperty("op.admin.refund.audit.uri");

    //运营平台--商品价格策略
    public static String opSkuUpdatePriceUri = STATIC_PROPERTIES.getProperty("op.sku.updateprice.uri");

    //商城--线下收银api
    public static String mallGetCreditUri = STATIC_PROPERTIES.getProperty("mall.get.credit.uri");
    public static String counterpaymentApiUrl = STATIC_PROPERTIES.getProperty("counterpayment.api.url");
    public static String counterpaymentClientId = STATIC_PROPERTIES.getProperty("counterpayment.client_id");
    public static String counterpaymentClientSecret = STATIC_PROPERTIES.getProperty("counterpayment.client_secret");
    public static String getCodeInfoUri = STATIC_PROPERTIES.getProperty("get.code.info.uri");



    //品牌管理
    public static String supplierBrandAddUri = STATIC_PROPERTIES.getProperty("supplier.brandreview.add.uri");
    public static String SupplierBrandreviewListUri = STATIC_PROPERTIES.getProperty("supplier.brandreview.list.uri");
    public static String SupplierBrandreviewUpdateUri = STATIC_PROPERTIES.getProperty("supplier.brandreview.update.uri");

    public static String opAdminBrandreviewListUri = STATIC_PROPERTIES.getProperty("op.admin.brandreview.list.uri");
    public static String opAdminBrandreviewUpdateStatusUri = STATIC_PROPERTIES.getProperty("op.admin.brandreview.updatestatus.uri");
    public static String opAdminBrandListUri = STATIC_PROPERTIES.getProperty("op.admin.brand.list.uri");
    public static String opAdminBrandDelUri = STATIC_PROPERTIES.getProperty("op.admin.brand.del.uri");

    //供应商平台登录
    public static String supplierLoginUri = STATIC_PROPERTIES.getProperty("supplier.login.uri");
    //平台供应商--对账管理
    public static String supplierStatementCreateWaitStatementUri = STATIC_PROPERTIES.getProperty("supplier.statement.createWaitStatement.uri");
    public static String supplierStatementGetStatementAccountDetailUri = STATIC_PROPERTIES.getProperty("supplier.statement.getStatementAccountDetail.uri");
    public static String supplierStatementListUri = STATIC_PROPERTIES.getProperty("supplier.statement.list.uri");
    public static String supplierStatementInfoUri = STATIC_PROPERTIES.getProperty("supplier.statement.info.uri");
    public static String supplierStatementUpdateStatusUri = STATIC_PROPERTIES.getProperty("supplier.statement.updateStatus.uri");
    //运营平台--对账管理（供应商）
    public static String opAdminStatementInfoUri = STATIC_PROPERTIES.getProperty("op.admin.statement.info.uri");
    public static String opAdminStatementUpdateStatusUri = STATIC_PROPERTIES.getProperty("op.admin.statement.updateStatus.uri");























    /**********************************************************作废***********************************************************/

    //yolocast
    public static String yolocastUrl = STATIC_PROPERTIES.getProperty("yolocast.url");
    public static String loginYolocastEmail = STATIC_PROPERTIES.getProperty("login.Yolocast.email");
    public static String loginYolocastPassword = STATIC_PROPERTIES.getProperty("login.Yolocast.password");
    public static String loginUri = STATIC_PROPERTIES.getProperty("login.uri");
    public static String scheduleEventsListuri = STATIC_PROPERTIES.getProperty("schedule.events.List.uri");
    public static String sourceListuri = STATIC_PROPERTIES.getProperty("source.uri");
    public static String sourceResetKeyuri = STATIC_PROPERTIES.getProperty("source.reset.key.uri");
    public static String activityCreateuri = STATIC_PROPERTIES.getProperty("create.activity.uri");
    //test环境数据库
    public static String driver = STATIC_PROPERTIES.getProperty("driver");
    public static String username = STATIC_PROPERTIES.getProperty("username");
    public static String password = STATIC_PROPERTIES.getProperty("password");
    public static String yololivUrl = STATIC_PROPERTIES.getProperty("url");
    public static String activityUrl = STATIC_PROPERTIES.getProperty("activity.url");
    public static String streamUrl = STATIC_PROPERTIES.getProperty("stream.url");
    public static String usercenterUrl = STATIC_PROPERTIES.getProperty("usercenter.url");


}
