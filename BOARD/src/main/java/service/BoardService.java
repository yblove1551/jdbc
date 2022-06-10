package service;
import java.sql.SQLException;
import java.util.List;

import common.Autowired;
import common.Bean;
import dao.BoardDAO;
import dao.ReplyDAO;
import domain.Board;
import domain.BoardSelector;
@Bean
public class BoardService { 
	BoardDAO boardDAO;
	ReplyDAO replyDAO;

	@Autowired
	public BoardService(BoardDAO boardDAO, ReplyDAO replyDAO ) {
		this.boardDAO = boardDAO;
		this.replyDAO = replyDAO;
	}
	
	public int selectBoardCnt(BoardSelector bs) throws SQLException{
		return boardDAO.selectBoardCnt(bs);	
	}
	
	public Board selectBoard(int bno) throws SQLException{
		boardDAO.updateViewCnt(bno);
		return boardDAO.selectBoard(bno);
	}
	
	public List<Board> selectBoardList(BoardSelector selector) throws SQLException{
		return boardDAO.boardList(selector);
	}
	
	public int removeBoard(String writer, int bno) throws SQLException{
		int result = boardDAO.deleteBoard(writer, bno);
		if (result != 0) replyDAO.deleteByBno(bno);
		return result;
	}
	
	public int modifyBoard(Board board) throws SQLException {
		return boardDAO.updateBoard(board);
	}
	
	public int modifyViewCnt(int view_cnt) throws SQLException{
		return boardDAO.updateViewCnt(view_cnt);
	}

	public int addBoard(Board board) throws SQLException {
		return boardDAO.insertBoard(board);
	}

	@Override
	public String toString() {
		return "BoardService [boardDAO=" + boardDAO + ", replyDAO=" + replyDAO + "]";
	}
	
	
	
}
