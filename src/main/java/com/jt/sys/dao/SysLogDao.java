package com.jt.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jt.sys.entity.SysLog;
/**
 * SysLog表数据访问
 * @author zn
 *
 */
public interface SysLogDao {

	/**
	 * 基于条件查询日志信息
	 * @param username 查询条件：操作用户
	 * @param startIndex 当前页面起始位置
	 * @param pageeSize 当前页面大小
	 * @return 当前野德日志信息记录
	 * 数据库中每条日志信息封装到一个SysLog对象中
	 */
	List<SysLog> findPageObjects(@Param("username") String username,
			  					 @Param("startIndex") Integer startIndex,
			  					 @Param("pageSize") Integer pageSize); 
	/**
	 * 基于条件查询总记录数
	 * @param username 查询条件：操作用户
	 * @return 总记录数
	 */
	int getRowCount(@Param("username") String username);
	
	/**
	 * 基于日志id删除相关日志信息记录
	 * @param ids
	 * @return
	 */
	int deleteObjects(@Param("ids") Integer...ids);
}
