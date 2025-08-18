package io.renren.modules.finance.service;

import io.renren.common.page.PageData;
import io.renren.common.service.BaseService;
import io.renren.modules.finance.dao.UserBalanceDetailDao;
import io.renren.modules.finance.dto.UserBalanceDetailDTO;
import io.renren.modules.finance.entity.UserBalanceDetailEntity;

/**
 * 用户余额明细Service
 *
 * @author renren
 * @since 1.0.0
 */
public interface UserBalanceDetailService extends BaseService<UserBalanceDetailEntity> {

    /**
     * 分页查询账变明细
     *
     * @param page 页码
     * @param limit 每页记录数
     * @param biaoqian 标签筛选
     * @param biaoqianFlag 标签筛选标志 1有 0无
     * @param busiType 账务类型
     * @param endTime 结束时间
     * @param mobile 用户账号
     * @param order 排序方式
     * @param orderField 排序字段
     * @param startTime 开始时间
     * @return 分页数据
     */
    PageData<UserBalanceDetailDTO> getBalanceDetailPage(Integer page, Integer limit, String biaoqian, 
                                                       Integer biaoqianFlag, Integer busiType, Long endTime, 
                                                       String mobile, String order, String orderField, Long startTime);
}
