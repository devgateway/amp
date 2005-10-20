package org.digijava.module.aim.action;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.dbentity.AmpModality;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.FundingOrganization;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.editor.util.Constants;


public class AddAmpActivity extends Action {
	
	private static Logger logger = Logger.getLogger(AddAmpActivity.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {

		HttpSession session = request.getSession();
		TeamMember teamMember= new TeamMember();
		teamMember=(TeamMember)session.getAttribute("currentMember");

		// if user is not logged in, forward him to the home page
		if (session.getAttribute("currentMember") == null)
			return mapping.findForward("index");

		EditActivityForm eaForm = (EditActivityForm) form;
		

		try {
		
		//String fileName = getRealPath(memId);

		if (!eaForm.isEditAct() || eaForm.isReset()) {
			eaForm.reset(mapping, request);
		}

		if (eaForm.getPageId() < 0 || eaForm.getPageId() > 2)
			return mapping.findForward("index");
		
		if (eaForm.getPageId() == 2)
			eaForm.setStep("9");
		
		// added by Akash
		// desc: clearing comment properties
		// start
		String action = request.getParameter("action");
		logger.debug("action [inside AddAmpActivity] : " + action);
		if (action != null && action.trim().length() != 0) {
			if ("create".equals(action)) {
				eaForm.getCommentsCol().clear();
				eaForm.setCommentFlag(false);
			}
		}
		// end
		
		// added by Akash
		// desc: setting WorkingTeamLeadFlag & approval status in form bean
		// start
		if (!eaForm.isEditAct()) {
			eaForm.setWorkingTeamLeadFlag("no");
			eaForm.setApprovalStatus("started");
		}
		else {
			Long ampTeamId = teamMember.getTeamId();
			boolean teamLeadFlag = teamMember.getTeamHead();
			boolean workingTeamFlag = DbUtil.checkForParentTeam(ampTeamId);
			if (teamLeadFlag && workingTeamFlag)
				eaForm.setWorkingTeamLeadFlag("yes");
			else
				eaForm.setWorkingTeamLeadFlag("no");
			String actApprovalStatus = DbUtil.getActivityApprovalStatus(eaForm.getActivityId());
			eaForm.setApprovalStatus(actApprovalStatus);
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
			eaForm.setReset(false);
			
			Collection statusCol = null;
			if(eaForm.getStatusCollection() == null) {
				statusCol= DbUtil.getAmpStatus();
				eaForm.setStatusCollection(statusCol);
			}
			else {
				statusCol = eaForm.getStatusCollection();
			}
			
			if (eaForm.getImplementationLevel() == null)
				eaForm.setImplementationLevel("country");

			Collection modalColl = null;
			if (eaForm.getModalityCollection() == null) {
				modalColl = DbUtil.getAmpModality();
				eaForm.setModalityCollection(modalColl);
			} else {
				modalColl = eaForm.getModalityCollection();
			}

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
			if (eaForm.getLevelCollection() == null) {
				levelCol = DbUtil.getAmpLevels();
				eaForm.setLevelCollection(levelCol);
			} else {
				levelCol = eaForm.getLevelCollection();
			}
			
			eaForm.setProgramCollection(ProgramUtil.getAllThemes());				
			eaForm.setCurrencies(DbUtil.getAmpCurrency());	
			eaForm.setPerspectives(DbUtil.getAmpPerspective());
			eaForm.setFundingRegionId(new Long(-1));
			return mapping.findForward("addActivityStep1");
		} else if (eaForm.getStep().equals("1.1")) {
			eaForm.setStep("1");
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
		} else if (eaForm.getStep().equals("9")) { // finish wizard. add the activity details.

			if (eaForm.getAmpId() == null) { // if AMP-ID is not generated, generate the AMP-ID
				String ampId = "AMP";
				if (eaForm.getFundingOrganizations() != null) {
					
					if (eaForm.getFundingOrganizations().size() == 1) {
						Iterator itr = eaForm.getFundingOrganizations().iterator();
						if (itr.hasNext()) {
							FundingOrganization fOrg = (FundingOrganization) itr
									.next();
							ampId += "-" + DbUtil.getOrganisation(fOrg.getAmpOrgId()).getOrgCode();
						}
					}
				}
				long maxId = DbUtil.getActivityMaxId();
				maxId++;
				ampId += "-" + maxId;
				eaForm.setAmpId(ampId);
			}
			
			if ((!eaForm.isEditAct()) && 
					(eaForm.getActAthEmail() == null || eaForm.getActAthEmail().trim().length() == 0)) {
				User usr = DbUtil.getUser(teamMember.getEmail());
				if (usr != null) {
					eaForm.setActAthFirstName(usr.getFirstNames());
					eaForm.setActAthLastName(usr.getLastName());
					eaForm.setActAthEmail(usr.getEmail());
				} else {
					logger.debug("Usr is null");					
				}
			}
			
			if(eaForm.getStatusCollection() == null) {
				eaForm.setStatusCollection(DbUtil.getAmpStatus());
			}
			if (eaForm.getModalityCollection() == null) {
				eaForm.setModalityCollection(DbUtil.getAmpModality());
			}

			if (eaForm.getLevelCollection() == null) {
				eaForm.setLevelCollection(DbUtil.getAmpLevels());
			}
				
			if (eaForm.getProgramCollection() == null) {
				eaForm.setProgramCollection(ProgramUtil.getAllThemes());				
			}
			
			return mapping.findForward("preview");
		} else {
			return mapping.findForward("adminHome");
		}
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		return null;
	}
}