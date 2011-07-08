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
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.helper.TemporaryDocument;
import org.dgfoundation.amp.onepager.models.AmpActivityModel;
import org.digijava.module.aim.dbentity.AmpActivityDocument;
import org.digijava.module.aim.dbentity.AmpActivityGroup;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.IndicatorActivity;
import org.digijava.module.aim.helper.ActivityDocumentsConstants;
import org.digijava.module.aim.util.ActivityVersionUtil;
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
	public static void saveActivity(AmpActivityModel am){
		
		Session session = am.getSession();
		Transaction transaction = am.getTransaction();
		AmpAuthWebSession wicketSession = (AmpAuthWebSession) org.apache.wicket.Session.get();
		
		try {
			AmpActivityVersion a = (AmpActivityVersion) am.getObject();
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
			a.setModifiedDate(Calendar.getInstance().getTime());
			a.setModifiedBy(wicketSession.getAmpCurrentMember());
			a.setTeam(wicketSession.getAmpCurrentMember().getAmpTeam());
			
			saveIndicators(a, session);
			saveResources(a);
			saveEditors(session);

			if (ActivityVersionUtil.isVersioningEnabled()){
				a.setAmpActivityId(null); //hibernate will save as a new version
				session.save(a);
			}
			else
				session.saveOrUpdate(a);
			session.flush();
			transaction.commit();
			am.setTransaction(session.beginTransaction());
			am.resetSession();
		} catch (Exception exception) {
			logger.error("Error saving activity:", exception); // Log the exception
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
			
		Session session = am.getSession();
		am.setTransaction(session.beginTransaction());
		
		AmpActivityVersion act = (AmpActivityVersion) session.load(AmpActivityVersion.class, id);
		//check the activity group for the last version of an activity
		AmpActivityGroup group = act.getAmpActivityGroup();
		if (group == null){
			//Activity created previous to the versioning system?
			//AmpActivityVersion act = (AmpActivityVersion) session.load(AmpActivityVersion.class, id);
			if (act == null) //inexistent?
				return null;
			
			//we need to create a group for this activity
			group = new AmpActivityGroup();
			group.setAmpActivityLastVersion(act);
			
			session.save(group);
		}
		
		//last version
		AmpActivityVersion ret = group.getAmpActivityLastVersion();
		
		//is versioning activated?
		if (ActivityVersionUtil.isVersioningEnabled()){
			AmpAuthWebSession wicketSession = (AmpAuthWebSession) org.apache.wicket.Session.get();
			try {
				ret = ActivityVersionUtil.cloneActivity(ret, wicketSession.getAmpCurrentMember());
			} catch (CloneNotSupportedException e) {
				logger.error("Can't clone current Activity: ", e);
			}
		}
		ret.setAmpActivityGroup(group);
		
		return ret;
	}

	private static void saveEditors(Session session) {
		AmpAuthWebSession s =  (AmpAuthWebSession) org.apache.wicket.Session.get();
		HashMap<String, String> editors = s.getMetaData(OnePagerConst.EDITOR_ITEMS);
		
		AmpAuthWebSession wicketSession = ((AmpAuthWebSession)org.apache.wicket.Session.get());
		
		Iterator<String> it = editors.keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			
			Editor editor = null;
			try {
				editor = DbUtil.getEditor(wicketSession.getSite().getSiteId(), key, wicketSession.getLocale().getLanguage());
				if (editor != null)
					editor.setBody(editors.get(key));
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
		
		if (a.getDocuments() == null)
			a.setDocuments(new HashSet());

		HashSet<TemporaryDocument> newResources = s.getMetaData(OnePagerConst.RESOURCES_NEW_ITEMS);
		HashSet<AmpActivityDocument> deletedResources = s.getMetaData(OnePagerConst.RESOURCES_DELETED_ITEMS);
		
		/*
		 * remove old resources
		 */
		Iterator<AmpActivityDocument> it = deletedResources.iterator();
		while (it.hasNext()) {
			AmpActivityDocument tmpDoc = (AmpActivityDocument) it
					.next();
			a.getActivityDocuments().remove(tmpDoc);
		}
		
		/*
		 * Add new resources
		 */
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

}
