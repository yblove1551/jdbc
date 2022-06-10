package controller;

import static common.CommonResource.dataBind;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;

import common.AppContext;
import common.Autowired;
import domain.Reply;
import service.ReplyService;

@WebServlet("/reply/*")
public class ReplyController extends HttpServlet {
	@Autowired
	ReplyService replyService;
	
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
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{		
		String doUrl = request.getRequestURI().replace(request.getContextPath(), "");	
		PrintWriter out = response.getWriter();
		if (doUrl.equals("/reply/reply")) { // 댓글조회
			List<Reply> replyList = null;
			try {
				if (request.getParameter("bno") == null) {
					response.setStatus(400);
					throw new Exception("잘못된 요청입니다.");
				}
				
				int bno = Integer.parseInt(request.getParameter("bno"));
				
				try {
					replyList = replyService.readReplyList(bno);	
				} catch (SQLException e) {
					response.setStatus(500);
					Exception ex = new Exception("데이터베이스 오류가 발생했습니다");
					ex.initCause(e);
					throw ex;
				}

				JSONArray jsonArray = new JSONArray();
				
				for (Reply reply : replyList) 
					jsonArray.add(reply.toJSONObj());
				
				out.write(jsonArray.toString());
								
			//	out.write(jsonArray.toString());				
			} catch (Exception e) {
				e.printStackTrace();
				out.write("{\"result\":\"ERROR\", \"errMsg\":\""+e.getMessage()+"\"}");	
			}
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String doUrl = request.getRequestURI().replace(request.getContextPath(), "");	
		PrintWriter out = response.getWriter();
		
		if (request.getSession().getAttribute("id") == null) {
			response.setStatus(400);
			response.sendRedirect(request.getContextPath() + "?msg=" + URLEncoder.encode("로그인이 필요합니다.", "utf-8"));
			return;
		}
		
		if (doUrl.equals("/reply/add")) { // 댓글추가		
			try {
				Reply reply = null;
				try {
					reply = (Reply)dataBind(request.getParameterMap(), Class.forName("domain.Reply"));
				} catch (Exception e) {
					response.setStatus(400);
					throw new Exception("데이터 변환중 오류가 발생했습니다");
				}
				
				reply.setReplyer((String)request.getSession().getAttribute("id"));

				try {
					if (replyService.addReply(reply) <= 0){
						response.setStatus(400);
						throw new Exception("댓글 추가중 에러가 발생했습니다");
					}
			
				} catch (SQLException e) {
					response.setStatus(500);
					Exception ex = new Exception("데이터베이스 오류가 발생했습니다.");
					ex.initCause(e);
					throw ex;
				}					
				out.write("{\"result\":\"OK\"}");				
			} catch (Exception e) {
				e.printStackTrace();
				out.write("{\"result\":\"ERROR\", \"errMsg\":\""+e.getMessage()+"\"}");	
			}
		}else if (doUrl.equals("/reply/modify")) {//댓글 수정
			try {				
				if (request.getParameter("rno") == null) {
					response.setStatus(400);
					throw new Exception("잘못된 요청입니다.");					
				}
				
				Reply reply = null;
				try {
					reply = (Reply)dataBind(request.getParameterMap(), Class.forName("domain.Reply"));	
				} catch (Exception e) {
					response.setStatus(400);
					throw new Exception("데이터 변환중 오류가 발생했습니다");
				}
				
				String replyer = (String)request.getSession().getAttribute("id");
				
				//검증
				try {
					if (replyService.modifyReply(reply, replyer) <= 0) {
						response.setStatus(400);
						throw new Exception("댓글 수정중 오류가 발생했습니다");	
					}				
				} catch (SQLException e) {
					response.setStatus(500);
					Exception ex = new Exception("데이터베이스 오류가 발생했습니다.");
					ex.initCause(e);
					throw ex;
				};	
			//	throw new Exception("test에러");
				out.write("{\"result\":\"OK\"}");					
			} catch (Exception e) {
				e.printStackTrace();
				out.write("{\"result\":\"ERROR\", \"errMsg\":\""+e.getMessage()+"\"}");	
			}			
		}else if (doUrl.equals("/reply/remove")) {//삭제
			try {
				if (request.getParameter("rno") == null) {
					response.setStatus(400);
					throw new Exception("잘못된 요청입니다.");					
				}
				
				int rno = Integer.parseInt(request.getParameter("rno"));
				String replyer = (String)request.getSession().getAttribute("id");
				
				//검증	
				try {
					if (replyService.removeReply(rno, replyer) <= 0) {
						response.setStatus(400);
						throw new Exception("게시글 삭제중 오류가 발생했습니다");	
					}										
				} catch (SQLException e) {
					response.setStatus(500);
					Exception ex = new Exception("데이터베이스 오류가 발생했습니다.");
					ex.initCause(e);
					throw ex;
				}
				out.write("{\"result\":\"OK\"}");		
			} catch (Exception e) {
				e.printStackTrace();
				out.write("{\"result\":\"ERROR\", \"errMsg\":\""+e.getMessage()+"\"}");	
			}				
		}
	}
	@Override
	public String toString() {
		return "ReplyController [replyService=" + replyService + "]";
	}	
	
	
}
