/*
 * EditActivity.java
 * Created: Feb 10, 2005
 */

package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.GroupReportData;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityClosingDates;
import org.digijava.module.aim.dbentity.AmpActivityComponente;
import org.digijava.module.aim.dbentity.AmpActivityInternalId;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpActor;
import org.digijava.module.aim.dbentity.AmpComments;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpComponentFunding;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingMTEFProjection;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.dbentity.AmpIssues;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpMeasure;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpPhysicalPerformance;
import org.digijava.module.aim.dbentity.AmpRegionalFunding;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.CMSContentItem;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.form.ProposedProjCost;
import org.digijava.module.aim.helper.ActivityDocumentsUtil;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.ApplicationSettings;
import org.digijava.module.aim.helper.Components;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.Currency;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.CustomField;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.Documents;
import org.digijava.module.aim.helper.FilterParams;
import org.digijava.module.aim.helper.FinancingBreakdown;
import org.digijava.module.aim.helper.FinancingBreakdownWorker;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.Funding;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.aim.helper.FundingOrganization;
import org.digijava.module.aim.helper.FundingValidator;
import org.digijava.module.aim.helper.Issues;
import org.digijava.module.aim.helper.Location;
import org.digijava.module.aim.helper.MTEFProjection;
import org.digijava.module.aim.helper.Measures;
import org.digijava.module.aim.helper.OrgProjectId;
import org.digijava.module.aim.helper.PhysicalProgress;
import org.digijava.module.aim.helper.RegionalFunding;
import org.digijava.module.aim.helper.RelatedLinks;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.logic.FundingCalculationsHelper;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.ComponentsUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DecimalWraper;
import org.digijava.module.aim.util.DocumentUtil;
import org.digijava.module.aim.util.EUActivityUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.contentrepository.action.SelectDocumentDM;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;
import org.digijava.module.gateperm.core.GatePermConst;

/**
 * Loads the activity details of the activity specified in the form bean
 * variable 'activityId' to the EditActivityForm bean instance
 *
 * @author Priyajith
 */
