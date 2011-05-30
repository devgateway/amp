/**
 * 
 */
package org.digijava.module.dataExchange.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.jcr.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.axis.utils.ByteArrayOutputStream;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessages;
import org.dgfoundation.amp.error.AMPError;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityDocument;
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
import org.digijava.module.aim.helper.ActivityDocumentsConstants;
import org.digijava.module.aim.helper.Components;
import org.digijava.module.aim.helper.PhysicalProgress;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.ComponentsUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.helper.TemporaryDocumentData;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;
import org.digijava.module.dataExchange.action.ImportValidationEventHandler;
import org.digijava.module.dataExchange.dbentity.AmpDEImportLog;
import org.digijava.module.dataExchange.dbentity.DEMappingFields;
import org.digijava.module.dataExchange.jaxb.Activities;
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
import org.digijava.module.dataExchange.jaxb.ActivityType.Documents;
import org.digijava.module.dataExchange.jaxb.ActivityType.Id;
import org.digijava.module.dataExchange.jaxb.ActivityType.Issues;
import org.digijava.module.dataExchange.jaxb.ActivityType.Location;
import org.digijava.module.dataExchange.jaxb.ActivityType.RelatedLinks;
import org.digijava.module.dataExchange.jaxb.ActivityType.RelatedOrgs;
import org.digijava.module.dataExchange.jaxb.ActivityType.Issues.Measure;
import org.digijava.module.dataExchange.jaxb.FundingType.Projections;
import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.editor.exception.EditorException;
import org.springframework.util.FileCopyUtils;
import org.xml.sax.SAXException;

/**
 * @author dan
 *
 */
public class ImportBuilder {
	private AmpActivity activity = null;
	
	private Activities activities = null;
	
	private static Logger logger = Logger.getLogger(ImportBuilder.class);
	
	private HashMap hm = null;
	
	private Collection<AmpDEImportLog> importLogs = null;
	private Collection<String> activityList = null; 
	private AmpDEImportLog root = null;
	
	public ImportBuilder(){
		//activity = new AmpActivity();
		//request = null;
		importLogs = new ArrayList<AmpDEImportLog>();
		activityList =  new ArrayList<String>();
	}

	
	
	

	private void processPreStep(AmpActivity activity, TeamMember tm) throws Exception, AMPError{
		if (tm != null && tm.getTeamId()!=null && tm.getTeamId() != 0L) {
			AmpTeam team = TeamUtil.getAmpTeam(tm.getTeamId());
			activity.setTeam(team);
		} else {
			activity.setTeam(null);
		}

		if (activity.getCategories() == null) {
			activity.setCategories( new HashSet() );
		}
		
		//activity.setCreatedAsDraft(false);
		activity.setCreatedAsDraft(false);
	}
	
