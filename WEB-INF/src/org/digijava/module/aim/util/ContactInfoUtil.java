package org.digijava.module.aim.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityContact;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.form.EditActivityForm;
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
			queryString="select cont from " +AmpContact.class.getName() + " cont where cont.name like '%"+keyword+"%' or cont.lastname like '%"+keyword
			+"%' or cont.email like '%" + keyword + "%'";
			query=session.createQuery(queryString);
			contacts=query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return contacts;
	}
	
	public static int getContactsSize() throws Exception{
		int retValue=0;
		Session session=null;
		String queryString =null;
		Query query=null;
		try {
			session=PersistenceManager.getRequestDBSession();
			queryString= "select count(*) from " + AmpContact.class.getName();
			query=session.createQuery(queryString);
			retValue=(Integer)query.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retValue;
	}
	
	public static List<AmpContact> getPagedContacts(int fromRecord,int resultsNum,String sortBy,String keyword) throws Exception{
		List<AmpContact> contacts=null;
		Session session=null;
		String queryString =null;
		Query query=null;
		try {
			session=PersistenceManager.getRequestDBSession();
			queryString="select cont from " + AmpContact.class.getName() +" cont ";
			//filter
			if(keyword != null && keyword.length()>0){
				queryString+=" where cont.name like '%"+keyword+"%' or cont.lastname like '%"+keyword
							+"%' or cont.email like '%" + keyword + "%'";
			}
			//sort
			if(sortBy==null || sortBy.equals("nameAscending")){
				queryString += " order by cont.name" ;
			}else if(sortBy.equals("nameDescending")){
				queryString += " order by cont.name desc" ;
			}else if(sortBy.equals("emailAscending")){
				queryString += " order by cont.email";
			}else if(sortBy.equals("emailDescending")){
				queryString += " order by cont.email desc";
			}else if(sortBy.equals("orgNameAscending")){
				queryString += " order by cont.organisationName";
			}else if(sortBy.equals("orgNameDescending")){
				queryString += " order by cont.organisationName desc";
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
	
	public static AmpActivityContact getActivityPrimaryContact(Long activityId, String primaryContactType) throws Exception{
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
	
	/**
	 * gets all activity contacts and divides it into subgroups(donor,mofed, e.t.c.)
	 * @param activityContacts
	 * @param eaForm
	 */
	public static void copyContactsToSubLists(List<AmpActivityContact> activityContacts, EditActivityForm eaForm){
		//fill activity's donor contact List
		if(eaForm.getContactInformation().getContactType()!=null && eaForm.getContactInformation().getContactType().equals(Constants.DONOR_CONTACT)){
			List<AmpActivityContact> donorContacts=null;
			for (AmpActivityContact cont : activityContacts) {
				if(cont.getContactType().equals(Constants.DONOR_CONTACT)){
					if(donorContacts==null){
						donorContacts=new ArrayList<AmpActivityContact>();
					}
					donorContacts.add(cont);
				}
			}
			eaForm.getContactInformation().setDonorContacts(donorContacts);
		}
		//fill activity's mofed contact list
		else if(eaForm.getContactInformation().getContactType()!=null && eaForm.getContactInformation().getContactType().equals(Constants.MOFED_CONTACT)){
			List<AmpActivityContact> mofedContacts=null;
			for (AmpActivityContact cont : activityContacts) {
				if(cont.getContactType()!=null && cont.getContactType().equals(Constants.MOFED_CONTACT)){
					if(mofedContacts==null){
						mofedContacts=new ArrayList<AmpActivityContact>();
					}
					mofedContacts.add(cont);
				}
			}
			eaForm.getContactInformation().setMofedContacts(mofedContacts);
		}
		//fill project coordinator contact list
		else if(eaForm.getContactInformation().getContactType()!=null && eaForm.getContactInformation().getContactType().equals(Constants.PROJECT_COORDINATOR_CONTACT)){
			List<AmpActivityContact> projCoordinatorContacts=null;
			for (AmpActivityContact cont : activityContacts) {
				if(cont.getContactType().equals(Constants.PROJECT_COORDINATOR_CONTACT)){
					if(projCoordinatorContacts==null){
						projCoordinatorContacts=new ArrayList<AmpActivityContact>();
					}
					projCoordinatorContacts.add(cont);
				}
			}
			eaForm.getContactInformation().setProjCoordinatorContacts(projCoordinatorContacts);
		}
		//fill sector ministry contact list
		else if(eaForm.getContactInformation().getContactType()!=null && eaForm.getContactInformation().getContactType().equals(Constants.SECTOR_MINISTRY_CONTACT)){
			List<AmpActivityContact> sectorMinistryContacts=null;
			for (AmpActivityContact cont : activityContacts) {
				if(cont.getContactType().equals(Constants.SECTOR_MINISTRY_CONTACT)){
					if(sectorMinistryContacts==null){
						sectorMinistryContacts=new ArrayList<AmpActivityContact>();
					}
					sectorMinistryContacts.add(cont);
				}
			}
			eaForm.getContactInformation().setSectorMinistryContacts(sectorMinistryContacts);
		}
	}
	
}
