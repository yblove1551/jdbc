package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.AppContext;
import common.Autowired;
import common.CommonResource;
import domain.User;
import service.UserService;

@WebServlet("/user/*")
public class UserController extends HttpServlet {
	@Autowired
	UserService userService;
	
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
		PrintWriter out = response.getWriter();
		
		if (doUrl.equals("/user/id") || doUrl.equals("/user/email")) {
			try {
				User user = doUrl.equals("/user/id") ? 
						userService.selectUser(request.getParameter("id")) : 
						userService.selectUserByEmail(request.getParameter("email"));

				if (user == null) 
					out.write("{\"result\":\"OK\"}");
				else 
					out.write("{\"result\":\"NO\"}");		
			
			} catch (Exception e) {
				response.setStatus(500);
				out.write("{\"result\":\"ERROR\"}");
			}
			return;
		}else if (doUrl.equals("/user/checkEmail")){
			User user = null;
			String email = request.getParameter("email");
			String name = request.getParameter("name");	
			try {
				user = userService.selectByEmailAndName(email, name);
				if (user != null) 
					out.write("{\"result\":\"OK\", \"id\":\""+user.getId()+"\"}");
				else 
					out.write("{\"result\":\"NO\"}");
			} catch (Exception e) {
				out.write("{\"result\":\"ERROR\"}");
				response.setStatus(500);
			}
			return;
		}else if (doUrl.equals("/user/modify")) { //수정화면
			try {		
				if (request.getSession().getAttribute("id") == null) { //로그인이 안된 경우
					response.sendRedirect(request.getContextPath() + "/login/login"); //로그인폼으로 리다이렉트
					return;
				}	
				
				User user = null;
				try {
					user = userService.selectUser((String)request.getSession().getAttribute("id")); //세션에 있는 ID로 디비조회	
				} catch (SQLException e) {
					Exception ex = new Exception("데이터베이스 조회 중 오류가 발생했습니다.");
					ex.initCause(e);
					response.setStatus(500);
					throw ex;
				}

				if (user == null) {
					response.setStatus(400);
					throw new Exception("이미 탈퇴한 회원입니다.");			
				}
				
				request.setAttribute("user", user);

				responseUrl = CommonResource.viewPath + "userForm.jsp";
				
			}catch(Exception e) {
				e.printStackTrace();
				responseUrl = "/?msg=" + e.getMessage();
			}	
		}else if (doUrl.equals("/user/add")) { //등록화면
			responseUrl = CommonResource.viewPath + "userForm.jsp";
		}else if (doUrl.equals("/user/newpw")){
			String id = request.getParameter("id");
			request.setAttribute("id", id);
			responseUrl = CommonResource.viewPath + "newpw.jsp";	
		}else if (doUrl.equals("/user/find")){ //회원찾기 폼
			responseUrl = CommonResource.viewPath + "findUser.jsp";			
		}else {
			response.setStatus(404);
			responseUrl = "/?msg=" + URLEncoder.encode("요청을 찾을 수 없습니다.", "UTF-8");
		}
		request.getRequestDispatcher(responseUrl).forward(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String doUrl = request.getRequestURI().replace(request.getContextPath(), "");	
		String responseUrl = "";	
		PrintWriter out = response.getWriter();
		
		if (doUrl.equals("/user/remove")) { //삭제
			try {
				if (request.getParameter("id") == null 
						||request.getSession().getAttribute("id") == null
						||!request.getParameter("id").equals((String)request.getSession().getAttribute("id"))
					){
					response.setStatus(400);
					responseUrl = "/?msg=" +  URLEncoder.encode("잘못된 요청입니다.", "utf-8");
				}

				
				try {
					if (userService.removeUser(request.getParameter("id")) >= 0) {
						response.setStatus(400);	
						throw new Exception("이미 삭제된 회원입니다.");								
					}					
				} catch (SQLException e) {
					response.setStatus(500);
					Exception ex = new Exception("데이터베이스 오류가 발생했습니다");
					ex.initCause(e);
					throw ex;
				}
						
				request.getSession().invalidate();					
				response.sendRedirect(request.getContextPath() + "?msg=" + URLEncoder.encode("회원탈퇴에 성공하셨습니다.", "utf-8"));
				return;						
			} catch (Exception e) {
				e.printStackTrace();
				response.sendRedirect(request.getContextPath() + "?msg=" + URLEncoder.encode(e.getMessage(), "utf-8"));
			}
		}else if (doUrl.equals("/user/modify")) { //수정
			User user = null;
			try {		
				try {
					user = (User) CommonResource.dataBind(request.getParameterMap(), Class.forName("domain.User"));	
				}catch(Exception e) {
					response.setStatus(500);
					throw new Exception("데이터 변환중 오류가 발생했습니다.");
				}
				
				String msg = userValidation(user);

				if (!msg.equals("")) {
					response.setStatus(400);
					throw new Exception(msg);
				}
	
				try {
					if (userService.modifyUser(user) <= 0) {
						response.setStatus(400);
						throw new Exception("사용자를 찾을 수 없습니다");		
					}
				} catch (SQLException e) {
					response.setStatus(500);
					Exception ex = new Exception("데이터베이스 오류가 발생했습니다");
					ex.initCause(e);
					throw ex;
				}
	
				response.sendRedirect(request.getContextPath() + "?msg=" + URLEncoder.encode("회원정보가 수정되었습니다.", "utf-8"));
				
				return;
				
			}catch(Exception e) {
				e.printStackTrace();		
				response.sendRedirect(request.getContextPath() + "?msg" + URLEncoder.encode(e.getMessage(), "utf-8"));
				return;	
			}
		}
		else if (doUrl.equals("/user/add")) { //추가
			try {
				
				User user = null;
				try {
					user = (User)CommonResource.dataBind(request.getParameterMap(), Class.forName("domain.User"));
				}catch(Exception e) {
					response.setStatus(500);
					throw new Exception("데이터 변환중 오류가 발생했습니다.");
				}
						
				String msg = userValidation(user);
				
				if (!msg.equals("")) {
					response.setStatus(400);
					throw new Exception(msg);
				}
				
				try {
					if (userService.addUser(user) <= 0) {
						response.setStatus(400);
						throw new Exception("회원 추가중 에러가 발생했습니다");		
					}
				} catch (SQLException e) {
					response.setStatus(500);
					Exception ex = new Exception("데이터베이스 오류가 발생했습니다");
					ex.initCause(e);
					throw ex;
				} 
			
				request.getSession().setAttribute("id", user.getId());
				response.sendRedirect(request.getContextPath() + "?msg=" + URLEncoder.encode("회원가입에 성공하셨습니다.", "utf-8"));
				return;						
			}catch(Exception e) {
				e.printStackTrace();
				response.sendRedirect(request.getContextPath() + "?msg=" + URLEncoder.encode(e.getMessage(), "utf-8"));	
			}		
		}else if(doUrl.equals("/user/newpw")) {
			try {
				String id = request.getParameter("id");
				String pw = request.getParameter("pw");				
		
				if (userService.updatePw(id, pw) >= 1) 
					out.write("\"result\":\"OK\"");
				else
					out.write("\"result\":\"NO\"");
			} catch (Exception e) {
				out.write("\"result\":\"ERROR\"");
			}
		}else {
			responseUrl = "/?msg=" + URLEncoder.encode("요청을 찾을 수 없습니다.", "UTF-8");
			response.setStatus(404);
		}
		request.getRequestDispatcher(responseUrl).forward(request, response);
	}
	
	public String userValidation(User user) {
		String msg = "";
		
		if (CommonResource.isEmptyString(user.getId())) 
			msg += (msg.equals("") ? "" : ",") + "아이디는 필수값입니다.";
		else
		if (user.getId().trim().length() > 30 || user.getId().trim().length() < 5)
			msg += (msg.equals("") ? "" : ",") + "비밀번호는 5자 이상, 30자 이하입니다.";
		
		if (CommonResource.isEmptyString(user.getPwd()))
			msg += (msg.equals("") ? "" : ",") + "비밀번호는 필수값입니다.";
		else
		if (user.getPwd().trim().length() > 30 || user.getPwd().trim().length() < 5)
			msg += (msg.equals("") ? "" : ",") + "비밀번호는 5자 이상, 30자 이하입니다.";
		
		if (CommonResource.isEmptyString(user.getName()))
			msg += (msg.equals("") ? "" : ",") + "이름은 필수값입니다.";
		
		return msg;
	}
	
	
}
