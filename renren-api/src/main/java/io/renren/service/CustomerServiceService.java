package io.renren.service;

import io.renren.dto.CustomerServiceDTO;

import java.util.List;

/**
 * 客服号服务接口
 *
 * @author renren
 * @since 2024-01-01
 */
public interface CustomerServiceService {

    /**
     * 获取所有客服号列表
     * 查询sys_user表，type=2且status=1的数据
     *
     * @return 客服号列表
     */
    List<CustomerServiceDTO> getAllCustomerServices();
}
