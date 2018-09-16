package com.jt.common.vo;

import java.io.Serializable;
/**
 * 用于封装数据的节点对象
 * 1）sys_menus (存储系统菜单信息)
 * 2）sys_role_menus
 * @author zn
 *
 */
public class Node implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 节点id(例如：菜单id)*/
	private Integer id;
	/** 节点名*/
	private String name;
	/** 父节点id*/
	private Integer parentId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Node [id=" + id + ", name=" + name + ", parentId=" + parentId + "]";
	}
	
}
