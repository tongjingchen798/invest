

package io.renren.modules.sys.service;

import io.renren.common.page.PageData;
import io.renren.common.service.BaseService;
import io.renren.modules.sys.dto.SysDictTypeDTO;
import io.renren.modules.sys.entity.DictType;
import io.renren.modules.sys.entity.SysDictTypeEntity;
import io.renren.modules.sys.dto.BalanceDetailBusiTypeDTO;

import java.util.List;
import java.util.Map;

/**
 * 数据字典
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface SysDictTypeService extends BaseService<SysDictTypeEntity> {

    PageData<SysDictTypeDTO> page(Map<String, Object> params);

    SysDictTypeDTO get(Long id);

    void save(SysDictTypeDTO dto);

    void update(SysDictTypeDTO dto);

    void delete(Long[] ids);

    /**
     * 获取所有字典
     */
    List<DictType> getAllList();

    /**
     * 获取余额明细业务类型
     * @return 业务类型数据
     */
    BalanceDetailBusiTypeDTO getBalanceDetailBusiType();
}