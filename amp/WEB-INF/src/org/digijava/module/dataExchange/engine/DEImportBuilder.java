/**
 * 
 */
package org.digijava.module.dataExchange.engine;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessages;

import org.dgfoundation.amp.ar.AmpARFilter;

import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;

import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityContact;
import org.digijava.module.aim.dbentity.AmpActivityDocument;
import org.digijava.module.aim.dbentity.AmpActivityGroup;
import org.digijava.module.aim.dbentity.AmpActivityInternalId;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpActivityProgram;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpActor;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpComponentFunding;
import org.digijava.module.aim.dbentity.AmpComponentType;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpContactProperty;
import org.digijava.module.aim.dbentity.AmpContentTranslation;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpFundingMTEFProjection;
import org.digijava.module.aim.dbentity.AmpIssues;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpMeasure;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpRegionalFunding;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTheme;

import org.digijava.module.aim.helper.ActivityDocumentsConstants;
import org.digijava.module.aim.helper.Components;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityVersionUtil;
import org.digijava.module.aim.util.AuditLoggerUtil;
import org.digijava.module.aim.util.ComponentsUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.LuceneUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.SectorUtil;

import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.helper.TemporaryDocumentData;

import org.digijava.module.dataExchange.dbentity.AmpMappedField;
import org.digijava.module.dataExchange.dbentity.DELogPerExecution;
import org.digijava.module.dataExchange.dbentity.DELogPerItem;
import org.digijava.module.dataExchange.dbentity.DEMappingFields;
import org.digijava.module.dataExchange.dbentity.DESourceSetting;

import org.digijava.module.dataExchange.iati.IatiVersion;
import org.digijava.module.dataExchange.jaxb.Activities;
import org.digijava.module.dataExchange.jaxb.ActivityType;
import org.digijava.module.dataExchange.jaxb.ActivityType.Component;
import org.digijava.module.dataExchange.jaxb.ActivityType.Documents;
import org.digijava.module.dataExchange.jaxb.ActivityType.Id;
import org.digijava.module.dataExchange.jaxb.ActivityType.Issues;
import org.digijava.module.dataExchange.jaxb.ActivityType.Issues.Measure;
import org.digijava.module.dataExchange.jaxb.ActivityType.Location;
import org.digijava.module.dataExchange.jaxb.ActivityType.RelatedLinks;
import org.digijava.module.dataExchange.jaxb.ActivityType.RelatedOrgs;
import org.digijava.module.dataExchange.jaxb.AdditionalFieldType;
import org.digijava.module.dataExchange.jaxb.CodeValueType;
import org.digijava.module.dataExchange.jaxb.ComponentFundingType;
import org.digijava.module.dataExchange.jaxb.ContactType;
import org.digijava.module.dataExchange.jaxb.FreeTextType;
import org.digijava.module.dataExchange.jaxb.FundingDetailType;
import org.digijava.module.dataExchange.jaxb.FundingType;
import org.digijava.module.dataExchange.jaxb.FundingType.Projections;
import org.digijava.module.dataExchange.jaxb.LocationFundingType;
import org.digijava.module.dataExchange.jaxb.PercentageCodeValueType;

import org.digijava.module.dataExchange.pojo.DEActivityLog;
import org.digijava.module.dataExchange.pojo.DECurrencyMissingLog;
import org.digijava.module.dataExchange.pojo.DEFinancInstrMissingLog;
import org.digijava.module.dataExchange.pojo.DEImplLevelMissingLog;
import org.digijava.module.dataExchange.pojo.DEImportItem;
import org.digijava.module.dataExchange.pojo.DEImportValidationEventHandler;
import org.digijava.module.dataExchange.pojo.DEMTEFMissingLog;
import org.digijava.module.dataExchange.pojo.DEOrgMissingLog;
import org.digijava.module.dataExchange.pojo.DEProgramMissingLog;
import org.digijava.module.dataExchange.pojo.DEProgramPercentageLog;
import org.digijava.module.dataExchange.pojo.DESectorMissingLog;
import org.digijava.module.dataExchange.pojo.DEStatusMissingLog;
import org.digijava.module.dataExchange.pojo.DETypeAssistMissingLog;

import org.digijava.module.dataExchange.util.SessionSourceSettingDAO;
import org.digijava.module.dataExchange.util.SourceSettingDAO;
import org.digijava.module.dataExchange.utils.DEConstants;
import org.digijava.module.dataExchange.utils.DataExchangeUtils;

import org.digijava.module.dataExchangeIATI.iatiSchema.v1_03.jaxb.IatiActivities;
import org.digijava.module.dataExchangeIATI.iatiSchema.v1_03.jaxb.IatiActivity;

import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.editor.exception.EditorException;
import org.digijava.module.editor.util.Constants;
import org.digijava.module.message.triggers.ActivitySaveTrigger;
import org.digijava.module.message.triggers.NotApprovedActivityTrigger;
import org.hibernate.HibernateException;
import org.hibernate.type.NullableType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.xml.sax.SAXException;

/**
 * @author dan
 * 
 * DEImportBuilder.java class is building the import: check, xml validation, content validation, save
 *
 */
public class DEImportBuilder {
    
    private static Logger logger = Logger.getLogger(DEImportBuilder.class);
    private DEImportItem ampImportItem;
    private ArrayList<String> changedIDs;
    private boolean ignoreSameAsCheck = false;
    private String defaultLanguage = null;

    // The selected country should be only one.
    // Because the situation when we import activities that belong to country A into country B is not possible as of today
    private String selectedCountry;

    public String getSelectedCountry() {
        return selectedCountry;
    }

    public void setSelectedCountry(String selectedCountry) {
        this.selectedCountry = selectedCountry;
    }

    public boolean isIgnoreSameAsCheck() {
        return ignoreSameAsCheck;
    }

    public void setIgnoreSameAsCheck(boolean ignoreSameAsCheck) {
        this.ignoreSameAsCheck = ignoreSameAsCheck;
    }

    public HashMap<String, Boolean> getHashFields() {
        return this.getAmpImportItem().getHashFields();
    }

    public void setHashFields(HashMap<String, Boolean> hashFields) {
        this.getAmpImportItem().setHashFields(hashFields);
    }

    public DEImportBuilder() {
        // TODO Auto-generated constructor stub
    }
    
    public DEImportBuilder(DEImportItem ampImportItem) {
        super();
        this.ampImportItem = ampImportItem;
    }

    //fill a new and empty activity based on field selection
    private void insertActivity(AmpActivity activity, ActivityType actType, Boolean update, HttpServletRequest request ) throws Exception{
        //update = false for new activity
        processPreStep(activity, update);
        processStep1(activity, actType, update);
        processStep2(activity, actType, update);
        processStep3(activity, actType, update);
        processStep4(activity, actType, update);
        processStep5(activity, actType, update);
        processStep6(activity, actType, update, request);
        processStep7(activity, actType, update);
        processAdditionalFields(activity, actType, update);
        //DataExchangeUtils.saveComponents(activity, request, tempComps);
        //TODO logger
        saveActivity(activity, update,request);
        
    }
    
    private AmpActivity getAmpActivity(ActivityType actType){
        AmpActivity activity = null;
        activity = getAmpActivityByComposedKey(actType,getDESourceSetting().getUniqueIdentifier(), getDESourceSetting().getUniqueIdentifierSeparator());
        return activity;
    }
    
    private void updateActivity(AmpActivity activity, ActivityType actType, Boolean update, HttpServletRequest request )  throws Exception{
        //update = true for update activity
        processPreStep(activity, update);
        processStep1(activity, actType, update);
        processStep2(activity, actType, update);
        processStep3(activity, actType, update);
        processStep4(activity, actType, update);
        processStep5(activity, actType, update);
        processStep6(activity, actType, update, request);
        processStep7(activity, actType, update);
        processAdditionalFields(activity, actType, update);
        //DataExchangeUtils.saveComponents(activity, request, tempComps);
        //TODO logger
        saveActivity(activity, update,request);
        
    }
    
    private void validateActivityContent(ActivityType actType, DEActivityLog logger){
        validateStep1(actType, logger);
        
        //sectors, programs
        validateStep2(actType, logger);
        
        //funding, locations
        validateStep3(actType, logger);
        
        //related orgs
        validateStep4(actType, logger);
        
    }


    
    private void saveActivity(AmpActivity activity, Boolean update,HttpServletRequest request) throws Exception{
        // TODO Auto-generated method stub
        String action=null;
        String additionalDetails=null;
        List<String> details=null;
        if(!update) {
            DataExchangeUtils.saveActivityNoLogger(activity);
            new ActivitySaveTrigger(activity);
            action="added";
        }
        else {
            DataExchangeUtils.updateActivityNoLogger(activity);
            details=new ArrayList<String>();
            action="update";
        }
        if(!("allOff".equals(AmpARFilter.getEffectiveSettings().getValidation()))&&!activity.getApprovalStatus().equals(org.digijava.module.aim.helper.Constants.APPROVED_STATUS)){
            new NotApprovedActivityTrigger(activity);
            additionalDetails="imported & pending approval";
        }
        else{
            additionalDetails="imported & approved,";
        }
        if(details!=null){
            details.add(additionalDetails);
            AuditLoggerUtil.logActivityUpdate(request, activity,  details);
        }
        else{
            AuditLoggerUtil.logObject(request, activity, action, additionalDetails);
        }
        
    }

    private void processPreStep(AmpActivity activity, Boolean update) {

        //set the amp team
        //if no workspace was selected the activity will be unassigned
        
        //original request: if activity exist we keep the existing settings
//      if(update == false){
//          AmpTeam team = getImportedWorkspace();
//          activity.setTeam(team);
//          
//          //all the activities are not saved as draft
//          activity.setCreatedAsDraft(false);
//      }

        //AMP-9213
        AmpTeam team = getAssignedWorkspace();
            activity.setTeam(team);
            
        //activity creator is the team leader of the workspace
        activity.setActivityCreator(team.getTeamLead());
        
        activity.setApprovalStatus(getApprovalStatus());
    }

    
    //process Step 1 from activity form
    // title, description, objective, activity datas, status and statusReason
    private void processStep1(AmpActivity activity, ActivityType actType, Boolean update){
        
        //title is mandatory
        processTitle(activity, actType);
        
        if(isFieldSelected("activity.description"))
            processDescription(activity, actType);
        if(isFieldSelected("activity.objective"))
            processObjective(activity, actType);
        if(isFieldSelected("activity.id"))
            processActivityAssigningOrg(activity,actType);

        //activity dates 
        if(isFieldSelected("activity.proposedApprovalDate"))
            processProposedApprovalDate(activity, actType);
        if(isFieldSelected("activity.actualApprovalDate"))
            processActualApprovalDate(activity, actType);

        if(isFieldSelected("activity.proposedStartDate"))
            processProposedStartDate(activity, actType);

        if(isFieldSelected("activity.actualStartDate"))
            processActualStartDate(activity, actType);

        if(isFieldSelected("activity.modifiedClosingDate"))
            processModifiedClosingDate(activity, actType);

        if(isFieldSelected("activity.closingDate"))
            processClosingDate(activity, actType);
        
        if(isFieldSelected("activity.status"))
            processStatus(activity, actType, update);

        if(isFieldSelected("activity.statusReason"))
            processStatusReason(activity, actType);

    }

    //process step 2 from activity form
    //sectors, programs
    private void processStep2(AmpActivity activity, ActivityType actType, Boolean update){
        if(isFieldSelected("activity.sectors"))
            processSectors(activity,actType);
        if(isFieldSelected("activity.programs"))
            processPrograms(activity,actType);

    }
    
    //process step 3 from activity form
    //fundings, regional fundings
    private void processStep3(AmpActivity activity, ActivityType actType, Boolean update){
        if(isFieldSelected("activity.funding"))
            processFunding(activity,actType, update);
        if(isFieldSelected("activity.location"))
            processLocation(activity,actType);

    }

    
    //process step 4 
    //related organizations
    private void processStep4(AmpActivity activity, ActivityType actType, Boolean update) {
        if(isFieldSelected("activity.relatedOrgs"))
            processRelatedOrgs(activity, actType);
    }


    //process step 5 from activity form
    //components
    private void processStep5(AmpActivity activity, ActivityType actType, Boolean update) {
        // TODO Auto-generated method stub
        if(isFieldSelected("activity.component"))
            processComponent(activity, actType);
    }

    //process step 6 from activity form
    // documents and related links
    private void processStep6(AmpActivity activity, ActivityType actType, Boolean update, HttpServletRequest request) {
        // TODO Auto-generated method stub
        if(isFieldSelected("activity.documents"))
            processDocuments(activity, actType, request);

        if(isFieldSelected("activity.relatedLinks"))
            processRelatedLinks(activity, actType, request);
    }

    //process step 7 from activity form
    // contact information, issues
    private void processStep7(AmpActivity activity, ActivityType actType, Boolean update) {

        if(isFieldSelected("activity.donorContacts"))
            processDonorContacts(activity, actType);

        if(isFieldSelected("activity.govContacts"))
            processGovContacts(activity, actType);

        if(isFieldSelected("activity.issues"))
            processIssues(activity, actType);

    }



