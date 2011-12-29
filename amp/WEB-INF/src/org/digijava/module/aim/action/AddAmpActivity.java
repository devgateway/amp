/*
 * AddAmpActivity.java
 */

package org.digijava.module.aim.action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.jcr.RepositoryException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.ReportData;
import org.dgfoundation.amp.utils.AmpCollectionUtils;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.kernel.util.collections.HierarchyDefinition;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpActivityContact;
import org.digijava.module.aim.dbentity.AmpActivityReferenceDoc;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpComments;
import org.digijava.module.aim.dbentity.AmpComponentType;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpField;
import org.digijava.module.aim.dbentity.AmpFieldsVisibility;
import org.digijava.module.aim.dbentity.AmpIndicatorRiskRatings;
import org.digijava.module.aim.dbentity.AmpModulesVisibility;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.CMSContentItem;
import org.digijava.module.aim.dbentity.EUActivity;
import org.digijava.module.aim.dbentity.IPAContract;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.form.EditActivityForm.ActivityContactInfo;
import org.digijava.module.aim.form.ProposedProjCost;
import org.digijava.module.aim.helper.ActivityIndicator;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.Documents;
import org.digijava.module.aim.helper.FundingOrganization;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.ReferenceDoc;
import org.digijava.module.aim.helper.RelatedLinks;
import org.digijava.module.aim.helper.SurveyFunding;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.ChapterUtil;
import org.digijava.module.aim.util.ComponentsUtil;
import org.digijava.module.aim.util.ContactInfoUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DocumentUtil;
import org.digijava.module.aim.util.EUActivityUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.IndicatorUtil;
import org.digijava.module.aim.util.MEIndicatorsUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.budget.dbentity.AmpDepartments;
import org.digijava.module.budget.helper.BudgetDbUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.contentrepository.action.SelectDocumentDM;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;
import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.editor.exception.EditorException;
import org.digijava.module.editor.util.Constants;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.util.PermissionUtil;
import org.hibernate.HibernateException;

/**
 * Used to capture the activity details to the form bean of type org.digijava.module.aim.form.EditActivityForm
 *
 * Add Activity is an eight step process with a preview at the last. The same action is used for all the eight
 * steps + preview. A form bean variable identified by the name 'step' is used for this purpose. When the user
 * clicks the next button in the jsp page, the value of the step is incremented by 1. Thus based on the value of
 * this variable the action forwards it to the eight steps and the preview.
 *
 * @author Priyajith
 */
public class AddAmpActivity extends Action {

  private static Logger logger = Logger.getLogger(AddAmpActivity.class);

  private ServletContext ampContext = null;

