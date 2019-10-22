/**
 * 
 */
package org.digijava.module.dataExchange.engine;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.apache.log4j.Logger;
import org.digijava.module.aim.dbentity.AmpActivityContact;
import org.digijava.module.aim.dbentity.AmpActivityInternalId;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpContactProperty;
import org.digijava.module.aim.dbentity.AmpContentTranslation;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingAmount;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpRegionalFunding;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.dbentity.AmpSector;

import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.ContactInfoUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.SectorUtil;

import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

import org.digijava.module.dataExchange.dbentity.AmpMappedField;
import org.digijava.module.dataExchange.dbentity.DEMappingFields;
import org.digijava.module.dataExchange.dbentity.DESourceSetting;
import org.digijava.module.dataExchange.iati.IatiVersion;
import org.digijava.module.dataExchange.pojo.DEProposedProjectCost;
import org.digijava.module.dataExchange.util.DataExchangeConstants;
import org.digijava.module.dataExchange.utils.DEConstants;
import org.digijava.module.dataExchange.utils.DataExchangeUtils;

import org.digijava.module.dataExchangeIATI.iatiSchema.v1_03.jaxb.*;

import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.editor.exception.EditorException;
import org.digijava.module.translation.util.ContentTranslationUtil;

/**
 * @author dan
 *
 */
public class IatiActivityWorker {

    private static Logger logger = Logger.getLogger(IatiActivityWorker.class);

    private boolean ignoreSameAsCheck = false;
    private boolean saveObjects = true;
    private boolean isLoad = false;
    private Set<DEMappingFields> accumulate = new HashSet<DEMappingFields>();

    private IatiVersion iatiVersion;

    // The selected country should be only one.
    // Because the situation when we import activities that belong to country A into country B is not possible as of today
    private String selectedCountry;

    // could be either update or create. Create by default
    private int mode = DEConstants.MODE_CREATE;

    public void setSelectedCountry(String selectedCountry) {
        this.selectedCountry = selectedCountry;
    }

    public boolean isIgnoreSameAsCheck() {
        return ignoreSameAsCheck;
    }

    public void setIgnoreSameAsCheck(boolean ignoreSameAsCheck) {
        this.ignoreSameAsCheck = ignoreSameAsCheck;
    }

    public Set<DEMappingFields> getAccumulate() {
        return accumulate;
    }

    public void setAccumulate(Set<DEMappingFields> accumulate) {
        this.accumulate = accumulate;
    }

    public boolean isSaveObjects() {
        return saveObjects;
    }

    public void setSaveObjects(boolean saveObjects) {
        this.saveObjects = saveObjects;
    }

    /**
     * 
     */
    private IatiActivity iActivity;
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIatiID() {
        return iatiID;
    }

    public void setIatiID(String iatiID) {
        this.iatiID = iatiID;
    }

    private String title ="";
    private String iatiID="";
    private Long ampID = null;
    
    private Boolean existingActivity = false;
    
    public Boolean getExistingActivity() {
        return existingActivity;
    }

    public void setExistingActivity(Boolean existingActivity) {
        this.existingActivity = existingActivity;
    }

    public Long getAmpID() {
        return ampID;
    }

    public void setAmpID(Long ampID) {
        this.ampID = ampID;
    }

