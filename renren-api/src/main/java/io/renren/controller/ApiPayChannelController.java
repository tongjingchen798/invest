package io.renren.controller;

import io.renren.common.utils.Result;
import io.renren.dao.PayChannelDao;
import io.renren.dao.SysParamsDao;
import io.renren.dto.PayChannelDTO;
import io.renren.entity.PayChannelEntity;
import io.renren.service.USDTRechargeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 支付通道管理接口
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/api/paychannel")
@Api(tags = "支付通道管理接口")
public class ApiPayChannelController {

    @Autowired
    private PayChannelDao payChannelDao;
    
    @Autowired
    private SysParamsDao sysParamsDao;
    
    @Autowired
    private USDTRechargeService usdtRechargeService;

    @GetMapping("getPay")
    @ApiOperation("查询支付方式")
    public Result<Map<String, Object>> getPayChannels() {
            // 查询所有上架的支付通道
            List<PayChannelEntity> activeChannels = payChannelDao.selectAllActiveChannels();
            if (activeChannels == null || activeChannels.isEmpty()) {
                // 如果没有数据，返回空结果
                Map<String, Object> emptyResult = new HashMap<>();
                return new Result<Map<String, Object>>().ok(emptyResult);
            }
            // 转换为DTO
            List<PayChannelDTO> channelDTOs = activeChannels.stream()
                .map(this::convertToPayChannelDTO)
                .collect(Collectors.toList());
            
            // 按照通道类型分组
            Map<String, List<PayChannelDTO>> groupedChannels = channelDTOs.stream()
                .collect(Collectors.groupingBy(PayChannelDTO::getChannelType));
            
            // 构建响应数据，按照要求的格式
            Map<String, Object> result = new HashMap<>();
            
            // 确保所有通道类型都存在，即使没有数据也返回空数组
            result.put("UPI", groupedChannels.getOrDefault("UPI", new ArrayList<>()));
            result.put("SWIPE", groupedChannels.getOrDefault("SWIPE", new ArrayList<>()));
            
            // 处理USDT通道
            List<PayChannelDTO> usdtChannels = groupedChannels.getOrDefault("USDT", new ArrayList<>());
            if (!usdtChannels.isEmpty()) {
                // USDT不为空时，从sys_params表中查询USDT相关参数
                String usdtSysPrice = sysParamsDao.getValueByCode("usdtsysprice");
                String usdtRealPrice = sysParamsDao.getValueByCode("usdtrealprice");
                
                // 为每个USDT通道设置从系统参数表查询的值
                for (PayChannelDTO usdtChannel : usdtChannels) {
                    if (usdtSysPrice != null) {
                        usdtChannel.setUsdtGiftRatio(usdtSysPrice);
                    }
                    if (usdtRealPrice != null) {
                        usdtChannel.setUsdtLocalCurrencyRate(usdtRealPrice);
                    }
                }
            }
            result.put("USDT", usdtChannels);
            

            return new Result<Map<String, Object>>().ok(result);
    }
    
    /**
     * 将支付通道实体转换为DTO
     */
    private PayChannelDTO convertToPayChannelDTO(PayChannelEntity entity) {
        PayChannelDTO dto = new PayChannelDTO();
        dto.setChannelid(Long.valueOf(entity.getChannelid()));
        dto.setChannelName(entity.getChannelName());
        dto.setChannelType(entity.getChannelType());
        dto.setChargeorwithdraw(entity.getChargeorwithdraw());
        dto.setMerchantid(entity.getMerchantid());
        dto.setStatus(entity.getStatus());
        dto.setUsdtGiftRatio(entity.getUsdtGiftRatio());
        dto.setUsdtLocalCurrencyRate(entity.getUsdtLocalCurrencyRate());
        
        // 格式化日期
        if (entity.getCreateDate() != null) {
            dto.setCreateDate(entity.getCreateDate().toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDateTime()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        }
        
        if (entity.getUpdateDate() != null) {
            dto.setUpdateDate(entity.getUpdateDate().toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDateTime()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        }
        
        return dto;
    }
    
    @PostMapping("generateUSDTQRCode")
    @ApiOperation("生成USDT充值二维码")
    public Result<Map<String, Object>> generateUSDTQRCode(
            @ApiParam(value = "充值金额（可选）", required = false) @RequestParam(required = false) String amount) {
        try {
            Map<String, Object> result = usdtRechargeService.generateUSDTRechargeQRCode(amount);
            return new Result<Map<String, Object>>().ok(result);
        } catch (Exception e) {
            return new Result<Map<String, Object>>().error("生成USDT充值二维码失败: " + e.getMessage());
        }
    }
    
    @GetMapping("getUSDTAddress")
    @ApiOperation("获取USDT-TRC20地址")
    public Result<String> getUSDTAddress() {
        try {
            String usdtAddress = usdtRechargeService.getUSDTAddress();
            if (usdtAddress == null || usdtAddress.isEmpty()) {
                return new Result<String>().error("USDT-TRC20地址未配置");
            }
            return new Result<String>().ok(usdtAddress);
        } catch (Exception e) {
            return new Result<String>().error("获取USDT地址失败: " + e.getMessage());
        }
    }
}
