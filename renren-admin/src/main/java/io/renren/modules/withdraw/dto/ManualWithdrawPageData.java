package io.renren.modules.withdraw.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 手工提现分页数据
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Data
@ApiModel(value = "手工提现分页数据")
public class ManualWithdrawPageData implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "列表数据")
    private List<ManualWithdrawDTO> list;

    @ApiModelProperty(value = "汇总")
    private Map<String, Object> sum;

    @ApiModelProperty(value = "总记录数")
    private Integer total;
}
