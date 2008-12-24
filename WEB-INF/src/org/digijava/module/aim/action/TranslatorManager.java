package org.digijava.module.aim.action;
/*
* @ author Dan Mihaila
*/
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeFactory;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.dgfoundation.amp.te.ampte.jaxb.ObjectFactory;
import org.dgfoundation.amp.te.ampte.jaxb.Translations;
import org.dgfoundation.amp.te.ampte.jaxb.Trn;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.form.TranslatorManagerForm;
import org.digijava.module.aim.helper.TrnHashMap;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.mathcs.backport.java.util.Arrays;

public class TranslatorManager extends Action {
	private static Logger logger = Logger.getLogger(TranslatorManager.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
	{
		
		HttpSession session = request.getSession();
		if (session.getAttribute("ampAdmin") == null) {
			return mapping.findForward("index");
		} else {
			String str = (String)session.getAttribute("ampAdmin");
			if (str.equals("no")) {
					return mapping.findForward("index");
				}
			}
		
		TreeSet<String> languages= this.getPossibleLanguages();
		TranslatorManagerForm tMngForm=(TranslatorManagerForm) form;
		tMngForm.setLanguages(languages);
		
		if(request.getParameter("import")!=null)
		{
			logger.info("I am in import step");
			FormFile myFile = tMngForm.getFileUploaded();
	        byte[] fileData    = myFile.getFileData();
	        InputStream inputStream= new ByteArrayInputStream(fileData);
	        session.setAttribute("myFile",myFile);
	        
	        JAXBContext jc = JAXBContext.newInstance("org.dgfoundation.amp.te.ampte.jaxb");
	        Unmarshaller m = jc.createUnmarshaller();
	        org.dgfoundation.amp.te.ampte.jaxb.Translations trns_in;
	        //Translations trns_in;
	        TreeSet<String> languagesImport = new TreeSet<String>();
	        
	        
	        try {
				trns_in = (org.dgfoundation.amp.te.ampte.jaxb.Translations) m.unmarshal(inputStream);
				if (trns_in.getTrn() != null) {
					Iterator<Trn> it = trns_in.getTrn().iterator();
					while (it.hasNext()) {
						Trn element = it.next();
						languagesImport.add(element.getLangIso());
					}
					tMngForm.setImportedLanguages(languagesImport);

				}
			} catch (Exception ex) {
				logger.error("Exception : " + ex.getMessage());
				ex.printStackTrace(System.out);
				ActionErrors errors = new ActionErrors();
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
						"error.aim.importErrorFileContentTranslation"));
				
				saveErrors(request, errors);
				return mapping.findForward("forward");
			}
	        
	        
		}
		if(request.getParameter("importLang")!=null) {
			
			FormFile myFile = (FormFile)session.getAttribute("myFile");
			session.removeAttribute("myFile");
			if(myFile==null)
			{
				ActionErrors errors = new ActionErrors();
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.aim.importErrorFileContentTranslation"));				
				saveErrors(request, errors);
				return mapping.findForward("forward");
			}
			if(myFile.getFileData()==null) 
			{
				ActionErrors errors = new ActionErrors();
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.aim.importErrorFileContentTranslation"));				
				saveErrors(request, errors);
				return mapping.findForward("forward");
			}
	        byte[] fileData    = myFile.getFileData();
	        InputStream inputStream= new ByteArrayInputStream(fileData);
	        JAXBContext jc = JAXBContext.newInstance("org.dgfoundation.amp.te.ampte.jaxb");
	        Unmarshaller m = jc.createUnmarshaller();
	        Translations trns_in;
	        
	        //optimized code for parsing the xml file
	        ArrayList<TrnHashMap> trnHashMaps=new ArrayList<TrnHashMap>();
	        for(int i=0; i<tMngForm.getSelectedImportedLanguages().length;i++)
	        	{
	        		TrnHashMap tHashMap=new TrnHashMap();
	        		tHashMap.setLang(tMngForm.getSelectedImportedLanguages()[i]);
	        		trnHashMaps.add(tHashMap);
	        	}
	        try {
				trns_in = (Translations) m.unmarshal(inputStream);
				if (trns_in.getTrn() != null) {
					Iterator<Trn> it = trns_in.getTrn().iterator();
					while(it.hasNext())
					{
						Trn element = it.next();
						Iterator<TrnHashMap> itForLang=trnHashMaps.iterator();
						
						while(itForLang.hasNext())
						{
							TrnHashMap tHashMap=(TrnHashMap)itForLang.next();
							if(element.getLangIso().compareTo(tHashMap.getLang())==0)
								tHashMap.getTranslations().add(element);								
						}						
					}
				}
	        }catch (Exception ex) {
					logger.error("Exception : " + ex.getMessage());
					ex.printStackTrace(System.out);
					ActionErrors errors = new ActionErrors();
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.aim.importErrorFileContentTranslation"));					
					saveErrors(request, errors);
					return mapping.findForward("forward");
			}	        
	        
	        try {	
					if(tMngForm!=null && tMngForm.getSelectedImportedLanguages()!=null && tMngForm.getSelectedImportedLanguages().length>0)
					{
						//for every language read from file
						TreeSet<String> languagesFromSite= this.getPossibleLanguages();
						Iterator<String> itLangFromSite;
						for(int i=0; i<tMngForm.getSelectedImportedLanguages().length;i++)
						{
							itLangFromSite=languagesFromSite.iterator();
							boolean searchedLang=false;
							while(itLangFromSite.hasNext())
							{
								String langSearched=itLangFromSite.next();
								if(tMngForm.getSelectedImportedLanguages()[i].compareTo(langSearched)==0) searchedLang=true;
							}
							if(searchedLang==false && request.getParameter("LANG:"+tMngForm.getSelectedImportedLanguages()[i]).compareTo("update")==0)
							{
								ActionErrors errors = new ActionErrors();								
								errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.aim.updateErrorTranslation"));								
								saveErrors(request, errors);
								return mapping.findForward("forward");
							}	
						}
						
						for(int i=0; i<tMngForm.getSelectedImportedLanguages().length;i++)//************************ai aq unda shemowmdes is keywordebi
						{
							Iterator<TrnHashMap> it=trnHashMaps.iterator();
							while(it.hasNext())
							{	
								Map<String,Trn> translationsMap=null;
								TrnHashMap tHP=(TrnHashMap)it.next();
								if(tHP.getLang().compareTo(tMngForm.getSelectedImportedLanguages()[i])==0)
								{	
									//what was selected: overwrite,update or insert non-existing
									String actionName=request.getParameter("LANG:"+tMngForm.getSelectedImportedLanguages()[i]);
									List<String> keywords=tMngForm.getKeywords()==null?new ArrayList<String>():Arrays.asList(tMngForm.getKeywords());
									//if overwrite is selected , then we should delete all translations,except those,which have keywords!=null(both on local db or in file)
									if(actionName.compareTo("overwrite")==0){
										//build Map from translations
										translationsMap=buildMap(tHP.getTranslations());
										//filter and delete unused messages										
										removeUnusedMessages(translationsMap, tMngForm.getSelectedImportedLanguages()[i],keywords);
									}
									
									Iterator<Trn> trnsIterator=tHP.getTranslations().iterator();									
									
									while(trnsIterator.hasNext()){
										Trn element=(Trn) trnsIterator.next();
										
										Message msg= new Message();
										msg.setKey(element.getMessageKey());
										msg.setLocale(element.getLangIso());
										msg.setSiteId(element.getSiteId());
										msg.setMessage(element.getValue());
										msg.setCreated(new java.sql.Timestamp(element.getCreated().toGregorianCalendar().getTime().getTime()));//element.getCreated());
										msg.setKeyWords(element.getKeywords());
										if(element.getLastAccessed()!=null){
											msg.setLastAccessed(new java.sql.Timestamp(element.getLastAccessed().toGregorianCalendar().getTime().getTime()));
										}										
										//now overwrite,update or insert non-existing translation
										if(actionName.compareTo("nonexisting")==0){
											updateNonExistingTranslationMessage(msg,tMngForm.getSelectedImportedLanguages()[i]);
											logger.info("updating non existing...."+msg.getKey());
										}else if(actionName.compareTo("update")==0){											
											overrideOrUpdateTrns(msg,tMngForm.getSelectedImportedLanguages()[i],actionName,keywords);
											logger.info("updating new tr msg...."+msg.getKey());
										}else if(actionName.compareTo("overwrite")==0){											
											overrideOrUpdateTrns(msg,tMngForm.getSelectedImportedLanguages()[i],actionName,keywords);
											logger.info("inserting...."+msg.getKey());
										}
									}
								}
							}
						}
					}		
	        } catch (Exception ex) {
				logger.error("Exception : " + ex.getMessage());
				ex.printStackTrace(System.out);
				ActionErrors errors = new ActionErrors();
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.aim.importErrorFileContentTranslation"));				
				saveErrors(request, errors);
				return mapping.findForward("forward");
			}	        
			
			session.removeAttribute("aimTranslatorManagerForm");
			return mapping.findForward("forward");
		}
		if(request.getParameter("export")!=null)
		{
			if(tMngForm!=null && tMngForm.getSelectedLanguages()!=null && tMngForm.getSelectedLanguages().length>0)
			{
				logger.info("I am in Export step");
				JAXBContext jc = JAXBContext.newInstance("org.dgfoundation.amp.te.ampte.jaxb");
				Marshaller m = jc.createMarshaller();
				response.setContentType("text/xml");
				response.setHeader("content-disposition", "attachment; filename=exportLanguage.xml");
				ObjectFactory objFactory = new ObjectFactory();
				Translations trns_out = objFactory.createTranslations();
				Vector<Trn> rsAux=new Vector<Trn>();
				//trns_out.setSrc(request.getServerName() + request.getContextPath());
				
				for(int i=0; i<tMngForm.getSelectedLanguages().length;i++)
				{
					rsAux=getTranslationsForALanguage(tMngForm.getSelectedLanguages()[i]);
					trns_out.getTrn().addAll(rsAux);					
				}
				
				m.marshal(trns_out,response.getOutputStream());
				form=null;
				
				session.removeAttribute("aimTranslatorManagerForm");
				return null;
			}
			else {
				ActionErrors errors = new ActionErrors();
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.aim.pleaseChooseALanguageForExport"));
				saveErrors(request, errors);
				return mapping.findForward("forward");
			}
		}	
		
