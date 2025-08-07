/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.entity.TransactionDetailEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 账变明细表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
@Mapper
public interface TransactionDetailDao extends BaseMapper<TransactionDetailEntity> {

}
