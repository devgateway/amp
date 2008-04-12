package org.dgfundation.amp.support.dbentity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "USER_LOGON")
public class Login {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long loginid;

	@Column(name = "USER_NAME")
	private String username;

	@Column(name = "USER_PASSWORD")
	private String password;

	@OneToOne(targetEntity = Country.class)
	@JoinColumn(name = "COUNTRY_CODE")
	private Country country;

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public Long getLoginid() {
		return loginid;
	}

	public void setLoginid(Long loginid) {
		this.loginid = loginid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
