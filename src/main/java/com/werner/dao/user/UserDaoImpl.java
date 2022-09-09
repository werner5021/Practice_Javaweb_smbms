package com.werner.dao.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.StringUtils;
import com.werner.dao.BaseDao;
import com.werner.pojo.User;

public class UserDaoImpl implements UserDao{

	//得到要登入的用戶 (數據庫連接對象、userCode)
	public User getLoginUser(Connection connection, String userCode) throws SQLException {
		System.out.println("進入 UserDaoImpl...");

		PreparedStatement pstm = null;
		ResultSet rs = null;
		User user =null;		

		//判斷數據庫連接是否成功
		if(connection != null) {
			//使用SQL語句查出登入的用戶
			String sql = "select * from smbms_user where userCode=?";
			Object[] params = {userCode};  //參數要封裝

			//獲得User的結果集
			rs = BaseDao.execute(connection, pstm, rs, sql, params );
			//使用結果集歷遍
			if(rs.next()) {
				user = new User(); //查出來的登入的用戶數據，封裝到用戶user裡面
				user.setId(rs.getInt("id"));
				user.setUserCode(rs.getString("userCode"));
				user.setUserName(rs.getString("userName"));
				user.setUserPassword(rs.getString("userPassword"));
				user.setGender(rs.getInt("gender"));
				user.setBirthday(rs.getDate("birthday"));
				user.setPhone(rs.getString("phone"));
				user.setAddress(rs.getString("address"));
				user.setUserRole(rs.getInt("userRole"));
				user.setCreatedBy(rs.getInt("createdBy"));
				user.setCreationDate(rs.getTimestamp("creationDate"));
				user.setModifyBy(rs.getInt("modifyBy"));
				user.setModifyDate(rs.getTimestamp("modifyDate"));
			}
			//關閉數據
			BaseDao.closeResources(null, pstm, rs);  //連接到先不關，到事務的時候再處理
		}		
		return user;
	}

	//修改當前用戶密碼
	public int updatePwd(Connection connection, int id, String password) throws SQLException{

		System.out.println("UserDaoImpl 新密碼為:" + password);

		PreparedStatement pstm = null;  //執行的對象，用來執行預編譯
		int execute = 0;

		if(connection != null) {			
			String sql="update smbms_user set userPassword=? where id=?";
			Object[] params = {password, id};  //把參數值接封裝成陣列丟入
			execute = BaseDao.execute(connection, pstm, sql, params);

			BaseDao.closeResources(null, pstm, null);
		}		
		return execute;
	}

	@Override
	public int getUserCount(Connection connection, String userName, int userRole) throws SQLException {
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int count = 0;

		if(connection != null) {
			StringBuffer sql = new StringBuffer();
			List<Object> list = new ArrayList<Object>();

			sql.append("select count(1) as count from smbms_user u, smbms_role r where u.userRole=r.id");
			if(!StringUtils.isNullOrEmpty(userName)) {
				sql.append(" and u.userName like ?");
				list.add("%"+userName+"%");
			}
			if(userRole>0) {
				sql.append(" and u.userRole=?");
				list.add(userRole);
			}
			Object[] params = list.toArray();			

			rs = BaseDao.execute(connection, pstm, rs, sql.toString(), params);
			if(rs.next()) {
				count = rs.getInt("count");
			}

			BaseDao.closeResources(null, pstm, rs);
		}

		return count; 		
	}

