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
import domain.Mail;
@Bean
public class MailDAO {	
	private DataSource dataSource;
	private Connection conn;
	private ResultSet rs;
	private PreparedStatement pstmt;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public int insertMailLog(Mail mail) throws SQLException {
		try {
			conn = dataSource.getConnection();
			
			String sql = "";
			sql += "INSERT INTO MAIL_LOG          									"+System.lineSeparator();
			sql += "(seq, 				kind, 			sender, 	sender_email,	"+System.lineSeparator();
			sql += " receiver, 			receiver_email, title, 		content, 	  	"+System.lineSeparator();
			sql += " send_time,			success,		err_msg)					"+System.lineSeparator();
			sql += "VALUES                                                    	    "+System.lineSeparator();
			sql += "(MAIL_SEQ.NEXTVAL, 	?, 				?, 			?, 				"+System.lineSeparator();
			sql += " ?, 				?, 				?, 			?, 				"+System.lineSeparator();
			sql += "sysdate, 			?, 				?)							";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, mail.getKind());
			pstmt.setString(2, mail.getSender());
			pstmt.setString(3, mail.getSender_email());
			pstmt.setString(4, mail.getReceiver());
			pstmt.setString(5, mail.getReceiver_email());
			pstmt.setString(6, mail.getTitle());
			pstmt.setString(7, mail.getContent());
			pstmt.setString(8, mail.getSuccess());
			pstmt.setString(9, mail.getErr_msg());
			
			return pstmt.executeUpdate();

		} catch (SQLException e) {
			throw e;
		} finally {
			closeAutoCloseableResource(rs,pstmt, conn);
		}
	}
		
	public List<Mail> selectByStringColumn(String column, String value) throws SQLException{
		List<Mail> list = new ArrayList<>();
		try {
			conn = dataSource.getConnection();
			
			String sql = "";
			sql += "SELECT * FROM MAIL_LOG WHERE " + column + " = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, value);
			rs = pstmt.executeQuery();

			while(rs.next()) {
				Mail mail = new Mail();
				mail.setSeq(rs.getInt("seq"));
				mail.setKind(rs.getInt("kind"));
				mail.setSender(rs.getString("sender"));
				mail.setSender_email(rs.getString("sender_email"));
				mail.setReceiver(rs.getString("receiver"));
				mail.setReceiver_email(rs.getString("receiver_email"));
				mail.setTitle(rs.getString("title"));
				mail.setContent(rs.getString("content"));
				mail.setSend_time(new java.util.Date(rs.getTimestamp("send_time").getTime()));
				mail.setSuccess(rs.getString("success"));			
				list.add(mail);
			}
			
			return list;
			
		} catch (SQLException e) {
			throw e;
		} finally {
			closeAutoCloseableResource(rs,pstmt, conn);
		}			
	}
	public List<Mail> selectBySender(String sender) throws SQLException {
		return selectByStringColumn("sender", sender);
	}
	
	public List<Mail> selectByReceiver(String receiver) throws SQLException {
		return selectByStringColumn("receiver", receiver);	
	}
	
	public List<Mail> selectBySenderEmail(String sender_email) throws SQLException{
		return selectByStringColumn("sender_email", sender_email);
		
	}

	public List<Mail> selectByReceiverEmail(String receiver_email) throws SQLException{
		return selectByStringColumn("receiver_email", receiver_email);
	}
	
	public int deleteAll() throws SQLException{
		try {
			conn = dataSource.getConnection();
			
			String sql = "";
			sql += "DELETE FROM MAIL_LOG WHERE 1=1";
			
			pstmt = conn.prepareStatement(sql);
			
			return pstmt.executeUpdate();
			
		} catch (SQLException e) {
			throw e;
		} finally {
			closeAutoCloseableResource(pstmt, conn);
		}			
	}
	
	public int deleteBySenderEmail(String sender_email) throws SQLException{
		try {
			conn = dataSource.getConnection();
			
			String sql = "";
			sql += "DELETE FROM MAIL_LOG WHERE sender_Email = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, sender_email);
			
			return pstmt.executeUpdate();
			
		} catch (SQLException e) {
			throw e;
		} finally {
			closeAutoCloseableResource(pstmt, conn);
		}		
	}
	
	public int deleteByReceiverEmail(String receiver_email) throws SQLException{
		try {
			conn = dataSource.getConnection();
			
			String sql = "";
			sql += "DELETE FROM MAIL_LOG WHERE receiver_email = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, receiver_email);
			
			return pstmt.executeUpdate();
			
		} catch (SQLException e) {
			throw e;
		} finally {
			closeAutoCloseableResource(pstmt, conn);
		}		
	}
	
	@Override
	public String toString() {
		return "MailDAO [dataSource=" + dataSource + ", conn=" + conn + ", pstmt=" + pstmt + ", rs=" + rs + "]";
	}	
}
