package com.jt.sys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.vo.JsonResult;
import com.jt.sys.entity.SysMenu;
import com.jt.sys.service.SysMenuService;
/**
 * 菜单管理控制
 * @author zn
 *
 */
@Controller
@RequestMapping("/menu/")
public class SysMenuController {

	 @Autowired
	 private SysMenuService sysMenuService;
	 
	 /**
	  * 加载菜单列表页面
	  * @return
	  */
	@RequestMapping("doMenuListUI")
	public String doMenuListUI() {
		return "sys/menu_list";
	}
	
	/**
	 * 加载菜单编辑页面
	 * 添加+修改页面共用
	 * @return
	 */
	@RequestMapping("doMenuEditUI")
	public String doMenuEditUI() {
		return "sys/menu_edit";
	}
	
	/**
	 * 返回JSON字段,获取系统菜单所有数据
	 * @return
	 */
	@RequestMapping("doFindObjects")
	@ResponseBody
	public JsonResult doFindObjects() {
		return new JsonResult(sysMenuService.findObjects());
	}
	
	/**
	 * 返回JSON字段,按id删除系统子菜单
	 * @param id
	 * @return
	 */
	@RequestMapping("doDeleteObject")
	@ResponseBody
	public JsonResult doDeleteObject(Integer id) {
		sysMenuService.deleteObject(id);
		return new JsonResult("Delete OK");
	}
	
	/**
	 * 查询菜单节点信息
	 * @return
	 */
	@RequestMapping("doFindZtreeMenuNodes")
	@ResponseBody
	public JsonResult doFindZtreeMenuNodes() {
		return new JsonResult(sysMenuService.findZtreeMenuNodes());
	}
	
	/**
	 * 添加系统菜单数据
	 * @param entity
	 * @return
	 */
	@RequestMapping("doSaveObject")
	@ResponseBody
	public JsonResult doSaveObject(SysMenu entity) {
		sysMenuService.saveObject(entity);
		return new JsonResult("Save OK");
	}
}
