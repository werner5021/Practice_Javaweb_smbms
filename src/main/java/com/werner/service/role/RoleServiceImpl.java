package com.werner.service.role;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.werner.dao.BaseDao;
import com.werner.dao.role.RoleDao;
import com.werner.dao.role.RoleDaoImpl;
import com.werner.pojo.Role;

public class RoleServiceImpl implements RoleService{

	//引入Dao (私有方法、公有方法)
	private RoleDao roleDao;
	public RoleServiceImpl() {
		roleDao = new RoleDaoImpl();
	}
	
	@Override
	public List<Role> getRoleList() {
		
		Connection connection = null;
		List<Role> roleList = new ArrayList();
		
		try {
			connection = BaseDao.getConnection();			
			roleList = roleDao.getRoleList(connection);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			BaseDao.closeResources(connection, null, null);
		}
		return roleList;
	}
	
	@Test
	public void test() {
		RoleService roleService = new RoleServiceImpl();
		List<Role> roleList = roleService.getRoleList();		
		for(Role role:roleList) {
			System.out.println(role.getRoleName());
		}		
	}
	
}
