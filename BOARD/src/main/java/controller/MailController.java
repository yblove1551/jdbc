package controller;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.AppContext;
import common.Autowired;
import common.MailAuth;
import domain.Mail;
import domain.MailTemplete;
import service.MailService;
import service.MailTempleteService;

//웹브라우저에서 메일 관련 요청을 처리하는 컨트롤러

@WebServlet("/mail/*")
public class MailController extends HttpServlet {
	@Autowired
	MailTempleteService mailTempleteService;
	@Autowired
	MailService mailService;
	
	private Set<MailAuth> mailAuthSet = new HashSet<>();
		
	@Override
	public void init() throws ServletException {
		AppContext app = (AppContext)getServletContext().getAttribute("appContext");
		try {
			app.servletAutowired(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException{
		String doUrl = request.getRequestURI().replace(request.getContextPath(), "");	
		String responseUrl = "";	
		PrintWriter out = response.getWriter();
		
		
		if (doUrl.equals("/mail/mail")) { //메일발송
			try {

				int kind = Integer.parseInt(request.getParameter("kind"));
				MailTemplete mailTemplete = mailTempleteService.selectByKind(kind);			
			//	Mail mail = setMailContent(mailTemplete, new Mail(), request.getParameterMap());
			
				Mail mail = setMailContent(mailTemplete, new Mail(), request.getParameterMap());
				try {
					sendMail(mail);	
					mail.setSuccess("Y");
				}catch(Exception e) {
					e.printStackTrace();
					mail.setSuccess("N");						
				}
								
				try {
					mailService.insertMailLog(mail);	
				}catch(Exception e){
					e.printStackTrace();
				}
				
				if (mail.getSuccess().equals("Y")) 	
					out.write("{\"result\":\"OK\"}");			
				else
					out.write("{\"result\":\"NO\"}");
					
			} catch (Exception e) {
				response.setStatus(500);
				out.write("{\"result\":\"ERROR\"}");
			}	
		}else if (doUrl.equals("/mail/auth")){
			try {
				int authNumber = Integer.parseInt(request.getParameter("authNumber"));
				String email = request.getParameter("email");
				
				if (!findByEmailAndAuthNum(email, authNumber)) {
					out.write("{\"result\":\"INVALID\"}");
					return;
				}			
				removeByEmail(email);
				out.write("{\"result\":\"OK\"}");
			}catch(Exception e){
				response.setStatus(500);
				out.write("{\"result\":\"ERROR\"}");
			}
		}		
	}
	
	public void sendMail(Mail mail) throws Exception{
		Properties p = new Properties();
		try {
			p.load(new FileReader(new File("c:\\mailset.properties")));
			System.out.println(p);
		} catch (Exception e) {
			throw new Exception("메일설정파일을 찾을 수 없습니다");
		}
						
		String email = p.getProperty("email");
		String password = p.getProperty("password");
		String name = p.getProperty("name");

		
		mail.setSender(name);
		mail.setSender_email(email);
		
		Session session = Session.getDefaultInstance(p, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() { 
				return new PasswordAuthentication(email, password); 
			} 
		});	

	    try {
	    	//메시지 설정
	        MimeMessage message = new MimeMessage(session);
	        
			message.setFrom(new InternetAddress(mail.getSender_email(), mail.getSender(), "UTF-8")); //보낸이 이메일, 보낸이 명
	        message.addRecipient(Message.RecipientType.TO, new InternetAddress(mail.getReceiver_email())); //수신인 이메일
	        message.setSubject(mail.getTitle(), "UTF-8"); //메일 제목
	        message.setText(mail.getContent(), "UTF-8");    //메일 내용
	        Transport.send(message); ////전송
	    } catch (AddressException e) {
	    	//이메일 주소가 잘못된경우
	        e.printStackTrace();
	        throw e;
	    } catch (MessagingException e) {
	    	//메시지 전송 에러
	        e.printStackTrace();
	        throw e;
	    }
	
	}
	
	public Integer add(String email) {
		//인증번호생성
		int authNum = -1;
		
		Set<Integer> authNumSet = mailAuthSet.stream().map(MailAuth::getAuthNumber).collect(Collectors.toSet());   
		while(true) {
			authNum = (int)(Math.random() * 9000 + 1000);
			if (!authNumSet.contains(authNum))
				break;
		}
		//맵에추가
		mailAuthSet.add(new MailAuth(email, authNum, System.currentTimeMillis()));
		//랜덤생성 값 리턴
		return authNum;
	}
	
	public void removeTimeOut() {
		Long currentTime = System.currentTimeMillis(); 
		mailAuthSet.removeIf(s -> currentTime - s.getCreateTime() > 1000 * 60 * 5); //5분	
	}
	
	public void removeByEmail(String email) {
		mailAuthSet.removeIf(s -> s.getEmail().equals(email));
	}
	
	public boolean findByEmailAndAuthNum(String email, int authNum) {
		return mailAuthSet.contains(new MailAuth(email, authNum, 0L));
	}
	
	public Mail setMailContent(MailTemplete temp, Mail mail, Map<String, String[]> inputMap) {
		mail.setKind(temp.getKind()); //메일타입
		mail.setTitle(temp.getTitle()); //메일제목
		mail.setContent(temp.getContent()); //메일내용
  		mail.setReceiver(String.valueOf(inputMap.get("name")[0])); //받는사람
		mail.setReceiver_email(String.valueOf(inputMap.get("email")[0])); //받는사람이메일
		mail.setSuccess("N");
		
		if (temp.getKind() == 3)
			mail.setContent(mail.getContent().replaceAll("%id", String.valueOf(inputMap.get("id")[0])));
		else
			mail.setContent(mail.getContent().replaceAll("%authNumber", String.valueOf(add(String.valueOf(inputMap.get("email")[0])))));
		
		
		//%name, %id등 메일 템플릿의 키워드를 입력값으로 변환
		inputMap.forEach((s1, s2) -> {
			mail.setTitle(mail.getTitle().replaceAll("%"+String.valueOf(s1), String.valueOf(s2[0])));
			mail.setContent(mail.getContent().replaceAll("%"+String.valueOf(s1), String.valueOf(s2[0])));
		});
		
		return mail;
	}

	@Override
	public String toString() {
		return "MailController [mailTempleteService=" + mailTempleteService + ", mailService=" + mailService
				+ ", mailAuthSet=" + mailAuthSet + "]";
	}
		
	
	
}
