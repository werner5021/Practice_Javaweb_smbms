package com.werner.dao.user;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.werner.pojo.User;

public interface UserDao {
	
	public User getLoginUser(Connection connection, String userCode) throws SQLException;
		
	public int updatePwd(Connection connection, int id, String password) throws SQLException;
	
	public int getUserCount(Connection connection, String userName, int userRole) throws SQLException;
	
	//通過條件查詢用戶 userList
	public List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize) throws SQLException;
	//新增
	public int add(Connection connection, User user) throws SQLException;
	//刪除
	public int deleteById(Connection connection, Integer delId) throws SQLException;
	//修改
	public int modify(Connection connection, User user) throws SQLException;
	
	public User getUserById(Connection connection, String id) throws SQLException;
}
