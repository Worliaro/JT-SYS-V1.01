package com.test;

import java.util.List;

import org.junit.Test;

import com.jt.common.vo.PageObject;
import com.jt.sys.entity.SysLog;
import com.jt.sys.service.impl.SysLogServiceImpl;


public class SysLogServiceTest extends TestBase {

	@Test
	public void testSysLogService() {
		SysLogServiceImpl service = ctx.getBean("sysLogServiceImpl",SysLogServiceImpl.class);
		PageObject<SysLog> po = service.findPageObjects("admin", 2);
		List<SysLog> records = po.getRecords();
		for(SysLog s:records) {
			System.out.println(s);
		}
	}
}
