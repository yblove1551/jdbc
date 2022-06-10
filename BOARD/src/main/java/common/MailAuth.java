package common;

import java.util.Objects;

public class MailAuth {
	private String email;
	private Integer authNumber;
	private Long createTime;
	
	public MailAuth(String email, Integer authNumber, Long createTime) {
		this.email = email;
		this.authNumber = authNumber;
		this.createTime = createTime;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getAuthNumber() {
		return authNumber;
	}
	public void setAuthNumber(Integer authNumber) {
		this.authNumber = authNumber;
	}
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	@Override
	public int hashCode() {
		return Objects.hash(authNumber, email);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MailAuth other = (MailAuth) obj;
		return Objects.equals(authNumber, other.authNumber) && Objects.equals(email, other.email);
	}	
}
