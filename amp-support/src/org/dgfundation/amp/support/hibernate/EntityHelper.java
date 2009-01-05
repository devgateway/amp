package org.dgfundation.amp.support.hibernate;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.dgfundation.amp.support.dbentity.AmpModules;
import org.dgfundation.amp.support.dbentity.AmpVersion;
import org.dgfundation.amp.support.dbentity.Country;
import org.dgfundation.amp.support.dbentity.Login;
import org.dgfundation.amp.support.dbentity.OperatingSystem;
import org.dgfundation.amp.support.dbentity.RequestSupport;
import org.dgfundation.amp.support.dto.ModuleDto;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import sun.util.logging.resources.logging;

/**
 * 
 * @author Diego Dimunzio
 *
 */

public class EntityHelper {
	private static Logger log = Logger.getLogger(EntityHelper.class.getName());

	/**
	 * @param request
	 * @throws Exception
	 */
	public static RequestSupport saveRequest(RequestSupport request)
			throws Exception {
		Session session = HibernateUtil.getSession();
		try {
			Transaction trans = session.beginTransaction();
			session.save(request);
			trans.commit();
			return request;
		} catch (Exception e) {
			log.error(e);
			throw (e);
		}
	}
	/**
	 * 
	 * @param user
	 * @return
	 */
	public static Collection<RequestSupport> getRequestsbyUser(long user) {
		Session session = HibernateUtil.getSession();
		String sqlquery;
		sqlquery = "select l from "
				+ RequestSupport.class.getName()
				+ " l where l.login=:pid ";
		Query qry = session.createQuery(sqlquery);
		qry.setLong("pid", user);
		List result = qry.list();
		return result;
	}
	
	/**
	 * @param username
	 * @param password
	 * @param country
	 * @return Login Object
	 */
	public static Login getLogin(String username, String password,
			Country country) {
		Session session = HibernateUtil.getSession();
		String sqlquery;
		sqlquery = "select l from "
				+ Login.class.getName()
				+ " l where l.username=:pname  and l.password=:ppasword and l.country=:pcountrycode";
		Query qry = session.createQuery(sqlquery);
		qry.setParameter("pname", username);
		qry.setParameter("ppasword", password);
		qry.setParameter("pcountrycode", country);
		List result = qry.list();
		if (result.size() == 0) {
			return null;
		} else {
			return (Login) result.get(0);
		}
	}
	
