/*
 * ActivityUtil.java
 * Created: 14 Feb, 2005
 */

package org.digijava.module.aim.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.utils.AmpCollectionUtils.KeyResolver;
import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.error.ExceptionFactory;
import org.dgfoundation.amp.error.keeper.ErrorReportingPlugin;
import org.dgfoundation.amp.utils.AmpCollectionUtils;
import org.dgfoundation.amp.utils.AmpCollectionUtils.KeyResolver;
import org.dgfoundation.amp.visibility.AmpObjectVisibility;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.action.GetFundingTotals;
import org.digijava.module.aim.action.RecoverySaveParameters;
import org.digijava.module.aim.action.ShowAddComponent;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityClosingDates;
import org.digijava.module.aim.dbentity.AmpActivityComponente;
import org.digijava.module.aim.dbentity.AmpActivityContact;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpActivityProgram;
import org.digijava.module.aim.dbentity.AmpActivityReferenceDoc;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpActor;
import org.digijava.module.aim.dbentity.AmpAhsurvey;
import org.digijava.module.aim.dbentity.AmpAhsurveyResponse;
import org.digijava.module.aim.dbentity.AmpClosingDateHistory;
import org.digijava.module.aim.dbentity.AmpComments;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpComponentFunding;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpContactProperty;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFeaturesVisibility;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpIndicatorRiskRatings;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;
import org.digijava.module.aim.dbentity.AmpIssues;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpMeasure;
import org.digijava.module.aim.dbentity.AmpModulesVisibility;
import org.digijava.module.aim.dbentity.AmpNotes;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpOrganisationContact;
import org.digijava.module.aim.dbentity.AmpPhysicalComponentReport;
import org.digijava.module.aim.dbentity.AmpPhysicalPerformance;
import org.digijava.module.aim.dbentity.AmpRegionalFunding;
import org.digijava.module.aim.dbentity.AmpRegionalObservation;
import org.digijava.module.aim.dbentity.AmpRegionalObservationActor;
import org.digijava.module.aim.dbentity.AmpRegionalObservationMeasure;
import org.digijava.module.aim.dbentity.AmpReportCache;
import org.digijava.module.aim.dbentity.AmpReportLocation;
import org.digijava.module.aim.dbentity.AmpReportPhysicalPerformance;
import org.digijava.module.aim.dbentity.AmpReportSector;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.CMSContentItem;
import org.digijava.module.aim.dbentity.EUActivity;
import org.digijava.module.aim.dbentity.IPAContract;
import org.digijava.module.aim.dbentity.IPAContractDisbursement;
import org.digijava.module.aim.dbentity.IndicatorActivity;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.helper.ActivityIndicator;
import org.digijava.module.aim.helper.ActivityItem;
import org.digijava.module.aim.helper.AmpProjectDonor;
import org.digijava.module.aim.helper.Components;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.CustomField;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.aim.helper.FundingValidator;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.Issues;
import org.digijava.module.aim.helper.Measures;
import org.digijava.module.aim.helper.PhysicalProgress;
import org.digijava.module.aim.helper.RelatedLinks;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * ActivityUtil is the persister class for all activity related
 * entities
 *
 * @author Priyajith
 */
public class ActivityUtil {

  private static Logger logger = Logger.getLogger(ActivityUtil.class);

  ///**
  // * Persists an AmpActivity object to the database
  // * This function is used to create a new activity
  // * @param activity The activity to be persisted
  // */
  //I've seen no references so I marked it deprecated
  //@Deprecated
//  public static Long saveActivity(AmpActivity activity, ArrayList commentsCol,
//                                  boolean serializeFlag, Long field,
//                                  Collection relatedLinks, Long memberId,
//                                  Set<Components<AmpComponentFunding>> ampTempComp,List<IPAContract> contracts) throws Exception {
//    /*
//     * calls saveActivity(AmpActivity activity,Long oldActivityId,boolean edit)
//     * by passing null and false to the paramindicatorseters oldActivityId and edit respectively
//     * since this is creating a new activity
//     */
//    return saveActivity(activity, null, false, commentsCol, serializeFlag,
//                        field, relatedLinks, memberId, null, ampTempComp, contracts, false);
//  }

