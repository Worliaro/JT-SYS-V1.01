package com.jt.sys.dao;

import java.util.List;
import java.util.Map;

import com.jt.common.vo.Node;
import com.jt.sys.entity.SysDept;

/**
 * 部门管理
 * @author zn
 *
 */
public interface SysDeptDao {

	/**
	 * 询所有部门以及上一级部门信息
	 * @return
	 */
	List<Map<String,Object>> findObjects();
	
	/**
	 * 根据部门id统计子部门的个数
	 * @param id
	 * @return
	 */
	int getChildCount(Integer id);
	
	/**
	 * 根据id删除部门
	 * @param id
	 * @return
	 */
	int deleteObject(Integer id);
	
	/**
	 * 查询部门所有子节点
	 * @return
	 */
	List<Node> findZtreeDeptNodes();
	
	/**
	 * 添加部门信息
	 * @param entity
	 * @return
	 */
	int insertObject(SysDept entity);
	
	/**
	 * 修改部门信息
	 * @param entity
	 * @return
	 */
	int updateObject(SysDept entity);
}