	/**
	 *
	 */
	public static Collection<Login> getAllUsers() {
		Session session = HibernateUtil.getSession();
		String sqlquery;
		sqlquery = " from " + Login.class.getName();
		Query qry = session.createQuery(sqlquery);
		List result = qry.list();
		return result;
	}
	
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static Login getLoginById(long id){
		Session session = HibernateUtil.getSession();
		String sqlquery;
		sqlquery = "select l from "
				+ Login.class.getName()
				+ " l where l.loginid=:pid ";
		Query qry = session.createQuery(sqlquery);
		qry.setLong("pid", id);
		List result = qry.list();
		return (Login) result.get(0);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static AmpVersion getVersionbyId(long id){
		Session session = HibernateUtil.getSession();
		return (AmpVersion) session.get(AmpVersion.class, id);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static OperatingSystem getOsId(long id){
		Session session = HibernateUtil.getSession();
		return (OperatingSystem) session.get(OperatingSystem.class, id);
	}
	
	
	/**
	 * 
	 * @param countryCode
	 * @return
	 */
	public static Login getLoginbyCountry(Country country) {
		Session session = HibernateUtil.getSession();
		String sqlquery;
		sqlquery = "select l from "
				+ Login.class.getName()
				+ " l where l.country=:pcountry";
		try {
			Query qry = session.createQuery(sqlquery);
			qry.setParameter("pcountry", country);
			List result = qry.list();
			if (result.size() == 0) {
				return null;
			} else {
				return (Login) result.get(0);
			}	
		} catch (Exception e) {
			System.out.print(e);
		}
		return null;
	}
	
	/**
	 * 
	 * @param country object
	 * @param loging object
	 */
	public static void addLogin(Login l){
		Session session =HibernateUtil.getSession();
		Transaction trans = session.beginTransaction();
		Login tml = getLoginbyCountry(l.getCountry());
		if (tml == null){
			session.save(l);
		}else{
			l.setLoginid(tml.getLoginid());
			session.evict(tml);
			session.update(l);
			}
		trans.commit();
	}
	
	/**
	 * 
	 * @param loging object
	 */
	public static void deleteLogin(Login l){
		Session session =HibernateUtil.getSession();
		session.beginTransaction();
		Collection<RequestSupport> rlist = getRequestsbyUser(l.getLoginid());
		if (rlist.size()>0){
			for (Iterator iterator = rlist.iterator(); iterator.hasNext();) {
				RequestSupport requestSupport = (RequestSupport) iterator.next();
				session.delete(requestSupport);
			}
		}
		session.delete(l);
		session.getTransaction().commit();
	}
	
	/**
	 * @return a List with all countries
	 */
	public static List<Country> getAllCountries() {
		Session session = HibernateUtil.getSession();
		String sqlquery;
		sqlquery = " from " + Country.class.getName();
		Query qry = session.createQuery(sqlquery);
		List result = qry.list();
		return result;
	}

	/**
	 * @param code
	 * @return Country - Especific country filter by code
	 */
	public static Country getCountry(String code) {
		Session session = HibernateUtil.getSession();
		String sqlquery;
		sqlquery = "select l from "
				+ Country.class.getName()
				+ " l where l.code=:pcode ";
		Query qry = session.createQuery(sqlquery);
		qry.setParameter("pcode",code);
		List result = qry.list();
		if (result.size()>0){
			return (Country) result.get(0);
		}
		return null;
	}
	
	/**
	 * 
	 * @param country
	 */
	public static void addCountry(Country c){
		Session session =HibernateUtil.getSession();
		Transaction trans = session.beginTransaction();
		 Country tmpc = (Country) session.get(Country.class, c.getCode());
		if (tmpc == null){
			session.save(c);
		}else{
			session.evict(tmpc);
			session.update(c);
			}
		trans.commit();
	}
	
	/**
	 * Delete the country and relations in cascade (login and request support tables)
	 * @param country
	 */
	public static void deleteContry(Country country){
		Session session =HibernateUtil.getSession();
		session.beginTransaction();
		Login loging = getLoginbyCountry(country);
		if (loging!=null){
			Collection<RequestSupport> rlist = getRequestsbyUser(loging.getLoginid());
			if (rlist.size()>0){
				for (Iterator iterator = rlist.iterator(); iterator.hasNext();) {
					RequestSupport requestSupport = (RequestSupport) iterator.next();
					session.delete(requestSupport);
				}
			}
			session.delete(loging);
		}
		session.delete(country);
		session.getTransaction().commit();
	}
	/**
	 * 
	 * @return a List with all Amp modules
	 */
	public static List<ModuleDto> getMoudules(String locale) {
		try {

			Session session = HibernateUtil.getSession();
			String sqlquery;
			sqlquery = " from " + AmpModules.class.getName();
			Query qry = session.createQuery(sqlquery);

			List<AmpModules> result =qry.list();
			ArrayList<ModuleDto> listDto = new ArrayList<ModuleDto>(result
					.size());
			for (AmpModules modules : result) {
				String name = "";
				Method m = AmpModules.class.getMethod("getName_" + locale, null);
				name = (String) m.invoke(modules, null);
				ModuleDto mdt = new ModuleDto(modules.getIdmodule(), name);
				listDto.add(mdt);
			}

			return listDto;

		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static AmpModules getModById(Long id){
		Session session = HibernateUtil.getSession();
		return (AmpModules) session.get(AmpModules.class, id);
	}
	/**
	 * 
	 * @return a List with all Amp modules
	 */
	public static List<AmpModules> getAllMoudules() {
		try {
			Session session = HibernateUtil.getSession();
			String sqlquery;
			sqlquery = " from " + AmpModules.class.getName();
			Query qry = session.createQuery(sqlquery);

			List<AmpModules> result =qry.list();
			return result;

		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 
	 * @param module
	 */
	public static void addModule(AmpModules module){
		Session session =HibernateUtil.getSession();
		session.beginTransaction();
		try {
			session.save(module);
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
		}
	}
	
	/**
	 * 
	 * @param ampversion
	 */
	public static void addVersion(AmpVersion v){
		Session session =HibernateUtil.getSession();
		session.beginTransaction();
		try {
			session.save(v);
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
		}
	}
	
	public static void addOs(OperatingSystem o){
		Session session =HibernateUtil.getSession();
		session.beginTransaction();
		try {
			session.save(o);
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
		}
	}
	
	/**
	 * 
	 * @param module
	 */
	public static void deleteModule(AmpModules module){
		Session session =HibernateUtil.getSession();
		session.beginTransaction();
		session.delete(module);
		session.getTransaction().commit();
	}
	
	/**
	 * 
	 * @param ampversion
	 */
	public static void deleteVersion(AmpVersion v){
		Session session =HibernateUtil.getSession();
		session.beginTransaction();
		session.delete(v);
		session.getTransaction().commit();
	}
	
	/**
	 * 
	 * @param os
	 */
	public static void deleteOs(OperatingSystem o){
		Session session =HibernateUtil.getSession();
		session.beginTransaction();
		session.delete(o);
		session.getTransaction().commit();
	}
	/**
	 * 
	 * @return a List with all OS
	 */
	public static List<OperatingSystem> getOs() {
		try {
			Session session = HibernateUtil.getSession();
			String sqlquery;
			sqlquery = " from " + OperatingSystem.class.getName();
			Query qry = session.createQuery(sqlquery);

			List result =qry.list();
			return result;

		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 
	 * @return a List with all AMPVersions
	 */
	public static List<AmpVersion> getAmpVersion() {
		try {
			Session session = HibernateUtil.getSession();
			String sqlquery;
			sqlquery = " from " + AmpVersion.class.getName();
			Query qry = session.createQuery(sqlquery);

			List result =qry.list();
			return result;

		} catch (Exception e) {
			return null;
		}
	}
}
