package io.renren.service.impl;

import io.renren.common.service.impl.BaseServiceImpl;
import io.renren.dao.ChargeOrderDao;
import io.renren.dto.ChargeOrderDetailDTO;
import io.renren.dto.ChargePageData;
import io.renren.entity.ChargeOrderEntity;
import io.renren.enums.ChargeTypeEnum;
import io.renren.service.ChargeOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 充值订单服务实现类
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Service
public class ChargeOrderServiceImpl extends BaseServiceImpl<ChargeOrderDao, ChargeOrderEntity> implements ChargeOrderService {

    @Autowired
    private ChargeOrderDao chargeOrderDao;

    @Override
    public ChargeOrderDetailDTO getChargeOrderDetail(Long userId) {
        try {
            // 获取用户最新的充值订单
            List<ChargeOrderEntity> orders = chargeOrderDao.selectByUserId(userId);
            if (orders == null || orders.isEmpty()) {
                return createDefaultChargeOrderDetail(userId);
            }

            // 获取最新的充值订单
            ChargeOrderEntity latestOrder = orders.get(0);
            
            // 转换为DTO
            ChargeOrderDetailDTO detailDTO = new ChargeOrderDetailDTO();
            BeanUtils.copyProperties(latestOrder, detailDTO);
            
            return detailDTO;
            
        } catch (Exception e) {
            e.printStackTrace();
            return createDefaultChargeOrderDetail(userId);
        }
    }

    @Override
    public ChargeOrderDetailDTO getChargeOrderDetailByOrderNo(String orderNo) {
        try {
            // 根据订单号查询充值订单
            ChargeOrderEntity order = chargeOrderDao.selectByOrderno(orderNo);
            if (order == null) {
                throw new RuntimeException("订单不存在: " + orderNo);
            }
            
            // 转换为DTO
            ChargeOrderDetailDTO detailDTO = new ChargeOrderDetailDTO();
            BeanUtils.copyProperties(order, detailDTO);
            
            return detailDTO;
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取充值订单详情失败: " + e.getMessage());
        }
    }

    @Override
    public List<ChargeOrderEntity> getChargeOrdersByUserId(Long userId) {
        return chargeOrderDao.selectByUserId(userId);
    }

    @Override
    public ChargeOrderEntity getChargeOrderByOrderno(String orderno) {
        return chargeOrderDao.selectByOrderno(orderno);
    }

    @Override
    public ChargeOrderEntity getChargeOrderByThreeOrderNo(String threeorderNo) {
        return chargeOrderDao.selectByThreeOrderNo(threeorderNo);
    }

    @Override
    public List<ChargeOrderEntity> getChargeOrdersByState(Integer state) {
        return chargeOrderDao.selectByState(state);
    }

    @Override
    public List<ChargeOrderEntity> getChargeOrdersByParams(Map<String, Object> params) {
        return chargeOrderDao.selectByParams(params);
    }

    @Override
    public Long getTotalChargeAmountByUserId(Long userId) {
        return chargeOrderDao.selectTotalAmountByUserId(userId);
    }



