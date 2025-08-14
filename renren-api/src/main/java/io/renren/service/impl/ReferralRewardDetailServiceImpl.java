package io.renren.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.dao.UserBalanceDetailDao;
import io.renren.dao.UserDao;
import io.renren.dto.ReferralRewardDetailDTO;
import io.renren.dto.ReferralRewardPageData;
import io.renren.entity.UserBalanceDetailEntity;
import io.renren.entity.UserEntity;
import io.renren.service.ReferralRewardDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 推荐返利流水查询服务实现类
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Slf4j
@Service("referralRewardDetailService")
public class ReferralRewardDetailServiceImpl implements ReferralRewardDetailService {

    @Autowired
    private UserBalanceDetailDao userBalanceDetailDao;

    @Autowired
    private UserDao userDao;



}