  public ActionForward execute(ActionMapping mapping, ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws java.lang.
      Exception {

    HttpSession session = request.getSession();
    	ampContext = getServlet().getServletContext();


    //Always reset returnSearch session variable 
  	session.removeAttribute("returnSearch");
    	
    TeamMember teamMember = new TeamMember();
    
    request.setAttribute(GatePermConst.ACTION_MODE, GatePermConst.Actions.EDIT);

    // Get the current member who has logged in from the session
    teamMember = (TeamMember) session.getAttribute(org.digijava.module.aim.helper.Constants.CURRENT_MEMBER);
    String action = request.getParameter("action");
    if (action != null && action.trim().length() != 0)
      if ("create".equals(action)) 
    	  PermissionUtil.resetScope(session);
    PermissionUtil.putInScope(session, GatePermConst.ScopeKeys.CURRENT_MEMBER, teamMember);


    //PermissionUtil.putInScope(session, GatePermConst.ScopeKeys.CURRENT_MEMBER, teamMember);

    // if user is not logged in, forward him to the home page
    //if (session.getAttribute("currentMember") == null)
    //return mapping.findForward("index");

    //return mapping.findForward("publicPreview");

    EditActivityForm eaForm = (EditActivityForm) form;
    session.setAttribute("selectedSectorsForActivity", eaForm.getSectors().getActivitySectors());
    String currCode = "";
    AmpCurrency currObj = null;
    if ( teamMember  != null ) {
    	currObj = CurrencyUtil.getAmpcurrency(
            teamMember.getAppSettings().getCurrencyId());
    }
    	        
    if (currObj != null) {
        currCode 	= currObj.getCurrencyCode();
    }
    else
    	currCode	= org.digijava.module.aim.helper.Constants.DEFAULT_CURRENCY;
    if (eaForm.getFundingCurrCode() == null) {
        eaForm.setFundingCurrCode(currCode);
    }
    if (eaForm.getRegFundingPageCurrCode() == null) {
        eaForm.setRegFundingPageCurrCode(currCode);
    }

    String resetMessages = request.getParameter("resetMessages");
    if(resetMessages != null && resetMessages.equals("true")) {
    	if(eaForm.getMessages() != null) {
    		eaForm.getMessages().clear();
        }
    }
    
    //START-patch for error redirecting
    String reqStep = (String) request.getAttribute("step");
    if (reqStep != null && (reqStep.compareTo(eaForm.getStep()) != 0))
    	eaForm.setStep(reqStep);
    //END
    
    Long idForOriginalActivity = null; //AMP-9633
    if(request.getSession().getAttribute("idForOriginalActivity")!=null){
    	idForOriginalActivity = (Long) request.getSession().getAttribute("idForOriginalActivity") ;
    	request.getSession().removeAttribute("idForOriginalActivity");
    	if(idForOriginalActivity.equals(new Long(-1))){ //create case
    		eaForm.setActivityId(null);
    	}else{ //edit case
    		eaForm.setActivityId(idForOriginalActivity);
    	}    	
    }
    
    
    /**
     * Contacts
     */
  	ActivityContactInfo contactInfo=eaForm.getContactInformation();
			//if several contact types(donor and mofed for example) contained same contact,then
			//after editing contact,edited one should be replaced in all contact types.
				
				  List<AmpActivityContact> allContacts=new ArrayList<AmpActivityContact>();
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
					
					
					AmpContact contact=(AmpContact)request.getSession().getAttribute("contactToBeReplaced");
			    	
				  	if(contact!=null && contact.getId()!=null && allContacts!=null){
				  		replaceOldContactWithNew(contact,allContacts);
				  	}
				  	request.getSession().removeAttribute("contactToBeReplaced");

				  //Normalize primary contacts
	 	 	 	 	ContactInfoUtil.normalizeActivityContacts(contactInfo);

      //Normalize primary contacts
      ContactInfoUtil.normalizeActivityContacts(contactInfo);
	  
	  //end of Contacts
    
    
    
    
    if(eaForm.getFunding().getFundingOrganizations()!=null){
    	eaForm.getFunding().setFundingDetails(new ArrayList());
	    for (Iterator itOrg = eaForm.getFunding().getFundingOrganizations().iterator(); itOrg.hasNext();) {
			FundingOrganization org = (FundingOrganization) itOrg.next();
			if(org.getFundings()!=null)
	         for (Iterator itFD = org.getFundings().iterator(); itFD.hasNext();) {
	        	 org.digijava.module.aim.helper.Funding funding = (org.digijava.module.aim.helper.Funding) itFD.next();
					if(funding!=null && funding.getFundingDetails()!=null){ 
						eaForm.getFunding().getFundingDetails().addAll(funding.getFundingDetails());
					}
	         }
		}
    }
    
    if(eaForm.getSectors().getClassificationConfigs()==null){
    	List<AmpClassificationConfiguration> configs = SectorUtil.getAllClassificationConfigs();
    	if(configs!=null){
    		AmpClassificationConfiguration primConf = null;
        	Iterator<AmpClassificationConfiguration> it = configs.iterator();        	
        	while(it.hasNext()){
        		AmpClassificationConfiguration conf = it.next();        		
				if(conf.isPrimary())
        			primConf = conf;
        	}
        	if(!isPrimarySectorEnabled() && primConf!=null){
        		configs.remove(primConf);
        	}
    	}
        eaForm.getSectors().setClassificationConfigs(configs);
    }

    //set the level, if available
    String levelTxt=request.getParameter("activityLevelId");
    if(levelTxt!=null) eaForm.getIdentification().setActivityLevel(Long.parseLong(levelTxt));

     //set the contracts, if available
     //eaForm.getCurrCode()
    if(eaForm.getActivityId()!=null){
           List contracts=ActivityUtil.getIPAContracts(eaForm.getActivityId(),eaForm.getCurrCode());
           if(eaForm.getContracts().getContracts()==null){
        	   eaForm.getContracts().setContracts(contracts);
           }           
     }

     // load all pledges
     ArrayList<FundingPledges> pledges = new ArrayList<FundingPledges>();
     eaForm.getFunding().setPledgeslist(pledges);
      
     // load all the active currencies
      eaForm.setCurrencies(CurrencyUtil.getActiveAmpCurrencyByName());
      ArrayList<AmpComponentType> ampComponentTypes = new ArrayList<AmpComponentType>(ComponentsUtil.getAmpComponentTypes());
      eaForm.getComponents().setAllCompsType(ampComponentTypes);
      
      
       if (eaForm.getActivityId()!=null && eaForm.getActivityId()!=0 && eaForm.getIndicator().getIndicatorsME()==null){
              List indicators=IndicatorUtil.createActivityIndicatorHelperBeans(eaForm.getActivityId());                                        
              eaForm.getIndicator().setIndicatorsME(indicators);           
       }
       
       if(request.getParameter("previewClicked")!=null){
    	   if(eaForm.getIndicator().getIndicatorsME()!=null){
    		   session.setAttribute("indsME", eaForm.getIndicator().getIndicatorsME());
    		   List<AmpIndicatorRiskRatings> risks=new ArrayList<AmpIndicatorRiskRatings>();
    		   for (ActivityIndicator actInd :(Collection<ActivityIndicator>) eaForm.getIndicator().getIndicatorsME()) {
    			   if(actInd.getRisk()!=null){
    				   AmpIndicatorRiskRatings risk=IndicatorUtil.getRisk(actInd.getRisk());
    				   risks.add(risk);
    			   }
    		   }
    		   session.setAttribute("indsRisks", risks);
    	   }
       }

    //Only currencies having exchanges rates AMP-2620
      ArrayList<AmpCurrency> validcurrencies = new ArrayList<AmpCurrency>();
      eaForm.getFunding().setValidcurrencies(validcurrencies);
      if(eaForm.getCurrencies()!=null && eaForm.getCurrencies().size()>0){
    	  for (Iterator iter = eaForm.getCurrencies().iterator(); iter.hasNext();) {
    			AmpCurrency element = (AmpCurrency) iter.next();
    			 if( CurrencyUtil.isRate(element.getCurrencyCode())== true)
    					{
    				 		if(element!=null && element.getCurrencyCode()!=null) 
    				 			eaForm.getFunding().getValidcurrencies().add((CurrencyUtil.getCurrencyByCode(element.getCurrencyCode())));
    					}
    			}
      }
      if (eaForm.getAgencies().getExecutingAgencies() == null) {
          eaForm.getAgencies().setExecutingAgencies(new ArrayList<AmpOrganisation>());
          eaForm.getAgencies().setExecutingOrgToInfo(new HashMap<String, String>());
      }
      if (eaForm.getAgencies().getImpAgencies() == null) {
          eaForm.getAgencies().setImpAgencies(new ArrayList<AmpOrganisation>());
          eaForm.getAgencies().setImpOrgToInfo(new HashMap<String, String>());
      }
      if (eaForm.getAgencies().getBenAgencies() == null) {
          eaForm.getAgencies().setBenAgencies(new ArrayList<AmpOrganisation>());
          eaForm.getAgencies().setBenOrgToInfo(new HashMap<String, String>());
      }
      if (eaForm.getAgencies().getConAgencies() == null) {
          eaForm.getAgencies().setConAgencies(new ArrayList<AmpOrganisation>());
          eaForm.getAgencies().setConOrgToInfo(new HashMap<String, String>());
      }
      if (eaForm.getAgencies().getReportingOrgs() == null) {
          eaForm.getAgencies().setReportingOrgs(new ArrayList<AmpOrganisation>());
          eaForm.getAgencies().setRepOrgToInfo(new HashMap<String, String>());
      }
      if (eaForm.getAgencies().getSectGroups() == null) {
          eaForm.getAgencies().setSectGroups(new ArrayList<AmpOrganisation>());
          eaForm.getAgencies().setSectOrgToInfo(new HashMap<String, String>());
      }
      if (eaForm.getAgencies().getRegGroups() == null) {
          eaForm.getAgencies().setRegGroups(new ArrayList<AmpOrganisation>());
          eaForm.getAgencies().setRegOrgToInfo(new HashMap<String, String>());
      }
      if (eaForm.getAgencies().getRespOrganisations() == null) {
          eaForm.getAgencies().setRespOrganisations(new ArrayList<AmpOrganisation>());
          eaForm.getAgencies().setRespOrgToInfo(new HashMap<String, String>());
      }




    /*Clear eventually dirty information found in session related to DM*/
		if ( request.getParameter("action") != null && request.getParameter("action").equals("create") ){
						eaForm.setSurveyFundings(null);
						if(eaForm.getSurvey() != null) {
							eaForm.getSurvey().setAhsurvey(null);
							eaForm.getSurvey().setAmpSurveyId(null);
							eaForm.getSurvey().setFundingOrg(null);
							eaForm.getSurvey().setIndicators(null);
							eaForm.setSurvey(null);
						}
						eaForm.setActivityId(null);
						
                        SelectDocumentDM.clearContentRepositoryHashMap(request);
                        eaForm.getPrograms().setActPrograms(null);
                        eaForm.setEditAct(false);
                        if (ProgramUtil.getAmpActivityProgramSettingsList() != null) {
                                eaForm.getPrograms().setNationalSetting(ProgramUtil.
                                                          getAmpActivityProgramSettings(
                                                              ProgramUtil.
                                                              NATIONAL_PLAN_OBJECTIVE));
                                eaForm.getPrograms().setPrimarySetting(ProgramUtil.
                                                         getAmpActivityProgramSettings(
                                                             ProgramUtil.PRIMARY_PROGRAM));
                                eaForm.getPrograms().setSecondarySetting(ProgramUtil.
                                                           getAmpActivityProgramSettings(
                                                               ProgramUtil.SECONDARY_PROGRAM));
               }

                }

		
		//eaForm.getDocuments().getCrDocuments().addAll(TemporaryDocumentData.retrieveTemporaryDocDataList(request));
		/*if(eaForm.getDocuments().getDocuments() == null) {
			eaForm.getDocuments().setDocuments(new ArrayList());
		}*/
		//eaForm.getDocuments().getDocuments().addAll(TemporaryDocumentData.retrieveTemporaryDocDataList(request));
		request.getSession().setAttribute(SelectDocumentDM.CONTENT_REPOSITORY_HASH_MAP, SelectDocumentDM.getContentRepositoryHashMap(request));
		
	//===============Sectors START===========================

		// Add sectors
	if (request.getParameter("addSector") != null) {
		return addSector(mapping, session, eaForm);
	}else if (request.getParameter("remSectors") != null) {
		return removeSector(mapping, request, session, eaForm);
    }
    //

    //we use this pointer to simplify adding items in the selectors:
    session.setAttribute("eaf", eaForm);
    String logframepr = (String) session.getAttribute("logframepr"); //logframepreview
    if (logframepr == null || logframepr == "")
      logframepr = "";
    if (logframepr.compareTo("true") == 0) {
      eaForm.setStep("9");
      eaForm.setPageId(1);
    }
    //===============Sectors END=============================


    /*
    //===============Componentes START=======================
    
    // Add componente
    if (request.getParameter("addComponente") != null) {
      return addComponente(mapping, session, eaForm);
    } else if (request.getParameter("remComponentes") != null) { // Remove componentes
      return removeComponentes(mapping, session, eaForm);
    }

    //===============Componentes END=========================
	*/

    //eaForm.setAllComps(ActivityUtil.getAllComponentNames());
    ProposedProjCost propProjCost = null;
    if (eaForm.getFunding().getProProjCost() != null) {
      propProjCost = eaForm.getFunding().getProProjCost();
      if (propProjCost.getCurrencyCode() == null &&
          propProjCost.getFunAmount() == null &&
          propProjCost.getFunDate() == null) {
        eaForm.getFunding().setProProjCost(null);
      }
    }

    try {

      if (!eaForm.isEditAct() || eaForm.isReset()) {
        eaForm.reset(mapping, request);
      }

      /*
       * The action 'AddAmpActivity' is used by 'Add Activity', 'View Channel Overview', and
       * 'show activity details' page. In the case if 'view channel overview', and
       * 'show activity details', we have to directly forward to the preview page.
       * A form bean variable called pageId is used for this purpose. All the requests
       * coming from pages other than 'Add activity' page will have a pageId value
       * which is greater than 1.
       */
      if (eaForm.getPageId() > 1)
        eaForm.setStep("9");

      // added by Akash
      // desc: clearing comment properties
      // start
      action = request.getParameter("action");
      if (action != null && action.trim().length() != 0) {
        if ("create".equals(action)) {
          eaForm.getComments().getCommentsCol().clear();
          eaForm.getComments().setCommentFlag(false);
          eaForm.getFunding().setProProjCost(null);
          eaForm.getPrograms().setActPrograms(null);
        }
      }
      // end

      Collection themes = new ArrayList();
      themes = ProgramUtil.getAllThemes();
      eaForm.getPrograms().setProgramCollection(themes);

      // added by Akash
      // desc: setting WorkingTeamLeadFlag & approval status in form bean
      // start
      boolean teamLeadFlag = false;
      boolean workingTeamFlag = true;
      if (teamMember != null) {
        Long ampTeamId = teamMember.getTeamId();
        teamLeadFlag = teamMember.getTeamHead();
        workingTeamFlag = TeamUtil.checkForParentTeam(ampTeamId);
      }
      
      if(eaForm.getIdentification().getChapterCode()!=null)
			try {
				eaForm.getIdentification().setChapterForPreview(ChapterUtil.getChapterByCode(eaForm.getIdentification().getChapterCode()));
			} catch (HibernateException e) {
				logger.error(e);
				e.printStackTrace();
			} catch (SQLException e) {
				logger.error(e);
				e.printStackTrace();
			}

      if (teamLeadFlag && workingTeamFlag)
        eaForm.setWorkingTeamLeadFlag("yes");
      else
        eaForm.setWorkingTeamLeadFlag("no");
        eaForm.setTeamLead(teamLeadFlag);


      boolean activityApprovalStatusProcess=false;

      if (eaForm.getIsPreview()==1 || !eaForm.isEditAct() || logframepr.compareTo("true") == 0 || request.getParameter("logframe") != null) {
       if (teamMember != null)
        if ("true".compareTo((String) session.getAttribute("teamLeadFlag"))==0||teamMember.isApprover()){
            eaForm.getIdentification().setApprovalStatus(org.digijava.module.aim.helper.Constants.APPROVED_STATUS);
      	  }else{
      		  synchronized (ampContext) {
      			  //ampContext=this.getServlet().getServletContext();
	        	  AmpTreeVisibility ampTreeVisibility=(AmpTreeVisibility) ampContext.getAttribute("ampTreeVisibility");
	        	 // AmpModulesVisibility moduleToTest=FeaturesUtil.getModuleVisibility("Activity Approval Process");
	        	  AmpModulesVisibility moduleToTest=ampTreeVisibility.getModuleByNameFromRoot("Activity Approval Process");
	        	  if(moduleToTest!=null)
	          	  	activityApprovalStatusProcess= moduleToTest.isVisibleTemplateObj(ampTreeVisibility.getRoot());
	        	  if(activityApprovalStatusProcess==true ) eaForm.getIdentification().setApprovalStatus(org.digijava.module.aim.helper.Constants.STARTED_STATUS);
	        	  	else eaForm.getIdentification().setApprovalStatus(org.digijava.module.aim.helper.Constants.APPROVED_STATUS);
	        	  }
            }
      }
      else {
    	  /*
        String sessId = session.getId();
        ArrayList sessList = (ArrayList) ampContext.getAttribute(
            org.digijava.module.aim.helper.Constants.SESSION_LIST);
        if (sessList.contains(sessId) == false) {
          ActionMessages errors = new ActionMessages();
          errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
              "error.aim.activityAlreadyOpenedForEdit"));
          saveErrors(request, errors);

          String url = "/aim/viewChannelOverview.do?ampActivityId="
              + eaForm.getActivityId() + "&tabIndex=0";
          RequestDispatcher rd = getServlet().getServletContext()
              .getRequestDispatcher(url);
          rd.forward(request, response);
        }
        */

        synchronized (ampContext) {
          HashMap tsList = (HashMap) ampContext.getAttribute(org.digijava.
              module.aim.helper.Constants.TS_ACT_LIST);
          if (tsList != null) {
            tsList.put(eaForm.getActivityId(),
                       new Long(System.currentTimeMillis()));
          }
          ampContext.setAttribute(org.digijava.module.aim.helper.Constants.
                                  TS_ACT_LIST, tsList);
        }
      }
      // end

      if (eaForm.getStep().equals("1")) { // show the step 1 page.

        return showStep1(mapping, request, teamMember, eaForm);
      }
      else if (eaForm.getStep().equals("1.1") || eaForm.getStep().equals("2.2")) { // shows the edit page of the editor module
    	if (eaForm.getStep().equals("1.1"))  
    		eaForm.setStep("1");
    	else
    		eaForm.setStep("2");
    		
        // When the contents are saved the editor module redirects to the url specified in the 'referrer' parameter
        session.setAttribute("activityName", eaForm.getIdentification().getTitle());
        session.setAttribute("activityFieldName", request.getParameter("fieldName"));
        String url = "/editor/showEditText.do?id=" + eaForm.getEditKey() +"&lang="+RequestUtils.getNavigationLanguage(request).getCode()+
            "&referrer=" + eaForm.getContext() +"/aim/addActivity.do?edit=true";
        response.sendRedirect(eaForm.getContext() + url);
      }
      else if (eaForm.getStep().equals("1_5")) { // show the 'Refernces' step page.
          return mapping.findForward("addActivityStep1_5");
      }
      else if (eaForm.getStep().equals("2")) { // show the step 2 page.

          if (eaForm.getErrors() != null && !eaForm.getErrors().isEmpty()) {
            Set <Map.Entry<String,String>> entrySet =  eaForm.getErrors().entrySet();

              ActionErrors errors = new ActionErrors();
            for (Map.Entry<String,String> entry : entrySet) {
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(entry.getKey(), entry.getValue()));
            }
            saveErrors(request, errors);
              eaForm.clearMessages();
          }

                  //HashMap<String, String>

     	  return showStep2(mapping, request, session, teamMember, eaForm);
      }
      else if (eaForm.getStep().equals("3")) { // show the step 3 page.
        return mapping.findForward("addActivityStep3");
      }
      else if (eaForm.getStep().equals("4")) { // show the step 4 page.
        return mapping.findForward("addActivityStep4");
      }
      else if (eaForm.getStep().equals("5")) { // show the step 5 page.
        return mapping.findForward("addActivityStep5");
      }
      else if (eaForm.getStep().equals("6")) { // show the step 6 page.
        return mapping.findForward("addActivityStep6");
      }
      else if (eaForm.getStep().equals("7")) { // show the step 7 page.
        return mapping.findForward("addActivityStep7");
      }
      else if (eaForm.getStep().equals("8")) { // show the step 7 page.
        return mapping.findForward("addActivityStep8");
      }
      else if (eaForm.getStep().equals("11")) { // show the step 11 page.
        return mapping.findForward("addActivityStep11");
      }
      else if (eaForm.getStep().equals("12")) { // show the step 12 page.
	        return mapping.findForward("addActivityStep12");
	      }
       else if (eaForm.getStep().equals("13")) { // show the step 13 page.
          return mapping.findForward("addActivityStep13");
      }
      else if (eaForm.getStep().equals("9")) { // show the preview page.

		 return showStep9(mapping, request, session, teamMember, eaForm, logframepr,
				action);
      }
      else if (eaForm.getStep().equals("10")) { // show step 9 - M&E page     
    	  return showStep10(mapping, eaForm);
      }
      else if (eaForm.getStep().equals("17")) { // show step 17 - PI page     
    	  return showStep17(mapping, eaForm);
      }
      else if (eaForm.getStep().equals("14")) { // show step 14 - Regional Observations     
    	  return mapping.findForward("addActivityStep14");
      }
      else {
        return mapping.findForward("adminHome");
      }
    }
    catch (Exception e) {
      e.printStackTrace(System.out);
      return null;
    }
    return null;
  }
  
