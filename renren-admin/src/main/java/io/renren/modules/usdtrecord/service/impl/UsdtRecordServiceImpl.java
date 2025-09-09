package io.renren.modules.usdtrecord.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.constant.BusinessTypeEnum;
import io.renren.common.page.PageData;
import io.renren.modules.charge.dao.ChargeOrderDao;
import io.renren.modules.charge.entity.ChargeOrderEntity;
import io.renren.modules.finance.dao.UserBalanceDetailDao;
import io.renren.modules.finance.entity.UserBalanceDetailEntity;
import io.renren.modules.usdtrecord.dao.UsdtRecordDao;
import io.renren.modules.usdtrecord.dto.UsdtRecordDTO;
import io.renren.modules.usdtrecord.entity.UsdtRecordEntity;
import io.renren.modules.usdtrecord.service.UsdtRecordService;
import io.renren.modules.member.dao.MemberDao;
import io.renren.modules.member.entity.MemberEntity;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * USDT收款记录服务实现类
 *
 * @author renren
 * @since 2024-01-01
 */
@Service
@Slf4j
public class UsdtRecordServiceImpl extends ServiceImpl<UsdtRecordDao, UsdtRecordEntity> implements UsdtRecordService {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private ChargeOrderDao chargeOrderDao;
    
    @Autowired
    private MemberDao memberDao;
    
    @Autowired
    private UserBalanceDetailDao userBalanceDetailDao;

    @Override
    public PageData<UsdtRecordDTO> page(Map<String, Object> params) {
        try {
            // 获取分页参数，提供默认值
            long current = 1;
            long size = 10;
            
            if (params.get("page") != null) {
                try {
                    current = Long.parseLong(params.get("page").toString());
                } catch (NumberFormatException e) {
                    current = 1;
                }
            }
            
            if (params.get("limit") != null) {
                try {
                    size = Long.parseLong(params.get("limit").toString());
                } catch (NumberFormatException e) {
                    size = 10;
                }
            }
            
            // 创建分页对象
            Page<UsdtRecordDTO> page = new Page<>(current, size);
            
            // 执行自定义分页查询
            IPage<UsdtRecordDTO> resultPage = baseMapper.selectUsdtRecordPage(page, params);
            
            // 直接使用查询结果，时间已在SQL中格式化
            List<UsdtRecordDTO> dtoList = resultPage.getRecords();
            
            return new PageData<>(dtoList, resultPage.getTotal());
            
        } catch (Exception e) {
            log.error("查询USDT收款记录分页数据失败", e);
            return new PageData<>(new ArrayList<>(), 0L);
        }
    }

    /**
     * 将实体转换为DTO，处理字段映射
     */
    private UsdtRecordDTO convertToDTO(UsdtRecordEntity entity) {
        UsdtRecordDTO dto = new UsdtRecordDTO();
        dto.setId(entity.getId());
        dto.setTransactionId(entity.getTransactionId());
        dto.setFromAddress(entity.getFromAddress());
        dto.setToAddress(entity.getToAddress());
        dto.setContractAddress(entity.getContractAddress());
        
        // 处理区块时间戳
        if (entity.getBlockTs() != null) {
            dto.setBlockTs(entity.getBlockTs().toString());
        }
        
        // 处理区块号
        if (entity.getBlockNumber() != null) {
            dto.setBlock(entity.getBlockNumber().toString());
        }
        
        // 处理金额字段映射：entity.amount -> dto.quant
        dto.setQuant(entity.getAmount());
        
        dto.setContractType(entity.getContractType());
        dto.setIsRisk(entity.getIsRisk());
        dto.setIsProcess(entity.getIsProcess());
        
        // 处理区块时间
        if (entity.getBlockTime() != null) {
            dto.setBlockTime(DATE_FORMAT.format(entity.getBlockTime()));
        }
        
        // 处理订单号字段映射：entity.orderNo -> dto.orderno
        dto.setOrderno(entity.getOrderNo());
        
        // 处理创建时间
        if (entity.getCreateDate() != null) {
            dto.setCreateDate(DATE_FORMAT.format(entity.getCreateDate()));
        }
        
        // 处理更新时间
        if (entity.getUpdateDate() != null) {
            dto.setUpdateDate(DATE_FORMAT.format(entity.getUpdateDate()));
        }
        
        return dto;
    }


