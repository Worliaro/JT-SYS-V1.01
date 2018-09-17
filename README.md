# JT-SYS-V1.01
## JT-电商项目-权限管理子系统
## 菜单管理
[toc]

---
### 1.菜单页面呈现
> 核心业务:点击系统首页中的菜单管理, 在对应的一个div中异步加载菜单页面.

#### 1.1.服务端实现
##### 1.1.1. 菜单Controller实现
- **业务描述**
1)	定义controller对象处理客户端菜单访问请求
2)	定义相关方法处理列表页面加载需求
- **业务实现**
> 创建一个SysMenuController类,基于Controller类定义页面响应方法
- **代码实现**
```java
package com.jt.sys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * 菜单管理控制
 * @author zn
 *
 */
@Controller
@RequestMapping("/menu/")
public class SysMenuController {

	@RequestMapping("doMenuListUI")
	public String doMenuListUI() {
		return "sys/menu_list";
	}
}
```
#### 1.2. 客户端实现
##### 1.2.1. 定义菜单列表页面
> 在WEB-INF/pages/sys目录下定义menu_list.html页面.

##### 1.2.2. 首页页面菜单管理事件处理
- **业务描述**
1) 在starter.html页面中注册菜单管理的点击事件
2) 在starter.html页面中定义事件处理函数,异步加载菜单列表页面
- **代码实现**

```js
     $(function(){
    	 doLoadUI("load-menu-id","menu/doMenuListUI.do")
    	 doLoadUI("load-log-id","log/doLogListUI.do")
      })
      function doLoadUI(id,url){
    	  $("#"+id).click(function(){
    		   //异步加载url对应的资源,并将资源插入到#mainContentId位置
         	   $("#mainContentId").load(url,function(){
         		   //初始化#mainContentId位置上的信息资源
         		  $("#mainContentId").removeData()
         	   });
          });
      }
```
### 2.菜单页面数据呈现
#### 2.1. 服务端实现
> 核心业务：<br>
从数据库查询菜单以及上一级的菜单信息,并将数据信息进行封装(本模块封装到map),传递

##### 2.1.1. DAO实现
- **业务描述**
1)	定义菜单持久层对象,处理数据访问操作
2)	定义菜单查询方法,查询所有菜单以及上一级菜单信息(只取id,名字)
- **业务实现**
> 定义查询方法,返回map对象(一行记录对应一个map,多个map放list)
```java
public interface SysMenuDao {

	/**
	 * 查询系统菜单所有数据信息
	 * @return map对象
	 */
	List<Map<String, Object>> findObjects();
	
}
```
##### 2.1.2. Mapper实现
- **业务描述**
1) 基于SysMenuDao创建SysMenuMapper文件,并在文件中
2) 基于SysMenuDao的查询方法定义SQL映射元素
- **业务实现**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.jt.sys.dao.SysMenuDao">
	
	<!-- 查询系统菜单 -->
	<select id="findObjects" resultType="map">
		SELECT
			c.*, p. NAME parentName
		FROM
			sys_menus c
		LEFT JOIN sys_menus p ON c.parentId = p.id
		<!-- 方案二
		 SELECT
			c.*, (
				SELECT
					p. NAME
				FROM
					sys_menus p
				WHERE
					c.parentId = p.id
			) parentName
		FROM
			sys_menus c
			 -->
	</select>
</mapper>
```
##### 2.1.3. Service实现
- **业务描述**
1)	定义菜单业务接口,负责处理菜单模块业务
2)	定义业务方法,访问dao层方法获取菜单信息
- **业务实现**
SysMenuService
```java
public interface SysMenuService {

	/**
	 * 查询系统菜单所有记录
	 * @return
	 */
	List<Map<String,Object>> findObjects();
}
```
SysMenuServiceImpl

```java
@Service
public class SysMenuServiceImpl implements SysMenuService{

	@Autowired
	private SysMenuDao sysMenuDao;

	@Override
	public List<Map<String, Object>> findObjects() {
		return sysMenuDao.findObjects();
	}
}
```
##### 2.1.4. Controller实现
- **业务描述**
1)基于客户端请求,借助业务层对象访问菜单信息
2)对菜单信息进行封装,并返回
- **业务实现**
SysMenuController

```java
    /**
	 * 返回JSON字段,获取系统菜单所有数据
	 * @return
	 */
	@RequestMapping("doFindObjects")
	@ResponseBody
	public JsonResult doFindObjects() {
		return new JsonResult(sysMenuService.findObjects());
	}
