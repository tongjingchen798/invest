

 package io.renren.service;

 import com.baomidou.mybatisplus.extension.service.IService;
 import io.renren.common.service.BaseService;
 import io.renren.entity.ProjectTypeEntity;
 import io.renren.dto.ProjectTypeDTO;
 
 import java.util.List;
 
 /**
  * 投资项目分类
  *
  * @author Mark sunlightcs@gmail.com
  * @since 1.0.0
  */
 public interface ProjectTypeService extends BaseService<ProjectTypeEntity> {
 
     /**
      * 查询投资项目分类列表
      */
     List<ProjectTypeDTO> queryList();
 }