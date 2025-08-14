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
    
    @Autowired
    private ProjectDao projectDao;
    
    @Autowired
    private UserDao userDao;

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

    /**
     * 转换为DTO
     */
    private List<MyInvestmentDTO> convertToDto(List<InvestmentRecordEntity> entityList) {
        if (entityList == null || entityList.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<MyInvestmentDTO> dtoList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        for (InvestmentRecordEntity entity : entityList) {
            MyInvestmentDTO dto = new MyInvestmentDTO();
            
            // 基础字段映射
            dto.setId(entity.getId() != null ? entity.getId().intValue() : null);
            dto.setUserId(entity.getUserId() != null ? entity.getUserId().intValue() : null);
            dto.setInvestId(entity.getProjectId() != null ? entity.getProjectId().intValue() : null);
            dto.setOrderId(entity.getOrderId() != null ? entity.getOrderId().intValue() : null);
            dto.setOrderAbbr(entity.getOrderAbbr());
            dto.setInvestmentAmount(entity.getInvestmentAmount() != null ? entity.getInvestmentAmount().intValue() : null);
            dto.setProfitAmount(entity.getProfitAmount() != null ? entity.getProfitAmount().intValue() : null);
            dto.setProfitInterest(entity.getProfitInterest() != null ? entity.getProfitInterest().intValue() : null);
            dto.setProfitPrincipal(entity.getProfitPrincipal() != null ? entity.getProfitPrincipal().intValue() : null);
            dto.setStatus(entity.getStatus());
            dto.setCycle(entity.getCycle());
            dto.setCycleType(entity.getCycleType());
            dto.setDdsy(entity.getDdsy() != null ? entity.getDdsy().intValue() : null);
            dto.setInvestCount(entity.getInvestCount());
            dto.setRushMinute(entity.getRushMinute());
            dto.setAgent(entity.getAgent());
            dto.setSalesmanid(entity.getSalesmanid());
            
            // 日期字段格式化
            if (entity.getOrderDate() != null) {
                dto.setOrderDate(sdf.format(entity.getOrderDate()));
            }
            if (entity.getProfitDate() != null) {
                dto.setProfitDate(sdf.format(entity.getProfitDate()));
            }
            
            // 从项目表查询项目相关信息
            if (entity.getProjectId() != null) {
                ProjectEntity project = projectDao.selectById(entity.getProjectId());
                if (project != null) {
                    dto.setInvestName(project.getInvestName());
                    dto.setImg(project.getImg());
                } else {
                    // 如果项目不存在，设置默认值
                    dto.setInvestName("项目不存在");
                    dto.setImg("");
                }
            } else {
                dto.setInvestName("未知项目");
                dto.setImg("");
            }
            
            // 从用户表查询用户手机号
            if (entity.getUserId() != null) {
                UserEntity user = userDao.getUserByUserId(entity.getUserId());
                if (user != null && StringUtils.isNotBlank(user.getMobile())) {
                    dto.setMobile(user.getMobile());
                } else {
                    dto.setMobile("");
                }
            } else {
                dto.setMobile("");
            }
            
            dtoList.add(dto);
        }
        
        return dtoList;
    }

    /**
     * 构建查询条件
     */
    private QueryWrapper<InvestmentRecordEntity> buildQueryWrapper(Map<String, Object> params) {
        String userId = (String) params.get("userId");
        Integer status = (Integer) params.get("status");
        String order = (String) params.get(Constant.ORDER);
        String orderField = (String) params.get(Constant.ORDER_FIELD);

        QueryWrapper<InvestmentRecordEntity> queryWrapper = new QueryWrapper<>();
        
        // 用户ID筛选
        if (StringUtils.isNotBlank(userId)) {
            queryWrapper.eq("user_id", userId);
        }
        
        // 状态筛选
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        
        // 排序处理
        if (StringUtils.isNotBlank(orderField)) {
            if (Constant.DESC.equalsIgnoreCase(order)) {
                queryWrapper.orderByDesc(orderField);
            } else {
                queryWrapper.orderByAsc(orderField);
            }
        } else {
            // 默认按投资日期倒序排序
            queryWrapper.orderByDesc("order_date");
        }

        return queryWrapper;
    }
}