```
#### 2.2. 客户端实现
##### 2.2.1. 菜单列表页面实现
- **业务描述**
1)	数据呈现时使用Bootstrap中的treeGrid插件.
2)	页面加载完成,异步加载数据,以树结构table形式呈现.
- **业务实现**
1)	引入treeGrid相关js文件
2)	定义方法异步加载数据
- **代码实现**
```js
/**
 * 初始化表格的列
 */
var columns = [
{
	field : 'selectItem',
	radio : true
},
{
	title : '菜单ID',
	field : 'id',
	visible : false,
	align : 'center',
	valign : 'middle',
	width : '80px'
},
{
	title : '菜单名称',
	field : 'name',
	align : 'center',
	valign : 'middle',
	sortable : true,
	width : '130px'
},
{
	title : '上级菜单',
	field : 'parentName',
	align : 'center',
	valign : 'middle',
	sortable : true,
	width : '100px'
},
{
	title : '类型',
	field : 'type',
	align : 'center',
	valign : 'middle',
	sortable : true,
	width : '70px',
	formatter : function(item, index) {
		if (item.type == 1) {
			return '<span class="label label-success">菜单</span>';
		}
		if (item.type == 2) {
			return '<span class="label label-warning">按钮</span>';
		}
	}
}, 
{
	title : '排序号',
	field : 'sort',
	align : 'center',
	valign : 'middle',
	sortable : true,
	width : '70px'
}, 
{
	title : '菜单URL',
	field : 'url',
	align : 'center',
	valign : 'middle',
	sortable : true,
	width : '160px'
}, 
{
	title : '授权标识',       //标题名称
	field : 'permission',  //json串key
	align : 'center',      //水平居中
	valign : 'middle',     //垂直居中
	sortable : true        //是否排序
} ];//格式来自官方demos -->treeGrid(jquery扩展的一个网格树插件)

$(function(){
	doGetObjects()
})
function doGetObjects(){
	//构建table对象
	var table = new TreeTable(
			"menuTable",
			"menu/doFindObjects.do",
			columns)
	table.setExpandColumn(2)
	//初始化table对象(底层发起ajax请求)
	table.init()
}
```
#### 2.3. 测试
##### 2.3.1 接口测试
![image](https://java-1257588924.cos.ap-shanghai.myqcloud.com/JT/20180915121344.png)
![image](https://java-1257588924.cos.ap-shanghai.myqcloud.com/JT/20180915121619.png)
##### 2.3.2 web端测试
![image](https://java-1257588924.cos.ap-shanghai.myqcloud.com/JT/20180915121804.png)
### 3. 菜单数据删除
> 核心业务

1)	查询此菜单有没有对应的子菜单,有则不允许删除
2)	删除菜单(市场有两种业务)
-	有角色菜单关系数据,则不允许删除
-	有角色菜单关系数据,将关系数据一起删除.(本次策略)
#### 3.1. 服务端实现
##### 3.1.1. DAO实现
- **业务实现**
> SysMenuDao

1)	基于菜单id查询是否有子菜单(有子菜单则不能删除)

```java
    /**
	 * 根据菜单id统计子菜单的个数
	 * @param id 菜单id
	 * @return 子菜单个数
	 */
	int getChildCount(Integer id);
```

2)	根据菜单id删除菜单自身信息.

```java
    /**
	 * 根据菜单id删除菜单
	 * @param id
	 * @return
	 */
	int deleteObject(Integer id);
```
> SysRoleMenuDao

3)	根据菜单id删除角色菜单关系数据

```java
public interface SysRoleMenuDao {

	/**
	 * 根据删除的菜单id删除关联的角色信息
	 * @param menuId
	 * @return
	 */
	int deleteObjectsByMenuId(@Param("menuId") Integer menuId);
}

```
##### 3.1.2. Mapper实现
- **业务描述**
1) 基于SysMenuDao中方法定义映射元素.
2) 基于SysRoleMenuDao中方法定义映射元素
- **业务实现**
1)	SysMenuMapper中统计子菜单元素定义
2)	SysMenuMapper根据id删除菜单的元素定义

```xml
    <!-- 根据id统计子菜单个数 -->
	<select id="getChildCount" resultType="int">
		select count(*) 
		from sys_menus
		where parentId = #{id}
	</select>
	
	<!-- 根据id删除菜单 -->
	<delete id="deleteObject">
		delete from sys_menus
		where id = #{id}
	</delete>
