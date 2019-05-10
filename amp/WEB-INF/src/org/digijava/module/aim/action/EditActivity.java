 /*
 * EditActivity.java
 * Created: Feb 10, 2005
 */

package org.digijava.module.aim.action;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.ARUtil;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.LocalizableLabel;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpActivityBudgetStructure;
import org.digijava.module.aim.dbentity.AmpActivityContact;
import org.digijava.module.aim.dbentity.AmpActivityDocument;
import org.digijava.module.aim.dbentity.AmpActivityInternalId;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpActor;
import org.digijava.module.aim.dbentity.AmpAnnualProjectBudget;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpComments;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpComponentFunding;
import org.digijava.module.aim.dbentity.AmpComponentType;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpField;
import org.digijava.module.aim.dbentity.AmpFundingAmount;
import org.digijava.module.aim.dbentity.AmpGPISurvey;
import org.digijava.module.aim.dbentity.AmpGPISurveyResponse;
import org.digijava.module.aim.dbentity.AmpIssues;
import org.digijava.module.aim.dbentity.AmpLineMinistryObservation;
import org.digijava.module.aim.dbentity.AmpLineMinistryObservationActor;
import org.digijava.module.aim.dbentity.AmpLineMinistryObservationMeasure;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpMeasure;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpRegionalObservation;
import org.digijava.module.aim.dbentity.AmpRegionalObservationActor;
import org.digijava.module.aim.dbentity.AmpRegionalObservationMeasure;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpStructure;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.ApprovalStatus;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.form.EditActivityForm.ActivityContactInfo;
import org.digijava.module.aim.form.ProposedProjCost;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.AmpContactsWorker;
import org.digijava.module.aim.helper.BudgetStructure;
import org.digijava.module.aim.helper.Components;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.aim.helper.FundingValidator;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.Issues;
import org.digijava.module.aim.helper.Location;
import org.digijava.module.aim.helper.Measures;
import org.digijava.module.aim.helper.OrgProjectId;
import org.digijava.module.aim.helper.RegionalFunding;
import org.digijava.module.aim.helper.RegionalFundingsHelper;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.ActivityVersionUtil;
import org.digijava.module.aim.util.AidEffectivenessIndicatorUtil;
import org.digijava.module.aim.util.AuditLoggerUtil;
import org.digijava.module.aim.util.ComponentsUtil;
import org.digijava.module.aim.util.ContactInfoUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DecimalWraper;
import org.digijava.module.aim.util.DocumentUtil;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.EUActivityUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LocationUtil.HelperLocationAncestorLocationNamesAsc;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.aim.util.ValidationStatus;
import org.digijava.module.aim.version.exception.CannotGetLastVersionForVersionException;
import org.digijava.module.budget.dbentity.AmpDepartments;
import org.digijava.module.budget.helper.BudgetDbUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.contentrepository.helper.DocumentData;
import org.digijava.module.esrigis.dbentity.AmpMapConfig;
import org.digijava.module.esrigis.helpers.DbHelper;
import org.digijava.module.esrigis.helpers.MapConstants;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.util.PermissionUtil;
import org.hibernate.Hibernate;
import org.hibernate.Session;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Loads the activity details of the activity specified in the form bean
 * variable 'activityId' to the EditActivityForm bean instance
 *
 * @author Priyajith
 */
public class EditActivity extends Action {

  private ServletContext ampContext = null;

  private static Logger logger = Logger.getLogger(EditActivity.class);

  @SuppressWarnings("unchecked")
  public ActionForward execute(ActionMapping mapping, ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws Exception {


    HttpSession session = request.getSession();

    TeamMember tm = (TeamMember) session.getAttribute("currentMember");

    //added in tanzania
    AmpTeam currentTeam = null;
    if(tm != null)
        currentTeam=TeamUtil.getAmpTeam(tm.getTeamId());
    boolean isPreview = mapping.getPath().trim().endsWith("viewActivityPreview");

    String langCode = RequestUtils.getNavigationLanguage(request).getCode();



    AmpActivityVersion activity = null;
    String computeTotals = FeaturesUtil.getGlobalSettingValue(Constants.
        GLOBALSETTINGS_COMPUTE_TOTALS);

    boolean debug = request.getParameter("debug") != null;

    //if("true".compareTo(request.getParameter("public"))!=0)
    //return mapping.findForward("forward");

    ActionMessages errors = new ActionMessages();

    ampContext = getServlet().getServletContext();

    // if user is not logged in, forward him to the home page
    if (session.getAttribute("currentMember") == null &&
        request.getParameter("edit") != null)

    if ("true".compareTo(request.getParameter("public")) != 0)
        return mapping.findForward("index");

    EditActivityForm eaForm = (EditActivityForm) form; // form bean instance

    Long activityId = null;
    activityId = eaForm.getActivityId();
    Long actIdParam = null;
    if(request.getParameter("ampActivityId")!=null) actIdParam = new Long(request.getParameter("ampActivityId"));
    if(actIdParam != null && actIdParam !=0L )
        activityId=actIdParam;
    eaForm.setWarningMessges(new ArrayList<String>());
    try{
        activityId  = this.getCorrectActivityVersionIdToUse(activityId, eaForm);
    }
    catch(CannotGetLastVersionForVersionException e) {
        logger.error(e.getMessage(), e);
    }

    // TODO this is temporary until we change all links in all modules that link to the previe
    // TODO on scope of this ticket too we will implement the messages that are displayed from outside the preview
    // TODO also we need still to be able to call the old preview for testing purposes AMP-28330


    if (request.getParameter("callOldActivityPreview") == null
            && request.getParameter("exportActivityToWord") == null
            && FeaturesUtil.getGlobalSettingValueBoolean(GlobalSettingsConstants.USE_NEW_ACTIVITY_PREVIEW)) {
       callActivityPreview(request, response, activityId);
    }



      String resetMessages = request.getParameter("resetMessages");
    if(resetMessages != null && resetMessages.equals("true")) {
        if(eaForm.getMessages() != null) {
            eaForm.getMessages().clear();
        }
    }

    /* Set Map configuration */
    AmpMapConfig map = DbHelper.getMapByType(MapConstants.MapType.ARCGIS_API);
    if (map != null && map.getMapUrl() != null && !map.getMapUrl().equals(""))
        eaForm.setEsriapiurl(map.getMapUrl());


    // set Globam Settings Multi-Sector Selecting
   /* String multiSectorSelect = FeaturesUtil.getGlobalSettingValue(Constants.
            GLOBALSETTINGS_MULTISECTORSELECT);
    eaForm.setMultiSectorSelecting(multiSectorSelect);
    */
    //
    String errorMsgKey = "";

    //gateperm checks are non mandatory, this means that an user still has permissions to edit an activity
    //those permissions can come from someplace else
    boolean gatePermEditAllowed = false;
    Session hsession = null;

    try {

    hsession=PersistenceManager.getSession();

    if (activityId != null) {
        //check whether activity exists
        Integer count = ActivityUtil.activityExists(activityId, hsession);
        if(count==null || count==0){
            eaForm.setActivityExists("no");
            return mapping.findForward("forward");
        }else{
            eaForm.setActivityExists("yes");
        }

        activity = (AmpActivityVersion) hsession.load(AmpActivityVersion.class, activityId);
        ///TODO this is only for testing AMP-20375 will be removed
                if (request.getParameter("simulateUpdate") != null) {
                    try{
                    if (request.getParameter("simulateUpdateDate") != null) {
                        String strDateUpdated = request.getParameter("simulateUpdateDate");
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        Date dateUpdated = format.parse(strDateUpdated);
                        if (tm != null && tm.getMemberId() != null) {
                            AmpTeamMember teamMember = TeamMemberUtil.getAmpTeamMemberCached(tm.getMemberId());
                            activity.setModifiedBy(teamMember);
                            hsession.update(activity);
                            List<String> details=new ArrayList<String>();
                            details.add("approved");
                            AuditLoggerUtil.logActivityUpdate(request, activity,details,dateUpdated);

                            ARUtil.writeResponse(response, "Activity *" + activity.getName() +"* successfully updated " );
                            return null;
                        }
                    }
                    } catch(Exception ex){
                        ARUtil.writeResponse(response, "Could not update activity please check logs ----" + ex.getMessage() );
                        logger.error(ex.getMessage(), ex);
                        return null;
                    }
                }

        eaForm.getIdentification().setWasDraft(activity.isCreatedAsDraft());
        if(activity!=null)
        {
            if (activity.getActivityCreator() != null && activity.getActivityCreator().getUser() != null)
            {
                eaForm.getIdentification().setActAthFirstName(activity.getActivityCreator().getUser().getFirstNames());
                eaForm.getIdentification().setActAthLastName(activity.getActivityCreator().getUser().getLastName());
                eaForm.getIdentification().setActAthEmail(activity.getActivityCreator().getUser().getEmail());
            }
            if (activity.getDraft() != null && activity.getDraft()) {
                eaForm.getWarningMessges().add(TranslatorWorker.translateText("This is a draft activity"));
            } else {
                ValidationStatus validationStatus = ActivityUtil.getValidationStatus(activity, tm);
               switch(validationStatus) {
                   case AUTOMATIC_VALIDATION:
                       LocalizableLabel label = new LocalizableLabel("The activity is awaiting approval and "
                               + "will be automatically approved within {0} days.",
                               ActivityUtil.daysToValidation(activity));
                       eaForm.getWarningMessges().add(label.toString());
                       break;
                   case AWAITING_VALIDATION:
                       eaForm.getWarningMessges().add(TranslatorWorker.translateText("The activity is awaiting "
                               + "approval."));
                       break;
                   case CANNOT_BE_VALIDATED:
                       eaForm.getWarningMessges().add(TranslatorWorker.translateText("This activity cannot be "
                               + "validated because there is no Workspace Manager."));
                       break;
                   case UNKNOWN:
                   default:
                       break;

               }

            }
            Map scope=new HashMap();
            scope.put(GatePermConst.ScopeKeys.CURRENT_MEMBER, tm);
            PermissionUtil.putInScope(session, GatePermConst.ScopeKeys.ACTIVITY, activity);
            gatePermEditAllowed = activity.canDo(GatePermConst.Actions.EDIT, scope);
        }
    }


    //old permission checking - this will be replaced by a global gateperm stuff
    // Checking whether the user have write access to the activity

    if (!gatePermEditAllowed) {

//          if (errorMsgKey.trim().length() > 0) {
//              errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
//                      errorMsgKey));
//              saveErrors(request, errors);
//
//              errorMsgKey = "error.aim.editActivity.userPartOfManagementTeam";
//              String url = "/aim/viewChannelOverview.do?ampActivityId="
//                      + activityId + "&tabIndex=0";
//              RequestDispatcher rd = getServlet().getServletContext()
//                      .getRequestDispatcher(url);
//              rd.forward(request, response);
//              return null;
//
//          }

            //TODO this for tanzania. think we should have plugable rules cos all cantries have different rules.
            if (!isPreview && activity!=null && activity.getTeam()!=null && currentTeam!=null){
                AmpTeam activityTeam=activity.getTeam();
                //if user is member of same team to which activity belongs then it can be edited
                if (currentTeam.getComputation() != null && currentTeam.getComputation()){
                    if (!currentTeam.getAmpTeamId().equals(activityTeam.getAmpTeamId())){
                        errorMsgKey="error.aim.editActivity.noWritePermissionForUser";
                    }
                }
            }

            if (errorMsgKey.trim().length() > 0 && !isPreview) {
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                        errorMsgKey));
                saveErrors(request, errors);
                String url = "/aim/viewChannelOverview.do?ampActivityId="
                        + activityId + "&tabIndex=0";
                RequestDispatcher rd = getServlet().getServletContext()
                        .getRequestDispatcher(url);
                rd.forward(request, response);
                return null;
            }
        }
    Collection euActs = EUActivityUtil.getEUActivities(activityId);
    request.setAttribute("costs", euActs);

    // load all themes
    Collection themes = new ArrayList(ProgramUtil.getAllThemes());
    eaForm.getPrograms().setProgramCollection(themes);

      // Checking whether the activity is already opened for editing
      // by some other user
      eaForm.setReset(true);
      eaForm.getLocation().setLocationReset(true);
      eaForm.getPhisycalProgress().setPhyProgReset(true);
      eaForm.getDocuments().setDocReset(true);
      eaForm.getComponents().setComponentReset(true);
      eaForm.reset(mapping, request);

      eaForm.setActivityId(activityId);
      HashMap activityMap = (HashMap) ampContext
          .getAttribute(Constants.EDIT_ACT_LIST);

      boolean canEdit = true;
      /*
       * modified by Govind
       */
      String step = request.getParameter("step");

      eaForm.setActivityId(activityId);
      eaForm.setReset(false);

      ProposedProjCost propProjCost = null;
      if (eaForm.getFunding().getProProjCost() != null) {
        propProjCost = new ProposedProjCost();
        propProjCost = eaForm.getFunding().getProProjCost();
        if (propProjCost.getCurrencyCode() == null &&
            propProjCost.getFunAmount() == null &&
            propProjCost.getFunDate() == null) {
          eaForm.getFunding().setProProjCost(null);
        }
      }
      
      ProposedProjCost revisedProjCost = null;
      if (eaForm.getFunding().getRevProjCost() != null) {
          revisedProjCost = new ProposedProjCost();
          revisedProjCost = eaForm.getFunding().getProProjCost();
        if (revisedProjCost.getCurrencyCode() == null &&
                revisedProjCost.getFunAmount() == null &&
                        revisedProjCost.getFunDate() == null) {
          eaForm.getFunding().setRevProjCost(null);
        }
      }
      
        List<AmpActivityBudgetStructure> budgetStructure = DbUtil.getBudgetStructure(eaForm.getActivityId());
        List<BudgetStructure> pojoBS = new ArrayList<BudgetStructure>();
        for (AmpActivityBudgetStructure aabs : budgetStructure) {
            pojoBS.add(new BudgetStructure(aabs.getBudgetStructureName(), aabs.getBudgetStructurePercentage()));
        }

      eaForm.setBudgetStructure(pojoBS);
      List nationalPlanObjectivePrograms=new ArrayList();
      List primaryPrograms=new ArrayList();
      List secondaryPrograms=new ArrayList();
      List tertiaryPrograms = new ArrayList();
      eaForm.getPrograms().setNationalPlanObjectivePrograms(nationalPlanObjectivePrograms);
      eaForm.getPrograms().setPrimaryPrograms(primaryPrograms);
      eaForm.getPrograms().setSecondaryPrograms(secondaryPrograms);
      eaForm.getPrograms().setTertiaryPrograms(tertiaryPrograms);
      
    //allComments
      List<AmpComments> colAux  = null;
      Collection ampFields          = DbUtil.getAmpFields();
      HashMap allComments           = new HashMap();

        if (ampFields != null) {
            for (Object ampField : ampFields) {
                AmpField field = (AmpField) ampField;
                colAux = DbUtil.getAllCommentsByField(field.getAmpFieldId(), activityId);
                allComments.put(field.getFieldName(), colAux);
            }
        }

        eaForm.getComments().setAllComments(allComments);

        String currCode = "";
        String currName = "";

        AmpCurrency curr = CurrencyUtil.getEffectiveCurrency();

        if (curr != null) {
            currCode = curr.getCurrencyCode();
            currName = curr.getCurrencyName();
        }

        eaForm.setCurrCode(currCode);
        eaForm.setCurrName(currName);

        if (eaForm.getFundingCurrCode() == null) {
            eaForm.setFundingCurrCode(currCode);
        }

        if (eaForm.getRegFundingPageCurrCode() == null) {
            eaForm.setRegFundingPageCurrCode(currCode);
        }


        // checking its the activity is already opened for editing...
        if (activityMap != null && activityMap.containsValue(activityId)) {
            // The activity is already opened for editing
            synchronized (ampContext) {
                HashMap tsaMap = (HashMap) ampContext
                        .getAttribute(Constants.TS_ACT_LIST);
                if (tsaMap != null) {
                    Long timeStamp = (Long) tsaMap.get(activityId);
                    if (timeStamp != null) {

                        if ((System.currentTimeMillis() - timeStamp) > Constants.MAX_TIME_LIMIT) {
                            // time limit has execeeded. invalidate the activity references
                            tsaMap.remove(activityId);

                HashMap userActList = (HashMap) ampContext
                    .getAttribute(Constants.USER_ACT_LIST);
                Iterator itr = userActList.keySet().iterator();
                while (itr.hasNext()) {
                  Long userId = (Long) itr.next();
                  Long actId = (Long) userActList.get(userId);
                  if (actId.longValue() == activityId
                      .longValue()) {
                    userActList.remove(userId);
                    break;
                  }
                }
                itr = activityMap.keySet().iterator();
                String sessId = null;
                while (itr.hasNext()) {
                  sessId = (String) itr.next();
                  Long actId = (Long) activityMap.get(sessId);
                  if (actId.longValue() ==
                      activityId.longValue()) {
                    activityMap.remove(sessId);
                    break;
                  }
                }
                ArrayList sessList = (ArrayList) ampContext
                    .getAttribute(Constants.SESSION_LIST);
                sessList.remove(sessId);
                Collections.sort(sessList);

                ampContext.setAttribute(Constants.EDIT_ACT_LIST, activityMap);
                ampContext.setAttribute(Constants.USER_ACT_LIST, userActList);
                ampContext.setAttribute(Constants.SESSION_LIST, sessList);
                ampContext.setAttribute(Constants.TS_ACT_LIST, tsaMap);
              }
              else
                canEdit = false;
            }
            else
              canEdit = false;
          }
          else
            canEdit = false;
        }
      }

      //logger.info("CanEdit = " + canEdit);
      //AMP-3461 When an activity is opened by a user, another user should be able to preview it
      if (!canEdit && !isPreview) {
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
            "error.aim.activityAlreadyOpenedForEdit"));
        saveErrors(request, errors);

