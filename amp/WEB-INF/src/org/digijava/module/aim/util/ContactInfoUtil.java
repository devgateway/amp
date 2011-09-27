package org.digijava.module.aim.util;

import java.util.List;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityContact;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpContactProperty;
import org.digijava.module.aim.dbentity.AmpOrganisationContact;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
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
//beginTransaction();
			session.saveOrUpdate(contact);
			//tx.commit();
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
//beginTransaction();
			if(contact.getOrganizationContacts()!=null && contact.getOrganizationContacts().size()>0){
				contact.getOrganizationContacts().clear();
			}
			session.delete(contact);
			//tx.commit();
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
			queryString="select distinct(contact) from " + AmpContact.class.getName() + " contact left join contact.properties property where contact.name like '%"+keyword+"%' or contact.lastname like '%"
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
	
	/**
	 * @param fromRecord from which record should be loaded contacts
	 * @param resultsNum how many contacts should be loaded
	 * @param sortBy - with which column should records be sorted
	 * @param sortDir - sorting direction is ascending or descending
	 * @param keyword - records, whose name or name+lastname contain keyword text
	 * @param alpha -records , whose name start with alpha
	 * @return contacts
	 * @throws Exception
	 */
	public static List<AmpContact> getPagedContacts(int fromRecord,int resultsNum,String sortBy,String sortDir,String keyword,String alpha) throws Exception{		
		List<AmpContact> contacts=null;
		Session session=null;
		String queryString =null;
		Query query=null;
		try {
			session=PersistenceManager.getRequestDBSession();
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
			if(sortBy!=null && sortDir!=null){
				if (sortBy.equals("name") && sortDir.equals("asc")) {
					queryString += " order by concat(cont.name,cont.lastname) " ;
				} else if (sortBy.equals("name") && sortDir.equals("desc")) {
					queryString += " order by concat(cont.name,cont.lastname) desc " ;
				}else if(sortBy.equals("title") && sortDir.equals("asc")){
					queryString += " order by cont.title ";
				}else if(sortBy.equals("title") && sortDir.equals("desc")){
					queryString += " order by cont.title desc ";
				}else if(sortBy.equals("function") && sortDir.equals("asc")){
					queryString += " order by cont.function ";
				}else if(sortBy.equals("function") && sortDir.equals("desc")){
					queryString += " order by cont.function desc ";
				}
			}
			query=session.createQuery(queryString);
			query.setFirstResult(fromRecord);
			if(resultsNum!=-1){
				query.setMaxResults(resultsNum);
			}			
			contacts=query.list();
		} catch (Exception e) {
			logger.error(e);
		}
		return contacts;
	}
	/**
	 * @return contacts
	 * @throws Exception
	 */
	public static List<AmpContact> getContactsByNameLastName(String name,
			String lastname) throws Exception {
		List<AmpContact> contacts = null;
		Session session = null;
		StringBuilder queryString = new StringBuilder();
		Query query = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			queryString .append("select cont from ");
			queryString .append(AmpContact.class.getName());
			queryString .append(" cont where ");
			boolean isNameProvided=name!=null&&!name.trim().equals("");
			boolean isLastNameProvided=lastname!=null&&!lastname.trim().equals("");
			if(isNameProvided){
				queryString.append(" lower(cont.name) like lower(:name) ");
			}
			if(isLastNameProvided){
				if(isNameProvided){
					queryString.append(" or ");	
				}
				queryString.append(" lower(cont.lastname) like lower(:lastname)");
			}
			queryString.append(" order by cont.function desc ");
			query = session.createQuery(queryString.toString());
			if(isNameProvided){
				query.setString("name", "%"+ name+"%");
			}
			if(isLastNameProvided){
				query.setString("lastname", "%"+ lastname+"%");	
			}
			contacts = query.list();
		} catch (Exception e) {
			logger.error(e);
			throw e;
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
//beginTransaction();
			session.saveOrUpdate(activityContact);
			//tx.commit();
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
		if (activityId==null) return null;
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
	
	public static List<AmpActivityContact> getActivityContactsForType(Long activityId,String contactType) throws Exception{
		if (activityId==null) return null;
		Session session=null;
		String queryString =null;
		Query query=null;
		List<AmpActivityContact> retValue=null;
		try {
			session=PersistenceManager.getRequestDBSession();
			queryString= "select a from " + AmpActivityContact.class.getName()+ " a where a.activity.ampActivityId=:id and a.contactType=:conttype";
			query=session.createQuery(queryString);
			query.setParameter("id", activityId);
			query.setParameter("conttype", contactType);
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
//beginTransaction();
			session.saveOrUpdate(property);
			//tx.commit();
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
//beginTransaction();
			session.delete(property);
			//tx.commit();
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
	
	
	public static void saveOrUpdateOrganisationContact(AmpOrganisationContact orgContact) throws Exception{
		Session session= null;
		Transaction tx=null;
		try {
			session=PersistenceManager.getRequestDBSession();
//beginTransaction();
			session.saveOrUpdate(orgContact);
			//tx.commit();
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
	
	public static void deleteOrgContact(AmpOrganisationContact orgContact) throws Exception{
		Session session= null;
		Transaction tx=null;
		try {
			session=PersistenceManager.getRequestDBSession();
//beginTransaction();
			session.delete(orgContact);
//session.flush();
			//tx.commit();
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
	
	public static List<AmpOrganisationContact> getOrganizationContacts(Long organizatioId){
		List<AmpOrganisationContact> retVal=null;
		Session session=null;
		String queryString =null;
		Query query=null;
		try {
			session=PersistenceManager.getRequestDBSession();
			queryString="select orgCont from " +AmpOrganisationContact.class.getName()+" orgCont where orgCont.organisation.ampOrgId="+organizatioId ;
			query=session.createQuery(queryString);
			retVal=(List<AmpOrganisationContact>) query.list();
		} catch (Exception e) {
			logger.error("couldn't load contact organizations" + e.getMessage());	
			e.printStackTrace();
		}
		return retVal;
	}
	
	public static List<AmpOrganisationContact> getContactOrganizations(Long contactId){
		List<AmpOrganisationContact> retVal=null;
		Session session=null;
		String queryString =null;
		Query query=null;
		try {
			session=PersistenceManager.getRequestDBSession();
			queryString="select contOrg from " + AmpOrganisationContact.class.getName()+" contOrg where contOrg.contact.id="+contactId;
			query=session.createQuery(queryString);
			retVal=(List<AmpOrganisationContact>) query.list();
		} catch (Exception e) {
			logger.error("couldn't load contact organizations" + e.getMessage());	
			e.printStackTrace();
		}
		return retVal;
	}
	
	public static AmpOrganisationContact getAmpOrganisationContact(Long id){
		AmpOrganisationContact retVal=null;
		Session session=null;
		String queryString =null;
		Query query=null;
		try {
			session=PersistenceManager.getRequestDBSession(); 
			queryString="select orgCont from " +AmpOrganisationContact.class.getName()+" orgCont where orgCont.id=:orgContactId" ;
			query=session.createQuery(queryString);
			query.setLong("orgContactId", id);
			retVal=(AmpOrganisationContact) query.uniqueResult();
		} catch (Exception e) {
			logger.error("couldn't load amp organization contact" + e.getMessage());	
			e.printStackTrace();
		}
		return retVal;
	}

    public static String getFormatedPhoneNum (String phoneNum) {
        StringBuffer retVal = new StringBuffer();
        if (phoneNum != null && phoneNum.length() > 0 && phoneNum.indexOf(" ") > -1) {
            int typeIdSeparatorPos = phoneNum.indexOf(" ");
            String phoneTypeIdStr = phoneNum.substring(0, typeIdSeparatorPos);
            String phoneNumberStr = phoneNum.substring(typeIdSeparatorPos, phoneNum.length());
            Long phoneTypeId = null;

            try {
                phoneTypeId = Long.parseLong(phoneTypeIdStr);
                AmpCategoryValue catVal = CategoryManagerUtil.getAmpCategoryValueFromDb(phoneTypeId, false);
                if(catVal!=null){
                	retVal.append(catVal.getValue());
                }                
            } catch (NumberFormatException ex){
                //Old style record processing
                retVal.append(phoneTypeIdStr);
            }
            retVal.append(" ");
            retVal.append(phoneNumberStr);

        } else {
            retVal.append("Incorrect phone number");
        }

        return retVal.toString();
    }

    //these methods are quick workaraound on translation problem
    public static String getActualPhoneNumber (String phoneNum) {
        String retVal = null;
        if (phoneNum != null && phoneNum.length() > 0) {
        	if(phoneNum.indexOf(" ") > -1){
        		int typeIdSeparatorPos = phoneNum.indexOf(" ");
                String phoneNumberStr = phoneNum.substring(typeIdSeparatorPos, phoneNum.length());
                retVal=phoneNumberStr;
        	}else if (phoneNum.indexOf(" ") == -1) {
        		retVal=phoneNum;
        	}
        }
        return retVal;
    }
    //these methods are  workaraound on translation problem

    public static String getPhoneCategory(String phoneNum) {
        String retVal = null;
        AmpCategoryValue phoneCategoryValue = getPhoneCategoryValue(phoneNum);
        if (phoneCategoryValue != null) {
            retVal = phoneCategoryValue.getValue();
        } else {
            retVal = "None";
        }


        return retVal;
    }

    public static AmpCategoryValue getPhoneCategoryValue(String phoneNum) {
        AmpCategoryValue phoneCategoryValue = null;
        if (phoneNum != null && phoneNum.length() > 0 && phoneNum.indexOf(" ") > -1) {
            int typeIdSeparatorPos = phoneNum.indexOf(" ");
            String phoneTypeIdStr = phoneNum.substring(0, typeIdSeparatorPos);
            Long phoneTypeId = null;
            try {
                phoneTypeId = Long.parseLong(phoneTypeIdStr);
                phoneCategoryValue = CategoryManagerUtil.getAmpCategoryValueFromDb(phoneTypeId, false);

            } catch (NumberFormatException ex) {
                return phoneCategoryValue;

            }
        }

        return phoneCategoryValue;
    }

    /*
        Normalizes primary contacts. If no primary contact selected for any contact type it will select
        te first one from the list.
    */
    public static void normalizeActivityContacts (EditActivityForm.ActivityContactInfo contactInfo) {
        //Donor contacts
        if (contactInfo.getDonorContacts() != null && !contactInfo.getDonorContacts().isEmpty()) {
            if (contactInfo.getPrimaryDonorContId() == null ) {
                contactInfo.setPrimaryDonorContId(new String());
            }
            contactInfo.setPrimaryDonorContId(getCategoryPrimaryContactId (contactInfo.getDonorContacts()));
        }

        //MOFED contacts
        if (contactInfo.getMofedContacts() != null && !contactInfo.getMofedContacts().isEmpty()) {
            contactInfo.setPrimaryMofedContId(getCategoryPrimaryContactId (contactInfo.getMofedContacts()));
        }

        //projCoordinator contacts
        if (contactInfo.getProjCoordinatorContacts() != null && !contactInfo.getProjCoordinatorContacts().isEmpty()) {
            contactInfo.setPrimaryProjCoordContId(getCategoryPrimaryContactId (contactInfo.getProjCoordinatorContacts()));
        }

        //SecMinCont contacts
        if (contactInfo.getSectorMinistryContacts() != null && !contactInfo.getSectorMinistryContacts().isEmpty()) {
            contactInfo.setPrimarySecMinContId(getCategoryPrimaryContactId (contactInfo.getSectorMinistryContacts()));
        }

        //primaryImplExecuting contacts
        if (contactInfo.getImplExecutingAgencyContacts() != null && !contactInfo.getImplExecutingAgencyContacts().isEmpty()) {
            contactInfo.setPrimaryImplExecutingContId(getCategoryPrimaryContactId (contactInfo.getImplExecutingAgencyContacts()));
        }
    }
   
	 
	 private static String getCategoryPrimaryContactId (List <AmpActivityContact> contacts) {
	 	boolean hasPrimary = false;
	 	String primaryContactTmpId = null;
	 	String primaryId = null;
	 	
	 	for (AmpActivityContact contact : contacts) {
	 		if (!hasPrimary && contact.getPrimaryContact() != null && contact.getPrimaryContact().booleanValue() == true) {
	 			hasPrimary = true;
	 			primaryContactTmpId = contact.getContact().getTemporaryId();
	 		} else if (hasPrimary && contact.getPrimaryContact() != null && contact.getPrimaryContact().booleanValue() == true) {
	 			//If somehow we have 2 contacts set as primary
	 			contact.setPrimaryContact(new Boolean(false));
	 		}
	 	}
	 	
	 	if (hasPrimary) {
	 		primaryId = primaryContactTmpId;
	 	} else {
	 		//Set first as a primary if no primary exists
	 		primaryId = contacts.get(0).getContact().getTemporaryId();
	 		contacts.get(0).setPrimaryContact(new Boolean(true));
	 	}
	 	return primaryId;
	 }
}