```

3)	SysRoleMenuMapper文件中定义基于菜单id删除记录的元素

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.jt.sys.dao.SysRoleMenuDao">
	
	<!-- 根据子菜单菜单删除关联的角色记录 -->
	<delete id="deleteObjectsByMenuId" parameterType="int">
		delete from sys_role_menus
		where menu_id =#{menuId}
	</delete>
</mapper>
```
##### 3.1.3. Service实现
> 中间表服务于其他表没有业务层

- **业务描述**
1)	接收控制数据(id),并对数据进行合法验证
2)	基于id查询菜单子元素,并进行判定
3)	删除菜单元素
4)	删除角色菜单关系数据.
5)	返回结果.
- **业务实现**
> SysMenuService

```java
    /**
	 * 执行删除指定的菜单操作
	 * @param id 菜单id
	 * @return 删除的行数
	 */
	int deleteObject(Integer id);
```
> SysMenuServiceImpl (这里没有开启事务处理)

```java
@Service
public class SysMenuServiceImpl implements SysMenuService{

	@Autowired
	private SysMenuDao sysMenuDao;

	@Autowired
	private SysRoleMenuDao sysRoleMenuDao;
	
	//...其他操作

	@Override
	public int deleteObject(Integer id) {
		//1.验证数据的合法性
		if(id == null|| id<=0)
			throw new ServiceException("请先选择一个要删除的子菜单");
		//2.基于id进行子元素的查询
		int count = sysMenuDao.getChildCount(id);
		if(count>0)
			throw new ServiceException("请先删除子菜单");
		//3.删除菜单元素
		int rows = sysMenuDao.deleteObject(id);
		if(rows == 0)
			throw new ServiceException("此菜单可能已经不存在");
		//4.删除角色,菜单关联的角色数据
		sysRoleMenuDao.deleteObjectsByMenuId(id);
		return rows;
	}
}
```
##### 3.1.4. Controller实现
- **业务描述**
1)	获取客户端请求数据(菜单id)
2)	调用业务层方法删除数据
3)	封装并返回结果
- **业务实现**

```java
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
```
##### 3.1.5. 服务端测试
> 测试删除根菜单

