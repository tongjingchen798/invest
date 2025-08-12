# 用户信息接口文档

## 获取用户信息

**接口地址**: `/api/userInfo`

**请求方式**: `GET`

**请求数据类型**: `application/x-www-form-urlencoded`

**响应数据类型**: `*/*`

**接口描述**: 获取当前登录用户的详细信息

**请求参数**: 无（通过登录token获取用户信息）

**响应状态**:

| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
| 200 | OK | UserInfoDTO |
| 401 | Unauthorized | |
| 403 | Forbidden | |
| 404 | Not Found | |

**响应参数**:

| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
| id | 用户ID | integer(int64) | |
| username | 用户名 | string | |
| mobile | 手机号 | string | |
| inviteCode | 邀请码 | string | |
| upinviteCode | 上级邀请码 | string | |
| agent | 代理信息 | string | |
| channel | 客户渠道号 | string | |
| equipment | 登录端口(1:安卓, 2:ios, 3:pc, 4:未知) | integer(int32) | |
| createDate | 创建时间 | string | |
| assets | 资产 | integer(int64) | |
| biaoqian | 标签 | string | |
| endedItems | 已结束项目数 | integer(int64) | |
| endedPrincipal | 已结束本金 | integer(int64) | |
| endedProfit | 已结束收益 | integer(int64) | |
| flag | 标志 | integer(int64) | |
| idCard | 身份证号 | string | |
| inviteCodeStatus | 邀请码状态 0：禁用 1：正常 | integer(int32) | |
| itmes | 项目数 | integer(int64) | |
| jrProfit | 今日收益 | integer(int64) | |
| loginTime | 登录时间 | string | |
| paymentPwd | 支付密码 | string | |
| registerIp | 注册IP | string | |
| rewardWithdrawStatus | 奖励提现状态 0：禁用 1：正常 | integer(int32) | |
| salesmanid | 业务员ID | string | |
| status | 状态 0：禁用 1：正常 | integer(int32) | |
| superiorUa | 上级U级账户余额 | integer(int64) | |
| superiorUb | 上级U级账户余额 | integer(int64) | |
| superiorUc | 上级U级账户余额 | integer(int64) | |
| todaybalance10 | 今日余额10 | integer(int64) | |
| todaybalance20 | 今日余额20 | integer(int64) | |
| todaybalance5 | 今日余额5 | integer(int64) | |
| todaycharge100 | 今日充值100 | integer(int64) | |
| todaycharge20 | 今日充值20 | integer(int64) | |
| todaycharge50 | 今日充值50 | integer(int64) | |
| totalPrincipal | 总本金 | integer(int64) | |
| totalProfit | 总收益 | integer(int64) | |
| two_pwd | 二级密码 | string | |
| tzWithdrawStatus | 投资提现状态 0：禁用 1：正常 | integer(int32) | |
| valid3user | 有效3个月用户数 | integer(int64) | |
| valid6user | 有效6个月用户数 | integer(int64) | |
| valid9user | 有效9个月用户数 | integer(int64) | |
| vip | VIP等级 | integer(int32) | |
| vip1state | VIP1状态 | integer(int64) | |
| vip2state | VIP2状态 | integer(int64) | |
| vip3state | VIP3状态 | integer(int64) | |
| vip4state | VIP4状态 | integer(int64) | |
| vip5state | VIP5状态 | integer(int64) | |
| vip6state | VIP6状态 | integer(int64) | |
| viplr | VIP利率 | integer(int32) | |

