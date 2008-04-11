package org.digijava.module.aim.action;
/*
* @ author Dan Mihaila
*/
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.dgfoundation.amp.te.ampte.ObjectFactory;
import org.dgfoundation.amp.te.ampte.Translations;
import org.dgfoundation.amp.te.ampte.Trn;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.form.TranslatorManagerForm;

public class TranslatorManager extends Action {
	private static Logger logger = Logger.getLogger(TranslatorManager.class);
	public ActionForward execute(ActionMapping mapping, ActionForm form, 
	HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
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
		
		TreeSet languages= this.getPossibleLanguages();
		TranslatorManagerForm tMngForm=(TranslatorManagerForm) form;
		tMngForm.setLanguages(languages);
		
		if(request.getParameter("import")!=null)
		{
			logger.info("I am in import step");
			FormFile myFile = tMngForm.getFileUploaded();
	        byte[] fileData    = myFile.getFileData();
	        InputStream inputStream= new ByteArrayInputStream(fileData);
	        session.setAttribute("myFile",myFile);
	        
	        JAXBContext jc = JAXBContext
			.newInstance("org.dgfoundation.amp.te.ampte");
	        Unmarshaller m = jc.createUnmarshaller();
	        Translations trns_in;
	        TreeSet languagesImport = new TreeSet();
	        
	        
	        try {
				trns_in = (Translations) m.unmarshal(inputStream);
				if (trns_in.getTrn() != null) {
					Iterator it = trns_in.getTrn().iterator();
					while (it.hasNext()) {
						Trn element = (Trn) it.next();
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
		if(request.getParameter("importLang")!=null)
		{
			
			FormFile myFile = (FormFile)session.getAttribute("myFile");
			session.removeAttribute("myFile");
			if(myFile==null)
			{
				ActionErrors errors = new ActionErrors();
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
						"error.aim.importErrorFileContentTranslation"));
				
				saveErrors(request, errors);
				return mapping.findForward("forward");
			}
			if(myFile.getFileData()==null) 
			{
				ActionErrors errors = new ActionErrors();
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
						"error.aim.importErrorFileContentTranslation"));
				
				saveErrors(request, errors);
				return mapping.findForward("forward");
			}
	        byte[] fileData    = myFile.getFileData();
	        InputStream inputStream= new ByteArrayInputStream(fileData);
	        JAXBContext jc = JAXBContext
			.newInstance("org.dgfoundation.amp.te.ampte");
	        Unmarshaller m = jc.createUnmarshaller();
	        Translations trns_in;
	        
	        //optimized code for parsing the xml file
	        ArrayList trnHashMaps=new ArrayList();
	        for(int i=0; i<tMngForm.getSelectedImportedLanguages().length;i++)
	        	{
	        		TrnHashMap tHashMap=new TrnHashMap();
	        		tHashMap.setLang(tMngForm.getSelectedImportedLanguages()[i]);
	        		trnHashMaps.add(tHashMap);
	        	}
	        try {
				trns_in = (Translations) m.unmarshal(inputStream);
				if (trns_in.getTrn() != null) {
					Iterator it = trns_in.getTrn().iterator();
					while(it.hasNext())
					{
						Trn element = (Trn) it.next();
						Iterator itForLang=trnHashMaps.iterator();
						
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
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"error.aim.importErrorFileContentTranslation"));
					
					saveErrors(request, errors);
					return mapping.findForward("forward");
			}
	        
	        
	        try {
					
					
					if(tMngForm!=null && tMngForm.getSelectedImportedLanguages()!=null && tMngForm.getSelectedImportedLanguages().length>0)
					{
						//for every language read from file
						TreeSet languagesFromSite= this.getPossibleLanguages();
						Iterator itLangFromSite;
						for(int i=0; i<tMngForm.getSelectedImportedLanguages().length;i++)
						{
							itLangFromSite=languagesFromSite.iterator();
							boolean searchedLang=false;
							while(itLangFromSite.hasNext())
							{
								String langSearched=(String)itLangFromSite.next();
								if(tMngForm.getSelectedImportedLanguages()[i].compareTo(langSearched)==0) searchedLang=true;
							}
							if(searchedLang==false && request.getParameter("LANG:"+tMngForm.getSelectedImportedLanguages()[i]).compareTo("update")==0)
							{
								ActionErrors errors = new ActionErrors();
								
								errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
										"error.aim.updateErrorTranslation"));
								
								saveErrors(request, errors);
								return mapping.findForward("forward");
							}	
						}
						
						for(int i=0; i<tMngForm.getSelectedImportedLanguages().length;i++)
						{
							Iterator it=trnHashMaps.iterator();
							//if update
							if(request.getParameter("LANG:"+tMngForm.getSelectedImportedLanguages()[i]).compareTo("update")==0)
								while(it.hasNext())
								{
									TrnHashMap tHP=(TrnHashMap)it.next();
									if(tHP.getLang().compareTo(tMngForm.getSelectedImportedLanguages()[i])==0)
									{
										Iterator iti=tHP.getTranslations().iterator();
										while(iti.hasNext())
										{
											Trn element=(Trn) iti.next();
										
											Message msg= new Message();
											msg.setKey(element.getMessageKey());
											msg.setLocale(element.getLangIso());
											msg.setSiteId(element.getSiteId());
											msg.setMessage(element.getValue());
											msg.setCreated(new java.sql.Timestamp(element.getCreated().getTime().getTime()));//element.getCreated());
											this.updateNewTranslationMessage(msg,tMngForm.getSelectedImportedLanguages()[i]);
											
										}
									}
								}
							else 
								if(request.getParameter("LANG:"+tMngForm.getSelectedImportedLanguages()[i]).compareTo("overwrite")==0)
								{
									deleteTransForLang(tMngForm.getSelectedImportedLanguages()[i]);
									while(it.hasNext())
									{
										TrnHashMap tHP=(TrnHashMap)it.next();
										if(tHP.getLang().compareTo(tMngForm.getSelectedImportedLanguages()[i])==0)
										{
											Iterator iti=tHP.getTranslations().iterator();
											while(iti.hasNext())
											{
												Trn element = (Trn) iti.next();
												Message msg= new Message();
												msg.setKey(element.getMessageKey());
												msg.setLocale(element.getLangIso());
												msg.setSiteId(element.getSiteId());
												msg.setMessage(element.getValue());
												msg.setCreated(new java.sql.Timestamp(element.getCreated().getTime().getTime()));
												insertTranslationMessage(msg);
											}
										}
									}
									
								}
							//ifoverwrite
							
							
						}
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
	        
			
			session.removeAttribute("aimTranslatorManagerForm");
			return mapping.findForward("forward");
		}
		if(request.getParameter("export")!=null)
		{
			if(tMngForm!=null && tMngForm.getSelectedLanguages()!=null && tMngForm.getSelectedLanguages().length>0)
			{
				logger.info("I am in Export step");
				JAXBContext jc = JAXBContext.newInstance("org.dgfoundation.amp.te.ampte");
				Marshaller m = jc.createMarshaller();
				response.setContentType("text/xml");
				response.setHeader("content-disposition", "attachment; filename=exportLanguage.xml");
				ObjectFactory objFactory = new ObjectFactory();
				Translations trns_out = objFactory.createTranslations();
				Vector rsAux=new Vector();
				trns_out.setSrc(request.getServerName() + request.getContextPath());
				
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
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
						"error.aim.pleaseChooseALanguageForExport"));
				saveErrors(request, errors);
				return mapping.findForward("forward");
			}
		}
	
		
		return mapping.findForward("forward");
	}
	
	
	private TreeSet getPossibleLanguages()
	{
		TreeSet ret 	= new TreeSet();
		Session session = null;
		String qryStr = null;
		
		try{
				session				= PersistenceManager.getSession();
				Query qry;
				qryStr	= "select m.locale from "
					+ Message.class.getName() + " m ";
				qry=session.createQuery(qryStr);
				Iterator itr = qry.list().iterator();
				while (itr.hasNext()){
					String strAux=(String)itr.next();
					ret.add(strAux);
				}
				
		}
		catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
			ex.printStackTrace(System.out);
		} 
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
		return ret;
	}


	private Vector getTranslationsForALanguage(String language)
	{
		Vector ret 	= new Vector();
		Session session = null;
		String qryStr = null;
		ObjectFactory objFactory = new ObjectFactory();
		Query qry;
		
		try{
				session				= PersistenceManager.getSession();
				qryStr	= "select m from "
					+ Message.class.getName() + " m "
					+ "where (m.locale=:langIso)";
				qry= session.createQuery(qryStr);
				qry.setParameter("langIso", language, Hibernate.STRING);
				
				Iterator itr = qry.list().iterator();
				while (itr.hasNext()){
					Trn trnAux=objFactory.createTrn();
				
					Message msg= (Message)itr.next();
					trnAux.setMessageKey(msg.getKey());
					trnAux.setLangIso(msg.getLocale());
					trnAux.setSiteId(msg.getSiteId());
					trnAux.setMessageUtf8("");
					trnAux.setValue(msg.getMessage());
					Calendar cal_u = Calendar.getInstance();
					cal_u.setTime(msg.getCreated());
					trnAux.setCreated(cal_u);
					ret.add(trnAux);
				}
				

		}
		catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
			ex.printStackTrace(System.out);
		} 
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
		
		return ret;
	}
	
	private void deleteTransForLang(String lang)
	{
		Session session = null;
		try{
				session				= PersistenceManager.getSession();
				Transaction tx=session.beginTransaction();
				int i=session.delete("from "
						+ Message.class.getName() + " m where m.locale = ?",lang,Hibernate.STRING);

				tx.commit();
				//session.close();
				
		}
		catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
			ex.printStackTrace(System.out);
		} 
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
	}
	
	private void insertTranslationMessage(Object o)
	{
		Session session = null;
		Message msgLocal=(Message)o;
		try{
				session	= PersistenceManager.getSession();
				Transaction tx=session.beginTransaction();
				session.save(msgLocal);
				tx.commit();
				//session.close();
				
		}
		catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
			ex.printStackTrace(System.out);
		} 
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
	}

	private void updateNewTranslationMessage(Object o, String lang)
	{
		Session session = null;
		Message msgLocal=(Message)o;
		Query qry;
		String qryStr;
		
		try{
				session				= PersistenceManager.getSession();
				Transaction tx=session.beginTransaction();
				qryStr	= "select m from "
					+ Message.class.getName() + " m "
					+ "where (m.key=:msgKey) and (m.locale=:langIso)";
				qry= session.createQuery(qryStr);
				qry.setParameter("msgKey", msgLocal.getKey(), Hibernate.STRING);
				qry.setParameter("langIso", lang, Hibernate.STRING);
				
				if (qry.list().iterator().hasNext()) {
				Iterator itr = qry.list().iterator();
				while (itr.hasNext()) {
					Message msg = (Message) itr.next();

					if (msgLocal.getCreated().after(msg.getCreated())
							&& msgLocal.getLocale().compareTo(msg.getLocale()) == 0 || msg.getMessage().equalsIgnoreCase("")) {
						msg.setCreated(msgLocal.getCreated());
						msg.setMessage(msgLocal.getMessage());
						session.saveOrUpdate(msg);

					}
				}
				tx.commit();
				session.close();

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
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
	}
	
}