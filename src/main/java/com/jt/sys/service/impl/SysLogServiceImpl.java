package com.jt.sys.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.common.exception.ServiceException;
import com.jt.common.vo.PageObject;
import com.jt.sys.dao.SysLogDao;
import com.jt.sys.entity.SysLog;
import com.jt.sys.service.SysLogService;

@Service
public class SysLogServiceImpl implements SysLogService{

	/**
	 * 关联SysLogDao对象
	 */
	@Autowired
	private SysLogDao sysLogDao;
	
	
	@Override
	public PageObject<SysLog> findPageObjects(String username, Integer pageCurrent) {
		//参数有效性验证
		if(pageCurrent == null || pageCurrent <1)
			throw new IllegalArgumentException("当前页码不正确");
		//查询总记录数进行验证
		int rowCount = sysLogDao.getRowCount(username);
		if(rowCount == 0)
			throw new ServiceException("无记录");
		//查询当前日志记录
		int pageSize = 3;
		int startIndex = (pageCurrent-1)*pageSize;
		List<SysLog> records = sysLogDao.findPageObjects(username, startIndex, pageSize);
		//对结果集进行封装
		PageObject<SysLog> po = new PageObject<>();
		po.setRecords(records);
		po.setRowCount(rowCount);
		po.setPageSize(pageSize);
		po.setPageCurrent(pageCurrent);
		po.setPageCount((rowCount-1)/pageSize+1);
		return po;
	}


	@Override
	public int deleteObjects(Integer... ids) {
		//1. 判定参数合法性
		if(ids==null || ids.length==0)
			throw new IllegalArgumentException("请选择一个要删除的数据");
		//2. 执行删除操作
		int rows;
		try {
			rows = sysLogDao.deleteObjects(ids);			
		} catch (Exception e) {
			e.printStackTrace();
			//给运维人员发出报警信息
			throw new ServiceException("系统故障,正在恢复中...");
		}
		//4.对结果进行验证
		if(rows==0) {
			throw new ServiceException("记录可能已经不存在");
		}
		return rows;
	}

	

	
}
