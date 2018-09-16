package com.jt.sys.dao;

import java.util.List;
import java.util.Map;

import com.jt.common.vo.Node;
import com.jt.sys.entity.SysMenu;
/**
 * 系统菜单数据访问接口
 * @author zn
 *
 */
public interface SysMenuDao {

	/**
	 * 查询系统菜单所有数据信息
	 * @return map对象
	 */
	List<Map<String, Object>> findObjects();
	
	/**
	 * 根据菜单id统计子菜单的个数
	 * @param id 菜单id
	 * @return 子菜单个数
	 */
	int getChildCount(Integer id);
	
	/**
	 * 根据菜单id删除菜单
	 * @param id
	 * @return
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
	int insertObject(SysMenu entity);
}
