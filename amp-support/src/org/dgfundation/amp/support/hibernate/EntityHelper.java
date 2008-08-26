package org.dgfundation.amp.support.hibernate;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dgfundation.amp.support.dbentity.AmpModules;
import org.dgfundation.amp.support.dbentity.Country;
import org.dgfundation.amp.support.dbentity.Login;
import org.dgfundation.amp.support.dbentity.RequestSupport;
import org.dgfundation.amp.support.dto.ModuleDto;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

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
		return (Country) session.load(Country.class, code);

	}

	/**
	 * 
	 * @return a List with all Amp moudules
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
}
