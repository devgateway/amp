/*
 * ActivityUtil.java
 * Created: 14 Feb, 2005
 */

package org.digijava.module.aim.util;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.helper.Constants;
/**
 * ActivityUtil is the persister class for all activity related
 * entities
 *
 * @author Priyajith
 */
public class AuditLoggerUtil {

	private static Logger logger = Logger.getLogger(AuditLoggerUtil.class);

	public static void logObject(HttpSession hsession,HttpServletRequest request,
			LoggerIdentifiable o, String action) {

		Session session = null;
		Transaction tx = null;
		TeamMember tm = (TeamMember) hsession.getAttribute(Constants.CURRENT_MEMBER);
		String objId;
		objId = o.getIdentifier().toString();
		String objType = (String) o.getObjectType();
		String browser=request.getHeader("user-agent");
		try {
			session = PersistenceManager.getSession();

			tx = session.beginTransaction();
			AmpAuditLogger aal = new AmpAuditLogger();
			long time = System.currentTimeMillis();
			Timestamp ts = new Timestamp(time);
			if ("update".compareTo(action) == 0) {
				Collection<AmpAuditLogger> col = getAudits(session, objId, objType);
				if (col != null && col.size() == 1) {
					AmpAuditLogger existentLoggerObj = (AmpAuditLogger) col.iterator().next();
					aal.setAuthorEmail(existentLoggerObj.getAuthorEmail());
					aal.setAuthorName(existentLoggerObj.getAuthorName());
					aal.setLoggedDate(existentLoggerObj.getLoggedDate());
				}
			} else {
				aal.setAuthorName(tm.getMemberName());
				aal.setAuthorEmail(tm.getEmail());
				aal.setLoggedDate(ts);
			}


			aal.setEditorEmail(tm.getEmail());
			aal.setEditorName(tm.getMemberName());
			aal.setAction(action);
			aal.setModifyDate(ts);
			aal.setBrowser(browser);
			aal.setIp(request.getRemoteAddr());
			aal.setObjectId((String) o.getIdentifier().toString());
			aal.setObjectType((String) o.getObjectType());
			aal.setTeamName(tm.getTeamName());
			aal.setObjectName(o.getObjectName());
			
			session.save(aal);
			tx.commit();
		} catch (Exception ex) {
			logger.error("Exception : ", ex);
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed :", rsf);
				}
			}
		}

		return;
	}

	private static Collection<AmpAuditLogger> getAudits(Session session,
			String objId, String objType) {
		String qryStr;
		Query qry;
		try {
			String addAction = "add";
			qryStr = "select f from "
				+ AmpAuditLogger.class.getName()
				+ " f where f.objectType=:objectType and f.action=:actionObj and f.objectId=:objectId ";
			qry = session.createQuery(qryStr);
			qry.setParameter("objectType", objType.toString(),
					Hibernate.STRING);
			qry.setParameter("actionObj", addAction.toString(),
					Hibernate.STRING);
			qry.setParameter("objectId", objId.toString(),
					Hibernate.STRING);
			return qry.list();
		} catch (Exception e) {
			logger.error("Exception:", e);
		}
		return null;
	}

	public static void logActivityUpdate(HttpSession hsession,HttpServletRequest request, AmpActivity activity, List<String> details){
		Session session = null;
		Transaction tx = null;
		TeamMember tm = (TeamMember) hsession.getAttribute(Constants.CURRENT_MEMBER);
		String objId;
		objId = activity.getIdentifier().toString();
		String objType = (String) activity.getObjectType();
		String browser=request.getHeader("user-agent");
		try {
			session = PersistenceManager.getSession();

			tx = session.beginTransaction();			
			long time = System.currentTimeMillis();
			Timestamp ts = new Timestamp(time);			
			AmpAuditLogger existentLoggerObj = null;
				
			Collection<AmpAuditLogger> col = getAudits(session, objId, objType);
			if (col != null && col.size() == 1) {
				existentLoggerObj = (AmpAuditLogger) col
						.iterator().next();
			}

			Iterator<String> it = details.iterator();
			while(it.hasNext()){
				AmpAuditLogger aal = new AmpAuditLogger();
				if(existentLoggerObj!=null){
					aal.setAuthorEmail(existentLoggerObj.getAuthorEmail());
					aal.setAuthorName(existentLoggerObj.getAuthorName());
					aal.setLoggedDate(existentLoggerObj.getLoggedDate());
				} else{
					aal.setAuthorName(tm.getMemberName());
					aal.setAuthorEmail(tm.getEmail());
					aal.setLoggedDate(ts);
				}
				String message = it.next();
				aal.setEditorEmail(tm.getEmail());
				aal.setEditorName(tm.getMemberName());
				aal.setAction("update");
				aal.setModifyDate(ts);
				aal.setBrowser(browser);
				aal.setIp(request.getRemoteAddr());
				aal.setObjectId((String) activity.getIdentifier().toString());
				aal.setObjectType((String) activity.getObjectType());
				aal.setTeamName(tm.getTeamName());
				aal.setObjectName(activity.getObjectName());
				aal.setDetail(message);
				session.save(aal);				
			}

			tx.commit();
		} catch (Exception ex) {
			logger.error("Exception : ", ex);
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed :", rsf);
				}
			}
		}			
	}
	
	/**
	 * @author dan
	 */
	public static Collection getLogObjects() {
		Session session = null;
		Collection<AmpAuditLogger> col = new ArrayList<AmpAuditLogger>();
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

	public static List<AmpAuditLogger> getActivityLogObjects(String activityId) {
		Session session = null;
		List<AmpAuditLogger> col = new ArrayList<AmpAuditLogger>();
		String qryStr = null;
		Query qry = null;
		
		try {
			session = PersistenceManager.getSession();
			
			qryStr = "select f from "
					+ AmpAuditLogger.class.getName()
					+ " f where f.objectType=:objectType and f.objectId=:objectId order by f.modifyDate desc";
			qry = session.createQuery(qryStr);
			qry.setParameter("objectType", AmpActivity.class.getCanonicalName(), Hibernate.STRING);
			qry.setParameter("objectId", activityId, Hibernate.STRING);
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

	public static List<String> generateLogs(AmpActivity activity,
			Long activityId) {
		List<String> auditTrail = new ArrayList<String>();
		Session session = null;
		try {
			session = PersistenceManager.getSession();
			AmpActivity oldActivity = (AmpActivity) session.load(
					AmpActivity.class, activityId);
			if (oldActivity.getName() != null
					&& !oldActivity.getName().equals(activity.getName())) {
				auditTrail.add("Name changed");
			}
		} catch (HibernateException e) {
			logger.error("HibernateException", e);
		} catch (SQLException e) {
			logger.error("SQLException", e);
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed.", rsf);
				}
			}
		}
		return auditTrail;

	} 

	
} 