  /**
   * Persist an AmpActivity object to the database
   * This function is used to either update an existing activity
   * or creating a new activity. If the parameter 'edit' is set to
   * true the function will update an existing activity with id
   * given by the parameter 'oldActivityId'. If the 'edit' parameter
   * is false, the function will create a new activity
   *
   * @param activity The AmpActivity object to be persisted
   * @param oldActivityId The id of the AmpActivity object which is to be updated
   * @param edit This boolean variable represents whether to create a new
   * activity object or to update the existing activity object
   * @throws Exception 
   */
public static Long saveActivity(RecoverySaveParameters rsp) throws Exception {
	//Retrieving parameters
	AmpActivity activity = rsp.getActivity();
	Long oldActivityId = rsp.getOldActivityId();
	boolean edit = rsp.isEdit();
	ArrayList commentsCol = rsp.getEaForm().getComments().getCommentsCol();
	boolean serializeFlag = rsp.getEaForm().isSerializeFlag();
	Long field = rsp.getField();
	Collection relatedLinks = rsp.getRelatedLinks();
	Long memberId = rsp.getTm().getMemberId();
	Collection indicators = rsp.getEaForm().getIndicator().getIndicatorsME();
	//Set<Components<AmpComponentFunding>> componentsFunding = rsp.getTempComp();
	List<IPAContract> contracts = rsp.getEaForm().getContracts().getContracts();
	boolean alwaysRollback = rsp.isAlwaysRollback();
	//***
	
	
	
    logger.debug("In save activity " + activity.getName());
    Session session = null;
    Transaction tx = null;
    AmpActivity oldActivity = null;

    Long activityId = null;
    Set fundSet		= null;
    boolean exceptionRaised = false;
    
    AMPException savedEx = new AMPException(Constants.AMP_ERROR_LEVEL_ERROR, false, "Save activity failure");
    
    try {
      session = PersistenceManager.getRequestDBSession();
      session.connection().setAutoCommit(false);
      tx = session.beginTransaction();
      
      AmpTeamMember member = (AmpTeamMember) session.load(AmpTeamMember.class,
          memberId);

      if (edit) { /* edit an existing activity */
        oldActivity = (AmpActivity) session.load(AmpActivity.class,
                                                 oldActivityId);

        activityId = oldActivityId;

        activity.setAmpActivityId(oldActivityId);

        if (oldActivity == null) {
          logger.debug("Previous Activity is null");
        }

        // delete previos fundings and funding details
        /*Set fundSet = oldActivity.getFunding();
        /*if (fundSet != null) {
          Iterator fundSetItr = fundSet.iterator();
          while (fundSetItr.hasNext()) {
            AmpFunding fund = (AmpFunding) fundSetItr.next();

                   Set fundDetSet = fund.getFundingDetails();
                   if (fundDetSet != null) {
             Iterator fundDetItr = fundDetSet.iterator();
             while (fundDetItr.hasNext()) {
             AmpFundingDetail ampFundingDetail = (AmpFundingDetail) fundDetItr.next();
              session.delete(ampFundingDetail);
             }
                   }
            session.delete(fund);
          }
        }*/

        // delete previous regional fundings
        oldActivity.getRegionalFundings().clear();
        /*fundSet = oldActivity.getRegionalFundings();
        if (fundSet != null) {
          Iterator fundSetItr = fundSet.iterator();
          while (fundSetItr.hasNext()) {
            AmpRegionalFunding regFund = (AmpRegionalFunding) fundSetItr.next();
            session.delete(regFund);
          }
        }*/

        // delete all previous components
        /*Set comp = oldActivity.getComponents();
             if (comp != null) {
         Iterator compItr = comp.iterator();
         while (compItr.hasNext()) {
          AmpComponent ampComp = (AmpComponent) compItr.next();
          session.delete(ampComp);
         }
             }*/

        // delete all previous org roles
        oldActivity.getOrgrole().clear();
       /* Set orgrole = oldActivity.getOrgrole();
        if (orgrole != null) {
          Iterator orgroleItr = orgrole.iterator();
          while (orgroleItr.hasNext()) {
            AmpOrgRole ampOrgrole = (AmpOrgRole) orgroleItr.next();
            session.delete(ampOrgrole);
          }
        }*/

        // delete all previous closing dates
        oldActivity.getClosingDates().clear();
        /*Set closeDates = oldActivity.getClosingDates();
        if (closeDates != null) {
          Iterator dtItr = closeDates.iterator();
          while (dtItr.hasNext()) {
            AmpActivityClosingDates date = (AmpActivityClosingDates) dtItr.next();
            session.delete(date);
          }
        }*/

        // delete all previous issues
        oldActivity.getIssues().clear();
/*        Set issues = oldActivity.getIssues();
        if (issues != null) {
          Iterator iItr = issues.iterator();
          while (iItr.hasNext()) {
            AmpIssues issue = (AmpIssues) iItr.next();
            session.delete(issue);
          }
        }*/
        
        if(oldActivity.getRegionalObservations()!=null) {
        	oldActivity.getRegionalObservations().clear();
        }

        // delete all previous Reference Docs
        oldActivity.getReferenceDocs().clear();
        /*if (oldActivity.getReferenceDocs() != null) {
          Iterator refItr = oldActivity.getReferenceDocs().iterator();
          while (refItr.hasNext()) {
            AmpActivityReferenceDoc refDoc = (AmpActivityReferenceDoc) refItr.next();
            session.delete(refDoc);
          }
        }*/

        oldActivity.getComponentes().clear();
        
        // delete all previous sectors
        oldActivity.getSectors().clear();
       /* if (oldActivity.getSectors() != null) {
          Iterator iItr = oldActivity.getSectors().iterator();
          while (iItr.hasNext()) {
            AmpActivitySector sec = (AmpActivitySector) iItr.next();
            session.delete(sec);
          }
        }*/
        
//				 delete all previous costs
        oldActivity.getCosts().clear();
        /*if (oldActivity.getCosts() != null) {
          Iterator iItr = oldActivity.getCosts().iterator();
          while (iItr.hasNext()) {
            EUActivity eu = (EUActivity) iItr.next();
            session.delete(eu);
          }
        }
*/
        // delete all previous comments
        
          ArrayList col = org.digijava.module.aim.util.DbUtil.
              getAllCommentsByActivityId( oldActivity.getAmpActivityId(), session );
          if (col != null) {
            Iterator itr = col.iterator();
            while (itr.hasNext()) {
              AmpComments comObj = (AmpComments) itr.next();
              session.delete(comObj);
            }
          }
        
        

        if ( oldActivity.getCategories() != null ) {
        	oldActivity.getCategories().clear();
        }
        else
        	oldActivity.setCategories( new HashSet() );

        if ( oldActivity.getFunding() != null ) {
        	oldActivity.getFunding().clear();
        }
        else
        	oldActivity.setFunding( new HashSet() );
        oldActivity.setCreatedAsDraft(activity.isCreatedAsDraft());
        oldActivity.getClosingDates().clear();
        oldActivity.getComponents().clear();
      
        if ( oldActivity.getComponentProgress() != null ) {
	        Collection<AmpPhysicalPerformance> oldProgress= oldActivity.getComponentProgress();
	      
	        for (Iterator iterator = oldProgress.iterator(); iterator.hasNext();) {
				AmpPhysicalPerformance ampPhysicalPerformance = (AmpPhysicalPerformance) iterator.next();
				session.delete(ampPhysicalPerformance);
			}
        }
        
        if ( activity.getComponentProgress() != null  ) {
	        Collection<AmpPhysicalPerformance> newProgress= activity.getComponentProgress();
	        for (AmpPhysicalPerformance ampPhysicalPerformance : newProgress) {
	        	session.save(ampPhysicalPerformance);
			}
        }
        
        oldActivity.getComponentFundings().clear();
      
        
        
        oldActivity.getDocuments().clear();
        oldActivity.getInternalIds().clear();
        oldActivity.getLocations().clear();
       // oldActivity.getOrgrole().clear();
       // oldActivity.getReferenceDocs().clear();
       // oldActivity.getSectors().clear();
       // oldActivity.getCosts().clear();
        oldActivity.getActivityDocuments().clear();
        oldActivity.getActivityPrograms().clear();

        oldActivity.setLineMinRank(activity.getLineMinRank());
        oldActivity.setPlanMinRank(activity.getPlanMinRank());
        oldActivity.setActivityCreator(activity.getActivityCreator());
        oldActivity.setActualApprovalDate(activity.getActualApprovalDate());
        oldActivity.setActualCompletionDate(activity.getActualCompletionDate());
        oldActivity.setActualStartDate(activity.getActualStartDate());
        oldActivity.setAmpId(activity.getAmpId());
        oldActivity.setCalType(activity.getCalType());
        oldActivity.setCondition(activity.getCondition());
        oldActivity.setContFirstName(activity.getContFirstName());
        oldActivity.setContLastName(activity.getContLastName());
        oldActivity.setContractors(activity.getContractors());
        oldActivity.setDescription(activity.getDescription());
        oldActivity.setDocumentSpace(activity.getDocumentSpace());
        oldActivity.setEmail(activity.getEmail());
        oldActivity.setLanguage(activity.getLanguage());

        oldActivity.setDnrCntTitle(activity.getDnrCntTitle());
        oldActivity.setDnrCntOrganization(activity.getDnrCntOrganization());
        oldActivity.setDnrCntPhoneNumber(activity.getDnrCntPhoneNumber());
        oldActivity.setDnrCntFaxNumber(activity.getDnrCntFaxNumber());

        oldActivity.setMfdCntTitle(activity.getMfdCntTitle());
        oldActivity.setMfdCntOrganization(activity.getMfdCntOrganization());
        oldActivity.setMfdCntPhoneNumber(activity.getMfdCntPhoneNumber());
        oldActivity.setMfdCntFaxNumber(activity.getMfdCntFaxNumber());
        
        oldActivity.setPrjCoFirstName(activity.getPrjCoFirstName());
        oldActivity.setPrjCoLastName(activity.getPrjCoLastName());
        oldActivity.setPrjCoTitle(activity.getPrjCoTitle());
        oldActivity.setPrjCoOrganization(activity.getPrjCoOrganization());
        oldActivity.setPrjCoPhoneNumber(activity.getPrjCoPhoneNumber());
        oldActivity.setPrjCoEmail(activity.getPrjCoEmail());
        oldActivity.setPrjCoFaxNumber(activity.getPrjCoFaxNumber());
        
        oldActivity.setSecMiCntFirstName(activity.getSecMiCntFirstName());
        oldActivity.setSecMiCntLastName(activity.getSecMiCntLastName());
        oldActivity.setSecMiCntEmail(activity.getSecMiCntEmail());
        oldActivity.setSecMiCntOrganization(activity.getSecMiCntOrganization());
        oldActivity.setSecMiCntTitle(activity.getSecMiCntTitle());
        oldActivity.setSecMiCntPhoneNumber(activity.getSecMiCntPhoneNumber());
        oldActivity.setSecMiCntFaxNumber(activity.getSecMiCntFaxNumber());

//				oldActivity.setLevel(activity.getLevel()); //TO BE DELETED
        oldActivity.setModality(activity.getModality());
        oldActivity.setMofedCntEmail(activity.getMofedCntEmail());
        oldActivity.setMofedCntFirstName(activity.getMofedCntFirstName());
        oldActivity.setMofedCntLastName(activity.getMofedCntLastName());
        oldActivity.setName(activity.getName());
        //oldActivity.setBudget(activity.getBudget());
        oldActivity.setBudgetsector(activity.getBudgetsector());
        oldActivity.setBudgetorganization(activity.getBudgetorganization());
        oldActivity.setBudgetdepartment(activity.getBudgetdepartment());
        oldActivity.setBudgetprogram(activity.getBudgetprogram());
        oldActivity.setChapter(activity.getChapter());
        oldActivity.setProjectComments(activity.getProjectComments());
        oldActivity.setObjective(activity.getObjective());
        oldActivity.setResults(activity.getResults());
        oldActivity.setPurpose(activity.getPurpose());
        oldActivity.setProgramDescription(activity.getProgramDescription());
        oldActivity.setProposedApprovalDate(activity.getProposedApprovalDate());
        oldActivity.setProposedStartDate(activity.getProposedStartDate());
        oldActivity.setProposedCompletionDate(activity.
                                              getProposedCompletionDate());
        oldActivity.setContractingDate(activity.getContractingDate());
        oldActivity.setDisbursmentsDate(activity.getDisbursmentsDate());
        
        /*
        if (activity.getActivityContacts() != null) {
	        for (AmpActivityContact contact : activity.getActivityContacts()) {
	        	contact.setActivity(oldActivity);
	        }
        }
        */
        
        oldActivity.setActivityContacts(activity.getActivityContacts());

        oldActivity.setStatusReason(activity.getStatusReason());
        oldActivity.setThemeId(activity.getThemeId());
        oldActivity.setUpdatedDate(activity.getUpdatedDate());
        if (activity.getClosingDates() != null)
        	oldActivity.getClosingDates().addAll(activity.getClosingDates());
        
        
        if (activity.getComponents() != null){
        	oldActivity.getComponents().addAll(activity.getComponents());
        }
        
        if(activity.getComponentProgress()!=null){
        	oldActivity.getComponentProgress().addAll(activity.getComponentProgress());
        }
        
        
        if(activity.getComponentFundings()!=null){
        	oldActivity.getComponentFundings().addAll(activity.getComponentFundings());
        }
        
      
        
        //oldActivity.setDocuments(activity.getDocuments());
        if (activity.getFunding() != null)
        	oldActivity.getFunding().addAll(activity.getFunding());
        if (activity.getRegionalFundings() != null)
        	oldActivity.getRegionalFundings().addAll(activity.getRegionalFundings());
        if (activity.getInternalIds() != null)
        	oldActivity.getInternalIds().addAll(activity.getInternalIds());
        if (activity.getLocations() != null)
        	oldActivity.getLocations().addAll(activity.getLocations());
       
        if (activity.getOrgrole() != null)
        	oldActivity.getOrgrole().addAll(activity.getOrgrole());
        if (activity.getReferenceDocs() != null)
        	oldActivity.getReferenceDocs().addAll(activity.getReferenceDocs());
        if (activity.getSectors() != null)
        	oldActivity.getSectors().addAll(activity.getSectors()); 
        if (activity.getComponentes() != null)
        	oldActivity.getComponentes().addAll(activity.getComponentes());
        
       
        
        if (activity.getIssues() != null)
        	oldActivity.getIssues().addAll(activity.getIssues());
        
        if (activity.getRegionalObservations() != null) {
        	oldActivity.setRegionalObservations(new HashSet());
        	oldActivity.getRegionalObservations().addAll(activity.getRegionalObservations());
        }
        
        if (activity.getCosts() != null)
        	oldActivity.getCosts().addAll(activity.getCosts());
        if (activity.getActivityPrograms() != null)
        	oldActivity.getActivityPrograms().addAll(activity.getActivityPrograms());

        if (activity.getCategories() != null)
        	oldActivity.getCategories().addAll( activity.getCategories() );

        if(activity.getActivityDocuments() !=null)
        	oldActivity.getActivityDocuments().addAll( activity.getActivityDocuments() );

        oldActivity.setFunAmount(activity.getFunAmount());
        oldActivity.setCurrencyCode(activity.getCurrencyCode());
        oldActivity.setFunDate(activity.getFunDate());

        oldActivity.setApprovalStatus(activity.getApprovalStatus());

        if(activity.getApprovalDate()!=null){
        	oldActivity.setApprovalDate(activity.getApprovalDate());
        	oldActivity.setApprovedBy(activity.getApprovedBy());
        }
        
        Set programs = activity.getActPrograms();
        Set oldPrograms = oldActivity.getActPrograms();
        Set deletedPrograms = new HashSet();
        Set newPrograms = new HashSet();
        if (oldPrograms != null && oldPrograms.size() > 0) {
                Iterator iterOldProgram = oldPrograms.iterator();
                while (iterOldProgram.hasNext()) {
                        AmpActivityProgram oldProgram = (AmpActivityProgram)
                            iterOldProgram.next();
                        boolean delete = true;
                        if (programs != null) {
                                Iterator iterProgram = programs.iterator();
                                while (iterProgram.hasNext()) {
                                        AmpActivityProgram program = (
                                            AmpActivityProgram)
                                            iterProgram.next();
                                        if (program.getAmpActivityProgramId() == null) {
                                                program.setActivity(oldActivity);
                                                newPrograms.add(program);
                                        }
                                        else {
                                                if (oldProgram.
                                                    getAmpActivityProgramId().
                                                    equals(
                                                    program.
                                                    getAmpActivityProgramId())) {
                                                        oldProgram.
                                                            setProgramPercentage(
                                                            program.
                                                            getProgramPercentage());
                                                        delete = false;
                                                        break;

                                                }

                                        }

                                }
                                if (delete) {
                                        deletedPrograms.add(oldProgram);
                                }

                        }
                }
                oldActivity.getActPrograms().removeAll(deletedPrograms);
                oldActivity.getActPrograms().addAll(newPrograms);
                if (deletedPrograms.size() > 0) {
                        Iterator delProgramIter = deletedPrograms.
                            iterator();
                        while (delProgramIter.hasNext()) {
                                AmpActivityProgram delProgram = (
                                    AmpActivityProgram) delProgramIter.
                                    next();
                                //  delProgram.setActivity(null);
                                session.delete(delProgram);
                        }
                }
                oldActivity.setActPrograms(oldPrograms);

        }
        else {
                if (programs != null) {
                        Iterator iterProgram = programs.iterator();
                        while (iterProgram.hasNext()) {
                                AmpActivityProgram program = (
                                    AmpActivityProgram) iterProgram.next();
                                program.setActivity(oldActivity);
                        }

                }
                oldActivity.setActPrograms(programs);
        }


        List<CustomField<?>> customFields = CustomFieldsUtil.getCustomFields();
		if(customFields!=null){
			Iterator<CustomField<?>> cfi = customFields.iterator();
			while(cfi.hasNext()){
				CustomField customField = cfi.next();
				String propertyName = customField.getAmpActivityPropertyName();
				if(propertyName == null){
					logger.warn("Please set AmpActivityPropertyName for all custom fields.");
					continue;
				}
				try{
					//String value = BeanUtils.getProperty(activity, customField.getAmpActivityPropertyName());
					Object value = PropertyUtils.getSimpleProperty(activity, customField.getAmpActivityPropertyName());
					if(value!=null)
					BeanUtils.setProperty(oldActivity, customField.getAmpActivityPropertyName(), value);
				}catch(Exception e){
					logger.error("Custom Field [" + customField.getAmpActivityPropertyName() + "] exception", e);
				}
			}
		}        
        

        /*
         * tanzania ADDS
         */
        oldActivity.setFY(activity.getFY());
        oldActivity.setGovernmentApprovalProcedures(activity.
            isGovernmentApprovalProcedures());
        oldActivity.setJointCriteria(activity.isJointCriteria());
        oldActivity.setHumanitarianAid(activity.isHumanitarianAid());
        oldActivity.setGbsSbs(activity.getGbsSbs());
        oldActivity.setProjectCode(activity.getProjectCode());
        oldActivity.setSubProgram(activity.getSubProgram());
        oldActivity.setSubVote(activity.getSubVote());
        oldActivity.setVote(activity.getVote());
        oldActivity.setActivityCreator(activity.getActivityCreator());
        oldActivity.setDraft(activity.getDraft());
        oldActivity.setGovAgreementNumber(activity.getGovAgreementNumber());
        oldActivity.setBudgetCodeProjectID(activity.getBudgetCodeProjectID());

        oldActivity.setCrisNumber(activity.getCrisNumber());
      }

      Iterator itr = null;
      if (relatedLinks != null && relatedLinks.size() > 0) {
        itr = relatedLinks.iterator();
        member = (AmpTeamMember) session.load(AmpTeamMember.class, memberId);
        while (itr.hasNext()) {
          RelatedLinks rl = (RelatedLinks) itr.next();
          CMSContentItem temp = (CMSContentItem) session.get(CMSContentItem.class,
              new Long(rl.getRelLink().getId()));
          if (temp == null) {
            logger.debug("Item doesn't exist. Creating the CMS item");
            temp = rl.getRelLink();
            session.save(temp);
          }
          logger.debug("CMS item = " + temp.getId());
          if (rl.isShowInHomePage()) {
            if (member.getLinks() == null)
              member.setLinks(new HashSet());
            member.getLinks().add(temp);
          }

          if (edit) {
            if (oldActivity.getDocuments() == null) {
              oldActivity.setDocuments(new HashSet());
            }
            oldActivity.getDocuments().add(temp);
          }
          else {
            if (activity.getDocuments() == null) {
              activity.setDocuments(new HashSet());
            }
            activity.getDocuments().add(temp);
          }
        }
        session.saveOrUpdate(member);
      }


      /* Persists the activity */
      if (edit) {
        // update the activity
        logger.debug("updating ....");
        oldActivity.setUpdatedDate(new Date(System.currentTimeMillis()));
        oldActivity.setUpdatedBy(member);
        session.saveOrUpdate(oldActivity);
        activityId = oldActivity.getAmpActivityId();
        String ampId=generateAmpId(member.getUser(),activityId,session );
        if (oldActivity.getAmpId()==null){
            oldActivity.setAmpId(ampId);
        }
        session.update(oldActivity);
        activity = oldActivity;
        /*
                // added by Akash
                // desc: Saving team members in amp_member_activity table in case activity is Approved
                // start
             if ("approved".equals(oldActivity.getApprovalStatus())) {
              Long teamId = oldActivity.getTeam().getAmpTeamId();
              Query qry = null;
         String queryString = "select tm from " + AmpTeamMember.class.getName()
             + " tm where (tm.ampTeam=:teamId)";
              qry = session.createQuery(queryString);
              qry.setParameter("teamId", teamId, Hibernate.LONG);
              Iterator tmItr = qry.list().iterator();
              member = new AmpTeamMember();
              while (tmItr.hasNext()) {
               member = (AmpTeamMember) tmItr.next();
               if (!member.getAmpMemberRole().getTeamHead().booleanValue()) {
                if (member.getActivities() == null)
         member.setActivities(new HashSet());
               }
               member.getActivities().add(oldActivity);
               session.saveOrUpdate(member);
              }
             }
             // end	*/

      }
      else {
        // create the activity
        logger.debug("creating ....");
        if (activity.getMember() == null) {
                activity.setMember(new HashSet());
                Set programs = activity.getActPrograms();
                if (programs != null) {
                        Iterator iterProgram = programs.iterator();
                        while (iterProgram.hasNext()) {
                                AmpActivityProgram program = (
                                    AmpActivityProgram) iterProgram.next();
                                program.setActivity(activity);
                        }

                }

        }

        activity.getMember().add(activity.getActivityCreator());
        /*
             member = (AmpTeamMember) session.load(AmpTeamMember.class,
                activity.getActivityCreator().getAmpTeamMemId());
             if (member.getActivities() == null) {
            member.setActivities(new HashSet());
             }
             member.getActivities().add(activity);
         */
        session.save(activity);
        activityId = activity.getAmpActivityId();
        String ampId=generateAmpId(member.getUser(),activityId , session);
        activity.setAmpId(ampId);
        session.update(activity);
        //session.saveOrUpdate(member);
     
      if(activity.getComponentProgress()!=null){
	        Collection<AmpPhysicalPerformance> newProgress= activity.getComponentProgress();
	        for (AmpPhysicalPerformance ampPhysicalPerformance : newProgress) {
	      	  ampPhysicalPerformance.setAmpActivityId(activity);
	      	  session.save(ampPhysicalPerformance);
	  		}
	      }
      }
      
      //Session session = PersistenceManager.getRequestDBSession();
      Iterator<Long> iActors = rsp.getEaForm().getObservations().getDeletedActors().iterator();
      while(iActors.hasNext()){
    	  Long id = iActors.next();
    	  AmpRegionalObservationActor auxActor = (AmpRegionalObservationActor) session.load(AmpRegionalObservationActor.class, id);
    	  if(auxActor != null) {
    		  session.delete(auxActor);
    	  }
    	  
      }
      Iterator<Long> iMeasures = rsp.getEaForm().getObservations().getDeletedMeasures().iterator();
      while(iMeasures.hasNext()){
    	  Long id = iMeasures.next();
    	  AmpRegionalObservationMeasure auxMeasure = (AmpRegionalObservationMeasure) session.load(AmpRegionalObservationMeasure.class, id);
    	  /*if(auxMeasure != null) {
    		  Iterator<AmpRegionalObservationActor> iDBActors = auxMeasure.getActors().iterator();
    		  while(iDBActors.hasNext()) {
    			  AmpRegionalObservationActor auxActor = iDBActors.next();
    			  session.delete(auxActor);
    		  }*/
    		  //auxMeasure.getActors().clear();
    		  session.delete(auxMeasure);
    	  //}
      }
      Iterator<Long> iObservations = rsp.getEaForm().getObservations().getDeletedObservations().iterator();
      while(iObservations.hasNext()){
    	  Long id = iObservations.next();
    	  AmpRegionalObservation auxObs = (AmpRegionalObservation) session.load(AmpRegionalObservation.class, id);
    	  if(auxObs != null) {
    		  /*Iterator<AmpRegionalObservationMeasure> iDBMeasures = auxObs.getRegionalObservationMeasures().iterator();
    		  while(iDBMeasures.hasNext()) {
    			  AmpRegionalObservationMeasure auxMeasure = iDBMeasures.next();
    			  auxMeasure.getActors().clear();
    			  session.delete(auxMeasure);
    		  }
    		  auxObs.getRegionalObservationMeasures().clear();*/
    		  session.delete(auxObs);
    	  }
      }

      /*
      Collection<AmpComponentFunding>  componentFundingCol = getFundingComponentActivity(activityId, session);
      Iterator<Components<AmpComponentFunding>> componentsFundingIt = componentsFunding.iterator();
      while(componentsFundingIt.hasNext()){
    	  Components<AmpComponentFunding> ampTempComp = componentsFundingIt.next();

	      //to save the Component fundings
	      if (ampTempComp.getCommitments() != null) {
	        Iterator compItr = ampTempComp.getCommitments().iterator();
	        while (compItr.hasNext()) {
	          AmpComponentFunding ampComp = (AmpComponentFunding) compItr.next();
	          session.saveOrUpdate(ampComp);
	          if (componentFundingCol != null) {
	        	  componentFundingCol.remove(ampComp);
	          }
	        }
	      }

	      if (ampTempComp.getDisbursements() != null) {
	        Iterator compItr = ampTempComp.getDisbursements().iterator();
	        while (compItr.hasNext()) {
	          AmpComponentFunding ampComp = (AmpComponentFunding) compItr.next();
	          session.saveOrUpdate(ampComp);
	          if (componentFundingCol != null) {
	        	  componentFundingCol.remove(ampComp);
	          }
	        }
	      }

	      if (ampTempComp.getExpenditures() != null) {
	        Iterator compItr = ampTempComp.getExpenditures().iterator();
	        while (compItr.hasNext()) {
	          AmpComponentFunding ampComp = (AmpComponentFunding) compItr.next();
	          session.saveOrUpdate(ampComp);
	          if (componentFundingCol != null) {
	        	  componentFundingCol.remove(ampComp);
			  }
	        }
	      }


	      Collection<AmpPhysicalPerformance> phyProgress = DbUtil.getAmpPhysicalProgress(activityId,ampTempComp.getComponentId(), session);

	      if (ampTempComp.getPhyProgress() != null) {
				Iterator compItr = ampTempComp.getPhyProgress().iterator();
				while (compItr.hasNext()) {
					AmpPhysicalPerformance ampPhyPerf = (AmpPhysicalPerformance) compItr.next();
					session.saveOrUpdate(ampPhyPerf);
					phyProgress.remove(ampPhyPerf);
				}
	      }

	      if (phyProgress != null&&ampTempComp.getComponentId()!=null) {
				Iterator<AmpPhysicalPerformance> phyProgressColIt = phyProgress.iterator();
				while (phyProgressColIt.hasNext()) {
					session.delete(phyProgressColIt.next());
				}
		  }

      }
      if (componentFundingCol != null) {
			Iterator<AmpComponentFunding> componentFundingColIt = componentFundingCol.iterator();
			while (componentFundingColIt.hasNext()) {
				session.delete(componentFundingColIt.next());
			}
	  }
      */
      

      /**
       * Contact Information
       */ 
      List<AmpActivityContact> activityContacts=rsp.getEaForm().getContactInformation().getActivityContacts();
      // if activity contains contact,which is not in contact list, we should remove it
      List<AmpActivityContact> activityDbContacts=ContactInfoUtil.getActivityContacts(oldActivityId);
      if(activityDbContacts!=null && activityDbContacts.size()>0){
    	  Iterator<AmpActivityContact> iter=activityDbContacts.iterator();
    	  while(iter.hasNext()){
    		  AmpActivityContact actContact=iter.next();
    		  int count=0;
    		  if(activityContacts!=null){
    			  for (AmpActivityContact activityContact : activityContacts) {
					if(activityContact.getId()!=null && activityContact.getId().equals(actContact.getId())){
						count++;
						break;
					}
				}
    		  }
    		  if(count==0){ //if activity contains contact,which is not in contact list, we should remove it
    			  AmpActivityContact activityCont=(AmpActivityContact)session.get(AmpActivityContact.class, actContact.getId());
    			  AmpContact cont=activityCont.getContact();
    			  session.delete(activityCont);
    			  cont.getActivityContacts().remove(activityCont);
    			  session.update(cont);    			  		  
    		  }
    	  }
      }
      //add or edit activity contact and amp contact
      if(activityContacts!=null && activityContacts.size()>0){
    	  for (AmpActivityContact activityContact : activityContacts) {
    	   	//save or update contact
    		AmpContact contact=activityContact.getContact();
    		AmpContact ampContact=null;
    		if(contact.getId()!=null){ //contact already exists.
    			ampContact=(AmpContact)session.get(AmpContact.class, contact.getId());
    			ampContact.setName(contact.getName());
    			ampContact.setLastname(contact.getLastname());
    			ampContact.setTitle(contact.getTitle());
    			ampContact.setOrganisationName(contact.getOrganisationName());
    			ampContact.setCreator(contact.getCreator());
    			ampContact.setShared(true);
    			ampContact.setOfficeaddress(contact.getOfficeaddress());
    			ampContact.setFunction(contact.getFunction());


    			
    			//remove old properties
    			List<AmpContactProperty> dbProperties=ContactInfoUtil.getContactProperties(ampContact);
    			if(dbProperties!=null){
    				for (AmpContactProperty dbProperty : dbProperties) {
    					session.delete(dbProperty);
    				}
    			}
    			ampContact.setProperties(null);    			
    			//remove old organization contacts
    			List<AmpOrganisationContact> dbOrgConts=ContactInfoUtil.getContactOrganizations(ampContact.getId());
    			if(dbOrgConts!=null){
    				for (AmpOrganisationContact orgCont :dbOrgConts) {
						session.delete(orgCont);
					}
    			}

    			ampContact.setOrganizationContacts(null);
    			
    			    			
    			session.update(ampContact);    			    			
    		}else{
    			session.save(contact);
    		}
    		
    		
    		//save properties
    		if(contact.getProperties()!=null){
				for (AmpContactProperty formProperty : contact.getProperties()) {
					if(ampContact!=null){
						formProperty.setContact(ampContact);
					}else{
						formProperty.setContact(contact);
					}
					session.save(formProperty);
				}
			}
    		//save cont. organizations
    		if(contact.getOrganizationContacts()!=null){
    			for (AmpOrganisationContact orgCont : contact.getOrganizationContacts()) {
					if(ampContact!=null){
						orgCont.setContact(ampContact);
					}else{
						orgCont.setContact(contact);
					}
					session.save(orgCont);
				}
    		}

    		//link activity to activityContact
    		/*
    		if(activityContact.getId()!=null){
    			AmpActivityContact ampActContact= new AmpActivityContact();
    				//(AmpActivityContact)session.get(AmpActivityContact.class, activityContact.getId());
    			ampActContact.setContact(activityContact.getContact());
    			ampActContact.setContactType(activityContact.getContactType());
    			ampActContact.setPrimaryContact(activityContact.getPrimaryContact());
    			ampActContact.setActivity(activity);
    			ampActContact.setId(null);
    			session.save(ampActContact);
    		}else{
    			activityContact.setActivity(activity);
        		session.save(activityContact);
    		}*/
    		
    		if(activityContact.getId()!=null){

    			AmpActivityContact ampActContact = (AmpActivityContact)session.get(AmpActivityContact.class, activityContact.getId());

                /*
    			ampActContact.setContact(activityContact.getContact());
    			ampActContact.setContactType(activityContact.getContactType());
    			ampActContact.setPrimaryContact(activityContact.getPrimaryContact());
    			ampActContact.setActivity(activity);
    			ampActContact.setId(activityContact.getId());
    			*/
                ampActContact.setPrimaryContact(activityContact.getPrimaryContact());
    			session.update(ampActContact);
    		}else{
    			activityContact.setActivity(activity);
        		session.save(activityContact);
    		}
    		
//    		session.save(activityContact);
    		
    	  }
      }
      
      
      
      
      
      
      
      
      
      /* Persists comments, of type AmpComments, related to the activity */
      if (commentsCol != null && !commentsCol.isEmpty()) {
        logger.debug("commentsCol.size() [Inside Persisting]: " +
                     commentsCol.size());
        boolean flag = true;
        /*
             if (edit && serializeFlag)
         flag = false; */
        logger.debug("flag [Inside Persisting comments]: " + flag);
        itr = commentsCol.iterator();
        while (itr.hasNext()) {
          AmpComments comObj = (AmpComments) itr.next();
          comObj.setAmpActivityId(activity);
          session.save(comObj);
          logger.debug("Comment Saved [AmpCommentId] : " +
                       comObj.getAmpCommentId());
        }
      }
      else
        logger.debug("commentsCol is empty");
      
      
      if(activity.getIndicators()!=null && activity.getIndicators().size()>0){
    	  itr = activity.getIndicators().iterator();
          while (itr.hasNext()) {
            IndicatorActivity actInd = (IndicatorActivity) itr.next();
            int count=0;
            if(indicators!=null){
            for (Object inds : indicators) {
            	ActivityIndicator actIndicator=(ActivityIndicator)inds;
            	if(actIndicator.getIndicatorId().equals(actInd.getIndicator().getIndicatorId())){
            		count++; //if indicators contain actInd then increment count; 
            		break;
            	}
			} 
            }
            if (count==0){//if activity has indicator,which is not in indicators list,we should remove it from db            	
            	 AmpIndicator ind=(AmpIndicator)session.get(AmpIndicator.class,actInd.getIndicator().getIndicatorId());
            	 IndicatorActivity indConn=IndicatorUtil.findActivityIndicatorConnection(activity, ind, session);
            	 IndicatorUtil.removeConnection(indConn);
            }
          }
      }

      if (indicators != null && indicators.size() > 0) {
        itr = indicators.iterator();
        while (itr.hasNext()) {
          ActivityIndicator actInd = (ActivityIndicator) itr.next();
          
          AmpIndicatorRiskRatings risk=null;
                    
          AmpIndicator ind=(AmpIndicator)session.get(AmpIndicator.class,actInd.getIndicatorId());
          //if actInd.getRisk()==0 , than no Risk is selected
          if(actInd.getRisk()!=null && actInd.getRisk().longValue()>0){
        	  risk=(AmpIndicatorRiskRatings)session.load(AmpIndicatorRiskRatings.class, actInd.getRisk());  
          }          

          AmpCategoryValue categoryValue = null;
          if(actInd.getIndicatorsCategory() != null && actInd.getIndicatorsCategory().getId() != null)
        	  categoryValue = (AmpCategoryValue) session.get(AmpCategoryValue.class, actInd.getIndicatorsCategory().getId());

          
          //try to find connection of current activity with current indicator
          IndicatorActivity indConn=IndicatorUtil.findActivityIndicatorConnection(activity, ind, session);
          //if no connection found then create new one. Else clear old values for the connection.
          boolean newIndicator = false;
          if (indConn == null){
        	  indConn=new IndicatorActivity();
              indConn.setActivity(activity);
              indConn.setIndicator(ind);
              indConn.setValues(new HashSet<AmpIndicatorValue>());
              newIndicator = true;
          } else {
        	  if ((indConn.getValues() != null) && (indConn.getValues().size() > 0)) {
        		  for (AmpIndicatorValue value : indConn.getValues()) {
        			  session.delete(value);
        		  }
        		  indConn.getValues().clear();
        	  }
          }

          //create each type of value and assign to connection
          AmpIndicatorValue indValActual = null;
          if (actInd.getCurrentVal()!=null){
        	  indValActual = new AmpIndicatorValue();
        	  indValActual.setValueType(AmpIndicatorValue.ACTUAL);
        	  indValActual.setValue(new Double(actInd.getCurrentVal()));
        	  indValActual.setComment(actInd.getCurrentValComments());
        	  indValActual.setValueDate(DateConversion.getDate(actInd.getCurrentValDate()));
        	  indValActual.setRisk(risk);
        	  indValActual.setLogFrame(categoryValue);
        	  indValActual.setIndicatorConnection(indConn);
        	  indConn.getValues().add(indValActual);
          }
          AmpIndicatorValue indValTarget = null;
          if (actInd.getTargetVal()!=null){
        	  indValTarget = new AmpIndicatorValue();
        	  indValTarget.setValueType(AmpIndicatorValue.TARGET);
        	  indValTarget.setValue(new Double(actInd.getTargetVal()));
        	  indValTarget.setComment(actInd.getTargetValComments());
        	  indValTarget.setValueDate(DateConversion.getDate(actInd.getTargetValDate()));
        	  indValTarget.setRisk(risk);
        	  indValTarget.setLogFrame(categoryValue);
        	  indValTarget.setIndicatorConnection(indConn);
        	  indConn.getValues().add(indValTarget);
          }
          AmpIndicatorValue indValBase = null;
          if (actInd.getBaseVal()!=null){
        	  indValBase = new AmpIndicatorValue();
        	  indValBase.setValueType(AmpIndicatorValue.BASE);
        	  indValBase.setValue(new Double(actInd.getBaseVal()));
        	  indValBase.setComment(actInd.getBaseValComments());
        	  indValBase.setValueDate(DateConversion.getDate(actInd.getBaseValDate()));
        	  indValBase.setRisk(risk);
        	  indValBase.setLogFrame(categoryValue);
        	  indValBase.setIndicatorConnection(indConn);
        	  indConn.getValues().add(indValBase);
          }
          AmpIndicatorValue indValRevised = null;
          if (actInd.getRevisedTargetVal()!=null){
        	  indValRevised = new AmpIndicatorValue();
        	  indValRevised.setValueType(AmpIndicatorValue.REVISED);
        	  indValRevised.setValue(new Double(actInd.getRevisedTargetVal()));
        	  indValRevised.setComment(actInd.getRevisedTargetValComments());
        	  indValRevised.setValueDate(DateConversion.getDate(actInd.getRevisedTargetValDate()));
        	  indValRevised.setRisk(risk);
        	  indValRevised.setLogFrame(categoryValue);
        	  indValRevised.setIndicatorConnection(indConn);
        	  indConn.getValues().add(indValRevised);
          }
          // save connection with its new values.
          if(newIndicator){
        	  // Save the new indicator that is NOT present in the indicators collection from the Activity.
        	  //IndicatorUtil.saveConnectionToActivity(indConn, session);
        	  session.saveOrUpdate(indConn);
          } else {
        	  //They are loaded by different sessions!
        	  for (AmpIndicatorValue value : indConn.getValues()) {
				session.save(value);
        	  }
        	 
        	  // Save the activity in order to save the indicators collection and its changes (values).
        	  // This is for AMP-4317, you can't save the collection because is in the Activity.        	  
        	  session.saveOrUpdate(activity);
          }
        }
      }
      
        String queryString = "select con from " + IPAContract.class.getName() + " con where con.activity.ampActivityId=" + activityId;
      	IPAContract ipaAux = (IPAContract) session.get(IPAContract.class, activityId);
        String ids = "";
        if (contracts != null) {

            Iterator<IPAContract> ipaConIter = contracts.iterator();
            while (ipaConIter.hasNext()) {
                IPAContract contract = ipaConIter.next();
                contract.setActivity(activity);
                if (contract.getId() != null) {
                    IPAContract oldContract = (IPAContract) session.get(IPAContract.class, contract.getId());
                    oldContract.setContractName(contract.getContractName());
                    oldContract.setDescription(contract.getDescription());
                    oldContract.setContractingOrganizationText(contract.getContractingOrganizationText());
                    oldContract.setActivityCategory(contract.getActivityCategory());
                    oldContract.setStartOfTendering(contract.getStartOfTendering());
                    oldContract.setSignatureOfContract(contract.getSignatureOfContract());
                    oldContract.setContractValidity(contract.getContractValidity());
                    oldContract.setContractCompletion(contract.getContractCompletion());

                    oldContract.setTotalPrivateContribAmountDate(contract.getTotalPrivateContribAmountDate());
                    oldContract.setTotalNationalContribIFIAmountDate(contract.getTotalNationalContribIFIAmountDate());
                    oldContract.setTotalNationalContribRegionalAmountDate(contract.getTotalNationalContribRegionalAmountDate());
                    oldContract.setTotalNationalContribCentralAmountDate(contract.getTotalNationalContribCentralAmountDate());
                    oldContract.setTotalECContribINVAmountDate(contract.getTotalECContribINVAmountDate());
                    oldContract.setTotalECContribIBAmountDate(contract.getTotalECContribIBAmountDate());
                    
                    oldContract.setTotalECContribIBAmount(contract.getTotalECContribIBAmount());
                    oldContract.setTotalAmount(contract.getTotalAmount());
                    oldContract.setContractTotalValue(contract.getContractTotalValue());
                    oldContract.setTotalAmountCurrency(contract.getTotalAmountCurrency());
                    oldContract.setDibusrsementsGlobalCurrency(contract.getDibusrsementsGlobalCurrency());
                    oldContract.setExecutionRate(contract.getExecutionRate());
                    oldContract.setTotalECContribINVAmount(contract.getTotalECContribINVAmount());
                    oldContract.setTotalNationalContribCentralAmount(contract.getTotalNationalContribCentralAmount());
                    oldContract.setTotalNationalContribRegionalAmount(contract.getTotalNationalContribRegionalAmount());
                    oldContract.setTotalNationalContribIFIAmount(contract.getTotalNationalContribIFIAmount());
                    oldContract.setTotalPrivateContribAmount(contract.getTotalPrivateContribAmount());
                    oldContract.setOrganization(contract.getOrganization());
                    oldContract.setStatus(contract.getStatus());
                    oldContract.setType(contract.getType());
                    oldContract.setContractType(contract.getContractType());
                    //oldContract.getDisbursements().clear();
                    Set toRetain=new HashSet();

                    Set newOrgs = contract.getOrganizations();
                    if (newOrgs != null && newOrgs.size() > 0) {
                        Iterator<AmpOrganisation> iter = newOrgs.iterator();
                        while (iter.hasNext()) {
                            AmpOrganisation newOrg = iter.next();
                            if (newOrg.getAmpOrgId() != null) {
                                AmpOrganisation oldDisb = (AmpOrganisation) session.load(AmpOrganisation.class,
                                        newOrg.getAmpOrgId());
                                toRetain.add(oldDisb);
                            } else {
                                if (oldContract.getOrganization() == null) {
                                    oldContract.setOrganizations(new HashSet());
                                }
                                oldContract.getOrganizations().add(newOrg);
                                toRetain.add(newOrg);
                                
                            }
                        }
                        oldContract.getOrganizations().addAll(toRetain);
                    }
                    else{
                        if(oldContract.getOrganizations()!=null){
                        oldContract.getOrganizations().clear();
                        }
                    }
                    
                    
                    Set newDisbs = contract.getDisbursements();
                    if (newDisbs != null && newDisbs.size() > 0) {
                        Iterator<IPAContractDisbursement> iterNewDisb = newDisbs.iterator();
                        while (iterNewDisb.hasNext()) {
                            IPAContractDisbursement newDisb = iterNewDisb.next();
                            if (newDisb.getId() != null) {
                                IPAContractDisbursement oldDisb = (IPAContractDisbursement) session.load(IPAContractDisbursement.class,
                                        newDisb.getId());
                                oldDisb.setAdjustmentType(newDisb.getAdjustmentType());
                                oldDisb.setAmount(newDisb.getAmount());
                                oldDisb.setCurrency(newDisb.getCurrency());
                                oldDisb.setDate(newDisb.getDate());
                                toRetain.add(oldDisb);
                            } else {
                                if (oldContract.getDisbursements() == null) {
                                    oldContract.setDisbursements(new HashSet());
                                }
                                newDisb.setContract(oldContract);
                                oldContract.getDisbursements().add(newDisb);
                                toRetain.add(newDisb);
                                
                            }
                        }
                        oldContract.getDisbursements().retainAll(toRetain);
                    }
                    else{
                        if(oldContract.getDisbursements()!=null){
                        oldContract.getDisbursements().clear();
                        }
                    }

                    contract=oldContract;

                }
                session.saveOrUpdate(contract);
                ids += contract.getId() + ", ";
            }
            if(ids.length()>2)
            ids = ids.substring(0, ids.length() - 2);



        }
        if (ids.length() != 0) {
            queryString += " and con.id not in (" + ids + ")";
        }
       if(ipaAux != null){//if no row is returned there is an Hibernate exception.
    	   //session.delete(queryString); This method has been moved to hibernate.classic.Session.delete() and it's Deprecated
       }
        
       session.flush();
       if (alwaysRollback == false)
    	  tx.commit(); // commit the transcation

       logger.debug("Activity saved");    
    }
    catch (Exception ex) {
      logger.error("Exception from saveActivity().", ex);
      //we can't throw here the exception because we need to rollback the transaction
      ex.printStackTrace();
      exceptionRaised = true;
      savedEx = ExceptionFactory.newAMPException(ex);
      if (tx != null) {
        try {
          alwaysRollback = false;
          tx.rollback();
          logger.debug("Transaction Rollbacked");
        }
        catch (HibernateException e) {
          logger.error("Rollback failed", e);
        }
      }
    }
    finally {
		if (alwaysRollback){  //if 
			if (tx != null) {
				try {
					tx.rollback();
					logger.debug("Transaction Rollbacked");
				}
				catch (HibernateException e) {
					logger.error("Rollback failed", e);
					AMPException ae = ExceptionFactory.newAMPException(Constants.AMP_ERROR_LEVEL_WARNING, false, e);
					ErrorReportingPlugin.handle(ae, logger);
					exceptionRaised = true;
				}
			}
		}
		if (exceptionRaised){
			throw savedEx;
    }
    }

    return activityId;
  }

