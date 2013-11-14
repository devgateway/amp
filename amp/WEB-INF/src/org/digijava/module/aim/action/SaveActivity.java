/*
 * SaveActivity.java
 */
package org.digijava.module.aim.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.jcr.Node;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.ar.dyn.DynamicColumnsUtil;
import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.error.AMPUncheckedException;
import org.dgfoundation.amp.error.ExceptionFactory;
import org.dgfoundation.amp.error.keeper.ErrorReportingPlugin;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityContact;
import org.digijava.module.aim.dbentity.AmpActivityDocument;
import org.digijava.module.aim.dbentity.AmpActivityInternalId;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpActivityProgram;
import org.digijava.module.aim.dbentity.AmpActivityReferenceDoc;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpActor;
import org.digijava.module.aim.dbentity.AmpAhsurvey;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpComponentFunding;
import org.digijava.module.aim.dbentity.AmpContactProperty;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFeaturesVisibility;
import org.digijava.module.aim.dbentity.AmpFieldsVisibility;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpFundingMTEFProjection;
import org.digijava.module.aim.dbentity.AmpIssues;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpMeasure;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpPhysicalPerformance;
import org.digijava.module.aim.dbentity.AmpRegionalFunding;
import org.digijava.module.aim.dbentity.AmpRegionalObservation;
import org.digijava.module.aim.dbentity.AmpRegionalObservationActor;
import org.digijava.module.aim.dbentity.AmpRegionalObservationMeasure;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTeamMemberRoles;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.EUActivity;
import org.digijava.module.aim.dbentity.EUActivityContribution;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.form.EditActivityForm.ActivityContactInfo;
import org.digijava.module.aim.form.EditActivityForm.Survey;
import org.digijava.module.aim.helper.ActivityDocumentsConstants;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.AmpProject;
import org.digijava.module.aim.helper.Components;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.CustomField;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.Funding;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.aim.helper.FundingOrganization;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.Issues;
import org.digijava.module.aim.helper.Location;
import org.digijava.module.aim.helper.MTEFProjection;
import org.digijava.module.aim.helper.Measures;
import org.digijava.module.aim.helper.OrgProjectId;
import org.digijava.module.aim.helper.PhysicalProgress;
import org.digijava.module.aim.helper.ReferenceDoc;
import org.digijava.module.aim.helper.RegionalFunding;
import org.digijava.module.aim.helper.RelatedLinks;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.ActivityVersionUtil;
import org.digijava.module.aim.util.AuditLoggerUtil;
import org.digijava.module.aim.util.ChapterUtil;
import org.digijava.module.aim.util.ComponentsUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DesktopUtil;
import org.digijava.module.aim.util.DocumentUtil;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.aim.util.LuceneUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.contentrepository.action.SelectDocumentDM;
import org.digijava.module.contentrepository.helper.DocumentData;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.helper.TemporaryDocumentData;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;
import org.digijava.module.fundingpledges.dbentity.PledgesEntityHelper;
import org.digijava.module.message.triggers.ActivitySaveTrigger;
import org.digijava.module.message.triggers.ApprovedActivityTrigger;
import org.digijava.module.message.triggers.NotApprovedActivityTrigger;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * SaveActivity class creates a 'AmpActivity' object and populate the fields
 * with the values entered by the user and passes this object to the persister
 * class.
 *
 */
public class SaveActivity extends Action {

	private static final String MODULE_TAG = "save";

	private static Logger logger = Logger.getLogger(SaveActivity.class);

	private ServletContext ampContext = null;
	
	private void processPreStep(EditActivityForm eaForm, AmpActivityVersion activity, TeamMember tm, Boolean[] createdAsDraft) throws Exception, AMPException{
		// if the activity is being added from a users workspace,
		// associate the
		// activity with the team of the current member.
		if (tm != null && (eaForm.isEditAct() == false)) {
			AmpTeam team = TeamUtil.getAmpTeam(tm.getTeamId());
			activity.setTeam(team);
		} else {
			activity.setTeam(null);
		}

		
		if (activity.getCategories() == null) {
			activity.setCategories( new HashSet() );
		}

		if(!eaForm.isEditAct()){
			activity.setCreatedAsDraft(eaForm.getIdentification().getDraft());
			createdAsDraft[0]=eaForm.getIdentification().getDraft();
		}
		else{
			if(eaForm.getIdentification().getWasDraft()&&!eaForm.getIdentification().getDraft()){
				activity.setCreatedAsDraft(false);
				createdAsDraft[0]=false;
			}
			else{
				createdAsDraft[0]=true;
			}
		}
	}
	
	
	
	/**
	 * this method is also called from processStep1
	 * it is necessary in the saveRecovery if processStep1 fails 
	 * 
	 * these are required informations for an activity to be able to be created as draft
	 */ 
	private void processActivityMustHaveInfo(EditActivityForm eaForm, AmpActivityVersion activity) throws Exception, AMPException{
		activity.setName(eaForm.getIdentification().getTitle());
	}
	
