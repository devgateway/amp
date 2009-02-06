/**
 * 
 */
package org.digijava.module.dataExchange.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpComponentFunding;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpPhysicalPerformance;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.helper.Components;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.AuditLoggerUtil;
import org.digijava.module.dataExchange.dbentity.DEMappingFields;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * @author dan
 *
 */
public class DataExchangeUtils {
	
	private static Logger logger = Logger.getLogger(DataExchangeUtils.class);

	/**
	 * @author dan
	 */
	
	public static Collection getAmpDEMappingFields() {
		Session session = null;
		Collection col = new ArrayList();
		String qryStr = null;
		Query qry = null;

		try {
			//session = PersistenceManager.getSession();
			session = PersistenceManager.getRequestDBSession();
			qryStr = "select f from " + DEMappingFields.class.getName() + " f";
			qry = session.createQuery(qryStr);
			col = qry.list();

		}
		catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
		}
		finally {
			if (session != null) {
				try {
					;//PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
		return col;
	}
	
	
	/**
	 *
	 * @author dan
	 *
	 * @return
	 */
	public static boolean insertDEMappingField(String code, String value, Long ampFieldId, String fieldType, String status ) {
		Session session = null;
		Transaction tx = null;

		try {
			//session = PersistenceManager.getSession();
			session = PersistenceManager.getRequestDBSession();
			tx = session.beginTransaction();
			DEMappingFields demf = new DEMappingFields();
			if(code == null && value == null) return false;
			if(code!=null) demf.setImportedFieldCode(code);
			 else demf.setImportedFieldCode(null);
			
			if(value!=null) demf.setImportedFieldValue(value);
			 else demf.setImportedFieldValue(null);
			
			if(ampFieldId!=null) demf.setAmpFieldId(ampFieldId);
			 else demf.setAmpFieldId(null);
			
			if(fieldType!=null) demf.setFieldType(fieldType);
			 else demf.setFieldType(null);
			
			if(status!=null) demf.setStatus(status);
			 else demf.setStatus(null);
			
			session.save(demf);
			tx.commit();
		}
		catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
		}
		finally {
			if (session != null) {
				try {
					;//PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}

		return true;
	}
	
	/**
	 * @author dan
	 */
	
	public static Date XMLGregorianDateToDate(XMLGregorianCalendar importedDate){
		
		  // importedDate= actType.getProposedApprovalDate().getDate();
		  boolean dateToSet=false;
		  if(importedDate == null ) return null;
		  String defaultFormat= "yyyy-MM-dd";
		  SimpleDateFormat formater=new SimpleDateFormat(defaultFormat);
		  GregorianCalendar result=new GregorianCalendar();
		  if(importedDate.getYear()>0 && importedDate.getMonth()>0 && importedDate.getDay()>0)
				  {
				  	String sDate=importedDate.getYear()+"-"+importedDate.getMonth()+"-"+importedDate.getDay();
					try {
					    result.setTime(formater.parse(sDate));
					    dateToSet=true;
					} catch (ParseException e) {
					    // TODO Auto-generated catch block
					  e.printStackTrace();
					}
				  	
			  }
		  if(dateToSet=true) return result.getTime();
		  else return null;
	}

	public static Date intDateToDate(int importedDate){
		
		  // importedDate= actType.getProposedApprovalDate().getDate();
		  boolean dateToSet=false;
		  if(!(importedDate > 1900) ) return null;
		  String defaultFormat= "yyyy-MM-dd";
		  SimpleDateFormat formater=new SimpleDateFormat(defaultFormat);
		  GregorianCalendar result=new GregorianCalendar();
		  String sDate=importedDate+"-1-1";
			try {
			    result.setTime(formater.parse(sDate));
			    dateToSet=true;
			} catch (ParseException e) {
				    // TODO Auto-generated catch block
			   e.printStackTrace();
			  }
				  	
		  if(dateToSet==true) return result.getTime();
		  else return null;
	}

	
	
	/**
	 * @author dan
	 */
	public static void saveActivity(AmpActivity activity, HttpServletRequest request){
		Session session = null;
		HttpSession httpSession=request.getSession();
	    Transaction tx = null;

	    Long activityId = null;
	    
	    try {
	    	//session = PersistenceManager.getSession();
	    	session = PersistenceManager.getRequestDBSession();
	    	//session.connection().setAutoCommit(false);
	    	tx = session.beginTransaction();

			session.save(activity);
	        activityId = activity.getAmpActivityId();
	        String ampId=ActivityUtil.numericAmpId("00",activityId);//generateAmpId(member.getUser(),activityId );
	        activity.setAmpId(ampId);
	        //session.update(activity);
	        tx.commit();
	        
	    }catch (Exception ex) {
	        logger.error("Exception from saveActivity().", ex);
	        //we can't throw here the exception because we need to rollback the transaction
	        ex.printStackTrace();
	        if ( tx != null)
	        	tx.rollback();
	        }
	    finally {
	    	try {
				;//PersistenceManager.releaseSession(session);
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
		//TODO: update the lucene index
		//LuceneUtil.addUpdateActivity(request, false, activityId);
		//for logging the activity
		AuditLoggerUtil.logObject(httpSession, request, activity, "add");
	}

	/**
	 * @author dan
	 * @param name
	 * @return
	 */
	
	public static AmpOrganisation getOrganisationByName(String name) {
		AmpOrganisation obResult=null;
        Session sess = null;
        Query qry = null;
        String queryString = null;

        try {
            sess = PersistenceManager.getRequestDBSession();
            queryString = "select o from " + AmpOrganisation.class.getName()
                + " o where (TRIM(o.name)=:orgName)";
            qry = sess.createQuery(queryString);
            qry.setParameter("orgName", name, Hibernate.STRING);

            List  result=qry.list();
            if (result.size() > 0){
            	obResult= (AmpOrganisation) result.get(0);
            }
            //System.out.println("DBUTIL.GETORGANISATIONBYNAME() : " + qry.getQueryString());
        } catch (Exception e) {
            logger.debug("Exception from getOrganisationByName(): " + e);
            e.printStackTrace(System.out);
        }
        return obResult;
	}
	
	
	/**
	 * @author dan
	 * @param id
	 * @return
	 */

	
	public static AmpOrganisation getOrganizationById(Long id) {
		Session session = null;
		AmpOrganisation ampOrg = null;
		try {
			//session = PersistenceManager.getSession();
			session = PersistenceManager.getRequestDBSession();
			ampOrg = (AmpOrganisation) session.load(AmpOrganisation.class, id);
		}
		catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
		}
		finally {
			if (session != null) {
				try {
					;//PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
		return ampOrg;
	}
	
	
	/**
	 * @author dan
	 * @param id
	 * @return
	 */

	
	public static AmpSector getSectorById(Long id) {
		Session session = null;
		AmpSector ampOrg = null;
		try {
			//session = PersistenceManager.getSession();
			session = PersistenceManager.getRequestDBSession();
			ampOrg = (AmpSector) session.load(AmpSector.class, id);
		}
		catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
		}
		finally {
			if (session != null) {
				try {
					;//PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
		return ampOrg;
	}
	
	
	/**
	 * @author dan
	 * @param fieldType
	 * @param id
	 * @return
	 */
	public static Object getElementFromAmp(String fieldType, Object id){
		
		if( !fieldType.equals(null) ){
			
			if(Constants.AMP_ORGANIZATION.equals(fieldType)){
				if (id instanceof Long) {
					Long newId = (Long) id;
					return getOrganizationById(newId);
				}
				if (id instanceof String) {
					String orgName = (String) id;
					return getOrganisationByName(orgName);
				}
			}
			
			if(Constants.AMP_SECTOR.equals(fieldType)){
				if (id instanceof Long) {
					Long newId = (Long) id;
					return getSectorById(newId);
				}
				if (id instanceof String) {
					String sectorName = (String) id;
					return getSectorByName(sectorName);
				}
			}
			
			
			
		}
		return null;
	}
	/**
	 * @author dan
	 * @param object
	 */
    public static Object addObjectoToAmp(Object object) {
        logger.debug("In add " + object.getClass().getName());
        Session session = null;
        Transaction tx = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            tx = session.beginTransaction();
            session.save(object);
            tx.commit();
        } catch (Exception e) {
            logger.error("Unable to add");
            e.printStackTrace(System.out);
            if (tx != null) {
                try {
                    tx.rollback();
                } catch (HibernateException ex) {
                    logger.error("rollback() failed", ex);
                }
            }
        }
        return object;
    }
	
	/**
	 * @author dan
	 * @param name
	 * @return
	 */
	
	public static AmpSector getSectorByName(String name) {
		AmpSector obResult=null;
        Session sess = null;
        Query qry = null;
        String queryString = null;

        try {
            sess = PersistenceManager.getRequestDBSession();
            queryString = "select o from " + AmpSector.class.getName()
                + " o where (TRIM(o.name)=:sectorName)";
            qry = sess.createQuery(queryString);
            qry.setParameter("sectorName", name, Hibernate.STRING);

            List  result=qry.list();
            if (result.size() > 0){
            	obResult= (AmpSector) result.get(0);
            }
        } catch (Exception e) {
            logger.debug("Exception from getSectorByname(): " + e);
            e.printStackTrace(System.out);
        }
        return obResult;
	}


	public static void saveComponents(AmpActivity activity,	HttpServletRequest request,	Collection<Components<AmpComponentFunding>> tempComps) {
		// TODO Auto-generated method stub
		
		for (Iterator it = tempComps.iterator(); it.hasNext();) {
			Components<AmpComponentFunding> acfs = (Components<AmpComponentFunding>) it.next();
			for (Iterator itcomm = acfs.getCommitments().iterator(); itcomm.hasNext();) {
				AmpComponentFunding comm = (AmpComponentFunding) itcomm.next();
				comm.getActivity().setComponents(new HashSet());
				comm.getComponent().setActivities(new HashSet());
				DataExchangeUtils.addObjectoToAmp(comm);
			}
			for (Iterator itcomm = acfs.getDisbursements().iterator(); itcomm.hasNext();) {
				AmpComponentFunding disb = (AmpComponentFunding) itcomm.next();
				disb.getActivity().setComponents(new HashSet());
				disb.getComponent().setActivities(new HashSet());
				DataExchangeUtils.addObjectoToAmp(disb);
			}
			for (Iterator itcomm = acfs.getExpenditures().iterator(); itcomm.hasNext();) {
				AmpComponentFunding exp = (AmpComponentFunding) itcomm.next();
				exp.getActivity().setComponents(new HashSet());
				exp.getComponent().setActivities(new HashSet());
				DataExchangeUtils.addObjectoToAmp(exp);
			}
			for (Iterator itcomm = acfs.getPhyProgress().iterator(); itcomm.hasNext();) {
				AmpPhysicalPerformance pp =  (AmpPhysicalPerformance) itcomm.next();
				pp.getAmpActivityId().setComponents(new HashSet());
				pp.getComponent().setActivities(new HashSet());
				DataExchangeUtils.addObjectoToAmp(pp);
			}
			
		}
		
		
	}

    
    
}
