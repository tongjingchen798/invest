package io.renren.modules.qd.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.common.page.PageData;
import io.renren.modules.qd.dao.QdDao;
import io.renren.modules.qd.dto.QdRecordDTO;
import io.renren.modules.qd.service.QdService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 签到记录服务实现类
 *
 * @author renren
 * @since 2024-01-01
 */
@Service
@Slf4j
public class QdServiceImpl implements QdService {

    @Autowired
    private QdDao qdDao;

    @Override
    public PageData<QdRecordDTO> getQdRecordList(Map<String, Object> params) {
        try {
            // 获取分页参数，提供默认值
            long current = 1;
            long size = 10;
            
            if (params.get("page") != null) {
                try {
                    current = Long.parseLong(params.get("page").toString());
                } catch (NumberFormatException e) {
                    current = 1;
                }
            }
            
            if (params.get("limit") != null) {
                try {
                    size = Long.parseLong(params.get("limit").toString());
                } catch (NumberFormatException e) {
                    size = 10;
                }
            }
            
            // 创建分页对象
            Page<QdRecordDTO> page = new Page<>(current, size);
            
            // 执行分页查询，直接传递参数给DAO
            IPage<QdRecordDTO> resultPage = qdDao.selectQdRecordPage(page, params);
            
            // 转换为PageData
            return new PageData<>(resultPage.getRecords(), resultPage.getTotal());
            
        } catch (Exception e) {
            log.error("查询签到记录失败", e);
            throw new RuntimeException("查询签到记录失败: " + e.getMessage());
        }
    }
}
