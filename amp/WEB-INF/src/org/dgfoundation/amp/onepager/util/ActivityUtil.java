/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.helper.TemporaryDocument;
import org.dgfoundation.amp.onepager.models.AmpActivityModel;
import org.digijava.module.aim.dbentity.AmpActivityContact;
import org.digijava.module.aim.dbentity.AmpActivityDocument;
import org.digijava.module.aim.dbentity.AmpActivityGroup;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpComments;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpContactProperty;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpOrganisationContact;
import org.digijava.module.aim.dbentity.IndicatorActivity;
import org.digijava.module.aim.helper.ActivityDocumentsConstants;
import org.digijava.module.aim.util.ActivityVersionUtil;
import org.digijava.module.aim.util.ContactInfoUtil;
import org.digijava.module.aim.util.IndicatorUtil;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.helper.TemporaryDocumentData;
import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.editor.exception.EditorException;
import org.digijava.module.editor.util.DbUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Util class used to manipulate an activity
 * @author aartimon@dginternational.org 
 * @since Jun 17, 2011
 */
public class ActivityUtil {
	private static final Logger logger = Logger.getLogger(ActivityUtil.class);
	
	
	/**
	 * Method used to save an Activity/ActivityVersion depending
	 * on activation of versioning option
	 * @param am
	 */
	public static void saveActivity(AmpActivityModel am, boolean draft){
		
		Session session = AmpActivityModel.getSession();
		Transaction transaction = am.getTransaction();
		AmpAuthWebSession wicketSession = (AmpAuthWebSession) org.apache.wicket.Session.get();
		
		try {
			session.clear();
			AmpActivityVersion a = (AmpActivityVersion) am.getObject();
			AmpActivityVersion oldA = a;

			if (a.getAmpActivityId() == null){
				AmpAuthWebSession s =  (AmpAuthWebSession) org.apache.wicket.Session.get();
				a.setActivityCreator(s.getAmpCurrentMember());
				a.setCreatedBy(s.getAmpCurrentMember());
			}
			
			if (a.getDraft() == null)
				a.setDraft(false);
			boolean draftChange = draft != a.getDraft();
			
			a.setDraft(draft);
			a.setDeleted(false);
			//is versioning activated?
			if (a != null && !a.getDraft() && ActivityVersionUtil.isVersioningEnabled()){
				try {
					AmpActivityGroup tmpGroup = a.getAmpActivityGroup();
					
					a = ActivityVersionUtil.cloneActivity(a, wicketSession.getAmpCurrentMember());
					
					if (tmpGroup == null){
						//we need to create a group for this activity
						tmpGroup = new AmpActivityGroup();
						tmpGroup.setAmpActivityLastVersion(a);
						
						session.save(tmpGroup);
					}
					
					a.setAmpActivityGroup(tmpGroup);
					a.setMember(new HashSet());
					a.setAmpActivityId(null);
				} catch (CloneNotSupportedException e) {
					logger.error("Can't clone current Activity: ", e);
				}
			}
			if (oldA.getAmpActivityId() != null)
				session.evict(oldA);
			
			if ((draft == draftChange) && ActivityVersionUtil.isVersioningEnabled()){
				//a.setAmpActivityId(null); //hibernate will save as a new version
				session.save(a);
			}
			else
				session.saveOrUpdate(a);
			
			AmpActivityGroup group = null;
			if (a.getAmpActivityId() != null){
				//existing activity
				group = a.getAmpActivityGroup();
				if (group == null){
					throw new RuntimeException("Non-existent group should have been added at activity load");
				}
				
				//setting lastVersion in group and
				//previousVersion for current activity
				a.setAmpActivityPreviousVersion(group.getAmpActivityLastVersion());
				group.setAmpActivityLastVersion(a);
				session.update(group);
			}
			else{
				//new activity => create ActivityGroup for it
				group = new AmpActivityGroup();
				group.setAmpActivityLastVersion(a);
				session.save(group);
			}
			
			a.setAmpActivityGroup(group);
			a.setCreatedDate(Calendar.getInstance().getTime());
			a.setModifiedDate(Calendar.getInstance().getTime());
			a.setModifiedBy(wicketSession.getAmpCurrentMember());
			a.setTeam(wicketSession.getAmpCurrentMember().getAmpTeam());
			
			
			saveIndicators(a, session);
			/*
			if (a.getActivityContacts() != null)
				a.getActivityContacts().clear();
				*/
			//saveContacts(a, session);
			saveResources(a); 
			saveEditors(session); 
			saveComments(a, session); 
			
			session.flush();
			transaction.commit();
			am.setTransaction(session.beginTransaction());
			am.resetSession();
			am.setObject(a);
		} catch (Exception exception) {
			logger.error("Error saving activity:", exception); // Log the exception
			transaction.rollback();
			if (exception instanceof SQLException) {
			   while(exception != null) {
			         // Get cause if present
			         Throwable t = exception.getCause();
			         while(t != null) {
			               logger.info("Cause:" + t);
			               t = t.getCause();
			         }
			         // procees to the next exception
			         exception = ((SQLException) exception).getNextException();
			         logger.error(exception);
			   }
			}
			throw new RuntimeException("Can't save activity:", exception);
		}
	}