    //process addditional fields
    private void processAdditionalFields(AmpActivity activity, ActivityType actType, Boolean update) {
        if(actType.getAdditional() != null && actType.getAdditional().size() > 0){
            for (AdditionalFieldType aft : actType.getAdditional()) {
                //Senegal add
                if( isEqualStringsNWS(aft.getField(), "PTIP") ){
                    activity.setCrisNumber(aft.getValue());
                }
                //Senegal add
                if( isEqualStringsNWS(aft.getField(), "Code du Chapitre") ){
                    activity.setVote(aft.getValue().substring(0, 3));
                    activity.setSubVote(aft.getValue().substring(3, 5));
                    activity.setSubProgram(aft.getValue().substring(5, 8));
                    activity.setProjectCode(aft.getValue().substring(8, 11));
                }
                //Senegal add
                if( isEqualStringsNWS(aft.getField(), "onBudget") )
                {
                    AmpCategoryValue onCV = CategoryConstants.ACTIVITY_BUDGET_ON.getAmpCategoryValueFromDB();
                    CategoryManagerUtil.addCategoryToSet(onCV.getId(), activity.getCategories());
                }
            }
        }
    }



    /*
 * *********************** step 1 process methods
 */
    //title 
    private void processTitle(AmpActivity activity, ActivityType actType){
        
        //title
        activity.setName("");
        //TODO default language to be added is en
        String lang = "en";
        if(actType.getTitle()!=null && actType.getTitle().size() >0 )
        {
            boolean found=false;
            ArrayList<FreeTextType> titlesList=(ArrayList<FreeTextType>) actType.getTitle();
            for (Iterator iterator = titlesList.iterator(); iterator.hasNext();) {
                FreeTextType title = (FreeTextType) iterator.next();
                //TODO language default received by param
                if(title.getLang()!=null && lang.toLowerCase().equals(title.getLang().toLowerCase()) ) {
                    found=true;
                    if(isStringLengthLower(title.getValue(), 255)) 
                        activity.setName(title.getValue());
                }
            }
            //if the default language doesn;t not exist in the title imported, we put the first language
            if(!found && !titlesList.isEmpty()){
                String titleAct= ((FreeTextType)titlesList.iterator().next()).getValue();
                if(isStringLengthLower(titleAct, 255))
                    activity.setName( titleAct );
            }
        }
        
        //TODO throw error if the title is too long or null or only white spaces
        if("".equals(activity.getName())) 
            ;

    }

    //description
    private void processDescription(AmpActivity activity, ActivityType actType) {
        // TODO Auto-generated method stub
        //description
        if (actType.getDescription() != null && actType.getDescription().size() >0 ) {
            activity.setDescription(setEditorFreeTextType(actType.getDescription(), activity, "aim-desc-00-"));
        }
    }

    //objective
    private void processObjective(AmpActivity activity, ActivityType actType) {
        // TODO Auto-generated method stub
        if(actType.getObjective()!=null && actType.getObjective().size() > 0){
            activity.setObjective(setEditorFreeTextType(actType.getObjective(), activity, "aim-obj-00-"));
        }
    }

    
    private void processActivityAssigningOrg(AmpActivity activity, ActivityType actType) {
        // TODO Auto-generated method stub
        if(actType.getId() != null && actType.getId().size() > 0){
            Set internalIds = new HashSet();
            ArrayList<Id> ids = (ArrayList<Id>) actType.getId();
            for (Id id : ids) {
                AmpActivityInternalId actInternalId = new AmpActivityInternalId();
                if (isValidString(id.getUniqID()))
                    actInternalId.setInternalId(id.getUniqID());
                actInternalId.setAmpActivity(activity);
                AmpOrganisation org = (AmpOrganisation) getAmpObject(DEConstants.AMP_ORGANIZATION, id.getAssigningOrg());
                if (org != null)
                    actInternalId.setOrganisation(org);
                internalIds.add(actInternalId);
            }
            if(activity.getInternalIds() == null)
                activity.setInternalIds(new HashSet());
            else activity.getInternalIds().clear();
            activity.getInternalIds().addAll(internalIds);
        }
    }

    //proposed Approval date
    private void processProposedApprovalDate(AmpActivity activity, ActivityType actType){
      if(actType.getProposedApprovalDate()!=null && "".compareTo(actType.getProposedApprovalDate().getDate().toString()) !=0){
          Date date=DataExchangeUtils.XMLGregorianDateToDate(actType.getProposedApprovalDate().getDate());
          if(date!=null)
              activity.setProposedApprovalDate(date);
      }
    }

    //closing date
    private void processClosingDate(AmpActivity activity, ActivityType actType) {
      if(actType.getClosingDate()!=null && "".compareTo(actType.getClosingDate().getDate().toString()) !=0){
          Date date=DataExchangeUtils.XMLGregorianDateToDate(actType.getClosingDate().getDate());
          if(date!=null)
              activity.setProposedCompletionDate(date);
      }
    }

    //modified closing date
    private void processModifiedClosingDate(AmpActivity activity, ActivityType actType) {
      if(actType.getModifiedClosingDate()!=null && "".compareTo(actType.getModifiedClosingDate().getDate().toString()) !=0){
          Date date=DataExchangeUtils.XMLGregorianDateToDate(actType.getModifiedClosingDate().getDate());
          if(date!=null)
              activity.setActualCompletionDate(date);
      }
    }

      
    //actual start date
    private void processActualStartDate(AmpActivity activity, ActivityType actType) {
      if(actType.getActualStartDate()!=null && "".compareTo(actType.getActualStartDate().getDate().toString()) !=0){
          Date date=DataExchangeUtils.XMLGregorianDateToDate(actType.getActualStartDate().getDate());
          if(date!=null)
              activity.setActualStartDate(date);
      }
    }

    //proposed start date
    private void processProposedStartDate(AmpActivity activity, ActivityType actType) {
      if(actType.getProposedStartDate()!=null && "".compareTo(actType.getProposedStartDate().getDate().toString()) !=0){
          Date date=DataExchangeUtils.XMLGregorianDateToDate(actType.getProposedStartDate().getDate());
          if(date!=null)
              activity.setProposedStartDate(date);
      } 
    }

    //actual Approval date
    private void processActualApprovalDate(AmpActivity activity, ActivityType actType) {
      if(actType.getActualApprovalDate()!=null && "".compareTo(actType.getActualApprovalDate().getDate().toString()) !=0){
          Date date=DataExchangeUtils.XMLGregorianDateToDate(actType.getActualApprovalDate().getDate());
          if(date!=null)
              activity.setActualApprovalDate(date);
      }
    }

    //activity status
    private void processStatus(AmpActivity activity, ActivityType actType, Boolean update) {
        // TODO Auto-generated method stub
        if( isValidCodeValueTypeValue(actType.getStatus())){
            
            if (activity.getCategories() == null) {
                activity.setCategories( new HashSet() );
            }
            
            if(update){
                for (Iterator it = activity.getCategories().iterator(); it.hasNext();) {
                    AmpCategoryValue acv = (AmpCategoryValue) it.next();
                    if(DEConstants.CATEG_VALUE_ACTIVITY_STATUS.equals(acv.getAmpCategoryClass().getKeyName()))
                        it.remove();
                        //activity.getCategories().remove(acv);
                }
            }
            AmpCategoryValue acv = getAmpCategoryValueFromCVT(actType.getStatus(), DEConstants.CATEG_VALUE_ACTIVITY_STATUS);
                
            if(acv!=null)
                activity.getCategories().add(acv);
        }
    }

    //status reason
    private void processStatusReason(AmpActivity activity, ActivityType actType) {
        if( actType.getStatusReason()!=null){
            if(isValidString(actType.getStatusReason().getValue()))
                activity.setStatusReason(actType.getStatusReason().getValue());
        }

    }

/*
 * ********************** step 2 process methods
 */
    
    private void processSectors(AmpActivity activity, ActivityType actType) {
        Long sectorId;
        if(actType.getSectors()!=null && actType.getSectors().size() > 0){
            Set<AmpActivitySector> sectors = new HashSet<AmpActivitySector>();

            for (PercentageCodeValueType idmlSector : actType.getSectors()) {
                CodeValueType sectorAux = new CodeValueType();
                sectorAux.setCode(idmlSector.getCode());

                String sectorValue = "";

                sectorAux.setValue(idmlSector.getValue());
                sectorAux.setValue(idmlSector.getCode() + ". " + idmlSector.getValue());
                //sectorValue= idmlSector.getValue();
                //SENEGAL changes
//              sectorValue= idmlSector.getCode()+". "+idmlSector.getValue();
//              sectorAux.setValue(sectorValue);

                sectorAux.setValue(idmlSector.getCode() + ". " + idmlSector.getValue());
                //get sector by name and code
                AmpSector ampSector = (AmpSector) getAmpObject(DEConstants.AMP_SECTOR, sectorAux);

                //this can not happen. if the sector is null it should throw an exception
                if (ampSector == null || ampSector.getAmpSectorId() == null) continue;

                AmpActivitySector ampActSector = new AmpActivitySector();
                ampActSector.setActivityId(activity);
                sectorId = ampSector.getAmpSectorId();
                if (sectorId != null && (!sectorId.equals(new Long(-1))))
                    ampActSector.setSectorId(ampSector);
                ampActSector.setSectorPercentage(new Float(idmlSector.getPercentage()));

                AmpClassificationConfiguration primConf = null;
                ArrayList<AmpClassificationConfiguration> allClassifConfigs = null;
                //trying to find the classification
                primConf = getConfiguration(ampSector, allClassifConfigs);
                //if the classification doesn't exist we will put the sector under the primary classification!
                if (primConf == null) primConf = getPrimaryClassificationConfiguration(allClassifConfigs);

                ampActSector.setClassificationConfig(primConf);
                sectors.add(ampActSector);
            }
            if (activity.getSectors() == null) {
                activity.setSectors(new HashSet());
            }
            else activity.getSectors().clear();
            activity.getSectors().addAll(sectors);
            //activity.setSectors(sectors);
        }

    }

    
    
    private void processPrograms(AmpActivity activity, ActivityType actType) {
        if(actType.getPrograms()!=null && actType.getPrograms().size() > 0){
            Set<AmpActivityProgram> programs = new HashSet<AmpActivityProgram>();
            
            for (Iterator iterator = actType.getPrograms().iterator(); iterator.hasNext();) {
                PercentageCodeValueType idmlProgram = (PercentageCodeValueType) iterator.next();
                CodeValueType programAux = new CodeValueType();
                programAux.setCode(idmlProgram.getCode());
                programAux.setValue(idmlProgram.getValue());
                
                AmpTheme ampTheme = (AmpTheme) getAmpObject(DEConstants.AMP_PROGRAM,programAux);
                
                if(ampTheme == null || ampTheme.getAmpThemeId() == null) continue;
                
                AmpActivityProgram ampActivityProgram = new AmpActivityProgram();
                ampActivityProgram.setActivity(activity);
                ampActivityProgram.setProgramPercentage((new Float(idmlProgram.getPercentage())));
                ampActivityProgram.setProgram(ampTheme);
                
                ArrayList<AmpActivityProgramSettings> allClassifConfigs = (ArrayList<AmpActivityProgramSettings>) getAllAmpActivityProgramSettings();
                AmpActivityProgramSettings primConf = null;
                
                primConf = getAmpActivityProgramSettings(allClassifConfigs, ampTheme);
                if(primConf == null)
                    primConf = getNationalPlanObjectiveSetting(allClassifConfigs);

                ampActivityProgram.setProgramSetting(primConf);

                programs.add(ampActivityProgram);
            }
            
            if (activity.getActPrograms() == null) 
                activity.setActPrograms(new HashSet());
            else activity.getActPrograms().clear();
            activity.getActPrograms().addAll(programs);
        }

    }

/*
 ******************************* step 3 process methods 
 */
    
    //fundings
    private void processFunding(AmpActivity activity, ActivityType actType, Boolean update) {
        if(actType.getFunding() != null && actType.getFunding().size() > 0){
            Set fundings = new HashSet();
            fundings = (HashSet) getFundingXMLtoAMP(activity, actType, update);
            
            if(activity.getFunding() == null) 
                activity.setFunding(new HashSet());
            else
                activity.getFunding().clear();
            activity.getFunding().addAll(fundings);
        }
    }
    
