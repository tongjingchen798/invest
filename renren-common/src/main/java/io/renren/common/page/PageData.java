

package io.renren.common.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 分页工具类
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@ApiModel(value = "分页数据")
public class PageData<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "总记录数")
    private int total;

    @ApiModelProperty(value = "列表数据")
    private List<T> list;

    @ApiModelProperty(value = "汇总数据")
    private Map<String, Object> sum;

    /**
     * 分页
     * @param list   列表数据
     * @param total  总记录数
     */
    public PageData(List<T> list, long total) {
        this.list = list;
        this.total = (int)total;
        this.sum = null;
    }

    /**
     * 分页（包含汇总）
     * @param list   列表数据
     * @param total  总记录数
     * @param sum    汇总数据
     */
    public PageData(List<T> list, long total, Map<String, Object> sum) {
        this.list = list;
        this.total = (int)total;
        this.sum = sum;
    }
}