![image](https://java-1257588924.cos.ap-shanghai.myqcloud.com/JT/sys_menu/20180915144236.png)
![image](https://java-1257588924.cos.ap-shanghai.myqcloud.com/JT/sys_menu/20180915144650.png)
> 测试删除子菜单

![image](https://java-1257588924.cos.ap-shanghai.myqcloud.com/JT/sys_menu/20180915144434.png)
![image](https://java-1257588924.cos.ap-shanghai.myqcloud.com/JT/sys_menu/20180915144902.png)

#### 3.2. 客户端实现
##### 3.2.1. 列表页面删除按钮事件处理
- **业务描述**
> 点击删除按钮时向服务端提交异步请求,删除用户选中的菜单记录.
- **业务实现**
1)	删除按钮事件注册
2)	删除按钮事件处理函数定义.
- **JS代码**
```js
$(function(){
	//...
	$(".input-group-btn").on("click",".btn-delete",doDeleteObject)
})
//子菜单删除函数
function doDeleteObject(){
	//1.获取选中的id
	var id = doGetCheckedId()
	if(!id){
		alert("请选择你要删除的子菜单")
		return
	}
	//2.异步请求
	//定义url和参数
	var url = "menu/doDeleteObject.do"
	var params = {"id":id}
	$.post(url,params,function(result){
		if(result.state==1){
			alert(result.message)
			doGetObjects()
		}else{
			alert(result.message)
		}
	})
}
//获取选中记录的id值
function doGetCheckedId(){
	//1.获取选中的记录
	var selections=$("#menuTable")
	//bootstrapTreeTable是treeGrid插件内部定义的jquery扩展函数
	//getSelections为扩展函数内部要调用的一个方法
	.bootstrapTreeTable("getSelections");
	//2.对记录进行判定
	if(selections.length==1)
	return selections[0].id;

}
```
##### 3.2.2 web端测试
![image](https://java-1257588924.cos.ap-shanghai.myqcloud.com/JT/sys_menu/20180915153520.png)
![image](https://java-1257588924.cos.ap-shanghai.myqcloud.com/JT/sys_menu/20180915153538.png)

```
DEBUG [http-bio-80-exec-8] - DispatcherServlet with name 'dispatcherServlet' processing POST request for [/JT-SYS-V1.01/menu/doDeleteObject.do]
DEBUG [http-bio-80-exec-8] - Looking up handler method for path /menu/doDeleteObject.do
DEBUG [http-bio-80-exec-8] - Returning handler method [public com.jt.common.vo.JsonResult com.jt.sys.controller.SysMenuController.doDeleteObject(java.lang.Integer)]
DEBUG [http-bio-80-exec-8] - Returning cached instance of singleton bean 'sysMenuController'
DEBUG [http-bio-80-exec-8] - Skip CORS processing: request is from same origin
DEBUG [http-bio-80-exec-8] - Creating a new SqlSession
DEBUG [http-bio-80-exec-8] - SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@5dc84ba2] was not registered for synchronization because synchronization is not active
DEBUG [http-bio-80-exec-8] - Fetching JDBC Connection from DataSource
DEBUG [http-bio-80-exec-8] - JDBC Connection [com.mysql.jdbc.JDBC4Connection@2a935bc6] will not be managed by Spring
DEBUG [http-bio-80-exec-8] - ==>  Preparing: select count(*) from sys_menus where parentId = ? 
DEBUG [http-bio-80-exec-8] - ==> Parameters: 131(Integer)
DEBUG [http-bio-80-exec-8] - <==      Total: 1
DEBUG [http-bio-80-exec-8] - Closing non transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@5dc84ba2]
DEBUG [http-bio-80-exec-8] - Returning JDBC Connection to DataSource
DEBUG [http-bio-80-exec-8] - Creating a new SqlSession
DEBUG [http-bio-80-exec-8] - SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@7cab3d6c] was not registered for synchronization because synchronization is not active
DEBUG [http-bio-80-exec-8] - Fetching JDBC Connection from DataSource
DEBUG [http-bio-80-exec-8] - JDBC Connection [com.mysql.jdbc.JDBC4Connection@2a935bc6] will not be managed by Spring
DEBUG [http-bio-80-exec-8] - ==>  Preparing: delete from sys_menus where id = ? 
DEBUG [http-bio-80-exec-8] - ==> Parameters: 131(Integer)
DEBUG [http-bio-80-exec-8] - <==    Updates: 1
DEBUG [http-bio-80-exec-8] - Closing non transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@7cab3d6c]
DEBUG [http-bio-80-exec-8] - Returning JDBC Connection to DataSource
DEBUG [http-bio-80-exec-8] - Creating a new SqlSession
DEBUG [http-bio-80-exec-8] - SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@67c56184] was not registered for synchronization because synchronization is not active
DEBUG [http-bio-80-exec-8] - Fetching JDBC Connection from DataSource
DEBUG [http-bio-80-exec-8] - JDBC Connection [com.mysql.jdbc.JDBC4Connection@2a935bc6] will not be managed by Spring
DEBUG [http-bio-80-exec-8] - ==>  Preparing: delete from sys_role_menus where menu_id =? 
DEBUG [http-bio-80-exec-8] - ==> Parameters: 131(Integer)
DEBUG [http-bio-80-exec-8] - <==    Updates: 0
DEBUG [http-bio-80-exec-8] - Closing non transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@67c56184]
DEBUG [http-bio-80-exec-8] - Returning JDBC Connection to DataSource
DEBUG [http-bio-80-exec-8] - Written [com.jt.common.vo.JsonResult@7d403b3e] as "application/json" using [org.springframework.http.converter.json.MappingJackson2HttpMessageConverter@6be57ff4]
DEBUG [http-bio-80-exec-8] - Null ModelAndView returned to DispatcherServlet with name 'dispatcherServlet': assuming HandlerAdapter completed request handling
DEBUG [http-bio-80-exec-8] - Successfully completed request
DEBUG [http-bio-80-exec-8] - DispatcherServlet with name 'dispatcherServlet' processing GET request for [/JT-SYS-V1.01/menu/doFindObjects.do]
DEBUG [http-bio-80-exec-8] - Looking up handler method for path /menu/doFindObjects.do
DEBUG [http-bio-80-exec-8] - Returning handler method [public com.jt.common.vo.JsonResult com.jt.sys.controller.SysMenuController.doFindObjects()]
DEBUG [http-bio-80-exec-8] - Returning cached instance of singleton bean 'sysMenuController'
DEBUG [http-bio-80-exec-8] - Last-Modified value for [/JT-SYS-V1.01/menu/doFindObjects.do] is: -1
DEBUG [http-bio-80-exec-8] - Creating a new SqlSession
DEBUG [http-bio-80-exec-8] - SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@31c5bc53] was not registered for synchronization because synchronization is not active
DEBUG [http-bio-80-exec-8] - Fetching JDBC Connection from DataSource
DEBUG [http-bio-80-exec-8] - JDBC Connection [com.mysql.jdbc.JDBC4Connection@2a935bc6] will not be managed by Spring
DEBUG [http-bio-80-exec-8] - ==>  Preparing: SELECT c.*, p. NAME parentName FROM sys_menus c LEFT JOIN sys_menus p ON c.parentId = p.id 
DEBUG [http-bio-80-exec-8] - ==> Parameters: 
DEBUG [http-bio-80-exec-8] - <==      Total: 18
DEBUG [http-bio-80-exec-8] - Closing non transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@31c5bc53]
DEBUG [http-bio-80-exec-8] - Returning JDBC Connection to DataSource
DEBUG [http-bio-80-exec-8] - Written [com.jt.common.vo.JsonResult@172282dd] as "application/json" using [org.springframework.http.converter.json.MappingJackson2HttpMessageConverter@6be57ff4]
DEBUG [http-bio-80-exec-8] - Null ModelAndView returned to DispatcherServlet with name 'dispatcherServlet': assuming HandlerAdapter completed request handling
DEBUG [http-bio-80-exec-8] - Successfully completed request
```

### 4. 菜单添加页面呈现

#### 4.1. 服务端实现
##### 4.1.1. 菜单Controller方法定义
- **业务描述**
> 基于客户端的添加请求返回一个响应页面
- **业务实现**
> SysMenuController
```
    /**
	 * 加载菜单编辑页面
	 * 添加+修改页面共用
	 * @return
	 */
	@RequestMapping("doMenuEditUI")
	public String doMenuEditUI() {
		return "sys/menu_edit";
	}
```
#### 4.2. 客户端实现
##### 4.2.1. 菜单编辑页面定义
> 在WEB-INF/pages/sys目录中添加menu_edit.html.

##### 4.2.2. 菜单列表页面添加按钮事件处理
- **业务描述**

> 在菜单管理页面点击添加按钮异步访问服务器,加载对应菜单编辑页面

- **业务实现**

```js
$(function(){
    //...	
	$(".input-group-btn")
    .on("click",".btn-add",doLoadEditUI);
});
```
### 5.菜单编辑页面上级菜单呈现
> 核心业务

1)	点击编辑页面的上级菜单时向服务端发起异步请求
2)	服务端基于请求获取菜单节点数据
3)	客户端通过服务端返回数据异步刷新页面.

#### 5.1. 服务端实现
##### 5.1.1. Node值对象
- **业务描述**
> 定义一个Node对象,借助此对象封装从数据库查询到的数据
- **业务实现**

```java
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

    //...省略get/set方法

	@Override
	public String toString() {
		return "Node [id=" + id + ", name=" + name + ", parentId=" + parentId + "]";
	}
	
}
```
##### 5.1.2. Dao实现
- **业务描述**
> 基于请求获取数据库对应的菜单表中的所有菜单信息(id,name,parentId)
- **业务实现**
> SysMenuDao
```java
    /**
	 * 查询菜单节点信息
	 * @return
	 */
	List<Node> findZtreeMenuNodes();
```
##### 5.1.3. Mapper实现
- **业务描述**
> 基于SysMenuDao中方法定义菜单节点查询元素
- **业务实现**

```xml
    <!-- 查询菜单节点信息(最后将信息呈现到zTree上)-->
	 <select id="findZtreeMenuNodes" resultType="com.jt.common.vo.Node">
        select id,name,parentId
        from sys_menus        
    </select>
```
##### 5.1.4. Service实现
- **业务描述**
> 基于客户端请求,借助dao对象,访问菜单节点信息,并返回
- **业务实现**
> SysMenuService

```java
    /**
	 * 查询菜单节点信息
	 * @return
	 */
	List<Node> findZtreeMenuNodes();
```
> SysMenuServiceImpl

```java
    @Override
	public List<Node> findZtreeMenuNodes() {
		return sysMenuDao.findZtreeMenuNodes();
	}
```
##### 5.1.5. Controller实现
- **业务描述**
> 基于客户端请求,访问业务层对象方法,获取菜单节点对象,并封装返回.
- **业务实现**
> SysMenuController

```java
    /**
	 * 查询菜单节点信息
	 * @return
	 */
	@RequestMapping("doFindZtreeMenuNodes")
	@ResponseBody
	public JsonResult doFindZtreeMenuNodes() {
		return new JsonResult(sysMenuService.findZtreeMenuNodes());
	}
```
##### 5.1.6. 接口测试
#### 5.2. 客户端实现
> zTree

##### 5.2.1. 菜单编辑页面事件处理
- **业务描述**
点击上级菜单时,异步加载菜单节点信息,并进行数据呈现
- **业务实现**
1)	点击事件注册
2)	事件处理函数定义
- **JS代码**
> zTree
```js
var zTree; 
  //初始化zTree时会用到
  var setting = {
  	data : {
  		simpleData : {
  			enable : true,
  			idKey : "id",  //节点数据中保存唯一标识的属性名称
  			pIdKey : "parentId",  //节点数据中保存其父节点唯一标识的属性名称
  			rootPId : null  //根节点id
  		}//json 格式javascript对象
  	}
  }//json 格式的javascript对象
```
> 注册事件

