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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpComponentFunding;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpPhysicalPerformance;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.helper.Components;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.AuditLoggerUtil;
import org.digijava.module.dataExchange.dbentity.AmpDEImportLog;
import org.digijava.module.dataExchange.dbentity.DEMappingFields;
import org.digijava.module.dataExchange.jaxb.CodeValueType;
import org.digijava.module.dataExchange.util.DataExchangeConstants;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.type.NullableType;

/**
 * @author dan
 *
 */
public class DataExchangeUtils {
	
	private static Logger logger = Logger.getLogger(DataExchangeUtils.class);

	/**
	 * @author dan
	 */
	
	static String entityNames[]={
			"&",	"'",	"\"",	"¢",	"£",	"¤",	"¥",	"¦",	"§",
			"¨",	"©",	"ª",	"«",	"¬",	"­",	    "®",	"¯",	"°",	"±",	"²",	"³",	"´",
			"µ",	"¶",	"·",	"¸",	"¹",	"º",	"»",	"¼",	"½",	"¾",	"¿",	"×",	"÷",
			"À",	"Á",	"Â",	"Ã",	"Ä",	"Å",	"Æ",	"Ç",	"È",	"É",	"Ê",	"Ë",	"Ì",
			"Í",	"Î",	"Ï",	"Ð",	"Ñ",	"Ò",	"Ó",	"Ô",	"Õ",	"Ö",	"Ø",	"Ù",	"Ú",
			"Û",	"Ü",	"Ý",	"Þ",	"ß",	"à",	"á",	"â",	"ã",	"ä",	"å",	"æ",	"ç",
			"è",	"é",	"ê",	"ë",	"ì",	"í",	"î",	"ï",	"ð",	"ñ",	"ò",	"ó",	"ô",
			"õ",	"ö",	"ø",	"ù",	"ú",	"û",	"ü",	"ý",	"þ",	"ÿ", "","","","",""," ", "'", " ","'" , "-", "\"","\"", "œ" };
		//entity names to replace entity charactersentityCharacters
	static String entityCharacters[]={
			"&amp;",	"&apos;",	"\"",	
			"&cent;",	"&pound;",	"&curren;",	"&yen;",	"&brvbar;",	"&sect;",	"&uml;",
			"&copy;",	"&ordf;",	"&laquo;",	"&not;",	"&shy;",	"&reg;",	"&macr;",
			"&deg;",	"&plusmn;",	"&sup2;",	"&sup3;",	"&acute;",	"&micro;",	"&para;",	
			"&middot;",	"&cedil;",	"&sup1;",	"&ordm;",	"&raquo;",	"&frac14;",	"&frac12;",
			"&frac34;",	"&iquest;",	"&times;",	"&divide;",	"&Agrave;",	"&Aacute;",	"&Acirc;",	
			"&Atilde;",	"&Auml;",	"&Aring;",	"&AElig;",	"&Ccedil;",	"&Egrave;",	"&Eacute;",
			"&Ecirc;",	"&Euml;",	"&Igrave;",	"&Iacute;",	"&Icirc;",	"&Iuml;",	"&ETH;",	
			"&Ntilde;",	"&Ograve;",	"&Oacute;",	"&Ocirc;",	"&Otilde;",	"&Ouml;",	"&Oslash;",
			"&Ugrave;",	"&Uacute;",	"&Ucirc;",	"&Uuml;",	"&Yacute;",	"&THORN;",	"&szlig;",	
			"&agrave;",	"&aacute;",	"&acirc;",	"&atilde;",	"&auml;",	"&aring;",	"&aelig;",	
			"&ccedil;",	"&egrave;",	"&eacute;",	"&ecirc;",	"&euml;",	"&igrave;",	"&iacute;",
			"&icirc;",	"&iuml;",	"&eth;",	"&ntilde;",	"&ograve;",	"&oacute;",	"&ocirc;",
			"&otilde;",	"&ouml;",	"&oslash;",	"&ugrave;",	"&uacute;",	"&ucirc;",	"&uuml;",	
			"&yacute;",	"&thorn;",	"&yuml;", "&lt;" , "&quot;" , "&#x9;" , "&#xA;" , "&#xD;", "&nbsp;" , "&rsquo;", "&bull;", "&lsquo;" ,"&ndash;" , "&rdquo;" ,"&ldquo;",
			"&oelig;"};

	
	
	
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
			//tx = session.beginTransaction();
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
			//tx.commit();
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
		
