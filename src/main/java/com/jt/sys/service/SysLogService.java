package com.jt.sys.service;

import com.jt.common.vo.PageObject;
import com.jt.sys.entity.SysLog;

public interface SysLogService {

	/**
	 * 分页查询日志信息
	 * @param username 用户名
	 * @param pageCurrent 当前页面
	 * @return 对查询结果的封装
	 */
	PageObject<SysLog> findPageObjects(String username,Integer pageCurrent);

	/**
	 * 删除日志信息
	 * @param ids
	 * @return
	 */
	int deleteObjects(Integer... ids);
}
