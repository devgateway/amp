package org.digijava.module.aim.action;
/*
* @ author Dan Mihaila
* co-author dare :D :D
*/
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
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
import org.digijava.kernel.translator.CachedTranslatorWorker;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.form.TranslatorManagerForm;
import org.digijava.module.aim.helper.TrnHashMap;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;

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
	        Translations trns_in;	        
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
						
						for(int i=0; i<tMngForm.getSelectedImportedLanguages().length;i++)	{
							Iterator<TrnHashMap> it=trnHashMaps.iterator();
							while(it.hasNext())
							{									
								TrnHashMap tHP=(TrnHashMap)it.next();
								if(tHP.getLang().compareTo(tMngForm.getSelectedImportedLanguages()[i])==0)
								{	
									//what was selected: overwrite,update or insert non-existing
									String actionName=request.getParameter("LANG:"+tMngForm.getSelectedImportedLanguages()[i]);
									//what to do with translations,which have keywords that were typed by user on import page.
									String skipOrUpdateTrnsWithKeywords=tMngForm.getSkipOrUpdateTrnsWithKeywords();
									List<String> keywords=tMngForm.getKeywords()==null?new ArrayList<String>():Arrays.asList(tMngForm.getKeywords());
									
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
											updateNonExistingTranslationMessage(msg,tMngForm.getSelectedImportedLanguages()[i],request);
											logger.info("updating non existing...."+msg.getKey());
										}else if(actionName.compareTo("update")==0){											
											overrideOrUpdateTrns(msg,tMngForm.getSelectedImportedLanguages()[i],actionName,keywords,skipOrUpdateTrnsWithKeywords,request);
											logger.info("updating new tr msg...."+msg.getKey());
										}else if(actionName.compareTo("overwrite")==0){											
											overrideOrUpdateTrns(msg,tMngForm.getSelectedImportedLanguages()[i],actionName,keywords,skipOrUpdateTrnsWithKeywords,request);
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
	
	
	private TreeSet<String> getPossibleLanguages()	{
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


	private Vector<Trn> getTranslationsForALanguage(String language){
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
	
	private void updateNonExistingTranslationMessage(Message msgLocal, String lang,HttpServletRequest request){
		CachedTranslatorWorker trnWorker=(CachedTranslatorWorker)TranslatorWorker.getInstance("");
		String siteId = RequestUtils.getSite(request).getId().toString();
		try {
			Message dbMessage=trnWorker.getByKey(msgLocal.getKey(), lang, siteId,false,null);
			if(dbMessage==null){
				trnWorker.save(msgLocal);
				logger.info("updated");
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}		
	}
	
	
	private void overrideOrUpdateTrns(Message msgLocal,String lang,String actionName,List<String>keyWords,String skipOrUpdateTrnsWithKeywords,HttpServletRequest request) throws Exception{
		CachedTranslatorWorker trnWorker=(CachedTranslatorWorker)TranslatorWorker.getInstance("");
		String siteId = RequestUtils.getSite(request).getId().toString();
		//get message from cache,if exists.
		Message dbMessage=trnWorker.getByKey(msgLocal.getKey(), lang, siteId,false,null);
		if(dbMessage!=null){
			boolean gotoNextCheck=false;
			if(skipOrUpdateTrnsWithKeywords.equalsIgnoreCase("skip") && !keyWords.contains(msgLocal.getKeyWords()) && !keyWords.contains(dbMessage.getKeyWords()) ){
				gotoNextCheck=true;
			}else if( skipOrUpdateTrnsWithKeywords.equalsIgnoreCase("update") && (keyWords.contains(msgLocal.getKeyWords()) || keyWords.contains(dbMessage.getKeyWords()) ) ){
				gotoNextCheck=true;
			}else if(skipOrUpdateTrnsWithKeywords.equalsIgnoreCase("updateEverything")){
				gotoNextCheck=true;
			}
			
			if(gotoNextCheck){
				boolean saveOrUpdate=false;
				if(actionName.equals("update")){
					if (msgLocal.getCreated().after(dbMessage.getCreated())&& msgLocal.getLocale().compareTo(dbMessage.getLocale()) == 0 || dbMessage.getMessage().equalsIgnoreCase("")) {
						saveOrUpdate=true;								
					}
				}else if(actionName.equals("overwrite")){
					saveOrUpdate=true;							
				}
				if(saveOrUpdate){
					dbMessage.setCreated(msgLocal.getCreated());
					dbMessage.setMessage(msgLocal.getMessage());
					dbMessage.setLastAccessed(msgLocal.getLastAccessed());
					dbMessage.setKeyWords(msgLocal.getKeyWords());					
					//this will update both:cache and db
					trnWorker.update(dbMessage);
				}
			}
		}else{
			boolean insertNewMsg=true;
			if(skipOrUpdateTrnsWithKeywords.equalsIgnoreCase("update") && !keyWords.contains(msgLocal.getKeyWords())){
				//if we have skipOrUpdateTrnsWithKeywords=update case,this means that only messages with keywords(that match user's typed ones) should be inserted
				insertNewMsg=false;
			}
			if(insertNewMsg){
				logger.debug("New Key Found adding "+ msgLocal.getKey() + " to local db");
				Message msg= new Message();
				msg.setKey(msgLocal.getKey());
				msg.setLocale(lang);
				msg.setSiteId(msgLocal.getSiteId());
				msg.setMessage(msgLocal.getMessage().trim());
				msg.setCreated(new java.sql.Timestamp(msgLocal.getCreated().getTime()));
				msg.setKeyWords(msgLocal.getKeyWords());				
				//this will update both:cache and db
				trnWorker.save(msg);
			}
		}
	}
}