    @Override
    public boolean matchOrder(Long id, String orderno) {
        try {
            // 查询USDT记录是否存在
            UsdtRecordEntity entity = this.getById(id);
            if (entity == null) {
                log.error("USDT记录不存在，ID: {}", id);
                return false;
            }
            
            // 查询充值订单
            ChargeOrderEntity chargeOrder = chargeOrderDao.selectByOrderNo(orderno);
            if (Objects.isNull(chargeOrder)) {
                log.error("充值订单不存在，订单号: {}", orderno);
                return false;
            }
            
            // 检查订单状态
            if (chargeOrder.getState() == 1) {
                log.error("充值订单已匹配过不允许再次执行，订单号: {}", orderno);
                return false;
            }
            
            // 更新USDT记录
            entity.setOrderNo(orderno);
            entity.setIsProcess(1);
            entity.setUpdateDate(new Date());
            boolean usdtUpdateResult = this.updateById(entity);
            
            if (!usdtUpdateResult) {
                log.error("更新USDT记录失败，ID: {}", id);
                return false;
            }
            
            // 更新充值订单状态为成功
            chargeOrder.setState(1); // 1-充值成功
            chargeOrder.setUpdateTime(new Date());
            chargeOrder.setRemark("USDT充值匹配成功 - 交易哈希: " + entity.getTransactionId());
            int orderUpdateResult = chargeOrderDao.updateById(chargeOrder);
            
            if (orderUpdateResult==0) {
                log.error("更新充值订单状态失败，订单号: {}", orderno);
                return false;
            }
            
            // 给用户增加可用余额
            boolean balanceUpdateResult = updateUserBalance(chargeOrder);
            if (!balanceUpdateResult) {
                log.error("更新用户余额失败，用户ID: {}, 订单号: {}", chargeOrder.getUserId(), orderno);
                return false;
            }
            
            // 记录余额明细
            boolean detailRecordResult = recordBalanceDetail(chargeOrder, entity);
            if (!detailRecordResult) {
                log.error("记录余额明细失败，用户ID: {}, 订单号: {}", chargeOrder.getUserId(), orderno);
                return false;
            }
            
            // 更新用户充值统计
            boolean statsUpdateResult = updateUserRechargeStats(chargeOrder);
            if (!statsUpdateResult) {
                log.error("更新用户充值统计失败，用户ID: {}, 订单号: {}", chargeOrder.getUserId(), orderno);
                return false;
            }
            
            log.info("订单匹配成功，USDT记录ID: {}, 订单号: {}, 用户ID: {}, 充值金额: {} 分", 
                    id, orderno, chargeOrder.getUserId(), chargeOrder.getAmount());
            
            return true;
            
        } catch (Exception e) {
            // 记录错误日志
            log.error("匹配订单失败，ID: {}, 订单号: {}, 错误: {}", id, orderno, e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * 更新用户余额
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserBalance(ChargeOrderEntity chargeOrder) {
        try {
            // 获取用户信息
            MemberEntity user = memberDao.selectById(chargeOrder.getUserId());
            if (user == null) {
                log.error("用户不存在，用户ID: {}", chargeOrder.getUserId());
                return false;
            }
            
            // 更新用户可用余额
            Long currentAssets = user.getAssets() != null ? user.getAssets() : 0L;
            Long newAssets = currentAssets + chargeOrder.getAmount();
            
            user.setAssets(newAssets);

            int updateResult = memberDao.updateById(user);
            if (updateResult <= 0) {
                log.error("更新用户余额失败，用户ID: {}, 当前余额: {}, 充值金额: {}", 
                        chargeOrder.getUserId(), currentAssets, chargeOrder.getAmount());
                return false;
            }
            
            log.info("用户余额更新成功，用户ID: {}, 原余额: {} 分, 充值金额: {} 分, 新余额: {} 分", 
                    chargeOrder.getUserId(), currentAssets, chargeOrder.getAmount(), newAssets);
            
            return true;
            
        } catch (Exception e) {
            log.error("更新用户余额异常，用户ID: {}, 订单号: {}", chargeOrder.getUserId(), chargeOrder.getOrderno(), e);
            return false;
        }
    }
    
    /**
     * 记录余额明细
     */
    private boolean recordBalanceDetail(ChargeOrderEntity chargeOrder, UsdtRecordEntity usdtRecord) {
        try {
            // 获取用户信息
            MemberEntity user = memberDao.selectById(chargeOrder.getUserId());
            if (user == null) {
                log.error("用户不存在，用户ID: {}", chargeOrder.getUserId());
                return false;
            }
            
            // 创建余额明细记录
            UserBalanceDetailEntity balanceDetail = new UserBalanceDetailEntity();
            balanceDetail.setUserId(chargeOrder.getUserId());
            balanceDetail.setTransactionDate(new Date());
            balanceDetail.setAgentId(user.getAgent());
            balanceDetail.setAgentName(user.getAgentName());
            balanceDetail.setBusiType(BusinessTypeEnum.ONLINE_RECHARGE.getCode()); // 11-线上充值
            balanceDetail.setChannel("USDT");
            balanceDetail.setStreamId(chargeOrder.getOrderno()); // 使用订单号作为流水ID
            balanceDetail.setUseAmount(chargeOrder.getAmount()); // 充值金额
            balanceDetail.setOriginalAmount(user.getAssets() != null ? user.getAssets() - chargeOrder.getAmount() : 0L); // 充值前余额
            balanceDetail.setTransactionAmount(user.getAssets() != null ? user.getAssets() : chargeOrder.getAmount()); // 充值后余额
            balanceDetail.setRemarks("USDT充值成功 - 订单号: " + chargeOrder.getOrderno() + 
                    ", 交易哈希: " + usdtRecord.getTransactionId() + 
                    ", 转账地址: " + usdtRecord.getFromAddress());
            balanceDetail.setSalesmanName(user.getSalesmanName());
            balanceDetail.setSalesmanId(user.getSalesmanid());
            balanceDetail.setStatus(1); // 1-正常
            balanceDetail.setCreateDate(new Date());
            balanceDetail.setUpdateDate(new Date());
            
            // 插入余额明细记录
            int insertResult = userBalanceDetailDao.insert(balanceDetail);
            if (insertResult <= 0) {
                log.error("插入余额明细失败，用户ID: {}, 订单号: {}", chargeOrder.getUserId(), chargeOrder.getOrderno());
                return false;
            }
            
            log.info("余额明细记录成功，用户ID: {}, 订单号: {}, 充值金额: {} 分", 
                    chargeOrder.getUserId(), chargeOrder.getOrderno(), chargeOrder.getAmount());
            
            return true;
            
        } catch (Exception e) {
            log.error("记录余额明细异常，用户ID: {}, 订单号: {}", chargeOrder.getUserId(), chargeOrder.getOrderno(), e);
            return false;
        }
    }
    
    /**
     * 更新用户充值统计
     */
    private boolean updateUserRechargeStats(ChargeOrderEntity chargeOrder) {
        try {
            // 获取用户信息
            MemberEntity user = memberDao.selectById(chargeOrder.getUserId());
            if (user == null) {
                log.error("用户不存在，用户ID: {}", chargeOrder.getUserId());
                return false;
            }
            
            // 更新充值统计
            Long currentTodayRecharge = user.getTodayRecharge() != null ? user.getTodayRecharge() : 0L;
            Long currentChargeSum = user.getChargeSum() != null ? user.getChargeSum() : 0L;
            Long currentTodayRechargeCnt = user.getTodayRechargeCnt() != null ? user.getTodayRechargeCnt() : 0L;
            Long currentHistorychargecnt = user.getHistorychargecnt() != null ? user.getHistorychargecnt() : 0L;
            
            user.setTodayRecharge(currentTodayRecharge + chargeOrder.getAmount());
            user.setChargeSum(currentChargeSum + chargeOrder.getAmount());
            user.setTodayRechargeCnt(currentTodayRechargeCnt + 1);
            user.setHistorychargecnt(currentHistorychargecnt + 1);

            int updateResult = memberDao.updateById(user);
            if (updateResult <= 0) {
                log.error("更新用户充值统计失败，用户ID: {}, 订单号: {}", chargeOrder.getUserId(), chargeOrder.getOrderno());
                return false;
            }
            
            log.info("用户充值统计更新成功，用户ID: {}, 订单号: {}, 今日充值: {} 分, 累计充值: {} 分", 
                    chargeOrder.getUserId(), chargeOrder.getOrderno(), 
                    currentTodayRecharge + chargeOrder.getAmount(), 
                    currentChargeSum + chargeOrder.getAmount());
            
            return true;
            
        } catch (Exception e) {
            log.error("更新用户充值统计异常，用户ID: {}, 订单号: {}", chargeOrder.getUserId(), chargeOrder.getOrderno(), e);
            return false;
        }
    }
}
