/**
 *
 */
package org.digijava.module.aim.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.EUActivity;
import org.digijava.module.aim.exception.AimException;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * @author mihai
 *
 */
public final class EUActivityUtil {
	private static Logger logger = Logger.getLogger(EUActivityUtil.class);

	public static Collection<EUActivity> getEUActivities(Long actId) {
		Session session = null;
		Collection<EUActivity> euActivities = new ArrayList<EUActivity>();

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select eu from " + EUActivity.class.getName() +
 			 " eu " +  "where (eu.activity.ampActivityId=:actId)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("actId",actId,Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				EUActivity act = (EUActivity) itr.next();
				euActivities.add(act);
			}
		} catch(Exception ex) {
			logger.error("Unable to get EUActivities for activityid="+actId +" "+ ex);
			ex.printStackTrace();
		}
		return euActivities;
	}

	public static Double getTotalCostConverted(Collection euActivities,Long desktopCurrencyId) {
		double ret=0;
		Iterator i = euActivities.iterator();
		while (i.hasNext()) {
			EUActivity element = (EUActivity) i.next();
			element.setDesktopCurrencyId(desktopCurrencyId);
                        try {
                                ret += element.getTotalCostConverted();
                        }
                        catch (AimException ex) {
                                logger.error(
                                    "Unable to get total converted cost=" + ex);
                        }
		}
		return new Double(ret);
	}

	public static Double getTotalContributionsConverted(Collection euActivities,Long desktopCurrencyId) {
		double ret=0;
		Iterator i = euActivities.iterator();
		while (i.hasNext()) {
			EUActivity element = (EUActivity) i.next();
			element.setDesktopCurrencyId(desktopCurrencyId);
                        try {
                                ret += element.getTotalContributionsConverted();
                        }
                        catch (AimException ex) {
                                logger.error("Unable to get total converted cost contribution" + ex);
                        }

		}
		return new Double(ret);
	}

}