    @Override
    public ChargePageData getChargePageData(Long userId, Integer page, Integer limit) {
        try {
            // 使用MyBatis-Plus分页查询
            Page<ChargeOrderEntity> pageParam = new Page<>(page, limit);
            
            // 构建查询条件
            QueryWrapper<ChargeOrderEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId)
                       .orderByDesc("create_time");
            
            // 执行分页查询
            Page<ChargeOrderEntity> result = chargeOrderDao.selectPage(pageParam, queryWrapper);
            
            // 转换为DTO列表
            List<ChargeOrderDetailDTO> dtoList = convertToDTOList(result.getRecords());
            
            // 计算汇总信息
            Map<String, Object> sum = calculateSum(result.getRecords());
            
            // 构建分页数据
            ChargePageData pageData = new ChargePageData();
            pageData.setList(dtoList);
            pageData.setSum(sum);
            pageData.setTotal((int) result.getTotal());
            
            return pageData;
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取充值分页数据失败: " + e.getMessage());
        }
    }

    /**
     * 转换为DTO列表
     */
    private List<ChargeOrderDetailDTO> convertToDTOList(List<ChargeOrderEntity> chargeOrders) {
        List<ChargeOrderDetailDTO> dtoList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        for (ChargeOrderEntity order : chargeOrders) {
            ChargeOrderDetailDTO dto = new ChargeOrderDetailDTO();
            BeanUtils.copyProperties(order, dto);
            dtoList.add(dto);
        }
        
        return dtoList;
    }

    /**
     * 计算汇总信息
     */
    private Map<String, Object> calculateSum(List<ChargeOrderEntity> chargeOrders) {
        Map<String, Object> sum = new HashMap<>();
        long totalAmount = 0;
        long totalRealAmount = 0;
        long totalUAmount = 0;
        int successCount = 0;
        int pendingCount = 0;
        int failedCount = 0;
        
        for (ChargeOrderEntity order : chargeOrders) {
            totalAmount += order.getAmount() != null ? order.getAmount() : 0;
            totalRealAmount += order.getRealAmount() != null ? order.getRealAmount() : 0;
            totalUAmount += order.getUamout() != null ? order.getUamout() : 0;
            
            if (order.getState() != null) {
                switch (order.getState()) {
                    case 0:
                        pendingCount++;
                        break;
                    case 1:
                        successCount++;
                        break;
                    case 2:
                        failedCount++;
                        break;
                }
            }
        }
        
        sum.put("totalAmount", totalAmount);
        sum.put("totalRealAmount", totalRealAmount);
        sum.put("totalUAmount", totalUAmount);
        sum.put("successCount", successCount);
        sum.put("pendingCount", pendingCount);
        sum.put("failedCount", failedCount);
        sum.put("totalCount", chargeOrders.size());
        
        return sum;
    }

    @Override
    public String createChargeOrder(Long userId, Long amount, Integer chargeType, Long channelid) {
        try {
            // 生成订单号
            String orderno = generateOrderNo();
            
            // 创建充值订单实体
            ChargeOrderEntity chargeOrder = new ChargeOrderEntity();
            chargeOrder.setUserId(userId);
            chargeOrder.setAmount(amount);
            chargeOrder.setRealAmount(amount);
            chargeOrder.setOrderno(orderno);
            chargeOrder.setState(0); // 待审核
            chargeOrder.setCreateTime(new Date());
            chargeOrder.setUpdateTime(new Date());
            
            // 设置支付通道ID
            chargeOrder.setChannelid(channelid);
            
            // 根据充值类型设置相关字段
            setChargeTypeFields(chargeOrder, chargeType);
            
            // 保存到数据库
            chargeOrderDao.insert(chargeOrder);
            
            return orderno;
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("创建充值订单失败: " + e.getMessage());
        }
    }

    /**
     * 生成订单号
     */
    private String generateOrderNo() {
        return "CHG" + System.currentTimeMillis() + String.format("%04d", (int)(Math.random() * 10000));
    }

    /**
     * 根据充值类型设置相关字段
     */
    private void setChargeTypeFields(ChargeOrderEntity chargeOrder, Integer chargeType) {
        ChargeTypeEnum chargeTypeEnum = ChargeTypeEnum.getByCode(chargeType);
        if (chargeTypeEnum != null) {
            chargeOrder.setChannel(chargeTypeEnum.getChannel());
            chargeOrder.setChannelType(chargeTypeEnum.getChannelType());
            chargeOrder.setSourcetypeName(chargeTypeEnum.getName());
        } else {
            chargeOrder.setChannel("unknown");
            chargeOrder.setChannelType("unknown");
            chargeOrder.setSourcetypeName("未知渠道");
        }
    }

    /**
     * 创建默认的充值订单详情（当用户没有充值记录时）
     */
    private ChargeOrderDetailDTO createDefaultChargeOrderDetail(Long userId) {
        ChargeOrderDetailDTO detailDTO = new ChargeOrderDetailDTO();
        detailDTO.setAmount(0L);
        detailDTO.setURealAmout(0L);
        detailDTO.setUprice(0L);
        detailDTO.setWalletAddr("");
        detailDTO.setWalletId(0L);
        return detailDTO;
    }
}