  private void replaceOldContactWithNew(AmpContact contact,List<AmpActivityContact> allContacts){
		for (AmpActivityContact ampActivityContact : allContacts) {
			if(ampActivityContact.getContact().getId()!=null && ampActivityContact.getContact().getId().equals(contact.getId())){
				ampActivityContact.setContact(contact);
			}			
		}
	}

/*
private ActionForward removeComponentes(ActionMapping mapping,
		HttpSession session, EditActivityForm eaForm) {
	Long selComponentes[] = eaForm.getComponents().getSelActivityComponentes();
      Collection<ActivitySector> prevSelComponentes = eaForm.getComponents().getActivityComponentes();
      Collection newComponentes = new ArrayList();

      Iterator<ActivitySector> itr = prevSelComponentes.iterator();

      boolean flag = false;

      while (itr.hasNext()) {
        ActivitySector asec =  itr.next();
        flag = false;
        for (int i = 0; i < selComponentes.length; i++) {

          if (asec.getSubsectorLevel1Id() == -1 && asec.getSectorId().equals(selComponentes[i])) {
            flag = true;
            break;
          }
          if (asec.getSubsectorLevel1Id() != -1 && asec.getSubsectorLevel2Id() == -1 && asec.getSubsectorLevel1Id().equals(selComponentes[i])) {
              flag = true;
              break;
            }
          if (asec.getSubsectorLevel1Id() != -1 && asec.getSubsectorLevel2Id() != -1 && asec.getSubsectorLevel2Id().equals(selComponentes[i])) {
              flag = true;
              break;
            }
        }
        if (!flag) {
        	newComponentes.add(asec);
        }
      }

      eaForm.getSectors().setActivitySectors(newComponentes);
      eaForm.getSectors().setPrimarySectorVisible(FeaturesUtil.isVisibleSectors("Primary", ampContext)?"true":"false");
      eaForm.getSectors().setSecondarySectorVisible(FeaturesUtil.isVisibleSectors("Secondary", ampContext)?"true":"false");
      session.setAttribute("Primary Sector", eaForm.getSectors().getPrimarySectorVisible());
      session.setAttribute("Secondary Sector", eaForm.getSectors().getSecondarySectorVisible());
      return mapping.findForward("addActivityStep2");
}


private ActionForward addComponente(ActionMapping mapping, HttpSession session,
		EditActivityForm eaForm) {
	
	try{
	ActivitySector selectedComponente = (ActivitySector) session.getAttribute("addComponente");
      if(selectedComponente==null) selectedComponente=new ActivitySector();
      session.removeAttribute("componenteSelected");

      Collection<ActivitySector> prevSelComponentes = eaForm.getComponents().getActivityComponentes();



      boolean addComponente = true;
      if (prevSelComponentes != null) {
    	  Iterator<ActivitySector> itr = prevSelComponentes.iterator();
    	  while (itr.hasNext()) {
    		  ActivitySector asec =  itr.next();
	          if (asec.getSectorName().equals(selectedComponente.getSectorName())){
	        	  if (selectedComponente.getSubsectorLevel1Name() == null) {
	        		  addComponente = false;
						break;
	        	  }
	        	  if(asec.getSubsectorLevel1Name() != null ) {
						if(asec.getSubsectorLevel1Name().equals(selectedComponente.getSubsectorLevel1Name())){
							if(selectedComponente.getSubsectorLevel2Name() == null){
								addComponente = false;
							      break;
							}
							if(asec.getSubsectorLevel2Name() != null){
								if(asec.getSubsectorLevel2Name().equals(selectedComponente.getSubsectorLevel2Name())){
									addComponente = false;
							        break;
							 	}
							}else{
								addComponente = false;
						        break;
							}
						}
		          }else{
		        	  addComponente = false;
						break;
		          }
	          }
    	  }
      }
      if (addComponente) {
			if (prevSelComponentes != null) {
				if (prevSelComponentes.isEmpty())
					selectedComponente.setSectorPercentage(new Float(100));
				prevSelComponentes.add(selectedComponente);
			} else {
				selectedComponente.setSectorPercentage(new Float(100));
				prevSelComponentes = new ArrayList<ActivitySector>();
				prevSelComponentes.add(selectedComponente);
			}
      }

      eaForm.getComponents().setActivityComponentes(prevSelComponentes);
      eaForm.getSectors().setPrimarySectorVisible(FeaturesUtil.isVisibleSectors("Primary", ampContext)?"true":"false");
      eaForm.getSectors().setSecondarySectorVisible(FeaturesUtil.isVisibleSectors("Secondary", ampContext)?"true":"false");
      session.setAttribute("Primary Sector", eaForm.getSectors().getPrimarySectorVisible());
      session.setAttribute("Secondary Sector", eaForm.getSectors().getSecondarySectorVisible());
	}catch (Exception e) {
		logger.error("addComponente exception", e);
	}
      return mapping.findForward("addActivityStep2");
}
*/
private ActionForward removeSector(ActionMapping mapping,
		HttpServletRequest request, HttpSession session, EditActivityForm eaForm) {
	try{
	Long selSectors[] = eaForm.getSectors().getSelActivitySectors();
      String configId=request.getParameter("configId");
      Collection<ActivitySector> prevSelSectors = eaForm.getSectors().getActivitySectors();
      session.setAttribute("removedSector", eaForm.getSectors().getSelActivitySectors());
      Collection newSectors = new ArrayList();

      Iterator<ActivitySector> itr = prevSelSectors.iterator();

      boolean flag = false;

      while (itr.hasNext()) {
        ActivitySector asec = (ActivitySector) itr.next();
        flag = false;
        for (int i = 0; i < selSectors.length; i++) {

          if (asec.getSubsectorLevel1Id() == -1 && asec.getSectorId().equals(selSectors[i])&&asec.getConfigId().equals(Long.parseLong(configId))) {
            flag = true;
            break;
          }
          if (asec.getSubsectorLevel1Id() != -1 && asec.getSubsectorLevel2Id() == -1 && asec.getSubsectorLevel1Id().equals(selSectors[i])&&asec.getConfigId().equals(Long.parseLong(configId))) {
              flag = true;
              break;
            }
          if (asec.getSubsectorLevel1Id() != -1 && asec.getSubsectorLevel2Id() != -1 && asec.getSubsectorLevel2Id().equals(selSectors[i])&&asec.getConfigId().equals(Long.parseLong(configId))) {
              flag = true;
              break;
            }
        }
        if (!flag) {
          newSectors.add(asec);
        }
      }

      eaForm.getSectors().setActivitySectors(newSectors);
      session.setAttribute("selectedSectorsForActivity", eaForm.getSectors().getActivitySectors());
      eaForm.getSectors().setPrimarySectorVisible(FeaturesUtil.isVisibleSectors("Primary", ampContext)?"true":"false");
      eaForm.getSectors().setSecondarySectorVisible(FeaturesUtil.isVisibleSectors("Secondary", ampContext)?"true":"false");
      eaForm.getSectors().setTertiarySectorVisible(FeaturesUtil.isVisibleSectors("Tertiary", ampContext)?"true":"false");
      session.setAttribute("Tertiary Sector", eaForm.getSectors().getTertiarySectorVisible());
      session.setAttribute("Primary Sector", eaForm.getSectors().getPrimarySectorVisible());
      session.setAttribute("Secondary Sector", eaForm.getSectors().getSecondarySectorVisible());
	}catch (Exception e) {
		logger.error("removeSector exception", e);
	}      
      return mapping.findForward("addActivityStep2");
}

private ActionForward addSector(ActionMapping mapping, HttpSession session,
		EditActivityForm eaForm) {
	Object searchedsector = session.getAttribute("add");

	if (searchedsector != null && searchedsector.equals("true")) {
		Collection selectedSecto = (Collection) session
				.getAttribute("sectorSelected");
		Collection<ActivitySector> prevSelSectors = eaForm.getSectors()
				.getActivitySectors();

		if (selectedSecto != null) {
			Iterator<ActivitySector> itre = selectedSecto.iterator();
			while (itre.hasNext()) {
				ActivitySector selectedSector = (ActivitySector) itre
						.next();

				boolean addSector = true;
				if (prevSelSectors != null) {
					Iterator<ActivitySector> itr = prevSelSectors
							.iterator();
					while (itr.hasNext()) {
						ActivitySector asec = (ActivitySector) itr
								.next();

						if (asec.getSectorName().equals(selectedSector.getSectorName())) {
							if (selectedSector.getSubsectorLevel1Name() == null) {
								addSector = false;
								break;
							}
							if (asec.getSubsectorLevel1Name() != null) {
								if (asec.getSubsectorLevel1Name().equals(selectedSector.getSubsectorLevel1Name())) {
									if (selectedSector.getSubsectorLevel2Name() == null) {
										addSector = false;
										break;
									}
									if (asec.getSubsectorLevel2Name() != null) {
										if (asec.getSubsectorLevel2Name().equals(selectedSector.getSubsectorLevel2Name())) {
											addSector = false;
											break;
										}
									} else {
										addSector = true;
										break;
									}
								}
							} else {
								addSector = true;
								break;
							}
						}
					}
				}

				if (addSector) {
					// if an activity already has one or more
					// sectors,than after adding new one
					// the percentages must equal blanks and user should
					// fill them
	                if (prevSelSectors != null) {
	                    Iterator iter = prevSelSectors.iterator();
	                    boolean firstSecForConfig = true;
                        while (iter.hasNext()) {
                            ActivitySector actSect = (ActivitySector) iter
                                .next();
                            if (actSect.getConfigId().equals(selectedSector.getConfigId())) {
                            	if(actSect.getSectorPercentage() != null && actSect.getSectorPercentage()==100f){
                            		actSect.setSectorPercentage(0f);
                            	}	
                            		
                                firstSecForConfig = false;
                                break;
                            }

                        }
                        if (firstSecForConfig) {
	                        selectedSector.setSectorPercentage(100f);
	                    }
	                    prevSelSectors.add(selectedSector);
	                } else {
	                    selectedSector.setSectorPercentage(new Float(
	                        100));
	                    prevSelSectors = new ArrayList<ActivitySector> ();
	                    prevSelSectors.add(selectedSector);
	                }
				}

				eaForm.getSectors().setActivitySectors(prevSelSectors);
			}

		}
		session.removeAttribute("sectorSelected");
		session.removeAttribute("add");
	    session.setAttribute("selectedSectorsForActivity", eaForm.getSectors().getActivitySectors());
	    eaForm.getSectors().setPrimarySectorVisible(FeaturesUtil.isVisibleSectors("Primary", ampContext)?"true":"false");
	    eaForm.getSectors().setSecondarySectorVisible(FeaturesUtil.isVisibleSectors("Secondary", ampContext)?"true":"false");
        eaForm.getSectors().setTertiarySectorVisible(FeaturesUtil.isVisibleSectors("Tertiary", ampContext) ? "true" : "false");
        session.setAttribute("Tertiary Sector", eaForm.getSectors().getTertiarySectorVisible());
	    session.setAttribute("Primary Sector", eaForm.getSectors().getPrimarySectorVisible());
	    session.setAttribute("Secondary Sector", eaForm.getSectors().getSecondarySectorVisible());
		return mapping.findForward("addActivityStep2");

	} else {
		ActivitySector selectedSector = (ActivitySector) session
				.getAttribute("sectorSelected");
		Collection<ActivitySector> prevSelSectors = eaForm
		.getSectors().getActivitySectors();

		boolean addSector = true;
		if (prevSelSectors != null) {
			Iterator<ActivitySector> itr = prevSelSectors.iterator();
			while (itr.hasNext()) {
				ActivitySector asec = (ActivitySector) itr.next();
				if (asec.getSectorName().equals(
						selectedSector.getSectorName())) {
					if (selectedSector.getSubsectorLevel1Name() == null) {
						addSector = false;
						break;
					}
					if (asec.getSubsectorLevel1Name() != null) {
						if (asec
								.getSubsectorLevel1Name()
								.equals(
										selectedSector
												.getSubsectorLevel1Name())) {
							if (selectedSector.getSubsectorLevel2Name() == null) {
								addSector = false;
								break;
							}
							if (asec.getSubsectorLevel2Name() != null) {
								if (asec
										.getSubsectorLevel2Name()
										.equals(
												selectedSector
														.getSubsectorLevel2Name())) {
									addSector = false;
									break;
								}
							} else {
								addSector = false;
								break;
							}
						}
					} else {
						addSector = false;
						break;
					}
				}
			}
		}

	    if (addSector) {
	        // if an activity already has one or more sectors,than after
	        // adding new one
	        // the percentages must equal blanks and user should fill
	        // them
	        if (prevSelSectors != null) {
	            Iterator iter = prevSelSectors.iterator();
	            boolean firstSecForConfig = true;
	            while (iter.hasNext()) {
	                ActivitySector actSect = (ActivitySector) iter
	                    .next();
	                if (actSect.getConfigId().equals(selectedSector.getConfigId())) {
	                	if(actSect.getSectorPercentage()==100f){
	                		actSect.setSectorPercentage(0.0f);
	                	}                            	
	                    firstSecForConfig = false;
	                    break;
	                }

	            }
	            if (firstSecForConfig) {
	                selectedSector.setSectorPercentage(100f);
	            }
	            prevSelSectors.add(selectedSector);
	        } else {
	            selectedSector.setSectorPercentage(new Float(
	                100));
	            prevSelSectors = new ArrayList<ActivitySector> ();
	            prevSelSectors.add(selectedSector);
	        }
	    }
		eaForm.getSectors().setActivitySectors(prevSelSectors);
		session.removeAttribute("sectorSelected");
	    session.setAttribute("selectedSectorsForActivity", eaForm.getSectors().getActivitySectors());
	    eaForm.getSectors().setPrimarySectorVisible(FeaturesUtil.isVisibleSectors("Primary", ampContext)?"true":"false");
	    eaForm.getSectors().setSecondarySectorVisible(FeaturesUtil.isVisibleSectors("Secondary", ampContext)?"true":"false");
        eaForm.getSectors().setTertiarySectorVisible(FeaturesUtil.isVisibleSectors("Tertiary", ampContext)?"true":"false");
        session.setAttribute("Tertiary Sector", eaForm.getSectors().getTertiarySectorVisible());
	    session.setAttribute("Primary Sector", eaForm.getSectors().getPrimarySectorVisible());
	    session.setAttribute("Secondary Sector", eaForm.getSectors().getSecondarySectorVisible());
		return mapping.findForward("addActivityStep2");

	}
}

private ActionForward showStep9(ActionMapping mapping,
		HttpServletRequest request, HttpSession session, TeamMember teamMember,
		EditActivityForm eaForm, String logframepr, String action)
		throws DgException, AimException {
	//       if (eaForm.getAmpId() == null ) { // if AMP-ID is not generated, generate the AMP-ID
	          /*
	           * The logic for geerating the AMP-ID is as follows:
	           * 1. get default global country code
	           * 2. Get the maximum of the ampActivityId + 1, MAX_NUM
	           * 3. merge them
	           */
	
	        /*
	         * If the mode is 'Add', set the Activity Creator as the current logged in user
	         */
	        if (action != null && eaForm.getIsPreview() != 1) {
	          if (teamMember != null && (!eaForm.isEditAct()) &&
	              (eaForm.getIdentification().getActAthEmail() == null ||
	               eaForm.getIdentification().getActAthEmail().trim().length() == 0)) {
	            User usr = DbUtil.getUser(teamMember.getEmail());
	            if (usr != null) {
	              eaForm.getIdentification().setActAthFirstName(usr.getFirstNames());
	              eaForm.getIdentification().setActAthLastName(usr.getLastName());
	              eaForm.getIdentification().setActAthEmail(usr.getEmail());
	              eaForm.getIdentification().setActAthAgencySource(usr.getOrganizationName());
	              if(eaForm.getIdentification().getChapterCode()!=null)
					try {
						eaForm.getIdentification().setChapterForPreview(ChapterUtil.getChapterByCode(eaForm.getIdentification().getChapterCode()));
					} catch (HibernateException e) {
						logger.error(e);
						e.printStackTrace();
					} catch (SQLException e) {
						logger.error(e);
						e.printStackTrace();
					}
	            }
	          }
	
	        }
	        else {
	          AmpActivityVersion activity = ActivityUtil.loadActivity(eaForm.
	              getActivityId());
	          if("edit".equals(action)) {
	          	//check if we have edit permissin for this activity
	          	Long ampActivityId=Long.parseLong(request.getParameter("ampActivityId"));
	
	          }
	
	
	
	    	if (activity.getActivityCreator() != null) {
	            eaForm.getIdentification().setActAthFirstName(activity.getActivityCreator().getUser().
	                                      getFirstNames());
	            eaForm.getIdentification().setActAthLastName(activity.getActivityCreator().getUser().
	                                     getLastName());
	            eaForm.getIdentification().setActAthEmail(activity.getActivityCreator().getUser().
	                                  getEmail());
	            eaForm.getIdentification().setActAthAgencySource(activity.getActivityCreator().getUser().
	                                         getOrganizationName());
	          }
	          eaForm.setIsPreview(0);
	        }
	
	
	
	Collection<AmpCategoryValue> catValues	= null;
	try {
		catValues = CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.REFERENCE_DOCS_KEY,false,request);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	    	if (catValues!=null && eaForm.getDocuments().getReferenceDocs()==null){
	        	List<ReferenceDoc> refDocs=new ArrayList<ReferenceDoc>();
	    		Collection<AmpActivityReferenceDoc> activityRefDocs=null;
	    		Map<Long, AmpActivityReferenceDoc> categoryRefDocMap=null;
	
	    		if (eaForm.getActivityId()!=null){
	        		//get list of ref docs for activity
	    			activityRefDocs=ActivityUtil.getReferenceDocumentsFor(eaForm.getActivityId());
	            	//create map where keys are category value ids.
	    			categoryRefDocMap = AmpCollectionUtils.createMap(
	    					activityRefDocs,
	    					new ActivityUtil.CategoryIdRefDocMapBuilder());
	    		}
	
	        	//create arrays, number of elements as much as category values
	        	Long[] refdocIds=new Long[catValues.size()];
	        	String[] refdocComments=new String[catValues.size()];
	
	        	int c=0;
	        	int selectedIds=0;
	        	for(AmpCategoryValue catVal: catValues){
	        		AmpActivityReferenceDoc refDoc=(categoryRefDocMap==null)?null:categoryRefDocMap.get(catVal.getId());
	        		ReferenceDoc doc=new ReferenceDoc();
	        		doc.setCategoryValueId(catVal.getId());
	        		doc.setCategoryValue(catVal.getValue());
	        		if (refDoc==null){
	        			refdocComments[c]="";
	        			doc.setComment("");
	        			doc.setChecked(false);
	        		}else{
	        			refdocIds[selectedIds++]=refDoc.getCategoryValue().getId();
	        			refdocComments[c]=refDoc.getComment();
	        			doc.setComment(refDoc.getComment());
	        			doc.setRefDocId(refDoc.getId());
	        			doc.setChecked(true);
	        		}
	        		refDocs.add(doc);
	        		c++;
	        	}
	
	        	//set selected ids
	        	eaForm.getDocuments().setAllReferenceDocNameIds(refdocIds);
	        	//set all comments, some are empty
	//        	eaForm.setRefDocComments(refdocComments);
	
	    		if(refDocs.size()>0){
	    			ReferenceDoc[] myrefDoc = (ReferenceDoc[]) refDocs.toArray(new ReferenceDoc[0]);
	    			eaForm.getDocuments().setReferenceDocs(myrefDoc);
	    		}
	    		else{
	    			eaForm.getDocuments().setReferenceDocs(null);
	    		}

	
	    	}
	
	
	
	        Collection euActs = EUActivityUtil.getEUActivities(eaForm.getActivityId());
	        Collection <EUActivity> formEuActs= eaForm.getCosting().getCosts();
	        if(formEuActs!=null && formEuActs.size()>0){
	        	if(euActs==null){
	        		euActs=new ArrayList<EUActivity>();
	        	}
	        	Map<Long, EUActivity> euActMap=AmpCollectionUtils.createMap(euActs, new ActivityUtil.EUActivityKeyResolver());
	        	for(EUActivity object : formEuActs){
	        		EUActivity euActivity=euActMap.get(object.getId());
	        		if(euActivity==null){
	        			euActs.add(object);
	        		}
	        	}
	        }
	        // EUActivities = same as Costs
	        request.setAttribute("costs", euActs);
	        request.setAttribute("actId", eaForm.getActivityId());
	        int risk = MEIndicatorsUtil.getOverallRisk(eaForm.getActivityId());
	        String riskName = MEIndicatorsUtil.getRiskRatingName(risk);
	        String rskColor = MEIndicatorsUtil.getRiskColor(risk);
	        request.setAttribute("overallRisk", riskName);
	        request.setAttribute("riskColor", rskColor);
	
	        Long prev = new Long( -1);
	        Long next = new Long( -1);
	        if (eaForm.getActivityId() != null) {
	          ReportData rep = (ReportData) session.getAttribute("report");
	          if (rep != null) {
	            Collection ids = (Collection) rep.getOwnerIds();
	            Iterator it = ids.iterator();
	
	            while (it.hasNext()) {
	              Long el = (Long) it.next();
	              if (el.compareTo(eaForm.getActivityId()) == 0) {
	                if (it.hasNext())
	                  next = new Long( ( (Long) it.next()).longValue());
	                break;
	              }
	              prev = new Long(el.longValue());
	            }
	          }
	          request.setAttribute("nextId", next);
	          request.setAttribute("prevId", prev);
	        }
	
	        if (eaForm.getIdentification().getLevelCollection() == null) {
	          eaForm.getIdentification().setLevelCollection(DbUtil.getAmpLevels());
	        }
	
	//      patch for comments that were not saved yet
	        boolean currentlyEditing	= false;
	        if( request.getParameter("currentlyEditing")!=null && request.getParameter("currentlyEditing").equals("true")) {
	        	currentlyEditing		= true;
	        }
	
	        HashMap unsavedComments = (HashMap) session.getAttribute("commentColInSession");
	        Set keySet = null;
	        if (unsavedComments != null)
	        	keySet = unsavedComments.keySet();
	
	
	        if (teamMember == null){
	        	request.setAttribute(GatePermConst.ACTION_MODE, GatePermConst.Actions.VIEW);
	        	return mapping.findForward("publicPreview");
	        }
	        else {
	          ArrayList<AmpComments> colAux	= null;
	          Collection ampFields 			= DbUtil.getAmpFields();
	          HashMap allComments 			= new HashMap();
	
	          for (Iterator itAux = ampFields.iterator(); itAux.hasNext(); ) {
	            AmpField field = (AmpField) itAux.next();
	            if ( currentlyEditing && keySet!=null && keySet.contains(field.getAmpFieldId()) ) {
	            	colAux							= new ArrayList<AmpComments>();
	            	Collection<AmpComments> toAdd 	= (Collection) unsavedComments.get(field.getAmpFieldId());
	            	colAux.addAll( toAdd );
	            }
	            else {
	            	colAux = DbUtil.getAllCommentsByField(field.getAmpFieldId(),
	                                                  eaForm.getActivityId());
	            }

	            allComments.put(field.getFieldName(), colAux);
	          }
	
	          eaForm.getComments().setAllComments(allComments);
	          //eaForm.setCommentsCol(colAux);
	
	          if (request.getParameter("logframe") != null || logframepr.compareTo("true") == 0) {
	//            eaForm.setIndicatorsME(IndicatorUtil.getActivityIndicatorsList(eaForm.getActivityId()));
	            eaForm.getIndicator().setIndicatorsME(IndicatorUtil.getActivityIndicatorHelperBeans(eaForm.getActivityId()));
	            if (!eaForm.isEditAct()) {
	              eaForm.getIndicator().setIndicatorId(null);
	              eaForm.getIndicator().setIndicatorValId(null);
	              eaForm.getIndicator().setExpIndicatorId(null);
	              eaForm.getIndicator().setBaseVal(null);
	              eaForm.getIndicator().setBaseValDate(null);
	              eaForm.getIndicator().setTargetVal(null);
	              eaForm.getIndicator().setTargetValDate(null);
	              eaForm.getIndicator().setRevTargetVal(null);
	              eaForm.getIndicator().setRevTargetValDate(null);
	              //eaForm.getIndicatorME().setIndicatorPriorValues(null);
	              eaForm.getIndicator().setCurrentVal(null);
	              eaForm.getIndicator().setCurrentValDate(null);
	              eaForm.getIndicator().setIndicatorRisk(null);
	              eaForm.getContracts().setIpaBudget(new Double(0));
	              
	              
	
	            }
	
	            Double totalEUContrib = new Double(0);
	            
	            if (eaForm.getContracts().getContracts() != null){
	            	Iterator it2 = eaForm.getContracts().getContracts().iterator();
	            	while (it2.hasNext()) {
	            		IPAContract contr = (IPAContract) it2.next();
						if (contr.getTotalECContribIBAmount() != null) {
							totalEUContrib += contr.getTotalECContribIBAmount();
						}
						if (contr.getTotalECContribINVAmount() != null) {
							totalEUContrib += contr.getTotalECContribINVAmount();
						}
	            	}
	            }
	            eaForm.getContracts().setIpaBudget(totalEUContrib);
	            //get the levels of risks
	
	            Long defaultCurrency=teamMember.getAppSettings().getCurrencyId();
		        double allCosts=0;
		        if(eaForm.getCosting().getCosts() != null)
		        	for(Iterator it=eaForm.getCosting().getCosts().iterator();it.hasNext();)
		        	{
		        		EUActivity euAct=(EUActivity) it.next();
		        		euAct.setDesktopCurrencyId(defaultCurrency);
		        		allCosts+=euAct.getTotalCostConverted();
		        	}
	            eaForm.getCosting().setAllCosts(new Double(allCosts));
	            if ((eaForm.getIndicator().getIndicatorsME() != null) && (!eaForm.getIndicator().getIndicatorsME().isEmpty()))
	              eaForm.getIndicator().setRiskCollection(MEIndicatorsUtil.getAllIndicatorRisks());
	            request.setAttribute(GatePermConst.ACTION_MODE, GatePermConst.Actions.VIEW);
	            return mapping.findForward("previewLogframe");
	          }
	          /* Setting documents for preview */
	          Collection rlList	= eaForm.getDocuments().getDocumentList();
	          eaForm.getDocuments().setDocuments( new ArrayList<Documents>() );
	          if (rlList != null ) {
	        	  Iterator iter 		= rlList.iterator();
	        	  if(iter.hasNext())
	        	  {
	        		  RelatedLinks rl		= (RelatedLinks) iter.next();
	        		  CMSContentItem item	= rl.getRelLink();
	        		  if ( item != null ) {
	        			  eaForm.getDocuments().getDocuments().add( createHelperDocument(item, null, null) );
	        		  }
	        	  }
	          }
	          eaForm.getDocuments().setCrDocuments( DocumentManagerUtil.createDocumentDataCollectionFromSession(request) );
	
	          /* END - Setting documents for preview */
	          request.setAttribute(GatePermConst.ACTION_MODE, GatePermConst.Actions.VIEW);
	          DocumentManagerUtil.logoutJcrSessions(request.getSession());
	          return mapping.findForward("preview");
	        }
}

private ActionForward showStep10(ActionMapping mapping, EditActivityForm eaForm) {
	          if (!eaForm.isEditAct()) {
	            eaForm.getIndicator().setIndicatorId(null);
	            eaForm.getIndicator().setIndicatorValId(null);
	            eaForm.getIndicator().setExpIndicatorId(null);
	            eaForm.getIndicator().setBaseVal(null);
	            eaForm.getIndicator().setBaseValDate(null);
	            eaForm.getIndicator().setTargetVal(null);
	            eaForm.getIndicator().setTargetValDate(null);
	            eaForm.getIndicator().setRevTargetVal(null);
	            eaForm.getIndicator().setRevTargetValDate(null);
	            eaForm.getIndicator().setIndicatorPriorValues(null);
	            eaForm.getIndicator().setCurrentVal(null);
	            eaForm.getIndicator().setCurrentValDate(null);
	            eaForm.getIndicator().setIndicatorRisk(null);
	          }
	
	          //get the levels of risks
	          if (eaForm.getIndicator().getIndicatorsME()!=null && !eaForm.getIndicator().getIndicatorsME().isEmpty())
	            eaForm.getIndicator().setRiskCollection(MEIndicatorsUtil.getAllIndicatorRisks());
	
	          return mapping.findForward("addActivityStep10");
}

