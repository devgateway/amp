/*
 * ActivityUtil.java
 * Created: 14 Feb, 2005
 */

package org.digijava.module.aim.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.WorkspaceFilter;
import org.dgfoundation.amp.ar.viewfetcher.InternationalizedModelDescription;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.error.ExceptionFactory;
import org.dgfoundation.amp.error.keeper.ErrorReportingPlugin;
import org.dgfoundation.amp.onepager.models.AbstractAmpAutoCompleteModel;
import org.dgfoundation.amp.utils.AmpCollectionUtils;
import org.dgfoundation.amp.utils.AmpCollectionUtils.KeyResolver;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.user.User;
import org.digijava.module.admin.helper.AmpActivityFake;
import org.digijava.module.aim.action.GetFundingTotals;
import org.digijava.module.aim.action.RecoverySaveParameters;
import org.digijava.module.aim.action.ShowAddComponent;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityContact;
import org.digijava.module.aim.dbentity.AmpActivityGroup;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpActivityProgram;
import org.digijava.module.aim.dbentity.AmpActivityReferenceDoc;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpActor;
import org.digijava.module.aim.dbentity.AmpAhsurvey;
import org.digijava.module.aim.dbentity.AmpAhsurveyResponse;
import org.digijava.module.aim.dbentity.AmpComments;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpComponentFunding;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpContactProperty;
import org.digijava.module.aim.dbentity.AmpContentTranslation;
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
import org.digijava.module.aim.dbentity.AmpStructureImg;
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
import org.digijava.module.visualization.util.DashboardUtil;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

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
	AmpActivityVersion activity = rsp.getActivity();
	Long oldActivityId = rsp.getOldActivityId();
	boolean edit = rsp.isEdit();
	//edit = false;
	ArrayList commentsCol = rsp.getEaForm().getComments().getCommentsCol();
	boolean serializeFlag = rsp.getEaForm().isSerializeFlag();
	Long field = rsp.getField();
	Collection relatedLinks = rsp.getRelatedLinks();
	Long memberId = rsp.getTm().getMemberId();
	Collection indicators = rsp.getEaForm().getIndicator().getIndicatorsME();
	//Set<Components<AmpComponentFunding>> componentsFunding = rsp.getTempComp();
	List<IPAContract> contracts = rsp.getEaForm().getContracts().getContracts();
	boolean alwaysRollback = rsp.isAlwaysRollback();
	AmpActivityGroup usedAmpActivityGroup = null;
	//***
	
	
	
    logger.debug("In save activity " + activity.getName());
    Session session = null;
    Transaction tx = null;
    AmpActivityVersion oldActivity = null;

    Long activityId = null;
    Set fundSet		= null;
    boolean exceptionRaised = false;
    
    AMPException savedEx = new AMPException(Constants.AMP_ERROR_LEVEL_ERROR, false, "Save activity failure");
    
    try {
      session = PersistenceManager.getRequestDBSession();
      session.connection().setAutoCommit(false);
//beginTransaction();
      
      AmpTeamMember member = (AmpTeamMember) session.load(AmpTeamMember.class,
          memberId);

      if (oldActivityId != null) {
          oldActivity = (AmpActivityVersion) session.load(AmpActivity.class, oldActivityId);
      }
     
      if (edit) { /* edit an existing activity */        
        activityId = oldActivityId;
        activity.setAmpActivityId(oldActivityId);
        
        usedAmpActivityGroup = (AmpActivityGroup) session.load(AmpActivityGroup.class, oldActivity.getAmpActivityGroup().getAmpActivityGroupId());

        //creo una nueva actividad a ver q pasa.
        //oldActivity = new AmpActivity();
        oldActivity.setAmpActivityId(new Long(oldActivityId.intValue()+1));

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
        
        if (activity.getIssues() != null)
        	oldActivity.getIssues().addAll(activity.getIssues());
        
        if (activity.getRegionalObservations() != null) {
        	oldActivity.setRegionalObservations(new HashSet());
        	oldActivity.getRegionalObservations().addAll(activity.getRegionalObservations());
        }
        
        if (activity.getCosts() != null)
        	oldActivity.getCosts().addAll(activity.getCosts());

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
        oldActivity.setModifiedBy(member);
        session.saveOrUpdate(oldActivity);
        activityId = oldActivity.getAmpActivityId();
        String ampId=generateAmpId(member.getUser(),activityId,session );
        if (oldActivity.getAmpId()==null){
            oldActivity.setAmpId(ampId);
        }
        session.update(oldActivity);
        activity = oldActivity;
        
        /**
         * ACTIVITY VERSIONING: This section saves a new record in amp_activity_group table.
         */
        //TODO: Check for activated versioning.
        usedAmpActivityGroup.setAmpActivityLastVersion(oldActivity);
        session.save(usedAmpActivityGroup);
        oldActivity.setAmpActivityGroup(usedAmpActivityGroup);
        oldActivity.setModifiedDate(Calendar.getInstance().getTime());
        oldActivity.setModifiedBy(member);
        session.update(oldActivity);
                
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
        
        		/**
				* ACTIVITY VERSIONING: This section saves a new record in
				* amp_activity_group table.
				*/
				// TODO: Check for activated versioning.
				if (oldActivity == null) {
					// New activity.
					AmpActivityGroup newActivityGroup = new AmpActivityGroup();
					newActivityGroup.setAmpActivityLastVersion(activity);
					session.save(newActivityGroup);
					activity.setAmpActivityGroup(newActivityGroup);
					activity.setModifiedDate(Calendar.getInstance().getTime());
					activity.setModifiedBy(member);
					session.update(activity);
					oldActivity = activity;
				} else if (oldActivity != null && oldActivity.getAmpActivityGroup() != null) {
					// Edited activity with version group.
					AmpActivityGroup auxActivityGroup = (AmpActivityGroup) session.load(AmpActivityGroup.class,
							oldActivity.getAmpActivityGroup().getAmpActivityGroupId());
					auxActivityGroup.setAmpActivityLastVersion(activity);
					session.save(auxActivityGroup);
					activity.setAmpActivityGroup(auxActivityGroup);
					activity.setModifiedDate(Calendar.getInstance().getTime());
					activity.setModifiedBy(member);
					session.update(activity);
				} else if (oldActivity != null && oldActivity.getAmpActivityGroup() == null) {
					// Edited activity with no version group info (activity
					// created BEFORE the versioning system).
					AmpActivityGroup newActivityGroup = new AmpActivityGroup();
					newActivityGroup.setAmpActivityLastVersion(activity);
					session.save(newActivityGroup);
					activity.setAmpActivityGroup(newActivityGroup);
					activity.setModifiedDate(Calendar.getInstance().getTime());
					activity.setModifiedBy(member);
					session.update(activity);
					// Add version info to old un-versioned activity.
					oldActivity.setAmpActivityGroup(newActivityGroup);
					session.update(oldActivity);
			}        
    
		      if (activity.getComponentProgress() != null) {
					Collection<AmpPhysicalPerformance> newProgress = activity.getComponentProgress();
					for (AmpPhysicalPerformance ampPhysicalPerformance : newProgress) {
						ampPhysicalPerformance.setAmpActivityId(activity);
						session.save(ampPhysicalPerformance);
					}
				} else {
					if (oldActivity.getComponentProgress() != null) {
						Collection<AmpPhysicalPerformance> oldProgress = oldActivity.getComponentProgress();
						for (AmpPhysicalPerformance ampPhysicalPerformance : oldProgress) {
							AmpPhysicalPerformance auxPP = new AmpPhysicalPerformance();
							auxPP.setAmpActivityId(activity);
							auxPP.setComments(ampPhysicalPerformance.getComments());
							// TODO: Evaluate if the creation of a new component
							// part is needed.
							AmpComponent auxComponent = new AmpComponent();
							// auxComponent.setActivities(new HashSet());
							// auxComponent.getActivities().add(activity);
							auxComponent.setCode(ampPhysicalPerformance.getComponent().getCode());
							auxComponent.setCreationdate(ampPhysicalPerformance.getComponent().getCreationdate());
							auxComponent.setDescription(ampPhysicalPerformance.getComponent().getDescription());
							auxComponent.setTitle(ampPhysicalPerformance.getComponent().getTitle());
							auxComponent.setType(ampPhysicalPerformance.getComponent().getType());
							auxComponent.setUrl(ampPhysicalPerformance.getComponent().getUrl());
							auxPP.setComponent(auxComponent);
							activity.getComponents().add(auxComponent);
							// If the upper code is disabled then enable the
							// following line.
							// auxPP.setComponent(ampPhysicalPerformance.getComponent());
							auxPP.setDescription(ampPhysicalPerformance.getDescription());
							auxPP.setLanguage(ampPhysicalPerformance.getLanguage());
							auxPP.setReportingDate(ampPhysicalPerformance.getReportingDate());
							auxPP.setReportingOrgId(ampPhysicalPerformance.getReportingOrgId());
							auxPP.setTitle(ampPhysicalPerformance.getTitle());
							auxPP.setType(ampPhysicalPerformance.getType());
							auxPP.setAmpActivityId(activity);
							// session.saveOrUpdate(activity);
							session.save(auxComponent);
							session.save(auxPP);
						}
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
       
       // Group different versions of the same funding.
       Iterator<AmpFunding> iterFunding = activity.getFunding().iterator();
       while(iterFunding.hasNext()) {
    	   AmpFunding auxFunding = iterFunding.next();
    	   if(auxFunding.getGroupVersionedFunding() == null) {
    		   auxFunding.setGroupVersionedFunding(auxFunding.getAmpFundingId());
    	   }
       }
       
//session.flush();
       if (alwaysRollback == false)
    	  //tx.commit(); // commit the transcation

       logger.debug("Activity saved"); 
       
       //Save surveys for new activities.
       //TODO: Check if this code can be removed, apparently the survey and their responses
       //are being saved ok.
       /*if (!edit) {
	       session = PersistenceManager.getRequestDBSession();
//beginTransaction();
	       AmpActivityVersion savedActivity = (AmpActivityVersion) session.load(AmpActivityVersion.class, activity.getAmpActivityId());
	       savedActivity.setSurvey(activity.getSurvey());
	       session.saveOrUpdate(savedActivity);
	       //tx.commit();
       }*/
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
    AmpActivityVersion oldActivity = null;

    try {
      session = PersistenceManager.getRequestDBSession();
//beginTransaction();

      oldActivity = (AmpActivityVersion) session.load(AmpActivityVersion.class, activityId);

      if (oldActivity == null) {
        logger.debug("Previous Activity is null");
        return;
      }

      oldActivity.setActivityCreator(creator);

      session.update(oldActivity);
      //tx.commit();
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
		AmpActivityVersion oldActivity = null;

		try {
			session = PersistenceManager.getRequestDBSession();
//beginTransaction();

			oldActivity = (AmpActivityVersion) session.load(AmpActivityVersion.class,
					activityId);

			if (oldActivity == null) {
				logger.debug("Previous Activity is null");
				return;
			}

			oldActivity.setDocuments(documents);

			session.update(oldActivity);
			//tx.commit();
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
  
  public static List<AmpComponent> getComponents(Long actId) {
    Session session = null;
    List<AmpComponent> col = new ArrayList<AmpComponent>();
    logger.info(" this is the other components getting called....");
    try {
      session = PersistenceManager.getRequestDBSession();
      String rewrittenColumns = SQLUtils.rewriteQuery("amp_components", "ac", 
    		  new HashMap<String, String>(){{
    			  put("title", InternationalizedModelDescription.getForProperty(AmpComponent.class, "title").getSQLFunctionCall("ac.amp_component_id"));
    			  put("description", InternationalizedModelDescription.getForProperty(AmpComponent.class, "description").getSQLFunctionCall("ac.amp_component_id"));
    		  }});      
      String queryString = "select " + rewrittenColumns + " from amp_components ac " +
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
  public static Collection<AmpActivityVersion> searchActivities(Long ampThemeId,
      String statusCode,
      String donorOrgId,
      Date fromDate,
      Date toDate,
      Long locationId,
      TeamMember teamMember,Integer pageStart,Integer rowCount) throws DgException{
    Collection<AmpActivityVersion> result = null;
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


      String oql = "select distinct  new  org.digijava.module.aim.helper.ActivityItem(latestAct,prog.programPercentage) " +
      		"	from " + AmpActivityProgram.class.getName() + " prog ";

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
	      String oql = "select count(distinct latestAct) from " + AmpActivityProgram.class.getName() + " prog ";
	      oql += getSearchActivitiesWhereClause(ampThemeId, statusCode, donorOrgId, fromDate, toDate, locationId, teamMember);
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
      oql+=" inner join act.ampActivityGroup grp";
      oql+=" inner join grp.ampActivityLastVersion latestAct";
      if (statusCode!=null && !"".equals(statusCode.trim())){
    	  oql+=" join  latestAct.categories as categories ";
      }
      StringBuilder whereTeamStatement=new StringBuilder();
      boolean relatedOrgsCriteria=false;
      if (teamMember != null) {
          //oql += " and " +getTeamMemberWhereClause(teamMember);
    	  AmpTeam team = TeamUtil.getAmpTeam(teamMember.getTeamId());
          if (teamMember.getComputation()!=null&&teamMember.getComputation()) {
              String ids = DashboardUtil.getComputationOrgsQry(team);
              if(ids.length()>1){
              ids = ids.substring(0, ids.length() - 1);
              	whereTeamStatement.append("  and ( latestAct.team.ampTeamId =:teamId or  role.organisation.ampOrgId in(" + ids+"))");
              }
              relatedOrgsCriteria=true;
          }
          else{
				if (team.getAccessType().equals("Management")) {
					whereTeamStatement.append(String.format(" and (latestAct.draft=false or latestAct.draft is null) and latestAct.approvalStatus IN ('%s', '%s') ", Constants.APPROVED_STATUS, Constants.STARTED_APPROVED_STATUS));
					List<AmpTeam> teams = new ArrayList<AmpTeam>();
					DashboardUtil.getTeams(team, teams);
					String relatedOrgs = "", teamIds = "";
					for (AmpTeam tm : teams) {
						if (tm.getComputation() != null && tm.getComputation()) {
							relatedOrgs += DashboardUtil
									.getComputationOrgsQry(tm);
							 relatedOrgsCriteria=true;
						}
						teamIds += tm.getAmpTeamId() + ",";
					}
					if (relatedOrgs.length() > 1) {
						relatedOrgs = relatedOrgs.substring(0,
								relatedOrgs.length() - 1);
						whereTeamStatement.append("  and ( latestAct.team.ampTeamId ="+team.getAmpTeamId()+" or  role.organisation.ampOrgId in("
								+ relatedOrgs + "))");
					}
					if (teamIds.length() > 1) {
						teamIds = teamIds.substring(0, teamIds.length() - 1);
						whereTeamStatement.append(" and latestAct.team.ampTeamId in ( " + teamIds
								+ ")");
					}

				} else {
					whereTeamStatement.append(" and ( latestAct.team.ampTeamId =:teamId ) ");
          }
          }
        
      }
      if(relatedOrgsCriteria){
          oql+=" inner join latestAct.orgrole role ";
      }
      oql+=" where 1=1 ";
      if (ampThemeId != null) {
          oql += " and ( theme.ampThemeId = :ampThemeId) ";
        }
      if (donorOrgId != null&&!donorOrgId.trim().equals("")) {
        String s = " and latestAct in (select f.ampActivityId from " +
             AmpFunding.class.getName() + " f inner join f.ampDonorOrgId org " +
            " where org.ampOrgId in ("+donorOrgId+")) ";
        oql += s;
      }
      if (statusCode != null&&!"".equals(statusCode.trim())) {
        oql += " and categories.id in ("+statusCode+") ";
      }
      if (fromDate != null) {
        oql += " and (latestAct.actualStartDate >= :FromDate or (latestAct.actualStartDate is null and latestAct.proposedStartDate >= :FromDate) )";
      }
      if (toDate != null) {
        oql += " and (latestAct.actualStartDate <= :ToDate or (latestAct.actualStartDate is null and latestAct.proposedStartDate <= :ToDate) ) ";
      }
      if (locationId != null) {
        oql += " and latestAct.locations in (from " + AmpLocation.class.getName() +" loc where loc.id=:LocationID)";
      }
      oql+=whereTeamStatement.toString();
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
        if (teamMember!=null && teamMember.getTeamId()!=null&&!teamMember.getTeamAccessType().equals("Management")){
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

      AmpActivityVersion act = (AmpActivityVersion) session.load(AmpActivityVersion.class, actId);

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
  public static AmpActivityVersion loadActivity(Long id) throws DgException {
		AmpActivityVersion result = null;
		Session session = PersistenceManager.getRequestDBSession();
		try {
//session.flush();
			result = (AmpActivityVersion) session.get(AmpActivityVersion.class, id);
			session.evict(result);
			result = (AmpActivityVersion) session.get(AmpActivityVersion.class, id);
			Hibernate.initialize(result.getCosts());
			Hibernate.initialize(result.getInternalIds());
			Hibernate.initialize(result.getLocations());
			Hibernate.initialize(result.getSectors());
			Hibernate.initialize(result.getFunding());
			if(result.getFunding()!=null){
				for(Object obj:result.getFunding()){
					AmpFunding funding=(AmpFunding)obj;
					Hibernate.initialize(funding.getFundingDetails());
					Hibernate.initialize(funding.getMtefProjections());
				}
			}
			Hibernate.initialize(result.getActivityDocuments());
			Hibernate.initialize(result.getComponents());
			Hibernate.initialize(result.getOrgrole());
			Hibernate.initialize(result.getIssues());
			Hibernate.initialize(result.getRegionalObservations());
			Hibernate.initialize(result.getStructures());
		} catch (ObjectNotFoundException e) {
			logger.debug("AmpActivityVersion with id=" + id + " not found");
		} catch (Exception e) {
			throw new DgException("Cannot load AmpActivityVersion with id " + id, e);
		}
		return result;
	}
  
  public static AmpActivityVersion loadAmpActivity(Long id){
	  try{
		 return (AmpActivityVersion) PersistenceManager.getRequestDBSession().load(AmpActivityVersion.class.getName(), id); 
	  }
	  catch (Exception e){
		  throw new RuntimeException(e);
	  }
  }
  
  public static AmpActivityVersion getAmpActivityVersion(Long id) {
		// TODO: This is a mess, shouldn't be here. Check where it is used and
		// change it.

		Session session = null;
		AmpActivityVersion activity = null;

		try {
			session = PersistenceManager.getRequestDBSession();

			activity = (AmpActivityVersion) session.load(AmpActivityVersion.class, id);
		} catch (Exception e) {
			logger.error("Unable to getAmpActivity");
			e.printStackTrace(System.out);
		}
		return activity;
	}

  public static AmpActivityVersion getChannelOverview(Long actId) {
    Session session = null;
    AmpActivityVersion activity = null;

    try {
      session = PersistenceManager.getSession();
      activity = (AmpActivityVersion) session.load( AmpActivityVersion.class, actId);
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
      String queryString = "select a from " + AmpActivityVersion.class.getName() +
          " a " + "where (a.ampActivityId=:actId)";
      Query qry = session.createQuery(queryString);
      qry.setParameter("actId", actId, Hibernate.LONG);
      Iterator itr = qry.list().iterator();
      if (itr.hasNext()) {
        AmpActivityVersion act = (AmpActivityVersion) itr.next();
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
	      String rewrittenColumns = SQLUtils.rewriteQuery("amp_organisation", "ao", 
	    		  new HashMap<String, String>(){{
	    			  put("name", InternationalizedModelDescription.getForProperty(AmpOrganisation.class, "name").getSQLFunctionCall("ao.amp_org_id"));
	    			  put("description", InternationalizedModelDescription.getForProperty(AmpOrganisation.class, "description").getSQLFunctionCall("ao.amp_org_id"));
	    		  	}});
	      String queryString = "select " + rewrittenColumns + " from amp_organisation ao " +	      		"inner join amp_org_role aor on (aor.organisation = ao.amp_org_id) " +
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
  
  public static int getFundingByOrgCount(Long id) {
	    Session session = null;
	    int orgrolesCount = 0;
	    try {
	      session = PersistenceManager.getSession();
	      String queryString = "select count(*) from " + AmpFunding.class.getName() +" f, "
	    		  + AmpActivity.class.getName()	+ " a "
	    		  + "where f.ampActivityId=a.ampActivityId and (f.ampDonorOrgId=:orgId)";
	      Query qry = session.createQuery(queryString);
	      qry.setParameter("orgId", id, Hibernate.LONG);
	      orgrolesCount = (Integer)qry.uniqueResult();
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
	    return orgrolesCount;
	  }

  public static Collection<Components> getAllComponents(Long id) {
    Collection<Components> componentsCollection = new ArrayList<Components>();

    Session session = null;

    try {
      session = PersistenceManager.getSession();
      AmpActivityVersion activity = (AmpActivityVersion) session.load(AmpActivityVersion.class, id);
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
            fd.setAdjustmentTypeName(cf.getAdjustmentType());
 
            fd.setCurrencyCode(cf.getCurrency().getCurrencyCode());
            fd.setCurrencyName(cf.getCurrency().getCurrencyName());
            fd.setTransactionAmount(FormatHelper.formatNumber(cf.getTransactionAmount().doubleValue()));
            fd.setTransactionDate(DateConversion.ConvertDateToString(cf.getTransactionDate()));
			fd.setFiscalYear(DateConversion.convertDateToFiscalYearString(cf.getTransactionDate()));
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
  
public static Collection<AmpActivityVersion> getOldActivities(Session session,int size,Date date){
		Collection colGr = null;
		Collection colAv = null;
		Collection<AmpActivityVersion> colAll = new ArrayList<AmpActivityVersion>();
		logger.info(" inside getting the old activities.....");
		try {

			List result = session.createSQLQuery("Select * from ( select amp_activity_id, amp_activity_group_id, date_updated, rank() over (PARTITION BY amp_activity_group_id order by date_updated desc) as rank from amp_activity_version order by amp_activity_group_id) as SQ where sq.rank > "+size).list();
			Iterator iter = result.iterator();
			List<Long> idActivities = new ArrayList<Long>();
			while(iter.hasNext()){
				Object[] objects = (Object[]) iter.next();
			     BigInteger id = (BigInteger) objects[0];
			     idActivities.add(id.longValue());
			}
			if(idActivities.size()>0){
				String qryGroups = "select av from "
						+ AmpActivityVersion.class.getName()+" av where av.ampActivityId in:list";
				Query qry = session.createQuery(qryGroups);
				qry.setParameterList("list", idActivities);
				colGr = qry.list();
				System.out.println(result.size());
				colAv = qry.list();
				if (colAv != null && !colAv.isEmpty()) {
					Iterator itrAv = colAv.iterator();
					while (itrAv.hasNext()) {
						AmpActivityVersion act = (AmpActivityVersion) itrAv
								.next();
						if (act.getUpdatedDate().before(date))
							colAll.add(act);
					}
				}
			}

		} catch (Exception e) {
			logger.debug("Exception in getOldActivities() " + e.getMessage());
		}
		return colAll;
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
			AmpActivityVersion activity = (AmpActivityVersion) session.load(AmpActivityVersion.class, id);
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
      AmpActivityVersion activity = (AmpActivityVersion) session.load(AmpActivityVersion.class, id);
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
              measure.setMeasureDate(FormatHelper.formatDate(ampMeasure.getMeasureDate()));
                 
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
      AmpActivityVersion activity = (AmpActivityVersion) session.load(AmpActivityVersion.class, id);
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
      AmpActivityVersion activity = (AmpActivityVersion) session.load(AmpActivityVersion.class, id);
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

  public static AmpActivity getActivityByNameExcludingGroup(String name , AmpActivityGroup g) {
	  
		Session session=null;
		try {
			session = PersistenceManager.getRequestDBSession();
		} catch (DgException e) {
			logger.error(e);
			e.printStackTrace();
		}
		
	  Criteria crit = session.createCriteria(AmpActivity.class);
	  
	  Conjunction conjunction = Restrictions.conjunction();
	  String locale = TLSUtils.getLangCode();
	  conjunction.add(SQLUtils.getUnaccentILikeExpression("name", name, locale, MatchMode.EXACT));
	  if(g!=null) conjunction.add(Restrictions.not(Restrictions.eq("ampActivityGroup",g)));
	  crit.add(conjunction);
	  
	  List ret = crit.list();
	  if(ret.size()>0) return (AmpActivity) ret.get(0);
				
	  return null;
  }

  
//  public static AmpActivity getActivityByName(String name , Long actId) {
//    AmpActivity activity = null;
//    Session session = null;
//    try {
//      session = PersistenceManager.getSession();
//      String qryStr = "select a from " + AmpActivity.class.getName() + " a " +
//    		  String.format("where lower(%s) = :lowerName",
//    				  AmpActivityVersion.hqlStringForName("a"));      
//      if(actId!=null){
//    	  qryStr+=" and a.ampActivityId!="+actId;
//      }
//      Query qry = session.createQuery(qryStr);
//      qry.setString("lowerName", name.toLowerCase());
//      Iterator itr = qry.list().iterator();
//      if (itr.hasNext()) {
//        activity = (AmpActivity) itr.next();
//      }
//    }
//    catch (Exception e) {
//      logger.debug("Exception in isActivityExisting() " + e.getMessage());
//      e.printStackTrace(System.out);
//    }
//    finally {
//      if (session != null) {
//        try {
//          PersistenceManager.releaseSession(session);
//        }
//        catch (Exception ex) {
//          logger.debug("Exception while releasing session " + ex.getMessage());
//        }
//      }
//    }
//    return activity;
//  }

  public static void saveDonorFundingInfo(Long actId, Set fundings) {
    Session session = null;
    Transaction tx = null;

    try {
      session = PersistenceManager.getSession();
//beginTransaction();

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
      //tx.commit();
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


    public static StringBuilder getDonorsForActivity(Long activityId) {
        StringBuilder donors = new StringBuilder();
        if (activityId != null) {
            Session session = PersistenceManager.getSession();
            String queryString = "select distinct donor from " + AmpFunding.class.getName() + " f inner join f.ampDonorOrgId donor inner join f.ampActivityId act ";
            queryString += " where act.ampActivityId=:activityId";
            Query qry = session.createQuery(queryString);
            qry.setLong("activityId", activityId);

            List<AmpOrganisation> organizations = qry.list();
            if (organizations != null && organizations.size() > 1) {
                Collections.sort(organizations, new Comparator<AmpOrganisation>() {
                    public int compare(AmpOrganisation o1, AmpOrganisation o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });
            }

            if (organizations != null) {
                for (AmpOrganisation donor : organizations) {
                    donors.append(donor.getName());
                    donors.append(", ");
                }

                if (donors.length() > 1) {
                    // remove last coma
                    donors.setLength(donors.length() - 2);
                }
            }

        }
        return donors;
    }

    public static List getSortedActivitiesByDonors (List<AmpActivityVersion> acts, boolean acs) {
        List<AmpActivityVersion> retVal = new ArrayList<AmpActivityVersion>();

        Map <String, AmpActivityVersion> donorNameActivityMap = new HashMap <String, AmpActivityVersion> ();
        List <AmpActivityVersion> noFundingActivities = null;
        for (AmpActivityVersion actItem : acts) {
            if (actItem.getFunding() != null && !actItem.getFunding().isEmpty()) {
                /*
                Set <String> nameSorter = new HashSet<String>();
                for (Object fndObj : actItem.getFunding()) {
                    AmpFunding fnd = (AmpFunding) fndObj;
                    nameSorter.add(fnd.getAmpDonorOrgId().getName());
                }
                List <String> sortedList = new ArrayList<String>(nameSorter);
                Collections.sort(sortedList);*/
                //donorNameActivityMap.put(sortedList.get(0), actItem);
                //donorNameActivityMap.put(((AmpFunding)actItem.getFunding().iterator().next()).getAmpDonorOrgId().getName(), actItem);
                
                StringBuilder donorNames = new StringBuilder();
                
                List organizations = new ArrayList(actItem.getFunding());
                if (organizations != null && organizations.size() > 1) {
                    Collections.sort(organizations, new Comparator<AmpFunding>() {
                        public int compare(AmpFunding o1, AmpFunding o2) {
                            return o1.getAmpDonorOrgId().getName().compareTo(o2.getAmpDonorOrgId().getName());
                        }
                    });
                }

                for (Object fndObj : organizations) {
                    AmpFunding fnd = (AmpFunding) fndObj;
                    donorNames.append(fnd.getAmpDonorOrgId().getName());
                    donorNames.append(",");
                }
                donorNameActivityMap.put(donorNames.toString(), actItem);

            } else {
                if (noFundingActivities == null) {
                    noFundingActivities = new ArrayList <AmpActivityVersion> ();
                }
                noFundingActivities.add(actItem);
            }
        }
        
        Set <String> keys = donorNameActivityMap.keySet();
        List <String> sortedKeys = new ArrayList <String> (keys);
        Collections.sort(sortedKeys);
        if (!acs) {
            Collections.reverse(sortedKeys);
        }
        
        for (String key : sortedKeys) {
            retVal.add(donorNameActivityMap.get(key));
        }

        if (noFundingActivities != null) {
            retVal.addAll(noFundingActivities);
        }
        
        return retVal;
    }

  public static Collection getDonors(Long actId) {
    Collection col = new ArrayList();
    Session session = null;
    try {
      session = PersistenceManager.getSession();
      AmpActivityVersion act = (AmpActivityVersion) session.load(AmpActivityVersion.class, actId);
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
          + AmpActivityVersion.class.getName() + " act";
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

  public static AmpActivityVersion getProjectChannelOverview(Long id) {
    Session session = null;
    AmpActivityVersion activity = null;

    try {
      logger.debug("Id is " + id);
      session = PersistenceManager.getRequestDBSession();

      // modified by Priyajith
      // Desc: removed the usage of session.load and used the select query
      // start
      String queryString = "select a from " + AmpActivityVersion.class.getName()
          + " a " + "where (a.ampActivityId=:id)";
      Query qry = session.createQuery(queryString);
      qry.setLong("id", id);
      activity =(AmpActivityVersion)qry.uniqueResult();
      // end
    }
    catch (Exception ex) {
      logger
          .error("Unable to get Amp Activity getProjectChannelOverview() :"
                 + ex);
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

  	public static List<AmpActivityFake> getLastUpdatedActivities() {
 		String workspaceQuery = Util.toCSStringForIN(org.digijava.module.gis.util.DbUtil.getAllLegalAmpActivityIds());
  		
 		List<AmpActivityFake> res = new ArrayList<AmpActivityFake>();
		Session session = null;
		Query qry = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select ampAct.ampActivityId, ampAct.ampId, " + AmpActivityVersion.hqlStringForName("ampAct") + " from "
				+ AmpActivityVersion.class.getName()
				+ " ampAct where ampAct.ampActivityId IN (" + workspaceQuery + ")"
				+ " AND (ampAct.deleted = false or ampAct.deleted is null) AND (ampAct.team.id IS NOT NULL) "
				+ " order by ampAct.ampActivityId desc";
			qry = session.createQuery(queryString).setMaxResults(5);
			List<Object[]> results = qry.list();
			for(Object[] activityInfo:results)
			{
				AmpActivityFake activityDigest = new AmpActivityFake(PersistenceManager.getString(activityInfo[2]), PersistenceManager.getString(activityInfo[1]), PersistenceManager.getLong(activityInfo[0]));
				res.add(activityDigest);
			}
		} catch (Exception e1) {
			logger.error("Could not retrieve the activities list from getLastUpdatedActivities", e1);
		}
		return res;
	}
  
  /*
   * get the list of all the activities
   * to display in the activity manager of Admin
   */
  public static List<AmpActivityVersion> getAllActivitiesList() {
    List col = null;
    Session session = null;
    Query qry = null;

    try {
      session = PersistenceManager.getRequestDBSession();
      String queryString = "select ampAct from " + AmpActivityVersion.class.getName() +
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
  
  public static List<AmpActivityVersion> getAllAssignedActivitiesList() {
	    List col = null;
	    Session session = null;
	    Query qry = null;

	    try {
	      session = PersistenceManager.getRequestDBSession();
	      String queryString = "select ampAct from " + AmpActivityVersion.class.getName() + " ampAct where ampAct.team is not null";
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
  public static List<AmpActivityVersion> getAllActivitiesByName(String name) {
    List col = null;
    Session session = null;
    Query qry = null;

    try {
      session = PersistenceManager.getSession();
      String queryString = "select ampAct from " + AmpActivityVersion.class.getName() +
    		String.format(" ampAct where upper(%s) like upper(:name)",
    		AmpActivityVersion.hqlStringForName("ampAct"));
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
  
  public static List <AmpActivityGroup> getActivityGroups(Session session , Long actId){
	  List <AmpActivityGroup> retVal=null;
	  //Session session = null;
	  Query qry = null;
	  try {
		  //session = PersistenceManager.getRequestDBSession();	      
	      String queryString ="select group from "+ AmpActivityGroup.class.getName()+" group where group.ampActivityLastVersion.ampActivityId="+actId;
	      qry = session.createQuery(queryString);
	      retVal = qry.list();
	} catch (Exception e) {
		logger.error("Could not retrieve groups list");
	    e.printStackTrace(System.out);
	}
	  return retVal;
  }

  /* functions to DELETE an activity by Admin start here.... */
  public static void deleteActivity(Long ampActId) {
	  Date startDate = new Date(System.currentTimeMillis());
    Session session = null;
    Transaction tx = null;

    try {
      session = PersistenceManager.getSession();
//beginTransaction();

      AmpActivityVersion ampAct = (AmpActivityVersion) session.load(AmpActivityVersion.class, ampActId);

      if (ampAct == null)
			logger.debug("Activity is null. Hence no activity with id : " + ampActId);
		else {
			// Delete access info.
			
//			Query qry = session.createSQLQuery("DELETE FROM amp_activity_access WHERE amp_activity_id = ?");
//			qry.setParameter(0, ampActId);
//			qry.executeUpdate();

			// Delete group info.
			AmpActivityGroup auxGroup = ampAct.getAmpActivityGroup();
			Query qry = session.createQuery("UPDATE " + AmpActivityVersion.class.getName()+ " SET ampActivityPreviousVersion = NULL WHERE ampActivityPreviousVersion = " + ampActId);
			qry.executeUpdate();
			ampAct.setAmpActivityGroup(null);
			
			
			session.update(ampAct);
	    	auxGroup.getActivities().remove(ampAct);
	    	session.update(auxGroup);
	    	session.update(ampAct);
	    	
	    	// Delete group info.
	      	List<AmpActivityGroup> groups=getActivityGroups(session , ampAct.getAmpActivityId());
	      	if(groups!=null && groups.size()>0){
	      		for (AmpActivityGroup ampActivityGroup : groups) {
	      			Set<AmpActivityVersion> activityversions=ampActivityGroup.getActivities();
	      			if(activityversions!=null && activityversions.size()>0){
	      				for (Iterator<AmpActivityVersion> iterator = activityversions.iterator(); iterator.hasNext();) {
	      					AmpActivityVersion ampActivityVersion=iterator.next();
	      					ampActivityVersion.setAmpActivityGroup(null);
	      					session.update(ampActivityVersion);
						}
	      			}
	  				session.delete(ampActivityGroup);
	  			}
	      	}
			
			
			// TODO: relink ampActivityPreviousVersion if needed (when
			// deleting drafts to older non-draft versions).
          
	      	deleteActivityContent(ampAct,session);
		}
      

    
    ActivityUtil.deleteActivityPhysicalComponentReport(DbUtil.getActivityPhysicalComponentReport(ampActId), session);
  	ActivityUtil.deleteActivityAmpReportCache(DbUtil.getActivityReportCache(ampActId), session);
  	ActivityUtil.deleteActivityReportLocation(DbUtil.getActivityReportLocation(ampActId), session);
  	ActivityUtil.deleteActivityReportPhyPerformance(DbUtil.getActivityRepPhyPerformance(ampActId), session);
  	ActivityUtil.deleteActivityReportSector(DbUtil.getActivityReportSector(ampActId), session);
  	//This is not deleting AmpMEIndicators, just indicators, ME is deprecated.
  	ActivityUtil.deleteActivityIndicators(DbUtil.getActivityMEIndValue(ampActId), ampAct, session);

      
	  session.delete(ampAct);
      //tx.commit();
//session.flush();
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
    logger.warn(new Date(System.currentTimeMillis()).getTime() - startDate.getTime());
  }
    
    public static void deleteActivityContent(AmpActivityVersion ampAct, Session session) throws Exception{
        /* delete fundings and funding details */
        /*Set fundSet = ampAct.getFunding();
        if (fundSet != null) {
          Iterator fundSetItr = fundSet.iterator();
          while (fundSetItr.hasNext()) {
            AmpFunding fund = (AmpFunding) fundSetItr.next();
            Set fundDetSet = fund.getFundingDetails();
            if (fundDetSet != null) {
              Iterator fundDetItr = fundDetSet.iterator();
              while (fundDetItr.hasNext()) {
                AmpFundingDetail ampFundingDetail = (AmpFundingDetail)fundDetItr.next();
                if(ampFundingDetail.getContract()!=null) 
                	session.delete(ampFundingDetail.getContract());
                //fundDet
                session.delete(ampFundingDetail);
              }
            }
            
            session.delete(fund);
          }
        }*/

        /*Set contracts=ampAct.getContracts();
        if(contracts!=null){
        	for (Iterator it = contracts.iterator(); it.hasNext();) {
				IPAContract c = (IPAContract) it.next();
				session.delete(c);
			}
        }*/
        
        /* delete regional fundings */
        /*fundSet = ampAct.getRegionalFundings();
        if (fundSet != null) {
          Iterator fundSetItr = fundSet.iterator();
          while (fundSetItr.hasNext()) {
            AmpRegionalFunding regFund = (AmpRegionalFunding) fundSetItr.next();
            session.delete(regFund);
          }
        }*/
        /* delete components */
       /* Set comp = ampAct.getComponents();
        if (comp != null) {
          Iterator compItr = comp.iterator();
          while (compItr.hasNext()) {
            AmpComponent ampComp = (AmpComponent) compItr.next();
            ampComp.getActivities().remove(ampAct);           
            session.delete(ampComp);
          }
          //Set<AmpComponent> test = new HashSet<AmpComponent>();
          //ampAct.setComponents(null);
          ampAct.getComponents().clear();
        }
        */

        /* delete Component Fundings */
       /* Collection<AmpComponentFunding>  componentFundingCol = ampAct.getComponentFundings();
        if (componentFundingCol != null) {
  			Iterator<AmpComponentFunding> componentFundingColIt = componentFundingCol.iterator();
  			while (componentFundingColIt.hasNext()) {
  				session.delete(componentFundingColIt.next());
  			}
	  	}*/

        /* delete org roles */
        /*Set orgrole = ampAct.getOrgrole();
        if (orgrole != null) {
          Iterator orgroleItr = orgrole.iterator();
          while (orgroleItr.hasNext()) {
            AmpOrgRole ampOrgrole = (AmpOrgRole) orgroleItr.next();
            session.delete(ampOrgrole);
          }
        }*/

				/* delete issues,measures,actors */
				/*Set issues = ampAct.getIssues();
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
				}*/
        
        /* delete observations,measures,actors */
				/*Set observations = ampAct.getRegionalObservations();
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
				}*/
        // delete all previous sectors
       /* Set sectors = ampAct.getSectors();
        if (sectors != null) {
          Iterator iItr = sectors.iterator();
          while (iItr.hasNext()) {
            AmpActivitySector sec = (AmpActivitySector) iItr.next();
            ampAct.getSectors().remove(sec);
            session.delete(sec);
            iItr = sectors.iterator();
          }
        }*/
       
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
                ahSurvey.getResponses().remove(surveyResp);
                session.delete(surveyResp);
                session.flush();
                ahSurveyItr = ahSurvey.getResponses().iterator();
              }
            }
            ampAct.getSurvey().remove(ahSurvey);
            session.delete(ahSurvey);
            session.flush();
            surveyItr = ampAct.getSurvey().iterator();
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
        String deleteActivityComments = "DELETE FROM amp_comments WHERE amp_activity_id = " + ampAct.getAmpActivityId();
        Connection con = session.connection();
        Statement stmt = con.createStatement();
        stmt.executeUpdate(deleteActivityComments);
        logger.info("comments deleted");
        
        //Delete the connection with Team.
        String deleteActivityTeam = "DELETE FROM amp_team_activities WHERE amp_activity_id = " + ampAct.getAmpActivityId();
         con = session.connection();
         stmt = con.createStatement();
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

      
      
    //Section moved here from ActivityManager.java because it didn't worked there.
	//ActivityUtil.deleteActivityAmpComments(DbUtil.getActivityAmpComments(ampActId), session);
    
    }
    
	public static void removeMergeSources(Long ampActivityId,Session session){
		String queryString1 = "select act from " + AmpActivityVersion.class.getName() + " act where (act.mergeSource1=:activityId)";
		String queryString2 = "select act from " + AmpActivityVersion.class.getName() + " act where (act.mergeSource2=:activityId)";
		Query qry1 = session.createQuery(queryString1);
		Query qry2 = session.createQuery(queryString2);
		qry1.setParameter("activityId", ampActivityId, Hibernate.LONG);
		qry2.setParameter("activityId", ampActivityId, Hibernate.LONG);
		
		Collection col =qry1.list();
		if (col != null && col.size() > 0) {
			Iterator<AmpActivityVersion> itrAmp = col.iterator();
			while(itrAmp.hasNext()){
				AmpActivityVersion actVersion = itrAmp.next();
				actVersion.setMergeSource1(null);
				session.update(actVersion);
			}
		}
		col =qry2.list();
		if (col != null && col.size() > 0) {
			Iterator<AmpActivityVersion> itrAmp = col.iterator();
			while(itrAmp.hasNext()){
				AmpActivityVersion actVersion = itrAmp.next();
				actVersion.setMergeSource2(null);
				session.update(actVersion);
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
          ampComment.setAmpActivityId(null);
          session.delete(ampComment);
        }
     }
  }
  
  public static void deleteActivityPhysicalComponentReportSession(Long ampActId, Session session)throws Exception{
	  Collection col = null;
      Query qry = null;
      String queryString = "select phyCompReport from "
          + AmpPhysicalComponentReport.class.getName()
          + " phyCompReport "
          + " where (phyCompReport.ampActivityId=:ampActId)";
      qry = session.createQuery(queryString);
      qry.setParameter("ampActId", ampActId, Hibernate.LONG);
      col = qry.list();
      
      Iterator itrCompRep = col.iterator();
      while(itrCompRep.hasNext()){
    	  AmpPhysicalComponentReport compRep = (AmpPhysicalComponentReport)itrCompRep.next();
    	  session.delete(compRep);
      }
  }
  
  /**
   * @deprecated
   * VERY SLOW. Use {@link #getAllTeamAmpActivitiesResume(Long, boolean, String, String...)}
   * @param ampActIds
   * @param session
   * @return
   */
  public static List<AmpActivity> getActivityById(Set<Long> ampActIds ,Session session){
	  List<AmpActivity> act = null;
      if (ampActIds != null && !ampActIds.isEmpty()) {
	  try {
	      Query qry = null;
	      String queryString = "select a from "
	          + AmpActivity.class.getName()
	          + " a where a.ampActivityId in(:ampActIds) ";
	          //+ " where (phyCompReport.ampActivityId=:ampActId)";
	      qry = session.createQuery(queryString);
	      qry.setParameterList("ampActIds", ampActIds);
	      qry.setCacheable(false);
	      act = qry.list();
	  }
	    catch (Exception e1) {
	      logger.error("Could not retrieve the activities ");
	      e1.printStackTrace(System.out);
	    }
      } else {
          act = new ArrayList<AmpActivity> ();
      }
      return act;
  }

//  DbUtil.getActivityPhysicalComponentReport(ampActId), session
  
  public static void deleteActivitySectors(Long ampActId, Session session) {
		Collection col = null;
		Query qry = null;
		String queryString = "select amp_activity_id from "
				+ AmpActivitySector.class.getName() + " actSector "
				+ " where (actSector.ampActivityId=:ampActId)";
		qry = session.createQuery(queryString);
		qry.setParameter("ampActId", ampActId, Hibernate.LONG);
		col = qry.list();

		Iterator itr = col.iterator();
		while (itr.hasNext()) {
			AmpActivitySector actSector = (AmpActivitySector) itr.next();
			session.delete(actSector);
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
  
  public static void deleteActivityAmpReportCacheSession(Long ampActId, Session session) throws Exception {
		Collection col = null;
		Query qry = null;
		String queryString = "select repCache from "
				+ AmpReportCache.class.getName() + " repCache "
				+ " where (repCache.ampActivityId=:ampActId)";
		qry = session.createQuery(queryString);
		qry.setParameter("ampActId", ampActId, Hibernate.LONG);
		col = qry.list();

		Iterator itr = col.iterator();
		while (itr.hasNext()) {
			AmpReportCache repCache = (AmpReportCache) itr.next();
			session.delete(repCache);
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
  
  public static void deleteActivityReportLocationSession(Long ampActId, Session session) throws Exception {
		Collection col = null;
		Query qry = null;
		String queryString = "select repLoc from "
				+ AmpReportLocation.class.getName() + " repLoc "
				+ " where (repLoc.ampActivityId=:ampActId)";
		qry = session.createQuery(queryString);
		qry.setParameter("ampActId", ampActId, Hibernate.LONG);
		col = qry.list();
		
		Iterator itr = col.iterator();
		while (itr.hasNext()) {
			AmpReportLocation repLoc = (AmpReportLocation) itr.next();
			session.delete(repLoc);
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

  public static void deleteActivityReportPhyPerformanceSession(Long ampActId, Session session) throws Exception {
		Collection col = null;
		Query qry = null;
		String queryString = "select phyPer from "
				+ AmpReportPhysicalPerformance.class.getName() + " phyPer "
				+ " where (phyPer.ampActivityId=:ampActId)";
		qry = session.createQuery(queryString);
		qry.setParameter("ampActId", ampActId, Hibernate.LONG);
		col = qry.list();
		
		Iterator itr = col.iterator();
		while (itr.hasNext()) {
			AmpReportPhysicalPerformance repPhy = (AmpReportPhysicalPerformance) itr.next();
			session.delete(repPhy);
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
  
  public static void deleteActivityReportSectorSession(Long ampActId, Session session) throws Exception {
		Collection col = null;
		Query qry = null;
		String queryString = "select repSector from "
				+ AmpReportSector.class.getName() + " repSector "
				+ " where (repSector.ampActivityId=:ampActId)";
		qry = session.createQuery(queryString);
		qry.setParameter("ampActId", ampActId, Hibernate.LONG);
		col = qry.list();
		
		Iterator itr = col.iterator();
		while (itr.hasNext()) {
			AmpReportSector repSec = (AmpReportSector) itr.next();
			session.delete(repSec);
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
  
  public static void deleteActivityIndicatorsSession(Long ampActivityId,Session session) throws Exception{
		Collection col = null;
		Query qry = null;
		String queryString = "select indAct from "
				+ IndicatorActivity.class.getName() + " indAct "
				+ " where (indAct.activity=:ampActId)";
		qry = session.createQuery(queryString);
		qry.setParameter("ampActId", ampActivityId, Hibernate.LONG);
		col = qry.list();
		
		Iterator itrIndAct = col.iterator();
		while(itrIndAct.hasNext()){
			IndicatorActivity indAct =(IndicatorActivity)itrIndAct.next();
			session.delete(indAct);
		}
	  
  }

  public static void deleteActivityIndicators(Collection activityInd, AmpActivityVersion activity, Session session) throws Exception {
    
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

  public static ActivityAmounts getActivityAmmountIn(AmpActivityVersion act,
      String tocode,Float percent, boolean donorFundingOnly) throws Exception {
    double tempProposed = 0;
    double tempActual = 0;
    double tempPlanned = 0;
    ActivityAmounts result = new ActivityAmounts();
    percent=(percent==null)?100:percent;

    AmpCategoryValue statusValue = CategoryManagerUtil.
        getAmpCategoryValueFromListByKey(CategoryConstants.ACTIVITY_STATUS_KEY,act.getCategories());

    if (act != null && statusValue != null) {
      if (CategoryConstants.ACTIVITY_STATUS_PROPOSED.equalsCategoryValue(statusValue) && act.getFunAmount() != null) {
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

          Set<AmpFunding> fundings = act.getFunding();
          if (fundings != null) {
              Iterator<AmpFunding> fundItr = act.getFunding().iterator();
              while(fundItr.hasNext()) {
                  AmpFunding ampFunding = fundItr.next();
                  org.digijava.module.aim.logic.FundingCalculationsHelper calculations = new org.digijava.module.aim.logic.FundingCalculationsHelper();
                  calculations.doCalculations(ampFunding, tocode);
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
            AmpActivityVersion act1 = (AmpActivityVersion) obj1;
            AmpActivityVersion act2 = (AmpActivityVersion) obj2;
            return (act1.getName()!=null && act2.getName()!=null)?act1.getName().compareTo(act2.getName()):0; 
        }
    }

  /**
   * Comparator for AmpActivityVersion class.
   * Compears activities by its ID's.
   * AmpActivityVersion is comparable itself, but it is comparable by names,
   * so this class was created to compeare them with ID's
   * @see AmpActivityVersion#compareTo(AmpActivityVersion)
   */
  public static class ActivityIdComparator
      implements Comparator<AmpActivityVersion> {

    public int compare(AmpActivityVersion act1, AmpActivityVersion act2) {
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
	            String queryStr	= "SELECT a FROM " + AmpActivityVersion.class.getName()  + " a left join a.member m WHERE " +
	            			"(a.activityCreator=:atmId) OR (a.modifiedBy=:atmId) OR (a.approvedBy = :atmId) OR (m.ampTeamMemId = :atmId)  OR (a.modifiedBy = :atmId)";
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
	
	public static String collectionToCSV(Collection<AmpActivityVersion> activities) {
		if ( activities == null )
			return null;
		String ret	= "";
		Iterator<AmpActivityVersion> iter	= activities.iterator();
		while ( iter.hasNext() ) {
			AmpActivityVersion activity	= iter.next();
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
//            Long templId=FeaturesUtil.getGlobalSettingValueLong(GlobalSettingsConstants.VISIBILITY_TEMPLATE);
//            AmpFeaturesVisibility step1=FeaturesUtil.getFeatureByName("Identification", "Project ID and Planning", templId);// step 1
//            AmpFeaturesVisibility step1_1=FeaturesUtil.getFeatureByName("Planning", "Project ID and Planning", templId); // step 1
//            AmpModulesVisibility step2=FeaturesUtil.getModuleByName("References", "PROJECT MANAGEMENT", templId);
//            AmpFeaturesVisibility step3=FeaturesUtil.getFeatureByName("Location", "Project ID and Planning", templId);
//            AmpFeaturesVisibility step3_2=FeaturesUtil.getFeatureByName("Sectors", "Project ID and Planning", templId);
//            AmpModulesVisibility step3_3=FeaturesUtil.getModuleByName("National Planning Dashboard", "NATIONAL PLAN DASHBOARD", templId);
//            AmpFeaturesVisibility step4=FeaturesUtil.getFeatureByName("Funding Information", "Funding", templId);
//            AmpFeaturesVisibility step5=FeaturesUtil.getFeatureByName("Regional Funding", "Funding", templId);
//            AmpFeaturesVisibility step6=FeaturesUtil.getFeatureByName("Components", "Components", templId);
//            AmpFeaturesVisibility step6_2=FeaturesUtil.getFeatureByName("Issues", "Issues", templId);
//            AmpModulesVisibility step7=FeaturesUtil.getModuleByName("Document", "PROJECT MANAGEMENT", templId);
//            AmpFeaturesVisibility step10=FeaturesUtil.getFeatureByName("Paris Indicator", "Add & Edit Activity", templId);
//            
//            AmpModulesVisibility step11=FeaturesUtil.getModuleByName("M & E", "MONITORING AND EVALUATING", templId);
//            AmpFeaturesVisibility step12=FeaturesUtil.getFeatureByName("Costing", "Activity Costing", templId);
//            AmpFeaturesVisibility step13=FeaturesUtil.getFeatureByName("Contracting", "Contracting", templId);
//            
//            AmpFeaturesVisibility step14=FeaturesUtil.getFeatureByName("Regional Observations", "Regional Observations", templId);
//            
//            if(step1!=null||step1_1!=null){
//                Step newStep1=new Step();
//                newStep1.setStepActualNumber(steps.size()+1);
//                newStep1.setStepNumber("1");
//                steps.add(newStep1);
//                        
//            }
//            if(step2!=null){
//                Step newStep2=new Step();
//                newStep2.setStepActualNumber(steps.size()+1);
//                newStep2.setStepNumber("1_5");
//                steps.add(newStep2);
//                
//            }
//            if(step3!=null||step3_2!=null||step3_3!=null){
//                Step newStep3=new Step();
//                newStep3.setStepActualNumber(steps.size()+1);
//                newStep3.setStepNumber("2");
//                steps.add(newStep3);
//                
//            }
//            if(step4!=null){
//                Step newStep4=new Step();
//                newStep4.setStepActualNumber(steps.size()+1);
//                newStep4.setStepNumber("3");
//                steps.add(newStep4);
//                
//            }
//             if(step5!=null){
//                Step newStep5=new Step();
//                newStep5.setStepActualNumber(steps.size()+1);
//                newStep5.setStepNumber("4");
//                steps.add(newStep5);
//                
//            }
//            if(step6!=null||step6_2!=null){
//                Step newStep6=new Step();
//                newStep6.setStepActualNumber(steps.size()+1);
//                newStep6.setStepNumber("5");
//                steps.add(newStep6);
//                
//            }
//            
//            if(step7!=null){
//                Step newStep7=new Step();
//                newStep7.setStepActualNumber(steps.size()+1);
//                newStep7.setStepNumber("6");
//                steps.add(newStep7);
//                
//            }
//            Step newStep8 = new Step();
//            newStep8.setStepActualNumber(steps.size() + 1);
//            newStep8.setStepNumber("7");
//            steps.add(newStep8);
//                
//            Step newStep9 = new Step();
//            newStep9.setStepActualNumber(steps.size() + 1);
//            newStep9.setStepNumber("8");
//            steps.add(newStep9);
//            
//            if (step10 != null) {
//                Step newStep10 = new Step();
//                newStep10.setStepActualNumber(steps.size() + 1);
//                newStep10.setStepNumber("17");
//                steps.add(newStep10);
//            }
//            
//             if(step11!=null){
//                Step newStep11=new Step();
//                newStep11.setStepActualNumber(steps.size()+1);
//                newStep11.setStepNumber("10");
//                steps.add(newStep11);
//                
//            }
//            
//             if(step12!=null){
//                Step newStep12=new Step();
//                newStep12.setStepActualNumber(steps.size()+1);
//                newStep12.setStepNumber("11");
//                steps.add(newStep12);
//                
//            }
//           
//            
//              if(step13!=null){
//                Step newStep13=new Step();
//                newStep13.setStepActualNumber(steps.size()+1);
//                newStep13.setStepNumber("13");
//                steps.add(newStep13);
//                
//            }
//              
//            if (step14 != null) {
//				Step newStep14 = new Step();
//				newStep14.setStepActualNumber(steps.size() + 1);
//				newStep14.setStepNumber("14");
//				steps.add(newStep14);
//            }
//            
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
//                String teamType=member.getTeamType();
                activityStatus.add(Constants.APPROVED_STATUS);
                activityStatus.add(Constants.EDITED_STATUS);
                Set relatedTeams=TeamUtil.getRelatedTeamsForMember(member);
                Set teamAO = TeamUtil.getComputedOrgs(relatedTeams);
                String activityNameString = AmpActivityVersion.hqlStringForName("a");
                // computed workspace
                if (teamAO != null && !teamAO.isEmpty()) {
                  	queryString = "select " + activityNameString + ", a.ampActivityId from " + AmpActivity.class.getName() + " a left outer join a.orgrole r  left outer join a.funding f " +
                   			" where  a.team in  (" + Util.toCSStringForIN(relatedTeams) + ")    or (r.organisation in  (" + Util.toCSStringForIN(teamAO) + ") or f.ampDonorOrgId in (" + Util.toCSStringForIN(teamAO) + ")) order by " + activityNameString;

                } else 
                {
                	// not computed (e.g. team) workspace
                    queryString = "select " + activityNameString + ", a.ampActivityId from " + AmpActivity.class.getName() + " a  where  a.team in  (" + Util.toCSString(relatedTeams) + ")    ";
//                    if (teamType!= null && teamType.equalsIgnoreCase(Constants.ACCESS_TYPE_MNGMT)) {
//                    	queryString += "  and approvalStatus in (" + Util.toCSString(activityStatus) + ")  ";
//                    }
                    queryString += " order by " + activityNameString;
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

    public static String[] searchActivitiesNamesAndIds(TeamMember member, String searchStr) throws DgException{
        	Session session=null;
    		String queryString =null;
    		Query query=null;
    		List activities=null;
    		String [] retValue=null;
    		try {
                    session=PersistenceManager.getRequestDBSession();

                Set<String> activityStatus = new HashSet<String>();
//                String teamType=member.getTeamType();
		activityStatus.add(Constants.APPROVED_STATUS);
		activityStatus.add(Constants.EDITED_STATUS);
                Set relatedTeams=TeamUtil.getRelatedTeamsForMember(member);
                    Set teamAO = TeamUtil.getComputedOrgs(relatedTeams);
                    // computed workspace
//                    if (teamAO != null && !teamAO.isEmpty()) {
//                        queryString = "select a.name, a.ampActivityId from " + AmpActivityVersion.class.getName() + " a left outer join a.orgrole r  left outer join a.funding f " +
//                                " where  a.team in  (" + Util.toCSString(relatedTeams) + ")    or (r.organisation in  (" + Util.toCSString(teamAO) + ") or f.ampDonorOrgId in (" + Util.toCSString(teamAO) + ")) and lower(a.name) like lower(:searchStr) order by a.name";
//
//                    } else {
//                        // none computed workspace
//                        queryString = "select a.name, a.ampActivityId from " + AmpActivityVersion.class.getName() + " a  where  a.team in  (" + Util.toCSString(relatedTeams) + ") and lower(a.name) like lower(:searchStr)";
//                        if (teamType!= null && teamType.equalsIgnoreCase(Constants.ACCESS_TYPE_MNGMT)) {
//                            queryString += "  and approvalStatus in (" + Util.toCSString(activityStatus) + ")  ";
//                        }
//                        queryString += " order by a.name ";
//                    }
                    
                    String activityName = AmpActivityVersion.hqlStringForName("gr.ampActivityLastVersion");
                    queryString ="select " + activityName + ", gr.ampActivityLastVersion.ampActivityId from "+ AmpActivityGroup.class.getName()+" gr ";                    
                    if (teamAO != null && !teamAO.isEmpty()) {
                    	queryString +=" left outer join gr.ampActivityLastVersion.orgrole r  left outer join gr.ampActivityLastVersion.funding f "+
                    	" where gr.ampActivityLastVersion.team in (" + Util.toCSStringForIN(relatedTeams) + ")  " +
                    			" or (r.organisation in  (" + Util.toCSStringForIN(teamAO) + ") or f.ampDonorOrgId in (" + Util.toCSStringForIN(teamAO) + ")) ";
                    	
                    } else {
                        // none computed workspace
                    	queryString +=" where gr.ampActivityLastVersion.team in  (" + Util.toCSStringForIN(relatedTeams) + ") ";                    	
//                        if (teamType!= null && teamType.equalsIgnoreCase(Constants.ACCESS_TYPE_MNGMT)) {
//                            queryString += "  and gr.ampActivityLastVersion.approvalStatus in (" + Util.toCSString(activityStatus) + ")  ";
//                        }
                        
                    }
                queryString += "  and lower(" + activityName + ") like lower(:searchStr) group by gr.ampActivityLastVersion.ampActivityId," + activityName + " order by " + activityName;
    			query=session.createQuery(queryString);
                query.setParameter("searchStr", searchStr + "%", Hibernate.STRING);
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
        public static String getActivityName(Long actId){
        	Session session=null;
    		String queryString =null;
    		Query query=null;    		
    		String name=null;
    		try {
    			session=PersistenceManager.getRequestDBSession();                   
    			String activityName = AmpActivityVersion.hqlStringForName("gr.ampActivityLastVersion");
    			queryString ="select " + activityName + " from "+ AmpActivityGroup.class.getName()+" gr where gr.ampActivityLastVersion.ampActivityId = " + actId;                    
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
                queryString = "select distinct a.budgetCodeProjectID from " + AmpActivityVersion.class.getName() + " a";    			  			
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
                
                int amountsUnitCode = Integer.valueOf(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS));

                if (amountsUnitCode == AmpARFilter.AMOUNT_OPTION_IN_THOUSANDS) {
                    transAmt *= 1000;
                }
                if (amountsUnitCode == AmpARFilter.AMOUNT_OPTION_IN_MILLIONS) {
                    transAmt *= 1000000;
                }                
                double exchangeRate=(helperFdet.getFixedExchangeRate()!=null)?FormatHelper.parseDouble( helperFdet.getFixedExchangeRate() ):Util.getExchange(detCurr.getCurrencyCode(), dt);
                
                AmpFundingDetail fundDet = new AmpFundingDetail(helperFdet.getTransactionType(), helperFdet.getAdjustmentTypeName(), transAmt, date, detCurr, exchangeRate);
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
			queryString=" select act.modifiedBy from " + AmpActivityVersion.class.getName()+" act where act.ampActivityId="+actId;
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
  			queryString= "select a.budgetsector  from " + AmpActivityVersion.class.getName()+ " a where a.ampActivityId="+actId;
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
    			queryString= "select a.budgetprogram  from " + AmpActivityVersion.class.getName()+ " a where a.ampActivityId="+actId;
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
    			queryString= "select a.budgetorganization  from " + AmpActivityVersion.class.getName()+ " a where a.ampActivityId="+actId;
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
    			queryString= "select a.budgetdepartment  from " + AmpActivityVersion.class.getName()+ " a where a.ampActivityId="+actId;
    			query=session.createQuery(queryString);    			
    			department=(Long)query.uniqueResult();    			
    		}catch(Exception ex) { 
    			logger.error("couldn't load Activity" + ex.getMessage());	
    			ex.printStackTrace(); 
    		} 
    		return department;
        }
      
	public static String getApprovedActivityQueryString(String label) {
//		String query = null;
//		query = " AND " + label + ".draft = false AND " + label + ".approvalStatus LIKE 'approved' ";
		String query = String.format(" AND (%s.draft IS NULL OR %s.draft = false) AND (%s.approvalStatus='%s' OR %s.approvalStatus='%s')",
				label, label, 
				label, Constants.APPROVED_STATUS,
				label, Constants.STARTED_APPROVED_STATUS
				);
		return query;
	}

	public static String getNonDraftActivityQueryString(String label) {
		String query = null;
		query = " AND " + label + ".draft = false ";
		return query;
	}
	
	/**
	 * Insert into table 'amp_activity_access' a new record to keep track of users access to activities.
	 * Related to AMP-4660.
	 * @param user Currently logged user.
	 * @param activityId Accessed activitys id.
	 * @param isUpdate True if user saved changes on the activity, false if the user accessed Activity Overview.
	 */
	public static void updateActivityAccess(User user, Long activityId, boolean isUpdate) {
		try {
			Session session = PersistenceManager.getRequestDBSession();
			String sqry = "INSERT INTO amp_activity_access(viewed, updated, change_date, dg_user_id, amp_activity_id) VALUES(?,?,?,?,?)";
			Query qry = session.createSQLQuery(sqry);
			qry.setParameter(0, (isUpdate) ? 0 : 1);
			qry.setParameter(1, (isUpdate) ? 1 : 0);
			qry.setParameter(2, new Date(System.currentTimeMillis()));
			qry.setParameter(3, user.getId().intValue());
			qry.setParameter(4, activityId);
			qry.executeUpdate();
			logger.info("Access logged for activity: " + activityId + " - User: " + user.getId());
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();	
		}
	}
	
	
    public static ArrayList<AmpActivityFake> getAllActivitiesAdmin(String searchTerm) {
        try {
            Session session = PersistenceManager.getSession();
            
            boolean isSearchByName = searchTerm != null && (!searchTerm.trim().isEmpty());
            String activityName = AmpActivityVersion.hqlStringForName("f");
            String nameSearchQuery;
            if (isSearchByName) {
            	nameSearchQuery = " (f.ampActivityId IN (SELECT t.objectId FROM " + AmpContentTranslation.class.getName() + " t WHERE t.objectClass = '" + AmpActivityVersion.class.getName() + "' AND upper(t.translation) like upper(:searchTerm)))" +
            "OR f.ampActivityId IN (SELECT f2.ampActivityId from " + AmpActivity.class.getName() + " f2 WHERE upper(f2.name) LIKE upper(:searchTerm) OR upper(f2.ampId) LIKE upper(:searchTerm) ) )" + 
            " AND "; 
            } else {
            	nameSearchQuery = "";
            }
            
            String queryString = "select f.ampActivityId, f.ampId, " + activityName + ", ampTeam , ampGroup FROM " + AmpActivity.class.getName() +  
            	" as f left join f.team as ampTeam left join f.ampActivityGroup as ampGroup WHERE " + nameSearchQuery + " ((f.deleted = false) or (f.deleted is null))";
            
            Query qry = session.createQuery(queryString);
            if(isSearchByName) {
            	qry.setString("searchTerm", "%" + searchTerm + "%");
            }
            Iterator iter = qry.list().iterator();
            ArrayList<AmpActivityFake> result = new  ArrayList<AmpActivityFake>();
            while (iter.hasNext()) {
                Object[] item = (Object[])iter.next();
                Long ampActivityId = (Long) item[0];
                String ampId = (String) item[1];
                String name = (String) item[2];
                AmpTeam team = (AmpTeam) item[3];
                AmpActivityGroup ampActGroup = (AmpActivityGroup) item[4];
                AmpActivityFake activity = new AmpActivityFake(name,team,ampId,ampActivityId,ampActGroup);
                result.add(activity);
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteAmpActivityWithVersions(Long ampActId){
    	  Session session = null;
		    Transaction tx = null;

		    try {
		      session = PersistenceManager.getSession();
//beginTransaction();

		      List<AmpActivityGroup> groups=getActivityGroups(session , ampActId);
		      if(groups!=null && groups.size()>0){
		      		for (AmpActivityGroup ampActivityGroup : groups) {
		      			
		      			Query qry = session.createQuery("UPDATE " + AmpActivityVersion.class.getName()+ " SET ampActivityPreviousVersion = NULL WHERE ampActivityGroup = " + ampActivityGroup.getAmpActivityGroupId());
						qry.executeUpdate();
						
		      			Set<AmpActivityVersion> activityversions=ampActivityGroup.getActivities();
		      			if(activityversions!=null && activityversions.size()>0){
		      				for (Iterator<AmpActivityVersion> iterator = activityversions.iterator(); iterator.hasNext();) {
		      					AmpActivityVersion ampActivityVersion=iterator.next();
		      					ampActivityVersion.setAmpActivityGroup(null);
		      					session.update(ampActivityVersion);
		      					deleteFullActivityContent(ampActivityVersion,session);
		      				    
		      					session.delete(ampActivityVersion);
							}
		      			}
		      			else{
		      				AmpActivityVersion ampAct = (AmpActivityVersion) session.load(AmpActivityVersion.class, ampActId);
		      				deleteFullActivityContent(ampAct,session);
	      					session.delete(ampAct);
		      			}
		  				session.delete(ampActivityGroup);
		  			}
		      	}
		      //tx.commit();
//session.flush();
		      
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
    
    public static void  deleteFullActivityContent(AmpActivityVersion ampAct, Session session) throws Exception{
    	ActivityUtil.deleteActivityContent(ampAct,session);
    	Long ampActId = ampAct.getAmpActivityId();
    	ActivityUtil.deleteActivitySectors(ampActId, session);
    	ActivityUtil.deleteActivityPhysicalComponentReport(DbUtil.getActivityPhysicalComponentReport(ampActId), session);
	  	ActivityUtil.deleteActivityAmpReportCache(DbUtil.getActivityReportCache(ampActId), session);
	  	ActivityUtil.deleteActivityReportLocation(DbUtil.getActivityReportLocation(ampActId), session);
	  	ActivityUtil.deleteActivityReportPhyPerformance(DbUtil.getActivityRepPhyPerformance(ampActId), session);
	  	ActivityUtil.deleteActivityReportSector(DbUtil.getActivityReportSector(ampActId), session);
	  	//This is not deleting AmpMEIndicators, just indicators, ME is deprecated.
	  	ActivityUtil.deleteActivityIndicators(DbUtil.getActivityMEIndValue(ampActId), ampAct, session);
    }
    
    public static void  deleteAllActivityContent(AmpActivityVersion ampAct, Session session) throws Exception{
    	ActivityUtil.deleteActivityContent(ampAct,session);
    	Long ampActId = ampAct.getAmpActivityId();
    	ActivityUtil.removeMergeSources(ampActId, session);
    	ActivityUtil.deleteActivityPhysicalComponentReportSession(ampActId, session);
    	ActivityUtil.deleteActivityAmpReportCacheSession(ampActId, session);
    	ActivityUtil.deleteActivityReportLocationSession(ampActId, session);
    	ActivityUtil.deleteActivityReportPhyPerformanceSession(ampActId, session);
    	ActivityUtil.deleteActivityReportSectorSession(ampActId, session);
    	ActivityUtil.deleteActivityIndicatorsSession(ampActId, session);
    }
    
	public static void archiveAmpActivityWithVersions(Long ampActId) {
		logger.error("archiving activity and all of its versions: " + ampActId);
		Transaction tx = null;
		try{
			Session session = PersistenceManager.getSession();
			tx = session.beginTransaction();
			List<AmpActivityGroup> groups = getActivityGroups(session, ampActId);
			logger.error("\tactivity groups linked with this ampActId: " + Util.toCSString(groups));
			if(groups != null && groups.size() > 0){
				for (AmpActivityGroup ampActivityGroup : groups) {
					logger.error("\tprocessing AmpActivityGroup with id = " + ampActivityGroup.getAmpActivityGroupId());
					for(AmpActivityVersion ampActivityVersion: ampActivityGroup.getActivities()){
						logger.error("\t\tmarking AmpActivityVersion as deleted, id = " + ampActivityVersion.getAmpActivityId());
						session.createQuery("UPDATE " + AmpActivityVersion.class.getName() + " aav SET aav.deleted = true WHERE aav.ampActivityId = " + ampActivityVersion.getAmpActivityId()).executeUpdate();
					}
				}
			}		      
//			logger.error("\tnow marking the ampActId per se as deleted: " + ampActId);
//			session.createQuery("UPDATE " + AmpActivityVersion.class.getName() + " aav SET aav.deleted = true WHERE aav.ampActivityId = " + ampActId).executeUpdate();	     
			logger.error("\tnow committing transaction");
			tx.commit();
		}
		catch (Exception e) {
			logger.error("error while marking activity as deleted: ", e);
			tx.rollback();
		}
	}
	
	public static Integer activityExists (Long versionId,Session session) throws Exception{
		Integer retVal = null;
		try {
			Query qry= session.createQuery("select count(a) from " +AmpActivityVersion.class.getName() +" a where a.ampActivityId="+versionId);
			retVal = (Integer)qry.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return retVal;		
	}
	
//	private static Connection createConnection()
//	{
//		try
//		{
//			return PersistenceManager.getJdbcConnection();
//		}
//		catch(SQLException e)
//		{
//			e.printStackTrace();
//			return null;
//		}
//	}	
	
	/**
	 * returns a subset of activities which can/should be validated by a team member
	 * @param tm
	 * @param activityIds
	 * @return
	 */
	public static Set<Long> getActivitiesWhichShouldBeValidated(TeamMember tm, Collection<Long> activityIds)
	{
		Set<Long> result = new HashSet<Long>();
		boolean crossTeamValidationEnabled = tm != null && tm.getAppSettings() != null && tm.getAppSettings().isCrossTeamValidationEnabled();
		try
		{
			String query = "SELECT a.amp_activity_id FROM amp_activity a WHERE a.amp_activity_id IN (" + TeamUtil.getCommaSeparatedList(activityIds) + ") " +
				"AND (a.approval_status = 'started' OR a.approval_status='edited' OR a.approval_status='rejected') AND (a.draft IS NULL OR a.draft IS FALSE)"; // AND (a.amp_team_id = " + tm.getTeamId() + ")";
			if (!crossTeamValidationEnabled)
				query += "  AND (a.amp_team_id = " + tm.getTeamId() + ")";
			
			List<BigInteger> validated_activity_ids = PersistenceManager.getSession().createSQLQuery(query).list();
			for(BigInteger bi:validated_activity_ids)
				result.add(bi.longValue());
			return result;
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public static boolean shouldThisUserBeAbleToEdit(TeamMember tm, Long activityId)
	{
		if (tm == null)
			return false;
		return WorkspaceFilter.isActivityWithinWorkspace(activityId);
	}
	
	public static boolean shouldThisUserValidate (TeamMember tm, Long activityId) {
		if (tm.getTeamHead() )
		//synchronized(lock) // cheaper to synchronize than to get a new connection every time
		{
			try 
			{						
				String query = "SELECT a.amp_activity_id, a.amp_team_id, a.draft, a.approval_status from amp_activity_version a where a.amp_activity_id = " + activityId;
				
				List<Object[]> sqlRes = PersistenceManager.getSession().createSQLQuery(query).list();				
				
				boolean returnValue = false;
				
				int count = sqlRes.size();
				if (count != 1)
					return false;
				
				Object[] rs = sqlRes.get(0);

				long actId = ((BigInteger) rs[0]).longValue();
				long teamId = ((BigInteger) rs[1]).longValue();
				Boolean draft = (Boolean) rs[2];
				String status = (String) rs[3];
					
				if (draft == null)
					draft = false;
					
				if (true || tm.getTeamId().equals(teamId) ) {
					if ( !draft && (Constants.STARTED_STATUS.equals(status) || Constants.EDITED_STATUS.equals(status) || Constants.REJECTED_STATUS.equals(status)) )
					returnValue = true;
				}
				
				return returnValue;
				
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}			
		}		
		return false;
	}
	
	
	 public static void changeActivityArchiveStatus(Collection<Long> activityIds, boolean status) {
			try {
				Session session 			= PersistenceManager.getRequestDBSession();
				String qryString			= "update " + AmpActivityVersion.class.getName()  + 
						" av  set av.archived=:archived where av.ampActivityId in (" + Util.toCSStringForIN(activityIds) + ")";
				Query query					= session.createQuery(qryString);
				query.setBoolean("archived", status);
				query.executeUpdate();
				session.flush();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
	    }  
	 
	 public static AmpStructureImg getStructureImage(Long structureId, Long imgId) {
		 return DbUtil.getStructureImage(structureId, imgId);
	 }
	 public static AmpStructureImg getMostRecentlyUploadedStructureImage(Long structureId) {
		 return DbUtil.getMostRecentlyUploadedStructureImage(structureId);
	 }
	 
	 public static  java.util.List<String[]> getAidEffectivenesForExport( AmpActivityVersion activity) {
		 java.util.List<String[]>aidEffectivenesForExport= new ArrayList<String[]>();
			String aidEffectivenesToAdd[];

			if (FeaturesUtil.isVisibleModule("/Activity Form/Aid Effectivenes/Project uses parallel project implementation unit")) {
				 aidEffectivenesToAdd= new String[2];

				aidEffectivenesToAdd[0]= TranslatorWorker.translateText("Project uses parallel project implementation unit");
				if(activity.getProjectImplementationUnit()!=null){
					aidEffectivenesToAdd[1]= activity.getProjectImplementationUnit();
				}	else{
					aidEffectivenesToAdd[1]="";
				}
				aidEffectivenesForExport.add(aidEffectivenesToAdd);
			}
			AmpCategoryValue ampCategoryValue = CategoryManagerUtil
					.getAmpCategoryValueFromListByKey(CategoryConstants.PROJECT_IMPLEMENTATION_MODE_KEY, activity.getCategories());

			if (FeaturesUtil.isVisibleModule("/Activity Form/Aid Effectivenes/Project Implementation Mode") && ampCategoryValue != null) {
				aidEffectivenesToAdd= new String[2];

				aidEffectivenesToAdd [0] = TranslatorWorker.translateText("Project Implementation Mode") + ":";
				aidEffectivenesToAdd [1]= ampCategoryValue.getValue() + "";
				aidEffectivenesForExport.add(aidEffectivenesToAdd);

			}
			if (FeaturesUtil.isVisibleModule("/Activity Form/Aid Effectivenes/Project has been approved by IMAC")) {
				aidEffectivenesToAdd= new String[2];

				aidEffectivenesToAdd [0]= TranslatorWorker.translateText("Project has been approved by IMAC") ;
				if(activity.getImacApproved()!=null){
					aidEffectivenesToAdd [1]= activity.getImacApproved() ;
				}else{
					aidEffectivenesToAdd [1]="";
				}
				aidEffectivenesForExport.add(aidEffectivenesToAdd);
			}
			if (FeaturesUtil.isVisibleModule("/Activity Form/Aid Effectivenes/Government is meber of project steering committee")) {
				 aidEffectivenesToAdd= new String[2];

				aidEffectivenesToAdd [0] = TranslatorWorker.translateText("Government is meber of project steering committee");
				if(activity.getNationalOversight()!=null){
					aidEffectivenesToAdd [1]= activity.getNationalOversight() ; 
				}
				else{
					aidEffectivenesToAdd [1]="";
				}
				aidEffectivenesForExport.add(aidEffectivenesToAdd);
			}
			if (FeaturesUtil.isVisibleModule("/Activity Form/Aid Effectivenes/Project is on budget")) {
				 aidEffectivenesToAdd= new String[2];

				aidEffectivenesToAdd [0]= TranslatorWorker.translateText("Project is on budget") ;
				if(activity.getOnBudget()!=null){
					aidEffectivenesToAdd [1]= activity.getOnBudget();	
				}
				else{
					aidEffectivenesToAdd [1]="";
				}
				aidEffectivenesForExport.add(aidEffectivenesToAdd);
			}
			if (FeaturesUtil.isVisibleModule("/Activity Form/Aid Effectivenes/Project is on parliament")) {
				aidEffectivenesToAdd= new String[2];

				aidEffectivenesToAdd [0]= TranslatorWorker
						.translateText("Project is on parliament") ;
				if(activity.getOnParliament()!=null){
					aidEffectivenesToAdd [1]= activity.getOnParliament() ;
				}
				else{
					aidEffectivenesToAdd [1]="";
				}
				aidEffectivenesForExport.add(aidEffectivenesToAdd);
			}
			if (FeaturesUtil.isVisibleModule("/Activity Form/Aid Effectivenes/Project disburses directly into the Goverment single treasury account")) {
				aidEffectivenesToAdd= new String[2];

				aidEffectivenesToAdd [0]= TranslatorWorker.translateText("Project disburses directly into the Goverment single treasury account");
				if(activity.getOnTreasury()!=null){
					aidEffectivenesToAdd [1]= activity.getOnTreasury();	
				}
				else{
					aidEffectivenesToAdd [1]="";
				}
				aidEffectivenesForExport.add(aidEffectivenesToAdd);
			}
			if (FeaturesUtil.isVisibleModule("/Activity Form/Aid Effectivenes/Project uses national financial management systems")) {
				aidEffectivenesToAdd= new String[2];

				aidEffectivenesToAdd [0]= TranslatorWorker.translateText("Project uses national financial management systems");
				if(activity.getNationalFinancialManagement()!=null){
					aidEffectivenesToAdd [1]= activity.getNationalFinancialManagement();	
				}
				else{
					aidEffectivenesToAdd [1]="";
				}
				aidEffectivenesForExport.add(aidEffectivenesToAdd);
			}
			if (FeaturesUtil.isVisibleModule("/Activity Form/Aid Effectivenes/Project uses national procurement systems")) {
				aidEffectivenesToAdd= new String[2];

				aidEffectivenesToAdd [0]= TranslatorWorker.translateText("Project uses national procurement systems");
				if(activity.getNationalProcurement()!=null){
					aidEffectivenesToAdd [1]= activity.getNationalProcurement();
				}
				else{
					aidEffectivenesToAdd [1]="";
				}
				aidEffectivenesForExport.add(aidEffectivenesToAdd);
			}
			if (FeaturesUtil.isVisibleModule("/Activity Form/Aid Effectivenes/Project uses national audit systems")) {
				aidEffectivenesToAdd = new String[2];

				aidEffectivenesToAdd [0]= TranslatorWorker.translateText("Project uses national audit systems");
				if(activity.getNationalAudit()!=null){
					aidEffectivenesToAdd [1]= activity.getNationalAudit();
				}
				else{
					aidEffectivenesToAdd [1]="";
				}
				aidEffectivenesForExport.add(aidEffectivenesToAdd);
			}
			return aidEffectivenesForExport;
		}
} // End