		return mapping.findForward("forward");
	}
	
	
	private TreeSet<String> getPossibleLanguages()
	{
		TreeSet<String> ret = new TreeSet<String>();
		Session session = null;
		String qryStr = null;		
		try{
			session	= PersistenceManager.getRequestDBSession();
			Query qry;
			qryStr	= "select m.locale from "+ Message.class.getName() + " m ";
			qry=session.createQuery(qryStr);
			Iterator<String> itr = qry.list().iterator();
			while (itr.hasNext()){
				String strAux=itr.next();
				ret.add(strAux);
			}	
		}
		catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
			ex.printStackTrace(System.out);
		} 
		return ret;
	}


	private Vector<Trn> getTranslationsForALanguage(String language)
	{
		Vector<Trn> ret	= new Vector<Trn>();
		Session session = null;
		String qryStr = null;
		ObjectFactory objFactory = new ObjectFactory();
		Query qry;		
		try{
				session	= PersistenceManager.getRequestDBSession();
				qryStr	= "select m from "+ Message.class.getName() + " m where (m.locale=:langIso)";
				qry= session.createQuery(qryStr);
				qry.setParameter("langIso", language, Hibernate.STRING);
				
				Iterator<Message> itr = qry.list().iterator();
				while (itr.hasNext()){
					Trn trnAux=objFactory.createTrn();
				
					Message msg= (Message)itr.next();
					trnAux.setMessageKey(msg.getKey());
					trnAux.setLangIso(msg.getLocale());
					trnAux.setSiteId(msg.getSiteId());					
					trnAux.setValue(msg.getMessage());
					Calendar cal_u = Calendar.getInstance();
					cal_u.setTime(msg.getCreated());
					trnAux.setCreated(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar(cal_u.get(Calendar.YEAR),cal_u.get(Calendar.MONTH),cal_u.get(Calendar.DAY_OF_MONTH),cal_u.get(Calendar.HOUR),cal_u.get(Calendar.MINUTE),cal_u.get(Calendar.SECOND))));
					if(msg.getKeyWords()!=null){
						trnAux.setKeywords(msg.getKeyWords());
					}					
					//last accessed
					if(msg.getLastAccessed()!=null){
						Calendar lastAccessed = Calendar.getInstance();
						lastAccessed.setTime(msg.getLastAccessed());						
						trnAux.setLastAccessed(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar(lastAccessed.get(Calendar.YEAR),lastAccessed.get(Calendar.MONTH),lastAccessed.get(Calendar.DAY_OF_MONTH),lastAccessed.get(Calendar.HOUR),lastAccessed.get(Calendar.MINUTE),lastAccessed.get(Calendar.SECOND))));						
					}					
//					else{
//						trnAux.setLastAccessed(DatatypeFactory.newInstance().newXMLGregorianCalendar());
//					}
					ret.add(trnAux);
				}

		}
		catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
			ex.printStackTrace(System.out);
		}		
		return ret;
	}
	
	/**
	 * Remove messages which have no keywords neither in local db, nor in imported file
	 * @author Dare
	 */
	private void removeUnusedMessages(Map<String,Trn> trnsMap,String lang,List<String> keywords) throws Exception{
		Collection<Message> dbMessages=null;
		Collection<Message> messagesToBeRemoved=null;
		try {
			dbMessages=getTranslationsForLang(lang);
			for (Message message : dbMessages) {
				if(trnsMap.get(message.getKey())==null || (!keywords.contains(message.getKeyWords()) && !keywords.contains(trnsMap.get(message.getKey()).getKeywords()))){
					if(messagesToBeRemoved==null){
						messagesToBeRemoved=new ArrayList<Message>();
					}
					messagesToBeRemoved.add(message);
				}				
			}
			//remove not useful messages
			for (Message message : messagesToBeRemoved) {
				deleteMessage(message);
			}
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}
	
	/**
	 * return all translations of the given language
	 */
	private List<Message> getTranslationsForLang(String lang){
		Session session = null;
		List<Message> messages=null;
		try{
			session=PersistenceManager.getRequestDBSession();
			Transaction tx=session.beginTransaction();
			Query query = session.createQuery("select m from " + Message.class.getName() + " m where m.locale =:locale");
			query.setString("locale", lang);
			messages=query.list();				
		}
		catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
			ex.printStackTrace(System.out);
		}
		return messages;
	}
	
	private void deleteMessage(Message message)	throws Exception{
		Session session=null;
		Transaction trans=null;
		try {
			session=PersistenceManager.getRequestDBSession();
			trans=session.beginTransaction();			
			session.delete(message);
			trans.commit();
		} catch (Exception ex) {
			if(trans!=null) {
				try {
					trans.rollback();					
				}catch(Exception e ) {
					logger.error("...Rollback failed");
					throw new AimException("Can't rollback", e);
				}			
			}
			logger.error("Exception : " + ex.getMessage());
			ex.printStackTrace(System.out);
		}
	}

	
	private void insertTranslationMessage(Object o)
	{
		Session session = null;
		Message msgLocal=(Message)o;
		try{
			session	= PersistenceManager.getRequestDBSession();
			Transaction tx=session.beginTransaction();
			session.save(msgLocal);
			tx.commit();
		}
		catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
			ex.printStackTrace(System.out);
		}
	}
	
	private void updateNonExistingTranslationMessage(Object o, String lang)
	{
		Session session = null;
		Message msgLocal=(Message)o;
		Query qry;
		String qryStr;
		
		try{
			session=PersistenceManager.getRequestDBSession();
			Transaction tx=session.beginTransaction();
			qryStr	= "select m from "	+ Message.class.getName() + " m where (m.key=:msgKey) and (m.locale=:langIso)";
			qry= session.createQuery(qryStr);
			qry.setParameter("msgKey", msgLocal.getKey(), Hibernate.STRING);
			qry.setParameter("langIso", lang, Hibernate.STRING);				
			if (qry.list().isEmpty()) {
				session.save(msgLocal);
			}
			tx.commit();
		}
		catch (Exception ex) {
			logger.error("Error...Inserting non existing messages... " + ex.getMessage());
			ex.printStackTrace(System.out);
		}
	}

	
	private void overrideOrUpdateTrns(Message msgLocal,String lang,String actionName,List<String>keyWords) throws Exception{
		Session session = null;		
		Query qry;
		String qryStr;		
		try{
			session = PersistenceManager.getRequestDBSession();
			Transaction tx=session.beginTransaction();
			qryStr	= "select m from "	+ Message.class.getName() + " m where (m.key=:msgKey) and (m.locale=:langIso)";
			qry= session.createQuery(qryStr);
			qry.setParameter("msgKey", msgLocal.getKey(), Hibernate.STRING);
			qry.setParameter("langIso", lang, Hibernate.STRING);
			
			if (qry.list().iterator().hasNext()) {
				Iterator<Message> itr = qry.list().iterator();
				while (itr.hasNext()) {
					Message msg = itr.next();
					if(!keyWords.contains(msgLocal.getKeyWords()) && !keyWords.contains(msg.getKeyWords())){
						boolean saveOrUpdate=false;
						if(actionName.equals("update")){
							if (msgLocal.getCreated().after(msg.getCreated())&& msgLocal.getLocale().compareTo(msg.getLocale()) == 0 || msg.getMessage().equalsIgnoreCase("")) {
								saveOrUpdate=true;								
							}
						}else if(actionName.equals("overwrite")){
							saveOrUpdate=true;							
						}
						if(saveOrUpdate){
							msg.setCreated(msgLocal.getCreated());
							msg.setMessage(msgLocal.getMessage());
							msg.setLastAccessed(msgLocal.getLastAccessed());
							msg.setKeyWords(msgLocal.getKeyWords());
							session.saveOrUpdate(msg);
						}
					}					
				}
				tx.commit();
			}else{
				logger.debug("New Key Found adding "+ msgLocal.getKey() + " to local db");
				Message msg= new Message();
				msg.setKey(msgLocal.getKey());
				msg.setLocale(lang);
				msg.setSiteId(msgLocal.getSiteId());
				msg.setMessage(msgLocal.getMessage().trim());
				msg.setCreated(new java.sql.Timestamp(msgLocal.getCreated().getTime()));
				insertTranslationMessage(msg);
				
			}
		}
		catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
			ex.printStackTrace(System.out);
		}
	}
	
	/**
	 * builds map from Trn objects with key=translation's key and value=translation
	 * @param translations
	 * @author dare
	 */
	private Map<String,Trn> buildMap(ArrayList<Trn> translations){
		Map<String,Trn> myMap=new HashMap<String, Trn>();
		for (Trn trn : translations) {
			myMap.put(trn.getMessageKey(), trn);
		}
		return myMap;
	}
}