	/**
	 * Method used to load the last version of an object
	 * @param id 
	 * @param ampActivityModel
	 * @return
	 */
	public static AmpActivityVersion load(AmpActivityModel am, Long id) {
		if (id == null){
			return new AmpActivityVersion();
		}
			
		Session session = AmpActivityModel.getSession();//am.getSession();
		am.setTransaction(session.beginTransaction());
		
		AmpActivityVersion act = (AmpActivityVersion) session.load(AmpActivityVersion.class, id);

		//check the activity group for the last version of an activity
		AmpActivityGroup group = act.getAmpActivityGroup();
		if (group == null){
			//Activity created previous to the versioning system?
			if (act == null) //inexistent?
				return null;
			
			//we need to create a group for this activity
			group = new AmpActivityGroup();
			group.setAmpActivityLastVersion(act);
			
			session.save(group);
		}

		
		/**
		 * TODO:
		 * 
		 * Temporary
		 * REMOVE COMMENT!
		 */
		//last version
		//act = group.getAmpActivityLastVersion();
		
		if (act.getDraft() == null)
			act.setDraft(false);
		act.setAmpActivityGroup(group);
		
		if (act.getComponentFundings() != null)
			act.getComponentFundings().size();
		if (act.getCosts() != null)
			act.getCosts().size();
		if (act.getMember() != null)
			act.getMember().size();
		
		return act;
	}

	private static void saveComments(AmpActivityVersion a, Session session) {
		AmpAuthWebSession s =  (AmpAuthWebSession) org.apache.wicket.Session.get();
		
		
		HashSet<AmpComments> newComm = s.getMetaData(OnePagerConst.COMMENTS_ITEMS);
		HashSet<AmpComments> delComm = s.getMetaData(OnePagerConst.COMMENTS_DELETED_ITEMS);
		
		if (delComm != null){
			Iterator<AmpComments> di = delComm.iterator();
			while (di.hasNext()) {
				AmpComments tComm = (AmpComments) di.next();
				session.delete(tComm);
			}
		}

		if (newComm != null){
			Iterator<AmpComments> ni = newComm.iterator();
			while (ni.hasNext()) {
				AmpComments tComm = (AmpComments) ni.next();
				if (ActivityVersionUtil.isVersioningEnabled()){
					try {
						tComm = (AmpComments) tComm.prepareMerge(a);
					} catch (CloneNotSupportedException e) {
						logger.error("can't clone: ", e);
					}
				}
					
				if (tComm.getMemberId() == null)
					tComm.setMemberId(((AmpAuthWebSession)org.apache.wicket.Session.get()).getAmpCurrentMember());
				if (tComm.getAmpActivityId() == null)
					tComm.setAmpActivityId(a);
				session.saveOrUpdate(tComm);
			}
		}
	}

