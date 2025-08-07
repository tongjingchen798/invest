
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
 
     BankPageData<BankDTO> queryPageData(Map<String, Object> params);
 }