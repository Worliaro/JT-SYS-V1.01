# JT-权限管理系统
## 日志管理
[toc]

---
### 1.日志管理列表页面呈现
#### 1.1. 服务端实现
##### 1.1.1. Controller实现
- **业务描述**
 
1.定义日志管理Controller处理日志管理的客户端请求<br>
2.修改PageController添加doPageUI方法返回page页面

- **业务实现**

> 日志管理控制SysLogController类

```java
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

}
```
> PageController实现

```java
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
```
#### 1.2. 客户端实现
##### 1.2.1. 首页页面日志管理事件处理
- **页面描述**
1) 准备日志列表页面(WEB-INF/pages/sys/log_list.html) <br>
2) starter.html页面中点击日志管理菜单时异步加载日志列表页面
- **业务实现**
1) 事件注册(被点击的元素上)
2) 事件处理函数定义
- **JS代码**

```js
  $(function(){
    	 doLoadUI("load-log-id","log/doLogListUI.do")
      })
      function doLoadUI(id,url){
    	  $("#"+id).click(function(){
    	       //load函数为jquery中的ajax异步请求函数
         	   $("#mainContentId").load(url,function(){
         		  $("#mainContentId").removeData()
         	   });
          });
      }    
```
##### 1.2.2. 创建日志列表页面
- **业务描述**
1) 在WEB-INF/pages/sys目录下定义log_list.html页面.
2) 当页面加载完成以后异步加载分页页面(page.html).
- **业务实现**
1) 在PageController中添加doPageUI方法返回page页面(假如已有则无须再次添加)
2) 在log_list.html页面中异步加载page页面页面
- **JS代码**

```js
$(function(){
       $("#pageId").load("doPageUI.do");
});
```
### 2.日志管理列表数据呈现
#### 2.1. 服务器实现
- 日志模块实现整体业务架构设计
![image](https://java-1257588924.cos.ap-shanghai.myqcloud.com/spring-MVC/20180912090724.png)
- 服务端日志分页查询代码基本架构
![image](https://java-1257588924.cos.ap-shanghai.myqcloud.com/JT/image003.png)
- 服务端分页查询数据基本架构
- ![image](https://java-1257588924.cos.ap-shanghai.myqcloud.com/JT/20180912161459.png)
##### 2.1.1 Entity类实现
- **业务描述**
定义实体对象（POJO）封装从数据库查询的数据实现ORM映射
- **业务实现**

> 构建与sys_logs表对应的实体类型

```java
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

	// ...省略get/set方法
	
	// Test测试时使用
	@Override
	public String toString() {
		return "SysLog [id=" + id + ", username=" + username + ", operation=" + operation + ", method=" + method
				+ ", params=" + params + ", time=" + time + ", ip=" + ip + ", createdTime=" + createdTime + "]";
	}

}
```
> 通过此对象除了可以封装从数据库查询的数据，还可以封装客户端请求数据，实现层与层之间数据的传递

##### 2.1.2. Dao接口实现
- **业务描述**：(核心-查询当前页显示的数据以及总记录数)
1) 接收业务层参数数据
2) 基于参数进行数据查询
3) 将查询结果进行封装
4) 将结果返回给业务层对象
- **代码实现**

