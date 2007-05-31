package org.dgfoundation.amp; 

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.swarmcache.ObjectCache;

import org.apache.log4j.Logger;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.DigiCacheManager;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.editor.exception.EditorException;

public final class Util {

	protected static Logger logger = Logger.getLogger(Util.class);

	/**
	 * Returns a set with objects from the "source" collection, which were identified by "selected" id
	 * @param source
	 * @param selected an object whose toString property returns the id of the selected object
	 * @return the set
	 * @see getSelectedObjects method
	 */
	public static Identifiable getSelectedObject(Collection source, Object selected) {
		Set ret=getSelectedObjects(source, new Object[] {selected});
		if(ret.size()==0) return null;
		return (Identifiable) ret.iterator().next();
	}
	
	/**
	 * Returns a set with objects from the "source" collection, which were identified by Ids present in the "selected" array
	 * @param source a Collection of Identifiable objects
	 * @param selected an array of objects whose toString property returns the Id of the selected object
	 * @return a set with objects
	 * @see Identifiable
	 */
	public static Set getSelectedObjects(Collection source, Object[] selected) {
		Set ret = new HashSet();
		if(selected==null) return ret;
		Iterator i = source.iterator();
		while (i.hasNext()) {
			Identifiable element = (Identifiable) i.next();
			for (int k = 0; k < selected.length; k++)
				if (element.getIdentifier().equals(
						new Long(selected[k].toString())))
					ret.add(element);
		}

		return ret;

	}
	
	public static String getEditorBody(Site site,String key,  Locale navLang)
			throws EditorException {
		String editorBody = null;
		
		Session session=null;
		try {
			session = PersistenceManager.getSession();

			Query q = session
					.createQuery("select e.body,e.language from "
							+ (org.digijava.module.editor.dbentity.Editor.class)
									.getName()
							+ " e "
							+ " where (e.siteId=:siteId) and (e.editorKey=:editorKey)");
	          q.setParameter("siteId", site.getSiteId(), Hibernate.STRING);
	          q.setParameter("editorKey", key, Hibernate.STRING);
	          List result = q.list();
	          if(result.size()==0) return "";
	          Iterator i=result.iterator();
	          Object[] res=null;
	          while (i.hasNext()) {
				 res= (Object[]) i.next();
				if(res[1].equals(navLang.getCode())) editorBody=(String) res[0];				
			}
	         if(editorBody==null) editorBody=(String) res[0];
	          

		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();			
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed ");
			}
		}

		return editorBody;
	}
	
	
    

	/**
	 * Return a list of AmpOrganisationS that match the specified role
	 * 
	 * @param orgRoles
	 *            the AmpOrgRole collection with org-role mappings
	 * @param roleCode
	 *            the role code
	 * @return the list of AmpOrgs with that role
	 * @author mihai 06.05.2007
	 */
	public static Collection getOrgsByRole(Collection orgRoles, String roleCode) {
		ArrayList ret = new ArrayList();
		Iterator i = orgRoles.iterator();
		while (i.hasNext()) {
			AmpOrgRole element = (AmpOrgRole) i.next();
			if (element.getRole().getRoleCode().equals(roleCode))
				ret.add(element.getOrganisation());
		}
		return ret;
	}

	/**
	 * Returns comma separated view string representation of the collection
	 * items
	 * 
	 * @param col
	 *            the collectoin
	 * @return the comma separated string
	 * @author mihai 06.05.2007
	 */
	public static String toCSString(Collection col) {
		String ret = "";
		if (col == null || col.size() == 0)
			return ret;
		Iterator i = col.iterator();
		while (i.hasNext()) {
			Object element = (Object) i.next();
			if (element == null)
				continue;
			if (element instanceof String)
				ret += "'" + (String) element + "'";
			else
				ret += element.toString();
			if (i.hasNext())
				ret += ",";
		}
		return ret;
	}

	/**
	 * QUICK access to exchange rates. Gets the exchange rate for the given
	 * currency at the specified date. The exchange rate is fetched directy from
	 * the DB using a stored function. Afterwards it is stored inside the digi
	 * cache for later usage so we do not need to access the DB again. All the
	 * logic involving exchange rate calculation is done at the DB level,
	 * therefore the result is returned very quickly.
	 * 
	 * @param currency
	 *            the currency code
	 * @param currencyDate
	 *            the currency date
	 * @return the exchange rate
	 * @author mihai 06.05.2007
	 */
	public static double getExchange(String currency, java.sql.Date currencyDate) {
		Connection conn = null;
		double ret = 1;

		// we try the digi cache:
		ObjectCache ratesCache = DigiCacheManager.getInstance().getCache(
				"EXCHANGE_RATES_CACHE");

		Double cacheRet = (Double) ratesCache.get(new String(currency
				+ currencyDate));
		if (cacheRet != null)
			return cacheRet.doubleValue();
		Session sess = null;
		try {
			sess = PersistenceManager.getSession();
			conn = sess.connection();
		} catch (HibernateException e) {
			logger.error(e);
			e.printStackTrace();
		} catch (SQLException e) {
			logger.error(e);
			e.printStackTrace();
		}

		String query = "SELECT getExchange(?,?)";
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, currency);
			ps.setDate(2, currencyDate);

			ResultSet rs = ps.executeQuery();

			if (rs.next())
				ret = rs.getDouble(1);

			rs.close();

		} catch (SQLException e) {
			logger.error("Unable to get exchange rate for currencty "
					+ currency + " for the date " + currencyDate);
			logger.error(e);
			e.printStackTrace();
		}

		try {
			sess.close();
		} catch (HibernateException e) {
			logger.error(e);
			e.printStackTrace();
		}

		logger.info("rate for " + currency + " to USD on " + currencyDate
				+ " is " + ret);
		if (ret != 1)
			ratesCache
					.put(new String(currency + currencyDate), new Double(ret));

		return ret;

	}

	public static SimpleDateFormat dateFormat = new SimpleDateFormat(
	"yyyy-MM-dd");
}
