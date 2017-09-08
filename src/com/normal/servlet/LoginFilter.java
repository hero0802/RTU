package com.normal.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.normal.action.User;
import org.normal.function.Func;

public class LoginFilter implements Filter {
	private Func fun = new Func();
	private String xmlPath = LoginFilter.class.getResource("/config.xml")
			.getPath();

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// 获得在下面代码中要用的request,response,session对象
		HttpServletRequest servletRequest = (HttpServletRequest) request;
		HttpServletResponse servletResponse = (HttpServletResponse) response;
		HttpSession session = servletRequest.getSession();

		// 获得用户请求的URI
		String path = servletRequest.getRequestURI();
		// 从session里取员工工号信息
		String session_prefix = fun.readXml("Other", "session_prefix");
		String username = (String) session.getAttribute(session_prefix
				+ "username");
		//System.out.println("session: username=" + username);

		// 登陆页面无需过滤
		if (path.indexOf("/login.html") > -1) {
			chain.doFilter(servletRequest, servletResponse);
			return;
		} else if (path.indexOf("/register.html") > -1) {
			chain.doFilter(servletRequest, servletResponse);
			return;
		} else if (path.indexOf("controller") > -1) {
			chain.doFilter(servletRequest, servletResponse);
			return;
		} else if (path.indexOf("resources") > -1) {
			chain.doFilter(servletRequest, servletResponse);
			return;
		} else {
		}

		// 判断如果没有取到登陆信息,就跳转到登陆页面

		if (username == null || "".equals(username)) {
			// 跳转到登陆页面
			servletResponse.sendRedirect(servletRequest.getContextPath()+"/view/login.html");

		} else {
			// 已经登陆,继续此次请求
			chain.doFilter(request, response);
		}

	}

}