```java
public interface SysLogDao {

	/**
	 * 基于条件查询日志信息
	 * @param username 查询条件：操作用户
	 * @param startIndex 当前页面起始位置
	 * @param pageeSize 当前页面大小
	 * @return 当前野德日志信息记录
	 * 数据库中每条日志信息封装到一个SysLog对象中
	 */
	List<SysLog> findPageObjects(@Param("username") String username,
			  					 @Param("startIndex") Integer startIndex,
			  					 @Param("pageSize") Integer pageSize); 
	/**
	 * 基于条件查询总记录数
	 * @param username 查询条件：操作用户
	 * @return 总记录数
	 */
	int getRowCount(@Param("username") String username);
}
```
- **说明**：
1)  当DAO中方法参数多余一个时尽量使用@Param注解进行修饰并指定名字，然后再Mapper文件中便可以通过类似#{username}方式进行获取，否则只能通过#{0}，#{1}或者#{param1}，#{param2}等方式进行获取。
2)  当DAO方法中的参数应用在动态SQL中时无论多少个参数，尽量使用@Param注解进行修饰并定义。
##### 2.1.3. Mapper文件实现
- **业务描述**
1)  基于Dao接口创建映射文件
2)  基于Dao方法在映射文件中创建映射元素建映射元素
- **业务实现**
1) 创建映射文件
2) 创建映射元素实现翻页查询操作
3) 创建映射元素实现查询统计操作

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.jt.sys.dao.SysLogDao">
	
	<!-- 动态SQL拼接保证DAO参数数量可以变化 -->
	<sql id="queryWhereId">
		<where>
			  <if test="username!=null and username !=''">
				 username like concat("%",#{username},"%")
			</if>
		</where>
	</sql>
	<!-- 按条件查询日志信息 -->
	<select id="findPageObjects"
			resultType="com.jt.sys.entity.SysLog">
		select 
			*
		from
			sys_logs
		<include refid="queryWhereId"></include>
		order by
			createdTime
		desc
			limit #{startIndex},#{pageSize}
	</select>
	<!-- 按条件查询日志信息总记录数 -->
	<select id="getRowCount" resultType="int">
		select 
			count(*)
		from 
			sys_logs
	</select>

</mapper>
```
- **思考**
1)  动态sql:基于用于需求动态拼接SQL
2)  Sql标签元素的作用是什么?对sql语句中的共性进行提取,以遍实现更好的复用.
3)  Include标签的作用是什么?引入使用sql标签定义的元素
- **说明**
1)  当不记得数据库中有哪些函数时可以借助 ? functions 的方式进行查询.其中这个?等价于help单词.
##### 2.1.4. Service接口及实现类
- **业务描述**

> 核心业务就是分页查询数据并对数据进行封装

1) 通过参数变量接收控制层数据(例如username,pageCurrent)
2) 对数据进行合法验证(例如pageCurrent不能小于1)
3) 基于参数数据进行总记录数查询(通过此结果要计算总页数)
4) 基于参数数据进行当前页记录的查询(username,startIndex,pageSize)
5) 对数据进行封装(例如封装分页信息和当前页记录)
6) ......
- **业务实现**
1) 业务值对象定义:（封装分页信息以及当前数据）
-   包名 com.jt.common.vo （封装值的对象）
-  类名 PageObject<T> (实现序列化接口并添加序列化id)
-   属性 (总行数,总页数，当前页码，页面大小，当前页记录信息)
-   方法 (set/get)
```java
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
```
2) 接口定义：为控制层请求提供服务

```java
public interface SysLogService {

	/**
	 * 分页查询日志信息
	 * @param username 用户名
	 * @param pageCurrent 当前页面
	 * @return 对查询结果的封装
	 */
	PageObject<SysLog> findPageObjects(String username,Integer pageCurrent);
	
}
```
3)接口实现类(SysLogServiceImpl)

```java
@Service
public class SysLogServiceImpl implements SysLogService{

	/**
	 * 关联SysLogDao对象
	 */
	@Autowired
	private SysLogDao sysLogDao;
	
	
	@Override
	public PageObject<SysLog> findPageObjects(String username, Integer pageCurrent) {
		//参数有效性验证
		if(pageCurrent == null || pageCurrent <1)
			throw new IllegalArgumentException("当前页码不正确");
		//查询总记录数进行验证
		int rowCount = sysLogDao.getRowCount(username);
		if(rowCount == 0)
			throw new ServiceException("无记录");
		//查询当前日志记录
		int pageSize = 3;
		int startIndex = (pageCurrent-1)*pageSize;
		List<SysLog> records = sysLogDao.findPageObjects(username, startIndex, pageSize);
		//对结果集进行封装
		PageObject<SysLog> po = new PageObject<>();
		po.setRecords(records);
		po.setRowCount(rowCount);
		po.setPageSize(pageSize);
		po.setPageCurrent(pageCurrent);
		po.setPageCount((rowCount-1)/pageSize+1);
		return po;
	}
}
```
4) 自定义业务层异常,定位错误确定问题以便提供用户体验

```java
/**
 * 业务层运行时异常类
 * @author zn
 *
 */
public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ServiceException() {
		super();
	}

	public ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}

}
```
##### 2.1.5. Controller类的实现
- **业务描述**
> 处理客户端请求

1) 接收客户端请求中的数据
2) 基于请求调用业务方法进行请求处理
3) 对处理结果进行封装(JsonResult)
4) 将结果转换为json格式的字符串(适配不同客户端)
5) 将字符串通过服务器输出到客户端。
- **业务实现**
1) 值对象定义：（封装控制层方法的返回结果）

```java
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
	
	//...省略get/set方法
}
```
2)控制器层编写(SysLogController)

```java
@Controller
@RequestMapping("/log/")
public class SysLogController {
    //...
    
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
}
```
3)统一异常类及方法定义

```java
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
```
#### 2.2 客户端实现
##### 2.2.1 日志信息呈现
- **业务描述**
1) 列表页面加载完成发起异步请求加载日志信息
2) 通过服务端返回的数据更新当前列表页面
- **业务实现**
1) 定义doGetObjects()函数，通过此函数执行异步加载操作。
2) 分页页面加载完成以后调用doGetObjects()
- **JS代码**

log_list.html
```js
$(function(){
	  //回调函数
	  $("#pageId").load("doPageUI.do",function(){
		  doGetObjects()
	  })
)
//分页查询函数
function doGetObjects(){
	  //url
	  var url = "log/doFindPageObjects.do"
	  //参数param
	  var pageCurrent=$("#pageId").data("pageCurrent")
	  if(!pageCurrent) pageCurrent=1
	  // var pageCurrent=1
	  var params = {"pageCurrent":pageCurrent}
	  //验证参数
	  console.log(params);
	  //发起GET请求
	  $.getJSON(url,params,function(result){
	      //JsonResult->PageObject->List<SysLogs>+...
		  console.log(result)
		  doHandleResponseResult(result)
	  })  
}
//设置异步结果
function doHandleResponseResult(result){
	  if(result.state==1){
		  //更新table中tbody内部的数据
		  doSetTableBodyRows(result.data.records)   
		  //执行page分页操作
		  doSetPagination(result.data)
	  }else{
		  alert(result.msg)
	  }
}
//将异步响应结果呈现在table的tbody位置
function doSetTableBodyRows(records){
	  //1.获取tbody对象并清空
	  var tbody = $("#tbodyId")
	  tbody.empty()
	  //2.迭代records记录,追加到tbody上
	  for(var i in records){
		  var tr = $("<tr></tr>")
		  var tds = doCreateTds(records[i])
		  tr.append(tds)
		  tbody.append(tr)
	  }
}
//在td中加入相应Object对象信息
function doCreateTds(row){
	  var tds ="<td><input type='checkbox' class='cBox' name='cItem' value='"+row.id+"'></td>"+
		  "<td>"+row.username+"</td>"+
      	 "<td>"+row.operation+"</td>"+
      	 "<td>"+row.method+"</td>"+
     	 "<td>"+row.params+"</td>"+
      	 "<td>"+row.ip+"</td>"+
     	 "<td>"+row.time+"</td>"
    
     	 return tds
}
```
##### 2.2.2. 分页信息呈现
- **业务描述**
1) 列表日志信息异步加载完成以后初始化分页数据（调用setPagination函数）
2) 点击分页元素时异步加载当前页(pageCurrent)数据(调用jumpToPage)
- **业务实现** (page.html页面中定义JS函数)
1) 定义doSetPagination方法(实现分页数据初始化)
2) 定义doJumpToPage方法(通过此方法实现当前数据查询)
3) page.html页面加载完成以后在对应元素上注册click事件
- **JS代码**

```js
 $(function(){
	  $("#pageId").on("click",".first,.pre,.next,.last",doJumpToPage);
  });
  function doSetPagination(pageObject){
	 //1.初始化数据
	 $(".rowCount").html("总记录数("+pageObject.rowCount+")");
	 $(".pageCount").html("总页数("+pageObject.pageCount+")");
	 $(".pageCurrent").html("当前页("+pageObject.pageCurrent+")");
     //2.绑定当前页码以及总页数
     $("#pageId").data("pageCurrent",pageObject.pageCurrent);
     $("#pageId").data("pageCount",pageObject.pageCount);
  }
  function doJumpToPage(){
	// debugger
	 //1.获取点击对象的class值
	 var cls=$(this).prop("class");
	 //2.基于class值修改pageCurrent的值
	 var pageCurrent=$("#pageId").data("pageCurrent");
	 var pageCount=$("#pageId").data("pageCount");
	 if(cls=="first"){
		 pageCurrent=1;
	 }else if(cls=="pre"&&pageCurrent>1){
		 pageCurrent--;
	 }else if(cls=="next"&&pageCurrent<pageCount){
		 pageCurrent++;
	 }else if(cls=="last"){
		 pageCurrent=pageCount;
	 }
	 //3.绑定当前页码值到pageId对应的元素
	 $("#pageId").data("pageCurrent",pageCurrent);
	 //4.重新执行查询操作
	 doGetObjects();
  }
```
##### 2.2.3. 列表信息查询实现
- **业务说明**
1) 列表查询按钮事件注册
2) 列表查询按钮事件处理函数定义
3) 列表查询参数获取以及传递
- **业务实现**
1) 在$(function(){})回调函数中追加查询按钮的事件注册。
2) 定义查询按钮的事件处理函数doQueryObjects
3) 重用doGetObjects函数并添加查询参数name
- **JS代码**
> 1.查询按钮事件注册
```js
$(".input-group-btn").on("click",".btn-search",doQueryObjects)  
```
> 2.查询按钮事件处理函数定义

```js
function doQueryObjects(){
	//初始化pageCurrent=1
	$("#pageId").data("pageCurrent",1)
	//执行查询操作(复用)
	doGetObjects();
	
}
```
> 3.分页查询函数中追加name参数

```js
function doGetObjects(){
      //url
	  var url = "log/doFindPageObjects.do"
	  //参数param
	  var pageCurrent=$("#pageId").data("pageCurrent")
	  if(!pageCurrent) pageCurrent=1
	  // var pageCurrent=1
	  var params = {"pageCurrent":pageCurrent}
	  //基于用户名获取查询参数值username
	  var username = $("#searchNameId").val().trim();
	  if(username) params.username = username;
	  //验证参数
	  console.log(params);
	  //发起GET请求
	  $.getJSON(url,params,function(result){
		  console.log(result)
		  doHandleResponseResult(result)
	  }) 
}
```
### 3.日志管理删除操作实现
#### 3.1. 服务端实现
##### 3.1.1. Dao接口实现
- **业务描述**
1) 接收业务层参数数据（Integer… ids）
2) 基于数据执行删除操作
- **业务实现**
> 在SysLogDao接口中定义删除方法
- **代码实现**

```java
    /**
	 * 基于日志id删除相关日志信息记录
	 * @param ids
	 * @return
	 */
	int deleteObjects(@Param("ids") Integer...ids);
```
##### 3.1.2. Mapper文件实现
- **业务描述**
> 基于SysLogDao中方法的定义，编写删除元素。
- **业务实现**

```xml
    <delete id="deleteObjects">
		 delete from sys_Logs
       		where id in
       	<foreach collection="ids" open="(" separator="," close=")" item="item">
       		#{item}
       	</foreach>
	</delete>
```
##### 3.1.3. Service接口实现
- **业务描述**
1) 接收控制层数据并进行合法验证
2) 基于业务层数据执行删除操作
3) 对删除过程进行监控(先进行异常捕获)
4) 对删除结果进行验证并返回
- **业务实现**
> 在SysLogService接口及实现类中添加方法

```java
    /**
	 * 删除日志信息
	 * @param ids
	 * @return
	 */
	int deleteObjects(Integer... ids);
```
> 实现类

```java
    @Override
	public int deleteObjects(Integer... ids) {
		//1. 判定参数合法性
		if(ids==null || ids.length==0)
			throw new IllegalArgumentException("请选择一个要删除的数据");
		//2. 执行删除操作
		int rows;
		try {
			rows = sysLogDao.deleteObjects(ids);			
		} catch (Exception e) {
			e.printStackTrace();
			//给运维人员发出报警信息
			throw new ServiceException("系统故障,正在恢复中...");
		}
		//4.对结果进行验证
		if(rows==0) {
			throw new ServiceException("记录可能已经不存在");
		}
		return rows;
	}
```
##### 3.1.4. Controller实现
- **业务描述**

1) 接收客户端请求数据
2) 调用业务层方法执行删除操作
3) 封装结果并返回
- **业务实现**
> 在SysLogController中定义删除方法

```java
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
```
#### 3.2. 客户端的实现
##### 3.2.1. 日志列表页面实现
- **业务描述**

1) 页面全选操作实现
2) 页面删除按钮事件注册
3) 页面删除操作事件处理函数定义
- **业务实现**
1) Thead中全选checkbox元素事件注册及事件处理函数doCheckAll定义
2) Tbody中checkbox元素事件注册及事件处理函数doChangeCheckAllState定义
3) 在$(function(){})回调函数中追加删除按钮的事件注册操作
4) 定义事件处理函数doDeleteObjects,处理删除按钮的点击操作
- **JS代码**
> 删除函数

```js
//执行删除操作
function doDeleteObjects(){
	//1.获取选中的id值(执行doGetCkeckedIds函数)
	var ids = doGetCheckedIds()
	if(ids.length==0){
		alert("请至少选择一个删除数据")
		return
	}
	//2.发出请求
	//获取url和参数
	var url = "log/doDeleteObjects.do"
	var params = {"ids":ids.toString()} //[1,2,3,4] ->1,2,3,4
	//测试参数
	console.log(params)
	$.post(url,params,function(result){
		if(result.state==1){
			alert(result.message)
			doGetObjects()
		}else{
			alert(result.message)
		}
	})
}
```
> 全选

```js
//全选函数
//当tbody中checkbox的状态发生变化以后,修改thead中全选元素的状态值
function doChangeTBodyCheckBoxState(){
	 //1.获取当前点击对象的checked属性的值
  	var flag=$(this).prop("checked");//true or false
 	 $("#tbodyId input[name='cItem']").prop("checked",flag);
}

//Tbody中checkbox的状态影响thead中全选元素的状态
function doChangeTheadCheckBoxState(){
	
	var flag = true;
	$("#tbodyId input[type='checkbox']").each(function(){
		flag=flag&&$(this).prop("checked")
	})
	//修改全选元素checkbox的值为flag
	$("#checkAll").prop("checked",flag)
}
```
### 4.日志管理数据添加实现
#### 4.1. 服务端实现
#### 4.2.　客户端实现
### 5.测试
#### 5.1 DAO层测试
#####  1)测试代码

```java
public class SysLogDaoTest extends TestBase {

	@Test
	public void testFindPageObjects() {
		SysLogDao dao = ctx.getBean("sysLogDao",SysLogDao.class);
		List<SysLog> list = dao.findPageObjects("admin", 0, 10);
		for(SysLog s:list) {
			System.out.println(s);
		}
	}
	
	@Test
	public void testgetRowCount() {
		SysLogDao dao = ctx.getBean("sysLogDao",SysLogDao.class);
		int rowCount = dao.getRowCount("admin");
		System.out.println(rowCount);
	}
	
	@Test
	public void testdeleteObjects() {
		SysLogDao dao = ctx.getBean("sysLogDao",SysLogDao.class);
		int rows = dao.deleteObjects(38);
		System.out.println("删除了"+rows+"行记录");
	}
}
```
##### 2)测试结果
testFindPageObjects:
![image](https://java-1257588924.cos.ap-shanghai.myqcloud.com/JT/20180914222509.png)
testgetRowCount():
![image](https://java-1257588924.cos.ap-shanghai.myqcloud.com/JT/20180914222634.png)
testdeleteObjects():<br>
![image](https://java-1257588924.cos.ap-shanghai.myqcloud.com/JT/20180914164522.png)
![image](https://java-1257588924.cos.ap-shanghai.myqcloud.com/JT/20180914165005.png)
![image](https://java-1257588924.cos.ap-shanghai.myqcloud.com/JT/20180914164609.png)
#### 5.2 Service层测试
##### 1)测试代码

```java
public class SysLogServiceTest extends TestBase {

	@Test
	public void testSysLogService() {
		SysLogServiceImpl service = ctx.getBean("sysLogServiceImpl",SysLogServiceImpl.class);
		PageObject<SysLog> po = service.findPageObjects("admin", 2);
		List<SysLog> records = po.getRecords();
		for(SysLog s:records) {
			System.out.println(s);
		}
	}
}
```
![image](https://java-1257588924.cos.ap-shanghai.myqcloud.com/JT/20180914223615.png)
#### 5.3 Cotroller层测试及页面效果(启tomcat)
##### 1.服务端接口测试
分页查询日志信息
- 访问案例(参数完整)
![image](https://java-1257588924.cos.ap-shanghai.myqcloud.com/SSM/20180912212534.png)
![image](https://java-1257588924.cos.ap-shanghai.myqcloud.com/SSM/20180912212753.png)
- 访问案例(username参数不完整)
![image](https://java-1257588924.cos.ap-shanghai.myqcloud.com/SSM/20180912212836.png)
![image](https://java-1257588924.cos.ap-shanghai.myqcloud.com/SSM/20180912212822.png)
- 访问案例(pageCurrent参数不正确)
![image](https://java-1257588924.cos.ap-shanghai.myqcloud.com/SSM/20180912213330.png)
![image](https://java-1257588924.cos.ap-shanghai.myqcloud.com/SSM/20180912213920.png)
按id删除日志信息
![image](https://java-1257588924.cos.ap-shanghai.myqcloud.com/JT/20180914164557.png)
##### 2.客户端演示
![image](https://java-1257588924.cos.ap-shanghai.myqcloud.com/JT/20180914225331.png)

