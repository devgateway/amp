package org.digijava.module.aim.util;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityContact;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpContactProperty;
import org.digijava.module.aim.dbentity.AmpOrganisationContact;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.query.Query;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

import java.util.List;

public class ContactInfoUtil {
    private static Logger logger = Logger.getLogger(ContactInfoUtil.class);
    
    public static void saveOrUpdateContact(AmpContact contact) throws Exception{
        Session session= null;
        try {
            session=PersistenceManager.getRequestDBSession();
//beginTransaction();
            session.saveOrUpdate(contact);
            //tx.commit();
        }catch(Exception ex) {
            throw new AimException("update failed",ex);
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Long> getContactIds() {
        return PersistenceManager.getRequestDBSession()
                .createCriteria(AmpContact.class)
                .setProjection(Projections.property("id"))
                .list();
    }

    public static void deleteContact(AmpContact contact) throws Exception{
        Session session= null;
        try {
            session=PersistenceManager.getRequestDBSession();
//beginTransaction();
            if(contact.getOrganizationContacts()!=null && contact.getOrganizationContacts().size()>0){
                contact.getOrganizationContacts().clear();
            }
            session.delete(contact);
            //tx.commit();
        }catch(Exception ex) {
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
    
    public static List<AmpContact> getContacts(List<Long> ids) {
        String queryString = "select a from " + AmpContact.class.getName() + " a where a.id in (:ids)";
        return PersistenceManager.getSession().createQuery(queryString).setParameterList("ids", ids).list();
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
                query.setParameter("id", id, LongType.INSTANCE);
            }
            query.setParameter("email", email, StringType.INSTANCE);
            Long longValue = (Long) query.uniqueResult();
            retValue= longValue.intValue();
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
            Long longValue = (Long) query.uniqueResult();
            retValue= longValue.intValue();
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
                queryString+=" where lower(cont.name) like lower('"+alpha+"%') and concat(cont.name,"+"' ',"+"cont.lastname) like '%"+keyword+"%'";
            }else if(keyword!=null && alpha == null){
                //queryString+=" where concat(cont.name,"+"' ',"+"cont.lastname) like '%"+keyword+"%'";
                queryString+=" where concat(lower(cont.name),"+"' ',"+"lower(cont.lastname)) like '%"+keyword.toLowerCase()+"%'";
            }else if (keyword==null && alpha!=null){
                queryString+=" where lower(cont.name) like lower('"+alpha+"%')";
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
            logger.error(e.getMessage(), e);
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
                query.setParameter("name", "%"+ name+"%",StringType.INSTANCE);
            }
            if(isLastNameProvided){
                query.setParameter("lastname", "%"+ lastname+"%",StringType.INSTANCE);
            }
            contacts = query.list();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
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
                
                ////System.out.println(nameRow);
                retValue[i]=new String(nameRow + " " + lastnameRow);                    
                i++;
            }
        }
        return retValue;
    }
    
    public static void saveOrUpdateActivityContact(AmpActivityContact activityContact) throws Exception{
        Session session= null;
        try {
            session=PersistenceManager.getRequestDBSession();
//beginTransaction();
            session.saveOrUpdate(activityContact);
            //tx.commit();
        }catch(Exception ex) {
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
    
    public static List<Long> getActivityContactIds(Long activityId) throws Exception{
        if (activityId==null) return null;
        Session session=null;
        String queryString =null;
        Query query=null;
        List<Long> retValue=null;
        try {
            session=PersistenceManager.getRequestDBSession();
            queryString= "select a.id from " + AmpActivityContact.class.getName()+ " a where a.activity.ampActivityId=:id";
            query=session.createQuery(queryString);
            query.setParameter("id", activityId);
            retValue=(List<Long>)query.list();
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
        try {
            session=PersistenceManager.getRequestDBSession();
//beginTransaction();
            session.saveOrUpdate(property);
            //tx.commit();
        }catch(Exception ex) {
            throw new AimException("update failed",ex);
        }
    }
    
    
    public static void saveOrUpdateOrganisationContact(AmpOrganisationContact orgContact) throws Exception{
        Session session= null;
        try {
            session=PersistenceManager.getRequestDBSession();
//beginTransaction();
            session.saveOrUpdate(orgContact);
            //tx.commit();
        }catch(Exception ex) {
            throw new AimException("update failed",ex);
        }
    }
    
    public static void deleteOrgContact(AmpOrganisationContact orgContact) throws Exception{
        Session session= null;
        try {
            session=PersistenceManager.getRequestDBSession();
//beginTransaction();
            session.delete(orgContact);
//session.flush();
            //tx.commit();
        }catch(Exception ex) {
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

    public static String getFormattedPhoneNum(AmpCategoryValue type, String value) {
        StringBuilder retVal = new StringBuilder();

        if (type != null) {
            retVal.append(type.getValue()).append(" ");
        }

        retVal.append(value);

        return retVal.toString();
    }
}
