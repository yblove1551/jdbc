package dao;

import static common.CommonResource.closeAutoCloseableResource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import common.Autowired;
import common.Bean;
import common.CommonResource;
import domain.User;

@Bean
public class UserDAO {
	private DataSource dataSource;
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;	
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	//로그인
	public User selectUser(String id, String pwd) throws SQLException{
		User user = null;
		try {
			conn = dataSource.getConnection();
			
			String sql = "";
			sql += "SELECT * FROM USER_INFO " + System.lineSeparator();
			sql += "WHERE id = ?            ";	
			
			if (!CommonResource.isEmptyString(pwd))
				sql += " AND pwd = ?              ";	
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			
			if (!CommonResource.isEmptyString(pwd))
				pstmt.setString(2, pwd);
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				user = new User();
				user.setId(rs.getString("id"));
				user.setPwd(rs.getString("pwd"));
				user.setName(rs.getString("name"));
				user.setEmail(rs.getString("email"));
				user.setPhone(rs.getString("phone"));
				user.setBirth(rs.getDate("birth")); //
				user.setReg_date(new java.util.Date(rs.getDate("reg_date").getTime()));
			}
			
			return user;
			
		} catch(SQLException e) {
			throw e;
		} finally {
			closeAutoCloseableResource(rs, pstmt, conn);
		}	
	}	
	
//	조회
	public User selectUser(String id) throws SQLException{
		return selectUser(id, null);
	}
	
	public User selectByEmail(String email)  throws SQLException{
		User user = null;
		try {
			conn = dataSource.getConnection();
			
			String sql = "";
			sql += "SELECT * FROM USER_INFO " + System.lineSeparator();
			sql += "WHERE email = ?         ";	
						
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, email);
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				user = new User();
				user.setId(rs.getString("id"));
				user.setPwd(rs.getString("pwd"));
				user.setName(rs.getString("name"));
				user.setEmail(rs.getString("email"));
				user.setPhone(rs.getString("phone"));
				user.setBirth(rs.getDate("birth")); //
				user.setReg_date(new java.util.Date(rs.getDate("reg_date").getTime()));
			}
			
			return user;
			
		} catch(SQLException e) {
			throw e;
		} finally {
			closeAutoCloseableResource(rs, pstmt, conn);
		}			
	}
	
	public User selectByEmailAndName(String email, String name)  throws SQLException{
		User user = null;
		try {
			conn = dataSource.getConnection();
			
			String sql = "";
			sql += "SELECT * FROM USER_INFO 		" + System.lineSeparator();
			sql += "WHERE email = ? AND name = ?  	";	
						
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, email);
			pstmt.setString(2, name);
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				user = new User();
				user.setId(rs.getString("id"));
				user.setPwd(rs.getString("pwd"));
				user.setName(rs.getString("name"));
				user.setEmail(rs.getString("email"));
				user.setPhone(rs.getString("phone"));
				user.setBirth(rs.getDate("birth")); //
				user.setReg_date(new java.util.Date(rs.getDate("reg_date").getTime()));
			}
			
			return user;
			
		} catch(SQLException e) {
			throw e;
		} finally {
			closeAutoCloseableResource(rs, pstmt, conn);
		}			
	}
	
//	삭제
	public int deleteUser(String id) throws SQLException {
		try {
			conn = dataSource.getConnection();
			
			String sql = "";
			sql += "DELETE FROM USER_INFO " + System.lineSeparator();
			sql += "WHERE id=?            ";		
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			
			return pstmt.executeUpdate();
						
		} catch(SQLException e) {
			throw e;
		} finally {
			closeAutoCloseableResource(pstmt, conn);
		}	
	}
//	수정
	public int updateUser(User user) throws SQLException {
		try {
			conn = dataSource.getConnection();
			
			String sql = "";
			sql += "UPDATE USER_INFO      " + System.lineSeparator();
			sql += "SET pwd = ?           " + System.lineSeparator();
			sql += "   ,name = ?          " + System.lineSeparator();
			sql += "   ,email = ?         " + System.lineSeparator();
			sql += "   ,birth = ?         " + System.lineSeparator();
			sql += "   ,phone = ?         " + System.lineSeparator();
			sql += "WHERE id = ?          ";
					
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user.getPwd());
			pstmt.setString(2, user.getName());
			pstmt.setString(3, user.getEmail());
			pstmt.setDate(4, new java.sql.Date(user.getBirth().getTime()));
			pstmt.setString(5, user.getPhone());
			pstmt.setString(6, user.getId());
			
			return pstmt.executeUpdate();
			
		} catch (SQLException e) {
			throw e;
		} finally {
			closeAutoCloseableResource(pstmt, conn);
		}	
	}
	
	public int updatePw(String id, String pw) throws SQLException{
		try {
			conn = dataSource.getConnection();
			
			String sql = "";
			sql += "UPDATE USER_INFO      " + System.lineSeparator();
			sql += "SET pwd = ?           " + System.lineSeparator();
			sql += "WHERE id = ?          ";
					
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, pw);
			pstmt.setString(2, id);		
			
			return pstmt.executeUpdate();
			
		} catch (SQLException e) {
			throw e;
		} finally {
			closeAutoCloseableResource(pstmt, conn);
		}			
	}
	
	
//	추가
	public int insertUser(User user) throws SQLException {
		try {
			conn = dataSource.getConnection();
			
			String sql = "";
			sql += "INSERT INTO USER_INFO   " + System.lineSeparator();
			sql += "(id,	pwd,	name,   " + System.lineSeparator();
			sql += " email,	birth,	phone,  " + System.lineSeparator();
			sql += " reg_date)              " + System.lineSeparator();
			sql += "VALUES                  " + System.lineSeparator();
			sql += "(?,		?,		?,	    " + System.lineSeparator();
			sql += " ?,		?,		?,	    " + System.lineSeparator();
			sql += " sysdate)               ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user.getId());	
			pstmt.setString(2, user.getPwd());
			pstmt.setString(3, user.getName());
			pstmt.setString(4, user.getEmail());
			pstmt.setDate(5, new java.sql.Date(user.getBirth().getTime()));
			pstmt.setString(6, user.getPhone());
			
			return pstmt.executeUpdate();
			
		} catch (SQLException e) {
			throw e;
		} finally {
			closeAutoCloseableResource(pstmt, conn);
		}	
	}
	@Override
	public String toString() {
		return "UserDAO [dataSource=" + dataSource + ", conn=" + conn + ", pstmt=" + pstmt + ", rs=" + rs + "]";
	}	
	
	
}
