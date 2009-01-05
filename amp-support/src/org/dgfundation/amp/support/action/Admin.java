package org.dgfundation.amp.support.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.dgfundation.amp.support.dbentity.AmpModules;
import org.dgfundation.amp.support.dbentity.AmpVersion;
import org.dgfundation.amp.support.dbentity.Country;
import org.dgfundation.amp.support.dbentity.Languages;
import org.dgfundation.amp.support.dbentity.Login;
import org.dgfundation.amp.support.dbentity.OperatingSystem;
import org.dgfundation.amp.support.dbentity.UserRoles;
import org.dgfundation.amp.support.hibernate.EntityHelper;

import sun.util.logging.resources.logging;

import com.opensymphony.xwork2.ActionSupport;

public class Admin extends ActionSupport implements ServletRequestAware{

	private static final long serialVersionUID = 5499126656626123250L;
	
	private HttpServletRequest request;
	private Collection<Country> Countries;
	private Collection<Login> users;
	private Collection<AmpModules> modules;
	private Collection<AmpVersion> versions;
	private Collection<OperatingSystem> os;
	private String contryname;
	private String countrycode;
	private String countrymail;
	private String userpassword;
	private String usernname;
	private int userrole;
	private String userccode;
	private String enname;
	private String esname;
	private String frname;
	private String vname;
	private String osname;
	
	public String getEnname() {
		return enname;
	}

	public void setEnname(String enname) {
		this.enname = enname;
	}

	public String getEsname() {
		return esname;
	}

	public void setEsname(String esname) {
		this.esname = esname;
	}

	public String getFrname() {
		return frname;
	}

	public void setFrname(String frname) {
		this.frname = frname;
	}

	public String getUserccode() {
		return userccode;
	}

	public void setUserccode(String userccode) {
		this.userccode = userccode;
	}

	public List<UserRoles> getRoles() {
		List<UserRoles> roleslist = new ArrayList<UserRoles>();
		roleslist.add(new UserRoles("User",1));
		roleslist.add(new UserRoles("Admin",2));
		return roleslist;
	}
	
	public Collection<AmpModules> getModules() {
		return modules;
	}

	public void setModules(Collection<AmpModules> modules) {
		this.modules = modules;
	}

	public Collection<Login> getUsers() {
		return users;
	}

	public String getContryname() {
		return contryname;
	}

	public void setContryname(String contryname) {
		this.contryname = contryname;
	}

	public String getCountrycode() {
		return countrycode;
	}

	public void setCountrycode(String countrycode) {
		this.countrycode = countrycode;
	}

	public String getCountrymail() {
		return countrymail;
	}

	public void setCountrymail(String countrymail) {
		this.countrymail = countrymail;
	}

	public void setUsers(Collection<Login> users) {
		this.users = users;
	}

	public String execute() throws Exception {
		this.Countries = EntityHelper.getAllCountries();
		this.users = EntityHelper.getAllUsers();
		this.modules = EntityHelper.getAllMoudules();
		this.versions = EntityHelper.getAmpVersion();
		this.os = EntityHelper.getOs();
		return "admin";
	}
	
	public void setServletRequest(HttpServletRequest arg) {
		this.request = arg;
		
	}

	public Collection<Country> getCountries() {
		return Countries;
	}

	public void setCountries(Collection<Country> countries) {
		Countries = countries;
	}
	
	public String DeleteCountry(){
		String ccode = (String) request.getParameter("ccode");
		EntityHelper.deleteContry(EntityHelper.getCountry(ccode));
		return "isadmin";
	}
	
	public String DeleteUser(){
		Long id = Long.parseLong(request.getParameter("id"));
		EntityHelper.deleteLogin(EntityHelper.getLoginById(id));
		return "isadmin";
	}
	
	public String DeleteModule(){
		long id = Long.parseLong(request.getParameter("idmodule"));
		EntityHelper.deleteModule(EntityHelper.getModById(id));
		return "isadmin";
	}
	
	public String DeleteVersion(){
		long id = Long.parseLong(request.getParameter("idversion"));
		EntityHelper.deleteVersion(EntityHelper.getVersionbyId(id));
		return "isadmin";
	}
	
	public String DeleteOs(){
		long id = Long.parseLong(request.getParameter("idos"));
		EntityHelper.deleteOs(EntityHelper.getOsId(id));
		return "isadmin";
	}
	
	public String addUser(){
		Login newuser = new Login();
		newuser.setCountry(EntityHelper.getCountry(this.userccode));
		newuser.setPassword(this.userpassword);
		newuser.setRole(this.userrole);
		newuser.setUsername(this.usernname);
		EntityHelper.addLogin(newuser);
		return "isadmin";
	}
	
	public String addCountry(){
		Country newcontry = new Country();
		newcontry.setCode(this.countrycode);
		newcontry.setName(this.contryname);
		newcontry.setMail(this.countrymail);
		EntityHelper.addCountry(newcontry);
		return "isadmin";
	}
	
	public String addModule(){
		AmpModules module = new AmpModules();
		module.setName_en(enname);
		module.setName_es(esname);
		module.setName_fr(frname);
		EntityHelper.addModule(module);
		return "isadmin";
	}
	
	public String addVersion(){
		AmpVersion v = new AmpVersion();
		v.setName(this.vname);
		EntityHelper.addVersion(v);
		return "isadmin";
	}
	
	public String addOs(){
		OperatingSystem o = new OperatingSystem();
		o.setName(this.osname);
		EntityHelper.addOs(o);
		return "isadmin";
	}
	
	public int getUserrole() {
		return userrole;
	}

	public void setUserrole(int userrole) {
		this.userrole = userrole;
	}

	public String getUserpassword() {
		return userpassword;
	}

	public void setUserpassword(String userpassword) {
		this.userpassword = userpassword;
	}

	public String getUsernname() {
		return usernname;
	}

	public void setUsernname(String usernname) {
		this.usernname = usernname;
	}

	public Collection<AmpVersion> getVersions() {
		return versions;
	}

	public void setVersions(Collection<AmpVersion> versions) {
		this.versions = versions;
	}

	public Collection<OperatingSystem> getOs() {
		return os;
	}

	public void setOs(Collection<OperatingSystem> os) {
		this.os = os;
	}

	public String getVname() {
		return vname;
	}

	public void setVname(String vname) {
		this.vname = vname;
	}

	public String getOsname() {
		return osname;
	}

	public void setOsname(String osname) {
		this.osname = osname;
	}
	
}
