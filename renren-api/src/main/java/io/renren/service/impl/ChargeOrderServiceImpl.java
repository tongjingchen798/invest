package io.renren.service.impl;

import io.renren.common.service.impl.BaseServiceImpl;
import io.renren.dao.ChargeOrderDao;
import io.renren.dao.PayChannelDao;
import io.renren.dao.PayMerchantDao;
import io.renren.dao.UserDao;
import io.renren.dto.ChargeOrderDetailDTO;
import io.renren.dto.ChargePageData;
import io.renren.dto.UserChargeInfoDTO;
import io.renren.entity.ChargeOrderEntity;
import io.renren.entity.PayChannelEntity;
import io.renren.entity.PayMerchantEntity;
import io.renren.entity.UserEntity;
import io.renren.enums.ChargeTypeEnum;
import io.renren.service.ChargeOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    @Autowired
    private UserDao userDao;

    @Autowired
    private PayChannelDao payChannelDao;

    @Autowired
    private PayMerchantDao  payMerchantDao;

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
            // 创建分页对象
            Page<ChargeOrderEntity> pageParam = new Page<>(page, limit);
            
            // 构建查询条件
            QueryWrapper<ChargeOrderEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId);
            queryWrapper.orderByDesc("create_time");
            
            // 执行分页查询
            Page<ChargeOrderEntity> result = chargeOrderDao.selectPage(pageParam, queryWrapper);
            
            // 转换为分页数据
            ChargePageData pageData = new ChargePageData();
            pageData.setTotal((int) result.getTotal());
            pageData.setList(convertToUserChargeInfoList(result.getRecords()));
            pageData.setSum(new HashMap<>());
            
            return pageData;
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取充值分页数据失败: " + e.getMessage());
        }
    }

    /**
     * 转换为用户充值信息列表
     */
    private List<UserChargeInfoDTO> convertToUserChargeInfoList(List<ChargeOrderEntity> orders) {
        List<UserChargeInfoDTO> result = new ArrayList<>();
        if (orders == null || orders.isEmpty()) {
            return result;
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        for (ChargeOrderEntity order : orders) {
            UserChargeInfoDTO dto = new UserChargeInfoDTO();
            
            // 基本信息
            dto.setChargeId(order.getChargeId() != null ? order.getChargeId().intValue() : 0);
            dto.setOrderno(order.getOrderno());
            dto.setThreeorderNo(order.getThreeorderNo());
            dto.setAmount(order.getAmount() != null ? new BigDecimal(order.getAmount()) : BigDecimal.ZERO);
            dto.setState(order.getState());
            dto.setCreateTime(order.getCreateTime() != null ? sdf.format(order.getCreateTime()) : "");
            dto.setChargeTime(order.getChargeTime() != null ? sdf.format(order.getChargeTime()) : "");
            
            // 用户信息
            dto.setUserId(order.getUserId());
            dto.setMobile(order.getMobile());
            dto.setInfoIp(order.getInfoIp());
            
            // 渠道信息
            dto.setChannel(order.getChannel());
            dto.setChannelType(order.getChannelType());
            dto.setChannelid(order.getChannelid());
            dto.setPlatform(order.getPlatform());
            dto.setSourcetypeName(order.getSourcetypeName());
            
            // 商户信息
            dto.setMerchantid(order.getMerchantid() != null ? order.getMerchantid().intValue() : 0);
            dto.setMerchantname(order.getMerchantname());
            
            // 代理信息
            dto.setAgent(order.getAgent());
            dto.setAgentName("");
            
            // 业务员信息
            dto.setSalesmanid(order.getSalesmanid());
            dto.setSalesmanName("");
            
            // 其他字段
//            dto.setBiaoqian(order.getBiaoqian());
//            dto.setLiebian(order.getLiebian());
//            dto.setInviteCodeStatus(order.getInviteCodeStatus());
            dto.setOperCode(order.getOperCode());
            dto.setRemark(order.getRemark());
            
            // USDT相关
            dto.setUprice(order.getUprice() != null ? new BigDecimal(order.getUprice()) : BigDecimal.ZERO);
            dto.setUamout(order.getUamout() != null ? new BigDecimal(order.getUamout()) : BigDecimal.ZERO);
            dto.setURealAmout(order.getURealAmout() != null ? new BigDecimal(order.getURealAmout()) : BigDecimal.ZERO);
            
            // 钱包信息
            dto.setWalletAddr(order.getWalletAddr());
            dto.setWalletId(order.getWalletId() != null ? order.getWalletId().intValue() : 0);
            
            // 真实金额
            dto.setRealAmount(order.getRealAmount());
            
            // 成功统计（暂时设置为默认值）
            dto.setSuccesscnt(1);
            dto.setSuccesscz(1);
            
            // 用户创建时间（暂时设置为空）
            dto.setUsercreateTime("");
            
            result.add(dto);
        }
        
        return result;
    }

    /**
     * 计算汇总数据
     */
    private Map<String, Object> calculateSummary(List<ChargeOrderEntity> orders) {
        Map<String, Object> summary = new HashMap<>();
        
        if (orders != null && !orders.isEmpty()) {
            long totalAmount = 0;
            long totalRealAmount = 0;
            long totalUAmount = 0;
            
            for (ChargeOrderEntity order : orders) {
                if (order.getAmount() != null) totalAmount += order.getAmount();
                if (order.getRealAmount() != null) totalRealAmount += order.getRealAmount();
                if (order.getUamout() != null) totalUAmount += order.getUamout();
            }
            
            summary.put("totalAmount", totalAmount);
            summary.put("totalRealAmount", totalRealAmount);
            summary.put("totalUAmount", totalUAmount);
            summary.put("totalCount", orders.size());
        } else {
            summary.put("totalAmount", 0);
            summary.put("totalRealAmount", 0);
            summary.put("totalUAmount", 0);
            summary.put("totalCount", 0);
        }
        
        return summary;
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
            chargeOrder.setChargeTime(new Date());
            chargeOrder.setChannelid(channelid);
            PayChannelEntity channelEntity=payChannelDao.selectById(channelid);
            chargeOrder.setMerchantid(channelEntity.getMerchantid());
            chargeOrder.setChannelType(channelEntity.getChannelType());
            UserEntity user=userDao.selectById(userId);
            chargeOrder.setMobile(user.getMobile());
            chargeOrder.setAgent(user.getAgent());
            chargeOrder.setSalesmanid(user.getSalesmanid());
            chargeOrder.setRemark("前端充值");
            PayChannelEntity payChannelEntity=payChannelDao.selectById(chargeOrder.getChannelid());
            if(payChannelEntity!=null){
                chargeOrder.setMerchantid(payChannelEntity.getMerchantid());
                PayMerchantEntity payMerchantEntity=payMerchantDao.selectById(payChannelEntity.getMerchantid());
                if(payMerchantEntity!=null){
                    chargeOrder.setMerchantname(payMerchantEntity.getMerchantname());
                }
            }

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