	private void processStep1(boolean check, EditActivityForm eaForm, AmpActivityVersion activity, ActionMessages errors, HttpServletRequest request) throws Exception, AMPException{
		AMPException err;
		err = new AMPException(Constants.AMP_ERROR_LEVEL_WARNING, false);
		
		if (check){
			//Do the checks here
			if (eaForm.getIdentification().getTitle() != null) {
				if (eaForm.getIdentification().getTitle().trim().length() == 0) {
					errors.add("title", new ActionMessage(
					"error.aim.addActivity.titleMissing", TranslatorWorker.translateText("Please enter the title")));
				} else if (eaForm.getIdentification().getTitle().length() > 255) {
					errors.add("title", new ActionMessage(
					"error.aim.addActivity.titleTooLong", TranslatorWorker.translateText("Title should be less than 255 characters")));
				}
			}
			
			Long statId=eaForm.getIdentification().getStatusId();
			
			if(isStatusEnabled()){
				if(eaForm.getIdentification().getDraft()==null || !eaForm.getIdentification().getDraft().booleanValue()){
					if (statId != null && statId.equals(new Long(0))) {
						errors.add("status", new ActionMessage(
						"error.aim.addActivity.statusMissing", TranslatorWorker.translateText("Please select the status")));
					}
				}
			}
			

			if (eaForm.getIdentification().getVote() == null || eaForm.getIdentification().getVote().trim().length() == 0){
				//is validate mandatory and activity is on budget then add a new error
				if (Boolean.parseBoolean(eaForm.getIdentification().getBudgetCheckbox()) 
						&& FeaturesUtil.isVisibleField("Validate Mandatory Vote", ampContext)){
					errors.add("budgetVoteMissing",
							new ActionMessage("error.aim.addActivity.budgetVoteMissing", TranslatorWorker.translateText("Please enter Vote under identification section")));
				}else{
					activity.setVote(new String(" "));
				}
			}else{
				activity.setVote(eaForm.getIdentification().getVote());
			}
			
			if (eaForm.getIdentification().getSubVote() == null || eaForm.getIdentification().getSubVote().trim().length() == 0){
				if (Boolean.parseBoolean(eaForm.getIdentification().getBudgetCheckbox()) 
						&& FeaturesUtil.isVisibleField("Validate Mandatory Sub-Vote", ampContext)){
					errors.add("budgetSubVoteMissing",
							new ActionMessage("error.aim.addActivity.budgetSubVoteMissing", TranslatorWorker.translateText("Please enter Sub-Vote under identification section")));
				}else{
					activity.setSubVote(new String(" "));
				}
			}else{
				activity.setSubVote(eaForm.getIdentification().getSubVote());
			}
			
			if (eaForm.getIdentification().getSubProgram() == null || eaForm.getIdentification().getSubProgram().trim().length() == 0){
				if (Boolean.parseBoolean(eaForm.getIdentification().getBudgetCheckbox()) 
						&& FeaturesUtil.isVisibleField("Validate Mandatory Sub-Program", ampContext)){
					errors.add("budgetSubProgramMissing",
							new ActionMessage("error.aim.addActivity.budgetSubProgramMissing", TranslatorWorker.translateText("Please enter Sub Program under identification section")));
				}else{
					activity.setSubProgram(new String(" "));
				}
			}else{
				activity.setSubProgram(eaForm.getIdentification().getSubProgram());
			}
			
			if (eaForm.getIdentification().getProjectCode() == null || eaForm.getIdentification().getProjectCode().trim().length() == 0){
				if (Boolean.parseBoolean(eaForm.getIdentification().getBudgetCheckbox()) 
						&& FeaturesUtil.isVisibleField("Validate Mandatory Project Code", ampContext)){
					errors.add("budgetProjectCodeMissing",
							new ActionMessage("error.aim.addActivity.budgetProjectCodeMissing", TranslatorWorker.translateText("Please enter Project Code under identification section")));
				}else{
					activity.setProjectCode(new String(" "));
				}
			}else{
				activity.setProjectCode(eaForm.getIdentification().getProjectCode());
			}
			
			
			end:
			if (errors.size() > 0){
				//we have all the errors for this step saved and we must throw the amp error
				saveErrors(request, errors);
				throw err;
			}
		}
		
		//Do the initializations and all the information transfer between beans here
		activity.setAmpId(eaForm.getIdentification().getAmpId());
		processActivityMustHaveInfo(eaForm, activity);
		//activity.setBudget(eaForm.getIdentification().getBudget());
		if (eaForm.getIdentification().getDescription() == null
				|| eaForm.getIdentification().getDescription().trim().length() == 0) {
			activity.setDescription(new String(" "));
		} else {
			activity.setDescription(eaForm.getIdentification().getDescription());
		}
		
		if (eaForm.getIdentification().getProjectComments() == null
				|| eaForm.getIdentification().getProjectComments().trim().length() == 0) {
			activity.setProjectComments(new String(" "));
		} else {
			activity.setProjectComments(eaForm.getIdentification().getProjectComments());
		}
		
		if (eaForm.getIdentification().getLessonsLearned() == null
				|| eaForm.getIdentification().getLessonsLearned().trim().length() == 0) {
			activity.setLessonsLearned(new String(" "));
		} else {
			activity.setLessonsLearned(eaForm.getIdentification().getLessonsLearned());
		}

		if (eaForm.getIdentification().getProjectImpact() == null
				|| eaForm.getIdentification().getProjectImpact().trim().length() == 0) {
			activity.setProjectImpact(new String(" "));
		} else {
			activity.setProjectImpact(eaForm.getIdentification().getProjectImpact());
		}
		if(eaForm.getIdentification().getBudgetCV() != null && (eaForm.getIdentification().getBudgetCV().intValue()==0 || eaForm.getIdentification().getBudgetCV().intValue()==1))
			activity.setChapter(null);
		else
			activity.setChapter(ChapterUtil.getChapterByCode(eaForm.getIdentification().getChapterCode()));
		if (eaForm.getIdentification().getActivitySummary() == null
				|| eaForm.getIdentification().getActivitySummary().trim().length() == 0) {
			activity.setActivitySummary(new String(" "));
		} else {
			activity.setActivitySummary(eaForm.getIdentification().getActivitySummary());
		}

		if (eaForm.getIdentification().getContractingArrangements() == null
				|| eaForm.getIdentification().getContractingArrangements().trim().length() == 0) {
			activity.setContractingArrangements(new String(" "));
		} else {
			activity.setContractingArrangements(eaForm.getIdentification().getContractingArrangements());
		}

		if (eaForm.getIdentification().getCondSeq() == null
				|| eaForm.getIdentification().getCondSeq().trim().length() == 0) {
			activity.setCondSeq(new String(" "));
		} else {
			activity.setCondSeq(eaForm.getIdentification().getCondSeq());
		}

		if (eaForm.getIdentification().getLinkedActivities() == null
				|| eaForm.getIdentification().getLinkedActivities().trim().length() == 0) {
			activity.setLinkedActivities(new String(" "));
		} else {
			activity.setLinkedActivities(eaForm.getIdentification().getLinkedActivities());
		}

		if (eaForm.getIdentification().getConditionality() == null
				|| eaForm.getIdentification().getConditionality().trim().length() == 0) {
			activity.setConditionality(new String(" "));
		} else {
			activity.setConditionality(eaForm.getIdentification().getConditionality());
		}

		if (eaForm.getIdentification().getProjectManagement() == null
				|| eaForm.getIdentification().getProjectManagement().trim().length() == 0) {
			activity.setProjectManagement(new String(" "));
		} else {
			activity.setProjectManagement(eaForm.getIdentification().getProjectManagement());
		}

		if (eaForm.getIdentification().getPurpose() == null
				|| eaForm.getIdentification().getPurpose().trim().length() == 0) {
			activity.setPurpose(new String(" "));
		} else {
			activity.setPurpose(eaForm.getIdentification().getPurpose());
		}
		if (eaForm.getIdentification().getResults() == null
				|| eaForm.getIdentification().getResults().trim().length() == 0) {
			activity.setResults(new String(" "));
		} else {
			activity.setResults(eaForm.getIdentification().getResults());
		}
		if (eaForm.getIdentification().getObjectives() == null
				|| eaForm.getIdentification().getObjectives().trim().length() == 0) {
			activity.setObjective(new String(" "));
		} else {
			activity.setObjective(eaForm.getIdentification().getObjectives());
		}

		if (eaForm.getIdentification().getStatusReason() == null 
				|| eaForm.getIdentification().getStatusReason().trim().length() == 0) {
			activity.setStatusReason(" ");
		} else {
			activity.setStatusReason(eaForm.getIdentification().getStatusReason().trim());
		}
		
		if (eaForm.getContracts().getContractDetails() == null
				|| eaForm.getContracts().getContractDetails().trim().length() == 0) {
			activity.setContractDetails(new String(" "));
		} else {
			activity.setContractDetails(eaForm.getContracts().getContractDetails());
		}
		
		
		/*
		 * tanzania adds
		 */
//		if (eaForm.getIdentification().getFY() == null || eaForm.getIdentification().getFY().trim().length() == 0)
//			activity.setFY(new String(" "));
//		else
//			activity.setFY(eaForm.getIdentification().getFY());
		if(eaForm.getIdentification().getResetselectedFYs()!=null && eaForm.getIdentification().getResetselectedFYs()){
			eaForm.getIdentification().setSelectedFYs(null);
		}
		
		if (eaForm.getIdentification().getSelectedFYs() == null || eaForm.getIdentification().getSelectedFYs().length == 0){
			activity.setFY(new String(" "));
		}else{
			String [] selectedFYs = eaForm.getIdentification().getSelectedFYs();
			String fy ="";
			for(int i=0;i<selectedFYs.length;i++){
				fy+=selectedFYs[i];
				if(i!=selectedFYs.length-1){
					fy+=",";
				}				
			}
			activity.setFY(fy);
		}
		
		
		
		activity.setGovernmentApprovalProcedures(eaForm.getIdentification().getGovernmentApprovalProcedures());

		if (eaForm.getIdentification().getGovAgreementNumber() == null
				|| eaForm.getIdentification().getGovAgreementNumber().trim().length() == 0)
			activity.setGovAgreementNumber(new String(" "));
		else
			activity.setGovAgreementNumber(eaForm.getIdentification().getGovAgreementNumber());
		
		if (eaForm.getIdentification().getBudgetCodeProjectID() == null
				|| eaForm.getIdentification().getBudgetCodeProjectID().trim().length() == 0)
			activity.setBudgetCodeProjectID(new String(" "));
		else
			activity.setBudgetCodeProjectID(eaForm.getIdentification().getBudgetCodeProjectID());
		
		//Budget sector classification
		if (eaForm.getIdentification().getSelectedbudgedsector() != null){
			activity.setBudgetsector(eaForm.getIdentification().getSelectedbudgedsector());
		}
		if (eaForm.getIdentification().getSelectedorg() != null){
			activity.setBudgetorganization(eaForm.getIdentification().getSelectedorg());
		}
		if(eaForm.getIdentification().getBudgetCV() != null && (eaForm.getIdentification().getBudgetCV().intValue()!=0 || eaForm.getIdentification().getBudgetCV().intValue()!=1 )){
			if (eaForm.getIdentification().getSelecteddepartment()!=null){
				activity.setBudgetdepartment(eaForm.getIdentification().getSelecteddepartment());
			}
			if (eaForm.getIdentification().getSelectedprogram()!=null){
				activity.setBudgetprogram(eaForm.getIdentification().getSelectedprogram());
			}
		}
		
		if (eaForm.getIdentification().getCrisNumber() == null
				|| eaForm.getIdentification().getCrisNumber().trim().length() == 0)
			activity.setCrisNumber(new String(" "));
		else
			activity.setCrisNumber(eaForm.getIdentification().getCrisNumber());

		activity.setJointCriteria(eaForm.getIdentification().getJointCriteria());

		activity.setHumanitarianAid(eaForm.getIdentification().getHumanitarianAid());

		if (eaForm.getIdentification().getConditions() == null
				|| eaForm.getIdentification().getConditions().trim().length() == 0) {
			activity.setCondition(" ");
		} else {
			activity.setCondition(eaForm.getIdentification().getConditions());
		}

		try {
			activity.setLineMinRank(Integer.valueOf(eaForm.getPlanning().getLineMinRank()));
			if (activity.getLineMinRank().intValue() < 1 || activity.getLineMinRank().intValue() > 5) {
				logger.debug("Line Ministry Rank is out of permisible range (1 to 5)");
				activity.setLineMinRank(null);
			}
		}
		catch (NumberFormatException nex) {
			logger.debug("Line Ministry Rank is not a number : " + nex);
			activity.setLineMinRank(null);
		}
		try {
			activity.setPlanMinRank(Integer.valueOf(eaForm.getPlanning().getPlanMinRank()));
			if (activity.getPlanMinRank().intValue() < 1 || activity.getPlanMinRank().intValue() > 5) {
				logger.debug("Plan Ministry Rank is out of permisible range (1 to 5)");
				activity.setPlanMinRank(null);
			}
		}
		catch (NumberFormatException nex) {
			logger.debug("Plan Ministry Rank is not a number : " + nex);
			activity.setPlanMinRank(null);
		}

		activity.setProposedApprovalDate(DateConversion.getDate(eaForm
				.getPlanning().getOriginalAppDate()));
		activity.setActualApprovalDate(DateConversion.getDate(eaForm
				.getPlanning().getRevisedAppDate()));
		activity.setProposedStartDate(DateConversion.getDate(eaForm
				.getPlanning().getOriginalStartDate()));
		activity.setActualStartDate(DateConversion.getDate(eaForm
				.getPlanning().getRevisedStartDate()));
		activity.setActualCompletionDate(DateConversion.getDate(eaForm
				.getPlanning().getCurrentCompDate()));
		activity.setOriginalCompDate(DateConversion.getDate(eaForm
				.getPlanning().getProposedCompDate()));
		activity.setContractingDate(DateConversion.getDate(eaForm
				.getPlanning().getContractingDate()));
		activity.setDisbursmentsDate(DateConversion.getDate(eaForm
				.getPlanning().getDisbursementsDate()));
        activity.setProposedCompletionDate(DateConversion.getDate(eaForm
        		.getPlanning().getProposedCompDate()));

		// set activity internal ids
		Set internalIds = new HashSet();
		if (eaForm.getIdentification().getSelectedOrganizations() != null) {
			OrgProjectId orgProjId[] = eaForm
					.getIdentification().getSelectedOrganizations();
			for (int i = 0; i < orgProjId.length; i++) {
				AmpActivityInternalId actInternalId = new AmpActivityInternalId();
				if(orgProjId[i] != null){
					actInternalId.setAmpActivity(activity);
					actInternalId.setOrganisation(DbUtil
							.getOrganisation(orgProjId[i].getOrganisation().getAmpOrgId()));
					actInternalId
							.setInternalId(orgProjId[i].getProjectId());
					internalIds.add(actInternalId);
				}
			}
		}
		//activity.getInternalIds().clear();
		activity.setInternalIds(internalIds);

		/* Saving categories to AmpActivityVersion */
		CategoryManagerUtil.addCategoryToSet(eaForm.getIdentification().getProcurementSystem(), activity.getCategories() );
		CategoryManagerUtil.addCategoryToSet(eaForm.getIdentification().getReportingSystem(), activity.getCategories() );
		CategoryManagerUtil.addCategoryToSet(eaForm.getIdentification().getAuditSystem(), activity.getCategories() );
		CategoryManagerUtil.addCategoryToSet(eaForm.getIdentification().getInstitutions(), activity.getCategories() );
		CategoryManagerUtil.addCategoryToSet(eaForm.getIdentification().getAccessionInstrument(), activity.getCategories() );
		CategoryManagerUtil.addCategoryToSet(eaForm.getIdentification().getAcChapter(), activity.getCategories() );
		CategoryManagerUtil.addCategoryToSet(eaForm.getIdentification().getStatusId(), activity.getCategories() );
		CategoryManagerUtil.addCategoryToSet(eaForm.getLocation().getLevelId(), activity.getCategories() );
		CategoryManagerUtil.addCategoryToSet(eaForm.getIdentification().getGbsSbs(), activity.getCategories() );
        CategoryManagerUtil.addCategoryToSet(eaForm.getLocation().getImplemLocationLevel(), activity.getCategories() );
        CategoryManagerUtil.addCategoryToSet(eaForm.getIdentification().getActivityLevel(), activity.getCategories());
        CategoryManagerUtil.addCategoryToSet(eaForm.getIdentification().getProjectCategory(), activity.getCategories());
        CategoryManagerUtil.addCategoryToSet(eaForm.getIdentification().getProjectImplUnitId(), activity.getCategories());
        CategoryManagerUtil.addCategoryToSet(eaForm.getIdentification().getBudgetCV(), activity.getCategories());
		/* END - Saving categories to AmpActivityVersion */
			
        
		
		
		activity.setComments(" ");

		
        activity.setDraft(eaForm.getIdentification().getDraft());

		
	}
	
	
	private void processStep1_5(boolean check, EditActivityForm eaForm, AmpActivityVersion activity, ActionMessages errors, HttpServletRequest request) throws Exception, AMPException{
		AMPException err;
		err = new AMPException(Constants.AMP_ERROR_LEVEL_WARNING, false);
		
		if (check){
			
			
			end:
			if (errors.size() > 0){
				//we have all the errors for this step saved and we must throw the amp error
				saveErrors(request, errors);
				throw err;
			}
		}
		
		//Do the initializations and all the information transfer between beans here
		//List formRefDocs=eaForm.getDocuments().getReferenceDocs();
		ReferenceDoc[] myrefDoc = eaForm.getDocuments().getReferenceDocs();
    	Set<AmpActivityReferenceDoc> resultRefDocs=new HashSet<AmpActivityReferenceDoc>();
    	if(myrefDoc!=null && myrefDoc.length!=0){
	    	for(int i=0;i<myrefDoc.length;i++){
				ReferenceDoc refDoc = myrefDoc[i];
				if(ArrayUtils.contains(eaForm.getDocuments().getAllReferenceDocNameIds(), refDoc.getCategoryValueId())){
					AmpActivityReferenceDoc dbRefDoc=null;//categoryRefDocMap.get(refDoc.getCategoryValueId());
					if (refDoc.getChecked() == true){
						dbRefDoc=new AmpActivityReferenceDoc();
						dbRefDoc.setCreated(new Date());
						AmpCategoryValue catVal=CategoryManagerUtil.getAmpCategoryValueFromDb(refDoc.getCategoryValueId());
						dbRefDoc.setCategoryValue(catVal);
						dbRefDoc.setActivity(activity);
					//}
					dbRefDoc.setActivity(activity);
					dbRefDoc.setComment(refDoc.getComment());
					dbRefDoc.setLastEdited(new Date());
					resultRefDocs.add(dbRefDoc);
	
				}else{
					dbRefDoc=null;
					resultRefDocs.add(dbRefDoc);
					}
				}
			}
			activity.setReferenceDocs(resultRefDocs);
    	}
	}
	
	
	private void processStep2(boolean check,EditActivityForm eaForm, AmpActivityVersion activity, ActionMessages errors, HttpServletRequest request, HttpSession session) throws Exception, AMPException{
		
		AMPException err;
		err = new AMPException(Constants.AMP_ERROR_LEVEL_WARNING, false);
		
		eaForm.getSectors().setPrimarySectorVisible(FeaturesUtil.isVisibleSectors("Primary", ampContext)?"true":"false");
		eaForm.getSectors().setSecondarySectorVisible(FeaturesUtil.isVisibleSectors("Secondary", ampContext)?"true":"false");
        eaForm.getSectors().setTertiarySectorVisible(FeaturesUtil.isVisibleSectors("Tertiary", ampContext)?"true":"false");
		session.setAttribute("Primary Sector", eaForm.getSectors().getPrimarySectorVisible());
		session.setAttribute("Secondary Sector", eaForm.getSectors().getSecondarySectorVisible());
        session.setAttribute("Tertiary Sector", eaForm.getSectors().getTertiarySectorVisible());
		
		if (check){
			//Do the checks here
			if(eaForm.getIdentification().getDraft()==null || !eaForm.getIdentification().getDraft().booleanValue()){
				if(isSectorEnabled()){
					if (eaForm.getSectors().getActivitySectors() == null || eaForm.getSectors().getActivitySectors().size() < 1) {
						errors.add("sector", new ActionMessage("error.aim.addActivity.sectorEmpty", TranslatorWorker.translateText("Please add a sector")));
					}
					else{
						float primaryPrc=0, secondaryPrc=0, tertiaryPrc=0;
						boolean hasPrimarySectorsAdded=false, hasSecondarySectorsAdded=false, hasTertiarySectorsAdded=false;
						
						Iterator<ActivitySector> secPerItr = eaForm.getSectors().getActivitySectors().iterator();
						while (secPerItr.hasNext()) {
							ActivitySector actSect = (ActivitySector) secPerItr.next();
							AmpClassificationConfiguration config=SectorUtil.getClassificationConfigById(actSect.getConfigId());
							if("Primary".equals(config.getName())) 
								hasPrimarySectorsAdded=true;
							if("Secondary".equals(config.getName())) 
								hasSecondarySectorsAdded=true;
                            if("Tertiary".equals(config.getName()))
								hasTertiarySectorsAdded=true;
							
							if (null == actSect.getSectorPercentage() || "".equals(actSect.getSectorPercentage())) {
								errors.add("sectorPercentageEmpty", new ActionMessage("error.aim.addActivity.sectorPercentageEmpty", TranslatorWorker.translateText("Please enter sector percentage")));
							}
							// sector percentage is not a number
							else {
								try {
									if("Primary".equals(config.getName())) primaryPrc+=actSect.getSectorPercentage().floatValue();
									if("Secondary".equals(config.getName())) secondaryPrc+=actSect.getSectorPercentage().floatValue();
                                    if("Tertiary".equals(config.getName())) tertiaryPrc+=actSect.getSectorPercentage().floatValue();
								} catch (NumberFormatException nex) {
									errors.add("sectorPercentageNonNumeric",
											new ActionMessage("error.aim.addActivity.sectorPercentageNonNumeric", TranslatorWorker.translateText("Sector percentage must be numeric")));
								}
							}
						}
						
						if (isPrimarySectorEnabled() && isInConfig(eaForm,"Primary")){
							if(!hasPrimarySectorsAdded){
								if (FeaturesUtil.isVisibleField("Validate Mandatory Primary Sector", ampContext)){
								errors.add("noPrimarySectorsAdded",
										new ActionMessage("error.aim.addActivity.noPrimarySectorsAdded", TranslatorWorker.translateText("please add primary sectors")));
							}
							}
							else if(primaryPrc!=100)
								errors.add("primarySectorPercentageSumWrong", new ActionMessage("error.aim.addActivity.primarySectorPercentageSumWrong", TranslatorWorker.translateText("Sum of all primary sector percentage must be 100")));						
						}

						
						if (isSecondarySectorEnabled() && isInConfig(eaForm, "Secondary")){
							if(!hasSecondarySectorsAdded){
								if (FeaturesUtil.isVisibleField("Validate Mandatory Secondary Sector", ampContext)){
								errors.add("noSecondarySectorsAdded",
										new ActionMessage("error.aim.addActivity.noSecondarySectorsAdded", TranslatorWorker.translateText("please add secondary sectors")));								
							}
							}
							else if(hasSecondarySectorsAdded && secondaryPrc!=100)
								errors.add("secondarySectorPercentageSumWrong", new ActionMessage("error.aim.addActivity.secondarySectorPercentageSumWrong", TranslatorWorker.translateText("Sum of all secondary sector percentage must be 100")));							
						}
                        if (Boolean.parseBoolean(eaForm.getSectors().getTertiarySectorVisible()) && isInConfig(eaForm, "Tertiary")){
							if(!hasTertiarySectorsAdded){
								if (FeaturesUtil.isVisibleField("Validate Mandatory Tertiary Sector", ampContext)){
								errors.add("noTertiarySectorsAdded",
										new ActionMessage("error.aim.addActivity.noTertiarySectorsAdded", TranslatorWorker.translateText("please add tertiary sectors")));
							}
							}
							else if(hasTertiarySectorsAdded && tertiaryPrc!=100)
								errors.add("tertiarySectorPercentageSumWrong", new ActionMessage("error.aim.addActivity.tertiarySectorPercentageSumWrong", TranslatorWorker.translateText("Sum of all tertiary sector percentage must be 100")));
						}

					}
				}
				
				//check if the FM option is enabled to check location percentages
				if (FeaturesUtil.isVisibleField("Validate Mandatory Regional Percentage", ampContext)){
					if(eaForm.getLocation().getSelectedLocs() != null && eaForm.getLocation().getSelectedLocs().size()>0){
						Iterator<Location> itr = eaForm.getLocation().getSelectedLocs().iterator();
						//Double totalPercentage = 0d;
						BigDecimal totalPercentage = new BigDecimal(0);
						while (itr.hasNext()) {
							Location loc = itr.next();
							// Not yet implemented.
							//Double percentage=FormatHelper.parseDouble(loc.getPercent());
							Double percentage = new Double(loc.getPercent());							
							if(percentage != null)
								totalPercentage = totalPercentage.add(new BigDecimal(percentage));
						}
						Double totaltocompare = totalPercentage.setScale(1,BigDecimal.ROUND_UP).doubleValue();
						//Checks if it's 100%
						if (totaltocompare!=100){
							errors.add("locationPercentageSumWrong",
									new ActionMessage("error.aim.addActivity.locationPercentageSumWrong", TranslatorWorker.translateText("Sum of all location percentage must be 100")));
						}
					}
				}
				
				if (eaForm.getPrograms().getNationalPlanObjectivePrograms() != null
						&& eaForm.getPrograms().getNationalPlanObjectivePrograms().size() > 0) {
					boolean failNOP = false;
					Iterator<AmpActivityProgram> npoIt = eaForm.getPrograms().getNationalPlanObjectivePrograms().iterator();
					Double totalPercentage = 0d;
					while (npoIt.hasNext()) {
						AmpActivityProgram activityProgram = npoIt.next();
						totalPercentage += activityProgram.getProgramPercentage();
						if(activityProgram.getProgramPercentage() <= 0){
							failNOP = true;
						}
					}
					if (totalPercentage != 100) {
						errors.add("nationalPlanProgramsPercentageSumWrong",
								new ActionMessage("error.aim.addActivity.nationalPlanProgramsPercentageSumWrong", TranslatorWorker.translateText("Sum of all National Planning Objective percentages must be 100")));
					}
					if(failNOP == true) {
						errors.add("nationalPlanProgramsPercentageWrong",
								new ActionMessage("error.aim.addActivity.nationalPlanProgramsPercentageWrong", TranslatorWorker.translateText("Please enter all National Planning Objective percentages")));
					}
				}
				
				if (eaForm.getPrograms().getPrimaryPrograms()!= null
						&& eaForm.getPrograms().getPrimaryPrograms().size() > 0) {
					Iterator<AmpActivityProgram> ppIt = eaForm.getPrograms().getPrimaryPrograms().iterator();
					Double totalPercentage = 0d;
					while (ppIt.hasNext()) {
						AmpActivityProgram activityProgram = ppIt.next();
						totalPercentage += activityProgram.getProgramPercentage();
					}
					if (totalPercentage != 100)
						errors.add("primaryProgramsPercentageSumWrong",
								new ActionMessage("error.aim.addActivity.primaryProgramsPercentageSumWrong", TranslatorWorker.translateText("Sum of all Primary Program percentages must be 100")));
				}
				
				if (eaForm.getPrograms().getSecondaryPrograms()!= null
						&& eaForm.getPrograms().getSecondaryPrograms().size() > 0) {
					Iterator<AmpActivityProgram> spIt = eaForm.getPrograms().getSecondaryPrograms().iterator();
					Double totalPercentage = 0d;
					while (spIt.hasNext()) {
						AmpActivityProgram activityProgram = spIt.next();
						totalPercentage += activityProgram.getProgramPercentage();
					}
					if (totalPercentage != 100)
						errors.add("secondaryProgramsPercentageSumWrong",
								new ActionMessage("error.aim.addActivity.secondaryProgramsPercentageSumWrong", TranslatorWorker.translateText("Sum of all Secondary Program percentages must be 100")));
				}
			}
			
			end:
			if (errors.size() > 0){
				//we have all the errors for this step saved and we must throw the amp error
				saveErrors(request, errors);
				throw err;
			}
		}

		//Do the initializations and all the information transfer between beans here
		if (eaForm.getCrossIssues().getEqualOpportunity() == null
				|| eaForm.getCrossIssues().getEqualOpportunity().trim().length() == 0) {
			activity.setEqualOpportunity(new String(" "));
		} else {
			activity.setEqualOpportunity(eaForm.getCrossIssues().getEqualOpportunity());
		}

		if (eaForm.getCrossIssues().getEnvironment() == null
				|| eaForm.getCrossIssues().getEnvironment().trim().length() == 0) {
			activity.setEnvironment(new String(" "));
		} else {
			activity.setEnvironment(eaForm.getCrossIssues().getEnvironment());
		}

		if (eaForm.getCrossIssues().getMinorities() == null
				|| eaForm.getCrossIssues().getMinorities().trim().length() == 0) {
			activity.setMinorities(new String(" "));
		} else {
			activity.setMinorities(eaForm.getCrossIssues().getMinorities());
		}

		// set the sectors
		if (eaForm.getSectors().getActivitySectors() != null && eaForm.getSectors().getActivitySectors().size()>0) {
			Set sectors = new HashSet();
			if (eaForm.getSectors().getActivitySectors() != null) {
				Iterator itr = eaForm.getSectors().getActivitySectors().iterator();
				while (itr.hasNext()) {
					ActivitySector actSect = (ActivitySector) itr.next();
					Long sectorId = null;
					if (actSect.getSubsectorLevel2Id() != null
							&& (!actSect.getSubsectorLevel2Id().equals(new Long(-1)))) {
						sectorId = actSect.getSubsectorLevel2Id();
					} else if (actSect.getSubsectorLevel1Id() != null
							&& (!actSect.getSubsectorLevel1Id().equals(new Long(-1)))) {
						sectorId = actSect.getSubsectorLevel1Id();
					} else {
						sectorId = actSect.getSectorId();
					}
					AmpActivitySector amps = new AmpActivitySector();
					amps.setActivityId(activity);
					if (sectorId != null && (!sectorId.equals(new Long(-1))))
						amps.setSectorId(SectorUtil.getAmpSector(sectorId));
					amps.setSectorPercentage(actSect.getSectorPercentage());
                                                amps.setClassificationConfig(SectorUtil.getClassificationConfigById(actSect.getConfigId()));
					sectors.add(amps);
				}
			}
			activity.setSectors(sectors);
		}

		Set<AmpActivityProgram> programs = new HashSet<AmpActivityProgram>();
		if (eaForm.getPrograms().getNationalPlanObjectivePrograms() != null && eaForm.getPrograms().getNationalPlanObjectivePrograms().size()>0){
			if(eaForm.getPrograms().getNationalPlanObjectivePrograms() != null){
				Iterator<AmpActivityProgram> itr = eaForm.getPrograms().getNationalPlanObjectivePrograms().iterator();

				while(itr.hasNext()){
					AmpActivityProgram program = (AmpActivityProgram) itr.next();
					AmpActivityProgram newProgram=new AmpActivityProgram();
					newProgram.setActivity(activity);
					newProgram.setProgram(program.getProgram());
					newProgram.setProgramPercentage(program.getProgramPercentage());
					newProgram.setProgramSetting(program.getProgramSetting());
                    programs.add(newProgram);
				}
			}
		}
		if (eaForm.getPrograms().getPrimaryPrograms() != null && eaForm.getPrograms().getPrimaryPrograms().size()>0){
			if(eaForm.getPrograms().getPrimaryPrograms() != null){
				Iterator<AmpActivityProgram> itr = eaForm.getPrograms().getPrimaryPrograms().iterator();

				while(itr.hasNext()){
					AmpActivityProgram program = (AmpActivityProgram) itr.next();
					AmpActivityProgram newProgram=new AmpActivityProgram();
					newProgram.setActivity(activity);
					newProgram.setProgram(program.getProgram());
					newProgram.setProgramPercentage(program.getProgramPercentage());
					newProgram.setProgramSetting(program.getProgramSetting());
                    programs.add(newProgram);
				}
			}
		}
		if (eaForm.getPrograms().getSecondaryPrograms() != null && eaForm.getPrograms().getSecondaryPrograms().size()>0){
			if(eaForm.getPrograms().getSecondaryPrograms() != null){
				Iterator<AmpActivityProgram> itr = eaForm.getPrograms().getSecondaryPrograms().iterator();

				while(itr.hasNext()){
					AmpActivityProgram program = (AmpActivityProgram) itr.next();
					AmpActivityProgram newProgram=new AmpActivityProgram();
					newProgram.setActivity(activity);
					newProgram.setProgram(program.getProgram());
					newProgram.setProgramPercentage(program.getProgramPercentage());
					newProgram.setProgramSetting(program.getProgramSetting());
                    programs.add(newProgram);
				}
			}
		}
		activity.setActPrograms(programs);
		
//		if (eaForm.getComponents().getActivityComponentes() != null) {
//			Set componentes = new HashSet();
//			if (eaForm.getComponents().getActivityComponentes() != null && eaForm.getComponents().getActivityComponentes().size()>0) {
//				Iterator itr = eaForm.getComponents().getActivityComponentes().iterator();
//				while (itr.hasNext()) {
//					ActivitySector actSect = (ActivitySector) itr.next();
//					Long sectorId = null;
//					if (actSect.getSubsectorLevel2Id() != null
//							&& (!actSect.getSubsectorLevel2Id().equals(new Long(-1)))) {
//						sectorId = actSect.getSubsectorLevel2Id();
//					} else if (actSect.getSubsectorLevel1Id() != null
//							&& (!actSect.getSubsectorLevel1Id().equals(new Long(-1)))) {
//						sectorId = actSect.getSubsectorLevel1Id();
//					} else {
//						sectorId = actSect.getSectorId();
//					}
//					AmpActivityComponente ampc = new AmpActivityComponente();
//					ampc.setActivity(activity);
//					if (sectorId != null && (!sectorId.equals(new Long(-1))))
//						ampc.setSector(SectorUtil.getAmpSector(sectorId));
//					ampc.setPercentage(new Float(actSect.getSectorPercentage()));
//					componentes.add(ampc);
//				}
//			}
//			activity.setComponentes(componentes);
//		}

		if (eaForm.getPrograms().getProgram() != null
				&& (!eaForm.getPrograms().getProgram().equals(new Long(-1)))) {
			AmpTheme theme = ProgramUtil.getThemeObject(eaForm.getPrograms().getProgram());
			if (theme != null) {
				activity.setThemeId(theme);
			}
		}
		if (eaForm.getPrograms().getProgramDescription() != null
				&& eaForm.getPrograms().getProgramDescription().trim().length() != 0) {
			activity.setProgramDescription(eaForm.getPrograms()
					.getProgramDescription());
		} else {
			activity.setProgramDescription(" ");
		}

		// set locations
		if (eaForm.getLocation().getSelectedLocs() != null && eaForm.getLocation().getSelectedLocs().size()>0) {
			Set<AmpActivityLocation> locations = new HashSet<AmpActivityLocation>();
			Iterator<Location> itr = eaForm.getLocation().getSelectedLocs().iterator();
			while (itr.hasNext()) {
				Location loc = itr.next();
                                        String countryIso=loc.getNewCountryId();
				//AmpLocation ampLoc = LocationUtil.getAmpLocation(countryIso, loc.getRegionId(), loc.getZoneId(), loc.getWoredaId());
                AmpLocation ampLoc		= LocationUtil.getAmpLocationByCVLocation( loc.getLocId() );

				if (ampLoc == null) {
					ampLoc = new AmpLocation();
					//ampLoc.setCountry(loc.getCountry());
						//if(countryIso!=null){
								//ampLoc.setDgCountry(DbUtil.getDgCountry(loc.getNewCountryId()));
                        //}
					//ampLoc.setRegion(loc.getRegion());
					//ampLoc.setAmpRegion(LocationUtil.getAmpRegion(loc.getRegionId()));
					//ampLoc.setAmpZone(LocationUtil.getAmpZone(loc.getZoneId()));
					//ampLoc.setAmpWoreda(LocationUtil.getAmpWoreda(loc.getWoredaId()));
					ampLoc.setDescription(new String(" "));
					
					ampLoc.setLocation( loc.getAmpCVLocation() );
					if ( loc.getAmpCVLocation() != null ) {
						AmpCategoryValueLocations regionLocation	= 
							DynLocationManagerUtil.getAncestorByLayer(loc.getAmpCVLocation(), CategoryConstants.IMPLEMENTATION_LOCATION_REGION );
						if ( regionLocation != null ) {
							ampLoc.setRegionLocation(regionLocation);
							ampLoc.setRegion( regionLocation.getName() );
						}
					}
					
					DbUtil.add(ampLoc);
				}

				//AMP-2250
				AmpActivityLocation actLoc=new AmpActivityLocation();
				actLoc.setActivity(activity);//activity);
				actLoc.getActivity().setAmpActivityId(eaForm.getActivityId());
				actLoc.setLocation(ampLoc);
				Double percent=null;
				if(loc.getPercent()!=null && loc.getPercent().length()>0) {
					//percent=FormatHelper.parseDouble(loc.getPercent());
					percent = new Double(loc.getPercent());
					actLoc.setLocationPercentage(percent.floatValue());
				}
				locations.add(actLoc);
				//locations.add(ampLoc);
				//AMP-2250
			}
			activity.setLocations(locations);
		}
		if(eaForm.getLocation().getSelectedLocs() != null && eaForm.getLocation().getSelectedLocs().size() == 1){
			Iterator<Location> itr = eaForm.getLocation().getSelectedLocs().iterator();
			while (itr.hasNext()) {
				Location loc = itr.next();
				if(loc.getRegionName()==null && eaForm.getFunding().getRegionalFundings()!=null){
					eaForm.getFunding().getRegionalFundings().clear();
		 	      }
			 }
		}else if (eaForm.getLocation().getSelectedLocs()==null || eaForm.getLocation().getSelectedLocs().size() == 0){
			if (eaForm.getFunding().getRegionalFundings()!=null) eaForm.getFunding().getRegionalFundings().clear();
		}


		//Set programs = new HashSet();
		/*List activityNPO = eaForm.getPrograms().getNationalPlanObjectivePrograms();
		List activityPP = eaForm.getPrograms().getPrimaryPrograms();
		List activitySP = eaForm.getPrograms().getSecondaryPrograms();
		if (activityNPO != null) {
		        programs.addAll(activityNPO);
		}
		if (activityPP != null) {
		        programs.addAll(activityPP);
		}
		if (activitySP != null) {
		        programs.addAll(activitySP);
		}
		activity.setActPrograms(programs);
		 */
		
		
		
		
	}

