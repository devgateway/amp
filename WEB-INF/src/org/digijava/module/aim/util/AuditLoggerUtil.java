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

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
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
			Collection col = new ArrayList();
			String qryStr = null;
			Query qry = null;
			String objId;
			//if(o.getIdentifier() instanceof Long) objId=o.getIdentifier().toString(); 
			objId=o.getIdentifier().toString();
			String objType=(String) o.getObjectType();
			String browser=request.getHeader("user-agent");
			try {
				session = PersistenceManager.getSession();

				tx = session.beginTransaction();
				AmpAuditLogger aal=new AmpAuditLogger();
				AmpAuditLogger existentLoggerObj=null;
				long time=System.currentTimeMillis();
				Timestamp ts=new Timestamp(time);
				if("update".compareTo(action)==0)// || "delete".compareTo(action)==0)
				{
					String addAction="add";
					try {
					qryStr = "select f from " + AmpAuditLogger.class.getName() + " f where f.objectType=:objectType and f.action=:actionObj and f.objectId=:objectId ";
					qry = session.createQuery(qryStr);
					qry.setParameter("objectType", objType.toString(), Hibernate.STRING);
					qry.setParameter("actionObj", addAction.toString(), Hibernate.STRING);
					qry.setParameter("objectId",objId.toString(), Hibernate.STRING);
					col = qry.list();
					} catch (Exception e) {
						e.printStackTrace();
						logger.error(e);
					}
//queryString = "select a from " + AmpModulesVisibility.class.getName()
//				+ " a where (a.moduleName=:moduleName) ";
//					q.setParameter("moduleName", moduleName, Hibernate.STRING);
					if(col.size()==1)
					{
						existentLoggerObj = (AmpAuditLogger) col.iterator().next();
						aal.setAuthorEmail(existentLoggerObj.getAuthorEmail());
						aal.setAuthorName(existentLoggerObj.getAuthorName());
						aal.setLoggedDate(existentLoggerObj.getLoggedDate());
					}
					else
					{
						;//there are data before audit logger was implemented!
						//aal.setAuthorName(tm.getMemberName());
						//aal.setAuthorEmail(tm.getEmail());
						//aal.setLoggedDate(ts);
					}
				}
				else
				{
					aal.setAuthorName(tm.getMemberName());
					aal.setAuthorEmail(tm.getEmail());
					aal.setLoggedDate(ts);
				}
				try{
				if(tm.getEmail()!=null) aal.setEditorEmail(tm.getEmail());
				}catch(Exception e)
				{
					e.printStackTrace();
				}
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
