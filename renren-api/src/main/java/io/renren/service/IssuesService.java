 

package io.renren.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.service.BaseService;
import io.renren.dto.IssuesDTO;
import io.renren.dto.IssuesPageData;
import io.renren.entity.IssuesEntity;

import java.util.List;
import java.util.Map;

/**
 * 广告/图片管理
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface IssuesService extends BaseService<IssuesEntity> {

    IssuesPageData<IssuesDTO> queryPageData(Map<String, Object> params);

}