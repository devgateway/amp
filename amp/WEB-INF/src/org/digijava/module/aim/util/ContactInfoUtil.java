package org.digijava.module.aim.util;

import java.util.List;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityContact;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpContactProperty;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.helper.Constants;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ContactInfoUtil {
	private static Logger logger = Logger.getLogger(ContactInfoUtil.class);
	
	public static void saveOrUpdateContact(AmpContact contact) throws Exception{
		Session session= null;
		Transaction tx=null;
		try {
			session=PersistenceManager.getRequestDBSession();
			tx=session.beginTransaction();
			session.saveOrUpdate(contact);
			tx.commit();
		}catch(Exception ex) {
			if(tx!=null) {
				try {
					tx.rollback();					
				}catch(Exception e ) {
					logger.error("...Rollback failed");
					throw new AimException("Can't rollback", e);
				}			
			}
			throw new AimException("update failed",ex);
		}
	}
	
	
	public static void deleteContact(AmpContact contact) throws Exception{
		Session session= null;
		Transaction tx=null;
		try {
			session=PersistenceManager.getRequestDBSession();
			tx=session.beginTransaction();
			session.delete(contact);
			tx.commit();
		}catch(Exception ex) {
			if(tx!=null) {
				try {
					tx.rollback();					
				}catch(Exception e ) {
					logger.error("...Rollback failed");
					throw new AimException("Can't rollback", e);
				}			
			}
			throw new AimException("delete failed",ex);
		}
	}
	
	public static List<AmpContact> getContacts() throws Exception{
		Session session=null;
		String queryString =null;
		Query query=null;
		List<AmpContact> contacts=null;
		try {
			session=PersistenceManager.getRequestDBSession();
			queryString= "select cont from " + AmpContact.class.getName()+" cont";
			query=session.createQuery(queryString);
			contacts=(List<AmpContact>)query.list();
		}catch(Exception ex) {
			logger.error("couldn't load contacts" + ex.getMessage());	
			ex.printStackTrace();
		}
		return contacts;
	}
	
	public static AmpContact getContact(Long id) throws Exception{
		Session session=null;
		String queryString =null;
		Query query=null;
		AmpContact returnValue=null;
		try {
			session=PersistenceManager.getRequestDBSession();
			queryString= "select a from " + AmpContact.class.getName()+ " a where a.id=:id";
			query=session.createQuery(queryString);
			query.setParameter("id", id);
			returnValue=(AmpContact)query.uniqueResult();
		}catch(Exception ex) {
			logger.error("couldn't load Message" + ex.getMessage());	
			ex.printStackTrace();
		}
		return returnValue;
	}
	
	public static List<AmpContact> searchContacts(String keyword) throws Exception{
		List<AmpContact> contacts=null;
		Session session=null;
		String queryString =null;
		Query query=null;
		try {
			session=PersistenceManager.getRequestDBSession();
			queryString="select distinct(property.contact) from " + AmpContactProperty.class.getName() + " property where property.contact.name like '%"+keyword+"%' or property.contact.lastname like '%"
			+keyword+"%' or (property.value like '%" + keyword + "%' and property.name='"+Constants.CONTACT_PROPERTY_NAME_EMAIL +"')";
			query=session.createQuery(queryString);
			contacts=query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return contacts;
	}
	

	
	public static int getContactsCount(String email,Long id) throws Exception{
		int retValue=0;
		Session session=null;
		String queryString =null;
		Query query=null;
		try {
			session=PersistenceManager.getRequestDBSession();
			queryString="select count(property.contact) from " +AmpContactProperty.class.getName() + " property where property.value=:email  ";
            if(id!=null){
            	queryString+=" and property.contact.id!=:id";
            }
			query=session.createQuery(queryString);
            if(id!=null){
            	query.setLong("id", id);
            }
            query.setString("email", email);
			retValue=(Integer)query.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retValue;
	}

	public static int getContactsSize(String keyword,String alpha) throws Exception{
		int retValue=0;
		Session session=null;
		String queryString =null;
		Query query=null;
		try {
			session=PersistenceManager.getRequestDBSession();
			queryString= "select count(*) from " + AmpContact.class.getName()+ " cont";
			if(keyword!=null && alpha!=null){
				queryString+=" where cont.name like '"+alpha+"%' and concat(cont.name,"+"' ',"+"cont.lastname) like '%"+keyword+"%'";
			}else if(keyword!=null && alpha == null){
				queryString+=" where concat(cont.name,"+"' ',"+"cont.lastname) like '%"+keyword+"%'";
			}else if (keyword==null && alpha!=null){
				queryString+=" where cont.name like '"+alpha+"%'";
			}			
			query=session.createQuery(queryString);
			retValue=(Integer)query.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retValue;
	}
	
	public static List<AmpContact> getPagedContacts(int fromRecord,int resultsNum,String sortBy,String keyword,String alpha) throws Exception{
		List<AmpContact> contacts=null;
		Session session=null;
		String queryString =null;
		Query query=null;
		try {
			session=PersistenceManager.getRequestDBSession();
			session.clear();
			queryString="select cont from " + AmpContact.class.getName() +" cont ";
			//filter
			if(keyword!=null && alpha!=null){
				queryString+=" where cont.name like '"+alpha+"%' and concat(cont.name,"+"' ',"+"cont.lastname) like '%"+keyword+"%'";
			}else if(keyword!=null && alpha == null){
				queryString+=" where concat(cont.name,"+"' ',"+"cont.lastname) like '%"+keyword+"%'";
			}else if (keyword==null && alpha!=null){
				queryString+=" where cont.name like '"+alpha+"%'";
			}
			//sort
			if(sortBy==null || sortBy.equals("nameAscending")){
				queryString += " order by cont.name" ;
			}else if(sortBy.equals("nameDescending")){
				queryString += " order by cont.name desc" ;
			}
			else if(sortBy.equals("orgNameAscending")){
				queryString += " order by cont.organisationName";
			}else if(sortBy.equals("orgNameDescending")){
				queryString += " order by cont.organisationName desc";
			}else if(sortBy.equals("titleAscending")){
				queryString += " order by cont.title";
			}else if(sortBy.equals("titleDescending")){
				queryString += " order by cont.title desc";
			}
			query=session.createQuery(queryString);
			query.setFirstResult(fromRecord);
			if(resultsNum!=-1){
				query.setMaxResults(resultsNum);
			}			
			contacts=query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return contacts;
	}
	
	/**
	 * get contact name and lastname for autocomplete box 
	 * @return
	 * @throws Exception
	 */
	public static String[] getContactNames () throws Exception {
		String[] retValue=null;
		Session session=null;
		String queryString =null;
		Query query=null;
		List contactNames=null;
		try{
			session=PersistenceManager.getRequestDBSession();
			queryString="select cont.name,cont.lastname from " +AmpContact.class.getName()+" cont " ;
			query=session.createQuery(queryString);
			contactNames=query.list();
		}catch (Exception e) {
			logger.error("...Failed to get contacts");
			throw new AimException("Can't get Contacts", e);
		}
		
		if(contactNames!=null){
			retValue=new String[contactNames.size()];    		
			int i=0;
			for (Object rawRow : contactNames) {
				Object[] row = (Object[])rawRow; //:)
				String nameRow=(String)row[0];
				String lastnameRow=(String)row[1];
				if(nameRow != null){
					nameRow = nameRow.replace('\n', ' ');
					nameRow = nameRow.replace('\r', ' ');
					nameRow = nameRow.replace("\\", "");
				}
				if(lastnameRow!=null){
					lastnameRow = lastnameRow.replace('\n', ' ');
					lastnameRow = lastnameRow.replace('\r', ' ');
					lastnameRow = lastnameRow.replace("\\", "");
				}
				
				//System.out.println(nameRow);
				retValue[i]=new String(nameRow + " " + lastnameRow);					
				i++;
			}
		}
		return retValue;
	}
	
	public static void saveOrUpdateActivityContact(AmpActivityContact activityContact) throws Exception{
		Session session= null;
		Transaction tx=null;
		try {
			session=PersistenceManager.getRequestDBSession();
			tx=session.beginTransaction();
			session.saveOrUpdate(activityContact);
			tx.commit();
		}catch(Exception ex) {
			if(tx!=null) {
				try {
					tx.rollback();
				}catch(Exception e ) {
					logger.error("...Rollback failed");
					throw new AimException("Can't rollback", e);
				}
			}
			throw new AimException("update failed",ex);
		}
	}
	
	public static List<AmpActivityContact> getActivityContacts(Long activityId) throws Exception{
		Session session=null;
		String queryString =null;
		Query query=null;
		List<AmpActivityContact> retValue=null;
		try {
			session=PersistenceManager.getRequestDBSession();
			queryString= "select a from " + AmpActivityContact.class.getName()+ " a where a.activity.ampActivityId=:id";
			query=session.createQuery(queryString);
			query.setParameter("id", activityId);
			retValue=(List<AmpActivityContact>)query.list();
		}catch(Exception ex) {
			logger.error("couldn't load Message" + ex.getMessage());	
			ex.printStackTrace();
		}
		return retValue;
	}
	
	public static AmpActivityContact getActivityPrimaryContact(Long activityId, String primaryContactType){
		Session session=null;
		String queryString =null;
		Query query=null;
		AmpActivityContact retValue=null;
		try {
			session=PersistenceManager.getRequestDBSession();
			queryString= "select a from " + AmpActivityContact.class.getName()+ " a where a.activity.ampActivityId=:id and a.contactType='"+primaryContactType+"' and a.primaryContact="+true;
			query=session.createQuery(queryString);
			query.setParameter("id", activityId);
			retValue=(AmpActivityContact)query.uniqueResult();
		}catch(Exception ex) {
			logger.error("couldn't load Message" + ex.getMessage());	
			ex.printStackTrace();
		}
		return retValue;
	}
	
	public static void saveOrUpdateContactProperty(AmpContactProperty property) throws Exception{
		Session session= null;
		Transaction tx=null;
		try {
			session=PersistenceManager.getRequestDBSession();
			tx=session.beginTransaction();
			session.saveOrUpdate(property);
			tx.commit();
		}catch(Exception ex) {
			if(tx!=null) {
				try {
					tx.rollback();					
				}catch(Exception e ) {
					logger.error("...Rollback failed");
					throw new AimException("Can't rollback", e);
				}			
			}
			throw new AimException("update failed",ex);
		}
	}
	
	public static void deleteContactProperty(AmpContactProperty property) throws Exception{
		Session session= null;
		Transaction tx=null;
		try {
			session=PersistenceManager.getRequestDBSession();
			tx=session.beginTransaction();
			session.delete(property);
			tx.commit();
		}catch(Exception ex) {
			if(tx!=null) {
				try {
					tx.rollback();					
				}catch(Exception e ) {
					logger.error("...Rollback failed");
					throw new AimException("Can't rollback", e);
				}			
			}
			throw new AimException("delete failed",ex);
		}
	}
	
	public static List<AmpContactProperty> getContactProperties(AmpContact contact){		
		return getContactProperties(contact.getId());
	}
	
	public static List<AmpContactProperty> getContactProperties(Long contactId){
		List<AmpContactProperty> properties=null;
		Session session=null;
		String queryString =null;
		Query query=null;
		try {
			session=PersistenceManager.getRequestDBSession();
			queryString= "select prop from " + AmpContactProperty.class.getName()+ " prop where prop.contact.id="+contactId;
			query=session.createQuery(queryString);
			properties=(List<AmpContactProperty>) query.list();
		} catch (Exception e) {
			logger.error("couldn't load Properties" + e.getMessage());	
			e.printStackTrace();
		}
		return properties;
	}
	
	public static List<String> getContactEmails(Long contactId){
		List<String> emails=null;
		Session session=null;
		String queryString =null;
		Query query=null;
		try {
			session=PersistenceManager.getRequestDBSession();
			queryString= "select prop.value from " + AmpContactProperty.class.getName()+ " prop where prop.contact.id="+contactId+
			" and prop.name='"+Constants.CONTACT_PROPERTY_NAME_EMAIL+"'";
			query=session.createQuery(queryString);
			emails=(List<String>) query.list();
		} catch (Exception e) {
			logger.error("couldn't load Emails" + e.getMessage());	
			e.printStackTrace();
		}
		return emails;
	}
		
}
