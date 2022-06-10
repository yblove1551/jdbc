package dao;

import static common.CommonResource.closeAutoCloseableResource;
import static common.CommonResource.isEmptyString;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import common.Autowired;
import common.Bean;
import domain.Board;
import domain.BoardSelector;

@Bean
public class BoardDAO {
	private DataSource dataSource;
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;	
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	
	//게시글 수 조회
	public int selectBoardCnt(BoardSelector bs) throws SQLException{
		int paramIdx = 1;
		try {
			conn = dataSource.getConnection();
			
			String sql = "";
			sql += "SELECT COUNT(*)       " + System.lineSeparator();
			sql += "FROM BOARD            " + System.lineSeparator();
			sql += "WHERE 1=1             ";
			
			if (!isEmptyString(bs.getOption()) &&
				!isEmptyString(bs.getKeyword())) 
				switch (bs.getOption()) {
						case "T" : 
							sql += " 		AND TITLE LIKE '%'||?||'%' "; 
							break;
						case "W" :
							sql += "		AND WRITER LIKE '%'||?||'%' ";
							break;
						case "A" : 
							sql += " 	    AND (TITLE LIKE '%'||?||'%'   ";
							sql += "  		 OR  CONTENT LIKE '%'||?||'%') ";
					}
			pstmt = conn.prepareStatement(sql);

			if (!isEmptyString(bs.getOption()) &&
				!isEmptyString(bs.getKeyword())) 
				switch (bs.getOption()) {
					case "T" : 
						pstmt.setString(paramIdx++, bs.getKeyword());
						break;
					case "W" :
						pstmt.setString(paramIdx++, bs.getKeyword());								
						break;
					case "A" : 
						pstmt.setString(paramIdx++, bs.getKeyword());
						pstmt.setString(paramIdx++, bs.getKeyword());
				}
			rs = pstmt.executeQuery();
			
			rs.next();
			
			return rs.getInt(1);
			
		} catch(SQLException e) {
			throw e;
		} finally {
			closeAutoCloseableResource(rs, pstmt, conn);
		}						
	}
	
	//게시글 단독 조회
	public Board selectBoard(int bno) throws SQLException{
		Board board = new Board();		
		try {
			conn = dataSource.getConnection();
			
			String sql = "";
			sql += "SELECT BNO,      TITLE, 	CONTENT, 	WRITER," + System.lineSeparator();
			sql += "       VIEW_CNT, REG_DATE, 	UP_DATE            " + System.lineSeparator();
			sql += "FROM BOARD                                     " + System.lineSeparator();
			sql += "WHERE bno = ?                                  ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bno);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				board.setBno(rs.getInt("bno"));
				board.setTitle(rs.getString("title"));
				board.setContent(rs.getString("content"));
				board.setWriter(rs.getString("writer"));
				board.setView_cnt(rs.getInt("view_cnt"));
				board.setReg_date(new java.util.Date(rs.getDate("reg_date").getTime()));
				board.setUp_date(new java.util.Date(rs.getDate("up_date").getTime()));
			}
			
			return board;
	
		} catch(SQLException e) {
			throw e;
		} finally {
			closeAutoCloseableResource(rs, pstmt, conn);
		}		
	}
	//게시글 조회
	public List<Board> boardList(BoardSelector selector) throws SQLException{
		List<Board> list = new ArrayList<Board>();
		int paramIdx = 1;
		try {
			conn = dataSource.getConnection();
			
			String sql = "";
			sql += "SELECT * 						" + System.lineSeparator();
			sql += "FROM   (SELECT ROWNUM RW,   	" + System.lineSeparator();
			sql += "               G1.* 			" + System.lineSeparator();
			sql += "        FROM   (SELECT bno, 	" + System.lineSeparator();
			sql += "			           title, 	" + System.lineSeparator();
			sql += "                       content, " + System.lineSeparator();
			sql += "                       writer, 	" + System.lineSeparator();
			sql += "                       view_cnt," + System.lineSeparator();
			sql += "                       reg_date," + System.lineSeparator();
			sql += "                       up_date 	" + System.lineSeparator();
			sql += "                FROM   board 	" + System.lineSeparator();
			sql += "                WHERE  1 = 1 	" + System.lineSeparator();	
			
			if (!isEmptyString(selector.getOption()) &&
				!isEmptyString(selector.getKeyword())) 
				switch (selector.getOption()) {
						case "T" : 
							sql += "                AND TITLE LIKE '%'||?||'%' " + System.lineSeparator(); 
							break;
						case "W" :
							sql += "                AND WRITER LIKE '%'||?||'%' " + System.lineSeparator();
							break;
						case "A" : 
							sql += "                AND (TITLE LIKE '%'||?||'%'    " + System.lineSeparator();
							sql += "                 OR  CONTENT LIKE '%'||?||'%') " + System.lineSeparator();
					}
			sql += "        ORDER  BY bno DESC) G1) G2 								" + System.lineSeparator();
			sql += "WHERE  G2.rw BETWEEN ? AND ?                                 	";
			
			pstmt = conn.prepareStatement(sql);

			if (!isEmptyString(selector.getOption()) &&
				!isEmptyString(selector.getKeyword())) 
				switch (selector.getOption()) {
					case "T" : 
						pstmt.setString(paramIdx++, selector.getKeyword());
						break;
					case "W" :
						pstmt.setString(paramIdx++, selector.getKeyword());								
						break;
					case "A" : 
						pstmt.setString(paramIdx++, selector.getKeyword());
						pstmt.setString(paramIdx++, selector.getKeyword());
				}
			//(page-1) * pageSize + 1 ~ page * pageSize  
			pstmt.setInt(paramIdx++, (selector.getPage()-1) * selector.getPageSize() + 1);
			pstmt.setInt(paramIdx++, selector.getPage() * selector.getPageSize());						
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				Board board = new Board();
				board.setBno(rs.getInt("bno"));
				board.setTitle(rs.getString("title"));
				board.setContent(rs.getString("content"));
				board.setWriter(rs.getString("writer"));
				board.setView_cnt(rs.getInt("view_cnt"));
				board.setReg_date(new java.util.Date(rs.getTimestamp("reg_date").getTime()));
				board.setUp_date(new java.util.Date(rs.getTimestamp("up_date").getTime()));
				list.add(board);
			}
			
			return list;
	
		} catch(SQLException e) {
			throw e;
		} finally {
			closeAutoCloseableResource(rs, pstmt, conn);
		}	
	}	
	
