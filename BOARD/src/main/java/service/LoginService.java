package service;
import java.sql.SQLException;

import common.Autowired;
import common.Bean;
import dao.UserDAO;
import domain.User;
@Bean
public class LoginService { 
	UserDAO userDAO;
	
	@Autowired
	public LoginService(UserDAO userDAO){
		this.userDAO = userDAO;
	}

	public User login(String id, String pw) throws SQLException{
		return userDAO.selectUser(id,  pw);
	}

	@Override
	public String toString() {
		return "LoginService [userDAO=" + userDAO + "]";
	}	
	
	
}
