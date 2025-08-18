package io.renren.controller;

import io.renren.common.utils.Result;
import io.renren.dao.PayChannelDao;
import io.renren.dto.PayChannelDTO;
import io.renren.entity.PayChannelEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("getPay")
    @ApiOperation("查询支付方式")
    public Result<Map<String, Object>> getPayChannels() {
        try {
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
            result.put("USDT", groupedChannels.getOrDefault("USDT", new ArrayList<>()));
            
            return new Result<Map<String, Object>>().ok(result);
            
        } catch (Exception e) {
            return new Result<Map<String, Object>>().error("查询支付方式失败: " + e.getMessage());
        }
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
}
