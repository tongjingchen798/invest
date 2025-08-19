package io.renren.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.constant.Constant;
import io.renren.common.service.impl.BaseServiceImpl;
import io.renren.dao.MyInvestmentDao;
import io.renren.dao.ProjectDao;
import io.renren.dao.UserDao;
import io.renren.dto.MyInvestmentDTO;
import io.renren.dto.MyInvestmentPageData;
import io.renren.entity.InvestmentRecordEntity;
import io.renren.entity.ProjectEntity;
import io.renren.entity.UserEntity;
import io.renren.service.MyInvestmentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的投资服务实现类
 *
 * @author Mark sunlightcs@gmail.com
 */
@Service
public class MyInvestmentServiceImpl extends ServiceImpl<MyInvestmentDao, InvestmentRecordEntity> implements MyInvestmentService {

    @Autowired
    private MyInvestmentDao myInvestmentDao;
    

    @Override
    public MyInvestmentPageData<MyInvestmentDTO> queryPageData(Map<String, Object> params) {
        try {
            // 使用 MyBatis-Plus 分页
            Page<MyInvestmentDTO> page = new Page<>(
                (Integer) params.get(Constant.PAGE), 
                (Integer) params.get(Constant.LIMIT)
            );
            
            // 执行分页查询（使用自定义SQL）
            IPage<MyInvestmentDTO> pageResult = myInvestmentDao.selectPage(page, params);
            
            // 构建分页数据
            MyInvestmentPageData<MyInvestmentDTO> pageData = new MyInvestmentPageData<>();
            pageData.setList(pageResult.getRecords());
            pageData.setTotal((int) pageResult.getTotal());
            pageData.setSum(new HashMap<>()); // 暂时设置为空对象，可根据需要添加汇总信息
            
            return pageData;
            
        } catch (Exception e) {
            e.printStackTrace();
            // 设置默认值
            MyInvestmentPageData<MyInvestmentDTO> pageData = new MyInvestmentPageData<>();
            pageData.setList(null);
            pageData.setTotal(0);
            pageData.setSum(new HashMap<>());
            return pageData;
        }
    }
    
    @Override
    public MyInvestmentPageData<MyInvestmentDTO> queryPageData(Long userId, Integer page, Integer limit, Integer status, String order, String orderField) {
        try {
            // 创建MyBatis-Plus分页对象
            Page<MyInvestmentDTO> pageParam = new Page<>(page, limit);
            
            // 构建查询参数
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId.toString());
            params.put("status", status);
            if (StringUtils.isNotBlank(order)) {
                params.put(Constant.ORDER, order);
            }
            if (StringUtils.isNotBlank(orderField)) {
                params.put(Constant.ORDER_FIELD, orderField);
            }
            
            // 执行分页查询（使用自定义SQL）
            IPage<MyInvestmentDTO> pageResult = myInvestmentDao.selectPage(pageParam, params);
            
            // 构建分页数据
            MyInvestmentPageData<MyInvestmentDTO> pageData = new MyInvestmentPageData<>();
            pageData.setList(pageResult.getRecords());
            pageData.setTotal((int) pageResult.getTotal());
            pageData.setSum(new HashMap<>()); // 暂时设置为空对象，可根据需要添加汇总信息
            
            return pageData;
            
        } catch (Exception e) {
            e.printStackTrace();
            // 设置默认值
            MyInvestmentPageData<MyInvestmentDTO> pageData = new MyInvestmentPageData<>();
            pageData.setList(null);
            pageData.setTotal(0);
            pageData.setSum(new HashMap<>());
            return pageData;
        }
    }

}
