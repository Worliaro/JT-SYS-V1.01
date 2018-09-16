package com.test;

import java.util.List;

import org.junit.Test;

import com.jt.sys.dao.SysLogDao;
import com.jt.sys.entity.SysLog;

public class SysLogDaoTest extends TestBase {

	@Test
	public void testFindPageObjects() {
		SysLogDao dao = ctx.getBean("sysLogDao",SysLogDao.class);
		List<SysLog> list = dao.findPageObjects("admin", 0, 10);
		for(SysLog s:list) {
			System.out.println(s);
		}
	}
	
	@Test
	public void testgetRowCount() {
		SysLogDao dao = ctx.getBean("sysLogDao",SysLogDao.class);
		int rowCount = dao.getRowCount("admin");
		System.out.println(rowCount);
	}
	
	@Test
	public void testdeleteObjects() {
		SysLogDao dao = ctx.getBean("sysLogDao",SysLogDao.class);
		int rows = dao.deleteObjects(38);
		System.out.println("删除了"+rows+"行记录");
	}
}
