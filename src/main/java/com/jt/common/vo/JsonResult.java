package com.jt.common.vo;
/**
 * 封装控制器对象,负责进一步封装控制层对象结果
 * @author zn
 *
 */
public class JsonResult {

	private static final int SUCCESS=1;
	private static final int ERROR=0;
	/** 状态码：1表示正确，0表示错误*/
	private int state = SUCCESS;
	/** 状态吗对应信息  */
	private String message = "OK";
	/** 结果数据  */
	private Object data;
	
	public JsonResult() {
		
	}
	
	public JsonResult(Object data) {
		this.data = data;
	}
	
	public JsonResult(String message) {
		this.message = message;
	}
	
	/**
	 * 出现异常
	 * @param exp
	 */
	public JsonResult(Throwable exp){
		this.state=ERROR;
		this.message=exp.getMessage();
	}
	
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
