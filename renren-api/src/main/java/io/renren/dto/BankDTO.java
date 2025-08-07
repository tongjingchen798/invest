/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 银行管理
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
@Data
@ApiModel(value = "银行管理")
public class BankDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "银行简称")
    private String blankCode;

    @ApiModelProperty(value = "银行名称")
    private String blankName;

    @ApiModelProperty(value = "渠道")
    private String channel;

    @ApiModelProperty(value = "货币")
    private String currency;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "状态")
    private Integer state;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "状态时间")
    private String stateTime;
}
