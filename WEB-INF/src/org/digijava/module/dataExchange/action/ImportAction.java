/**
 * 
 */
package org.digijava.module.dataExchange.action;

import java.io.ByteArrayInputStream;
import java.io.File;
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
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.dgfoundation.amp.error.AMPError;
import org.dgfoundation.amp.utils.MultiAction;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityDocument;
import org.digijava.module.aim.dbentity.AmpActivityInternalId;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpActor;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpComponentFunding;
import org.digijava.module.aim.dbentity.AmpComponentType;
import org.digijava.module.aim.dbentity.AmpFieldsVisibility;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpFundingMTEFProjection;
import org.digijava.module.aim.dbentity.AmpIssues;
import org.digijava.module.aim.dbentity.AmpMeasure;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpPhysicalPerformance;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.helper.ActivityDocumentsConstants;
import org.digijava.module.aim.helper.Components;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.PhysicalProgress;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.ComponentsUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.helper.TemporaryDocumentData;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;
import org.digijava.module.dataExchange.dbentity.DEMappingFields;
import org.digijava.module.dataExchange.form.ImportForm;
import org.digijava.module.dataExchange.jaxb.ActivityType;
import org.digijava.module.dataExchange.jaxb.CodeValueType;
import org.digijava.module.dataExchange.jaxb.ComponentFundingType;
import org.digijava.module.dataExchange.jaxb.ContactType;
import org.digijava.module.dataExchange.jaxb.FreeTextType;
import org.digijava.module.dataExchange.jaxb.FundingDetailType;
import org.digijava.module.dataExchange.jaxb.FundingType;
import org.digijava.module.dataExchange.jaxb.ActivityType.Component;
import org.digijava.module.dataExchange.jaxb.ActivityType.Documents;
import org.digijava.module.dataExchange.jaxb.ActivityType.Id;
import org.digijava.module.dataExchange.jaxb.ActivityType.Issues;
import org.digijava.module.dataExchange.jaxb.ActivityType.Location;
import org.digijava.module.dataExchange.jaxb.ActivityType.RelatedLinks;
import org.digijava.module.dataExchange.jaxb.ActivityType.RelatedOrgs;
import org.digijava.module.dataExchange.jaxb.ActivityType.Issues.Measure;
import org.digijava.module.dataExchange.jaxb.FundingType.Projections;
import org.digijava.module.dataExchange.utils.Constants;
import org.digijava.module.dataExchange.utils.DataExchangeUtils;
import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.editor.exception.EditorException;
import org.xml.sax.SAXException;

/**
 * @author dan
 *
 */
public class ImportAction extends MultiAction {

	private static Logger logger = Logger.getLogger(ImportAction.class);
	
	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.utils.MultiAction#modePrepare(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward modePrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		return modeSelect(mapping, form, request, response);
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.utils.MultiAction#modeSelect(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward modeSelect(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		HttpSession session=request.getSession();
		session.setAttribute("errorLogForDE","");
		session.setAttribute("messageLogForDe","");
		if(request.getParameter("import")!=null) return modeUploadedFile(mapping, form, request, response);
				
