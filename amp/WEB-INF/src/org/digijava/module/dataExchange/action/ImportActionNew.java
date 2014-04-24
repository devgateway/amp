package org.digijava.module.dataExchange.action;

import com.sun.jersey.api.json.JSONConfigurated;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.dataExchange.dbentity.*;
import org.digijava.module.dataExchange.engine.DEImportBuilder;
import org.digijava.module.dataExchange.engine.SourceBuilder;
import org.digijava.module.dataExchange.form.ImportFormNew;
import org.digijava.module.dataExchange.pojo.DEImportItem;
import org.digijava.module.dataExchange.pojo.DEImportValidationEventHandler;
import org.digijava.module.dataExchange.util.DataExchangeConstants;
import org.digijava.module.dataExchange.util.DbUtil;
import org.digijava.module.dataExchange.util.SessionSourceSettingDAO;
import org.digijava.module.dataExchange.util.SourceSettingDAO;
import org.digijava.module.dataExchange.utils.Constants;
import org.digijava.module.dataExchange.utils.DataExchangeUtils;
import org.digijava.module.dataExchangeIATI.iatiSchema.jaxb.IatiActivities;
import org.digijava.module.dataExchangeIATI.iatiSchema.jaxb.IatiActivity;
import org.digijava.module.dataExchangeIATI.iatiSchema.jaxb.IatiIdentifier;
import org.digijava.module.dataExchangeIATI.iatiSchema.jaxb.RecipientCountry;
import org.hibernate.HibernateException;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: flyer
 * Date: 3/12/14
 * Time: 11:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImportActionNew extends DispatchAction {
    public static final String IATI_LABELS_SORTED = "IATI_LABELS_SORTED";

    public static final int IATI_IMPORT_PAGE_UPLOAD = 0;
    public static final int IATI_IMPORT_PAGE_LOGS = 1;
    public static final int IATI_IMPORT_PAGE_MAPPING = 2;
    public static final int IATI_IMPORT_PAGE_FILTERS = 3;
    public static final int IATI_IMPORT_PAGE_SESSIONS = 4;

    private static String countryCodeNullVal = "N/A";

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
        List<DESourceSetting> sources		= new SessionSourceSettingDAO().getPagedAmpSourceSettingsObjects(0, "name");
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
        myform.setCountryList(new ArrayList<AbstractMap.SimpleEntry<String, String>>());

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

    private Map <IatiActivity, Set<DEMappingFields>> importAndGetImportedItemMap(DESourceSetting dess, InputStream is, HttpServletRequest request, List<DELogPerItem> logItems, String itemId) {
        return getImportedItemMap(dess, is, request, logItems, itemId);
    }

    private Map <IatiActivity, Set<DEMappingFields>> getImportedItemMap(DESourceSetting dess, InputStream is, HttpServletRequest request, List<DELogPerItem> logItems) {
        return getImportedItemMap(dess, is, request, logItems, null);
    }

    private Map <IatiActivity, Set<DEMappingFields>> getImportedItemMap(DESourceSetting dess, InputStream is, HttpServletRequest request, List<DELogPerItem> logItems, String itemId) {
        Map <IatiActivity, Set<DEMappingFields>> items = null;
        DEImportBuilder deib = new DEImportBuilder();
        DEImportItem deii = new DEImportItem();
        SourceBuilder desb = new SourceBuilder() {

            @Override
            public void process() {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        };

        desb.setDESourceSetting(dess);
        desb.setRawStream(is);
        desb.setInputString("");
        desb.setPreviousInputStream("");
        deii.setSourceBuilder(desb);
        deib.setAmpImportItem(deii);
        if (itemId == null) {
            deib.checkIATIInputString("check", true);
        } else {
            deib.checkIATIInputString("import", true);
        }


        DELogPerExecution execLog 	= new DELogPerExecution(dess);
        if(execLog.getLogItems() == null)
            execLog.setLogItems(new ArrayList<DELogPerItem>());
        execLog.setDeSourceSetting(dess);

        if (itemId == null) {
            items = deib.processIATIFeedReturnItems(request,execLog, null,"check", null);
        } else {
            items = deib.processIATIFeedReturnItems(request,execLog, null,"import", itemId);
        }

        if (logItems != null) logItems.addAll(execLog.getLogItems());
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
        Map<String, Integer> retVal = new HashMap<String, Integer>();
        for (Iterator it = acts.getIatiActivityOrAny().iterator(); it.hasNext();) {
            IatiActivity act = (IatiActivity) it.next();
            Set<String> countryISOs = getActivityCountries(act);

            for (String countryISO : countryISOs) {
                if (retVal.get(countryISO) == null) {
                    retVal.put(countryISO, new Integer(0));
                }
                retVal.put(countryISO, retVal.get(countryISO) + 1);
            }
        }
        return retVal;
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
                        String lang = (String)((org.digijava.module.dataExchangeIATI.iatiSchema.jaxb.TextType) objCasted.getValue()).getLang();
                        if (lang == null || lang.equals("en")) {
                            title = (String)((org.digijava.module.dataExchangeIATI.iatiSchema.jaxb.TextType) objCasted.getValue()).getContent().get(0);

                        }
                    }
                }

                if (obj instanceof org.digijava.module.dataExchangeIATI.iatiSchema.jaxb.IatiIdentifier) {
                    org.digijava.module.dataExchangeIATI.iatiSchema.jaxb.IatiIdentifier objCasted =
                            (org.digijava.module.dataExchangeIATI.iatiSchema.jaxb.IatiIdentifier) obj;
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

        Map <IatiActivity, Set<DEMappingFields>> items = getImportedItemMap(dess, is, request, logItems);
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

        Map <IatiActivity, Set<DEMappingFields>> importAndGetImportedItemMap = getImportedItemMap(dess, is, request, logItems, String.valueOf(selLogItem.getId()));

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

    public ActionForward showMapping(ActionMapping mapping, ActionForm form,
                                  HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        ImportFormNew myform = (ImportFormNew) form;
        AmpDEUploadSession upSess = myform.getUpSess();

        Set<String> selCountries = new HashSet<String> (Arrays.asList(myform.getSelCountries()));
        upSess.setSelCountries(selCountries);
        AmpDEUploadSession sess = myform.getUpSess();


        //Modify XML on initial serialization (will not be able to change country filters anymore)
        String xmlSrc = null;
        if (upSess.getId() == null) {
            if (myform.getCountryList().size() > 1) {
                DEImportValidationEventHandler fromXmllog = new DEImportValidationEventHandler();
                InputStream is = myform.getFile().getInputStream();
                IatiActivities parsed = fromXml(is, fromXmllog);
                Set<String> countryISOs = selCountries;
                for (Iterator it = parsed.getIatiActivityOrAny().iterator(); it.hasNext();) {
                    IatiActivity iatiAct = (IatiActivity) it.next();
                    Set<String> countryCode = getActivityCountries(iatiAct);
                    boolean contains = false;
                    for (String iso : countryCode) {
                        if (countryISOs.contains(iso)) {
                            contains = true;
                            break;
                        }
                    }

                    if (!contains) it.remove();
                }
                DEImportValidationEventHandler toXmllog = new DEImportValidationEventHandler();
                try {
                    xmlSrc = toXml(parsed, toXmllog);
                } catch (Exception ex) {
                    int gg = 1;
                }
                sess.setFileSrc(xmlSrc);
            } else {
                xmlSrc = new String(myform.getFile().getFileData(), "UTF-8");
                sess.setFileSrc(xmlSrc);
            }
            //upSess.setFileSrc(modifiedXML);
        } else {
            xmlSrc = upSess.getFileSrc();
        }

        ArrayList<DELogPerItem> logItems = new ArrayList<DELogPerItem>();

        DESourceSetting dess = sess.getSettingsAssigned();

        InputStream is = new ByteArrayInputStream(xmlSrc.getBytes("UTF-8"));

        Map <IatiActivity, Set<DEMappingFields>> items = getImportedItemMap(dess, is, request, logItems);

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

    public ActionForward getMappingObjects(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        ImportFormNew myform = (ImportFormNew) form;
        String selAmpClass = request.getParameter("selectedClass");

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
        String maxResultCountStr = request.getParameter("maxResultCount");
        //String searchStr = request.getParameter("searchStr");
        String searchStr = request.getHeader("searchStr");
        int maxResultCount = Integer.parseInt(maxResultCountStr);

        Map<Long, String> sortedLabels = (Map<Long,String>) request.getSession().getAttribute(IATI_LABELS_SORTED);
        JSONArray objArray = new JSONArray();
        JSONObject retObj = new JSONObject();


        JSONObject unmappedObj = new JSONObject();
        unmappedObj.accumulate("id", "0");
        unmappedObj.accumulate("val", "Unmapped");
        objArray.add(unmappedObj);

        JSONObject addNewObj = new JSONObject();
        addNewObj.accumulate("id", "-1");
        addNewObj.accumulate("val", "Add new");
        objArray.add(addNewObj);

        for (java.util.Map.Entry<Long, String> item : sortedLabels.entrySet()) {
            if (searchStr == null || searchStr.trim().isEmpty() || (item.getValue() != null && item.getValue().toLowerCase().contains(searchStr.toLowerCase()))) {

                objArray.add(new JSONObject().accumulate("id", item.getKey()).accumulate("val", item.getValue().replaceAll("'", "&lsquo;")));
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
                mf.setAmpId(newVal);
                mf.setAmpValues(newAmpObjTitle);
                mf.setDirty(true);
            }
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
                if (mf.isDirty() && mf.getAmpId()!= null && !mf.getAmpId().equals(0l)) {
                    if (mf.getId() == null || mf.getId().equals(0l)) {
                        forCreate.add(mf);
                    } else {
                        forUpdate.add(mf);
                    }
                }

                if (mf.isDirty() && (mf.getAmpId()== null || mf.getAmpId().equals(0l))) {
                    forDelete.add(mf);
                    forDeleteIds.add(mf.getId());
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
        JAXBContext jc = JAXBContext.newInstance(Constants.IATI_JAXB_INSTANCE);
        Unmarshaller m = jc.createUnmarshaller();
        URL rootUrl   = this.getClass().getResource("/");
        String path="";
        try {
            path     = rootUrl.toURI().resolve("../../").getPath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        SchemaFactory sf = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = sf.newSchema(new File(path+Constants.IATI_SCHEMA_LOCATION));

        m.setSchema(schema);
        m.setEventHandler(log);
        IatiActivities parsed = (IatiActivities) m.unmarshal(is) ;
        return parsed;
    }

    private String toXml(IatiActivities parsed, DEImportValidationEventHandler log) throws SAXException, JAXBException {
        JAXBContext jc = JAXBContext.newInstance(Constants.IATI_JAXB_INSTANCE);
        URL rootUrl   = this.getClass().getResource("/");
        String path="";
        try {
            path     = rootUrl.toURI().resolve("../../").getPath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        SchemaFactory sf = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = sf.newSchema(new File(path+Constants.IATI_SCHEMA_LOCATION));
        OutputStream bOut = new ByteArrayOutputStream();
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setSchema(schema);

        marshaller.marshal(parsed, bOut);
        return bOut.toString();
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

        Map <IatiActivity, Set<DEMappingFields>> items = getImportedItemMap(dess, is, request, null);
        myform.setIatiImportedProjectMap(items);

        Set<String> countryISOs = sess.getSelCountries();

        myform.setSelCountries(countryISOs.toArray(new String[0]));

        Map <String, Set<IatiActivity>> countryActMap = getCountryActivityMap(items);
        myform.setCountryActMap(countryActMap);
        myform.setPage(IATI_IMPORT_PAGE_MAPPING);
        myform.setUpSess(sess);

        return mapping.findForward("map");
    }


}