```js
 $(function(){
	  //注册上级菜单事件
	  $(".form-horizontal").on("click",".load-sys-menu",doLoadZtreeMenuNodes)
	  //显示和隐藏zTree
	  $("#menuLayer").on("click",".btn-confirm",doSetSelectNode)
	  .on("click",".btn-cancel",doHideTree)
	 	  
  })
```
> 事件函数定义

```js
//异步请求
  function doLoadZtreeMenuNodes(){
	  //显示div(zTree)
	  $("#menuLayer").css("display","block")
	  //获取url
	  var url = "menu/doFindZtreeMenuNodes.do"
	  $.getJSON(url,function(result){
		  if(result.state==1){
			  //使用init函数需要先引入ztree对应的js文件
			  zTree=$.fn.zTree.init(
					  $("#menuTree"),
					  setting,
					  result.data);
		  }else{
			  alert(result.message);
		  }
	  })
		  
  }
  //显示zTree
  function doSetSelectNode(){
	  //1.获取选中的节点对象
	  var nodes=zTree.getSelectedNodes()
	  if(nodes.length==0)return
	  var node=nodes[0]
	  console.log(node)
	  //2.将对象中内容,填充到表单
	  $("#parentId").data("parentId",node.id)
	  $("#parentId").val(node.name)
	  //3.隐藏树对象
	  doHideTree()
  }
  //隐藏zTree
  function doHideTree(){
	  $("#menuLayer").css("display","none")
  }
```
### 6.菜单数据添加操作
#### 6.1.服务端实现
> 核心业务描述

