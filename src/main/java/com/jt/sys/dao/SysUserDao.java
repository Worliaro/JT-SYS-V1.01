package com.jt.sys.dao;

public interface SysUserDao {

	/**
	 * 根据部门id删除获取部门下所有用户数
	 * @param id
	 * @return
	 */
	int getUserCountByDeptId(Integer id);
}
