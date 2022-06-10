package service;

import java.sql.SQLException;

import common.Autowired;
import common.Bean;
import dao.MailDAO;
import domain.Mail;
@Bean
public class MailService {
	MailDAO mailDAO;
	
	@Autowired
	public MailService(MailDAO mailDAO) {
		this.mailDAO = mailDAO;
	}

	public int insertMailLog(Mail mail) throws SQLException {
		return mailDAO.insertMailLog(mail);
	}

	@Override
	public String toString() {
		return "MailService [mailDAO=" + mailDAO + "]";
	}
	
	
}
