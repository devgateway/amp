/**
 * 
 */
package org.digijava.module.dataExchange.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityInternalId;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpActivityProgram;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpActor;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpComponentFunding;
import org.digijava.module.aim.dbentity.AmpComponentType;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpFundingMTEFProjection;
import org.digijava.module.aim.dbentity.AmpIssues;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpMeasure;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpPhysicalPerformance;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.helper.Components;
import org.digijava.module.aim.util.ComponentsUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.dataExchange.action.ImportValidationEventHandler;
import org.digijava.module.dataExchange.dbentity.DESourceSetting;
import org.digijava.module.dataExchange.jaxb.ActivityType;
import org.digijava.module.dataExchange.jaxb.AdditionalFieldType;
import org.digijava.module.dataExchange.jaxb.CodeValueType;
import org.digijava.module.dataExchange.jaxb.ComponentFundingType;
import org.digijava.module.dataExchange.jaxb.ContactType;
import org.digijava.module.dataExchange.jaxb.FreeTextType;
import org.digijava.module.dataExchange.jaxb.FundingDetailType;
import org.digijava.module.dataExchange.jaxb.FundingType;
import org.digijava.module.dataExchange.jaxb.PercentageCodeValueType;
import org.digijava.module.dataExchange.jaxb.ActivityType.Component;
import org.digijava.module.dataExchange.jaxb.ActivityType.Id;
import org.digijava.module.dataExchange.jaxb.ActivityType.Issues;
import org.digijava.module.dataExchange.jaxb.ActivityType.Location;
import org.digijava.module.dataExchange.jaxb.ActivityType.RelatedOrgs;
import org.digijava.module.dataExchange.jaxb.ActivityType.Issues.Measure;
import org.digijava.module.dataExchange.jaxb.FundingType.Projections;
import org.digijava.module.dataExchange.pojo.DEImportItem;
import org.digijava.module.dataExchange.utils.Constants;
import org.digijava.module.dataExchange.utils.DataExchangeUtils;
import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.editor.exception.EditorException;
import org.xml.sax.SAXException;

/**
 * @author dan
 *
 */
public class DEImportBuilder {
	
	private static Logger logger = Logger.getLogger(DEImportBuilder.class);
	private DEImportItem ampImportItem;
	private HashMap<String,Boolean> hashFields;

	public HashMap<String, Boolean> getHashFields() {
		return hashFields;
	}

	public void setHashFields(HashMap<String, Boolean> hashFields) {
		this.hashFields = hashFields;
	}

	public DEImportBuilder() {
		// TODO Auto-generated constructor stub
	}
	
	public DEImportBuilder(DEImportItem ampImportItem) {
		super();
		this.ampImportItem = ampImportItem;
	}

	//fill a new and empty activity based on field selection
	private void insertActivity(AmpActivity activity, ActivityType actType, Boolean update ){
		//update = false for new activity
		processPreStep(activity, update);
		processStep1(activity, actType, update);
		processStep2(activity, actType, update);
		processStep3(activity, actType, update);
		processStep4(activity, actType, update);
		processStep5(activity, actType, update);
		processStep6(activity, actType, update);
		processStep7(activity, actType, update);
		processAdditionalFields(activity, actType, update);
		//DataExchangeUtils.saveComponents(activity, request, tempComps);
		//TODO logger
		saveActivity(activity, update);
		
	}
	
	private AmpActivity loadAmpActivity(Boolean update){
		AmpActivity activity = null;
		if(!update) activity = new AmpActivity();
		else activity = getAmpActivityByComposedKey(getDESourceSetting().getUniqueIdentifier(), getDESourceSetting().getUniqueIdentifierSeparator());
		return activity;
	}
	
	
	private AmpActivity getAmpActivityByComposedKey(String key, String separator) {
		// TODO Auto-generated method stub
		AmpActivity activity = null;
		//activity = ;
		return activity;
	}

