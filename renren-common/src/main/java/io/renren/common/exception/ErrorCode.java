 

package io.renren.common.exception;

/**
 * 错误编码，由5位数字组成，前2位为模块编码，后3位为业务编码
 * <p>
 * 如：10001（10代表系统模块，001代表业务代码）
 * </p>
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
public interface ErrorCode {
    // ==================== HTTP状态码 ====================
    /** 服务器内部错误 */
    int INTERNAL_SERVER_ERROR = 500;
    /** 未授权访问 */
    int UNAUTHORIZED = 401;

    // ==================== 系统基础错误码 (10xxx) ====================
    /** 参数不能为空 */
    int NOT_NULL = 10001;
    /** 数据库中已存在该记录 */
    int DB_RECORD_EXISTS = 10002;
    /** 获取参数失败 */
    int PARAMS_GET_ERROR = 10003;
    /** 账号或密码错误 */
    int ACCOUNT_PASSWORD_ERROR = 10004;
    /** 账号已被停用 */
    int ACCOUNT_DISABLE = 10005;
    /** 唯一标识符不能为空 */
    int IDENTIFIER_NOT_NULL = 10006;
    /** 验证码不正确 */
    int CAPTCHA_ERROR = 10007;
    /** 请先删除子菜单或按钮 */
    int SUB_MENU_EXIST = 10008;
    /** 原密码不正确 */
    int PASSWORD_ERROR = 10009;
    /** 上级部门选择错误 */
    int SUPERIOR_DEPT_ERROR = 10011;
    /** 上级菜单不能为自身 */
    int SUPERIOR_MENU_ERROR = 10012;
    /** 数据权限接口，只能是Map类型参数 */
    int DATA_SCOPE_PARAMS_ERROR = 10013;
    /** 请先删除下级部门 */
    int DEPT_SUB_DELETE_ERROR = 10014;
    /** 请先删除部门下的用户 */
    int DEPT_USER_DELETE_ERROR = 10015;
    /** 请上传文件 */
    int UPLOAD_FILE_EMPTY = 10019;
    /** token不能为空 */
    int TOKEN_NOT_EMPTY = 10020;
    /** token失效，请重新登录 */
    int TOKEN_INVALID = 10021;
    /** 账号已被锁定 */
    int ACCOUNT_LOCK = 10022;
    /** 上传文件失败 */
    int OSS_UPLOAD_FILE_ERROR = 10024;
    /** Redis服务异常 */
    int REDIS_ERROR = 10027;
    /** 定时任务失败 */
    int JOB_ERROR = 10028;
    /** 不能包含非法字符 */
    int INVALID_SYMBOL = 10029;
    /** 该手机号已经注册，请直接登录或使用其他手机号 */
    int PHONE_NUMBER_HAS_BEEN_REGISTERED=10030;
    /** 余额不足，请去充值 */
    int INSUFFICIENT_BALANCE = 10039;
    /** 无效的验证码 */
    int INVALID_CODE=10033;
    
    // ==================== 充值相关错误码 (60xxx) ====================
    /** 充值失败 */
    int CHARGE_FAILED = 60001;
    /** 获取充值订单详情失败 */
    int CHARGE_ORDER_DETAIL_FAILED = 60002;
    /** 获取资金详情失败 */
    int CHARGE_PAGE_DATA_FAILED = 60003;
    /** 充值金额无效 */
    int CHARGE_AMOUNT_INVALID = 60004;
    /** 充值类型无效 */
    int CHARGE_TYPE_INVALID = 60005;
    
    // ==================== API模块相关错误码 (70xxx) ====================
    /** 手机号不能为空 */
    int PHONE_NUMBER_EMPTY = 70001;
    /** 手机号格式错误 */
    int PHONE_NUMBER_FORMAT_ERROR = 70002;
    /** 两次输入的密码不一致 */
    int PASSWORD_NOT_MATCH = 70003;
    /** 密码重置失败，请检查手机号是否正确 */
    int PASSWORD_RESET_FAILED = 70004;
    /** 密码修改失败 */
    int PASSWORD_CHANGE_FAILED = 70005;
    /** 验证码发送失败，请稍后重试 */
    int VERIFICATION_CODE_SEND_FAILED = 70006;
    /** 系统异常，请稍后重试 */
    int SYSTEM_EXCEPTION = 70007;
    /** 签到失败 */
    int SIGN_IN_FAILED = 70008;
    /** 该银行账号已存在 */
    int BANK_ACCOUNT_EXISTS = 70009;
    /** 请选择要删除的支付信息 */
    int PAYMENT_INFO_DELETE_ERROR = 70010;
    /** 获取用户任务信息失败 */
    int USER_TASK_INFO_FAILED = 70011;
    /** 找回密码失败 */
    int FIND_PASSWORD_FAILED = 70012;
    /** 获取余额信息失败 */
    int GET_BALANCE_INFO_FAILED = 70013;
    /** 您还没有获取验证码，请先获取验证码 */
    int VERIFICATION_CODE_NOT_FOUND = 70014;
    /** 验证码不正确 */
    int VERIFICATION_CODE_INCORRECT = 70015;
    /** 验证码不能为空 */
    int VERIFICATION_CODE_EMPTY = 70016;
    
    // ==================== VIP相关错误码 (80xxx) ====================
    /** 不满足领取条件 */
    int VIP_CLAIM_CONDITION_NOT_MET = 80001;
    /** 领取失败，请稍后再试 */
    int VIP_CLAIM_FAILED = 80002;
    
    // ==================== 提现相关错误码 (80xxx) ====================
    /** 您的提现功能已被禁用，请联系客服 */
    int WITHDRAWAL_DISABLED = 80003;
    /** 您有一个待处理的提现订单，请等待完成后再申请 */
    int WITHDRAWAL_PENDING_ORDER = 80004;
    /** 提现余额不足 */
    int WITHDRAWAL_INSUFFICIENT_BALANCE = 80005;
    /** 提现金额不能小于200卢比 */
    int WITHDRAWAL_AMOUNT_TOO_SMALL = 80006;
    /** 提现金额不能大于100000卢比 */
    int WITHDRAWAL_AMOUNT_TOO_LARGE = 80007;
    /** 提现功能正在维护中，请稍后再申请 */
    int WITHDRAWAL_MAINTENANCE = 80008;
    /** 卡号不能为空 */
    int WITHDRAWAL_CARD_EMPTY = 80009;
    
    // ==================== 服务层相关错误码 (90xxx) ====================
    /** 获取提现分页数据失败 */
    int GET_WITHDRAWAL_PAGE_DATA_FAILED = 90001;
    /** 获取佣金提现分页数据失败 */
    int GET_COMMISSION_WITHDRAWAL_PAGE_DATA_FAILED = 90002;
    /** 获取佣金提现统计失败 */
    int GET_COMMISSION_WITHDRAWAL_STATS_FAILED = 90003;
    /** 更新USDT收款记录失败 */
    int UPDATE_USDT_RECORD_FAILED = 90004;
    /** 更新充值订单状态失败 */
    int UPDATE_CHARGE_ORDER_STATUS_FAILED = 90005;
    /** 用户不存在 */
    int USER_NOT_EXISTS = 90006;
    /** 记录USDT充值账变明细失败 */
    int RECORD_USDT_RECHARGE_BALANCE_DETAIL_FAILED = 90007;
    /** 创建USDT收款记录失败 */
    int CREATE_USDT_RECORD_FAILED = 90008;
    /** 订单不存在 */
    int ORDER_NOT_EXISTS = 90009;
    /** 获取充值订单详情失败 */
    int GET_CHARGE_ORDER_DETAIL_FAILED = 90010;
    /** 获取充值分页数据失败 */
    int GET_CHARGE_PAGE_DATA_FAILED = 90011;
    /** USDT-TRC20地址未配置 */
    int USDT_ADDRESS_NOT_CONFIGURED = 90012;
    /** 生成二维码失败 */
    int GENERATE_QR_CODE_FAILED = 90013;
    /** 图片转换失败 */
    int IMAGE_CONVERSION_FAILED = 90014;
    /** 获取代理佣金信息失败 */
    int GET_AGENT_COMMISSION_INFO_FAILED = 90015;
    /** 获取代理中心数据失败 */
    int GET_AGENT_CENTER_DATA_FAILED = 90016;
    /** 生成签名失败 */
    int GENERATE_SIGNATURE_FAILED = 90017;
    /** 获取锁失败 */
    int ACQUIRE_LOCK_FAILED = 90018;
    /** 获取资金明细失败 */
    int GET_BALANCE_DETAIL_FAILED = 90019;
    /** 获取付息还本记录失败 */
    int GET_PROFIT_ENDED_RECORD_FAILED = 90020;
    /** 获取投资中项目统计失败 */
    int GET_INVESTMENT_PROJECT_STATS_FAILED = 90021;
    /** 获取积分明细失败 */
    int GET_TEAM_POINTS_DETAIL_FAILED = 90022;
    
    // ==================== 投资项目相关错误码 (90xxx) ====================
    /** 项目已下架或不可投资 */
    int PROJECT_NOT_AVAILABLE = 90023;
    /** 投资金额必须大于0 */
    int INVESTMENT_AMOUNT_INVALID = 90024;
    /** 购买份数必须大于0 */
    int INVESTMENT_COUNT_INVALID = 90025;
}
