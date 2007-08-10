/*
 * ActivityUtil.java
 * Created: 14 Feb, 2005
 */

package org.digijava.module.aim.util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpAuditLogger;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.helper.TeamMember;

/**
 * ActivityUtil is the persister class for all activity related
 * entities
 *
 * @author Priyajith
 */
public class AuditLoggerUtil {

	private static Logger logger = Logger.getLogger(AuditLoggerUtil.class);

	public static void logObject(HttpSession hsession,HttpServletRequest request, LoggerIdentifiable o, String action){
		
			Session session = null;
			Transaction tx = null;
			TeamMember tm = (TeamMember) hsession.getAttribute("currentMember");
			//String objectName="";

			String browser=request.getHeader("user-agent");
			try {
				session = PersistenceManager.getSession();
				tx = session.beginTransaction();
				AmpAuditLogger aal=new AmpAuditLogger();
				aal.setAction(action);
				long time=System.currentTimeMillis();
				Timestamp ts=new Timestamp(time);
				aal.setLoggedDate(ts);
				aal.setAuthorName(tm.getMemberName());
				aal.setAuthorEmail(tm.getEmail());
				aal.setBrowser(browser);
				aal.setIp(request.getRemoteAddr());
				aal.setObjectId((String) o.getIdentifier());
				aal.setObjectType((String) o.getObjectType());
				aal.setTeamName(tm.getTeamName());
				aal.setObjectName(o.getObjectName());
				session.save(aal);
				tx.commit();
			} catch (Exception ex) {
				logger.error("Exception : " + ex.getMessage());
			} finally {
				if (session != null) {
					try {
						PersistenceManager.releaseSession(session);
					} catch (Exception rsf) {
						logger.error("Release session failed :" + rsf.getMessage());
					}
				}
			}

			return ;
		}

	/**
	 * @author dan
	 */
	public static Collection getLogObjects() {
		Session session = null;
		Collection col = new ArrayList();
		String qryStr = null;
		Query qry = null;
		
		try {
			session = PersistenceManager.getSession();
			qryStr = "select f from " + AmpAuditLogger.class.getName() + " f";
			qry = session.createQuery(qryStr);
			col = qry.list();
		} catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
		return col;
	}


	/**
	 * @author dan
	 */
	public static Object loadObject(String idObj, String className) {
		Session session = null;
		Collection col = new ArrayList();
		String qryStr = null;
		Query qry = null;
		Long id=new Long(Long.parseLong(idObj));
		Object o=null;
		try {
			session = PersistenceManager.getSession();
			qryStr = "select f from " + className + " f where f.id="+id;
			if(className.contains("AmpReports")) {
				qryStr = "select f from " + className + " f where f.ampReportId="+id;
				//System.out.println("aaaaaaaaaaa"+qryStr);
				
			}
			qry = session.createQuery(qryStr);
			col = qry.list();
			o=col.iterator().next();
		} catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
		return o;
	}

	
} 