**响应示例**:
```javascript
{
  "code": 0,
  "msg": "success",
  "data": {
    "id": 1938603271541919746,
    "username": "",
    "mobile": "917777777713",
    "inviteCode": "BE1H0Z",
    "upinviteCode": null,
    "agent": "1748403717627",
    "channel": "",
    "equipment": 2,
    "createDate": "2025-06-27 22:20:12",
    "assets": 797900,
    "biaoqian": "",
    "endedItems": 5,
    "endedPrincipal": 500000,
    "endedProfit": 15000,
    "flag": 0,
    "idCard": "",
    "inviteCodeStatus": 1,
    "itmes": 8,
    "jrProfit": 1500,
    "loginTime": "",
    "paymentPwd": "",
    "registerIp": "182.239.114.217",
    "rewardWithdrawStatus": 1,
    "salesmanid": "1748403763980",
    "status": 1,
    "superiorUa": 0,
    "superiorUb": 0,
    "superiorUc": 0,
    "todaybalance10": 0,
    "todaybalance20": 0,
    "todaybalance5": 0,
    "todaycharge100": 0,
    "todaycharge20": 0,
    "todaycharge50": 0,
    "totalPrincipal": 800000,
    "totalProfit": 25000,
    "two_pwd": "",
    "tzWithdrawStatus": 0,
    "valid3user": 3,
    "valid6user": 2,
    "valid9user": 1,
    "vip": 0,
    "vip1state": 0,
    "vip2state": 0,
    "vip3state": 0,
    "vip4state": 0,
    "vip5state": 0,
    "vip6state": 0,
    "viplr": 0
  }
}
```

## 数据库表结构

### tb_user 表字段说明

