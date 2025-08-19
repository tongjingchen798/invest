package io.renren.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.dao.AdvertisementDao;
import io.renren.entity.AdvertisementEntity;
import io.renren.dto.AdvertisementDTO;
import io.renren.service.AdvertisementService;
import io.renren.common.page.PageData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 广告素材
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2025-07-10 20:05:01
 */
@Service("advertisementService")
public class AdvertisementServiceImpl extends ServiceImpl<AdvertisementDao, AdvertisementEntity> implements AdvertisementService {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<AdvertisementDTO> getByType(Integer type) {
        List<AdvertisementEntity> entities = this.baseMapper.selectByType(type);
        return convertToDto(entities);
    }

    @Override
    public List<AdvertisementDTO> getAllEnabled() {
        List<AdvertisementEntity> entities = this.baseMapper.selectEnabled();
        return convertToDto(entities);
    }

    @Override
    public List<AdvertisementDTO> getByEffectiveTime() {
        String currentTime = DATE_FORMAT.format(new java.util.Date());
        List<AdvertisementEntity> entities = this.baseMapper.selectByEffectiveTime(currentTime);
        return convertToDto(entities);
    }

    @Override
    public PageData<AdvertisementDTO> getPage(Map<String, Object> params) {
        // 获取分页参数
        long page = Long.parseLong(params.getOrDefault("page", "1").toString());
        long limit = Long.parseLong(params.getOrDefault("limit", "10").toString());

        // 创建MyBatis-Plus分页对象
        IPage<AdvertisementEntity> pageParam = new Page<>(page, limit);

        // 构建查询条件
        QueryWrapper<AdvertisementEntity> queryWrapper = buildQueryWrapper(params);

        // 使用MyBatis-Plus分页插件执行分页查询
        IPage<AdvertisementEntity> result = this.baseMapper.selectPage(pageParam, queryWrapper);

        // 转换为DTO
        List<AdvertisementDTO> dtoList = convertToDto(result.getRecords());

        // 转换为PageData，使用MyBatis-Plus分页结果的总数
        return new PageData<>(dtoList, result.getTotal());
    }

    /**
     * 构建查询条件
     */
    private QueryWrapper<AdvertisementEntity> buildQueryWrapper(Map<String, Object> params) {
        QueryWrapper<AdvertisementEntity> queryWrapper = new QueryWrapper<>();

        // 广告类型筛选
        Object typeObj = params.get("type");
        if (typeObj != null) {
            if (typeObj instanceof Integer) {
                queryWrapper.eq("type", typeObj);
            } else if (typeObj instanceof String && StringUtils.isNotBlank((String) typeObj)) {
                try {
                    queryWrapper.eq("type", Integer.parseInt((String) typeObj));
                } catch (NumberFormatException e) {
                    // 忽略无效的类型值
                }
            }
        }

        // 只查询启用的数据
        queryWrapper.eq("status", 1);

        // 排序 - 支持MyBatis-Plus的动态排序
        Object orderFieldObj = params.get("orderField");
        Object orderObj = params.get("order");
        if (orderFieldObj != null && StringUtils.isNotBlank(orderFieldObj.toString()) &&
            orderObj != null && StringUtils.isNotBlank(orderObj.toString())) {
            boolean isAsc = "asc".equalsIgnoreCase(orderObj.toString());
            queryWrapper.orderBy(true, isAsc, orderFieldObj.toString());
        } else {
            // 默认按排序字段和创建时间排序
            queryWrapper.orderByAsc("sort");
        }

        return queryWrapper;
    }

    /**
     * 转换为DTO
     */
    private List<AdvertisementDTO> convertToDto(List<AdvertisementEntity> entities) {
        return entities.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    /**
     * 转换为DTO
     */
    private AdvertisementDTO convertToDto(AdvertisementEntity entity) {
        AdvertisementDTO dto = new AdvertisementDTO();
        
        // 设置基本字段
        dto.setId(entity.getId().intValue());
        dto.setType(entity.getType());
        dto.setTitle(entity.getTitle());
        dto.setLogosAddr(entity.getLogosAddr());
        dto.setLogosLinkaddr(entity.getLogosLinkaddr());
        dto.setRemark(entity.getRemark());
        dto.setHour(entity.getHour());
        
        // 格式化时间字段
        if (entity.getCreateDate() != null) {
            dto.setCreateDate(DATE_FORMAT.format(entity.getCreateDate()));
        }
        if (entity.getSxDate() != null) {
            dto.setSxDate(DATE_FORMAT.format(entity.getSxDate()));
        }
        
        // 计算isPop字段
        if (entity.getType() != null && entity.getType() == 4) {
            dto.setIsPop(1); // 弹窗广告
        } else {
            dto.setIsPop(0); // 非弹窗广告
        }
        
        return dto;
    }
}
