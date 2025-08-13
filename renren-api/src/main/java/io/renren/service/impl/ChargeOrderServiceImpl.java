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
            
            // 格式化日期字段
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (latestOrder.getChargeTime() != null) {
                detailDTO.setChargeTime(sdf.format(latestOrder.getChargeTime()));
            }
            if (latestOrder.getCreateTime() != null) {
                detailDTO.setCreateTime(sdf.format(latestOrder.getCreateTime()));
            }
            
            return detailDTO;
            
        } catch (Exception e) {
            e.printStackTrace();
            return createDefaultChargeOrderDetail(userId);
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
    public Long getChargeCountByUserId(Long userId) {
        return chargeOrderDao.selectCountByUserId(userId);
    }

    @Override
    public ChargePageData getChargePageData(Long userId, Integer page, Integer limit) {
        try {
            // 计算偏移量
            int offset = (page - 1) * limit;
            
            // 查询总数
            Long total = chargeOrderDao.selectCountByUserId(userId);
            
            // 查询分页数据
            List<ChargeOrderEntity> chargeOrders = chargeOrderDao.selectPageByUserId(userId, offset, limit);
            
            // 转换为DTO列表
            List<ChargeOrderDetailDTO> dtoList = convertToDTOList(chargeOrders);
            
            // 计算汇总信息
            Map<String, Object> sum = calculateSum(chargeOrders);
            
            // 构建分页数据
            ChargePageData pageData = new ChargePageData();
            pageData.setList(dtoList);
            pageData.setSum(sum);
            pageData.setTotal(total.intValue());
            
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
            
            // 格式化日期字段
            if (order.getChargeTime() != null) {
                dto.setChargeTime(sdf.format(order.getChargeTime()));
            }
            if (order.getCreateTime() != null) {
                dto.setCreateTime(sdf.format(order.getCreateTime()));
            }
            
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
        
        // 设置默认值
        detailDTO.setAgent("");
        detailDTO.setAmount(0L);
        detailDTO.setChannel("");
        detailDTO.setChannelType("");
        detailDTO.setChannelid(0L);
        detailDTO.setChargeId(0L);
        detailDTO.setChargeTime("");
        detailDTO.setCreateTime("");
        detailDTO.setInfoIp("");
        detailDTO.setMerchantid(0L);
        detailDTO.setMerchantname("");
        detailDTO.setMobile("");
        detailDTO.setOperCode("");
        detailDTO.setOrderno("");
        detailDTO.setPlatform("");
        detailDTO.setRealAmount(0L);
        detailDTO.setRemark("");
        detailDTO.setSalesmanid("");
        detailDTO.setSourcetypeName("");
        detailDTO.setState(0);
        detailDTO.setThreeorderNo("");
        detailDTO.setURealAmout(0L);
        detailDTO.setUamout(0L);
        detailDTO.setUprice(0L);
        detailDTO.setUserId(userId);
        detailDTO.setWalletAddr("");
        detailDTO.setWalletId(0L);
        
        return detailDTO;
    }
}
