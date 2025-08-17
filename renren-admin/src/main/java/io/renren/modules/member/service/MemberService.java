package io.renren.modules.member.service;

import io.renren.common.page.PageData;
import io.renren.modules.member.dto.MemberInfoDTO;

/**
 * 会员查询服务接口
 *
 * @author renren
 * @since 1.0.0
 */
public interface MemberService {

    /**
     * 分页查询会员信息
     * @param page 页码
     * @param limit 每页大小
     * @param agent 代理下拉框
     * @param balanceFlag 可用余额筛选：有余额=1，无余额=0，查全部，不传参
     * @param biaoqian 标签筛选：传标签
     * @param biaoqianFlag 标签筛选：1 有 0 无 查全部，不传参
     * @param channel 渠道筛选：传渠道
     * @param chargeFlag 充值筛选：有充值=1，无充值=0，查全部，不传参
     * @param endTime 结束日期:时间戳
     * @param liebian 裂变筛选：是 =1，否 =0，查全部，不传参
     * @param mobile 用户账号
     * @param order 排序方式，可选值(asc、desc)
     * @param orderField 排序字段
     * @param salesmanid 业务员下拉框
     * @param startTime 开始日期:时间戳
     * @param tzFlag 投资筛选：有投资=1，无投资=0，查全部，不传参
     * @param username 用户姓名
     * @param vip 0-6 查全部，不传参
     * @param viplr 0-4,5,6 查全部，不传参
     * @param withdrawFlag 提现筛选：有提现=1，无提现=0，查全部，不传参
     * @return 分页数据
     */
    PageData<MemberInfoDTO> getMemberPage(Integer page, Integer limit, Long agent, Integer balanceFlag, 
                                         String biaoqian, Integer biaoqianFlag, String channel, Integer chargeFlag,
                                         Long endTime, Integer liebian, String mobile, String order, String orderField,
                                         Long salesmanid, Long startTime, Integer tzFlag, String username, 
                                         Integer vip, Integer viplr, Integer withdrawFlag);
}
