package io.renren.service.impl;

import io.renren.dao.WithdrawOrderDao;
import io.renren.service.WithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 提现服务实现类
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Service
public class WithdrawServiceImpl implements WithdrawService {

    @Autowired
    private WithdrawOrderDao withdrawOrderDao;

    @Override
    public Map<String, Object> checkFirstWithdraw(Long userId) {
        try {
            Map<String, Object> result = new HashMap<>();
            
            // 查询用户提现记录数量
            Long withdrawCount = withdrawOrderDao.selectCountByUserId(userId);
            
            // 判断是否首次提现
            boolean isFirstWithdraw = withdrawCount == 0;
            
            // 构建返回结果
            result.put("isFirstWithdraw", isFirstWithdraw);
            result.put("withdrawCount", withdrawCount);
            result.put("userId", userId);
            
            return result;
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("检查首次提现状态失败: " + e.getMessage());
        }
    }
}
