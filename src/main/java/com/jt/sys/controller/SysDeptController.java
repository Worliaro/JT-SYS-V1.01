package com.jt.sys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.vo.JsonResult;
import com.jt.sys.entity.SysDept;
import com.jt.sys.service.SysDeptService;

@Controller
@RequestMapping("/dept/")
public class SysDeptController {

	@Autowired
	private SysDeptService sysDeptService;
	
	/**
	 * 加载到部门管理页面
	 * @return
	 */
	@RequestMapping("doDeptListUI")
	public String doDeptListUI() {
		return "sys/dept_list";
	}
	
	/**
	 * 加载部门编辑页面
	 * @return
	 */
	@RequestMapping("doDeptEditUI")
	public String doDeptEditUI() {
		return "sys/dept_edit";
	}
	
	/**
	 * 访问部门信息
	 * @return
	 */
	@RequestMapping("doFindObjects")
	@ResponseBody
	public JsonResult doFindObjects() {
		return new JsonResult(sysDeptService.findObjects());
	}
	
	/**
	 * 按id删除部门信息
	 * @param id
	 * @return
	 */
	@RequestMapping("doDeleteObject")
	@ResponseBody
	public JsonResult doDeleteObject(Integer id) {
		sysDeptService.deleteObject(id);
		return new JsonResult("Delete Ok");
	}
	
	/**
	 * 查询部门下所有节点
	 * @return
	 */
	@RequestMapping("doFindZtreeDeptNodes")
	@ResponseBody
	public JsonResult doFindZtreeDeptNodes() {
		return new JsonResult(sysDeptService.findZtreeDeptNodes());
	}
	
	/**
	 * 添加部门信息
	 * @param entity
	 * @return
	 */
	@RequestMapping("doSaveObject")
	@ResponseBody
	public JsonResult doSaveObject(SysDept entity) {
		sysDeptService.saveObject(entity);
		return new JsonResult("save OK");
	}
	
	/**
	 * 修改部门信息
	 * @param entity
	 * @return
	 */
	@RequestMapping("doUpdateObject")
	@ResponseBody
	public JsonResult doUpdateObject(SysDept entity) {
		sysDeptService.updateObject(entity);
		return new JsonResult("update OK");
	}
}
