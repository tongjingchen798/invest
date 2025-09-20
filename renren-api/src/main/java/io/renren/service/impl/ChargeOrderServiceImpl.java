package io.renren.service.impl;

import io.renren.common.exception.ErrorCode;
import io.renren.common.exception.RenException;
import io.renren.common.service.impl.BaseServiceImpl;
import io.renren.dao.*;
import io.renren.dto.*;
import io.renren.entity.*;
import io.renren.enums.ChargeTypeEnum;
import io.renren.service.ChargeOrderService;
import io.renren.service.QePayPaymentService;
import io.renren.service.WePayPaymentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

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

    @Autowired
    private WePayPaymentService wePayPaymentService;

    @Autowired
    private SysParamsDao sysParamsDao;

    @Autowired
    private UAddressConfigDao uAddressConfigDao;

    @Autowired
    private QePayPaymentService qePayPaymentService;

    @Override
    public ChargeOrderDetailDTO getChargeOrderDetail(Long userId) {
        try {
            // 获取用户最新的充值订单
            List<ChargeOrderEntity> orders = chargeOrderDao.selectByUserId(userId);
            if (orders == null || orders.isEmpty()) {
                return createDefaultChargeOrderDetail();
            }

            // 获取最新的充值订单
            ChargeOrderEntity latestOrder = orders.get(0);
            
            // 转换为DTO
            ChargeOrderDetailDTO detailDTO = new ChargeOrderDetailDTO();
            BeanUtils.copyProperties(latestOrder, detailDTO);
            
            return detailDTO;
            
        } catch (Exception e) {
            e.printStackTrace();
            return createDefaultChargeOrderDetail();
        }
    }

    @Override
    public ChargeOrderDetailDTO getChargeOrderDetailByOrderNo(String orderNo) {
        try {
            // 根据订单号查询充值订单
            ChargeOrderEntity order = chargeOrderDao.selectByOrderno(orderNo);
            if (order == null) {
                throw new RenException(ErrorCode.ORDER_NOT_EXISTS);
            }
            
            // 转换为DTO
            ChargeOrderDetailDTO detailDTO = new ChargeOrderDetailDTO();
            BeanUtils.copyProperties(order, detailDTO);
            
            return detailDTO;
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RenException(ErrorCode.GET_CHARGE_ORDER_DETAIL_FAILED);
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
            pageData.setList(result.getRecords());
            pageData.setSum(new HashMap<>());
            
            return pageData;
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RenException(ErrorCode.GET_CHARGE_PAGE_DATA_FAILED);
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
        
        for (ChargeOrderEntity order : orders) {
            UserChargeInfoDTO dto = new UserChargeInfoDTO();
            
            // 基本信息
            dto.setChargeId(order.getChargeId().intValue());
            dto.setOrderno(order.getOrderno());
            dto.setThreeorderNo(order.getThreeorderNo());
            dto.setAmount(order.getAmount() != null ? new BigDecimal(order.getAmount()) : BigDecimal.ZERO);
            dto.setState(order.getState());
            dto.setCreateTime(order.getCreateTime());
            dto.setChargeTime(order.getChargeTime());
            
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
            // 业务员信息
            dto.setSalesmanid(order.getSalesmanid());
            // 其他字段
//            dto.setLiebian(order.getLiebian());
//            dto.setInviteCodeStatus(order.getInviteCodeStatus());
            dto.setOperCode(order.getOperCode());
            dto.setRemark(order.getRemark());
            
            // USDT相关
            dto.setUprice(order.getUprice());
            dto.setUamout(order.getUamout());
            dto.setURealAmout(order.getURealAmout());
            
            // 钱包信息
            dto.setWalletAddr(order.getWalletAddr());
            dto.setWalletId(order.getWalletId() != null ? order.getWalletId().intValue() : 0);
            
            // 真实金额
            dto.setRealAmount(order.getRealAmount());
            // 成功统计（暂时设置为默认值）
            dto.setSuccesscnt(1);
            dto.setSuccesscz(1);
            result.add(dto);
        }
        
        return result;
    }

//    /**
//     * 计算汇总数据
//     */
//    private Map<String, Object> calculateSummary(List<ChargeOrderEntity> orders) {
//        Map<String, Object> summary = new HashMap<>();
//
//        if (orders != null && !orders.isEmpty()) {
//            long totalAmount = 0;
//            long totalRealAmount = 0;
//            long totalUAmount = 0;
//
//            for (ChargeOrderEntity order : orders) {
//                if (order.getAmount() != null) totalAmount += order.getAmount();
//                if (order.getRealAmount() != null) totalRealAmount += order.getRealAmount();
//                if (order.getUamout() != null) totalUAmount += order.getUamout();
//            }
//
//            summary.put("totalAmount", totalAmount);
//            summary.put("totalRealAmount", totalRealAmount);
//            summary.put("totalUAmount", totalUAmount);
//            summary.put("totalCount", orders.size());
//        } else {
//            summary.put("totalAmount", 0);
//            summary.put("totalRealAmount", 0);
//            summary.put("totalUAmount", 0);
//            summary.put("totalCount", 0);
//        }
//
//        return summary;
//    }

    @Override
    public ChargeResponseDTO createChargeOrder(Long userId, Long amount, Integer chargeType, Long channelid) {
            PayChannelEntity payChannelEntity = payChannelDao.selectById(channelid);
            if (payChannelEntity == null) {
//                throw new RenException(500, "该通道已关闭,请选择其他充值通道");
                throw new RenException(500, "This channel has been closed, please choose another recharge channel");
            }
            PayMerchantEntity payMerchantEntity = payMerchantDao.selectById(payChannelEntity.getMerchantid());
            if (payMerchantEntity == null) {
                throw new RenException(500, "Merchant has been disabled");
            }
            // 生成订单号
            String orderno = generateOrderNo();
            UserEntity user=userDao.selectById(userId);
            // 创建充值订单实体
            ChargeOrderEntity chargeOrder = new ChargeOrderEntity();
            chargeOrder.setUserId(userId);
            if(chargeType==2){
                String usdtSysPrice = sysParamsDao.getValueByCode("usdtsysprice");
                String usdtRealPrice = sysParamsDao.getValueByCode("usdtrealprice");

                Long usdtSysPriceAmount=Long.parseLong(usdtSysPrice);
                Long usdtRealPriceAmount=Long.parseLong(usdtRealPrice);
                chargeOrder.setAmount(amount*usdtSysPriceAmount);
                chargeOrder.setRealAmount(amount*usdtRealPriceAmount);
                UAddressConfigEntity uAddressConfigEntity=uAddressConfigDao.selectAddrLimit();
                if(uAddressConfigEntity!=null){
                    chargeOrder.setWalletAddr(uAddressConfigEntity.getAddr());
                    chargeOrder.setWalletId(uAddressConfigEntity.getId());
                }
                chargeOrder.setUamout(new BigDecimal(amount/100));
                chargeOrder.setUprice(new BigDecimal(usdtSysPrice));
                chargeOrder.setURealAmout(new BigDecimal(usdtRealPrice));
            }else {
                chargeOrder.setAmount(amount);
                chargeOrder.setRealAmount(amount);
            }
            chargeOrder.setOrderno(orderno);
            chargeOrder.setState(0); // 待审核
            chargeOrder.setCreateTime(new Date());
            chargeOrder.setUpdateTime(new Date());
            chargeOrder.setChargeTime(new Date());
            chargeOrder.setChannelid(channelid);
            chargeOrder.setSalesmanid(user.getSalesmanid());
            PayChannelEntity channelEntity=payChannelDao.selectById(channelid);
            chargeOrder.setMerchantid(channelEntity.getMerchantid());
            chargeOrder.setChannelType(channelEntity.getChannelType());
            chargeOrder.setMobile(user.getMobile());
            chargeOrder.setAgent(user.getAgent());
            chargeOrder.setRemark("前端充值");
            chargeOrder.setMerchantname(payMerchantEntity.getMerchantname());
            // 设置支付通道ID
            chargeOrder.setChannelid(channelid);
            // 根据充值类型设置相关字段
            setChargeTypeFields(chargeOrder, chargeType);
            // 保存到数据库
            chargeOrderDao.insert(chargeOrder);

            // 构建充值响应数据
            ChargeResponseDTO responseDTO = new ChargeResponseDTO();
            responseDTO.setOrderNo(orderno);
            responseDTO.setMerchantNo(payMerchantEntity.getMerchantno());
            responseDTO.setAmount(amount);

            // 根据充值类型设置不同的响应数据
            ChargeTypeEnum chargeTypeEnum = ChargeTypeEnum.getByCode(chargeType);
            if (chargeTypeEnum != null) {
                switch (chargeTypeEnum) {
                    case USDT:
                        responseDTO.setMerchantNo(payMerchantEntity.getMerchantno());
                        responseDTO.setUsdtInfo(
                                chargeOrder.getWalletAddr(), // USDT地址
                                chargeOrder.getUamout(), // u数量
                                chargeOrder.getUprice(),   // u价格
                                chargeOrder.getURealAmout()  // 实际支付u数量
                        );
                        // USDT充值不设置payUrl
                        responseDTO.setPayUrl("");
                        responseDTO.setPOrderNo(orderno);
                        responseDTO.setErrorCode(0);
                        break;

                    case UPI:
                    case PAYTM:
                    case BANK_CARD:
                        // 其他支付方式，设置支付相关信息
                        responseDTO.setUamount(BigDecimal.ZERO);
                        responseDTO.setUprice(BigDecimal.ZERO);
                        responseDTO.setURealAmount(BigDecimal.ZERO);
                        PaymentResponseDTO paymentResponse =null;
                        if(payMerchantEntity.getMerchantCode().equals("WEPAY")) {
                            // 调用WePay支付服务创建支付订单 分转换为元
                            paymentResponse = wePayPaymentService.createPaymentOrder(
                                    user, amount / 100, orderno, payChannelEntity, payMerchantEntity);
                        }else if(payMerchantEntity.getMerchantCode().equals("QEPAY")){
                            //调用qePay
                            paymentResponse = qePayPaymentService.createPaymentOrder(
                                    user, amount / 100, orderno, payChannelEntity, payMerchantEntity);
                        }

                        // 设置支付地址
                        String payUrl = "";
                        if (paymentResponse.getData() != null) {
                            payUrl = paymentResponse.getData().getPayUrl();
                        }
                        responseDTO.setBankCardInfo(payUrl);
                        if(Objects.isNull(paymentResponse.getData())){
                            responseDTO.setPOrderNo(paymentResponse.getData().getTradeNo());
                            responseDTO.setErrorCode(0);
                        }else {
                            responseDTO.setErrorCode(500);
                        }

                        break;
                }
            }
            responseDTO.setFlag(1); //2是内部
            return responseDTO;
    }

    /**
     * 生成订单号
     */
    private String generateOrderNo() {
        return "R" + System.currentTimeMillis() + String.format("%04d", (int)(Math.random() * 10000));
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
            chargeOrder.setChannel("1");
            chargeOrder.setChannelType("unknown");
            chargeOrder.setSourcetypeName("未知渠道");
        }
    }

    /**
     * 创建默认的充值订单详情
     */
    private ChargeOrderDetailDTO createDefaultChargeOrderDetail() {
        ChargeOrderDetailDTO detailDTO = new ChargeOrderDetailDTO();
        detailDTO.setAmount(0L);
        detailDTO.setURealAmout(BigDecimal.ZERO);
        detailDTO.setUprice(BigDecimal.ZERO);
        detailDTO.setWalletAddr("");
        detailDTO.setWalletId(0L);
        return detailDTO;
    }
}
