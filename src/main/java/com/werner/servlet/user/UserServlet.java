package com.werner.servlet.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;
import com.werner.pojo.Role;
import com.werner.pojo.User;
import com.werner.service.role.RoleServiceImpl;
import com.werner.service.user.UserService;
import com.werner.service.user.UserServiceImpl;
import com.werner.util.Constants;
import com.werner.util.PageSupport;

public class UserServlet extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String method = req.getParameter("method");
		if(method.equals("savepwd") && method != null) {
			this.updatePwd(req, resp);
		}else if(method.equals("pwdmodify")) {
			this.pwdModify(req, resp);
		}else if(method.equals("query")) {
			this.query(req, resp);
		}else if(method.equals("add")) {  //見useradd.jsp 18行
			this.add(req, resp);	
		}else if(method.equals("getrolelist")) {
			this.getRoleList(req, resp);
		}else if(method.equals("ucexist")) {
			this.ucexist(req, resp);
		}else if(method.equals("deluser")) {
			this.delUser(req, resp);
		}else if(method.equals("view")) {
			this.view(req, resp);
		}else if(method.equals("modify")) {
			this.modify(req, resp);
		}
	}


	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

	public void updatePwd(HttpServletRequest req, HttpServletResponse resp) {

		System.out.println("進入 UserServlet的updatePwd");		


		Object userObj = req.getSession().getAttribute(Constants.USER_SESSION);
		String newpassword = req.getParameter("newpassword");
		boolean flag = false;

		if(userObj != null && newpassword != null) {
			UserService userService = new UserServiceImpl();
			flag = userService.updatePwd(((User) userObj).getId(), newpassword);

			if(flag) {
				req.setAttribute("message", "密碼修改成功");
				req.getSession().removeAttribute(Constants.USER_SESSION);
			}else {
				req.setAttribute("message", "密碼修改失敗");
			}
		}else {
			req.setAttribute("message", "新密碼密碼有問題");
		}
		try {
			req.getRequestDispatcher("/jsp/pwdmodify.jsp").forward(req, resp);
		} catch (ServletException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void pwdModify(HttpServletRequest req, HttpServletResponse resp) {

		System.out.println("進入 UserServlet的pwdModify");		

		Object userObj = req.getSession().getAttribute(Constants.USER_SESSION);
		String oldpassword = req.getParameter("oldpassword");
		Map<String,String> resultMap = new HashMap<String, String>(); 

		if(userObj ==null) {
			resultMap.put("result", "sessionerror");
		}else if(StringUtils.isNullOrEmpty(oldpassword)) {
			resultMap.put("result", "error");
		}else {
			String userPassword = ((User) userObj).getUserPassword();
			if(oldpassword.equals(userPassword)) {
				resultMap.put("result", "true");
			}else {
				resultMap.put("result", "false");
			}			
		}		
		try {
			resp.setContentType("application/json");
			PrintWriter writer = resp.getWriter();
			writer.write(JSONArray.toJSONString(resultMap));
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	public void query(HttpServletRequest req, HttpServletResponse resp) {

		String queryUserName = req.getParameter("queryname");
		String roleStr = req.getParameter("queryUserRole");
		String pageIndex = req.getParameter("pageIndex");
		int queryUserRole = 0;
		int currentPageNo = 1;
		int pageSize = 5;
		List<User> userList = null;
		List<Role> roleList = null;

		if(queryUserName==null) {
			queryUserName = "";
		}
		if(roleStr!=null && !roleStr.equals("")) {
			queryUserRole = Integer.parseInt(roleStr);
		}
		if(pageIndex!=null && !pageIndex.equals("")) {
			currentPageNo = Integer.parseInt(pageIndex);
		}		

		UserServiceImpl userService = new UserServiceImpl();
		int totalCount = userService.getUserCount(queryUserName, queryUserRole);

		PageSupport pageSupport = new PageSupport();
		pageSupport.setCurrentPageNo(currentPageNo);
		pageSupport.setPageSize(pageSize);
		pageSupport.setTotalCount(totalCount);
		pageSupport.setTotalPageCountByRs();
		int totalPageCount = pageSupport.getTotalPageCount();

		if(totalPageCount<1) {
			totalPageCount= 1;
		}else if(currentPageNo > totalPageCount) {
			currentPageNo = totalPageCount;
		}


		userList = userService.getUserList(queryUserName, queryUserRole, currentPageNo, pageSize);
		req.setAttribute("userList", userList);

		RoleServiceImpl roleService = new RoleServiceImpl();
		roleList = roleService.getRoleList();
		req.setAttribute("roleList", roleList);

		req.setAttribute("totalPageCount", totalPageCount);
		req.setAttribute("totalCount", totalCount);
		req.setAttribute("currentPageNo", currentPageNo);
		req.setAttribute("totalPageCount", totalPageCount);
		req.setAttribute("queryUserName", queryUserName);
		req.setAttribute("queryUserRole", queryUserRole);		

		try {
			req.getRequestDispatcher("userlist.jsp").forward(req, resp);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void add(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		String userCode = req.getParameter("userCode");
		String userName = req.getParameter("userName");
		String userPassword = req.getParameter("userPassword");
		String gender_str = req.getParameter("gender");
		String birthday_str = req.getParameter("birthday");
		String phone = req.getParameter("phone");
		String address = req.getParameter("address");
		String userRole_str = req.getParameter("userRole");
		User user = null;

		int gender = 0;
		Date birthday = new Date();
		int userRole = 0;

		if(gender_str != null && !StringUtils.isNullOrEmpty(gender_str)) {
			gender = Integer.parseInt(gender_str);
		}
		try {
			if(birthday_str != null && !StringUtils.isNullOrEmpty(birthday_str)) {
			}
			birthday = new SimpleDateFormat("yyyy-MM-dd").parse(birthday_str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(userRole_str != null && !StringUtils.isNullOrEmpty(userRole_str)) {
			userRole = Integer.parseInt(userRole_str);
		}

		user.setUserCode(userCode);
		user.setUserName(userName);
		user.setUserPassword(userPassword);
		user.setGender(gender);
		user.setBirthday(birthday);
		user.setPhone(phone);
		user.setAddress(address);
		user.setUserRole(userRole);
		user.setCreationDate(new Date());
		user.setCreatedBy(((User)req.getSession().getAttribute(Constants.USER_SESSION)).getId());

		UserServiceImpl userService = new UserServiceImpl();
		boolean flag = userService.add(user);

		if(flag) {
			resp.sendRedirect(req.getContextPath() + "/jsp/user.do?method=query");
		}else {
			req.getRequestDispatcher("useradd.jsp").forward(req, resp);
		}
	}

	public void getRoleList(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		System.out.println("進入 UserServlet的getRoleList");		

		RoleServiceImpl roleService = new RoleServiceImpl();
		List<Role> roleList = roleService.getRoleList();

		resp.setContentType("application/json");
		PrintWriter writer = resp.getWriter();
		writer.write(JSONArray.toJSONString(roleList));
		writer.flush();
		writer.close();
	}

	public void ucexist(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		String userCode = req.getParameter("userCode");

		Map<String , String> resultMap = new HashMap<String,String>();

		if(userCode==null || userCode.equals("")) {
			resultMap.put("userCode", "exist");
		}else {
			UserServiceImpl userService = new UserServiceImpl();
			User user = userService.selectUserCodeExist(userCode);
			if(user != null) {
				resultMap.put("userCode", "notexist");
			}else {
				resultMap.put("userCode", "exist");
			}
		}
		//把resultMap转为json字符串以json的形式输出
        //配置上下文的输出类型
        resp.setContentType("application/json");
        //从response对象中获取往外输出的writer对象
        PrintWriter outPrintWriter = resp.getWriter();
        //把resultMap转为json字符串 输出
        outPrintWriter.write(JSONArray.toJSONString(resultMap));
        outPrintWriter.flush();//刷新
        outPrintWriter.close();//关闭流

	}
	public void delUser(HttpServletRequest req, HttpServletResponse resp) {

		String idStr = req.getParameter("uid");		
		int delId = 0;
		if(idStr!=null && idStr!="") {
			delId = Integer.parseInt(idStr);
		}
		Map<String ,String> resulMap = new HashMap<String,String>();
		if(delId<=0) {
			resulMap.put("delResult", "notexist");
		}else {
			UserServiceImpl userService = new UserServiceImpl();
			boolean flag = userService.deleteUserById(delId);
			if(flag) {
				resulMap.put("delResult", "true");
			}else {
				resulMap.put("delResult", "false");
			}			
		}		
	}

	public void view(HttpServletRequest req, HttpServletResponse resp) throws IOException{

		String id = req.getParameter("uid");	
		if(id != null && !id.equals("")) {
			UserServiceImpl userService = new UserServiceImpl();
			User user = userService.getUserById(id);
			req.setAttribute("user", user);
			resp.sendRedirect("userview.jsp");
		}
	}

	public void modify(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException{

		User user = new User();
		String id = req.getParameter("uid");		
		String userName = req.getParameter("userName");
		String gender_str = req.getParameter("gender");
		String birthday_str = req.getParameter("birthday");
		String phone = req.getParameter("phone");
		String address = req.getParameter("address");
		String userRole_str = req.getParameter("userRole");

		user.setId(Integer.valueOf(id));
		user.setUserName(userName);
		user.setGender(Integer.valueOf(gender_str));
		try {
			user.setBirthday(new SimpleDateFormat("yyyy-mm-dd").parse(birthday_str));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		user.setPhone(phone);
		user.setAddress(address);
		user.setUserRole(Integer.parseInt(userRole_str));
		
		UserServiceImpl userService = new UserServiceImpl();
		boolean flag = userService.modify(user);
		if(flag) {
			resp.sendRedirect(req.getContextPath()+"jsp/user.do?method=query");
		}else {
			req.getRequestDispatcher("usermodify.jsp").forward(req, resp);
		}
		

	}


}
