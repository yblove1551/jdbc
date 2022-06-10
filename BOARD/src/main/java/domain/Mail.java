package domain;

import java.util.Date;

public class Mail {
	private int seq;
	private int kind;
	private String sender;
	private String sender_email;
	private String receiver;
	private String receiver_email;
	private String title;
	private String content;
	private Date send_time;
	private String success;
	private String err_msg;
		
	@Override
	public String toString() {
		return "Mail [seq=" + seq + ", kind=" + kind + ", sender=" + sender + ", sender_email=" + sender_email
				+ ", receiver=" + receiver + ", receiver_email=" + receiver_email + ", title=" + title + ", content="
				+ content + ", send_time=" + send_time + ", success=" + success + "]";
	}
	
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public int getKind() {
		return kind;
	}
	public void setKind(int kind) {
		this.kind = kind;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getSender_email() {
		return sender_email;
	}
	public void setSender_email(String sender_email) {
		this.sender_email = sender_email;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getReceiver_email() {
		return receiver_email;
	}
	public void setReceiver_email(String receiver_email) {
		this.receiver_email = receiver_email;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getSend_time() {
		return send_time;
	}
	public void setSend_time(Date send_time) {
		this.send_time = send_time;
	}
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}

	public String getErr_msg() {
		return err_msg;
	}

	public void setErr_msg(String err_msg) {
		this.err_msg = err_msg;
	}
	
	
	
}
