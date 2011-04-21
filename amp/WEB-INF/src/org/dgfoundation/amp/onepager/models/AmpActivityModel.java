/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.models;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.helper.TemporaryDocument;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityDocument;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.IndicatorActivity;
import org.digijava.module.aim.helper.ActivityDocumentsConstants;
import org.digijava.module.aim.util.IndicatorUtil;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.helper.TemporaryDocumentData;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * @author mpostelnicu@dgateway.org since Sep 24, 2010
 */
public class AmpActivityModel extends LoadableDetachableModel<AmpActivityVersion> implements IModel<AmpActivityVersion>  {
	private static Logger logger = Logger.getLogger(AmpActivityModel.class);
	
	
	protected transient AmpActivityVersion a;
	protected Long id;
	protected transient Session session;
	protected transient Transaction transaction;

	private static final long serialVersionUID = 3915904820384659360L;

	
	public AmpActivityModel() {
		resetSession();
		a = new AmpActivityVersion();
		AmpAuthWebSession s =  (AmpAuthWebSession) org.apache.wicket.Session.get();
		a.setActivityCreator(s.getAmpCurrentMember());
		a.setCreatedBy(s.getAmpCurrentMember());
		a.setCategories(new HashSet());
		a.setInternalIds(new HashSet());
		//a.setRegionalFundings(new HashSet());
		a.setLocations(new HashSet());
		a.setActPrograms(new HashSet());
		a.setSectors(new HashSet());
		a.setFunding(new HashSet());
		a.setOrgrole(new HashSet());
		a.setComponents(new HashSet());
		a.setIssues(new HashSet());
		a.setRegionalObservations(new HashSet());
		a.setActivityContacts(new HashSet());
		a.setMember(new HashSet());
	}	
	
	public AmpActivityModel(Long id) {
		resetSession();
		
		this.id = id;
		this.a = null;
	}
	
	private void resetSession() {
		AmpAuthWebSession s =  (AmpAuthWebSession) org.apache.wicket.Session.get();
		s.setMetaData(OnePagerConst.RESOURCES_NEW_ITEMS, null);
		s.setMetaData(OnePagerConst.RESOURCES_DELETED_ITEMS, null);
	}
	
	protected void initDBSession() {
		try {
			session = PersistenceManager.getSession();
			
		} catch (HibernateException e) {
			throw new RuntimeException(e);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	protected AmpActivityVersion load() {
		resetSession();

		AmpActivityVersion ret = null;
		if(session==null || !session.isOpen()) initDBSession();
		ret = (AmpActivityVersion) session.load(AmpActivityVersion.class, id);
		transaction=session.beginTransaction();
		return ret;
	}

	@Override   
	public void setObject(AmpActivityVersion arg0) {
 
		try {
			if(a==null) a=(AmpActivityVersion) arg0;
			if(session==null) initDBSession();
			
			if (transaction==null){
				logger.error("Transaction was null!");
				transaction = session.beginTransaction();
			}

			saveIndicators();
			saveResources();

			session.saveOrUpdate(a);
			transaction.commit();
			logger.error("Saved activity id=" + a.getAmpActivityId());
			transaction=session.beginTransaction();
			resetSession();
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

	private void saveResources() {
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

	private void saveIndicators() throws Exception {
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

	@Override
	public void detach() {

	}

	@Override
	public AmpActivityVersion getObject() {
		if (a == null)
			a = load();
		return a;
	}

}
