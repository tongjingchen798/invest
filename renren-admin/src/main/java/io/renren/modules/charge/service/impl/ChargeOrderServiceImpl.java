package io.renren.modules.charge.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.common.constant.Constant;
import io.renren.common.service.impl.BaseServiceImpl;
import io.renren.modules.charge.dao.ChargeOrderDao;
import io.renren.modules.charge.dto.ChargeOrderDetailDTO;
import io.renren.modules.charge.dto.ChargePageData;
import io.renren.modules.charge.entity.ChargeOrderEntity;
import io.renren.modules.charge.service.ChargeOrderService;
import io.renren.modules.security.user.SecurityUser;
import io.renren.modules.security.user.UserDetail;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 充值订单服务实现类
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Slf4j
@Service("chargeOrderService")
public class ChargeOrderServiceImpl extends BaseServiceImpl<ChargeOrderDao, ChargeOrderEntity> implements ChargeOrderService {

    @Autowired
    private ChargeOrderDao chargeOrderDao;

    @Override
    public ChargePageData getAdminChargePage(Map<String, Object> params) {
        try {
            // 创建MyBatis-Plus分页对象
            long curPage = 1;
            long limit = 10;
            
            if (params.get(Constant.PAGE) != null) {
                curPage = Long.parseLong((String) params.get(Constant.PAGE));
            }
            if (params.get(Constant.LIMIT) != null) {
                limit = Long.parseLong((String) params.get(Constant.LIMIT));
            }
            
            // 添加权限控制参数
            UserDetail user = SecurityUser.getUser();
            if (user != null) {
                params.put("currentUserId", user.getId());
                params.put("currentUserType", user.getType());
            }
            
            // 创建分页对象，注意这里使用ChargeOrderDetailDTO作为泛型
            Page<ChargeOrderDetailDTO> page = new Page<>(curPage, limit);
            
            // 调用自定义的XML查询方法
            IPage<ChargeOrderDetailDTO> pageResult = chargeOrderDao.selectAdminChargePage(page, params);
            
            // 构建充值统计数据
            Map<String, Object> sumData = new HashMap<>();
            
            // 统计总充值金额（amount字段，充值金币）
            Long totalAmount = pageResult.getRecords().stream()
                    .mapToLong(order -> order.getAmount() != null ? order.getAmount() : 0L)
                    .sum();
            
            // 统计真实充值金额（realAmount字段，真实充值额）
            Long totalRealAmount = pageResult.getRecords().stream()
                    .mapToLong(order -> order.getRealAmount() != null ? order.getRealAmount() : 0L)
                    .sum();
            
            sumData.put("amount", totalAmount);
            sumData.put("realAmount", totalRealAmount);
            
            // 构建分页数据
            ChargePageData pageData = new ChargePageData();
            pageData.setList(pageResult.getRecords());
            pageData.setTotal((int) pageResult.getTotal());
            pageData.setSum(sumData);
            
            return pageData;
            
        } catch (Exception e) {
            log.error("查询充值列表失败", e);
            throw new RuntimeException("查询充值列表失败: " + e.getMessage());
        }
    }

    @Override
    public List<ChargeOrderEntity> getChargeOrdersByUserId(Long userId) {
        QueryWrapper<ChargeOrderEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return chargeOrderDao.selectList(queryWrapper);
    }

    @Override
    public ChargeOrderEntity getChargeOrderByOrderno(String orderno) {
        QueryWrapper<ChargeOrderEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("orderno", orderno);
        return chargeOrderDao.selectOne(queryWrapper);
    }

    @Override
    public ChargeOrderEntity getChargeOrderByThreeOrderNo(String threeorderNo) {
        QueryWrapper<ChargeOrderEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("threeorder_no", threeorderNo);
        return chargeOrderDao.selectOne(queryWrapper);
    }

    @Override
    public List<ChargeOrderEntity> getChargeOrdersByState(Integer state) {
        QueryWrapper<ChargeOrderEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("state", state);
        return chargeOrderDao.selectList(queryWrapper);
    }

    @Override
    public List<ChargeOrderEntity> getChargeOrdersByParams(Map<String, Object> params) {
        QueryWrapper<ChargeOrderEntity> queryWrapper = new QueryWrapper<>();
        // 根据参数构建查询条件
        if (params.get("userId") != null) {
            queryWrapper.eq("user_id", params.get("userId"));
        }
        if (params.get("state") != null) {
            queryWrapper.eq("state", params.get("state"));
        }
        return chargeOrderDao.selectList(queryWrapper);
    }

    @Override
    public Long getTotalChargeAmountByUserId(Long userId) {
        QueryWrapper<ChargeOrderEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("state", 1); // 只统计审核通过的
        List<ChargeOrderEntity> orders = chargeOrderDao.selectList(queryWrapper);
        return orders.stream()
                .mapToLong(order -> order.getAmount() != null ? order.getAmount() : 0L)
                .sum();
    }


}
