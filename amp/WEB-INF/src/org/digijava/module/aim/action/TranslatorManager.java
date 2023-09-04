package org.digijava.module.aim.action;
/*
* @ author Dan Mihaila
* co-author dare :D :D
*/

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.apache.struts.upload.FormFile;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.translator.CachedTranslatorWorker;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.form.TranslatorManagerForm;
import org.digijava.module.aim.helper.TrnHashMap;
import org.digijava.module.translation.entity.MessageGroup;
import org.digijava.module.translation.jaxb.Language;
import org.digijava.module.translation.jaxb.ObjectFactory;
import org.digijava.module.translation.jaxb.Translations;
import org.digijava.module.translation.jaxb.Trn;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

//TODO replaced with ImportExportTranslations.java in translator module. Remove this file. see AMP-9085
@Deprecated
public class TranslatorManager extends Action {
    private static Logger logger = Logger.getLogger(TranslatorManager.class);
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        
        HttpSession session = request.getSession();
        if (session.getAttribute("ampAdmin") == null) {
            return mapping.findForward("index");
        } else {
            String str = (String)session.getAttribute("ampAdmin");
            if (str.equals("no")) {
                    return mapping.findForward("index");
                }
            }
        
        // TreeSet<String> languages= this.getPossibleLanguages();
        List<String> languages = TranslatorWorker.getAllUsedLanguages();
        TranslatorManagerForm tMngForm=(TranslatorManagerForm) form;
        tMngForm.setLanguages(languages);
        
