package io.renren.service;

import io.renren.dto.DownloadPageDataDTO;

/**
 * 下载页数据服务接口
 * 
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
public interface DownloadPageService {
	
	/**
	 * 获取下载页统计数据
	 * @return 下载页数据
	 */
	DownloadPageDataDTO getDownloadPageData();
	
	/**
	 * 增加访问量
	 * @return 是否成功
	 */
	boolean incrementVisitCount();
	
	/**
	 * 增加下载量
	 * @return 是否成功
	 */
	boolean incrementDownloadCount();
	
	/**
	 * 获取实时统计数据
	 * @return 实时统计数据
	 */
	DownloadPageDataDTO getRealTimeData();
}
