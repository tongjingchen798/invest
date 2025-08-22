package io.renren.modules.sys.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * 余额明细业务类型数据传输对象
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Data
public class BalanceDetailBusiTypeDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 字典类型
     */
    private String dictType;

    /**
     * 数据列表
     */
    private List<DictDataItem> dataList;

    /**
     * 字典数据项
     */
    @Data
    public static class DictDataItem implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 字典标签
         */
        private String dictLabel;

        /**
         * 字典值
         */
        private String dictValue;
    }
}
