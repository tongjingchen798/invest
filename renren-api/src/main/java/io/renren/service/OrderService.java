package io.renren.service;

import io.renren.dto.PlaceOrderDTO;

import java.util.Map;

/**
 * 下单服务接口
 * 
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
public interface OrderService {
	
	/**
	 * 下单
	 * @param dto 下单参数
	 * @param userId 用户ID
	 * @return 下单结果
	 */
	Map<String, String> placeOrder(PlaceOrderDTO dto, Long userId);
	

	/**
	 * 计算投资金额
	 * @param dto 下单参数
	 * @return 计算后的金额信息
	 */
	Map<String, Object> calculateAmount(PlaceOrderDTO dto);
	
	/**
	 * 生成订单号
	 * @return 订单号
	 */
	String generateOrderNumber();
}
