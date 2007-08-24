/*
 * AddAmpActivity.java
 */

package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import org.digijava.module.aim.dbentity.AmpField; 
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.ar.ReportData;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.kernel.util.collections.HierarchyDefinition;
import org.digijava.module.aim.dbentity.AmpCategoryClass;
import org.digijava.module.aim.dbentity.AmpCategoryValue;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.dbentity.AmpModality;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.EUActivity;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.form.ProposedProjCost;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.CategoryConstants;
import org.digijava.module.aim.helper.CategoryManagerUtil;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DocumentUtil;
import org.digijava.module.aim.util.EUActivityUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.MEIndicatorsUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.editor.util.Constants;

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

	//private static Logger logger = Logger.getLogger(AddAmpActivity.class);

	private ServletContext ampContext = null;

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {

		HttpSession session = request.getSession();

		ampContext = getServlet().getServletContext();

		TeamMember teamMember= new TeamMember();
		// Get the current member who has logged in from the session
		teamMember=(TeamMember)session.getAttribute("currentMember");
				// if user is not logged in, forward him to the home page
		//if (session.getAttribute("currentMember") == null)
			//return mapping.findForward("index");

		//return mapping.findForward("publicPreview");
		
		EditActivityForm eaForm = (EditActivityForm) form;
		// Add sectors
		if (request.getParameter("addSector") != null){
			ActivitySector sect = (ActivitySector) session.getAttribute("sectorSelected");
			session.removeAttribute("sectorSelected");
			Collection prevSelSectors = eaForm.getActivitySectors();
			if (prevSelSectors != null){
				if (prevSelSectors.isEmpty())
					sect.setSectorPercentage(new Integer(100));
				prevSelSectors.add(sect);
			}
			else{
				sect.setSectorPercentage(new Integer(100));
				prevSelSectors = new ArrayList();
				prevSelSectors.add(sect);
			}
			
				Collection col =FeaturesUtil.getGlobalSettings();
	            Iterator itr = col.iterator();
	            String view=null;
	            while(itr.hasNext())
	           {
	             AmpGlobalSettings args = (AmpGlobalSettings)itr.next();
	             view = args.getGlobalSettingsValue();
	           }
		            if (view.equalsIgnoreCase("On"))
					{
						sect.setCount(1);
					}
					  else 
					{
						sect.setCount(2);
					}
		
			eaForm.setActivitySectors(prevSelSectors);
			return mapping.findForward("addActivityStep2");
	}
		// Remove sectors
		else
			if (request.getParameter("remSectors") != null){
				Long selSectors[] = eaForm.getSelActivitySectors();
				Collection prevSelSectors = eaForm.getActivitySectors();
				Collection newSectors = new ArrayList();

				Iterator itr = prevSelSectors.iterator();

		        boolean flag =false;

				while (itr.hasNext()) {
					ActivitySector asec = (ActivitySector) itr.next();
		            flag=false;
					for (int i = 0; i < selSectors.length; i++) {
						if (asec.getSectorId().equals(selSectors[i])) {
							flag=true;
		                    break;
						}
					}

		            if(!flag){
		                newSectors.add(asec);
		            }
				}

				eaForm.setActivitySectors(newSectors);
				return mapping.findForward("addActivityStep2");
			}
		//
		
		//we use this pointer to simplify adding items in the selectors: 
		session.setAttribute("eaf",eaForm);
		String logframepr=(String)session.getAttribute("logframepr");//logframepreview
		if(logframepr==null || logframepr=="") logframepr="";
		if(logframepr.compareTo("true")==0){
				eaForm.setStep("9");
				eaForm.setPageId(1);
			}
		
		
		//eaForm.setAllComps(ActivityUtil.getAllComponentNames());
        ProposedProjCost propProjCost=null;
        if(eaForm.getProProjCost()!=null){
            propProjCost=eaForm.getProProjCost();
            if(propProjCost.getCurrencyCode()==null &&
               propProjCost.getFunAmount()==null &&
               propProjCost.getFunDate()==null){
                eaForm.setProProjCost(null);
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
		String action = request.getParameter("action");
		if (action != null && action.trim().length() != 0) {
			if ("create".equals(action)) {
				eaForm.getCommentsCol().clear();
				eaForm.setCommentFlag(false);
                eaForm.setProProjCost(null);
                eaForm.setActPrograms(null);
			}
		}
		// end

        Collection themes=new ArrayList();
        themes = ProgramUtil.getAllThemes();
        eaForm.setProgramCollection(themes);


        // added by Akash
		// desc: setting WorkingTeamLeadFlag & approval status in form bean
		// start
        boolean teamLeadFlag=false;
        boolean workingTeamFlag=true;
        if(teamMember!=null) {
		Long ampTeamId = teamMember.getTeamId();
			teamLeadFlag = teamMember.getTeamHead();
			workingTeamFlag = TeamUtil.checkForParentTeam(ampTeamId);
        }
    
		if (teamLeadFlag && workingTeamFlag)
			eaForm.setWorkingTeamLeadFlag("yes");
		else
			eaForm.setWorkingTeamLeadFlag("no");
    
		if (!eaForm.isEditAct() || logframepr.compareTo("true")==0){
			if(teamMember!=null)
			if (teamMember.getTeamHead())
				eaForm.setApprovalStatus("approved");
			else
				eaForm.setApprovalStatus("started");
		}
		else {
			String sessId = session.getId();
			ArrayList sessList = (ArrayList) ampContext.getAttribute(
					org.digijava.module.aim.helper.Constants.SESSION_LIST);
			if (sessList.contains(sessId) == false) {
				ActionErrors errors = new ActionErrors();
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
						"error.aim.activityAlreadyOpenedForEdit"));
				saveErrors(request, errors);

				String url = "/aim/viewChannelOverview.do?ampActivityId="
						+ eaForm.getActivityId() + "&tabIndex=0";
				RequestDispatcher rd = getServlet().getServletContext()
						.getRequestDispatcher(url);
				rd.forward(request, response);
			}

			synchronized (ampContext) {
				HashMap tsList = (HashMap) ampContext.getAttribute(org.digijava.module.aim.helper.Constants.TS_ACT_LIST);
				if (tsList != null) {
					tsList.put(eaForm.getActivityId(),new Long(System.currentTimeMillis()));
				}
				ampContext.setAttribute(org.digijava.module.aim.helper.Constants.TS_ACT_LIST,tsList);
			}
		}
		// end

		if (eaForm.getStep().equals("1")) { // show the step 1 page.

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
		    if (eaForm.getDescription() == null || eaForm.getDescription().trim().length() == 0) {
		        eaForm.setDescription("aim-desc-" + teamMember.getMemberId() + "-" + System.currentTimeMillis());
				User user = RequestUtils.getUser(request);
		        String currentLang = RequestUtils.getNavigationLanguage(request).getCode();
		        String refUrl = RequestUtils.getSourceURL(request);
		        String key = eaForm.getDescription();
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
		    if (eaForm.getPurpose() == null || eaForm.getPurpose().trim().length() == 0) {
		        eaForm.setPurpose("aim-purp-" + teamMember.getMemberId() + "-" + System.currentTimeMillis());
				User user = RequestUtils.getUser(request);
		        String currentLang = RequestUtils.getNavigationLanguage(request).getCode();
		        String refUrl = RequestUtils.getSourceURL(request);
		        String key = eaForm.getPurpose();
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
		    
		    if (eaForm.getResults() == null || eaForm.getResults().trim().length() == 0) {
		        eaForm.setResults("aim-results-" + teamMember.getMemberId() + "-" + System.currentTimeMillis());
				User user = RequestUtils.getUser(request);
		        String currentLang = RequestUtils.getNavigationLanguage(request).getCode();
		        String refUrl = RequestUtils.getSourceURL(request);
		        String key = eaForm.getResults();
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
		    if (eaForm.getPurpose() == null || eaForm.getPurpose().trim().length() == 0) {
		        eaForm.setPurpose("aim-purp-" + teamMember.getMemberId() + "-" + System.currentTimeMillis());
				User user = RequestUtils.getUser(request);
		        String currentLang = RequestUtils.getNavigationLanguage(request).getCode();
		        String refUrl = RequestUtils.getSourceURL(request);
		        String key = eaForm.getPurpose();
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
		    
		    if (eaForm.getResults() == null || eaForm.getResults().trim().length() == 0) {
		        eaForm.setResults("aim-results-" + teamMember.getMemberId() + "-" + System.currentTimeMillis());
				User user = RequestUtils.getUser(request);
		        String currentLang = RequestUtils.getNavigationLanguage(request).getCode();
		        String refUrl = RequestUtils.getSourceURL(request);
		        String key = eaForm.getResults();
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
		    if (eaForm.getObjectives() == null || eaForm.getObjectives().trim().length() == 0) {
		        eaForm.setObjectives("aim-obj-" + teamMember.getMemberId() + "-" + System.currentTimeMillis());
				User user = RequestUtils.getUser(request);
		        String currentLang = RequestUtils.getNavigationLanguage(request).getCode();
		        String refUrl = RequestUtils.getSourceURL(request);
		        String key = eaForm.getObjectives();
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
            if(DocumentUtil.isDMEnabled()) {
                if(eaForm.getDocumentSpace() == null ||
                   eaForm.getDocumentSpace().trim().length() == 0) {
                    eaForm.setDocumentSpace("aim-document-space-" +
                                            teamMember.getMemberId() +
                                            "-" + System.currentTimeMillis());
                    Site currentSite = RequestUtils.getSite(request);
                    DocumentUtil.createDocumentSpace(currentSite, eaForm.getDocumentSpace());
                }
            }
			eaForm.setReset(false);

			// loading Activity Rank collection
			if (null == eaForm.getActRankCollection()) {
				eaForm.setActRankCollection(new ArrayList());
				for (int i = 1; i < 6; i++)
					eaForm.getActRankCollection().add(new Integer(i));
			}

			if(eaForm.getCosts()!=null && eaForm.getCosts().size()!=0) {
				double grandCost = 0;
				double grandContribution = 0;
				Long currencyId = teamMember.getAppSettings().getCurrencyId();
				Iterator i=eaForm.getCosts().iterator();
				while (i.hasNext()) {
					EUActivity element = (EUActivity) i.next();
					element.setDesktopCurrencyId(currencyId);
					grandCost += element.getTotalCostConverted();
					grandContribution += element.getTotalContributionsConverted();
				}
				eaForm.setOverallCost(new Double(grandCost));
				eaForm.setOverallContribution(new Double(grandContribution));
				
				
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
			// Initailly setting the implementation level as "country"
			if (eaForm.getImplemLocationLevel() == null)
				eaForm.setImplemLocationLevel(
						CategoryManagerUtil.getAmpCategoryValueFromDb(CategoryConstants.IMPLEMENTATION_LOCATION_KEY, new Long(0)).getId()
				);

			Collection modalColl = null;
			// load the modalities from the database
			if (eaForm.getModalityCollection() == null) {
				modalColl = DbUtil.getAmpModality();
				eaForm.setModalityCollection(modalColl);
			} else {
				modalColl = eaForm.getModalityCollection();
			}

			// Initally set the modality as "Project Support"
			if (modalColl != null && eaForm.getModality() == null) {
				Iterator itr = modalColl.iterator();
				while (itr.hasNext()) {
					AmpModality mod = (AmpModality) itr.next();
					if (mod.getName().equalsIgnoreCase("Project Support")) {
						eaForm.setModality(mod.getAmpModalityId());
						break;
					}
				}
			}
			Collection levelCol = null;
			// Loading the levels from the database
			if (eaForm.getLevelCollection() == null) {
				levelCol = DbUtil.getAmpLevels();
				eaForm.setLevelCollection(levelCol);
			} else {
				levelCol = eaForm.getLevelCollection();
			}

			// load all themes

			//eaForm.setProgramCollection(ProgramUtil.getAllThemes());

			// load all the active currencies
			eaForm.setCurrencies(CurrencyUtil.getAmpCurrency());

			// load all the perspectives
			eaForm.setPerspectives(DbUtil.getAmpPerspective());

			eaForm.setFundingRegionId(new Long(-1));
			return mapping.findForward("addActivityStep1");
		} else if (eaForm.getStep().equals("1.1")) { // shows the edit page of the editor module
			eaForm.setStep("1");
			// When the contents are saved the editor module redirects to the url specified in the 'referrer' parameter
		    String url = "/editor/showEditText.do?id="+eaForm.getEditKey()+"&referrer="+eaForm.getContext()+"/aim/addActivity.do?edit=true";
		    response.sendRedirect(eaForm.getContext() + url);
		} else if (eaForm.getStep().equals("2")) { // show the step 2 page.
			return mapping.findForward("addActivityStep2");
		} else if (eaForm.getStep().equals("3")) { // show the step 3 page.
			return mapping.findForward("addActivityStep3");
		} else if (eaForm.getStep().equals("4")) { // show the step 4 page.
			return mapping.findForward("addActivityStep4");
		} else if (eaForm.getStep().equals("5")) { // show the step 5 page.
			return mapping.findForward("addActivityStep5");
		} else if (eaForm.getStep().equals("6")) { // show the step 6 page.
			return mapping.findForward("addActivityStep6");
		} else if (eaForm.getStep().equals("7")) { // show the step 7 page.
			return mapping.findForward("addActivityStep7");
		} else if (eaForm.getStep().equals("8")) { // show the step 7 page.
			return mapping.findForward("addActivityStep8");
		} else if (eaForm.getStep().equals("11")) { // show the step 11 page.
			return mapping.findForward("addActivityStep11");
		} else if (eaForm.getStep().equals("9")) { // show the preview page.

			if (eaForm.getAmpId() == null) { // if AMP-ID is not generated, generate the AMP-ID
				/*
				 * The logic for geerating the AMP-ID is as follows:
				 * 1. get default global country code
				 * 2. Get the maximum of the ampActivityId + 1, MAX_NUM
				 * 3. merge them
				 */
				String ampId = 
					FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GLOBAL_DEFAULT_COUNTRY).toUpperCase();
				/*if (eaForm.getFundingOrganizations() != null) {
					if (eaForm.getFundingOrganizations().size() == 1) {
						Iterator itr = eaForm.getFundingOrganizations().iterator();
						if (itr.hasNext()) {
							FundingOrganization fOrg = (FundingOrganization) itr
									.next();
							ampId += "-" + DbUtil.getOrganisation(fOrg.getAmpOrgId()).getOrgCode();
						}
					}
				}*/
				
				
				long maxId = ActivityUtil.getActivityMaxId();
				maxId++;
				ampId += "/" + maxId;
				eaForm.setAmpId(ampId);
			}

			/*
			 * If the mode is 'Add', set the Activity Creator as the current logged in user
			 */
			if (teamMember!=null && (!eaForm.isEditAct()) &&
					(eaForm.getActAthEmail() == null || eaForm.getActAthEmail().trim().length() == 0)) {
				User usr = DbUtil.getUser(teamMember.getEmail());
				if (usr != null) {
					eaForm.setActAthFirstName(usr.getFirstNames());
					eaForm.setActAthLastName(usr.getLastName());
					eaForm.setActAthEmail(usr.getEmail());
					eaForm.setActAthAgencySource(usr.getOrganizationName());
				}
				
			}

			Collection euActs=EUActivityUtil.getEUActivities(eaForm.getActivityId());
			// EUActivities = same as Costs
			request.setAttribute("costs",euActs);
			request.setAttribute("actId", eaForm.getActivityId());
			int risk = MEIndicatorsUtil.getOverallRisk(eaForm.getActivityId());
			String riskName = MEIndicatorsUtil.getRiskRatingName(risk);
			String rskColor = MEIndicatorsUtil.getRiskColor(risk);
			request.setAttribute("overallRisk",riskName);
			request.setAttribute("riskColor",rskColor);
			
			Long prev = new Long(-1);
			Long next = new Long(-1);
			if (eaForm.getActivityId() != null){
				ReportData rep = (ReportData) session.getAttribute("report");
				if(rep!=null) {
				Collection ids = (Collection) rep.getOwnerIds();
				Iterator it = ids.iterator();
				
				while (it.hasNext()) {
					Long el = (Long) it.next();
					if (el.compareTo(eaForm.getActivityId()) == 0){
						if (it.hasNext())
							next = new Long(((Long)it.next()).longValue());
						break;
					}
					prev = new Long(el.longValue());				
				}
			}
			request.setAttribute("nextId", next);
			request.setAttribute("prevId", prev);
			}
			
//			if(eaForm.getStatusCollection() == null) { //TO BE DELETED
//				eaForm.setStatusCollection(DbUtil.getAmpStatus());
//			}
			if (eaForm.getModalityCollection() == null) {
				eaForm.setModalityCollection(DbUtil.getAmpModality());
			}

			if (eaForm.getLevelCollection() == null) {
				eaForm.setLevelCollection(DbUtil.getAmpLevels());
			}
			
			if(teamMember==null) return mapping.findForward("publicPreview"); 
			else 
				{
				ArrayList colAux=new ArrayList();
				Collection ampFields=DbUtil.getAmpFields();
				HashMap allComments=new HashMap();
				for(Iterator itAux=ampFields.iterator(); itAux.hasNext();)
				{
					AmpField field = (AmpField) itAux.next();
					colAux = DbUtil.getAllCommentsByField(field.getAmpFieldId(),eaForm.getActivityId());
					allComments.put(field.getFieldName(),colAux);
				}
				eaForm.setAllComments(allComments);
				eaForm.setCommentsCol(colAux);


					if(request.getParameter("logframe")!=null || logframepr.compareTo("true")==0)
						{
						eaForm.setIndicatorsME(
								MEIndicatorsUtil.getActivityIndicators(eaForm.getActivityId()));
						if(!eaForm.isEditAct())
						{
							eaForm.setIndicatorId(null);
							eaForm.setIndicatorValId(null);
							eaForm.setExpIndicatorId(null);
							eaForm.setBaseVal(0);
							eaForm.setBaseValDate(null);
							eaForm.setTargetVal(0);
							eaForm.setTargetValDate(null);
							eaForm.setRevTargetVal(0);
							eaForm.setRevTargetValDate(null);
							eaForm.setIndicatorPriorValues(null);
							eaForm.setCurrentVal(0);
							eaForm.setCurrentValDate(null);
							eaForm.setIndicatorRisk(null);
							
							
						}

						//get the levels of risks
						
						if(!eaForm.getIndicatorsME().isEmpty())
							eaForm.setRiskCollection(MEIndicatorsUtil.getAllIndicatorRisks());
							return mapping.findForward("previewLogframe");
						}
			 	    return mapping.findForward("preview");
				}
		} else if (eaForm.getStep().equals("10")) {		// show step 9 - M&E page

			eaForm.setIndicatorsME(
					MEIndicatorsUtil.getActivityIndicators(eaForm.getActivityId()));
			if(!eaForm.isEditAct())
			{
				eaForm.setIndicatorId(null);
				eaForm.setIndicatorValId(null);
				eaForm.setExpIndicatorId(null);
				eaForm.setBaseVal(0);
				eaForm.setBaseValDate(null);
				eaForm.setTargetVal(0);
				eaForm.setTargetValDate(null);
				eaForm.setRevTargetVal(0);
				eaForm.setRevTargetValDate(null);
				eaForm.setIndicatorPriorValues(null);
				eaForm.setCurrentVal(0);
				eaForm.setCurrentValDate(null);
				eaForm.setIndicatorRisk(null);
			}

			//get the levels of risks
			if(!eaForm.getIndicatorsME().isEmpty())
				eaForm.setRiskCollection(MEIndicatorsUtil.getAllIndicatorRisks());

			return mapping.findForward("addActivityStep10");
		}
		else {
			return mapping.findForward("adminHome");
		}
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		return null;
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

class HierarchicalDefinition implements HierarchyDefinition {
    public Object getObjectIdentity(Object object) {
        AmpTheme i = (AmpTheme) object;
        return i.getAmpThemeId();

    }
    public Object getParentIdentity(Object object) {
        AmpTheme i = (AmpTheme) object;
        if (i.getParentThemeId() == null) {
            return null;
        } else {
            return i.getParentThemeId().getAmpThemeId();
        }
    }
}
