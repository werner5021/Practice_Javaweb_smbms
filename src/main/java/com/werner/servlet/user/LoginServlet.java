package com.werner.servlet.user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.werner.pojo.User;
import com.werner.service.user.UserService;
import com.werner.service.user.UserServiceImpl;
import com.werner.util.Constants;

public class LoginServlet extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			
		System.out.println("進入 LoginServlet...");
		
		String userCode = req.getParameter("userCode");
		String userPassword = req.getParameter("userPassword");
		
		UserService userService = new UserServiceImpl();
		User user = userService.login(userCode, userPassword);
		
		if(user != null) {
			req.getSession().setAttribute(Constants.USER_SESSION, user);
			resp.sendRedirect(req.getContextPath()+"/jsp/frame.jsp");
		}else {
			req.setAttribute("error", "使用者名稱或密碼錯誤");
			req.getRequestDispatcher("/login.jsp").forward(req, resp);
		}		
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
	
	
	
	
	
	
	
}
