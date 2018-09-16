package com.test;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.jt.sys.dao.SysDeptDao;

public class SysMenuDaoTest extends TestBase {

	@Test
	public void testFindObjects() {
		SysDeptDao dao = ctx.getBean("sysDeptDao",SysDeptDao.class);
		List<Map<String,Object>> list = dao.findObjects();
		for(Map<String, Object> s:list) {
			System.out.println(s);
		}
	}
	
}