	//search for the title of the activity imported in db
	//if the title in any language already exists return true, else return false
	private boolean activityExists(ActivityType actType){
		ArrayList<FreeTextType> titlesList=(ArrayList<FreeTextType>) actType.getTitle();
		for (Iterator iterator = titlesList.iterator(); iterator.hasNext();) {
			FreeTextType title = (FreeTextType) iterator.next();
			AmpActivity act = ActivityUtil.getActivityByName(title.getValue(),null);
			if (act != null) {
				logger.debug("Activity with the name " + actType + " already exist.");
				return true;
			}
		}
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
	
	private void processStep1( ActivityType actType, AmpActivity activity, HttpServletRequest request, String lang, HashMap hm) throws Exception{

		//title
		
		activity.setName("");
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
		
		//objectives
		if(actType.getObjective()!=null){
			activity.setObjective(setEditorFreeTextType(actType.getObjective(), activity, request, "aim-obj-00-"));
		}
		
		//description
		if(actType.getDescription()!=null){
			activity.setDescription(setEditorFreeTextType(actType.getDescription(), activity, request, "aim-desc-00-"));
		}
		
		//implementation levels
		//changes for Senegal : goto to step3
		/*
		if( actType.getImplementationLevels()!=null ){
			
			AmpCategoryValue acv=addCategValueForCodeValueType(actType.getImplementationLevels(), hm, Constants.IDML_IMPLEMENTATION_LEVELS, Constants.CATEG_VALUE_IMPLEMENTATION_LEVEL);
			if(acv!=null)
				activity.getCategories().add(acv);
			
		}
		*/
		//approval status Senegal change
		
		activity.setApprovalStatus(org.digijava.module.aim.helper.Constants.STARTED_STATUS);
		
		
		//assigning org
		
		if(actType.getId() != null){
			Set internalIds = new HashSet();
			ArrayList<Id> ids = (ArrayList<Id>) actType.getId();
			for (Iterator it = ids.iterator(); it.hasNext();) {
				Id id = (Id) it.next();
				AmpActivityInternalId actInternalId = new AmpActivityInternalId();
				if(isStringValid(id.getUniqID()))
					actInternalId.setInternalId(id.getUniqID());
				actInternalId.setAmpActivity(activity);
				AmpOrganisation org = (AmpOrganisation) mapCodeValueTypeElementInAmp(Constants.AMP_ORGANIZATION,id.getAssigningOrg(),hm);
				if(org != null)
					actInternalId.setOrganisation(org);
				internalIds.add(actInternalId);
			}
			if(activity.getInternalIds() == null || activity.getInternalIds().size() == 0)
				activity.setInternalIds(new HashSet());
			activity.setInternalIds(internalIds);
		}
		
	  //proposed Approval date
	  if(actType.getProposedApprovalDate()!=null){
		  Date date=DataExchangeUtils.XMLGregorianDateToDate(actType.getProposedApprovalDate().getDate());
		  if(date!=null)
			  activity.setProposedApprovalDate(date);
	  }
	  
	//actual Approval date
	  if(actType.getActualApprovalDate()!=null){
		  Date date=DataExchangeUtils.XMLGregorianDateToDate(actType.getActualApprovalDate().getDate());
		  if(date!=null)
			  activity.setActualApprovalDate(date);
	  }
	  
	//proposed start date
	  if(actType.getProposedStartDate()!=null){
		  Date date=DataExchangeUtils.XMLGregorianDateToDate(actType.getProposedStartDate().getDate());
		  if(date!=null)
			  activity.setProposedStartDate(date);
	  } 
	  
	//actual start date
	  if(actType.getActualStartDate()!=null){
		  Date date=DataExchangeUtils.XMLGregorianDateToDate(actType.getActualStartDate().getDate());
		  if(date!=null)
			  activity.setActualStartDate(date);
	  } 
	  
	  
	  //modified closing date
	  if(actType.getModifiedClosingDate()!=null){
		  
		  Date date=DataExchangeUtils.XMLGregorianDateToDate(actType.getModifiedClosingDate().getDate());
		  if(date!=null)
			  activity.setActualCompletionDate(date);
		  
//		  Date date=DataExchangeUtils.XMLGregorianDateToDate(actType.getModifiedClosingDate().getDate());
//		  if(date!=null)
//			  {
//			  	if(activity.getClosingDates()==null) 
//			  		activity.setClosingDates(new HashSet());
//			  	activity.getClosingDates().add(date);
//			  	
//			  }
	  }

	  //closing date
	  if(actType.getClosingDate()!=null){
		  
		  Date date=DataExchangeUtils.XMLGregorianDateToDate(actType.getClosingDate().getDate());
		  if(date!=null)
			  activity.setProposedCompletionDate(date);
	  }
	  
	  
		//status
		if( actType.getStatus()!=null ){
			AmpCategoryValue acv = addCategValueForCodeValueType(actType.getStatus(), hm, Constants.IDML_STATUS, Constants.CATEG_VALUE_ACTIVITY_STATUS);
			if(acv!=null)
				activity.getCategories().add(acv);
		}
	  
		//status reason
		if( actType.getStatusReason()!=null){
			activity.setStatusReason(actType.getStatusReason().getValue());
		}

	  
	}//end of step 1	

	
	//sectors, programs
	private void processStep2(ActivityType activityImported, AmpActivity activity, HttpServletRequest request, String string, HashMap hm) {

		
		//sectors
		Long sectorId;
		if(activityImported.getSectors()!=null && activityImported.getSectors().size() > 0){
			Set<AmpActivitySector> sectors = new HashSet<AmpActivitySector>();
			
			//checking the sum of the sector's percentage
			long percentage = 0;
			for (Iterator iterator = activityImported.getSectors().iterator(); iterator.hasNext();) {
				PercentageCodeValueType idmlSector = (PercentageCodeValueType) iterator.next();
				percentage+=idmlSector.getPercentage();
				
			}
			if(percentage != 100){
				//TODO 
				//logger.error("The sum of sectors percentage is not 100!!!!");
			}
			for (Iterator iterator = activityImported.getSectors().iterator(); iterator.hasNext();) {
				PercentageCodeValueType idmlSector = (PercentageCodeValueType) iterator.next();
				CodeValueType sectorAux = new CodeValueType();
				sectorAux.setCode(idmlSector.getCode());
				
				String sectorValue = "";
				
				//sectorAux.setValue(idmlSector.getValue());
				//sectorValue= idmlSector.getValue();
				//SENEGAL changes
			    sectorValue= idmlSector.getCode()+". "+idmlSector.getValue();
				
			    sectorAux.setValue(sectorValue);
				AmpSector ampSector = (AmpSector) mapCodeValueTypeElementInAmp(Constants.AMP_SECTOR,sectorAux,hm);
				
				if(ampSector == null || ampSector.getAmpSectorId() == null) continue;
				
				AmpActivitySector amps = new AmpActivitySector();
				amps.setActivityId(activity);
				sectorId = ampSector.getAmpSectorId();
				if (sectorId != null && (!sectorId.equals(new Long(-1))))
					amps.setSectorId(ampSector);
				amps.setSectorPercentage(new Float(idmlSector.getPercentage()));
				
				ArrayList<AmpClassificationConfiguration> allClassifConfigs = (ArrayList<AmpClassificationConfiguration>) getAllClassificationConfiguration();
				AmpClassificationConfiguration primConf = null;
				primConf = getPrimaryClassificationConfiguration();
				for (Iterator it2 = allClassifConfigs.iterator(); it2.hasNext();) {
					AmpClassificationConfiguration acc = (AmpClassificationConfiguration) it2.next();
					if(acc.getClassification().getAmpSecSchemeId().equals(ampSector.getAmpSecSchemeId().getAmpSecSchemeId()))
						{
							primConf=acc;
							break;
						}
				}
                amps.setClassificationConfig(primConf);
                sectors.add(amps);
			}
			if (activity.getSectors() == null) {
			      activity.setSectors(new HashSet());
			}
			activity.setSectors(sectors);
		}
		
		//programs
		
		

		if(activityImported.getPrograms()!=null && activityImported.getPrograms().size() > 0){
			Set<AmpActivityProgram> programs = new HashSet<AmpActivityProgram>();
			
			//checking the sum of the program's percentage
			long percentage = 0;
			for (Iterator iterator = activityImported.getPrograms().iterator(); iterator.hasNext();) {
				PercentageCodeValueType idmlProgram = (PercentageCodeValueType) iterator.next();
				percentage+=idmlProgram.getPercentage();
				
			}
			if(percentage != 100){
				//TODO 
				logger.error("The sum of programs' percentage is not 100!!!!");
			}
			for (Iterator iterator = activityImported.getPrograms().iterator(); iterator.hasNext();) {
				PercentageCodeValueType idmlProgram = (PercentageCodeValueType) iterator.next();
				CodeValueType programAux = new CodeValueType();
				programAux.setCode(idmlProgram.getCode());
				programAux.setValue(idmlProgram.getValue());
				AmpTheme ampTheme = (AmpTheme) mapCodeValueTypeElementInAmp(Constants.AMP_PROGRAM,programAux,hm);
				
				if(ampTheme == null || ampTheme.getAmpThemeId() == null) continue;
				
				AmpActivityProgram ampActivityProgram = new AmpActivityProgram();
				ampActivityProgram.setActivity(activity);
				ampActivityProgram.setProgramPercentage((new Float(idmlProgram.getPercentage())).longValue());
				ampActivityProgram.setProgram(ampTheme);
				
				//ampActivityProgram.setProgramSetting()
				
				ArrayList<AmpActivityProgramSettings> allClassifConfigs = (ArrayList<AmpActivityProgramSettings>) getAllAmpActivityProgramSettings();
				AmpActivityProgramSettings primConf = null;
				primConf = getNationalPlanObjectiveSetting();
				AmpTheme parent = searchAmpProgram(ampTheme);
				for (Iterator it2 = allClassifConfigs.iterator(); it2.hasNext();) {
					AmpActivityProgramSettings acc = (AmpActivityProgramSettings) it2.next();
					if(acc.getDefaultHierarchy().getAmpThemeId().equals(parent.getAmpThemeId())) {
						{
							primConf=acc;break;
						}
					}
					
				}
				ampActivityProgram.setProgramSetting(primConf);

                programs.add(ampActivityProgram);
			}
			if (activity.getActPrograms() == null) {
			      activity.setActPrograms(new HashSet());
			}
			activity.setActPrograms(programs);
		}

		
		
		
		
	}//end of step 2
	
	
	
	private AmpTheme searchAmpProgram(AmpTheme child) {
		
        AmpTheme parent = child.getParentThemeId();
        if (parent == null) {
                return child;
        }
        else {return searchAmpProgram(child.getParentThemeId()); }
		
	}

	//funding
	private void processStep3( ActivityType actType, AmpActivity activity, HttpServletRequest request, String lang, HashMap hm) throws Exception{
		
		//fundings
		if(actType.getFunding() != null && actType.getFunding().size() > 0){
			ArrayList fundings = null;
			fundings = (ArrayList) getFundingXMLtoAMP(actType, activity, hm);
			if(activity.getFunding() == null) activity.setFunding(new HashSet());
			activity.getFunding().addAll(fundings);
		}
		
		//regional funding - locations.fundings
		
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
					acv = addCategValueForCodeValueType(cvt, hm, Constants.IDML_IMPLEMENTATION_LOCATION, Constants.CATEG_VALUE_IMPLEMENTATION_LOCATION);
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
						acv = addCategValueForCodeValueType(cvt, hm, Constants.IDML_IMPLEMENTATION_LOCATION, Constants.CATEG_VALUE_IMPLEMENTATION_LOCATION);
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
						acv1 = addCategValueForCodeValueType(cvt1, hm, Constants.IDML_IMPLEMENTATION_LEVELS, Constants.CATEG_VALUE_IMPLEMENTATION_LEVEL);
					}
					else
						acv1 = addCategValueForCodeValueType(actType.getImplementationLevels(), hm, Constants.IDML_IMPLEMENTATION_LEVELS, Constants.CATEG_VALUE_IMPLEMENTATION_LEVEL);
					if(acv1!=null)
						activity.getCategories().add(acv1);
					
				}
				AmpLocation ampLoc			= 	DynLocationManagerUtil.getAmpLocation(ampCVLoc);
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
			activity.setLocations(locations);
		}
		
		
		
		
	}//end of step 3
	
	
	//related orgs, components
	
	private void processStep5(ActivityType activityImported, AmpActivity activity, HttpServletRequest request, String string, HashMap hm, Collection<Components<AmpComponentFunding>> tempComps) {
		// TODO Auto-generated method stub
		
		//related orgs
		ArrayList<RelatedOrgs> relatedOrgs = (ArrayList<RelatedOrgs>)activityImported.getRelatedOrgs();
		Set orgRole = new HashSet();
		for (Iterator it = relatedOrgs.iterator(); it.hasNext();) {
			RelatedOrgs relOrg = (RelatedOrgs) it.next();
			String type=null;
			if(isStringValid(relOrg.getType()) )
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
							//senegal add
							if(isStringValid(relOrg.getCode()))
								activity.setFY(relOrg.getCode());
						}
					if(isEqualStringsNWS(type, Constants.IDML_SECTOR_GROUP))
						role=DbUtil.getAmpRole(org.digijava.module.aim.helper.Constants.SECTOR_GROUP);
					if(role !=null){
						CodeValueType cvt= new CodeValueType();
						if( isStringValid(relOrg.getCode()) )
							cvt.setCode(relOrg.getCode());
						if( isStringValid(relOrg.getValue()) )
							cvt.setValue(relOrg.getValue());
						AmpOrganisation org = (AmpOrganisation) mapCodeValueTypeElementInAmp(Constants.AMP_ORGANIZATION,cvt,hm);
						AmpOrgRole ampOrgRole = new AmpOrgRole();
						ampOrgRole.setActivity(activity);
						ampOrgRole.setRole(role);
						ampOrgRole.setOrganisation(org);
						orgRole.add(ampOrgRole);
					}
				}
			if (activity.getOrgrole() == null || activity.getOrgrole().size() == 0 ){
				activity.setOrgrole(orgRole);
			}
			else activity.getOrgrole().addAll(orgRole);
		}
		
		
		
		
		
		//components
		ArrayList<Component> componentList = (ArrayList<Component>) activityImported.getComponent();
		//Collection<Components<AmpComponentFunding>> tempComps = new HashSet();
		if(tempComps == null ) tempComps= new HashSet();
		for (Iterator it = componentList.iterator(); it.hasNext();) {
			Component component = (Component) it.next();
			AmpComponent ampComp= new AmpComponent();
			
			ampComp.setTitle(component.getComponentName());
			Components<AmpComponentFunding> tempComp = new Components<AmpComponentFunding>();
			
			//TODO: ampComponentsType -> probably soon will be moved to category manager
			ArrayList ampCompTypes = (ArrayList) ComponentsUtil.getAmpComponentTypes();
			boolean found=false;
			for (Iterator ampCompTypesIt = ampCompTypes.iterator(); ampCompTypesIt.hasNext();) {
				AmpComponentType act = (AmpComponentType) ampCompTypesIt.next();
				if(act.getName() != null && "".compareTo(act.getName().trim()) !=0 
						&& act.getName().compareTo(component.getComponentType().getValue())==0){
					found =true;
					ampComp.setType(act);
				}
			}
			if(found){
				
					if(ampComp.getActivities() == null) ampComp.setActivities(new HashSet());
					ampComp = (AmpComponent)DataExchangeUtils.addObjectoToAmp(ampComp);
					
					//ampComp.getActivities().add(activity);
					if(activity.getComponents() == null || activity.getComponents().size() == 0)
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
						if(pp.getDescription()!=null && isStringValid(pp.getDescription().getValue()))
							ampPhyPerf.setDescription(pp.getDescription().getValue());
						ampPhyPerf.setReportingDate(DataExchangeUtils.XMLGregorianDateToDate(pp.getReportingDate().getDate()));
						
						if(pp.getTitle()!=null && isStringValid(pp.getTitle().getValue()))
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
			
//		if(activity.getComponents() == null) activity.setComponents(new HashSet());
//		activity.getComponents().add(ampComp);
//		DataExchangeUtils.addObjectoToAmp(ampComp);
		}
		
		
	}//end of step 5

	private void addFundingDetailToAmpCompFund(AmpComponentFunding acf,	PhysicalProgress phyprog) {
		// TODO Auto-generated method stub
		
	}

	//compare without white spaces
	private boolean isEqualStringsNWS(String s, String t){
		return (s.toLowerCase().trim().equals(t.toLowerCase().trim()));
	}
	
	private void addCollectionToAmp(HashSet<AmpComponentFunding> acfs) {
		// TODO Auto-generated method stub
		for (Iterator it = acfs.iterator(); it.hasNext();) {
			AmpComponentFunding acf = (AmpComponentFunding) it.next();
			DataExchangeUtils.addObjectoToAmp(acf);
		}
	}

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
		
		//TODO how are the amounts? in thousands?
		//if("true".equals(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS)))
			//acf.setTransactionAmount(new Double(fundingDetailType.getAmount()*1000));
		//else 
		acf.setTransactionAmount(new Double(fundingDetailType.getAmount().doubleValue()));
		
	}

	//documents and links
	private void processStep6(ActivityType activityImported, AmpActivity activity, HttpServletRequest request, String string, HashMap hm) {
		// TODO Auto-generated method stub
		
		
		if(activityImported.getDocuments() != null || activityImported.getDocuments().size() > 0){
			setTeamMember(request, "admin@amp.org", 0L);		
				for (Iterator it = activityImported.getDocuments().iterator(); it.hasNext();) {
					Documents doc = (Documents) it.next();
					TemporaryDocumentData tdd = new TemporaryDocumentData();
					tdd.setTitle(doc.getTitle());
					tdd.setDescription(doc.getDescription());
					//tdd.setFormFile(new  FormFile());
					//tdd.setContentType("");
					tdd.setWebLink(null);
					ActionMessages errors=new ActionMessages();
					NodeWrapper nodeWrapper			= tdd.saveToRepository(request, errors);
					
					if ( nodeWrapper != null ){
						AmpActivityDocument aac		= new AmpActivityDocument();
						aac.setUuid(nodeWrapper.getUuid());
						aac.setDocumentType( ActivityDocumentsConstants.RELATED_DOCUMENTS );
						if(activity.getActivityDocuments() == null || activity.getActivityDocuments().size() == 0)
							activity.setActivityDocuments(new HashSet());
						activity.getActivityDocuments().add(aac);
					}
				}
		}
		
		if(activityImported.getRelatedLinks() != null || activityImported.getRelatedLinks().size() > 0){
			setTeamMember(request, "admin@amp.org", 0L);
				for (Iterator it = activityImported.getRelatedLinks().iterator(); it.hasNext();) {
					RelatedLinks doc = (RelatedLinks) it.next();
					TemporaryDocumentData tdd = new TemporaryDocumentData();
					tdd.setTitle(doc.getLabel());
					tdd.setDescription(doc.getDescription());
					//FormFile f;
					//tdd.setFormFile(new  FormFile());
					tdd.setWebLink(doc.getUrl());
					//tdd.setContentType("");
					ActionMessages errors=new ActionMessages();
					NodeWrapper nodeWrapper			= tdd.saveToRepository(request, errors);
					
					if ( nodeWrapper != null ){
						AmpActivityDocument aac		= new AmpActivityDocument();
						aac.setUuid(nodeWrapper.getUuid());
						aac.setDocumentType( ActivityDocumentsConstants.RELATED_DOCUMENTS );
						if(activity.getActivityDocuments() == null || activity.getActivityDocuments().size() == 0)
							activity.setActivityDocuments(new HashSet());
						activity.getActivityDocuments().add(aac);
					}
				}
		}
		
		setTeamMember(request, null, null);
		
		
	}//end of step 6

	private void setTeamMember(HttpServletRequest request, String email, Long teamId){
		HttpSession	httpSession		= request.getSession();
		TeamMember teamMember		= (TeamMember)httpSession.getAttribute(org.digijava.module.aim.helper.Constants.CURRENT_MEMBER);
		teamMember.setEmail(email);
		teamMember.setTeamId(teamId);
		//httpSession.setAttribute(org.digijava.module.aim.helper.Constants.CURRENT_MEMBER,teamMember);
	}

	//contact information, issues
	private void processStep7(ActivityType activityImported, AmpActivity activity, HttpServletRequest request, String string, HashMap hm) {
	
		if (activityImported.getDonorContacts() != null && activityImported.getDonorContacts().size() > 0){
			//TODO: after refactoring the contact in AMP, save all the information
			ContactType contacts = activityImported.getDonorContacts().iterator().next();
			if(contacts != null){
				activity.setContFirstName(contacts.getFirstName());
				activity.setContLastName(contacts.getLastName());
				activity.setEmail(contacts.getEmail());
			}
		}
		
		if(activityImported.getGovContacts() !=null  && activityImported.getGovContacts().size() > 0){
			ContactType contacts = activityImported.getGovContacts().iterator().next();
			if(contacts != null){
				activity.setMofedCntFirstName(contacts.getFirstName());
				activity.setMofedCntLastName(contacts.getLastName());
				activity.setMofedCntEmail(contacts.getEmail());
			}
		}
	
		if(activityImported.getIssues() !=null && activityImported.getIssues().size() >0){
			Set issueSet = new HashSet();
			for (Iterator itIssues = activityImported.getIssues().iterator(); itIssues.hasNext();) {
				Issues issue = (Issues) itIssues.next();
				AmpIssues ampIssue = new AmpIssues();
				ampIssue.setActivity(activity);
				if(isFreeTextTypeValid(issue.getTitle()))
					ampIssue.setName(issue.getTitle().getValue());
				
				Set measureSet = new HashSet();
				
				if (issue.getMeasure() != null && issue.getMeasure().size() > 0) {
					for (int j = 0; j < issue.getMeasure().size(); j++) {
						Measure measure = (Measure) issue.getMeasure().get(j);
						AmpMeasure ampMeasure = new AmpMeasure();
						ampMeasure.setIssue(ampIssue);
						if(isFreeTextTypeValid(measure.getTitle()))
							ampMeasure.setName(measure.getTitle().getValue());
						Set actorSet = new HashSet();
						if (measure.getActor() != null && measure.getActor().size() > 0) {
							for (int k = 0; k < measure.getActor().size(); k++) {
								FreeTextType idmlActor = (FreeTextType) measure.getActor().get(k);
								AmpActor actor= new AmpActor();
								if(isFreeTextTypeValid(idmlActor))
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
			activity.setIssues(issueSet);
		}
		
	}
	
	private void processStep8(ActivityType activityImported, AmpActivity activity, HttpServletRequest request, String string, HashMap hm) {
		
		if(activityImported.getAdditional() != null && activityImported.getAdditional().size() > 0){
			for (AdditionalFieldType aft : activityImported.getAdditional()) {
				if( isEqualStringsNWS(aft.getField(), "PTIP") ){
					activity.setCrisNumber(aft.getValue());
				}
				if( isEqualStringsNWS(aft.getField(), "Code du Chapitre") ){
					activity.setVote(aft.getValue().substring(0, 3));
					activity.setSubVote(aft.getValue().substring(3, 5));
					activity.setSubProgram(aft.getValue().substring(5, 8));
					activity.setProjectCode(aft.getValue().substring(8, 11));
				}
				if( isEqualStringsNWS(aft.getField(), "onBudget") ){
					try{
						AmpCategoryValue onCV	= CategoryManagerUtil.getAmpCategoryValueFromDB(CategoryConstants.ACTIVITY_BUDGET_ON);
						CategoryManagerUtil.addCategoryToSet(onCV.getId(), activity.getCategories());
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
	}
	private boolean isFreeTextTypeValid(FreeTextType title) {
		// TODO Auto-generated method stub
		if(title != null && title.getValue()!=null) return true;
		return false;
	}

	private Collection getFundingXMLtoAMP(ActivityType actType, AmpActivity activity, HashMap hm) throws Exception{
		ArrayList<AmpFunding> fundings= null;
		Set orgRole = new HashSet();
		for (Iterator<FundingType> it = actType.getFunding().iterator(); it.hasNext();) {
			AmpRole role = DbUtil.getAmpRole(org.digijava.module.aim.helper.Constants.FUNDING_AGENCY);
			
			
			
			FundingType funding = (FundingType) it.next();
			CodeValueType fundingOrg=funding.getFundingOrg();
			AmpFunding ampFunding = new AmpFunding();
			ampFunding.setActive(true);
			AmpOrganisation ampOrg = (AmpOrganisation) mapCodeValueTypeElementInAmp(Constants.AMP_ORGANIZATION,fundingOrg,hm);
			ampFunding.setAmpDonorOrgId(ampOrg);
			ampFunding.setFundingDetails(new HashSet<AmpFundingDetail>());
			Set<AmpFundingDetail> fundDetails = new HashSet<AmpFundingDetail>();
			addMTEFProjectionsToSet(funding.getProjections(),ampFunding,hm);
			addFundingDetailsToSet(funding.getCommitments(), fundDetails, org.digijava.module.aim.helper.Constants.COMMITMENT);
			addFundingDetailsToSet(funding.getDisbursements(), fundDetails, org.digijava.module.aim.helper.Constants.DISBURSEMENT);
			addFundingDetailsToSet(funding.getExpenditures(), fundDetails, org.digijava.module.aim.helper.Constants.EXPENDITURE);
			if(ampFunding.getFundingDetails() == null ) ampFunding.setFundingDetails(new HashSet<AmpFundingDetail>());
			if(fundDetails != null) ampFunding.getFundingDetails().addAll(fundDetails);
			if(funding.getAssistanceType() != null){
				AmpCategoryValue acv = addCategValueForCodeValueType(funding.getAssistanceType(), hm, Constants.IDML_ASSISTANCE_TYPE, Constants.CATEG_VALUE_TYPE_OF_ASSISTANCE);
				ampFunding.setTypeOfAssistance(acv);
			}
			if(funding.getFinancingInstrument() != null){
				AmpCategoryValue acv = addCategValueForCodeValueType(funding.getFinancingInstrument(), hm, Constants.IDML_FINANCING_INSTRUMENT, Constants.CATEG_VALUE_FINANCING_INSTRUMENT);
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
	
	private void addMTEFProjectionsToSet(List<Projections> projections,	AmpFunding ampFunding,  HashMap hm) {
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
					AmpCategoryValue acv = addCategValueForCodeValueType(cvt, hm, Constants.IDML_FUNDING_PROJECTIONS_TYPE, Constants.CATEG_VALUE_MTEF_PROJECTION);
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
			ampFundDet.setTransactionAmount(new Double(fundDet.getAmount().doubleValue()));
			fundDetails.add(ampFundDet);
		}
		
	}

	private Object mapCodeValueTypeElementInAmp(String fieldType, CodeValueType element, HashMap hm) {
		
		
		if(fieldType!=null && element != null && element.getValue() != null && 
				!("".equals(fieldType.trim())) && !("".equals(element.getValue().trim())) ){
			
			//if it is in the db
			Object obj=null;
			obj = DataExchangeUtils.getElementFromAmp(fieldType, element.getValue(),element);
			if(obj != null) 
				return obj; //return the field which is in db
			else 
			{//the field is not in AMP_db
				DEMappingFields demf= getMappedFieldFromCache(hm, fieldType, element.getCode(), element.getValue());
				if( demf == null ) 
					{
						//the field is not in mapping table
						obj = insertFakeFieldInAmp(fieldType, element);
					}
				else{
					// the field is already in mapping table
						obj = DataExchangeUtils.getElementFromAmp(fieldType, demf.getAmpFieldId(),element);
				}
				return obj;
			}
		}
		return null;// TODO Auto-generated method stub
	}



	private Object insertFakeFieldInAmp(String fieldType,CodeValueType element) {
		// TODO Auto-generated method stub
		Object obj=null;
		if(fieldType!=null){

			if(Constants.AMP_ORGANIZATION.equals(fieldType)){
				//insert an organization
				AmpOrganisation ampOrg=new AmpOrganisation();
				ampOrg.setName(element.getValue());
				ampOrg.setOrgCode(element.getCode());
				obj = DataExchangeUtils.addObjectoToAmp(ampOrg);
				DataExchangeUtils.insertDEMappingField( element.getCode(), element.getValue(), ((AmpOrganisation)obj).getAmpOrgId(), fieldType, org.digijava.module.dataExchange.utils.Constants.STATUS_UNAPPROVED);
				return obj;
			}
			
			if(Constants.AMP_SECTOR.equals(fieldType)){
				//insert a sector
				
				//ArrayList<AmpClassificationConfiguration> allClassifConfigs = (ArrayList<AmpClassificationConfiguration>) getAllClassificationConfiguration();
				
				AmpClassificationConfiguration primConf = null;
		    	primConf = getPrimaryClassificationConfiguration();
		    	if(primConf != null){
		    		if(primConf.getClassification() != null && primConf.getClassification().getAmpSecSchemeId() != null ){
		    				//SectorUtil.getAllSectorsFromScheme(primConf.getClassification().getAmpSecSchemeId());
		    			AmpSector ampSector=new AmpSector();
		    			ampSector.setName(element.getValue());
		    			ampSector.setSectorCode(element.getCode());
		    			ampSector.setParentSectorId(null);
		    			ampSector.setAmpSecSchemeId(primConf.getClassification());
		    			obj = DataExchangeUtils.addObjectoToAmp(ampSector);
		    			DataExchangeUtils.insertDEMappingField( element.getCode(), element.getValue(), ((AmpSector)obj).getAmpSectorId(), fieldType, org.digijava.module.dataExchange.utils.Constants.STATUS_UNAPPROVED);
		    			
		    		}
		    	}
				
				
			 return obj;
			}

			if(Constants.AMP_PROGRAM.equals(fieldType)){
				//insert a program
				//by default it is under the National Plan Objective, under the first program
				AmpActivityProgramSettings primConf = null;
		    	primConf = getNationalPlanObjectiveSetting();
		    	if(primConf != null){
		    				//SectorUtil.getAllSectorsFromScheme(primConf.getClassification().getAmpSecSchemeId());
		    			AmpTheme ampProgram=new AmpTheme();
		    			ampProgram.setName(element.getValue());
		    			ampProgram.setParentThemeId(primConf.getDefaultHierarchy());
		    			ampProgram.setThemeCode(element.getCode());
		    			ampProgram.setIndlevel(new Integer(1));
		    			ampProgram.setTypeCategoryValue(primConf.getDefaultHierarchy().getTypeCategoryValue());
		    			obj = DataExchangeUtils.addObjectoToAmp(ampProgram);
		    			DataExchangeUtils.insertDEMappingField( element.getCode(), element.getValue(), ((AmpTheme)obj).getAmpThemeId(), fieldType, org.digijava.module.dataExchange.utils.Constants.STATUS_UNAPPROVED);
		    	}
			 return obj;
			}
			
		}
		
		return null;
	}
	
	private AmpClassificationConfiguration getPrimaryClassificationConfiguration(){
		AmpClassificationConfiguration primConf = null;
    	List<AmpClassificationConfiguration> configs = null;
		try {
			configs = SectorUtil.getAllClassificationConfigs();
		} catch (DgException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	if(configs!=null){
        	Iterator<AmpClassificationConfiguration> it = configs.iterator();        	
        	while(it.hasNext()){
        		AmpClassificationConfiguration conf = it.next();        		
				if(conf.isPrimary()) primConf = conf;
        	}
    	}
    	return primConf;
	}

	
	private AmpActivityProgramSettings getNationalPlanObjectiveSetting(){
		AmpActivityProgramSettings primConf = null;
    	List<AmpActivityProgramSettings> configs = null;
		configs = DataExchangeUtils.getAllAmpActivityProgramSettings();
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

	private List<AmpActivityProgramSettings> getAllAmpActivityProgramSettings(){
    	List<AmpActivityProgramSettings> configs = null;
		configs = DataExchangeUtils.getAllAmpActivityProgramSettings();
    	return configs;
	}
	
	private List<AmpClassificationConfiguration> getAllClassificationConfiguration(){
		AmpClassificationConfiguration primConf = null;
    	List<AmpClassificationConfiguration> configs = null;
		try {
			configs = SectorUtil.getAllClassificationConfigs();
		} catch (DgException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return configs;
	}

	private HashMap creatingImportCacheTree(){
		ArrayList allMappingFields = (ArrayList) DataExchangeUtils.getAmpDEMappingFields();
		HashMap<String, ArrayList<DEMappingFields> > hm= new HashMap<String, ArrayList<DEMappingFields>>();
		if(allMappingFields != null){
			for (Iterator it = allMappingFields.iterator(); it.hasNext();) {
				DEMappingFields demf = (DEMappingFields) it.next();
				if( hm.containsKey(demf.getFieldType()) )
					hm.get(demf.getFieldType()).add(demf);
				else {
					ArrayList<DEMappingFields> aux= new ArrayList<DEMappingFields>();
					aux.add(demf);
					hm.put(demf.getFieldType(), aux);
				}
			}
		}
		return hm;
	}
	
	private boolean existInImportCache(HashMap<String, ArrayList<DEMappingFields> > hm, String key, String code, String value){
		boolean found=false;
		ArrayList<DEMappingFields> listFromKey= hm.get(key);
		if(listFromKey!=null)
			for (Iterator it = listFromKey.iterator(); it.hasNext();) {
				DEMappingFields demf = (DEMappingFields) it.next();
				// TODO: for better safety we can put && instead of ||
				if( (demf.getImportedFieldCode() !=null && demf.getImportedFieldCode().equals(code)) || (demf.getImportedFieldValue() !=null && demf.getImportedFieldValue().equals(value))) 
				 //the imported value or code exist
					return true;
				
			}
		return false;
		
	}
	
	private DEMappingFields getMappedFieldFromCache(HashMap<String, ArrayList<DEMappingFields> > hm, String key, String code, String value){
		boolean found=false;
		ArrayList<DEMappingFields> listFromKey= hm.get(key);
		if(listFromKey!=null)
			for (Iterator it = listFromKey.iterator(); it.hasNext();) {
				DEMappingFields demf = (DEMappingFields) it.next();
				// TODO: for better safety we can put && instead of ||
				
				if( (demf.getImportedFieldCode() !=null && demf.getImportedFieldCode().equals(code)  && !"".equals(code.trim())) 
						||
					(demf.getImportedFieldValue() !=null && demf.getImportedFieldValue().equals(value)  && !"".equals(value.trim())) ) 
				 //the imported value or code exist
					return demf;
				
			}
		return null;
		
	}
	
	
	
	private String setEditorFreeTextType(List<FreeTextType> list , AmpActivity activity, HttpServletRequest request, String preKey){
		boolean found=false;
		//ArrayList<FreeTextType> descriptionList=(ArrayList<FreeTextType>) actType.getDescription();
		String key = preKey + System.currentTimeMillis();
		String refUrl = RequestUtils.getSourceURL(request);
		User user = RequestUtils.getUser(request);
		for (Iterator iterator = list.iterator(); iterator.hasNext();) 
		{
			FreeTextType obj = (FreeTextType) iterator.next();
			if( isValidLang(obj.getLang()) && obj.getValue()!=null){
				Editor ed = org.digijava.module.editor.util.DbUtil.createEditor(user,obj.getLang().toLowerCase(),refUrl,key,key," ",null,request);
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
	
	private boolean isValidLang(String lang){
		
		//TODO: validate lang based on dg_locale code for languages
		if(lang!=null && lang.length() == 2 && !"".equals(lang))
			return true;
		return false;
		
	}
	
	private boolean isStringValid(String lang ){
		
		//TODO: validate lang based on dg_locale code for languages
		if(lang != null && !"".equals(lang.trim()))
			return true;
		return false;
		
	}
	
	private AmpCategoryValue addCategValueForCodeValueType(CodeValueType element, HashMap hm, String fieldType, String categoryKey ){
		//TODO: refresh the cache!!!!
		AmpCategoryValue acv=null;
		Collection<AmpCategoryValue> allCategValues;
		allCategValues = (Collection<AmpCategoryValue>) CategoryManagerUtil.getAmpCategoryValueCollectionByKey(categoryKey);
		String valueToCateg="";
		
		if( element.getValue()!=null && !"".equals(element.getValue().trim()) )
			valueToCateg=element.getValue();
		else if( element.getCode()!=null && !"".equals(element.getCode().trim()) )
			valueToCateg=element.getCode();

		if(valueToCateg == null || valueToCateg == "") return null;
		
		for (Iterator itacv = allCategValues.iterator(); itacv.hasNext();) {
			acv = (AmpCategoryValue) itacv.next();
			if(acv.getValue().equals(valueToCateg)) return acv;
		}
		
		if( !existInImportCache(hm, fieldType, element.getCode(), element.getValue()) )
			{
				//have to be inserted
			    
				if(!valueToCateg.equals("")){
					try{
						CategoryManagerUtil.addValueToCategory(CategoryConstants.DATA_EXCHANGE_KEY, fieldType+"."+valueToCateg);
					}catch(Exception ex){
						ex.printStackTrace();
						//the value already there
					}
					 allCategValues = (Collection<AmpCategoryValue>) CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.DATA_EXCHANGE_KEY);
					acv=(AmpCategoryValue) allCategValues.toArray()[allCategValues.size()-1];
					DataExchangeUtils.insertDEMappingField( element.getCode(), element.getValue(), acv.getId(), fieldType, org.digijava.module.dataExchange.utils.Constants.STATUS_UNAPPROVED);
					//activity.getCategories().add(acv);
					
					
				}
			}
		else {// the field exist in cache, adding the ampCategoryValue to the activity
			DEMappingFields demf=getMappedFieldFromCache(hm, fieldType, element.getCode(), element.getValue());
			//AmpCategoryValue acv = null;
			if(demf!=null)
				acv= CategoryManagerUtil.getAmpCategoryValueFromDb(demf.getAmpFieldId()); //get it from categ value
			//if(acv!=null) activity.getCategories().add(acv);
		}
		return acv;
		}
	
//	public void builImportActivitiesToAMP() throws Exception, AMPError{
//		if(activities !=null && activities.getActivity() !=null)
//			for (Iterator<ActivityType> it = activities.getActivity().iterator(); it.hasNext();) {
//				ActivityType activityImported = (ActivityType) it.next();
//				AmpActivity activity = new AmpActivity();
//				processPreStep(activity, tm);
//			//if( !activityExists(activityImported) ){
//				HashMap hm = creatingImportCacheTree();
//				processPreStep(activity, tm);
//				
//				//identification, assigningOrg
//				processStep1(activityImported, activity,request, "en",hm);
//				
//				//sectors, programs
//				processStep2(activityImported, activity,request, "en",hm);
//				
//				//funding , regional funding
//				processStep3(activityImported, activity,request, "en",hm);
//				
//				//relatedOrg, components
//				Collection<Components<AmpComponentFunding>> tempComps = new HashSet();
//				processStep5(activityImported, activity,request, "en",hm, tempComps);
//				
//				//documents
//				processStep6(activityImported, activity,request, "en",hm);
//				
//				//contact information
//				processStep7(activityImported, activity,request, "en",hm);
//				
//				DataExchangeUtils.saveComponents(activity, request, tempComps);
//				DataExchangeUtils.saveActivity(activity, request);
//				
//			//}
//			//else logger.info("Activity was not imported-> pls implement the update step :)");
//			}
//	}
	
	public void saveActivities(HttpServletRequest request, TeamMember tm) {
		// TODO Auto-generated method stub
		if(this.getRoot()!=null && this.getRoot().getElements() != null)
			for (Iterator it = this.getRoot().getElements().iterator(); it.hasNext();) {
				AmpDEImportLog iLog = (AmpDEImportLog) it.next();
				if(iLog.isSelect()){
					Activities activities = iLog.getActivities();
					try {
						//if there are no errors with that activity we can import it
						if(!iLog.isError())
							this.builImportActivitiesToAMP(activities, request, tm);
					} catch (AMPError e) {
						// TODO Auto-generated catch block
						logger.info("AMP ERROR : error in saving activity:"+iLog.getObjectNameLogged()+" in AMP db");
						e.printStackTrace();
						iLog.addError("AMP ERROR : error in saving activity in AMP db");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logger.info("AMP ERROR : error in saving activity:"+iLog.getObjectNameLogged()+" in AMP db");
						e.printStackTrace();
						iLog.addError("AMP ERROR ::: error in saving activity in AMP db");
					}
				}
			}
	}
	
	public void builImportActivitiesToAMP(Activities activities,HttpServletRequest request, TeamMember tm) throws Exception, AMPError{
		if(activities !=null && activities.getActivity() !=null)
			for (Iterator<ActivityType> it = activities.getActivity().iterator(); it.hasNext();) {
				ActivityType activityImported = (ActivityType) it.next();
				AmpActivity activity = new AmpActivity();
				processPreStep(activity, tm);
			//if( !activityExists(activityImported) ){
				HashMap hm = creatingImportCacheTree();
				processPreStep(activity, tm);
				
				//identification, assigningOrg
				processStep1(activityImported, activity,request, "en",hm);
				
				//sectors, programs
				processStep2(activityImported, activity,request, "en",hm);
				
				//funding , regional funding
				processStep3(activityImported, activity,request, "en",hm);
				
				//relatedOrg, components
				Collection<Components<AmpComponentFunding>> tempComps = new HashSet();
				processStep5(activityImported, activity,request, "en",hm, tempComps);
				
				//documents
				processStep6(activityImported, activity,request, "en",hm);
				
				//contact information
				processStep7(activityImported, activity,request, "en",hm);
				
				//custom fields
				processStep8(activityImported, activity,request, "en",hm);
				
				DataExchangeUtils.saveComponents(activity, request, tempComps);
				DataExchangeUtils.saveActivity(activity, request);
				
			//}
			//else logger.info("Activity was not imported-> pls implement the update step :)");
			}
	}
	
	
	public boolean splitInChunks(InputStream inputStream) {
		
		String result="";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
        	   FileCopyUtils.copy(inputStream, outputStream);
           } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
           }
        result = outputStream.toString();
        String[] s =result.split("<activity");
        String header, footer=null;
        header = s[0];
        String[] forFooter = null;
        if(s[s.length-1].contains("<spider"))
        	{
        		forFooter = s[s.length-1].split("<spider");
        		footer = "<spider"+ forFooter[1];
        	}
        else{
        	forFooter = s[s.length-1].split("</activities>");
    		footer = "</activities>";
        }
        	
        s[s.length-1] = forFooter[0];
        
        //ArrayList<InputStream> activitiesChunks = new ArrayList<InputStream>();
        
        if(this.getImportLogs() == null || this.getImportLogs().size() < 1) this.setImportLogs(new ArrayList<AmpDEImportLog>());
        String newActivity = "";
        if(s.length <2) return false;
        for (int i = 1; i < s.length; i++) {
			newActivity="";
        	newActivity+=header+"<activity"+s[i]+footer;
        	AmpDEImportLog ilog = new AmpDEImportLog();
        	String content = s[i];
        	String[] aux = content.split("<title");
        	String[] aux1 ;
        	String[] aux2 ;
        	if(aux.length < 2){
        		ilog.addError("JAXB Exception - XML file is damaged for this activity - NO TITLE");
        		ilog.setObjectNameLogged("NoNameActivity");
        		this.getActivityList().add("NoNameActivity");
        	}
        	else{
        		aux1 = aux[1].split(">",2);
        		if(aux1.length == 2)
        			aux2 = aux1[1].split("</title>");
        		else aux2 = aux1[0].split("</title>");
        		ilog.setObjectNameLogged(i+". "+aux2[0]);
        		this.getActivityList().add(aux2[0]);
        	}

        	ilog.setCounter(i);
        	
        	ilog.setObjectTypeLogged("IDMLActivity");
        	//OutputStream outputStream = new ByteArrayOutputStream();
        	outputStream = new ByteArrayOutputStream();
        	try {
				FileCopyUtils.copy(new ByteArrayInputStream(newActivity.getBytes()), outputStream);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	ilog.setOutputStream(outputStream);
        	this.getImportLogs().add(ilog);
        	//return true;
		}
        return true;
		
	}
//	
//
//	public boolean checkXMLIntegrity(String xsdPath, InputStream inputS, InputStream inputStream1) {
//		// TODO Auto-generated method stub
//		
//		boolean isOk = true;
//		this.setIsOk(true);
//		Activities acts = null;
//		
//		try {
//		JAXBContext jc = JAXBContext.newInstance("org.digijava.module.dataExchange.jaxb");
//        Unmarshaller m = jc.createUnmarshaller();
//        FeaturesUtil.errorLog="";
//        boolean xsdValidate = true;
//        	
//        	if(xsdValidate){
//                // create a SchemaFactory that conforms to W3C XML Schema
//                 SchemaFactory sf = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
//
//                 // parse the purchase order schema
//                 Schema schema = sf.newSchema(new File(xsdPath));
//
//                 m.setSchema(schema);
//                 // set your error handler to catch errors during schema construction
//                 // we can use custom validation event handler
//                 m.setEventHandler(new ImportValidationEventHandler());
//           }
//        	acts = (org.digijava.module.dataExchange.jaxb.Activities) m.unmarshal(this.inputStream);
//        } 
//		catch (SAXException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			isOk = false;
//			this.setIsOk(false);
//		}
//        catch (javax.xml.bind.JAXBException jex) {
//        	jex.printStackTrace();
//         
//        	String line = null;
//            String result="";
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            try {
//            	   FileCopyUtils.copy(inputStream1, outputStream);
//               } catch (IOException e) {
//    			// TODO Auto-generated catch block
//    			e.printStackTrace();
//               }
//            result = outputStream.toString();
//            String[] s =result.split("<activity");
//            String header, footer=null;
//            header = s[0];
//            String[] forFooter = s[s.length-1].split("<spider");
//            footer = "<spider"+ forFooter[1];
//            s[s.length-1] = forFooter[0];
//            
//            ArrayList<InputStream> activitiesChunks = new ArrayList<InputStream>();
//            for (int i = 1; i < s.length; i++) {
//				String newActivity = "";
//				newActivity+=header+"<activity"+s[i]+footer;
//				activitiesChunks.add(new ByteArrayInputStream(newActivity.getBytes()));
//			}
//            if(this.generatedActivities == null) this.generatedActivities = new ArrayList<InputStream>();
//            this.generatedActivities = activitiesChunks;
//            isOk = false;
//            this.setIsOk(false);
//            logger.error("JAXB Exception!") ;
//        } 
//       this.activities = acts;
//	  return isOk;
//	}

	
	public boolean checkXMLIntegrityNoChunks(String xsdPath, InputStream inputS) {
		// TODO Auto-generated method stub
		
		boolean isOk = true;
		//this.setIsOk(true);
		Activities acts = null;
		
		try {
		JAXBContext jc = JAXBContext.newInstance("org.digijava.module.dataExchange.jaxb");
        Unmarshaller m = jc.createUnmarshaller();
        FeaturesUtil.errorLog="";
        boolean xsdValidate = true;
        	
        	if(xsdValidate){
                // create a SchemaFactory that conforms to W3C XML Schema
                 SchemaFactory sf = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);

                 // parse the purchase order schema
                 Schema schema = sf.newSchema(new File(xsdPath));

                 m.setSchema(schema);
                 // set your error handler to catch errors during schema construction
                 // we can use custom validation event handler
                 m.setEventHandler(new ImportValidationEventHandler());
           }
        	acts = (org.digijava.module.dataExchange.jaxb.Activities) m.unmarshal(inputS);
        } 
		catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			isOk = false;
			//this.setIsOk(false);
		}
        catch (javax.xml.bind.JAXBException jex) {
        	jex.printStackTrace();
        	
            logger.error("JAXB Exception!") ;
        } 
        this.activities = acts;
	  return isOk;
	}

	public void generateLogForActivities(String path) {
		// TODO Auto-generated method stub
		Activities acts = null;
		for (Iterator it = this.getImportLogs().iterator(); it.hasNext();) {
			AmpDEImportLog iLog = (AmpDEImportLog) it.next();
			FeaturesUtil.errorLog = "";
			boolean ok= false;
			try {
				JAXBContext jc = JAXBContext.newInstance("org.digijava.module.dataExchange.jaxb");
		        Unmarshaller m = jc.createUnmarshaller();
		        FeaturesUtil.errorLog="";
		        boolean xsdValidate = true;
		        	
		        	if(xsdValidate){
		                // create a SchemaFactory that conforms to W3C XML Schema
		                 SchemaFactory sf = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);

		                 // parse the purchase order schema
		                 Schema schema = sf.newSchema(new File(path));

		                 m.setSchema(schema);
		                 // set your error handler to catch errors during schema construction
		                 // we can use custom validation event handler
		                 m.setEventHandler(new ImportValidationEventHandler());
		           }
		        	acts = (org.digijava.module.dataExchange.jaxb.Activities) m.unmarshal(iLog.getInputStream());
		        	ok = true;
		        } 
				catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					logger.error("SAX Exception!") ;
					iLog.addError("SAX Exception - XML file is damaged for this activity");
				}
		        catch (javax.xml.bind.JAXBException jex) {
		        	jex.printStackTrace();
		        	iLog.addError("JAXB Exception - XML file is damaged for this activity");
		            logger.error("JAXB Exception!") ;
		        }
		      if(!FeaturesUtil.errorLog.equals("")) 
		    	  iLog.addWarning(FeaturesUtil.errorLog.toString());
		      FeaturesUtil.errorLog = "";
		      if(ok) iLog.setActivities(acts);
		      
		}
		
		
	}
	
	public void createActivityTree() {
		// TODO Auto-generated method stub
		
		if(this.getImportLogs() ==null || this.getImportLogs().size()<1) return;
		root = new AmpDEImportLog();
		root.setSelect(false);
		root.setKey("Activities");
		root.setObjectNameLogged("Activities");
		root.setElements(new ArrayList<AmpDEImportLog>());
		root.setCounter(0);
		for (Iterator it = this.getImportLogs().iterator(); it.hasNext();) {
			AmpDEImportLog iLog = (AmpDEImportLog) it.next();
			root.getElements().add(iLog);
		}
	}
	
	public String printLogs(){
		String result = new String();
		for (Iterator it = this.getImportLogs().iterator(); it.hasNext();) {
			AmpDEImportLog iLog = (AmpDEImportLog) it.next();
			result+="<![CDATA[\"<br/>\"]]>" + iLog.printLog("<![CDATA[\"<br/>\"]]>");
		}
		return result;
	}
	
	public Collection<AmpDEImportLog> getImportLogs() {
		return importLogs;
	}

	public void setImportLogs(Collection<AmpDEImportLog> importLogs) {
		this.importLogs = importLogs;
	}

	public Collection<String> getActivityList() {
		return activityList;
	}

	public void setActivityList(Collection<String> activityList) {
		this.activityList = activityList;
	}

	public AmpDEImportLog getRoot() {
		return root;
	}

	public void setRoot(AmpDEImportLog root) {
		this.root = root;
	}

	public HashMap getHm() {
		return hm;
	}

	public void setHm(HashMap hm) {
		this.hm = hm;
	}







	
}
