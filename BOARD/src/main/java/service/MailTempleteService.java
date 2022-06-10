package service;

import java.sql.SQLException;
import java.util.List;

import common.Autowired;
import common.Bean;
import dao.MailTempleteDAO;
import domain.MailTemplete;
@Bean
public class MailTempleteService {
	MailTempleteDAO mailTempleteDAO; 
	
	@Autowired
	public MailTempleteService(MailTempleteDAO mailTempleteDAO) {
		this.mailTempleteDAO = mailTempleteDAO;
	}

	public MailTemplete selectByKind(int kind) throws SQLException{
		return mailTempleteDAO.selectByKind(kind);
	}
		
	public List<MailTemplete> selectAll() throws SQLException{
		return mailTempleteDAO.selectAll();
	}

	@Override
	public String toString() {
		return "MailTempleteService [mailTempleteDAO=" + mailTempleteDAO + "]";
	}
	
	
}
