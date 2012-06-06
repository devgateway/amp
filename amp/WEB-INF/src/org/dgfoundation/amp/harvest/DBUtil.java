/**
 * DBUtil.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.harvest;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpExternalMapping;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Aug 8, 2006
 *
 */
public class DBUtil {

	/**
	 * Creates a Map object binding internal (AMP) and external ids for similar objects. 
	 * This can be used by harvesters/publishers.
	 * @param externalSource the source of the externalIds that are mapped by this map
	 * @param objectType the type of the object Id for the objects the returned map will hold
	 * @param inverse true if externalId is put as the map Key
	 * @return the generated Map object
	 * @see org.digijava.module.aim.dbentity.AmpExternalMapping
	 */
	public static Map getExternalMapping(String externalSource,String objectType, boolean inverse) {
		HashMap ret=new HashMap();
		try {
			Session session = PersistenceManager.getSession();
			
			String queryString = "select a from "
				+ AmpExternalMapping.class.getName() + " a "
				+ "WHERE (a.objectType=:objType) and (a.externalSource=:extSource)";
			Query qry = session.createQuery(queryString);
			PersistenceManager.releaseSession(session);
			qry.setParameter("objType", objectType, Hibernate.STRING);
			qry.setParameter("extSource", externalSource, Hibernate.STRING);
	
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				AmpExternalMapping mapping = (AmpExternalMapping) itr.next();
				if (inverse) ret.put(mapping.getExternalId(),mapping.getAmpId()); 
				else ret.put(mapping.getAmpId(),mapping.getExternalId());
			}
		
			
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ret;
		
		
	}
	
	public static void saveMapping(AmpExternalMapping obj) {
		try {

		Session session = PersistenceManager.getSession();
//beginTransaction();
		
			session.saveOrUpdate(obj);
		PersistenceManager.releaseSession(session);
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
