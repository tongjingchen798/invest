package io.renren.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 分页数据
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@ApiModel(value = "分页数据")
public class PageData<T> {
    @ApiModelProperty(value = "列表数据")
    private List<T> list;

    @ApiModelProperty(value = "汇总")
    private Object sum;

    @ApiModelProperty(value = "总记录数")
    private Long total;
}
