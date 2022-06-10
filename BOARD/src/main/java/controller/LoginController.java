package controller;


import static common.CommonResource.viewPath;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.AppContext;
import common.Autowired;
import domain.User;
import service.LoginService;

@WebServlet("/login/*")
public class LoginController extends HttpServlet {
	@Autowired
	LoginService loginService;
	
	@Override
	public void init() throws ServletException {
		AppContext app = (AppContext)getServletContext().getAttribute("appContext");
		try {
			app.servletAutowired(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String doUrl = request.getRequestURI().replace(request.getContextPath(), "");	
		String responseUrl = "";	
		
		if (doUrl.equals("/login/login")) { //로그인폼
			responseUrl = viewPath +  "loginForm.jsp";	
			if (request.getParameter("msg") != null)
				responseUrl += "?msg=" + request.getParameter("msg"); 
			if (request.getParameter("toURL") != null)
				responseUrl += (responseUrl.lastIndexOf("?") >= 0 ? "&" : "?") + "toURL=" + request.getParameter("toURL"); 
		}else if (doUrl.equals("/login/logout.do")) {//로그아웃
			request.getSession().invalidate();
			response.sendRedirect(request.getContextPath());
			return;
		}else {
			responseUrl = "/?msg=" + URLEncoder.encode("요청을 찾을 수 없습니다.", "utf-8");
		}
		request.getRequestDispatcher(responseUrl).forward(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String doUrl = request.getRequestURI().replace(request.getContextPath(), "");	
		String responseUrl = "";	
	
		if (doUrl.equals("/login/login")) { //로그인
			try {
				if (request.getParameter("id") == null || request.getParameter("pwd") == null) {
					response.sendRedirect(request.getContextPath() + "?msg=" + URLEncoder.encode("id, pwd를 입력해주세요", "utf-8"));
					return;
				}
									
				User user = null;
				try {
					user = loginService.login((String)request.getParameter("id"), (String)request.getParameter("pwd"));
				}catch (SQLException e) {
					response.setStatus(500);
					Exception ex = new Exception("데이터베이스 조회중 오류가 발생했습니다.");
					ex.initCause(e);
					throw ex;
				}

				if (user == null) {
					responseUrl = "/login/login?msg=" + URLEncoder.encode("아이디 또는 비밀번호가 일치하지 않습니다.", "UTF-8");
					if (request.getParameter("toURL") != null) 
						responseUrl += "&toURL=" + request.getParameter("toURL"); 				 

					response.sendRedirect(responseUrl);
					return;
				}
				
				request.getSession().setAttribute("id", user.getId());
			       			
				Cookie cookie = new Cookie("id", user.getId());
					
				if (request.getParameter("rememberId") == null ||
				   !((String)request.getParameter("rememberId")).equals("true")) 
					cookie.setMaxAge(0);
					
				response.addCookie(cookie);
				
				if (request.getParameter("toURL") != null) 
					response.sendRedirect(request.getContextPath() + request.getParameter("toURL"));			 
				else 
					response.sendRedirect(request.getContextPath()); 
				
				return;		
			} catch (Exception e) {
				e.printStackTrace();
				response.sendRedirect(request.getContextPath() + "/login/login?msg=" + URLEncoder.encode(e.getMessage(), "UTF-8"));	
				return;
			}		
		}else {
			responseUrl = "/?msg=" + URLEncoder.encode("요청을 찾을 수 없습니다.", "utf-8");
		}
		request.getRequestDispatcher(responseUrl).forward(request, response);
	}

	@Override
	public String toString() {
		return "LoginController [loginService=" + loginService + "]";
	}
	
	
}