	private ActionForward showStep14(ActionMapping mapping, EditActivityForm svForm) {
		logger.debug("In edit survey list action");
        logger.debug("step[before] : " + svForm.getStep());
        svForm.setStep("14");
        logger.debug("step[after] : " + svForm.getStep());
		
        return mapping.findForward("addActivityStep14");
	}

	private ActionForward showStep17(ActionMapping mapping, EditActivityForm svForm) {
		//EditSurveyList surveyListAction = new EditSurveyList();
		//surveyListAction.execute(mapping, eaForm, request, response);
        
        logger.debug("In edit survey list action");
        logger.debug("step[before] : " + svForm.getStep());
        svForm.setStep("17"); // for indicators tab in donor-view
        logger.debug("step[after] : " + svForm.getStep());
        
        Comparator sfComp = new Comparator() {
            public int compare(Object o1, Object o2) {
                SurveyFunding sf1 = (SurveyFunding) o1;
                SurveyFunding sf2 = (SurveyFunding) o2;
                return sf1.getFundingOrgName().trim().toLowerCase().compareTo(sf2.getFundingOrgName().trim().toLowerCase());
            }
        };
        List<SurveyFunding> surveyColl = new ArrayList<SurveyFunding>();
        svForm.setEditAct(false);
        if (svForm.isEditAct() == true) {
        	surveyColl = (List<SurveyFunding>) DbUtil.getAllSurveysByActivity(svForm.getActivityId(), svForm);
        	Collections.sort(surveyColl, sfComp);
            svForm.setSurveyFundings(surveyColl);
        } else {
        	//This is a new activity not saved yet.
        	//If the activity has fundings then a survey can be taken.
        	if(svForm.getFunding() != null 
        			&& svForm.getFunding().getFundingOrganizations()!= null 
        			&& svForm.getFunding().getFundingOrganizations().size() > 0 
        			&& svForm.getFunding().getFundingDetails() != null
        			&& svForm.getFunding().getFundingDetails().size() > 0) {
        		Iterator<FundingOrganization> iterOrgFundings = svForm.getFunding().getFundingOrganizations().iterator();
        		int tempID = 0;
        		while (iterOrgFundings.hasNext()) {
        			FundingOrganization fundingOrganization = iterOrgFundings.next();
        			AmpOrganisation auxOrg = DbUtil.getOrganisation(fundingOrganization.getAmpOrgId());
        			if(auxOrg.getOrgGrpId().getOrgType().getOrgTypeCode().equalsIgnoreCase("BIL") || 
        					auxOrg.getOrgGrpId().getOrgType().getOrgTypeCode().equalsIgnoreCase("MUL")) {
	        			SurveyFunding auxSurveyFunding = new SurveyFunding();
	        			AmpOrganisation auxOrganization = DbUtil.getOrganisation(fundingOrganization.getAmpOrgId());
	        			auxSurveyFunding.setAcronim(auxOrganization.getOrgCode());
	        			auxSurveyFunding.setOrgID(fundingOrganization.getAmpOrgId());
	        			//TODO: Check if the user entered a second time and changed the Point of Delivery Donor.
	        			auxSurveyFunding.setDeliveryDonorName(auxOrganization.getName());
	        			auxSurveyFunding.setFundingOrgName(auxOrganization.getName());
	        			//I have to assign an ID, for new surveys I use negative numbers and the donor id.
	        			tempID = fundingOrganization.getAmpOrgId().intValue() * -1;
	        			auxSurveyFunding.setSurveyId(new Long(tempID));
	        			surveyColl.add(auxSurveyFunding);
        			}
        		}
        		svForm.setSurveyFundings(surveyColl);
        	}
        }
		return mapping.findForward("addActivityStep17");
	}

private ActionForward showStep2(ActionMapping mapping,
		HttpServletRequest request, HttpSession session, TeamMember teamMember,
		EditActivityForm eaForm) {
	if (eaForm.getContext() == null) {
	      SiteDomain currentDomain = RequestUtils.getSiteDomain(request);

	      String url = SiteUtils.getSiteURL(currentDomain, request.getScheme(),
	                                        request.getServerPort(),
	                                        request.getContextPath());
	      eaForm.setContext(url);
	    }

	  if (eaForm.getCrossIssues().getEqualOpportunity() == null ||
	            eaForm.getCrossIssues().getEqualOpportunity().trim().length() == 0) {
	          eaForm.getCrossIssues().setEqualOpportunity("aim-eo-" + teamMember.getMemberId() + "-" +
	                               System.currentTimeMillis());
	          setEditorKey(eaForm.getCrossIssues().getEqualOpportunity(), request);
	        }
	  if (eaForm.getCrossIssues().getEnvironment() == null ||
	        eaForm.getCrossIssues().getEnvironment().trim().length() == 0) {
	      eaForm.getCrossIssues().setEnvironment("aim-env-" + teamMember.getMemberId() + "-" +
	                           System.currentTimeMillis());
	      setEditorKey(eaForm.getCrossIssues().getEnvironment(), request);
	    }
	  if (eaForm.getCrossIssues().getMinorities() == null ||
	        eaForm.getCrossIssues().getMinorities().trim().length() == 0) {
	      eaForm.getCrossIssues().setMinorities("aim-min-" + teamMember.getMemberId() + "-" +
	                           System.currentTimeMillis());
	      setEditorKey(eaForm.getCrossIssues().getMinorities(), request);
	    }
	  eaForm.getSectors().setPrimarySectorVisible(FeaturesUtil.isVisibleSectors("Primary", ampContext)?"true":"false");
	  eaForm.getSectors().setSecondarySectorVisible(FeaturesUtil.isVisibleSectors("Secondary", ampContext)?"true":"false");
      eaForm.getSectors().setTertiarySectorVisible(FeaturesUtil.isVisibleSectors("Tertiary", ampContext)?"true":"false");
      session.setAttribute("Tertiary Sector", eaForm.getSectors().getTertiarySectorVisible());
	  session.setAttribute("Primary Sector", eaForm.getSectors().getPrimarySectorVisible());
	  session.setAttribute("Secondary Sector", eaForm.getSectors().getSecondarySectorVisible());
	return mapping.findForward("addActivityStep2");
}

private ActionForward showStep1(ActionMapping mapping,
		HttpServletRequest request, TeamMember teamMember,
		EditActivityForm eaForm) throws EditorException, AimException,
		RepositoryException, DgException {
	if (eaForm.getContext() == null) {
	  SiteDomain currentDomain = RequestUtils.getSiteDomain(request);

	  String url = SiteUtils.getSiteURL(currentDomain, request.getScheme(),
	                                    request.getServerPort(),
	                                    request.getContextPath());
	  eaForm.setContext(url);
	}

	/*
	 * AMP uses the editor module of the DiGi java framework to store the description and
	 * objectives in the html form. The editor module requires an entry in the DG_EDITOR table
	 * for the fields which needs to be shown in html format. So a key is generated for both the
	 * description and objective fields. The logic for generating the key for description is to
	 * append teamMember id and the current time to the string "aim-desc". The logic for generating
	 * key for objective is to append the team member id and the current time to the string "aim-obj".
	 * Initially the contents for both the description and objectives are set as a blank string
	 */
	// Creating a new entry in the DG_EDITOR table for description with the initial value for description as " "
	if (eaForm.getIdentification().getDescription() == null ||
	    eaForm.getIdentification().getDescription().trim().length() == 0) {
	  eaForm.getIdentification().setDescription("aim-desc-" + teamMember.getMemberId() + "-" +
	                        System.currentTimeMillis());
	  User user = RequestUtils.getUser(request);
	  String currentLang = RequestUtils.getNavigationLanguage(request).
	      getCode();
	  String refUrl = RequestUtils.getSourceURL(request);
	  String key = eaForm.getIdentification().getDescription();
	  Editor ed = org.digijava.module.editor.util.DbUtil.createEditor(user,
	      currentLang,
	      refUrl,
	      key,
	      key,
	      " ",
	      null,
	      request);
	  ed.setLastModDate(new Date());
	  ed.setGroupName(Constants.GROUP_OTHER);
	  org.digijava.module.editor.util.DbUtil.saveEditor(ed);
	}
//	TODO : this already exist delete it
//	//---
//	if (eaForm.getIdentification().getPurpose() == null ||
//	    eaForm.getIdentification().getPurpose().trim().length() == 0) {
//	  eaForm.getIdentification().setPurpose("aim-purp-" + teamMember.getMemberId() + "-" +
//	                    System.currentTimeMillis());
//	  User user = RequestUtils.getUser(request);
//	  String currentLang = RequestUtils.getNavigationLanguage(request).
//	      getCode();
//	  String refUrl = RequestUtils.getSourceURL(request);
//	  String key = eaForm.getIdentification().getPurpose();
//	  Editor ed = org.digijava.module.editor.util.DbUtil.createEditor(user,
//	      currentLang,
//	      refUrl,
//	      key,
//	      key,
//	      " ",
//	      null,
//	      request);
//	  ed.setLastModDate(new Date());
//	  ed.setGroupName(Constants.GROUP_OTHER);
//	  org.digijava.module.editor.util.DbUtil.saveEditor(ed);
//	}

//      ---
	if (eaForm.getIdentification().getLessonsLearned() == null ||
	    eaForm.getIdentification().getLessonsLearned().trim().length() == 0) {
	  eaForm.getIdentification().setLessonsLearned("aim-less-" + teamMember.getMemberId() + "-" +
	                    System.currentTimeMillis());
	  User user = RequestUtils.getUser(request);
	  String currentLang = RequestUtils.getNavigationLanguage(request).
	      getCode();
	  String refUrl = RequestUtils.getSourceURL(request);
	  String key = eaForm.getIdentification().getLessonsLearned();
	  Editor ed = org.digijava.module.editor.util.DbUtil.createEditor(user,
	      currentLang,
	      refUrl,
	      key,
	      key,
	      " ",
	      null,
	      request);
	  ed.setLastModDate(new Date());
	  ed.setGroupName(Constants.GROUP_OTHER);
	  org.digijava.module.editor.util.DbUtil.saveEditor(ed);
	}
	eaForm.getIdentification().setProjectImpact(Util.initLargeTextProperty("aim-projimp-",eaForm.getIdentification().getProjectImpact(), request));
	eaForm.getIdentification().setActivitySummary(Util.initLargeTextProperty("aim-actsum-",eaForm.getIdentification().getActivitySummary(), request));
	eaForm.getIdentification().setContractingArrangements(Util.initLargeTextProperty("aim-contrarr-",eaForm.getIdentification().getContractingArrangements(), request));
	eaForm.getIdentification().setCondSeq(Util.initLargeTextProperty("aim-condseq-",eaForm.getIdentification().getCondSeq(), request));
	eaForm.getIdentification().setLinkedActivities(Util.initLargeTextProperty("aim-linkedact-",eaForm.getIdentification().getLinkedActivities(), request));
	eaForm.getIdentification().setConditionality(Util.initLargeTextProperty("aim-conditional-",eaForm.getIdentification().getConditionality(), request));
	eaForm.getIdentification().setProjectManagement(Util.initLargeTextProperty("aim-projmanag-",eaForm.getIdentification().getProjectManagement(), request));
	eaForm.getContracts().setContractDetails(Util.initLargeTextProperty("aim-contrdetail-",eaForm.getContracts().getContractDetails(), request));
	eaForm.getIdentification().setBudgetCodes(ActivityUtil.getBudgetCodes());
	
	try {
	 AmpCategoryValue budgetOff =  CategoryManagerUtil.getAmpCategoryValueFromDB(CategoryConstants.ACTIVITY_BUDGET_OFF);	
     eaForm.getIdentification().setBudgetCVOff(
   		  (budgetOff==null)?0:budgetOff.getId()
   		  );
     AmpCategoryValue budgetOn =  CategoryManagerUtil.getAmpCategoryValueFromDB(CategoryConstants.ACTIVITY_BUDGET_ON);	
     eaForm.getIdentification().setBudgetCVOn(
   		  (budgetOn==null)?1:budgetOn.getId()
   		  );
     
    //if turned on from GS, put as default the Off Budget option if the current team is of type Computed
 	boolean computedTeamsDefaultOffBudget = "true".equalsIgnoreCase(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.COMPUTED_TEAMS_DEFAULT_OFF_BUDGET));
	AmpTeam ampTeam = TeamUtil.getAmpTeam(teamMember.getTeamId());
	if (computedTeamsDefaultOffBudget && ampTeam.getComputation() && eaForm.getActivityId()==null)
		eaForm.getIdentification().setBudgetCV(eaForm.getIdentification().getBudgetCVOff());
	
	}
	catch (Exception e) {
		e.printStackTrace();
	}
	
