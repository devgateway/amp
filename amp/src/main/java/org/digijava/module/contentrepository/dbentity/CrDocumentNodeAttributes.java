package org.digijava.module.contentrepository.dbentity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.contentrepository.helper.ObjectReferringDocument;
import org.hibernate.Query;
import org.hibernate.Session;

public class CrDocumentNodeAttributes extends ObjectReferringDocument {
	private String publicVersionUUID;
	private Boolean publicDocument;
	
	public CrDocumentNodeAttributes() {
		publicVersionUUID			= null;
		publicDocument				= false;
	}
	
	public Boolean getPublicDocument() {
		return publicDocument;
	}
	public void setPublicDocument(Boolean publicDocument) {
		this.publicDocument = publicDocument;
	}
	public String getPublicVersionUUID() {
		return publicVersionUUID;
	}
	public void setPublicVersionUUID(String publicVersionUUID) {
		this.publicVersionUUID = publicVersionUUID;
	}
	
	
	/* Utility functions for CrDocumentNodeAttributes */
	private static Logger logger	= Logger.getLogger(CrDocumentNodeAttributes.class);
	
	public static HashMap<String,CrDocumentNodeAttributes> getPublicDocumentsMap (boolean keyIsVersionOfDocument) {
		HashMap<String, CrDocumentNodeAttributes> returnMap	= new HashMap<String, CrDocumentNodeAttributes>();
		List docAttributes									= getPublicDocuments();
		if (docAttributes != null) {
			Iterator iterator			= docAttributes.iterator();
			while( iterator.hasNext() ) {
				CrDocumentNodeAttributes docAttribute	= (CrDocumentNodeAttributes) iterator.next();
				if (keyIsVersionOfDocument)
						returnMap.put( docAttribute.getPublicVersionUUID(), docAttribute );
				else
						returnMap.put( docAttribute.getUuid(), docAttribute ) ;
			}
		}	
		return returnMap;
	}
	
	public static List getPublicDocuments() {
		Session hbSession							= null;
		List docAttributes							= null;
		try {
			hbSession			= PersistenceManager.getSession();
			String queryStr		= "SELECT a FROM " + CrDocumentNodeAttributes.class.getName() + " a WHERE a.publicDocument=:publicDocument" ;
			Query query			= hbSession.createQuery(queryStr);
			
			query.setBoolean("publicDocument", true);
			
			docAttributes		= query.list();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return docAttributes;
	}
	
	public static CrDocumentNodeAttributes getCrDocumentNodeAttributeByPublicVersionUUID(String publicVersionUUID) {
		Session hbSession							= null;
		CrDocumentNodeAttributes docNodeAtt			= null;
		try {
			hbSession			= PersistenceManager.getSession();
			String queryStr		= "SELECT a FROM " + CrDocumentNodeAttributes.class.getName() + " a WHERE a.publicVersionUUID=:uuid" ;
			Query query			= hbSession.createQuery(queryStr);
			
			query.setString("uuid", publicVersionUUID);
			
			docNodeAtt			= (CrDocumentNodeAttributes)query.uniqueResult();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return docNodeAtt;
	}
	
}
