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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 银行分页响应数据
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@ApiModel(value = "分页数据")
public class BankPageData<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "列表数据")
    private List<T> list;

    @ApiModelProperty(value = "汇总")
    private Map<String, Object> sum;

    @ApiModelProperty(value = "总记录数")
    private Integer total;

    public BankPageData() {
        this.sum = new HashMap<>();
    }

    public BankPageData(List<T> list, Integer total) {
        this.list = list;
        this.total = total;
        this.sum = new HashMap<>();
    }

    public BankPageData(List<T> list, Integer total, Map<String, Object> sum) {
        this.list = list;
        this.total = total;
        this.sum = sum != null ? sum : new HashMap<>();
    }
}