  /**
	 * Return all reference documents for Activity
	 * 
	 * @param activityId
	 * @return
	 */
  @SuppressWarnings("unchecked")
  public static Collection<AmpActivityReferenceDoc> getReferenceDocumentsFor(Long activityId) throws DgException{
	  String oql="select refdoc from "+AmpActivityReferenceDoc.class.getName()+" refdoc "+
	  " where refdoc.activity.ampActivityId=:actId";
	  try {
		Session session=PersistenceManager.getRequestDBSession();
		Query query=session.createQuery(oql);
		query.setLong("actId", activityId);
		return query.list();
	} catch (Exception e) {
		logger.error(e);
		throw new DgException("Cannot load reference documents for activity id="+activityId,e);
	}
  }

  public static void updateActivityCreator(AmpTeamMember creator,
                                           Long activityId) {
    Session session = null;
    Transaction tx = null;
    AmpActivity oldActivity = null;

    try {
      session = PersistenceManager.getRequestDBSession();
      tx = session.beginTransaction();

      oldActivity = (AmpActivity) session.load(AmpActivity.class, activityId);

      if (oldActivity == null) {
        logger.debug("Previous Activity is null");
        return;
      }

      oldActivity.setActivityCreator(creator);

      session.update(oldActivity);
      tx.commit();
      logger.debug("Activity saved");
    }
    catch (Exception ex) {
      logger.error("Exception from saveActivity()  " + ex.getMessage());
      ex.printStackTrace(System.out);
      if (tx != null) {
        try {
          tx.rollback();
          logger.debug("Transaction Rollbacked");
        }
        catch (HibernateException e) {
          logger.error("Rollback failed :" + e);
        }
      }
    }
  }