		return mapping.findForward("forward");
	}

	public ActionForward modeUploadedFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		
		HttpSession session = request.getSession();
		
		try{
		ImportForm deImportForm= (ImportForm) form;
		
		FormFile myFile = deImportForm.getUploadedFile();
        byte[] fileData    = myFile.getFileData();
        InputStream inputStream= new ByteArrayInputStream(fileData);
        
	        JAXBContext jc = JAXBContext.newInstance("org.digijava.module.dataExchange.jaxb");
	        Unmarshaller m = jc.createUnmarshaller();
	        org.digijava.module.dataExchange.jaxb.Activities activities;
	        FeaturesUtil.errorLog="";
	        boolean xsdValidate = true;
	        try {
	        	
	        	if(xsdValidate){
	                // create a SchemaFactory that conforms to W3C XML Schema
	                 SchemaFactory sf = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
	
	                 // parse the purchase order schema
	                 Schema schema = sf.newSchema(new File(this.getServlet().getServletContext().getRealPath("/")+"/doc/IDML2.0.xsd"));
	
	                 m.setSchema(schema);
	                 // set your error handler to catch errors during schema construction
	                 // we can use custom validation event handler
	                 m.setEventHandler(new ImportValidationEventHandler());
	           }
	        	
	        	activities = (org.digijava.module.dataExchange.jaxb.Activities) m.unmarshal(inputStream);
	        	
	        	ArrayList<ActivityType> a=(ArrayList) activities.getActivity();
	        	
	        	TeamMember tm = null;
	        	if (session.getAttribute("currentMember") != null)
	        		tm = (TeamMember) session.getAttribute("currentMember");
	        	
	        	for (Iterator<ActivityType> it = a.iterator(); it.hasNext();) {
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
						
						DataExchangeUtils.saveActivity(activity, request);
						DataExchangeUtils.saveComponents(activity, request, tempComps);
						
					//}
					//else logger.info("Activity was not imported-> pls implement the update step :)");
				}
	        
	        	
	        	
	        	
			} catch (SAXException ex) {
	            System.out.println(ex.getMessage());
	            ex.printStackTrace();
	        } 
        
        } 
         catch (javax.xml.bind.JAXBException jex) {
            System.out.println("JAXB Exception!") ;
            jex.printStackTrace();
          }
         catch (java.io.FileNotFoundException fex) {
             System.out.println("File not Found!");
             fex.printStackTrace();
         }
        //session=request.getSession();
 		session.setAttribute("errorLogForDE",FeaturesUtil.errorLog);
 		if("".equals(FeaturesUtil.errorLog)) 
 			session.setAttribute("messageLogForDe","There are no errors after import. <br/> Import successfully");
        return mapping.findForward("forward");
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
		
		activity.setCreatedAsDraft(false);
	}
	
	//search for the title of the activity imported in db
	//if the title in any language already exists return true, else return false
	public boolean activityExists(ActivityType actType){
		ArrayList<FreeTextType> titlesList=(ArrayList<FreeTextType>) actType.getTitle();
		for (Iterator iterator = titlesList.iterator(); iterator.hasNext();) {
			FreeTextType title = (FreeTextType) iterator.next();
			AmpActivity act = ActivityUtil.getActivityByName(title.getValue());
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
		if( actType.getImplementationLevels()!=null ){
			
			AmpCategoryValue acv=addCategValueForCodeValueType(actType.getImplementationLevels(), hm, Constants.IDML_IMPLEMENTATION_LEVELS, Constants.CATEG_VALUE_IMPLEMENTATION_LEVEL);
			if(acv!=null)
				activity.getCategories().add(acv);
		}
		
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
		if(activityImported.getSectors()!=null){
			Set<AmpActivitySector> sectors = new HashSet<AmpActivitySector>();
			for (Iterator iterator = activityImported.getSectors().iterator(); iterator.hasNext();) {
				CodeValueType idmlSector = (CodeValueType) iterator.next();
				AmpSector ampSector = (AmpSector) mapCodeValueTypeElementInAmp(Constants.AMP_SECTOR,idmlSector,hm);
				
				if(ampSector == null || ampSector.getAmpSectorId() == null) continue;
				
				AmpActivitySector amps = new AmpActivitySector();
				amps.setActivityId(activity);
				sectorId = ampSector.getAmpSectorId();
				if (sectorId != null && (!sectorId.equals(new Long(-1))))
					amps.setSectorId(ampSector);
				amps.setSectorPercentage(100.00f);
				
				ArrayList<AmpClassificationConfiguration> allClassifConfigs = (ArrayList<AmpClassificationConfiguration>) getAllClassificationConfiguration();
				AmpClassificationConfiguration primConf = null;
				primConf = getPrimaryClassificationConfiguration();
				for (Iterator it2 = allClassifConfigs.iterator(); it2.hasNext();) {
					AmpClassificationConfiguration acc = (AmpClassificationConfiguration) it2.next();
					if(acc.getClassification().getAmpSecSchemeId().equals(ampSector.getAmpSecSchemeId().getAmpSecSchemeId()))
						primConf=acc;
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
		
		
		
	}//end of step 2
	
	
	
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
			
			for (Iterator it = actType.getLocation().iterator(); it.hasNext();) {
				Location location = (Location) it.next();
				
			}
			
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
					if(isEqualStringsNWS(type, Constants.IDML_EXECUTIN_AGENCY))
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
						role=DbUtil.getAmpRole(org.digijava.module.aim.helper.Constants.RESPONSIBLE_ORGANISATION);
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
					
					ampComp.getActivities().add(activity);
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
		if("true".equals(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS)))
			acf.setTransactionAmount(new Double(fundingDetailType.getAmount()*1000));
		else acf.setTransactionAmount(new Double(fundingDetailType.getAmount()));
		
	}

	//documents and links
	private void processStep6(ActivityType activityImported, AmpActivity activity, HttpServletRequest request, String string, HashMap hm) {
		// TODO Auto-generated method stub
		
		
		if(activityImported.getDocuments() != null || activityImported.getDocuments().size() > 0){
			setTeamMember(request, "admin@amp.org", 0L);
			Session writeSession = DocumentManagerUtil.getWriteSession(request);
				for (Iterator it = activityImported.getDocuments().iterator(); it.hasNext();) {
					Documents doc = (Documents) it.next();
					TemporaryDocumentData tdd = new TemporaryDocumentData();
					tdd.setTitle(doc.getTitle());
					tdd.setDescription(doc.getDescription());
					//tdd.setFormFile(new  FormFile());
					//tdd.setContentType("");
					tdd.setWebLink(null);
					ActionErrors errors=new ActionErrors();
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
			Session writeSession = DocumentManagerUtil.getWriteSession(request);
				for (Iterator it = activityImported.getRelatedLinks().iterator(); it.hasNext();) {
					RelatedLinks doc = (RelatedLinks) it.next();
					TemporaryDocumentData tdd = new TemporaryDocumentData();
					tdd.setTitle(doc.getLabel());
					tdd.setDescription(doc.getDescription());
					//FormFile f;
					//tdd.setFormFile(new  FormFile());
					tdd.setWebLink(doc.getUrl());
					//tdd.setContentType("");
					ActionErrors errors=new ActionErrors();
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
	
	
	private boolean isFreeTextTypeValid(FreeTextType title) {
		// TODO Auto-generated method stub
		if(title != null && title.getValue()!=null) return true;
		return false;
	}

	private Collection getFundingXMLtoAMP(ActivityType actType, AmpActivity activity, HashMap hm) throws Exception{
		ArrayList<AmpFunding> fundings= null;
		for (Iterator<FundingType> it = actType.getFunding().iterator(); it.hasNext();) {
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
				AmpCategoryValue acv = addCategValueForCodeValueType(actType.getStatus(), hm, Constants.IDML_ASSISTANCE_TYPE, Constants.CATEG_VALUE_TYPE_OF_ASSISTANCE);
				ampFunding.setTypeOfAssistance(acv);
			}
			if(funding.getFinancingInstrument() != null){
				AmpCategoryValue acv = addCategValueForCodeValueType(actType.getStatus(), hm, Constants.IDML_FINANCING_INSTRUMENT, Constants.CATEG_VALUE_FINANCING_INSTRUMENT);
				ampFunding.setFinancingInstrument(acv);
			}
			if(activity !=null ) ampFunding.setAmpActivityId(activity);
			if(activity.getFunding() ==  null) activity.setFunding(new HashSet<AmpFunding>());
			
			if(fundings == null) fundings=new ArrayList<AmpFunding>();
			
			//conditions
			//TODO: the language - lang attribute
			if(funding.getConditions() != null) ampFunding.setConditions(funding.getConditions().getValue());
			fundings.add(ampFunding);
		}
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
				AmpFundingMTEFProjection ampmtef=new AmpFundingMTEFProjection();
				
				if("true".equals(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS)))
					ampmtef.setAmount(new Double(mtef.getAmount()*1000));
				else ampmtef.setAmount(new Double(mtef.getAmount()));
				

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
			if("true".equals(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS)))
				ampFundDet.setTransactionAmount(new Double(fundDet.getAmount()*1000));
			else ampFundDet.setTransactionAmount(new Double(fundDet.getAmount()));
			fundDetails.add(ampFundDet);
		}
		
	}

	private Object mapCodeValueTypeElementInAmp(String fieldType, CodeValueType element, HashMap hm) {
		
		
		if(fieldType!=null && element != null && element.getValue() != null && 
				!("".equals(fieldType.trim())) && !("".equals(element.getValue().trim())) ){
			
			//if it is in the db
			Object obj=null;
			obj = DataExchangeUtils.getElementFromAmp(fieldType, element.getValue());
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
						obj = DataExchangeUtils.getElementFromAmp(fieldType, demf.getAmpFieldId());
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
			
			
		}
		
		return null;
	}
	
	public AmpClassificationConfiguration getPrimaryClassificationConfiguration(){
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
	
	public List<AmpClassificationConfiguration> getAllClassificationConfiguration(){
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

	public HashMap creatingImportCacheTree(){
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
	
	public boolean existInImportCache(HashMap<String, ArrayList<DEMappingFields> > hm, String key, String code, String value){
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
	
	public DEMappingFields getMappedFieldFromCache(HashMap<String, ArrayList<DEMappingFields> > hm, String key, String code, String value){
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
	
	
	
	public String setEditorFreeTextType(List<FreeTextType> list , AmpActivity activity, HttpServletRequest request, String preKey){
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
	
	public boolean isValidLang(String lang){
		
		//TODO: validate lang based on dg_locale code for languages
		if(lang!=null && lang.length() == 2 && !"".equals(lang))
			return true;
		return false;
		
	}
	
	public boolean isStringValid(String lang ){
		
		//TODO: validate lang based on dg_locale code for languages
		if(lang != null && !"".equals(lang.trim()))
			return true;
		return false;
		
	}
	public AmpCategoryValue addCategValueForCodeValueType(CodeValueType element, HashMap hm, String fieldType, String categoryKey ){
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
	
	
	  private boolean isPrimarySectorEnabled() {
	 	    ServletContext ampContext = getServlet().getServletContext();
		    AmpTreeVisibility ampTreeVisibility=(AmpTreeVisibility) ampContext.getAttribute("ampTreeVisibility");		
			AmpTemplatesVisibility currentTemplate=(AmpTemplatesVisibility) ampTreeVisibility.getRoot();
			if(currentTemplate!=null)
				if(currentTemplate.getFeatures()!=null)
					for(Iterator it=currentTemplate.getFields().iterator();it.hasNext();)
					{
						AmpFieldsVisibility field=(AmpFieldsVisibility) it.next();
						if(field.getName().compareTo("Primary Sector")==0) 
						{	
							return true;
						}
				
					}
			return false;
	  }
	
}
