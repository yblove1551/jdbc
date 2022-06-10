package service;
import java.sql.SQLException;

import common.Autowired;
import common.Bean;
import dao.BoardDAO;
import dao.ReplyDAO;
import dao.UserDAO;
import domain.User;
@Bean
public class UserService { 
	private UserDAO userDAO;
	private BoardDAO boardDAO;
	private ReplyDAO replyDAO;
	
	@Autowired
	public UserService(UserDAO userDAO, BoardDAO boardDAO, ReplyDAO replyDAO) {
		this.userDAO = userDAO;
		this.boardDAO = boardDAO;
		this.replyDAO = replyDAO;
	}
	
	
	public User selectUser(String id) throws SQLException{
		return userDAO.selectUser(id); 
	}
	
	public int removeUser(String id) throws SQLException{ 
		replyDAO.deleteByReplyer(id);
		boardDAO.deleteByWriter(id);
		return userDAO.deleteUser(id);
	}
	
	public int modifyUser(User user) throws SQLException{
		return userDAO.updateUser(user);
	}
	
	public int addUser(User user) throws SQLException{
		return userDAO.insertUser(user);
	}
	
	public User selectUserByEmail(String email) throws SQLException{
		return userDAO.selectByEmail(email);
	}
	
	public User selectByEmailAndName(String email, String name)  throws SQLException{
		return userDAO.selectByEmailAndName(email, name);	
	}
	
	public int updatePw(String id, String pw) throws SQLException{
		return userDAO.updatePw(id, pw);		
	}

	@Override
	public String toString() {
		return "UserService [userDAO=" + userDAO + ", boardDAO=" + boardDAO + ", replyDAO=" + replyDAO + "]";
	}
	
}
