package io.renren.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.exception.ErrorCode;
import io.renren.common.exception.RenException;
import io.renren.common.service.impl.BaseServiceImpl;
import io.renren.dao.RedPacketDao;
import io.renren.dao.RedPacketRecordDao;
import io.renren.entity.RedPacketEntity;
import io.renren.entity.RedPacketRecordEntity;
import io.renren.service.RedPacketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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

    private static final Logger logger = LoggerFactory.getLogger(RedPacketServiceImpl.class);

    @Autowired
    private RedPacketRecordDao redPacketRecordDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> receiveRedPacket(String password, Long userId, String userName, String ipAddress, String deviceInfo) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 参数验证
            if (!StringUtils.hasText(password)) {
                throw new RenException(ErrorCode.RED_PACKET_PASSWORD_EMPTY);
            }
            if (userId == null || userId <= 0) {
                throw new RenException(ErrorCode.RED_PACKET_USER_INVALID);
            }
            if (!StringUtils.hasText(userName)) {
                throw new RenException(ErrorCode.RED_PACKET_USERNAME_EMPTY);
            }

            logger.info("用户[{}]尝试领取红包，口令码: {}", userName, password);

            // 1. 根据口令码查询红包
            QueryWrapper<RedPacketEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("password", password);
            RedPacketEntity redPacket = baseDao.selectOne(queryWrapper);

            if (redPacket == null) {
                logger.warn("红包不存在，口令码: {}", password);
                throw new RenException(ErrorCode.RED_PACKET_NOT_EXISTS);
            }

            // 2. 检查红包状态
            if (redPacket.getStatus() == 1) {
                logger.warn("红包已过期，红包ID: {}, 状态: {}", redPacket.getId(), redPacket.getStatus());
                throw new RenException(ErrorCode.RED_PACKET_EXPIRED);
            }

//            // 3. 检查红包是否有效
//            Date now = new Date();
//            if (redPacket.getStartTime() != null && now.before(redPacket.getStartTime())) {
//                logger.warn("红包未到开始时间，红包ID: {}, 开始时间: {}", redPacket.getId(), redPacket.getStartTime());
//                throw new RenException(ErrorCode.RED_PACKET_NOT_STARTED);
//            }
//            if (redPacket.getEndTime() != null && now.after(redPacket.getEndTime())) {
//                logger.warn("红包已过期，红包ID: {}, 结束时间: {}", redPacket.getId(), redPacket.getEndTime());
//                throw new RenException(ErrorCode.RED_PACKET_EXPIRED);
//            }

            // 4. 检查是否已领完
            if (redPacket.getCollated() >= redPacket.getNumber()) {
                logger.warn("红包已领完，红包ID: {}, 已领取: {}, 总数: {}", redPacket.getId(), redPacket.getCollated(), redPacket.getNumber());
                throw new RenException(ErrorCode.RED_PACKET_ALL_RECEIVED);
            }

            // 5. 检查用户是否已领取过
            QueryWrapper<RedPacketRecordEntity> recordQuery = new QueryWrapper<>();
            recordQuery.eq("red_packet_id", redPacket.getId())
                    .eq("user_id", userId);
            RedPacketRecordEntity existRecord = redPacketRecordDao.selectOne(recordQuery);

            if (existRecord != null) {
                logger.warn("用户已领取过此红包，用户ID: {}, 红包ID: {}", userId, redPacket.getId());
                throw new RenException(ErrorCode.RED_PACKET_ALREADY_RECEIVED);
            }

            // 6. 更新红包已领取数量（使用乐观锁）
            baseDao.updateCollated(redPacket.getId());
            Date now = new Date();
            // 7. 创建领取记录
            RedPacketRecordEntity record = new RedPacketRecordEntity();
            record.setRedPacketId(redPacket.getId());
            record.setUserId(userId);
            record.setUserName(userName);
            record.setAmount(redPacket.getAmount());
            record.setReceiveTime(now);
            record.setIpAddress(ipAddress);
            record.setDeviceInfo(deviceInfo);
            record.setStatus(1);

            int insertResult = redPacketRecordDao.insert(record);
            if (insertResult <= 0) {
                logger.error("创建红包领取记录失败，用户ID: {}, 红包ID: {}", userId, redPacket.getId());
                throw new RenException(ErrorCode.RED_PACKET_RECORD_CREATE_FAILED);
            }

            // 8. 记录成功日志
            logger.info("用户[{}]成功领取红包，红包ID: {}, 金额: {}", userName, redPacket.getId(), redPacket.getAmount());

            // 9. 返回领取结果
            result.put("success", true);
            result.put("message", "领取成功");
            result.put("amount", redPacket.getAmount());
            result.put("redPacketTitle", redPacket.getTitle());
            result.put("redPacketId", redPacket.getId());
            result.put("receiveTime", now);

            return result;
            
        } catch (RenException e) {
            logger.error("红包领取业务异常: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("红包领取系统异常，用户ID: {}, 口令码: {}", userId, password, e);
            throw new RenException(ErrorCode.SYSTEM_EXCEPTION);
        }
    }
}