	private void processStep3(boolean check, EditActivityForm eaForm, AmpActivityVersion activity, ActionMessages errors, HttpServletRequest request) throws Exception, AMPException{
		AMPException err;
		err = new AMPException(Constants.AMP_ERROR_LEVEL_WARNING, false);
		
		if (check){
//			//Do the checks here
//			Date activityActualStartDate= DateConversion.getDate(eaForm.getPlanning().getRevisedStartDate());
//			if(activityActualStartDate!=null){
//				Collection<FundingDetail> fundDets = eaForm.getFunding().getFundingDetails();
//				if(fundDets!=null && fundDets.size()>0){
//					for (FundingDetail fundingDetail : fundDets) {
//						Date transDate = DateConversion.getDate(fundingDetail.getTransactionDate());
//						if(transDate.before(activityActualStartDate)){
//							errors.add("",new ActionError("error.aim.invalidFundingDate", TranslatorWorker.translateText("Funding contains transaction, which precedes activity actual start date. Please change date", request)));
//						}
//					}
//				}
//			}
			
			
			end:
			if (errors.size() > 0){
				//we have all the errors for this step saved and we must throw the amp error
				saveErrors(request, errors);
				throw err;
			}
		}
		
		//Do the initializations and all the information transfer between beans here
        if(eaForm.getFunding().getProProjCost()==null){
            activity.setFunAmount(null);
            activity.setFunDate(null);
            activity.setCurrencyCode(null);
        }else{
            activity.setFunAmount(eaForm.getFunding().getProProjCost().getFunAmountAsDouble());
            //check for null on bolivia
            if(eaForm.getFunding().getProProjCost().getFunDate()!=null){
        	activity.setFunDate(FormatHelper.parseDate(eaForm.getFunding().getProProjCost().getFunDate()).getTime());
            }
            activity.setCurrencyCode(eaForm.getFunding().getProProjCost().getCurrencyCode());
        }
        
		// set organizations role
		Set orgRole = new HashSet();
		if (eaForm.getFunding().getFundingOrganizations() != null && eaForm.getFunding().getFundingOrganizations().size()>0) { // funding

														// organizations
			AmpRole role = DbUtil.getAmpRole(Constants.FUNDING_AGENCY);
			Iterator itr = eaForm.getFunding().getFundingOrganizations().iterator();
			while (itr.hasNext()) {
				FundingOrganization fOrg = (FundingOrganization) itr
						.next();
				AmpOrganisation ampOrg = DbUtil.getOrganisation(fOrg
						.getAmpOrgId());
				AmpOrgRole ampOrgRole = new AmpOrgRole();
				ampOrgRole.setActivity(activity);
				ampOrgRole.setRole(role);
				ampOrgRole.setOrganisation(ampOrg);
				orgRole.add(ampOrgRole);
			}
			activity.setOrgrole(orgRole);
		}

        
		//set funding and funding details
		Set fundings = new HashSet();
		if (eaForm.getFunding().getFundingOrganizations() != null && eaForm.getFunding().getFundingOrganizations().size()>0) {
			Iterator itr1 = eaForm.getFunding().getFundingOrganizations().iterator();
			while (itr1.hasNext()) {
				FundingOrganization fOrg = (FundingOrganization) itr1.next();
				//add fundings
				if (fOrg.getFundings() != null && fOrg.getFundings().size()>0) {
					Iterator itr2 = fOrg.getFundings().iterator();
					while (itr2.hasNext()) {
						Funding fund = (Funding) itr2.next();
						AmpFunding ampFunding = new AmpFunding();
						ampFunding.setActive(fOrg.getFundingActive());
						if("unchecked".equals(fOrg.getDelegatedCooperationString()) || fOrg.getDelegatedCooperation()==null)
							ampFunding.setDelegatedCooperation(false);
						else
							ampFunding.setDelegatedCooperation(true);
						if("unchecked".equals(fOrg.getDelegatedPartnerString()) || fOrg.getDelegatedPartner()==null)
							ampFunding.setDelegatedPartner(false);
						else
							ampFunding.setDelegatedPartner(true);

						ampFunding.setAmpDonorOrgId(DbUtil.getOrganisation(fOrg.getAmpOrgId()));
						ampFunding.setFinancingId(fund.getOrgFundingId());
						/*
						 * if (fund.getSignatureDate() != null)
						 * ampFunding.setSignatureDate(DateConversion
						 * .getDate(fund.getSignatureDate()));
						 */
						//ampFunding.setModalityId(fund.getModality());
						if (fund.getConditions() != null
								&& fund.getConditions().trim().length() != 0) {
							ampFunding.setConditions(fund.getConditions());
						} else {
							ampFunding.setConditions(new String(" "));
						}
						ampFunding.setComments(new String(" "));
						/*ampFunding.setAmpTermsAssistId(fund.getAmpTermsAssist());*/
						
						ampFunding.setFinancingInstrument( fund.getFinancingInstrument() );
						ampFunding.setTypeOfAssistance( fund.getTypeOfAssistance() );
						ampFunding.setFundingStatus( fund.getFundingStatus() );
						ampFunding.setModeOfPayment( fund.getModeOfPayment() );
						ampFunding.setDonorObjective( fund.getDonorObjective() );
						ampFunding.setGroupVersionedFunding(fund.getGroupVersionedFunding());

                        ampFunding.setCapitalSpendingPercentage(fund.getCapitalSpendingPercentage());

						if ( fund.getActStartDate() != null && fund.getActStartDate().length() > 0 )
							ampFunding.setActualStartDate( FormatHelper.parseDate(fund.getActStartDate()).getTime() );
						if ( fund.getActCloseDate() != null && fund.getActCloseDate().length() > 0 )
							ampFunding.setActualCompletionDate( FormatHelper.parseDate(fund.getActCloseDate()).getTime() );
						
						ampFunding.setAmpActivityId(activity);

						// add funding details for each funding
						Set fundDeatils = new HashSet();
						if (fund.getFundingDetails() != null) {
							Iterator itr3 = fund.getFundingDetails().iterator();
							while (itr3.hasNext()) {
								FundingDetail fundDet = (FundingDetail) itr3.next();
								AmpFundingDetail ampFundDet = new AmpFundingDetail();
								ampFundDet.setTransactionType(new Integer(fundDet.getTransactionType()));
								// ampFundDet.setPerspectiveId(DbUtil.getPerspective(Constants.MOFED));
								ampFundDet.setAdjustmentType(fundDet.getAdjustmentTypeName());
								ampFundDet.setReportingDate(fundDet.getReportingDate());
								ampFundDet.setTransactionDate(DateConversion.getDate(fundDet.getTransactionDate()));
								boolean useFixedRate = false;
								if (fundDet.getTransactionType() == Constants.COMMITMENT && fundDet.getFixedExchangeRate()!=null) {
									double fixedExchangeRate		=  FormatHelper.parseDouble( fundDet.getFixedExchangeRate() );
									if (fundDet.isUseFixedRate()
											&& fixedExchangeRate > 0
											&& fixedExchangeRate  != 1) {
										useFixedRate = true;
									}
								}

								if (!useFixedRate) {
									Double transAmt = new Double(
											FormatHelper.parseDouble(fundDet.getTransactionAmount()));
									ampFundDet.setTransactionAmount(transAmt);
									ampFundDet.setAmpCurrencyId(CurrencyUtil.getCurrencyByCode(fundDet.getCurrencyCode()));
									ampFundDet.setFixedExchangeRate(null);
									ampFundDet.setFixedRateBaseCurrency(null);
								} else {
									// Use the fixed exchange rate
									double transAmt = FormatHelper.parseDouble(fundDet.getTransactionAmount());
									Date trDate = DateConversion.getDate(fundDet.getTransactionDate());
									// double frmExRt =
									// CurrencyUtil.getExchangeRate(fundDet.getCurrencyCode(),1,trDate);
									// double amt =
									// CurrencyWorker.convert1(transAmt,
									// frmExRt,1);
									// amt *=
									// fundDet.getFixedExchangeRate();
									String currCode		= FeaturesUtil.getGlobalSettingValue( GlobalSettingsConstants.BASE_CURRENCY ) ;
									if ( currCode == null ) {
										currCode = "USD";
									}
									ampFundDet.setTransactionAmount(new Double(transAmt));
									ampFundDet.setFixedExchangeRate( FormatHelper.parseDouble( fundDet.getFixedExchangeRate() ) );
									ampFundDet.setFixedRateBaseCurrency( CurrencyUtil.getCurrencyByCode(currCode) );
									ampFundDet.setAmpCurrencyId(CurrencyUtil.getCurrencyByCode(fundDet
															.getCurrencyCode()));
								}
								ampFundDet.setAmpFundingId(ampFunding);
								if (fundDet.getTransactionType() == Constants.EXPENDITURE) {
									ampFundDet.setExpCategory(fundDet.getClassification());
								}
								ampFundDet.setDisbOrderId(fundDet.getDisbOrderId());
								ampFundDet.setContract(fundDet.getContract());
								ampFundDet.setDisbursementOrderRejected(fundDet.getDisbursementOrderRejected());
								if (fundDet.getPledge()!=0) {
									FundingPledges selectedpledge = PledgesEntityHelper.getPledgesById(fundDet.getPledge());
									ampFundDet.setPledgeid(selectedpledge);
								}else{
									ampFundDet.setPledgeid(null);
								}
								fundDeatils.add(ampFundDet);
							}
						}
						ampFunding.setFundingDetails(fundDeatils);
						this.saveMTEFProjections(fund, ampFunding);
						fundings.add(ampFunding);
					}
				}
				//AMP-2947 Funding amount should not be mandatory when adding a Funding Agency
				else{
					Funding fund = new Funding();
					AmpFunding ampFunding = new AmpFunding();
					ampFunding.setActive(fOrg.getFundingActive());
					if("unchecked".equals(fOrg.getDelegatedCooperationString()) || fOrg.getDelegatedCooperation()==null)
						ampFunding.setDelegatedCooperation(false);
					else
						ampFunding.setDelegatedCooperation(true);
					if("unchecked".equals(fOrg.getDelegatedPartnerString()) || fOrg.getDelegatedPartner()==null)
						ampFunding.setDelegatedPartner(false);
					else
						ampFunding.setDelegatedPartner(true);

					ampFunding.setAmpDonorOrgId(DbUtil.getOrganisation(fOrg.getAmpOrgId()));
					ampFunding.setFinancingId(fund.getOrgFundingId());

					ampFunding.setFinancingInstrument(fund.getFinancingInstrument());
					if (fund.getConditions() != null
							&& fund.getConditions().trim().length() != 0) {
						ampFunding.setConditions(fund.getConditions());
					} else {
						ampFunding.setConditions(new String(" "));
					}
					ampFunding.setComments(new String(" "));
					ampFunding.setTypeOfAssistance( fund.getTypeOfAssistance() );
					if(fund.getActStartDate()!=null)
						ampFunding.setActualStartDate( FormatHelper.parseDate(fund.getActStartDate()).getTime() );
					if(fund.getActCloseDate()!=null)
						ampFunding.setActualCompletionDate( FormatHelper.parseDate(fund.getActCloseDate()).getTime() );
					ampFunding.setAmpActivityId(activity);
					this.saveMTEFProjections(fund, ampFunding);
					fundings.add(ampFunding);
				}
			}
		}
		activity.setFunding(fundings);

		if(eaForm.getFunding().getProProjCost()==null){
            activity.setFunAmount(null);
            activity.setFunDate(null);
            activity.setCurrencyCode(null);
        }else{
            activity.setFunAmount(eaForm.getFunding().getProProjCost().getFunAmountAsDouble());
            //check null for bolivia
            if (eaForm.getFunding().getProProjCost().getFunDate()!=null){
                activity.setFunDate(FormatHelper.parseDate(eaForm.getFunding().getProProjCost().getFunDate()).getTime());
            }
            activity.setCurrencyCode(eaForm.getFunding().getProProjCost().getCurrencyCode());
        }
		
		
		
		
	}
	
	
	private void processStep4(boolean check, EditActivityForm eaForm, AmpActivityVersion activity, ActionMessages errors, HttpServletRequest request) throws Exception, AMPException{
		AMPException err;
		err = new AMPException(Constants.AMP_ERROR_LEVEL_WARNING, false);
		
		if (check){
			//Do the checks here
//			Date activityActualStartDate= DateConversion.getDate(eaForm.getPlanning().getRevisedStartDate());
//			if(activityActualStartDate!=null){
//				Collection<RegionalFunding> regFundDets = eaForm.getFunding().getRegionalFundings();
//				if(regFundDets!=null && regFundDets.size()>0){
//					for (RegionalFunding fundingDetail : regFundDets) {
//						Date transDate = null;
//						Collection<FundingDetail> commitments = fundingDetail.getCommitments();
//						Collection<FundingDetail> disbursements = fundingDetail.getDisbursements();
//						Collection<FundingDetail> expenditures = fundingDetail.getExpenditures();
//						Collection<FundingDetail> allFundDets= new ArrayList<FundingDetail>();
//						if(commitments!=null && commitments.size()>0){
//							allFundDets.addAll(commitments);
//						}
//						if(disbursements!=null && disbursements.size() >0){
//							allFundDets.addAll(disbursements);
//						}
//						if(expenditures!=null && expenditures.size() >0){
//							allFundDets.addAll(expenditures);
//						}
//						
//						if(allFundDets!=null && allFundDets.size() > 0){
//							for (FundingDetail commitmentsFundDet : allFundDets) {
//								transDate = DateConversion.getDate(commitmentsFundDet.getTransactionDate());
//								if(transDate.before(activityActualStartDate)){
//									errors.add("",new ActionError("error.aim.invalidRegionalFundingDate", TranslatorWorker.translateText("Regional Funding contains transaction, which precedes activity actual start date. Please change transaction date", request)));
//									break;
//								}
//							}
//						}
//					}
//				}
//			}
//			Date activityActualStartDate= DateConversion.getDate(eaForm.getPlanning().getRevisedStartDate());
//			if(activityActualStartDate!=null){
//				Collection<RegionalFunding> regFundDets = eaForm.getFunding().getRegionalFundings();
//				if(regFundDets!=null && regFundDets.size()>0){
//					for (RegionalFunding fundingDetail : regFundDets) {
//						Date transDate = null;
//						Collection<FundingDetail> commitments = fundingDetail.getCommitments();
//						Collection<FundingDetail> disbursements = fundingDetail.getDisbursements();
//						Collection<FundingDetail> expenditures = fundingDetail.getExpenditures();
//						Collection<FundingDetail> allFundDets= new ArrayList<FundingDetail>();
//						if(commitments!=null && commitments.size()>0){
//							allFundDets.addAll(commitments);
//						}
//						if(disbursements!=null && disbursements.size() >0){
//							allFundDets.addAll(disbursements);
//						}
//						if(expenditures!=null && expenditures.size() >0){
//							allFundDets.addAll(expenditures);
//						}
//						
//						if(allFundDets!=null && allFundDets.size() > 0){
//							for (FundingDetail commitmentsFundDet : allFundDets) {
//								transDate = DateConversion.getDate(commitmentsFundDet.getTransactionDate());
//								if(transDate.before(activityActualStartDate)){
//									errors.add("",new ActionError("error.aim.invalidRegionalFundingDate", TranslatorWorker.translateText("Regional Funding contains transaction, which precedes activity actual start date. Please change transaction date", request)));
//									break;
//								}
//							}
//						}
//					}
//				}
//			}
			
			end:
			if (errors.size() > 0){
				//we have all the errors for this step saved and we must throw the amp error
				saveErrors(request, errors);
				throw err;
			}
		}
		
		//Do the initializations and all the information transfer between beans here
		// set Regional fundings
		Set regFundings = new HashSet();
		if (eaForm.getFunding().getRegionalFundings() != null
				&& eaForm.getFunding().getRegionalFundings().size() > 0) {
			Iterator itr1 = eaForm.getFunding().getRegionalFundings().iterator();
			while (itr1.hasNext()) {
				RegionalFunding regFund = (RegionalFunding) itr1.next();
				if (regFund.getCommitments() != null
						&& regFund.getCommitments().size() > 0) {
					Iterator itr2 = regFund.getCommitments().iterator();
					while (itr2.hasNext()) {
						AmpRegionalFunding ampRegFund = new AmpRegionalFunding();
						ampRegFund.setActivity(activity);
						ampRegFund.setTransactionType(new Integer(
								Constants.COMMITMENT));
						FundingDetail fd = (FundingDetail) itr2.next();
						Iterator tmpItr=null;
						if(eaForm.getCurrencies()!=null){
							tmpItr = eaForm.getCurrencies()
							.iterator();
							while (tmpItr.hasNext()) {
								AmpCurrency curr = (AmpCurrency) tmpItr
								.next();
								if (curr.getCurrencyCode().equals(
										fd.getCurrencyCode())) {
									ampRegFund.setCurrency(curr);
									break;
								}
							}
						}

						boolean regionFlag=false;

						if(eaForm.getFunding().getFundingRegions()!=null && eaForm.getFunding().getFundingRegions().size()>0){
							Iterator<AmpCategoryValueLocations> regItr = eaForm.getFunding().getFundingRegions().iterator();
							while (regItr.hasNext()) {
								AmpCategoryValueLocations reg = regItr.next();
								if (reg.getId().equals(
										regFund.getRegionId())) {
									ampRegFund.setRegionLocation(reg);
									regionFlag=true;
									break;
								}
							}
						}
						ampRegFund.setTransactionAmount(new Double(
								FormatHelper.parseDouble(fd
										.getTransactionAmount())));
						ampRegFund.setTransactionDate(DateConversion
								.getDate(fd.getTransactionDate()));
						ampRegFund.setAdjustmentType(fd.getAdjustmentTypeName());
						if(regionFlag){
							regFundings.add(ampRegFund);
						}
					}
				}

				if (regFund.getDisbursements() != null
						&& regFund.getDisbursements().size() > 0) {
					Iterator itr2 = regFund.getDisbursements()
					.iterator();
					while (itr2.hasNext()) {
						AmpRegionalFunding ampRegFund = new AmpRegionalFunding();
						ampRegFund.setActivity(activity);
						ampRegFund.setTransactionType(new Integer(
								Constants.DISBURSEMENT));
						FundingDetail fd = (FundingDetail) itr2.next();
						Iterator tmpItr=null;
						if(eaForm.getCurrencies()!=null){
							tmpItr = eaForm.getCurrencies()
							.iterator();
							while (tmpItr.hasNext()) {
								AmpCurrency curr = (AmpCurrency) tmpItr
								.next();
								if (curr.getCurrencyCode().equals(
										fd.getCurrencyCode())) {
									ampRegFund.setCurrency(curr);
									break;
								}
							}
						}
						boolean regionFlag=false;
						if(eaForm.getFunding().getFundingRegions()!=null && eaForm.getFunding().getFundingRegions().size()>0){
							Iterator<AmpCategoryValueLocations> regItr = eaForm.getFunding().getFundingRegions().iterator();
							while (regItr.hasNext()) {
								AmpCategoryValueLocations reg = regItr.next();
								if (reg.getId().equals(
										regFund.getRegionId())) {
									ampRegFund.setRegionLocation(reg);
									regionFlag=true;
									break;
								}
							}
						}

						ampRegFund.setTransactionAmount(new Double(
								FormatHelper.parseDouble(fd
										.getTransactionAmount())));
						ampRegFund.setTransactionDate(DateConversion
								.getDate(fd.getTransactionDate()));
						ampRegFund.setAdjustmentType(fd.getAdjustmentTypeName());
						if(regionFlag){
							regFundings.add(ampRegFund);
						}
					}
				}

				if (regFund.getExpenditures() != null
						&& regFund.getExpenditures().size() > 0) {

					Iterator itr2 = regFund.getExpenditures()
					.iterator();
					while (itr2.hasNext()) {
						AmpRegionalFunding ampRegFund = new AmpRegionalFunding();
						ampRegFund.setActivity(activity);
						ampRegFund.setTransactionType(new Integer(
								Constants.EXPENDITURE));
						FundingDetail fd = (FundingDetail) itr2.next();
						Iterator tmpItr=null;
						if(eaForm.getCurrencies()!=null){
							tmpItr = eaForm.getCurrencies()
							.iterator();
							while (tmpItr.hasNext()) {
								AmpCurrency curr = (AmpCurrency) tmpItr
								.next();
								if (curr.getCurrencyCode().equals(
										fd.getCurrencyCode())) {
									ampRegFund.setCurrency(curr);
									break;
								}
							}
						}
						boolean regionFlag=false;
						if( eaForm.getFunding().getFundingRegions()!=null && eaForm.getFunding().getFundingRegions().size()>0){
							Iterator<AmpCategoryValueLocations> regItr = eaForm.getFunding().getFundingRegions().iterator();
							while (regItr.hasNext()) {
								AmpCategoryValueLocations reg = regItr.next();
								if (reg.getId().equals(
										regFund.getRegionId())) {
									ampRegFund.setRegionLocation(reg);
									regionFlag=true;
									break;
								}
							}
						}

						ampRegFund.setTransactionAmount(new Double(
								FormatHelper.parseDouble(fd
										.getTransactionAmount())));
						ampRegFund.setTransactionDate(DateConversion
								.getDate(fd.getTransactionDate()));
						ampRegFund.setAdjustmentType(fd.getAdjustmentTypeName());
						if(regionFlag){
							regFundings.add(ampRegFund);
						}
					}

				}
			}
		}
		activity.setRegionalFundings(regFundings);


	
	}
	