        String url = "/aim/viewChannelOverview.do?ampActivityId="
            + activityId + "&tabIndex=0";
        RequestDispatcher rd = getServlet().getServletContext()
            .getRequestDispatcher(url);
        rd.forward(request, response);
        return null;
      }
      else {
        // logger.info("Path = " + mapping.getPath());
        if (!mapping.getPath().trim().endsWith("viewActivityPreview")) {
          // Edit the activity
          //logger.info("mapping does not end with viewActivityPreview.do");
          String sessId = session.getId();
          synchronized (ampContext) {
            ArrayList sessList = (ArrayList) ampContext.
                getAttribute(Constants.SESSION_LIST);
            HashMap userActList = (HashMap) ampContext.getAttribute(
                Constants.USER_ACT_LIST);

            HashMap tsaList = (HashMap) ampContext.getAttribute(
                Constants.TS_ACT_LIST);

            if (sessList == null) {
              sessList = new ArrayList();
            }
            if (userActList == null) {
              userActList = new HashMap();
            }
            if (activityMap == null) {
              activityMap = new HashMap();
            }
            if (tsaList == null) {
              tsaList = new HashMap();
            }

            sessList.add(sessId);
            Collections.sort(sessList);
            activityMap.put(sessId, activityId);
            if (tm != null)
              userActList.put(tm.getMemberId(), activityId);
            tsaList.put(activityId,
                        new Long(System.currentTimeMillis()));

            ampContext.setAttribute(Constants.SESSION_LIST, sessList);
            ampContext.setAttribute(Constants.EDIT_ACT_LIST, activityMap);
            ampContext.setAttribute(Constants.USER_ACT_LIST, userActList);
            ampContext.setAttribute(Constants.TS_ACT_LIST, tsaList);
          }
          eaForm.setEditAct(true);
        }
        else {
            session.removeAttribute("previousActivity");
            session.removeAttribute("nextActivity");
        }
      }

      eaForm.setReset(false);

      if (activityId != null) {
          /* Clearing Tanzania Adds */
          eaForm.getIdentification().setVote(null);
          eaForm.getIdentification().setSubVote(null);
          eaForm.getIdentification().setFY(null);
          eaForm.getIdentification().setSelectedFYs(null);
          eaForm.getIdentification().setSubProgram(null);
          eaForm.getIdentification().setProjectCode(null);
          eaForm.getIdentification().setMinistryCode(null);
          eaForm.getIdentification().setGovernmentApprovalProcedures(null);
          eaForm.getIdentification().setJointCriteria(null);
          /* END - Clearing Tanzania Adds */

          eaForm.getIdentification().setCrisNumber(null);
        /* Insert Categories */
          Set<AmpCategoryValue> categories=activity.getCategories();
        AmpCategoryValue ampCategoryValue = CategoryManagerUtil.
            getAmpCategoryValueFromList(CategoryConstants.ACCHAPTER_NAME, categories);

        if (ampCategoryValue != null)
            eaForm.getIdentification().setAcChapter(new Long(ampCategoryValue.getId()));

        ampCategoryValue = CategoryManagerUtil.getAmpCategoryValueFromListByKey(
            CategoryConstants.PROCUREMENT_SYSTEM_KEY, activity.getCategories());
        if (ampCategoryValue != null)
          eaForm.getIdentification().setProcurementSystem(new Long(ampCategoryValue.getId()));

        ampCategoryValue = CategoryManagerUtil.getAmpCategoryValueFromList(
            CategoryConstants.REPORTING_SYSTEM_NAME, activity.getCategories());
        if (ampCategoryValue != null)
          eaForm.getIdentification().setReportingSystem(new Long(ampCategoryValue.getId()));

        ampCategoryValue = CategoryManagerUtil.getAmpCategoryValueFromList(
            CategoryConstants.AUDIT_SYSTEM_NAME, activity.getCategories());
        if (ampCategoryValue != null)
          eaForm.getIdentification().setAuditSystem(new Long(ampCategoryValue.getId()));

        ampCategoryValue = CategoryManagerUtil.getAmpCategoryValueFromList(
            CategoryConstants.INSTITUTIONS_NAME, activity.getCategories());
        if (ampCategoryValue != null)
          eaForm.getIdentification().setInstitutions(new Long(ampCategoryValue.getId()));

        ampCategoryValue = CategoryManagerUtil.getAmpCategoryValueFromList(
            CategoryConstants.ACCESSION_INSTRUMENT_NAME, activity.getCategories());
        if (ampCategoryValue != null)
          eaForm.getIdentification().setAccessionInstrument(new Long(ampCategoryValue.getId()));

        ampCategoryValue = CategoryManagerUtil.getAmpCategoryValueFromListByKey(
                CategoryManagerUtil.getAlternateKey(currentTeam, CategoryConstants.ACTIVITY_STATUS_KEY), activity
                        .getCategories());
        if (ampCategoryValue != null)
          eaForm.getIdentification().setStatusId(new Long(ampCategoryValue.getId()));

        ampCategoryValue = CategoryManagerUtil.getAmpCategoryValueFromListByKey(CategoryConstants.PROJECT_IMPLEMENTING_UNIT_KEY, activity.getCategories());
        if (ampCategoryValue != null){
            eaForm.getIdentification().setProjectImplUnitId(new Long(ampCategoryValue.getId()));
        }
        eaForm.getIdentification().setFundingSourcesNumber(activity.getFundingSourcesNumber());

        eaForm.setInternalIds(activity.getInternalIds());
        if (activity.getIndicators() != null && activity.getIndicators().size() > 0) {
            eaForm.setIndicators(activity.getIndicators());
        }

        ampCategoryValue = CategoryManagerUtil.getAmpCategoryValueFromListByKey(
            CategoryConstants.IMPLEMENTATION_LEVEL_KEY, activity.getCategories());
        if (ampCategoryValue != null)
          eaForm.getLocation().setLevelId(new Long(ampCategoryValue.getId()));

        ampCategoryValue = CategoryManagerUtil.getAmpCategoryValueFromListByKey(
                CategoryConstants.ACTIVITY_LEVEL_KEY, activity.getCategories());
            if (ampCategoryValue != null)
              eaForm.getIdentification().setActivityLevel(new Long(ampCategoryValue.getId()));


        ampCategoryValue = CategoryManagerUtil.getAmpCategoryValueFromListByKey(
                CategoryConstants.FINANCIAL_INSTRUMENT_KEY, activity.getCategories());
//            if (ampCategoryValue != null)
//              eaForm.getIdentification().setGbsSbs(new Long(ampCategoryValue.getId()));

        ampCategoryValue = CategoryManagerUtil.getAmpCategoryValueFromListByKey(
                CategoryConstants.IMPLEMENTATION_LOCATION_KEY, activity.getCategories());
            if (ampCategoryValue != null) {
                eaForm.getLocation().setImplemLocationLevel(new Long(ampCategoryValue.getId()));
                eaForm.getLocation().setLevelIdx(ampCategoryValue.getIndex());
            }

          ampCategoryValue = CategoryManagerUtil.getAmpCategoryValueFromListByKey(
                  CategoryManagerUtil.getAlternateKey(currentTeam, CategoryConstants.PROJECT_CATEGORY_KEY), activity
                          .getCategories());
            if (ampCategoryValue != null)
                  eaForm.getIdentification().setProjectCategory(new Long(ampCategoryValue.getId()));

        ampCategoryValue = CategoryManagerUtil.getAmpCategoryValueFromListByKey(
                    CategoryConstants.ACTIVITY_BUDGET_KEY, activity.getCategories());
            if (ampCategoryValue != null)
                  eaForm.getIdentification().setBudgetCV(new Long(ampCategoryValue.getId()));
            else
                 eaForm.getIdentification().setBudgetCV(0L);



        /* End - Insert Categories */

        for (AmpActivityDocument actDoc : activity.getActivityDocuments()) {
            eaForm.getDocuments().getCrDocuments().add(DocumentData.buildFromUuid(actDoc.getUuid()));
        }

        /* Clearing session information about comments */
        String action = request.getParameter("action");
        if (action != null && action.trim().length() != 0) {
          if ("edit".equals(action)) {
            if (eaForm.getComments().getCommentsCol() != null) {
                eaForm.getComments().getCommentsCol().clear();
            } else {
                eaForm.getComments().setCommentsCol(new ArrayList<AmpComments>());
            }
            eaForm.getComments().setCommentFlag(false);
            /**
             * The commentColInSession session attribute is a map of lists.
             * Each list contains the AmpComments for a specific field. So the keys are the fields' ids.
             * It isn't really needed anymore except for compatibility with previewLogframe which
             * still uses this map.
             */
            HashMap<Long, List<AmpComments>> commentColInSession    = (HashMap)request.getSession().getAttribute("commentColInSession");
            if ( commentColInSession != null ) {
                commentColInSession.clear();
            }
            else {
                commentColInSession     = new HashMap<Long, List<AmpComments>>();
                request.getSession().setAttribute("commentColInSession", commentColInSession );
            }
            AmpComments.populateWithComments( eaForm.getComments().getCommentsCol(), commentColInSession,
                                activityId);
          }
        }
        /* END - Clearing session information about comments */

        // load the activity details
        ApprovalStatus actApprovalStatus = DbUtil.getActivityApprovalStatus(activityId);
        
        //eaForm.setApprovalStatus(actApprovalStatus);
        if (tm != null && tm.getTeamId()!=null && activity.getTeam() != null && activity.getTeam().getAmpTeamId() != null) {
                    if (("true".compareTo((String) session
                            .getAttribute("teamLeadFlag")) == 0 || tm
                            .isApprover())
                            && tm.getTeamId().equals(
                                    activity.getTeam().getAmpTeamId()) ){
              AmpTeamMember teamMember = TeamMemberUtil.getAmpTeamMemberCached(tm.getMemberId());
              eaForm.getIdentification().setApprovedBy(teamMember);
              eaForm.getIdentification().setApprovalDate(new Date());
              //eaForm.getIdentification().setApprovalStatus(ApprovalStatus.APPROVED);
              eaForm.getIdentification().setApprovalStatus(actApprovalStatus.getDbName());
              }

            else{
              //eaForm.setApprovalStatus(ApprovalStatus.STARTED);//actApprovalStatus);
                eaForm.getIdentification().setApprovalStatus(ApprovalStatus.EDITED.getDbName());
            }
        }
        

        
        //AMP-17127
        //for modalities that is a SSC category we have to add the SSC prefix
          List<AmpCategoryValue> modalities = CategoryManagerUtil.getAmpCategoryValuesFromListByKey(
                  CategoryManagerUtil.getAlternateKey(currentTeam,
                          CategoryConstants.MODALITIES_KEY), activity.getCategories());
        String[] actModalities=null;
        
        if(modalities !=null && modalities.size() >0){
            actModalities=new String[modalities.size()];
            int m=0;
            
            for (AmpCategoryValue modal : modalities) {
                actModalities[m] = modal.getLabel();
                m++;
            }           
        }
        eaForm.getIdentification().setSsc_modalities(actModalities );
       
        AmpCategoryValue typeOfCooperation = CategoryManagerUtil.getAmpCategoryValueFromListByKey(
                CategoryManagerUtil.getAlternateKey(currentTeam, CategoryConstants.TYPE_OF_COOPERATION_KEY), activity
                        .getCategories());
        eaForm.getIdentification().setSsc_typeOfCooperation(typeOfCooperation == null ? null : typeOfCooperation.getLabel());

        AmpCategoryValue typeOfImplementation = CategoryManagerUtil.getAmpCategoryValueFromListByKey(
                CategoryManagerUtil.getAlternateKey(currentTeam, CategoryConstants.TYPE_OF_IMPLEMENTATION_KEY),
                activity.getCategories());
        eaForm.getIdentification().setSsc_typeOfImplementation(typeOfImplementation == null ? null : typeOfImplementation.getLabel());

       // eaForm.getIdentification().setFundingSourcesNumber(activity.getFundingSourcesNumber());

        if (activity != null) {
            
            // set annual budgets and proposed project cost
            Set<AmpAnnualProjectBudget> annualBudgets = activity.getAnnualProjectBudgets();
            List<ProposedProjCost> proposedAnnualBudgets = new ArrayList<ProposedProjCost>();
            if (annualBudgets != null) {
                for (AmpAnnualProjectBudget annualBudget : annualBudgets) {
                    String ppcCurrencyCode = annualBudget.getAmpCurrencyId() != null ? annualBudget.getAmpCurrencyId().getCurrencyCode() : null; 
                    ProposedProjCost ppc = getProposedProjectCost(activity, eaForm, annualBudget.getAmount(), ppcCurrencyCode, 
                            annualBudget.getYear(), true, AmpFundingAmount.FundingType.PROPOSED);
                    proposedAnnualBudgets.add(ppc);
                }
            }
            
            Collections.sort(proposedAnnualBudgets);
            eaForm.getFunding().setProposedAnnualBudgets(proposedAnnualBudgets);
            
            eaForm.getFunding().setProProjCost(getProjectCost(activity, eaForm, AmpFundingAmount.FundingType.PROPOSED));
            eaForm.getFunding().setRevProjCost(getProjectCost(activity, eaForm, AmpFundingAmount.FundingType.REVISED));
            

          // load programs by type
          if(ProgramUtil.getAmpActivityProgramSettingsList()!=null){
                       List activityNPO=ActivityUtil.getActivityProgramsByProgramType(activityId,ProgramUtil.NATIONAL_PLAN_OBJECTIVE);
                       List activityPP=ActivityUtil.getActivityProgramsByProgramType(activityId,ProgramUtil.PRIMARY_PROGRAM);
                       List activitySP=ActivityUtil.getActivityProgramsByProgramType(activityId,ProgramUtil.SECONDARY_PROGRAM);
                       List activityTP=ActivityUtil.getActivityProgramsByProgramType(activityId,ProgramUtil.TERTIARY_PROGRAM);
                       eaForm.getPrograms().setNationalPlanObjectivePrograms(activityNPO);
                       eaForm.getPrograms().setPrimaryPrograms(activityPP);
                       eaForm.getPrograms().setSecondaryPrograms(activitySP);
                       eaForm.getPrograms().setTertiaryPrograms(activityTP);
                       eaForm.getPrograms().setNationalSetting(ProgramUtil.getAmpActivityProgramSettings(ProgramUtil.NATIONAL_PLAN_OBJECTIVE));
                       eaForm.getPrograms().setPrimarySetting(ProgramUtil.getAmpActivityProgramSettings(ProgramUtil.PRIMARY_PROGRAM));
                       eaForm.getPrograms().setSecondarySetting(ProgramUtil.getAmpActivityProgramSettings(ProgramUtil.SECONDARY_PROGRAM));
                       eaForm.getPrograms().setTertiarySetting(ProgramUtil.getAmpActivityProgramSettings(ProgramUtil.TERTIARY_PROGRAM));
            }

          //aid Effectiveness Section
          AidEffectivenessIndicatorUtil.sortSelectedEffectivenessOptions(activity);
          eaForm.setSelectedEffectivenessIndicatorOptions(activity.getSelectedEffectivenessIndicatorOptions());
          eaForm.setAllEffectivenessIndicators(AidEffectivenessIndicatorUtil.getAllActiveIndicators());

          // set title,description and objective
          eaForm.getIdentification().setTitle(activity.getName().trim());
          eaForm.getCosting().setCosts(new ArrayList(activity.getCosts()));
          eaForm.getIdentification().setTeam(activity.getTeam());
          eaForm.getIdentification().setCreatedBy(activity.getActivityCreator());
          eaForm.getIdentification().setModifiedBy(activity.getModifiedBy());
          

         // eaForm.getIdentification().setBudget(activity.getBudget());
          AmpCategoryValue budgetOff =  CategoryConstants.ACTIVITY_BUDGET_OFF.getAmpCategoryValueFromDB();
          eaForm.getIdentification().setBudgetCVOff((budgetOff == null) ? 0 : budgetOff.getId());
          AmpCategoryValue budgetOn = CategoryConstants.ACTIVITY_BUDGET_ON.getAmpCategoryValueFromDB();
          eaForm.getIdentification().setBudgetCVOn(budgetOn == null ? 1 : budgetOn.getId());

          eaForm.getIdentification().setHumanitarianAid(activity.isHumanitarianAid());
          eaForm.getIdentification().setGovAgreementNumber(activity.getGovAgreementNumber());

          if(activity.getBudgetCodeProjectID()!=null)
              eaForm.getIdentification().setBudgetCodeProjectID(activity.getBudgetCodeProjectID().trim());

          eaForm.getIdentification().setBudgetCodes(ActivityUtil.getBudgetCodes());


        eaForm.getIdentification().setBudgetsectors(BudgetDbUtil.getBudgetSectors());
        eaForm.getIdentification().setBudgetprograms(BudgetDbUtil.getBudgetPrograms());
        if (eaForm.getIdentification().getSelectedbudgedsector()!=null){
            eaForm.getIdentification().setBudgetorgs(
                new ArrayList<AmpOrganisation>(BudgetDbUtil.getOrganizationsBySector(eaForm.getIdentification().getSelectedbudgedsector())));
        }else{
            eaForm.getIdentification().setBudgetorgs(new ArrayList<AmpOrganisation>());
        }
        if (eaForm.getIdentification().getSelectedorg()!=null){
            eaForm.getIdentification().setBudgetdepartments(
                new ArrayList<AmpDepartments>(BudgetDbUtil.getDepartmentsbyOrg(eaForm.getIdentification().getSelectedorg())));
        }else{
            eaForm.getIdentification().setBudgetdepartments(new ArrayList<AmpDepartments>());
        }

          /*
           * Tanzania adds
           */
          if (activity.getFY() != null) {
              String fy =activity.getFY().trim();
              eaForm.getIdentification().setFY(fy);
              String[] years = fy.split(",");
              eaForm.getIdentification().setSelectedFYs(years);

          }


          if (activity.getVote() != null)
            eaForm.getIdentification().setVote(activity.getVote().trim());
          if (activity.getSubVote() != null)
            eaForm.getIdentification().setSubVote(activity.getSubVote().trim());
          if (activity.getSubProgram() != null)
            eaForm.getIdentification().setSubProgram(activity.getSubProgram().trim());
          if (activity.getProjectCode() != null)
            eaForm.getIdentification().setProjectCode(activity.getProjectCode().trim());
          if (activity.getMinistryCode() != null)
              eaForm.getIdentification().setMinistryCode(activity.getMinistryCode().trim());



          if (activity.isGovernmentApprovalProcedures() != null)
            eaForm.getIdentification().setGovernmentApprovalProcedures(activity.isGovernmentApprovalProcedures());
          else
            eaForm.getIdentification().setGovernmentApprovalProcedures(null);

          if (activity.isJointCriteria() != null)
            eaForm.getIdentification().setJointCriteria(activity.isJointCriteria());
          else
             eaForm.getIdentification().setJointCriteria(null);

          if (activity.isHumanitarianAid() != null)
              eaForm.getIdentification().setHumanitarianAid(activity.isHumanitarianAid());
          else
              activity.setHumanitarianAid(null);


          if (activity.getCrisNumber() != null)
              eaForm.getIdentification().setCrisNumber(activity.getCrisNumber().trim());
    

          if (activity.getDescription() != null)
            eaForm.getIdentification().setDescription(activity.getDescription().trim());

          if (activity.getEqualOpportunity() != null)
              eaForm.getCrossIssues().setEqualOpportunity(activity.getEqualOpportunity().trim());

          if (activity.getEnvironment() != null)
              eaForm.getCrossIssues().setEnvironment(activity.getEnvironment().trim());

          if (activity.getMinorities() != null)
              eaForm.getCrossIssues().setMinorities(activity.getMinorities().trim());


          if (activity.getLessonsLearned()!=null)
              eaForm.getIdentification().setLessonsLearned(activity.getLessonsLearned().trim());

        eaForm.getIdentification().setProjectImpact(activity.getProjectImpact());

            if (activity.getStatusOtherInfo() != null) {
                eaForm.getIdentification().setStatusOtherInfo(activity.getStatusOtherInfo());
            }

            if (activity.getProjectCategoryOtherInfo() != null) {
                eaForm.getIdentification().setProjectCategoryOtherInfo(activity.getProjectCategoryOtherInfo());
            }

            if (activity.getModalitiesOtherInfo() != null) {
                eaForm.getIdentification().setModalitiesOtherInfo(activity.getModalitiesOtherInfo());
            }

        eaForm.getIdentification().setActivitySummary(activity.getActivitySummary());

        eaForm.getIdentification().setConditionality(activity.getConditionality());
    
        if (StringUtils.isNotBlank(activity.getIatiIdentifier())) {
            eaForm.getIdentification().setIatiIdentifier(activity.getIatiIdentifier());
        }

        eaForm.getIdentification().setProjectManagement(activity.getProjectManagement());

        if (activity.getProjectComments() != null)
            eaForm.getIdentification().setProjectComments(activity.getProjectComments().trim());

        if (activity.getObjective() != null)
            eaForm.getIdentification().setObjectives(activity.getObjective().trim());

          if (activity.getPurpose() != null)
            eaForm.getIdentification().setPurpose(activity.getPurpose().trim());

          if (activity.getResults() != null)
            eaForm.getIdentification().setResults(activity.getResults());

          if (activity.getDocumentSpace() == null ||
              activity.getDocumentSpace().trim().length() == 0) {
            if (tm != null && DocumentUtil.isDMEnabled()) {
              eaForm.getDocuments().setDocumentSpace("aim-document-space-" +
                                      tm.getMemberId() +
                                      "-" + System.currentTimeMillis());
              Site currentSite = RequestUtils.getSite(request);
              DocumentUtil.createDocumentSpace(currentSite,
                                               eaForm.getDocuments().getDocumentSpace());
            }
          }
          else {
            eaForm.getDocuments().setDocumentSpace(activity.getDocumentSpace().
                                    trim());
          }
          eaForm.getIdentification().setAmpId(activity.getAmpId());
          
           if (activity.getStatusReason() != null)
              eaForm.getIdentification().setStatusReason(activity.getStatusReason());
    
            List gpiSurveys = new ArrayList();
            if (activity.getGpiSurvey() != null) {
                eaForm.setGpiSurvey(activity.getGpiSurvey());
                for (AmpGPISurvey survey : activity.getGpiSurvey()) {
                    List<AmpGPISurveyResponse> list = new ArrayList<>(survey.getResponses());
                    Collections.sort(list, new AmpGPISurveyResponse.AmpGPISurveyResponseComparator());
                    gpiSurveys.add(list);
                }
                request.setAttribute("gpiSurveys", gpiSurveys);
            }

          if (null != activity.getLineMinRank()) {
              eaForm.getPlanning().setLineMinRank(activity.getLineMinRank().toString());
          } else {
              eaForm.getPlanning().setLineMinRank("-1");
          }
          eaForm.getPlanning().setProposedProjectLife(activity.getProposedProjectLife());

          eaForm.getPlanning().setActRankCollection(new ArrayList());
          for(int i = 1; i < 6; i++) {
              eaForm.getPlanning().getActRankCollection().add(i);
          }

          eaForm.getIdentification().setCreatedDate(DateConversion
                                .convertDateToLocalizedString(activity.
              getCreatedDate()));
          eaForm.getIdentification().setUpdatedDate(DateConversion
                                .convertDateToLocalizedString(activity.
              getUpdatedDate()));
          eaForm.getPlanning().setOriginalAppDate(DateConversion
                                    .convertDateToLocalizedString(activity
                                            .getProposedApprovalDate()));
          eaForm.getPlanning().setRevisedAppDate(DateConversion
                                   .convertDateToLocalizedString(activity
                                           .getActualApprovalDate()));
          eaForm.getPlanning().setOriginalStartDate(DateConversion
                                      .convertDateToLocalizedString(activity
                                              .getProposedStartDate()));
          eaForm.getPlanning().setRevisedStartDate(DateConversion
                                   .convertDateToLocalizedString(activity
                                           .getActualStartDate()));
          eaForm.getPlanning().setContractingDate(DateConversion
                                    .convertDateToLocalizedString(activity.
                                            getContractingDate()));
          eaForm.getPlanning().setDisbursementsDate(DateConversion
                                      .convertDateToLocalizedString(activity.
                                              getDisbursmentsDate()));
          eaForm.getPlanning().setProposedCompDate(DateConversion
                                     .convertDateToLocalizedString(activity.
                                             getProposedCompletionDate()));

          eaForm.getPlanning().setOriginalCompDate(DateConversion
                                    .convertDateToLocalizedString(activity
                                            .getOriginalCompDate()));

          eaForm.getPlanning().setCurrentCompDate(DateConversion
                                    .convertDateToLocalizedString(activity
                                            .getActualCompletionDate()));
          eaForm.getPlanning().setProjectImplementationDelay(DateConversion.getFormattedPeriod(
                  ActivityUtil.getProjectImplementationDelay(activity)));

          /*eaForm.getPlanning().setProposedCompDate(DateConversion.ConvertDateToString(
              activity.getProposedCompletionDate()));*/

          // loading organizations and thier project ids.
          Set orgProjIdsSet = activity.getInternalIds();
          if (orgProjIdsSet != null) {
            Iterator projIdItr = orgProjIdsSet.iterator();
            Collection temp = new ArrayList();
            while (projIdItr.hasNext()) {
              AmpActivityInternalId actIntId = (
                  AmpActivityInternalId) projIdItr
                  .next();
              OrgProjectId projId = new OrgProjectId();
              projId.setId(actIntId.getId());
              projId.setOrganisation(actIntId.getOrganisation());
              projId.setProjectId(actIntId.getInternalId());
              temp.add(projId);
            }
            if (temp != null && temp.size() > 0) {
              OrgProjectId orgProjectIds[] = new OrgProjectId[
                  temp
                  .size()];
              Object arr[] = temp.toArray();
              for (int i = 0; i < arr.length; i++) {
                orgProjectIds[i] = (OrgProjectId) arr[i];
              }
              eaForm.getIdentification().setSelectedOrganizations(orgProjectIds);
            }
          }

          // loading the locations
          int impLevel = 0;

          Collection ampLocs = activity.getLocations();

          if (ampLocs != null && ampLocs.size() > 0) {
           List<Location> locs = new ArrayList<Location>();

            Iterator locIter = ampLocs.iterator();
            boolean maxLevel = false;

            AmpCategoryValueLocations defCountry    = DynLocationManagerUtil.getDefaultCountry();
            AmpCategoryValue implLevel                              = CategoryManagerUtil.getAmpCategoryValueFromListByKey(
                    CategoryConstants.IMPLEMENTATION_LEVEL_KEY, activity.getCategories());
            AmpCategoryValue implLocValue                   = CategoryManagerUtil.getAmpCategoryValueFromListByKey(
                    CategoryConstants.IMPLEMENTATION_LOCATION_KEY, activity.getCategories());
            boolean setFullPercForDefaultCountry    = false;
            if ( !"true".equals( FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.ALLOW_PERCENTAGES_FOR_ALL_COUNTRIES ) ) &&
                    implLevel!=null && implLocValue!=null &&
                            CategoryConstants.IMPLEMENTATION_LEVEL_INTERNATIONAL.equalsCategoryValue(implLevel) &&
                            CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY.equalsCategoryValue(implLocValue)
            ) {
                setFullPercForDefaultCountry            = true;
            }

            while (locIter.hasNext()) {
                AmpActivityLocation actLoc = (AmpActivityLocation) locIter.next();  //AMP-2250
                if (actLoc == null)
                    continue;
                AmpLocation loc=actLoc.getLocation();                               //AMP-2250

              if (loc != null) {
                Location location = new Location();
                location.setLocId(loc.getAmpLocationId());
                location.setLat(loc.getLocation().getGsLat());
                location.setLon(loc.getLocation().getGsLong());

                String cIso = FeaturesUtil.getDefaultCountryIso();
                //logger.info(" this is the settings Value" + cIso);
                Country cntry = DbUtil.getDgCountry(cIso);
                location.setCountryId(cntry.getCountryId());
                location.setCountry(cntry.getCountryName());
                location.setNewCountryId(cntry.getIso());

                location.setAmpCVLocation( loc.getLocation() );
                if ( loc.getLocation() != null ){
                    location.setAncestorLocationNames( DynLocationManagerUtil.getParents( loc.getLocation()) );
                    location.setLocationName(loc.getLocation().getName());
                    location.setLocId( loc.getLocation().getId() );
                    location.setLevelIdx(loc.getLocation().getParentCategoryValue().getIndex());
                }
                AmpCategoryValueLocations ampCVRegion   =
                    DynLocationManagerUtil.getAncestorByLayer(loc.getLocation(), CategoryConstants.IMPLEMENTATION_LOCATION_REGION);

                if ( ampCVRegion != null ) {
//                if (loc.getAmpRegion() != null) {
//                  location.setRegion(loc.getAmpRegion()
//                                     .getName());
//                  location.setRegionId(loc.getAmpRegion()
//                                       .getAmpRegionId());
                  if (eaForm.getFunding().getFundingRegions() == null) {
                    eaForm.getFunding()
                        .setFundingRegions(new ArrayList());
                  }
                  if (!eaForm.getFunding().getFundingRegions().contains(ampCVRegion) ) {
                    eaForm.getFunding().getFundingRegions().add( ampCVRegion );
                  }
                }

                if(actLoc.getLocationPercentage()!=null){
//                  String strPercentage    = FormatHelper.formatNumberNotRounded((double)actLoc.getLocationPercentage() );
                    String strPercentage = FormatHelper.formatPercentage(actLoc.getLocationPercentage());
                    location.setPercent(strPercentage);
//                  location.setPercent( strPercentage.replace(",", ".") );
                }

                if ( setFullPercForDefaultCountry && (actLoc.getLocationPercentage()==null || actLoc.getLocationPercentage() == 0.0) &&
                        CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY.equalsCategoryValue(loc.getLocation().getParentCategoryValue()) &&
                                loc.getLocation().getId() != defCountry.getId() )
                {
                    location.setPercentageBlocked(true);
                }

                locs.add(location);
              }
            }

              Collections.sort(locs, new HelperLocationAncestorLocationNamesAsc(langCode));
              eaForm.getLocation().setSelectedLocs(locs);
          }


          eaForm.getDocuments().setAllReferenceDocNameIds(null);
          eaForm.getDocuments().setReferenceDocs(null);

          eaForm=setSectorsToForm(eaForm, activity);
          
          List<AmpClassificationConfiguration> classificationConfigs = SectorUtil.getAllClassificationConfigsOrdered();
          eaForm.getSectors().setClassificationConfigs(classificationConfigs);
          
          if (activity.getProgramDescription() != null)
              eaForm.getPrograms().setProgramDescription(activity
                                       .getProgramDescription().trim());




          eaForm.getFunding().setShowActual(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.isActiveInDatabase());
          eaForm.getFunding().setShowPlanned(CategoryConstants.ADJUSTMENT_TYPE_PLANNED.isActiveInDatabase());
          eaForm.getFunding().setShowPipeline(CategoryConstants.ADJUSTMENT_TYPE_PIPELINE.isActiveInDatabase());
          eaForm.getFunding().setShowOfficialDevelopmentAid(CategoryConstants.ADJUSTMENT_TYPE_ODA_SSC.isActiveInDatabase());
          eaForm.getFunding().setShowBilateralSsc(CategoryConstants.ADJUSTMENT_TYPE_BILATERAL_SSC.isActiveInDatabase());
          eaForm.getFunding().setShowTriangularSsc(CategoryConstants.ADJUSTMENT_TYPE_TRIANGULAR_SSC.isActiveInDatabase());

          String toCurrCode=null;
          if (tm != null && tm.getAppSettings() != null)
              toCurrCode = CurrencyUtil.getAmpcurrency(tm.getAppSettings().getCurrencyId()).getCurrencyCode();
          else {
              toCurrCode = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY);
          }

          eaForm.getFunding().populateFromFundings(activity, toCurrCode, tm, debug);


          ArrayList regFunds = RegionalFundingsHelper.getRegionalFundings(activity.getRegionalFundings(), toCurrCode, 0);
          // Sort the funding details based on Transaction date.
          Iterator itr1 = regFunds.iterator();
          int index = 0;
          while (itr1.hasNext()) {
            RegionalFunding regFund = (RegionalFunding) itr1.next();
            List list = null;
            if (regFund.getCommitments() != null) {
              list = new ArrayList(regFund.getCommitments());
              Collections.sort(list, FundingValidator.dateComp);
            }
            regFund.setCommitments(list);
            list = null;
            if (regFund.getDisbursements() != null) {
              list = new ArrayList(regFund.getDisbursements());
              Collections.sort(list, FundingValidator.dateComp);
            }
            regFund.setDisbursements(list);
            list = null;
            if (regFund.getExpenditures() != null) {
              list = new ArrayList(regFund.getExpenditures());
              Collections.sort(list, FundingValidator.dateComp);
            }
            regFund.setExpenditures(list);
            regFunds.set(index++, regFund);
          }
          eaForm.getFunding().setRegionalFundings(regFunds);

          eaForm.getComponents().setSelectedComponents(null);
          eaForm.getComponents().setCompTotalDisb(0);

          if (activity.getComponents() != null && activity.getComponents().size() > 0) {
            getComponents(activity, eaForm, toCurrCode);
          }

          Site currentSite = RequestUtils.getSite(request);
          eaForm.getDocuments().setManagedDocumentList(DocumentUtil.getDocumentsForActivity(currentSite, activity));
          eaForm.getAgencies().setExecutingAgencies(new ArrayList<AmpOrganisation>());
          eaForm.getAgencies().setImpAgencies(new ArrayList<AmpOrganisation>());
          eaForm.getAgencies().setBenAgencies(new ArrayList<AmpOrganisation>());
          eaForm.getAgencies().setConAgencies(new ArrayList<AmpOrganisation>());
          eaForm.getAgencies().setReportingOrgs(new ArrayList<AmpOrganisation>());
          eaForm.getAgencies().setSectGroups(new ArrayList<AmpOrganisation>());
          eaForm.getAgencies().setRegGroups(new ArrayList<AmpOrganisation>());
          eaForm.getAgencies().setRespOrganisations(new ArrayList<AmpOrganisation>());

          eaForm.getAgencies().setExecutingOrgToInfo(new HashMap<String, String>());
          eaForm.getAgencies().setImpOrgToInfo(new HashMap<String, String>());
          eaForm.getAgencies().setBenOrgToInfo(new HashMap<String, String>());
          eaForm.getAgencies().setConOrgToInfo(new HashMap<String, String>());
          eaForm.getAgencies().setRepOrgToInfo(new HashMap<String, String>());
          eaForm.getAgencies().setSectOrgToInfo(new HashMap<String, String>());
          eaForm.getAgencies().setRegOrgToInfo(new HashMap<String, String>());
          eaForm.getAgencies().setRespOrgToInfo(new HashMap<String, String>());

          eaForm.getAgencies().setExecutingOrgPercentage(new HashMap<String, String>());
          eaForm.getAgencies().setImpOrgPercentage(new HashMap<String, String>());
          eaForm.getAgencies().setBenOrgPercentage(new HashMap<String, String>());
          eaForm.getAgencies().setConOrgPercentage(new HashMap<String, String>());
          eaForm.getAgencies().setRepOrgPercentage(new HashMap<String, String>());
          eaForm.getAgencies().setSectOrgPercentage(new HashMap<String, String>());
          eaForm.getAgencies().setRegOrgPercentage(new HashMap<String, String>());
          eaForm.getAgencies().setRespOrgPercentage(new HashMap<String, String>());

          Set<AmpOrgRole> relOrgs = activity.getOrgrole();
          if (relOrgs != null) {
            Iterator<AmpOrgRole> relOrgsItr = relOrgs.iterator();
            AmpOrgRole orgRole = null;
            AmpOrganisation organisation = null;
            while (relOrgsItr.hasNext()) {
              orgRole = relOrgsItr.next();
              organisation = DbUtil.getOrganisation(orgRole.getOrganisation().getAmpOrgId());
              //
              if (orgRole.getRole().getRoleCode().equals(Constants.RESPONSIBLE_ORGANISATION)
                      && (!eaForm.getAgencies().getRespOrganisations().contains(organisation))) {
                      eaForm.getAgencies().getRespOrganisations().add(organisation);
                      if ( orgRole.getAdditionalInfo() != null && orgRole.getAdditionalInfo().length() > 0 )
                          eaForm.getAgencies().getRespOrgToInfo().put(organisation.getAmpOrgId().toString(), orgRole.getAdditionalInfo() );
                      if(orgRole.getPercentage() != null ){
                          eaForm.getAgencies().getRespOrgPercentage().put(organisation.getAmpOrgId().toString(), FormatHelper.formatPercentage(orgRole.getPercentage()));
                      }
                 }
              if (orgRole.getRole().getRoleCode().equals(
                  Constants.EXECUTING_AGENCY)
                  && (!eaForm.getAgencies().getExecutingAgencies().contains(organisation))) {
                  eaForm.getAgencies().getExecutingAgencies().add(organisation);
                  if ( orgRole.getAdditionalInfo() != null && orgRole.getAdditionalInfo().length() > 0 )
                      eaForm.getAgencies().getExecutingOrgToInfo().put(organisation.getAmpOrgId().toString(), orgRole.getAdditionalInfo() );
                  if(orgRole.getPercentage() != null ){
                      eaForm.getAgencies().getExecutingOrgPercentage().put(organisation.getAmpOrgId().toString(), FormatHelper.formatPercentage(orgRole.getPercentage()));
                  }
             }
              else if (orgRole.getRole().getRoleCode().equals(
                  Constants.IMPLEMENTING_AGENCY)
                       && (!eaForm.getAgencies().getImpAgencies().contains(
                           organisation))) {
                eaForm.getAgencies().getImpAgencies().add(
                    organisation);
                if ( orgRole.getAdditionalInfo() != null && orgRole.getAdditionalInfo().length() > 0 )
                    eaForm.getAgencies().getImpOrgToInfo().put(organisation.getAmpOrgId().toString(), orgRole.getAdditionalInfo() );
                if(orgRole.getPercentage() != null ){
                  eaForm.getAgencies().getImpOrgPercentage().put(organisation.getAmpOrgId().toString(), FormatHelper.formatPercentage(orgRole.getPercentage()));
                }
              }

              else if (orgRole.getRole().getRoleCode().equals(Constants.BENEFICIARY_AGENCY)
                       && (!eaForm.getAgencies().getBenAgencies().contains(organisation))) {
                eaForm.getAgencies().getBenAgencies().add(organisation);
                if ( orgRole.getAdditionalInfo() != null && orgRole.getAdditionalInfo().length() > 0 )
                  eaForm.getAgencies().getBenOrgToInfo().put(organisation.getAmpOrgId().toString(), orgRole.getAdditionalInfo() );
                if(orgRole.getPercentage() != null ){
                  eaForm.getAgencies().getBenOrgPercentage().put(organisation.getAmpOrgId().toString(), FormatHelper.formatPercentage(orgRole.getPercentage()));
                }
              }
              else if (orgRole.getRole().getRoleCode().equals(Constants.CONTRACTING_AGENCY)
                       && (!eaForm.getAgencies().getConAgencies().contains(organisation))) {
                eaForm.getAgencies().getConAgencies().add(organisation);
                if ( orgRole.getAdditionalInfo() != null && orgRole.getAdditionalInfo().length() > 0 )
                  eaForm.getAgencies().getConOrgToInfo().put(organisation.getAmpOrgId().toString(), orgRole.getAdditionalInfo() );
                if(orgRole.getPercentage() != null ){
                  eaForm.getAgencies().getConOrgPercentage().put(organisation.getAmpOrgId().toString(), FormatHelper.formatPercentage(orgRole.getPercentage()));
                }
              }
              else if (orgRole.getRole().getRoleCode().equals( Constants.REPORTING_AGENCY)
                       && (!eaForm.getAgencies().getReportingOrgs().contains(organisation))) {
                eaForm.getAgencies().getReportingOrgs().add(organisation);
                if ( orgRole.getAdditionalInfo() != null && orgRole.getAdditionalInfo().length() > 0 )
                  eaForm.getAgencies().getRepOrgToInfo().put(organisation.getAmpOrgId().toString(), orgRole.getAdditionalInfo() );
                if(orgRole.getPercentage() != null ){
                  eaForm.getAgencies().getRepOrgPercentage().put(organisation.getAmpOrgId().toString(), FormatHelper.formatPercentage(orgRole.getPercentage()));
                }
              } else if (orgRole.getRole().getRoleCode().equals(Constants.SECTOR_GROUP)
                      && (!eaForm.getAgencies().getSectGroups().contains(organisation))) {
               eaForm.getAgencies().getSectGroups().add(organisation);
               if ( orgRole.getAdditionalInfo() != null && orgRole.getAdditionalInfo().length() > 0 )
                  eaForm.getAgencies().getSectOrgToInfo().put(organisation.getAmpOrgId().toString(), orgRole.getAdditionalInfo() );
               if(orgRole.getPercentage() != null ){
                  eaForm.getAgencies().getSectOrgPercentage().put(organisation.getAmpOrgId().toString(), FormatHelper.formatPercentage(orgRole.getPercentage()));
               }
             } else if (orgRole.getRole().getRoleCode().equals(Constants.REGIONAL_GROUP)
                     && (!eaForm.getAgencies().getRegGroups().contains( organisation))) {
              eaForm.getAgencies().getRegGroups().add(organisation);
              if ( orgRole.getAdditionalInfo() != null && orgRole.getAdditionalInfo().length() > 0 )
                  eaForm.getAgencies().getRegOrgToInfo().put(organisation.getAmpOrgId().toString(), orgRole.getAdditionalInfo() );
              if(orgRole.getPercentage() != null ){
                  eaForm.getAgencies().getRegOrgPercentage().put(organisation.getAmpOrgId().toString(), FormatHelper.formatPercentage(orgRole.getPercentage()));
              }
            }

            }
          }

          if (activity.getIssues() != null && activity.getIssues().size() > 0) {
            ArrayList issueList = new ArrayList();
            for (AmpIssues ampIssue : activity.getIssues()) {
              Issues issue = new Issues();
              issue.setId(ampIssue.getAmpIssueId());
              issue.setName(ampIssue.getName());
              issue.setIssueDate(FormatHelper.formatDate(ampIssue.getIssueDate()));
              ArrayList measureList = new ArrayList();
              if (ampIssue.getMeasures() != null && ampIssue.getMeasures().size() > 0) {
                for (AmpMeasure ampMeasure : ampIssue.getMeasures()) {
                  Measures measure = new Measures();
                  measure.setId(ampMeasure.getAmpMeasureId());
                  measure.setName(ampMeasure.getName());
                  measure.setMeasureDate(FormatHelper.formatDate(ampMeasure.getMeasureDate()));
                  ArrayList actorList = new ArrayList();
                  if (ampMeasure.getActors() != null && ampMeasure.getActors().size() > 0) {
                    for (AmpActor actor : ampMeasure.getActors()) {
                      actorList.add(actor);
                    }
                  }
                  measure.setActors(actorList);
                  measureList.add(measure);
                }
              }
              issue.setMeasures(measureList);
              issueList.add(issue);
            }
            eaForm.getIssues().setIssues(issueList);
          }
          else {
            eaForm.getIssues().setIssues(null);
          }

        // Regional Observations step.
        if (activity.getRegionalObservations() != null) {
            ArrayList issueList = new ArrayList();
            for (AmpRegionalObservation ampRegionalObservation : activity.getRegionalObservations()) {
                Issues issue = new Issues();
                issue.setId(ampRegionalObservation.getAmpRegionalObservationId());
                issue.setName(ampRegionalObservation.getName());
                issue.setIssueDate(FormatHelper.formatDate(ampRegionalObservation.getObservationDate()));
                ArrayList measureList = new ArrayList();
                if (ampRegionalObservation.getRegionalObservationMeasures() != null) {
                    for (AmpRegionalObservationMeasure ampMeasure : ampRegionalObservation.getRegionalObservationMeasures()) {
                        Measures measure = new Measures();
                        measure.setId(ampMeasure.getAmpRegionalObservationMeasureId());
                        measure.setName(ampMeasure.getName());
                        ArrayList actorList = new ArrayList();
                        if (ampMeasure.getActors() != null) {
                            for (AmpRegionalObservationActor actor : ampMeasure.getActors()) {
                                AmpActor auxAmpActor = new AmpActor();
                                auxAmpActor.setAmpActorId(actor.getAmpRegionalObservationActorId());
                                auxAmpActor.setName(actor.getName());
                                actorList.add(auxAmpActor);
                            }
                        }
                        measure.setActors(actorList);
                        measureList.add(measure);
                    }
                }
                issue.setMeasures(measureList);
                issueList.add(issue);
            }
            eaForm.getObservations().setIssues(issueList);
        } else {
            eaForm.getObservations().setIssues(null);
        }


          ActivityContactInfo contactInfo=eaForm.getContactInformation();
          //Reset contact info
          contactInfo.setDonorContacts(new ArrayList<AmpActivityContact>());
          contactInfo.setImplExecutingAgencyContacts(new ArrayList<AmpActivityContact>());
          contactInfo.setMofedContacts(new ArrayList<AmpActivityContact>());
          contactInfo.setProjCoordinatorContacts(new ArrayList<AmpActivityContact>());
          contactInfo.setSectorMinistryContacts(new ArrayList<AmpActivityContact>());



          List<AmpActivityContact> activityContacts=null;
          try {
              activityContacts=ContactInfoUtil.getActivityContacts(activity.getAmpActivityId());
              if(activityContacts!=null && activityContacts.size()>0){
                  for (AmpActivityContact ampActCont : activityContacts) {
                    AmpContact contact= ampActCont.getContact();
                    contact.setTemporaryId(contact.getId().toString());
                }
              }
        } catch (Exception e) {
            addErrorMessageToForm(eaForm, e);
        }
          contactInfo.setActivityContacts(activityContacts);
          if(activityContacts!=null && activityContacts.size()>0){
              for (AmpActivityContact ampActContact : activityContacts) {
                //donor contact
                if(ampActContact.getContactType().equals(Constants.DONOR_CONTACT)){
                    if(contactInfo.getDonorContacts()==null){
                        contactInfo.setDonorContacts(new ArrayList<AmpActivityContact>());
                    }
                    if(ampActContact.getPrimaryContact()!=null && ampActContact.getPrimaryContact()){
                        contactInfo.setPrimaryDonorContId(ampActContact.getContact().getTemporaryId());
                        /*
                        contactInfo.setPrimaryDonorContId(ampActContact.getContact().getTemporaryId());
                        /*if(contactInfo.getPrimaryDonorContIds()==null){
                            contactInfo.setPrimaryDonorContIds(new String[1]);
                        }
                        contactInfo.getPrimaryDonorContIds()[0]=ampActContact.getContact().getTemporaryId();*/

                    }
                    contactInfo.getDonorContacts().add(ampActContact);
                }
                //mofed contact
                else if(ampActContact.getContactType().equals(Constants.MOFED_CONTACT)){
                    if(contactInfo.getMofedContacts()==null){
                        contactInfo.setMofedContacts(new ArrayList<AmpActivityContact>());
                    }
                    if(ampActContact.getPrimaryContact()!=null && ampActContact.getPrimaryContact()){
                        contactInfo.setPrimaryMofedContId(ampActContact.getContact().getTemporaryId());
                        /*
                        contactInfo.setPrimaryMofedContId(ampActContact.getContact().getTemporaryId());
                        /*if(contactInfo.getPrimaryMofedContIds()==null){
                            contactInfo.setPrimaryMofedContIds(new String[1]);
                        }
                        contactInfo.getPrimaryMofedContIds()[0]=ampActContact.getContact().getTemporaryId();*/

                    }
                    contactInfo.getMofedContacts().add(ampActContact);
                }
                //project coordinator contact
                else if(ampActContact.getContactType().equals(Constants.PROJECT_COORDINATOR_CONTACT)){
                    if(contactInfo.getProjCoordinatorContacts()==null){
                        contactInfo.setProjCoordinatorContacts(new ArrayList<AmpActivityContact>());
                    }

                    if(ampActContact.getPrimaryContact()!=null && ampActContact.getPrimaryContact()){
                        contactInfo.setPrimaryProjCoordContId (ampActContact.getContact().getTemporaryId());
                        /*
                        contactInfo.setPrimaryProjCoordContId (ampActContact.getContact().getTemporaryId());
                        /*if(contactInfo.getPrimaryProjCoordContIds()==null){
                            contactInfo.setPrimaryProjCoordContIds(new String[1]);
                        }
                        contactInfo.getPrimaryProjCoordContIds()[0]=ampActContact.getContact().getTemporaryId();*/


                    }
                    contactInfo.getProjCoordinatorContacts().add(ampActContact);
                }
                //sector ministry contact
                else if(ampActContact.getContactType().equals(Constants.SECTOR_MINISTRY_CONTACT)){
                    if(contactInfo.getSectorMinistryContacts()==null){
                        contactInfo.setSectorMinistryContacts(new ArrayList<AmpActivityContact>());
                    }
                    if(ampActContact.getPrimaryContact()!=null && ampActContact.getPrimaryContact()){
                        contactInfo.setPrimarySecMinContId (ampActContact.getContact().getTemporaryId());
                        /*
                        contactInfo.setPrimarySecMinContId (ampActContact.getContact().getTemporaryId());
                        /*if(contactInfo.getPrimarySecMinContIds()==null){
                            contactInfo.setPrimarySecMinContIds(new String[1]);
                        }
                        contactInfo.getPrimarySecMinContIds()[0]=ampActContact.getContact().getTemporaryId();*/

                    }
                    contactInfo.getSectorMinistryContacts().add(ampActContact);
                }
                //implementing/executing agency
                else if(ampActContact.getContactType().equals(Constants.IMPLEMENTING_EXECUTING_AGENCY_CONTACT)){
                    if(contactInfo.getImplExecutingAgencyContacts()==null){
                        contactInfo.setImplExecutingAgencyContacts(new ArrayList<AmpActivityContact>());
                    }
                    if(ampActContact.getPrimaryContact()!=null && ampActContact.getPrimaryContact()){
                        contactInfo.setPrimaryImplExecutingContId (ampActContact.getContact().getTemporaryId());
                        /*
                        contactInfo.setPrimaryImplExecutingContId (ampActContact.getContact().getTemporaryId());
                        /*if(contactInfo.getPrimaryImplExecutingContIds()==null){
                            contactInfo.setPrimaryImplExecutingContIds(new String[1]);
                        }
                        contactInfo.getPrimaryImplExecutingContIds()[0]=ampActContact.getContact().getTemporaryId();*/

                    }
                    contactInfo.getImplExecutingAgencyContacts().add(ampActContact);
                }
            }

          }

          if(activityContacts!=null){
              AmpContactsWorker.copyContactsToSubLists(activityContacts, eaForm);
          }


          if (activity.getActivityCreator() != null) {
            User usr = activity.getActivityCreator().getUser();
            if (usr != null) {
              eaForm.getIdentification().setActAthFirstName(usr.getFirstNames());
              eaForm.getIdentification().setActAthLastName(usr.getLastName());
              eaForm.getIdentification().setActAthEmail(usr.getEmail());
              eaForm.getIdentification().setActAthAgencySource(usr.getOrganizationName());
            }
          }
        }

      }

      // Initally set the modality as "Project Support"
      Collection financingInstrValues = CategoryManagerUtil.
          getAmpCategoryValueCollectionByKey(CategoryConstants.FINANCING_INSTRUMENT_KEY, null);
      if (financingInstrValues != null && financingInstrValues.size() > 0) {
        Iterator itr = financingInstrValues.iterator();
        while (itr.hasNext()) {
          AmpCategoryValue financingInstr = (AmpCategoryValue) itr.next();
          if(financingInstr!=null)
              if(financingInstr.getValue()!=null)
                  if (financingInstr.getValue().equalsIgnoreCase("Project Support")) {
                      eaForm.getFunding().setModality(financingInstr.getId());
                      break;
                  }
        }
      }
      //Collection levelCol = null;
      // Loading the levels from the database
      /*if(eaForm.getLevelCollection() == null) {
          levelCol = DbUtil.getAmpLevels();
          eaForm.setLevelCollection(levelCol);
                   } else {
          levelCol = eaForm.getLevelCollection();
                   }
       */

      // load all the active currencies
      eaForm.setCurrencies(CurrencyUtil.getActiveAmpCurrencyByName());

      eaForm.getFunding().setValidcurrencies(CurrencyUtil.getUsableCurrencies());

      //load the possible projection values
      eaForm.getFunding().setProjections(CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.MTEF_PROJECTION_KEY, false));

    } catch (Exception e) {
        addErrorMessageToForm(eaForm, e);
    }
    
    if (request.getParameter("logframepr") != null)
      if (request.getParameter("logframepr").compareTo("true") == 0) {
        session.setAttribute("logframepr", "true");
        return mapping.findForward("forwardToPreview");
      }

    TeamMember teamMember = (TeamMember) session.getAttribute("currentMember");
    eaForm.getFunding().fillFinancialBreakdowns(activityId, DbUtil.getAmpFunding(activityId), debug);
    
    if (teamMember != null && teamMember.getTeamAccessType() != null){
        Long ampTeamId = teamMember.getTeamId();
        boolean workingTeamFlag = TeamUtil.checkForParentTeam(ampTeamId);

        String globalProjectsValidation     = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.PROJECTS_VALIDATION);
        if ("Management".toLowerCase().compareTo(teamMember.getTeamAccessType().toLowerCase()) == 0) {
            eaForm.setButtonText("none");
        }
        else{ //not a management team
            //there is another simple way to write these "if"s, but it is more clear like this
            eaForm.setButtonText("edit");
            if(activity!=null && activity.getDraft()!=null && !activity.getDraft())
                if (Constants.PROJECT_VALIDATION_OFF.equalsIgnoreCase(globalProjectsValidation.toLowerCase())) {
                    //global validation off
                    eaForm.setButtonText("edit");
                } else {
                    //global validation is on
                    //only the team leader of the team that owns the activity has rights to validate it if cross team validation is off
                    if (ActivityUtil.canValidateActivity(activity, teamMember)) {
                        eaForm.setButtonText("validate");
                    }
                }
        }
    }
    
    setRegionalObservationsToForm(activity, eaForm);
    
    setLineMinistryObservationsToForm(activity, eaForm);

    //structures

    ArrayList<AmpStructure> structures = new ArrayList<AmpStructure>(activity.getStructures());
    Collections.sort(structures);

    // force images load to avoid LazyInitializationException
    for (AmpStructure structure : structures) {
        Hibernate.initialize(structure.getImages());
        Hibernate.initialize(structure.getType());
        Hibernate.initialize(structure.getCoordinates());
    }

    eaForm.setStructures(structures);


    String debugFM = request.getParameter("debugFM");
    if (debugFM != null && "true".equals(debugFM)) {
        return mapping.findForward("forwardDebugFM");
    }

    if ("true".equals(request.getParameter("popupView"))) {
            return mapping.findForward("popupPreview");
    }
    //TODO the word export will be refactored since it relies on the activity
      // be loaded by the preview. Its outside of the scope of this iteration
      // to do such refactoring. AMP-28330
    if (request.getParameter("exportActivityToWord") != null) {
        response.sendRedirect("/aim/exportActToWord.do?activityid=" + activityId);
    }
      return mapping.findForward("forward");
  }

    private void callActivityPreview(HttpServletRequest request, HttpServletResponse response, Long ampActivityId) {
        try {
            String message = null;
            if (request.getParameter("editingUserId") != null) {
                Long editingUserId = Long.valueOf(request.getParameter("editingUserId"));
                if (TeamUtil.getCurrentMember().getMemberId().equals(editingUserId)) {
                    message = TranslatorWorker.translateText("You may only edit one activity at a time.");
                } else {
                    message = TranslatorWorker.translateText("Current activity is being edited by:")
                            + TeamMemberUtil.getTeamMember(editingUserId).getMemberName();
                }
            }

            if (request.getParameter("editPermissionError") != null) {
                message = TranslatorWorker.translateText("You do not have permissions to edit this activity.");
            }
            //
            if (message != null) {
                message = "?message=" + URLEncoder.encode(message, "UTF-8");
            } else {
                message = "";
            }
            response.sendRedirect("/TEMPLATE/reamp/modules/activity-preview/index.html#/activity/" + ampActivityId
                    + message);
        } catch (IOException e) {
            logger.error("Cannot redirect to activity preview");
        }
    }

    private ProposedProjCost getProposedProjectCost(AmpActivityVersion activity, EditActivityForm eaForm, 
            Double ppcAmount, String ppcCurrencyCode, Date year, boolean yearDate, AmpFundingAmount.FundingType funType) {

        ProposedProjCost ppc = new ProposedProjCost();
        Calendar c = Calendar.getInstance();
        c.setTime(year);
        
        AmpCurrency ppcCurrency;
        AmpFundingAmount activityPpc = activity.getProjectCostByType(funType);
        if (ppcCurrencyCode != null) {
            ppcCurrency = CurrencyUtil.getCurrencyByCode(ppcCurrencyCode);
        } else if (activityPpc != null && activityPpc.getCurrencyCode() != null) {
            ppcCurrency = CurrencyUtil.getCurrencyByCode(activityPpc.getCurrencyCode());
        } else {
            ppcCurrency = CurrencyUtil.getCurrencyByCode(eaForm.getCurrCode());
        }
    
        java.sql.Date ppcDate = new java.sql.Date(c.getTimeInMillis());
        double frmExRt = Util.getExchange(ppcCurrency.getCurrencyCode(), ppcDate);
        double toExRt = Util.getExchange(eaForm.getCurrCode(), ppcDate);
        
        DecimalWraper amt = CurrencyWorker.convertWrapper(ppcAmount, frmExRt, toExRt, ppcDate);
        ppc.setFunAmountAsDouble(amt.doubleValue());
        ppc.setCurrencyCode(eaForm.getCurrCode());
        ppc.setCurrencyName(eaForm.getCurrName());
        ppc.setFunAmount(FormatHelper.formatNumber(amt.doubleValue()));
        
        if (yearDate) {
            ppc.setFunDate(Integer.toString(c.get(Calendar.YEAR)));
        } else {
            ppc.setFunDate(FormatHelper.formatDate(activityPpc.getFunDate()));
        }
        
        return ppc;
    }

  public final static ArrayList<AmpStructure> eager_copy(Set<AmpStructure> structures) throws CloneNotSupportedException
  {
      ArrayList<AmpStructure> res = new ArrayList<AmpStructure>();
      for(AmpStructure struc:structures)
      {
          AmpStructure z = (AmpStructure) struc.clone();
          z.setImages(new HashSet(struc.getImages()));
          /*z.setActivities(new HashSet(struc.getActivities()));
          z.setAmpStructureId(struc.getAmpStructureId());
          z.setCreationdate(struc.getCreationdate());
          z.setDescription(struc.getDescription());*/
          res.add(z);
      }
      return res;
  }


  private Long getCorrectActivityVersionIdToUse(Long activityId, EditActivityForm form) {
      Long lastVersionId    = ActivityVersionUtil.getLastVersionForVersion(activityId);
      if ( lastVersionId != null && !lastVersionId.equals(activityId) ) {
          form.getWarningMessges().add(TranslatorWorker.translateText("Requested activity version was not the latest "
                  + "version. Preview switched to showing the last version!"));
          return lastVersionId;
      }
      return activityId;
}
    
    
    private void setRegionalObservationsToForm(AmpActivityVersion activity, EditActivityForm eaForm) {
        if (activity.getRegionalObservations() != null && activity.getRegionalObservations().size() > 0) {
            ArrayList issueList = new ArrayList();
            
            for (AmpRegionalObservation aro : activity.getRegionalObservations()) {
                Issues issue = new Issues();
                issue.setId(aro.getAmpRegionalObservationId());
                issue.setName(aro.getName());
                issue.setIssueDate(FormatHelper.formatDate(aro.getObservationDate()));
                ArrayList measureList = new ArrayList();
                if (aro.getRegionalObservationMeasures() != null) {
                    for (AmpRegionalObservationMeasure ampMeasure : aro.getRegionalObservationMeasures()) {
                        Measures measure = new Measures();
                        measure.setId(ampMeasure.getAmpRegionalObservationMeasureId());
                        measure.setName(ampMeasure.getName());
                        ArrayList actorList = new ArrayList();
                        if (ampMeasure.getActors() != null) {
                            for (AmpRegionalObservationActor actor : ampMeasure.getActors()) {
                                AmpActor auxAmpActor = new AmpActor();
                                auxAmpActor.setAmpActorId(actor.getAmpRegionalObservationActorId());
                                auxAmpActor.setName(actor.getName());
                                actorList.add(auxAmpActor);
                            }
                        }
                        measure.setActors(actorList);
                        measureList.add(measure);
                    }
                }
                issue.setMeasures(measureList);
                issueList.add(issue);
            }
            eaForm.getRegionalObservations().setIssues(issueList);
        } else {
            eaForm.getRegionalObservations().setIssues(null);
        }
    }

    private void setLineMinistryObservationsToForm(AmpActivityVersion activity, EditActivityForm eaForm) {
        if(activity.getLineMinistryObservations() != null && activity.getLineMinistryObservations().size()>0){
                ArrayList issueList = new ArrayList();

                for(AmpLineMinistryObservation ampLineMinistryObservation : activity.getLineMinistryObservations()){
                    Issues issue = new Issues();
                    issue.setId(ampLineMinistryObservation.getAmpLineMinistryObservationId());
                    issue.setName(ampLineMinistryObservation.getName());
                    issue.setIssueDate(FormatHelper.formatDate(ampLineMinistryObservation.getObservationDate()));
                    ArrayList measureList = new ArrayList();
                    if (ampLineMinistryObservation.getLineMinistryObservationMeasures() != null) {

                        for(AmpLineMinistryObservationMeasure ampMeasure: ampLineMinistryObservation.getLineMinistryObservationMeasures()){
                            Measures measure = new Measures();
                            measure.setId(ampMeasure.getAmpLineMinistryObservationMeasureId());
                            measure.setName(ampMeasure.getName());
                            ArrayList actorList = new ArrayList();
                            if (ampMeasure.getActors() != null) {
                                for(AmpLineMinistryObservationActor actor : ampMeasure.getActors()){
                                    AmpActor auxAmpActor = new AmpActor();
                                    auxAmpActor.setAmpActorId(actor.getAmpLineMinistryObservationActorId());
                                    auxAmpActor.setName(actor.getName());
                                    actorList.add(auxAmpActor);
                                }
                            }
                            measure.setActors(actorList);
                            measureList.add(measure);
                        }
                    }
                    issue.setMeasures(measureList);
                    issueList.add(issue);
                }

                eaForm.getLineMinistryObservations().setIssues(issueList);
            } else {
                eaForm.getLineMinistryObservations().setIssues(null);
            }

    }

  private EditActivityForm setSectorsToForm(EditActivityForm form, AmpActivityVersion activity) {
        Collection<AmpActivitySector> sectors = activity.getSectors();

        if (sectors != null && sectors.size() > 0) {
            List<ActivitySector> activitySectors = new ArrayList<ActivitySector>();
            for (AmpActivitySector ampActSect : sectors) {
                if (ampActSect != null) {
                    AmpSector sec = ampActSect.getSectorId();
                    if (sec != null) {
                        AmpSector parent = null;
                        AmpSector subsectorLevel1 = null;
                        AmpSector subsectorLevel2 = null;
                        if (sec.getParentSectorId() != null) {
                            if (sec.getParentSectorId().getParentSectorId() != null) {
                                subsectorLevel2 = sec;
                                subsectorLevel1 = sec.getParentSectorId();
                                parent = sec.getParentSectorId().getParentSectorId();
                            } else {
                                subsectorLevel1 = sec;
                                parent = sec.getParentSectorId();
                            }
                        } else {
                            parent = sec;
                        }
                        ActivitySector actSect = new ActivitySector();
                                                actSect.setConfigId(ampActSect.getClassificationConfig().getId());
                        if (parent != null) {
                            actSect.setId(parent.getAmpSectorId());
                            String view = FeaturesUtil.getGlobalSettingValue("Allow Multiple Sectors");
                            if (view != null)
                                if (view.equalsIgnoreCase("On")) {
                                    actSect.setCount(1);
                                } else {
                                    actSect.setCount(2);
                                }

                            actSect.setSectorId(parent.getAmpSectorId());
                            actSect.setSectorName(parent.getName());
                            if (subsectorLevel1 != null) {
                                actSect.setSubsectorLevel1Id(subsectorLevel1.getAmpSectorId());
                                actSect.setSubsectorLevel1Name(subsectorLevel1.getName());
                                if (subsectorLevel2 != null) {
                                    actSect.setSubsectorLevel2Id(subsectorLevel2.getAmpSectorId());
                                    actSect.setSubsectorLevel2Name(subsectorLevel2.getName());
                                }
                            }
                            actSect.setSectorPercentage(FormatHelper.formatPercentage(ampActSect.getSectorPercentage()));
                            actSect.setSectorScheme(parent.getAmpSecSchemeId().getSecSchemeName());

                        }

                        activitySectors.add(actSect);
                    }
                }
            }
            Collections.sort(activitySectors);
            form.getSectors().setActivitySectors(activitySectors);
        }
        return form;
    }


    /**
     *
     * @param activity
     * @param eaForm
     * @param toCurrCode
     */
    private void getComponents(AmpActivityVersion activity, EditActivityForm eaForm, String toCurrCode) {

//      Collection activityComponents = activity.getComponents();
        List<Components<FundingDetail>> selectedComponents = new ArrayList<Components<FundingDetail>>();
//      Iterator compItr = componets.iterator();
        for(AmpComponent temp : activity.getComponents()) {
//          AmpComponent temp = (AmpComponent) compItr.next();
            Components<FundingDetail> tempComp = new Components<FundingDetail>();
            tempComp.setTitle(temp.getTitle());
            tempComp.setComponentId(temp.getAmpComponentId());
            tempComp.setType_Id((temp.getType() != null) ? temp.getType().getType_id() : null);

            if (temp.getDescription() == null) {
                tempComp.setDescription(" ");
            } else {
                tempComp.setDescription(temp.getDescription().trim());
            }
            tempComp.setCode(temp.getCode());
            tempComp.setUrl(temp.getUrl());
            tempComp.setCommitments(new ArrayList<FundingDetail>());
            tempComp.setDisbursements(new ArrayList<FundingDetail>());
            tempComp.setExpenditures(new ArrayList<FundingDetail>());

            Collection<AmpComponentFunding> fundingComponentActivity = temp.getFundings();
            Iterator<AmpComponentFunding> cItr = fundingComponentActivity.iterator();

            while (cItr.hasNext()) {
                AmpComponentFunding ampCompFund = (AmpComponentFunding) cItr.next();

                double disb = 0;

                if (ampCompFund.getAdjustmentType().getValue().equals(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey())
                        && ampCompFund.getTransactionType().intValue() == 1)
                disb = ampCompFund.getTransactionAmount().doubleValue();

                eaForm.getComponents().setCompTotalDisb(eaForm.getComponents().getCompTotalDisb() + disb);

                FundingDetail fd = new FundingDetail();
                fd.setAdjustmentTypeName(ampCompFund.getAdjustmentType() );

            //  fd.setAdjustmentType(ampCompFund.getAdjustmentType().intValue());

                fd.setAmpComponentFundingId(ampCompFund.getAmpComponentFundingId());

                //convert to  default currency

                java.sql.Date dt = new java.sql.Date(ampCompFund.getTransactionDate().getTime());

                double frmExRt = Util.getExchange(ampCompFund.getCurrency().getCurrencyCode(), dt);
                double toExRt = Util.getExchange(toCurrCode, dt);
                DecimalWraper amt = CurrencyWorker.convertWrapper(ampCompFund.getTransactionAmount(), frmExRt, toExRt, dt);


                fd.setCurrencyCode(toCurrCode);
                fd.setTransactionAmount(FormatHelper.formatNumber( amt.getValue()));


                fd.setCurrencyName(ampCompFund.getCurrency().getCurrencyName());
                fd.setTransactionDate(DateConversion.convertDateToLocalizedString(ampCompFund.getTransactionDate()));
                fd.setFiscalYear(DateConversion.convertDateToFiscalYearString(ampCompFund.getTransactionDate()));
                fd.setTransactionType(ampCompFund.getTransactionType().intValue());
                fd.setComponentOrganisation(ampCompFund.getReportingOrganization());
                fd.setComponentSecondResponsibleOrganization(ampCompFund.getComponentSecondResponsibleOrganization());
                fd.setComponentTransactionDescription(ampCompFund.getDescription());
                
                if (fd.getTransactionType() == 0) {

                    tempComp.getCommitments().add(fd);
                } else if (fd.getTransactionType() == 1) {
                    tempComp.getDisbursements().add(fd);

                } else if (fd.getTransactionType() == 2) {
                    tempComp.getExpenditures().add(fd);
                }
            }
            ComponentsUtil.calculateFinanceByYearInfo(tempComp, fundingComponentActivity);
            selectedComponents.add(tempComp);
        }

        // Sort the funding details based on Transaction date.
        Iterator compIterator = selectedComponents.iterator();
        int index = 0;
        while (compIterator.hasNext()) {
            Components components = (Components) compIterator.next();
            if (components.getType_Id() != null) {
                AmpComponentType type = ComponentsUtil.getComponentTypeById(components.getType_Id());
                components.setTypeName(type.getName());
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
            selectedComponents.set(index++, components);
        }

        eaForm.getComponents().setSelectedComponents(selectedComponents);
    }
    
    private ProposedProjCost getProjectCost(AmpActivityVersion activity,  EditActivityForm eaForm, 
            AmpFundingAmount.FundingType funType) {
        ProposedProjCost projCost = new ProposedProjCost();
        AmpFundingAmount ppc = activity.getProjectCostByType(funType);
                if (ppc != null && ppc.getFunAmount() != null && ppc.getFunDate() != null) {
                    projCost = getProposedProjectCost(activity, eaForm, ppc.getFunAmount(),
                            ppc.getCurrencyCode(), ppc.getFunDate(), false, funType);
                }
        return projCost;
    }
    
    private void addErrorMessageToForm(EditActivityForm eaForm, Exception e) {
        eaForm.getWarningMessges().add(TranslatorWorker.translateText("An error occurred when loading the page. Please contact the AMP administrator."));
        logger.error(e.getMessage(), e);
    }
}
