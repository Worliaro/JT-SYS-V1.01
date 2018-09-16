package com.jt.sys.dao;

import org.apache.ibatis.annotations.Param;

public interface SysRoleMenuDao {

	/**
	 * 根据删除的菜单id删除关联的角色信息
	 * @param menuId
	 * @return
	 */
	int deleteObjectsByMenuId(@Param("menuId") Integer menuId);
}