	private void processStep5(boolean check, EditActivityForm eaForm, AmpActivityVersion activity, ActionMessages errors, HttpServletRequest request) throws Exception, AMPException{
		AMPException err;
		err = new AMPException(Constants.AMP_ERROR_LEVEL_WARNING, false);
		
		if (check){
			//Do the checks here
			
			end:
			if (errors.size() > 0){
				//we have all the errors for this step saved and we must throw the amp error
				saveErrors(request, errors);
				throw err;
			}
		}
		
		//Do the initializations and all the information transfer between beans here

		//set components
		proccessComponents(eaForm, activity);

		if (eaForm.getIssues().getIssues() != null && eaForm.getIssues().getIssues().size() > 0) {
			Set issueSet = new HashSet();
			for (int i = 0; i < eaForm.getIssues().getIssues().size(); i++) {
				Issues issue = (Issues) eaForm.getIssues().getIssues().get(i);
				AmpIssues ampIssue = new AmpIssues();
				ampIssue.setActivity(activity);
				ampIssue.setName(issue.getName());
				if (issue.getIssueDate()!=null && issue.getIssueDate().trim().length()>0){
					ampIssue.setIssueDate(FormatHelper.parseDate(issue.getIssueDate()).getTime());
				}
				Set measureSet = new HashSet();
				if (issue.getMeasures() != null
						&& issue.getMeasures().size() > 0) {
					for (int j = 0; j < issue.getMeasures().size(); j++) {
						Measures measure = (Measures) issue
								.getMeasures().get(j);
						AmpMeasure ampMeasure = new AmpMeasure();
						ampMeasure.setIssue(ampIssue);
						ampMeasure.setName(measure.getName());
						Set actorSet = new HashSet();
						if (measure.getActors() != null
								&& measure.getActors().size() > 0) {
							for (int k = 0; k < measure.getActors()
									.size(); k++) {
								AmpActor actor = (AmpActor) measure
										.getActors().get(k);
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

    

	private void processStep6(boolean check, EditActivityForm eaForm, AmpActivityVersion activity, ActionMessages errors, HttpServletRequest request, Collection relatedLinks) throws Exception, AMPException{
		AMPException err;
		err = new AMPException(Constants.AMP_ERROR_LEVEL_WARNING, false);
		
		if (check){
			//Do the checks here
			
			end:
			if (errors.size() > 0){
				//we have all the errors for this step saved and we must throw the amp error
				saveErrors(request, errors);
				throw err;
			}
		}
		
		//Do the initializations and all the information transfer between beans here
		if(eaForm.getDocuments().getDocumentSpace() == null
	            || eaForm.getDocuments().getDocumentSpace().trim().length() == 0) {
			activity.setDocumentSpace(new String(" "));
		} else {
			activity.setDocumentSpace(eaForm.getDocuments().getDocumentSpace());
		}
		
		//relatedLinks = new ArrayList();
		relatedLinks.clear();
		if (eaForm.getDocuments().getDocumentList() != null && eaForm.getDocuments().getDocumentList() .size()>0) {
			Iterator itr = eaForm.getDocuments().getDocumentList().iterator();
			while (itr.hasNext()) {
				RelatedLinks rl = (RelatedLinks) itr.next();
				relatedLinks.add(rl);
			}
		}
		if (eaForm.getDocuments().getLinksList() != null && eaForm.getDocuments().getLinksList().size()>0) {
			Iterator itr = eaForm.getDocuments().getLinksList().iterator();
			while (itr.hasNext()) {
				RelatedLinks rl = (RelatedLinks) itr.next();
				relatedLinks.add(rl);
			}
		}
		
		/* Saving related documents into AmpActivityVersion */
        HashSet<String>UUIDs				= new HashSet<String>();
        Collection<DocumentData> tempDocs	= TemporaryDocumentData.retrieveTemporaryDocDataList(request);
        Iterator<DocumentData> docIter			= tempDocs.iterator();
        while ( docIter.hasNext() ) {
        	TemporaryDocumentData tempDoc	= (TemporaryDocumentData) docIter.next();
        	NodeWrapper nodeWrapper			= tempDoc.saveToRepository(request, errors);
        	if ( nodeWrapper != null )
        			UUIDs.add( nodeWrapper.getUuid() );
        }
        if(SelectDocumentDM.getSelectedDocsSet(request, ActivityDocumentsConstants.RELATED_DOCUMENTS, false)!=null) {
        	UUIDs.addAll(SelectDocumentDM.getSelectedDocsSet(request, ActivityDocumentsConstants.RELATED_DOCUMENTS, false));
        }
		if (UUIDs != null && UUIDs.size() >0 ) {
			if ( activity.getActivityDocuments() == null )
					activity.setActivityDocuments(new HashSet<AmpActivityDocument>() );
			else
					activity.getActivityDocuments().clear();
			Iterator<String> iter	= UUIDs.iterator();

			while ( iter.hasNext() ) {
				String uuid					= iter.next();
				AmpActivityDocument doc		= new AmpActivityDocument();
				doc.setUuid(uuid);
				doc.setDocumentType( ActivityDocumentsConstants.RELATED_DOCUMENTS );
				activity.getActivityDocuments().add(doc);
			}
		}
		SelectDocumentDM.clearContentRepositoryHashMap(request);
		/* END -Saving related documents into AmpActivityVersion */


	}

    
	private void processStep7(boolean check, EditActivityForm eaForm, AmpActivityVersion activity, ActionMessages errors, HttpServletRequest request) throws Exception, AMPException{
		AMPException err;
		err = new AMPException(Constants.AMP_ERROR_LEVEL_WARNING, false);
		
		if (check){
			if(eaForm.getIdentification().getDraft()==null || !eaForm.getIdentification().getDraft().booleanValue()){
			//Do the checks here
				if(FeaturesUtil.isVisibleField("Beneficiary Agency Percentage", ampContext)){
					if (eaForm.getAgencies().getBenOrgPercentage()!= null && eaForm.getAgencies().getBenOrgPercentage().size() > 0) {
						Iterator<String> ppIt = eaForm.getAgencies().getBenOrgPercentage().keySet().iterator();
						Double totalPercentage = 0d;
						while (ppIt.hasNext()) {
                            String benAgencyIdStr = ppIt.next();
							String percent = eaForm.getAgencies().getBenOrgPercentage().get(benAgencyIdStr);
							if (null == percent|| "".equals(percent)) {
								errors.add("BenOrgPercentageEmpty", new ActionMessage("error.aim.addActivity.BenOrgPercentagePercentageEmpty", TranslatorWorker.translateText("Please enter beneficiary agency  percentage")));
								break;
							}

                            for (AmpOrganisation benAgecny : eaForm.getAgencies().getBenAgencies()) {
                                if (benAgencyIdStr.equals(String.valueOf(benAgecny.getAmpOrgId()))) {
                                    Double percentage = new Double(percent);
                                    totalPercentage += percentage;
                                    break;
                                }
                            }


						}
						if (totalPercentage != 100)
							errors.add("BenOrgPercentageSumWrong",
									new ActionMessage("error.aim.addActivity.benOrgsPercentageSumWrong", TranslatorWorker.translateText("Sum of all Beneficiary Agency percentages must be 100")));
			}
		}
		
				if(FeaturesUtil.isVisibleField("Implementing Agency Percentage", ampContext)){
					if (eaForm.getAgencies().getImpOrgPercentage()!= null && eaForm.getAgencies().getImpOrgPercentage().size() > 0) {
						Iterator<String> ppIt = eaForm.getAgencies().getImpOrgPercentage().values().iterator();
						Double totalPercentage = 0d;
						while (ppIt.hasNext()) {
							String percent = ppIt.next() ;
							if (null == percent|| "".equals(percent)) {
								errors.add("impOrgPercentageEmpty", new ActionMessage("error.aim.addActivity.impOrgPercentagePercentageEmpty", TranslatorWorker.translateText("Please enter Implementing Agency percentage")));
								break;
							}
		
							Double percentage = new Double(percent);
							totalPercentage += percentage;
						}
						if (totalPercentage != 100)
							errors.add("implOrgPercentageSumWrong",
									new ActionMessage("error.aim.addActivity.implOrgsPercentageSumWrong", TranslatorWorker.translateText("Sum of all Implementing Agency percentages must be 100")));
					}
				}
				
				if(FeaturesUtil.isVisibleField("Contracting Agency Percentage", ampContext)){
					if (eaForm.getAgencies().getConOrgPercentage()!= null && eaForm.getAgencies().getConOrgPercentage().size() > 0) {
						Iterator<String> ppIt = eaForm.getAgencies().getConOrgPercentage().values().iterator();
						Double totalPercentage = 0d;
						while (ppIt.hasNext()) {
							String percent = ppIt.next() ;
							if (null == percent|| "".equals(percent)) {
								errors.add("contOrgPercentageEmpty", new ActionMessage("error.aim.addActivity.contOrgPercentagePercentageEmpty", TranslatorWorker.translateText("Please enter Contracting Agency percentage")));
								break;
							}
							
							Double percentage = new Double(percent);
							totalPercentage += percentage;
						}
						if (totalPercentage != 100)
							errors.add("contOrgPercentageSumWrong",
									new ActionMessage("error.aim.addActivity.contOrgsPercentageSumWrong", TranslatorWorker.translateText("Sum of all Contracting Agency percentages must be 100")));
					}
				}
				
				if(FeaturesUtil.isVisibleField("Executing Agency Percentage", ampContext)){
					if (eaForm.getAgencies().getExecutingOrgPercentage()!= null && eaForm.getAgencies().getExecutingOrgPercentage().size() > 0) {
						Iterator<String> ppIt = eaForm.getAgencies().getExecutingOrgPercentage().values().iterator();
						Double totalPercentage = 0d;
						while (ppIt.hasNext()) {
							String percent = ppIt.next() ;
							if (null == percent|| "".equals(percent)) {
								errors.add("execOrgPercentageEmpty", new ActionMessage("error.aim.addActivity.execOrgPercentagePercentageEmpty", TranslatorWorker.translateText("Please enter Executing Agency percentage")));
								break;
							}
							
							Double percentage = new Double(percent);
							totalPercentage += percentage;
						}
						if (totalPercentage != 100)
							errors.add("execOrgPercentageSumWrong",
									new ActionMessage("error.aim.addActivity.execOrgsPercentageSumWrong", TranslatorWorker.translateText("Sum of all Executing Agency percentages must be 100")));
					}
				}
				
				if(FeaturesUtil.isVisibleField("Regional Group Percentage", ampContext)){
					if (eaForm.getAgencies().getRegOrgPercentage()!= null && eaForm.getAgencies().getRegOrgPercentage().size() > 0) {
						Iterator<String> ppIt = eaForm.getAgencies().getRegOrgPercentage().values().iterator();
						Double totalPercentage = 0d;
						while (ppIt.hasNext()) {
							String percent = ppIt.next() ;
							if (null == percent|| "".equals(percent)) {
								errors.add("regOrgPercentageEmpty", new ActionMessage("error.aim.addActivity.regOrgPercentagePercentageEmpty", TranslatorWorker.translateText("Please enter Regional Group percentage")));
								break;
							}
							
							Double percentage = new Double(percent);
							totalPercentage += percentage;
						}
						if (totalPercentage != 100)
							errors.add("regOrgPercentageSumWrong",
									new ActionMessage("error.aim.addActivity.regOrgsPercentageSumWrong", TranslatorWorker.translateText("Sum of all Regional Group Percentages must be 100")));
					}
				}
				
				if(FeaturesUtil.isVisibleField("Responsible Organization Percentage", ampContext)){
					if (eaForm.getAgencies().getRespOrgPercentage()!= null && eaForm.getAgencies().getRespOrgPercentage().size() > 0) {
						Iterator<String> ppIt = eaForm.getAgencies().getRespOrgPercentage().values().iterator();
						Double totalPercentage = 0d;
						while (ppIt.hasNext()) {
							String percent = ppIt.next() ;
							if (null == percent|| "".equals(percent)) {
								errors.add("resOrgPercentageEmpty", new ActionMessage("error.aim.addActivity.resOrgPercentagePercentageEmpty", TranslatorWorker.translateText("Please enter Responsible Organization percentage")));
								break;
							}
							
							Double percentage = new Double(percent);
							totalPercentage += percentage;
						}
						if (totalPercentage != 100)
							errors.add("resOrgPercentageSumWrong",
									new ActionMessage("error.aim.addActivity.resOrgsPercentageSumWrong", TranslatorWorker.translateText("Sum of all Responsible Organization Percentages must be 100")));
					}
				}
				
				if(FeaturesUtil.isVisibleField("Sector Group Percentage", ampContext)){
					if (eaForm.getAgencies().getSectOrgPercentage()!= null && eaForm.getAgencies().getSectOrgPercentage().size() > 0) {
						Iterator<String> ppIt = eaForm.getAgencies().getSectOrgPercentage().values().iterator();
						Double totalPercentage = 0d;
						while (ppIt.hasNext()) {
							String percent = ppIt.next() ;
							if (null == percent|| "".equals(percent)) {
								errors.add("sectOrgPercentageEmpty", new ActionMessage("error.aim.addActivity.sectOrgPercentagePercentageEmpty", TranslatorWorker.translateText("Please enter Sector Group percentage")));
								break;
							}
							
							Double percentage = new Double(percent);
							totalPercentage += percentage;
						}
						if (totalPercentage != 100)
							errors.add("sectOrgPercentageSumWrong",
									new ActionMessage("error.aim.addActivity.sectOrgsPercentageSumWrong", TranslatorWorker.translateText("Sum of all Sector Group Percentages must be 100")));
					}
				}
			}
			
			end:
			if (errors.size() > 0){
				//we have all the errors for this step saved and we must throw the amp error
				saveErrors(request, errors);
				throw err;
			}
		}
		
		//Do the initializations and all the information transfer between beans here
//		if (eaForm.getContractors() == null
//				|| eaForm.getContractors().trim().length() == 0) {
//			activity.setContractors(" ");
//		} else {
//			activity.setContractors(eaForm.getContractors());
//		}
		Set orgRole = new HashSet();
		
		if (eaForm.getAgencies().getExecutingAgencies() != null && eaForm.getAgencies().getExecutingAgencies().size()>0) { // executing
			// agencies
			AmpRole role = DbUtil.getAmpRole(Constants.EXECUTING_AGENCY);
			Iterator itr = eaForm.getAgencies().getExecutingAgencies().iterator();
			boolean assignWholePercentage = true;
			while (itr.hasNext()) {
				AmpOrganisation tmp= (AmpOrganisation) itr.next();
				AmpOrgRole ampOrgRole=new AmpOrgRole();
				ampOrgRole.setActivity(activity);
				ampOrgRole.setRole(role);
				ampOrgRole.setOrganisation(tmp);
				if (eaForm.getAgencies().getExecutingOrgToInfo()!=null){
				String additionalInfo			= eaForm.getAgencies().getExecutingOrgToInfo().get( tmp.getAmpOrgId().toString() );
				if ( additionalInfo != null && additionalInfo.length() > 0 ){
					ampOrgRole.setAdditionalInfo(additionalInfo);
					}
				}
				String percentage 		= eaForm.getAgencies().getExecutingOrgPercentage().get(tmp.getAmpOrgId().toString()); //keySet()get(tmp.getAmpOrgId());
				if(percentage != null && percentage.length() > 0){
					ampOrgRole.setPercentage(new Float(percentage));
				}else{
					if(assignWholePercentage){
						assignWholePercentage=false;
						ampOrgRole.setPercentage(100f);
					}else{
						ampOrgRole.setPercentage(0f);
					}
					
				}
				orgRole.add(ampOrgRole);
			}
		}
		if (eaForm.getAgencies().getImpAgencies() != null && eaForm.getAgencies().getImpAgencies().size()>0) { // implementing agencies
			AmpRole role = DbUtil.getAmpRole(Constants.IMPLEMENTING_AGENCY);
			Iterator itr = eaForm.getAgencies().getImpAgencies().iterator();
			boolean assignWholePercentage = true;
			while (itr.hasNext()) {
				AmpOrganisation org = (AmpOrganisation) itr.next();
				AmpOrgRole ampOrgRole = new AmpOrgRole();
				ampOrgRole.setActivity(activity);
				ampOrgRole.setRole(role);
				ampOrgRole.setOrganisation(org);
				if (eaForm.getAgencies().getImpOrgToInfo()!=null){
				String additionalInfo			= eaForm.getAgencies().getImpOrgToInfo().get( org.getAmpOrgId().toString() );
					if ( additionalInfo != null && additionalInfo.length() > 0 ){
						ampOrgRole.setAdditionalInfo(additionalInfo);
					}
				}
				String percentage 		= eaForm.getAgencies().getImpOrgPercentage().get(org.getAmpOrgId().toString());
				if(percentage != null && percentage.length() > 0){
					ampOrgRole.setPercentage(new Float(percentage));
				}else{
					if(assignWholePercentage){
						assignWholePercentage=false;
						ampOrgRole.setPercentage(100f);
					}else{
						ampOrgRole.setPercentage(0f);
					}
				}
				orgRole.add(ampOrgRole);
			}
		}
		if (eaForm.getAgencies().getBenAgencies() != null && eaForm.getAgencies().getBenAgencies().size()>0) { // beneficiary agencies
			AmpRole role = DbUtil.getAmpRole(Constants.BENEFICIARY_AGENCY);
			Iterator itr = eaForm.getAgencies().getBenAgencies().iterator();
			boolean assignWholePercentage = true;
			while (itr.hasNext()) {
				AmpOrganisation org = (AmpOrganisation) itr.next();
				AmpOrgRole ampOrgRole = new AmpOrgRole();
				ampOrgRole.setActivity(activity);
				ampOrgRole.setRole(role);
				ampOrgRole.setOrganisation(org);
				if (eaForm.getAgencies().getBenOrgToInfo()!=null){
				String additionalInfo			= eaForm.getAgencies().getBenOrgToInfo().get( org.getAmpOrgId().toString() );
				if ( additionalInfo != null && additionalInfo.length() > 0 ){
					ampOrgRole.setAdditionalInfo(additionalInfo);
					}
				}
				String percentage 		= eaForm.getAgencies().getBenOrgPercentage().get(org.getAmpOrgId().toString());
				if(percentage != null && percentage.length() > 0){
					ampOrgRole.setPercentage(new Float(percentage));
				}else{
					if(assignWholePercentage){
						assignWholePercentage=false;
						ampOrgRole.setPercentage(100f);
					}else{
						ampOrgRole.setPercentage(0f);
					}
				}
				orgRole.add(ampOrgRole);
			}
		}
		if (eaForm.getAgencies().getConAgencies() != null && eaForm.getAgencies().getConAgencies().size()>0) { // contracting agencies
			AmpRole role = DbUtil.getAmpRole(Constants.CONTRACTING_AGENCY);
			Iterator itr = eaForm.getAgencies().getConAgencies().iterator();
			boolean assignWholePercentage = true;
			while (itr.hasNext()) {
				AmpOrganisation org = (AmpOrganisation) itr.next();
				AmpOrgRole ampOrgRole = new AmpOrgRole();
				ampOrgRole.setActivity(activity);
				ampOrgRole.setRole(role);
				ampOrgRole.setOrganisation(org);
				if (eaForm.getAgencies().getConOrgToInfo()!=null){
				String additionalInfo			= eaForm.getAgencies().getConOrgToInfo().get( org.getAmpOrgId().toString() );
					if ( additionalInfo != null && additionalInfo.length() > 0 ){
					ampOrgRole.setAdditionalInfo(additionalInfo);
					}
				}
				String percentage 		= eaForm.getAgencies().getConOrgPercentage().get(org.getAmpOrgId().toString());
				if(percentage != null && percentage.length() > 0){
					ampOrgRole.setPercentage(new Float(percentage));
				}else{
					if(assignWholePercentage){
						assignWholePercentage=false;
						ampOrgRole.setPercentage(100f);
					}else{
						ampOrgRole.setPercentage(0f);
					}
				}
				orgRole.add(ampOrgRole);
			}
		}
		if (eaForm.getAgencies().getRegGroups() != null && eaForm.getAgencies().getRegGroups().size()>0) { // regional groups
			AmpRole role = DbUtil.getAmpRole(Constants.REGIONAL_GROUP);
			Iterator itr = eaForm.getAgencies().getRegGroups().iterator();
			boolean assignWholePercentage = true;
			while (itr.hasNext()) {
				AmpOrganisation org = (AmpOrganisation) itr.next();
				AmpOrgRole ampOrgRole = new AmpOrgRole();
				ampOrgRole.setActivity(activity);
				ampOrgRole.setRole(role);
				ampOrgRole.setOrganisation(org);
				if (eaForm.getAgencies().getRegOrgToInfo()!=null){
				String additionalInfo			= eaForm.getAgencies().getRegOrgToInfo().get( org.getAmpOrgId().toString() );
				if ( additionalInfo != null && additionalInfo.length() > 0 ){
					ampOrgRole.setAdditionalInfo(additionalInfo);
				}
				}
				String percentage 		= eaForm.getAgencies().getRegOrgPercentage().get(org.getAmpOrgId().toString());
				if(percentage != null && percentage.length() > 0){
					ampOrgRole.setPercentage(new Float(percentage));
				}else{
					if(assignWholePercentage){
						assignWholePercentage=false;
						ampOrgRole.setPercentage(100f);
					}else{
						ampOrgRole.setPercentage(0f);
					}
				}
				orgRole.add(ampOrgRole);
			}
		}
		if (eaForm.getAgencies().getSectGroups() != null && eaForm.getAgencies().getSectGroups().size()>0) { // sector groups
			AmpRole role = DbUtil.getAmpRole(Constants.SECTOR_GROUP);
			Iterator itr = eaForm.getAgencies().getSectGroups().iterator();
			boolean assignWholePercentage = true;
			while (itr.hasNext()) {
				AmpOrganisation org = (AmpOrganisation) itr.next();
				AmpOrgRole ampOrgRole = new AmpOrgRole();
				ampOrgRole.setActivity(activity);
				ampOrgRole.setRole(role);
				ampOrgRole.setOrganisation(org);
				if(eaForm.getAgencies().getSectOrgToInfo()!=null){
				String additionalInfo			= eaForm.getAgencies().getSectOrgToInfo().get( org.getAmpOrgId().toString() );
				if ( additionalInfo != null && additionalInfo.length() > 0 ){
					ampOrgRole.setAdditionalInfo(additionalInfo);
				}
				}
				String percentage 		= eaForm.getAgencies().getSectOrgPercentage().get(org.getAmpOrgId().toString());
				if(percentage != null && percentage.length() > 0){
					ampOrgRole.setPercentage(new Float(percentage));
				}else{
					if(assignWholePercentage){
						assignWholePercentage=false;
						ampOrgRole.setPercentage(100f);
					}else{
						ampOrgRole.setPercentage(0f);
					}
				}
				orgRole.add(ampOrgRole);
			}
		}
		if (eaForm.getAgencies().getReportingOrgs() != null && eaForm.getAgencies().getReportingOrgs().size()>0) { // Reporting
			// Organization
			AmpRole role = DbUtil.getAmpRole(Constants.REPORTING_AGENCY);
			Iterator itr = eaForm.getAgencies().getReportingOrgs().iterator();
			boolean assignWholePercentage = true;
			while (itr.hasNext()) {
				AmpOrganisation org = (AmpOrganisation) itr.next();
				AmpOrgRole ampOrgRole = new AmpOrgRole();
				ampOrgRole.setActivity(activity);
				ampOrgRole.setRole(role);
				ampOrgRole.setOrganisation(org);
				if (eaForm.getAgencies().getRepOrgToInfo()!=null){
				String additionalInfo			= eaForm.getAgencies().getRepOrgToInfo().get( org.getAmpOrgId().toString() );
					if ( additionalInfo != null && additionalInfo.length() > 0 ){
						ampOrgRole.setAdditionalInfo(additionalInfo);
					}
				}
				String percentage 		= eaForm.getAgencies().getRepOrgPercentage().get(org.getAmpOrgId().toString());
				if(percentage != null && percentage.length() > 0){
					ampOrgRole.setPercentage(new Float(percentage));
				}else{
					if(assignWholePercentage){
						assignWholePercentage=false;
						ampOrgRole.setPercentage(100f);
					}else{
						ampOrgRole.setPercentage(0f);
					}
				}
				orgRole.add(ampOrgRole);
			}
		}
		if (eaForm.getAgencies().getRespOrganisations() != null && eaForm.getAgencies().getRespOrganisations().size()>0) { // Responsible Organisation
			AmpRole role = DbUtil.getAmpRole(Constants.RESPONSIBLE_ORGANISATION);
			Iterator itr = eaForm.getAgencies().getRespOrganisations().iterator();
			boolean assignWholePercentage = true;
			while (itr.hasNext()) {
				AmpOrganisation org = (AmpOrganisation) itr.next();
				AmpOrgRole ampOrgRole = new AmpOrgRole();
				ampOrgRole.setActivity(activity);
				ampOrgRole.setRole(role);
				ampOrgRole.setOrganisation(org);
				if(eaForm.getAgencies().getRespOrgToInfo()!=null){
				String additionalInfo			= eaForm.getAgencies().getRespOrgToInfo().get( org.getAmpOrgId().toString() );
					if ( additionalInfo != null && additionalInfo.length() > 0 ){
						ampOrgRole.setAdditionalInfo(additionalInfo);
					}
				}
				String percentage 		= eaForm.getAgencies().getRespOrgPercentage().get(org.getAmpOrgId().toString());
				if(percentage != null && percentage.length() > 0){
					ampOrgRole.setPercentage(new Float(percentage));
				}else{
					if(assignWholePercentage){
						assignWholePercentage=false;
						ampOrgRole.setPercentage(100f);
					}else{
						ampOrgRole.setPercentage(0f);
					}
				}
				orgRole.add(ampOrgRole);
			}
		}
		//a previous step is adding another role so we have to check it
		if (activity.getOrgrole()==null){
			activity.setOrgrole(orgRole);
		}else{
			activity.getOrgrole().addAll(orgRole);
		}
		
		
	}

	private void processStep8(boolean check, EditActivityForm eaForm, AmpActivityVersion activity, ActionMessages errors, HttpServletRequest request) throws Exception, AMPException{
		AMPException err;
		err = new AMPException(Constants.AMP_ERROR_LEVEL_WARNING, false);
		ActivityContactInfo contactInfo=eaForm.getContactInformation();
		
		if(contactInfo.getResetDonorIds()!=null && contactInfo.getResetDonorIds()){
			contactInfo.setPrimaryDonorContIds(null);
		}
		if(contactInfo.getResetMofedIds()!=null && contactInfo.getResetMofedIds()){
			contactInfo.setPrimaryMofedContIds(null);
		}
		if(contactInfo.getResetProjCoordIds()!=null && contactInfo.getResetProjCoordIds()){
			contactInfo.setPrimaryProjCoordContIds(null);
		}
		if(contactInfo.getResetSecMinIds()!=null && contactInfo.getResetSecMinIds()){
			contactInfo.setPrimarySecMinContIds(null);
		}
		if(contactInfo.getResetImplExecutingIds()!=null && contactInfo.getResetImplExecutingIds()){
			contactInfo.setPrimaryImplExecutingContIds(null);
		}
		/*
        /*
		String[] donorContsIds=null;
		String[] mofedContsIds=null;
		String[] projCoordContsIds=null;
		String[] sectorMinContsIds=null;
		String[] implExecutingContsIds=null;
		
			donorContsIds=contactInfo.getPrimaryDonorContIds();
			mofedContsIds=contactInfo.getPrimaryMofedContIds();
			projCoordContsIds=contactInfo.getPrimaryProjCoordContIds();
			sectorMinContsIds=contactInfo.getPrimarySecMinContIds();
			implExecutingContsIds=contactInfo.getPrimaryImplExecutingContIds();
			
		if (check){
			// If the activity has at least one contact (no matter the type)
			// then one primary contact has to be selected.			
			if ((contactInfo.getDonorContacts() != null && contactInfo.getDonorContacts().size() != 0)
					|| (contactInfo.getMofedContacts() != null && contactInfo.getMofedContacts().size() != 0)
					|| (contactInfo.getProjCoordinatorContacts() != null && contactInfo.getProjCoordinatorContacts()
							.size() != 0)
					|| (contactInfo.getSectorMinistryContacts() != null && contactInfo.getSectorMinistryContacts()
							.size() != 0)
					|| (contactInfo.getImplExecutingAgencyContacts() != null && contactInfo
							.getImplExecutingAgencyContacts().size() != 0)) {

				int contactsCount = 0;
				if (donorContsIds != null) {
					contactsCount += donorContsIds.length;
				}
				if (mofedContsIds != null) {
					contactsCount += mofedContsIds.length;
				}
				if (projCoordContsIds != null) {
					contactsCount += projCoordContsIds.length;
				}
				if (sectorMinContsIds != null) {
					contactsCount += sectorMinContsIds.length;
				}
				if (implExecutingContsIds != null) {
					contactsCount += implExecutingContsIds.length;
				}
				
				if(contactsCount != 1){
					errors.add("invalidDonorCont", new ActionMessage("error.aim.addActivity.contactInfo.invalidPrimaryCont",
							TranslatorWorker.translateText("Must be one Primary Contact", locale, siteId)));
				}
			}
						
			//end:
			if (errors.size() > 0){
				//we have all the errors for this step saved and we must throw the amp error
				saveErrors(request, errors);
				throw err;
			}
		}
		*/
		//Do the initializations and all the information transfer between beans here
		List<AmpActivityContact> allContacts=new ArrayList<AmpActivityContact>(); //eaForm.getContactInformation().getActivityContacts();
		if(contactInfo.getDonorContacts()!=null && contactInfo.getDonorContacts().size()>0){
			allContacts.addAll(contactInfo.getDonorContacts());
		}
		if(contactInfo.getMofedContacts()!=null && contactInfo.getMofedContacts().size()>0){
			allContacts.addAll(contactInfo.getMofedContacts());
		}
		if(contactInfo.getSectorMinistryContacts()!=null && contactInfo.getSectorMinistryContacts().size()>0){
			allContacts.addAll(contactInfo.getSectorMinistryContacts());
		}
		if(contactInfo.getProjCoordinatorContacts()!=null && contactInfo.getProjCoordinatorContacts().size()>0){
			allContacts.addAll(contactInfo.getProjCoordinatorContacts());
		}
		if(contactInfo.getImplExecutingAgencyContacts()!=null && contactInfo.getImplExecutingAgencyContacts().size()>0){
			allContacts.addAll(contactInfo.getImplExecutingAgencyContacts());
		}
		
		if(allContacts!=null && allContacts.size()>0){
			for (AmpActivityContact ampActContact : allContacts) {
				ampActContact.setActivity(activity);
				if(ampActContact.getContactType().equals(Constants.DONOR_CONTACT)){
					fillActivityContactPrimaryField(contactInfo.getPrimaryDonorContId(),ampActContact);
				}else if(ampActContact.getContactType().equals(Constants.MOFED_CONTACT)){
					fillActivityContactPrimaryField(contactInfo.getPrimaryMofedContId(), ampActContact);
				}else if(ampActContact.getContactType().equals(Constants.PROJECT_COORDINATOR_CONTACT)){
					fillActivityContactPrimaryField(contactInfo.getPrimaryProjCoordContId(),	ampActContact);
				}else if(ampActContact.getContactType().equals(Constants.SECTOR_MINISTRY_CONTACT)){
					fillActivityContactPrimaryField(contactInfo.getPrimarySecMinContId(),	ampActContact);
				}else if(ampActContact.getContactType().equals(Constants.IMPLEMENTING_EXECUTING_AGENCY_CONTACT)){
					fillActivityContactPrimaryField(contactInfo.getPrimaryImplExecutingContId(),	ampActContact);
				}
			}
		}
		
		//Set activity contact properties
		
		for (AmpActivityContact cont : allContacts) {
			for (AmpContactProperty prop : cont.getContact().getProperties()) {
				if (prop.getContact() == null) {
					prop.setContact(cont.getContact());
				}
			}
		}
		
		contactInfo.setActivityContacts(allContacts);
		activity.setActivityContacts(new HashSet(allContacts));
	}
	/*
    /*
	private void fillActivityContactPrimaryField(String[] actContactIds,AmpActivityContact ampActContact) {
		String actContId=ampActContact.getContact().getTemporaryId()==null ? ampActContact.getContact().getId().toString() :  ampActContact.getContact().getTemporaryId();
		if(actContactIds!=null && actContactIds.length>0){
			for (int i = 0; i < actContactIds.length; i++) {
				if(actContId.equals(actContactIds[i])){
					ampActContact.setPrimaryContact(true);
				}else{
					ampActContact.setPrimaryContact(false);
				}
			}
		}else{
			ampActContact.setPrimaryContact(false);
		}
	}
	*/

    private void fillActivityContactPrimaryField(String actContactId,AmpActivityContact ampActContact) {
		String actContId=ampActContact.getContact().getTemporaryId()==null ? ampActContact.getContact().getId().toString() :  ampActContact.getContact().getTemporaryId();
		if(actContactId!=null && actContactId.length() >0){
            if(actContId.equals(actContactId)){
                ampActContact.setPrimaryContact(true);
            }else{
                ampActContact.setPrimaryContact(false);
            }
		}else{
			ampActContact.setPrimaryContact(false);
		}
	}
	
	private void processStep9(boolean check, EditActivityForm eaForm, AmpActivityVersion activity, ActionMessages errors, HttpServletRequest request) throws Exception, AMPException{
		AMPException err;
		err = new AMPException(Constants.AMP_ERROR_LEVEL_WARNING, false);
		
		if (check){
			//Do the checks here
			
			end:
			if (errors.size() > 0){
				//we have all the errors for this step saved and we must throw the amp error
				saveErrors(request, errors);
				throw err;
			}
		}
		
		//Do the initializations and all the information transfer between beans here
		if(eaForm.isEditAct()){
			if (eaForm.getSurveys() != null) {
				Iterator<Survey> iterSurveys = eaForm.getSurveys().iterator();
				while (iterSurveys.hasNext()) {
					Survey auxSurvey = iterSurveys.next();
					if (auxSurvey.getAhsurvey() != null) {
						DbUtil.updateSurvey(auxSurvey.getAhsurvey(), activity);
					}
					if (auxSurvey.getAmpSurveyId() != null) {
			        	DbUtil.saveSurveyResponses(auxSurvey.getAmpSurveyId(), auxSurvey.getIndicators());
					}
				}
			}
		} else {
            // Process surveys from original version that have not been viewed.
			Session session = PersistenceManager.getRequestDBSession();
			Query qry = session.createQuery("select survey from " + AmpAhsurvey.class.getName()
					+ " survey where ampActivityId.ampActivityId = ?");
			qry.setParameter(0, eaForm.getActivityId());
			Collection savedAhSurveyList = qry.list();
			Iterator iterOriginalAhSurveys = savedAhSurveyList.iterator();
			while (iterOriginalAhSurveys.hasNext()) {
				AmpAhsurvey originalSurvey = (AmpAhsurvey) iterOriginalAhSurveys.next();
				boolean exists = false;
				if (eaForm.getSurveys() != null) {
					Iterator<Survey> iterSurveys = eaForm.getSurveys().iterator();
					while (iterSurveys.hasNext()) {
						Survey auxSurvey = iterSurveys.next();
						if (auxSurvey != null && auxSurvey.getAhsurvey() != null
								&& auxSurvey.getAhsurvey().getAmpDonorOrgId() != null
								&& auxSurvey.getAhsurvey().getAmpDonorOrgId().equals(originalSurvey.getAmpDonorOrgId())) {
							exists = true;
							// eaForm.getSurveys().remove(auxSurvey);
						}
					}
					if (!exists) {
						DbUtil.saveNewSurvey(originalSurvey, activity);
					}
				} else {
					DbUtil.saveNewSurvey(originalSurvey, activity);
				}
			}
            
			if (eaForm.getSurveys() != null) {
				Iterator<Survey> iterSurveys = eaForm.getSurveys().iterator();
				while (iterSurveys.hasNext()) {
					Survey auxSurvey = iterSurveys.next();
					DbUtil.saveNewSurvey(auxSurvey.getAhsurvey(), activity, auxSurvey.getIndicators());
				}
			}
		}
	}

	private void processStep11(boolean check, EditActivityForm eaForm, AmpActivityVersion activity, ActionMessages errors, HttpServletRequest request) throws Exception, AMPException{
		AMPException err;
		err = new AMPException(Constants.AMP_ERROR_LEVEL_WARNING, false);
		
		if (check){
			//Do the checks here
			
			end:
			if (errors.size() > 0){
				//we have all the errors for this step saved and we must throw the amp error
				saveErrors(request, errors);
				throw err;
			}
		}
		
		//Do the initializations and all the information transfer between beans here
		if(eaForm.getCosting().getCosts()!=null && eaForm.getCosting().getCosts().size()>0) {
			Set costs=new HashSet();
			Iterator i=eaForm.getCosting().getCosts().iterator();
			while (i.hasNext()) {
				EUActivity element = (EUActivity) i.next();
				element.setActivity(activity);
				element.setId(null);
				if(element.getContributions()!=null){
					Iterator ii=element.getContributions().iterator();
					while (ii.hasNext()) {
						EUActivityContribution element2 = (EUActivityContribution) ii.next();
						element2.setId(null);
					}
				}
				costs.add(element);
			}
			activity.setCosts(costs);
		}

	}
	
	private void processStep14(boolean check, EditActivityForm eaForm, AmpActivityVersion activity, ActionMessages errors,
			HttpServletRequest request) throws Exception, AMPException {
		AMPException err;
		err = new AMPException(Constants.AMP_ERROR_LEVEL_WARNING, false);

		if (check) {
			end: if (errors.size() > 0) {
				// we have all the errors for this step saved and we must throw
				// the amp error
				saveErrors(request, errors);
				throw err;
			}
		}

		// Do the initializations and all the information transfer between beans
		// here
		// proccessComponents(eaForm, activity);
		if (eaForm.getObservations() != null && eaForm.getObservations().getIssues() != null) {
			Set<AmpRegionalObservation> observationSet = new HashSet();
			Iterator<Issues> iIssues = eaForm.getObservations().getIssues().iterator();
			while (iIssues.hasNext()) {
				Issues auxIssue = iIssues.next();
				AmpRegionalObservation auxObservation = new AmpRegionalObservation();
				auxObservation.setActivity(activity);
				auxObservation.setName(auxIssue.getName());
				if (auxIssue.getId() < 100000) {
					auxObservation.setAmpRegionalObservationId(auxIssue.getId());
				}
				if (auxIssue.getIssueDate() != null && auxIssue.getIssueDate().trim().length() > 0) {
					auxObservation.setObservationDate(FormatHelper.parseDate(auxIssue.getIssueDate()).getTime());
				}
				Set<AmpRegionalObservationMeasure> measureSet = new HashSet();
				if (auxIssue.getMeasures() != null) {
					Iterator<Measures> iterMeasures = auxIssue.getMeasures().iterator();
					while (iterMeasures.hasNext()) {
						Measures auxHelperMeasure = iterMeasures.next();
						AmpRegionalObservationMeasure auxMeasure = new AmpRegionalObservationMeasure();
						auxMeasure.setRegionalObservation(auxObservation);
						auxMeasure.setName(auxHelperMeasure.getName());
						if (auxHelperMeasure.getId() < 100000) {
							auxMeasure.setAmpRegionalObservationMeasureId(auxHelperMeasure.getId());
						}
						Set<AmpRegionalObservationActor> actorSet = new HashSet();
						// TODO: If there is a helper called Measure then it
						// should exist a helper called Actor too.
						if (auxHelperMeasure.getActors() != null) {
							Iterator<AmpActor> iActors = auxHelperMeasure.getActors().iterator();
							while (iActors.hasNext()) {
								AmpActor auxAmpActor = iActors.next();
								AmpRegionalObservationActor auxRegionalObservationActor = new AmpRegionalObservationActor();
								auxRegionalObservationActor.setMeasure(auxMeasure);
								auxRegionalObservationActor.setName(auxAmpActor.getName());
								if (auxAmpActor.getAmpActorId() < 100000) {
									auxRegionalObservationActor.setAmpRegionalObservationActorId(auxAmpActor
										.getAmpActorId());
								}
								actorSet.add(auxRegionalObservationActor);
							}
						}
						auxMeasure.setActors(actorSet);
						measureSet.add(auxMeasure);
					}
				}
				auxObservation.setRegionalObservationMeasures(measureSet);
				observationSet.add(auxObservation);
			}
			activity.setRegionalObservations(observationSet);
		}
	}
	
	private void processPostStep(EditActivityForm eaForm, AmpActivityVersion activity, TeamMember tm){
		Long teamId=null;
		
		if (eaForm.isEditAct() || eaForm.getActivityId()!=null && eaForm.getActivityId()!=0) {
		
			//AmpActivityVersion act = ActivityUtil.getActivityByName(eaForm.getTitle());
			// Setting approval status of activity
			activity.setApprovalStatus(eaForm.getIdentification().getApprovalStatus());
			activity.setApprovalDate(eaForm.getIdentification().getApprovalDate());
			activity.setApprovedBy(eaForm.getIdentification().getApprovedBy());
			

			//AMP-3464
			//if an approved activity is edited and the appsettings is set to newOnly then the activity
			//doesn't need to be approved again!
			AmpActivityVersion aAct = null;
			try {
				aAct = ActivityUtil.loadActivity(eaForm.getActivityId());
			} catch (DgException e) {
				logger.error(e);
			}
			teamId=aAct.getTeam().getAmpTeamId();
			String validation = DbUtil.getValidationFromTeamAppSettings(teamId);
			eaForm.getIdentification().setPreviousApprovalStatus(aAct.getApprovalStatus());

			activity.setApprovalStatus(aAct.getApprovalStatus());
			if(tm.getTeamId().equals(aAct.getTeam().getAmpTeamId())){
				if(eaForm.getIdentification().getDraft()==true){
					if(tm.getTeamHead()||tm.isApprover()){
						activity.setApprovalStatus(Constants.APPROVED_STATUS);
					}else{
						activity.setApprovalStatus(aAct.getApprovalStatus());
					}
				}
				if("newOnly".equals(validation))
				{
					if(Constants.APPROVED_STATUS.equals(activity.getApprovalStatus()) || Constants.EDITED_STATUS.equals(activity.getApprovalStatus()) )
						activity.setApprovalStatus(Constants.APPROVED_STATUS);
				}

				if("allEdits".equals(validation)){
					if(!tm.getTeamHead()){
						if(Constants.APPROVED_STATUS.equals(aAct.getApprovalStatus()) || Constants.STARTED_APPROVED_STATUS.equals(aAct.getApprovalStatus())) activity.setApprovalStatus(Constants.EDITED_STATUS);
						else activity.setApprovalStatus(aAct.getApprovalStatus());
					}
				}
				
				if("validationOff".equals(validation))
				{
					activity.setApprovalStatus(Constants.APPROVED_STATUS);
				}
				
				if(tm.getTeamHead()||tm.isApprover()) activity.setApprovalStatus(Constants.APPROVED_STATUS);
			}
			else{
				if("newOnly".equals(validation))
				{
					if(Constants.APPROVED_STATUS.equals(activity.getApprovalStatus()) || Constants.EDITED_STATUS.equals(activity.getApprovalStatus()) )
						activity.setApprovalStatus(Constants.APPROVED_STATUS);
				}

				if("allEdits".equals(validation)){
					if(Constants.APPROVED_STATUS.equals(aAct.getApprovalStatus())) activity.setApprovalStatus(Constants.EDITED_STATUS);
					else activity.setApprovalStatus(aAct.getApprovalStatus());
				}
				
				if("validationOff".equals(validation))
				{
					activity.setApprovalStatus(Constants.APPROVED_STATUS);
			}
			}
			activity.setActivityCreator(eaForm.getIdentification().getCreatedBy());
		}
		else{
			AmpTeamMember teamMember = TeamMemberUtil.getAmpTeamMember(tm.getMemberId());
			teamId=tm.getTeamId();
			activity.setActivityCreator(teamMember);
			Calendar cal = Calendar.getInstance();
			activity.setCreatedDate(cal.getTime());
			// Setting approval status of activity
			Long ampTeamId = null;
			if(activity!=null && activity.getTeam()!=null && activity.getTeam().getAmpTeamId() !=null)
				ampTeamId = activity.getTeam().getAmpTeamId();
			else ampTeamId = teamMember.getAmpTeamMemId();
			String validation = DbUtil.getValidationFromTeamAppSettings(teamId);
			if (activity.getDraft() && tm.getTeamHead() || (validation!=null&&"validationOff".equals(validation))||tm.isApprover()){
				activity.setApprovalStatus(Constants.STARTED_APPROVED_STATUS);
			}else{
				activity.setApprovalStatus(eaForm.getIdentification().getApprovalStatus());
			}

		}
		if("allOff".equals(DbUtil.getValidationFromTeamAppSettings(tm.getTeamId()))){
				activity.setApprovalStatus(Constants.APPROVED_STATUS);
		}

	}
	
	
	
	/**
	 * This is the structure that a processStep should have .. it's only to be used as a starting point in treating a new step
	 * parameters:
	 *   - check - set to true when data validation should be made, if data is invalid user is redirected to that step to correct it
	 *
	 * 
	 * DO NOT MODIFY
	 * 
	 * @author Arty
	 */
	private void processStepBulk(boolean check, EditActivityForm eaForm, AmpActivityVersion activity, ActionMessages errors, HttpServletRequest request) throws Exception, AMPException{
		AMPException err;
		err = new AMPException(Constants.AMP_ERROR_LEVEL_WARNING, false);
		if (check){
			//Do the checks here			
			
			end:
			if (errors.size() > 0){
				//we have all the errors for this step saved and we must throw the amp error
				saveErrors(request, errors);
				throw err;
			}
		}
		
		//Do the initializations and all the information transfer between beans here
		
	}

	private void symErr() throws Exception, AMPException{
		AMPException err;
		err = new AMPException(Constants.AMP_ERROR_LEVEL_WARNING, false);
		throw err;
	}
	
	private void processStepX(int stepNumber, boolean check, EditActivityForm eaForm, AmpActivityVersion activity, 
			ActionMessages errors, HttpServletRequest request, HttpSession session,
			Collection relatedLinks, String stepText[]) throws Exception, AMPException{
		
		
		/**
		 * 
		 * Step Number shouldn't be necessary the same as processStepX 
		 * But it will be nice to keep the step's in a order
		 * 
		 */
		switch (stepNumber) {
		/**
		 * Step Text should be set before runing the handler so forwards can be properlly set
		 * in case of any exception
		 */
		case 0:
			stepText[stepNumber] = "1";
			processStep1(check, eaForm, activity, errors, request);
			break;
		case 1:
			stepText[stepNumber] = "1_5";
			processStep1_5(check, eaForm, activity, errors, request);
			break;
		case 2:
			stepText[stepNumber] = "2";
			processStep2(check, eaForm, activity, errors, request, session);
			break;
		case 3:
			stepText[stepNumber] = "3";
			processStep3(check, eaForm, activity, errors, request);
//			if (check == false)
//				symErr();
			break;
		case 4:
			stepText[stepNumber] = "4";
			processStep4(check, eaForm, activity, errors, request);
			break;
		case 5:
			stepText[stepNumber] = "5";
			processStep5(check, eaForm, activity, errors, request);
			break;
		case 6:
			stepText[stepNumber] = "6";
			processStep6(check, eaForm, activity, errors, request, relatedLinks);
			break;
		case 7:
			stepText[stepNumber] = "7";
			processStep7(check, eaForm, activity, errors, request);
			break;
		case 8:
			stepText[stepNumber] = "8";
			boolean draft = !eaForm.getIdentification().getDraft();
			processStep8(draft, eaForm, activity, errors, request);
			break;
		case 9:
			stepText[stepNumber] = "9";
			processStep9(check, eaForm, activity, errors, request);
			break;
		case 10:
			stepText[stepNumber] = "11";
			processStep11(check, eaForm, activity, errors, request);
			break;
		case 11:
			stepText[stepNumber] = "14";
			processStep14(check, eaForm, activity, errors, request);
			break;
		default:
			break;
		}
		
	}
	
	private void cleanup(EditActivityForm eaForm, HttpSession session, HttpServletRequest request, ActionMapping mapping, Long actId, TeamMember tm){
		
		eaForm.getFunding().setFundDonor(null);
		eaForm.setStep("1");
		eaForm.setReset(true);
		eaForm.getDocuments().setDocReset(true);
		eaForm.getLocation().setLocationReset(true);
		//eaForm.get.setOrgPopupReset(true);
		//eaForm.setOrgSelReset(true);
		eaForm.getComponents().setComponentReset(true);
//		eaForm.setSectorReset(true);
		eaForm.getPhisycalProgress().setPhyProgReset(true);
		// Clearing comment properties
		eaForm.getComments().getCommentsCol().clear();
		eaForm.getComments().setCommentFlag(false);
		// Clearing approval process properties
		eaForm.setWorkingTeamLeadFlag("no");
		eaForm.getFunding().setFundingRegions(null);
		eaForm.getFunding().setRegionalFundings(null);
		eaForm.getPlanning().setLineMinRank(null);
		eaForm.getPlanning().setPlanMinRank(null);
                    eaForm.setTeamLead(false);

		/* Clearing categories */
        eaForm.getIdentification().setProcurementSystem(new Long(0));
        eaForm.getIdentification().setReportingSystem(new Long(0));
        eaForm.getIdentification().setAuditSystem(new Long(0));
        eaForm.getIdentification().setInstitutions(new Long(0));
        eaForm.getIdentification().setAccessionInstrument(new Long(0));
        eaForm.getIdentification().setAcChapter(new Long(0));
		eaForm.getIdentification().setStatusId(new Long(0));
		eaForm.getIdentification().setGbsSbs(new Long(0));
        eaForm.getIdentification().setDraft(null);
        eaForm.getIdentification().setProjectCategory(new Long(0));
		/* END - Clearing categories */

		eaForm.setPageId(-1);
		//UpdateDB.updateReportCache(activity.getAmpActivityId());
		eaForm.reset(mapping, request);

		if (session.getAttribute(Constants.AMP_PROJECTS) != null) {

			Collection col = (Collection) session.getAttribute(
					Constants.AMP_PROJECTS);
			AmpProject project = new AmpProject();
			project.setAmpActivityId(actId);
			col.remove(project);

			Collection actIds = new ArrayList();
			actIds.add(actId);
			Collection dirtyActivities = DesktopUtil.getAmpProjects(actIds);
			if(dirtyActivities!=null && dirtyActivities.size()>0){
				Iterator pItr = dirtyActivities.iterator();
				if (pItr.hasNext()) {
					AmpProject proj = (AmpProject) pItr.next();
					col.add(proj);
				}
			}

			session.setAttribute(Constants.AMP_PROJECTS,col);
			session.setAttribute(Constants.DIRTY_ACTIVITY_LIST,dirtyActivities);
			session.setAttribute(Constants.DESKTOP_SETTINGS_CHANGED,new Boolean(true));
		}

		if (tm.getTeamHead()) {
			if (session.getAttribute(Constants.MY_TASKS) != null) {
				session.removeAttribute(Constants.MY_TASKS);
			}
		}
		
	}
	
	private Long switchSave(RecoverySaveParameters rsp)
			throws Exception{
		
		Long actId;
		if (rsp.getEaForm().isEditAct()) {
			rsp.setOldActivityId(rsp.getEaForm().getActivityId());
			rsp.setEdit(true);

			/*actId = ActivityUtil.saveActivity(activity, eaForm.getActivityId(),
					true, eaForm.getCommentsCol(), eaForm
					.isSerializeFlag(), field, relatedLinks, tm
					.getMemberId(), eaForm.getIndicatorsME(),tempComp,eaForm.getContracts(), alwaysRollback);
			*/
			actId = ActivityUtil.saveActivity(rsp);
		}
		else{
			rsp.setOldActivityId(null);
			rsp.setEdit(false);
            //Added for activity versioning.
            rsp.setOldActivityId(rsp.getEaForm().getActivityId());
			/*actId = ActivityUtil.saveActivity(activity, null, false, 
					eaForm.getCommentsCol(), eaForm.isSerializeFlag(),
					field, relatedLinks,tm.getMemberId() , 
					eaForm.getIndicatorsME(), tempComp, eaForm.getContracts(), 
					alwaysRollback);
			*/
			actId = ActivityUtil.saveActivity(rsp);
		}
		return actId;
	}
	
	public Long recoverySave(RecoverySaveParameters rsp) throws AMPException{
		
		AMPException err;
		Long actId;
		
		AmpActivityVersion recoveryActivity=null;
		boolean recoveryMode = false;
		logger.debug("Attempting normal save!");
		try {
			rsp.setAlwaysRollback(false);
			//symErr();
			actId = switchSave(rsp);
			logger.debug("Succeeded!");
			return actId;
		} catch (Exception e) {
			// Record error that initially caused save problems
			AMPException ae = ExceptionFactory.newAMPException(Constants.AMP_ERROR_LEVEL_ERROR, true, e);
			ae.addTag(MODULE_TAG); //even if the error only gets reported we still have to tag it
			ErrorReportingPlugin.handle(ae, logger);
			//logger.error(e);  //no need for this, the ErrorReporting will log the error too
			
			recoveryMode = true;
		}
		
		if (recoveryMode){
			logger.warn("<<<<RECOVERY MODE>>>>");
			rsp.setDidRecover(true);
			int currentStep = 0;
			int badSteps = 0;
			
			//we try to add each step to the amp activity and after adding each step we try to save
			//if the save fails we exclude the step, and we rebuild the AmpActivityVersion without that step
			boolean rebuild = false;
			logger.debug("Building savable activity!");
			while (currentStep < rsp.getNoOfSteps() || rebuild){
				recoveryActivity = new AmpActivityVersion();
				rsp.setActivity(recoveryActivity);
				thisStep:
				{
					//We force this step 
					try {
						processActivityMustHaveInfo(rsp.getEaForm(), recoveryActivity);
						processPreStep(rsp.getEaForm(), recoveryActivity, rsp.getTm(),new Boolean[]{ rsp.isCreatedAsDraft()});

						//we set the activity as draft
						//this must be set after the method processPreStep so it overrides the value
						recoveryActivity.setCreatedAsDraft(true);
						
					} catch (Exception e) {
						//these steps are essential
						logger.error("Essential Step Failure!");
						
						//err = new AMPException(Constants.AMP_ERROR_LEVEL_ERROR, false, e);
						//throw err;
						
						AMPUncheckedException aue = ExceptionFactory.newAMPUncheckedException(Constants.AMP_ERROR_LEVEL_CRITICAL, false, e);
						aue.addTag(MODULE_TAG);
						throw aue;
					}				
					//We rebuild AmpActivityVersion including all steps lower than currentStep
					logger.debug("Adding previous steps!");
					for (int i = 0; i < currentStep; i++)
						if (!rsp.getStepFailure()[i])
							try {
								processStepX(i, false, rsp.getEaForm(), recoveryActivity, rsp.getErrors(), rsp.getRequest(), rsp.getSession(), rsp.getRelatedLinks(), rsp.getStepText());
							} catch (Exception e) {
								// All steps till here should be validated, but let's take precaution
								rsp.getStepFailure()[i] = true;
								
								//Log a small error, that here I should never be
								AMPException aue = new AMPException(Constants.AMP_ERROR_LEVEL_ERROR, true, e);
								aue.addTag(MODULE_TAG);
								ErrorReportingPlugin.handle(aue, logger);
								
								//We invalidated this step so let's take it all over again
								badSteps++;
								if (badSteps >= rsp.getNoOfSteps()){
									logger.error("WARNING: FOREVER LOOP SPOTTED!");
									//precaution so we don't end in a forever loop
									AMPUncheckedException aue2 = new AMPUncheckedException(Constants.AMP_ERROR_LEVEL_ERROR, false, "FOREVER LOOP SPOTTED!");
									aue2.addTag(MODULE_TAG);
									throw aue2;
								}
								break thisStep;
							}
					//no more problems with the steps ... we reset the counter
					badSteps = 0;
					logger.debug("Activity built!");
					if (rebuild){ //if we needed just to rebuild the activity then exit
						//adding final touches to activity
						processPostStep(rsp.getEaForm(), recoveryActivity, rsp.getTm());
						recoveryActivity.setDraft(true);
						
						rebuild = false;
						break thisStep;
					}
					logger.debug("Adding step:" + String.valueOf(currentStep));
					//we got to the step that we're trying to add 
					try{
						processStepX(currentStep, false, rsp.getEaForm(), recoveryActivity, rsp.getErrors(), rsp.getRequest(), rsp.getSession(), rsp.getRelatedLinks(), rsp.getStepText());
					} catch (Exception e) {
						logger.debug("    FAILED!");
						//mark the step as failed 
						rsp.getStepFailure()[currentStep] = true;
						rsp.getStepFailureText()[currentStep] = new String(ActivityUtil.stackTraceToString(e));
						//no need to try saving ... because we didn't improve anything
						currentStep++;
						
						//Log a small error know what happened
						AMPException aue = new AMPException(Constants.AMP_ERROR_LEVEL_ERROR, true, e);
						aue.addTag(MODULE_TAG);
						ErrorReportingPlugin.handle(aue, logger);

						break thisStep;
					}
					
					//adding final touches to activity
					processPostStep(rsp.getEaForm(), recoveryActivity, rsp.getTm());
					recoveryActivity.setDraft(true);
					
					logger.debug("Attempting partial save!");
					//now let's try saving with the currently built AmpActivity
					try {
						rsp.setAlwaysRollback(true);
						actId = switchSave(rsp);						
					} catch (Exception e) {
						logger.debug("     FAILED!");
						rsp.getStepFailure()[currentStep] = true;
						rsp.getStepFailureText()[currentStep] = new String(ActivityUtil.stackTraceToString(e));
						
						//Log a error -- see what happens
						AMPException aue = new AMPException(Constants.AMP_ERROR_LEVEL_ERROR, true, e);
						aue.addTag(MODULE_TAG);
						ErrorReportingPlugin.handle(aue, logger);
					}
					if (currentStep == rsp.getNoOfSteps() - 1) //if the last added Step has failed then rebuild the activity
						rebuild = true;
					currentStep++;
				}
			}
			
		}
		logger.debug("PARTIAL SAVE!");
		try {
			if (recoveryActivity != null){
				rsp.setAlwaysRollback(false);
				actId = switchSave(rsp);
			}
			else{
				AMPException ae = new AMPException(Constants.AMP_ERROR_LEVEL_ERROR, false, "Recover Activity NULL");
				ae.addTag(MODULE_TAG);
				throw ae;
			}
			logger.debug("SUCCESS!");
			return actId;
		} catch (Exception e) {
			logger.debug("FAILED!!!!!!!!!!!!");

			AMPUncheckedException aue = ExceptionFactory.newAMPUncheckedException(Constants.AMP_ERROR_LEVEL_ERROR, false, e);
			aue.addTag(MODULE_TAG);
			//err = new AMPException(Constants.AMP_ERROR_LEVEL_ERROR, false, e);
			throw aue;
		}
	}
	
	//refractoring Save activity
	//Arty - AMP-4012
	//
	// errors will be treated per step, so user will be redirected to the first step where he has errors 
	// and he will be warned about ALL the errors that he has to correct .. thus improving efficiency for the user
	//
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//BIG Try Catch to Tag errors
		try{
		Site site = RequestUtils.getSite(request);
		Locale navigationLanguage = RequestUtils.getNavigationLanguage(request);
							
		Long actId = null;
		HttpSession session = request.getSession();
		//Set<Components<AmpComponentFunding>> tempComp = new HashSet<Components<AmpComponentFunding>>();
		ampContext = getServlet().getServletContext();
		ActionMessages errors = new ActionMessages();
		EditActivityForm eaForm = (EditActivityForm) form;
		AmpActivityVersion activity = new AmpActivityVersion();
		Collection relatedLinks = new ArrayList();
		
		eaForm.setEditAct(false);
		
		/**
		 * Forward the user if:
		 * if user has not logged in
		 * 
		 */
		if (session.getAttribute("currentMember") == null || eaForm.getPageId() < 0 || eaForm.getPageId() > 1) {
			return mapping.findForward("index");
		}

		//Some session cleanup
		//session.removeAttribute("report");
		//session.removeAttribute("reportMeta");
		session.removeAttribute("forStep9");
		session.removeAttribute("commentColInSession");

		TeamMember tm = null;
		if (session.getAttribute("currentMember") != null)
			tm = (TeamMember) session.getAttribute("currentMember");

		Boolean[] createdAsDraft={false};
		//any processing that needs to be done to the activity before the actual steps
		
		processPreStep(eaForm, activity, tm, createdAsDraft);

		String toDelete = request.getParameter("delete");
		Long idForOriginalActivity = (Long) request.getSession().getAttribute("idForOriginalActivity");
		if (toDelete == null || (!toDelete.trim().equalsIgnoreCase("true"))) {
			AmpActivityVersion act = null;
			if (eaForm.isEditAct() == false) {
				/*AmpActivityVersion act = ActivityUtil.getActivityByName(eaForm.getIdentification().getTitle());
				if (act != null) {
				//storing original id is needed if user decides not to overwrite activity,but cancel.
				//otherwise incorrect actId is set in form and editing/create will work incorrect
				if(eaForm.isEditAct()==true && idForOriginalActivity==null){ //need to keep edited activity id
					request.getSession().setAttribute("idForOriginalActivity",eaForm.getActivityId());
				}
				if(eaForm.isEditAct()==false && idForOriginalActivity==null){
					request.getSession().setAttribute("idForOriginalActivity",new Long(-1));
				}
					request.setAttribute("existingActivity", act);
					eaForm.setActivityId(act.getAmpActivityId());
				logger.debug("Activity with the name "	+ eaForm.getIdentification().getTitle() + " already exist.");
					return mapping.findForward("activityExist");
				}*/
			}
		} else if (toDelete.trim().equals("true")) {
			eaForm.setEditAct(true);
			request.getSession().removeAttribute("idForOriginalActivity");
		} else {
			logger.debug("No duplicate found");
		}

		
		boolean titleFlag = false;
		boolean statusFlag = false;

		
		
		//The number of processStep methods we have
		final int noOfSteps = 12;
		String stepText[] = new String[noOfSteps];
		Boolean stepFailure[] = new Boolean[noOfSteps];
		String stepFailureText[] = new String[noOfSteps];
		for (int i = 0; i < noOfSteps; i++){
			stepFailure[i] = false;
			stepText[i] = new String();
			stepFailureText[i] = new String();
		}
		eaForm.setStepText(stepText);
		eaForm.setStepFailure(stepFailure);
		eaForm.setStepFailureText(stepFailureText);
		
		logger.debug("No of steps:" + String.valueOf(eaForm.getSteps().size()));
		
		int stepNumber = 0;
		while (stepNumber < noOfSteps){
			try {
				processStepX(stepNumber, true, eaForm, activity, errors, request, session, relatedLinks, stepText);
				} catch (AMPException error) {
				if (!error.isContinuable()){
					if (error.getLevel() == Constants.AMP_ERROR_LEVEL_WARNING){
						//in this case user messed up, the ActionMessage was set-up in the method
						//we just redirect him to the page where he needs to correct the input
						logger.debug(">>> Warning -> redirect to step:" + stepText[stepNumber]);
						eaForm.setStep(stepText[stepNumber]);
						request.setAttribute("step", stepText[stepNumber]);
						String forwardText = "addActivityStep" + stepText[stepNumber];
						forwardText = "addActivityStepXNR";
						return mapping.findForward(forwardText);
					}
					else{
						//TODO: redirect to custom error page
						logger.error(">>> Error on step:" + stepText[stepNumber]);
					}
				}
				else{
					logger.error(">>> Error that is not continuable on step:" + stepText[stepNumber]);
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(">>> Unknown error on step:" + stepText[stepNumber]);
			}
			stepNumber++;
		}

		if(eaForm.getCustomFields()!=null){
			List<CustomField<?>> customFields = eaForm.getCustomFields();
			Iterator<CustomField<?>> cfi = customFields.iterator();
			while(cfi.hasNext()){
				CustomField customField = cfi.next();
				String propertyName = customField.getAmpActivityPropertyName();
				if(propertyName == null){
					logger.warn("Please set AmpActivityPropertyName for all custom fields.");
					continue;
				}
				try{
					if(customField.getValue()!=null)
					BeanUtils.setProperty(activity, customField.getAmpActivityPropertyName(), customField.getValue());
				}catch(Exception e){
					logger.error("Custom Field [" + customField.getAmpActivityPropertyName() + "] exception", e);
				}
			}
		}
				
		//TAG HERE YOU SHOULD BE

		Long field = null;
		if (eaForm.getComments().getField() != null)
			field = eaForm.getComments().getField().getAmpFieldId();



		
		/*
		 * Do all remaining processing for the activity before saving
		 * Both cases (when editing and when creating a new activity) are treated inside the method 
		 */
	    processPostStep(eaForm, activity, tm);
	    RecoverySaveParameters rsp;
	    if (eaForm.isEditAct()) {
			List<String> auditTrail = AuditLoggerUtil.generateLogs(
					activity, eaForm.getActivityId());
			//*** Preparing parameters for the recovery save
			rsp = new RecoverySaveParameters();
			rsp.setNoOfSteps(noOfSteps);
			rsp.setStepText(stepText);
			rsp.setStepFailure(stepFailure);
			rsp.setStepFailureText(stepFailureText);
			rsp.setEaForm(eaForm);
			rsp.setTm(tm);
			
			rsp.setActivity(activity);
			rsp.setCreatedAsDraft(createdAsDraft[0]);
			rsp.setErrors(errors);
			rsp.setRequest(request);
			rsp.setSession(session);
			rsp.setField(field);
			
			rsp.setRelatedLinks(relatedLinks);
			//rsp.setTempComp(tempComp);
			rsp.setDidRecover(false);
			//***
			
			//get member, who previously edited activity. Needed for approved activity trigger
        	AmpTeamMember previouslyUpdatedBy=ActivityUtil.getActivityUpdator(eaForm.getActivityId());
			
			// update an existing activity
			actId = recoverySave(rsp);
			activity = rsp.getActivity();
			
			// in case it was "overwrite" existing activity,need to remove previous act(that was opened for editing)...
			if(toDelete!=null && toDelete.trim().equals("true") && idForOriginalActivity!=null && ! idForOriginalActivity.equals(new Long(-1))){
				ActivityUtil.deleteActivity(idForOriginalActivity);
			}
			
			String additionalDetails="approved";
			//if validation is off in team setup no messages should be generated
			if (!"allOff".equals(DbUtil.getValidationFromTeamAppSettings(tm.getTeamId()))){
				AmpActivityVersion aAct = ActivityUtil.loadActivity(actId);
                if (aAct.getDraft() != null && !aAct.getDraft() &&
                		!(aAct.getApprovalStatus().equals(eaForm.getIdentification().getPreviousApprovalStatus()) && aAct.getApprovalStatus().equals(Constants.EDITED_STATUS))) { //AMP-6948
                    if (aAct.getApprovalStatus().equals(Constants.APPROVED_STATUS)) {
                        if (!eaForm.getIdentification().getApprovalStatus().equals(Constants.APPROVED_STATUS)||(eaForm.getIdentification().getWasDraft()!=null&&eaForm.getIdentification().getWasDraft())) {                                	
                            if(!eaForm.getIdentification().getPreviousApprovalStatus().equals(Constants.APPROVED_STATUS)){ //if previous status was approved,no need to create new Approved Activity Alert
                            	new ApprovedActivityTrigger(aAct, previouslyUpdatedBy);
                            }                                	
                        }
                    } else {
                        new NotApprovedActivityTrigger(aAct);
                        additionalDetails="pending approval";
                    }
                }
                else{
                	if(aAct.getDraft() != null && aAct.getDraft()){
                		additionalDetails="draft";
                	}
                }
			}
                        
			
			/*actId = ActivityUtil.saveActivity(activity, eaForm.getActivityId(),
					true, eaForm.getCommentsCol(), eaForm
					.isSerializeFlag(), field, relatedLinks, tm
					.getMemberId(), eaForm.getIndicatorsME(),tempComp,eaForm.getContracts());
			*/
			
			//update lucene index
			LuceneUtil.addUpdateActivity(request, true, actId);
			//for logging the activity
			AuditLoggerUtil.logActivityUpdate(request,
					activity, auditTrail);

			// remove the activity details from the edit activity list
			if (toDelete == null
					|| (!toDelete.trim().equalsIgnoreCase("true"))) {
				String sessId = session.getId();
				synchronized (ampContext) {
					HashMap activityMap = (HashMap) ampContext
					.getAttribute(Constants.EDIT_ACT_LIST);
					activityMap.remove(sessId);
					ArrayList sessList = (ArrayList) ampContext
					.getAttribute(Constants.SESSION_LIST);
					sessList.remove(sessId);
					Collections.sort(sessList);
					ampContext.setAttribute(Constants.EDIT_ACT_LIST,
							activityMap);
					ampContext.setAttribute(Constants.SESSION_LIST,
							sessList);

					HashMap tsList = (HashMap) ampContext
					.getAttribute(Constants.TS_ACT_LIST);
					if (tsList != null) {
						tsList.remove(eaForm.getActivityId());
					}
					ampContext.setAttribute(Constants.TS_ACT_LIST,
							tsList);
					HashMap uList = (HashMap) ampContext
					.getAttribute(Constants.USER_ACT_LIST);
					if (uList != null) {
						uList.remove(tm.getMemberId());
					}
					ampContext.setAttribute(Constants.USER_ACT_LIST,
							uList);

				}
			}
			DynamicColumnsUtil.createInexistentMtefColumns(ampContext);
		} else {
			// create a new activity
			

			//*** Preparing parameters for the recovery save
			rsp = new RecoverySaveParameters();
			rsp.setNoOfSteps(noOfSteps);
			rsp.setStepText(stepText);
			rsp.setStepFailure(stepFailure);
			rsp.setStepFailureText(stepFailureText);
			rsp.setEaForm(eaForm);
			rsp.setTm(tm);
			
			rsp.setActivity(activity);
			rsp.setCreatedAsDraft(createdAsDraft[0]);
			rsp.setErrors(errors);
			rsp.setRequest(request);
			rsp.setSession(session);
			rsp.setField(field);
			
			rsp.setRelatedLinks(relatedLinks);
			//rsp.setTempComp(tempComp);
			rsp.setDidRecover(false);
			//***

			actId = recoverySave(rsp);			
			activity = rsp.getActivity();
			String additionalDetails="approved";
                        
			AmpActivityVersion aAct=ActivityUtil.loadActivity(actId);
			//get member, who previously edited activity. Needed for approved activity trigger
        	AmpTeamMember previouslyUpdatedBy=ActivityUtil.getActivityUpdator(eaForm.getActivityId());
			//if validation is off in team setup no messages should be generated
				if (activity.getDraft() != null
						&& !activity.getDraft()
						&& !("allOff".equals(DbUtil.getValidationFromTeamAppSettings(tm.getTeamId())))) {
            	if(activity.getApprovalStatus().equals(Constants.APPROVED_STATUS)||activity.getApprovalStatus().equals(Constants.STARTED_APPROVED_STATUS)){
            		AmpTeamMemberRoles role=aAct.getActivityCreator().getAmpMemberRole();
            		boolean isTeamHead=false;
            		if(role.getTeamHead()!=null&&role.getTeamHead()){
            			isTeamHead=true;
            		}
					if(!role.isApprover() ){
            			new ApprovedActivityTrigger(aAct,previouslyUpdatedBy); //if TL or approver created activity, then no Trigger is needed
            		}
            	}else{
            		new NotApprovedActivityTrigger(aAct);
            		additionalDetails="pending approval";
            	}
            }
			else{
				if (activity.getDraft() != null&& activity.getDraft()){
					additionalDetails="draft";
				}
			}
			/*actId = ActivityUtil.saveActivity(activity, null, false, eaForm.getCommentsCol(), eaForm.isSerializeFlag(),
					field, relatedLinks,tm.getMemberId() , eaForm.getIndicatorsME(), tempComp, eaForm.getContracts());
			*/
			//update lucene index
			LuceneUtil.addUpdateActivity(request, false, actId);
			//for logging the activity
				if (eaForm.getActivityId() != null&& eaForm.getActivityId() != 0) {
					List<String> details=new ArrayList<String>();
					details.add(additionalDetails);
					AuditLoggerUtil.logActivityUpdate(request, activity,details);
				} else {
					AuditLoggerUtil.logObject(request, activity, "add",additionalDetails);
				}
			//AuditLoggerUtil.logObject(request, activity, "add",additionalDetails);
			
			DynamicColumnsUtil.createInexistentMtefColumns(ampContext);
		}

		//If we're adding an activity, create system/admin message
		if(activity.getDraft()!=null && !activity.getDraft()) {
			ActivitySaveTrigger ast=new ActivitySaveTrigger(activity);
		}
		
        // AMP-4660: Add filters for viewed, created and updated activities.
			if (eaForm.getActivityId() != null) {
				ActivityUtil.updateActivityAccess((User) request.getSession().getAttribute("org.digijava.kernel.user"),
						eaForm.getActivityId(), true);
			} /*
			 * else { ActivityUtil.updateActivityAccess((User)
			 * request.getSession().getAttribute("org.digijava.kernel.user"),
			 * actId, true); }
			 */

			// When an activity is draft then delete the old version (and is not
			// a new activity).
			if (eaForm.getActivityId() != null) {
				AmpActivityVersion originalActivity = ActivityUtil.getAmpActivityVersion(eaForm.getActivityId());
				if (originalActivity.getDraft() != null && originalActivity.getDraft().booleanValue() == true) {
					ActivityUtil.deleteActivity(eaForm.getActivityId());
				} else {
					// Delete old version when versioning is disabled on GS.
					if (ActivityVersionUtil.numberOfVersions() == 0) {
						ActivityUtil.deleteActivity(eaForm.getActivityId());
					}
				}
			}
		
		
		if(DocumentUtil.isDMEnabled()) {
			Site currentSite = RequestUtils.getSite(request);
			Node spaceNode = DocumentUtil.getDocumentSpace(currentSite,
					activity.getDocumentSpace());
			DocumentUtil.renameNode(spaceNode, activity.getName());
		}

		//We need to getPageId before the cleanup so it doesn't get reset
		int temp = eaForm.getPageId();
		if(temp == 0) {
			temp = 1;
		}

		/**
		 *  Perform session & form cleanup
		 */
        int redirectDraft=eaForm.getDraftRedirectedPage();
		cleanup(eaForm, session, request, mapping, actId, tm);

		//OLD!!!
		//boolean surveyFlag = false;
		
        if(eaForm.getMessages() != null) {
            eaForm.getMessages().clear();
        }
        
		if (temp == 0)
			return mapping.findForward("adminHome");
		else if (temp == 1) {
			//OLD surveyFlag was always false
			/*if (surveyFlag) { // forwarding to edit survey action for
				// saving survey responses
				logger.debug("forwarding to edit survey action...");
				return mapping.findForward("saveSurvey");
			} else {*/
            if (rsp.isDidRecover()) {
                return mapping.findForward("saveErrors");
            } else {
            	if ((activity.getDraft()!=null && !activity.getDraft()) || redirectDraft==Constants.DRAFRT_GO_TO_DESKTOP) {
            		return mapping.findForward("viewMyDesktop");
                } else {
                    //eaForm.setActivityId(ActivityVersionUtil.getLastActivityFromGroup(activity.getAmpActivityGroup().getAmpActivityGroupId()).getAmpActivityId());
                    // Set to 1 or next time will be -1 and will fail some checks.
                    eaForm.setPageId(1);
                    eaForm.addMessage("message.aim.draftSavedSuccesfully", "Your changes have been saved successfully.");
                    eaForm.setActivityId(actId);
                    return mapping.findForward("saveDraft");
                }
            }
			//}
		} else {
			logger.info("returning null....");
			return null;
		}
		}catch (Exception e) {
			AMPUncheckedException au = ExceptionFactory.newAMPUncheckedException(e); //we used unchecked so that code currently using this method won't be affected(no try catch)
			au.addTag(MODULE_TAG); //tag this in concordance to the documents on Confluence
			au.setContinuable(false); //we don't know what happend, we can't handle it
			au.setLevel(Constants.AMP_ERROR_LEVEL_ERROR); //this should probabilly do
			throw au; //thow
		}
	}


	private boolean isSectorEnabled() {
 	    ServletContext ampContext = getServlet().getServletContext();
	    AmpTreeVisibility ampTreeVisibility=(AmpTreeVisibility) ampContext.getAttribute("ampTreeVisibility");
		AmpTemplatesVisibility currentTemplate=(AmpTemplatesVisibility) ampTreeVisibility.getRoot();
		if(currentTemplate!=null)
			if(currentTemplate.getFeatures()!=null)
				for(AmpFeaturesVisibility feature:currentTemplate.getFeatures())
				{
					if(feature.getName().compareTo("Sectors")==0)
					{
						return true;
					}
				}
		return false;
	}

	private boolean isStatusEnabled() {
 	   ServletContext ampContext = getServlet().getServletContext();

	   AmpTreeVisibility ampTreeVisibility=(AmpTreeVisibility) ampContext.getAttribute("ampTreeVisibility");

		AmpTemplatesVisibility currentTemplate=(AmpTemplatesVisibility) ampTreeVisibility.getRoot();
		if(currentTemplate!=null)
			return currentTemplate.fieldExists("Status");
		
		return false;
	}
	
	private boolean isFieldEnabled(String fieldName) {
		ServletContext ampContext = getServlet().getServletContext();

		   AmpTreeVisibility ampTreeVisibility=(AmpTreeVisibility) ampContext.getAttribute("ampTreeVisibility");

			AmpTemplatesVisibility currentTemplate=(AmpTemplatesVisibility) ampTreeVisibility.getRoot();
			if(currentTemplate!=null)
				return currentTemplate.fieldExists(fieldName);
			return false;
	}

	/**
	 * @param tempComp
	 * @param eaForm
	 * @param activity
	 */
	private void proccessComponents(EditActivityForm eaForm, AmpActivityVersion activity) {
		activity.setComponents(new HashSet());
		activity.setComponentFundings(new HashSet<AmpComponentFunding>());
		if (eaForm.getComponents().getSelectedComponents() != null) {
			Iterator<Components<FundingDetail>> itr = eaForm.getComponents().getSelectedComponents().iterator();
			while (itr.hasNext()) {
				Components<FundingDetail> comp = itr.next();
				AmpComponent ampComp = ComponentsUtil.getComponentById(comp.getComponentId());
				activity.getComponents().add(ampComp);

			
				Set<Integer> transactionTypes = new HashSet<Integer>();
				transactionTypes.add(Constants.COMMITMENT);
				transactionTypes.add(Constants.DISBURSEMENT);
				transactionTypes.add(Constants.EXPENDITURE);
				
				for (Iterator iterator = transactionTypes.iterator(); iterator
						.hasNext();) {
					Integer transactionType = (Integer) iterator.next();
				
					Iterator<FundingDetail> fdIterator = null;
					if (transactionType.equals(Constants.COMMITMENT)
							&& comp.getCommitments() != null
							&& comp.getCommitments().size() > 0) {
						fdIterator = comp.getCommitments().iterator();
					} else if (transactionType.equals(Constants.DISBURSEMENT)
							&& comp.getDisbursements() != null
							&& comp.getDisbursements().size() > 0) {
						fdIterator = comp.getDisbursements().iterator();
					} else if (transactionType.equals(Constants.EXPENDITURE)
							&& comp.getExpenditures() != null
							&& comp.getExpenditures().size() > 0) {
						fdIterator = comp.getExpenditures().iterator();
					}else{
						//maybe .getXX==null or .size()==0 
						continue;
					}
					
					while (fdIterator.hasNext()) {
						FundingDetail fd = fdIterator.next();
						AmpComponentFunding ampCompFund = null;						
						ampCompFund = new AmpComponentFunding();
						
						
						if (fd.getAmpComponentFundingId()!=null) {
							try {
								Session session = PersistenceManager.getRequestDBSession();
								Object load = session.load(AmpComponentFunding.class, fd.getAmpComponentFundingId());
								session.evict(load);
							} catch (DgException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						//ampCompFund.setAmpComponentFundingId(fd.getAmpComponentFundingId());
						ampCompFund.setActivity(activity);
						
						ampCompFund.setTransactionType(transactionType);
						
						Iterator tmpItr = eaForm.getCurrencies()
								.iterator();
						while (tmpItr.hasNext()) {
							AmpCurrency curr = (AmpCurrency) tmpItr
									.next();
							if (curr.getCurrencyCode().equals(
									fd.getCurrencyCode())) {
								ampCompFund.setCurrency(curr);
								break;
							}
						}

						ampCompFund.setReportingOrganization(null);
						ampCompFund.setTransactionAmount(FormatHelper.parseDouble(fd.getTransactionAmount()));
						ampCompFund.setTransactionDate(DateConversion
								.getDate(fd.getTransactionDate()));
						ampCompFund.setAdjustmentType(fd.getAdjustmentTypeName());
						ampCompFund.setComponent(ampComp);
						
						activity.getComponentFundings().add(ampCompFund);
						
					}
				}


				// set physical progress
				if(activity.getComponentProgress()==null)
					activity.setComponentProgress(new HashSet<AmpPhysicalPerformance>());

				if (comp.getPhyProgress() != null) {
					Iterator itr1 = comp.getPhyProgress().iterator();
					while (itr1.hasNext()) {
						PhysicalProgress phyProg = (PhysicalProgress) itr1
								.next();
						AmpPhysicalPerformance ampPhyPerf = new AmpPhysicalPerformance();
						if (phyProg.getDescription() == null
								|| phyProg.getDescription().trim()
										.length() == 0) {
							ampPhyPerf.setDescription(new String(" "));
						} else {
							ampPhyPerf.setDescription(phyProg
									.getDescription());
						}
                                                if(!phyProg.isNewProgress()){
                                                  ampPhyPerf.setAmpPpId(phyProg.
                                                      getPid());
                                                }
						ampPhyPerf.setReportingDate(DateConversion
								.getDate(phyProg.getReportingDate()));
						ampPhyPerf.setTitle(phyProg.getTitle());
						ampPhyPerf.setAmpActivityId(activity);
						ampPhyPerf.setComponent(ampComp);
						ampPhyPerf.setComments(" ");
						activity.getComponentProgress().add(ampPhyPerf);
					}
					
				}
                
			}
		}
	}

	private void saveMTEFProjections (Funding fund, AmpFunding ampFunding) {
		if ( ampFunding.getMtefProjections() != null ) {
			ampFunding.getMtefProjections().clear();
		}
		else
			ampFunding.setMtefProjections(new HashSet<AmpFundingMTEFProjection> ());
		if(fund.getMtefProjections()!=null)
		{
			Iterator mtefItr=fund.getMtefProjections().iterator();
			while (mtefItr.hasNext())
			{
				MTEFProjection mtef=(MTEFProjection)mtefItr.next();
				AmpFundingMTEFProjection ampmtef=new AmpFundingMTEFProjection();
				ampmtef.setAmount(FormatHelper.parseDouble(mtef.getAmount()));

				ampmtef.setAmpFunding(ampFunding);
				ampmtef.setAmpCurrency(CurrencyUtil.getCurrencyByCode(mtef.getCurrencyCode()));
				ampmtef.setProjected( CategoryManagerUtil.getAmpCategoryValueFromDb(mtef.getProjected()) );
				ampmtef.setProjectionDate( DateConversion.getDate(mtef.getProjectionDate()) );

				ampFunding.getMtefProjections().add(ampmtef);
			}
		}
	}
	  private boolean isPrimarySectorEnabled() {
	 	    ServletContext ampContext = getServlet().getServletContext();
		    AmpTreeVisibility ampTreeVisibility=(AmpTreeVisibility) ampContext.getAttribute("ampTreeVisibility");
			AmpTemplatesVisibility currentTemplate=(AmpTemplatesVisibility) ampTreeVisibility.getRoot();
			if(currentTemplate!=null)
				return currentTemplate.fieldExists("Primary Sector");
			return false;
	  }
	  
	  private boolean isSecondarySectorEnabled() {
	 	    ServletContext ampContext = getServlet().getServletContext();
		    AmpTreeVisibility ampTreeVisibility=(AmpTreeVisibility) ampContext.getAttribute("ampTreeVisibility");
			AmpTemplatesVisibility currentTemplate=(AmpTemplatesVisibility) ampTreeVisibility.getRoot();
			if(currentTemplate!=null)
				return currentTemplate.fieldExists("Secondary Sector");
			return false;
	  }
	  
		private boolean isInConfig(EditActivityForm eaForm, String sectorLevel) {
			List <AmpClassificationConfiguration> classConfig = eaForm.getSectors().getClassificationConfigs();
			for(AmpClassificationConfiguration cls : classConfig){
				if(cls.getName().compareToIgnoreCase(sectorLevel)==0){         
					return true;
				}
			}
			return false;
		}
}