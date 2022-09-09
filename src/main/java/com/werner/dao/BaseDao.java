package com.werner.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class BaseDao {

	private static String driver;
	private static String url;
	private static String user;
	private static String password;
	
	static {
		
		InputStream in = BaseDao.class.getClassLoader().getResourceAsStream("db.properties");
		Properties props = new Properties();
		try {
			props.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		driver = props.getProperty("driver");
		url = props.getProperty("url");
		user = props.getProperty("user");
		password = props.getProperty("password");		
	}
	//建立連接
	public static Connection getConnection() {
		Connection connection = null;		
		try {
			Class.forName(driver);
			connection = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}
	
	public static ResultSet execute(Connection connection, PreparedStatement pstm, ResultSet rs,String sql,  Object[] params ) throws SQLException {
		
		pstm = connection.prepareStatement(sql);
		
		for(int i=0; i<params.length; i++) {
			//setObject，佔位符從1開始，但陣列是從0開始
			pstm.setObject(i+1, params[i]);
		}
		
		rs = pstm.executeQuery();
		return rs;
	}
	
	public static int execute(Connection connection,  PreparedStatement pstm,String sql, Object[] params) throws SQLException {
		pstm = connection.prepareStatement(sql);
		
		for(int i=0; i<params.length; i++) {
			//setObject，佔位符從1開始，但陣列是從0開始
			pstm.setObject(i+1, params[i]);
		} 
		int updateRow = pstm.executeUpdate();		
		return updateRow;		
	}
	
	
	public static boolean closeResources(Connection connection, PreparedStatement pstm, ResultSet rs) {
		boolean flag = true;
		
		if(connection != null) {
			try {
				connection.close();
				connection = null;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				flag = false;
			}
		}
		if(pstm != null) {
			try {
				pstm.close();
				pstm = null;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				flag = false;
			}
		}
		if(rs != null) {
			try {
				rs.close();
				rs = null;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				flag = false;
			}
		}
		return flag;
		
	}
}