点击页面保存按钮时，获取菜单表单数据，异步提交到服务器端
##### 6.1.1. Entity对象定义
- **业务描述**
创建实体类()封装客户端请求中的的菜单数据
- **业务实现**

```java
package com.jt.sys.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统菜单实体类
 * @author zn
 *
 */
public class SysMenu implements Serializable {

	private static final long serialVersionUID = 1L;
	/** 菜单id */
	private Integer id;
	/** 菜单名 */
	private String name;
	/** 菜单对应的url */
	private String url;
	/** 菜单类型 */
	private Integer type;
	/** 菜单的排序号 */
	private Integer sort;
	/** 备注 */
	private String note;
	/** 菜单的父id */
	private Integer parentId;
	/** 权限标识 */
	private String permission;

	private Date createdTime;
	private Date modifiedTime;
	private String createdUser;
	private String modifiedUser;

	//...省略set/get方法
	
	@Override
	public String toString() {
		return "SysMenu [id=" + id + ", name=" + name + ", url=" + url + ", type=" + type + ", sort=" + sort + ", note="
				+ note + ", parentId=" + parentId + ", permission=" + permission + ", createdTime=" + createdTime
				+ ", modifiedTime=" + modifiedTime + ", createdUser=" + createdUser + ", modifiedUser=" + modifiedUser
				+ "]";
	}

}
```
##### 6.1.2.Dao 实现
- **业务描述**
> 在SysMenuDao中添加插入数据的方法，用于将菜单信息写入到数据库
- **业务实现**

