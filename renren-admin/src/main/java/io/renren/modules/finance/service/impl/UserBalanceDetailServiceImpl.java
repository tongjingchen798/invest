package io.renren.modules.finance.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.common.page.PageData;
import io.renren.common.service.impl.BaseServiceImpl;
import io.renren.common.utils.ConvertUtils;
import io.renren.modules.finance.dao.UserBalanceDetailDao;
import io.renren.modules.finance.dto.UserBalanceDetailDTO;
import io.renren.modules.finance.entity.UserBalanceDetailEntity;
import io.renren.modules.finance.service.UserBalanceDetailService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 用户余额明细Service实现类
 *
 * @author renren
 * @since 1.0.0
 */
@Service
public class UserBalanceDetailServiceImpl extends BaseServiceImpl<UserBalanceDetailDao, UserBalanceDetailEntity> implements UserBalanceDetailService {

    @Override
    public PageData<UserBalanceDetailDTO> getBalanceDetailPage(Integer page, Integer limit, String biaoqian, 
                                                             Integer biaoqianFlag, Integer busiType, Long endTime, 
                                                             String mobile, String order, String orderField, Long startTime) {
        Page<UserBalanceDetailEntity> pageParam = new Page<>(page, limit);
        
        // 转换时间戳为Date对象
        Date startDate = startTime != null ? new Date(startTime) : null;
        Date endDate = endTime != null ? new Date(endTime) : null;

        // 使用自定义的DAO方法进行关联查询
        IPage<UserBalanceDetailDTO> pageResult = baseDao.selectBalanceDetailPage(pageParam, biaoqian, biaoqianFlag,
                                                       busiType, endDate, mobile, startDate);
        
        return new PageData<>(pageResult.getRecords(), pageResult.getTotal());
    }
}
