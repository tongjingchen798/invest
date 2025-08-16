
 package io.renren.service;

 import com.baomidou.mybatisplus.extension.service.IService;
 import io.renren.common.service.BaseService;
 import io.renren.dto.BankDTO;
 import io.renren.dto.BankPageData;
 import io.renren.entity.BankEntity;
 
 import java.util.Map;
 
 /**
  * 银行管理
  *
  * @author Mark sunlightcs@gmail.com
  * @since 1.0.0
  */
 public interface BankService extends BaseService<BankEntity> {
 
     /**
      * 查询分页数据（使用Map参数）
      */
     BankPageData<BankDTO> queryPageData(Map<String, Object> params);
     
     /**
      * 查询分页数据（直接参数）
      * @param page 页码
      * @param limit 每页大小
      * @param order 排序方式
      * @param orderField 排序字段
      * @param state 状态筛选
      * @return 分页数据
      */
     BankPageData<BankDTO> queryPageData(Integer page, Integer limit, String order, String orderField, Integer state);
 }