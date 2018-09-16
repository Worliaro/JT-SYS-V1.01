package com.jt.sys.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统日志信息类
 * 
 * @author zn
 * 
 */
public class SysLog implements Serializable{

	/**
	 * 版本号
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	/** 操作用户 */
	private String username;
	/** 执行的操作 */
	private String operation;
	/** 操作方法 */
	private String method;
	/** 方法传递的参数 */
	private String params;
	/** 方法执行历史纪录次数 */
	private Long time;
	/** 用户ip地址 */
	private String ip;
	/** 日志创建时间 */
	private Date createdTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	@Override
	public String toString() {
		return "SysLog [id=" + id + ", username=" + username + ", operation=" + operation + ", method=" + method
				+ ", params=" + params + ", time=" + time + ", ip=" + ip + ", createdTime=" + createdTime + "]";
	}

	
}

