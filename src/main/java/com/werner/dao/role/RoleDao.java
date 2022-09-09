package com.werner.dao.role;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.werner.pojo.Role;

public interface RoleDao {
	
	public List<Role> getRoleList(Connection connection) throws SQLException;
}
