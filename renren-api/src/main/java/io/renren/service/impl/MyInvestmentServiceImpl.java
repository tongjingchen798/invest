package io.renren.service.impl;

import io.renren.common.service.impl.BaseServiceImpl;
import io.renren.dao.MyInvestmentDao;
import io.renren.dto.MyInvestmentDTO;
import io.renren.dto.MyInvestmentPageData;
import io.renren.service.MyInvestmentService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的投资服务实现类
 *
 * @author Mark sunlightcs@gmail.com
 */
@Service
public class MyInvestmentServiceImpl extends BaseServiceImpl<MyInvestmentDao, MyInvestmentDTO> implements MyInvestmentService {

    @Override
    public MyInvestmentPageData<MyInvestmentDTO> queryPageData(Map<String, Object> params) {
        MyInvestmentPageData<MyInvestmentDTO> pageData = new MyInvestmentPageData<>();
        
        try {
            // 获取分页参数
            Integer page = Integer.parseInt(params.get("page").toString());
            Integer limit = Integer.parseInt(params.get("limit").toString());
            
            // 计算偏移量
            int offset = (page - 1) * limit;
            
            // 查询总数
            int total = baseDao.selectTotal(params);
            
            // 设置分页参数
            params.put("offset", offset);
            params.put("limit", limit);
            
            // 查询列表数据
            List<MyInvestmentDTO> list = baseDao.selectList(params);
            
            // 设置分页数据
            pageData.setList(list);
            pageData.setTotal(total);
            pageData.setSum(new HashMap<>()); // 暂时设置为空对象
            
        } catch (Exception e) {
            e.printStackTrace();
            // 设置默认值
            pageData.setList(null);
            pageData.setTotal(0);
            pageData.setSum(new HashMap<>());
        }
        
        return pageData;
    }
}
