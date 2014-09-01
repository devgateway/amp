package org.digijava.module.dataExchange.action;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.kernel.request.TLSUtils;

import org.digijava.module.dataExchange.dbentity.AmpDEUploadSession;
import org.digijava.module.dataExchange.dbentity.DELogPerExecution;
import org.digijava.module.dataExchange.dbentity.DELogPerItem;
import org.digijava.module.dataExchange.dbentity.DEMappingFields;
import org.digijava.module.dataExchange.dbentity.DESourceSetting;
import org.digijava.module.dataExchange.dbentity.IatiCodeType;

import org.digijava.module.dataExchange.engine.DEImportBuilder;
import org.digijava.module.dataExchange.engine.SourceBuilder;
import org.digijava.module.dataExchange.form.ImportFormNew;

import org.digijava.module.dataExchange.iati.IatiRules;
import org.digijava.module.dataExchange.iati.IatiVersion;

import org.digijava.module.dataExchangeIATI.iatiSchema.v1_03.jaxb.*;

import org.digijava.module.dataExchange.pojo.DEImportItem;
import org.digijava.module.dataExchange.pojo.DEImportValidationEventHandler;

import org.digijava.module.dataExchange.util.DataExchangeConstants.IatiCodeTypeEnum;
import org.digijava.module.dataExchange.util.DbUtil;
import org.digijava.module.dataExchange.util.IatiHelper;
import org.digijava.module.dataExchange.util.SessionSourceSettingDAO;
import org.digijava.module.dataExchange.utils.DEConstants;
import org.digijava.module.dataExchange.utils.DataExchangeUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Created with IntelliJ IDEA.
 * User: flyer
 * Date: 3/12/14
 * Time: 11:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImportActionNew extends DispatchAction {
	private static final Logger logger = Logger.getLogger(ImportActionNew.class); 
	
    public static final String IATI_LABELS_SORTED = "IATI_LABELS_SORTED";

    public static final int IATI_IMPORT_PAGE_UPLOAD = 0;
    public static final int IATI_IMPORT_PAGE_LOGS = 1;
    public static final int IATI_IMPORT_PAGE_MAPPING = 2;
    public static final int IATI_IMPORT_PAGE_FILTERS = 3;
    public static final int IATI_IMPORT_LANG_FILTERS = 4;
    public static final int IATI_IMPORT_PAGE_SESSIONS = 5;

    private static String countryCodeNullVal = "N/A";
    private static final Pattern LANG_PATTERN = Pattern.compile("xml:lang=\"[a-zA-Z]{2}\"");

    public ActionForward unspecified(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        ImportFormNew myform = (ImportFormNew) form;
        myform.resetForm();
        return listUploadSessions(mapping, form, request, response);
    }

    public ActionForward showUploadScreen(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        ImportFormNew myform = (ImportFormNew) form;
        myform.resetForm();

        myform.setPage(IATI_IMPORT_PAGE_UPLOAD);
        List<DESourceSetting> sources		= new SessionSourceSettingDAO().getAmpSourceSettingsObjects(0, "name", false);
        List<DESourceSetting> viewSources = new ArrayList<DESourceSetting>();
        for(int i=0;i<sources.size();i++){
            if(sources.get(i).getAttachedFile()==null)
                viewSources.add(sources.get(i));
        }
        myform.setConfigurations(viewSources);

        return mapping.findForward("forward");
    }

    public ActionForward upload(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        ImportFormNew myform = (ImportFormNew) form;
        DESourceSetting dess = new SessionSourceSettingDAO().getSourceSettingById(myform.getConfigurationId());

        AmpDEUploadSession upSess = new AmpDEUploadSession();
        String fileSrc = new String(myform.getFile().getFileData(), "UTF-8");
        upSess.setFileName(myform.getFile().getFileName());
        upSess.setFileSrc(fileSrc);
        upSess.setUploadDate(new Date());
        upSess.setSettingsAssigned(dess);

        /*
        List<DELogPerItem> logItems = new ArrayList<DELogPerItem>();
        Map <IatiActivity, Set<DEMappingFields>> items = getImportedItemMap(dess, myform.getFile().getInputStream(), request, logItems);
        myform.setIatiImportedProjectMap(items);
        */

        DEImportValidationEventHandler fromXmllog = new DEImportValidationEventHandler();
        InputStream is = new ByteArrayInputStream(fileSrc.getBytes("UTF-8"));
        IatiActivities xmlActs = fromXml(is, fromXmllog);
        Map<String, Integer> countryISOs = getCountrySetActCountFromActivities(xmlActs);
        
        //configure Language filters
        String currentLanguage = TLSUtils.getEffectiveLangCode(); 
        Map<String, String> languageISOs = getLanguages(fileSrc, currentLanguage);
        myform.setLanguageList(languageISOs.entrySet());

        if (languageISOs.containsKey(currentLanguage)) {
            myform.setSelLanguages(new String[]{currentLanguage});
            myform.setDefaultLanguage(currentLanguage);
        } else if (languageISOs.size() > 0) {
            String firstRandomLanguage = languageISOs.get(languageISOs.keySet().iterator().next());
            myform.setSelLanguages(new String[]{firstRandomLanguage});
            myform.setDefaultLanguage(firstRandomLanguage);
        }

        /*
        int tmpLogId = 0;
        for (DELogPerItem lpi : logItems) {
            lpi.setDeLogPerExecution(null);
            lpi.setTmpId(tmpLogId);
            tmpLogId ++;
        } */
        //myform.setLogItems(logItems);
        //upSess.setLogItems(logItems);
        /*
        Map <String, Set<IatiActivity>> countryActMap = getCountryActivityMap(items);
        myform.setCountryActMap(countryActMap);
          */

        //Prepare country filter items
        myform.setCountryList(new ArrayList<Map.Entry<String, String>>());

        /*
        for (Map.Entry <String, Set<IatiActivity>> me : countryActMap.entrySet()) {

            AbstractMap.SimpleEntry <String, String> toAdd =
                    new AbstractMap.SimpleEntry<String, String>(me.getKey(),
                            new StringBuilder(me.getKey()).append(" (").
                                    append(me.getValue().size()).append(")").toString());

            myform.getCountryList().add(toAdd);
            myform.setSelCountries(null);
        } */


        for (Map.Entry <String, Integer> iso : countryISOs.entrySet()) {

            AbstractMap.SimpleEntry <String, String> toAdd =
                    new AbstractMap.SimpleEntry<String, String>(iso.getKey(),
                            new StringBuilder(iso.getKey()).append(" (").
                                    append(iso.getValue()).append(")").toString());

            myform.getCountryList().add(toAdd);
            myform.setSelCountries(null);
        }



        myform.setUpSess(upSess);
        return mapping.findForward("filters");
    }

    private Map <IatiActivity, Set<DEMappingFields>> getImportedItemMap(AmpDEUploadSession upSess, InputStream is, HttpServletRequest request, List<DELogPerItem> logItems, boolean ignoreSameAs) {
        return getImportedItemMap(upSess, is, request, logItems, ignoreSameAs, null);
    }

    private Map <IatiActivity, Set<DEMappingFields>> getImportedItemMap(AmpDEUploadSession upSess, InputStream is, HttpServletRequest request, List<DELogPerItem> logItems, boolean ignoreSameAs, String itemId) {
        Map <IatiActivity, Set<DEMappingFields>> items = null;
        DEImportBuilder deib = new DEImportBuilder();
        DEImportItem deii = new DEImportItem();
        SourceBuilder desb = new SourceBuilder() {

            @Override
            public void process() {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        };

        desb.setDESourceSetting(upSess.getSettingsAssigned());
        desb.setRawStream(is);
        desb.setInputString("");
        desb.setPreviousInputStream("");
        deii.setSourceBuilder(desb);
        deib.setAmpImportItem(deii);
        deib.setDefaultLanguage(upSess.getSelDefaultLanugage());
        if (itemId == null) {
            deib.checkIATIInputString("check", true);
        } else {
            deib.checkIATIInputString("import", true);
        }


        DELogPerExecution execLog 	= new DELogPerExecution(upSess.getSettingsAssigned());
        if (execLog.getLogItems() == null) {
            execLog.setLogItems(new ArrayList<DELogPerItem>());
        }
        execLog.setDeSourceSetting(upSess.getSettingsAssigned());

        if (itemId == null) {
            deib.setIgnoreSameAsCheck(ignoreSameAs);
            items = deib.processIATIFeedReturnItems(request, execLog, null, "check", null);
        } else {
            items = deib.processIATIFeedReturnItems(request, execLog, null, "import", itemId);
        }

        if (logItems != null) {
            logItems.addAll(execLog.getLogItems());
        }
        return items;
    }

    private Set<String> getCountrySetFromActivities(IatiActivities acts) {
        Set retVal = new HashSet();
        for (Iterator it = acts.getIatiActivityOrAny().iterator(); it.hasNext();) {
            IatiActivity act = (IatiActivity) it.next();
            retVal.addAll(getActivityCountries(act));
        }
        return retVal;
    }

    private Map<String, Integer> getCountrySetActCountFromActivities(IatiActivities acts) {
        Map<String, Integer> retVal = new HashMap<>();
        for (Object tmp : acts.getIatiActivityOrAny()) {

            if (tmp instanceof IatiActivity) {
                IatiActivity act = (IatiActivity) tmp;
                Set<String> countryISOs = getActivityCountries(act);

                for (String countryISO : countryISOs) {
                    if (retVal.get(countryISO) == null) {
                        retVal.put(countryISO, 0);
                    }
                    retVal.put(countryISO, retVal.get(countryISO) + 1);
                }
            }
        }
        return retVal;
    }
    
    private Map<String, String> getLanguages(String fileSrc, String currentIsoLanguage) {
    	Map<String, String> langMap = new TreeMap<String, String>();

    	Matcher m = LANG_PATTERN.matcher(fileSrc);
    	while(m.find()) {
    		String isoLang = m.group();
    		isoLang = isoLang.substring("xml:lang=\"".length(), isoLang.length()-1);
    		if (!langMap.containsKey(isoLang))
    			langMap.put(isoLang, getLanguageNameOrIso(isoLang));
    	}

        /** Do not add language if one is absent in the import file. AMP-18053
    	if (!langMap.containsKey(currentIsoLanguage))
			langMap.put(currentIsoLanguage, getLanguageNameOrIso(currentIsoLanguage));
		*/
        /** add it only if map is empty - i.e. no languages are defined in the import file
         *
         */
        if (langMap.isEmpty()) {
            langMap.put(currentIsoLanguage, getLanguageNameOrIso(currentIsoLanguage));
        }


    	return langMap;
    }
    
    private String getLanguageNameOrIso(String isoLang) {
    	String codeName = IatiHelper.getIatiCodeName(IatiCodeTypeEnum.Language, isoLang);
		return codeName==null ? isoLang :codeName;
    }

    private Map <String, Set<IatiActivity>> getCountryActivityMap (Map <IatiActivity, Set<DEMappingFields>> items) {
        Set<IatiActivity> keys = items.keySet();
        Map <String, Set<IatiActivity>> countryActMap = new HashMap<String, Set<IatiActivity>>();
        for (IatiActivity iatiAct : keys) {
            Set<String> countryCodes = getActivityCountries(iatiAct);

            for (String countryCode : countryCodes) {
                if (!countryActMap.containsKey(countryCode)) {
                    countryActMap.put(countryCode, new HashSet<IatiActivity>());
                }
                countryActMap.get(countryCode).add(iatiAct);
            }
        }
        return countryActMap;
    }

    private Set<String> getActivityCountries (IatiActivity iatiAct) {
        Set<String> retVal = new HashSet<String>();
        for (Object actProp : iatiAct.getActivityWebsiteOrReportingOrgOrParticipatingOrg()) {
            if (actProp instanceof RecipientCountry) {
                RecipientCountry rc = (RecipientCountry) actProp;
                retVal.add(rc.getCode());
            }
        }
        if (retVal.isEmpty()) retVal.add(countryCodeNullVal);
        return retVal;
    }

    private void filterLogs (Set<IatiActivity> filteredActs, List<DELogPerItem> logsToFilter) {
        Set <String> filteredActNames = new HashSet<String>();
        for (IatiActivity act : filteredActs) {
            String title = null;
            String iatiIdt = null;
            for (Object obj : act.getActivityWebsiteOrReportingOrgOrParticipatingOrg()) {
                if (obj instanceof javax.xml.bind.JAXBElement) {
                    javax.xml.bind.JAXBElement objCasted = (javax.xml.bind.JAXBElement) obj;
                    if (objCasted.getName().getLocalPart().equals("title")) {
                        String lang = (String)((TextType) objCasted.getValue()).getLang();
                        /*
                         * Remove this condition. Process titles of other languages as well
                         * AMP-18053
                         * if (lang == null || lang.equals("en")) {
                         */
                            title = (String)((TextType) objCasted.getValue()).getContent().get(0);
                         //}
                    }
                }

                if (obj instanceof IatiIdentifier) {
                    IatiIdentifier objCasted = (IatiIdentifier) obj;
                    iatiIdt = objCasted.getContent();
                }

                if (title != null && iatiIdt != null) {
                	 filteredActNames.add(new StringBuilder(title).append(" - ").append(iatiIdt).toString());
                     break;
                }
            }
        }

        int tmpLogId = 0;
        Iterator <DELogPerItem> lpiIt = logsToFilter.iterator();
        while (lpiIt.hasNext()) {
            DELogPerItem lpi = lpiIt.next();
            boolean containsInFiltered = false;
            //String normName = lpi.getName().indexOf(" - ")>-1 ? lpi.getName().substring(0, lpi.getName().indexOf(" - ")) : lpi.getName();
            for (String filteredActName : filteredActNames) {
                if (filteredActName.equals(lpi.getName())) {
                    containsInFiltered = true;
                    break;
                }
            }
            if (!containsInFiltered) {
                lpiIt.remove();
            } else {
                lpi.setDeLogPerExecution(null);
                lpi.setTmpId(tmpLogId);
                tmpLogId ++;
            }
        }
    }

    public ActionForward showLogs(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        ImportFormNew myform = (ImportFormNew) form;
        myform.setPage(IATI_IMPORT_PAGE_LOGS);

        List<DELogPerItem> logItems = new ArrayList<DELogPerItem>();
        AmpDEUploadSession sess = myform.getUpSess();
        DESourceSetting dess = sess.getSettingsAssigned();

        InputStream is = new ByteArrayInputStream(sess.getFileSrc().getBytes("UTF-8"));

        Map <IatiActivity, Set<DEMappingFields>> items = getImportedItemMap(sess, is, request, logItems, false);
        Map <String, Set<IatiActivity>> countryActMap = getCountryActivityMap (items);

        Set<String> selCountries = sess.getSelCountries();
        Set<IatiActivity> filteredActs = new HashSet<IatiActivity>();
        for (String countryIso : selCountries) {
            filteredActs.addAll(countryActMap.get(countryIso));
        }

        filterLogs (filteredActs, logItems);



        AmpDEUploadSession dbSess = DbUtil.getAmpDEUploadSession(sess.getId(), true);
        //AmpDEUploadSession dbSess = myform.getUpSess();

        List<DELogPerItem> forUpdate = new ArrayList<DELogPerItem>();
        List<DELogPerItem> forCreate = new ArrayList<DELogPerItem>();
        List<DELogPerItem> dbLogItems = dbSess.getLogItems();

        for (DELogPerItem memoryLogItem : logItems) {
            boolean exist = false;
            for (DELogPerItem dbLogItem : dbLogItems) {
                if (dbLogItem.getName().equals(memoryLogItem.getName())) {
                    dbLogItem.update(memoryLogItem);
                    exist = true;
                    break;
                }
            }
            if (exist) {
                forUpdate.add(memoryLogItem);
            } else {
                forCreate.add(memoryLogItem);
            }
        }

        dbSess.getLogItems().addAll(forCreate);

        List<DELogPerItem> memoryAll = new ArrayList<DELogPerItem>();
        memoryAll.addAll(forUpdate);
        memoryAll.addAll(forCreate);

        Set dbItemNames = new HashSet();//Check for duplicates
        for (Iterator <DELogPerItem> it = dbLogItems.iterator(); it.hasNext();) {
            DELogPerItem dbLogItem = it.next();
            boolean exist = false;
            for (DELogPerItem memoryLogItem : logItems) {
                if (dbLogItem.getName().equals(memoryLogItem.getName())) {
                    dbLogItem.update(memoryLogItem);
                    exist = true;
                    break;
                }
            }
            if (!exist || dbItemNames.contains(dbLogItem.getName())) {
                it.remove();
            } else {
                dbItemNames.add(dbLogItem.getName());
            }
        }

        //Check for duplicates




//        sess.getLogItems().clear();
//        sess.getLogItems().addAll(logItems);
        DbUtil.saveObject(dbSess);

        myform.setLogItems(dbSess.getLogItems());


        return mapping.findForward("forward");
    }

    
    public ActionForward executeImportAll(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
    	 ImportFormNew myform = (ImportFormNew) form;
         myform.setPage(IATI_IMPORT_PAGE_LOGS);
         List<DELogPerItem> iterableLogItems = Collections.unmodifiableList(myform.getLogItems());
         AmpDEUploadSession sess = myform.getUpSess();
         DESourceSetting dess = sess.getSettingsAssigned();


         DELogPerItem selLogItem = null;
         for (DELogPerItem delog : iterableLogItems) {
	         if (!delog.getLogType().equals("OK")) {
	             	continue;
	         }
	         selLogItem = delog;
	         List<DELogPerItem> logItems = new ArrayList<DELogPerItem>();
	         InputStream is = new ByteArrayInputStream(sess.getFileSrc().getBytes("UTF-8"));
	         Map <IatiActivity, Set<DEMappingFields>> importAndGetImportedItemMap = getImportedItemMap(sess, is, request, logItems, false, String.valueOf(selLogItem.getId()));
	
	         //Update import date
	         Date newDateTime = new Date();
	         DELogPerItem dbObj = (DELogPerItem) DbUtil.getObject(DELogPerItem.class, selLogItem.getId());
	
	         for (DELogPerItem logItem : logItems) {
	             if (selLogItem.getName().equals(logItem.getName())) {
	                 dbObj.update(logItem);
	                 selLogItem.update(logItem);
	             }
	         }
	
	         dbObj.setImportDoneOn(newDateTime);
	         selLogItem.setImportDoneOn(newDateTime);
	         DbUtil.saveObject(dbObj);
         }
         
         return mapping.findForward("sessions");
    }
    	
    
    public ActionForward executeImport(ActionMapping mapping, ActionForm form,
                                  HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        ImportFormNew myform = (ImportFormNew) form;
        myform.setPage(IATI_IMPORT_PAGE_LOGS);
        Long objId = myform.getObjId();
        List<DELogPerItem> logItems = myform.getLogItems();


        DELogPerItem selLogItem = null;
        for (DELogPerItem delog : logItems) {
            if (delog.getId().equals(objId)) {
                selLogItem = delog;
                break;
            }
        }


        logItems = new ArrayList<DELogPerItem>();
        AmpDEUploadSession sess = myform.getUpSess();
        DESourceSetting dess = sess.getSettingsAssigned();

        InputStream is = new ByteArrayInputStream(sess.getFileSrc().getBytes("UTF-8"));

        Map <IatiActivity, Set<DEMappingFields>> importAndGetImportedItemMap = getImportedItemMap(sess, is, request, logItems, false, String.valueOf(selLogItem.getId()));

        //Update import date
        Date newDateTime = new Date();
        DELogPerItem dbObj = (DELogPerItem) DbUtil.getObject(DELogPerItem.class, selLogItem.getId());

        for (DELogPerItem logItem : logItems) {
            if (selLogItem.getName().equals(logItem.getName())) {
                dbObj.update(logItem);
                selLogItem.update(logItem);
            }
        }

        dbObj.setImportDoneOn(newDateTime);
        selLogItem.setImportDoneOn(newDateTime);
        DbUtil.saveObject(dbObj);


        return mapping.findForward("forward");
    }



    public ActionForward showFilters(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        ImportFormNew myform = (ImportFormNew) form;
        myform.setPage(IATI_IMPORT_PAGE_FILTERS);
        return mapping.findForward("forward");
    }
    
    public ActionForward showLangFilters(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
    	ImportFormNew myform = (ImportFormNew) form;
    	myform.setPage(IATI_IMPORT_LANG_FILTERS);
    	return mapping.findForward("forward");
	}

    public ActionForward showMapping(ActionMapping mapping, ActionForm form,
                                  HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        ImportFormNew myform = (ImportFormNew) form;
        AmpDEUploadSession upSess = myform.getUpSess();

        Set<String> selCountries = new HashSet<String> (Arrays.asList(myform.getSelCountries()));
        upSess.setSelCountries(selCountries);
        AmpDEUploadSession sess = myform.getUpSess();
        addLanguageFilters(sess, myform);

        //Modify XML on initial serialization (will not be able to change country filters anymore)
        String xmlSrc = null;
        if (upSess.getId() == null) {
        	//getting IATI Activities
        	DEImportValidationEventHandler fromXmllog = new DEImportValidationEventHandler();
        	InputStream is = myform.getFile().getInputStream();
            IatiActivities parsed = fromXml(is, fromXmllog);
            
            //applying country filter, if any
            if (myform.getCountryList().size() > 1) {
                Set<String> countryISOs = selCountries;
                for (Iterator it = parsed.getIatiActivityOrAny().iterator(); it.hasNext();) {
                    Object tmp = it.next();
                    boolean contains = false;
                    if (tmp instanceof IatiActivity) {
                        IatiActivity iatiAct = (IatiActivity) tmp;
                        Set<String> countryCode = getActivityCountries(iatiAct);
                        for (String iso : countryCode) {
                            if (countryISOs.contains(iso)) {
                                contains = true;
                                break;
                            }
                        }
                    }

                    if (!contains) it.remove();
                }
            }
            //propagate parent language (or default language if root without lang) 
            IatiHelper.setLangToAll(parsed.getIatiActivityOrAny(), myform.getDefaultLanguage());
            
            DEImportValidationEventHandler toXmllog = new DEImportValidationEventHandler();
            try {
                xmlSrc = toXml(parsed, toXmllog);
            } catch (Exception ex) {
                logger.error(ex.getMessage());
            }
            
            //remove useless translations
            xmlSrc = cleanupByLang(xmlSrc, myform);
            sess.setFileSrc(xmlSrc);
        } else {
            xmlSrc = upSess.getFileSrc();
        }

        ArrayList<DELogPerItem> logItems = new ArrayList<DELogPerItem>();

        DESourceSetting dess = sess.getSettingsAssigned();

        InputStream is = new ByteArrayInputStream(xmlSrc.getBytes("UTF-8"));

        Map <IatiActivity, Set<DEMappingFields>> items = getImportedItemMap(sess, is, request, logItems, true);

        myform.setIatiImportedProjectMapFiltered(items);

        Set<DEMappingFields> uniqueFields = new HashSet<DEMappingFields>();
        int tmpId = 0;
        for (Map.Entry<IatiActivity ,Set<DEMappingFields>> cuAct : items.entrySet()) {
            for (DEMappingFields iterFld : cuAct.getValue()) {
                if (!containsDEMappingFields(uniqueFields, iterFld)) {
                    tmpId ++;
                    iterFld.setTmpId(tmpId);
                    uniqueFields.add(iterFld);
                }
            }
        }

        List<IatiCodeType> allCodetypes = DbUtil.getAllCodetypes();
        Map <String, IatiCodeType> iatiNameCodetypeMap = new HashMap<String, IatiCodeType>();
        for (IatiCodeType type : allCodetypes) {
            iatiNameCodetypeMap.put(type.getAmpName(), type);
        }

        Map <String, Set<DEMappingFields>> groupFldsByPath = new HashMap <String, Set<DEMappingFields>>();
        TreeSet <String>ampClassSet = new TreeSet<String>();
        for (DEMappingFields fld : uniqueFields) {
            if (iatiNameCodetypeMap.containsKey(fld.getIatiPath())) {
                //String IATIAmpPath = iatiNameCodetypeMap.get(fld.getIatiPath());
                IatiCodeType codeType = iatiNameCodetypeMap.get(fld.getIatiPath());

                String iatiValue =fld.getIatiValues();
                String processedIatiValue = null;

                if (codeType != null && iatiValue.indexOf("|")>-1) {
                    processedIatiValue = iatiValue.substring(iatiValue.lastIndexOf("|") + 1);
                } else {
                    processedIatiValue = iatiValue;
                }

                String codeName = null;
                if (codeType != null){
                    codeName = codeType.getNameForCode(processedIatiValue);
                }
                if (codeName != null) {
                    fld.setIatiValuesForDisplay(codeName);
                } else {
                    fld.setIatiValuesForDisplay(fld.getIatiValues());
                }
            } else {
                fld.setIatiValuesForDisplay(fld.getIatiValues());
            }

            if (groupFldsByPath.get(fld.getAmpClass()) == null) {
                ampClassSet.add(fld.getAmpClass());
                groupFldsByPath.put(fld.getAmpClass(), new HashSet<DEMappingFields>());
            }
            groupFldsByPath.get(fld.getAmpClass()).add(fld);
        }

        myform.setGroupFldsByPath(groupFldsByPath);
        myform.setAmpClasses(ampClassSet);

        myform.setPage(IATI_IMPORT_PAGE_MAPPING);
        return mapping.findForward("forward");
    }
    
    private void addLanguageFilters(AmpDEUploadSession sess, ImportFormNew form) {
    	sess.setSelDefaultLanugage(form.getDefaultLanguage());
    	sess.setSelLanugages(Arrays.asList(form.getSelLanguages()));
    }

    /**
     * Removes from XML definition elements with unsupported language or useless translations. <br>
     * Useful translations like activity title, description, etc. are kept intact. <br>
     * Note: useful translations are defined in {@link IatiRules}
     * @param xml - the XML to update
     * @param importForm - import form that stores language preferences
     * @return updated XML
     */
    private String cleanupByLang(String xml, ImportFormNew importForm) {
    	IatiVersion iatiVersion = IatiVersion.V_1_01; //TODO: should be detected from XML, part upcoming AMP-17873
    	boolean regenerateXML = false;
    	List<String> removedElementsSignature = new ArrayList<String>();
    	//extract translations that must be completely excluded from processing
    	List<String> notAllowedLangs = getNotAllowedLangs(importForm);
    	
    	//prepare XQuery strings
    	String allowedMultilingual = ""; //query for list of elements with useful translations to be kept
    	String or = "";
    	for(String allowedElem: IatiRules.getMultilingualElements(iatiVersion)) {
    		allowedMultilingual += or + "//" + allowedElem;
    		or = " | ";
    	}
    	String langNodes = "//iati-activity//*[@lang]"; 		//query for all nodes with lang attribute
    	//String noLangNodes = "//iati-activity//*[not(@lang)]"; 	//query for all nodes with no lang attribute
    	String notAllowed = "//iati-activity//*["; 				//query for elements to be completely ignored
    	or = "";
    	for(String notAllowedLang : notAllowedLangs) {
    		notAllowed += or + "@lang='" + notAllowedLang + "'";
    		or = " or ";
    	}
    	notAllowed += "]"; 

    	XPath xpath = XPathFactory.newInstance().newXPath();
    	try {
    		InputStream is = new ByteArrayInputStream(xml.getBytes("UTF-8"));
    		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    		Document document = builder.parse(is);
    		XPathExpression expr = null;
    		NodeList nodes = null;
    		
    		//removes translations to be ignored 
    		if (notAllowedLangs.size()>0) {
	    		expr = xpath.compile(notAllowed);
				nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
				regenerateXML = IatiHelper.removeNodes(nodes, removedElementsSignature);
    		}
			
			/*
			//store the list of nodes with no lang attribute 
			expr = xpath.compile(noLangNodes);
			NodeList noLangNodeList = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
			
			//update to parent or default language
			IatiHelper.setAttr(document, document, "xml:lang", importForm.getDefaultLanguage(), true, false);
			*/
			
			//get allowed multilingual nodes
			Set<Node> processed = new HashSet<Node>();
			expr = xpath.compile(allowedMultilingual);
			nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
			for (int i=0; i<nodes.getLength(); i++)
				processed.add(nodes.item(i)); //mark them as processed to not be touched during duplicate elements removal below
			
			//remove duplicate elements with useless translations
			expr = xpath.compile(langNodes);
			nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
			regenerateXML = IatiHelper.removeUselessTranslations(nodes, importForm.getDefaultLanguage(), processed, removedElementsSignature) || regenerateXML;
			
			/*
			//restore noLang
			for (int i=0; i<noLangNodeList.getLength(); i++) {
				IatiHelper.removeAttr(noLangNodeList.item(i), "xml:lang");
			}
			*/
			logger.info("The following XML entries are removed based on language filters (selected languages = " + importForm.getSelLanguages().toString() 
					+ ", default language = " + importForm.getDefaultLanguage() + "): " + removedElementsSignature.toString());
			
			if (regenerateXML) { //from altered document structure 
				Transformer transformer = TransformerFactory.newInstance().newTransformer();
				transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
				StringWriter sw = new StringWriter();
				transformer.transform(new DOMSource(document), new StreamResult(sw));
				xml = sw.toString();
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}     	
    	return xml;
    }
    
    private List<String> getNotAllowedLangs(ImportFormNew importForm) {
    	List<String> notAllowedLangs = new ArrayList<String>();
    	for (Map.Entry<String, String> entry : importForm.getLanguageList()) {
    		boolean found = false;
    		for (String allowed : importForm.getSelLanguages())
    		{
    			if (allowed.equals(entry.getKey())) {
    				found = true;
    				break;
    			}
    		}
    		if (!found)
    			notAllowedLangs.add(entry.getKey());
    	}
    	return notAllowedLangs;
    }
    
    public ActionForward getMappingObjects(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        ImportFormNew myform = (ImportFormNew) form;
        String selAmpClass = request.getParameter("selectedClass");
        myform.setSelAmpClass(selAmpClass);

        TreeMap<Long, String> allEntities = DataExchangeUtils.getAllAmpEntitiesByClass(selAmpClass);

        org.digijava.module.dataExchange.utils.ValueComparator bvc =
                new org.digijava.module.dataExchange.utils.ValueComparator(allEntities);
        TreeMap<Long, String> sortedLabels = new TreeMap<Long,String>(bvc);
        sortedLabels.putAll(allEntities);

        request.getSession().setAttribute(IATI_LABELS_SORTED, sortedLabels);

        Map <String, Set<DEMappingFields>> groupFldsByPath = myform.getGroupFldsByPath();



        JSONObject retObj = new JSONObject();
        JSONArray objects = new JSONArray();

        Set<DEMappingFields> selFlds = groupFldsByPath.get(selAmpClass);

        myform.setSelFlds(selFlds);

        //objects.addAll(selFlds);

        List<DEMappingFields> selFldsSorted = new ArrayList<DEMappingFields>(selFlds);
        Collections.sort(selFldsSorted, new Comparator() {
            @Override
            public int compare(Object o, Object o2) {
                DEMappingFields cast1 = (DEMappingFields) o;
                DEMappingFields cast2 = (DEMappingFields) o2;
                return cast1.getIatiValues().compareTo(cast2.getIatiValues());
            }
        });

        for (DEMappingFields fld : selFldsSorted) {
            JSONObject newObj = new JSONObject();
            newObj.accumulate("iatiItems", fld.getIatiItems());
            newObj.accumulate("iatiValues", fld.getIatiValuesForDisplay());
            newObj.accumulate("tmpId", fld.getTmpId());
            newObj.accumulate("ampId", fld.getAmpId());
            newObj.accumulate("sameAsTmpId", fld.getSameAsMaping() == null ? -1 : fld.getSameAsMaping().getTmpId());
            newObj.accumulate("sameAsText", fld.getSameAsMaping() == null ? "" : fld.getSameAsMaping().getIatiValuesForDisplay());
            String ampVal = fld.getAmpValues() != null ? fld.getAmpValues().replaceAll("'", "&lsquo;") : null;
            newObj.accumulate("ampValues", ampVal);
            objects.add(newObj);
        }



        retObj.accumulate("objects", objects);

        PrintWriter pw =  response.getWriter();
        retObj.write(pw);
        pw.close();

        return null;
    }

    public ActionForward getOptionsAjaxAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        ImportFormNew myform = (ImportFormNew) form;


        String maxResultCountStr = request.getParameter("maxResultCount");
        String searchStr = request.getHeader("searchStr");
        int maxResultCount = Integer.parseInt(maxResultCountStr);


        Map<Long, String> sortedLabels = (Map<Long,String>) request.getSession().getAttribute(IATI_LABELS_SORTED);
        JSONArray objArray = new JSONArray();
        JSONObject retObj = new JSONObject();


        JSONObject unmappedObj = new JSONObject();
        unmappedObj.accumulate("id", DEConstants.AMP_ID_UNMAPPED);
        unmappedObj.accumulate("val", "Unmapped");
        objArray.add(unmappedObj);

        if (myform.getSelAmpClass().equalsIgnoreCase("Activity")) {
            JSONObject addNewObj = new JSONObject();
            addNewObj.accumulate("id", DEConstants.AMP_ID_CREATE_NEW);
            addNewObj.accumulate("val", "Add new");
            objArray.add(addNewObj);

            JSONObject dontImport = new JSONObject();
            dontImport.accumulate("id", DEConstants.AMP_ID_DO_NOT_IMPORT);
            dontImport.accumulate("val", "Don't import");
            objArray.add(dontImport);
        }

        JSONObject sameAsObj = new JSONObject();
        sameAsObj.accumulate("id", "-2");
        sameAsObj.accumulate("val", "Same as");
        objArray.add(sameAsObj);

        for (java.util.Map.Entry<Long, String> item : sortedLabels.entrySet()) {
            if (item.getValue() != null) {
                if (searchStr == null || searchStr.trim().isEmpty() || (item.getValue() != null && item.getValue().toLowerCase().contains(searchStr.toLowerCase()))) {

                    objArray.add(new JSONObject().accumulate("id", item.getKey()).accumulate("val", item.getValue().replaceAll("'", "&lsquo;")));
                }
            }
            if (maxResultCount > 0 && objArray.size() >= maxResultCount) break;
        }

        retObj.accumulate("totalCount", sortedLabels.size());
        retObj.accumulate("criteriaCount", objArray.size());
        retObj.accumulate("objects", objArray);
        PrintWriter out = response.getWriter();
        retObj.write(out);
        out.close();
        return null;
    }

    public ActionForward getSameAsOptionsAjaxAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        ImportFormNew myform = (ImportFormNew) form;
        String searchStr = request.getHeader("searchStr");
        List<DEMappingFields> selFldsSorted = new ArrayList<DEMappingFields>(myform.getSelFlds());
        Collections.sort(selFldsSorted, new Comparator() {
            @Override
            public int compare(Object o, Object o2) {
                DEMappingFields cast1 = (DEMappingFields) o;
                DEMappingFields cast2 = (DEMappingFields) o2;
                return cast1.getIatiValues().compareTo(cast2.getIatiValues());
            }
        });

        JSONObject sameAsMap = new JSONObject();
        JSONArray items = new JSONArray();
        for (DEMappingFields mf : selFldsSorted) {
            if ( mf.getAmpId() != null && (mf.getAmpId() > 0l || DEConstants.AMP_ID_CREATE_NEW.equals(mf.getAmpId())) ) {//Allow "Same As" only to AMP mapped objects
                if (searchStr == null || searchStr.trim().isEmpty() || mf.getIatiValuesForDisplay().toLowerCase().contains(searchStr.toLowerCase())) {
                    JSONObject sameAsItem = new JSONObject();
                    sameAsItem.accumulate("text", mf.getIatiValuesForDisplay());
                    sameAsItem.accumulate("tmpId", mf.getTmpId());
                    items.add(sameAsItem);
                }
            }
        }
        sameAsMap.accumulate("items", items);

        response.setContentType("application/json");
        PrintWriter pw = response.getWriter();
        sameAsMap.write(pw);
        pw.close();

        return null;
    }

    public ActionForward updateSameAsMapping(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        ImportFormNew myform = (ImportFormNew) form;

        String objIdStr = request.getParameter("objId");
        int objId = Integer.parseInt(objIdStr);
        String newValStr = request.getParameter("newVal");
        Long newVal = Long.parseLong(newValStr);
        Map<Long, String> sortedLabels = (Map<Long,String>) request.getSession().getAttribute(IATI_LABELS_SORTED);

        String newAmpObjTitle = sortedLabels.get(newVal);

        Set<DEMappingFields> selFlds = myform.getSelFlds();

        DEMappingFields setAsSameAs = null;

        for (DEMappingFields mf : selFlds) {
            if (mf.getTmpId() == newVal) {
                setAsSameAs = mf;
                break;
            }
        }

        for (DEMappingFields mf : selFlds) {
            if (mf.getTmpId() == objId) {
                mf.setSameAsMaping(setAsSameAs);
                mf.setDirty(true);
            }
        }

        return null;
    }

    public ActionForward updateMapping(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        ImportFormNew myform = (ImportFormNew) form;

        String objIdStr = request.getParameter("objId");
        int objId = Integer.parseInt(objIdStr);
        String newValStr = request.getParameter("newVal");
        Long newVal = Long.parseLong(newValStr);
        Map<Long, String> sortedLabels = (Map<Long,String>) request.getSession().getAttribute(IATI_LABELS_SORTED);

        String newAmpObjTitle = sortedLabels.get(newVal);

        Set<DEMappingFields> selFlds = myform.getSelFlds();

        for (DEMappingFields mf : selFlds) {
            if (mf.getTmpId() == objId) {
                if( !DEConstants.AMP_ID_SAME_AS_MAPPING.equals(newVal) ) { //Not "Same As" mapping
                    mf.setAmpId(newVal);
                    mf.setAmpValues(newAmpObjTitle);
                    mf.setSameAsMaping(null);
                    mf.setDirty(true);
                } else {
                    mf.setAmpId(null);
                    mf.setAmpValues(null);
                    mf.setDirty(true);
                }
            }
        }


        if (DEConstants.AMP_ID_SAME_AS_MAPPING.equals(newVal)) { //Generate "Same As" mapping items
            JSONObject sameAsMap = new JSONObject();
            JSONArray items = new JSONArray();
            for (DEMappingFields mf : selFlds) {
                if (mf.getAmpId() != null) {//Allow "Same As" only to AMP mapped objects
                    JSONObject sameAsItem = new JSONObject();
                    sameAsItem.accumulate("text", mf.getIatiValuesForDisplay());
                    sameAsItem.accumulate("tmpId", mf.getTmpId());
                    items.add(sameAsItem);
                }
            }
            sameAsMap.accumulate("items", items);

            response.setContentType("application/json");
            PrintWriter pw = response.getWriter();
            sameAsMap.write(pw);
            pw.close();
        }



        return null;
    }

    private boolean containsDEMappingFields (Set<DEMappingFields> set, DEMappingFields fld) {
        boolean retVal = false;
        for (DEMappingFields iterFld : set) {
            if (iterFld.compare(fld)) {
                retVal = true;
                break;
            }
        }
        return retVal;
    }

    public ActionForward saveMapping(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        ImportFormNew myform = (ImportFormNew) form;
        Map <String, Set<DEMappingFields>> groupFldsByPath = myform.getGroupFldsByPath();
        AmpDEUploadSession upSess = myform.getUpSess();

        //Modify XML on initial serialization (will not be able to change country filters anymore)
        /*
        if (upSess.getId() != null) {
            DEImportValidationEventHandler fromXmllog = new DEImportValidationEventHandler();
            InputStream is = new ByteArrayInputStream(upSess.getFileSrc().getBytes("UTF-8"));
            IatiActivities parsed = fromXml(is, fromXmllog);

            Set<String> countryISOs = upSess.getSelCountries();


            for (Iterator it = parsed.getIatiActivityOrAny().iterator(); it.hasNext();) {
                IatiActivity iatiAct = (IatiActivity) it.next();
                String countryCode = getActivityCountry(iatiAct, countryCodeNullVal);

                if (!countryISOs.contains(countryCode)) it.remove();
            }

            DEImportValidationEventHandler toXmllog = new DEImportValidationEventHandler();
            String modifiedXML = toXml(parsed, toXmllog);
            upSess.setFileSrc(modifiedXML);
        }  */

        Set <DEMappingFields> forCreate = new HashSet<DEMappingFields>();
        Set <DEMappingFields> forUpdate = new HashSet<DEMappingFields>();
        Set <DEMappingFields> forDelete = new HashSet<DEMappingFields>();
        Set <Long> forDeleteIds = new HashSet<Long>();
        for (Map.Entry<String, Set<DEMappingFields>> iter : groupFldsByPath.entrySet()) {
            for (DEMappingFields mf :iter.getValue()) {
                if (mf.isDirty()) {
                     if ((mf.getAmpId()!= null && !mf.getAmpId().equals(0l)) || mf.getSameAsMaping()!= null) {
                        if (mf.getId() == null || mf.getId().equals(0l)) {
                            forCreate.add(mf);
                        } else {
                            forUpdate.add(mf);
                        }
                    }

                    if ((mf.getAmpId()== null || mf.getAmpId().equals(0l)) && mf.getSameAsMaping() == null) {
                        forDelete.add(mf);
                        forDeleteIds.add(mf.getId());
                    }
                }
            }
        }

        if (upSess.getId() != null && !upSess.getId().equals(0l)) {  //Update
            upSess = DbUtil.getAmpDEUploadSession(upSess.getId());
            upSess.setLastEditDate(new Date());

            Iterator<DEMappingFields> it = upSess.getMappedFields().iterator();
            while (it.hasNext()) {
                DEMappingFields itermf = it.next();
                for(DEMappingFields mf : forUpdate) {
                    if (itermf.getId().equals(mf.getId())) {
                        itermf.setAmpValues(mf.getAmpValues());
                        itermf.setAmpId(mf.getAmpId());
                        itermf.setSameAsMaping(mf.getSameAsMaping());
                    }
                }

                for(DEMappingFields mf : forDelete) {
                    if (itermf.getId().equals(mf.getId())) {
                        it.remove();
                    }
                }
            }
        } else { //new
            upSess = myform.getUpSess();
            upSess.setMappedFields(forUpdate);
            //upSess.getMappedFields().addAll(forUpdate);
        }

        String[] selCountries = myform.getSelCountries();
        upSess.setSelCountries(new HashSet<String> (Arrays.asList(selCountries)));
        addLanguageFilters(upSess, myform);

        if (!forDeleteIds.isEmpty()) DbUtil.deleteMappings(forDeleteIds);

        /*
        for (DEMappingFields mf : forCreate) {
            if (mf.getUploadSessionsLinked() == null) {
                Set<AmpDEUploadSession> upSessSet = new HashSet<AmpDEUploadSession>();
                upSessSet.add(upSess);
                mf.setUploadSessionsLinked(upSessSet);
            }
        } */
        upSess.getMappedFields().addAll(forCreate);
        DbUtil.saveObject(upSess);

        request.getSession().removeAttribute(IATI_LABELS_SORTED);

        String retMapping = null;
        if (request.getParameter("stayOnPage") != null && request.getParameter("stayOnPage").equals("true")) {
            return showMapping(mapping, form, request, response);
        } else {
            return mapping.findForward("logs");
        }
    }

    private IatiActivities fromXml(InputStream is, DEImportValidationEventHandler log) throws SAXException, JAXBException {
        JAXBContext jc = JAXBContext.newInstance(DEConstants.IATI_JAXB_INSTANCE);
        Unmarshaller m = jc.createUnmarshaller();
        URL rootUrl = this.getClass().getResource("/");
        String path="";
        try {
            path     = rootUrl.toURI().resolve("../../").getPath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        SchemaFactory sf = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = sf.newSchema(new File(path+DEConstants.IATI_SCHEMA_LOCATION));

        m.setSchema(schema);
        m.setEventHandler(log);
        IatiActivities parsed = (IatiActivities) m.unmarshal(is) ;
        return parsed;
    }

    private String toXml(IatiActivities parsed, DEImportValidationEventHandler log) throws SAXException, JAXBException {
        JAXBContext jc = JAXBContext.newInstance(DEConstants.IATI_JAXB_INSTANCE);
        URL rootUrl   = this.getClass().getResource("/");
        String path="";
        try {
            path = rootUrl.toURI().resolve("../../").getPath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        SchemaFactory sf = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = sf.newSchema(new File(path+DEConstants.IATI_SCHEMA_LOCATION));
        Writer writer = new StringWriter();
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setSchema(schema);

        marshaller.marshal(parsed, writer);
        return writer.toString();
    }


    public ActionForward listUploadSessions(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        ImportFormNew myform = (ImportFormNew) form;
        List<AmpDEUploadSession> upSessionList = DbUtil.getAllAmpDEUploadSessions();
        myform.setUploadSessions(upSessionList);
        myform.setPage(IATI_IMPORT_PAGE_SESSIONS);
        return mapping.findForward("forward");
    }

    public ActionForward loadUploadSession(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        ImportFormNew myform = (ImportFormNew) form;
        AmpDEUploadSession sess = DbUtil.getAmpDEUploadSession(myform.getObjId());
        DESourceSetting dess = sess.getSettingsAssigned();
        InputStream is = new ByteArrayInputStream(sess.getFileSrc().getBytes("UTF-8"));
        
        request.setAttribute("isLoad", true);

        Map <IatiActivity, Set<DEMappingFields>> items = getImportedItemMap(sess, is, request, null, true);
        myform.setIatiImportedProjectMap(items);

        Set<String> countryISOs = sess.getSelCountries();

        myform.setSelCountries(countryISOs.toArray(new String[0]));
        myform.setSelLanguages(sess.getSelLanugages().toArray(new String[sess.getSelLanugages().size()]));
        myform.setDefaultLanguage(sess.getSelDefaultLanugage());

        Map <String, Set<IatiActivity>> countryActMap = getCountryActivityMap(items);
        myform.setCountryActMap(countryActMap);
        myform.setPage(IATI_IMPORT_PAGE_MAPPING);
        myform.setUpSess(sess);

        return mapping.findForward("map");
    }


}
