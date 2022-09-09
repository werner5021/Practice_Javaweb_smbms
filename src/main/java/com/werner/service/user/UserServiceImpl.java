package com.werner.service.user;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.werner.dao.BaseDao;
import com.werner.dao.user.UserDao;
import com.werner.dao.user.UserDaoImpl;
import com.werner.pojo.User;

public class UserServiceImpl implements UserService{

	private UserDao userDao;
	public UserServiceImpl() {
		userDao = new UserDaoImpl();
	}

	@Override
	public User login(String userCode, String userPassword) {
		System.out.println("進入 UserServiceImpl...");

		Connection connection = null;
		User user = null;

		try {
			connection = BaseDao.getConnection();
			user = userDao.getLoginUser(connection, userCode);
			//								
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			BaseDao.closeResources(connection, null, null);
		}
		if(null != user) {
			if(!userPassword.equals(user.getUserPassword())) {
				user =null;
			}
		}
		return user;
	}

	@Override
	public boolean updatePwd(int id, String pwd) {		
		boolean flag = true;

		Connection connection = BaseDao.getConnection();
		try {
			int updateRow = userDao.updatePwd(connection, id, pwd);
			if(updateRow<=0) {
				flag = false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			BaseDao.closeResources(connection, null, null);
		}
		return flag;
	}

	@Override
	public int getUserCount(String userName, int userRole) {
		Connection connection = null;
		int count = 0;		
		try {
			connection = BaseDao.getConnection();
			count = userDao.getUserCount(connection, userName, userRole);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			BaseDao.closeResources(connection, null, null);
		}

		return count;
	}

	@Override
	public List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize) {
		Connection connection =null;
		List<User> userList = null;
		System.out.println("queryUserName ---- > " + queryUserName);
		System.out.println("queryUserRole ---- > " + queryUserRole);
		System.out.println("currentPageNo ---- > " + currentPageNo);
		System.out.println("pageSize ---- > " + pageSize);
		try {
			connection = BaseDao.getConnection();
			userList = userDao.getUserList(connection, queryUserName, queryUserRole, currentPageNo, pageSize);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			BaseDao.closeResources(connection, null, null);
		}
		return userList;
	}

	@Override
	public boolean add(User user) {
		Connection connection = null;
		int updateRows = 0;
		boolean flag = false; 
		try {
			connection = BaseDao.getConnection();
			connection.setAutoCommit(false);
			updateRows = userDao.add(connection, user);
			connection.commit();
			if( updateRows>0) {
				flag = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}finally {
			BaseDao.closeResources(connection, null, null);
		}				
		return flag;
	}

	@Override
	public boolean deleteUserById(int id) {
		Connection connection = null;
		boolean flag = false; 
		int deleteId = 0;
		try {
			connection = BaseDao.getConnection();
			deleteId = userDao.deleteById(connection, id);
			if(deleteId!= 0) {
				flag = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			BaseDao.closeResources(connection, null, null);
		}		
		return flag;
	}

	@Override
	public boolean modify(User user) {		
		Connection connection = null;
		boolean flag = false; 
		int execute = 0;		
		
		try {
			connection = BaseDao.getConnection();
			execute = userDao.modify(connection, user);
			if(execute!= 0) {
				flag = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			BaseDao.closeResources(connection, null, null);
		}		
		
		return flag;
	}

	@Override
	public User getUserById(String id) {
		Connection connection = null;		
		User user = null;
		try {
			connection = BaseDao.getConnection();			
			user = userDao.getUserById(connection, id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			BaseDao.closeResources(connection, null, null);
		}		
		return user;
	}

	public User selectUserCodeExist(String userCode) {
		Connection connection = null;
		User user = null;
		
		connection = BaseDao.getConnection();
		try {
			user = userDao.getLoginUser(connection, userCode);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
            BaseDao.closeResources(connection, null, null);
        }
        return user;
		
		
		
	}
	
	
	
	
	
	
}