  public static void updateActivityDocuments(Long activityId, Set documents) {
		Session session = null;
		Transaction tx = null;
		AmpActivity oldActivity = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			tx = session.beginTransaction();

			oldActivity = (AmpActivity) session.load(AmpActivity.class,
					activityId);

			if (oldActivity == null) {
				logger.debug("Previous Activity is null");
				return;
			}

			oldActivity.setDocuments(documents);

			session.update(oldActivity);
			tx.commit();
			logger.debug("Activity saved");
		} catch (Exception ex) {
			logger.error("Exception from saveActivity()  " + ex.getMessage());
			ex.printStackTrace(System.out);
			if (tx != null) {
				try {
					tx.rollback();
					logger.debug("Transaction Rollbacked");
				} catch (HibernateException e) {
					logger.error("Rollback failed :" + e);
				}
			}
		}
	}


  public static Collection getAmpActivityComponente(Long actId) {
    Session session = null;
    Collection col = new ArrayList();
    logger.info(" this is the other components getting called....");
    try {
      session = PersistenceManager.getRequestDBSession();
      String queryString = "select aac.* from amp_activity_componente aac " +
    	  "where (aac.amp_activity_id=:actId)";
      Query qry = session.createSQLQuery(queryString).addEntity(AmpActivityComponente.class);
      qry.setParameter("actId", actId, Hibernate.LONG);
      col = qry.list();
    }
    catch (Exception e) {
      logger.error("Unable to get AmpActivityComponente");
      logger.error(e.getMessage());
    }
    return col;
  }

  
  public static Collection getComponents(Long actId) {
    Session session = null;
    Collection col = new ArrayList();
    logger.info(" this is the other components getting called....");
    try {
      session = PersistenceManager.getRequestDBSession();
      String queryString = "select ac.* from amp_components ac " +
      		"inner join amp_activity_components aac on (aac.amp_component_id = ac.amp_component_id) " +
      		"where (aac.amp_activity_id=:actId)";
      Query qry = session.createSQLQuery(queryString).addEntity(AmpComponent.class);
      qry.setParameter("actId", actId, Hibernate.LONG);
      col = qry.list();
    }
    catch (Exception e) {
      logger.error("Unable to get all components");
      logger.error(e.getMessage());
    }
//    finally {
//      try {
//        PersistenceManager.releaseSession(session);
//      }
//      catch (Exception ex) {
//        logger.error("Release Session failed :" + ex);
//      }
//    }
    return col;
  }

  /**
   * Searches activities.
   * Please note that this method is too slow if there are too many activities, because hibernate should load them all. Please use pagination.
   * @param ampThemeId filter by program
   * @param statusCode filter by status if not null
   * @param donorOrgId filter by donor org if not null
   * @param fromDate filter by date if not null
   * @param toDate filter by date if not null
   * @param locationId filter by location if not null
   * @param teamMember filter by team if not null
   * @param pageStart if null then 0 is assumed.
   * @param rowCount number of activities to return
   * @return list of activities.
   * @throws DgException
   */
  public static Collection<AmpActivity> searchActivities(Long ampThemeId,
      String statusCode,
      String donorOrgId,
      Date fromDate,
      Date toDate,
      Long locationId,
      TeamMember teamMember,Integer pageStart,Integer rowCount) throws DgException{
    Collection<AmpActivity> result = null;
    try {
      Session session = PersistenceManager.getRequestDBSession();

      String oql = "select distinct act from " + AmpActivityProgram.class.getName() + " prog ";
      oql+= getSearchActivitiesWhereClause(ampThemeId, statusCode, donorOrgId, fromDate, toDate, locationId, teamMember);
      oql += " order by act.name";

      Query query = session.createQuery(oql);
      
      setSearchActivitiesQueryParams(query, ampThemeId, statusCode, donorOrgId, fromDate, toDate, locationId, teamMember);
      
      if (pageStart!=null && rowCount!=null){
          query.setFirstResult(pageStart);
          query.setMaxResults(rowCount);
      }
      
      result = query.list();
    }
    catch (Exception ex) {
      throw new DgException("Cannot search activities for NPD",ex);
    }

    return result;
  }

  /**
   * <p>Searches activities which are assigened to specific program and returns collection of {@link ActivityItem} objects.
   * Each object is created using {@link ActivityItem#ActivityItem(AmpActivity,Long)} constructor.</p>
   * <p>Please note that this method is too slow if there are too many activities, because hibernate should load them all. Please use pagination.</p>
   * @param ampThemeId filter by program
   * @param statusCode filter by status if not null
   * @param donorOrgId filter by donor org if not null
   * @param fromDate filter by date if not null
   * @param toDate filter by date if not null
   * @param locationId filter by location if not null
   * @param teamMember filter by team if not null
   * @param pageStart if null then 0 is assumed.
   * @param rowCount number of activities to return
   * @return list of activities.
   * @see ActivityItem
   * @throws DgException
   */
  public static Collection<ActivityItem> searchActivitieProgPercents(Long ampThemeId,
      String statusCode,
      String donorOrgId,
      Date fromDate,
      Date toDate,
      Long locationId,
      TeamMember teamMember,Integer pageStart,Integer rowCount) throws DgException{
      List<ActivityItem> result = null;
    try {
      Session session = PersistenceManager.getRequestDBSession();

      String oql = "select distinct  new  org.digijava.module.aim.helper.ActivityItem(act,prog.programPercentage) from " + AmpActivityProgram.class.getName() + " prog ";
      oql+= getSearchActivitiesWhereClause(ampThemeId, statusCode, donorOrgId, fromDate, toDate, locationId, teamMember);
    
      
      Query query = session.createQuery(oql);

      setSearchActivitiesQueryParams(query, ampThemeId, statusCode, donorOrgId, fromDate, toDate, locationId, teamMember);

      if (pageStart!=null && rowCount!=null){
          query.setFirstResult(pageStart);
          query.setMaxResults(rowCount);
      }

      result = query.list();
    }
    catch (Exception ex) {
      throw new DgException("Cannot search activities for NPD",ex);
    }

    return result;
  }


  /**
   * Count how man activities will find search without pagination.
   * This method is used together with {@link #searchActivities(Long, String, Long, Date, Date, Long, TeamMember, Integer, Integer)}
   * @param ampThemeId
   * @param statusCode
   * @param donorOrgId
   * @param fromDate
   * @param toDate
   * @param locationId
   * @param teamMember
   * @return
   * @throws DgException
   */
  public static Integer searchActivitiesCount(Long ampThemeId,
	      String statusCode,
	      String donorOrgId,
	      Date fromDate,
	      Date toDate,
	      Long locationId,
	      TeamMember teamMember) throws DgException{
	    Integer result = null;
	    try {
	      Session session = PersistenceManager.getRequestDBSession();
	      String oql = "select count(distinct act) from " + AmpActivityProgram.class.getName() + " prog ";
	      oql += getSearchActivitiesWhereClause(ampThemeId, statusCode, donorOrgId, fromDate, toDate, locationId, teamMember);
	      oql += " order by act.name";

	      Query query = session.createQuery(oql);

	      setSearchActivitiesQueryParams(query, ampThemeId, statusCode, donorOrgId, fromDate, toDate, locationId, teamMember);
	      
	      result = (Integer)query.uniqueResult();
	    }
	    catch (Exception ex) {
	      throw new DgException("Cannot count activities for NPD",ex);
	    }

	    return result;
	  }

  /**
   * Setups query string where clause for search and count methods.
   * @param ampThemeId
   * @param statusCode
   * @param donorOrgId
   * @param fromDate
   * @param toDate
   * @param locationId
   * @param teamMember
   * @return
   * @see #searchActivities(Long, String, Long, Date, Date, Long, TeamMember, Integer, Integer)
   * @see #searchActivitiesCount(Long, String, Long, Date, Date, Long, TeamMember)
   */
  public static String getSearchActivitiesWhereClause(Long ampThemeId,
	      String statusCode,
	      String donorOrgId,
	      Date fromDate,
	      Date toDate,
	      Long locationId,
	      TeamMember teamMember) {
	  
	  String oql="";
	  
      if (ampThemeId!=null){
    	  oql += " inner join prog.program as theme ";
      }
      oql+=" inner join prog.activity as  act ";
      if (statusCode!=null && !"".equals(statusCode.trim())){
    	  oql+=" join  act.categories as categories ";
      }
      if(teamMember!=null&&teamMember.getComputation()!=null&&teamMember.getComputation()){
          oql+=" inner join act.orgrole role ";
      }
      oql+=" where 1=1 ";
      if (ampThemeId != null) {
          oql += " and ( theme.ampThemeId = :ampThemeId) ";
        }
      if (donorOrgId != null&&!donorOrgId.trim().equals("")) {
        String s = " and act in (select f.ampActivityId from " +
             AmpFunding.class.getName() + " f " +
            " where f.ampDonorOrgId.ampOrgId in ("+donorOrgId+")) ";
        oql += s;
      }
      if (statusCode != null&&!"".equals(statusCode.trim())) {
        oql += " and categories.id in ("+statusCode+") ";
      }
      if (fromDate != null) {
        oql += " and (act.actualStartDate >= :FromDate or (act.actualStartDate is null and act.proposedStartDate >= :FromDate) )";
      }
      if (toDate != null) {
        oql += " and (act.actualStartDate <= :ToDate or (act.actualStartDate is null and act.proposedStartDate <= :ToDate) ) ";
      }
      if (locationId != null) {
        oql += " and act.locations in (from " + AmpLocation.class.getName() +" loc where loc.id=:LocationID)";
      }
      if (teamMember != null) {
          //oql += " and " +getTeamMemberWhereClause(teamMember);
          if (teamMember.getComputation()!=null&&teamMember.getComputation()) {
              AmpTeam team = TeamUtil.getAmpTeam(teamMember.getTeamId());
              Set<AmpOrganisation> orgs = team.getOrganizations();
              Iterator<AmpOrganisation> iter = orgs.iterator();
              String ids = "";
              while (iter.hasNext()) {
                  AmpOrganisation org = iter.next();
                  ids += org.getAmpOrgId() + ",";
              }
              if(ids.length()>1){
              ids = ids.substring(0, ids.length() - 1);
              oql += "  and ( act.team.ampTeamId =:teamId or  role.organisation.ampOrgId in(" + ids+"))";
              }
          }
          else{
               oql += " and ( act.team.ampTeamId =:teamId ) ";
          }
        
      }
        oql+=" and act.team is not NULL ";
	  return oql;
  }

  public static void setSearchActivitiesQueryParams(Query query, Long ampThemeId,
	      String statusCode,
	      String donorOrgId,
	      Date fromDate,
	      Date toDate,
	      Long locationId,
	      TeamMember teamMember) {
	  
      if (ampThemeId != null) {
          query.setLong("ampThemeId", ampThemeId.longValue());
        }
      
        if (fromDate != null) {
          query.setDate("FromDate", fromDate);
        }
        if (toDate != null) {
          query.setDate("ToDate", toDate);
        }
        if (locationId != null) {
          query.setLong("LocationID", locationId.longValue());
        }
        if (teamMember!=null && teamMember.getTeamId()!=null){
      	  query.setLong("teamId", teamMember.getTeamId());
        }
        
  }
	  
  
  
  private static String getTeamMemberWhereClause(TeamMember teamMember) {
    Long teamId = teamMember.getTeamId();
    //boolean teamHead = teamMember.getTeamHead();
    String result = " ( act.team.ampTeamId = ";
    result += teamId.toString();
    result += " ) ";
    return result;
  }

  public static Collection getActivityCloseDates(Long activityId) {
	    Session session = null;
	    Collection col = new ArrayList();

	    try {
	      session = PersistenceManager.getRequestDBSession();
	      String queryString = "select date from " +
	          AmpActivityClosingDates.class.getName() +
	          " date where (date.ampActivityId=:actId) and type in (0,1) order by date.ampActivityClosingDateId";
	      Query qry = session.createQuery(queryString);
	      qry.setParameter("actId", activityId, Hibernate.LONG);
	      col = qry.list();
	    }
	    catch (Exception e) {
	      logger.error("Unable to get activity close dates");
	      logger.error(e.getMessage());
	    }
//	    finally {
//	      try {
//	        PersistenceManager.releaseSession(session);
//	      }
//	      catch (Exception ex) {
//	        logger.error("Release Session failed :" + ex);
//	      }
//	    }
	    return col;
	  }

  public static Collection getActivityPrograms(Long activityId) {
	    Session session = null;
	    Collection col = new ArrayList();

	    try {
	      session = PersistenceManager.getRequestDBSession();
	      String queryString = "select prog from " +
	          AmpTheme.class.getName() +
	          " prog where (prog.activityId=:actId) ";
	      Query qry = session.createQuery(queryString);
	      qry.setParameter("actId", activityId, Hibernate.LONG);
	      col = qry.list();
	    }
	    catch (Exception e) {
	      logger.error("Unable to get activity programs");
	      logger.error(e.getMessage());
	    }
//	    finally {
//	      try {
//	        PersistenceManager.releaseSession(session);
//	      }
//	      catch (Exception ex) {
//	        logger.error("Release Session failed :" + ex);
//	      }
//	    }
	    return col;
	  }


  public static Collection getActivityLocations(Long activityId) {
	    Session session = null;
	    Collection col = new ArrayList();

	    try {
	      session = PersistenceManager.getRequestDBSession();
	      String queryString = "select locs.* from amp_activity_location locs where (locs.amp_activity_id=:actId) ";
	      Query qry = session.createSQLQuery(queryString).addEntity(AmpActivityLocation.class);
	      qry.setParameter("actId", activityId, Hibernate.LONG);
	      col = qry.list();
	    }
	    catch (Exception e) {
	      logger.error("Unable to get activity locations");
	      logger.error(e.getMessage());
	    }
//	    finally {
//	      try {
//	        PersistenceManager.releaseSession(session);
//	      }
//	      catch (Exception ex) {
//	        logger.error("Release Session failed :" + ex);
//	      }
//	    }
	    return col;
	  }

  public static Collection getOrganizationWithRole(Long actId, String roleCode) {
    Session session = null;
    Collection col = new ArrayList();
    try {
      session = PersistenceManager.getSession();
      String qryStr = "select aor from " + AmpOrgRole.class.getName() + " aor " +
          "where (aor.activity=actId)";
      Query qry = session.createQuery(qryStr);
      qry.setParameter("actId", actId, Hibernate.LONG);
      Collection orgRoles = qry.list();
      Collection temp = new ArrayList();

      Iterator orgItr = orgRoles.iterator();
      while (orgItr.hasNext()) {
        AmpOrgRole orgRole = (AmpOrgRole) orgItr.next();
        if (orgRole.getRole().getRoleCode().equalsIgnoreCase(roleCode)) {
          if (!temp.contains(orgRole.getOrganisation())) {
            temp.add(orgRole.getOrganisation());
          }
        }
      }

      orgItr = temp.iterator();
      while (orgItr.hasNext()) {
        AmpOrganisation org = (AmpOrganisation) orgItr.next();
        col.add(org.getName());
      }

      AmpActivity act = (AmpActivity) session.load(AmpActivity.class, actId);

      if (act.getOrgrole() != null) {

      }
    }
    catch (Exception e) {
      logger.error("Unable to get Organization with role " + roleCode);
      logger.error(e.getMessage());
    }
    finally {
      try {
        PersistenceManager.releaseSession(session);
      }
      catch (Exception ex) {
        logger.error("Release Session failed :" + ex);
      }
    }
    return col;
  }

  /**
   * Load activity from db.
   * Use this one instead of method below this if you realy want to load all data.
   * @author irakli
   * @param id
   * @return
   * @throws DgException
   */
  public static AmpActivity loadActivity(Long id) throws DgException {
		AmpActivity result = null;
		Session session = PersistenceManager.getRequestDBSession();
		try {
			session.flush();
			result = (AmpActivity) session.get(AmpActivity.class, id);
			session.evict(result);
			result = (AmpActivity) session.get(AmpActivity.class, id);
		} catch (ObjectNotFoundException e) {
			logger.debug("AmpActivity with id=" + id + " not found");
		} catch (Exception e) {
			throw new DgException("Cannot load AmpActivity with id " + id, e);
		}
		return result;
	}
  
  
  //WTF!!!!
  public static AmpActivity getAmpActivity(Long id) {
	  //TODO: This is a mess, shouldn't be here. Check where it is used and change it.
	  
    Session session = null;
    AmpActivity activity = null;

    try {
         session = PersistenceManager.getRequestDBSession();

      activity = (AmpActivity) session.load(AmpActivity.class,
          id);
    }
    catch (Exception e) {
      logger.error("Unable to getAmpActivity");
      e.printStackTrace(System.out);
    }
    return activity;
  }

  public static AmpActivity getChannelOverview(Long actId) {
    Session session = null;
    AmpActivity activity = null;

    try {
      session = PersistenceManager.getSession();
      activity = (AmpActivity) session.load( AmpActivity.class, actId);
//      Collection act = qry.list();
//      Iterator actItr = act.iterator();
//
//      activity.setCurrCompDate(DateConversion.
//                                 ConvertDateToString(ampAct.
//            getActualCompletionDate()));
//        activity.setOrigAppDate(DateConversion.
//                                ConvertDateToString(ampAct.
//            getProposedApprovalDate()));
//        activity.setOrigStartDate(DateConversion.
//                                  ConvertDateToString(ampAct.
//            getProposedStartDate()));
//        activity.setPropCompDate(DateConversion.ConvertDateToString(ampAct.getProposedCompletionDate()));
//        activity.setRevAppDate(DateConversion.
//                               ConvertDateToString(ampAct.getActualApprovalDate()));
//        activity.setRevStartDate(DateConversion.
//                                 ConvertDateToString(ampAct.getActualStartDate()));
//        activity.setContractingDate(DateConversion.
//                                    ConvertDateToString(ampAct.
//            getContractingDate()));
//        activity.setDisbursmentsDate(DateConversion.
//                                     ConvertDateToString(ampAct.
//            getDisbursmentsDate()));
//
//        /* Set Categories */
//        activity.setProjectCategory(
//            CategoryManagerUtil.getStringValueOfAmpCategoryValue(
//                CategoryManagerUtil.getAmpCategoryValueFromList(
//            CategoryConstants.PROJECT_CATEGORY_NAME, ampAct.getCategories())
//            )
//            );
//        activity.setAccessionInstrument(
//                CategoryManagerUtil.getStringValueOfAmpCategoryValue(
//                    CategoryManagerUtil.getAmpCategoryValueFromList(
//                CategoryConstants.ACCESSION_INSTRUMENT_NAME, ampAct.getCategories())
//                )
//                );
//        activity.setAcChapter(
//            CategoryManagerUtil.getStringValueOfAmpCategoryValue(
//                CategoryManagerUtil.getAmpCategoryValueFromList(
//            CategoryConstants.ACCHAPTER_NAME, ampAct.getCategories())
//            )
//            );
//        activity.setStatus(
//            CategoryManagerUtil.getStringValueOfAmpCategoryValue(
//                CategoryManagerUtil.getAmpCategoryValueFromListByKey(
//            CategoryConstants.ACTIVITY_STATUS_KEY, ampAct.getCategories())
//            )
//            );
//        activity.setImpLevel(
//            CategoryManagerUtil.getStringValueOfAmpCategoryValues(
//                CategoryManagerUtil.getAmpCategoryValuesFromListByKey(
//            CategoryConstants.IMPLEMENTATION_LEVEL_KEY, ampAct.getCategories())
//            )
//            );
//      
//        
//        activity.setImpLocation(
//                CategoryManagerUtil.getStringValueOfAmpCategoryValues(
//                    CategoryManagerUtil.getAmpCategoryValuesFromListByKey(
//                CategoryConstants.IMPLEMENTATION_LOCATION_KEY, ampAct.getCategories())
//                )
//                );
//          
//        activity.setFinancialInstrument(CategoryManagerUtil.getStringValueOfAmpCategoryValue(
//                CategoryManagerUtil.getAmpCategoryValueFromListByKey(
//            CategoryConstants.FINANCIAL_INSTRUMENT_KEY, ampAct.getCategories())
//            ));
//        
//        /* END - Set Categories */
//        
//        Collection col = ampAct.getClosingDates();
//        List dates = new ArrayList();
//        if (col != null && col.size() > 0) {
//          Iterator itr = col.iterator();
//          while (itr.hasNext()) {
//            AmpActivityClosingDates cDate = (AmpActivityClosingDates) itr
//                .next();
//            if (cDate.getType().intValue() == Constants.REVISED.intValue()) {
//              dates.add(DateConversion.ConvertDateToString(cDate
//                  .getClosingDate()));
//            }
//          }
//        }
//        Collections.sort(dates, DateConversion.dtComp);
//        activity.setRevCompDates(dates);
//
//        if (ampAct.getThemeId() != null) {
//          activity.setProgram(ampAct.getThemeId().getName());
//          activity.setProgramDescription(ampAct.getProgramDescription());
//        }
//
//
//        activity.setMfdContFirstName(ampAct.getMofedCntFirstName());
//        activity.setMfdContLastName(ampAct.getMofedCntLastName());
//        activity.setMfdContEmail(ampAct.getMofedCntEmail());
//        
//        if (ampAct.getCreatedDate() != null) {
//          activity.setCreatedDate(
//              DateConversion.ConvertDateToString(ampAct.getCreatedDate()));
//        }
//
//        if (ampAct.getActivityCreator() != null) {
//          activity.setCreatedBy(ampAct.getActivityCreator());
//          User usr = ampAct.getActivityCreator().getUser();
//          if (usr != null) {
//            activity.setActAthFirstName(usr.getFirstNames());
//            activity.setActAthLastName(usr.getLastName());
//            activity.setActAthEmail(usr.getEmail());
//            activity.setActAthAgencySource(usr.getOrganizationName());
//          }
//        }
//
//        if (ampAct.getModality() != null) {
//          activity.setModality(ampAct.getModality().getValue());
//          activity.setModalityCode(ampAct.getModality().getIndex() + "");
//        }
//
//        queryString = "select distinct f.typeOfAssistance.value from " +
//            AmpFunding.class.getName() + " f where f.ampActivityId=:actId";
//
//        qry = session.createQuery(queryString);
//        qry.setParameter("actId", actId, Hibernate.LONG);
//
//        Collection temp = new ArrayList();
//        Iterator typesItr = qry.list().iterator();
//        while (typesItr.hasNext()) {
//          String code = (String) typesItr.next();
//          temp.add(code);
//        }
//        activity.setAssistanceType(temp);
//
//        Collection relOrgs = new ArrayList();
//        if (ampAct.getOrgrole() != null) {
//          Iterator orgItr = ampAct.getOrgrole().iterator();
//          while (orgItr.hasNext()) {
//            AmpOrgRole orgRole = (AmpOrgRole) orgItr.next();
//            AmpOrganisation auxOrgRel = orgRole.getOrganisation();
//            if(auxOrgRel!=null)
//            {
//            	RelOrganization relOrg = new RelOrganization();
//                relOrg.setOrgName(auxOrgRel.getName());
//                relOrg.setRole(orgRole.getRole().getRoleCode());
//                relOrg.setAcronym(auxOrgRel.getAcronym());
//                relOrg.setOrgCode(auxOrgRel.getOrgCode());
//                relOrg.setOrgGrpId(auxOrgRel.getOrgGrpId());
//                relOrg.setOrgTypeId(auxOrgRel.getOrgTypeId());
//                relOrg.setOrgId(auxOrgRel.getAmpOrgId());
//                if (!relOrgs.contains(relOrg)) {
//                	relOrgs.add(relOrg);
//                }
//            }
//          }
//        }
//        activity.setRelOrgs(relOrgs);
//
//        List<ActivitySector> sectors = new ArrayList<ActivitySector>();
//
//        if (ampAct.getSectors() != null) {
//          Iterator sectItr = ampAct.getSectors().iterator();
//			while (sectItr.hasNext()) {
//				AmpActivitySector ampActSect = (AmpActivitySector) sectItr.next();
//				if (ampActSect != null) {
//					AmpSector sec = ampActSect.getSectorId();
//					if (sec != null) {
//						AmpSector parent = null;
//						AmpSector subsectorLevel1 = null;
//						AmpSector subsectorLevel2 = null;
//						if (sec.getParentSectorId() != null) {
//							if (sec.getParentSectorId().getParentSectorId() != null) {
//								subsectorLevel2 = sec;
//								subsectorLevel1 = sec.getParentSectorId();
//								parent = sec.getParentSectorId().getParentSectorId();
//							} else {
//								subsectorLevel1 = sec;
//								parent = sec.getParentSectorId();
//							}
//						} else {
//							parent = sec;
//						}
//						ActivitySector actSect = new ActivitySector();
//                                                actSect.setConfigId(ampActSect.getClassificationConfig().getId());
//						if (parent != null) {
//							actSect.setId(parent.getAmpSectorId());
//							String view = FeaturesUtil.getGlobalSettingValue("Allow Multiple Sectors");
//							if (view != null)
//								if (view.equalsIgnoreCase("On")) {
//									actSect.setCount(1);
//								} else {
//									actSect.setCount(2);
//								}
//
//							actSect.setSectorId(parent.getAmpSectorId());
//							actSect.setSectorName(parent.getName());
//							if (subsectorLevel1 != null) {
//								actSect.setSubsectorLevel1Id(subsectorLevel1.getAmpSectorId());
//								actSect.setSubsectorLevel1Name(subsectorLevel1.getName());
//								if (subsectorLevel2 != null) {
//									actSect.setSubsectorLevel2Id(subsectorLevel2.getAmpSectorId());
//									actSect.setSubsectorLevel2Name(subsectorLevel2.getName());
//								}
//							}
//							actSect.setSectorPercentage(ampActSect.getSectorPercentage());
//                                                        actSect.setSectorScheme(parent.getAmpSecSchemeId().getSecSchemeName());
//                                                        
//						}
//                                               
//						sectors.add(actSect);
//					}
//				}
//          }
//        }
//        
//        Collections.sort(sectors);
//        activity.setSectors(sectors);
//        
//        
//        if (ampAct.getActivityPrograms() != null) {
//          Collection programs = new ArrayList();
//          programs.addAll(ampAct.getActivityPrograms());
//          activity.setActPrograms(programs);
//        }
//            
//        Collection locColl = new ArrayList();
//        if (ampAct.getLocations() != null) {
//          Iterator locItr = ampAct.getLocations().iterator();
//          while (locItr.hasNext()) {
//            AmpActivityLocation actLoc = (AmpActivityLocation) locItr.next();
//            if(actLoc!=null){
//            	AmpLocation ampLoc = actLoc.getLocation();
//                Location loc = new Location();
//                if (ampLoc.getAmpRegion() != null) {
//                  loc.setRegion(ampLoc.getAmpRegion().getName());
//                }
//                if (ampLoc.getAmpZone() != null) {
//                  loc.setZone(ampLoc.getAmpZone().getName());
//                }
//                if (ampLoc.getAmpWoreda() != null) {
//                  loc.setWoreda(ampLoc.getAmpWoreda().getName());
//                }
//                if(actLoc.getLocationPercentage()!=null)
//                	loc.setPercent(DecimalToText.ConvertDecimalToText(actLoc.getLocationPercentage()));
//                locColl.add(loc);
//            }
//            
//          }
//        }
//        activity.setLocations(locColl);
//        //set lessons learned
//        //activity.setLessonsLearned(ampAct.getLessonsLearned());
//
//        activity.setProjectIds(ampAct.getInternalIds());
//
//        Collection modalities = new ArrayList();
//        queryString = "select fund from " + AmpFunding.class.getName() +
//            " fund " +
//            "where (fund.ampActivityId=:actId)";
//        qry = session.createQuery(queryString);
//        qry.setParameter("actId", actId, Hibernate.LONG);
//        Iterator itr = qry.list().iterator();
//        while (itr.hasNext()) {
//          AmpFunding fund = (AmpFunding) itr.next();
//          if (fund.getFinancingInstrument() != null)
//        	  modalities.add( fund.getFinancingInstrument() );
//        }
//        activity.setModalities(modalities);
//        activity.setUniqueModalities(new TreeSet(modalities));


      }
    catch (Exception e) {
      logger.error("Unable to get channnel overview");
      e.printStackTrace(System.out);
    }
    finally {
      try {
        PersistenceManager.releaseSession(session);
      }
      catch (Exception ex) {
        logger.error("Release Session failed :" + ex);
      }
    }
    return activity;
  }

  public static Collection getActivitySectors(Long actId) {
    Session session = null;
    Collection sectors = new ArrayList();

    try {
      session = PersistenceManager.getSession();
      String queryString = "select a from " + AmpActivity.class.getName() +
          " a " + "where (a.ampActivityId=:actId)";
      Query qry = session.createQuery(queryString);
      qry.setParameter("actId", actId, Hibernate.LONG);
      Iterator itr = qry.list().iterator();
      if (itr.hasNext()) {
        AmpActivity act = (AmpActivity) itr.next();
        Set set = act.getSectors();
        if (set != null) {
          Iterator sectItr = set.iterator();
          while (sectItr.hasNext()) {
            AmpSector sec = (AmpSector) sectItr.next();
            sectors.add(sec);
          }
        }
      }
    }
    catch (Exception ex) {
      logger.error("Unable to get activity sectors :" + ex);
    }
    finally {
      try {
        PersistenceManager.releaseSession(session);
      }
      catch (Exception ex2) {
        logger.error("releaseSession() failed ");
      }
    }
    return sectors;
  }


  public static Collection getAmpActivitySectors(Long actId) {
    Session session = null;
    Collection sectors = new ArrayList();

    try {
      session = PersistenceManager.getRequestDBSession();
      String queryString = "select a.* from amp_activity_sector a " + "where a.amp_activity_id=:actId";
      Query qry = session.createSQLQuery(queryString).addEntity(AmpActivitySector.class);
      qry.setParameter("actId", actId, Hibernate.LONG);
      sectors = qry.list();
    }
    catch (Exception ex) {
      logger.error("Unable to get activity sectors :" + ex);
    }
    return sectors;
  }
  
    public static AmpActivitySector getAmpActivitySector(Long actSectorId) {
        Session session = null;
        AmpActivitySector activitySector = null;


        try {
            session = PersistenceManager.getRequestDBSession();
            activitySector = (AmpActivitySector)session.load(AmpActivitySector.class, actSectorId);
        } catch (Exception ex) {
            logger.error("Unable to get activity sectors :" + ex);
        }
        return activitySector;
    }


  public static Collection getOrgRole(Long id) {
    Session session = null;
    Collection orgroles = new ArrayList();
    try {
      session = PersistenceManager.getSession();
      String queryString = "select aor from " + AmpOrgRole.class.getName() +
          " aor " + "where (aor.activity=:actId)";
      Query qry = session.createQuery(queryString);
      qry.setParameter("actId", id, Hibernate.LONG);
      orgroles = qry.list();
    }
    catch (Exception ex) {
      logger.error("Unable to get activity sectors :" + ex);
    }
    finally {
      try {
        PersistenceManager.releaseSession(session);
      }
      catch (Exception ex2) {
        logger.error("releaseSession() failed ");
      }
    }
    return orgroles;
  }


  public static AmpRole getAmpRole(Long actId, Long orgRoleId) {
	    Session session = null;
	    AmpRole role = null;
	    try {
	      session = PersistenceManager.getSession();
	      String queryString = "select ar.* from amp_role ar " +
	      		"inner join amp_org_role aor on (aor.role = ar.amp_role_id) " +
	      		"inner join amp_activity aa on (aa.amp_activity_id = aor.activity) " +
	      		"where (aa.amp_activity_id=:actId) and (aor.amp_org_role_id=:orgRoleId)";
	      Query qry = session.createSQLQuery(queryString).addEntity(AmpRole.class);
	      qry.setParameter("actId", actId, Hibernate.LONG);
	      qry.setParameter("orgRoleId", orgRoleId, Hibernate.LONG);
	      if ((qry.list() != null) && (qry.list().size()>0)) {
	    	  role = (AmpRole)qry.list().get(0);
	      }
	    }
	    catch (Exception ex) {
	      logger.error("Unable to get amprole :" + ex);
	    }
	    return role;
	  }

  public static AmpOrganisation getAmpOrganisation(Long actId, Long orgRoleId) {
	    Session session = null;
	    AmpOrganisation organisation = null;
	    try {
	      session = PersistenceManager.getSession();
	      String queryString = "select ao.* from amp_organisation ao " +
	      		"inner join amp_org_role aor on (aor.organisation = ao.amp_org_id) " +
	      		"inner join amp_activity aa on (aa.amp_activity_id = aor.activity) " +
	      		"where (aa.amp_activity_id=:actId) and (aor.amp_org_role_id=:orgRoleId)";
	      Query qry = session.createSQLQuery(queryString).addEntity(AmpOrganisation.class);
	      qry.setParameter("actId", actId, Hibernate.LONG);
	      qry.setParameter("orgRoleId", orgRoleId, Hibernate.LONG);
	      if ((qry.list() != null) && (qry.list().size()>0)) {
	    	  organisation = (AmpOrganisation) qry.list().get(0);
	      }
	    }
	    catch (Exception ex) {
	      logger.error("Unable to get AmpOrganisation :" + ex);
	    }
	    return organisation;
	  }

  public static Collection getFundingByOrg(Long id) {
    Session session = null;
    Collection orgroles = new ArrayList();
    try {
      session = PersistenceManager.getSession();
      String queryString = "select f from " + AmpFunding.class.getName() +
          " f " + "where (f.ampDonorOrgId=:orgId)";
      Query qry = session.createQuery(queryString);
      qry.setParameter("orgId", id, Hibernate.LONG);
      orgroles = qry.list();
    }
    catch (Exception ex) {
      logger.error("Unable to get fundings for organization :" + ex);
    }
    finally {
      try {
        PersistenceManager.releaseSession(session);
      }
      catch (Exception ex2) {
        logger.error("releaseSession() failed ");
      }
    }
    return orgroles;
  }

  public static Collection<Components> getAllComponents(Long id) {
    Collection<Components> componentsCollection = new ArrayList<Components>();

    Session session = null;

    try {
      session = PersistenceManager.getSession();
      AmpActivity activity = (AmpActivity) session.load(AmpActivity.class, id);
      Set comp = activity.getComponents();
      if (comp != null && comp.size() > 0) {
        Iterator itr1 = comp.iterator();
        while (itr1.hasNext()) {
          AmpComponent ampComp = (AmpComponent) itr1.next();
          Components<FundingDetail> components = new Components<FundingDetail>();
          components.setComponentId(ampComp.getAmpComponentId());
          components.setDescription(ampComp.getDescription());
          components.setType_Id((ampComp.getType()!=null)?ampComp.getType().getType_id():null);
          components.setTitle(ampComp.getTitle());
          components.setCommitments(new ArrayList());
          components.setDisbursements(new ArrayList());
          components.setExpenditures(new ArrayList());
          components.setPhyProgress(new ArrayList());

          Collection<AmpComponentFunding> componentsFunding = ActivityUtil.getFundingComponentActivity(ampComp.
              getAmpComponentId(), activity.getAmpActivityId());
          Iterator compFundIterator = componentsFunding.iterator();
          while (compFundIterator.hasNext()) {
            AmpComponentFunding cf = (AmpComponentFunding) compFundIterator.next();
            FundingDetail fd = new FundingDetail();
            fd.setAdjustmentType(cf.getAdjustmentType().intValue());
            if (fd.getAdjustmentType() == Constants.PLANNED) {
              fd.setAdjustmentTypeName("Planned");
            }
            else if(fd.getAdjustmentType() == Constants.ACTUAL) {
                fd.setAdjustmentTypeName("Actual");
            } else if (fd.getAdjustmentType() == Constants.ADJUSTMENT_TYPE_PIPELINE) {
            	fd.setAdjustmentTypeName("Pipeline");
            }
            fd.setCurrencyCode(cf.getCurrency().getCurrencyCode());
            fd.setCurrencyName(cf.getCurrency().getCurrencyName());
            fd.setTransactionAmount(FormatHelper.formatNumber(cf.getTransactionAmount().doubleValue()));
            fd.setTransactionDate(
                DateConversion.ConvertDateToString(
                    cf.getTransactionDate()));
            fd.setTransactionType(cf.getTransactionType().intValue());
            if (fd.getTransactionType() == Constants.COMMITMENT) {
              components.getCommitments().add(fd);
            }
            else if (fd.getTransactionType() == Constants.DISBURSEMENT) {
              components.getDisbursements().add(fd);
            }
            else if (fd.getTransactionType() == Constants.EXPENDITURE) {
              components.getExpenditures().add(fd);
            }
          }
          Collection<AmpPhysicalPerformance> physicalPerf = ActivityUtil.getPhysicalProgressComponentActivity(
        		  											ampComp.getAmpComponentId(), activity.getAmpActivityId());
          Iterator<AmpPhysicalPerformance> physicalPerfIterator = physicalPerf.iterator();
          while (physicalPerfIterator.hasNext()) {
            AmpPhysicalPerformance ampPhyPerf = (AmpPhysicalPerformance) physicalPerfIterator.
                next();
            PhysicalProgress pp = new PhysicalProgress();
            pp.setDescription(ampPhyPerf.getDescription());
            pp.setPid(ampPhyPerf.getAmpPpId());
            pp.setReportingDate(
                DateConversion.ConvertDateToString(
                    ampPhyPerf.getReportingDate()));
            pp.setTitle(ampPhyPerf.getTitle());
            components.getPhyProgress().add(pp);
          }
          List list = null;
          if (components.getCommitments() != null) {
            list = new ArrayList(components.getCommitments());
            Collections.sort(list, FundingValidator.dateComp);
          }
          components.setCommitments(list);
          list = null;
          if (components.getDisbursements() != null) {
            list = new ArrayList(components.getDisbursements());
            Collections.sort(list, FundingValidator.dateComp);
          }
          components.setDisbursements(list);
          list = null;
          if (components.getExpenditures() != null) {
            list = new ArrayList(components.getExpenditures());
            Collections.sort(list, FundingValidator.dateComp);
          }
          components.setExpenditures(list);
          componentsCollection.add(components);
        }
      }

    }
    catch (Exception e) {
      logger.debug("Exception in getAmpComponents() " + e.getMessage());
    }
    finally {
      if (session != null) {
        try {
          PersistenceManager.releaseSession(session);
        }
        catch (Exception ex) {
          logger.debug("Exception while releasing session " + ex.getMessage());
        }
      }
    }
    return componentsCollection;
  }

  /*
   * edited by Govind Dalwani
   */
  // this function is to get the fundings for the components along with the activity Id

  public static Collection<AmpComponentFunding> getFundingComponentActivity(Long componentId, Long activityId) {
    Collection col = null;
    logger.debug(" inside getting the funding.....");
    Session session = null;

    try {
      session = PersistenceManager.getRequestDBSession();
    	//session = PersistenceManager.getRequestDBSession();
      String qryStr = "select a from " + AmpComponentFunding.class.getName() +
          " a " +
          "where amp_component_id = '" + componentId + "' and activity_id = '" + activityId +
          "'";
      Query qry = session.createQuery(qryStr);
      col = qry.list();
    }
    catch (Exception e) {
      logger.debug("Exception in getAmpComponents() " + e.getMessage());
      e.printStackTrace(System.out);
    }
    //getComponents();
    return col;
  }

  /*
   * This function gets AmpComponentFunding of an Activity.
   *
   * @param activityId Activity id
   */
  public static Collection<AmpComponentFunding> getFundingComponentActivity(Long activityId) {
    Collection col = null;
    logger.info(" inside getting the funding.....");
    Session session = null;

    try {
      session = PersistenceManager.getRequestDBSession();
      String qryStr = "select a from " + AmpComponentFunding.class.getName() +
          " a where activity_id = '" + activityId + "'";
      Query qry = session.createQuery(qryStr);
      col = qry.list();
    }
    catch (Exception e) {
      logger.debug("Exception in getAmpComponents() " + e.getMessage());
    }
    //getComponents();
    return col;
  }

  /*
   * This function gets AmpComponentFunding of an Activity.
   *
   * @param activityId Activity id
   */
  public static Collection<AmpComponentFunding> getFundingComponentActivity(Long activityId, Session session) {
    Collection col = null;
    logger.info(" inside getting the funding.....");

    try {
      String qryStr = "select a from " + AmpComponentFunding.class.getName() +
          " a where activity_id = '" + activityId + "'";
      Query qry = session.createQuery(qryStr);
      col = qry.list();
    }
    catch (Exception e) {
      logger.debug("Exception in getAmpComponents() " + e.getMessage());
    }
    //getComponents();
    return col;
  }

  // function for getting fundings for components and ids ends here

  //function for physical progress

  public static Collection<AmpPhysicalPerformance> getPhysicalProgressComponentActivity(Long id,
      Long actId) {
    Collection col = null;
    logger.info(" inside getting the Physical Progress.....");
    Session session = null;

    try {
      session = PersistenceManager.getSession();
      String qryStr = "select a from " + AmpPhysicalPerformance.class.getName() +
          " a " +
          "where amp_component_id = '" + id + "' and amp_activity_id = '" +
          actId + "'";
      Query qry = session.createQuery(qryStr);
      //Iterator itr = qry.list().iterator();
      col = qry.list();

    }
    catch (Exception e) {
      logger.debug("Exception in getAmpComponents() " + e.getMessage());
      e.printStackTrace(System.out);
    }
    finally {
      if (session != null) {
        try {
          PersistenceManager.releaseSession(session);
        }
        catch (Exception ex) {
          logger.debug("Exception while releasing session " + ex.getMessage());
        }
      }
    }
    return col;
  }

  //function end physical progress