        if (request.getParameter("import") != null) {
            logger.info("I am in import step");
            FormFile myFile = tMngForm.getFileUploaded();
            byte[] fileData    = myFile.getFileData();
            InputStream inputStream= new ByteArrayInputStream(fileData);
            session.setAttribute("myFile",myFile);
            
            JAXBContext jc = JAXBContext.newInstance("org.digijava.module.translation.jaxb");
            Unmarshaller m = jc.createUnmarshaller();
            Translations trns_in;           
            List<String> languagesImport = new ArrayList<String>();
            
            try {
                trns_in = (Translations) m.unmarshal(inputStream);
                if (trns_in.getTrn() != null) {
                    for (Trn element : trns_in.getTrn()) {
                        List<Language> langs = element.getLang();
                        // translation must have at least one language,so this
                        // list can't be null.
                        for (Language lang : langs) {
                            languagesImport.add(lang.getCode());
                        }
                    }
                    tMngForm.setImportedLanguages(languagesImport);

                }
            } catch (Exception ex) {
                logger.error("Exception : " + ex.getMessage());
                ex.printStackTrace(System.out);
                ActionMessages errors = new ActionMessages();
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.aim.importErrorFileContentTranslation"));                
                saveErrors(request, errors);
                return mapping.findForward("forward");
            }           
            
        }
        if(request.getParameter("importLang")!=null) {
            FormFile myFile = (FormFile)session.getAttribute("myFile");
            session.removeAttribute("myFile");
            if (myFile == null) {
                ActionMessages errors = new ActionMessages();
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.aim.importErrorFileContentTranslation"));                
                saveErrors(request, errors);
                return mapping.findForward("forward");
            }
            if (myFile.getFileData() == null) {
                ActionMessages errors = new ActionMessages();
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.aim.importErrorFileContentTranslation"));                
                saveErrors(request, errors);
                return mapping.findForward("forward");
            }
            byte[] fileData    = myFile.getFileData();
            InputStream inputStream= new ByteArrayInputStream(fileData);
            JAXBContext jc = JAXBContext.newInstance("org.digijava.module.translation.jaxb");
            Unmarshaller m = jc.createUnmarshaller();
            Translations trns_in;
            
            //optimized code for parsing the xml file
            ArrayList<TrnHashMap> trnHashMaps=new ArrayList<TrnHashMap>();
            for (int i = 0; i < tMngForm.getSelectedImportedLanguages().length; i++) {
                    TrnHashMap tHashMap=new TrnHashMap();
                    tHashMap.setLang(tMngForm.getSelectedImportedLanguages()[i]);
                    trnHashMaps.add(tHashMap);
                }           
            try {
                trns_in = (Translations) m.unmarshal(inputStream);
                if (trns_in.getTrn() != null) {
                    logger.info("Processing "+trns_in.getTrn().size()+" translation groups (trn tags)...");
//                  Iterator<Trn> it = trns_in.getTrn().iterator();                 
//                  while (it.hasNext() && (it == null)) {  DEAD CODE
//                      Trn trn = it.next();                        
//                      MessageGroup msgGroup=new MessageGroup(trn);
//                      
//                      Iterator<TrnHashMap> itForLang=trnHashMaps.iterator();
//                      while(itForLang.hasNext())  {
//                          TrnHashMap tanslationsHashMap = itForLang.next();
//                          // get requested languge's translation from the
//                          // msgGroup
//                          Message message=msgGroup.getMessageByLocale(tanslationsHashMap.getLang());
//                          if(message!=null){
//                              tanslationsHashMap.getTranslations().add(message);
//                          }
//                      }                       
//                  }
                }
            }catch (Exception ex) {
                    logger.error("Exception : " + ex.getMessage());
                    ex.printStackTrace(System.out);
                    ActionMessages errors = new ActionMessages();
                    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.aim.importErrorFileContentTranslation"));                    
                    saveErrors(request, errors);
                    return mapping.findForward("forward");
            }           
            
            try {   
                if (tMngForm.getSelectedImportedLanguages() != null
                        && tMngForm.getSelectedImportedLanguages().length > 0) {
                        //for every language read from file
                    List<String> languagesFromSite = TranslatorWorker.getAllUsedLanguages();
                        Iterator<String> itLangFromSite;
                    for (int i = 0; i < tMngForm.getSelectedImportedLanguages().length; i++) {
                            itLangFromSite=languagesFromSite.iterator();
                            boolean searchedLang=false;
                        while (itLangFromSite.hasNext()) {
                                String langSearched=itLangFromSite.next();
                            if (tMngForm.getSelectedImportedLanguages()[i].compareTo(langSearched) == 0)
                                searchedLang = true;
                            }
                        if (!searchedLang &&
                                request.getParameter("LANG:" + tMngForm.getSelectedImportedLanguages()[i]).compareTo("update") == 0) {
                                ActionMessages errors = new ActionMessages();                               
                                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.aim.updateErrorTranslation"));                               
                                saveErrors(request, errors);
                                return mapping.findForward("forward");
                            }   
                        }
                        
                        for(int i=0; i<tMngForm.getSelectedImportedLanguages().length;i++)  {
                            for (TrnHashMap tHP : trnHashMaps) {
                                if (tHP.getLang().compareTo(tMngForm.getSelectedImportedLanguages()[i]) == 0) {
                                    // what was selected: overwrite,update or insert
                                    // non-existing
                                    String actionName = request.getParameter("LANG:" + tMngForm.getSelectedImportedLanguages()[i]);
                                    // what to do with translations,which have
                                    // keywords that were typed by user on import
                                    // page.
                                    String skipOrUpdateTrnsWithKeywords = tMngForm.getSkipOrUpdateTrnsWithKeywords();
                                    List<String> keywords = null;
                                    if (tMngForm.getKeywords() == null) {
                                        keywords = new ArrayList<String>();
                                    } else {
                                        keywords = Arrays.asList(tMngForm.getKeywords());
                                    }

                                    for (Message message : (Iterable<Message>) tHP.getTranslations()) {
                                        // now overwrite,update or insert
                                        // non-existing translation
                                        if (actionName.compareTo("nonexisting") == 0) {
                                            updateNonExistingTranslationMessage(message, tMngForm.getSelectedImportedLanguages()[i], request);
                                            logger.info("updating non existing...." + message.getKey());
                                        } else if (actionName.compareTo("update") == 0) {
                                            overrideOrUpdateTrns(message, tMngForm.getSelectedImportedLanguages()[i], actionName, keywords, skipOrUpdateTrnsWithKeywords, request);
                                            logger.info("updating new tr msg...." + message.getKey());
                                        } else if (actionName.compareTo("overwrite") == 0) {
                                            overrideOrUpdateTrns(message, tMngForm.getSelectedImportedLanguages()[i], actionName, keywords, skipOrUpdateTrnsWithKeywords, request);
                                            logger.info("inserting...." + message.getKey());
                                        }
                                    }
                                }
                            }
                        }
                    }       
            } catch (Exception ex) {
                logger.error("Exception : " + ex.getMessage());
                ex.printStackTrace(System.out);
                ActionMessages errors = new ActionMessages();
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.aim.importErrorFileContentTranslation"));                
                saveErrors(request, errors);
                return mapping.findForward("forward");
            }           
            long timeEnd=System.currentTimeMillis();
            logger.info("Import finished in "+(timeEnd - timeEnd)+" milliseconds.");
            session.removeAttribute("aimTranslatorManagerForm");
            return mapping.findForward("forward");
        }
        
