package io.renren.modules.member.service;

import io.renren.common.page.PageData;
import io.renren.common.utils.Result;
import io.renren.modules.member.dto.MemberInfoDTO;
import io.renren.modules.member.dto.SettlementReportDTO;

/**
 * 会员查询管理
 *
 * @author renren
 * @since 1.0.0
 */
public interface MemberService {

    /**
     * 分页查询会员信息
     *
     * @param page 当前页码
     * @param limit 每页记录数
     * @param agent 代理ID
     * @param balanceFlag 余额筛选
     * @param biaoqian 标签
     * @param biaoqianFlag 标签筛选
     * @param channel 渠道
     * @param chargeFlag 充值筛选
     * @param endTime 结束时间戳
     * @param liebian 裂变筛选
     * @param mobile 手机号
     * @param order 排序方式
     * @param orderField 排序字段
     * @param salesmanid 业务员ID
     * @param startTime 开始时间戳
     * @param tzFlag 投资筛选
     * @param username 用户名
     * @param vip VIP等级
     * @param viplr VIP利润
     * @param withdrawFlag 提现筛选
     * @return 会员信息分页数据
     */
    PageData<MemberInfoDTO> getMemberPage(Integer page, Integer limit, Long agent, Integer balanceFlag,
                                           String biaoqian, Integer biaoqianFlag, String channel, Integer chargeFlag,
                                           Long endTime, Integer liebian, String mobile, String order, String orderField,
                                           Long salesmanid, Long startTime, Integer tzFlag, String username, 
                                           Integer vip, Integer viplr, Integer withdrawFlag);

    /**
     * 获取结算报表分页数据
     *
     * @param page 当前页码
     * @param limit 每页记录数
     * @param agent 代理ID
     * @param endTime 结束时间戳
     * @param order 排序方式
     * @param orderField 排序字段
     * @param salesmanid 业务员ID
     * @param startTime 开始时间戳
     * @return 结算报表分页数据
     */
    PageData<SettlementReportDTO> getSettlementReport(Integer page, Integer limit, Long agent, Long endTime,
                                                      String order, String orderField, Long salesmanid, Long startTime);

    /**
     * 获取所有标签列表
     *
     * @return 标签列表
     */
    java.util.List<String> getBiaoQianList();

    /**
     * 发放工资
     *
     * @param userId 用户ID
     * @param amount 工资金额（分）
     * @return 操作结果
     */
    Result paySalary(Long userId, Long amount);

    /**
     * 修改用户标签
     *
     * @param userId 用户ID
     * @param biaoqian 标签值
     * @param type 操作类型：1-设置标签，2-清除标签
     * @return 操作结果
     */
    Result updateUserBiaoqian(Long userId, Integer biaoqian, Integer type);

    /**
     * 修改用户业务员
     *
     * @param userId 用户ID
     * @param salesmanid 业务员ID
     * @return 操作结果
     */
    Result updateUserToAgent(Long userId, Long salesmanid);

    /**
     * 设置用户上级
     *
     * @param userId 用户ID
     * @param mobile 上级用户手机号
     * @return 操作结果
     */
    Result updateUp(Long userId, String mobile);

}
