package dao;

import static common.CommonResource.closeAutoCloseableResource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import common.Autowired;
import common.Bean;
import domain.Reply;

@Bean
public class ReplyDAO {
	private DataSource dataSource;
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;	

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
		
	//댓글리스트 조회
	public List<Reply> selectReplyList(int bno) throws SQLException{
		List<Reply> replyList = new ArrayList<Reply>();
		try {
			conn = dataSource.getConnection();
			
			String sql = "";
			sql += "SELECT  rno, 		bno, 		prno,  		reply,	" + System.lineSeparator();  
			sql += "		replyer, 	reg_date, 	up_date, 	level	" + System.lineSeparator();
			sql += "FROM REPLY 											" + System.lineSeparator();
			sql += "START WITH PRNO = 0 AND BNO = ? 					" + System.lineSeparator();
			sql += "CONNECT BY PRIOR RNO = PRNO 						" + System.lineSeparator();
			sql += "ORDER SIBLINGS BY RNO 								";
						
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bno);			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				Reply reply = new Reply();
				reply.setRno(rs.getInt("rno"));
				reply.setBno(rs.getInt("bno"));
				reply.setPrno(rs.getInt("prno"));
				reply.setReply(rs.getString("reply"));
				reply.setReplyer(rs.getString("replyer"));
				reply.setReg_date(new java.util.Date(rs.getTimestamp("reg_date").getTime())); 
				reply.setUp_date(new java.util.Date(rs.getTimestamp("up_date").getTime()));
				reply.setLevel(rs.getInt("level"));
				replyList.add(reply);
			}	
			
			return replyList; 
			
		} catch(SQLException e) {
			throw e;
		} finally {
			closeAutoCloseableResource(rs, pstmt, conn);
		}	
	}	
	
//	댓글단독조회
	public Reply selectReply(int rno) throws Exception{
		Reply reply = null;
		try {
			conn = dataSource.getConnection();
			
			String sql = "";
			sql += "SELECT * FROM REPLY " + System.lineSeparator();
			sql += "WHERE rno = ?       ";	
						
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, rno);			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				reply = new Reply();
				reply.setRno(rs.getInt("rno"));
				reply.setBno(rs.getInt("bno"));
				reply.setPrno(rs.getInt("prno"));
				reply.setReply(rs.getString("reply"));
				reply.setReplyer(rs.getString("replyer"));
				reply.setReg_date(new java.util.Date(rs.getTimestamp("reg_date").getTime())); 
				reply.setUp_date(new java.util.Date(rs.getTimestamp("up_date").getTime()));
			}
			
			return reply; 
			
		} catch(SQLException e) {
			throw e;
		} finally {
			closeAutoCloseableResource(rs, pstmt, conn);
		}
	}
	
	
	//삭제
	public int deleteReply(int rno, String replyer) throws SQLException {
		try {
			conn = dataSource.getConnection();
			
			String sql = "";
			sql += "DELETE FROM REPLY 							" + System.lineSeparator();
			sql += "WHERE rno IN (    							" + System.lineSeparator();
			sql += "	SELECT rno FROM REPLY					" + System.lineSeparator();
			sql += "	START WITH rno = ? AND replyer = ?		" + System.lineSeparator();
			sql += "	CONNECT BY PRIOR rno = prno				" + System.lineSeparator();
			sql += "	)										";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, rno);
			pstmt.setString(2, replyer);
			
			return pstmt.executeUpdate();
			
		} catch(SQLException e) {
			throw e;
		} finally {
			closeAutoCloseableResource(pstmt, conn);
		}	
	}
	
	public int deleteByReplyer(String replyer) throws SQLException{
		try {
			conn = dataSource.getConnection();
			
			String sql = "";
			sql += "DELETE FROM REPLY 							" + System.lineSeparator();
			sql += "WHERE rno IN (    							" + System.lineSeparator();
			sql += "	SELECT rno FROM REPLY					" + System.lineSeparator();
			sql += "	START WITH replyer = ?					" + System.lineSeparator();
			sql += "	CONNECT BY PRIOR rno = prno				" + System.lineSeparator();
			sql += "	)										";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, replyer);
			
			return pstmt.executeUpdate();	
			
		} catch(SQLException e) {
			throw e;
		} finally {
			closeAutoCloseableResource(pstmt, conn);
		}	
	}
	
	public int deleteByBno(int bno) throws SQLException{
		try {
			conn = dataSource.getConnection();
			
			String sql = "";
			sql += "DELETE FROM REPLY WHERE bno = ? ";     
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bno);
			
			return pstmt.executeUpdate();
			
		} catch(SQLException e) {
			throw e;
		} finally {
			closeAutoCloseableResource(pstmt, conn);
		}		
	}
//수정
	public int updateReply(Reply reply, String replyer) throws SQLException {
		try {
			conn = dataSource.getConnection();
			
			String sql = "";
			sql += "UPDATE REPLY              " + System.lineSeparator();
			sql += "SET reply = ?             " + System.lineSeparator();
			sql += "   ,up_date = sysdate     " + System.lineSeparator();
			sql += "WHERE rno=? AND replyer=? ";
					
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, reply.getReply());
			pstmt.setInt(2, reply.getRno());
			pstmt.setString(3, replyer);
	
			return pstmt.executeUpdate();
						
		} catch(SQLException e) {
			throw e;
		} finally {
			closeAutoCloseableResource(pstmt, conn);
		}	
	}
	
	
//	추가
	public int insertReply(Reply reply) throws SQLException {
		try {
			conn = dataSource.getConnection();
			
			String sql = "";
			sql += "INSERT INTO reply        	 		 " + System.lineSeparator();
			sql += "(rno,		bno,	 	prno,     	 " + System.lineSeparator();
			sql += " reply,	 	replyer, 	reg_date, 	 " + System.lineSeparator();
			sql += " up_date)                			 " + System.lineSeparator();
			sql += "VALUES                    			 " + System.lineSeparator();
			sql += "(REPLY_SEQ.NEXTVAL, ?, ?, ?, ?, sysdate, " + System.lineSeparator();
			sql += " sysdate)               		 	";
			
			pstmt = conn.prepareStatement(sql);	
			pstmt.setInt(1, reply.getBno());
			pstmt.setInt(2, reply.getPrno());
			pstmt.setString(3, reply.getReply());
			pstmt.setString(4, reply.getReplyer());
			
			return pstmt.executeUpdate();
						
		} catch(SQLException e) {
			throw e;
		} finally {
			closeAutoCloseableResource(pstmt, conn);
		}	
	}	
	@Override
	public String toString() {
		return "replyDAO [dataSource=" + dataSource + ", conn=" + conn + ", pstmt=" + pstmt + ", rs=" + rs + "]";
	}	
}