        if(request.getParameter("export")!=null) {
            if (tMngForm != null && tMngForm.getSelectedLanguages() != null && tMngForm.getSelectedLanguages().length > 0) {
                logger.info("I am in Export step");
                JAXBContext jc = JAXBContext.newInstance("org.digijava.module.translation.jaxb");
                Marshaller m = jc.createMarshaller();
                response.setContentType("text/xml");
                response.setHeader("content-disposition", "attachment; filename=exportLanguage.xml");
                ObjectFactory objFactory = new ObjectFactory();
                Translations trns_out = objFactory.createTranslations();
                Vector<Trn> rsAux=new Vector<Trn>();
                
                Map<String,MessageGroup> messageGroups=new HashMap<String, MessageGroup>();
                for(int i=0; i<tMngForm.getSelectedLanguages().length;i++)  {
                    fillMapWithTranslationsForALanguage(tMngForm.getSelectedLanguages()[i],messageGroups);                                      
                }
                rsAux=getTranslationsFromMessageGroup(messageGroups);
                trns_out.getTrn().addAll(rsAux);
                
                m.marshal(trns_out,response.getOutputStream());
                form=null;
                
                session.removeAttribute("aimTranslatorManagerForm");
                return null;
            } else {
                ActionMessages errors = new ActionMessages();
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.aim.pleaseChooseALanguageForExport"));
                saveErrors(request, errors);
                return mapping.findForward("forward");
            }
        }   
        
