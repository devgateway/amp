package org.dgfundation.amp.support.dbentity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "REQUEST")
public class RequestSupport {
	@Id
	@Column(name = "REQUEST_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@OneToOne(targetEntity = Login.class)
	@JoinColumn(name = "LOGIN_ID")
	private Login login;

	@Column(name = "DATE")
	private Date date;

	@OneToOne(targetEntity = Country.class)
	@JoinColumn(name = "COUNTRY_CODE")
	private Country country;

	@Column(name = "FULLUSERNAME")
	private String fullusername;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "BROWSER")
	private String browser;

	@Column(name = "BROWSERVERSION")
	private String browserversion;

	@Column(name = "OPERATINGSYSTEM")
	private String operatingsystem;

	@Column(name = "MODULE")
	private String module;

	@Column(name = "VERSION")
	private String version;

	@Column(name = "SUBJECT")
	private String subject;

	@Column(name = "DETAILS", length = 20000)
	private String details;

	@Column(name = "AMP_LOGIN")
	private String amplogin;

	@Column(name = "AMP_PASSWORD")
	private String amppassword;

	@Column(name = "MAIL_CC")
	private String mailcc;

	@Column(name = "LEVEL")
	private String level;

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public String getBrowserversion() {
		return browserversion;
	}

	public void setBrowserversion(String browserversion) {
		this.browserversion = browserversion;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullusername() {
		return fullusername;
	}

	public void setFullusername(String fullusername) {
		this.fullusername = fullusername;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Login getLogin() {
		return login;
	}

	public void setLogin(Login login) {
		this.login = login;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getOperatingsystem() {
		return operatingsystem;
	}

	public void setOperatingsystem(String operatingsystem) {
		this.operatingsystem = operatingsystem;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getAmplogin() {
		return amplogin;
	}

	public void setAmplogin(String amplogin) {
		this.amplogin = amplogin;
	}

	public String getAmppassword() {
		return amppassword;
	}

	public void setAmppassword(String amppassword) {
		this.amppassword = amppassword;
	}

	public String getMailcc() {
		return mailcc;
	}

	public void setMailcc(String mailcc) {
		this.mailcc = mailcc;
	}
}
