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
import domain.MailTemplete;
@Bean
public class MailTempleteDAO {
	private DataSource dataSource;
	private Connection conn;
	private ResultSet rs;
	private PreparedStatement pstmt;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
			
	public List<MailTemplete> selectAll() throws SQLException{
		List<MailTemplete> list = new ArrayList<MailTemplete>();
		try {
			conn = dataSource.getConnection();
			
			String sql = "";
			sql += "SELECT * FROM MAIL_TEMPLETE ";
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				MailTemplete mailTemplete = new MailTemplete();
				mailTemplete.setKind(rs.getInt("kind"));
				mailTemplete.setTitle(rs.getString("title"));
				mailTemplete.setContent(rs.getString("content"));
				mailTemplete.setDescription(rs.getString("description"));
				mailTemplete.setUse_yn(rs.getString("use_yn"));
				list.add(mailTemplete);
			}
			
			return list;
			
		} catch (SQLException e) {
			throw e;
		} finally {
			closeAutoCloseableResource(rs, pstmt, conn);
		}
	}
	
	public MailTemplete selectByKind(int kind) throws SQLException{
		MailTemplete mailTemplete = null;
		
		try {
			conn = dataSource.getConnection();
			
			String sql = "";
			sql += "SELECT * FROM MAIL_TEMPLETE WHERE KIND = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, kind);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				mailTemplete = new MailTemplete();
				mailTemplete.setKind(rs.getInt("kind"));
				mailTemplete.setTitle(rs.getString("title"));
				mailTemplete.setContent(rs.getString("content"));
				mailTemplete.setDescription(rs.getString("description"));
				mailTemplete.setUse_yn(rs.getString("use_yn"));
			}	
			
			return mailTemplete;
			
		} catch (SQLException e) {
			throw e;
		} finally {
			closeAutoCloseableResource(rs, pstmt, conn);
		}
	}	
	
	@Override
	public String toString() {
		return "MailTempDao [dataSource=" + dataSource + ", conn=" + conn + ", pstmt=" + pstmt + ", rs=" + rs + "]";
	}	

}