        return mapping.findForward("forward");
    }

    private Vector<Trn> getTranslationsFromMessageGroup(Map<String, MessageGroup> messageGroups){
        Vector<Trn> result=new Vector<Trn>();
        for(Map.Entry<String, MessageGroup> entry: messageGroups.entrySet()){
            try {
                Trn trn=entry.getValue().createTrn();
                result.add(trn);
            } catch (Exception e) {
                e.printStackTrace();
            }           
        }       
        return result;
    }
    
    private void fillMapWithTranslationsForALanguage(String language,Map<String,MessageGroup> map){     
        Session session = null;
        String qryStr = null;       
        Query qry;      
        try{
                session = PersistenceManager.getRequestDBSession();
                qryStr  = "select m from "+ Message.class.getName() + " m where m.locale='"+language+"'";
                qry= session.createQuery(qryStr);
            for (Message message : (Iterable<Message>) qry.list()) {
                // MessageGroup class key is equal to the message key, from
                // which it was created
                MessageGroup msgGroup = map.get(message.getKey());
                if (msgGroup == null) {
                    msgGroup = new MessageGroup(message);
                } else {
                    msgGroup.addMessage(message);
                }
                map.put(msgGroup.getKey(), msgGroup);
            }
        } catch (Exception ex) {
            logger.error("Exception : " + ex.getMessage());
            ex.printStackTrace(System.out);
        }
    }
    
    private void updateNonExistingTranslationMessage(Message msgLocal, String lang,HttpServletRequest request){
        CachedTranslatorWorker trnWorker=(CachedTranslatorWorker)TranslatorWorker.getInstance("");
        Long siteId = Objects.requireNonNull(RequestUtils.getSite(request)).getId();
        try {
            Message dbMessage=trnWorker.getByKey(msgLocal.getKey(), lang, siteId, false, null);
            if(dbMessage==null){
                trnWorker.save(msgLocal);
                logger.info("updated");
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }       
    }
    
    private void overrideOrUpdateTrns(
            Message msgLocal, 
            String lang, 
            String actionName, 
            List<String> keyWords, 
            String skipOrUpdateTrnsWithKeywords, 
            HttpServletRequest request
            ) throws Exception {
    
        CachedTranslatorWorker trnWorker=(CachedTranslatorWorker)TranslatorWorker.getInstance("");
        Long siteId = Objects.requireNonNull(RequestUtils.getSite(request)).getId();
        //get message from cache,if exists.
        Message dbMessage=trnWorker.getByKey(msgLocal.getKey(), lang, siteId, false, null);
        if(dbMessage!=null){
            boolean gotoNextCheck=false;
            if (
                    skipOrUpdateTrnsWithKeywords.equalsIgnoreCase("skip")
                    && !keyWords.contains(msgLocal.getKeyWords())
                    && !keyWords.contains(dbMessage.getKeyWords())
                ) {
                gotoNextCheck=true;
            } else if (
                    skipOrUpdateTrnsWithKeywords.equalsIgnoreCase("update")
                    && (keyWords.contains(msgLocal.getKeyWords()) || keyWords.contains(dbMessage.getKeyWords()))
                    ) {
                gotoNextCheck=true;
            }else if(skipOrUpdateTrnsWithKeywords.equalsIgnoreCase("updateEverything")){
                gotoNextCheck=true;
            }
            
            if(gotoNextCheck){
                boolean saveOrUpdate=false;
                if(actionName.equals("update")){
                    if (
                            msgLocal.getCreated().after(dbMessage.getCreated())
                            && msgLocal.getLocale().compareTo(dbMessage.getLocale()) == 0 
                            || dbMessage.getMessage().equalsIgnoreCase("")
                        ) {
                        saveOrUpdate=true;                              
                    }
                }else if(actionName.equals("overwrite")){
                    saveOrUpdate=true;                          
                }
                if(saveOrUpdate){
                    dbMessage.setCreated(msgLocal.getCreated());
                    dbMessage.setMessage(msgLocal.getMessage());
                    //dbMessage.setLastAccessed(msgLocal.getLastAccessed());
                    dbMessage.setKeyWords(msgLocal.getKeyWords());                  
                    //this will update both:cache and db
                    trnWorker.update(dbMessage);
                }
            }
        }else{
            boolean insertNewMsg= !skipOrUpdateTrnsWithKeywords.equalsIgnoreCase("update") || keyWords.contains(msgLocal.getKeyWords());
            // if we have skipOrUpdateTrnsWithKeywords=update case,this
            // means that only messages with keywords(that match user's
            // typed ones) should be inserted
            if(insertNewMsg){
                logger.debug("New Key Found adding "+ msgLocal.getKey() + " to local db");
                Message msg= new Message();
                msg.setKey(msgLocal.getKey());
                msg.setLocale(lang);
                msg.setSiteId(msgLocal.getSiteId());
                msg.setMessage(msgLocal.getMessage().trim());
                msg.setCreated(msgLocal.getCreated());
                msg.setKeyWords(msgLocal.getKeyWords());                
                //this will update both:cache and db
                trnWorker.save(msg);
            }
        }
    }
}