	private void updateActivity(AmpActivity activity, ActivityType actType, Boolean update ){
		//update = true for update activity
		processPreStep(activity, update);
		processStep1(activity, actType, update);
		processStep2(activity, actType, update);
		processStep3(activity, actType, update);
		processStep4(activity, actType, update);
		processStep5(activity, actType, update);
		processStep6(activity, actType, update);
		processStep7(activity, actType, update);
		processAdditionalFields(activity, actType, update);
		//DataExchangeUtils.saveComponents(activity, request, tempComps);
		//TODO logger
		saveActivity(activity, update);
		
	}

	private void saveActivity(AmpActivity activity, Boolean update) {
		// TODO Auto-generated method stub
		
		if(update == false) DataExchangeUtils.saveActivityNoLogger(activity);
		else DataExchangeUtils.updateActivityNoLogger(activity);
	}

	private void processPreStep(AmpActivity activity, Boolean update) {
		// TODO Auto-generated method stub
		//set the amp team
		//if no workspace was selected the activity will be unassigned
		//if activity exist we keep the existing settings
		if(update == false){
			AmpTeam team = getImportedWorkspace();
			activity.setTeam(team);
			
			//all the activities are not saved as draft
			activity.setCreatedAsDraft(false);
		}
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
			processFunding(activity,actType);
		if(isFieldSelected("activity.location"))
			processLocation(activity,actType);

	}

	
	//process step 4 
	//related organizations
	private void processStep4(AmpActivity activity, ActivityType actType, Boolean update) {
		// TODO Auto-generated method stub
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
	private void processStep6(AmpActivity activity, ActivityType actType, Boolean update) {
		// TODO Auto-generated method stub
		if(isFieldSelected("activity.documents"))
			processDocuments(activity, actType);

		if(isFieldSelected("activity.relatedLinks"))
			processRelatedLinks(activity, actType);
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
				if( isEqualStringsNWS(aft.getField(), "onBudget") ){
					activity.setBudget(new Integer(1));
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
		if(actType.getTitle()!=null)
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
			if( found==false && !titlesList.isEmpty()){
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
		if(actType.getDescription()!=null){
			activity.setDescription(setEditorFreeTextType(actType.getDescription(), activity, "aim-desc-00-"));
		}
	}

	//objective
	private void processObjective(AmpActivity activity, ActivityType actType) {
		// TODO Auto-generated method stub
		if(actType.getObjective()!=null){
			activity.setObjective(setEditorFreeTextType(actType.getObjective(), activity, "aim-obj-00-"));
		}
	}

	
	private void processActivityAssigningOrg(AmpActivity activity, ActivityType actType) {
		// TODO Auto-generated method stub
		if(actType.getId() != null){
			Set internalIds = new HashSet();
			ArrayList<Id> ids = (ArrayList<Id>) actType.getId();
			for (Iterator it = ids.iterator(); it.hasNext();) {
				Id id = (Id) it.next();
				AmpActivityInternalId actInternalId = new AmpActivityInternalId();
				if(isValidString(id.getUniqID()))
					actInternalId.setInternalId(id.getUniqID());
				actInternalId.setAmpActivity(activity);
				AmpOrganisation org = (AmpOrganisation) getAmpObject(Constants.AMP_ORGANIZATION,id.getAssigningOrg());
				if(org != null)
					actInternalId.setOrganisation(org);
				internalIds.add(actInternalId);
			}
			//if(activity.getInternalIds() == null || activity.getInternalIds().size() == 0)
			activity.setInternalIds(new HashSet());
			activity.getInternalIds().addAll(internalIds);
		}
	}

	//proposed Approval date
	private void processProposedApprovalDate(AmpActivity activity, ActivityType actType){
	  if(actType.getProposedApprovalDate()!=null){
		  Date date=DataExchangeUtils.XMLGregorianDateToDate(actType.getProposedApprovalDate().getDate());
		  if(date!=null)
			  activity.setProposedApprovalDate(date);
	  }
	}

	//closing date
	private void processClosingDate(AmpActivity activity, ActivityType actType) {
	  if(actType.getClosingDate()!=null){
		  Date date=DataExchangeUtils.XMLGregorianDateToDate(actType.getClosingDate().getDate());
		  if(date!=null)
			  activity.setProposedCompletionDate(date);
	  }
	}

	//modified closing date
	private void processModifiedClosingDate(AmpActivity activity, ActivityType actType) {
	  if(actType.getModifiedClosingDate()!=null){
		  Date date=DataExchangeUtils.XMLGregorianDateToDate(actType.getModifiedClosingDate().getDate());
		  if(date!=null)
			  activity.setActualCompletionDate(date);
	  }
	}

	  
	//actual start date
	private void processActualStartDate(AmpActivity activity, ActivityType actType) {
	  if(actType.getActualStartDate()!=null){
		  Date date=DataExchangeUtils.XMLGregorianDateToDate(actType.getActualStartDate().getDate());
		  if(date!=null)
			  activity.setActualStartDate(date);
	  }
	}

	//proposed start date
	private void processProposedStartDate(AmpActivity activity, ActivityType actType) {
	  if(actType.getProposedStartDate()!=null){
		  Date date=DataExchangeUtils.XMLGregorianDateToDate(actType.getProposedStartDate().getDate());
		  if(date!=null)
			  activity.setProposedStartDate(date);
	  } 
	}

	//actual Approval date
	private void processActualApprovalDate(AmpActivity activity, ActivityType actType) {
	  if(actType.getActualApprovalDate()!=null){
		  Date date=DataExchangeUtils.XMLGregorianDateToDate(actType.getActualApprovalDate().getDate());
		  if(date!=null)
			  activity.setActualApprovalDate(date);
	  }
	}

	//activity status
	private void processStatus(AmpActivity activity, ActivityType actType, Boolean update) {
		// TODO Auto-generated method stub
		if( actType.getStatus()!=null ){
			if(update == true){
				for (Iterator it = activity.getCategories().iterator(); it.hasNext();) {
					AmpCategoryValue acv = (AmpCategoryValue) it.next();
					if(Constants.CATEG_VALUE_ACTIVITY_STATUS.equals(acv.getAmpCategoryClass().getKeyName()))
						// is this working?
						activity.getCategories().remove(acv);
				}
			}
			AmpCategoryValue acv = getAmpCategoryValueFromCVT(actType.getStatus(), Constants.CATEG_VALUE_ACTIVITY_STATUS);
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

			for (Iterator iterator = actType.getSectors().iterator(); iterator.hasNext();) {
				PercentageCodeValueType idmlSector = (PercentageCodeValueType) iterator.next();
				CodeValueType sectorAux = new CodeValueType();
				sectorAux.setCode(idmlSector.getCode());
				
				String sectorValue = "";
				
				sectorAux.setValue(idmlSector.getValue());
				sectorValue= idmlSector.getValue();
				//SENEGAL changes
//			    sectorValue= idmlSector.getCode()+". "+idmlSector.getValue();
//			    sectorAux.setValue(sectorValue);
			    
			    //get sector by name and code 
				AmpSector ampSector = (AmpSector) getAmpObject(Constants.AMP_SECTOR,sectorAux);
				
				//this can not happen. if the sector is null it should throw an exception
				if(ampSector == null || ampSector.getAmpSectorId() == null) continue;
				
				AmpActivitySector ampActSector = new AmpActivitySector();
				ampActSector.setActivityId(activity);
				sectorId = ampSector.getAmpSectorId();
				if (sectorId != null && (!sectorId.equals(new Long(-1))))
					ampActSector.setSectorId(ampSector);
				ampActSector.setSectorPercentage(new Float(idmlSector.getPercentage()));
				
				AmpClassificationConfiguration primConf = null;
				ArrayList<AmpClassificationConfiguration> allClassifConfigs = null;
				//trying to find the classification
				primConf = getConfiguration(ampSector,allClassifConfigs);
				//if the classification doesn't exist we will put the sector under the primary classification!
				if(primConf == null)	primConf = getPrimaryClassificationConfiguration(allClassifConfigs);
				
				ampActSector.setClassificationConfig(primConf);
                sectors.add(ampActSector);
			}
			//if (activity.getSectors() == null) {
			activity.setSectors(new HashSet());
			//}
			activity.getSectors().addAll(sectors);
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
				
				AmpTheme ampTheme = (AmpTheme) getAmpObject(Constants.AMP_PROGRAM,programAux);
				
				if(ampTheme == null || ampTheme.getAmpThemeId() == null) continue;
				
				AmpActivityProgram ampActivityProgram = new AmpActivityProgram();
				ampActivityProgram.setActivity(activity);
				ampActivityProgram.setProgramPercentage((new Float(idmlProgram.getPercentage())).longValue());
				ampActivityProgram.setProgram(ampTheme);
				
				ArrayList<AmpActivityProgramSettings> allClassifConfigs = (ArrayList<AmpActivityProgramSettings>) getAllAmpActivityProgramSettings();
				AmpActivityProgramSettings primConf = null;
				
				primConf = getAmpActivityProgramSettings(allClassifConfigs, ampTheme);
				if(primConf == null)
					primConf = getNationalPlanObjectiveSetting(allClassifConfigs);

				ampActivityProgram.setProgramSetting(primConf);

                programs.add(ampActivityProgram);
			}
			
			//if (activity.getActPrograms() == null) {
			activity.setActPrograms(new HashSet());
			//}
			activity.getActPrograms().addAll(programs);
		}

	}

/*
 ******************************* step 3 process methods 
 */
	
	//fundings
	private void processFunding(AmpActivity activity, ActivityType actType) {
		if(actType.getFunding() != null && actType.getFunding().size() > 0){
			ArrayList fundings = null;
			fundings = (ArrayList) getFundingXMLtoAMP(activity, actType);
			//if(activity.getFunding() == null) 
			activity.setFunding(new HashSet());
			activity.getFunding().addAll(fundings);
		}
	}
	
	
	private void processLocation(AmpActivity activity, ActivityType actType) {
		// TODO Auto-generated method stub
	if(actType.getLocation() !=null && actType.getLocation().size() >0)	{
		
		Set<AmpActivityLocation> locations = new HashSet<AmpActivityLocation>();
		for (Iterator it = actType.getLocation().iterator(); it.hasNext();) {
			Location location = (Location) it.next();
			
			AmpCategoryValueLocations ampCVLoc		= null;
			AmpCategoryValue acv= null;
			CodeValueType cvt = new CodeValueType();
			boolean isCountry = false;
			boolean isZone = false;
			boolean isDistrict = false;
			if("001".equals(location.getLocationName().getCode()) || "0000".equals(location.getLocationName().getCode()) ){
				ampCVLoc = DynLocationManagerUtil.getLocationByCode("87274", (AmpCategoryValue)null );

				cvt.setCode("001");
				cvt.setValue("Country");
				acv = getAmpCategoryValueFromCVT(cvt, Constants.CATEG_VALUE_IMPLEMENTATION_LOCATION);
				isCountry = true;
			}
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
					acv = getAmpCategoryValueFromCVT(cvt, Constants.CATEG_VALUE_IMPLEMENTATION_LOCATION);
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
					acv1 = getAmpCategoryValueFromCVT(cvt1, Constants.CATEG_VALUE_IMPLEMENTATION_LEVEL);
				}
				else
					acv1 = getAmpCategoryValueFromCVT(actType.getImplementationLevels(), Constants.CATEG_VALUE_IMPLEMENTATION_LEVEL);
				if(acv1!=null)
					activity.getCategories().add(acv1);
				
			}
			
			AmpLocation ampLoc = null;
			try {
				ampLoc = DynLocationManagerUtil.getAmpLocation(ampCVLoc);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			AmpActivityLocation actLoc	=	new AmpActivityLocation();
			actLoc.setActivity(activity);
			actLoc.getActivity().setAmpActivityId(null);
			actLoc.setLocation(ampLoc);
			Double percent=new Double(100);
            actLoc.setLocationPercentage(percent.floatValue());
			locations.add(actLoc);

			if(acv!=null)
				activity.getCategories().add(acv);
			
			
		}
		activity.setLocations(new HashSet<AmpActivityLocation>());
		activity.getLocations().addAll(locations);
	}

	}
	
/*
 * ************************ step 4 process methods
 * 	
 */
	
	private void processRelatedOrgs(AmpActivity activity, ActivityType actType) {
		// TODO Auto-generated method stub
		ArrayList<RelatedOrgs> relatedOrgs = (ArrayList<RelatedOrgs>)actType.getRelatedOrgs();
		Set orgRole = new HashSet();
		for (Iterator it = relatedOrgs.iterator(); it.hasNext();) {
			RelatedOrgs relOrg = (RelatedOrgs) it.next();
			String type=null;
			if(isValidString(relOrg.getType()) )
				{
					type=relOrg.getType();
					AmpRole role = null;
					if(isEqualStringsNWS(type, Constants.IDML_BENEFICIARY_AGENCY))
						role=DbUtil.getAmpRole(org.digijava.module.aim.helper.Constants.BENEFICIARY_AGENCY);
					if(isEqualStringsNWS(type, Constants.IDML_CONTRACTING_AGENCY))
						role=DbUtil.getAmpRole(org.digijava.module.aim.helper.Constants.CONTRACTING_AGENCY);
					if(isEqualStringsNWS(type, Constants.IDML_CONTRACTOR))
						role=DbUtil.getAmpRole(org.digijava.module.aim.helper.Constants.CONTRACTOR);
					if(isEqualStringsNWS(type, Constants.IDML_EXECUTING_AGENCY))
						role=DbUtil.getAmpRole(org.digijava.module.aim.helper.Constants.EXECUTING_AGENCY);
					if(isEqualStringsNWS(type, Constants.IDML_FUNDING_AGENCY))
						role=DbUtil.getAmpRole(org.digijava.module.aim.helper.Constants.FUNDING_AGENCY);
					if(isEqualStringsNWS(type, Constants.IDML_IMPLEMENTING_AGENCY))
						role=DbUtil.getAmpRole(org.digijava.module.aim.helper.Constants.IMPLEMENTING_AGENCY);
					if(isEqualStringsNWS(type, Constants.IDML_REGIONAL_GROUP))
						role=DbUtil.getAmpRole(org.digijava.module.aim.helper.Constants.REGIONAL_GROUP);
					if(isEqualStringsNWS(type, Constants.IDML_RELATED_INSTITUTIONS))
						role=DbUtil.getAmpRole(org.digijava.module.aim.helper.Constants.RELATED_INSTITUTIONS);
					if(isEqualStringsNWS(type, Constants.IDML_REPORTING_AGENCY))
						role=DbUtil.getAmpRole(org.digijava.module.aim.helper.Constants.REPORTING_AGENCY);
					if(isEqualStringsNWS(type, Constants.IDML_RESPONSIBLE_ORGANIZATION))
						{
							role=DbUtil.getAmpRole(org.digijava.module.aim.helper.Constants.RESPONSIBLE_ORGANISATION);
							//senegal add ????
						//	if(isValidString(relOrg.getCode()))
						//	activity.setFY(relOrg.getCode());
						}
					if(isEqualStringsNWS(type, Constants.IDML_SECTOR_GROUP))
						role=DbUtil.getAmpRole(org.digijava.module.aim.helper.Constants.SECTOR_GROUP);
					if(role !=null){
						CodeValueType cvt= new CodeValueType();
						if( isValidString(relOrg.getCode()) )
							cvt.setCode(relOrg.getCode());
						if( isValidString(relOrg.getValue()) )
							cvt.setValue(relOrg.getValue());
						AmpOrganisation org = (AmpOrganisation) getAmpObject(Constants.AMP_ORGANIZATION, cvt);
						AmpOrgRole ampOrgRole = new AmpOrgRole();
						ampOrgRole.setActivity(activity);
						ampOrgRole.setRole(role);
						ampOrgRole.setOrganisation(org);
						orgRole.add(ampOrgRole);
					}
				}
			//if (activity.getOrgrole() == null || activity.getOrgrole().size() == 0 ){
			activity.setOrgrole(new HashSet());
			//}
			activity.getOrgrole().addAll(orgRole);
		}
	}
	
/*
 * ********************** step 5 process methods
 */

	//components
	private void processComponent(AmpActivity activity, ActivityType actType) {
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
							acf.setActivity(activity);
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
							acf.setActivity(activity);
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
							acf.setActivity(activity);
							addFundingDetailToAmpCompFund(acf,fundingDetailType, org.digijava.module.aim.helper.Constants.EXPENDITURE);
							//acfs.add(acf);
							temp2.add(acf);
						}
						tempComp.setExpenditures(temp2);
						
						//cft.get
					}
					Set phyProgess = new HashSet();
					for (Iterator itComm = component.getPhysicalProgress().iterator(); itComm.hasNext();) {
						org.digijava.module.dataExchange.jaxb.ActivityType.Component.PhysicalProgress pp = (org.digijava.module.dataExchange.jaxb.ActivityType.Component.PhysicalProgress) itComm.next();
						AmpPhysicalPerformance ampPhyPerf = new AmpPhysicalPerformance();
						if(pp.getDescription()!=null && isValidString(pp.getDescription().getValue()))
							ampPhyPerf.setDescription(pp.getDescription().getValue());
						ampPhyPerf.setReportingDate(DataExchangeUtils.XMLGregorianDateToDate(pp.getReportingDate().getDate()));
						
						if(pp.getTitle()!=null && isValidString(pp.getTitle().getValue()))
							ampPhyPerf.setTitle(pp.getTitle().getValue());
						ampPhyPerf.setAmpActivityId(activity);
						ampPhyPerf.setComponent(ampComp);
						ampPhyPerf.setComments(" ");
						phyProgess.add(ampPhyPerf);
					}
					tempComp.setPhyProgress(phyProgess);
					//addCollectionToAmp(acfs);
			
