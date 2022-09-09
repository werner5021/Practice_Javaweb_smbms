package com.werner.service.user;

import java.util.List;

import com.werner.pojo.User;

public interface UserService {
	public User login(String userCode, String userPassword);
	
	public boolean updatePwd(int id, String pwd);
	
	public int getUserCount(String userName, int userRole);
	
	public List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize);
	
	public boolean add(User user);
	
	public boolean deleteUserById(int id);
	
	public boolean modify(User user);
	
	public User getUserById(String id);
	
}