	@Override
	public List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize)
			throws SQLException {
		PreparedStatement pstm = null;
		ResultSet rs = null;
		List<User> userList= new ArrayList<User>();
		if(connection != null) {
			StringBuffer sql = new StringBuffer();
			sql.append("select u.*, r.roleName as userRoleName from smbms_user u, smbms_role r where u.userRole = r.id");
			List<Object> list = new ArrayList<Object>();
			if(!StringUtils.isNullOrEmpty(userName)) {
				sql.append(" and u.userName like ?");
				list.add("%"+ userName +"%");
			}
			if(userRole>0) {
				sql.append(" and u.userRole=?");
				list.add(userRole);
			}
			sql.append(" order by creationDate DESC limit ?,?");
			currentPageNo = (currentPageNo-1)*pageSize;
			list.add(currentPageNo);
			list.add(pageSize);

			Object[] params = list.toArray();
			System.out.println("sql ----> " + sql.toString());
			rs= BaseDao.execute(connection, pstm, rs, sql.toString(), params);			
			while(rs.next()) {
				User _user = new User();
				_user.setId(rs.getInt("id"));
				_user.setUserCode(rs.getString("userCode"));
				_user.setUserName(rs.getString("userName"));
				_user.setGender(rs.getInt("gender"));
				_user.setBirthday(rs.getDate("birthday"));
				_user.setPhone(rs.getString("phone"));
				_user.setUserRole(rs.getInt("userRole"));
				_user.setUserRoleName(rs.getString("userRoleName"));
				userList.add(_user);
			}
			BaseDao.closeResources(null, pstm, rs);
		}
		return userList; 
	}

	@Override
	public int add(Connection connection, User user) throws SQLException {
		PreparedStatement pstm = null;
		int updateRows = 0;
						
		if(connection != null) {
			String sql = "insert into smbms_user (userCode, userName, userPassword, userRole, gender, birthday, phone, address, creationDate, createdBy) values (?,?,?,?,?,?,?,?,?,?)";
			Object[] params = {
					user.getUserCode(),
					user.getUserName(),
					user.getUserPassword(),
					user.getUserRole(),
					user.getGender(),
					user.getBirthday(),
					user.getPhone(),
					user.getAddress(),
					user.getCreationDate(),
					user.getCreatedBy()
					};			
			updateRows = BaseDao.execute(connection, pstm, sql.toString(), params);				
		}
		return updateRows;		
	}

	@Override
	public int deleteById(Connection connection, Integer delId) throws SQLException {
		PreparedStatement pstm = null;
		int updateRows = 0;		
		if(connection != null) {
			String sql = "delete from smbms_user where id=?";
			Object[] params = {delId};
			updateRows = BaseDao.execute(connection, pstm, sql, params);
			BaseDao.closeResources(null, pstm, null);
		}
		return updateRows;
	}

	@Override
	public int modify(Connection connection, User user) throws SQLException {
		PreparedStatement pstm = null;
		int updateRows = 0;	
		if(connection!=null) {
			String sql = "update smbms_user set userCode=?, "
					+ "userName=?, userPassword=?, userRole=?, "
					+ "gender=?, birthday=?, phone=?, address=?, modifyDate=?, modifyBy=? where id=?";
			Object[] params = {
					user.getUserCode(),
					user.getUserName(),
					user.getUserPassword(),
					user.getUserRole(),
					user.getGender(),
					user.getBirthday(),
					user.getPhone(),
					user.getAddress(),
					user.getModifyDate(),
					user.getModifyBy(),
					user.getId()
			};
			updateRows = BaseDao.execute(connection, pstm, sql, params);		
			BaseDao.closeResources(null, pstm, null);
		}
		return updateRows;
	}		

	@Override
	public User getUserById(Connection connection, String id) throws SQLException {
		PreparedStatement pstm = null;
		ResultSet rs = null;
		User user = null;
		if(connection != null) {
			String sql = "select * from smbms_user where id=?";
			Object[] params = {id};
			rs = BaseDao.execute(connection, pstm, rs, sql, params);
			if(rs.next()) {
				user = new User();
                user.setId(rs.getInt("id"));
                user.setUserCode(rs.getString("userCode"));
                user.setUserName(rs.getString("userName"));
                user.setUserPassword(rs.getString("userPassword"));
                user.setGender(rs.getInt("gender"));
                user.setBirthday(rs.getDate("birthday"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setUserRole(rs.getInt("userRole"));
                user.setCreatedBy(rs.getInt("createdBy"));
                user.setCreationDate(rs.getTimestamp("creationDate"));
                user.setModifyBy(rs.getInt("modifyBy"));
                user.setModifyDate(rs.getTimestamp("modifyDate"));
                user.setUserRoleName(rs.getString("userRoleName"));
                }
			BaseDao.closeResources(null, pstm, rs);
		}	
		return user;
	}
	
}
