/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.service.impl.BaseServiceImpl;
import io.renren.dao.RedPacketDao;
import io.renren.dao.RedPacketRecordDao;
import io.renren.entity.RedPacketEntity;
import io.renren.entity.RedPacketRecordEntity;
import io.renren.service.RedPacketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 红包主表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
@Service
public class RedPacketServiceImpl extends BaseServiceImpl<RedPacketDao, RedPacketEntity> implements RedPacketService {

    @Autowired
    private RedPacketRecordDao redPacketRecordDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> receiveRedPacket(String password, Long userId, String userName, String ipAddress, String deviceInfo) {
        Map<String, Object> result = new HashMap<>();

        // 1. 根据口令码查询红包
        QueryWrapper<RedPacketEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("password", password);
        RedPacketEntity redPacket = baseDao.selectOne(queryWrapper);

        if (redPacket == null) {
            result.put("success", false);
            result.put("message", "红包不存在");
            return result;
        }

        // 2. 检查红包状态
        if (redPacket.getStatus() != 0) {
            result.put("success", false);
            result.put("message", "红包已过期或已领完");
            return result;
        }

        // 3. 检查是否已过期
        if (redPacket.getExpireTime() != null && new Date().after(redPacket.getExpireTime())) {
            result.put("success", false);
            result.put("message", "红包已过期");
            return result;
        }

        // 4. 检查是否已领完
        if (redPacket.getCollated() >= redPacket.getNumber()) {
            result.put("success", false);
            result.put("message", "红包已领完");
            return result;
        }

        // 5. 检查用户是否已领取过
        QueryWrapper<RedPacketRecordEntity> recordQuery = new QueryWrapper<>();
        recordQuery.eq("red_packet_id", redPacket.getId())
                .eq("user_id", userId);
        RedPacketRecordEntity existRecord = redPacketRecordDao.selectOne(recordQuery);

        if (existRecord != null) {
            result.put("success", false);
            result.put("message", "您已领取过此红包");
            return result;
        }

        // 6. 更新红包已领取数量
        int updateResult = baseDao.updateCollated(redPacket.getId());
        if (updateResult == 0) {
            result.put("success", false);
            result.put("message", "红包已被抢完");
            return result;
        }

        // 7. 创建领取记录
        RedPacketRecordEntity record = new RedPacketRecordEntity();
        record.setRedPacketId(redPacket.getId());
        record.setUserId(userId);
        record.setUserName(userName);
        record.setAmount(redPacket.getAmount());
        record.setReceiveTime(new Date());
        record.setIpAddress(ipAddress);
        record.setDeviceInfo(deviceInfo);
        record.setStatus(1);

        redPacketRecordDao.insert(record);

        // 8. 返回领取结果
        result.put("success", true);
        result.put("message", "领取成功");
        result.put("amount", redPacket.getAmount());
        result.put("redPacketTitle", redPacket.getTitle());

        return result;
    }
}