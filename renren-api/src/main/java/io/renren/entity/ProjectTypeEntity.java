 

 package io.renren.entity;

 import com.baomidou.mybatisplus.annotation.TableId;
 import com.baomidou.mybatisplus.annotation.TableName;
 import lombok.Data;
 
 import java.io.Serializable;
 import java.util.Date;
 
 /**
  * 投资项目分类
  *
  * @author Mark sunlightcs@gmail.com
  * @since 1.0.0
  */
 @Data
 @TableName("tb_project_type")
 public class ProjectTypeEntity implements Serializable {
     private static final long serialVersionUID = 1L;
 
     /**
      * 项目分类id
      */
     @TableId
     private Long typeId;
     
     /**
      * 分类名称
      */
     private String typeName;
     
     /**
      * 排序
      */
     private Integer sort;
     
     /**
      * 状态 0=禁用 1=启用
      */
     private Integer status;
     
     /**
      * 创建时间
      */
     private Date createDate;
     
     /**
      * 更新时间
      */
     private Date updateDate;
     
     /**
      * 创建者ID
      */
     private Long sysCreateUserId;
     
     /**
      * 更新者ID
      */
     private Long sysUpdateUserId;
 }