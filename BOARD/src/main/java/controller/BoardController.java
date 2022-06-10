package controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.AppContext;
import common.Autowired;
import common.CommonResource;
import domain.Board;
import domain.BoardSelector;
import service.BoardService;

@WebServlet("/board/*")
public class BoardController extends HttpServlet {
	@Autowired
	BoardService boardService;

	@Override
	public void init() {
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
		String responseUrl = "";
		if (doUrl.equals("/board/list")) { //게시글목록 조회
			//로그인창으로 이동
			if (request.getSession().getAttribute("id") == null) {
				response.sendRedirect( request.getContextPath() + "/login/login?toURL=/board/list");
				return;
			}else {
				try {
					List<Board> boardList = null;
				
					Calendar cal = Calendar.getInstance();
					cal.set(Calendar.HOUR_OF_DAY, 0);
					cal.set(Calendar.MINUTE, 0);
					cal.set(Calendar.SECOND, 0);
					cal.set(Calendar.MILLISECOND, 0);
					Date today = cal.getTime();
					
					BoardSelector boardSelector = null;
					try {
						boardSelector = (BoardSelector)CommonResource.dataBind(request.getParameterMap(), Class.forName("domain.BoardSelector"));	
					} catch (Exception e) {
						response.setStatus(500);
						throw new Exception("데이터 변환중 에러가 발생했습니다");				
					}
					
					try {
						boardSelector.setTotalCnt(boardService.selectBoardCnt(boardSelector)); //검색조건으로 총 게시글 수 조회
						boardSelector.calcPageInfo(); // 페이징
						boardList = boardService.selectBoardList(boardSelector);					
					} catch (SQLException e) {
						response.setStatus(500);
						Exception ex = new Exception("데이터베이스 에러가 발생했습니다.");
						ex.initCause(e);
						throw ex;
					}
	
					request.setAttribute("boardList", boardList);
					request.setAttribute("boardSelector", boardSelector);
					request.setAttribute("today", today);
		
					responseUrl =  CommonResource.viewPath + "boardList.jsp";
					
					if (request.getParameter("msg") != null)
						responseUrl += "?msg=" + URLEncoder.encode(request.getParameter("msg"), "utf-8");
			
				} catch (Exception e) {
					e.printStackTrace();	
					responseUrl = "/?msg=" + URLEncoder.encode(e.getMessage(), "utf-8");  
				}	
			}
		}else if(doUrl.equals("/board/board")) { //게시글 조회	
			try {
				
				int bno = request.getParameter("bno") != null ? Integer.parseInt(request.getParameter("bno")) : 1;
				
				BoardSelector boardSelector = null;
				
				try {
					boardSelector = (BoardSelector)CommonResource.dataBind(request.getParameterMap(), Class.forName("domain.BoardSelector")); 		
				} catch (Exception e) {
					response.setStatus(500);
					throw new Exception("데이터 변환 중 오류가 발생했습니다");
				}
 
				Board board = null;
				try {
					 board = boardService.selectBoard(bno);	
				} catch (SQLException e) {
					response.setStatus(500);
					Exception ex = new Exception("데이터베이스 오류가 발생했습니다");
					ex.initCause(e);
					throw ex;				
				}
				
				request.setAttribute("board", board); 
				request.setAttribute("boardSelector", boardSelector);
				
				responseUrl = CommonResource.viewPath + "board.jsp"; 
				
			} catch (Exception e) {
				e.printStackTrace();
				responseUrl = "/?msg=" + URLEncoder.encode(e.getMessage(), "utf-8");  
			}
		}else if(doUrl.equals("/board/add")) {
			responseUrl = CommonResource.viewPath + "board.jsp"; 
		}else {
			response.setStatus(404);
			responseUrl = "/?msg=" + URLEncoder.encode("요청을 찾을 수 없습니다", "utf-8"); 
		}
		request.getRequestDispatcher(responseUrl).forward(request, response);		
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String doUrl = request.getRequestURI().replace(request.getContextPath(), "");	
		String responseUrl = "";
		
		if (request.getSession().getAttribute("id") == null) {
			response.setStatus(400);
			response.sendRedirect(request.getContextPath() + "?msg=" + URLEncoder.encode("로그인이 필요합니다.", "utf-8"));
			return;
		}
				
		if(doUrl.equals("/board/modify")) {
			try {				
				if (request.getParameter("bno") == null) {
					response.setStatus(400);
					response.sendRedirect(request.getContextPath() + "?msg=" + URLEncoder.encode("잘못된 요청입니다.", "utf-8"));
					return;
				}
				
				Board board = null;
				BoardSelector boardSelector = null;
				try {
					board = (Board)CommonResource.dataBind(request.getParameterMap(), Class.forName("domain.Board"));
					boardSelector = (BoardSelector)CommonResource.dataBind(request.getParameterMap(), Class.forName("domain.BoardSelector")); 
				} catch (Exception e) {
					response.setStatus(400);
					throw new Exception("데이터 변환중 오류가 발생했습니다");
				}

				board.setWriter((String)request.getSession().getAttribute("id")); 
				
				String msg = BoardValidation(board);
				if (!msg.equals("")) {
					response.setStatus(400);
					throw new Exception(msg);
				}
				
				try {
					if (boardService.modifyBoard(board) != 1) {
						response.setStatus(400);
						throw new Exception("게시글 수정에 실패했습니다.");	
					}
				} catch (SQLException e) {
					response.setStatus(500);
					Exception ex = new Exception("데이터베이스 오류가 발생했습니다");
					ex.initCause(e);
					throw ex;	
				}
				
				String queryString = "";
				queryString += "?msg=" + URLEncoder.encode("글 수정에 성공하셨습니다.", "utf-8");
				queryString += "&" + boardSelector.queryString("utf-8");
		
				response.sendRedirect(request.getContextPath() + "/board/list" + queryString);			
				return;
			} catch (Exception e) {
				e.printStackTrace();
				response.sendRedirect(request.getContextPath() + "/board/list?" + URLEncoder.encode(e.getMessage(), "utf-8"));			
				return;
			}
		}else if(doUrl.equals("/board/remove")) {
			try {
				if (request.getParameter("bno") == null) {
					response.setStatus(400);
					response.sendRedirect(request.getContextPath() + "?msg=" + URLEncoder.encode("잘못된 요청입니다.", "utf-8"));
					return;
				}
				
				int bno = Integer.parseInt(request.getParameter("bno"));
				String id = (String)request.getSession().getAttribute("id");		
				BoardSelector boardSelector = null;
				
				try {
					boardSelector = (BoardSelector)CommonResource.dataBind(request.getParameterMap(), Class.forName("domain.BoardSelector"));	
				} catch (Exception e) {
					response.setStatus(400);
					throw new Exception("데이터 변환중 오류가 발생했습니다");
				}
			
				try {
					if (boardService.removeBoard(id, bno) <= 0) {
						response.setStatus(400);
						throw new Exception("글 삭제에 실패했습니다.");
					}
				} catch(SQLException e) {	
					response.setStatus(500);
					Exception ex =  new Exception("데이터베이스 오류가 발생했습니다");
					ex.initCause(e);
					throw ex;
				}
						
				String queryString = "";
				queryString += "?msg=" + URLEncoder.encode("글 삭제에 성공하셨습니다.", "utf-8");
				queryString += "&" + boardSelector.queryString("utf-8");				
				response.sendRedirect(request.getContextPath() + "/board/list" + queryString);
				return;
				
			} catch (Exception e) {
				e.printStackTrace();
				response.sendRedirect(request.getContextPath() + "/board/list?msg=" +URLEncoder.encode(e.getMessage(), "utf-8"));			
			}
		}else if(doUrl.equals("/board/add")) {
			try {
				Board board = null;
				try {
					board = (Board)CommonResource.dataBind(request.getParameterMap(), Class.forName("domain.Board"));	
				} catch (Exception e) {
					response.setStatus(400);
					throw new Exception("데이터 변환중 에러가 발생했습니다");
				}

				board.setWriter((String)request.getSession().getAttribute("id")); 
				
				String msg = BoardValidation(board);
				
				if (!msg.equals("")){
					response.setStatus(400);
					throw new Exception(msg);
				}
			
				try {
					if (boardService.addBoard(board) <= 0) {
						response.setStatus(400);
						throw new Exception("게시글 추가중 오류가 발생했습니다.");
					} 	
				} catch (SQLException e) {
					response.setStatus(500);
					Exception ex =  new Exception("데이터베이스 오류가 발생했습니다");
					ex.initCause(e);
					throw ex;
				}
			
				response.sendRedirect(request.getContextPath() + "/board/list?msg=" + URLEncoder.encode("게시글 작성에 성공하셨습니다.", "utf-8"));
				return;
				
			} catch (Exception e) {
				e.printStackTrace();
				response.sendRedirect(request.getContextPath() + "/board/list?msg=" + URLEncoder.encode(e.getMessage(), "utf-8"));
			}
		}else {
			response.setStatus(404);
			response.sendRedirect(request.getContextPath() + "/board/list?msg=" + URLEncoder.encode("요청을 찾을 수 없습니다.", "utf-8"));
			return;
		}
		request.getRequestDispatcher(responseUrl).forward(request, response);	
	}
	
	public String BoardValidation(Board board) {
		String msg = "";
		
		if (CommonResource.isEmptyString(board.getTitle()))
			msg += (msg.equals("") ? "" : ",") + "게시글 제목은 필수입니다.";
		
		if (CommonResource.isEmptyString(board.getContent()))
			msg += (msg.equals("") ? "" : ",") + "게시글 내용은 필수값입니다.";
	
		return msg;
	}
	
	@Override
	public String toString() {
		return "BoardController [boardService=" + boardService + "]";
	}
	
	
}