public class EditActivity
    extends Action {

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
    boolean isPreview=mapping.getPath().trim().endsWith("viewActivityPreview");
    
    

    AmpActivity activity = null;
    String computeTotals = FeaturesUtil.getGlobalSettingValue(Constants.
        GLOBALSETTINGS_COMPUTE_TOTALS);

    boolean debug = (request.getParameter("debug")!=null)?true:false;

    //if("true".compareTo(request.getParameter("public"))!=0)
    //return mapping.findForward("forward");

    ActionErrors errors = new ActionErrors();

    ampContext = getServlet().getServletContext();

    // if user is not logged in, forward him to the home page
    if (session.getAttribute("currentMember") == null &&
        request.getParameter("edit") != null)
      if ("true".compareTo(request.getParameter("public")) != 0)
        return mapping.findForward("index");

    boolean isPublicView = (request.getParameter("public")==null)?false:request.getParameter("public").equals("true");
    EditActivityForm eaForm = (EditActivityForm) form; // form bean instance
    Long activityId = eaForm.getActivityId();
    
  
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
    if (activityId != null) {
        activity = ActivityUtil.getAmpActivity(activityId);
        eaForm.setWasDraft(activity.isCreatedAsDraft());
        if(activity!=null)
        {
        	Map scope=new HashMap();
        	scope.put(GatePermConst.ScopeKeys.CURRENT_MEMBER, tm);
        	gatePermEditAllowed = activity.canDo(GatePermConst.Actions.EDIT, scope);
        }
    }


    //old permission checking - this will be replaced by a global gateperm stuff
    // Checking whether the user have write access to the activity

    if (!gatePermEditAllowed) {
			if (!mapping.getPath().trim().endsWith("viewActivityPreview")) {
				// if (! ("Team".equalsIgnoreCase(tm.getTeamAccessType()))&&
				// !("Donor".equalsIgnoreCase(tm.getTeamAccessType()))) {
				// errorMsgKey =
				// "error.aim.editActivity.userPartOfManagementTeam";
				// }
				// else
				if (tm.getWrite() == false) {
					//errorMsgKey = "error.aim.editActivity.noWritePermissionForUser";
				}
			} else {
				Collection euActs = EUActivityUtil.getEUActivities(activityId);
				request.setAttribute("costs", euActs);
			}

//			if (errorMsgKey.trim().length() > 0) {
//				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
//						errorMsgKey));
//				saveErrors(request, errors);
//
//				errorMsgKey = "error.aim.editActivity.userPartOfManagementTeam";
//				String url = "/aim/viewChannelOverview.do?ampActivityId="
//						+ activityId + "&tabIndex=0";
//				RequestDispatcher rd = getServlet().getServletContext()
//						.getRequestDispatcher(url);
//				rd.forward(request, response);
//				return null;
//
//			}
			
			//TODO this for tanzania. think we should have plugable rules cos all cantries have different rules.
			if (!isPreview && activity!=null && activity.getTeam()!=null && currentTeam!=null){
				AmpTeam activityTeam=activity.getTeam();
				//if user is member of same team to which activity belongs then it can be edited
				if (currentTeam.getComputation() != null && !currentTeam.getComputation()){
					if (!currentTeam.getAmpTeamId().equals(activityTeam.getAmpTeamId())){
						errorMsgKey="error.aim.editActivity.noWritePermissionForUser";
					}
				}
			}

			if (errorMsgKey.trim().length() > 0 && !isPreview) {
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
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
    // load all themes
    Collection themes = new ArrayList(ProgramUtil.getAllThemes());
    eaForm.setProgramCollection(themes);

    try {

      // Checking whether the activity is already opened for editing
      // by some other user

      eaForm.setReset(true);
      eaForm.setOrgSelReset(true);
//            eaForm.setSectorReset(true);
      eaForm.setLocationReset(true);
      eaForm.setPhyProgReset(true);
      eaForm.setDocReset(true);
      eaForm.setComponentReset(true);
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
      List nationalPlanObjectivePrograms=new ArrayList();
      List primaryPrograms=new ArrayList();
      List secondaryPrograms=new ArrayList();
      eaForm.getPrograms().setNationalPlanObjectivePrograms(nationalPlanObjectivePrograms);
      eaForm.getPrograms().setPrimaryPrograms(primaryPrograms);
      eaForm.getPrograms().setSecondaryPrograms(secondaryPrograms);

      if (tm != null && tm.getAppSettings() != null && tm.getAppSettings()
          .getCurrencyId() != null) {
              String currCode="";
              AmpCurrency curr=CurrencyUtil.
                  getAmpcurrency(
                      tm.getAppSettings()
                      .getCurrencyId());
              if(curr!=null){
                      currCode = curr.getCurrencyCode();
              }
              eaForm.setCurrCode(currCode);
      }

      /*List prLst = new ArrayList();
      if (eaForm.getActPrograms() == null) {
        eaForm.setActPrograms(prLst);
      }
      else {
        prLst = eaForm.getActPrograms();
        prLst.clear();
        eaForm.setActPrograms(prLst);
      }*/

   
       if(eaForm.getSteps()==null){
            List steps = ActivityUtil.getSteps();
            eaForm.setSteps(steps);
       }
        

      // checking its the activity is already opened for editing...
      if (activityMap != null && activityMap.containsValue(activityId)) {
        //logger.info("activity is in activityMap " + activityId);
        // The activity is already opened for editing
        synchronized (ampContext) {
          HashMap tsaMap = (HashMap) ampContext
              .getAttribute(Constants.TS_ACT_LIST);
          if (tsaMap != null) {
            Long timeStamp = (Long) tsaMap.get(activityId);
            if (timeStamp != null) {

              if ( (System.currentTimeMillis() - timeStamp
                    .longValue()) > Constants.MAX_TIME_LIMIT) {
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

                ampContext.setAttribute(Constants.EDIT_ACT_LIST,
                                        activityMap);
                ampContext.setAttribute(Constants.USER_ACT_LIST,
                                        userActList);
                ampContext.setAttribute(Constants.SESSION_LIST,
                                        sessList);
                ampContext.setAttribute(Constants.TS_ACT_LIST,
                                        tsaMap);

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
      //AMP-3461 When an activity is open by a user, an other user should be able to preview it
      if (!canEdit && !isPreview) {
        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
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

            ampContext.setAttribute(Constants.SESSION_LIST,
                                    sessList);
            ampContext.setAttribute(Constants.EDIT_ACT_LIST,
                                    activityMap);
            ampContext.setAttribute(Constants.USER_ACT_LIST,
                                    userActList);
            ampContext.setAttribute(Constants.TS_ACT_LIST, tsaList);
          }
          eaForm.setEditAct(true);
        }
        else {
        	if(session.getAttribute("report")!=null){
        		GroupReportData r = (GroupReportData) session.getAttribute("report");
        		TreeSet l = (TreeSet) r.getOwnerIds();
        		Iterator i = l.iterator();
        		Long prev = null, next = null;
        		while (i.hasNext()) {
        			Long e = (Long) i.next();
        			if (e.compareTo(activityId) == 0)
        				break;
        			else
        				prev = e;
        		}
        		if (i.hasNext())
        			next = (Long) i.next();
        		session.setAttribute("previousActivity", prev);
        		session.setAttribute("nextActivity", next);
        		request.setAttribute(Constants.ONLY_PREVIEW, "true");
        		logger.info("mapping does end with viewActivityPreview.do");
        	}
        }
      }


      logger.debug("step [before IF] : " + eaForm.getStep());

      if (step != null) {
    	  eaForm.setStep(step);
      }	else{
    	  eaForm.setStep("1");
      }

      eaForm.setReset(false);

      if (activityId != null) {
    	  /* Clearing Tanzania Adds */
    	  eaForm.setVote(null);
    	  eaForm.setSubVote(null);
    	  eaForm.setFY(null);
    	  eaForm.setSubProgram(null);
    	  eaForm.setProjectCode(null);
    	  eaForm.setGbsSbs(null);
    	  eaForm.getIdentification().setGovernmentApprovalProcedures(new Boolean(false));
    	  eaForm.getIdentification().setJointCriteria(new Boolean(false));
    	  /* END - Clearing Tanzania Adds */

    	  eaForm.getIdentification().setCrisNumber(null);
        /* Insert Categories */
        AmpCategoryValue ampCategoryValue = CategoryManagerUtil.
            getAmpCategoryValueFromList(CategoryConstants.ACCHAPTER_NAME,activity.getCategories());
        
        if (ampCategoryValue != null)
        	eaForm.getIdentification().setAcChapter(new Long(ampCategoryValue.getId()));

        ampCategoryValue = CategoryManagerUtil.getAmpCategoryValueFromList(
            CategoryConstants.ACCESSION_INSTRUMENT_NAME, activity.getCategories());
        if (ampCategoryValue != null)
          eaForm.getIdentification().setAccessionInstrument(new Long(ampCategoryValue.getId()));

        ampCategoryValue = CategoryManagerUtil.getAmpCategoryValueFromListByKey(
            CategoryConstants.ACTIVITY_STATUS_KEY, activity.getCategories());
        if (ampCategoryValue != null)
          eaForm.getPlanning().setStatusId(new Long(ampCategoryValue.getId()));

        ampCategoryValue = CategoryManagerUtil.getAmpCategoryValueFromListByKey(
            CategoryConstants.IMPLEMENTATION_LEVEL_KEY, activity.getCategories());
        if (ampCategoryValue != null)
          eaForm.getLocation().setLevelId(new Long(ampCategoryValue.getId()));

        ampCategoryValue = CategoryManagerUtil.getAmpCategoryValueFromListByKey(
                CategoryConstants.ACTIVITY_LEVEL_KEY, activity.getCategories());
            if (ampCategoryValue != null)
              eaForm.setActivityLevel(new Long(ampCategoryValue.getId()));


        ampCategoryValue = CategoryManagerUtil.getAmpCategoryValueFromListByKey(
                CategoryConstants.FINANCIAL_INSTRUMENT_KEY, activity.getCategories());
            if (ampCategoryValue != null)
              eaForm.setGbsSbs(new Long(ampCategoryValue.getId()));

        ampCategoryValue = CategoryManagerUtil.getAmpCategoryValueFromListByKey(
                CategoryConstants.IMPLEMENTATION_LOCATION_KEY, activity.getCategories());
            if (ampCategoryValue != null)
              eaForm.getLocation().setImplemLocationLevel(new Long(ampCategoryValue.getId()));
            
        ampCategoryValue = CategoryManagerUtil.getAmpCategoryValueFromListByKey(
                    CategoryConstants.PROJECT_CATEGORY_KEY, activity.getCategories());
            if (ampCategoryValue != null)
                  eaForm.getIdentification().setProjectCategory(new Long(ampCategoryValue.getId()));

            

        /* End - Insert Categories */ 

        /* Injecting documents into session */
        SelectDocumentDM.clearContentRepositoryHashMap(request);
        if (activity.getActivityDocuments() != null && activity.getActivityDocuments().size() > 0 )
        		ActivityDocumentsUtil.injectActivityDocuments(request, activity.getActivityDocuments());
        
        eaForm.setCrDocuments( DocumentManagerUtil.createDocumentDataCollectionFromSession(request) );
        /* END - Injecting documents into session */

        /* Clearing session information about comments */
        String action = request.getParameter("action");
        if (action != null && action.trim().length() != 0) {
          if ("edit".equals(action)) {
        	if (eaForm.getCommentsCol() != null)
        			eaForm.getCommentsCol().clear();
        	else
        		eaForm.setCommentsCol(new ArrayList<AmpComments>());
            eaForm.setCommentFlag(false);
            /**
             * The commentColInSession session attribute is a map of lists.
             * Each list contains the AmpComments for a specific field. So the keys are the fields' ids.
             * It isn't really needed anymore except for compatibility with previewLogframe which
             * still uses this map.
             */
            HashMap<Long, List<AmpComments>> commentColInSession	= (HashMap)request.getSession().getAttribute("commentColInSession");
            if ( commentColInSession != null ) {
            	commentColInSession.clear();
            }
            else {
            	commentColInSession		= new HashMap<Long, List<AmpComments>>();
            	request.getSession().setAttribute("commentColInSession", commentColInSession );
            }
            AmpComments.populateWithComments( eaForm.getCommentsCol(), commentColInSession,
            					activityId);
          }
        }
        /* END - Clearing session information about comments */

        // load the activity details
        String actApprovalStatus = DbUtil.getActivityApprovalStatus(
            activityId);
       // HttpSession session = request.getSession();
        
        //eaForm.setApprovalStatus(actApprovalStatus);
        if (tm != null) {
            if ("true".compareTo((String) session.getAttribute("teamLeadFlag"))==0 && tm.getTeamId().equals(activity.getTeam().getAmpTeamId())){ 
              eaForm.setApprovalStatus(Constants.APPROVED_STATUS);
              AmpTeamMember teamMember = TeamMemberUtil.getAmpTeamMember(tm.getMemberId());
			  eaForm.setApprovedBy(teamMember);
			  eaForm.setApprovalDate(new Date());
			  eaForm.setApprovalStatus(Constants.APPROVED_STATUS);
			  }

            else{
              //eaForm.setApprovalStatus(Constants.STARTED_STATUS);//actApprovalStatus);
            	eaForm.setApprovalStatus(Constants.EDITED_STATUS);
            }
        }

        if (activity != null) {
          // set title,description and objective

          ProposedProjCost pg = new ProposedProjCost();
          if (activity.getFunAmount() != null)
            pg.setFunAmountAsDouble(activity.getFunAmount());
          pg.setCurrencyCode(activity.getCurrencyCode());
          pg.setFunDate(FormatHelper.formatDate(activity.getFunDate()));
          eaForm.getFunding().setProProjCost(pg);

          //load programs by type
          if(ProgramUtil.getAmpActivityProgramSettingsList()!=null){
                       List activityNPO=ActivityUtil.getActivityProgramsByProgramType(activityId,ProgramUtil.NATIONAL_PLAN_OBJECTIVE);
                       List activityPP=ActivityUtil.getActivityProgramsByProgramType(activityId,ProgramUtil.PRIMARY_PROGRAM);
                       List activitySP=ActivityUtil.getActivityProgramsByProgramType(activityId,ProgramUtil.SECONDARY_PROGRAM);
                       eaForm.getPrograms().setNationalPlanObjectivePrograms(activityNPO);
                       eaForm.getPrograms().setPrimaryPrograms(activityPP);
                       eaForm.getPrograms().setSecondaryPrograms(activitySP);
                       eaForm.getPrograms().setNationalSetting(ProgramUtil.getAmpActivityProgramSettings(ProgramUtil.NATIONAL_PLAN_OBJECTIVE));
                       eaForm.getPrograms().setPrimarySetting(ProgramUtil.getAmpActivityProgramSettings(ProgramUtil.PRIMARY_PROGRAM));
                       eaForm.getPrograms().setSecondarySetting(ProgramUtil.getAmpActivityProgramSettings(ProgramUtil.SECONDARY_PROGRAM));
            }


         /* try {
            List actPrgs = new ArrayList();
            Set prgSet = activity.getActivityPrograms();
            if (prgSet != null) {
              Iterator prgItr = prgSet.iterator();
              while (prgItr.hasNext()) {
                AmpTheme prg = (AmpTheme) prgItr.next();
                String newName = ProgramUtil.getHierarchyName(prg);
                prg.setProgramviewname(newName);
                actPrgs.add(prg);
                 }
            }

            eaForm.setActPrograms(actPrgs);
          }

          catch (Exception ex) {
            ex.printStackTrace();
          }
          */
          eaForm.getIdentification().setTitle(activity.getName().trim());
          eaForm.setCosts(new ArrayList(activity.getCosts()));
          eaForm.setTeam(activity.getTeam());
          eaForm.setCreatedBy(activity.getActivityCreator());
          eaForm.setUpdatedBy(activity.getUpdatedBy());
          eaForm.getIdentification().setBudgetCheckbox(activity.getBudget().toString());
          eaForm.getIdentification().setHumanitarianAid(activity.getHumanitarianAid());
          eaForm.getIdentification().setGovAgreementNumber(activity.getGovAgreementNumber());
          eaForm.getIdentification().setBudgetCodeProjectID(activity.getBudgetCodeProjectID());

          /*
           * Tanzania adds
           */
          if (activity.getFY() != null)
            eaForm.setFY(activity.getFY().trim());
          if (activity.getVote() != null)
            eaForm.setVote(activity.getVote().trim());
          if (activity.getSubVote() != null)
            eaForm.setSubVote(activity.getSubVote().trim());
          if (activity.getSubProgram() != null)
            eaForm.setSubProgram(activity.getSubProgram().trim());
          if (activity.getProjectCode() != null)
            eaForm.setProjectCode(activity.getProjectCode().trim());

          if (activity.getGbsSbs() != null)
            eaForm.setGbsSbs(activity.getGbsSbs());

          if (activity.isGovernmentApprovalProcedures() != null)
            eaForm.getIdentification().setGovernmentApprovalProcedures(activity.isGovernmentApprovalProcedures());
          else
            eaForm.getIdentification().setGovernmentApprovalProcedures(new Boolean(false));

          if (activity.isJointCriteria() != null)
            eaForm.getIdentification().setJointCriteria(activity.isJointCriteria());
          else
            activity.setJointCriteria(new Boolean(false));
          
          if (activity.isHumanitarianAid() != null)
        	  eaForm.getIdentification().setHumanitarianAid(activity.isHumanitarianAid());
          else
        	  activity.setHumanitarianAid(new Boolean(false));


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

        
    	eaForm.getIdentification().setActivitySummary(activity.getActivitySummary());

  
    	eaForm.getIdentification().setContractingArrangements(activity.getContractingArrangements());

  
    	eaForm.getIdentification().setCondSeq(activity.getCondSeq());

  
    	eaForm.getIdentification().setLinkedActivities(activity.getLinkedActivities());

  
    	eaForm.getIdentification().setConditionality(activity.getConditionality());

  
    	eaForm.getIdentification().setProjectManagement(activity.getProjectManagement());
  
  
  	eaForm.setContractDetails(activity.getContractDetails());
  	

  		eaForm.setConvenioNumcont(activity.getConvenioNumcont());
  		eaForm.setClasiNPD(activity.getClasiNPD());
          if (activity.getObjective() != null)
            eaForm.getIdentification().setObjectives(activity.getObjective().trim());
          
          if (activity.getPurpose() != null)
            eaForm.getIdentification().setPurpose(activity.getPurpose().trim());
          
          if (activity.getResults() != null)
            eaForm.getIdentification().setResults(activity.getResults());
          
          if (activity.getDocumentSpace() == null ||
              activity.getDocumentSpace().trim().length() == 0) {
            if (tm != null && DocumentUtil.isDMEnabled()) {
              eaForm.setDocumentSpace("aim-document-space-" +
                                      tm.getMemberId() +
                                      "-" + System.currentTimeMillis());
              Site currentSite = RequestUtils.getSite(request);
              DocumentUtil.createDocumentSpace(currentSite,
                                               eaForm.getDocumentSpace());
            }
          }
          else {
            eaForm.setDocumentSpace(activity.getDocumentSpace().
                                    trim());
          }
          eaForm.setAmpId(activity.getAmpId());
          eaForm.getPlanning().setStatusReason(activity.getStatusReason());

          if (null != activity.getLineMinRank())
            eaForm.getPlanning().setLineMinRank(activity.getLineMinRank().
                                  toString());
          else
            eaForm.getPlanning().setLineMinRank("-1");
          if (null != activity.getPlanMinRank())
            eaForm.getPlanning().setPlanMinRank(activity.getPlanMinRank().
                                  toString());
          else
            eaForm.getPlanning().setPlanMinRank("-1");
          
          eaForm.getPlanning().setActRankCollection(new ArrayList());
          for(int i = 1; i < 6; i++) {
            eaForm.getPlanning().getActRankCollection().add(new Integer(i));
          }

          eaForm.setCreatedDate(DateConversion
                                .ConvertDateToString(activity.
              getCreatedDate()));
          eaForm.setUpdatedDate(DateConversion
                                .ConvertDateToString(activity.
              getUpdatedDate()));
          eaForm.getPlanning().setOriginalAppDate(DateConversion
                                    .ConvertDateToString(activity
              .getProposedApprovalDate()));
          eaForm.getPlanning().setRevisedAppDate(DateConversion
                                   .ConvertDateToString(activity
              .getActualApprovalDate()));
          eaForm.getPlanning().setOriginalStartDate(DateConversion
                                      .ConvertDateToString(activity
              .getProposedStartDate()));
          eaForm.getPlanning().setRevisedStartDate(DateConversion
                                   .ConvertDateToString(activity
              .getActualStartDate()));
          eaForm.getPlanning().setContractingDate(DateConversion
                                    .ConvertDateToString(activity.
              getContractingDate()));
          eaForm.getPlanning().setDisbursementsDate(DateConversion
                                      .ConvertDateToString(activity.
              getDisbursmentsDate()));
          eaForm.getPlanning().setProposedCompDate(DateConversion
                                     .ConvertDateToString(activity.
              getOriginalCompDate()));

          eaForm.getPlanning().setCurrentCompDate(DateConversion
                                    .ConvertDateToString(activity
              .getActualCompletionDate()));

          eaForm.getPlanning().setProposedCompDate(DateConversion.ConvertDateToString(
              activity.getProposedCompletionDate()));

          Collection col = activity.getClosingDates();
          List dates = new ArrayList();
          if (col != null && col.size() > 0) {
            Iterator itr = col.iterator();
            while (itr.hasNext()) {
              AmpActivityClosingDates cDate = (
                  AmpActivityClosingDates) itr
                  .next();
              if (cDate.getType().intValue() == Constants.REVISED
                  .intValue()) {
                dates.add(DateConversion
                          .ConvertDateToString(cDate
                                               .getClosingDate()));
              }
            }
          }

          Collections.sort(dates, DateConversion.dtComp);
          eaForm.setActivityCloseDates(dates);

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
            Collection locs = new ArrayList();

            Iterator locIter = ampLocs.iterator();
            boolean maxLevel = false;
            while (locIter.hasNext()) {
            	AmpActivityLocation actLoc = (AmpActivityLocation) locIter.next();	//AMP-2250
            	if (actLoc == null)
            		continue;
            	AmpLocation loc=actLoc.getLocation();								//AMP-2250
              if (!maxLevel) {
                if (loc.getAmpWoreda() != null) {
                  impLevel = 3;
                  maxLevel = true;
                }
                else if (loc.getAmpZone() != null
                         && impLevel < 2) {
                  impLevel = 2;
                }
                else if (loc.getAmpRegion() != null
                         && impLevel < 1) {
                  impLevel = 1;
                }
              }

              if (loc != null) {
                Location location = new Location();
                location.setLocId(loc.getAmpLocationId());
                Collection col1 = FeaturesUtil.getDefaultCountryISO();
                String ISO = null;
                Iterator itr1 = col1.iterator();
                while (itr1.hasNext()) {
                  AmpGlobalSettings ampG = (AmpGlobalSettings) itr1.next();
                  ISO = ampG.getGlobalSettingsValue();
                }
                logger.info(" this is the settings Value" + ISO);
                //Country cntry = DbUtil.getDgCountry(Constants.COUNTRY_ISO);
                Country cntry = DbUtil.getDgCountry(ISO);
                location.setCountryId(cntry.getCountryId());
                location.setCountry(cntry.getCountryName());
                location.setNewCountryId(cntry.getIso());
                if (loc.getAmpRegion() != null) {
                  location.setRegion(loc.getAmpRegion()
                                     .getName());
                  location.setRegionId(loc.getAmpRegion()
                                       .getAmpRegionId());
                  if (eaForm.getFundingRegions() == null) {
                    eaForm
                        .setFundingRegions(new ArrayList());
                  }
                  if (eaForm.getFundingRegions().contains(
                      loc.getAmpRegion()) == false) {
                    eaForm.getFundingRegions().add(
                        loc.getAmpRegion());
                  }
                }
                if (loc.getAmpZone() != null) {
                  location
                      .setZone(loc.getAmpZone().getName());
                  location.setZoneId(loc.getAmpZone()
                                     .getAmpZoneId());
                }
                if (loc.getAmpWoreda() != null) {
                  location.setWoreda(loc.getAmpWoreda()
                                     .getName());
                  location.setWoredaId(loc.getAmpWoreda()
                                       .getAmpWoredaId());
                }

                if(actLoc.getLocationPercentage()!=null)
                location.setPercent(FormatHelper.formatNumber( actLoc.getLocationPercentage().doubleValue()));

                locs.add(location);
              }
            }
            eaForm.getLocation().setSelectedLocs(locs);
          }

       
          eaForm.setAllReferenceDocNameIds(null);
          eaForm.setReferenceDocs(null);


          eaForm=setComponentesToForm(eaForm, activity);
          eaForm=setSectorsToForm(eaForm, activity);

          if (activity.getThemeId() != null) {
            eaForm
                .setProgram(activity.getThemeId()
                            .getAmpThemeId());
          }
          if (activity.getProgramDescription() != null)
        	  eaForm.setProgramDescription(activity
                                       .getProgramDescription().trim());

          
          
          FundingCalculationsHelper calculations=new FundingCalculationsHelper();    
          String toCurrCode=null;
          if (tm != null)
              toCurrCode = CurrencyUtil.getAmpcurrency(tm.getAppSettings().getCurrencyId()).getCurrencyCode();
          calculations.setDebug(debug);

                    ArrayList fundingOrgs = new ArrayList();
                    Iterator fundItr = activity.getFunding().iterator();
                    while(fundItr.hasNext()) {
                        AmpFunding ampFunding = (AmpFunding) fundItr.next();
                        AmpOrganisation org = ampFunding.getAmpDonorOrgId();
                        FundingOrganization fundOrg = new FundingOrganization();
                        fundOrg.setAmpOrgId(org.getAmpOrgId());
                        fundOrg.setOrgName(org.getName());

                        fundOrg.setFundingActive(ampFunding.getActive());
                        fundOrg.setDelegatedCooperation(ampFunding.getDelegatedCooperation());
                        fundOrg.setDelegatedPartner(ampFunding.getDelegatedPartner());

                        if ( fundOrg.getDelegatedCooperation()!=null && fundOrg.getDelegatedCooperation() ) {
                        	fundOrg.setDelegatedCooperationString("checked");
                        }
                        else
                        	fundOrg.setDelegatedCooperationString("unchecked");

                        if ( fundOrg.getDelegatedPartner()!=null && fundOrg.getDelegatedPartner() ) {
                        	fundOrg.setDelegatedPartnerString("checked");
                        }
                        else
                        	fundOrg.setDelegatedPartnerString("unchecked");

                        int index = fundingOrgs.indexOf(fundOrg);
                        //logger.info("Getting the index as " + index
                        //	+ " for fundorg " + fundOrg.getOrgName());
                        if(index > -1) {
                            fundOrg = (FundingOrganization) fundingOrgs
                                .get(index);
                        }


			            Funding fund = new Funding();
			            //fund.setAmpTermsAssist(ampFunding.getAmpTermsAssistId());
			            fund.setTypeOfAssistance(ampFunding.getTypeOfAssistance());
			            fund.setFundingId(ampFunding.getAmpFundingId().
			                              longValue());
			            fund.setOrgFundingId(ampFunding.getFinancingId());
			            fund.setFinancingInstrument(ampFunding.getFinancingInstrument());
			            fund.setConditions(ampFunding.getConditions());
			            fund.setDonorObjective(ampFunding.getDonorObjective());

			            /* Get MTEF Projections */
			            ArrayList<MTEFProjection> MTEFProjections	= new ArrayList<MTEFProjection>();
			            if (ampFunding.getMtefProjections() != null) {
			            	Iterator<AmpFundingMTEFProjection> iterMtef	= ampFunding.getMtefProjections().iterator();
			            	while ( iterMtef.hasNext() ) {
				            	AmpFundingMTEFProjection ampProjection		= iterMtef.next();
				            	MTEFProjection	projection					= new MTEFProjection();

				            	projection.setAmount( FormatHelper.formatNumber(ampProjection.getAmount()) + "" );
				            	if ( ampProjection.getProjected() != null )
				            		projection.setProjected( ampProjection.getProjected().getId() );
				            	else
				            		logger.error("Projection with date " + ampProjection.getProjectionDate() + " has no type (neither projection nor pipeline) !!!!");
				            	projection.setCurrencyCode( ampProjection.getAmpCurrency().getCurrencyCode() );
				            	projection.setCurrencyName( ampProjection.getAmpCurrency().getCurrencyName() );
				            	projection.setProjectionDate( DateConversion.ConvertDateToString(ampProjection.getProjectionDate()) );
				            	//projection.setIndex();
				            	projection.setAmpFunding( ampProjection.getAmpFunding() );
				            	MTEFProjections.add(projection);
			            	}

			            }
			            Collections.sort(MTEFProjections);
			            fund.setMtefProjections(MTEFProjections);
			            /* END - Get MTEF Projections */

			            Collection fundDetails = ampFunding.getFundingDetails();
			            if (fundDetails != null && fundDetails.size() > 0) {
			            //  Iterator fundDetItr = fundDetails.iterator();
			             // long indexId = System.currentTimeMillis();
			        
			            calculations.doCalculations(fundDetails, toCurrCode);
			            
			            List<FundingDetail> fundDetail = calculations.getFundDetailList();
			            if(isPreview){
                        Iterator fundingIterator = fundDetail.iterator();
                         while(fundingIterator.hasNext())
                         {
                         	FundingDetail currentFundingDetail = (FundingDetail)fundingIterator.next();
                         	
                         	if(currentFundingDetail.getFixedExchangeRate() == null)
                         	{
                            	Double currencyAppliedAmount = getAmountInDefaultCurrency(currentFundingDetail, tm.getAppSettings());

                            	String currentAmount = FormatHelper.formatNumber(currencyAppliedAmount);
                            	currentFundingDetail.setTransactionAmount(currentAmount);
                            	currentFundingDetail.setCurrencyCode(CurrencyUtil.getAmpcurrency(tm.getAppSettings().getCurrencyId() ).getCurrencyCode());
                         	}
                         	else
                         	{
                         		Double fixedExchangeRate = currentFundingDetail.getFixedExchangeRate();
                         		Double currencyAppliedAmount = CurrencyWorker.convert1(FormatHelper.parseDouble(currentFundingDetail.getTransactionAmount()),fixedExchangeRate,1);
                            	String currentAmount = FormatHelper.formatNumber(currencyAppliedAmount);
                            	currentFundingDetail.setTransactionAmount(currentAmount);
                            	currentFundingDetail.setCurrencyCode(CurrencyUtil.getAmpcurrency(tm.getAppSettings().getCurrencyId() ).getCurrencyCode());
                         	}
                         }
		            }
			            
			              if (fundDetail != null)
			                Collections.sort(fundDetail,
			                                 FundingValidator.dateComp);
			              fund.setFundingDetails(fundDetail);
			              fund.setAmpFundingDetails(fundDetails);
			              eaForm.getFunding().setFundingDetails(fundDetail);
			              // funding.add(fund);
			            }
			            if (fundOrg.getFundings() == null)
			              fundOrg.setFundings(new ArrayList());
			            fundOrg.getFundings().add(fund);

			            if (index > -1) {
			              fundingOrgs.set(index, fundOrg);
			              //	logger
			              //		.info("Setting the fund org obj to the index :"
			              //			+ index);
			            }
			            else {
			              fundingOrgs.add(fundOrg);
			              //	logger.info("Adding new fund org object");
			            }
          }
                 
          //Added for the calculation of the subtotal per Organization          
          Iterator<FundingOrganization> iterFundOrg = fundingOrgs.iterator();
          while (iterFundOrg.hasNext())
          {
        	  FundingOrganization currFundingOrganization = iterFundOrg.next();
        	  Iterator<Funding> iterFunding = currFundingOrganization.getFundings().iterator();
        	  while(iterFunding.hasNext()){
        		  Funding currFunding = iterFunding.next();
                  FundingCalculationsHelper calculationsSubtotal=new FundingCalculationsHelper();  
                  if(currFunding.getAmpFundingDetails()!=null){
	                  try{
		                  calculationsSubtotal.doCalculations(currFunding.getAmpFundingDetails(), toCurrCode);
		        		  currFunding.setSubtotalPlannedCommitments(FormatHelper.formatNumber(calculationsSubtotal.getTotPlannedComm().doubleValue()));
		        		  currFunding.setSubtotalActualCommitments(FormatHelper.formatNumber(calculationsSubtotal.getTotActualComm().doubleValue()));
		        		  currFunding.setSubtotalPlannedDisbursements(FormatHelper.formatNumber(calculationsSubtotal.getTotPlanDisb().doubleValue()));
		        		  currFunding.setSubtotalDisbursements(FormatHelper.formatNumber(calculationsSubtotal.getTotActualDisb().doubleValue()));
		        		  currFunding.setSubtotalPlannedExpenditures(FormatHelper.formatNumber(calculationsSubtotal.getTotPlannedExp().doubleValue()));
		        		  currFunding.setSubtotalExpenditures(FormatHelper.formatNumber(calculationsSubtotal.getTotActualExp().doubleValue()));
		        		  currFunding.setSubtotalActualDisbursementsOrders(FormatHelper.formatNumber(calculationsSubtotal.getTotActualDisbOrder().doubleValue()));
		        		  currFunding.setUnDisbursementBalance(FormatHelper.formatNumber(calculationsSubtotal.getUnDisbursementsBalance().doubleValue()));
		        		  currFunding.setAmpFundingDetails(null);
		        		  //TODO:aca se setearia el resto
	                  }
	                  catch(Exception ex){
	                	  ex.printStackTrace();
	                  }
                  }
        	  }
          }

                    
          //logger.info("size = " + fundingOrgs);
          eaForm.getFunding().setFundingOrganizations(fundingOrgs);
          //get the total depend of the 
         
          if(debug){
        	  eaForm.getFunding().setTotalCommitments(calculations.getTotalCommitments().getCalculations());
        	  eaForm.getFunding().setTotalCommitmentsDouble(calculations.getTotalCommitments()
        			  .getValue().doubleValue());
        	  
        	  eaForm.getFunding().setTotalDisbursements(calculations.getTotActualDisb().getCalculations());
        	  eaForm.getFunding().setTotalPlannedDisbursements(calculations.getTotPlanDisb().getCalculations());
        	  eaForm.getFunding().setTotalExpenditures(calculations.getTotPlannedExp().getCalculations());  
        	  eaForm.getFunding().setTotalPlannedCommitments(calculations.getTotPlannedComm().getCalculations());
        	  eaForm.getFunding().setTotalPlannedExpenditures(calculations.getTotPlannedExp().getCalculations());
        	  eaForm.getFunding().setTotalActualDisbursementsOrders(calculations.getTotActualDisbOrder().getCalculations());
        	  eaForm.getFunding().setTotalPlannedDisbursementsOrders(calculations.getTotPlannedDisbOrder().getCalculations());
        	  eaForm.getFunding().setUnDisbursementsBalance(calculations.getUnDisbursementsBalance().getCalculations());
          }
          else{
              	//actual
      		  eaForm.getFunding().setTotalCommitments(calculations.getTotalCommitments().toString());
        	  eaForm.getFunding().setTotalDisbursements(calculations.getTotActualDisb().toString());
        	  eaForm.getFunding().setTotalExpenditures(calculations.getTotActualExp().toString());
        	  eaForm.getFunding().setTotalActualDisbursementsOrders(calculations.getTotActualDisbOrder().toString());
        	  //planned
        	  eaForm.getFunding().setTotalPlannedDisbursements(calculations.getTotPlanDisb().toString());
        	  eaForm.getFunding().setTotalPlannedCommitments(calculations.getTotPlannedComm().toString());
        	  eaForm.getFunding().setTotalPlannedExpenditures(calculations.getTotPlannedExp().toString());
        	  eaForm.getFunding().setTotalPlannedDisbursementsOrders(calculations.getTotPlannedDisbOrder().toString());
        	  eaForm.getFunding().setUnDisbursementsBalance(calculations.getUnDisbursementsBalance().toString());
          }
          ArrayList regFunds = new ArrayList(); 
          Iterator rItr = activity.getRegionalFundings().iterator();

          eaForm.setRegionTotalDisb(0);
          while (rItr.hasNext()) {
            AmpRegionalFunding ampRegFund = (AmpRegionalFunding)
                rItr
                .next();

            double disb = 0;
            if (ampRegFund.getAdjustmentType().intValue() == 1 &&
                ampRegFund.getTransactionType().intValue() == 1)
              disb = ampRegFund.getTransactionAmount().
                  doubleValue();
            //if(!ampCompFund.getCurrency().getCurrencyCode().equals("USD")) {
            //double toRate=1;

            //	disb/=ARUtil.getExchange(ampCompFund.getCurrency().getCurrencyCode(),new java.sql.Date(ampCompFund.getTransactionDate().getTime()));
            //}
            eaForm.setRegionTotalDisb(eaForm.getRegionTotalDisb() +
                                      disb);

            FundingDetail fd = new FundingDetail();
            fd.setAdjustmentType(ampRegFund.getAdjustmentType()
                                 .intValue());
            if (fd.getAdjustmentType() == 1) {
              fd.setAdjustmentTypeName("Actual");
            }
            else if (fd.getAdjustmentType() == 0) {
              fd.setAdjustmentTypeName("Planned");
            }
            fd.setCurrencyCode(ampRegFund.getCurrency()
                               .getCurrencyCode());
            fd.setCurrencyName(ampRegFund.getCurrency()
                               .getCurrencyName());
            fd.setTransactionAmount(FormatHelper.formatNumber(
                                        ampRegFund
                                        .getTransactionAmount().doubleValue()));
            fd.setTransactionDate(DateConversion
                                  .ConvertDateToString(ampRegFund
                .getTransactionDate()));
            fd.setTransactionType(ampRegFund.getTransactionType()
                                  .intValue());

            RegionalFunding regFund = new RegionalFunding();
            regFund.setRegionId(ampRegFund.getRegion()
                                .getAmpRegionId());
            regFund.setRegionName(ampRegFund.getRegion().getName());

            if (regFunds.contains(regFund) == false) {
              regFunds.add(regFund);
            }

            int index = regFunds.indexOf(regFund);
            regFund = (RegionalFunding) regFunds.get(index);
            if (fd.getTransactionType() == 0) { // commitments
              if (regFund.getCommitments() == null) {
                regFund.setCommitments(new ArrayList());
              }
              regFund.getCommitments().add(fd);
            }
            else if (fd.getTransactionType() == 1) { // disbursements
              if (regFund.getDisbursements() == null) {
                regFund.setDisbursements(new ArrayList());
              }
              regFund.getDisbursements().add(fd);
            }
            else if (fd.getTransactionType() == 2) { // expenditures
              if (regFund.getExpenditures() == null) {
                regFund.setExpenditures(new ArrayList());
              }
              regFund.getExpenditures().add(fd);
            }
            regFunds.set(index, regFund);
          }

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

          eaForm.setRegionalFundings(regFunds);

          eaForm.setSelectedComponents(null);
          eaForm.setCompTotalDisb(0);

          if (activity.getComponents() != null && activity.getComponents().size() > 0) {
            getComponents(activity, eaForm, toCurrCode);
          }

          Collection memLinks = null;
          if (tm != null)
            memLinks = TeamMemberUtil.getMemberLinks(tm.getMemberId());
          Collection actDocs = activity.getDocuments();
          if (tm != null && actDocs != null && actDocs.size() > 0) {
            Collection docsList = new ArrayList();
            Collection linksList = new ArrayList();

            Iterator docItr = actDocs.iterator();
            while (docItr.hasNext()) {
              RelatedLinks rl = new RelatedLinks();

              CMSContentItem cmsItem = (CMSContentItem) docItr
                  .next();
              rl.setRelLink(cmsItem);
              if (tm != null)
                rl.setMember(TeamMemberUtil.getAmpTeamMember(tm
                    .getMemberId()));
              Iterator tmpItr = memLinks.iterator();
              while (tmpItr.hasNext()) {
                Documents doc = (Documents) tmpItr.next();
                if ( cmsItem.getDocType() != null)
                		doc.setDocType(cmsItem.getDocType().getValue());
                if (doc.getDocId().longValue() == cmsItem
                    .getId()) {
                  rl.setShowInHomePage(true);
                  break;
                }
              }

              if (cmsItem.getIsFile()) {
                docsList.add(rl);
              }
              else {
                linksList.add(rl);
              }
            }
            eaForm.setDocuments(DbUtil.getKnowledgeDocuments(eaForm.
                getActivityId()));
            eaForm.setDocumentList(docsList);
            eaForm.setLinksList(linksList);
          }
          Site currentSite = RequestUtils.getSite(request);
          eaForm.setManagedDocumentList(DocumentUtil.getDocumentsForActivity(currentSite, activity));
          eaForm.getAgencies().setExecutingAgencies(new ArrayList<AmpOrganisation>());
          eaForm.getAgencies().setImpAgencies(new ArrayList<AmpOrganisation>());
          eaForm.getAgencies().setBenAgencies(new ArrayList<AmpOrganisation>());
          eaForm.getAgencies().setConAgencies(new ArrayList<AmpOrganisation>());
          eaForm.getAgencies().setReportingOrgs(new ArrayList<AmpOrganisation>());
          eaForm.getAgencies().setSectGroups(new ArrayList<AmpOrganisation>());
          eaForm.getAgencies().setRegGroups(new ArrayList<AmpOrganisation>());
          eaForm.getAgencies().setRespOrganisations(new ArrayList<AmpOrganisation>());

          Set relOrgs = activity.getOrgrole();
          if (relOrgs != null) {
            Iterator relOrgsItr = relOrgs.iterator();
            while (relOrgsItr.hasNext()) {
              AmpOrgRole orgRole = (AmpOrgRole) relOrgsItr.next();
              if (orgRole.getRole().getRoleCode().equals(
                      Constants.RESPONSIBLE_ORGANISATION)
                      && (!eaForm.getAgencies().getRespOrganisations().contains(orgRole.getOrganisation()))) {
                	  eaForm.getAgencies().getRespOrganisations().add(orgRole.getOrganisation());
                 }          
              if (orgRole.getRole().getRoleCode().equals(
                  Constants.EXECUTING_AGENCY)
                  && (!eaForm.getAgencies().getExecutingAgencies().contains(orgRole.getOrganisation()))) {
            	  eaForm.getAgencies().getExecutingAgencies().add(orgRole.getOrganisation());
             }
              else if (orgRole.getRole().getRoleCode().equals(
                  Constants.IMPLEMENTING_AGENCY)
                       && (!eaForm.getAgencies().getImpAgencies().contains(
                           orgRole.getOrganisation()))) {
                eaForm.getAgencies().getImpAgencies().add(
                    orgRole.getOrganisation());
              }
              else if (orgRole.getRole().getRoleCode().equals(
                  Constants.BENEFICIARY_AGENCY)
                       && (!eaForm.getAgencies().getBenAgencies().contains(
                           orgRole.getOrganisation()))) {
                eaForm.getAgencies().getBenAgencies().add(
                    orgRole.getOrganisation());
              }
              else if (orgRole.getRole().getRoleCode().equals(
                  Constants.CONTRACTING_AGENCY)
                       && (!eaForm.getAgencies().getConAgencies().contains(
                           orgRole.getOrganisation()))) {
                eaForm.getAgencies().getConAgencies().add(
                    orgRole.getOrganisation());
              }
              else if (orgRole.getRole().getRoleCode().equals(
                  Constants.REPORTING_AGENCY)
                       && (!eaForm.getAgencies().getReportingOrgs().contains(
                           orgRole.getOrganisation()))) {
                eaForm.getAgencies().getReportingOrgs().add(
                    orgRole.getOrganisation());
              } else if (orgRole.getRole().getRoleCode().equals(
                      Constants.SECTOR_GROUP)
                      && (!eaForm.getAgencies().getSectGroups().contains(
                          orgRole.getOrganisation()))) {
               eaForm.getAgencies().getSectGroups().add(
                   orgRole.getOrganisation());
             } else if (orgRole.getRole().getRoleCode().equals(
                     Constants.REGIONAL_GROUP)
                     && (!eaForm.getAgencies().getRegGroups().contains(
                         orgRole.getOrganisation()))) {
              eaForm.getAgencies().getRegGroups().add(
                  orgRole.getOrganisation());
            }

            }
          }

          if (activity.getIssues() != null
              && activity.getIssues().size() > 0) {
            ArrayList issueList = new ArrayList();
            Iterator iItr = activity.getIssues().iterator();
            while (iItr.hasNext()) {
              AmpIssues ampIssue = (AmpIssues) iItr.next();
              Issues issue = new Issues();
              issue.setId(ampIssue.getAmpIssueId());
              issue.setName(ampIssue.getName());
              issue.setIssueDate(FormatHelper.formatDate(ampIssue.getIssueDate()));
              ArrayList measureList = new ArrayList();
              if (ampIssue.getMeasures() != null
                  && ampIssue.getMeasures().size() > 0) {
                Iterator mItr = ampIssue.getMeasures()
                    .iterator();
                while (mItr.hasNext()) {
                  AmpMeasure ampMeasure = (AmpMeasure) mItr
                      .next();
                  Measures measure = new Measures();
                  measure.setId(ampMeasure.getAmpMeasureId());
                  measure.setName(ampMeasure.getName());
                  ArrayList actorList = new ArrayList();
                  if (ampMeasure.getActors() != null
                      && ampMeasure.getActors().size() > 0) {
                    Iterator aItr = ampMeasure.getActors()
                        .iterator();
                    while (aItr.hasNext()) {
                      AmpActor actor = (AmpActor) aItr
                          .next();
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
            eaForm.setIssues(issueList);
          }
          else {
            eaForm.setIssues(null);
          }

          // loading the contact person details and condition
          eaForm.getContactInfo().setDnrCntFirstName(activity.getContFirstName());
          eaForm.getContactInfo().setDnrCntLastName(activity.getContLastName());
          eaForm.getContactInfo().setDnrCntEmail(activity.getEmail());
          eaForm.getContactInfo().setDnrCntTitle(activity.getDnrCntTitle());
          eaForm.getContactInfo().setDnrCntOrganization(activity.getDnrCntOrganization());
          eaForm.getContactInfo().setDnrCntPhoneNumber(activity.getDnrCntPhoneNumber());
          eaForm.getContactInfo().setDnrCntFaxNumber(activity.getDnrCntFaxNumber());

          eaForm.getContactInfo().setMfdCntFirstName(activity.getMofedCntFirstName());
          eaForm.getContactInfo().setMfdCntLastName(activity.getMofedCntLastName());
          eaForm.getContactInfo().setMfdCntEmail(activity.getMofedCntEmail());
          eaForm.getContactInfo().setMfdCntTitle(activity.getMfdCntTitle());
          eaForm.getContactInfo().setMfdCntOrganization(activity.getMfdCntOrganization());
          eaForm.getContactInfo().setMfdCntPhoneNumber(activity.getMfdCntPhoneNumber());
          eaForm.getContactInfo().setMfdCntFaxNumber(activity.getMfdCntFaxNumber());
          
          eaForm.getContactInfo().setPrjCoFirstName(activity.getPrjCoFirstName());
          eaForm.getContactInfo().setPrjCoLastName(activity.getPrjCoLastName());
          eaForm.getContactInfo().setPrjCoEmail(activity.getPrjCoEmail());
          eaForm.getContactInfo().setPrjCoTitle(activity.getPrjCoTitle());
          eaForm.getContactInfo().setPrjCoOrganization(activity.getPrjCoOrganization());
          eaForm.getContactInfo().setPrjCoPhoneNumber(activity.getPrjCoPhoneNumber());
          eaForm.getContactInfo().setPrjCoFaxNumber(activity.getPrjCoFaxNumber());
          
          eaForm.getContactInfo().setSecMiCntFirstName(activity.getSecMiCntFirstName());
          eaForm.getContactInfo().setSecMiCntLastName(activity.getSecMiCntLastName());
          eaForm.getContactInfo().setSecMiCntEmail(activity.getSecMiCntEmail());
          eaForm.getContactInfo().setSecMiCntTitle(activity.getSecMiCntTitle());
          eaForm.getContactInfo().setSecMiCntOrganization(activity.getSecMiCntOrganization());
          eaForm.getContactInfo().setSecMiCntPhoneNumber(activity.getSecMiCntPhoneNumber());
          eaForm.getContactInfo().setSecMiCntFaxNumber(activity.getSecMiCntFaxNumber());

          if (eaForm.getIsPreview() != 1 && !isPublicView) {
            AmpTeamMember teamMember = TeamMemberUtil.getAmpTeamMember(tm.
                getMemberId());
            activity.setActivityCreator(teamMember);
          }
          if (activity.getCondition() != null)
        	  eaForm.setConditions(activity.getCondition().trim());

          if (activity.getActivityCreator() != null) {
            User usr = activity.getActivityCreator().getUser();
            if (usr != null) {
              eaForm.setActAthFirstName(usr.getFirstNames());
              eaForm.setActAthLastName(usr.getLastName());
              eaForm.setActAthEmail(usr.getEmail());
              eaForm.setActAthAgencySource(usr.getOrganizationName());
            }
          }
        }
        
        Iterator<CustomField> itcf = eaForm.getCustomFields().iterator();
        while(itcf.hasNext()){
        	CustomField cf = itcf.next();
        	try{
        		String value = BeanUtils.getProperty(activity, cf.getAmpActivityPropertyName());
        		cf.setValue(value);
        	}catch(Exception e){
        		logger.error("Error getting property [" + cf.getAmpActivityPropertyName() + "] from bean ", e);
        	}
        }
        
      }
      //Collection statusCol = null;
      // load the status from the database
//            if(eaForm.getStatusCollection() == null) { // TO BE DELETED
//                statusCol = DbUtil.getAmpStatus();
//                eaForm.setStatusCollection(statusCol);
//            } else {
//                statusCol = eaForm.getStatusCollection();
//            }
      // Initailly setting the implementation level as "country"
      if (eaForm.getLocation().getImplemLocationLevel() == null)
        eaForm.getLocation().setImplemLocationLevel(
            CategoryManagerUtil.getAmpCategoryValueFromDb(CategoryConstants.
            IMPLEMENTATION_LOCATION_KEY, new Long(0)).getId()
            );

      //Collection modalColl = null;
      // load the modalities from the database
      /* if(eaForm.getModalityCollection() == null) { //No need to load modalitiees. Using category manager.
           modalColl = DbUtil.getAmpModality();
           eaForm.setModalityCollection(modalColl);
       } else {
           modalColl = eaForm.getModalityCollection();
       }*/

      // Initally set the modality as "Project Support"
      Collection financingInstrValues = CategoryManagerUtil.
          getAmpCategoryValueCollectionByKey(CategoryConstants.
                                             FINANCING_INSTRUMENT_KEY, null);
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
      eaForm.setCurrencies(CurrencyUtil.getAmpCurrency());
      
      //Only currencies havening exchanges rates AMP-2620
      ArrayList<AmpCurrency> validcurrencies = new ArrayList<AmpCurrency>();
      eaForm.getFunding().setValidcurrencies(validcurrencies);
      for (Iterator iter = eaForm.getCurrencies().iterator(); iter.hasNext();) {
		AmpCurrency element = (AmpCurrency) iter.next();
		 if( CurrencyUtil.isRate(element.getCurrencyCode())== true)
				{
			 	eaForm.getFunding().getValidcurrencies().add((CurrencyUtil.getCurrencyByCode(element.getCurrencyCode())));
				}
		}


      //load the possible projection values
      eaForm.getFunding().setProjections(CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.MTEF_PROJECTION_KEY, false));

    }
    catch (Exception e) {
      e.printStackTrace(System.out);
    }
    if (request.getParameter("logframepr") != null)
      if (request.getParameter("logframepr").compareTo("true") == 0) {
        session.setAttribute("logframepr", "true");
        return mapping.findForward("forwardToPreview");
      }

    Collection ampFundingsAux = DbUtil.getAmpFunding(activityId);
    FilterParams fp = (FilterParams) session.getAttribute("filterParams");
    TeamMember teamMember = (TeamMember) session.getAttribute("currentMember");
    if (fp == null) {
      fp = new FilterParams();
      int year = new GregorianCalendar().get(Calendar.YEAR);
		fp.setFromYear(year-Constants.FROM_YEAR_RANGE);
		fp.setToYear(year+Constants.TO_YEAR_RANGE);
    }

    ApplicationSettings apps = null;
    if (teamMember != null) {
      apps = teamMember.getAppSettings();
    }
    if (apps != null) {

      if (fp.getCurrencyCode() == null) {

        Currency curr = CurrencyUtil.getCurrency(apps.getCurrencyId());
        if (curr != null) {
          fp.setCurrencyCode(curr.getCurrencyCode());
        }

      }

      if (fp.getFiscalCalId() == null) {
        fp.setFiscalCalId(apps.getFisCalId());
      }

//      if (fp.getFromYear() == 0 || fp.getToYear() == 0) {
//        int year = new GregorianCalendar().get(Calendar.YEAR);
//        fp.setFromYear(year - Constants.FROM_YEAR_RANGE);
//        fp.setToYear(year + Constants.TO_YEAR_RANGE);
//      }

      Collection<FinancingBreakdown> fb = FinancingBreakdownWorker.getFinancingBreakdownList(
          activityId, ampFundingsAux, fp,debug);
      eaForm.setFinancingBreakdown(fb);
      String overallTotalCommitted = "";
      String overallTotalDisbursed = "";
      String overallTotalUnDisbursed = "";
      String overallTotalExpenditure = "";
      String overallTotalUnExpended = "";
      String overallTotalDisburOrder = "";
      
      overallTotalCommitted = FinancingBreakdownWorker.getOverallTotal(
          fb, Constants.COMMITMENT,debug);
      overallTotalDisbursed = FinancingBreakdownWorker.getOverallTotal(
          fb, Constants.DISBURSEMENT,debug);
      overallTotalDisburOrder=FinancingBreakdownWorker.getOverallTotal(
          fb, Constants.DISBURSEMENT_ORDER,debug);      
      if(!debug){
      overallTotalUnDisbursed = FormatHelper.getDifference(
          overallTotalCommitted, overallTotalDisbursed);
      }
      else{
    	  overallTotalUnDisbursed =overallTotalCommitted +"-" +overallTotalDisbursed; 
      }
      overallTotalExpenditure = FinancingBreakdownWorker.getOverallTotal(
          fb, Constants.EXPENDITURE,debug);
      if(!debug){
      overallTotalUnExpended = FormatHelper.getDifference(
          overallTotalDisbursed, overallTotalExpenditure);
      }
      else{
    	  overallTotalExpenditure = overallTotalDisbursed+ "-" + overallTotalExpenditure;
      }
      
      eaForm.getFunding().setTotalCommitted(overallTotalCommitted);
      eaForm.getFunding().setTotalDisbursed(overallTotalDisbursed);
      eaForm.getFunding().setTotalExpended(overallTotalExpenditure);
      eaForm.getFunding().setTotalUnDisbursed(overallTotalUnDisbursed);
      eaForm.getFunding().setTotalUnExpended(overallTotalUnExpended);
      eaForm.getFunding().setTotalDisbOrder(overallTotalDisburOrder);
    }
    String debugFM=request.getParameter("debugFM");
    if(debugFM!=null && "true".compareTo(debugFM)==0)
    	return mapping.findForward("forwardDebugFM");
    return mapping.findForward("forward");
  }

  private EditActivityForm setComponentesToForm(EditActivityForm form,AmpActivity activity){
		Collection<AmpActivityComponente> componentes = activity.getComponentes();

		if (componentes != null && componentes.size() > 0) {
			Collection activitySectors = new ArrayList();
			Iterator<AmpActivityComponente> sectItr = componentes.iterator();
			while (sectItr.hasNext()) {
				AmpActivityComponente ampActSect =  sectItr.next();
				if (ampActSect != null) {
					AmpSector sec = ampActSect.getSector();
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
						ActivitySector actCompo = new ActivitySector();
						if (parent != null) {
							actCompo.setId(parent.getAmpSectorId());
							String view = FeaturesUtil.getGlobalSettingValue("Allow Multiple Sectors");
							if (view != null)
								if (view.equalsIgnoreCase("On")) {
									actCompo.setCount(1);
								} else {
									actCompo.setCount(2);
								}

							actCompo.setSectorId(parent.getAmpSectorId());
							actCompo.setSectorName(parent.getName());
							if (subsectorLevel1 != null) {
								actCompo.setSubsectorLevel1Id(subsectorLevel1.getAmpSectorId());
								actCompo.setSubsectorLevel1Name(subsectorLevel1.getName());
								if (subsectorLevel2 != null) {
									actCompo.setSubsectorLevel2Id(subsectorLevel2.getAmpSectorId());
									actCompo.setSubsectorLevel2Name(subsectorLevel2.getName());
								}
							}
							actCompo.setSectorPercentage(ampActSect.getPercentage().floatValue());
						}
						activitySectors.add(actCompo);
					}
				}
			}

			form.getComponents().setActivityComponentes(activitySectors);
		}
		return form;
  }

  private EditActivityForm setSectorsToForm(EditActivityForm form, AmpActivity activity) {
		Collection sectors = activity.getSectors();

		if (sectors != null && sectors.size() > 0) {
			List<ActivitySector> activitySectors = new ArrayList<ActivitySector>();
			Iterator sectItr = sectors.iterator();
			while (sectItr.hasNext()) {
				AmpActivitySector ampActSect = (AmpActivitySector) sectItr.next();
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
							actSect.setSectorPercentage(ampActSect.getSectorPercentage());
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

  private Collection getSectosHelper(Collection sectors){
	  return null;
  }


/**
 * @param activity
 * @param eaForm
 * @param componets
 */
	private void getComponents(AmpActivity activity, EditActivityForm eaForm, String toCurrCode) {

		Collection componets = activity.getComponents();
		List<Components<FundingDetail>> selectedComponents = new ArrayList<Components<FundingDetail>>();
		Iterator compItr = componets.iterator();
		while (compItr.hasNext()) {
			AmpComponent temp = (AmpComponent) compItr.next();
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

			Collection<AmpComponentFunding> fundingComponentActivity = ActivityUtil.getFundingComponentActivity(tempComp.getComponentId(), activity.getAmpActivityId());
			Iterator cItr = fundingComponentActivity.iterator();
		
			while (cItr.hasNext()) {
				AmpComponentFunding ampCompFund = (AmpComponentFunding) cItr.next();

				double disb = 0;
				
				if (ampCompFund.getAdjustmentType().intValue() == 1 && ampCompFund.getTransactionType().intValue() == 1) 
				disb = ampCompFund.getTransactionAmount().doubleValue();

				eaForm.setCompTotalDisb(eaForm.getCompTotalDisb() + disb);
				
				FundingDetail fd = new FundingDetail();
				
				fd.setAdjustmentType(ampCompFund.getAdjustmentType().intValue());
				
				if (fd.getAdjustmentType() == 1) {
					fd.setAdjustmentTypeName("Actual");
				} else if (fd.getAdjustmentType() == 0) {
					fd.setAdjustmentTypeName("Planned");
				}
		
				fd.setAmpComponentFundingId(ampCompFund.getAmpComponentFundingId());
				
				//convert to  default currency 
				
				java.sql.Date dt = new java.sql.Date(ampCompFund.getTransactionDate().getTime());

				double frmExRt = ampCompFund.getExchangeRate() != null ? ampCompFund.getExchangeRate() : Util.getExchange(ampCompFund.getCurrency().getCurrencyCode(), dt);
				double toExRt = Util.getExchange(toCurrCode, dt);
				DecimalWraper amt = CurrencyWorker.convertWrapper(ampCompFund.getTransactionAmount(), frmExRt, toExRt, dt);

				
				fd.setCurrencyCode(toCurrCode);
				fd.setTransactionAmount(FormatHelper.formatNumber( amt.getValue()));
				
				
				fd.setCurrencyName(ampCompFund.getCurrency().getCurrencyName());
				fd.setTransactionDate(DateConversion.ConvertDateToString(ampCompFund.getTransactionDate()));
				
				fd.setTransactionType(ampCompFund.getTransactionType().intValue());
				
				if (fd.getTransactionType() == 0) {
				
					tempComp.getCommitments().add(fd);
				} else if (fd.getTransactionType() == 1) {
					tempComp.getDisbursements().add(fd);
					
				} else if (fd.getTransactionType() == 2) {
					tempComp.getExpenditures().add(fd);
				}
				
				
			}

			ComponentsUtil.calculateFinanceByYearInfo(tempComp, fundingComponentActivity);

			Collection<AmpPhysicalPerformance> phyProgress = ActivityUtil.getPhysicalProgressComponentActivity(tempComp.getComponentId(), activity.getAmpActivityId());

			if (phyProgress != null && phyProgress.size() > 0) {
				Collection physicalProgress = new ArrayList();
				Iterator phyProgItr = phyProgress.iterator();
				while (phyProgItr.hasNext()) {
					AmpPhysicalPerformance phyPerf = (AmpPhysicalPerformance) phyProgItr.next();
					PhysicalProgress phyProg = new PhysicalProgress();
					phyProg.setPid(phyPerf.getAmpPpId());
					phyProg.setDescription(phyPerf.getDescription());
					phyProg.setReportingDate(DateConversion.ConvertDateToString(phyPerf.getReportingDate()));
					phyProg.setTitle(phyPerf.getTitle());
					physicalProgress.add(phyProg);
				}
				tempComp.setPhyProgress(physicalProgress);
			}

			selectedComponents.add(tempComp);
		}

		// Sort the funding details based on Transaction date.
		Iterator compIterator = selectedComponents.iterator();
		int index = 0;
		while (compIterator.hasNext()) {
			Components components = (Components) compIterator.next();
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

		eaForm.setSelectedComponents(selectedComponents);
	}

	private double getAmountInDefaultCurrency(FundingDetail fundDet, ApplicationSettings appSet) {
		
		java.sql.Date dt = new java.sql.Date(DateConversion.getDate(fundDet.getTransactionDate()).getTime());
		double frmExRt = Util.getExchange(fundDet.getCurrencyCode(),dt);
		String toCurrCode = CurrencyUtil.getAmpcurrency( appSet.getCurrencyId() ).getCurrencyCode();
		double toExRt = Util.getExchange(toCurrCode,dt);
	
		double amt = CurrencyWorker.convert1(FormatHelper.parseDouble(fundDet.getTransactionAmount()),frmExRt,toExRt);
		
		return amt;
		
	}

}