	private static void saveEditors(Session session) {
		AmpAuthWebSession s =  (AmpAuthWebSession) org.apache.wicket.Session.get();
		HashMap<String, String> editors = s.getMetaData(OnePagerConst.EDITOR_ITEMS);
		
		AmpAuthWebSession wicketSession = ((AmpAuthWebSession)org.apache.wicket.Session.get());
		
		if (editors == null || editors.keySet() == null)
			return;
		Iterator<String> it = editors.keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			
			Editor editor = null;
			try {
				editor = DbUtil.getEditor(wicketSession.getSite().getSiteId(), key, wicketSession.getLocale().getLanguage());
				if (editor != null){
					editor.setBody(editors.get(key));
				}
				else{
					editor = new Editor();
					editor.setBody(editors.get(key));
					editor.setSiteId(wicketSession.getSite().getSiteId());
					editor.setLanguage(wicketSession.getLocale().getLanguage());
					editor.setEditorKey(key);
				}
				
				session.saveOrUpdate(editor);
			} catch (EditorException e) {
				logger.error("Can't get editor:", e);
				continue;
			}
		}
	}
	
	private static void saveResources(AmpActivityVersion a) {
		AmpAuthWebSession s =  (AmpAuthWebSession) org.apache.wicket.Session.get();
		
		if (a.getActivityDocuments() == null)
			a.setActivityDocuments(new HashSet());

		HashSet<TemporaryDocument> newResources = s.getMetaData(OnePagerConst.RESOURCES_NEW_ITEMS);
		HashSet<AmpActivityDocument> deletedResources = s.getMetaData(OnePagerConst.RESOURCES_DELETED_ITEMS);
		
		/*
		 * remove old resources
		 */
		if (deletedResources != null){
			Iterator<AmpActivityDocument> it = deletedResources.iterator();
			while (it.hasNext()) {
				AmpActivityDocument tmpDoc = (AmpActivityDocument) it
				.next();
				a.getActivityDocuments().remove(tmpDoc);
			}
		}
		
		/*
		 * Add new resources
		 */
		if (newResources != null){
			Iterator<TemporaryDocument> it2 = newResources.iterator();
			while (it2.hasNext()) {
				TemporaryDocument temp = (TemporaryDocument) it2
				.next();
				TemporaryDocumentData tdd = new TemporaryDocumentData(); 
				tdd.setTitle(temp.getTitle());
				tdd.setName(temp.getFileName());
				tdd.setDescription(temp.getDescription());
				tdd.setNotes(temp.getNote());
				tdd.setCmDocTypeId(temp.getType().getId());
				tdd.setDate(temp.getDate());
				tdd.setYearofPublication(temp.getYear());
				
				if (temp.getWebLink() == null || temp.getWebLink().length() == 0){
					tdd.setFileSize(temp.getFile().getSize());
					
					final FileUpload file = temp.getFile();
					/**
					 * For Document Manager compatibility purposes
					 */
					final FormFile formFile = new FormFile() {
						
						@Override
						public void setFileSize(int arg0) {
						}
						
						@Override
						public void setFileName(String arg0) {
						}
						
						@Override
						public void setContentType(String arg0) {
						}
						
						@Override
						public InputStream getInputStream() throws FileNotFoundException,
						IOException {
							return file.getInputStream();
						}
						
						@Override
						public int getFileSize() {
							return (int) file.getSize();
						}
						
						@Override
						public String getFileName() {
							return file.getClientFileName();
						}
						
						@Override
						public byte[] getFileData() throws FileNotFoundException, IOException {
							return file.getBytes();
						}
						
						@Override
						public String getContentType() {
							return file.getContentType();
						}
						
						@Override
						public void destroy() {
						}
					};
					tdd.setFormFile(formFile);
				}
				
				tdd.setWebLink(temp.getWebLink());
				
				ActionMessages messages = new ActionMessages();
				NodeWrapper node = tdd.saveToRepository(s.getHttpSession(), messages);
				
				AmpActivityDocument aad = new AmpActivityDocument();
				aad.setAmpActivity(a);
				aad.setDocumentType(ActivityDocumentsConstants.RELATED_DOCUMENTS);
				aad.setUuid(node.getUuid());
				a.getActivityDocuments().add(aad);
			}
		}
	}

	private static void saveIndicators(AmpActivityVersion a, Session session) throws Exception {
		if (a.getAmpActivityId() != null){
			//cleanup old indicators
			Set<IndicatorActivity> old = IndicatorUtil.getAllIndicatorsForActivity(a.getAmpActivityId());
			
			if (old != null){
				Iterator<IndicatorActivity> it = old.iterator();
				while (it.hasNext()) {
					IndicatorActivity oldInd = (IndicatorActivity) it
					.next();
					
					boolean found=false;
					if (a.getIndicators() == null)
						continue;
					Iterator<IndicatorActivity> it2 = a.getIndicators().iterator();
					while (it2.hasNext()) {
						IndicatorActivity newind = (IndicatorActivity) it2
								.next();
						if (newind.getId().compareTo(oldInd.getId()) == 0){
							found=true;
							break;
						}
					}
					if (!found){
						Object tmp = session.load(IndicatorActivity.class, oldInd.getId());
						session.delete(tmp);
					}
				}
			}
			
		}
		
		Set<IndicatorActivity> inds = a.getIndicators();
		if (inds != null){
			Iterator<IndicatorActivity> it = inds.iterator();
			while (it.hasNext()) {
				IndicatorActivity ind = (IndicatorActivity) it
						.next();
				ind.setActivity(a);
				session.saveOrUpdate(ind);
			}
		}
	}
	private static void saveContacts(AmpActivityVersion a, Session session) throws Exception {
		Set<AmpActivityContact> activityContacts=a.getActivityContacts();
	      // if activity contains contact,which is not in contact list, we should remove it
		Long oldActivityId = a.getAmpActivityId();
		Set<AmpActivityContact> newActivityContacts=new HashSet<AmpActivityContact>();

		if(oldActivityId != null&&!ActivityVersionUtil.isVersioningEnabled()){
			 List<AmpActivityContact> activityDbContacts=ContactInfoUtil.getActivityContacts(oldActivityId);
		      if(activityDbContacts!=null && activityDbContacts.size()>0){
		    	  Iterator<AmpActivityContact> iter=activityDbContacts.iterator();
		    	  while(iter.hasNext()){
		    		  AmpActivityContact actContact=iter.next();
		    		  int count=0;
		    		  if(activityContacts!=null){
		    			  for (AmpActivityContact activityContact : activityContacts) {
							if(activityContact.getId()!=null && activityContact.getId().equals(actContact.getId())){
								count++;
								break;
							}
						}
		    		  }
		    		  if(count==0){ //if activity contains contact,which is not in contact list, we should remove it
		    			  AmpActivityContact activityCont=(AmpActivityContact)session.get(AmpActivityContact.class, actContact.getId());
		    			  AmpContact cont=activityCont.getContact();
		    			  session.delete(activityCont);
		    			  cont.getActivityContacts().remove(activityCont);
		    			  session.update(cont);    			  		  
		    		  }
		    	  }
		      }
		}
	     
	      //add or edit activity contact and amp contact
	      if(activityContacts!=null && activityContacts.size()>0){
	    	  for (AmpActivityContact activityContact : activityContacts) {
	    		
	    	   	//save or update contact
	    		AmpContact contact=activityContact.getContact();
	    		AmpContact ampContact=null;
	    		if(contact.getId()!=null){ //contact already exists.
	    			ampContact=(AmpContact)session.get(AmpContact.class, contact.getId());
	    			ampContact.setName(contact.getName());
	    			ampContact.setLastname(contact.getLastname());
	    			ampContact.setTitle(contact.getTitle());
	    			ampContact.setOrganisationName(contact.getOrganisationName());
	    			ampContact.setCreator(contact.getCreator());
	    			ampContact.setShared(true);
	    			ampContact.setOfficeaddress(contact.getOfficeaddress());
	    			ampContact.setFunction(contact.getFunction());


	    			
	    			//remove old properties
	    			Set<AmpContactProperty> dbProperties=ampContact.getProperties();
	    			if(dbProperties!=null){
	    				for (AmpContactProperty dbProperty : dbProperties) {
	    					session.delete(dbProperty);
	    				}
	    			}
	    			ampContact.setProperties(null);    			
	    			//remove old organization contacts
	    			Set<AmpOrganisationContact> dbOrgConts=ampContact.getOrganizationContacts();
	    			if(dbOrgConts!=null){
	    				for (AmpOrganisationContact orgCont :dbOrgConts) {
	    					AmpOrganisation organization = orgCont.getOrganisation();
							organization.getOrganizationContacts().remove(orgCont);
							session.update(organization);
							session.delete(orgCont);
						}
	    			}

	    			ampContact.setOrganizationContacts(null);	    			
	    			session.update(ampContact);    			    			
	    		}else{
	    			session.save(contact);
	    		}
	    		
	    		
	    		//save properties
	    		if(contact.getProperties()!=null){
					for (AmpContactProperty formProperty : contact.getProperties()) {
						if(ampContact!=null){
							formProperty.setContact(ampContact);
						}else{
							formProperty.setContact(contact);
						}
						if(formProperty.getActualValue()!=null&&formProperty.getCategoryValue()!=null){
							formProperty.setValue(formProperty.getCategoryValue().getId()+" " +formProperty.getActualValue());
						}
						AmpContactProperty newProperty=new AmpContactProperty();
						newProperty.setContact(formProperty.getContact());
						newProperty.setName(formProperty.getName());
						newProperty.setValue(formProperty.getValue());
						session.save(newProperty);
						
					}
				}
	    		//save cont. organizations
	    		if(contact.getOrganizationContacts()!=null){
	    			for (AmpOrganisationContact orgCont : contact.getOrganizationContacts()) {
						if(ampContact!=null){
							orgCont.setContact(ampContact);
						}else{
							orgCont.setContact(contact);
						}
						AmpOrganisationContact newOrgCont=new AmpOrganisationContact();
						AmpOrganisation organization =(AmpOrganisation)session.load(AmpOrganisation.class, orgCont.getOrganisation().getAmpOrgId());
						if(organization.getOrganizationContacts()==null){
							organization.setOrganizationContacts(new HashSet<AmpOrganisationContact>());
						}
						newOrgCont.setOrganisation(organization);
						newOrgCont.setContact(orgCont.getContact());
						newOrgCont.setPrimaryContact(orgCont.getPrimaryContact());
						organization.getOrganizationContacts().add(newOrgCont);
						session.save(organization);
						session.save(newOrgCont);
					}
	    		}
	    		//is versioning activated?
    			if (ActivityVersionUtil.isVersioningEnabled()){
    				AmpActivityContact newActivityContact=new AmpActivityContact();
    				newActivityContact.setActivity(a);
    				newActivityContact.setContact(activityContact.getContact());
    				newActivityContact.setContactType(activityContact.getContactType());
    				newActivityContact.setPrimaryContact(activityContact.getPrimaryContact());
    				newActivityContacts.add(newActivityContact);
    			}
    			else{
    				session.saveOrUpdate(activityContact);
    			}
	    	  }
	      }
	
	      //add or edit activity contact and amp contact
	      if(newActivityContacts!=null && newActivityContacts.size()>0){
	    	  for (AmpActivityContact activityContact : newActivityContacts) {
	    		  session.save(activityContact);
	    	  }
	      }
	}

}