    private String lang = "en";
    
    
    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }


    private IatiActivityWorker(IatiActivity iActivity, String title, String iatiID, String log, String lang, IatiVersion version) {
        this.iActivity = iActivity;
        this.title = title;
        this.iatiID = iatiID;
        this.log = log;
        this.lang = lang;
        this.iatiVersion = version;
    }

    public IatiActivityWorker(IatiActivity iActivity, String lang, String log, IatiVersion version) {
        this(iActivity, null, null, log, lang, version);
    }

    public IatiActivityWorker(IatiActivity iActivity, String log, IatiVersion version) {
        this(iActivity, null, null, log, null, version);
    }

    public IatiActivity getiActivity() {
        return iActivity;
    }

    public void setiActivity(IatiActivity iActivity) {
        this.iActivity = iActivity;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    private String log;
    private Date iatiLastUpdateDate;
    
    public Date getIatiLastUpdateDate() {
        return iatiLastUpdateDate;
    }

    public void setIatiLastUpdateDate(Date iatiLastUpdateDate) {
        this.iatiLastUpdateDate = iatiLastUpdateDate;
    }

    public IatiActivityWorker() {
        // TODO Auto-generated constructor stub
    }

    public boolean existActivityByTitleIatiId(String s){
        if(s==null || "".compareTo(s.trim()) ==0 ) return false;
        String title = "";
        String id = "";
        for (Iterator<Object> it = this.getiActivity().getActivityWebsiteOrReportingOrgOrParticipatingOrg().iterator(); it.hasNext();) {
            Object contentItem = (Object) it.next();
            if(contentItem instanceof JAXBElement){
                JAXBElement i = (JAXBElement)contentItem;
                if(i.getName().equals(new QName("title"))){
                    JAXBElement<TextType> item = (JAXBElement<TextType>)i;
                    if (item.getValue().getLang() == null || item.getValue().getLang().equals(this.getLang())) {
                        title += printTextType(item);
                    }
                }
            }
            if(contentItem instanceof IatiIdentifier){
                IatiIdentifier item = (IatiIdentifier)contentItem;
                id += item.getContent();
            }
            
        }
        if( s.toLowerCase().compareTo((title+" - "+id).toLowerCase()) == 0 ) 
            return true;
        return false;
    }

        // TODO Auto-generated method stub
        public ArrayList<AmpMappedField> checkContent(int noAct, String hierarchies) {
            ArrayList<AmpMappedField> logs = new ArrayList<AmpMappedField>();
            if (this.getiActivity() != null && this.getiActivity().getHierarchy() != null) {
                if (hierarchies != null && !hierarchies.contains(this.getiActivity().getHierarchy().toString())) {
                    System.out.println("Skipping activity no " + noAct + " - Hierarchy no: " + this.getiActivity().getHierarchy());
                    return null;
                }
            }
            try {
                this.iatiLastUpdateDate = DataExchangeUtils.XMLGregorianDateToDate(this.getiActivity().getLastUpdatedDatetime());
                for (Object contentItem : this.getiActivity().getActivityWebsiteOrReportingOrgOrParticipatingOrg()) {
                    if (contentItem instanceof JAXBElement) {
                        JAXBElement i = (JAXBElement) contentItem;

                        //title
                        if (i.getName().equals(new QName("title"))) {
                            JAXBElement<TextType> item = (JAXBElement<TextType>) i;
                            if (item.getValue().getLang() == null || item.getValue().getLang().equals(this.getLang())) {
                                System.out.println("Activity " + noAct + ":" + printTextType(item) + "#");
                                this.title = printTextType(item);
                            }
                        }
                        //status
                        else if (i.getName().equals(new QName("activity-status"))) {
                            JAXBElement<CodeType> item = (JAXBElement<CodeType>) i;
                            AmpMappedField existStatusCode = checkStatusCode(item);
                            if (existStatusCode != null) logs.add(existStatusCode);
                        }

                        //default-finance-type == type of assistance
                        else if (i.getName().equals(new QName("default-finance-type"))) {
                            JAXBElement<CodeReqType> item = (JAXBElement<CodeReqType>) i;
                            AmpMappedField existFinanceType = checkFinanceType(item);
                            if (existFinanceType != null) logs.add(existFinanceType);
                        }

                        //default-aid-type == financing instrument
                        else if (i.getName().equals(new QName("default-aid-type"))) {
                            JAXBElement<CodeReqType> item = (JAXBElement<CodeReqType>) i;
                            AmpMappedField existAidType = checkAidType(item);
                            if (existAidType != null) logs.add(existAidType);
                        }

                        //implementation-level
                        else if (i.getName().equals(new QName("implementation-level"))) {
                            JAXBElement<CodeType> item = (JAXBElement<CodeType>) i;
                            AmpMappedField existAidType = checkLevelType(item);
                            if (existAidType != null) logs.add(existAidType);
                        }

                    } else if (contentItem instanceof IatiIdentifier) {
                        IatiIdentifier item = (IatiIdentifier) contentItem;
                        this.iatiID = item.getContent();
                    } else if (contentItem instanceof ReportingOrg) {
                        ReportingOrg item = (ReportingOrg) contentItem;

                        AmpMappedField existOrganization = checkOrganization(printList(item.getContent()), item.getLang(), item.getRef());
                        if (existOrganization != null) {
                            logs.add(existOrganization);
                        }
                    /* AMP-17404 remove organization type from the IATI import
                    AmpMappedField existOrganizationType = checkOrganizationType(item.getType());
                    if(existOrganizationType!=null) logs.add(existOrganizationType);
                    */
                    } else if (contentItem instanceof ParticipatingOrg) {
                        ParticipatingOrg item = (ParticipatingOrg) contentItem;
                        // AmpMappedField existOrganizationType = checkOrganizationType(item.getType());
                        AmpMappedField existOrganization = checkOrganization(printList(item.getContent()), item.getLang(), item.getRef());
                        if (existOrganization != null) logs.add(existOrganization);
                        // if(existOrganizationType!=null) logs.add(existOrganizationType);
                    } else if (contentItem instanceof OtherIdentifier) {
                        OtherIdentifier item = (OtherIdentifier) contentItem;
                        AmpMappedField existOrganization = checkOrganization(item.getOwnerName(), this.getLang(), item.getOwnerRef());
                        if (existOrganization != null) logs.add(existOrganization);
                    } else if (contentItem instanceof Location) {
                        Location item = (Location) contentItem;
                        AmpMappedField existLocation = checkLocation(item);
                        if (existLocation != null) logs.add(existLocation);
                    } else if (contentItem instanceof Sector) {
                        Sector item = (Sector) contentItem;
                        AmpMappedField existSector = checkSector(item);
                    /* AMP-17404 remove 'Sector Scheme from the IATI import'
                     AmpMappedField existVocabularyCode = checkVocabularyCode(item);
                     if(existVocabularyCode!=null) logs.add(existVocabularyCode);
                     */
                        if (existSector != null) logs.add(existSector);
                    } else if (contentItem instanceof Transaction) {
                        Transaction item = (Transaction) contentItem;
                        boolean ok = false;
                        ok = checkIATITransaction(item, logs);
                    }

                /*if(contentItem instanceof PlannedDisbursement){
                    PlannedDisbursement item = (PlannedDisbursement)contentItem;
                    boolean ok       = false;
                    ok               = checkIATITransaction(item,logs);
                }*/
                }
                AmpMappedField checkedActivity = checkActivity(this.title, this.iatiID, this.lang);
                if( checkedActivity!=null && checkedActivity.getItem()!=null ) {
                    if( (!this.isLoad()) && DEConstants.AMP_ID_DO_NOT_IMPORT.equals(checkedActivity.getItem().getAmpId()) ) {
                        checkedActivity.setWarningMsg("This activity will not be imported because the user has chosen to not import it.");
                        checkedActivity.setDoNotImport(true);
                        checkedActivity.setMainEntry(true);
                    } else if( checkedActivity.getItem().getAmpId() !=null){
                        this.ampID = checkedActivity.getItem().getAmpId();
                        if( !DEConstants.AMP_ID_CREATE_NEW.equals(this.ampID) && !DEConstants.AMP_ID_DO_NOT_IMPORT.equals(this.ampID) )
                            this.existingActivity = true;
                    }
                }
                logs.add(checkedActivity);
            }
            catch(Exception e){
                e.printStackTrace();
            }
            return logs;
        }
    



    public ArrayList<AmpMappedField> populateActivity(AmpActivityVersion a, AmpActivityVersion prevVer, DESourceSetting settings, 
            List<AmpContentTranslation> translations){
        ArrayList<AmpMappedField> logs = new ArrayList<AmpMappedField>();

        boolean isCreate = DEConstants.MODE_CREATE == mode;

        ArrayList<JAXBElement<TextType>> iatiTitleList = new ArrayList<JAXBElement<TextType>>();
        ArrayList<JAXBElement<CodeType>> iatiStatusList = new ArrayList<JAXBElement<CodeType>>();
        ArrayList<JAXBElement<CodeType>> iatiImplementationLevelList = new ArrayList<JAXBElement<CodeType>>();
        ArrayList<ParticipatingOrg> iatiPartOrgList = new ArrayList<ParticipatingOrg>();
        ArrayList<ParticipatingOrg> iatiDefaultFundingOrgList = new ArrayList<ParticipatingOrg>();
        ArrayList<ParticipatingOrg> iatiExtendingOrgList = new ArrayList<ParticipatingOrg>();
        ArrayList<ReportingOrg> iatiRepOrgList = new ArrayList<ReportingOrg>();
        ArrayList<Sector> iatiSectorList = new ArrayList<Sector>();
        ArrayList<Budget> iatiBudgetList = new ArrayList<Budget>();
        ArrayList<Transaction> iatiTransactionList = new ArrayList<Transaction>();
        ArrayList<PlannedDisbursement> iatiPlannedDisbList = new ArrayList<PlannedDisbursement>();
        ArrayList<Description> iatiDescriptionList = new ArrayList<Description>();
        ArrayList<IatiIdentifier> iatiID = new ArrayList<IatiIdentifier>();
        ArrayList<OtherIdentifier> iatiOtherIdList = new ArrayList<OtherIdentifier>();
        ArrayList<ActivityDate> iatiActDateList = new ArrayList<ActivityDate>();
        ArrayList<ContactInfo> iatiContactList = new ArrayList<ContactInfo>();
        ArrayList<Location> iatiLocationList = new ArrayList<Location>();
        
        ArrayList<AmpClassificationConfiguration> allClassificationConfiguration = (ArrayList<AmpClassificationConfiguration>) getAllClassificationConfiguration();
        
        ArrayList<JAXBElement<CodeReqType>> iatiDefaultFinanceType = new ArrayList<JAXBElement<CodeReqType>>();
        ArrayList<JAXBElement<CodeReqType>> iatiDefaultAidType = new ArrayList<JAXBElement<CodeReqType>>();
                
        
        
        if(this.getiActivity().getLang()!=null && !this.getiActivity().getLang().trim().isEmpty()){
            this.lang = this.getiActivity().getLang();
        }
        
        
        String iatiDefaultCurrency = "USD";
        if(this.getiActivity().getDefaultCurrency()!=null || "".compareTo(this.getiActivity().getDefaultCurrency().trim()) == 0)
            iatiDefaultCurrency = this.getiActivity().getDefaultCurrency();
        
        
        //seting the iati last update date
        Date iatiUpdatedDate = DataExchangeUtils.XMLGregorianDateToDate(this.getiActivity().getLastUpdatedDatetime());
        if (iatiUpdatedDate == null) {
            a.setIatiLastUpdatedDate(new Timestamp(System.currentTimeMillis()));
        } else {
            a.setIatiLastUpdatedDate(iatiUpdatedDate);
        }
        
        //workspace settings + team lead owner
        
        //populate the lists with IATI values of the activity
        extractIATIEntities(iatiTitleList, iatiStatusList, iatiImplementationLevelList, iatiPartOrgList,iatiDefaultFundingOrgList, iatiExtendingOrgList,
                iatiRepOrgList, iatiSectorList, iatiBudgetList, iatiTransactionList,iatiPlannedDisbList,
                iatiDescriptionList, iatiOtherIdList, iatiActDateList,
                iatiContactList, iatiLocationList,iatiDefaultFinanceType, iatiDefaultAidType, iatiID);

        if ((isCreate && settings.importEnabled("Title")) || (!isCreate && settings.updateEnabled("Title"))) {
            processTitle(a, iatiTitleList, translations);
        }

        if (a.getFunding() == null) {
            a.setFunding(new HashSet());
        }
        if (a.getSectors() == null) {
            a.setSectors(new HashSet());
        }
        if (a.getOrgrole() == null) {
            a.setOrgrole(new HashSet<AmpOrgRole>());
        }
        if (a.getLocations() == null) {
            a.setLocations(new HashSet());
        }
        if (a.getActivityContacts() == null) {
            a.setActivityContacts(new HashSet<AmpActivityContact>());
        }


        // clear the existing steps
        if (DEConstants.MODE_UPDATE == mode) {
            a.setDraft(false);
            a.getFunding().clear();
            a.getSectors().clear();
            a.getOrgrole().clear();
            a.getLocations().clear();
            a.getActivityContacts().clear();
        }


        processIdentificationStep(a,iatiStatusList,iatiImplementationLevelList,iatiDescriptionList,iatiID, isCreate, settings);

        processPlanningStep(a,iatiActDateList);

        if ((isCreate && settings.importEnabled("Sector")) || (!isCreate && settings.updateEnabled("Sector"))) {
            processSectorsStep(a,iatiSectorList,allClassificationConfiguration);
        }

        processRelOrgsStep(a, iatiPartOrgList);

        processActInternalIdsStep(a, iatiOtherIdList, iatiRepOrgList);
        processBudgetStep(a, iatiBudgetList, iatiDefaultFundingOrgList, iatiExtendingOrgList, iatiRepOrgList,iatiDefaultFinanceType,iatiDefaultAidType, iatiDefaultCurrency, isCreate, settings);
        processFundingStep(a, iatiTransactionList, iatiPlannedDisbList,iatiDefaultFinanceType,iatiDefaultAidType, iatiDefaultCurrency,
                iatiDefaultFundingOrgList, iatiExtendingOrgList, iatiRepOrgList, isCreate, settings, translations);

        if (settings.isRegionalFundings()) {
            a.getRegionalFundings().clear();
            if (a.getFunding() != null) {
                for (AmpFunding af : a.getFunding()) {
                    a.getRegionalFundings()
                            .addAll(getRegFundingsFromFunding(af, settings.getDefaultLocation(), a));
                }

                a.getFunding().clear();
            }

        }

        if ((isCreate && settings.importEnabled("Location")) || (!isCreate && settings.updateEnabled("Location"))) {
            processLocationStep(a, iatiLocationList, settings.getDefaultLocation());
        }

        if ((isCreate && settings.importEnabled("Contacts")) || (!isCreate && settings.updateEnabled("Contacts"))) {
            processContactsStep(a, iatiContactList);
        }
        return logs;
    }




    private void processTitle(AmpActivityVersion a, ArrayList<JAXBElement<TextType>> iatiTitleList,
                              List<AmpContentTranslation> translations) {
        if(iatiTitleList.isEmpty()) return;

        String title = null;
        for (Iterator <JAXBElement<TextType>> it = iatiTitleList.iterator(); it.hasNext();) {
            JAXBElement<TextType> itVal = it.next();
            String lang = itVal.getValue().getLang() == null? this.getLang() : itVal.getValue().getLang();
            String name = printList(itVal.getValue().getContent());
            //detects default language title
            if (lang.equals(this.getLang())) {
                title = name;
            } else {
                //multilingual titles
                translations.add(getAmpContentTranslation(a, a.getAmpActivityId(), "name", lang, name));
            }
        }
        // title to be used when multilingual is disabled
        a.setName(title);
        this.setTitle(a.getName());
    }

    private void processContactsStep(AmpActivityVersion a, ArrayList<ContactInfo> iatiContactList) {
        if (iatiContactList.isEmpty()) return;
        Set<AmpActivityContact> activityContacts = new HashSet<AmpActivityContact>();
        for (ContactInfo contactInfo : iatiContactList) {
            AmpContact ampContact = new AmpContact();
            setAmpContactDetails(contactInfo, ampContact);
            try {
                ContactInfoUtil.saveOrUpdateContact(ampContact);
            } catch (Exception e) {
                e.printStackTrace();
            }
            AmpActivityContact ampActContact = new AmpActivityContact();
            ampActContact.setActivity(a);
            ampActContact.setContact(ampContact);
            ampActContact.setContactType(Constants.DONOR_CONTACT);
            activityContacts.add(ampActContact);

        }
        
        a.setActivityContacts(activityContacts);
    }

    private void setAmpContactDetails(ContactInfo contactInfo, AmpContact ampContact) {
        Set<AmpContactProperty> contactProperties = new TreeSet<AmpContactProperty>();

        // getOrganisationOrPersonNameOrTelephone method was renamed in 1_03
        for (Object contentItem : contactInfo.getOrganisationOrPersonNameOrJobTitle()) {
            if (contentItem instanceof JAXBElement) {
                JAXBElement i = (JAXBElement) contentItem;

                //name
                if (i.getName().equals(new QName("person-name"))) {

                    String contactName = null;

                    if (i.getValue() instanceof PlainType) {
                        contactName = ((PlainType)i.getValue()).getContent();
                    } else if (i.getValue() instanceof TextType) {
                        List contentObj = ((TextType)i.getValue()).getContent();
                        if (contentObj != null && contentObj.size() > 0) {
                            contactName = contentObj.get(0).toString();
                        }
                    }

                    if (contactName != null) {
                        setContactName(contactName, ampContact);
                    }
                }
                //organisation
                if (i.getName().equals(new QName("organisation"))) {
                    String orgName = null;

                    if (i.getValue() instanceof PlainType) {
                        orgName = ((PlainType)i.getValue()).getContent();
                    } else if (i.getValue() instanceof TextType) {
                        List contentObj = ((TextType)i.getValue()).getContent();
                        if (contentObj != null && contentObj.size() > 0) {
                            orgName = contentObj.get(0).toString();
                        }
                    }

                    ampContact.setOrganisationName(orgName);
                }
                //phone
                if (i.getName().equals(new QName("telephone"))) {
                    ContactInfo.Telephone item = (ContactInfo.Telephone) i.getValue();
                    AmpContactProperty acp = new AmpContactProperty();
                    acp.setValue(item.getContent());
                    acp.setName(Constants.CONTACT_PROPERTY_NAME_PHONE);
                    contactProperties.add(acp);
                }

                //email
                if (i.getName().equals(new QName("email"))) {
                    JAXBElement<PlainType> item = (JAXBElement<PlainType>) i;
                    AmpContactProperty acp = new AmpContactProperty();
                    acp.setValue(item.getValue().getContent());
                    acp.setName(Constants.CONTACT_PROPERTY_NAME_EMAIL);
                    contactProperties.add(acp);
                }

                //mailing-address
                if (i.getName().equals(new QName("mailing-address"))) {
                    ContactInfo.MailingAddress item = (ContactInfo.MailingAddress) i.getValue();
                    ampContact.setOfficeaddress(item.getContent());
                }
            }
        }
        ampContact.setProperties(contactProperties);
    }

    private void setContactName(String s, AmpContact ampContact) {
        // TODO Auto-generated method stub
        int i = s.indexOf(" ");
        ampContact.setName(s.substring(0,i));
        ampContact.setLastname(s.substring(i+1,s.length()));
    }

    private void processLocationStep(AmpActivityVersion a, ArrayList<Location> iatiLocationList, AmpCategoryValueLocations defaultLocation) {

        if (iatiLocationList != null && !iatiLocationList.isEmpty()) {
            Set<AmpActivityLocation> locations = new HashSet<AmpActivityLocation>();
            for (Location location : iatiLocationList) {
                TreeMap<String, String> locationDetails = new TreeMap<String, String>();
                getLocationDetails(location, locationDetails);
                AmpLocation ampLocation = getAmpLocation(toIATIValues("locationName", "locationType", "locationCountry", "adm1", "adm2", "adm3"),
                        toIATIValues(locationDetails.get("name"), locationDetails.get("location-type"), locationDetails.get("country"),
                                locationDetails.get("adm1"), locationDetails.get("adm2"), locationDetails.get("adm3")));

                if (ampLocation != null) {
                    AmpActivityLocation actLoc = new AmpActivityLocation();
                    actLoc.setActivity(a);
                    actLoc.setLocation(ampLocation);
                    // the percentage is an optional field according to 1_03 schema.
                    // https://jira.dgfoundation.org/browse/AMP-18206
                    if (location.getPercentage() != null) {
                        Double percent = location.getPercentage().doubleValue();
                        actLoc.setLocationPercentage(percent.floatValue());
                    }
                    locations.add(actLoc);
                }
            }

            a.getLocations().addAll(locations);

        // if locations section is empty in the imported activity, then set the default one
        } else if (defaultLocation != null) {
            try {
                Set<AmpActivityLocation> locations = new HashSet<>();
                AmpLocation ampLocation = DynLocationManagerUtil.getAmpLocation(defaultLocation);

                if (ampLocation != null) {
                    AmpActivityLocation actLoc = new AmpActivityLocation();
                    actLoc.setActivity(a);
                    actLoc.setLocation(ampLocation);
                    locations.add(actLoc);
                    a.setLocations(locations);
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getStackTrace());
            }

        } else {
            a.setLocations(new HashSet<AmpActivityLocation>());
        }
    }

    private void processBudgetStep(AmpActivityVersion activity, ArrayList<Budget> iatiBudgetList,
            ArrayList<ParticipatingOrg> iatiDefaultFundingOrgList, 
            ArrayList<ParticipatingOrg> iatiExtendingOrgList, 
            ArrayList<ReportingOrg> iatiRepOrgList,
            ArrayList<JAXBElement<CodeReqType>> iatiDefaultFinanceType,
            ArrayList<JAXBElement<CodeReqType>> iatiDefaultAidType, String iatiDefaultCurrency,
            boolean isCreate, DESourceSetting settings) {

        if (iatiBudgetList.isEmpty()) {
            return;
        }

        JAXBElement<CodeReqType> iatiDefFinTypeLocal = null;
        if(iatiDefaultFinanceType !=null && !iatiDefaultFinanceType.isEmpty())
            iatiDefFinTypeLocal=iatiDefaultFinanceType.iterator().next();
        JAXBElement<CodeReqType> iatiDefAidTypeLocal = null;
        if (iatiDefaultAidType != null && !iatiDefaultAidType.isEmpty()) {
            iatiDefAidTypeLocal = iatiDefaultAidType.iterator().next();
        }
        AmpCategoryValue typeOfAssistance = getAmpCategoryValue(iatiDefFinTypeLocal, DataExchangeConstants.IATI_FINANCE_TYPE,
                toIATIValues("financeTypeValue","financeTypeCode"),this.getLang(),null,CategoryConstants.TYPE_OF_ASSISTENCE_NAME,null,null,"active");
        AmpCategoryValue financingInstrument = getAmpCategoryValue(iatiDefAidTypeLocal, DataExchangeConstants.IATI_AID_TYPE,
                toIATIValues("aidTypeValue","aidTypeCode"),this.getLang(),null,CategoryConstants.FINANCING_INSTRUMENT_NAME,null,null,"active");
        
        if (activity.getFunding() == null) {
            activity.setFunding(new HashSet());
        }

        Set<AmpFunding> fundings = activity.getFunding();
        for (Budget budget : iatiBudgetList) {
            AmpFunding f = getBudgetIATItoAMP(activity, budget, typeOfAssistance, financingInstrument, iatiDefaultCurrency, fundings,
                    iatiDefaultFundingOrgList,
                    iatiExtendingOrgList,
                    iatiRepOrgList, isCreate, settings);
            if (f != null)
                fundings.add(f);
        }
        //activity.getFunding().addAll(fundings);
    }

    private double extractRecipientCountryPercentage(String selectedCountry) {
        if (selectedCountry != null) {
            for (Object element : this.getiActivity().getActivityWebsiteOrReportingOrgOrParticipatingOrg()) {
                if (element instanceof RecipientCountry) {
                    RecipientCountry rc = (RecipientCountry) element;
                    if (selectedCountry.equalsIgnoreCase(rc.getCode()) && rc.getPercentage() != null) {
                        return rc.getPercentage().doubleValue();
                    }
                }
            }
        }
        return 100;
    }


    private void processFundingStep(AmpActivityVersion activity, ArrayList<Transaction> iatiTransactionList,ArrayList<PlannedDisbursement> iatiPlannedDisbList,ArrayList<JAXBElement<CodeReqType>> iatiDefaultFinanceType,
            ArrayList<JAXBElement<CodeReqType>> iatiDefaultAidType, String iatiDefaultCurrency, 
            ArrayList<ParticipatingOrg> iatiDefaultFundingOrgList, 
            ArrayList<ParticipatingOrg> iatiExtendingOrgList, 
            ArrayList<ReportingOrg> iatiRepOrgList,
            boolean isCreate, DESourceSetting settings, List<AmpContentTranslation> translations) {
        
        if(iatiTransactionList.isEmpty()) return;
        JAXBElement<CodeReqType> iatiDefFinTypeLocal = null;
        if(iatiDefaultFinanceType !=null && !iatiDefaultFinanceType.isEmpty())
            iatiDefFinTypeLocal=iatiDefaultFinanceType.iterator().next();
        JAXBElement<CodeReqType> iatiDefAidTypeLocal = null;
        if(iatiDefaultAidType !=null && !iatiDefaultAidType.isEmpty())
            iatiDefAidTypeLocal =iatiDefaultAidType.iterator().next();
        AmpCategoryValue typeOfAssistance = getAmpCategoryValue(iatiDefFinTypeLocal, DataExchangeConstants.IATI_FINANCE_TYPE,
                toIATIValues("financeTypeValue","financeTypeCode"),this.getLang(),null,CategoryConstants.TYPE_OF_ASSISTENCE_NAME,null,null,"active");
        AmpCategoryValue financingInstrument = getAmpCategoryValue(iatiDefAidTypeLocal, DataExchangeConstants.IATI_AID_TYPE,
                toIATIValues("aidTypeValue","aidTypeCode"),this.getLang(),null,CategoryConstants.FINANCING_INSTRUMENT_NAME,null,null,"active");
        
        //info regarding proposed project cost = sum of all actual commitments
//      Date proposedProjectCostDate            = null;
//      Double proposedProjectCostAmount        = new Double(0);
//      String proposedProjectCostCurrency  = null;
        DEProposedProjectCost proposedProjectCost = new DEProposedProjectCost();

        Set<AmpFunding> fundings = activity.getFunding();

        if (activity.getRegionalFundings() == null) {
            activity.setRegionalFundings(new HashSet());
        } else {
            activity.getRegionalFundings().clear();
        }

        // Set<AmpRegionalFunding> regionalFundings = activity.getRegionalFundings();
        for (Transaction transaction : iatiTransactionList) {
            AmpFunding f = getFundingIATItoAMP(activity, transaction, typeOfAssistance, financingInstrument, iatiDefaultCurrency,
                    iatiDefaultFundingOrgList, iatiExtendingOrgList, iatiRepOrgList,
                    proposedProjectCost, isCreate, settings, translations);

            if (f != null) {
                if (!settings.isRegionalFundings()) {
                    fundings.add(f);
                }/*else {
                    regionalFundings.addAll(getRegFundingsFromFunding(f, settings.getDefaultLocation(), activity));
                }  */
            }
        }

        for (PlannedDisbursement plannedDisb : iatiPlannedDisbList) {
            AmpFunding f = getFundingIATItoAMP(activity, plannedDisb, typeOfAssistance, financingInstrument, iatiDefaultCurrency,
                    iatiDefaultFundingOrgList, iatiExtendingOrgList, iatiRepOrgList,
                    proposedProjectCost, isCreate, settings);
            if (f != null) {
                if (!settings.isRegionalFundings()) {
                    fundings.add(f);
                } /*else {
                    regionalFundings.addAll(getRegFundingsFromFunding(f, settings.getDefaultLocation(), activity));
                }   */
            }
        }

        if ((isCreate && settings.importEnabled("Other Funding Items")) || (!isCreate && settings.updateEnabled("Other Funding Items"))) {
            if(isValidString(proposedProjectCost.getCurrency()) )
                proposedProjectCost.setCurrency(iatiDefaultCurrency);
            AmpFundingAmount ppc = new AmpFundingAmount();
            ppc.setActivity(activity);
            ppc.setCurrencyCode(proposedProjectCost.getCurrency());

            if(proposedProjectCost.getDate() == null)
                proposedProjectCost.setDate(new Date());
            ppc.setFunDate(proposedProjectCost.getDate());

            ppc.setFunAmount(proposedProjectCost.getAmount());
            activity.addCostAmount(ppc);
        }


//      activity.getFunding().addAll(fundings);

    }

    private Set<AmpRegionalFunding> getRegFundingsFromFunding(AmpFunding fnd, AmpCategoryValueLocations loc, AmpActivityVersion act) {
        Set<AmpRegionalFunding> retVal = new HashSet<AmpRegionalFunding>();
        for (AmpFundingDetail afd : fnd.getFundingDetails()) {
            AmpRegionalFunding regFnd = new AmpRegionalFunding();
            regFnd.setActivity(act);
            regFnd.setTransactionType(afd.getTransactionType());
            regFnd.setAdjustmentType(afd.getAdjustmentType());
            regFnd.setCurrency(afd.getAmpCurrencyId());
            regFnd.setTransactionAmount(afd.getTransactionAmount());
            regFnd.setTransactionDate(afd.getTransactionDate());
            regFnd.setRegionLocation(loc);
            retVal.add(regFnd);
        }
        return retVal;
    }

    
    private AmpFunding getBudgetIATItoAMP(AmpActivityVersion activity, Budget budget, AmpCategoryValue typeOfAssistance, 
            AmpCategoryValue financingInstrument, String iatiDefaultCurrency, Set<AmpFunding> fundings, 
            ArrayList<ParticipatingOrg> iatiDefaultFundingOrgList, 
            ArrayList<ParticipatingOrg> iatiExtendingOrgList, 
            ArrayList<ReportingOrg> iatiRepOrgList,
            boolean isCreate, DESourceSetting settings) {

        if ((isCreate && !settings.importEnabled("Other Funding Items")) || (!isCreate && !settings.updateEnabled("Other Funding Items"))) {
            return null;
        }

        Double currencyValue = 0.0;
        String currencyName = iatiDefaultCurrency;
        Date startDate = null;
        Date endDate = null;
        for (Object contentItem : budget.getPeriodStartOrPeriodEndOrValue()) {
            if (contentItem instanceof JAXBElement) {
                JAXBElement i = (JAXBElement) contentItem;
                //value and currency
                if (i.getName().equals(new QName("value"))) {
                    CurrencyType item = (CurrencyType) i.getValue();
                    currencyValue = item.getValue().doubleValue();
                    if (isValidString(item.getCurrency()))
                        currencyName = item.getCurrency();
                }

                if (i.getName().equals(new QName("period-start"))) {
                    DateType item = (DateType) i.getValue();
                    XMLGregorianCalendar isoDate = item.getIsoDate();

                    if (isoDate != null)
                        startDate = DataExchangeUtils.XMLGregorianDateToDate(isoDate);
                    //else dateToSet    =   DataExchangeUtils.stringToDate(item.get);
                }

                //TODO check if this still to be added to AMP
                if (i.getName().equals(new QName("period-end"))) {
                    DateType item = (DateType) i.getValue();
                    XMLGregorianCalendar isoDate = item.getIsoDate();

                    if (isoDate != null)
                        endDate = DataExchangeUtils.XMLGregorianDateToDate(isoDate);
                }
            }
        }
        
        AmpOrganisation ampOrg = null;
        ampOrg = findFundingOrganization(iatiDefaultFundingOrgList, iatiExtendingOrgList, iatiRepOrgList);
        //we can not import funding with Donor null
        if (ampOrg == null) return null;
        
//      ReportingOrg defaultReportingOrg = iatiRepOrgList.iterator().next();
//      ampOrg = getAmpOrganization(printList(defaultReportingOrg.getContent()), this.getLang(), defaultReportingOrg.getRef());
        //there is no participating organization with funding role and the 
        //the transaction has no source organization - donor
        //if(ampOrg==null) return null;
        
        Set<AmpFunding> ampFundings = fundings;
        if (ampFundings == null)
            ampFundings = new HashSet<AmpFunding>();

        AmpFunding ampFunding = null;
        for (AmpFunding af : ampFundings) {
            if (ampOrg.compareTo(af.getAmpDonorOrgId()) == 0) {
                ampFunding = af;
                break;
            }
        }
        
        // TODO: Evaluate if we need a more complex mapping here because IATI defines 4 organization roles (http://iatistandard.org/103/codelists/organisation_role/).
        // There is an example of that in the method 'processRelOrgsStep'.
        AmpRole role = DbUtil.getAmpRole(org.digijava.module.aim.helper.Constants.FUNDING_AGENCY);

        Set<AmpFundingDetail> ampFundDetails = new HashSet<AmpFundingDetail>();

        Date dateToSet = null;
        if (startDate != null) {
            dateToSet = startDate;
        } else if (endDate != null) {
            dateToSet = endDate;
        }
        //if there is no date, the budget element should be skipped
        else return null;

        populateFundingDetails(currencyValue, currencyName, dateToSet, ampFundDetails,
                org.digijava.module.aim.helper.Constants.COMMITMENT,
                Constants.ACTUAL);

        //ampFunding.setAmpDonorOrgId(ampOrg);
        if (ampFunding == null) {
            ampFunding = new AmpFunding();
            ampFunding.setAmpDonorOrgId(ampOrg);
            ampFunding.setGroupVersionedFunding(System.currentTimeMillis());
            ampFunding.setFundingDetails(ampFundDetails);
            ampFunding.setTypeOfAssistance(typeOfAssistance);
            ampFunding.setFinancingInstrument(financingInstrument);
            ampFunding.setModeOfPayment(null);
            ampFunding.setSourceRole(role);
        } else if (ampFunding.getFundingDetails() != null) {
            ampFunding.getFundingDetails().addAll(ampFundDetails);
        } else {
            ampFunding.setFundingDetails(ampFundDetails);
        }

        ampFunding.setActive(true);

        Set<AmpOrgRole> orgRole = new HashSet<AmpOrgRole>();
        AmpOrgRole ampOrgRole = new AmpOrgRole();
        ampOrgRole.setActivity(activity);
        ampOrgRole.setRole(role);
        ampOrgRole.setOrganisation(ampOrg);
        orgRole.add(ampOrgRole);

        if (activity != null) {
            ampFunding.setAmpActivityId(activity);

            if (activity.getOrgrole() == null) {
                activity.setOrgrole(orgRole);
            } else{
                activity.getOrgrole().addAll(orgRole);
            }
        }

        
        return ampFunding;
    }

    private AmpFunding getFundingIATItoAMP(AmpActivityVersion a, PlannedDisbursement t, AmpCategoryValue iatiDefaultFinanceType, AmpCategoryValue iatiDefaultAidType,
            String iatiDefaultCurrency, 
            ArrayList<ParticipatingOrg> iatiDefaultFundingOrgList, 
            ArrayList<ParticipatingOrg> iatiExtendingOrgList, 
            ArrayList<ReportingOrg> iatiRepOrgList, 
            DEProposedProjectCost ppc,
            boolean isCreate, DESourceSetting settings){

        if ((isCreate && !settings.importEnabled("Planned Disbursements")) || (!isCreate && !settings.updateEnabled("Planned Disbursements"))) {
            return null;
        }

        Set<AmpOrgRole> orgRole = new HashSet<AmpOrgRole>();
        AmpRole role = DbUtil.getAmpRole(org.digijava.module.aim.helper.Constants.FUNDING_AGENCY);
        AmpOrganisation ampOrg = null;
        AmpCategoryValue modeOfPayment = null;
        String transactionType = "";
        String description ="";
        Date sDate = new Date();
        Date eDate = new Date();
        Double currencyValue = 0.0;
        String currencyName = iatiDefaultCurrency;

        for (Object contentItem : t.getPeriodStartOrPeriodEndOrValue()) {
            if (contentItem instanceof JAXBElement) {
                JAXBElement i = (JAXBElement) contentItem;


                /**
                 *  <xsd:choice minOccurs="0" maxOccurs="unbounded">
                 *      <xsd:element name="period-start" type="dateType">
                 *      <xsd:element name="period-end" type="dateType">
                 */

                /**
                 * Check version flag.
                 * See https://jira.dgfoundation.org/browse/AMP-17830?focusedCommentId=114035&page=com.atlassian.jira.plugin.system.issuetabpanels:comment-tabpanel#comment-114035
                 * For details
                 */
                if (i.getName().equals(new QName("period-start"))) {
                    if (iatiVersion != null && (iatiVersion.ordinal() < IatiVersion.V_1_03.ordinal())) {
                        org.digijava.module.dataExchangeIATI.iatiSchema.jaxb.PlannedDisbursement.PeriodStart item =
                                (org.digijava.module.dataExchangeIATI.iatiSchema.jaxb.PlannedDisbursement.PeriodStart) i.getValue();
                        sDate = DataExchangeUtils.XMLGregorianDateToDate(item.getIsoDate());
                    } else {
                        DateType item = (DateType) i.getValue();
                        sDate = DataExchangeUtils.XMLGregorianDateToDate(item.getIsoDate());
                    }
                }

                if (i.getName().equals(new QName("period-end"))) {
                    DateType item = (DateType) i.getValue();
                    eDate = DataExchangeUtils.XMLGregorianDateToDate(item.getIsoDate());
                }

                if (i.getName().equals(new QName("value"))) {
                    CurrencyType item = (CurrencyType) i.getValue();
                    currencyValue = item.getValue().doubleValue();
                    if (isValidString(item.getCurrency()))
                        currencyName = item.getCurrency();
                }

            }
        }
        
        ampOrg = findFundingOrganization(iatiDefaultFundingOrgList, iatiExtendingOrgList, iatiRepOrgList);
        //we can not import funding with Donor null
        if (ampOrg == null) return null;

        Set<AmpFundingDetail> ampFundDetails = new HashSet<AmpFundingDetail>();
        populateFundingDetails(currencyValue, currencyName, sDate, ampFundDetails,
                org.digijava.module.aim.helper.Constants.DISBURSEMENT,
                org.digijava.module.aim.helper.Constants.PLANNED);
        
        Set<AmpFunding> ampFundings = a.getFunding();//fundings;
        if(ampFundings == null) 
            ampFundings = new HashSet<AmpFunding>();
        AmpFunding ampFunding = new AmpFunding();
        boolean found = false;
        for (AmpFunding af : ampFundings) {
            if(ampOrg.compareTo(af.getAmpDonorOrgId()) == 0){
//              ampFunding.setFinancingId(af.getFinancingId());
//              ampFunding.setModeOfPayment(af.getModeOfPayment());
//              ampFunding.setFundingStatus(af.getFundingStatus());
//              ampFunding.setActualStartDate(af.getActualStartDate());
//              ampFunding.setActualCompletionDate(af.getActualCompletionDate());
//              ampFunding.setPlannedStartDate(af.getPlannedStartDate());
//              ampFunding.setPlannedCompletionDate(af.getPlannedCompletionDate());
//              ampFunding.setConditions(af.getConditions());
//              ampFunding.setDonorObjective(af.getDonorObjective());
//              ampFunding.setGroupVersionedFunding(af.getGroupVersionedFunding());
                ampFunding = af;
                //ampFunding.getFundingDetails().clear();
                removeFundingDetails(ampFunding, Constants.DISBURSEMENT, org.digijava.module.aim.helper.Constants.PLANNED, false);
                ampFunding.getFundingDetails().addAll(ampFundDetails);
                found = true;
                break;
            }
        }
        
        //AmpFunding ampFunding = new AmpFunding();
        ampFunding.setActive(true);
        //ampFunding.setAmpDonorOrgId(ampOrg);
        if (!found) {
            ampFunding.setAmpDonorOrgId(ampOrg);
            ampFunding.setGroupVersionedFunding(System.currentTimeMillis());
            ampFunding.setFundingDetails(ampFundDetails);
            ampFunding.setTypeOfAssistance(iatiDefaultFinanceType);
            ampFunding.setFinancingInstrument(iatiDefaultAidType);
        }

        ampFunding.setAmpActivityId(a);

        //TODO: the language - lang attribute
        if(isValidString(description)) ampFunding.setConditions(description);

        AmpOrgRole ampOrgRole = new AmpOrgRole();
        ampOrgRole.setActivity(a);
        ampOrgRole.setRole(role);
        ampOrgRole.setOrganisation(ampOrg);
        orgRole.add(ampOrgRole);

        if (a.getOrgrole() == null) {
            a.setOrgrole(orgRole);
        } else {
            a.getOrgrole().addAll(orgRole);
        }

        return ampFunding;
    }
    
    private AmpFunding getFundingIATItoAMP(AmpActivityVersion a, Transaction t, AmpCategoryValue iatiDefaultFinanceType, AmpCategoryValue iatiDefaultAidType,
            String iatiDefaultCurrency, 
            ArrayList<ParticipatingOrg> iatiDefaultFundingOrgList, 
            ArrayList<ParticipatingOrg> iatiExtendingOrgList, 
            ArrayList<ReportingOrg> iatiRepOrgList, 
            DEProposedProjectCost ppc,
            boolean isCreate, DESourceSetting settings, List<AmpContentTranslation> translations) {

        if ((isCreate && !settings.importEnabled("Other Funding Items")) || (!isCreate && !settings.updateEnabled("Other Funding Items"))) {
            return null;
        }

        Set<AmpOrgRole> orgRole = new HashSet<AmpOrgRole>();
        AmpRole role = DbUtil.getAmpRole(org.digijava.module.aim.helper.Constants.FUNDING_AGENCY);
        AmpOrganisation ampOrg = null;
        AmpCategoryValue typeOfAssistance = iatiDefaultFinanceType;
        AmpCategoryValue modeOfPayment = null;
        AmpCategoryValue financingInstrument = iatiDefaultAidType;
        // other
        char transactionType = 'o';
        Map<String, String> descriptions = new HashMap<String,String>(); //<langISO, Translation>
        Date tDate = new Date();
        double currencyValue = 0;
        String currencyName = iatiDefaultCurrency;

        for (Object contentItem : t.getValueOrDescriptionOrTransactionType()) {
            if (contentItem instanceof JAXBElement) {
                JAXBElement i = (JAXBElement) contentItem;

                //receiver-org - usually in AMP gov is the receiver
                if (i.getName().equals(new QName("receiver-org"))) {
                }

                //provider-org
                if (i.getName().equals(new QName("provider-org"))) {
                    Transaction.ProviderOrg item = (Transaction.ProviderOrg) (i.getValue());
                    ampOrg = getAmpOrganization(printList(item.getContent()), this.getLang(), item.getRef());
                }

                //disbursement-channel == mode of payment
                if (i.getName().equals(new QName("disbursement-channel"))) {
                    JAXBElement<CodeReqType> item = (JAXBElement<CodeReqType>) i;
                    modeOfPayment = getAmpCategoryValue(item, DataExchangeConstants.IATI_DISBURSEMENT_CHANNEL, toIATIValues("disbursementChannelValue", "disbursementChannelCode"),
                            this.getLang(), null, CategoryConstants.MODE_OF_PAYMENT_NAME, null, null, "active");
                }

                //finance-type == type of assistance
                if (i.getName().equals(new QName("finance-type"))) {
                    JAXBElement<CodeReqType> item = (JAXBElement<CodeReqType>) i;
                    typeOfAssistance = getAmpCategoryValue(item, DataExchangeConstants.IATI_FINANCE_TYPE,
                            toIATIValues("financeTypeValue", "financeTypeCode"), this.getLang(), null, CategoryConstants.TYPE_OF_ASSISTENCE_NAME, null, null, "active");
                    if (typeOfAssistance == null) typeOfAssistance = iatiDefaultFinanceType;
                }

                //aid-type == financing instrument
                if (i.getName().equals(new QName("aid-type"))) {
                    JAXBElement<CodeReqType> item = (JAXBElement<CodeReqType>) i;
                    financingInstrument = getAmpCategoryValue(item, DataExchangeConstants.IATI_AID_TYPE,
                            toIATIValues("aidTypeValue", "aidTypeCode"), this.getLang(), null, CategoryConstants.FINANCING_INSTRUMENT_NAME, null, null, "active");
                    if (financingInstrument == null) financingInstrument = iatiDefaultAidType;
                }

                //transaction-type
                if (i.getName().equals(new QName("transaction-type"))) {
                    JAXBElement<CodeReqType> item = (JAXBElement<CodeReqType>) i;
                    String tName = printList(item.getValue().getContent()).toLowerCase();
                    String tCode = item.getValue().getCode().toLowerCase();
                    if ("commitment".compareTo(tName) == 0 || "c".compareTo(tCode) == 0) {
                        transactionType = 'c';
                    } else if ("disbursement".compareTo(tName) == 0 || "d".compareTo(tCode) == 0) {
                        transactionType = 'd';
                    } else if ("expenditure".compareTo(tName) == 0 || "e".compareTo(tCode) == 0) {
                        transactionType = 'e';
                    } else {
                        //the transaction is not C or D or E then it is not imported in AMP
                        return null;
                    }
                }

                //transaction-date
                if (i.getName().equals(new QName("transaction-date"))) {
                    Transaction.TransactionDate item = (Transaction.TransactionDate) i.getValue();
                    tDate = DataExchangeUtils.XMLGregorianDateToDate(item.getIsoDate());
                }

                //transaction description
                if (i.getName().equals(new QName("description"))) {
                    JAXBElement<TextType> item = (JAXBElement<TextType>) i;
                    String lang = item.getValue().getLang();
                    descriptions.put(lang, printList(item.getValue().getContent()));
                }

                //value and currency
                if (i.getName().equals(new QName("value"))) {
                    CurrencyType item = (CurrencyType) i.getValue();
                    currencyValue = item.getValue().doubleValue();
                    if (isValidString(item.getCurrency()))
                        currencyName = item.getCurrency();
                }
            }
        }
        
        if (!settings.isRegionalFundings() && ampOrg == null) {
            /**
             * If transaction organization is not set then find it among
             * iati-activities/iati-activity/participating-org/ where @type="Funding"
             * See for details https://jira.dgfoundation.org/browse/AMP-17672
             * ?focusedCommentId=113463&page=com.atlassian.jira.plugin.system.issuetabpanels:comment-tabpanel#comment-113463
             */
            ampOrg = findFundingOrganization(iatiDefaultFundingOrgList, iatiExtendingOrgList, iatiRepOrgList);
            //we can not import funding with Donor null
            if (ampOrg == null) return null;
        }
        
        Set<AmpFundingDetail> ampFundDetails = new HashSet<AmpFundingDetail>();

        switch(transactionType) {
            case 'c':
                populateFundingDetails(currencyValue, currencyName, tDate, ampFundDetails,
                        Constants.COMMITMENT,
                        org.digijava.module.aim.helper.Constants.PLANNED);
                if (!isValidString(ppc.getCurrency()))
                    ppc.setCurrency(currencyName);
                if (ppc.getDate() == null)
                    ppc.setDate(tDate);
                Double amount = ppc.getAmount();
                amount += currencyValue;
                ppc.setAmount(amount);
                break;
            case 'd':
                populateFundingDetails(currencyValue, currencyName, tDate, ampFundDetails,
                        Constants.DISBURSEMENT,
                        org.digijava.module.aim.helper.Constants.ACTUAL);
                break;
            case 'e':
                if (settings.isMergeDisbAndExp()) {
                    //DRC demand for DFID - merge disb with exp into disb
                    populateFundingDetails(currencyValue, currencyName, tDate, ampFundDetails,
                            Constants.DISBURSEMENT,
                            org.digijava.module.aim.helper.Constants.ACTUAL);
                } else {
                    populateFundingDetails(currencyValue, currencyName, tDate, ampFundDetails,
                            Constants.EXPENDITURE,
                            org.digijava.module.aim.helper.Constants.ACTUAL);
                }
                break;
            //the transaction is not C or D or E then it is not imported in AMP
            default: return null;
        }


        if (a.getFunding() == null) {
            a.setFunding(new HashSet<AmpFunding>());
        } /*else {
            removeFundingDetails
            a.getFunding().clear();
        }   */

        Set<AmpFunding> ampFundings = a.getFunding();//fundings;


        AmpFunding ampFunding = null;
        for (AmpFunding af : ampFundings) {
            if (ampOrg != null && ampOrg.compareTo(af.getAmpDonorOrgId()) == 0) {
//              ampFunding.setFinancingId(af.getFinancingId());
//              ampFunding.setModeOfPayment(af.getModeOfPayment());
//              ampFunding.setFundingStatus(af.getFundingStatus());
//              ampFunding.setActualStartDate(af.getActualStartDate());
//              ampFunding.setActualCompletionDate(af.getActualCompletionDate());
//              ampFunding.setPlannedStartDate(af.getPlannedStartDate());
//              ampFunding.setPlannedCompletionDate(af.getPlannedCompletionDate());
//              ampFunding.setConditions(af.getConditions());
//              ampFunding.setDonorObjective(af.getDonorObjective());
//              ampFunding.setGroupVersionedFunding(af.getGroupVersionedFunding());
                ampFunding = af;
                //ampFunding.getFundingDetails().clear();
                removeFundingDetails(ampFunding, Constants.DISBURSEMENT, org.digijava.module.aim.helper.Constants.PLANNED, false);
                ampFunding.getFundingDetails().addAll(ampFundDetails);
                break;
            }
        }
        
        
        //AmpFunding ampFunding = new AmpFunding();

        //ampFunding.setAmpDonorOrgId(ampOrg);
        if (ampFunding == null) {
            ampFunding = new AmpFunding();
            ampFunding.setAmpDonorOrgId(ampOrg);
            ampFunding.setGroupVersionedFunding(System.currentTimeMillis());
            ampFunding.setFundingDetails(ampFundDetails);
            ampFunding.setTypeOfAssistance(typeOfAssistance);
            ampFunding.setFinancingInstrument(financingInstrument);
            ampFunding.setModeOfPayment(modeOfPayment);
            ampFunding.setSourceRole(role);
        }

        ampFunding.setActive(true);
        
        
        if (a != null) {
            ampFunding.setAmpActivityId(a);
        }
        
        for (Map.Entry<String, String> isoLangDesc : descriptions.entrySet()) {
            translations.add(getAmpContentTranslation(
                    ampFunding, ampFunding.getAmpFundingId(), "conditions", isoLangDesc.getKey(), isoLangDesc.getValue()));
            if (this.lang.equals(isoLangDesc.getKey())) {
                ampFunding.setConditions(isoLangDesc.getValue());
            }
        }
        
        AmpOrgRole ampOrgRole = new AmpOrgRole();
        ampOrgRole.setActivity(a);
        ampOrgRole.setRole(role);
        ampOrgRole.setOrganisation(ampOrg);
        orgRole.add(ampOrgRole);
        
        if (a.getOrgrole() == null) {
            a.setOrgrole(orgRole);
        }else {
            a.getOrgrole().addAll(orgRole);
        }
        
        return ampFunding;
    }

    private void removeFundingDetails (AmpFunding fnd, int transactionType, int adjustmentType, boolean removeIatiImported) {
        AmpCategoryValue adjustmentTypeCatVal =
                CategoryManagerUtil.getAmpCategoryValueFromDb(CategoryConstants.ADJUSTMENT_TYPE_KEY,   new Long(adjustmentType));
        if (fnd != null && fnd.getFundingDetails() != null) {
            for (Iterator<AmpFundingDetail> it = fnd.getFundingDetails().iterator(); it.hasNext();) {
                AmpFundingDetail fndDet = it.next();
                if (fndDet.getTransactionType() == transactionType &&
                        fndDet.getAdjustmentType().getId() == adjustmentTypeCatVal.getId()) {
                    if (!fndDet.isIatiAdded() || (fndDet.isIatiAdded() && removeIatiImported)) {
                        it.remove();
                    }
                }
            }
        }
    }

    
    private AmpOrganisation findFundingOrganization(
            ArrayList<ParticipatingOrg> iatiDefaultFundingOrgList,
            ArrayList<ParticipatingOrg> iatiExtendingOrgList,
            ArrayList<ReportingOrg> iatiRepOrgList) {

        AmpOrganisation ampOrg = null;

        if (iatiDefaultFundingOrgList != null && !iatiDefaultFundingOrgList.isEmpty())  {
            ParticipatingOrg defaultFundingOrg = iatiDefaultFundingOrgList.iterator().next();
            ampOrg = getAmpOrganization(printList(defaultFundingOrg.getContent()), this.getLang(), defaultFundingOrg.getRef());
        } else if (iatiExtendingOrgList != null && !iatiExtendingOrgList.isEmpty()) {
            ParticipatingOrg defaultFundingOrg = iatiExtendingOrgList.iterator().next();
            ampOrg = getAmpOrganization(printList(defaultFundingOrg.getContent()), this.getLang(), defaultFundingOrg.getRef());
        } else if(iatiRepOrgList != null && !iatiRepOrgList.isEmpty()) {
            ReportingOrg defaultFundingOrg = iatiRepOrgList.iterator().next();
            ampOrg = getAmpOrganization(printList(defaultFundingOrg.getContent()), this.getLang(), defaultFundingOrg.getRef());
        }

        return ampOrg;
    }

    private void populateFundingDetails(Double currencyValue, String currencyCode, Date tDate, Set<AmpFundingDetail> fundDetails, int transactionType, int adjustmentType) {
        //senegal
        if (currencyValue == 0) return;

        // https://jira.dgfoundation.org/browse/AMP-18207
        currencyValue = currencyValue * extractRecipientCountryPercentage(selectedCountry) / 100;

        AmpFundingDetail ampFundDet = new AmpFundingDetail();
        ampFundDet.setIatiAdded(true);
        ampFundDet.setTransactionType(transactionType);
        ampFundDet.setTransactionDate(tDate);
        ampFundDet.setAdjustmentType(CategoryManagerUtil.getAmpCategoryValueFromDb(CategoryConstants.ADJUSTMENT_TYPE_KEY, (long) adjustmentType));
        ampFundDet.setAmpCurrencyId(CurrencyUtil.getCurrencyByCode(currencyCode));

        //TODO how are the amounts? in thousands?
        //if("true".equals(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS)))
            //ampFundDet.setTransactionAmount(new Double(fundDet.getAmount()*1000));
        //else
        ampFundDet.setTransactionAmount(currencyValue);

        fundDetails.add(ampFundDet);
    }
    
    private void processActInternalIdsStep(AmpActivityVersion a, ArrayList<OtherIdentifier> iatiOtherIdList, ArrayList<ReportingOrg> iatiRepOrgList) {
        Set<AmpActivityInternalId> internalIds = new HashSet<AmpActivityInternalId>();
        if(iatiOtherIdList.isEmpty()) {
            for (OtherIdentifier otherIdentifier : iatiOtherIdList) {
                AmpActivityInternalId actInternalId = new AmpActivityInternalId();
                actInternalId.setInternalId(otherIdentifier.getContent());
                actInternalId.setAmpActivity(a);

                String orgName = otherIdentifier.getOwnerName();
                String orgCode = otherIdentifier.getOwnerRef();
                AmpOrganisation org = getAmpOrganization(orgName, lang, orgCode);
                if (org != null)
                    actInternalId.setOrganisation(org);
                internalIds.add(actInternalId);
            }
        }
        AmpOrganisation ampOrg = null;
        ReportingOrg defaultReportingOrg = iatiRepOrgList.iterator().next();
        ampOrg = getAmpOrganization(printList(defaultReportingOrg.getContent()), this.getLang(), defaultReportingOrg.getRef());
        //if there is no rep organization we can not assign the iatiID to no ampORG 
        if(ampOrg!=null) {
            AmpActivityInternalId actInternalId = new AmpActivityInternalId();
            actInternalId.setInternalId(this.iatiID.replace(defaultReportingOrg.getRef()+"-", ""));
            actInternalId.setAmpActivity(a);
            actInternalId.setOrganisation(ampOrg);
            internalIds.add(actInternalId);
        }
        if(internalIds==null || internalIds.isEmpty()) return;
        
        if(a.getInternalIds() == null) a.setInternalIds(new HashSet<AmpActivityInternalId>());
        else a.getInternalIds().clear();
        
        a.getInternalIds().addAll(internalIds);
        
    }

    private void processRelOrgsStep(AmpActivityVersion a, ArrayList<ParticipatingOrg> iatiPartOrgList) {

        if (iatiPartOrgList.isEmpty()) return;
        Set<AmpOrgRole> orgRole = new HashSet<AmpOrgRole>();
        for (ParticipatingOrg participatingOrg : iatiPartOrgList) {
            AmpRole role = null;
            if ("extending".compareTo(participatingOrg.getRole().toLowerCase()) == 0)
                role = DbUtil.getAmpRole(Constants.EXECUTING_AGENCY);
            if ("implementing".compareTo(participatingOrg.getRole().toLowerCase()) == 0)
                role = DbUtil.getAmpRole(Constants.IMPLEMENTING_AGENCY);
            if ("accountable".compareTo(participatingOrg.getRole().toLowerCase()) == 0)
                role = DbUtil.getAmpRole(Constants.RESPONSIBLE_ORGANISATION);

            if (role != null) {
                String orgName = printList(participatingOrg.getContent());
                String orgCode = participatingOrg.getRef();

                AmpOrganisation org = getAmpOrganization(orgName, lang, orgCode);
                if (org != null) {
                    AmpOrgRole ampOrgRole = new AmpOrgRole();
                    ampOrgRole.setActivity(a);
                    ampOrgRole.setRole(role);
                    ampOrgRole.setOrganisation(org);
                    orgRole.add(ampOrgRole);
                }
            }

        }

        a.getOrgrole().addAll(orgRole);
    }

    private void processSectorsStep(AmpActivityVersion a, ArrayList<Sector> iatiSectorList, ArrayList<AmpClassificationConfiguration> allClassificationConfiguration) {
        if(iatiSectorList.isEmpty()) return;

        Set<AmpActivitySector> sectors = new HashSet<AmpActivitySector>();
        Long sectorId;
        for (Sector sector : iatiSectorList) {
            String sectorName = printList(sector.getContent());
            String vocabulary = sector.getVocabulary() == null ? "DAC" : sector.getVocabulary();
            DEMappingFields checkMappedField = checkMappedField(DataExchangeConstants.IATI_SECTOR, toIATIValues("vocabularyName", "sectorName", "sectorCode"),
                    toIATIValues(vocabulary, sectorName, sector.getCode()), this.getLang(), null, DataExchangeConstants.IATI_SECTOR, null, null, "active");

            if (checkMappedField == null || checkMappedField.getAmpId() == null) continue;
            AmpSector ampSector = SectorUtil.getAmpSector(checkMappedField.getAmpId());

            AmpActivitySector ampActSector = new AmpActivitySector();
            ampActSector.setActivityId(a);
            sectorId = ampSector.getAmpSectorId();
            if (sectorId != null && (!sectorId.equals(new Long(-1))))
                ampActSector.setSectorId(ampSector);

            Float sectorPercentage = sector.getPercentage() == null ? 100 : sector.getPercentage().floatValue();

            ampActSector.setSectorPercentage(sectorPercentage);

            AmpClassificationConfiguration primConf = null;
            //trying to find the classification
            primConf = getConfiguration(ampSector, allClassificationConfiguration);
            //if the classification doesn't exist we will put the sector under the primary classification!
            if (primConf == null) primConf = getPrimaryClassificationConfiguration(allClassificationConfiguration);

            ampActSector.setClassificationConfig(primConf);
            sectors.add(ampActSector);

        }

        a.getSectors().addAll(sectors);
    }

    private void processPlanningStep(AmpActivityVersion a, ArrayList<ActivityDate> iatiActDateList) {
        if(iatiActDateList.isEmpty()) return;
        for (Iterator<ActivityDate> it = iatiActDateList.iterator(); it.hasNext();) {
            ActivityDate date = (ActivityDate) it.next();
            String stringDate = date.getValue();
            XMLGregorianCalendar isoDate = date.getIsoDate();
            Date dateToSet = null;
            if(isoDate != null)
                dateToSet = DataExchangeUtils.XMLGregorianDateToDate(date.getIsoDate());
            else dateToSet  =   DataExchangeUtils.stringToDate(stringDate);
            
            //Proposed Start Date
            if("start-planned".compareTo(date.getType()) ==0 ){
                a.setProposedStartDate(dateToSet);
            }
            //Date of Planned Completion
            if("end-planned".compareTo(date.getType()) ==0 ){
                a.setOriginalCompDate(dateToSet);
            }
            //Date of Effective Agreement
            if("start-actual".compareTo(date.getType()) ==0 ){
                a.setActualStartDate(dateToSet);
            }
            //Date of Actual Completion
            if("end-actual".compareTo(date.getType()) ==0 ){
                a.setActualCompletionDate(dateToSet);
            }
        }
    }

    private void processIdentificationStep(AmpActivityVersion a, ArrayList<JAXBElement<CodeType>> iatiStatusList,
            ArrayList<JAXBElement<CodeType>> iatiImplementationLevelList, ArrayList<Description> iatiDescriptionList,
            ArrayList<IatiIdentifier> iatiIDList, boolean isCreate, DESourceSetting settings) {
        if ((isCreate && settings.importEnabled("Status")) || (!isCreate && settings.updateEnabled("Status"))) {
            processStatus(a,iatiStatusList);
        }
        processImplementationLevel(a,iatiImplementationLevelList);
        if ((isCreate && settings.importEnabled("Description")) || (!isCreate && settings.updateEnabled("Description"))) {
            processDescriptions(a,iatiDescriptionList);
        }
        processIatiID(a,iatiIDList);
    }

    private void processIatiID(AmpActivityVersion a, ArrayList<IatiIdentifier> iatiIDList) {
        //TODO process the iati-id
        if(iatiIDList!=null )
            {
                a.setProjectCode(iatiIDList.iterator().next().getContent());
                this.iatiID = a.getProjectCode();
            }
    }

    private void processDescriptions(AmpActivityVersion a, ArrayList<Description> iatiDescriptionList) {
        //same editor key should be used for multiple translations
        String descKey = "aim-iati-desc-" + System.currentTimeMillis();
        for (Iterator it = iatiDescriptionList.iterator(); it.hasNext();) {
            Description description = (Description) it.next();

            if(description.getType() != null && ("objectives".compareTo(description.getType().toLowerCase()) ==0 || "2".compareTo(description.getType().toLowerCase()) ==0) ){
                String d = setEditorDescription(description , "aim-iati-obj-");
                //if(this.getLang().compareTo(description.getLang()) == 0)
                    a.setObjective(d);
            }
            else 
            //  if( description.getType()==null || "general".compareTo(description.getType().toLowerCase()) ==0 )
                {
                    setEditorDescription(description , descKey);
                    a.setDescription(descKey);
                }
            
        }
    }
    
    private AmpContentTranslation getAmpContentTranslation(Object obj, Long objId, String field, String locale, String translation) {
        if (objId==null)
            objId = (long) System.identityHashCode(obj);
        AmpContentTranslation trn = ContentTranslationUtil.loadCachedFieldTranslationsInLocale(
                obj.getClass().getName(), objId, field, locale);
        if (trn==null)
            trn = new AmpContentTranslation(obj.getClass().getName(), objId, field, locale, translation);
        else
            trn.setTranslation(translation);
        return trn;
    }

    private String setEditorDescription(Description obj , String key){
            String value = printList(obj.getContent());
            if (isValidString(value)) {
                Editor ed = DataExchangeUtils.createEditor("amp", key, obj.getLang()==null?this.getLang():obj.getLang()); //TODO: bugs source
                ed.setLastModDate(new Date());
                ed.setGroupName(org.digijava.module.editor.util.Constants.GROUP_OTHER);
                ed.setBody(value);
                try {
                    org.digijava.module.editor.util.DbUtil.saveEditor(ed);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return key;
            }
            
        return null;
    }

    private boolean isValidLanguage(String lang){
        //TODO: validate lang based on dg_locale code for languages
        if(isValidString(lang) && lang.length() == 2 )
            return true;
        return false;
        
    }
    
    
    private void processStatus(AmpActivityVersion activity, ArrayList<JAXBElement<CodeType>> iatiStatusList) {
        // TODO Auto-generated method stub
        if(iatiStatusList.isEmpty()) return;
        JAXBElement<CodeType> item = getElementByLang(iatiStatusList,this.getLang());
        if(item!=null)
        {
            String code = getAttributeCodeType(item, "code");
            String value = printCodeType(item);
            DEMappingFields checkMappedField = checkMappedField(DataExchangeConstants.IATI_ACTIVITY_STATUS,toIATIValues("statusName","statusCode"),toIATIValues(value,code),
                    this.getLang(),null,DataExchangeConstants.IATI_ACTIVITY_STATUS,null,null,"active");
            //we are sure that activity id is not null - activity is mapped
            if(checkMappedField==null || checkMappedField.getAmpId() == null) return;
            AmpCategoryValue acv = CategoryManagerUtil.getAmpCategoryValueFromDb(checkMappedField.getAmpId());
            if (activity.getCategories() == null) {
                activity.setCategories( new HashSet() );
            }
            for (Iterator it = activity.getCategories().iterator(); it.hasNext();) {
                AmpCategoryValue acvAux = (AmpCategoryValue) it.next();
                if(org.digijava.module.dataExchange.utils.DEConstants.CATEG_VALUE_ACTIVITY_STATUS.equals(acvAux.getAmpCategoryClass().getKeyName()))
                    it.remove();
            }
            if(acv!=null)
                activity.getCategories().add(acv);
        }
    }
    

    private void processImplementationLevel(AmpActivityVersion activity, ArrayList<JAXBElement<CodeType>> iatiImplementationList) {
        // TODO Auto-generated method stub
        if(iatiImplementationList.isEmpty()) return;
        JAXBElement<CodeType> item = getElementByLang(iatiImplementationList,this.getLang());
        if(item!=null)
        {
            String code = getAttributeCodeType(item, "code");
            String value = printCodeType(item);
            DEMappingFields checkMappedField = checkMappedField(DataExchangeConstants.IMPLEMENTATION_LEVEL_TYPE,toIATIValues("implementationLevelName","implementationLevelCode"),toIATIValues(value,code),
                    this.getLang(),null,DataExchangeConstants.IMPLEMENTATION_LEVEL_TYPE,null,null,"active");
            //we are sure that activity id is not null - activity is mapped
            if(checkMappedField==null || checkMappedField.getAmpId() == null) return;
            AmpCategoryValue acv = CategoryManagerUtil.getAmpCategoryValueFromDb(checkMappedField.getAmpId());
            if (activity.getCategories() == null) {
                activity.setCategories( new HashSet() );
            }
            for (Iterator it = activity.getCategories().iterator(); it.hasNext();) {
                AmpCategoryValue acvAux = (AmpCategoryValue) it.next();
                if(org.digijava.module.dataExchange.utils.DEConstants.CATEG_VALUE_IMPLEMENTATION_LEVEL.equals(acvAux.getAmpCategoryClass().getKeyName()))
                    it.remove();
            }
            if(acv!=null)
                activity.getCategories().add(acv);
        }
    }


    private JAXBElement<CodeType> getElementByLang(ArrayList<JAXBElement<CodeType>> iatiStatusList, String lang) {
        // TODO Auto-generated method stub
        boolean isUnique = false;
        if(iatiStatusList.size() == 1)
            isUnique = true;
        for (Iterator it = iatiStatusList.iterator(); it.hasNext();) {
            JAXBElement<CodeType> je = (JAXBElement<CodeType>) it.next();
            if(isUnique) return je;
            if( je.getValue() !=null && je.getValue().getLang()!=null && lang.compareTo(je.getValue().getLang()) ==0 ) return je;
        }
        return null;
    }

    //populate the lists with IATI values of the activity
    private void extractIATIEntities(ArrayList<JAXBElement<TextType>> iatiTitleList,
            ArrayList<JAXBElement<CodeType>> iatiStatusList,
            ArrayList<JAXBElement<CodeType>> iatiImplementationLevelList,
            ArrayList<ParticipatingOrg> iatiPartOrgList,
            ArrayList<ParticipatingOrg> iatiFundOrgList,ArrayList<ParticipatingOrg> iatiExtendingOrgList,
            ArrayList<ReportingOrg> iatiRepOrgList,
            ArrayList<Sector> iatiSectorList,
            ArrayList<Budget> iatiBudgetList, ArrayList<Transaction> iatiTransactionList, ArrayList<PlannedDisbursement> iatiPlannedDisbList,
            ArrayList<Description> iatiDescriptionList,
            ArrayList<OtherIdentifier> iatiOtherIdList,
            ArrayList<ActivityDate> iatiActDateList,
            ArrayList<ContactInfo> iatiContactList,
            ArrayList<Location> iatiLocationList, ArrayList<JAXBElement<CodeReqType>> iatiDefaultFinanceType, 
            ArrayList<JAXBElement<CodeReqType>> iatiDefaultAidType, ArrayList<IatiIdentifier> iatiID) {
        
        
        
        for (Iterator it = this.getiActivity().getActivityWebsiteOrReportingOrgOrParticipatingOrg().iterator(); it.hasNext();) {
            Object contentItem = (Object) it.next();
            if(contentItem instanceof JAXBElement){
                JAXBElement i = (JAXBElement)contentItem;

                //title
                if(i.getName().equals(new QName("title"))){
                    JAXBElement<TextType> item = (JAXBElement<TextType>)i;
                    //System.out.println("activity title:" + printTextType(item)+"#");
                    iatiTitleList.add(item);
                }
                
                //status
                if(i.getName().equals(new QName("activity-status"))){
                    JAXBElement<CodeType> item = (JAXBElement<CodeType>)i;
                    iatiStatusList.add(item);
                }

                //default-finance-type == type of assistance
                if(i.getName().equals(new QName("default-finance-type"))){
                    JAXBElement<CodeReqType> item = (JAXBElement<CodeReqType>)i;
                    if(iatiDefaultFinanceType == null) iatiDefaultFinanceType=new ArrayList<JAXBElement<CodeReqType>>(); 
                    iatiDefaultFinanceType.add(item);
                }

                //default-aid-type == financing instrument
                if(i.getName().equals(new QName("default-aid-type"))){
                    JAXBElement<CodeReqType> item = (JAXBElement<CodeReqType>)i;
                    if(iatiDefaultAidType == null) iatiDefaultAidType=new ArrayList<JAXBElement<CodeReqType>>(); 
                    iatiDefaultAidType.add(item);
                }
                
                //implementation level
                if(i.getName().equals(new QName("implementation-level"))){
                    JAXBElement<CodeType> item = (JAXBElement<CodeType>)i;
                    iatiImplementationLevelList.add(item);
                }
                
            }
            if(contentItem instanceof OtherIdentifier){
                OtherIdentifier item = (OtherIdentifier)contentItem;
                iatiOtherIdList.add(item);
            }
            
            if(contentItem instanceof IatiIdentifier){
                IatiIdentifier item = (IatiIdentifier)contentItem;
                if(iatiID == null) iatiID=new ArrayList<IatiIdentifier>(); 
                iatiID.add(item);
            }
            
            if(contentItem instanceof ActivityDate){
                ActivityDate item = (ActivityDate)contentItem;
                iatiActDateList.add(item);
            }
            
            if(contentItem instanceof Description){
                Description item = (Description)contentItem;
                if (item.getLang()==null) 
                    item.setLang(this.getiActivity().getLang());
                iatiDescriptionList.add(item);
            }
            
            if(contentItem instanceof ContactInfo){
                ContactInfo item = (ContactInfo)contentItem;
                iatiContactList.add(item);
            }
            
            if(contentItem instanceof Location){
                Location item = (Location)contentItem;
                iatiLocationList.add(item);
            }
            
            if(contentItem instanceof ReportingOrg){
                ReportingOrg item = (ReportingOrg)contentItem;
                iatiRepOrgList.add(item);
            }
            
            if(contentItem instanceof ParticipatingOrg){
                ParticipatingOrg item = (ParticipatingOrg)contentItem;
                iatiPartOrgList.add(item);
                if(item.getRole()!=null && "funding".compareTo(item.getRole().toLowerCase())==0)
                    iatiFundOrgList.add(item);
                if(item.getRole()!=null && "extending".compareTo(item.getRole().toLowerCase())==0)
                    iatiExtendingOrgList.add(item);
            }
            
            if(contentItem instanceof Sector){
                Sector item = (Sector)contentItem;
                iatiSectorList.add(item);
            }
            
            if(contentItem instanceof Transaction){
                Transaction item = (Transaction)contentItem;
                iatiTransactionList.add(item);
            }
            
            if(contentItem instanceof PlannedDisbursement){
                PlannedDisbursement item = (PlannedDisbursement)contentItem;
                iatiPlannedDisbList.add(item);
            }
            
            if(contentItem instanceof Budget){
                Budget item = (Budget)contentItem;
                iatiBudgetList.add(item);
            }
        }
    }


    private AmpMappedField checkStatusCode(JAXBElement<CodeType> item) {
        String code = getAttributeCodeType(item, "code");
        String value = printCodeType(item);
        DEMappingFields checkMappedField = checkMappedField(DataExchangeConstants.IATI_ACTIVITY_STATUS,toIATIValues("statusName","statusCode"),toIATIValues(value,code),
                this.getLang(),null,DataExchangeConstants.IATI_ACTIVITY_STATUS,null,null,"inactive");
        AmpMappedField log = new AmpMappedField(checkMappedField);
        logMappingField(DataExchangeConstants.IATI_ACTIVITY_STATUS,toIATIValues("statusName","statusCode"),toIATIValues(value,code),
                this.getLang(),null,DataExchangeConstants.IATI_ACTIVITY_STATUS,null,null,"inactive", checkMappedField, log);
        return log;
    }


    private AmpMappedField checkOrganization(String content, String lang, String ref) {
        DEMappingFields checkMappedField = checkMappedField(DataExchangeConstants.IATI_ORGANIZATION,toIATIValues("organizationName","organizationCode"),
                toIATIValues(content,ref),lang,null,DataExchangeConstants.IATI_ORGANIZATION,null,null,"inactive");
        AmpMappedField log = new AmpMappedField(checkMappedField);
        logMappingField(DataExchangeConstants.IATI_ORGANIZATION,toIATIValues("organizationName","organizationCode"),toIATIValues(content,ref),
                lang,null,DataExchangeConstants.IATI_ORGANIZATION,null,null,"inactive", checkMappedField, log);
        return log;
    }
    
    public AmpMappedField checkActivity(String title, String iatiID, String lang) {
        DEMappingFields checkMappedField = checkMappedField(DataExchangeConstants.IATI_ACTIVITY,toIATIValues("activityName","iatiID"),toIATIValues(title,iatiID),
                lang,null,DataExchangeConstants.IATI_ACTIVITY,null,null,"inactive");
        AmpMappedField log = new AmpMappedField(checkMappedField);
        logMappingField(DataExchangeConstants.IATI_ACTIVITY,toIATIValues("activityName","iatiID"),toIATIValues(title,iatiID),
                lang,null,DataExchangeConstants.IATI_ACTIVITY,null,null,"inactive",checkMappedField, log);
        return log;
    }
    
    private AmpOrganisation getAmpOrganization(String orgName, String lang, String orgCode){
        DEMappingFields checkMappedField = checkMappedField(DataExchangeConstants.IATI_ORGANIZATION,toIATIValues("organizationName","organizationCode"),
                toIATIValues(orgName,orgCode),lang,null,DataExchangeConstants.IATI_ORGANIZATION,null,null,"inactive");
        
        AmpOrganisation org = null;
        if(checkMappedField!=null && checkMappedField.getAmpId()!=null) 
            org = (AmpOrganisation) DataExchangeUtils.getOrganizationById(checkMappedField.getAmpId());
        return org;
    }
    
    private AmpLocation getAmpLocation(String iatiItems, String iatiValues){
        DEMappingFields checkMappedField = checkMappedField(DataExchangeConstants.IATI_LOCATION,iatiItems,iatiValues,lang,null,DataExchangeConstants.IATI_LOCATION,null,null,"active");
        if(checkMappedField==null || checkMappedField.getAmpId()==null) return null; 
        AmpCategoryValueLocations ampCVLoc = DynLocationManagerUtil.getLocationByIdRequestSession(checkMappedField.getAmpId());
        AmpLocation ampLoc = null;
        try {
            ampLoc = DynLocationManagerUtil.getAmpLocation(ampCVLoc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ampLoc;
    }
    
    private AmpMappedField checkLocation(Location l) {
        
//      String locationType = null;
//      String locationName = null;
//      String locationCountry = null;
//      String adm1 = null;
//      String adm2 = null;
//      String adm3 = null;

        TreeMap<String, String> locationDetails = new TreeMap<String,String>();
        
        getLocationDetails(l,locationDetails);
        
        DEMappingFields checkMappedField = checkMappedField(DataExchangeConstants.IATI_LOCATION,toIATIValues("locationName","locationType","locationCountry","adm1","adm2","adm3"),
                toIATIValues(locationDetails.get("name"),
                        locationDetails.get("location-type"),locationDetails.get("country"),locationDetails.get("adm1"),locationDetails.get("adm2"),locationDetails.get("adm3")),
                        lang,null,DataExchangeConstants.IATI_LOCATION,null,null,"inactive");
        AmpMappedField log = new AmpMappedField(checkMappedField);
        logMappingField(DataExchangeConstants.IATI_LOCATION,toIATIValues("locationName","locationType","locationCountry","adm1","adm2","adm3"),
                toIATIValues(locationDetails.get("name"),locationDetails.get("location-type"),locationDetails.get("country"),locationDetails.get("adm1"),locationDetails.get("adm2"),locationDetails.get("adm3")),
                lang,null,DataExchangeConstants.IATI_LOCATION,null,null,"inactive", checkMappedField, log);
        return log;
    }
    

    private void getLocationDetails(Location l,TreeMap<String, String> locationDetails) {

        for (Iterator<Object> it = l.getLocationTypeOrNameOrDescription().iterator(); it.hasNext();) {
            Object contentItem = (Object) it.next();
            if(contentItem instanceof JAXBElement){
                JAXBElement i = (JAXBElement)contentItem;

                //location-type
                if(i.getName().equals(new QName("location-type"))){
                    JAXBElement<CodeReqType> item = (JAXBElement<CodeReqType>)i;
                    locationDetails.put("location-type",item.getValue().getCode());
                }
                //location name
                if(i.getName().equals(new QName("name"))){
                    JAXBElement<TextType> item = (JAXBElement<TextType>)i;
                    locationDetails.put("name", printList(item.getValue().getContent()));
                }
                
                //administrative
                if(i.getName().equals(new QName("administrative"))){
                    Location.Administrative item = (Location.Administrative)i.getValue();
                    locationDetails.put("country", item.getCountry());
                    locationDetails.put("adm1", item.getAdm1());
                    locationDetails.put("adm2", item.getAdm2());
                    locationDetails.put("adm3", item.getOtherAttributes().get(new QName("adm3")));
                }
                
            }
        }
        
    }

    private AmpMappedField checkOrganizationType(String type) {
        //if(!isValidString(type)) return null;
        DEMappingFields checkMappedField = checkMappedField(DataExchangeConstants.IATI_ORGANIZATION_TYPE,"organizationCodeType",type,
                this.getLang(),null,DataExchangeConstants.IATI_ORGANIZATION_TYPE,null,null,"inactive");
        AmpMappedField log = new AmpMappedField(checkMappedField);
        logMappingField(DataExchangeConstants.IATI_ORGANIZATION_TYPE,"organizationCodeType",type,
                this.getLang(),null,DataExchangeConstants.IATI_ORGANIZATION_TYPE,null,null,"inactive",checkMappedField,log);
        return log;
    }
    
    private AmpMappedField checkVocabularyCode(Sector item) {
        DEMappingFields mf = null;
        String name = item.getVocabulary()==null?"DAC":item.getVocabulary();
        mf = checkMappedField(DataExchangeConstants.IATI_VOCABULARY_CODE,"sectorVocabularyCode",name,this.getLang(),null,DataExchangeConstants.AMP_VOCABULARY_CODE,null,null,"inactive");
        AmpMappedField log = new AmpMappedField(mf);
        logMappingField(DataExchangeConstants.IATI_VOCABULARY_CODE,"sectorVocabularyCode",name,this.getLang(),null,DataExchangeConstants.AMP_VOCABULARY_CODE,null,null,"inactive",mf,log);
        return log;
    }
    
    private AmpMappedField checkSector(Sector sector) {
        String sectorName = printList(sector.getContent());
        String vocabulary = sector.getVocabulary()==null?"DAC":sector.getVocabulary();
        DEMappingFields checkMappedField = checkMappedField(DataExchangeConstants.IATI_SECTOR,toIATIValues("vocabularyName","sectorName","sectorCode"),
                toIATIValues(vocabulary,sectorName,sector.getCode()),this.getLang(),null,DataExchangeConstants.IATI_SECTOR,null,null,"inactive");
        AmpMappedField log = new AmpMappedField(checkMappedField);
        logMappingField(DataExchangeConstants.IATI_SECTOR,toIATIValues("vocabularyName","sectorName","sectorCode"),
                toIATIValues(vocabulary,sectorName,sector.getCode()),this.getLang(),null,DataExchangeConstants.IATI_SECTOR,null,null,"inactive",checkMappedField,log);
        return log;
    }
    
    //mapped to Type of Assistance
    private AmpMappedField checkFinanceType(JAXBElement<CodeReqType> item) {
        return checkCodeReqType(item, DataExchangeConstants.IATI_FINANCE_TYPE,toIATIValues("financeTypeValue","financeTypeCode"),this.getLang(),null,
                CategoryConstants.TYPE_OF_ASSISTENCE_NAME,null,null,"inactive");
    }
    
    
    
    //Financing Instrument
    private AmpMappedField checkAidType(JAXBElement<CodeReqType> item) {
        return checkCodeReqType(item, DataExchangeConstants.IATI_AID_TYPE,toIATIValues("aidTypeValue","aidTypeCode"),this.getLang(),null,
                CategoryConstants.FINANCING_INSTRUMENT_NAME,null,null,"inactive");
    }
    
    
    //Implementation Level
            /*private AmpMappedField checkLevelType(JAXBElement<CodeReqType> item) {
                return checkCodeReqType(item, DataExchangeConstants.IMPLEMENTATION_LEVEL_TYPE,toIATIValues("ImplementationLevelValue","ImplementationLevelCode"),this.getLang(),null,
                        CategoryConstants.IMPLEMENTATION_LEVEL_NAME,null,null,"inactive");
            }*/
            private AmpMappedField checkLevelType(JAXBElement<CodeType> item) {
                // TODO Auto-generated method stub
                String code = getAttributeCodeType(item, "code");
                String value = printCodeType(item);
                DEMappingFields checkMappedField = checkMappedField(DataExchangeConstants.IMPLEMENTATION_LEVEL_TYPE,toIATIValues("implementationLevelName","implementationLevelCode"),toIATIValues(value,code),
                        this.getLang(),null,DataExchangeConstants.IMPLEMENTATION_LEVEL_TYPE,null,null,"inactive");
                AmpMappedField log = new AmpMappedField(checkMappedField);
                logMappingField(DataExchangeConstants.IMPLEMENTATION_LEVEL_TYPE,toIATIValues("implementationLevelName","implementationLevelCode"),toIATIValues(value,code),
                        this.getLang(),null,DataExchangeConstants.IMPLEMENTATION_LEVEL_TYPE,null,null,"inactive", checkMappedField, log);
                return log;
            }
    //Mode of Payment
    private AmpMappedField checkDisbursementChannel(JAXBElement<CodeReqType> item) {
        return checkCodeReqType(item, DataExchangeConstants.IATI_DISBURSEMENT_CHANNEL,toIATIValues("disbursementChannelValue","disbursementChannelCode"),this.getLang(),null,
                CategoryConstants.MODE_OF_PAYMENT_NAME,null,null,"inactive");
    }
    
    private boolean checkIATITransaction(Transaction t, ArrayList<AmpMappedField> logs) {

        for (Object contentItem : t.getValueOrDescriptionOrTransactionType()) {
            if (contentItem instanceof JAXBElement) {
                JAXBElement i = (JAXBElement) contentItem;

                //flow-type, tied-status
                //TODO

                //receiver-org - usually in AMP gov is the receiver
                if (i.getName().equals(new QName("receiver-org"))) {
                    Transaction.ReceiverOrg item = (Transaction.ReceiverOrg) (i.getValue());
                    AmpMappedField existReceiverOrg = checkOrganization(printList(item.getContent()), this.getLang(), item.getRef());
                    //TODO logging
                    if (existReceiverOrg != null) logs.add(existReceiverOrg);
                }

                //provider-org - usually in AMP gov is the receiver
                if (i.getName().equals(new QName("provider-org"))) {
                    Transaction.ProviderOrg item = (Transaction.ProviderOrg) (i.getValue());
                    AmpMappedField existReceiverOrg = checkOrganization(printList(item.getContent()), this.getLang(), item.getRef());
                    //TODO logging
                    if (existReceiverOrg != null) logs.add(existReceiverOrg);
                }

                //disbursement-channel == mode of payment
                if (i.getName().equals(new QName("disbursement-channel"))) {
                    JAXBElement<CodeReqType> item = (JAXBElement<CodeReqType>) i;
                    AmpMappedField existDisbursementChannel = checkDisbursementChannel(item);
                    //TODO logging
                    if (existDisbursementChannel != null) logs.add(existDisbursementChannel);
                }

                //finance-type == type of assistance
                if (i.getName().equals(new QName("finance-type"))) {
                    JAXBElement<CodeReqType> item = (JAXBElement<CodeReqType>) i;
                    AmpMappedField existFinanceType = checkFinanceType(item);
                    //TODO logging
                    if (existFinanceType != null) logs.add(existFinanceType);
                }

                //aid-type == financing instrument
                if (i.getName().equals(new QName("aid-type"))) {
                    JAXBElement<CodeReqType> item = (JAXBElement<CodeReqType>) i;
                    AmpMappedField existAidType = checkAidType(item);
                    if (existAidType != null) logs.add(existAidType);
                }

            }
        }
        
        
        return false;
    }

    private boolean checkIATIPlannedDisbursement(PlannedDisbursement t, ArrayList<AmpMappedField> logs) {
        // TODO Auto-generated method stub
        
        for (Iterator it = t.getPeriodStartOrPeriodEndOrValue().iterator(); it.hasNext();) {
            Object contentItem = (Object) it.next();
            if(contentItem instanceof JAXBElement){
                JAXBElement i = (JAXBElement)contentItem;

                //flow-type, tied-status
                //TODO
                /*if(i.getName().equals(new QName("period-start"))){
                    i.getValue()
                }*/
                //receiver-org - usually in AMP gov is the receiver
                if(i.getName().equals(new QName("receiver-org"))){
                    Transaction.ReceiverOrg item = (Transaction.ReceiverOrg)(i.getValue());
                    AmpMappedField existReceiverOrg = checkOrganization(printList(item.getContent()), this.getLang(),item.getRef());
                    //TODO logging
                    if(existReceiverOrg!=null) logs.add(existReceiverOrg);
                }

                //provider-org - usually in AMP gov is the receiver
                if(i.getName().equals(new QName("provider-org"))){
                    Transaction.ProviderOrg item = (Transaction.ProviderOrg)(i.getValue());
                    AmpMappedField existReceiverOrg = checkOrganization(printList(item.getContent()), this.getLang(),item.getRef());
                    //TODO logging
                    if(existReceiverOrg!=null) logs.add(existReceiverOrg);
                }
                
                //disbursement-channel == mode of payment
                if(i.getName().equals(new QName("disbursement-channel"))){
                    JAXBElement<CodeReqType> item = (JAXBElement<CodeReqType>)i;
                    AmpMappedField existDisbursementChannel = checkDisbursementChannel(item);
                    //TODO logging
                    if(existDisbursementChannel!=null) logs.add(existDisbursementChannel);
                }
                
                //finance-type == type of assistance
                if(i.getName().equals(new QName("finance-type"))){
                    JAXBElement<CodeReqType> item = (JAXBElement<CodeReqType>)i;
                    AmpMappedField existFinanceType = checkFinanceType(item);
                    //TODO logging
                    if(existFinanceType!=null) logs.add(existFinanceType);
                }
                
                //aid-type == financing instrument
                if(i.getName().equals(new QName("aid-type"))){
                    JAXBElement<CodeReqType> item = (JAXBElement<CodeReqType>)i;
                    AmpMappedField existAidType = checkAidType(item);
                    if(existAidType!=null) logs.add(existAidType);
                }
                
            }
        }
        
        
        return false;
    }
    
    private AmpMappedField checkCodeReqType(JAXBElement<CodeReqType> item, String iatiPath, String iatiItems,
            String iatiLang, Long ampId, String ampClass,
            Long sourceId, String feedFileName, String status) {
        String code = item.getValue().getCode();
        String value = printCodeReqType(item);
        DEMappingFields checkMappedField = checkMappedField(iatiPath,iatiItems,toIATIValues(value,code),iatiLang,ampId,ampClass,sourceId,feedFileName,status);
        AmpMappedField log = new AmpMappedField(checkMappedField);
        logMappingField(iatiPath,iatiItems,toIATIValues(value,code),iatiLang,ampId,ampClass,sourceId,feedFileName,status, checkMappedField, log);
        return log;
    }
    private AmpCategoryValue getAmpCategoryValue(JAXBElement<CodeReqType> item, String iatiPath, String iatiItems,
            String iatiLang, Long ampId, String ampClass,
            Long sourceId, String feedFileName, String status){
        if(item == null) return null;
        String code = item.getValue().getCode();
        String value = printCodeReqType(item);
        DEMappingFields checkMappedField = checkMappedField(iatiPath,iatiItems,toIATIValues(value,code),iatiLang,ampId,ampClass,sourceId,feedFileName,status);
        AmpCategoryValue acv = null;
        if(checkMappedField!=null && checkMappedField.getAmpId()!=null) 
            acv = (AmpCategoryValue) CategoryManagerUtil.getAmpCategoryValueFromDb(checkMappedField.getAmpId());
        return acv;
    }

    private void logMappingField(String iatiPath, String iatiItems,
            String iatiValues, String iatiLang, Long ampId, String ampClass,
            Long sourceId, String feedFileName, String status,
            DEMappingFields checkMappedField, AmpMappedField log) {
        if (checkMappedField != null && checkMappedField.getSameAsMaping() != null) {
            checkMappedField = checkMappedField.getSameAsMaping();
        }
        if(checkMappedField==null )
            {
            //field has to be mapped
                if(isIatiValueok(iatiValues))
                {
                    checkMappedField = addMappingField(iatiPath,iatiItems,iatiValues,iatiLang,ampId,ampClass,sourceId,feedFileName,status);
                    //System.out.println("Activity:"+this.getTitle()+"# ADDED Logging path:"+iatiPath+"# items: "+iatiItems+"# values: "+iatiValues);
                    log.setItem(checkMappedField);
                }
                else{
                    checkMappedField = new DEMappingFields(iatiPath, iatiItems, iatiValues, iatiLang, ampId, ampClass.toString(), sourceId, feedFileName, status);
                    //System.out.println("Activity:"+this.getTitle()+"# ERROR to add path:"+iatiPath+"# items: "+iatiItems+"# values: "+iatiValues);
                }
                log.add(iatiItems);
                log.add(iatiValues);
            }
        else{
            //entity is not mapped yet or it has be marked to be added as new, but was not added yet
            if(checkMappedField.getAmpId()==null || (DEConstants.AMP_ID_CREATE_NEW.equals(checkMappedField.getAmpId()) && DataExchangeConstants.IATI_ACTIVITY.compareTo(iatiPath)!=0))
            {
                //System.out.println("Activity:"+this.getTitle()+"# Logging path:"+iatiPath+"# items: "+iatiItems+"# values: "+iatiValues);
                log.add(iatiItems);
                log.add(iatiValues);
            }
        }
    }
    
    private String getAttributeCodeType(JAXBElement<CodeType> item, String key) {
        String code = item.getValue().getCode();
        return code;
    }
    
    private String getAttributeCodeReqType(JAXBElement<CodeReqType> item, String key) {
        Map<QName, String> otherAttributes = item.getValue().getOtherAttributes();
        String code = otherAttributes.get(new QName(key));
        return code;
    }
    
    public String printTextType(JAXBElement<TextType> item){
        String result = "";
        result += printList(item.getValue().getContent());
        return result;
    }
    
    public String printCodeType(JAXBElement<CodeType> item){
        String result = "";
        //result +=item.getValue().getCode()+" ";
        result += printList(item.getValue().getContent());
        return result;
    }
    
    public String printCodeReqType(JAXBElement<CodeReqType> item){
        String result = "";
        //result +=item.getValue().getCode()+" ";
        result += printList(item.getValue().getContent());
        return result;
    }
    
    public String printList(List<Object> items){
        String result = "";
        for (Iterator it = items.iterator(); it.hasNext();) {
            Object o = (Object) it.next();
            if(o instanceof String){
                result +=(String)o;
            }
        }
        result = result.replace("\n","");
        result = result.replaceAll("\\s+", " ");
        return result;
    }
    
    public String toIATIValues(String a, String b){
        return a+"|||"+b;
    }

    public String toIATIValues(String a, String b, String c){
        return a+"|||"+b+"|||"+c;
    }

    public String toIATIValues(String a, String b, String c, String d, String e){
        return toIATIValues(a,b)+"|||"+toIATIValues(c,d,e);
    }
    
    public String toIATIValues(String a, String b, String c, String d, String e, String f){
        return toIATIValues(a,b,c)+"|||"+toIATIValues(d,e,f);
    }

    
    private DEMappingFields checkMappedField(String iatiPath, String iatiItems,
            String iatiValues, String iatiLang, Long ampId, String ampClass,
            Long sourceId, String feedFileName, String status) {
        if(!isIatiValueok(iatiValues)) 
            return null; 
        Collection<DEMappingFields> allAmpDEMappingFields = DataExchangeUtils.getAllAmpDEMappingFields();
        DEMappingFields mf = new DEMappingFields(iatiPath.trim(), iatiItems.trim(), iatiValues.trim(), iatiLang==null?this.getLang():iatiLang, ampId, ampClass.toString(), sourceId, feedFileName, status);
        for (Iterator ot = allAmpDEMappingFields.iterator(); ot.hasNext();) {
            DEMappingFields deMappingFields = (DEMappingFields) ot.next();

            /*
            if((ignoreSameAsCheck || deMappingFields.getSameAsMaping() == null) && mf.compare(deMappingFields)) {
                return deMappingFields;
            } else if(!ignoreSameAsCheck && deMappingFields.getSameAsMaping() != null && mf.compare(deMappingFields.getSameAsMaping())) {
                return deMappingFields.getSameAsMaping();
            } */

            if(mf.compare(deMappingFields)) {
                return (!ignoreSameAsCheck && deMappingFields.getSameAsMaping()!=null) ?
                        deMappingFields.getSameAsMaping():deMappingFields;
            }
        }
        return null;
    }

    private boolean isIatiValueok(String iatiValues) {
        // TODO Auto-generated method stub
        if(isValidStringSeparator(iatiValues,"|||")) 
            return true;
        return false;
    }

    private DEMappingFields addMappingField(String iatiPath, String iatiItems,
            String iatiValues, String iatiLang, Long ampId, String ampClass,
            Long sourceId, String feedFileName, String status) {
        DEMappingFields mf = new DEMappingFields(iatiPath, iatiItems, iatiValues, iatiLang, ampId, ampClass.toString(), sourceId, feedFileName, status);
        mf.setCreationDate(new Timestamp(System.currentTimeMillis()));
        if (saveObjects) {
            DataExchangeUtils.insertDEMappingField(mf);
        } else {
            accumulate.add(mf);
        }
        return mf;
    }
    
    
    private AmpCategoryValue getAmpCategoryValue(String value, String code, String categoryKey ){
        AmpCategoryValue acv=null;
        Collection<AmpCategoryValue> allCategValues;
        String valueToCateg="";
        
        allCategValues = (Collection<AmpCategoryValue>) CategoryManagerUtil.getAmpCategoryValueCollectionByKey(categoryKey);
        
        if( isValidString(value) )  valueToCateg=value;
        else if( isValidString(code) ) valueToCateg=code;

        if(valueToCateg == null || "".equals(valueToCateg.trim()) ) return null;
        
        for (Iterator itacv = allCategValues.iterator(); itacv.hasNext();) {
            acv = (AmpCategoryValue) itacv.next();
            if(acv.getValue().equals(valueToCateg)) return acv;
        }
        return null;
    }
    
    private AmpClassificationConfiguration getConfiguration(AmpSector ampSector, ArrayList<AmpClassificationConfiguration> allClassifConfigs) {
        // TODO Auto-generated method stub
        //allClassifConfigs = (ArrayList<AmpClassificationConfiguration>) getAllClassificationConfiguration();
        for (Iterator<AmpClassificationConfiguration> it2 = allClassifConfigs.iterator(); it2.hasNext();) {
            AmpClassificationConfiguration acc = (AmpClassificationConfiguration) it2.next();
            if(acc.getClassification().getAmpSecSchemeId().equals(ampSector.getAmpSecSchemeId().getAmpSecSchemeId()))
                    return acc;
        }
        return null;
    }

    private AmpClassificationConfiguration getPrimaryClassificationConfiguration(ArrayList<AmpClassificationConfiguration> allClassifConfigs){
        if(allClassifConfigs!=null){
            Iterator<AmpClassificationConfiguration> it = allClassifConfigs.iterator();         
            while(it.hasNext()){
                AmpClassificationConfiguration conf = it.next();                
                if(conf.isPrimary()) return conf;
            }
        }
        return null;
    }   

    //get all Amp Classification Configuration
    private List<AmpClassificationConfiguration> getAllClassificationConfiguration(){
        return SectorUtil.getAllClassificationConfigs();
    }
    
    
    
    //****************************************** Other methods ******************************
    

    private boolean isValidString(String s ){
        if(s != null && "".compareTo(s.trim())!=0 )
            return true;
        return false;
    }
    
    private boolean isValidStringSeparator(String s, String sep ){
        if(isValidString(s)){
            if("|||".compareTo(s.trim())==0 || "|||null".compareTo(s.trim())==0 || "null|||".compareTo(s.trim())==0 || "null|||null".compareTo(s.trim())==0)
                return false;
            else return true;
        }
        return false;
    }

    /**
     * @return true if request is part of a session load
     */
    public boolean isLoad() {
        return isLoad;
    }

    /**
     * @param isLoad flag to notify that the current request is on session load
     */
    public void setIsLoad(boolean isLoad) {
        this.isLoad = isLoad;
    }

}
