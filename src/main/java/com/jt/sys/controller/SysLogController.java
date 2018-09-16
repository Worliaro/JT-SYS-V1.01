package com.jt.sys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.vo.JsonResult;
import com.jt.sys.service.SysLogService;

/**
 * 日志信息控制器
 * @author zn
 *
 */
@Controller
@RequestMapping("/log/")
public class SysLogController {

	@Autowired
	private SysLogService sysLogService;
	
	/**
	 * 跳转到sys下的log_list页面
	 * @return
	 */
	@RequestMapping("doLogListUI")
	public String doLogListUI(){
		return "sys/log_list";
	}
	
	@RequestMapping("doLogListUIV2")
	public String doLogListUIV2(){
		return "SysLog-JQuery";
	}
	
	/**
	 * 按相应条件分页查询日志信息并转换成JsonResult格式的JSON传输
	 * @param username  查询条件：用户姓名
	 * @param pageCurrent 当前页面
	 * @return
	 */
	@RequestMapping("doFindPageObjects")
	@ResponseBody
	public JsonResult doFindPageObjects(String username,Integer pageCurrent) {
		return new JsonResult(sysLogService.findPageObjects(username, pageCurrent));
	}
	
	/**
	 * 删除相应的日志信息记录
	 * @param ids
	 * @return
	 */
	@RequestMapping("doDeleteObjects")
	@ResponseBody
	public JsonResult doDeleteObejcts(Integer...ids) {
		sysLogService.deleteObjects(ids);
		return new JsonResult("delete ok");
	}
}