		  Boolean dateToSet=new Boolean(false);
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
		  if(dateToSet==true) return result.getTime();
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
	public static void saveActivityNoLogger(AmpActivity activity) throws Exception{
		Session session = null;
		//HttpSession httpSession=request.getSession();
	    Transaction tx = null;

	    Long activityId = null;
	    
//	    try {
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
	        
//	    }catch (Exception ex) {
//	        logger.error("Exception from saveActivity().", ex);
//	        //we can't throw here the exception because we need to rollback the transaction
//	        ex.printStackTrace();
//	        if ( tx != null)
//	        	tx.rollback();
//	        }
//	    finally {
//	    	try {
//				;//PersistenceManager.releaseSession(session);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//	    }
		//TODO: update the lucene index
		//LuceneUtil.addUpdateActivity(request, false, activityId);
		//for logging the activity
		//AuditLoggerUtil.logObject(httpSession, request, activity, "add");
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
            qry.setParameter("orgName", name.trim(), Hibernate.STRING);

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
	
	public static String generateQuery(String info, String key, String separator, HashMap<String, String> hs) {
		// TODO Auto-generated method stub
		StringTokenizer stInfo = new StringTokenizer(info, separator);
		StringTokenizer stKey = new StringTokenizer(key, separator);
		if(stInfo.countTokens() != stKey.countTokens())
			return null;
		String query = "";
		ArrayList<String> alInfo = new ArrayList<String>();
		while  (stInfo.hasMoreElements())
			     {
					String token = (String)stInfo.nextElement();
					alInfo.add(token);
			     }
		
		query = "select a from " + AmpActivity.class.getName() + " a where 1=1";
		
		int i=0;
		while  (stKey.hasMoreElements())
		{
			String token = (String)stKey.nextElement();
			if(DataExchangeConstants.COLUMN_KEY_ID.equals(token.toLowerCase().trim()))
			{
				query	+=	" and TRIM(lower(a.ampActivityId)) = :id";
				hs.put(DataExchangeConstants.COLUMN_KEY_ID, alInfo.get(i).trim().toLowerCase());
			}
			if(DataExchangeConstants.COLUMN_KEY_AMPID.equals(token.toLowerCase().trim()))
			{
				query	+=	" and TRIM(lower(a.ampId)) = :ampid";
				hs.put(DataExchangeConstants.COLUMN_KEY_AMPID, alInfo.get(i).trim().toLowerCase());
			}
			if(DataExchangeConstants.COLUMN_KEY_PTIP.equals(token.toLowerCase().trim()))
			{
				query	+=	" and TRIM(lower(a.crisNumber)) = :ptip";
				hs.put(DataExchangeConstants.COLUMN_KEY_PTIP, alInfo.get(i).trim().toLowerCase());
			}
			
			if(DataExchangeConstants.COLUMN_KEY_TITLE.equals(token.toLowerCase().trim()))
			{
				query	+=	" and TRIM(lower(a.name)) = :title";
				hs.put(DataExchangeConstants.COLUMN_KEY_TITLE, alInfo.get(i).trim().toLowerCase());
			}
			i++;
		}
		return query;
	}
	
	  public static AmpActivity getActivityByComposedKey(String query, HashMap<String,String> param,HashMap<String,NullableType> types) {
		    AmpActivity activity = null;
		    Session session = null;
		    try {
		    	 session = PersistenceManager.getRequestDBSession();
//		      String qryStr = "select a from " + AmpActivity.class.getName() + " a " +
//		          "where lower(a.name) = :lowerName";
		      Query qry = session.createQuery(query);
//		      qry.setString("lowerName", key.toLowerCase());
		      for (Iterator it = param.keySet().iterator(); it.hasNext();) {
				String key = (String) it.next();
				qry.setParameter(key, param.get(key), types.get(key));
			}
		      List  result=qry.list();
	            if (result.size() > 0){
	            	activity= (AmpActivity) result.get(0);
	            }
		    }
		    catch (Exception e) {
		      logger.debug("Exception in getActivityByComposedKey() " + e.getMessage());
		      e.printStackTrace(System.out);
		    }

		    return activity;
		  }

	
	/**
	 * @author dan
	 * @param name
	 * @return
	 */
	
	public static AmpOrganisation getOrganisationByCode(String code) {
		AmpOrganisation obResult=null;
        Session sess = null;
        Query qry = null;
        String queryString = null;

        try {
            sess = PersistenceManager.getRequestDBSession();
            queryString = "select o from " + AmpOrganisation.class.getName()
                + " o where (TRIM(o.orgCode)=:orgCode)";
            qry = sess.createQuery(queryString);
            qry.setParameter("orgCode", code.trim(), Hibernate.STRING);

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
		
	public static Object getElementFromAmp(String fieldType, Object id, CodeValueType element){
		if( fieldType!=null ){
			
			if(Constants.AMP_ORGANIZATION.equals(fieldType)){
				if (id instanceof Long) {
					Long newId = (Long) id;
					return getOrganizationById(newId);
				}
				if (id instanceof String) {
					String orgName = (String) id;
					//return getOrganisationByName(orgName);
					//SENEGAL changes
					return getOrganisationByCode(element.getCode());
				}
			}
			
			if(Constants.AMP_SECTOR.equals(fieldType)){
				if (id instanceof Long) {
					Long newId = (Long) id;
					return getSectorById(newId);
				}
				if (id instanceof String) {
					String sectorName = (String) id;
					//return getSectorByName(sectorName);
					return getSectorByNameAndCode(sectorName,element.getCode());
				}
			}
			
			if(Constants.AMP_PROGRAM.equals(fieldType)){
				if (id instanceof Long) {
					Long newId = (Long) id;
					return getProgramById(newId);
				}
				if (id instanceof String) {
					String programName = (String) id;
					//return getProgramByName(programName);
					return getProgramByNameAndCode(programName,element.getCode());
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
            //tx = session.beginTransaction();
            session.save(object);
            //tx.commit();
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
            qry.setParameter("sectorName", name.trim(), Hibernate.STRING);

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


	/**
	 * @author dan
	 * @param name
	 * @return
	 */
	
	public static AmpSector getSectorByNameAndCode(String name, String code) {
		AmpSector obResult=null;
        Session sess = null;
        Query qry = null;
        String queryString = null;

        try {
            sess = PersistenceManager.getRequestDBSession();
            queryString = "select o from " + AmpSector.class.getName()
                + " o where (LOWER(TRIM(o.name))=:sectorName) and (LOWER(TRIM(o.sectorCodeOfficial))=:sectorCode)";
            qry = sess.createQuery(queryString);
            qry.setParameter("sectorName", name.trim().toLowerCase(), Hibernate.STRING);
            qry.setParameter("sectorCode", code.trim().toLowerCase(), Hibernate.STRING);

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
				//comm.getActivity().setComponents(new HashSet());
				//comm.getComponent().setActivities(new HashSet());
				DataExchangeUtils.addObjectoToAmp(comm);
			}
			for (Iterator itcomm = acfs.getDisbursements().iterator(); itcomm.hasNext();) {
				AmpComponentFunding disb = (AmpComponentFunding) itcomm.next();
				//disb.getActivity().setComponents(new HashSet());
				//disb.getComponent().setActivities(new HashSet());
				DataExchangeUtils.addObjectoToAmp(disb);
			}
			for (Iterator itcomm = acfs.getExpenditures().iterator(); itcomm.hasNext();) {
				AmpComponentFunding exp = (AmpComponentFunding) itcomm.next();
				//exp.getActivity().setComponents(new HashSet());
				//exp.getComponent().setActivities(new HashSet());
				DataExchangeUtils.addObjectoToAmp(exp);
			}
			for (Iterator itcomm = acfs.getPhyProgress().iterator(); itcomm.hasNext();) {
				AmpPhysicalPerformance pp =  (AmpPhysicalPerformance) itcomm.next();
				//pp.getAmpActivityId().setComponents(new HashSet());
				//pp.getComponent().setActivities(new HashSet());
				DataExchangeUtils.addObjectoToAmp(pp);
			}
			
		}
		
		
	}

	/**
	 * @author dan
	 * @param id
	 * @return
	 */

	
	public static AmpTheme getProgramById(Long id) {
		Session session = null;
		AmpTheme ampOrg = null;
		try {
			//session = PersistenceManager.getSession();
			session = PersistenceManager.getRequestDBSession();
			ampOrg = (AmpTheme) session.load(AmpTheme.class, id);
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
	 * @param name
	 * @return
	 */
	
	public static AmpTheme getProgramByName(String name) {
		AmpTheme obResult=null;
        Session sess = null;
        Query qry = null;
        String queryString = null;

        try {
            sess = PersistenceManager.getRequestDBSession();
            queryString = "select o from " + AmpTheme.class.getName()
                + " o where (TRIM(o.name)=:programName)";
            qry = sess.createQuery(queryString);
            qry.setParameter("programName", name.trim(), Hibernate.STRING);

            List  result=qry.list();
            if (result.size() > 0){
            	obResult= (AmpTheme) result.get(0);
            }
        } catch (Exception e) {
            logger.debug("Exception from getSectorByname(): " + e);
            e.printStackTrace(System.out);
        }
        return obResult;
	}

	
	/**
	 * @author dan
	 * @param name
	 * @return
	 */
	
	public static AmpTheme getProgramByNameAndCode(String name, String code) {
		AmpTheme obResult=null;
        Session sess = null;
        Query qry = null;
        String queryString = null;

        try {
            sess = PersistenceManager.getRequestDBSession();
            queryString = "select o from " + AmpTheme.class.getName()
                + " o where (TRIM(o.name)=:programName) and (TRIM(o.themeCode)=:themeCode)";
            qry = sess.createQuery(queryString);
            qry.setParameter("programName", name.trim(), Hibernate.STRING);
            qry.setParameter("themeCode", code.trim(), Hibernate.STRING);

            List  result=qry.list();
            if (result.size() > 0){
            	obResult= (AmpTheme) result.get(0);
            }
        } catch (Exception e) {
            logger.debug("Exception from getSectorByname(): " + e);
            e.printStackTrace(System.out);
        }
        return obResult;
	}

	
	   public static List<AmpActivityProgramSettings> getAllAmpActivityProgramSettings(){
	        String queryString = null;
	        Session session = null;
	        List<AmpActivityProgramSettings> configs = null;
	        Query qry = null;

	            try {
						session = PersistenceManager.getRequestDBSession();
			            queryString = "select cls from " + AmpActivityProgramSettings.class.getName() + " cls ";
			            qry = session.createQuery(queryString);
			            configs = qry.list();
	            } catch (DgException e) {
	            	// TODO Auto-generated catch block
	            	e.printStackTrace();
	            }
	        

	        return configs;
	    }
	
	public static String renderActivityTree(AmpDEImportLog node) {

		
		StringBuffer retValue = new StringBuffer();

		retValue.append(renderActivityTreeNode(node, "tree.getRoot()"));

		return retValue.toString();
	}
	
	private static String renderActivityTreeNode(AmpDEImportLog node, String parentNode) {
		
		Pattern pattern = Pattern.compile("[\\]\\[.]");
		Matcher matcher = pattern.matcher(node.getKey());
		String key = matcher.replaceAll("");
		StringBuffer retValue = new StringBuffer();
		String nodeVarName = "atn_"+ key;
		String result=node.getObjectNameLogged();
		result = DataExchangeUtils.convertHTMLtoChar(result);
		
		retValue.append("var "+ nodeVarName +" = new YAHOOAmp.widget.TaskNode(\"" +
				result + "\", " + parentNode + ", ");
		retValue.append("false , false, false,");
		retValue.append("\""+key+"\"");
		retValue.append("); ");
		retValue.append("\n");

		if (node.getElements() != null){
			for (AmpDEImportLog subNode : node.getElements()) {
				retValue.append("\n");
				retValue.append(renderActivityTreeNode(subNode, nodeVarName));
				retValue.append("\n");
			}
		}			
		return retValue.toString();
	}
	
	public static String renderHiddenElements(AmpDEImportLog tag){
//		<input type="hidden" name="activityTags[0].select" value="true">
	
		StringBuffer retValue = new StringBuffer();

		
		retValue.append("<input type=\"hidden\" ");

		Pattern pattern = Pattern.compile("[\\]\\[.]");
		Matcher matcher = pattern.matcher(tag.getKey());

		retValue.append("id=\"");
		retValue.append("id_"+ matcher.replaceAll(""));
		retValue.append("\" ");
		retValue.append("name=\"");
//			retValue.append("activityTags["+list.indexOf(tag)+"].select");
		retValue.append(tag.getKey());
		retValue.append("\" ");
		retValue.append("value=\"");
		retValue.append("false");
		retValue.append("\" ");
		retValue.append("/>");
		retValue.append("\n");
		
		if (tag.getElements() != null){
			for (AmpDEImportLog subNode : tag.getElements()) {
				retValue.append(renderHiddenElements(subNode));
			}
		}

		
		
		return retValue.toString();
	}

    
	public static String convertHTMLtoChar(String tempIdref){
		String result = tempIdref;
		for(int x=0;x<entityCharacters.length;x++){
			if(result.contains(entityCharacters[x]))
				{
					if(entityCharacters[x].equals("&#xd")){
						result = result.replaceAll(entityCharacters[x]+"  ", entityNames[x]);
					}
					else result = result.replaceAll(entityCharacters[x], entityNames[x]);
				}
		}
		return result.replaceAll("\n", "");
	}
	public static String convertEntityCharacters(String tempIdref){
		System.out.println("Attempting conversion...");
			//temp replacement string
		String replaceStr=null;
		
			/*
			 *	for the given string, loop though each entity
			 * 	character and replace each with the entity 
			 * 	name
			 */
		for(int x=0;x<entityCharacters.length;x++){
			
				// Compile regular expression
		    String patternStr = "("+entityCharacters[x]+")";
 
		    //System.out.print("Find "+patternStr+"\t");				//testing system.out
		    
		    	//value to replace within string
		    replaceStr=entityNames[x];
		   // System.out.print("\tReplace with "+replaceStr);			//testing system.out
		    
		    //compile the pattern
		    Pattern pattern = Pattern.compile(patternStr);
		    	// Replace all embedded entities with 
		    Matcher matcher = pattern.matcher(tempIdref);
	    		//replace with entity name
		    tempIdref = matcher.replaceAll(replaceStr);	
		    
		   // System.out.print("\tnew Results:\t"+tempIdref+"\n");		//testing system.out
		}
			//return newly modified string
		return tempIdref;
	}


	public static void updateActivityNoLogger(AmpActivity activity) throws Exception{
		// TODO Auto-generated method stub
		Session session = null;
		//HttpSession httpSession=request.getSession();
	    Transaction tx = null;

//	    try {
	    	//session = PersistenceManager.getSession();
	    	session = PersistenceManager.getRequestDBSession();
	    	//session.connection().setAutoCommit(false);
	    	tx = session.beginTransaction();

			//session.saveOrUpdate(activity);
	        session.update(activity);
	        tx.commit();
	        
//	    }catch (Exception ex) {
//	        logger.error("Exception from saveActivity().", ex);
//	        //we can't throw here the exception because we need to rollback the transaction
//	        ex.printStackTrace();
//	        if ( tx != null)
//	        	tx.rollback();
//	        }
		//TODO: update the lucene index
		//LuceneUtil.addUpdateActivity(request, false, activityId);
		//for logging the activity
		//AuditLoggerUtil.logObject(httpSession, request, activity, "add");

	}	
	
    /**
     * Get site by its primary key
     * @param id site's primary key
     * @return Site
     * @throws DgException if database error occurs
     */
    public static Site getSite(Long id) throws DgException {
        Site site = null;
        Session session = null;
        try {
            session = PersistenceManager.getSession();
            site = (Site)session.get(Site.class, id);
        }
        catch (Exception ex) {
            logger.debug("Unable to get site from database ", ex);
            throw new DgException("Unable to get site from database ", ex);
        }
        finally {
            try {
                if (session != null) {
                    PersistenceManager.releaseSession(session);
                }
            }
            catch (Exception ex1) {
                logger.warn("releaseSession() failed ", ex1);
            }
        }
        return site;
    }


	public static void generateHashMapTypes(HashMap<String, NullableType> hmType) {
		// TODO Auto-generated method stub
		hmType.put(DataExchangeConstants.COLUMN_KEY_ID, Hibernate.LONG);
		hmType.put(DataExchangeConstants.COLUMN_KEY_AMPID, Hibernate.STRING);
		hmType.put(DataExchangeConstants.COLUMN_KEY_PTIP, Hibernate.STRING);
		hmType.put(DataExchangeConstants.COLUMN_KEY_TITLE, Hibernate.STRING);
		
	}

	
}