```java
    /**
	 * 添加子菜单
	 * @param entity
	 * @return
	 */
	int insertObject(SysMenu entity);
```
##### 6.1.3. Mapper中元素定义
- **业务描述**
> 基于SysMenuDao中方法定义SQL映射元素
- **业务实现**
> SysMenuMapper

```xml
    <!-- 向sys_menus中添加数据,parameterType类型可省略 -->
	<insert id="insertObject" parameterType="com.jt.sys.entity.SysMenu">
		 insert into sys_menus
          (name,url,type,sort,note,parentId,permission,
			createdTime,modifiedTime,createdUser,modifiedUser)
          values
          (#{name},#{url},#{type},#{sort},#{note},#{parentId},#{permission},
			now(),now(),#{createdUser},#{modifiedUser})
	</insert>
```
##### 6.1.4. Service中方法定义
- **业务描述**
1)	获取页面中数据，并对数据进行合法验证
2)	将数据写入到数据库
3)	判定结果，并返回
- **业务实现**
> SysMenuService

```java
    /**
	 * 添加子菜单
	 * @param entity
	 * @return
	 */
	int saveObject(SysMenu entity);
```
> SysMenuServiceImpl

```java
    @Override
	public int saveObject(SysMenu entity) {
		// 1.验证数据有效性
		if(entity== null)
			throw new IllegalArgumentException("保存对象不能为空");
		if(StringUtils.isEmpty(entity.getUrl()))
			throw new IllegalArgumentException("路径不能为空,");
		if(StringUtils.isEmpty(entity.getPermission()))
			throw new IllegalArgumentException("权限标识不能为空");
		//2.保存数据
		int rows;
		try {
			rows=sysMenuDao.insertObject(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("保存失败");
		}
		//3.返回数据
		return rows;
	}
```
##### 6.1.5. Controller实现
- **业务描述**
1)	获取客户端请求数据
2)	调用业务层方法将数据写入数据库
3)	封装结果并返回
- **业务实现**
> SysMenuController

```java
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
```
#### 6.2. 客户端实现
##### 6.2.1. 保存按钮事件处理
- **业务描述**
1)	编辑页面save按钮事件注册及事件处理函数定义
2)	编辑页面Cancel按钮事件注册及事件处理函数定义
- **业务实现**
1)	按钮点击事件注册
2)	事件处理函数定义
- **JS代码**
> 事件注册

```js
  $(function(){
    //注册上级菜单事件
	  $(".form-horizontal").on("click",".load-sys-menu",doLoadZtreeMenuNodes)
	  .on("click",".btn-cancel",doCancel)
	  .on("click",".btn-save",doSaveOrUpdate)
	  //...
  }
```


> Cancel 按钮事件处理函数定义

```js
 //取消事件
  function doCancel(){
	  $("#mainContentId").load("menu/doMenuListUI.do",function(){
		  $("#mainContentId").removeData();
		  $("#parentId").removeData();
	  });
  }
  //保存事件
  function doSaveOrUpdate(){
	  //1.获取表单数据
	  var params=doGetEditFormData();
	  //2.定义url
	  var url="menu/doSaveObject.do";
	  //3.异步提交数据
	  $.post(url,params,function(result){
		  if(result.state==1){
			  alert(result.message);
			  doCancel();
		  }else{
			  alert(result.message);
		  }
	  });
  }
```
##### 6.2.2. web端测试
> 参数正常