| 字段名 | 类型 | 说明 | 默认值 |
|--------|------|------|--------|
| id | bigint(20) | 用户ID | 自增 |
| username | varchar(50) | 用户名 | NULL |
| mobile | varchar(20) | 手机号 | 必填 |
| password | varchar(100) | 密码 | 必填 |
| two_pwd | varchar(100) | 二级密码 | NULL |
| invite_code | varchar(20) | 邀请码 | NULL |
| upinvite_code | varchar(20) | 上级邀请码 | NULL |
| agent | varchar(50) | 代理信息 | NULL |
| agent_name | varchar(100) | 代理名称 | NULL |
| salesmanid | varchar(50) | 业务员ID | NULL |
| salesman_name | varchar(100) | 业务员名称 | NULL |
| superior_name | varchar(100) | 上级名称 | NULL |
| superior_code | varchar(20) | 上级邀请码 | NULL |
| channel | varchar(50) | 客户渠道号 | NULL |
| equipment | tinyint(4) | 登录端口(1:安卓, 2:ios, 3:pc, 4:未知) | 4 |
| register_ip | varchar(50) | 注册IP | NULL |
| last_ip | varchar(50) | 最后登录IP | NULL |
| last_date | datetime | 最后登录时间 | NULL |
| create_date | datetime | 创建时间 | CURRENT_TIMESTAMP |
| extension_name | varchar(100) | 推广名称 | NULL |
| today_investment | bigint(20) | 今日投资(分) | 0 |
| history_investment | bigint(20) | 历史投资(分) | 0 |
| today_profit | bigint(20) | 今日收益(分) | 0 |
| history_profit | bigint(20) | 历史收益(分) | 0 |
| today_recharge | bigint(20) | 今日充值(分) | 0 |
| today_withdraw | bigint(20) | 今日提现(分) | 0 |
| today_recharge_cnt | bigint(20) | 今日充值次数 | 0 |
| today_withdraw_cnt | bigint(20) | 今日提现次数 | 0 |
| charge_sum | bigint(20) | 累计充值(分) | 0 |
| withdraw_sum | bigint(20) | 累计提现(分) | 0 |
| historychargecnt | bigint(20) | 历史充值次数 | 0 |
| historywithdrawcnt | bigint(20) | 历史提现次数 | 0 |
| history_coupon_balance | bigint(20) | 历史优惠券余额(分) | 0 |
| coupon_cnt | bigint(20) | 优惠券数量 | 0 |
| coupon_balance | bigint(20) | 优惠券余额(分) | 0 |
| history_privilege_cnt | bigint(20) | 历史特权券数量 | 0 |
| privilege_cnt | bigint(20) | 特权券数量 | 0 |
| balance | bigint(20) | 余额(分) | 0 |
| cashwithdrawable | bigint(20) | 可提现余额(分) | 0 |
| freeze_balance | bigint(20) | 冻结余额(分) | 0 |
| uacnt | bigint(20) | U级账户余额(分) | 0 |
| ubcnt | bigint(20) | U级账户余额(分) | 0 |
| uccnt | bigint(20) | U级账户余额(分) | 0 |
| ua_profit | bigint(20) | U级账户收益(分) | 0 |
| ub_profit | bigint(20) | U级账户收益(分) | 0 |
| uc_profit | bigint(20) | U级账户收益(分) | 0 |
| history_ua_profit | bigint(20) | 历史U级账户收益(分) | 0 |
| history_ub_profit | bigint(20) | 历史U级账户收益(分) | 0 |
| history_uc_profit | bigint(20) | 历史U级账户收益(分) | 0 |
| to_daywithdraw_count | bigint(20) | 今日提现次数 | 0 |
| to_daywithdraw_quota | bigint(20) | 今日提现额度(分) | 0 |
| withdraw_count | bigint(20) | 提现次数 | 0 |
| withdraw_quota | bigint(20) | 提现额度(分) | 0 |
| commission_balance | bigint(20) | 佣金余额(分) | 0 |
| tgrs | bigint(20) | 推广人数 | 0 |
| sy_sum | bigint(20) | 收益总额(分) | 0 |
| to_dayctc | bigint(20) | 今日CTC(分) | 0 |
| historyctc | bigint(20) | 历史CTC(分) | 0 |
| status | tinyint(4) | 状态 0：禁用 1：正常 | 1 |
| invite_code_status | tinyint(4) | 邀请码状态 0：禁用 1：正常 | 1 |
| tz_withdraw_status | tinyint(4) | 投资提现状态 0：禁用 1：正常 | 0 |
| reward_withdraw_status | tinyint(4) | 奖励提现状态 0：禁用 1：正常 | 1 |
| biaoqian | varchar(200) | 标签 | NULL |
| vip | tinyint(4) | VIP等级 | 0 |
| viplr | bigint(20) | VIP利率(分) | 0 |
| assets | bigint(20) | 资产(分) | 0 |
| ended_items | bigint(20) | 已结束项目数 | 0 |
| ended_principal | bigint(20) | 已结束本金(分) | 0 |
| ended_profit | bigint(20) | 已结束收益(分) | 0 |
| flag | bigint(20) | 标志 | 0 |
| id_card | varchar(20) | 身份证号 | NULL |
| itmes | bigint(20) | 项目数 | 0 |
| jr_profit | bigint(20) | 今日收益(分) | 0 |
| login_time | varchar(50) | 登录时间 | NULL |
| payment_pwd | varchar(100) | 支付密码 | NULL |
| superior_ua | bigint(20) | 上级U级账户余额(分) | 0 |
| superior_ub | bigint(20) | 上级U级账户余额(分) | 0 |
| superior_uc | bigint(20) | 上级U级账户余额(分) | 0 |
| todaybalance10 | bigint(20) | 今日余额10(分) | 0 |
| todaybalance20 | bigint(20) | 今日余额20(分) | 0 |
| todaybalance5 | bigint(20) | 今日余额5(分) | 0 |
| todaycharge100 | bigint(20) | 今日充值100(分) | 0 |
| todaycharge20 | bigint(20) | 今日充值20(分) | 0 |
| todaycharge50 | bigint(20) | 今日充值50(分) | 0 |
| total_principal | bigint(20) | 总本金(分) | 0 |
| total_profit | bigint(20) | 总收益(分) | 0 |
| valid3user | bigint(20) | 有效3个月用户数 | 0 |
| valid6user | bigint(20) | 有效6个月用户数 | 0 |
| valid9user | bigint(20) | 有效9个月用户数 | 0 |
| vip1state | bigint(20) | VIP1状态 | 0 |
| vip2state | bigint(20) | VIP2状态 | 0 |
| vip3state | bigint(20) | VIP3状态 | 0 |
| vip4state | bigint(20) | VIP4状态 | 0 |
| vip5state | bigint(20) | VIP5状态 | 0 |
| vip6state | bigint(20) | VIP6状态 | 0 |

## 注意事项

1. 所有金额字段都以分为单位存储，避免浮点数精度问题
2. 状态字段使用tinyint类型，节省存储空间
3. 时间字段使用datetime类型，支持时区
4. 添加了必要的索引以提高查询性能
5. 接口返回的数据会自动填充一些关联字段的默认值
