 

package io.renren.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.service.BaseService;
import io.renren.dto.ProjectDTO;
import io.renren.entity.ProjectEntity;

import java.util.List;
import java.util.Map;

/**
 * 投资项目服务接口
 * 
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
public interface ProjectService extends BaseService<ProjectEntity> {

	/**
	 * 查询投资项目列表（分组）
	 */
	List<ProjectDTO> queryListGroup();

}
