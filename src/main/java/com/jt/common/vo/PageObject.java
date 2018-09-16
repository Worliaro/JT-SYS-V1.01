package com.jt.common.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 业务层对象,负责封装业务层数据
 * 
 * @author zn
 *
 * @param <T>
 */
public class PageObject<T> implements Serializable {

	/** 版本号 */
	private static final long serialVersionUID = 1L;

	/** 当前页的记录信息 */
	private List<T> records;

	/** 总记录数 */
	private int rowCount;

	/** 总页数 */
	private int pageSize = 3;

	/** 当前页的页码 */
	private int pageCurrent = 1;

	/** 总页数*/
	private Integer pageCount=0;
	
	public List<T> getRecords() {
		return records;
	}

	public void setRecords(List<T> records) {
		this.records = records;
	}

	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageCurrent() {
		return pageCurrent;
	}

	public void setPageCurrent(int pageCurrent) {
		this.pageCurrent = pageCurrent;
	}

	public Integer getPageCount() {
		pageCount=rowCount/pageSize;
		if(rowCount%pageSize!=0){
			pageCount++;
		}
		return pageCount;
	}
	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}
}
