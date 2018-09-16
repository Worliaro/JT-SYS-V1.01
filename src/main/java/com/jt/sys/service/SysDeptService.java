package com.jt.sys.service;

import java.util.List;
import java.util.Map;

import com.jt.common.vo.Node;
import com.jt.sys.entity.SysDept;

public interface SysDeptService {

	/**
	 * 询所有部门以及上一级部门信息
	 * @return
	 */
	List<Map<String,Object>> findObjects();
	
	/**
	 * 按id删除部门信息
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
	 * 添加部门相应信息
	 * @param entity
	 * @return 
	 */
	int saveObject(SysDept entity);
	
	/**
	 * 修改部门信息
	 * @param entity
	 * @return
	 */
	int updateObject(SysDept entity);
}
