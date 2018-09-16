package com.jt.sys.service;

import java.util.List;
import java.util.Map;

import com.jt.common.vo.Node;
import com.jt.sys.entity.SysMenu;

/**
 * 
 * @author zn
 *
 */
public interface SysMenuService {

	/**
	 * 查询系统菜单所有记录
	 * @return
	 */
	List<Map<String,Object>> findObjects();
	
	/**
	 * 执行删除指定的菜单操作
	 * @param id 菜单id
	 * @return 删除的行数
	 */
	int deleteObject(Integer id);
	
	/**
	 * 查询菜单节点信息
	 * @return
	 */
	List<Node> findZtreeMenuNodes();
	
	/**
	 * 添加子菜单
	 * @param entity
	 * @return
	 */
	int saveObject(SysMenu entity);
}