	//Budget classification
	if (eaForm.getIdentification().getBudgetsectors()==null || eaForm.getIdentification().getBudgetsectors().size()==0){
		eaForm.getIdentification().setSelectedbudgedsector(ActivityUtil.getBudgetSector(eaForm.getActivityId()));
	}
	if (eaForm.getIdentification().getSelectedorg()==null || eaForm.getIdentification().getSelectedorg()==0){
		eaForm.getIdentification().setSelectedorg((ActivityUtil.getBudgetOrganization(eaForm.getActivityId())));
	}
	if (eaForm.getIdentification().getSelecteddepartment()==null || eaForm.getIdentification().getSelecteddepartment()==0){
		eaForm.getIdentification().setSelecteddepartment(ActivityUtil.getBudgetDepartment(eaForm.getActivityId()));
	}
	if (eaForm.getIdentification().getSelectedprogram()==null || eaForm.getIdentification().getSelectedprogram()==0){
		eaForm.getIdentification().setSelectedprogram(ActivityUtil.getBudgetProgram(eaForm.getActivityId()));
	}
	
	
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
	
	//chapter
    eaForm.getIdentification().setChapterYears(Util.createBeanWrapperItemsCollection(ChapterUtil.getDistinctChapterYearList()));
    if(eaForm.getIdentification().getChapterYear()!=null) 
    	eaForm.getIdentification().setChapterCodes(Util.createBeanWrapperItemsCollection(ChapterUtil.getChapterListForYear(eaForm.getIdentification().getChapterYear())));
    
	
	if (eaForm.getIdentification().getResults() == null ||
	    eaForm.getIdentification().getResults().trim().length() == 0) {
	  eaForm.getIdentification().setResults("aim-results-" + teamMember.getMemberId() + "-" +
	                    System.currentTimeMillis());
	  User user = RequestUtils.getUser(request);
	  String currentLang = RequestUtils.getNavigationLanguage(request).
	      getCode();
	  String refUrl = RequestUtils.getSourceURL(request);
	  String key = eaForm.getIdentification().getResults();
	  Editor ed = org.digijava.module.editor.util.DbUtil.createEditor(user,
	      currentLang,
	      refUrl,
	      key,
	      key,
	      " ",
	      null,
	      request);
	  ed.setLastModDate(new Date());
	  ed.setGroupName(Constants.GROUP_OTHER);
	  org.digijava.module.editor.util.DbUtil.saveEditor(ed);
	}
	//---

