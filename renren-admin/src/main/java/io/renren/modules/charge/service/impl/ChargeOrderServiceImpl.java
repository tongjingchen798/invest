package io.renren.modules.charge.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.common.constant.Constant;
import io.renren.common.service.impl.BaseServiceImpl;
import io.renren.modules.charge.dao.ChargeOrderDao;
import io.renren.modules.charge.dto.ChargeOrderDetailDTO;
import io.renren.modules.charge.dto.ChargePageData;
import io.renren.modules.charge.entity.ChargeOrderEntity;
import io.renren.modules.charge.service.ChargeOrderService;
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
            // 获取分页参数
            Integer page = Integer.parseInt(params.get(Constant.PAGE).toString());
            Integer limit = Integer.parseInt(params.get(Constant.LIMIT).toString());
            
            // 创建分页对象
            Page<ChargeOrderEntity> pageParam = new Page<>(page, limit);
            
            // 构建查询条件
            QueryWrapper<ChargeOrderEntity> queryWrapper = new QueryWrapper<>();
            
            // 标签筛选
            if (params.get("biaoqian") != null && StringUtils.isNotBlank(params.get("biaoqian").toString())) {
                queryWrapper.eq("biaoqian", params.get("biaoqian"));
            }
            
            // 标签筛选标志
            if (params.get("biaoqianFlag") != null) {
                Integer biaoqianFlag = Integer.parseInt(params.get("biaoqianFlag").toString());
                if (biaoqianFlag == 1) {
                    queryWrapper.isNotNull("biaoqian");
                } else if (biaoqianFlag == 0) {
                    queryWrapper.isNull("biaoqian");
                }
            }
            
            // 时间范围筛选
            if (params.get("createstarttime") != null) {
                Long startTime = Long.parseLong(params.get("createstarttime").toString());
                queryWrapper.ge("create_time", new Date(startTime));
            }
            
            if (params.get("createendtime") != null) {
                Long endTime = Long.parseLong(params.get("createendtime").toString());
                queryWrapper.le("create_time", new Date(endTime));
            }
            
            if (params.get("startTime") != null) {
                Long startTime = Long.parseLong(params.get("startTime").toString());
                queryWrapper.ge("create_time", new Date(startTime));
            }
            
            if (params.get("endTime") != null) {
                Long endTime = Long.parseLong(params.get("endTime").toString());
                queryWrapper.le("create_time", new Date(endTime));
            }

            if(params.get("orderno") !=null){
                String orderno = params.get("orderno").toString();
                queryWrapper.eq("orderno", orderno);
            }

            // 裂变筛选 TODO 要查询用户
//            if (params.get("liebian") != null) {
//                Integer liebian = Integer.parseInt(params.get("liebian").toString());
//                queryWrapper.eq("liebian", liebian);
//            }
            
            // 用户账号筛选
            if (params.get("mobile") != null && StringUtils.isNotBlank(params.get("mobile").toString())) {
                queryWrapper.like("mobile", params.get("mobile"));
            }
            
            // 状态筛选
            if (params.get("state") != null) {
                Integer state = Integer.parseInt(params.get("state").toString());
                queryWrapper.eq("state", state);
            }
            
            // 三方订单号筛选
            if (params.get("threeorder_no") != null && StringUtils.isNotBlank(params.get("threeorder_no").toString())) {
                queryWrapper.like("threeorder_no", params.get("threeorder_no"));
            }
            
            // 排序处理
            if (params.get(Constant.ORDER_FIELD) != null && params.get(Constant.ORDER) != null) {
                String orderField = params.get(Constant.ORDER_FIELD).toString();
                String order = params.get(Constant.ORDER).toString();
                if ("desc".equalsIgnoreCase(order)) {
                    queryWrapper.orderByDesc(orderField);
                } else {
                    queryWrapper.orderByAsc(orderField);
                }
            } else {
                // 默认按创建时间倒序
                queryWrapper.orderByDesc("create_time");
            }
            
            // 执行分页查询
            Page<ChargeOrderEntity> result = chargeOrderDao.selectPage(pageParam, queryWrapper);
            
            // 转换为DTO
            List<ChargeOrderDetailDTO> dtoList = result.getRecords().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            
            // 构建分页数据
            ChargePageData pageData = new ChargePageData();
            pageData.setList(dtoList);
            pageData.setTotal((int) result.getTotal());
            
            // 构建充值统计数据
            Map<String, Object> sumData = new HashMap<>();
            
            // 统计总充值金额（amount字段，充值金币）
            Long totalAmount = result.getRecords().stream()
                    .mapToLong(order -> order.getAmount() != null ? order.getAmount() : 0L)
                    .sum();
            
            // 统计真实充值金额（realAmount字段，真实充值额）
            Long totalRealAmount = result.getRecords().stream()
                    .mapToLong(order -> order.getRealAmount() != null ? order.getRealAmount() : 0L)
                    .sum();
            
            sumData.put("amount", totalAmount);
            sumData.put("realAmount", totalRealAmount);
            
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

    /**
     * 转换为DTO
     */
    private ChargeOrderDetailDTO convertToDTO(ChargeOrderEntity entity) {
        ChargeOrderDetailDTO dto = new ChargeOrderDetailDTO();
        
        dto.setAgent(entity.getAgent());
        dto.setAmount(entity.getAmount() != null ? new java.math.BigDecimal(entity.getAmount()) : null);
        dto.setChannel(entity.getChannel());
        dto.setChannelType(entity.getChannelType());
        dto.setChannelid(entity.getChannelid());
        dto.setChargeId(entity.getChargeId());
        dto.setCreateTime(formatDate(entity.getCreateTime()));
        dto.setInfoIp(entity.getInfoIp());
        dto.setMerchantid(entity.getMerchantid());
        dto.setMerchantname(entity.getMerchantname());
        dto.setMobile(entity.getMobile());
        dto.setOperCode(entity.getOperCode());
        dto.setOrderno(entity.getOrderno());
        dto.setPlatform(entity.getPlatform());
        dto.setRealAmount(entity.getRealAmount() != null ? new java.math.BigDecimal(entity.getRealAmount()) : null);
        dto.setRemark(entity.getRemark());
        dto.setSourcetypeName(entity.getSourcetypeName());
        dto.setState(entity.getState());
        dto.setThreeorderNo(entity.getThreeorderNo());
        dto.setURealAmout(entity.getURealAmout() != null ? new java.math.BigDecimal(entity.getURealAmout()) : null);
        dto.setUamout(entity.getUamout() != null ? new java.math.BigDecimal(entity.getUamout()) : null);
        dto.setUprice(entity.getUprice() != null ? new java.math.BigDecimal(entity.getUprice()) : null);
        dto.setUserId(entity.getUserId());
        dto.setWalletAddr(entity.getWalletAddr());
        dto.setWalletId(entity.getWalletId());
        
        // 设置时间字段
        dto.setChargeTime(formatDate(entity.getChargeTime()));
        dto.setUsercreateTime(formatDate(entity.getCreateTime()));
        
        // 设置默认值 TODO
        dto.setAgentName(""); // 需要关联查询
        dto.setBiaoqian(""); // 需要关联查询
        dto.setInviteCodeStatus(0); // 需要关联查询
        dto.setLiebian(0); // 需要关联查询
        dto.setSalesmanName(""); // 需要关联查询
        dto.setSalesmanid(0L); // 需要关联查询
        dto.setSuccesscnt(0); // 需要关联查询
        dto.setSuccesscz(0); // 需要关联查询
        
        return dto;
    }

    /**
     * 格式化日期
     */
    private String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
}
