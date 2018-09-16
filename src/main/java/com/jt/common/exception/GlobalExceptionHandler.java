package com.jt.common.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.vo.JsonResult;
/**
 * 全局异常处理类
 * @author zn
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(RuntimeException.class)
	@ResponseBody
	public JsonResult doHandlerRuntimeException(RuntimeException e) {
		//e.printStackTrace();
		return new JsonResult(e);
	}
}