	//---
	if (eaForm.getIdentification().getPurpose() == null ||
	    eaForm.getIdentification().getPurpose().trim().length() == 0) {
	  eaForm.getIdentification().setPurpose("aim-purp-" + teamMember.getMemberId() + "-" +
	                    System.currentTimeMillis());
	  User user = RequestUtils.getUser(request);
	  String currentLang = RequestUtils.getNavigationLanguage(request).
	      getCode();
	  String refUrl = RequestUtils.getSourceURL(request);
	  String key = eaForm.getIdentification().getPurpose();
	  Editor ed = org.digijava.module.editor.util.DbUtil.createEditor(user,
	      currentLang,
	      refUrl,
	      key,
	      key,
	      " ",
	      null,
	      request);
	  ed.setLastModDate(new Date());
	  ed.setGroupName(Constants.GROUP_OTHER);
	  org.digijava.module.editor.util.DbUtil.saveEditor(ed);
	}

	if (eaForm.getIdentification().getResults() == null ||
	    eaForm.getIdentification().getResults().trim().length() == 0) {
	  eaForm.getIdentification().setResults("aim-results-" + teamMember.getMemberId() + "-" +
	                    System.currentTimeMillis());
	  User user = RequestUtils.getUser(request);
	  String currentLang = RequestUtils.getNavigationLanguage(request).
	      getCode();
	  String refUrl = RequestUtils.getSourceURL(request);
	  String key = eaForm.getIdentification().getResults();
	  Editor ed = org.digijava.module.editor.util.DbUtil.createEditor(user,
	      currentLang,
	      refUrl,
	      key,
	      key,
	      " ",
	      null,
	      request);
	  ed.setLastModDate(new Date());
	  ed.setGroupName(Constants.GROUP_OTHER);
	  org.digijava.module.editor.util.DbUtil.saveEditor(ed);
	}
	//---

