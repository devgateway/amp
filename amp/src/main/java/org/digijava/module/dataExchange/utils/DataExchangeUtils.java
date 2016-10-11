package org.digijava.module.dataExchange.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.SiteCache;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityContact;
import org.digijava.module.aim.dbentity.AmpActivityGroup;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpContactProperty;
import org.digijava.module.aim.dbentity.AmpContentTranslation;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpSectorScheme;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.AuditLoggerUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.dataExchange.dbentity.AmpDEImportLog;
import org.digijava.module.dataExchange.dbentity.DELogPerItem;
import org.digijava.module.dataExchange.dbentity.DEMappingFields;
import org.digijava.module.dataExchange.iati.IatiVersion;
import org.digijava.module.dataExchange.jaxb.Activities;
import org.digijava.module.dataExchange.jaxb.CodeValueType;
import org.digijava.module.dataExchange.jaxb.ObjectFactory;
import org.digijava.module.dataExchange.type.AmpColumnEntry;
import org.digijava.module.dataExchange.util.DataExchangeConstants;
import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.sdm.dbentity.Sdm;
import org.digijava.module.sdm.dbentity.SdmItem;
import org.hibernate.*;
import org.hibernate.type.LongType;
import org.hibernate.type.NullableType;
import org.hibernate.type.StringType;
import org.springframework.util.FileCopyUtils;

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

	
	public static DEMappingFields getAmpDEMappingField(Long id) {
		DEMappingFields obResult=null;
        Session sess = null;
        Query qry = null;
        String queryString = null;

        try {
            sess = PersistenceManager.getRequestDBSession();
            queryString = "select o from " + DEMappingFields.class.getName()
                + " o where o.id=:id)";
            qry = sess.createQuery(queryString);
            qry.setParameter("id", id, LongType.INSTANCE);

            List  result=qry.list();
            if (result.size() > 0){
            	obResult= (DEMappingFields) result.get(0);
            }
        } catch (Exception e) {
            logger.debug("Exception from getAmpDEMappingField(): " + e);
            e.printStackTrace(System.out);
        }
        return obResult;
	}
	
	public static Collection<DEMappingFields> getAllAmpDEMappingFields() {
		Session session = null;
		Collection col = new ArrayList();
		String qryStr = null;
		Query qry = null;

		try {
			//session = PersistenceManager.getSession();
			session = PersistenceManager.getRequestDBSession();
			qryStr = "select f from " + DEMappingFields.class.getName() + " f order by id";
			qry = session.createQuery(qryStr);
			col = qry.list();

		}
		catch (Exception ex) {
			logger.error("Exception getAllAmpDEMappingFields: " + ex.getMessage());
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
	 * Gets the list of iati-amp mappings for a spefic amp class & value
	 * @param ampClass is of DataExchangeConstatns constant type like IMPLEMENTATION_LEVEL_TYPE
	 * @return list of DEMappingFields
	 */
	public static List<DEMappingFields> getDEMappingFieldsByAmpClassAndAmpValues(String ampClass, String ampValues) {
		List<DEMappingFields> retVal = new ArrayList<DEMappingFields>();
		String queryStr = "select mf from " + DEMappingFields.class.getName() + " mf where mf.ampClass=:ampClass and mf.ampValues=:ampValues";
		try {
			Query qry = PersistenceManager.getRequestDBSession().createQuery(queryStr);
			qry.setParameter("ampClass", ampClass);
			qry.setParameter("ampValues", ampValues);
			retVal = (List<DEMappingFields>)qry.list();
		} catch (Exception ex) {
			logger.error("Could not getDEMappingFieldsByAmpClassAndAmpValue("+ampClass+", "+ampClass+") : "+ ex.getMessage());
		}
		return retVal;
	}
	
	public static Collection<DEMappingFields> getDEMappingFieldsByAmpClass(String ampClass, int startIndex,String sort,String sortOrder) {
		Session session = null;
		Collection col = new ArrayList();
		String qryStr = null;
		Query qry = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			qryStr = "select f from " + DEMappingFields.class.getName() + " f where f.ampClass=:ampClass order by f."+sort+" "+sortOrder;
			qry = session.createQuery(qryStr);
            qry.setParameter("ampClass", ampClass, StringType.INSTANCE);
            if(startIndex!=-1)
             {
            	qry.setFirstResult(startIndex);
            	qry.setMaxResults(DEConstants.MAPPING_RECORDS_AMOUNT_PER_PAGE);
             }
			
			col = qry.list();

		}
		catch (Exception ex) {
			logger.error("Exception getDEMappingFieldsByAmpClass: " + ex.getMessage());
			ex.printStackTrace();
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

	public static int getCountDEMappingFieldsByAmpClass(String ampClass) {
		Session session = null;
		Collection col = new ArrayList();
		String qryStr = null;
		Query qry = null;
		int resultList = 0;

		try {
			session = PersistenceManager.getRequestDBSession();
			qryStr = "select count(*) from " + DEMappingFields.class.getName() + " f where f.ampClass=:ampClass ";
			qry = session.createQuery(qryStr);
            qry.setParameter("ampClass", ampClass, StringType.INSTANCE);
            resultList	=  (Integer)qry.uniqueResult();
		}
		catch (Exception ex) {
			logger.error("Exception getCountDEMappingFieldsByAmpClass: " + ex.getMessage());
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
		return resultList;
	}
	
	
	/**
	 * @author dan
	 */
	
	public static Date XMLGregorianDateToDate(XMLGregorianCalendar importedDate){
		
		  Boolean dateToSet = Boolean.FALSE;
		  if (importedDate == null)
              return null;
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
		  if (dateToSet)
              return result.getTime();
		  else return null;
	}

	public static Date stringToDate(String importedDate){
		
		  Boolean dateToSet = Boolean.FALSE;
		  if (importedDate == null )
              return null;
		  String defaultFormat= "yyyy-MM-dd";
		  SimpleDateFormat formater=new SimpleDateFormat(defaultFormat);
		  GregorianCalendar result=new GregorianCalendar();
		  try {
			   result.setTime(formater.parse(importedDate));
			   dateToSet=true;
			  } catch (ParseException e) {
				  e.printStackTrace();
			  	}
		  if (dateToSet)
              return result.getTime();
		  else return null;
	}
	
	public static Date intDateToDate(int importedDate){
		
		  // importedDate= actType.getProposedApprovalDate().getDate();
		  boolean dateToSet = false;
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
//beginTransaction();

			session.save(activity);
	        activityId = activity.getAmpActivityId();
	        String ampId=ActivityUtil.numericAmpId("00",activityId);//generateAmpId(member.getUser(),activityId );
	        activity.setAmpId(ampId);
	        //session.update(activity);
	        //tx.commit();
		//TODO: update the lucene index
		//LuceneUtil.addUpdateActivity(request, false, activityId);
		//for logging the activity
		//AuditLoggerUtil.logObject(httpSession, request, activity, "add");
	}
	
	
	/**
	 * @author dan
	 * @throws DgException 
	 */
	public static void saveActivity(AmpActivity activity, HttpServletRequest request) throws DgException{
		Session session = null;

	    Long activityId = null;
	    
	    try {
	    	session = PersistenceManager.getRequestDBSession();
	    	//session.connection().setAutoCommit(false);
//beginTransaction();

	    	Set <AmpActivityContact> activityContacts = activity.getActivityContacts();
	    	//new
	    	if (activityContacts!= null) {
	    		for (AmpActivityContact ampActivityContact : activityContacts) {
	    			AmpContact cont = ampActivityContact.getContact();
	    			AmpContact dbContact = null;
	    			boolean contactExistsInDb = false;
	    			Set<AmpContactProperty> properties = cont.getProperties();
	    			for (AmpContactProperty formProperty : cont.getProperties()) {
	    				Query qry = session.createQuery("select prop.contact from " + AmpContactProperty.class.getName() + " prop where prop.value=:curEmail" +
						" and prop.contact.name=:contName and prop.contact.lastname=:contLastname");
						qry.setParameter("curEmail", formProperty.getValue());
						qry.setParameter("contName", cont.getName());
						qry.setParameter("contLastname", cont.getLastname());
						List retVal =  qry.list();
						if (retVal != null && retVal.size() >0) {
							contactExistsInDb =true;
							dbContact =(AmpContact) retVal.get(0);
							properties = compareProperties(properties, dbContact.getProperties());
							break;
						}
							
	    			}
	    			
	    			if (! contactExistsInDb){
	    				session.save(cont);
	    			}else {
	    				ampActivityContact.setContact(dbContact);
	    			}
	    			
	    			if(properties!=null){
	    				for (AmpContactProperty formProperty : properties) {
		    				if (dbContact != null) {
		    					formProperty.setContact(dbContact);
		    				}else {
		    					formProperty.setContact(cont);
		    				}						
							session.save(formProperty);
						}
	    			}
	    			
	    		}
	    	}
	    	
    	
			session.save(activity);
			
			if (activityContacts!= null) {
	    		for (AmpActivityContact ampActivityContact : activityContacts) {
	    			ampActivityContact.setActivity(activity);
	    			session.save(ampActivityContact);
	    		}
			}
	        activityId = activity.getAmpActivityId();
	        String ampId=ActivityUtil.numericAmpId("00",activityId);//generateAmpId(member.getUser(),activityId );
	        activity.setAmpId(ampId);
	        //session.update(activity);
	        //tx.commit();
	        
	    }catch (Exception ex) {
	        logger.error("Exception from saveActivity().", ex);
	        //we can't throw here the exception because we need to rollback the transaction
	        ex.printStackTrace();
	    }
		//TODO: update the lucene index
		//LuceneUtil.addUpdateActivity(request, false, activityId);
		//for logging the activity
		AuditLoggerUtil.logObject(request, activity, "add",null);
	}

	public static Set <AmpContactProperty> compareProperties (Set <AmpContactProperty> tobeImportedProperties, Set <AmpContactProperty> dbProperties) {
		Set <AmpContactProperty> retVal = null;
		for (AmpContactProperty toBeImported : tobeImportedProperties) {
			boolean propertyExists = false;
			String val = toBeImported.getValue();
			String name = toBeImported.getName();
			for (AmpContactProperty dbProperty : dbProperties) {
				if (dbProperty.getName().equals(name) && dbProperty.getValue().equals(val)) {
					propertyExists = true ;
					break;
				}
			}
			if (!propertyExists){
				if (retVal == null) {
					retVal =  new HashSet<AmpContactProperty>();
				}
				retVal.add(toBeImported);
			}
		}
		return retVal;
	}
	
	public static String generateQuery(String info, String key, String separator, HashMap<String, String> hs) {
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
				String activityNameHql = AmpActivityVersion.hqlStringForName("a");
				query	+=	String.format(" and TRIM(lower(%s)) = :title", activityNameHql);
				hs.put(DataExchangeConstants.COLUMN_KEY_TITLE, alInfo.get(i).trim().toLowerCase());
			}
			i++;
		}
		return query;
	}
	
	// AMP-16239 - see where it is used - might search by name
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
                + " o where (TRIM(o.orgCode)=:orgCode) and (o.deleted is null or o.deleted = false) ";
            qry = sess.createQuery(queryString);
            qry.setParameter("orgCode", code.trim(), StringType.INSTANCE);

            List  result=qry.list();
            if (result.size() > 0){
            	obResult= (AmpOrganisation) result.get(0);
            }
            ////System.out.println("DBUTIL.GETORGANISATIONBYNAME() : " + qry.getQueryString());
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
		if (id==null) return null;		
		return (AmpOrganisation) PersistenceManager.getSession().load(AmpOrganisation.class, id);
	}
	
	public static AmpActivityGroup getAmpActivityGroupById(Long id) {
		return (AmpActivityGroup) PersistenceManager.getSession().load(AmpActivityGroup.class, id);
	}
	
	public static DELogPerItem getDELogPerItemById(Long id) {
		Session session = null;
		DELogPerItem aag = null;
		try {
			//session = PersistenceManager.getSession();
			session = PersistenceManager.getRequestDBSession();
			aag = (DELogPerItem) session.load(DELogPerItem.class, id);
		}
		catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
		}
		return aag;
	}
	
	/**
	 * @author dan
	 * @param id
	 * @return
	 */

	
	public static AmpSector getSectorById(Long id) {
		return (AmpSector) PersistenceManager.getSession().load(AmpSector.class, id);
	}
	
	
	/**
	 * @author dan
	 * @param fieldType
	 * @param id
	 * @return
	 */
		
	public static Object getElementFromAmp(String fieldType, Object id, CodeValueType element){
		if( fieldType!=null ){
			
			if(DEConstants.AMP_ORGANIZATION.equals(fieldType)){
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
			
			if(DEConstants.AMP_SECTOR.equals(fieldType)){
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
			
			if(DEConstants.AMP_PROGRAM.equals(fieldType)){
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
    	PersistenceManager.getSession().save(object);
    	return object;
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
            String sectorNameHql = AmpSector.hqlStringForName("o");
            queryString = "select o from " + AmpSector.class.getName()
                + String.format(" o where (LOWER(TRIM(%s))=:sectorName) and (LOWER(TRIM(o.sectorCodeOfficial))=:sectorCode) and (o.deleted is null or o.deleted = false) ", sectorNameHql);
            qry = sess.createQuery(queryString);
            qry.setParameter("sectorName", name.trim().toLowerCase(), StringType.INSTANCE);
            qry.setParameter("sectorCode", code.trim().toLowerCase(), StringType.INSTANCE);

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
	 * @param id
	 * @return
	 */

	
	public static AmpTheme getProgramById(Long id) {
		return (AmpTheme) PersistenceManager.getSession().load(AmpTheme.class, id);
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
            String themeNameHql = AmpTheme.hqlStringForName("o");
            queryString = "select o from " + AmpTheme.class.getName()
                + String.format(" o where (TRIM(%s)=:programName) and (TRIM(o.themeCode)=:themeCode)", themeNameHql);
            qry = sess.createQuery(queryString);
            qry.setParameter("programName", name.trim(), StringType.INSTANCE);
            qry.setParameter("themeCode", code.trim(), StringType.INSTANCE);

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

	
	public static List<AmpActivityProgramSettings> getAllAmpActivityProgramSettings() {
		return PersistenceManager.getSession().createQuery("select cls from " + AmpActivityProgramSettings.class.getName() + " cls ").list();
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
		//String ret	=  result.replaceAll("\n", " ").replaceAll("<[^>]*?>", " ");
		String ret	=  result.replaceAll("\n", " ").replaceAll("\r", "").replaceAll("<!--.*-->", "").replaceAll("<[^>]*?>", " ");
		return ret;
	}

	public static void updateActivityNoLogger(AmpActivity activity) throws Exception{
		// TODO Auto-generated method stub
		Session session = null;
    	session = PersistenceManager.getRequestDBSession();
    	session.update(activity);	        
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
        return site;
    }


	public static void generateHashMapTypes(HashMap<String, NullableType> hmType) {
		// TODO Auto-generated method stub
//		hmType.put(DataExchangeConstants.COLUMN_KEY_ID, LongType.INSTANCE);
//		hmType.put(DataExchangeConstants.COLUMN_KEY_AMPID, StringType.INSTANCE);
//		hmType.put(DataExchangeConstants.COLUMN_KEY_PTIP, StringType.INSTANCE);
//		hmType.put(DataExchangeConstants.COLUMN_KEY_TITLE, StringType.INSTANCE);
		
	}


	public static Activities generateAmplifyExport(List<AmpActivity> ampActivities) {
		Activities activities = (new ObjectFactory()).createActivities();
		//List<AmpActivity> ampActivities = (List<AmpActivity>) ExportUtil.getWsActivities();
		for (Iterator iterator = ampActivities.iterator(); iterator.hasNext();) {
			//ExportBuilder eBuilder = null;
			AmpActivity ampActivity = (AmpActivity) iterator.next();

			Site site = SiteCache.lookupByName("amp"); // hack, but there are other places in AMP which assume "amp" too
			//eBuilder = new ExportBuilder(ampActivity, site);
			AmpColumnEntry columns = new AmpColumnEntry();
			columns.setKey("activityTree.select");
			columns.setName("activity");
			columns.setSelect(true);
			
		//
			
			AmpColumnEntry colid = new AmpColumnEntry("activityTree.elements[0].select","id","activity.id");
			colid.setSelect(true);
			colid.setMandatory(true);
			
			AmpColumnEntry coltitle = new AmpColumnEntry("activityTree.elements[1].select","title","activity.title");
			coltitle.setSelect(true);
			coltitle.setMandatory(true);
			
			AmpColumnEntry colissues = new AmpColumnEntry("activityTree.elements[2].select","issues","activity.issues");
			colissues.setSelect(true);
			colissues.setMandatory(false);
			
			AmpColumnEntry implementationLevels = new AmpColumnEntry("activityTree.elements[2].select","implementationLevels","activity.implementationLevels");
			implementationLevels.setSelect(true);
			implementationLevels.setMandatory(false);
			
			//location element
			AmpColumnEntry collocation = new AmpColumnEntry("activityTree.elements[4].select","location","activity.location");
			collocation.setSelect(true);
			collocation.setMandatory(true);
			
			ArrayList<AmpColumnEntry>locationitems = new ArrayList<AmpColumnEntry>();
			AmpColumnEntry locationname = new AmpColumnEntry("activityTree.elements[4].elements[0].select","locationname","activity.location.locationName");
			locationname.setName("locationname");
			locationname.setSelect(true);
			locationname.setMandatory(false);
			
			AmpColumnEntry locationFunding = new AmpColumnEntry("activityTree.elements[4].elements[1].select","locationFunding","activity.location.locationFunding");
			locationFunding.setName("locationFunding");
			locationFunding.setSelect(true);
			locationFunding.setMandatory(false);
			
			ArrayList<AmpColumnEntry>locationfundingitems = new ArrayList<AmpColumnEntry>();
			AmpColumnEntry loccommitments = new AmpColumnEntry("activityTree.elements[4].elements[1].elements[0].select","commitments","activity.location.locationFunding.commitments");
			loccommitments.setSelect(true);
			loccommitments.setName("commitments");
			AmpColumnEntry locdisbursements = new AmpColumnEntry("activityTree.elements[4].elements[1].elements[1].select","disbursements","activity.location.locationFunding.disbursements");
			locdisbursements.setSelect(true);
			locdisbursements.setName("disbursements");
			AmpColumnEntry locexpenditures = new AmpColumnEntry("activityTree.elements[4].elements[1].elements[2].select","expenditures","activity.location.locationFunding.expenditures");
			locexpenditures.setSelect(true);
			locexpenditures.setName("expenditures");
			locationfundingitems.add(loccommitments);
			locationfundingitems.add(locdisbursements);
			locationfundingitems.add(locexpenditures);
			locationFunding.setElements(locationfundingitems);
			
			locationitems.add(locationname);
			locationitems.add(locationFunding);
			collocation.setElements(locationitems);

			AmpColumnEntry coldocuments= new AmpColumnEntry("activityTree.elements[5].select","documents","activity.documents");
			coldocuments.setSelect(true);
			coldocuments.setMandatory(true);
			
			AmpColumnEntry relatedLinks= new AmpColumnEntry("activityTree.elements[6].select","relatedLinks","activity.relatedLinks");
			relatedLinks.setSelect(true);
			relatedLinks.setMandatory(true);
			
			AmpColumnEntry physicalProgress= new AmpColumnEntry("activityTree.elements[7].select","physicalProgress","activity.physicalProgress");
			physicalProgress.setSelect(true);
			physicalProgress.setMandatory(true);
			
			columns.setElements(new ArrayList<AmpColumnEntry>());
			columns.getElements().add(colid);
			columns.getElements().add(coltitle);
			columns.getElements().add(collocation);
			columns.getElements().add(coldocuments);
			columns.getElements().add(colissues);
			columns.getElements().add(implementationLevels);
			columns.getElements().add(physicalProgress);
			/*
			try {
				activities.getActivity().add(eBuilder.getActivityType(columns));
			} catch (AmpExportException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
		}
		return activities;
	}


	public static void insertDEMappingField(DEMappingFields mf) {
		Session session = null;
		Transaction tx = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			session.save(mf);
		}
		catch (Exception ex) {
			logger.error("Exception insertDEMappingField: " + ex.getMessage());
		}
	}

	
	public static AmpActivityVersion saveActivity(AmpActivityVersion ampActivity, AmpTeam team,
			List<AmpContentTranslation> translations) {
		try {
			ampActivity = org.dgfoundation.amp.onepager.util.ActivityUtil.saveActivityNewVersion(
					ampActivity, translations, team.getTeamLead(),
					false, PersistenceManager.getRequestDBSession(), false, false);
		} catch (Exception e) {
            e.printStackTrace();
			logger.error(e.getMessage());
		}
		return ampActivity;
	}

    public static TreeMap<Long,String> getNameGroupAllActivities() {
        Session session = null;
        Query qry = null;
        TreeMap<Long,String> result = new TreeMap<Long,String>();
        try {
            session = PersistenceManager.getSession();
            String activityNameHql = AmpActivityVersion.hqlStringForName("f");
            String queryString = String.format("select %s, f.ampActivityGroup from " + AmpActivity.class.getName()
                + " f order by %s asc", activityNameHql, activityNameHql);
            qry = session.createQuery(queryString);
            Iterator iter = qry.list().iterator();
            while (iter.hasNext()) {
                Object[] item = (Object[])iter.next();
                AmpActivityGroup actGroup = (AmpActivityGroup) item[1];
                if(actGroup!=null)
                	result.put(actGroup.getAmpActivityGroupId(),(String)item[0]);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public static List<String> getAllActivitiesName(String str){
    	Session session = null;
        Query qry = null;
        TreeMap<Long,String> result = new TreeMap<Long,String>();
        List<String> result1 = new ArrayList<String>();
        try {
            session = PersistenceManager.getSession();
            String activityNameHql = AmpActivityVersion.hqlStringForName("f");
            String queryString = String.format("select %s, f.ampActivityGroup from " + AmpActivity.class.getName()
                + " f where lower(%s) like lower('%s%%') order by %s asc", activityNameHql, activityNameHql, str, activityNameHql);
            qry = session.createQuery(queryString);
            Iterator iter = qry.list().iterator();
            while (iter.hasNext()) {
            	Object[] item = (Object[])iter.next();
                AmpActivityGroup actGroup = (AmpActivityGroup) item[1];
                if(actGroup!=null){
                	result.put(actGroup.getAmpActivityGroupId(),(String)item[0]);
                	//result1.add((String)item[0]+"!<>!"+actGroup.getAmpActivityGroupId());
                	//result1.add("[\""+(String)item[0]+"\",\""+actGroup.getAmpActivityGroupId()+"\"]");
                	result1.add((String)item[0]+"("+actGroup.getAmpActivityGroupId()+")");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result1;
    }
    
    public static List<String> getAllActivitiesOrganizations(String str){
    	Session session = null;
        Query qry = null;
        TreeMap<Long,String> result = new TreeMap<Long,String>();
        List<String> result1 = new ArrayList<String>();
        try {
            session = PersistenceManager.getSession();
    		//String orgIds = "SELECT DISTINCT(organisation) from amp_org_role";
            String queryString = "select role.organisation.name, role.organisation.ampOrgId FROM " + AmpOrgRole.class.getName()
                + " role WHERE lower(role.organisation.name) like lower('"+str+"%') ORDER BY role.organisation.name ASC";
            qry = session.createQuery(queryString);
            Iterator iter = qry.list().iterator();
            while (iter.hasNext()) {
            	Object[] item = (Object[])iter.next();
            	Long orgId = (Long) item[1];
            	if (!result.containsKey(orgId))
            	{
            		result.put(orgId, (String) item[0]);
            		result1.add((String)item[0] + "(" + orgId + ")");
            	}
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result1;
    }    
    
    public static List<String> getAllActivitiesOrganizationsType(String str){
    	Session session = null;
        Query qry = null;
        TreeMap<Long,String> result = new TreeMap<Long,String>();
        List<String> result1 = new ArrayList<String>();
        try {
            session = PersistenceManager.getSession();
    		//String orgIds = "SELECT DISTINCT(organisation) from amp_org_role";
            String queryString = "select role.organisation.orgType, role.organisation.ampOrgId FROM " + AmpOrgRole.class.getName()
                + " role WHERE lower(role.organisation.orgType) like lower('"+str+"%') ORDER BY role.organisation.orgType ASC";
            qry = session.createQuery(queryString);
            Iterator iter = qry.list().iterator();
            while (iter.hasNext()) {
            	Object[] item = (Object[])iter.next();
            	Long orgId = (Long) item[1];
            	if (!result.containsKey(orgId))
            	{
            		result.put(orgId, (String) item[0]);
            		result1.add((String)item[0] + "(" + orgId + ")");
            	}
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result1;
    }    
   
    
    public static List<String> getAllSectorNameCodeScheme(String str){
    	Session session = null;
        Query qry = null;
        //TreeMap<Long,String> result = new TreeMap<Long,String>();
        List<String> result1 = new ArrayList<String>();
        try {
            session = PersistenceManager.getSession();
    		//String orgIds = "SELECT DISTINCT(organisation) from amp_org_role";
            String sectorNameHql = AmpSector.hqlStringForName("sec");
            String sectorSchemeNameHql = AmpSectorScheme.hqlStringForName("sec.ampSecSchemeId");
            String queryString = String.format("select sec.ampSectorId, %s, %s, sec.sectorCode FROM " + AmpSector.class.getName()
                + " sec WHERE lower(%s) like lower('%s%%') ORDER BY sec.sectorCode ASC", sectorSchemeNameHql, sectorNameHql, sectorNameHql, str);
            qry = session.createQuery(queryString);
            Iterator iter = qry.list().iterator();
            while (iter.hasNext()) {
            	Object[] item = (Object[])iter.next();
            	Object secId = item[0];
//            	Object schemeName = item[1];
            	Object sectorName = item[2];
            	Object sectorCode = item[3];
            	result1.add((String)sectorName + ", code " + sectorCode + " (" + secId + ")");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result1;
    }    
    
    public static List<String> getAllSectorSchemes(String str){
    	Session session = null;
        Query qry = null;
        //TreeMap<Long,String> result = new TreeMap<Long,String>();
        List<String> result1 = new ArrayList<String>();
        try {
            session = PersistenceManager.getSession();
            String sectorSchemeNameHql = AmpSectorScheme.hqlStringForName("scheme");
            String queryString = String.format("select scheme.ampSecSchemeId, %s FROM " + AmpSectorScheme.class.getName()
                + " scheme WHERE lower(%s) like lower('%s%%') ORDER BY %s ASC", sectorSchemeNameHql, sectorSchemeNameHql, str, sectorSchemeNameHql);
            qry = session.createQuery(queryString);
            Iterator iter = qry.list().iterator();
            while (iter.hasNext()) {
            	Object[] item = (Object[])iter.next();
            	Object secId = item[0];
            	Object schemeName = item[1];
           		result1.add((String) schemeName + " (" + secId + ")");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result1;
    }    
    
    public static List<String> getAllActivitiesStatus(String str)
    {
        List<String> result1 = new ArrayList<String>();
        String queryString = "SELECT category_value, id FROM amp_category_value WHERE amp_category_class_id = (SELECT id from amp_category_class WHERE category_name='Activity Status') ORDER BY category_value ASC";
        List<Object[]> rows = PersistenceManager.getSession().createSQLQuery(queryString).list();
        for(Object[] row:rows)
        {
        	String name = PersistenceManager.getString(row[0]);
        	Long id = PersistenceManager.getLong(row[1]);
        	result1.add(name + "(" + id + ")");
        }
        return result1;
    }
    
	public static TreeMap<Long, String> getNameIdAllEntities(String queryString) {
	        Session session = null;
	        Query qry = null;
	        TreeMap<Long,String> result = new TreeMap<Long,String>();
	        try {
	            session = PersistenceManager.getSession();
	            qry = session.createQuery(queryString);
	            Iterator iter = qry.list().iterator();
	            while (iter.hasNext()) {
	                Object[] item = (Object[])iter.next();
	                if((Long)item[1]!=null)
	                	result.put((Long)item[1],(String)item[0]);
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return result;
	}

	public static TreeMap<Long, String> getNameIdAllEntitiesFromACVC(String key) {
		TreeMap<Long,String> result = new TreeMap<Long,String>();
		Collection<AmpCategoryValue> acvs = CategoryManagerUtil.getAmpCategoryValueCollectionByKey(key);
		for (Iterator iterator = acvs.iterator(); iterator.hasNext();) {
			AmpCategoryValue acv = (AmpCategoryValue) iterator.next();
			result.put(acv.getId(), acv.getLabel());
		}
		return result;
	}

	public static TreeMap<Long, String> getNameIdAllLocations() {
		// TODO Auto-generated method stub
		TreeMap<Long,String> result = new TreeMap<Long,String>();
		List<AmpCategoryValueLocations> allLocations = LocationUtil.getAllCountriesAndRegions();
		for (Iterator<AmpCategoryValueLocations> iterator = allLocations.iterator(); iterator.hasNext();) {
			AmpCategoryValueLocations acv = (AmpCategoryValueLocations) iterator.next();
			result.put(acv.getId(), acv.getLabel());
		}
		return result;
	}

	public static void getAmpClassesFromDb(TreeSet<String> ampClasses) {
		Session session = null;
        Query qry = null;
        try {
            session = PersistenceManager.getSession();
            String queryString = "select distinct f.ampClass from " + DEMappingFields.class.getName()+ " f";
            qry = session.createQuery(queryString);
            Iterator iter = qry.list().iterator();
            while (iter.hasNext()) {
               // Object[] item = (Object[])iter.next();
                String ampClass = (String) iter.next();
                if(ampClass !=null && ampClass.length()>0)
                	ampClasses.add(ampClass);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ;
		
	}

	public static ByteArrayOutputStream getFileByOutputstream(Sdm attachedFile) throws SQLException, DgException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			 
			SdmItem item = null;
			if (attachedFile!=null) {
				for (SdmItem sdmItem : attachedFile.getItems()) {
					item = sdmItem;
					break;
				}
				ByteArrayInputStream inStream = new ByteArrayInputStream(item.getContent());
				FileCopyUtils.copy(inStream, outputStream);
			}	
			
			//FileCopyUtils.copy(msForm.getXmlFile().getInputStream(), outputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return outputStream;
	}
	
	public static String getMD5(String s){
		byte[] defaultBytes = s.getBytes();
		String result="";
		try{
			MessageDigest algorithm = MessageDigest.getInstance("MD5");
			algorithm.reset();
			algorithm.update(defaultBytes);
			byte messageDigest[] = algorithm.digest();
		            
			StringBuffer hexString = new StringBuffer();
			for (int i=0;i<messageDigest.length;i++) {
				hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
			}
			String foo = messageDigest.toString();
			////System.out.println("sessionid "+s+" md5 version is "+hexString.toString());
			result=hexString+"";
		}catch(NoSuchAlgorithmException nsae){
		            
		}
		return result;
	}

    public static TreeMap<Long, String> getAllAmpEntitiesByClass(String ampClassTypeSelected) {
        // TODO Auto-generated method stub
        TreeMap<Long, String> allEntities 	=	null;
        if(DataExchangeConstants.IATI_ACTIVITY.compareTo(ampClassTypeSelected) ==0 )
            allEntities 	=	DataExchangeUtils.getNameGroupAllActivities();
        if(DataExchangeConstants.IATI_ORGANIZATION_TYPE.compareTo(ampClassTypeSelected)==0){
            String orgTypeNameHql = AmpOrgType.hqlStringForName("f");
            allEntities 	=	DataExchangeUtils.getNameIdAllEntities(String.format("select %s, f.ampOrgTypeId from " + AmpOrgType.class.getName()+ " f order by %s asc", orgTypeNameHql, orgTypeNameHql));
        }
        if(DataExchangeConstants.IATI_ORGANIZATION.compareTo(ampClassTypeSelected)==0){
            String orgNameHql = AmpOrganisation.hqlStringForName("f");
            allEntities 	=	DataExchangeUtils.getNameIdAllEntities(String.format("select %s, f.ampOrgId from " + AmpOrganisation.class.getName()+ " f where (f.deleted is null or f.deleted = false) order by %s asc", orgNameHql, orgNameHql));
        }
        if(DataExchangeConstants.IATI_LOCATION.compareTo(ampClassTypeSelected)==0){
            allEntities 	=	DataExchangeUtils.getNameIdAllLocations();
        }
        if(CategoryConstants.ACTIVITY_STATUS_NAME.compareTo(ampClassTypeSelected)==0){
            allEntities 	=	DataExchangeUtils.getNameIdAllEntitiesFromACVC(CategoryConstants.ACTIVITY_STATUS_KEY);
        }
        if(DataExchangeConstants.AMP_VOCABULARY_CODE.compareTo(ampClassTypeSelected)==0){
            String secSchemeNameHql = AmpSectorScheme.hqlStringForName("f");
            allEntities 	=	DataExchangeUtils.getNameIdAllEntities(String.format("select %s, f.ampSecSchemeId from " + AmpSectorScheme.class.getName()+ " f", secSchemeNameHql));
        }
        if(DataExchangeConstants.IATI_SECTOR.compareTo(ampClassTypeSelected)==0){
            String sectorNameHql = AmpSector.hqlStringForName("f");
            allEntities 	=	DataExchangeUtils.getNameIdAllEntities(String.format("select concat(f.sectorCodeOfficial,concat(' - ',%s)) as sname, f.ampSectorId  from " + AmpSector.class.getName()+ " f  where (f.deleted is null or f.deleted = false) order by sname", sectorNameHql));
        }
        //type of assistance
        if(CategoryConstants.TYPE_OF_ASSISTENCE_NAME.compareTo(ampClassTypeSelected)==0){
            allEntities 	=	DataExchangeUtils.getNameIdAllEntitiesFromACVC(CategoryConstants.TYPE_OF_ASSISTENCE_KEY);
        }
        //financing instrument
        if(CategoryConstants.FINANCING_INSTRUMENT_NAME.compareTo(ampClassTypeSelected)==0){
            allEntities 	=	DataExchangeUtils.getNameIdAllEntitiesFromACVC(CategoryConstants.FINANCING_INSTRUMENT_KEY);
        }
        //mode of payment
        if(CategoryConstants.MODE_OF_PAYMENT_NAME.compareTo(ampClassTypeSelected)==0){
            allEntities 	=	DataExchangeUtils.getNameIdAllEntitiesFromACVC(CategoryConstants.MODE_OF_PAYMENT_KEY);
        }

        if(CategoryConstants.IMPLEMENTATION_LEVEL_NAME.compareTo(ampClassTypeSelected)==0){
            allEntities 	=	DataExchangeUtils.getNameIdAllEntitiesFromACVC(CategoryConstants.IMPLEMENTATION_LEVEL_KEY);
        }
        return allEntities;
    }
	
    public static String getSchemaFileLoc(IatiVersion version) {
    	URL rootUrl   = DataExchangeUtils.class.getResource("/");
    	String path = null;
    	switch(version) {
    	case V_1_03: path = DEConstants.IATI_SCHEMA_LOCATION_V_1_03; break;
    	}
    	if (path!=null) {
    		try {
				path = rootUrl.toURI().resolve("../../").getPath() + path;
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				path = null;
			}
    	}
    	return path;
    }

    public static Editor createEditor(Site site, String key, String language) {
        Editor editor = new Editor();
        editor.setSiteId(site.getSiteId());
        editor.setSite(site);
        editor.setEditorKey(key);
        editor.setLanguage(language);
        return editor;
    }

    public static Editor createEditor(String siteId, String key, String language) {
        Editor editor = new Editor();
        editor.setSiteId(siteId);
        editor.setEditorKey(key);
        editor.setLanguage(language);
        return editor;
    }

}