				tempComps.add(tempComp);
			}
		}	
	}
	
/*
 * ****************** step 6 process methods	
 */
	//documents
	private void processDocuments(AmpActivity activity, ActivityType actType) {
		// TODO Auto-generated method stub
		
	}

	//related links
	private void processRelatedLinks(AmpActivity activity, ActivityType actType) {
		// TODO Auto-generated method stub
		
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
				activity.setEmail(contacts.getEmail());
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
				activity.setMofedCntEmail(contacts.getEmail());
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
    	List<AmpClassificationConfiguration> configs = null;
		try {
			configs = SectorUtil.getAllClassificationConfigs();
		} catch (DgException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return configs;
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
        if (parent == null)	return child;
        else 	return searchAmpProgram(child.getParentThemeId());
		
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
		
		if( isValidString(element.getValue()) ) 	valueToCateg=element.getValue();
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
				Editor ed = createEditor("amp", key, obj.getLang());
				ed.setLastModDate(new Date());
				ed.setGroupName(org.digijava.module.editor.util.Constants.GROUP_OTHER);
				ed.setBody(obj.getValue());
				try {
					org.digijava.module.editor.util.DbUtil.saveEditor(ed);
				} catch (EditorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return key;//activity.setDescription(key);
			}
			
		}
		return null;
	}
	
	private Editor createEditor(String siteId, String key, String language){
		Editor editor = new Editor();
		editor.setSiteId(siteId);
		editor.setEditorKey(key);
		editor.setLanguage(language);
		return editor;
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
	
	private AmpTeam getImportedWorkspace (){
		return getDESourceSetting().getImportWorkspace();
	}
	
	private String getApprovalStatus(){
		return this.getDESourceSetting().getApprovalStatus();
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
	
	private Collection getFundingXMLtoAMP(AmpActivity activity, ActivityType actType){
		ArrayList<AmpFunding> fundings= null;
		Set orgRole = new HashSet();
		for (Iterator<FundingType> it = actType.getFunding().iterator(); it.hasNext();) {
			AmpRole role = DbUtil.getAmpRole(org.digijava.module.aim.helper.Constants.FUNDING_AGENCY);
			FundingType funding = (FundingType) it.next();
			CodeValueType fundingOrg=funding.getFundingOrg();
			AmpFunding ampFunding = new AmpFunding();
			ampFunding.setActive(true);
			AmpOrganisation ampOrg = (AmpOrganisation) getAmpObject(Constants.AMP_ORGANIZATION,fundingOrg);
			Set<AmpFundingDetail> fundDetails = new HashSet<AmpFundingDetail>();

			ampFunding.setAmpDonorOrgId(ampOrg);
			ampFunding.setFundingDetails(new HashSet<AmpFundingDetail>());

			
			addMTEFProjectionsToSet(funding.getProjections(),ampFunding);
			addFundingDetailsToSet(funding.getCommitments(), fundDetails, org.digijava.module.aim.helper.Constants.COMMITMENT);
			addFundingDetailsToSet(funding.getDisbursements(), fundDetails, org.digijava.module.aim.helper.Constants.DISBURSEMENT);
			addFundingDetailsToSet(funding.getExpenditures(), fundDetails, org.digijava.module.aim.helper.Constants.EXPENDITURE);
			
			if(ampFunding.getFundingDetails() == null ) ampFunding.setFundingDetails(new HashSet<AmpFundingDetail>());
			if(fundDetails != null) ampFunding.getFundingDetails().addAll(fundDetails);
			if(funding.getAssistanceType() != null){
				AmpCategoryValue acv = getAmpCategoryValueFromCVT(funding.getAssistanceType(), Constants.CATEG_VALUE_TYPE_OF_ASSISTANCE);
				ampFunding.setTypeOfAssistance(acv);
			}
			if(funding.getFinancingInstrument() != null){
				AmpCategoryValue acv = getAmpCategoryValueFromCVT(funding.getFinancingInstrument(), Constants.CATEG_VALUE_FINANCING_INSTRUMENT);
				ampFunding.setFinancingInstrument(acv);
			}
			if(activity !=null ) ampFunding.setAmpActivityId(activity);
			if(activity.getFunding() ==  null) activity.setFunding(new HashSet<AmpFunding>());
			
			if(fundings == null) fundings=new ArrayList<AmpFunding>();
			
			//conditions
			//TODO: the language - lang attribute
			if(funding.getConditions() != null) ampFunding.setConditions(funding.getConditions().getValue());
			fundings.add(ampFunding);

			AmpOrgRole ampOrgRole = new AmpOrgRole();
			ampOrgRole.setActivity(activity);
			ampOrgRole.setRole(role);
			ampOrgRole.setOrganisation(ampOrg);
			orgRole.add(ampOrgRole);
		}
		
		if (activity.getOrgrole() == null || activity.getOrgrole().size() == 0 ){
			activity.setOrgrole(orgRole);
		}
		else activity.getOrgrole().addAll(orgRole);
		
		return fundings;
	}

	private void addMTEFProjectionsToSet(List<Projections> projections,	AmpFunding ampFunding) {
		// TODO Auto-generated method stub
		if(ampFunding.getMtefProjections() == null) ampFunding.setMtefProjections(new HashSet<AmpFundingMTEFProjection> ());
		if(projections!=null)
		{
			Iterator mtefItr=projections.iterator();
			while (mtefItr.hasNext())
			{
				Projections mtef=(Projections)mtefItr.next();
				//senegal add
				if(mtef.getAmount() == 0 ) continue;

				AmpFundingMTEFProjection ampmtef=new AmpFundingMTEFProjection();
				
				//if("true".equals(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS)))
					//ampmtef.setAmount(new Double(mtef.getAmount()*1000));
				//else
				ampmtef.setAmount(new Double(mtef.getAmount()));

				ampmtef.setAmpFunding(ampFunding);
				ampmtef.setAmpCurrency(CurrencyUtil.getCurrencyByCode(mtef.getCurrency()));
				
				if( mtef.getType()!=null ){
					CodeValueType cvt = new CodeValueType();
					cvt.setCode(mtef.getType());
					cvt.setValue(mtef.getType());
					AmpCategoryValue acv = getAmpCategoryValueFromCVT(cvt, Constants.CATEG_VALUE_MTEF_PROJECTION);
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
			if(fundDet.getAmount()==0) continue;
			
			AmpFundingDetail ampFundDet = new AmpFundingDetail();
	
			ampFundDet.setTransactionType(new Integer(transactionType));
			ampFundDet.setTransactionDate(DataExchangeUtils.XMLGregorianDateToDate(fundDet.getDate()));
			if( Constants.IDML_PLAN.equals(fundDet.getType()) ) 
				ampFundDet.setAdjustmentType(new Integer(org.digijava.module.aim.helper.Constants.PLANNED));
			if( Constants.IDML_ACTUAL.equals(fundDet.getType()) ) 
				ampFundDet.setAdjustmentType(new Integer(org.digijava.module.aim.helper.Constants.ACTUAL));
			
			//TODO mapping the currencies!!! ??
			ampFundDet.setAmpCurrencyId(CurrencyUtil.getCurrencyByCode(fundDet.getCurrency()));
			
			//TODO how are the amounts? in thousands?
			//if("true".equals(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS)))
				//ampFundDet.setTransactionAmount(new Double(fundDet.getAmount()*1000));
			//else 
			ampFundDet.setTransactionAmount(new Double(fundDet.getAmount()));
			fundDetails.add(ampFundDet);
		}
		
	}
	
	//check if two strings are the equals without white spaces
	private boolean isEqualStringsNWS(String s, String t){
		return (s.toLowerCase().trim().equals(t.toLowerCase().trim()));
	}
	
	//funding details for components
	private void addFundingDetailToAmpCompFund(AmpComponentFunding acf,	FundingDetailType fundingDetailType, int transactionType) {
		// TODO Auto-generated method stub
		
		acf.setTransactionType(new Integer(transactionType));
		
		acf.setTransactionDate(DataExchangeUtils.XMLGregorianDateToDate(fundingDetailType.getDate()));
		
		if( Constants.IDML_PLAN.equals(fundingDetailType.getType()) ) 
			acf.setAdjustmentType(new Integer(org.digijava.module.aim.helper.Constants.PLANNED));
		if( Constants.IDML_ACTUAL.equals(fundingDetailType.getType()) ) 
			acf.setAdjustmentType(new Integer(org.digijava.module.aim.helper.Constants.ACTUAL));
		
		//TODO mapping the currencies!!! ??
		acf.setCurrency(CurrencyUtil.getCurrencyByCode(fundingDetailType.getCurrency()));
		
		//if("true".equals(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS)))
			//acf.setTransactionAmount(new Double(fundingDetailType.getAmount()*1000));
		//else 
		acf.setTransactionAmount(new Double(fundingDetailType.getAmount()));
		
	}
	
	//generating the HashMap with the selected fields from UI
	public void generateFieldHashMap(){
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
	
	public void checkInputString(){
		//Activities acts;
		try {
			JAXBContext jc = JAXBContext.newInstance(Constants.JAXB_INSTANCE);
	        Unmarshaller m = jc.createUnmarshaller();
	        boolean xsdValidate = true;
	        	
	        	if(xsdValidate){
	                // create a SchemaFactory that conforms to W3C XML Schema
	                 SchemaFactory sf = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);

	                 // parse the purchase order schema
	                 Schema schema = sf.newSchema(new File(Constants.IDML_SCHEMA_LOCATION));

	                 m.setSchema(schema);
	                 // set your error handler to catch errors during schema construction
	                 // we can use custom validation event handler
	                 m.setEventHandler(new ImportValidationEventHandler());
	           }
	        	this.getAmpImportItem().setActivities( (org.digijava.module.dataExchange.jaxb.Activities) m.unmarshal(this.ampImportItem.getInputStream()) );
	        	
	        } 
			catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
	        catch (javax.xml.bind.JAXBException jex) {
	        	jex.printStackTrace();
	        	
	        }

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
	
}