	// Creating a new entry in the DG_EDITOR table for objective with the initial value for objective as " "
	if (eaForm.getIdentification().getObjectives() == null ||
		    eaForm.getIdentification().getObjectives().trim().length() == 0) {
		  eaForm.getIdentification().setObjectives("aim-obj-" + teamMember.getMemberId() + "-" +
		                       System.currentTimeMillis());
		  User user = RequestUtils.getUser(request);
		  String currentLang = RequestUtils.getNavigationLanguage(request).
		      getCode();
		  String refUrl = RequestUtils.getSourceURL(request);
		  String key = eaForm.getIdentification().getObjectives();
		  Editor ed = org.digijava.module.editor.util.DbUtil.createEditor(user,
		      currentLang,
		      refUrl,
		      key,
		      key,
		      " ",
		      null,
		      request);
		  ed.setLastModDate(new Date());
		  ed.setGroupName(Constants.GROUP_OTHER);
		  org.digijava.module.editor.util.DbUtil.saveEditor(ed);
		}

	if (eaForm.getIdentification().getProjectComments() == null ||
		    eaForm.getIdentification().getProjectComments().trim().length() == 0) {
		  eaForm.getIdentification().setProjectComments("aim-projcom-" + teamMember.getMemberId() + "-" +
		                       System.currentTimeMillis());
		  User user = RequestUtils.getUser(request);
		  String currentLang = RequestUtils.getNavigationLanguage(request).
		      getCode();
		  String refUrl = RequestUtils.getSourceURL(request);
		  String key = eaForm.getIdentification().getProjectComments();
		  Editor ed = org.digijava.module.editor.util.DbUtil.createEditor(user,
		      currentLang,
		      refUrl,
		      key,
		      key,
		      " ",
		      null,
		      request);
		  ed.setLastModDate(new Date());
		  ed.setGroupName(Constants.GROUP_OTHER);
		  org.digijava.module.editor.util.DbUtil.saveEditor(ed);
		}

