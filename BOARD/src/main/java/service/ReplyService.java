package service;
import java.sql.SQLException;
import java.util.List;

import common.Autowired;
import common.Bean;
import dao.ReplyDAO;
import domain.Reply;
@Bean
public class ReplyService { 
	ReplyDAO replyDAO;
	
	@Autowired
	public ReplyService(ReplyDAO replyDAO) {
		this.replyDAO = replyDAO;
	}
	
	public List<Reply> readReplyList(int bno) throws SQLException{
		return replyDAO.selectReplyList(bno);
	}
	
	public Reply readReply(int rno) throws Exception{
		return replyDAO.selectReply(rno);
	}
	
	public int removeReply(int rno, String replyer) throws SQLException {
		return replyDAO.deleteReply(rno, replyer);
	}
	
	public int modifyReply(Reply reply, String replyer) throws SQLException {
		return replyDAO.updateReply(reply, replyer);
	}
	
	public int addReply(Reply reply) throws SQLException {
		return replyDAO.insertReply(reply);
	}

	@Override
	public String toString() {
		return "ReplyService [replyDAO=" + replyDAO + "]";
	}
	
	
}