//	삭제
	public int deleteBoard(String writer, int bno) throws SQLException{
		try {
			conn = dataSource.getConnection();
			
			String sql = "";
			sql += "DELETE FROM BOARD WHERE bno = ? AND writer = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bno);
			pstmt.setString(2, writer);
			
			return pstmt.executeUpdate();
			
		} catch(SQLException e) {
			throw e;
		} finally {
			closeAutoCloseableResource(pstmt, conn);
		}
	}
	
	public int deleteByWriter(String writer) throws SQLException{
		try {
			conn = dataSource.getConnection();
			
			String sql = "";
			sql += "DELETE FROM BOARD WHERE writer = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, writer);
			
			return pstmt.executeUpdate();
			
		} catch(SQLException e) {
			throw e;
		} finally {
			closeAutoCloseableResource(pstmt, conn);
		}		
	}
	
//	수정
	public int updateBoard(Board board) throws SQLException {
		try {
			conn = dataSource.getConnection();
			
			String sql = "";
			sql += "UPDATE BOARD           " + System.lineSeparator();
			sql += "SET title = ?          " + System.lineSeparator();
			sql += "   ,content = ?        " + System.lineSeparator();
			sql += "   ,up_date = sysdate  " + System.lineSeparator();
			sql += "WHERE bno = ?          " + System.lineSeparator();
			sql += "  AND writer = ?       ";
					
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, board.getTitle());
			pstmt.setString(2, board.getContent());
			pstmt.setInt(   3, board.getBno());
			pstmt.setString(4, board.getWriter());
			
			return pstmt.executeUpdate();
			
		} catch(SQLException e) {
			throw e;
		} finally {
			closeAutoCloseableResource(pstmt, conn);
		}	
	}
	//조회수 업데이트
	public int updateViewCnt(int bno) throws SQLException{
		try {
			conn = dataSource.getConnection();
			
			String sql = "";
			sql += "UPDATE BOARD                " + System.lineSeparator();
			sql += "SET view_cnt = view_cnt + 1 " + System.lineSeparator();
			sql += "WHERE bno = ?               ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bno);
		
			return pstmt.executeUpdate();
			
		} catch(SQLException e) {
		 	throw e;
		} finally {
			closeAutoCloseableResource(pstmt, conn);
		}
	}
	
	//추가
	public int insertBoard(Board board) throws SQLException {
		try {
			conn = dataSource.getConnection();
			
			String sql = "";
			sql += "INSERT INTO BOARD           		 " + System.lineSeparator();
			sql += "(bno,	 title,	 content, writer)    " + System.lineSeparator();
			sql += "VALUES       	            		 " + System.lineSeparator();
			sql += "(BOARD_SEQ.NEXTVAL,	?, ?, ?)		 ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, board.getTitle());
			pstmt.setString(2, board.getContent());
			pstmt.setString(3, board.getWriter());
			
			return pstmt.executeUpdate();
						
		} catch(SQLException e) {
			throw e;
		} finally {
			closeAutoCloseableResource(pstmt, conn);
		}	
	}
	
	@Override
	public String toString() {
		return "boardDAO [dataSource=" + dataSource + ", conn=" + conn + ", pstmt=" + pstmt + ", rs=" + rs + "]";
	}	
	
}
