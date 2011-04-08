/*
 * ActivityUtil.java
 * Created: 14 Feb, 2005
 */

package org.digijava.module.aim.util;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.Collator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpAuditLogger;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.TeamMember;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.digijava.kernel.translator.TranslatorWorker;

import com.lowagie.text.Row;
/**
 * ActivityUtil is the persister class for all activity related
 * entities
 *
 * @author Priyajith
 */
public class AuditLoggerUtil {

	private static Logger logger = Logger.getLogger(AuditLoggerUtil.class);
	//can't we get session from request? why passing it as parameter?
	public static void logObject(HttpServletRequest request,
			LoggerIdentifiable o, String action,String additionalDetails) throws DgException { 

		Session session = null;
		Transaction tx = null;
		HttpSession hsession=request.getSession();
		TeamMember tm = (TeamMember) hsession.getAttribute(Constants.CURRENT_MEMBER);
		String objId;
		objId = o.getIdentifier().toString();
		String objType = (String) o.getObjectType();
		String browser=request.getHeader("user-agent");
		try {
			session = PersistenceManager.getRequestDBSession();

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
			aal.setDetail(additionalDetails);
			
			session.save(aal);
			tx.commit();
		} catch (Exception ex) {
			logger.error("Cannot save audit logger :", ex);
			if (tx!=null){
				try {
					tx.rollback();
				} catch (Exception e1) {
					logger.error("Release session failed :", e1);
					throw new DgException("Cannot rallback",e1);
				}
			}
			throw new DgException("Cannot save audit logger",ex);
		} 
		return;
	}
	
	
	public static void logObject(HttpSession hsession,HttpServletRequest request,
			LoggerIdentifiable o, String action) throws DgException {
		logObject(request,o, action,null);
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

	public static void logActivityUpdate(HttpServletRequest request, AmpActivity activity, List<String> details){
		Session session = null;
		Transaction tx = null;
		HttpSession hsession = request.getSession();
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
			StringBuilder message=new StringBuilder();
			for(String detail:details){
				message.append(detail+" ");
			}
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
				aal.setDetail(message.toString());
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
			qryStr = "select f from " + AmpAuditLogger.class.getName() + " f order by loggedDate desc";
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
	 * 
	 * @return
	 */
	public static Collection getTeamLogObjects(String teamname) {
		Session session = null;
		Collection<AmpAuditLogger> col = new ArrayList<AmpAuditLogger>();
		String qryStr = null;
		Query qry = null;
		
		try {
			session = PersistenceManager.getSession();
			qryStr = "select f from " 
				+ AmpAuditLogger.class.getName() 
				+ " f where f.teamName like '" + teamname + "'";
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
			if(auditTrail.isEmpty()){
				auditTrail.add("Updated");
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
	
	private static Date getDateRange(int interval) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.add(Calendar.DATE, -interval);
		//Logs doesn't take in account global setting format.
		return  cal.getTime();
    }
	/**
	 * @author Diego Dimunzio
	 * @param interval
	 * @return
	 */
	public static Collection getLogByPeriod(int interval) {
		Session session = null;
		Collection<AmpAuditLogger> col = new ArrayList<AmpAuditLogger>();
		String qryStr = null;
		Query qry = null;
		
		try {
			session = PersistenceManager.getSession();
			qryStr = "select f from " + 
				AmpAuditLogger.class.getName() 
				+ " f where f.modifyDate >= :dateParam";
			qry = session.createQuery(qryStr);
			qry.setParameter("dateParam",getDateRange(interval),Hibernate.DATE);
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
	 * @author Diego Dimunzio
	 * Delete all records whose date is less than the interval
	 * @param interval
	*/
	public static void DeleteLogsByPeriod(String interval){
		Session session = null;
		String qryStr = null;
		Query qry = null;
		Transaction tx = null;
		try {
			session = PersistenceManager.getSession();
			qryStr = "delete from "
				+ AmpAuditLogger.class.getName()
				+ " where loggedDate <= :dateParam or loggedDate=null";
			
			qry = session.createQuery(qryStr);
			qry.setParameter("dateParam",getDateRange(Integer.parseInt(interval)),Hibernate.DATE);
			int rowCount = qry.executeUpdate();
			logger.info("Row deleted from audit logger = " + rowCount);
		
		} catch (HibernateException e) {
			logger.error("HibernateException", e);
		} catch (SQLException e) {
			logger.error("SQLException", e);
	}
}
	/**
     * This class is used for sorting by name.
     * @author Diego Dimunzio
     *
     */
    public static class HelperAuditloggerNameComparator implements Comparator<AmpAuditLogger> {
    	Locale locale;
        Collator collator;

        public HelperAuditloggerNameComparator(){
            this.locale=new Locale("en", "EN");
        }

        public HelperAuditloggerNameComparator(String iso) {
            this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
        }

        public int compare(AmpAuditLogger o1, AmpAuditLogger o2) {
            collator = Collator.getInstance(locale);
            collator.setStrength(Collator.TERTIARY);
            
            int result = (o1.getObjectName()==null || o2.getObjectName()==null)?0:collator.compare(o1.getObjectName().toLowerCase(), o2.getObjectName().toLowerCase());
            return result;
        }
    }
	
    /**
     * This class is used for sorting by Object type.
     * @author Diego Dimunzio
     *
     */
    public static class HelperAuditloggerTypeComparator implements Comparator<AmpAuditLogger> {
    	Locale locale;
        Collator collator;

        public HelperAuditloggerTypeComparator(){
            this.locale=new Locale("en", "EN");
        }

        public HelperAuditloggerTypeComparator(String iso) {
            this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
        }

        public int compare(AmpAuditLogger o1, AmpAuditLogger o2) {
            collator = Collator.getInstance(locale);
            collator.setStrength(Collator.TERTIARY);
            
            int result = (o1.getObjectTypeTrimmed()==null || o2.getObjectName()==null)?0:collator.compare(o1.getObjectTypeTrimmed().toLowerCase(), o2.getObjectTypeTrimmed().toLowerCase());
            return result;
        }
    }
    
    /**
     * This class is used for sorting by Team Name.
     * @author Diego Dimunzio
     *
     */
    public static class HelperAuditloggerTeamComparator implements Comparator<AmpAuditLogger> {
    	Locale locale;
        Collator collator;

        public HelperAuditloggerTeamComparator(){
            this.locale=new Locale("en", "EN");
        }

        public HelperAuditloggerTeamComparator(String iso) {
            this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
        }

        public int compare(AmpAuditLogger o1, AmpAuditLogger o2) {
            collator = Collator.getInstance(locale);
            collator.setStrength(Collator.TERTIARY);
            
            int result = (o1.getTeamName()==null || o2.getTeamName()==null)?0:collator.compare(o1.getTeamName().toLowerCase(), o2.getTeamName().toLowerCase());
            return result;
        }
    }
    
    /**
     * This class is used for sorting by Author Name.
     * @author Diego Dimunzio
     *
     */
    public static class HelperAuditloggerAuthorComparator implements Comparator<AmpAuditLogger> {
    	Locale locale;
        Collator collator;

        public HelperAuditloggerAuthorComparator(){
            this.locale=new Locale("en", "EN");
        }

        public HelperAuditloggerAuthorComparator(String iso) {
            this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
        }

        public int compare(AmpAuditLogger o1, AmpAuditLogger o2) {
            collator = Collator.getInstance(locale);
            collator.setStrength(Collator.TERTIARY);
            
            int result = (o1.getAuthorName()==null || o2.getAuthorName()==null)?0:collator.compare(o1.getAuthorName().toLowerCase(), o2.getAuthorName().toLowerCase());
            return result;
        }
    }
    
    /**
     * This class is used for sorting by  Creation Date.
     * @author Diego Dimunzio
     *
     */
    public static class HelperAuditloggerCreationDateComparator implements Comparator<AmpAuditLogger> {
    	public int compare(AmpAuditLogger o1, AmpAuditLogger o2) {
            int result = (o1.getLoggedDate()==null || o2.getLoggedDate()==null)?0:o1.getLoggedDate().compareTo(o2.getLoggedDate());
            return result;
        }
    }
    
    /**
     * This class is used for sorting by  Change Date.
     * @author Diego Dimunzio
     *
     */
    public static class HelperAuditloggerChangeDateComparator implements Comparator<AmpAuditLogger> {
    	   public int compare(AmpAuditLogger o1, AmpAuditLogger o2) {
            int result = (o1.getModifyDate()==null || o2.getModifyDate()==null)?0:o1.getModifyDate().compareTo(o2.getModifyDate());
            return result;
        }
    }
    
    /**
     * This class is used for sorting by Editor Name.
     * @author Diego Dimunzio
     *
     */
    public static class HelperAuditloggerEditorNameComparator implements Comparator<AmpAuditLogger> {
    	Locale locale;
        Collator collator;

        public HelperAuditloggerEditorNameComparator(){
            this.locale=new Locale("en", "EN");
        }

        public HelperAuditloggerEditorNameComparator(String iso) {
            this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
        }

        public int compare(AmpAuditLogger o1, AmpAuditLogger o2) {
            collator = Collator.getInstance(locale);
            collator.setStrength(Collator.TERTIARY);
            
            int result = (o1.getEditorName()==null || o2.getEditorName()==null)?0:collator.compare(o1.getEditorName(), o2.getEditorName());
            return result;
        }
    }
    /**
     * This class is used for sorting by Action.
     * @author Diego Dimunzio
     *
     */
    public static class HelperAuditloggerActionComparator implements Comparator<AmpAuditLogger> {
    	Locale locale;
        Collator collator;

        public HelperAuditloggerActionComparator(){
            this.locale=new Locale("en", "EN");
        }

        public HelperAuditloggerActionComparator(String iso) {
            this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
        }

        public int compare(AmpAuditLogger o1, AmpAuditLogger o2) {
            collator = Collator.getInstance(locale);
            collator.setStrength(Collator.TERTIARY);
            
            int result = (o1.getAction()==null || o2.getAction()==null)?0:collator.compare(o1.getAction(), o2.getAction());
            return result;
        }
    }
    public static class HelperAuditloggerDetailComparator implements Comparator<AmpAuditLogger> {
    	private Locale locale;
        private Collator collator;
        private String siteId;

        public HelperAuditloggerDetailComparator(){
            this.locale=new Locale("en", "EN");
        }

        public HelperAuditloggerDetailComparator(String iso,String siteId) {
            this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
            this.siteId=siteId;
        }

        public int compare(AmpAuditLogger o1, AmpAuditLogger o2) {
            int result=0;
            try {
                collator = Collator.getInstance(locale);
                collator.setStrength(Collator.TERTIARY);
                String iso = locale.getLanguage();
                String o1detail=o1.getDetail();
                String o2detail=o2.getDetail();
                if(o1detail==null){
                    o1detail="";
                }
                if(o2detail==null){
                    o2detail="";
                }
                result =collator.compare(TranslatorWorker.translateText(o1detail, iso, siteId), TranslatorWorker.translateText(o2detail, iso, siteId));
            } catch (WorkerException ex) {
               logger.error("unable to translate log's detail",ex);
            }
            return result;
        }
    }
} 
