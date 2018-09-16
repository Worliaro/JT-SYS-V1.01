package com.jt.sys.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.jt.common.exception.ServiceException;
import com.jt.common.vo.Node;
import com.jt.sys.dao.SysDeptDao;
import com.jt.sys.dao.SysUserDao;
import com.jt.sys.entity.SysDept;
import com.jt.sys.service.SysDeptService;
@Service
public class SysDeptServiceImpl implements SysDeptService {

	@Autowired
	private SysDeptDao sysDeptDao;
	
	@Autowired
	private SysUserDao sysUserDao;
	
	@Override
	public List<Map<String, Object>> findObjects() {
		return sysDeptDao.findObjects();
	}

	@Override
	public int deleteObject(Integer id) {
		// 1.合法性验证
		if(id==null || id<=0)
			throw new ServiceException("数据不合法，id="+id);
		//2.执行删除操作
		//判断id对应得菜单是否有子元素
		int childCount = sysDeptDao.getChildCount(id);
		if(childCount>0)
			throw new ServiceException("该元素有子元素,不允许删除");
		//判断该部门是否有用户
		int userCount = sysUserDao.getUserCountByDeptId(id);
		if(userCount>0)
			throw new ServiceException("该部门有员工,不允许对部门进行删除");
		//判定此部门是否已经被用户使用,假如有则拒绝删除
		//删除部门
		int rows = sysDeptDao.deleteObject(id);
		if(rows ==0)
			throw new ServiceException("此信息可能已经不存在");
		return rows;
	}

	@Override
	public List<Node> findZtreeDeptNodes() {
		return sysDeptDao.findZtreeDeptNodes();
	}

	@Override
	public int saveObject(SysDept entity) {
		// 1.合法验证
		if(entity == null)
			throw new ServiceException("保存对象不能为空");
		if(StringUtils.isEmpty(entity.getName()))
			throw new ServiceException("部门名不能为空");
		int rows;
		// 2.保存数据
		try {
			rows = sysDeptDao.insertObject(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("保存失败");
		}
		// 3.返回数据
		return rows;
	}

	@Override
	public int updateObject(SysDept entity) {
		// 1.合法验证
		if(entity== null)
			throw new ServiceException("修改对象不能为空");
		if(StringUtils.isEmpty(entity.getName()))
			throw new ServiceException("部门名不能为空");
		//...
		// 2.更新数据
		int rows = sysDeptDao.updateObject(entity);
		if(rows==0)
			throw new ServiceException("记录可能已经不存在了");
		// 3.返回数据
		return rows;
	}

}