	// Exactly as description/objectives, AMP is using DigiJava's document
	// management module to store documents (if enabled). Before storing
	// Documents, we need to create space there.
	// Later, we will give to space user-friendly name
	if (DocumentUtil.isDMEnabled()) {
	  if (eaForm.getDocuments().getDocumentSpace() == null ||
	      eaForm.getDocuments().getDocumentSpace().trim().length() == 0) {
	    eaForm.getDocuments().setDocumentSpace("aim-document-space-" +
	                            teamMember.getMemberId() +
	                            "-" + System.currentTimeMillis());
	    Site currentSite = RequestUtils.getSite(request);
	    DocumentUtil.createDocumentSpace(currentSite,
	                                     eaForm.getDocuments().getDocumentSpace());
	  }
	}
	eaForm.setReset(false);

	// loading Activity Rank collection
	if (null == eaForm.getPlanning().getActRankCollection()) {
	  eaForm.getPlanning().setActRankCollection(new ArrayList());
	  for (int i = 1; i < 6; i++)
	    eaForm.getPlanning().getActRankCollection().add(new Integer(i));
	}
  

	if (eaForm.getCosting().getCosts() != null && eaForm.getCosting().getCosts().size() != 0) {
	  double grandCost = 0;
	  double grandContribution = 0;
	  Long currencyId = teamMember.getAppSettings().getCurrencyId();
	  Iterator i = eaForm.getCosting().getCosts().iterator();
	  while (i.hasNext()) {
	    EUActivity element = (EUActivity) i.next();
	    element.setDesktopCurrencyId(currencyId);
	    grandCost += element.getTotalCostConverted();
	    grandContribution += element.getTotalContributionsConverted();
	  }
	  eaForm.getCosting().setOverallCost(new Double(grandCost));
	  eaForm.getCosting().setOverallContribution(new Double(grandContribution));

	}

//			Collection statusCol = null; TO BE DELETED
//			// load the status from the database
//			if(eaForm.getStatusCollection() == null) {
//				statusCol= DbUtil.getAmpStatus();
//				eaForm.setStatusCollection(statusCol);
//			}
//			else {
//				statusCol = eaForm.getStatusCollection();
//			}
	// Initially setting the implementation level as "country"
	//get all possible refdoc names from categories
	Collection<AmpCategoryValue> catValues	= null;
	try {
		catValues = CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.REFERENCE_DOCS_KEY,false, request);
	} catch (Exception e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}

	if (catValues!=null && eaForm.getDocuments().getReferenceDocs()==null){
		List<ReferenceDoc> refDocs=new ArrayList<ReferenceDoc>();
		Collection<AmpActivityReferenceDoc> activityRefDocs=null;
		Map<Long, AmpActivityReferenceDoc> categoryRefDocMap=null;

		if (eaForm.getActivityId()!=null){
			//get list of ref docs for activity
			activityRefDocs=ActivityUtil.getReferenceDocumentsFor(eaForm.getActivityId());
	    	//create map where keys are category value ids.
			categoryRefDocMap = AmpCollectionUtils.createMap(
					activityRefDocs,
					new ActivityUtil.CategoryIdRefDocMapBuilder());
		}

		//create arrays, number of elements as much as category values
		Long[] refdocIds=new Long[catValues.size()];
		String[] refdocComments=new String[catValues.size()];

		int c=0;
		int selectedIds=0;
		for(AmpCategoryValue catVal: catValues){
			AmpActivityReferenceDoc refDoc=(categoryRefDocMap==null)?null:categoryRefDocMap.get(catVal.getId());
			ReferenceDoc doc=new ReferenceDoc();
			doc.setCategoryValueId(catVal.getId());
			doc.setCategoryValue(catVal.getValue());
			if (refDoc==null){
				refdocComments[c]="";
				doc.setComment("");
				doc.setChecked(false);
			}else{
				refdocIds[selectedIds++]=refDoc.getCategoryValue().getId();
				refdocComments[c]=refDoc.getComment();
				doc.setComment(refDoc.getComment());
				doc.setRefDocId(refDoc.getId());
				doc.setChecked(true);
			}
			refDocs.add(doc);
			c++;
		}

		//set selected ids
		eaForm.getDocuments().setAllReferenceDocNameIds(refdocIds);
		//set all comments, some are empty
//        	eaForm.setRefDocComments(refdocComments);
		if(refDocs.size()>0){
			ReferenceDoc[] myrefDoc = (ReferenceDoc[]) refDocs.toArray(new ReferenceDoc[0]);
			eaForm.getDocuments().setReferenceDocs(myrefDoc);
		}
		else{
			eaForm.getDocuments().setReferenceDocs(null);
		}

	}

	// Initally set the modality as "Project Support"
	Collection financingInstrValues		= null;
	try {
		financingInstrValues = CategoryManagerUtil.
		    getAmpCategoryValueCollectionByKey(CategoryConstants.FINANCING_INSTRUMENT_KEY, null, request);
	} catch (Exception e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	Iterator iter = financingInstrValues.iterator();
	while (iter.hasNext()) {
	  AmpCategoryValue financingInstrVal = (AmpCategoryValue) iter.next();
	  if(financingInstrVal!=null)
		  if ("Project Support".equalsIgnoreCase(financingInstrVal.getValue())) {
			  eaForm.getFunding().setModality(financingInstrVal.getId());
		  }
	}


	// load all the active currencies
	eaForm.setCurrencies(CurrencyUtil.getActiveAmpCurrencyByName());

	try {
		eaForm.getFunding().setProjections(CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.MTEF_PROJECTION_KEY, false, request));
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}


	eaForm.getFunding().setFundingRegionId(new Long( -1));
	  if (eaForm.getSteps() == null) {
	      List steps = ActivityUtil.getSteps();
	      eaForm.setSteps(steps);
	  }
	  
	//get yearsRange
	  List<LabelValueBean> yearsRange = EditOrganisation.getYearsBeanList();
	  eaForm.getIdentification().setYearsRange(yearsRange);
	  
     
	return mapping.findForward("addActivityStep1");
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

public static Documents createHelperDocument (CMSContentItem cmsItem, Long activityId, String activityName) {
		Documents document = new Documents();
      document.setActivityId( activityId );
      document.setActivityName( activityName );
      document.setDocId(new Long(cmsItem.getId()));
      document.setTitle(cmsItem.getTitle());
      document.setIsFile(cmsItem.getIsFile());
      document.setFileName(cmsItem.getFileName());
      document.setUrl(cmsItem.getUrl());
      document.setDocDescription(cmsItem.getDescription());
      document.setDate(cmsItem.getDate());
      if (cmsItem.getDocType() != null)
      	document.setDocType(cmsItem.getDocType().getValue());

      if (cmsItem.getDocLanguage() != null)
      	document.setDocLanguage( cmsItem.getDocLanguage().getValue() );
      document.setDocComment( cmsItem.getDocComment() );

      return document;
	}

  public void setEditorKey(String s, HttpServletRequest request)
  {
	  User user = RequestUtils.getUser(request);
      String currentLang = RequestUtils.getNavigationLanguage(request).
          getCode();
      String refUrl = RequestUtils.getSourceURL(request);
      String key = s;
      Editor ed = org.digijava.module.editor.util.DbUtil.createEditor(user,
          currentLang,
          refUrl,
          key,
          key,
          " ",
          null,
          request);
      ed.setLastModDate(new Date());
      ed.setGroupName(Constants.GROUP_OTHER);
      try {
		org.digijava.module.editor.util.DbUtil.saveEditor(ed);
	} catch (EditorException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
}
  }
}

class ProgramComparator
    implements Comparator {
  public int compare(Object o1, Object o2) {
    AmpTheme i1 = (AmpTheme) o1;
    AmpTheme i2 = (AmpTheme) o2;

    Long sk1 = i1.getAmpThemeId();
    Long sk2 = i2.getAmpThemeId();

    return sk1.compareTo(sk2);
  }
}

class HierarchicalDefinition
    implements HierarchyDefinition {
  public Object getObjectIdentity(Object object) {
    AmpTheme i = (AmpTheme) object;
    return i.getAmpThemeId();

  }

  public Object getParentIdentity(Object object) {
    AmpTheme i = (AmpTheme) object;
    if (i.getParentThemeId() == null) {
      return null;
    }
    else {
      return i.getParentThemeId().getAmpThemeId();
    }
  }
  
  
}