    //locations
    private void processLocation(AmpActivity activity, ActivityType actType) {
        // TODO Auto-generated method stub
    if(actType.getLocation() !=null && actType.getLocation().size() >0) {
        
        Set<AmpActivityLocation> locations = new HashSet<AmpActivityLocation>();
        for (Iterator it = actType.getLocation().iterator(); it.hasNext();) {
            Location location = (Location) it.next();
            
            AmpCategoryValueLocations ampCVLoc      = null;
            AmpCategoryValue acv= null;
            CodeValueType cvt = new CodeValueType();
            boolean isCountry = false;
            boolean isZone = false;
            boolean isDistrict = false;
            //senegal add
            if("001".equals(location.getLocationName().getCode()) || "0000".equals(location.getLocationName().getCode()) ){
                ampCVLoc = DynLocationManagerUtil.getLocationByCode("87274", (AmpCategoryValue)null );

                cvt.setCode("001");
                cvt.setValue("Country");
                acv = getAmpCategoryValueFromCVT(cvt, DEConstants.CATEG_VALUE_IMPLEMENTATION_LOCATION);
                isCountry = true;
            }
            //normal use
            else {
                    ampCVLoc = DynLocationManagerUtil.getLocationByCode(location.getLocationName().getCode(), (AmpCategoryValue)null );
                    
                    cvt.setCode(location.getLocationName().getCode());
                    if(location.getLocationName().getCode().length() <=3)
                        {
                            cvt.setValue("Zone");
                            isZone = true;
                        }
                    else {
                        cvt.setValue("District");
                        isDistrict = true;
                    }
                    acv = getAmpCategoryValueFromCVT(cvt, DEConstants.CATEG_VALUE_IMPLEMENTATION_LOCATION);
            }
            
            //implementation levels
            //added here for Senegal
            AmpCategoryValue acv1= new AmpCategoryValue();
            if( actType.getImplementationLevels()!=null ){
                
                if(actType.getImplementationLevels().getValue().compareTo("National") == 0 && (isZone || isDistrict))
                {
                    CodeValueType cvt1 = new CodeValueType();
                    cvt1.setCode("Both");
                    cvt1.setValue("Both");
                    acv1 = getAmpCategoryValueFromCVT(cvt1, DEConstants.CATEG_VALUE_IMPLEMENTATION_LEVEL);
                }
                else
                    acv1 = getAmpCategoryValueFromCVT(actType.getImplementationLevels(), DEConstants.CATEG_VALUE_IMPLEMENTATION_LEVEL);
                if(acv1!=null)
                    {
                        if (activity.getCategories() == null) {
                            activity.setCategories( new HashSet() );
                        }
                        removeCategoryFromActivity(activity,DEConstants.CATEG_VALUE_IMPLEMENTATION_LEVEL);
                        activity.getCategories().add(acv1);
                    }
                
            }
            
            AmpLocation ampLoc = null;
            try {
                ampLoc = DynLocationManagerUtil.getAmpLocation(ampCVLoc);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            AmpActivityLocation actLoc  =   new AmpActivityLocation();
            actLoc.setActivity(activity);
            //actLoc.getActivity().setAmpActivityId(null);
            actLoc.setLocation(ampLoc);
            Double percent=new Double(100);
            actLoc.setLocationPercentage(percent.floatValue());
            locations.add(actLoc);

            if(acv!=null)
                {
                    if (activity.getCategories() == null) {
                        activity.setCategories( new HashSet() );
                    }
                    removeCategoryFromActivity(activity,DEConstants.CATEG_VALUE_IMPLEMENTATION_LOCATION);
                    activity.getCategories().add(acv);
                }
            
            
            //regional funding
            importRegionalFunding(activity, location, ampCVLoc);
            
        }
        if(activity.getLocations() == null)
            activity.setLocations(new HashSet<AmpActivityLocation>());
        else activity.getLocations().clear();
        activity.getLocations().addAll(locations);
    }

    }
    
/*
 * ************************ step 4 process methods
 *  
 */
    
    private void removeCategoryFromActivity(AmpActivity activity, String categValueImplementationLevel) {
        // TODO Auto-generated method stub
        for (Iterator it = activity.getCategories().iterator(); it.hasNext();) {
            AmpCategoryValue acv = (AmpCategoryValue) it.next();
            if(categValueImplementationLevel.equals(acv.getAmpCategoryClass().getKeyName()))
                it.remove();
        }
    }

    private void importRegionalFunding(AmpActivity activity, Location location, AmpCategoryValueLocations ampCVLoc) {
        // TODO Auto-generated method stub
        Set regFundings = new HashSet();
        for (Iterator<LocationFundingType> it = location.getLocationFunding().iterator(); it.hasNext();) {
            LocationFundingType funding = (LocationFundingType) it.next();
            addRegionalFundingDetailsToSet(funding.getCommitments(), regFundings,  activity, org.digijava.module.aim.helper.Constants.COMMITMENT,ampCVLoc);
            addRegionalFundingDetailsToSet(funding.getDisbursements(), regFundings,  activity, org.digijava.module.aim.helper.Constants.DISBURSEMENT,ampCVLoc);
            addRegionalFundingDetailsToSet(funding.getExpenditures(), regFundings,  activity, org.digijava.module.aim.helper.Constants.EXPENDITURE,ampCVLoc);
        }
        if(activity.getRegionalFundings() == null)
            activity.setRegionalFundings(new HashSet());
        else 
            activity.getRegionalFundings().clear();
        
        activity.getRegionalFundings().addAll(regFundings);
        //getRegionalFundingXMLToAmp(location.getLocationFunding(),regFundings);
        
    }


    
    private void addRegionalFundingDetailsToSet( List<FundingDetailType> fundings, Set regFundings, AmpActivity activity, int transactionType, AmpCategoryValueLocations ampCVLoc) {
        // TODO Auto-generated method stub      
        
        for (Iterator it = fundings.iterator(); it.hasNext();) {
            FundingDetailType fdt = (FundingDetailType) it.next();
            AmpRegionalFunding ampRegFund = new AmpRegionalFunding();
            ampRegFund.setActivity(activity);
            ampRegFund.setTransactionType(new Integer(transactionType));
            ampRegFund.setCurrency(CurrencyUtil.getCurrencyByCode(fdt.getCurrency()));
            ampRegFund.setRegionLocation(ampCVLoc);
            
            try {
            if( DEConstants.IDML_PLAN.equals(fdt.getType()) ) 
                ampRegFund.setAdjustmentType(CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getAmpCategoryValueFromDB());
            if( DEConstants.IDML_ACTUAL.equals(fdt.getType()) ) 
                ampRegFund.setAdjustmentType(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getAmpCategoryValueFromDB());
            }catch(Exception ex)
            {
                logger.error("", ex); 
            }
            
            ampRegFund.setTransactionAmount(new Double( fdt.getAmount().doubleValue()));
            ampRegFund.setTransactionDate(DataExchangeUtils.XMLGregorianDateToDate(fdt.getDate()));
            regFundings.add(ampRegFund);
        }
    }

    private void processRelatedOrgs(AmpActivity activity, ActivityType actType) {
        // TODO Auto-generated method stub
        if(actType.getRelatedOrgs()== null || actType.getRelatedOrgs().size()<1) return;
        ArrayList<RelatedOrgs> relatedOrgs = (ArrayList<RelatedOrgs>)actType.getRelatedOrgs();
        Set orgRole = new HashSet();
        if(activity.getOrgrole() !=null )
            for (Iterator it = activity.getOrgrole().iterator(); it.hasNext();) {
                AmpOrgRole aor = (AmpOrgRole) it.next();
                if(  (org.digijava.module.aim.helper.Constants.FUNDING_AGENCY.equals(aor.getRole().getRoleCode())) ) ;
                else it.remove();
            }
        if(relatedOrgs!=null)
            for (Iterator it = relatedOrgs.iterator(); it.hasNext();) {
                RelatedOrgs relOrg = (RelatedOrgs) it.next();
                String type=null;
                if(isValidString(relOrg.getType()) )
                    {
                        type=relOrg.getType();
                        AmpRole role = null;
                        if(isEqualStringsNWS(type, DEConstants.IDML_BENEFICIARY_AGENCY))
                            role=DbUtil.getAmpRole(org.digijava.module.aim.helper.Constants.BENEFICIARY_AGENCY);
                        if(isEqualStringsNWS(type, DEConstants.IDML_CONTRACTING_AGENCY))
                            role=DbUtil.getAmpRole(org.digijava.module.aim.helper.Constants.CONTRACTING_AGENCY);
                        if(isEqualStringsNWS(type, DEConstants.IDML_CONTRACTOR))
                            role=DbUtil.getAmpRole(org.digijava.module.aim.helper.Constants.CONTRACTOR);
                        if(isEqualStringsNWS(type, DEConstants.IDML_EXECUTING_AGENCY))
                            role=DbUtil.getAmpRole(org.digijava.module.aim.helper.Constants.EXECUTING_AGENCY);
                        if(isEqualStringsNWS(type, DEConstants.IDML_FUNDING_AGENCY))
                            role=DbUtil.getAmpRole(org.digijava.module.aim.helper.Constants.FUNDING_AGENCY);
                        if(isEqualStringsNWS(type, DEConstants.IDML_IMPLEMENTING_AGENCY))
                            role=DbUtil.getAmpRole(org.digijava.module.aim.helper.Constants.IMPLEMENTING_AGENCY);
                        if(isEqualStringsNWS(type, DEConstants.IDML_REGIONAL_GROUP))
                            role=DbUtil.getAmpRole(org.digijava.module.aim.helper.Constants.REGIONAL_GROUP);
                        if(isEqualStringsNWS(type, DEConstants.IDML_RELATED_INSTITUTIONS))
                            role=DbUtil.getAmpRole(org.digijava.module.aim.helper.Constants.RELATED_INSTITUTIONS);
                        if(isEqualStringsNWS(type, DEConstants.IDML_REPORTING_AGENCY))
                            role=DbUtil.getAmpRole(org.digijava.module.aim.helper.Constants.REPORTING_AGENCY);
                        if(isEqualStringsNWS(type, DEConstants.IDML_RESPONSIBLE_ORGANIZATION))
                            {
                                role=DbUtil.getAmpRole(org.digijava.module.aim.helper.Constants.RESPONSIBLE_ORGANISATION);
                                //senegal add ????
                            //  if(isValidString(relOrg.getCode()))
                            //  activity.setFY(relOrg.getCode());
                            }
                        if(isEqualStringsNWS(type, DEConstants.IDML_SECTOR_GROUP))
                            role=DbUtil.getAmpRole(org.digijava.module.aim.helper.Constants.SECTOR_GROUP);
                        if(role !=null){
                            CodeValueType cvt= new CodeValueType();
                            if( isValidString(relOrg.getCode()) )
                                cvt.setCode(relOrg.getCode());
                            if( isValidString(relOrg.getValue()) )
                                cvt.setValue(relOrg.getValue());
                            AmpOrganisation org = (AmpOrganisation) getAmpObject(DEConstants.AMP_ORGANIZATION, cvt);
                            AmpOrgRole ampOrgRole = new AmpOrgRole();
                            ampOrgRole.setActivity(activity);
                            ampOrgRole.setRole(role);
                            ampOrgRole.setOrganisation(org);
                            orgRole.add(ampOrgRole);
                        }
                    }
                //if (activity.getOrgrole() == null || activity.getOrgrole().size() == 0 ){
                //activity.setOrgrole(new HashSet());
                //}
                //activity.getOrgrole().addAll(orgRole);
                if (activity.getOrgrole()==null){
                    activity.setOrgrole(orgRole);
                }else{
                    activity.getOrgrole().addAll(orgRole);
                }
//              if (activity.getOrgrole() == null){
//                  activity.setOrgrole(new HashSet());
//              }
//              else ;;//activity.getOrgrole().clear();
//              activity.getOrgrole().addAll(orgRole);
                
            }
    }
    
/*
 * ********************** step 5 process methods
 */

    //components
    private void processComponent(AmpActivity activity, ActivityType actType) {
        if(actType.getComponent() == null || actType.getComponent().size() < 1) return;
        Collection<Components<AmpComponentFunding>> tempComps = new HashSet();
        ArrayList<Component> componentList = (ArrayList<Component>) actType.getComponent();
        //Collection<Components<AmpComponentFunding>> tempComps = new HashSet();
        if(tempComps == null ) tempComps= new HashSet();
        for (Iterator it = componentList.iterator(); it.hasNext();) {
            Component component = (Component) it.next();
            AmpComponent ampComp= new AmpComponent();
            
            ampComp.setTitle(component.getComponentName());
            Components<AmpComponentFunding> tempComp = new Components<AmpComponentFunding>();
            
            //TODO: ampComponentsType -> probably soon will be moved to category manager
            //ArrayList ampCompTypes = (ArrayList) ComponentsUtil.getAmpComponentTypes();
            
            AmpComponentType act = null;
            if( isValidString(component.getComponentType().getValue()) )
                    act = ComponentsUtil.getAmpComponentTypeByName(component.getComponentType().getValue());
            if(act != null){
                
                    if(ampComp.getActivities() == null) ampComp.setActivities(new HashSet());
                    ampComp = (AmpComponent)DataExchangeUtils.addObjectoToAmp(ampComp);
                    
                    //if(activity.getComponents() == null || activity.getComponents().size() == 0)
                    activity.setComponents(new HashSet());
                    activity.getComponents().add(ampComp);
            
                    HashSet<AmpComponentFunding> acfs = new HashSet<AmpComponentFunding>();
                    for (Iterator iterator = component.getComponentFunding().iterator(); iterator.hasNext();) {
                        ComponentFundingType cft = (ComponentFundingType) iterator.next();
                        HashSet<AmpComponentFunding> temp = new HashSet<AmpComponentFunding>();
                        for (Iterator itComm = cft.getCommitments().iterator(); itComm.hasNext();) {
                            FundingDetailType fundingDetailType = (FundingDetailType) itComm.next();
                            AmpComponentFunding acf= new AmpComponentFunding();
                            acf.setComponent(ampComp);
                            addFundingDetailToAmpCompFund(acf,fundingDetailType, org.digijava.module.aim.helper.Constants.COMMITMENT);
                            //acfs.add(acf);
                            temp.add(acf);
                        }
                        tempComp.setCommitments(temp);
                        
                        HashSet<AmpComponentFunding> temp1 = new HashSet<AmpComponentFunding>();
                        for (Iterator itComm = cft.getDisbursements().iterator(); itComm.hasNext();) {
                            FundingDetailType fundingDetailType = (FundingDetailType) itComm.next();
                            AmpComponentFunding acf= new AmpComponentFunding();
                            acf.setComponent(ampComp);
                            addFundingDetailToAmpCompFund(acf,fundingDetailType, org.digijava.module.aim.helper.Constants.DISBURSEMENT);
                            //acfs.add(acf);
                            temp1.add(acf);
                        }
                        tempComp.setDisbursements(temp1);
                        
                        HashSet<AmpComponentFunding> temp2 = new HashSet<AmpComponentFunding>();
                        for (Iterator itComm = cft.getExpenditures().iterator(); itComm.hasNext();) {
                            FundingDetailType fundingDetailType = (FundingDetailType) itComm.next();
                            AmpComponentFunding acf= new AmpComponentFunding();
                            acf.setComponent(ampComp);
                            addFundingDetailToAmpCompFund(acf,fundingDetailType, org.digijava.module.aim.helper.Constants.EXPENDITURE);
                            //acfs.add(acf);
                            temp2.add(acf);
                        }
                        tempComp.setExpenditures(temp2);
                        
                        //cft.get
                    }
                    //addCollectionToAmp(acfs);
            
                tempComps.add(tempComp);
            }
        }   
    }
    
/*
 * ****************** step 6 process methods    
 */
    //documents
    private void processDocuments(AmpActivity activity, ActivityType actType, HttpServletRequest request) {
        // TODO Auto-generated method stub
        //(new MockStrutTest()).getActionMockObjectFactory().getMockRequest();
        if( actType.getDocuments() == null || actType.getDocuments().size() < 1 ) return;
        
        AmpTeamMember teamLead = activity.getTeam().getTeamLead();
//      DEMockTest mock = new DEMockTest();
//      try {
//          mock.setUp(teamLead.getUser().getId(), teamLead.getUser().getEmail());
//      } catch (Exception e) {
//          // TODO Auto-generated catch block
//          e.printStackTrace();
//      }
//      MockHttpServletRequest request = mock.getRequest();
        
        
        
        //if(actType.getRelatedLinks() != null || actType.getRelatedLinks().size() > 0){
            //setTeamMember(request, "admin@amp.org", 0L);
        HttpSession httpSession     = request.getSession();
        TeamMember oldTeamMember        = (TeamMember)httpSession.getAttribute(org.digijava.module.aim.helper.Constants.CURRENT_MEMBER);
            
        setTeamMember(request,teamLead.getUser().getEmail(),activity.getTeam().getAmpTeamId());         
                for (Iterator it = actType.getDocuments().iterator(); it.hasNext();) {
                    Documents doc = (Documents) it.next();
                    TemporaryDocumentData tdd = new TemporaryDocumentData();
            tdd.setName(doc.getTitle());
                    tdd.setTitle(doc.getTitle());
                    tdd.setDescription(doc.getDescription());
                    //tdd.setFormFile(new  FormFile());
                    //tdd.setContentType("");
                    tdd.setWebLink(null);
                    ActionMessages errors=new ActionMessages();
            NodeWrapper nodeWrapper         = tdd.saveToRepositoryDataExchange(request, errors);
                    
                    if ( nodeWrapper != null ){
                        AmpActivityDocument aac     = new AmpActivityDocument();
                        aac.setUuid(nodeWrapper.getUuid());
                        aac.setDocumentType( ActivityDocumentsConstants.RELATED_DOCUMENTS );
                        if(activity.getActivityDocuments() == null || activity.getActivityDocuments().size() == 0)
                            activity.setActivityDocuments(new HashSet());
                        activity.getActivityDocuments().add(aac);
                    }
                }
        }


    //related links
    private void processRelatedLinks(AmpActivity activity, ActivityType actType, HttpServletRequest request) {
        // TODO Auto-generated method stub
        if( actType.getRelatedLinks() == null || actType.getRelatedLinks().size() < 1 ) return;
        
        AmpTeamMember teamLead = activity.getTeam().getTeamLead();
//      DEMockTest mock = new DEMockTest();
//      try {
//          mock.setUp(teamLead.getUser().getId(), teamLead.getUser().getEmail());
//      } catch (Exception e) {
//          // TODO Auto-generated catch block
//          e.printStackTrace();
//      }
//      MockHttpServletRequest request = mock.getRequest();
        
        
        
        //if(actType.getRelatedLinks() != null || actType.getRelatedLinks().size() > 0){
            //setTeamMember(request, "admin@amp.org", 0L);
        HttpSession httpSession     = request.getSession();
        TeamMember oldTeamMember        = (TeamMember)httpSession.getAttribute(org.digijava.module.aim.helper.Constants.CURRENT_MEMBER);
        
        setTeamMember(request,teamLead.getUser().getEmail(),activity.getTeam().getAmpTeamId());
        for (Iterator it = actType.getRelatedLinks().iterator(); it.hasNext();) {
                    RelatedLinks doc = (RelatedLinks) it.next();
                    TemporaryDocumentData tdd = new TemporaryDocumentData();
            tdd.setName(doc.getLabel());
                    tdd.setTitle(doc.getLabel());
                    tdd.setDescription(doc.getDescription());
                    //FormFile f;
                    //tdd.setFormFile(new  FormFile());
                    tdd.setWebLink(doc.getUrl());
                    //tdd.setContentType("");
                    ActionMessages errors=new ActionMessages();
            NodeWrapper nodeWrapper         = tdd.saveToRepositoryDataExchange(request, errors);
                    
                    if ( nodeWrapper != null ){
                        AmpActivityDocument aac     = new AmpActivityDocument();
                        aac.setUuid(nodeWrapper.getUuid());
                        aac.setDocumentType( ActivityDocumentsConstants.RELATED_DOCUMENTS );
                        if(activity.getActivityDocuments() == null || activity.getActivityDocuments().size() == 0)
                            activity.setActivityDocuments(new HashSet());
                        activity.getActivityDocuments().add(aac);
                    }
                }
        
        httpSession.setAttribute(org.digijava.module.aim.helper.Constants.CURRENT_MEMBER, oldTeamMember);
        
        //}
        }
    
/*
 * ******************* step 7 process methods
 */
    
    //donor contacts
    private void processDonorContacts(AmpActivity activity, ActivityType actType) {
        
        if (actType.getDonorContacts() != null && actType.getDonorContacts().size() > 0){
            ContactType contacts = actType.getDonorContacts().iterator().next();
            if(contacts != null){
                activity.setContFirstName(contacts.getFirstName());
                activity.setContLastName(contacts.getLastName());
                activity.setEmail(contacts.getEmail().toString());
            }
        }
    }

    //gov contacts
    private void processGovContacts(AmpActivity activity, ActivityType actType) {
        if(actType.getGovContacts() !=null  && actType.getGovContacts().size() > 0){
            ContactType contacts = actType.getGovContacts().iterator().next();
            if(contacts != null){
                activity.setMofedCntFirstName(contacts.getFirstName());
                activity.setMofedCntLastName(contacts.getLastName());
                activity.setMofedCntEmail(contacts.getEmail().toString());
            }
        }
    }

    //issues
    private void processIssues(AmpActivity activity, ActivityType actType) {
        if(actType.getIssues() !=null && actType.getIssues().size() >0){
            Set issueSet = new HashSet();
            for (Iterator itIssues = actType.getIssues().iterator(); itIssues.hasNext();) {
                Issues issue = (Issues) itIssues.next();
                AmpIssues ampIssue = new AmpIssues();
                ampIssue.setActivity(activity);
                if(isValidFreeTextType(issue.getTitle()))
                    ampIssue.setName(issue.getTitle().getValue());
                
                Set measureSet = new HashSet();
                
                if (issue.getMeasure() != null && issue.getMeasure().size() > 0) {
                    for (int j = 0; j < issue.getMeasure().size(); j++) {
                        Measure measure = (Measure) issue.getMeasure().get(j);
                        AmpMeasure ampMeasure = new AmpMeasure();
                        ampMeasure.setIssue(ampIssue);
                        if(isValidFreeTextType(measure.getTitle()))
                            ampMeasure.setName(measure.getTitle().getValue());
                        Set actorSet = new HashSet();
                        if (measure.getActor() != null && measure.getActor().size() > 0) {
                            for (int k = 0; k < measure.getActor().size(); k++) {
                                FreeTextType idmlActor = (FreeTextType) measure.getActor().get(k);
                                AmpActor actor= new AmpActor();
                                if(isValidFreeTextType(idmlActor))
                                    actor.setName(idmlActor.getValue());
                                actor.setAmpActorId(null);
                                actor.setMeasure(ampMeasure);
                                actorSet.add(actor);
                            }
                        }
                        ampMeasure.setActors(actorSet);
                        measureSet.add(ampMeasure);
                    }
                }
                ampIssue.setMeasures(measureSet);
                issueSet.add(ampIssue);
            }
            activity.setIssues(new HashSet());
            activity.setIssues(issueSet);
        }

    }
    
    /*
     *********************** validation
     */
    private void validateStep4(ActivityType actType, DEActivityLog logger) {
        // TODO Auto-generated method stub
        if(actType.getRelatedOrgs()!=null)
            for (Iterator it = actType.getRelatedOrgs().iterator(); it.hasNext();) {
                RelatedOrgs relOrg = (RelatedOrgs) it.next();
                CodeValueType cvt= new CodeValueType();
                if( isValidString(relOrg.getCode()) )
                    cvt.setCode(relOrg.getCode());
                if( isValidString(relOrg.getValue()) )
                    cvt.setValue(relOrg.getValue());
                AmpOrganisation org = (AmpOrganisation) getAmpObject(DEConstants.AMP_ORGANIZATION, cvt);
                if(org == null )
                    //errors.add(toStringCVT(cvt));
                    logger.getItems().add(new DEOrgMissingLog(cvt));
                    
            }

    }

    private void validateStep3(ActivityType actType, DEActivityLog logger) {
        // TODO Auto-generated method stub
        
        //funding
        checkFunding(actType, logger);
        
        // validate locations
        checkLocation(actType, logger);
    }




    private void validateStep2(ActivityType actType, DEActivityLog logger) {
        // TODO Auto-generated method stub
        //check sector percentage
        if(actType.getSectors()!=null && actType.getSectors().size() > 0){
            //checking the sum of the sector's percentage
            long percentage = 0;
            for (Iterator iterator = actType.getSectors().iterator(); iterator.hasNext();) {
                PercentageCodeValueType idmlSector = (PercentageCodeValueType) iterator.next();
                percentage+=idmlSector.getPercentage();
                CodeValueType sectorAux = new CodeValueType();
                sectorAux.setCode(idmlSector.getCode());
                //senegal_change
                sectorAux.setValue(idmlSector.getCode()+". "+idmlSector.getValue());
                AmpSector ampSector = (AmpSector) getAmpObject(DEConstants.AMP_SECTOR,sectorAux);
                if(ampSector == null) 
                    //errors.add(toStringCVT(sectorAux));
                    logger.getItems().add(new DESectorMissingLog(sectorAux));
            }
            if(percentage != 100){
                //logger.getItems().add(new DESectorPercentageLog());
            }
        }
        
        
        if(actType.getPrograms()!=null && actType.getPrograms().size() > 0){
            Set<AmpActivityProgram> programs = new HashSet<AmpActivityProgram>();
            long percentage = 0;
            for (Iterator iterator = actType.getPrograms().iterator(); iterator.hasNext();) {
                PercentageCodeValueType idmlProgram = (PercentageCodeValueType) iterator.next();
                percentage+=idmlProgram.getPercentage();
                CodeValueType programAux = new CodeValueType();
                programAux.setCode(idmlProgram.getCode());
                programAux.setValue(idmlProgram.getValue());
                AmpTheme ampTheme = (AmpTheme) getAmpObject(DEConstants.AMP_PROGRAM,programAux);
                if(ampTheme == null)
                    //errors.add(toStringCVT(programAux));
                    logger.getItems().add(new DEProgramMissingLog(programAux));
            }
            if(percentage != 100){
                logger.getItems().add(new DEProgramPercentageLog());
            }
        }
        
        
        
    }

    private void validateStep1(ActivityType actType, DEActivityLog logger) {
        // TODO Auto-generated method stub
        //assigning organizations
        if(actType.getId() != null){
            ArrayList<Id> ids = (ArrayList<Id>) actType.getId();
            for (Iterator it = ids.iterator(); it.hasNext();) {
                Id id = (Id) it.next();
                AmpOrganisation org = (AmpOrganisation) getAmpObject(DEConstants.AMP_ORGANIZATION,id.getAssigningOrg());
                if(org == null){
                    //errors.add(toStringCVT(id.getAssigningOrg()));
                    logger.getItems().add(new DEOrgMissingLog(id.getAssigningOrg()));
                }
            }
        }
        
        //status
        if(actType.getStatus() != null){
            AmpCategoryValue acv = getAmpCategoryValueFromCVT(actType.getStatus(), DEConstants.CATEG_VALUE_ACTIVITY_STATUS);
            if(acv==null)
                //errors.add(toStringCVT(actType.getStatus()));
                logger.getItems().add(new DEStatusMissingLog(actType.getStatus()));
        }
        
    }
    
    
    /*
     ************************** utils **********************
     */
    
    //get an Amp entity from database based on a type and element
    private Object getAmpObject(String fieldType, CodeValueType element) {
        
        if(isValidCodeValueTypeStrict(element)){
            //if it is in the db
            Object obj=null;
            obj = DataExchangeUtils.getElementFromAmp(fieldType, element.getValue(),element);
            if(obj != null) 
                return obj; //return the field which is in db
            else return null; 

        }
        return null;
    }
    
    //get all Amp Classification Configuration
    private List<AmpClassificationConfiguration> getAllClassificationConfiguration(){
        return SectorUtil.getAllClassificationConfigs();
    }

    
    private List<AmpActivityProgramSettings> getAllAmpActivityProgramSettings(){
        List<AmpActivityProgramSettings> configs = null;
        configs = DataExchangeUtils.getAllAmpActivityProgramSettings();
        return configs;
    }
    
    private AmpActivityProgramSettings getNationalPlanObjectiveSetting(List<AmpActivityProgramSettings> configs){
        AmpActivityProgramSettings primConf = null;
        if(configs!=null){
            Iterator<AmpActivityProgramSettings> it = configs.iterator();           
            while(it.hasNext()){
                AmpActivityProgramSettings conf = it.next();                
                if(conf.getName().compareTo(ProgramUtil.NATIONAL_PLAN_OBJECTIVE) == 0) {
                    primConf = conf;break;
                }
            }
        }
        return primConf;
    }
    
    private AmpTheme searchAmpProgram(AmpTheme child) {
        AmpTheme parent = child.getParentThemeId();
        if (parent == null) return child;
        else    return searchAmpProgram(child.getParentThemeId());
        
    }
    
    private AmpClassificationConfiguration getConfiguration(AmpSector ampSector, ArrayList<AmpClassificationConfiguration> allClassifConfigs) {
        // TODO Auto-generated method stub
        allClassifConfigs = (ArrayList<AmpClassificationConfiguration>) getAllClassificationConfiguration();
        for (Iterator it2 = allClassifConfigs.iterator(); it2.hasNext();) {
            AmpClassificationConfiguration acc = (AmpClassificationConfiguration) it2.next();
            if(acc.getClassification().getAmpSecSchemeId().equals(ampSector.getAmpSecSchemeId().getAmpSecSchemeId()))
                    return acc;
        }
        return null;
    }

    private AmpClassificationConfiguration getPrimaryClassificationConfiguration(ArrayList<AmpClassificationConfiguration> allClassifConfigs){
        AmpClassificationConfiguration primConf = null;
        if(allClassifConfigs!=null){
            Iterator<AmpClassificationConfiguration> it = allClassifConfigs.iterator();         
            while(it.hasNext()){
                AmpClassificationConfiguration conf = it.next();                
                if(conf.isPrimary()) primConf = conf;
            }
        }
        return primConf;
    }   
    
    private AmpActivityProgramSettings getAmpActivityProgramSettings(ArrayList<AmpActivityProgramSettings> allClassifConfigs, AmpTheme ampTheme){
        
        AmpTheme parent = searchAmpProgram(ampTheme);
        for (Iterator it2 = allClassifConfigs.iterator(); it2.hasNext();) {
            AmpActivityProgramSettings acc = (AmpActivityProgramSettings) it2.next();
            if(acc.getDefaultHierarchy().getAmpThemeId().equals(parent.getAmpThemeId())) {
                return acc;
            }
            
        }
        return null;
        
    }
    
    private AmpCategoryValue getAmpCategoryValueFromCVT(CodeValueType element, String categoryKey ){
        AmpCategoryValue acv=null;
        Collection<AmpCategoryValue> allCategValues;
        String valueToCateg="";
        
        allCategValues = (Collection<AmpCategoryValue>) CategoryManagerUtil.getAmpCategoryValueCollectionByKey(categoryKey);
        
        if( isValidString(element.getValue()) )     valueToCateg=element.getValue();
        else if( isValidString(element.getCode()) ) valueToCateg=element.getCode();

        if(valueToCateg == null || "".equals(valueToCateg.trim()) ) return null;
        
        for (Iterator itacv = allCategValues.iterator(); itacv.hasNext();) {
            acv = (AmpCategoryValue) itacv.next();
            if(acv.getValue().equals(valueToCateg)) return acv;
        }
        return null;
    }
    
    //check if cvt is not null and the value is good.
    private boolean isValidCodeValueTypeCode(CodeValueType cvt ){
        if(cvt != null && isValidString(cvt.getCode()))
            return true;
        return false;
        
    }

    private boolean isValidCodeValueTypeValue(CodeValueType cvt ){
        if(cvt != null && isValidString(cvt.getValue()))
            return true;
        return false;
        
    }

    private boolean isValidCodeValueTypeStrict(CodeValueType cvt ){
        if(isValidCodeValueTypeValue(cvt) && isValidCodeValueTypeCode(cvt))
            return true;
        return false;
    }

    
    private String setEditorFreeTextType(List<FreeTextType> list , AmpActivity activity, String preKey){
        for (Iterator iterator = list.iterator(); iterator.hasNext();) 
        {
            String key = preKey + System.currentTimeMillis();
            FreeTextType obj = (FreeTextType) iterator.next();
            if(obj!=null && isValidLanguage(obj.getLang()) && isValidString(obj.getValue())){
                Editor ed = DataExchangeUtils.createEditor("amp", key, obj.getLang()); //TODO: bugs source
                ed.setLastModDate(new Date());
                ed.setGroupName(org.digijava.module.editor.util.Constants.GROUP_OTHER);
                ed.setBody(obj.getValue());
                try {
                    org.digijava.module.editor.util.DbUtil.saveEditor(ed);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return key;//activity.setDescription(key);
            }
            
        }
        return null;
    }

    private boolean isValidLanguage(String lang){
        //TODO: validate lang based on dg_locale code for languages
        if(isValidString(lang) && lang.length() == 2 )
            return true;
        return false;
        
    }
    
    private boolean isValidString(String s ){
        if(s != null && !"".equals(s.trim()))
            return true;
        return false;
        
    }
    
    private boolean isStringLengthLower(String s, int length){
        if (s != null) {
            if (s.trim().length() == 0 || s.length() > length) 
                return false;
            else return true;
        }
        return false;
        
    }
    
    private AmpTeam getAssignedWorkspace (){
        return getDESourceSetting().getImportWorkspace();
    }
    
    private String getApprovalStatus(){
        return this.getDESourceSetting().getApprovalStatus();
    }
    
    private String getHierarchies(){
        return this.getDESourceSetting().getLanguageId();
    }
    
    private DESourceSetting getDESourceSetting(){
        return this.getSourceBuilder().getDESourceSetting();
    }
    
    private SourceBuilder getSourceBuilder(){
        return ampImportItem.getSourceBuilder();
    }
    
    public void createAmpLogPerExecution(){
        
    }
    
    public void createAmpLogPerItem(){
        
    }
    
    private Set getFundingXMLtoAMP(AmpActivity activity, ActivityType actType, Boolean update){
        Set<AmpFunding> fundings= null;
        Set orgRole = new HashSet();
        if(activity.getOrgrole() !=null && actType.getFunding() != null)
            for (Iterator it = activity.getOrgrole().iterator(); it.hasNext();) {
                AmpOrgRole aor = (AmpOrgRole) it.next();
                if( org.digijava.module.aim.helper.Constants.FUNDING_AGENCY.equals(aor.getRole().getRoleCode()) )
                    it.remove();
                //else orgRole.add(aor);
            }
        for (Iterator<FundingType> it = actType.getFunding().iterator(); it.hasNext();) {
            AmpRole role = DbUtil.getAmpRole(org.digijava.module.aim.helper.Constants.FUNDING_AGENCY);
            FundingType funding = (FundingType) it.next();
            CodeValueType fundingOrg=funding.getFundingOrg();
            AmpFunding ampFunding = new AmpFunding();
            ampFunding.setActive(true);
            AmpOrganisation ampOrg = (AmpOrganisation) getAmpObject(DEConstants.AMP_ORGANIZATION,fundingOrg);
            Set<AmpFundingDetail> ampFundDetails = new HashSet<AmpFundingDetail>();

            ampFunding.setAmpDonorOrgId(ampOrg);
            ampFunding.setFundingDetails(new HashSet<AmpFundingDetail>());

            
            addMTEFProjectionsToSet(funding.getProjections(),ampFunding);
            addFundingDetailsToSet(funding.getCommitments(), ampFundDetails, org.digijava.module.aim.helper.Constants.COMMITMENT);
            addFundingDetailsToSet(funding.getDisbursements(), ampFundDetails, org.digijava.module.aim.helper.Constants.DISBURSEMENT);
            addFundingDetailsToSet(funding.getExpenditures(), ampFundDetails, org.digijava.module.aim.helper.Constants.EXPENDITURE);
            
            if(ampFunding.getFundingDetails() == null ) ampFunding.setFundingDetails(new HashSet<AmpFundingDetail>());
            if(ampFundDetails != null) ampFunding.getFundingDetails().addAll(ampFundDetails);
            if(funding.getAssistanceType() != null){
                AmpCategoryValue acv = getAmpCategoryValueFromCVT(funding.getAssistanceType(), DEConstants.CATEG_VALUE_TYPE_OF_ASSISTANCE);
                ampFunding.setTypeOfAssistance(acv);
            }
            if(funding.getFinancingInstrument() != null){
                AmpCategoryValue acv = getAmpCategoryValueFromCVT(funding.getFinancingInstrument(), DEConstants.CATEG_VALUE_FINANCING_INSTRUMENT);
                ampFunding.setFinancingInstrument(acv);
            }
            if(activity !=null ) ampFunding.setAmpActivityId(activity);
//          if(activity.getFunding() ==  null) activity.setFunding(new HashSet<AmpFunding>());
            
            //populate the funding with the existing data in AMP DB
            
            Set<AmpFunding> ampFundings = activity.getFunding();
            for (AmpFunding af : ampFundings) {
                if(ampFunding.getAmpDonorOrgId().compareTo(af.getAmpDonorOrgId()) == 0){
                    ampFunding.setFinancingId(af.getFinancingId());
                    ampFunding.setModeOfPayment(af.getModeOfPayment());
                    ampFunding.setFundingStatus(af.getFundingStatus());
                    ampFunding.setActualStartDate(af.getActualStartDate());
                    ampFunding.setActualCompletionDate(af.getActualCompletionDate());
                    ampFunding.setPlannedStartDate(af.getPlannedStartDate());
                    ampFunding.setPlannedCompletionDate(af.getPlannedCompletionDate());
                    ampFunding.setConditions(af.getConditions());
                    ampFunding.setDonorObjective(af.getDonorObjective());
                    break;
                }
            }
            
            if(fundings == null) fundings=new HashSet<AmpFunding>();
            
            //conditions
            //TODO: the language - lang attribute
            if(isValidFreeTextType(funding.getConditions())) ampFunding.setConditions(funding.getConditions().getValue());
            
            fundings.add(ampFunding);
            AmpOrgRole ampOrgRole = new AmpOrgRole();
            ampOrgRole.setActivity(activity);
            ampOrgRole.setRole(role);
            ampOrgRole.setOrganisation(ampOrg);
            orgRole.add(ampOrgRole);
        }
        
//      if (activity.getOrgrole() == null){
//          activity.setOrgrole(new HashSet<AmpOrgRole>());
//      }
//      else ; // already cleared at the beginning of the method!
        //activity.getOrgrole().addAll(orgRole);
        if (activity.getOrgrole()==null){
            activity.setOrgrole(orgRole);
        }else{
            activity.getOrgrole().addAll(orgRole);
        }
        return fundings;
    }

    private void addMTEFProjectionsToSet(List<Projections> projections, AmpFunding ampFunding) {
        // TODO Auto-generated method stub
        if(ampFunding.getMtefProjections() == null) ampFunding.setMtefProjections(new HashSet<AmpFundingMTEFProjection> ());
        if(projections!=null)
        {
            Iterator mtefItr=projections.iterator();
            while (mtefItr.hasNext())
            {
                Projections mtef=(Projections)mtefItr.next();
                //senegal add
                if(mtef.getAmount().doubleValue() == 0 ) continue;

                AmpFundingMTEFProjection ampmtef=new AmpFundingMTEFProjection();
                
                //if("true".equals(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS)))
                    //ampmtef.setAmount(new Double(mtef.getAmount()*1000));
                //else
                ampmtef.setAmount(new Double(mtef.getAmount().doubleValue()));

                ampmtef.setAmpFunding(ampFunding);
                ampmtef.setAmpCurrency(CurrencyUtil.getCurrencyByCode(mtef.getCurrency()));
                
                if( mtef.getType()!=null ){
                    CodeValueType cvt = new CodeValueType();
                    cvt.setCode(mtef.getType());
                    cvt.setValue(mtef.getType());
                    AmpCategoryValue acv = getAmpCategoryValueFromCVT(cvt, DEConstants.CATEG_VALUE_MTEF_PROJECTION);
                    if(acv!=null)
                        ampmtef.setProjected(acv);
                }
                
                ampmtef.setProjectionDate( DataExchangeUtils.intDateToDate(mtef.getStartYear()) );
                ampFunding.getMtefProjections().add(ampmtef);
            }
        }
    }
    
    private void addFundingDetailsToSet(List<FundingDetailType> fundingsDetails, Set<AmpFundingDetail> fundDetails, int transactionType) {
        // TODO Auto-generated method stub
        for (Iterator it = fundingsDetails.iterator(); it.hasNext();) {
            FundingDetailType fundDet = (FundingDetailType) it.next();
            
            //senegal
            if(fundDet.getAmount().doubleValue()==0) continue;
            
            AmpFundingDetail ampFundDet = new AmpFundingDetail();
    
            ampFundDet.setTransactionType(new Integer(transactionType));
            ampFundDet.setTransactionDate(DataExchangeUtils.XMLGregorianDateToDate(fundDet.getDate()));
            
            ampFundDet.setAdjustmentType(CategoryManagerUtil.getAmpCategoryValueFromDb(CategoryConstants.ADJUSTMENT_TYPE_KEY, new Long(fundDet.getType())));
                        
            //TODO mapping the currencies!!! ??
            ampFundDet.setAmpCurrencyId(CurrencyUtil.getCurrencyByCode(fundDet.getCurrency()));
            
            //TODO how are the amounts? in thousands?
            //if("true".equals(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS)))
                //ampFundDet.setTransactionAmount(new Double(fundDet.getAmount()*1000));
            //else 
            ampFundDet.setTransactionAmount(new Double(fundDet.getAmount().doubleValue()));
            fundDetails.add(ampFundDet);
        }
        
    }
    
    //check if two strings are the equals without white spaces
    private boolean isEqualStringsNWS(String s, String t){
        return (s.toLowerCase().trim().equals(t.toLowerCase().trim()));
    }
    
    //funding details for components
    private void addFundingDetailToAmpCompFund(AmpComponentFunding acf, FundingDetailType fundingDetailType, int transactionType) {
        // TODO Auto-generated method stub
        
        acf.setTransactionType(new Integer(transactionType));
        
        acf.setTransactionDate(DataExchangeUtils.XMLGregorianDateToDate(fundingDetailType.getDate()));
            
        try{
        if( DEConstants.IDML_PLAN.equals(fundingDetailType.getType()) ) 
            acf.setAdjustmentType(CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getAmpCategoryValueFromDB());
        if( DEConstants.IDML_ACTUAL.equals(fundingDetailType.getType()) ) 
            acf.setAdjustmentType(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getAmpCategoryValueFromDB());
        } catch(Exception ex) {
            
            logger.error("", ex);
        }
        //TODO mapping the currencies!!! ??
        acf.setCurrency(CurrencyUtil.getCurrencyByCode(fundingDetailType.getCurrency()));
        
        //if("true".equals(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS)))
            //acf.setTransactionAmount(new Double(fundingDetailType.getAmount()*1000));
        //else 
        acf.setTransactionAmount(new Double(fundingDetailType.getAmount().doubleValue()));
        
    }
    
    //generating the HashMap with the selected fields from UI
    public void generateFieldHashMap(){
        this.setHashFields(new HashMap<String,Boolean>());
        for (Iterator it = this.getDESourceSetting().getFields().iterator(); it.hasNext();) {
            String field = (String) it.next();
            this.getHashFields().put(field, new Boolean(true));
        }
    }
    
    
    private boolean isValidFreeTextType(FreeTextType title) {
        // TODO Auto-generated method stub
        if(title != null && isValidString(title.getValue())) return true;
        return false;
    }
    
    public boolean checkInputString(String inputLog){
        boolean isOk = true;
        Activities acts = null;
        DEImportValidationEventHandler log = new DEImportValidationEventHandler();
        try {
            JAXBContext jc = JAXBContext.newInstance(DEConstants.IDML_JAXB_INSTANCE);
            Unmarshaller m = jc.createUnmarshaller();
            URL rootUrl   = this.getClass().getResource("/");
            String path="";
            try {
                path     = rootUrl.toURI().resolve("../../").getPath();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            boolean xsdValidate = true;
            
                if(xsdValidate){
                    // create a SchemaFactory that conforms to W3C XML Schema
                     SchemaFactory sf = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");

                     // parse the purchase order schema
                     Schema schema = sf.newSchema(new File(path+DEConstants.IDML_SCHEMA_LOCATION));

                     m.setSchema(schema);
                     // set your error handler to catch errors during schema construction
                     // we can use custom validation event handler
                     m.setEventHandler(log);
                     //m.setValidating(false);
                      acts = (org.digijava.module.dataExchange.jaxb.Activities) m.unmarshal(this.getAmpImportItem().getInputStream()) ;
               }
            } 
            catch (SAXException e) {
                isOk = false;
                e.printStackTrace();
            }
            catch (javax.xml.bind.JAXBException jex) {
                jex.printStackTrace();
            }
            inputLog += log.getLog();
            if(isOk)
                this.getAmpImportItem().setActivities(acts);
            else this.getAmpImportItem().setActivities(null);
            return isOk;
            
    }


    
    public DEImportItem getAmpImportItem() {
        return ampImportItem;
    }

    public void setAmpImportItem(DEImportItem ampImportItem) {
        this.ampImportItem = ampImportItem;
    }
    

    public boolean isFieldSelected(String s){
        return this.getHashFields().containsKey(s);
    }   

    private void setTeamMember(HttpServletRequest request, String email, Long teamId){
        HttpSession httpSession     = request.getSession();
        TeamMember teamMember       = (TeamMember)httpSession.getAttribute(org.digijava.module.aim.helper.Constants.CURRENT_MEMBER);
        if(teamMember == null)
            teamMember = new TeamMember();
        teamMember.setEmail(email);
        teamMember.setTeamId(teamId);
        //httpSession.setAttribute(org.digijava.module.aim.helper.Constants.CURRENT_MEMBER,teamMember);
    }

    private AmpActivity getAmpActivityByComposedKey(ActivityType actType, String key, String separator) {
        // TODO Auto-generated method stub
        AmpActivity activity = null;
        //activity = ;
        String dbKey                    =   actType.getDbKey();
        HashMap<String, String> hm      =   new HashMap<String, String>();
        HashMap<String, NullableType> hmType    =   new HashMap<String, NullableType>();
        DataExchangeUtils.generateHashMapTypes(hmType);
        String query                    =   DataExchangeUtils.generateQuery(dbKey, key, separator, hm);
        if(query == null) return activity;
        activity                        =   DataExchangeUtils.getActivityByComposedKey(query, hm,hmType);
        
        return activity;
    }
    


    private String toStringCVT(CodeValueType cvt){
        return cvt.getValue()+":::"+cvt.getCode();
    }
    
    /*
     ******* execution
     */

    public void run(HttpServletRequest request) {
        // TODO Auto-generated method stub
        DELogPerExecution execLog   = new DELogPerExecution(this.getDESourceSetting());
        if(execLog.getLogItems() == null)
            execLog.setLogItems(new ArrayList<DELogPerItem>());
        execLog.setDeSourceSetting(this.getDESourceSetting());
        this.getDESourceSetting().getLogs().add(execLog);
        SourceSettingDAO iLog           = null;
        
        try {
                iLog         =  new SessionSourceSettingDAO();
        } catch (HibernateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DgException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        execLog.setExecutionTime(new Timestamp(System.currentTimeMillis()));
        iLog.saveObject(this.getDESourceSetting());
        execLog.setDescription("");
        boolean ok   =  checkInputString(execLog.getDescription());
        iLog.saveObject(this.getDESourceSetting());
        if(!ok) return;
        generateFieldHashMap();
        processFeed(execLog, iLog, request);
        
    }
    /**import strategy (dropdown):    
    public final static String IMPORT_STRATEGY_NEW_PROJ                 = "ADD NEW PROJECTS";
    public final static String IMPORT_STRATEGY_UPD_PROJ                 = "UPDATE PROJECTS";
    public final static String IMPORT_STRATEGY_NEW_PROJ_AND_UPD_PROJ    = "ADD NEW PROJECTS AND UPDATE PROJECTS";
    */
    
    private String getImportStrategy(){
        return this.getDESourceSetting().getImportStrategy();
    }
    
    private void processFeed(DELogPerExecution log, SourceSettingDAO iLog, HttpServletRequest request) {
        logger.info("SYSOUT: processing "+this.getAmpImportItem().getActivities().getActivity().size()+" projects");
        for (Iterator it = this.getAmpImportItem().getActivities().getActivity().iterator(); it.hasNext();) {
            ActivityType actType    = (ActivityType) it.next();
            AmpActivity activity    = getAmpActivity(actType);
            DELogPerItem    item    = new DELogPerItem();
            item.setItemType(DELogPerItem.ITEM_TYPE_ACTIVITY);
            DEActivityLog contentLogger = new DEActivityLog();
            validateActivityContent(actType, contentLogger);
            if(log.getLogItems() == null) 
                log.setLogItems(new ArrayList<DELogPerItem>());
            
            item.setDeLogPerExecution(log);
            item.setExecutionTime(new Timestamp(System.currentTimeMillis()));

            if(contentLogger.getItems() != null && contentLogger.getItems().size() > 0)
            {
                item.setDescription(TranslatorWorker.translateText("Activity")+": "+actType.getDbKey()+" "+contentLogger.display());
                item.setLogType(DELogPerItem.LOG_TYPE_ERROR);
                //iLog.saveObject(item);
                log.getLogItems().add(item);
                iLog.saveObject(log.getDeSourceSetting());
                continue;
            }
            item.setLogType(DELogPerItem.LOG_TYPE_INFO);
            item.setDescription(TranslatorWorker.translateText("Activity")+": "+actType.getDbKey()+" OK");
            try {
                if(activity == null && DESourceSetting.IMPORT_STRATEGY_NEW_PROJ_AND_UPD_PROJ.equals(getImportStrategy()) ||
                        activity == null && DESourceSetting.IMPORT_STRATEGY_NEW_PROJ.equals(getImportStrategy()) ){
                    //activity doesn't exist
                    activity = new AmpActivity();
                    insertActivity(activity, actType, false, request);
                }
                else 
                    if(activity != null && DESourceSetting.IMPORT_STRATEGY_NEW_PROJ_AND_UPD_PROJ.equals(getImportStrategy()) ||
                            activity != null && DESourceSetting.IMPORT_STRATEGY_UPD_PROJ.equals(getImportStrategy()) ){
                        //activity exists
                        updateActivity(activity, actType, true, request);
                    }
                    else {
                        //write in log that this activity was skipped
                        item.setLogType(DELogPerItem.LOG_TYPE_INFO);
                        item.setDescription(TranslatorWorker.translateText("Activity")+": "+actType.getDbKey()+" "+TranslatorWorker.translateText("was skipped"));
                    }
            
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                item.setLogType(DELogPerItem.LOG_TYPE_ERROR);
                item.setDescription(TranslatorWorker.translateText("Activity")+": "+actType.getDbKey()+" "+e.getMessage());
            }

            log.getLogItems().add(item);
            //item.setDeLogPerExecution(log);
            //item.setExecutionTime(new Timestamp(System.currentTimeMillis()));
            iLog.saveObject(log.getDeSourceSetting());
        }
    }   

    private void checkLocation(ActivityType actType, DEActivityLog logger) {
        // TODO Auto-generated method stub
    if(actType.getLocation() !=null && actType.getLocation().size() >0) {
        
        Set<AmpActivityLocation> locations = new HashSet<AmpActivityLocation>();
        for (Iterator it = actType.getLocation().iterator(); it.hasNext();) {
            Location location = (Location) it.next();
            
            AmpCategoryValueLocations ampCVLoc      = null;
            AmpCategoryValue acv= null;
            CodeValueType cvt = new CodeValueType();
            boolean isCountry = false;
            boolean isZone = false;
            boolean isDistrict = false;
            //senegal add
            if("001".equals(location.getLocationName().getCode()) || "0000".equals(location.getLocationName().getCode()) ){
                ampCVLoc = DynLocationManagerUtil.getLocationByCode("87274", (AmpCategoryValue)null );

                cvt.setCode("001");
                cvt.setValue("Country");
                acv = getAmpCategoryValueFromCVT(cvt, DEConstants.CATEG_VALUE_IMPLEMENTATION_LOCATION);
                isCountry = true;
            }
            //normal use
            else {
                    ampCVLoc = DynLocationManagerUtil.getLocationByCode(location.getLocationName().getCode(), (AmpCategoryValue)null );
                    
                    cvt.setCode(location.getLocationName().getCode());
                    if(location.getLocationName().getCode().length() <=3)
                        {
                            cvt.setValue("Zone");
                            isZone = true;
                        }
                    else {
                        cvt.setValue("District");
                        isDistrict = true;
                    }
                    acv = getAmpCategoryValueFromCVT(cvt, DEConstants.CATEG_VALUE_IMPLEMENTATION_LOCATION);
            }
            if(ampCVLoc == null)
                {
                    CodeValueType cvtLocation = new CodeValueType();
                    cvtLocation.setCode(location.getLocationName().getCode());
                    cvtLocation.setValue(location.getLocationName().getValue());
                    logger.getItems().add(new DEImplLevelMissingLog(cvtLocation));
                }
            if(acv==null)
            {
                logger.getItems().add(new DEImplLevelMissingLog(cvt));
            }
            //implementation levels
            //added here for Senegal
            AmpCategoryValue acv1= null;// new AmpCategoryValue();
            if( actType.getImplementationLevels()!=null ){
                        
                if(actType.getImplementationLevels().getValue().compareTo("National") == 0 && (isZone || isDistrict))
                {
                    CodeValueType cvt1 = new CodeValueType();
                    cvt1.setCode("Both");
                    cvt1.setValue("Both");
                    acv1 = getAmpCategoryValueFromCVT(cvt1, DEConstants.CATEG_VALUE_IMPLEMENTATION_LEVEL);
                }
                else
                    acv1 = getAmpCategoryValueFromCVT(actType.getImplementationLevels(), DEConstants.CATEG_VALUE_IMPLEMENTATION_LEVEL);
                if(acv1==null)
                    {
                        logger.getItems().add(new DEImplLevelMissingLog(actType.getImplementationLevels()));
                    }
                
            }
            //regional funding
            checkRegionalFundingCurrency(location, logger);
        }

    }

    }

    private void checkCurrencyFunding(List<FundingDetailType> funding, DEActivityLog logger) {
        for (Iterator it = funding.iterator(); it.hasNext();) {
            FundingDetailType fdt = (FundingDetailType) it.next();
            
            if(CurrencyUtil.getCurrencyByCode(fdt.getCurrency()) == null)
                {
                    CodeValueType cvt = new CodeValueType();
                    cvt.setValue(fdt.getCurrency());
                    cvt.setCode(fdt.getCurrency());
                    logger.getItems().add(new DECurrencyMissingLog(cvt));
                }
            
        }
    }
    
    private void checkRegionalFundingCurrency(Location location, DEActivityLog logger) {
        Set regFundings = new HashSet();
        for (Iterator<LocationFundingType> it = location.getLocationFunding().iterator(); it.hasNext();) {
            LocationFundingType funding = (LocationFundingType) it.next();
            checkCurrencyFunding(funding.getCommitments(), logger);
            checkCurrencyFunding(funding.getDisbursements(), logger);
            checkCurrencyFunding(funding.getExpenditures(), logger);
            
        }
    }

    private void checkFunding(ActivityType actType, DEActivityLog logger) {
        for (Iterator<FundingType> it = actType.getFunding().iterator(); it.hasNext();) {
            FundingType funding = (FundingType) it.next();
            CodeValueType fundingOrg=funding.getFundingOrg();
            AmpFunding ampFunding = new AmpFunding();
            ampFunding.setActive(true);
            AmpOrganisation ampOrg = (AmpOrganisation) getAmpObject(DEConstants.AMP_ORGANIZATION,fundingOrg);
            if(ampOrg == null)
                logger.getItems().add(new DEOrgMissingLog(fundingOrg));
            
            checkCurrencyFunding(funding.getCommitments(),logger);
            checkCurrencyFunding(funding.getDisbursements(),logger);
            checkCurrencyFunding(funding.getExpenditures(),logger);
            
            addMTEFProjectionsToSet(funding.getProjections(),ampFunding);
            
            //type of assistance
            if(funding.getAssistanceType() != null){
                AmpCategoryValue acv = getAmpCategoryValueFromCVT(funding.getAssistanceType(), DEConstants.CATEG_VALUE_TYPE_OF_ASSISTANCE);
                if(acv == null)
                    logger.getItems().add(new DETypeAssistMissingLog(funding.getAssistanceType()));
            }
            //financing instrument
            if(funding.getFinancingInstrument() != null){
                AmpCategoryValue acv = getAmpCategoryValueFromCVT(funding.getFinancingInstrument(), DEConstants.CATEG_VALUE_FINANCING_INSTRUMENT);
                if(acv == null)
                    logger.getItems().add(new DEFinancInstrMissingLog(funding.getFinancingInstrument()));
            }
            
            //MTEF projections
            if(funding.getProjections()!=null)
            {
                Iterator mtefItr=funding.getProjections().iterator();
                while (mtefItr.hasNext())
                {
                    Projections mtef=(Projections)mtefItr.next();
                    if( mtef.getType()!=null ){
                        CodeValueType cvt = new CodeValueType();
                        cvt.setCode(mtef.getType());
                        cvt.setValue(mtef.getType());
                        AmpCategoryValue acv = getAmpCategoryValueFromCVT(cvt, DEConstants.CATEG_VALUE_MTEF_PROJECTION);
                        if(acv==null)
                            logger.getItems().add(new DEMTEFMissingLog(cvt));
                    }
                }
            }
        }
    }

    //*****************************IATI import
    //private void validateIATIActivity(DELogPerExecution log, SourceSettingDAO iLog, HttpServletRequest request) {
    
    public void runIATI2(HttpServletRequest request, String runType, String[] itemId) {
        // TODO Auto-generated method stub
        DELogPerExecution execLog   = new DELogPerExecution(this.getDESourceSetting());
        if(execLog.getLogItems() == null)
            execLog.setLogItems(new ArrayList<DELogPerItem>());
        execLog.setDeSourceSetting(this.getDESourceSetting());
        this.getDESourceSetting().getLogs().add(execLog);
        SourceSettingDAO iLog           = null;
        
        try {
                iLog         =  new SessionSourceSettingDAO();
        } catch (HibernateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DgException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        execLog.setExecutionTime(new Timestamp(System.currentTimeMillis()));
        iLog.saveObject(this.getDESourceSetting());
        if("check".compareTo(runType)==0)
            execLog.setDescription(DEConstants.LOG_PER_EXECUTION_DESC_CHECK);
        if("import".compareTo(runType)==0)
            execLog.setDescription(DEConstants.LOG_PER_EXECUTION_DESC_IMPORT);
        boolean ok   =  checkIATIInputString(execLog.getDescription());
        iLog.saveObject(this.getDESourceSetting());
        if(!ok) return;
        //generateFieldHashMap();
        if("check".compareTo(runType)==0)
            processIATIFeed(request,execLog, iLog, "check",null);
        if("import".compareTo(runType)==0){
            if(itemId!=null && itemId.length>0){
                for(int i=0;i<itemId.length;i++){
                    processIATIFeed(request,execLog, iLog, "import", itemId[i]);
                }
            }
        }
        
    }

    public void runIATI(HttpServletRequest request, String runType, String itemId) {
        // TODO Auto-generated method stub
        DELogPerExecution execLog   = new DELogPerExecution(this.getDESourceSetting());
        if(execLog.getLogItems() == null)
            execLog.setLogItems(new ArrayList<DELogPerItem>());
        execLog.setDeSourceSetting(this.getDESourceSetting());
        this.getDESourceSetting().getLogs().add(execLog);
        SourceSettingDAO iLog           = null;
        
        try {
                iLog         =  new SessionSourceSettingDAO();
        } catch (HibernateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DgException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        execLog.setExecutionTime(new Timestamp(System.currentTimeMillis()));
        iLog.saveObject(this.getDESourceSetting());
        if("check".compareTo(runType)==0)
            execLog.setDescription(DEConstants.LOG_PER_EXECUTION_DESC_CHECK);
        if("import".compareTo(runType)==0)
            execLog.setDescription(DEConstants.LOG_PER_EXECUTION_DESC_IMPORT);
        boolean ok   =  checkIATIInputString(execLog.getDescription());
        iLog.saveObject(this.getDESourceSetting());
        if(!ok) return;
        //generateFieldHashMap();
        if("check".compareTo(runType)==0)
            processIATIFeed(request,execLog, iLog, "check",null);
        if("import".compareTo(runType)==0)
            processIATIFeed(request,execLog, iLog, "import", itemId);
        
    }

    public Map <IatiActivity, Set<DEMappingFields>> processIATIFeedReturnItems(
            HttpServletRequest request,
            DELogPerExecution log,
            SourceSettingDAO iLog,
            String actionType,
            String itemId,
            String selectedCountry) {

        Map <IatiActivity, Set<DEMappingFields>> retVal = new HashMap<IatiActivity, Set<DEMappingFields>>();
        processIATIFeed(request, log, iLog, actionType, itemId, selectedCountry, retVal);

        return retVal;
    }

    private void processIATIFeed(HttpServletRequest request, DELogPerExecution log, SourceSettingDAO iLog, String actionType, String itemId) {
        processIATIFeed(request, log, iLog, actionType, itemId, null, null);
    }

    private void processIATIFeed(
            HttpServletRequest request,
            DELogPerExecution log,
            SourceSettingDAO iLog,
            String actionType,
            String itemId,
            String selectedCountry,
            Map <IatiActivity, Set<DEMappingFields>> retVal) {

        logger.info("SYSOUT: processing iati activities");

        IatiActivities iatiActs = this.getAmpImportItem().getIatiActivities();
        int noAct = 0;
        
        for (Object obj : iatiActs.getIatiActivityOrAny()) {
            if (!(obj instanceof IatiActivity)) continue;
            IatiActivity iAct = (IatiActivity) obj;
            if (iAct.getLang() == null) {
                iAct.setLang(defaultLanguage);
            }
            String logAct = "";
            String title = "";
            String iatiID = "";
            String ampID = null;

            IatiVersion version = IatiVersion.getValueOf(iatiActs.getVersion());

            IatiActivityWorker iWorker = new IatiActivityWorker(iAct, logAct, version);
            iWorker.setLang(defaultLanguage);
            iWorker.setSelectedCountry(selectedCountry);

            //Only need to get structure
            if (retVal != null) {
                iWorker.setSaveObjects(false);
            }

            iWorker.setIgnoreSameAsCheck(ignoreSameAsCheck);
            iWorker.setIsLoad(Boolean.TRUE.equals(request.getAttribute("isLoad")));

            noAct++;
            ArrayList<AmpMappedField> activityLogs = null;
            if ("check".compareTo(actionType) == 0)
            //CHECK content
            {
                logger.info(".......Starting processing activity " + noAct);
                //System.out.println(".......Starting processing activity "+noAct);

                activityLogs = iWorker.checkContent(noAct, this.getHierarchies());

                if (retVal != null) {
                    retVal.put(iAct, iWorker.getAccumulate());
                    if (activityLogs != null) {
                        for (AmpMappedField ampMF : activityLogs) {
                            if (ampMF.getItem() != null && ampMF.getItem().getId() != null) {    //Add serialized ones
                                retVal.get(iAct).add(ampMF.getItem());
                            }
                        }
                    }
                }
                logger.info("..................End processing activity " + noAct);
                //System.out.println("..................End processing activity "+noAct);
            } else if ("import".compareTo(actionType) == 0)
            //import
            {
                DELogPerItem deLogPerItem = DataExchangeUtils.getDELogPerItemById(new Long(itemId));
                if( DELogPerItem.LOG_TYPE_IGNORE.equals(deLogPerItem.getLogType()) )
                    continue;
                if (iWorker.existActivityByTitleIatiId(deLogPerItem.getName())) {
                    logger.info(".......Starting importing activity " + noAct);
                    //System.out.println(".......Starting importing activity "+noAct);
                    Long grpId = new Long(deLogPerItem.getItemType());

                    AmpActivityVersion prevVersion = null;
                    AmpActivityVersion ampActivity = null;

                    if (grpId > -0l) {
                        AmpActivityGroup currentGroup = DataExchangeUtils.getAmpActivityGroupById(grpId);
                        prevVersion = currentGroup.getAmpActivityLastVersion();
                        AmpTeamMember modBy = prevVersion.getModifiedBy() == null ? prevVersion.getActivityCreator() : prevVersion.getModifiedBy();
                        try {
                            ampActivity = ActivityVersionUtil.cloneActivity(prevVersion, modBy);
                            ampActivity.setAmpActivityGroup(currentGroup);
                            iWorker.setMode(DEConstants.MODE_UPDATE);
                            // https://jira.dgfoundation.org/browse/AMP-18223
                            // The line below was added by some reason
                            /// ampActivity.setAmpActivityId(null);
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                    } else {
                        ampActivity = new AmpActivityVersion();
                    }

                    // Ugly hack. Sorry do not know other way how to fix one. The problem is sharing budgets collection
                    // between the sessions
                    if (ampActivity.getOrgrole() != null) {
                        for (AmpOrgRole role : ampActivity.getOrgrole()) {
                            role.setBudgets(null);
                        }
                    }

                    List<AmpContentTranslation> translations = new ArrayList<AmpContentTranslation>();
                    activityLogs = iWorker.populateActivity(ampActivity, prevVersion, this.getDESourceSetting(), translations);
                    AmpTeam team = getAssignedWorkspace();
                    ampActivity.setApprovalStatus(getApprovalStatus());
                    fixEmptyEditorFields(ampActivity, request);
                    ampActivity = DataExchangeUtils.saveActivity(ampActivity, team, translations);
                    LuceneUtil.addUpdateActivity(request.getServletContext().getRealPath("/"), prevVersion!=null, 
                            RequestUtils.getSite(request), Locale.forLanguageTag(defaultLanguage), ampActivity, prevVersion);
                    
                    //update the AmpId of the DEMappingField.
                    AmpMappedField checkedActivity = iWorker.checkActivity(iWorker.getTitle(), iWorker.getIatiID(), iWorker.getLang());
                    DEMappingFields item = checkedActivity.getItem();
                    if (ampActivity.getAmpActivityGroup() != null) {
                        item.setAmpId(ampActivity.getAmpActivityGroup().getAmpActivityGroupId());//getAmpActivityId());
                    }
                    item.setAmpValues(iWorker.toIATIValues(iWorker.getTitle(), iWorker.getIatiID()));
                    DataExchangeUtils.addObjectoToAmp(item);
                    logger.info("..................End importing activity " + noAct);
                    //System.out.println("..................End importing activity "+noAct);
                } else continue;
            }
            //process log
            if (activityLogs == null) continue;

            processLog(log, iLog, iWorker, activityLogs, actionType, (retVal == null));
        }
        if (retVal == null) iLog.saveObject(log.getDeSourceSetting());
    }

    private void fixEmptyEditorFields(AmpActivityVersion ampActivity, HttpServletRequest request) {
        String prefix ="_iati_import_";
        Site site = RequestUtils.getSite(request);

        String[] flds = {"LessonsLearned",
                "Objective",
        "Results",
        "Purpose",
        "ProjectComments",
        "ProjectImpact",
        "ActivitySummary",
        "ContractingArrangements",
        "CondSeq",
        "LinkedActivities",
        "Conditionality",
        "ProjectManagement",
        "Description"};

        Class noparams[] = {};
        Class clazz = null;
        try {
            clazz = Class.forName("org.digijava.module.aim.dbentity.AmpActivityVersion");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        long curMillis = System.currentTimeMillis();
        Date curDate = new Date();
        for (String fld : flds) {
            String getter = new StringBuilder("get").append(fld).toString();

            try {
                Method method = clazz.getMethod(getter, noparams);
                String val = (String) method.invoke(ampActivity, null);

                if (val == null || val.trim().isEmpty()) {
                    String key = new StringBuilder(prefix).append(fld).append("_").append(curMillis).toString();
                    Editor ed = null;
                    if (site != null && site.getId() != null) {
                        ed = DataExchangeUtils.createEditor(site, key, "en");
                    } else {
                        ed = DataExchangeUtils.createEditor("amp", key, "en");
                    }
                    ed.setLastModDate(curDate);
                    ed.setGroupName(Constants.GROUP_OTHER);
                    ed.setBody("");
                    try {
                        org.digijava.module.editor.util.DbUtil.saveEditor(ed);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    Class[] param = new Class[1];
                    param[0] = String.class;
                    String setter = new StringBuilder("set").append(fld).toString();
                    Method set = clazz.getMethod(setter, param);
                    set.invoke(ampActivity, key);
                }

            } catch (NoSuchMethodException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (InvocationTargetException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IllegalAccessException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    private void processLog(DELogPerExecution log, SourceSettingDAO iLog, IatiActivityWorker iWorker, ArrayList<AmpMappedField> activityLogs, String actionType) {
        processLog(log, iLog, iWorker, activityLogs, actionType, false);
    }

    private void processLog(DELogPerExecution log, SourceSettingDAO iLog, IatiActivityWorker iWorker, ArrayList<AmpMappedField> activityLogs, String actionType, boolean saveLogs) {
        String title;
        String iatiID;
        String ampID    = null;
        title = iWorker.getTitle();
        iatiID = iWorker.getIatiID();
        if(iWorker.getAmpID()!=null)
            ampID = iWorker.getAmpID().toString();
        
        DELogPerItem    item    = new DELogPerItem();
        item.setItemType(DELogPerItem.ITEM_TYPE_ACTIVITY);
        DEActivityLog contentLogger = new DEActivityLog();
        
        if(log.getLogItems() == null) 
            log.setLogItems(new ArrayList<DELogPerItem>());
        
        item.setDeLogPerExecution(log);
        item.setExecutionTime(new Timestamp(System.currentTimeMillis()));
        item.setName(title+" - "+iatiID);
        item.setItemType(ampID);
        
        //compute the logs
        TreeSet<String> warn = new TreeSet<String>();
        String logResult = getLogs(activityLogs,"<br/>",warn);
        if( isDoNotImport(activityLogs) ) {
            item.setLogType(DELogPerItem.LOG_TYPE_IGNORE);
        } else 
        if("".compareTo(logResult)!=0 && "<br/>".compareTo(logResult)!=0)
        {
            item.setDescription("<br/>"+TranslatorWorker.translateText("Errors")+"<br/>"+logResult+"<br/>"+TranslatorWorker.translateText("Warnings")+"<br/>"+printArrayList(warn));
            item.setLogType(DELogPerItem.LOG_TYPE_ERROR);
            //iLog.saveObject(item);
            log.getLogItems().add(item);
            if (saveLogs) iLog.saveObject(log.getDeSourceSetting());
            return;
        } else {
            item.setLogType(DELogPerItem.LOG_TYPE_OK);
        }
        item.setDescription("OK" + "<br/>"+TranslatorWorker.translateText("Warnings")+"<br/>"+printArrayList(warn));
        if(iWorker.getAmpID()!=null && !iWorker.getAmpID().equals(DEConstants.AMP_ID_DO_NOT_IMPORT) && iWorker.getExistingActivity()  && "check".compareTo(actionType) ==0){
            AmpActivityGroup ampActGroup = DataExchangeUtils.getAmpActivityGroupById(iWorker.getAmpID());
            AmpActivityVersion actualVersion = ampActGroup.getAmpActivityLastVersion();
            if(actualVersion.getIatiLastUpdatedDate() != null && iWorker.getIatiLastUpdateDate()!=null && actualVersion.getIatiLastUpdatedDate().before(iWorker.getIatiLastUpdateDate()))
            {
                item.setLogType(DELogPerItem.LOG_TYPE_INFO);
                item.setDescription(TranslatorWorker.translateText("The last version of IATI Activity is already imported"));
            }
            else
                if(this.changedIDs!=null && this.changedIDs.size()>0)
                    if( this.changedIDs.contains(iWorker.getIatiID()) )
                        {
                            item.setLogType(DELogPerItem.LOG_TYPE_INFO);
                            item.setDescription(TranslatorWorker.translateText("The last version of IATI Activity is already imported"));
                        }
        }
        log.getLogItems().add(item);
    }
    
    public String getLogs(ArrayList<AmpMappedField> logs,String delimitator, TreeSet<String> warnings){
        String s="";
        TreeSet<String> errors = new TreeSet<String>();
        for (Iterator<AmpMappedField> it = logs.iterator(); it.hasNext();) {
            AmpMappedField log = (AmpMappedField) it.next();
            try{
                if(log!=null && !log.isOK()) 
                    {
                        errors.add(log.getErrors());
                        warnings.add(log.getWarnings());
                    }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        for (Iterator<String> it = errors.iterator(); it.hasNext();) {
                String st = (String) it.next();
                if(isValidString(st))
                    {
                        s+=st;
                        s+=delimitator==null?"":delimitator;
                    }
        }
        return s;
    }
    
    private boolean isDoNotImport(ArrayList<AmpMappedField> logs) {
        for(AmpMappedField log: logs) {
            if(log!=null && !log.isOK() && log.isDoNotImport() && log.isMainEntry() ) {
                return true;
            }
        }
        return false;
    }
    
    public String printArrayList(TreeSet<String> l){
        String res = "";
        for (Iterator iterator = l.iterator(); iterator.hasNext();) {
            String string = (String) iterator.next();
            res+=string;
            res+="<br/>";
        }
        return res;
    }

    public boolean checkIATIInputString(String inputLog) {
        return checkIATIInputString(inputLog, false);
    }
    
    public boolean checkIATIInputString(String inputLog, boolean readFromRawStream){
        boolean isOk = true;
        IatiActivities iActs = null;
        IatiActivities iActsPrevious = null;
        DEImportValidationEventHandler log = new DEImportValidationEventHandler();
        try {
            JAXBContext jc = JAXBContext.newInstance(DEConstants.IATI_JAXB_INSTANCE);
            Unmarshaller m = jc.createUnmarshaller();
            URL rootUrl   = this.getClass().getResource("/");
            String path="";
            try {
                path     = rootUrl.toURI().resolve("../../").getPath();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            boolean xsdValidate = true;
            
                if(xsdValidate){
                    // create a SchemaFactory that conforms to W3C XML Schema
                     SchemaFactory sf = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");

                     // parse the purchase order schema
                     Schema schema = sf.newSchema(new File(path+DEConstants.IATI_SCHEMA_LOCATION_V_1_03));

                     m.setSchema(schema);
                     m.setEventHandler(log);
                    
                     //iActs = (IatiActivities) m.unmarshal(new FileInputStream(path+"doc/dataExchange/iati.xml")) ;

                    InputStream is = null;

                    if (!readFromRawStream) {
                        is = this.getAmpImportItem().getInputStream();
                    } else {
                        is = this.getAmpImportItem().getRawStream();
                    }

                    iActs = (IatiActivities) m.unmarshal(is) ;

                     if(this.getAmpImportItem().getPreviousInputStream() !=null &&  this.getAmpImportItem().getPreviousInputStream().available()>0)
                         iActsPrevious = (IatiActivities) m.unmarshal(this.getAmpImportItem().getPreviousInputStream()) ;
                     
                     this.changedIDs = checkXML(this.getAmpImportItem().getInputStream(), this.getAmpImportItem().getPreviousInputStream());
                     
               }
            } 
            catch (SAXException e) {
                isOk = false;
                e.printStackTrace();
            }
            catch (javax.xml.bind.JAXBException jex) {
                jex.printStackTrace();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            inputLog += log.getLog();
            if(isOk)
                {
                    this.getAmpImportItem().setIatiActivities(iActs);
                    if(iActsPrevious!=null)
                        this.getAmpImportItem().setPreviousIatiActivities(iActsPrevious);
                }
            else {
                
                this.getAmpImportItem().setIatiActivities(null);
                this.getAmpImportItem().setPreviousIatiActivities(null);
                
            }
            return isOk;
            
    }
    
    private ArrayList<String> checkXML(InputStream inputStream, InputStream previousInputStream) {
        
        ArrayList<String> resultIDs = new ArrayList<String>();
        
        Document currentDoc     = getDocumentFromInputStream(inputStream);
        Document previousDoc    = getDocumentFromInputStream(previousInputStream);
        
        HashMap<String, String> currIds = getActivitiesIds(currentDoc);
        HashMap<String, String> prevIds = getActivitiesIds(previousDoc);
        
        for (Map.Entry<String, String> entry : currIds.entrySet()) {
            if(prevIds.containsKey(entry.getKey())){
                if( prevIds.get(entry.getKey()).compareTo(currIds.get(entry.getKey())) ==0 )
                    resultIDs.add(entry.getKey());
            }
            ////System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
        }
        
        return resultIDs;
        
    }
    
    private Document getDocumentFromInputStream (InputStream is){
        Document doc = null;
        StringWriter writer = new StringWriter();
        try {
            IOUtils.copy(is, writer, "UTF-8");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String iString = writer.toString();
        doc = Jsoup.parse(iString, "", Parser.xmlParser());
        return doc;
    }

    private HashMap<String,String> getActivitiesIds(Document doc){
        HashMap<String, String> result = new HashMap<String,String>();
        Elements elementsByTag = doc.getElementsByTag("iati-activity");
        for (Iterator iterator = elementsByTag.iterator(); iterator.hasNext();) {
            Element act = (Element) iterator.next();
            String actMD5       = DataExchangeUtils.getMD5(act.toString());
            Elements ids = act.getElementsByTag("iati-identifier");
            for (Iterator iterator2 = ids.iterator(); iterator2.hasNext();) {
                Element id = (Element) iterator2.next();
                ////System.out.println(id.text());
                result.put(id.text(), actMD5);
            }
        }
        return result;
    }
    
    private void fillActivityWithContactInfo(List<ContactType> contacts,AmpActivity activity, String contactType) {
        if (contacts!= null && contacts.size() > 0){
            for (ContactType contact : contacts) {
                AmpContact cont = new AmpContact(contact.getFirstName(), contact.getLastName());
                List<String> emails = cont.getEmails();
                if (emails != null) {
                    for (String email : emails) {
                        AmpContactProperty prop = new AmpContactProperty();
                        prop.setName(org.digijava.module.aim.helper.Constants.CONTACT_PROPERTY_NAME_EMAIL);
                        prop.setValue(email);
                        if (cont.getProperties() == null) {
                            cont.setProperties(new HashSet<AmpContactProperty>());
                        }
                        cont.getProperties().add(prop);
                    }
                }
                AmpActivityContact actContant = new AmpActivityContact();
                actContant.setContactType(contactType);
                actContant.setContact(cont);
        //      actContant.setPrimaryContact(contact.isPrimary());
                if (activity.getActivityContacts() == null){
                    activity.setActivityContacts(new HashSet<AmpActivityContact>()) ;
                }
                activity.getActivityContacts().add(actContant);
                
            }           

        }
    }

    public ArrayList<String> getChangedIDs() {
        return changedIDs;
    }

    public void setChangedIDs(ArrayList<String> changedIDs) {
        this.changedIDs = changedIDs;
    }
    
    public String getDefaultLanguage() {
        return this.defaultLanguage;
    }
    
    public void setDefaultLanguage(String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }
}
