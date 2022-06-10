package domain;

import java.util.Date;
import java.util.Objects;

import common.BindTarget;
@BindTarget
public class User {
	private String id;
	private String pwd;
	private String name;
	private String email;
	private Date birth;
	private String phone;
	private Date reg_date;
	
	public User() {}
	
	public User(String id, String pwd, String name, String email, Date birth, String phone, Date reg_date) {
		this.id = id;
		this.pwd = pwd;
		this.name= name;
		this.email = email;
		this.birth = birth;
		this.phone = phone;
		this.reg_date = reg_date;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(email, id, name, phone, pwd);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(email, other.email) && Objects.equals(id, other.id)
				&& Objects.equals(name, other.name) && Objects.equals(phone, other.phone)
				&& Objects.equals(pwd, other.pwd);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getReg_date() {
		return reg_date;
	}

	public void setReg_date(Date reg_date) {
		this.reg_date = reg_date;
	}
	
	
}