//function to get all the components in the database
  public static Collection getAllComponentNames() {
    Collection col = null;
    logger.info(" inside getting the components.....");
    Session session = null;

    try {
      session = PersistenceManager.getSession();
      String qryStr = "select a from " + AmpComponent.class.getName() + " a ";
      Query qry = session.createQuery(qryStr);
      col = qry.list();

    }
    catch (Exception e) {
      logger.debug("Exception in getAmpComponents() " + e.getMessage());
      e.printStackTrace(System.out);
    }
    finally {
      if (session != null) {
        try {
          PersistenceManager.releaseSession(session);
        }
        catch (Exception ex) {
          logger.debug("Exception while releasing session " + ex.getMessage());
        }
      }
    }
    // EditActivityForm f = new EditActivityForm();
    //f.setAllComps(col);

    return col;
  }

  public static Collection getAmpIssues(Long actId) {
	  	Collection issues = null;
	    Session session = null;

	    try {
	      session = PersistenceManager.getRequestDBSession();
	      String qryStr = "select a from " + AmpIssues.class.getName() +
	          " a where a.activity.ampActivityId=:actId ";
	      Query qry = session.createQuery(qryStr);
	      qry.setParameter("actId", actId, Hibernate.LONG);
	      issues = qry.list();
	      // get issues measures
	      AmpIssues ampIssues = null;
	      Collection measures = null;
	      for (Iterator it = issues.iterator(); it.hasNext();) {
	    	  ampIssues = (AmpIssues) it.next();
	    	  measures = getAmpMeasures(ampIssues.getAmpIssueId());
	    	  if (ampIssues.getMeasures() == null) {
	    		  ampIssues.setMeasures(Collections.emptySet());
	    	  }
	    	  ampIssues.getMeasures().addAll(measures);
	    	  // get measures actors
	    	  AmpMeasure ampMeasure = null;
	    	  Collection actors = null;
	    	  for (Iterator it2 = measures.iterator(); it2.hasNext();) {
	    		  ampMeasure = (AmpMeasure) it2.next();
	    		  actors = getAmpActors(ampMeasure.getAmpMeasureId());
	    		  if (ampMeasure.getActors() == null) {
	    			  ampMeasure.setActors(Collections.emptySet());
	    		  }
	    		  ampMeasure.getActors().addAll(actors);
	    	  }
	      }
	    }
	    catch (Exception e) {
	      logger.debug("Exception in getAmpIssues() " + e.getMessage());
	    }
	    return issues;
  }

  public static Collection getAmpMeasures(Long issueId) {
	  	Collection col = null;
	    Session session = null;

	    try {
	      session = PersistenceManager.getRequestDBSession();
	      String qryStr = "select a from " + AmpMeasure.class.getName() +
	          " a where a.amp_issue_id=:issueId ";
	      Query qry = session.createQuery(qryStr);
	      qry.setParameter("issueId", issueId, Hibernate.LONG);
	      col = qry.list();
	    }
	    catch (Exception e) {
	      logger.debug("Exception in getAmpMeasures() " + e.getMessage());
	    }
	    return col;
  }
  

  public static Collection getAmpActors(Long measureId) {
	  	Collection col = null;
	    Session session = null;

	    try {
	      session = PersistenceManager.getRequestDBSession();
	      String qryStr = "select a from " + AmpActor.class.getName() +
	          " a where a.amp_measure_id=:measureId ";
	      Query qry = session.createQuery(qryStr);
	      qry.setParameter("measureId", measureId, Hibernate.LONG);
	      col = qry.list();
	    }
	    catch (Exception e) {
	      logger.debug("Exception in getAmpActors() " + e.getMessage());
	    }
	    return col;
  }

