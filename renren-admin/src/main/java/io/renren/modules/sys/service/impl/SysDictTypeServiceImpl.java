 

package io.renren.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.renren.common.page.PageData;
import io.renren.common.service.impl.BaseServiceImpl;
import io.renren.common.utils.ConvertUtils;
import io.renren.modules.sys.dao.SysDictDataDao;
import io.renren.modules.sys.dao.SysDictTypeDao;
import io.renren.modules.sys.dto.SysDictTypeDTO;
import io.renren.modules.sys.dto.BalanceDetailBusiTypeDTO;
import io.renren.modules.sys.entity.DictData;
import io.renren.modules.sys.entity.DictType;
import io.renren.modules.sys.entity.SysDictTypeEntity;
import io.renren.modules.sys.entity.SysDictDataEntity;
import io.renren.modules.sys.service.SysDictTypeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

/**
 * 字典类型
 *
 * @author Mark sunlightcs@gmail.com
 */
@Service
public class SysDictTypeServiceImpl extends BaseServiceImpl<SysDictTypeDao, SysDictTypeEntity> implements SysDictTypeService {
    @Autowired
    private SysDictDataDao sysDictDataDao;


    @Autowired
    private SysDictTypeDao sysDictTypeDao;
    @Override
    public PageData<SysDictTypeDTO> page(Map<String, Object> params) {
        IPage<SysDictTypeEntity> page = baseDao.selectPage(
            getPage(params, "sort", true),
            getWrapper(params)
        );

        return getPageData(page, SysDictTypeDTO.class);
    }

    private QueryWrapper<SysDictTypeEntity> getWrapper(Map<String, Object> params){
        String dictType = (String) params.get("dictType");
        String dictName = (String) params.get("dictName");

        QueryWrapper<SysDictTypeEntity> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(dictType), "dict_type", dictType);
        wrapper.like(StringUtils.isNotBlank(dictName), "dict_name", dictName);

        return wrapper;
    }

    @Override
    public SysDictTypeDTO get(Long id) {
        SysDictTypeEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, SysDictTypeDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(SysDictTypeDTO dto) {
        SysDictTypeEntity entity = ConvertUtils.sourceToTarget(dto, SysDictTypeEntity.class);

        insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysDictTypeDTO dto) {
        SysDictTypeEntity entity = ConvertUtils.sourceToTarget(dto, SysDictTypeEntity.class);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long[] ids) {
        //删除
        deleteBatchIds(Arrays.asList(ids));
    }

    @Override
    public List<DictType> getAllList() {
        List<DictType> typeList = baseDao.getDictTypeList();
        List<DictData> dataList = sysDictDataDao.getDictDataList();
        for(DictType type : typeList){
            for(DictData data : dataList){
                if(type.getId().equals(data.getDictTypeId())){
                    type.getDataList().add(data);
                }
            }
        }
        return typeList;
    }

    @Override
    public BalanceDetailBusiTypeDTO getBalanceDetailBusiType() {

        BalanceDetailBusiTypeDTO result = new BalanceDetailBusiTypeDTO();
        result.setDictType("busi_type");
        
        try {
            // 直接查询字典数据，使用已知的字典类型ID
            List<SysDictDataEntity> dictDataList = sysDictDataDao.selectList(
                new QueryWrapper<SysDictDataEntity>()
                    .eq("dict_type_id", getBusiTypeDictTypeId()) // 使用你提供的ID
                    .orderByAsc("sort")
            );
            
            if (dictDataList != null && !dictDataList.isEmpty()) {
                List<BalanceDetailBusiTypeDTO.DictDataItem> dataList = new ArrayList<>();
                
                for (SysDictDataEntity dictData : dictDataList) {
                    BalanceDetailBusiTypeDTO.DictDataItem item = new BalanceDetailBusiTypeDTO.DictDataItem();
                    item.setDictLabel(dictData.getDictLabel());
                    item.setDictValue(dictData.getDictValue());
                    dataList.add(item);
                }
                
                result.setDataList(dataList);
            } else {
                result.setDataList(new ArrayList<>());
            }
            
        } catch (Exception e) {
            result.setDataList(new ArrayList<>());
        }
        
        return result;
    }
    
    /**
     * 获取业务类型字典类型ID
     * 这里需要根据实际情况查询或配置
     */
    private Long getBusiTypeDictTypeId() {
        // 方法1：从数据库查询
        SysDictTypeEntity dictType = sysDictTypeDao.selectOne(
            new QueryWrapper<SysDictTypeEntity>()
                .eq("dict_type", "busi_type")
        );
        
        if (dictType != null) {
            return dictType.getId();
        }
        // 方法3：抛出异常，提示配置错误
        throw new RuntimeException("未找到业务类型字典类型配置，请检查sys_dict_type表中是否存在dict_type='busi_type'的记录");
    }
}