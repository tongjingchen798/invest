package io.renren.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.dao.SysUserDao;
import io.renren.dto.CustomerServiceDTO;
import io.renren.entity.SysUserEntity;
import io.renren.service.CustomerServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 客服号服务实现类
 *
 * @author renren
 * @since 2024-01-01
 */
@Service
public class CustomerServiceServiceImpl implements CustomerServiceService {

    @Autowired
    private SysUserDao sysUserDao;

    @Override
    public List<CustomerServiceDTO> getAllCustomerServices() {
        QueryWrapper<SysUserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", 2)  // 类型为代理
                   .eq("status", 1); // 状态为正常

        // 查询sys_user表
        List<SysUserEntity> sysUsers = sysUserDao.selectList(queryWrapper);

        // 转换为DTO
        return sysUsers.stream().map(sysUser -> {
            CustomerServiceDTO dto = new CustomerServiceDTO();
            dto.setId(String.valueOf(sysUser.getId())); // 转换为String类型
            dto.setUsername(sysUser.getUsername());
            dto.setTgnumber(sysUser.getTgnumber());
            dto.setWsnumber(sysUser.getWsnumber());
            dto.setWsimage(sysUser.getWsimage());
            return dto;
        }).collect(Collectors.toList());
    }
}
