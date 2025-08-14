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
     * 分页查询我的投资信息
     * @param params 查询参数
     * @return 分页数据
     */
    MyInvestmentPageData<MyInvestmentDTO> queryPageData(Map<String, Object> params);
}
