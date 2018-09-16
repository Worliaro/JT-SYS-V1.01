package com.jt.sys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/")
@Controller
public class PageController {
	/**
	 * 返回主页面
	 * @return
	 */
	@RequestMapping("doIndexUI")
	public String doIndexUI(){
		return "starter";//starter.html
	}
	
	/**
	 * 返回分页页面(main-content)
	 * @return
	 */
	@RequestMapping("doPageUI")
	public String doPageUI(){
		return "common/page";
	}
}