//end functino to get components
  
  public static ArrayList getRegionalObservations(Long id) {
		ArrayList<AmpRegionalObservation> list = new ArrayList<AmpRegionalObservation>();
		Session session = null;
		try {
			session = PersistenceManager.getSession();
			AmpActivity activity = (AmpActivity) session.load(AmpActivity.class, id);
			Set regObs = activity.getRegionalObservations();
			Iterator<AmpRegionalObservation> iRegObs = regObs.iterator();
			while (iRegObs.hasNext()) {
				AmpRegionalObservation auxRegOb = iRegObs.next();
				Iterator<AmpRegionalObservationMeasure> iRegMeasures = auxRegOb.getRegionalObservationMeasures()
						.iterator();
				while (iRegMeasures.hasNext()) {
					AmpRegionalObservationMeasure auxRegMeasure = iRegMeasures.next();
					Iterator<AmpRegionalObservationActor> iRegActors = auxRegMeasure.getActors().iterator();
					while (iRegActors.hasNext()) {
						iRegActors.next();
					}
				}

				list.add(auxRegOb);
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return list;
	}
  
  public static ArrayList getIssues(Long id) {
    ArrayList list = new ArrayList();

    Session session = null;
    try {
      session = PersistenceManager.getSession();
      AmpActivity activity = (AmpActivity) session.load(AmpActivity.class, id);
      Set issues = activity.getIssues();
      if (issues != null && issues.size() > 0) {
        Iterator iItr = issues.iterator();
        while (iItr.hasNext()) {
          AmpIssues ampIssue = (AmpIssues) iItr.next();
          Issues issue = new Issues();
          issue.setId(ampIssue.getAmpIssueId());
          issue.setName(ampIssue.getName());
          issue.setIssueDate(FormatHelper.formatDate(ampIssue.getIssueDate()));
          ArrayList mList = new ArrayList();
          if (ampIssue.getMeasures() != null &&
              ampIssue.getMeasures().size() > 0) {
            Iterator mItr = ampIssue.getMeasures().iterator();
            while (mItr.hasNext()) {
              AmpMeasure ampMeasure = (AmpMeasure) mItr.next();
              Measures measure = new Measures();
              measure.setId(ampMeasure.getAmpMeasureId());
              measure.setName(ampMeasure.getName());
              ArrayList aList = new ArrayList();
              if (ampMeasure.getActors() != null &&
                  ampMeasure.getActors().size() > 0) {
                Iterator aItr = ampMeasure.getActors().iterator();
                while (aItr.hasNext()) {
                  AmpActor actor = (AmpActor) aItr.next();
                  aList.add(actor);
                }
              }
              measure.setActors(aList);
              mList.add(measure);
            }
          }
          issue.setMeasures(mList);
          list.add(issue);
        }
      }
    }
    catch (Exception e) {
      logger.debug("Exception in getIssues() " + e.getMessage());
      e.printStackTrace(System.out);
    }
    finally {
      if (session != null) {
        try {
          PersistenceManager.releaseSession(session);
        }
        catch (Exception ex) {
          logger.debug("Exception while releasing session " + ex.getMessage());
        }
      }
    }
    return list;
  }

  public static Collection getRegionalFundings(Long id) {
    Collection col = new ArrayList();

    Session session = null;
    try {
      session = PersistenceManager.getRequestDBSession();
      AmpActivity activity = (AmpActivity) session.load(AmpActivity.class, id);
      col = activity.getRegionalFundings();
    }
    catch (Exception e) {
      logger.debug("Exception in getRegionalFundings() " + e.getMessage());
      e.printStackTrace(System.out);
    }
    return col;
  }

  public static Collection getRegionalFundings(Long id, Long regId) {
    Collection col = new ArrayList();

    Session session = null;
    try {
      session = PersistenceManager.getRequestDBSession();
      AmpActivity activity = (AmpActivity) session.load(AmpActivity.class, id);
      col = activity.getRegionalFundings();
      ArrayList temp = new ArrayList(col);
      Iterator itr = temp.iterator();
      AmpRegionalFunding regionFunding = new AmpRegionalFunding();
      regionFunding.setAmpRegionalFundingId(regId);
      while (itr.hasNext()) {
        AmpRegionalFunding regFund = (AmpRegionalFunding) itr.next();
        if (regionFunding.equals(regFund)) {
          col.remove(regFund);
        }
      }
    }
    catch (Exception e) {
      logger.debug("Exception in getRegionalFundings() " + e.getMessage());
      e.printStackTrace(System.out);
    }
    return col;
  }

  public static AmpActivity getActivityByName(String name , Long actId) {
    AmpActivity activity = null;
    Session session = null;
    try {
      session = PersistenceManager.getSession();
      String qryStr = "select a from " + AmpActivity.class.getName() + " a " +
          "where lower(a.name) = :lowerName";
      if(actId!=null){
    	  qryStr+=" and a.ampActivityId!="+actId;
      }
      Query qry = session.createQuery(qryStr);
      qry.setString("lowerName", name.toLowerCase());
      Iterator itr = qry.list().iterator();
      if (itr.hasNext()) {
        activity = (AmpActivity) itr.next();
      }
    }
    catch (Exception e) {
      logger.debug("Exception in isActivityExisting() " + e.getMessage());
      e.printStackTrace(System.out);
    }
    finally {
      if (session != null) {
        try {
          PersistenceManager.releaseSession(session);
        }
        catch (Exception ex) {
          logger.debug("Exception while releasing session " + ex.getMessage());
        }
      }
    }
    return activity;
  }

  public static void saveDonorFundingInfo(Long actId, Set fundings) {
    Session session = null;
    Transaction tx = null;

    try {
      session = PersistenceManager.getSession();
      tx = session.beginTransaction();

      //logger.info("Before iterating");
      Iterator itr = fundings.iterator();
      while (itr.hasNext()) {
        AmpFunding temp = (AmpFunding) itr.next();
        AmpFunding fund = (AmpFunding) session.load(AmpFunding.class,
            temp.getAmpFundingId());
        Iterator fItr = fund.getFundingDetails().iterator();
        while (fItr.hasNext()) {
          AmpFundingDetail fd = (AmpFundingDetail) fItr.next();
          session.delete(fd);
        }
        fund.getFundingDetails().clear();
        fund.setFundingDetails(temp.getFundingDetails());

        fund.getMtefProjections().clear();
        fund.getMtefProjections().addAll( temp.getMtefProjections() );
        //logger.info("Updating " + fund.getAmpFundingId());
        session.update(fund);
        //logger.info("Updated...");
      }
      tx.commit();
      //logger.info("Donor info. saved");
    }
    catch (Exception e) {
      logger.error("Exception from saveDonorFundingInfo()");
      e.printStackTrace(System.out);
      if (tx != null) {
        try {
          tx.rollback();
        }
        catch (Exception rbf) {
          logger.error("Rollback failed");
        }
      }
    }
    finally {
      if (session != null) {
        try {
          PersistenceManager.releaseSession(session);
        }
        catch (Exception rsf) {
          logger.error("Release session failed");
        }
      }
    }

  }

  public static boolean canViewActivity(Long actId, TeamMember tm) {
    boolean canView = false;
    Session session = null;
    try {
      session = PersistenceManager.getSession();
      if (tm.getTeamHead()) {
        if (tm.getTeamType().equalsIgnoreCase("DONOR")) {
          // DONOR team leader
          AmpTeam team = (AmpTeam) session.load(AmpTeam.class, tm.getTeamId());
          AmpActivity act = new AmpActivity();
          act.setAmpActivityId(actId);
          if (team.getActivityList().contains(act))
            canView = true;
        }
        else {
          // MOFED team leader
          //logger.info("Mofed team leader");
          //logger.info("loading activity " + actId);
          AmpActivity act = (AmpActivity) session.load(AmpActivity.class, actId);
          if (act.getTeam().getAmpTeamId().equals(tm.getTeamId())) {
            logger.debug("Can view " + actId + " , team " + tm.getTeamId());
            canView = true;
          }
          else {

          }
        }
      }
      else {
        AmpTeamMember ampTeamMem = (AmpTeamMember) session.load(AmpTeamMember.class,
            tm.getMemberId());
        AmpActivity act = new AmpActivity();
        act.setAmpActivityId(actId);
        if (ampTeamMem.getActivities().contains(act))
          canView = true;
      }
    }
    catch (Exception e) {
      logger.error("Exception from canViewActivity()");
      e.printStackTrace(System.out);
    }
    finally {
      if (session != null) {
        try {
          PersistenceManager.releaseSession(session);
        }
        catch (Exception rsf) {
          logger.error("Release session failed");
        }
      }
    }
    //logger.info("Canview =" + canView);
    return canView;
  }

  public static Collection getDonors(Long actId) {
    Collection col = new ArrayList();
    Session session = null;
    try {
      session = PersistenceManager.getSession();
      AmpActivity act = (AmpActivity) session.load(AmpActivity.class, actId);
      if (act.getFunding() != null) {
        Iterator itr = act.getFunding().iterator();
        while (itr.hasNext()) {
          AmpFunding fund = (AmpFunding) itr.next();
          AmpProjectDonor ampProjectDonor = new AmpProjectDonor();
          ampProjectDonor.setDonorName(fund.getAmpDonorOrgId().getName());
          ampProjectDonor.setAmpDonorId(fund.getAmpDonorOrgId().getAmpOrgId());
          col.add(ampProjectDonor);
        }
      }

    }
    catch (Exception e) {
      logger.error("Exception from getDonors()");
      e.printStackTrace(System.out);
    }
    finally {
      if (session != null) {
        try {
          PersistenceManager.releaseSession(session);
        }
        catch (Exception rsf) {
          logger.error("Release session failed");
        }
      }
    }
    return col;
  }

  public static long getActivityMaxId() {
    Session session = null;
    long maxId = 0;

    try {
      session = PersistenceManager.getSession();

      String queryString = "select max(act.ampActivityId) from "
          + AmpActivity.class.getName() + " act";
      Query qry = session.createQuery(queryString);
      Iterator itr = qry.list().iterator();
      if (itr.hasNext()) {
        Long temp = (Long) itr.next();
        if (temp != null) {
          maxId = temp.longValue();
        }
      }

    }
    catch (Exception e) {
      logger.error("Uanble to max id :" + e);
    }
    finally {

      try {
        PersistenceManager.releaseSession(session);
      }
      catch (Exception ex) {
        logger.error("releaseSession() failed " + ex);
      }
    }
    return maxId;
  }

  public static AmpActivity getProjectChannelOverview(Long id) {
    Session session = null;
    AmpActivity activity = null;

    try {
      logger.debug("Id is " + id);
      session = PersistenceManager.getSession();

      // modified by Priyajith
      // Desc: removed the usage of session.load and used the select query
      // start
      String queryString = "select a from " + AmpActivity.class.getName()
          + " a " + "where (a.ampActivityId=:id)";
      Query qry = session.createQuery(queryString);
      qry.setParameter("id", id, Hibernate.LONG);
      Iterator itr = qry.list().iterator();
      while (itr.hasNext())
        activity = (AmpActivity) itr.next();
      // end
    }
    catch (Exception ex) {
      logger
          .error("Unable to get Amp Activity getProjectChannelOverview() :"
                 + ex);
    }
    finally {
      try {
        PersistenceManager.releaseSession(session);
      }
      catch (Exception ex2) {
        logger.error("releaseSession() failed ");
      }
    }
    return activity;
  }
   /*
   * get the  the Contracts for Activity
   * 
   */
  
  public static List getIPAContracts(Long activityId) {
    Session session = null;
    List<IPAContract> contrcats = null;

    try {
      session = PersistenceManager.getRequestDBSession();

     
      String queryString = "select con from " + IPAContract.class.getName()
          + " con " + "where (con.activity=:activityId)";
      Query qry = session.createQuery(queryString);
      qry.setLong("activityId",activityId );
      contrcats = qry.list();
      ArrayList<IPAContract> fullContracts=new ArrayList<IPAContract>();
      String cc="";
      for(Iterator i=contrcats.iterator();i.hasNext();)
      {
    	  IPAContract c=(IPAContract) i.next();
    	  cc=c.getTotalAmountCurrency().getCurrencyCode();
    	  double td=0;
    	  for(Iterator j=c.getDisbursements().iterator();j.hasNext();)
    	  {
    		  IPAContractDisbursement cd=(IPAContractDisbursement) j.next();
    		  if(cd.getAmount()!=null)
    			  td+=cd.getAmount().doubleValue();
    	  }
    	  if(c.getDibusrsementsGlobalCurrency()!=null)
        	   cc=c.getDibusrsementsGlobalCurrency().getCurrencyCode();
    	  c.setTotalDisbursements(new Double(td));
    	  c.setExecutionRate(ActivityUtil.computeExecutionRateFromTotalAmount(c, c.getTotalAmountCurrency().getCurrencyCode()));
		  c.setFundingTotalDisbursements(ActivityUtil.computeFundingDisbursementIPA(c, cc));
		  c.setFundingExecutionRate(ActivityUtil.computeExecutionRateFromContractTotalValue(c, cc));
    	  
      }
    }
     
    catch (Exception ex) {
      logger.error(" [getIPAContracts(Long activityId)] Unable to get IPAContracts :"  + ex);
    }
    
    return  contrcats ;
  } 
/**
 * @author dan
 * @return
 */
  
  public static List getIPAContracts(Long activityId, String currCode) {
	    Session session = null;
	    List<IPAContract> contrcats = null;

	    try {
	      session = PersistenceManager.getRequestDBSession();

	     
	      String queryString = "select con from " + IPAContract.class.getName()
	          + " con " + "where (con.activity=:activityId)";
	      Query qry = session.createQuery(queryString);
	      qry.setLong("activityId",activityId );
	      contrcats = qry.list();
	      String cc=currCode;
          
          double usdAmount;  
  		   double finalAmount; 

	      for(Iterator i=contrcats.iterator();i.hasNext();)
	      {
	    	  IPAContract c=(IPAContract) i.next();
	    	  if(c.getTotalAmountCurrency()!=null)
	    		  cc=c.getTotalAmountCurrency().getCurrencyCode();
	    	  if(c.getDibusrsementsGlobalCurrency()!=null)
	          	   cc=c.getDibusrsementsGlobalCurrency().getCurrencyCode();
	    	  double td=0;
	    	  for(Iterator j=c.getDisbursements().iterator();j.hasNext();)
	    	  {
	    		  IPAContractDisbursement cd=(IPAContractDisbursement) j.next();
	    		  if(cd.getAmount()!=null)
     			  {
     			  	usdAmount = CurrencyWorker.convertToUSD(cd.getAmount().doubleValue(),cd.getCurrCode());
     			  	finalAmount = CurrencyWorker.convertFromUSD(usdAmount,cc);
     			  	td+=finalAmount;
     			  }
	    	  }
	    	  c.setTotalDisbursements(new Double(td));
	    	  c.setExecutionRate(ActivityUtil.computeExecutionRateFromTotalAmount(c, cc));
 		      c.setFundingTotalDisbursements(ActivityUtil.computeFundingDisbursementIPA(c, cc));
			  c.setFundingExecutionRate(ActivityUtil.computeExecutionRateFromContractTotalValue(c, cc));
	    	  
			  c.getAmendments().size();
	    	  c.getDonorContractFundinAmount();
	    	  c.getTotAmountDonorContractFunding();
	    	  c.getTotAmountCountryContractFunding();
	    	  c.getDonorContractFundingCurrency();
	    	  c.getTotalAmountCurrencyDonor();
	    	  c.getTotalAmountCurrencyCountry();
	      }
	    }
	     
	    catch (Exception ex) {
	      logger.error(" [getIPAContracts(Long activityId, String currCode)] Unable to get IPAContracts :" + ex);
	      ex.printStackTrace();
	    }
	    
	    return  contrcats ;
	  } 
  
  	public static double computeFundingDisbursementIPA(IPAContract contract, String cc){
  		
  		ArrayList<AmpFundingDetail> disbs1 = (ArrayList<AmpFundingDetail>) DbUtil.getDisbursementsFundingOfIPAContract(contract);	             
        //if there is no disbursement global currency saved in db we'll use the default from edit activity form
        
       if(contract.getTotalAmountCurrency()!=null)
    	   cc=contract.getTotalAmountCurrency().getCurrencyCode();
        double td=0;
        double usdAmount=0;  
		double finalAmount=0; 

		for(Iterator<AmpFundingDetail> j=disbs1.iterator();j.hasNext();)
  	  	{
			AmpFundingDetail fd=(AmpFundingDetail) j.next();
  		  // converting the amount to the currency from the top and adding to the final sum.
  		  if(fd.getTransactionAmount()!=null)
  			  {
  			  	try {
					usdAmount = CurrencyWorker.convertToUSD(fd.getTransactionAmount().doubleValue(),fd.getAmpCurrencyId().getCurrencyCode());
				} catch (AimException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
  			  	try {
					finalAmount = CurrencyWorker.convertFromUSD(usdAmount,cc);
				} catch (AimException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
  			  	td+=finalAmount;
  			  }
  	  	 }
//      	contract.setFundingTotalDisbursements(td);
//      	contract.setFundingExecutionRate(ActivityUtil.computeExecutionRateFromContractTotalValue(contract, cc));
  		return td;
  	}
  
  	public static double computeExecutionRateFromContractTotalValue(IPAContract c, String currCode){
  		double usdAmount1=0;  
		   double finalAmount1=0; 
      	try {
			if(c.getContractTotalValue()!=null && c.getTotalAmountCurrency().getCurrencyCode()!=null)	
				usdAmount1 = CurrencyWorker.convertToUSD(c.getContractTotalValue().doubleValue(),c.getTotalAmountCurrency().getCurrencyCode());
			else usdAmount1 = 0.0;
			} catch (AimException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  	try {
				finalAmount1 = CurrencyWorker.convertFromUSD(usdAmount1,currCode);
			} catch (AimException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		  
		  double execRate=0;
		  if(finalAmount1!=0)
			  execRate=c.getFundingTotalDisbursements()/finalAmount1;
		  c.setExecutionRate(execRate);
		  return execRate;
  	}

  	public static double computeExecutionRateFromTotalAmount(IPAContract c, String currCode){
  		double usdAmount1=0;  
		   double finalAmount1=0; 
      	try {
			if(c.getTotalAmount()!=null && c.getTotalAmountCurrency()!=null )	
				usdAmount1 = CurrencyWorker.convertToUSD(c.getTotalAmount().doubleValue(),c.getTotalAmountCurrency().getCurrencyCode());
			else usdAmount1=0.0;
			} catch (AimException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  	try {
				finalAmount1 = CurrencyWorker.convertFromUSD(usdAmount1,currCode);
			} catch (AimException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		  
		  double execRate=0;
		  if(finalAmount1!=0)
			  execRate=c.getTotalDisbursements()/finalAmount1;
		  c.setExecutionRate(execRate);
		  return execRate;
  	}

  	
  /*
   * get the list of all the activities
   * to display in the activity manager of Admin
   */
  public static List<AmpActivity> getAllActivitiesList() {
    List col = null;
    Session session = null;
    Query qry = null;

    try {
      session = PersistenceManager.getRequestDBSession();
      String queryString = "select ampAct from " + AmpActivity.class.getName() +
          " ampAct";
      qry = session.createQuery(queryString);
      col = qry.list();
      logger.debug("the size of the ampActivity : " + col.size());
    }
    catch (Exception e1) {
      logger.error("Could not retrieve the activities list from getallactivitieslist");
      e1.printStackTrace(System.out);
    }
    return col;
  }

  /*
   * get the list of all the activities
   * to display in the activity manager of Admin
   */
  public static List<AmpActivity> getAllActivitiesByName(String name) {
    List col = null;
    Session session = null;
    Query qry = null;

    try {
      session = PersistenceManager.getSession();
      String queryString = "select ampAct from " + AmpActivity.class.getName() +
          " ampAct where upper(ampAct.name) like upper(:name)";
      qry = session.createQuery(queryString);
      qry.setParameter("name", "%" + name + "%", Hibernate.STRING);
      col = qry.list();
      logger.debug("the size of the ampActivity : " + col.size());
    }
    catch (Exception e1) {
      logger.error("Could not retrieve the activities list from getallactivitiesbyname", e1);
      e1.printStackTrace();
    }
    finally {
      if (session != null) {
        try {
          PersistenceManager.releaseSession(session);
        }
        catch (Exception e2) {
          logger.error("Release session failed");
        }
      }
    }
    return col;
  }

  /* functions to DELETE an activity by Admin start here.... */
  public static void deleteActivity(Long ampActId) {
    Session session = null;
    Transaction tx = null;

    try {
      session = PersistenceManager.getSession();
      tx = session.beginTransaction();

      AmpActivity ampAct = (AmpActivity) session.load(
          AmpActivity.class, ampActId);

      if (ampAct == null)
        logger.debug("Activity is null. Hence no activity with id : " +
                     ampActId);
      else {
        /* delete fundings and funding details */
        Set fundSet = ampAct.getFunding();
        if (fundSet != null) {
          Iterator fundSetItr = fundSet.iterator();
          while (fundSetItr.hasNext()) {
            AmpFunding fund = (AmpFunding) fundSetItr.next();
            Set fundDetSet = fund.getFundingDetails();
            if (fundDetSet != null) {
              Iterator fundDetItr = fundDetSet.iterator();
              while (fundDetItr.hasNext()) {
                AmpFundingDetail ampFundingDetail = (AmpFundingDetail)fundDetItr.next();
                if(ampFundingDetail.getContract()!=null) session.delete(ampFundingDetail.getContract());
                session.delete(ampFundingDetail);
              }
            }
            Set closingDate = fund.getClosingDateHistory();
            if (closingDate != null) {
              Iterator closingDateItr = closingDate.iterator();
              while (closingDateItr.hasNext()) {
                AmpClosingDateHistory closeHistory = (AmpClosingDateHistory)
                    closingDateItr.next();
                session.delete(closeHistory);
              }
            }
            session.delete(fund);
          }
        }

        Set contracts=ampAct.getContracts();
        if(contracts!=null){
        	for (Iterator it = contracts.iterator(); it.hasNext();) {
				IPAContract c = (IPAContract) it.next();
				session.delete(c);
			}
        }
        
        /* delete regional fundings */
        fundSet = ampAct.getRegionalFundings();
        if (fundSet != null) {
          Iterator fundSetItr = fundSet.iterator();
          while (fundSetItr.hasNext()) {
            AmpRegionalFunding regFund = (AmpRegionalFunding) fundSetItr.next();
            session.delete(regFund);
          }
        }

        /* delete components */
        Set comp = ampAct.getComponents();
        if (comp != null) {
          Iterator compItr = comp.iterator();
          while (compItr.hasNext()) {
            AmpComponent ampComp = (AmpComponent) compItr.next();
            ampComp.getActivities().remove(ampAct);           
            //session.delete(ampComp);
          }
          ampAct.setComponents(null);
        }


        /* delete Component Fundings */
        Collection<AmpComponentFunding>  componentFundingCol = ampAct.getComponentFundings();
        if (componentFundingCol != null) {
  			Iterator<AmpComponentFunding> componentFundingColIt = componentFundingCol.iterator();
  			while (componentFundingColIt.hasNext()) {
  				session.delete(componentFundingColIt.next());
  			}
	  	}

        /* delete org roles */
        Set orgrole = ampAct.getOrgrole();
        if (orgrole != null) {
          Iterator orgroleItr = orgrole.iterator();
          while (orgroleItr.hasNext()) {
            AmpOrgRole ampOrgrole = (AmpOrgRole) orgroleItr.next();
            session.delete(ampOrgrole);
          }
        }

				/* delete issues,measures,actors */
				Set issues = ampAct.getIssues();
				if (issues != null) {
					Iterator iItr = issues.iterator();
					while (iItr.hasNext()) {
						AmpIssues issue = (AmpIssues) iItr.next();
						Set measure = issue.getMeasures();
						if (measure != null) {
							Iterator measureItr = measure.iterator();
							while (measureItr.hasNext()) {
								AmpMeasure ampMeasure = (AmpMeasure) measureItr.next();
								Set actor = ampMeasure.getActors();
								if (actor != null) {
									Iterator actorItr = actor.iterator();
									while (actorItr.hasNext()) {
										AmpActor ampActor = (AmpActor) actorItr.next();
										session.delete(ampActor);
									}
								}
								session.delete(ampMeasure);
							}
						}
						session.delete(issue);
					}
				}
        
        /* delete observations,measures,actors */
				Set observations = ampAct.getRegionalObservations();
				if (observations != null) {
					Iterator iItr = observations.iterator();
					while (iItr.hasNext()) {
						AmpRegionalObservation auxObservation = (AmpRegionalObservation) iItr.next();
						Set measure = auxObservation.getRegionalObservationMeasures();
						if (measure != null) {
							Iterator measureItr = measure.iterator();
							while (measureItr.hasNext()) {
								AmpRegionalObservationMeasure ampMeasure = (AmpRegionalObservationMeasure) measureItr.next();
								Set actor = ampMeasure.getActors();
								if (actor != null) {
									Iterator actorItr = actor.iterator();
									while (actorItr.hasNext()) {
										AmpRegionalObservationActor ampActor = (AmpRegionalObservationActor) actorItr.next();
										session.delete(ampActor);
									}
								}
								session.delete(ampMeasure);
							}
						}
						session.delete(auxObservation);
					}
				}

        // delete all previous sectors
        Set sectors = ampAct.getSectors();
        if (sectors != null) {
          Iterator iItr = sectors.iterator();
          while (iItr.hasNext()) {
            AmpActivitySector sec = (AmpActivitySector) iItr.next();
            session.delete(sec);
          }
        }

        /* delete activity internal id
             Set internalIds = ampAct.getInternalIds();
             if(internalIds != null)
             {
         Iterator interIdItr = internalIds.iterator();
         while(interIdItr.hasNext())
         {
         AmpActivityInternalId ampInternalId = (AmpActivityInternalId) interIdItr.next();
          logger.info("internal id : "+ampInternalId.getInternalId());
          session.delete(ampInternalId);
         }
             }
         */

        /* delete AMP activity Survey */
        Set ampSurvey = ampAct.getSurvey();
        if (ampSurvey != null) {
          Iterator surveyItr = ampSurvey.iterator();
          while (surveyItr.hasNext()) {
            AmpAhsurvey ahSurvey = (AmpAhsurvey) surveyItr.next();
            Set ahAmpSurvey = ahSurvey.getResponses();
            if (ahSurvey != null) {
              Iterator ahSurveyItr = ahAmpSurvey.iterator();
              while (ahSurveyItr.hasNext()) {
                AmpAhsurveyResponse surveyResp = (AmpAhsurveyResponse)
                    ahSurveyItr.next();
                session.delete(surveyResp);
              }
            }
            session.delete(ahSurvey);
          }
        }

        /* delete the activity relevant notes */
        Set notesSet = ampAct.getNotes();
        if (notesSet != null) {
          Iterator notesItr = notesSet.iterator();
          while (notesItr.hasNext()) {
            AmpNotes notesAmp = (AmpNotes) notesItr.next();
            session.delete(notesAmp);
          }
        }


        /* delete the activity closing dates */
        Set closingDates = ampAct.getClosingDates();
        if (closingDates != null) {
          Iterator closingDatesItr = closingDates.iterator();
          while (closingDatesItr.hasNext()) {
            AmpActivityClosingDates closingDatesItem = (AmpActivityClosingDates) closingDatesItr.next();
            session.delete(closingDatesItem);
            
          }
        }
       
        //	 delete all previous comments
        ArrayList col = org.digijava.module.aim.util.DbUtil.
            getAllCommentsByActivityId(ampAct.getAmpActivityId(), session);
        logger.info("col.size() [Inside deleting]: " + col.size());
        if (col != null) {
          Iterator itr = col.iterator();
          while (itr.hasNext()) {
            AmpComments comObj = (AmpComments) itr.next();
            comObj.setAmpActivityId(null);
            session.delete(comObj);
          }
        }
        logger.info("comments deleted");
        
        //Delete the connection with Team.
        String deleteActivityTeam = "DELETE FROM amp_team_activities WHERE amp_activity_id = " + ampAct.getAmpActivityId();
        Connection con = session.connection();
        Statement stmt = con.createStatement();
        int deletedRows = stmt.executeUpdate(deleteActivityTeam);
        
        //Delete the connection with amp_physical_performance.
        String deletePhysicalPerformance = "DELETE FROM amp_physical_performance WHERE amp_activity_id = " + ampAct.getAmpActivityId();
        con = session.connection();
        stmt = con.createStatement();
        deletedRows = stmt.executeUpdate(deletePhysicalPerformance);
        
        //Delete the connection with Indicator Project.
        //String deleteIndicatorProject = "DELETE FROM amp_indicator_project WHERE amp_activity_id = " + ampAct.getAmpActivityId();
        //con = session.connection();
        //stmt = con.createStatement();
        //deletedRows = stmt.executeUpdate(deleteIndicatorProject);
        
//        ArrayList ipacontracts = org.digijava.module.aim.util.DbUtil.getAllIPAContractsByActivityId(ampAct.getAmpActivityId());
//	    logger.debug("contracts number [Inside deleting]: " + ipacontracts.size());
//	    if (ipacontracts != null) {
//	      Iterator itr = ipacontracts.iterator();
//	      while (itr.hasNext()) {
//	        IPAContract contract = (IPAContract) itr.next();
//	        session.delete(contract);
//	      }
//	    }
//	    logger.debug("contracts deleted");

      }
      
    //Section moved here from ActivityManager.java because it didn't worked there.
	ActivityUtil.deleteActivityAmpComments(DbUtil.getActivityAmpComments(ampActId), session);
	ActivityUtil.deleteActivityPhysicalComponentReport(DbUtil.getActivityPhysicalComponentReport(ampActId), session);
	ActivityUtil.deleteActivityAmpReportCache(DbUtil.getActivityReportCache(ampActId), session);
	ActivityUtil.deleteActivityReportLocation(DbUtil.getActivityReportLocation(ampActId), session);
	ActivityUtil.deleteActivityReportPhyPerformance(DbUtil.getActivityRepPhyPerformance(ampActId), session);
	ActivityUtil.deleteActivityReportSector(DbUtil.getActivityReportSector(ampActId), session);
	//This is not deleting AmpMEIndicators, just indicators, ME is deprecated.
	ActivityUtil.deleteActivityIndicators(DbUtil.getActivityMEIndValue(ampActId), ampAct, session);
      
	  session.delete(ampAct);
      tx.commit();
      session.flush();
    }
    catch (Exception e1) {
      logger.error("Could not delete the activity with id : " + ampActId);
      e1.printStackTrace(System.out);
    }
    finally {
      if (session != null) {
        try {
          PersistenceManager.releaseSession(session);
        }
        catch (Exception e2) {
          logger.error("Release session failed");
        }
      }
    }
  }

  public static void deleteActivityAmpComments(Collection commentId, Session session) throws Exception{
     if (commentId != null) {
        Iterator commentItr = commentId.iterator();
        while (commentItr.hasNext()) {
          AmpComments ampComment = (AmpComments) commentItr.next();
          /*AmpComments ampComm = (AmpComments) session.load
              (AmpComments.class, ampComment.getAmpCommentId());*/
          session.delete(ampComment);
        }
     }
  }

  public static void deleteActivityPhysicalComponentReport(Collection
      phyCompReport, Session session) throws Exception{
      if (phyCompReport != null) {
        Iterator phyReportItr = phyCompReport.iterator();
        while (phyReportItr.hasNext()) {
          AmpPhysicalComponentReport phyReport = (AmpPhysicalComponentReport) phyReportItr.next();
          //AmpPhysicalComponentReport physicalReport = (AmpPhysicalComponentReport) session.load(AmpPhysicalComponentReport.class, phyReport.getAmpReportId());
          session.delete(phyReport);
        }
      }
  }

  public static void deleteActivityAmpReportCache(Collection repCache, Session session) throws Exception {
      if (repCache != null) {
        Iterator repCacheItr = repCache.iterator();
        while (repCacheItr.hasNext()) {
          AmpReportCache reportCache = (AmpReportCache) repCacheItr.next();
          /*AmpReportCache ampReportCache = (AmpReportCache) session.load
              (AmpReportCache.class, reportCache.getAmpReportId());*/
          session.delete(reportCache);
        }
      }
  }

  public static void deleteActivityReportLocation(Collection repLoc, Session session) throws Exception {
      if (repLoc != null) {
        Iterator repLocItr = repLoc.iterator();
        while (repLocItr.hasNext()) {
          AmpReportLocation repLocTemp = (AmpReportLocation) repLocItr.next();
          /*AmpReportLocation amprepLoc = (AmpReportLocation) session.load
              (AmpReportLocation.class, repLocTemp.getAmpReportId());*/
          session.delete(repLocTemp);
        }
      }
  }

  public static void deleteActivityReportPhyPerformance(Collection phyPerform, Session session) throws Exception {
      if (phyPerform != null) {
        Iterator phyPerformItr = phyPerform.iterator();
        while (phyPerformItr.hasNext()) {
          AmpReportPhysicalPerformance repPhyTemp = (
              AmpReportPhysicalPerformance) phyPerformItr.next();
          /*AmpReportPhysicalPerformance repPhyPerform = (
              AmpReportPhysicalPerformance) session.load
              (AmpReportPhysicalPerformance.class, repPhyTemp.getAmpPpId());*/
          session.delete(repPhyTemp);
        }
      }
  }

  public static void deleteActivityReportSector(Collection repSector, Session session) throws Exception {
      if (repSector != null) {
        Iterator repSectorItr = repSector.iterator();
        while (repSectorItr.hasNext()) {
          AmpReportSector repSecTemp = (AmpReportSector) repSectorItr.next();
          /*AmpReportSector ampRepSector = (AmpReportSector) session.load
              (AmpReportSector.class, repSecTemp.getAmpReportId());*/
          session.delete(repSecTemp);
        }
      }
  }

  public static void deleteActivityIndicators(Collection activityInd, AmpActivity activity, Session session) throws Exception {
    
			if (activityInd != null && activityInd.size() > 0) {
				for (Object indAct : activityInd) {

					AmpIndicator ind = (AmpIndicator) session.get(AmpIndicator.class, ((IndicatorActivity) indAct).getIndicator().getIndicatorId());
					IndicatorActivity indConn = IndicatorUtil.findActivityIndicatorConnection(activity, ind);
					IndicatorUtil.removeConnection(indConn);
					
					
					/*IndicatorActivity result = null;
					Long activityId = activity.getAmpActivityId();
					Long indicatorId = ind.getIndicatorId();
					String oql = "from "+IndicatorActivity.class.getName() + " conn ";
					oql += " where conn.activity.ampActivityId=:actId and conn.indicator.indicatorId=:indicId";
					Query query = session.createQuery(oql);
					query.setLong("actId", activityId);
					query.setLong("indicId", indicatorId);
					result = (IndicatorActivity) query.uniqueResult();
					session.delete(result);*/
				}
			}
  }

  /* functions to DELETE an activity by Admin end here.... */


  public static class ActivityAmounts {
    private Double proposedAmout;
    @Deprecated
    private Double plannedAmount;
    private Double actualAmount;
    private Double actualDisbAmount;

    public void AddPalenned(double amount) {
      if (plannedAmount != null) {
        plannedAmount = new Double(plannedAmount.doubleValue() + amount);
      }
      else {
        plannedAmount = new Double(amount);
      }
    }

     public void AddActualDisb(double amount) {
      if (actualDisbAmount != null) {
        actualDisbAmount = new Double(actualDisbAmount + amount);
      }
      else {
        actualDisbAmount = amount;
      }
    }

    public void AddActual(double amount) {
      if (actualAmount != null) {
        actualAmount = new Double(actualAmount.doubleValue() + amount);
      }
      else {
        actualAmount = new Double(amount);
      }
    }

    public String actualAmount() {
      if (actualAmount == null || actualAmount == 0) {
        return "N/A";
      }
      return FormatHelper.formatNumber(actualAmount);
    }

    public String actualDisbAmount() {
      if (actualDisbAmount == null || actualDisbAmount == 0) {
        return "N/A";
      }
      return FormatHelper.formatNumber(actualDisbAmount);
    }

    public String plannedAmount() {
      if (plannedAmount == null|| plannedAmount == 0) {
        return "N/A";
      }
      return FormatHelper.formatNumber(plannedAmount);
    }

    public String proposedAmout() {
      if (proposedAmout == null) {
        return "N/A";
      }
      return FormatHelper.formatNumber(proposedAmout);
    }

    public void setProposedAmout(double proposedAmout) {
      this.proposedAmout = new Double(proposedAmout);
    }


      public double getActualDisbAmoount() {
          if (actualDisbAmount == null) {
              return 0;
          }
          return actualDisbAmount;
      }

     public void setActualDisbAmount(Double actualDisbAmount) {
          this.actualDisbAmount = actualDisbAmount;
      }

    public double getActualAmount() {
      if (actualAmount == null) {
        return 0;
      }
      return actualAmount.doubleValue();
    }

    public double getPlannedAmount() {
      if (plannedAmount == null) {
        return 0;
      }
      return plannedAmount.doubleValue();
    }

    public double getProposedAmout() {
      if (proposedAmout == null) {
        return 0;
      }
      return proposedAmout.doubleValue();
    }

  }

  public static ActivityAmounts getActivityAmmountIn(AmpActivity act,
      String tocode,Long percent) throws Exception {
    double tempProposed = 0;
    double tempActual = 0;
    double tempPlanned = 0;
    ActivityAmounts result = new ActivityAmounts();

    AmpCategoryValue statusValue = CategoryManagerUtil.
        getAmpCategoryValueFromListByKey(CategoryConstants.ACTIVITY_STATUS_KEY,act.getCategories());

    if (act != null && statusValue != null) {
      if (CategoryManagerUtil.equalsCategoryValue(statusValue, CategoryConstants.ACTIVITY_STATUS_PROPOSED) &&
          act.getFunAmount() != null) {
        String currencyCode = act.getCurrencyCode();
        //AMP-1403 assume USD if no code is specified
        if (currencyCode == null || currencyCode.trim().equals("")) {
          currencyCode = "USD";
        } //end of AMP-1403
        //apply program percent
        tempProposed = CurrencyWorker.convert(act.getFunAmount().doubleValue()*percent/100,tocode);
        result.setProposedAmout(tempProposed);
      }
      else {

          Set fundings = act.getFunding();
          if (fundings != null) {
              Iterator fundItr = act.getFunding().iterator();
              while(fundItr.hasNext()) {
                  AmpFunding ampFunding = (AmpFunding) fundItr.next();
				  Collection fundDetails = ampFunding.getFundingDetails();

                  org.digijava.module.aim.logic.FundingCalculationsHelper calculations = new org.digijava.module.aim.logic.FundingCalculationsHelper();
                  calculations.doCalculations(fundDetails, tocode);
                  //apply program percent
                  result.AddActual(calculations.getTotActualComm().doubleValue()*percent/100);
                  result.AddPalenned(calculations.getTotPlannedComm().doubleValue()*percent/100);
                  result.AddActualDisb(calculations.getTotActualDisb().doubleValue()*percent/100);
              }
          }
        }

    }
    return result;
  }

  public static List getActivityProgramsByProgramType(Long actId, String settingName) {
                Session session = null;
                List col = new ArrayList();
                try {
                       session = PersistenceManager.getRequestDBSession();
                       String queryString = "select ap from " +AmpActivityProgram.class.getName() +
                       " ap join ap.programSetting s where (ap.activity=:actId) and (s.name=:settingName)";
                       Query qry = session.createQuery(queryString);
                       qry.setLong("actId",actId);
                       qry.setString("settingName",settingName);
                       col = qry.list();
                } catch (Exception e) {
                       logger.error("Unable to get all components");
                       logger.error(e.getMessage());
                }
                return col;
       }
  public static boolean isImplLocationCountry(Long actId) {
                Session session = null;
                boolean flag = false;
                try {
                       session = PersistenceManager.getRequestDBSession();
                       String queryString = "select apl from " +AmpActivityLocation.class.getName() +
                       " apl join apl.location l where (apl.activity=:actId) and (l.ampRegion is not NULL or l.ampZone is not  NULL or l.ampWoreda is not NULL)";
                       Query qry = session.createQuery(queryString);
                       qry.setLong("actId",actId);
                       if(qry.list()!=null&&qry.list().size()>0){
                           flag=true;
                       }
                } catch (Exception e) {
                       logger.error("Unable to get locations");
                       logger.error(e.getMessage());
                }
                return flag;
       }

  public static class HelperAmpActivityNameComparator
        implements Comparator {
        public int compare(Object obj1, Object obj2) {
            AmpActivity act1 = (AmpActivity) obj1;
            AmpActivity act2 = (AmpActivity) obj2;
            return (act1.getName()!=null && act2.getName()!=null)?act1.getName().compareTo(act2.getName()):0; 
        }
    }

  /**
   * Comparator for AmpActivity class.
   * Compears activities by its ID's.
   * AmpActivity is comparable itself, but it is comparable by names,
   * so this class was created to compeare them with ID's
   * @see AmpActivity#compareTo(AmpActivity)
   */
  public static class ActivityIdComparator
      implements Comparator<AmpActivity> {

    public int compare(AmpActivity act1, AmpActivity act2) {
      return act1.getAmpActivityId().compareTo(act2.getAmpActivityId());
    }
  }

  /**
   * Creates map from {@link AmpActivityReferenceDoc} collection
   * where each elements key is the id of {@link AmpCategoryValue} object which is asigned to the element itself
   *
   */
  public static class CategoryIdRefDocMapBuilder implements AmpCollectionUtils.KeyResolver<Long, AmpActivityReferenceDoc>{

	public Long resolveKey(AmpActivityReferenceDoc element) {
		return element.getCategoryValue().getId();
	}

  }
  
  /**
   * generates ampId
   * @param user,actId
   * @return ampId
   * @author dare
 * @param session 
   */
  public static String generateAmpId(User user,Long actId, Session session) {
		String retValue=null;		
		String globSetting="numeric";// TODO This should come from global settings
		if(globSetting.equals("numeric")){
			retValue=numericAmpId(user,actId,session);
		}else if(globSetting.equals("text")){
			retValue=combinedAmpId(actId);
		}
		return retValue;
	}
/**
 * combines countryId, current member id and last activityId+1 and makes ampId
 * @param user,actId
 * @return 
 * @author dare
 * @param session 
 */
	private static String numericAmpId(User user,Long actId, Session session){
		String retVal=null;
		String countryCode=FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GLOBAL_DEFAULT_COUNTRY);
		String userId=user.getId().toString();
	    Country country=(Country) session.load(Country.class, countryCode);   
            //    Country country=DbUtil.getDgCountry(countryCode);
                String countryId="0";
                if(country!=null){
                    countryId=country.getCountryId().toString();
                }
		
		String lastId=null;
		if(actId!=null){
			 lastId = actId.toString();	
		}		
		retVal=countryId+userId+lastId;
		return retVal;
	}

	/**
	 * combines countryId, current member id (for admin is 00) and last activityId+1 and makes ampId
	 * @param user,actId
	 * @return 
	 * @author dan
	 */
		public static String numericAmpId(String user,Long actId){
			String retVal=null;
			String countryCode=FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GLOBAL_DEFAULT_COUNTRY);
			String userId=user;
	                Country country=DbUtil.getDgCountry(countryCode);
	                String countryId="0";
	                if(country!=null){
	                    countryId=country.getCountryId().toString();
	                }
			
			String lastId=null;
			if(actId!=null){
				 lastId = actId.toString();	
			}		
			retVal=countryId+userId+lastId;
			return retVal;
		}
	
	/**
	 * combines countryIso and last activityId+1 and makes ampId
	 * @param actId
	 * @return 
	 * @author dare
	 */
	private static String combinedAmpId(Long actId){
		String retVal=null;
		String countryCode=FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GLOBAL_DEFAULT_COUNTRY);
		String lastId=null;
		if(actId!=null){
			 lastId = actId.toString();	
		}	
		retVal=countryCode.toUpperCase()+"/"+lastId;		
		return retVal;
	}
	
	public static Collection getActivitiesRelatedToAmpTeamMember(Session session, Long ampTeamMemberId) {
		  try {
	            String queryStr	= "SELECT a FROM " + AmpActivity.class.getName() + " a left join a.member m WHERE " +
	            			"(a.activityCreator=:atmId) OR (a.updatedBy=:atmId) OR (a.approvedBy = :atmId) OR (m.ampTeamMemId = :atmId)";
	            Query qry = session.createQuery(queryStr);
	            qry.setLong("atmId", ampTeamMemberId);
	            
	            return qry.list();
	            
		  }
		  catch (Exception ex) {
	        ex.printStackTrace();
	        logger.error("There was an error getting all activities related to AmpTeamMember " + ampTeamMemberId);
	        return null;
		  }
	}
	
	public static String collectionToCSV(Collection<AmpActivity> activities) {
		if ( activities == null )
			return null;
		String ret	= "";
		Iterator<AmpActivity> iter	= activities.iterator();
		while ( iter.hasNext() ) {
			AmpActivity activity	= iter.next();
			if ( activity.getName() != null ) {
				ret	+= "'" + activity.getName() + "'" + ", ";
			}
			else
				ret +=  "' '" + ", ";
		}
		return ret.substring(0, ret.length()-2);
		
	}
        /**
         * returns visible Steps (depends on features and module names in editActivityMenu.jsp)
	 * @return List of visible Steps
	 */
        public static List getSteps(){
            List<Step> steps=new ArrayList();
            Long templId=FeaturesUtil.getGlobalSettingValueLong("Visibility Template");
            AmpFeaturesVisibility step1=FeaturesUtil.getFeatureByName("Identification", "Project ID and Planning", templId);// step 1
            AmpFeaturesVisibility step1_1=FeaturesUtil.getFeatureByName("Planning", "Project ID and Planning", templId); // step 1
            AmpModulesVisibility step2=FeaturesUtil.getModuleByName("References", "PROJECT MANAGEMENT", templId);
            AmpFeaturesVisibility step3=FeaturesUtil.getFeatureByName("Location", "Project ID and Planning", templId);
            AmpFeaturesVisibility step3_2=FeaturesUtil.getFeatureByName("Sectors", "Project ID and Planning", templId);
            AmpModulesVisibility step3_3=FeaturesUtil.getModuleByName("National Planning Dashboard", "NATIONAL PLAN DASHBOARD", templId);
            AmpFeaturesVisibility step4=FeaturesUtil.getFeatureByName("Funding Information", "Funding", templId);
            AmpFeaturesVisibility step5=FeaturesUtil.getFeatureByName("Regional Funding", "Funding", templId);
            AmpFeaturesVisibility step6=FeaturesUtil.getFeatureByName("Components", "Components", templId);
            AmpFeaturesVisibility step6_2=FeaturesUtil.getFeatureByName("Issues", "Issues", templId);
            AmpModulesVisibility step7=FeaturesUtil.getModuleByName("Document", "PROJECT MANAGEMENT", templId);
            AmpFeaturesVisibility step10=FeaturesUtil.getFeatureByName("Paris Indicator", "Add & Edit Activity", templId);
            
            AmpModulesVisibility step11=FeaturesUtil.getModuleByName("M & E", "MONITORING AND EVALUATING", templId);
            AmpFeaturesVisibility step12=FeaturesUtil.getFeatureByName("Costing", "Activity Costing", templId);
            AmpFeaturesVisibility step13=FeaturesUtil.getFeatureByName("Contracting", "Contracting", templId);
            
            AmpFeaturesVisibility step14=FeaturesUtil.getFeatureByName("Regional Observations", "Regional Observations", templId);
            
            if(step1!=null||step1_1!=null){
                Step newStep1=new Step();
                newStep1.setStepActualNumber(steps.size()+1);
                newStep1.setStepNumber("1");
                steps.add(newStep1);
                        
            }
            if(step2!=null){
                Step newStep2=new Step();
                newStep2.setStepActualNumber(steps.size()+1);
                newStep2.setStepNumber("1_5");
                steps.add(newStep2);
                
            }
            if(step3!=null||step3_2!=null||step3_3!=null){
                Step newStep3=new Step();
                newStep3.setStepActualNumber(steps.size()+1);
                newStep3.setStepNumber("2");
                steps.add(newStep3);
                
            }
            if(step4!=null){
                Step newStep4=new Step();
                newStep4.setStepActualNumber(steps.size()+1);
                newStep4.setStepNumber("3");
                steps.add(newStep4);
                
            }
             if(step5!=null){
                Step newStep5=new Step();
                newStep5.setStepActualNumber(steps.size()+1);
                newStep5.setStepNumber("4");
                steps.add(newStep5);
                
            }
            if(step6!=null||step6_2!=null){
                Step newStep6=new Step();
                newStep6.setStepActualNumber(steps.size()+1);
                newStep6.setStepNumber("5");
                steps.add(newStep6);
                
            }
            
            if(step7!=null){
                Step newStep7=new Step();
                newStep7.setStepActualNumber(steps.size()+1);
                newStep7.setStepNumber("6");
                steps.add(newStep7);
                
            }
            Step newStep8 = new Step();
            newStep8.setStepActualNumber(steps.size() + 1);
            newStep8.setStepNumber("7");
            steps.add(newStep8);
                
            Step newStep9 = new Step();
            newStep9.setStepActualNumber(steps.size() + 1);
            newStep9.setStepNumber("8");
            steps.add(newStep9);
            
            if (step10 != null) {
                Step newStep10 = new Step();
                newStep10.setStepActualNumber(steps.size() + 1);
                newStep10.setStepNumber("17");
                steps.add(newStep10);
            }
            
             if(step11!=null){
                Step newStep11=new Step();
                newStep11.setStepActualNumber(steps.size()+1);
                newStep11.setStepNumber("10");
                steps.add(newStep11);
                
            }
            
             if(step12!=null){
                Step newStep12=new Step();
                newStep12.setStepActualNumber(steps.size()+1);
                newStep12.setStepNumber("11");
                steps.add(newStep12);
                
            }
           
            
              if(step13!=null){
                Step newStep13=new Step();
                newStep13.setStepActualNumber(steps.size()+1);
                newStep13.setStepNumber("13");
                steps.add(newStep13);
                
            }
              
            if (step14 != null) {
				Step newStep14 = new Step();
				newStep14.setStepActualNumber(steps.size() + 1);
				newStep14.setStepNumber("14");
				steps.add(newStep14);
            }
            
            return steps;
        }
        
        /**
         * @author Dare
         * @param partOfName
         * @return Array of Strings,which have a look like: activity_name(activiti_id) 
         */
        public static String[] loadActivitiesNamesAndIds(TeamMember member) throws DgException{
        	Session session=null;
    		String queryString =null;
    		Query query=null;
    		List activities=null;
    		String [] retValue=null;
    		try {
                    session=PersistenceManager.getRequestDBSession();
                    
                Set<String> activityStatus = new HashSet<String>();
                String teamType=member.getTeamType();
		activityStatus.add(Constants.APPROVED_STATUS);
		activityStatus.add(Constants.EDITED_STATUS);
                Set relatedTeams=TeamUtil.getRelatedTeamsForMember(member);
                    Set teamAO = TeamUtil.getComputedOrgs(relatedTeams);
                    // computed workspace
                    if (teamAO != null && !teamAO.isEmpty()) {
                        queryString = "select a.name, a.ampActivityId from " + AmpActivity.class.getName() + " a left outer join a.orgrole r  left outer join a.funding f " +
                                " where  a.team in  (" + Util.toCSString(relatedTeams) + ")    or (r.organisation in  (" + Util.toCSString(teamAO) + ") or f.ampDonorOrgId in (" + Util.toCSString(teamAO) + ")) order by a.name";

                    } else {
                        // none computed workspace
                        queryString = "select a.name, a.ampActivityId from " + AmpActivity.class.getName() + " a  where  a.team in  (" + Util.toCSString(relatedTeams) + ")    ";
                        if (teamType!= null && teamType.equalsIgnoreCase(Constants.ACCESS_TYPE_MNGMT)) {
                            queryString += "  and approvalStatus in (" + Util.toCSString(activityStatus) + ")  ";
                        }
                        queryString += " order by a.name ";
                    }
    			  			
    			query=session.createQuery(queryString);    			
    			activities=query.list(); 		
    		}catch(Exception ex) { 
    			logger.error("couldn't load Activities" + ex.getMessage());	
    			ex.printStackTrace(); 
    		} 
    		if (activities != null){
    			retValue=new String[activities.size()];    		
    			int i=0;
    			for (Object rawRow : activities) {
					Object[] row = (Object[])rawRow; //:)
					String nameRow=(String)row[0];			
					if(nameRow != null){
					nameRow = nameRow.replace('\n', ' ');
					nameRow = nameRow.replace('\r', ' ');
					nameRow = nameRow.replace("\\", "");
					}
					//System.out.println(nameRow);
					retValue[i]=nameRow+"("+row[1]+")";
					i++;					
				}
    		}
    		return retValue;
        }
        
        /** 
         * @param actId
         * @return activity name
         * @author dare
         */
        public static String getActivityName(Long actId) throws DgException{
        	Session session=null;
    		String queryString =null;
    		Query query=null;    		
    		String name=null;
    		try {
    			session=PersistenceManager.getRequestDBSession();
    			queryString= "select a.name  from " + AmpActivity.class.getName()+ " a where a.ampActivityId="+actId;
    			query=session.createQuery(queryString);    			
    			name=(String)query.uniqueResult();    			
    		}catch(Exception ex) { 
    			logger.error("couldn't load Activity" + ex.getMessage());	
    			ex.printStackTrace(); 
    		} 
    		return name;
        }
        
        public static String stackTraceToString(Throwable e) {
        	String retValue = null;
        	StringWriter sw = null;
        	PrintWriter pw = null;
        	try {
        		sw = new StringWriter();
        		pw = new PrintWriter(sw);
        		e.printStackTrace(pw);
        		retValue = sw.toString();
        	} finally {
        		try {
        			if(pw != null)  pw.close();
        			if(sw != null)  sw.close();
        		} catch (IOException ignore) {}
        	}
        	return retValue;
        }
        /**
         * @author Marcelo
         * @param 
         * @return Array of Strings, which have budget_code_project_id's 
         */
        public static String[] getBudgetCodes() throws DgException{
        	Session session=null;
    		String queryString =null;
    		Query query=null;
    		List activities=null;
    		String [] retValue=null;
    		try {
                session=PersistenceManager.getRequestDBSession();
                queryString = "select distinct a.budgetCodeProjectID from " + AmpActivity.class.getName() + " a";    			  			
    			query=session.createQuery(queryString);    			
    			activities=query.list(); 		
    		}catch(Exception ex) { 
    			logger.error("couldn't load Activities" + ex.getMessage());	
    			ex.printStackTrace(); 
    		} 
    		if (activities != null){
    			//filtering null and blank values 
    			ArrayList<String> codes = new ArrayList<String>();
    			for (Object rawRow : activities) {
    				String val = (String)rawRow; 
    				if(val!=null && val.trim().compareTo("")!=0){
    					codes.add(val);
    				}
				}
    			//add filtered values to the array
    			int i=0;
    			if(codes.size()!=0){
    				retValue=new String[codes.size()];
    				for(String desc : codes){
    					retValue[i]=desc;
    					i++;
    				}
    			}
    		}
    		return retValue;
        }
        /**
         * 
         * @param fundDets
         * @return
         * @see ShowAddComponent
         * @see GetFundingTotals
         * 
         */
      public static Collection<AmpFundingDetail> createAmpFundingDetails(Collection fundDets) {
        Collection<AmpFundingDetail> ampFundDets = new ArrayList<AmpFundingDetail>();
        if (fundDets != null) {
            Iterator<FundingDetail> iter = fundDets.iterator();
            while (iter.hasNext()) {
                FundingDetail helperFdet = iter.next();
                AmpCurrency detCurr = CurrencyUtil.getAmpcurrency(helperFdet.getCurrencyCode());
                Date date = DateConversion.getDate(helperFdet.getTransactionDate());
                java.sql.Date dt = new java.sql.Date(date.getTime());
                Double transAmt = new Double(FormatHelper.parseDouble(helperFdet.getTransactionAmount()));
                if ("true".equals(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS))) {
                    transAmt *= 1000;
                }
                double exchangeRate=(helperFdet.getFixedExchangeRate()!=null)?FormatHelper.parseDouble( helperFdet.getFixedExchangeRate() ):Util.getExchange(detCurr.getCurrencyCode(), dt);
                AmpFundingDetail fundDet = new AmpFundingDetail(helperFdet.getTransactionType(), helperFdet.getAdjustmentType(), transAmt, date, detCurr, exchangeRate);
                ampFundDets.add(fundDet);
            }
        }
        return ampFundDets;
    }
      
      public static AmpTeamMember getActivityUpdator(Long actId){
    	  AmpTeamMember updator=null;
    	  String queryString;
    	  Query query;
    	  Session session=null;
    	  try {
			session=PersistenceManager.getRequestDBSession();
			queryString=" select act.updatedBy from " + AmpActivity.class.getName()+" act where act.ampActivityId="+actId;
			query=session.createQuery(queryString);
			updator=(AmpTeamMember)query.uniqueResult();
		} catch (Exception e) {
			logger.error("couldn't load Activity Updator" + e.getMessage());			
		}
    	  return updator;
      }
      public static class EUActivityKeyResolver implements KeyResolver<Long, EUActivity>{
    		@Override
    		public Long resolveKey(EUActivity element){
    			return element.getId();
    		}
    	}
      
      public static Long getBudgetSector(Long actId) throws DgException{
      	Session session=null;
  		String queryString =null;
  		Query query=null;    		
  		Long sector=null;
  		try {
  			session=PersistenceManager.getRequestDBSession();
  			queryString= "select a.budgetsector  from " + AmpActivity.class.getName()+ " a where a.ampActivityId="+actId;
  			query=session.createQuery(queryString);    			
  			sector=(Long)query.uniqueResult();    			
  		}catch(Exception ex) { 
  			logger.error("couldn't load Activity" + ex.getMessage());	
  			ex.printStackTrace(); 
  		} 
  		return sector;
      }
      
      public static Long getBudgetProgram(Long actId) throws DgException{
        	Session session=null;
    		String queryString =null;
    		Query query=null;    		
    		Long program=null;
    		try {
    			session=PersistenceManager.getRequestDBSession();
    			queryString= "select a.budgetprogram  from " + AmpActivity.class.getName()+ " a where a.ampActivityId="+actId;
    			query=session.createQuery(queryString);    			
    			program=(Long)query.uniqueResult();    			
    		}catch(Exception ex) { 
    			logger.error("couldn't load Activity" + ex.getMessage());	
    			ex.printStackTrace(); 
    		} 
    		return program;
        }
      public static Long getBudgetOrganization(Long actId) throws DgException{
        	Session session=null;
    		String queryString =null;
    		Query query=null;    		
    		Long org=null;
    		try {
    			session=PersistenceManager.getRequestDBSession();
    			queryString= "select a.budgetorganization  from " + AmpActivity.class.getName()+ " a where a.ampActivityId="+actId;
    			query=session.createQuery(queryString);    			
    			org=(Long)query.uniqueResult();    			
    		}catch(Exception ex) { 
    			logger.error("couldn't load Activity" + ex.getMessage());	
    			ex.printStackTrace(); 
    		} 
    		return org;
        }
      public static Long getBudgetDepartment(Long actId) throws DgException{
        	Session session=null;
    		String queryString =null;
    		Query query=null;    		
    		Long department=null;
    		try {
    			session=PersistenceManager.getRequestDBSession();
    			queryString= "select a.budgetdepartment  from " + AmpActivity.class.getName()+ " a where a.ampActivityId="+actId;
    			query=session.createQuery(queryString);    			
    			department=(Long)query.uniqueResult();    			
    		}catch(Exception ex) { 
    			logger.error("couldn't load Activity" + ex.getMessage());	
    			ex.printStackTrace(); 
    		} 
    		return department;
        }
      
	public static String getApprovedActivityQueryString(String label) {
		String query = null;
		query = " AND " + label + ".draft = false AND " + label + ".approvalStatus LIKE 'approved' ";
		return query;
	}
} // End
