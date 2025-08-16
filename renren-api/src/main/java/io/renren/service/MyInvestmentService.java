package io.renren.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.service.BaseService;
import io.renren.dto.MyInvestmentDTO;
import io.renren.dto.MyInvestmentPageData;
import io.renren.entity.InvestmentRecordEntity;

import java.util.Map;

/**
 * 我的投资服务接口
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface MyInvestmentService extends IService<InvestmentRecordEntity> {

    /**
     * 分页查询我的投资信息（使用Map参数）
     * @param params 查询参数
     * @return 分页数据
     */
    MyInvestmentPageData<MyInvestmentDTO> queryPageData(Map<String, Object> params);
    
    /**
     * 分页查询我的投资信息（直接参数）
     * @param userId 用户ID
     * @param page 页码
     * @param limit 每页大小
     * @param status 状态
     * @param order 排序方式
     * @param orderField 排序字段
     * @return 分页数据
     */
    MyInvestmentPageData<MyInvestmentDTO> queryPageData(Long userId, Integer page, Integer limit, Integer status, String order, String orderField);
}
