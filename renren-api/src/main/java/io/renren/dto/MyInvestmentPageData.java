package io.renren.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 我的投资分页数据
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@ApiModel(value = "我的投资分页数据")
public class MyInvestmentPageData<T> {
    
    @ApiModelProperty(value = "列表数据")
    private List<T> list;
    
    @ApiModelProperty(value = "汇总")
    private Object sum;
    
    @ApiModelProperty(value = "总记录数")
    private Integer total;
}