![image](https://java-1257588924.cos.ap-shanghai.myqcloud.com/JT/sys_menu/20180915195104.png)
![image](https://java-1257588924.cos.ap-shanghai.myqcloud.com/JT/sys_menu/20180915195122.png)
![image](https://java-1257588924.cos.ap-shanghai.myqcloud.com/JT/sys_menu/20180915195138.png)
> 缺失参数

![image](https://java-1257588924.cos.ap-shanghai.myqcloud.com/JT/sys_menu/20180915195526.png)

```
DEBUG [http-bio-80-exec-4] - DispatcherServlet with name 'dispatcherServlet' processing GET request for [/JT-SYS-V1.01/menu/doFindObjects.do]
DEBUG [http-bio-80-exec-4] - Looking up handler method for path /menu/doFindObjects.do
DEBUG [http-bio-80-exec-4] - Returning handler method [public com.jt.common.vo.JsonResult com.jt.sys.controller.SysMenuController.doFindObjects()]
DEBUG [http-bio-80-exec-4] - Returning cached instance of singleton bean 'sysMenuController'
DEBUG [http-bio-80-exec-4] - Last-Modified value for [/JT-SYS-V1.01/menu/doFindObjects.do] is: -1
DEBUG [http-bio-80-exec-4] - Creating a new SqlSession
DEBUG [http-bio-80-exec-4] - SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@5aaac705] was not registered for synchronization because synchronization is not active
DEBUG [http-bio-80-exec-4] - Fetching JDBC Connection from DataSource
DEBUG [http-bio-80-exec-4] - JDBC Connection [com.mysql.jdbc.JDBC4Connection@1f2de8a7] will not be managed by Spring
DEBUG [http-bio-80-exec-4] - ==>  Preparing: SELECT c.*, p. NAME parentName FROM sys_menus c LEFT JOIN sys_menus p ON c.parentId = p.id 
DEBUG [http-bio-80-exec-4] - ==> Parameters: 
DEBUG [http-bio-80-exec-4] - <==      Total: 21
DEBUG [http-bio-80-exec-4] - Closing non transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@5aaac705]
DEBUG [http-bio-80-exec-4] - Returning JDBC Connection to DataSource
DEBUG [http-bio-80-exec-4] - Written [com.jt.common.vo.JsonResult@5a5c74b0] as "application/json" using [org.springframework.http.converter.json.MappingJackson2HttpMessageConverter@6ede87a2]
DEBUG [http-bio-80-exec-4] - Null ModelAndView returned to DispatcherServlet with name 'dispatcherServlet': assuming HandlerAdapter completed request handling
DEBUG [http-bio-80-exec-4] - Successfully completed request
DEBUG [http-bio-80-exec-3] - DispatcherServlet with name 'dispatcherServlet' processing POST request for [/JT-SYS-V1.01/menu/doSaveObject.do]
DEBUG [http-bio-80-exec-3] - Looking up handler method for path /menu/doSaveObject.do
DEBUG [http-bio-80-exec-3] - Returning handler method [public com.jt.common.vo.JsonResult com.jt.sys.controller.SysMenuController.doSaveObject(com.jt.sys.entity.SysMenu)]
DEBUG [http-bio-80-exec-3] - Returning cached instance of singleton bean 'sysMenuController'
DEBUG [http-bio-80-exec-3] - Skip CORS processing: request is from same origin
DEBUG [http-bio-80-exec-3] - Resolving exception from handler [public com.jt.common.vo.JsonResult com.jt.sys.controller.SysMenuController.doSaveObject(com.jt.sys.entity.SysMenu)]: java.lang.IllegalArgumentException: 路径不能为空,
DEBUG [http-bio-80-exec-3] - Creating shared instance of singleton bean 'globalExceptionHandler'
DEBUG [http-bio-80-exec-3] - Creating instance of bean 'globalExceptionHandler'
DEBUG [http-bio-80-exec-3] - Eagerly caching bean 'globalExceptionHandler' to allow for resolving potential circular references
DEBUG [http-bio-80-exec-3] - Finished creating instance of bean 'globalExceptionHandler'
DEBUG [http-bio-80-exec-3] - Invoking @ExceptionHandler method: public com.jt.common.vo.JsonResult com.jt.common.exception.GlobalExceptionHandler.doHandlerRuntimeException(java.lang.RuntimeException)
DEBUG [http-bio-80-exec-3] - Written [com.jt.common.vo.JsonResult@78d50804] as "application/json" using [org.springframework.http.converter.json.MappingJackson2HttpMessageConverter@21c65893]
DEBUG [http-bio-80-exec-3] - Null ModelAndView returned to DispatcherServlet with name 'dispatcherServlet': assuming HandlerAdapter completed request handling
DEBUG [http-bio-80-exec-3] - Successfully completed request
```
### 7.菜单修改页面数据呈现
#### 7.1. 服务端实现
##### 7.1.1. Controller实现
- **业务描述**
> 基于客户端的添加请求返回一个响应页面（与添加操作共用一个页面）
- **业务实现**
> SysMenuController
