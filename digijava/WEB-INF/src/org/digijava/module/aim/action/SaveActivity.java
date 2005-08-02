/*
 * SaveActivity.java
 */

package org.digijava.module.aim.action;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityClosingDates;
import org.digijava.module.aim.dbentity.AmpActivityInternalId;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpLevel;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpPhysicalPerformance;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpStatus;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.Components;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.DecimalToText;
import org.digijava.module.aim.helper.Funding;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.aim.helper.FundingOrganization;
import org.digijava.module.aim.helper.Location;
import org.digijava.module.aim.helper.OrgProjectId;
import org.digijava.module.aim.helper.PhysicalProgress;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.helper.UpdateDB;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.cms.dbentity.CMS;
import org.digijava.module.cms.dbentity.CMSContentItem;

/**
 * SaveActivity class creates a 'AmpActivity' object and populate the fields
 * with the values entered by the user and passes this object to the persister
 * class.
 * 
 * @author Priyajith
 */
public class SaveActivity extends Action {

	private static Logger logger = Logger.getLogger(SaveActivity.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		HttpSession session = request.getSession();

		// if user has not logged in, forward him to the home page
		if (session.getAttribute("currentMember") == null)
			return mapping.findForward("index");

		
		try {
		
		TeamMember tm = null;
		if (session.getAttribute("currentMember") != null)
			tm = (TeamMember) session.getAttribute("currentMember");

		/*
		 * if (session.getAttribute("ampAdmin") == null &&
		 * session.getAttribute("currentMember") == null) { return
		 * mapping.findForward("index"); } else { if
		 * (session.getAttribute("ampAdmin") != null) { String str = (String)
		 * session.getAttribute("ampAdmin"); if (!str.equals("yes")) { if (tm !=
		 * null) { if (tm.getTeamHead() == false) return
		 * mapping.findForward("index"); } else { return
		 * mapping.findForward("index"); } } } else if (tm != null) { if
		 * (tm.getTeamHead() == false) return mapping.findForward("index"); }
		 * else { return mapping.findForward("index"); } }
		 */

		EditActivityForm eaForm = (EditActivityForm) form;

		if (eaForm.getPageId() < 0 || eaForm.getPageId() > 1)
			return mapping.findForward("index");

		AmpActivity activity = new AmpActivity();

		if (eaForm.isEdit()) {
			if (eaForm.getActivityId() == null
					|| eaForm.getActivityId().intValue() < 1)
				return mapping.findForward("index");
		}

		/*
		 * Ronald
		 */
		ActionErrors errors = new ActionErrors();
		boolean titleFlag = false;
		boolean statusFlag = false;
		
		if (eaForm.getAmpId() == null) {
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
		
		if (eaForm.getTitle() != null) {
			if (eaForm.getTitle().trim().length() == 0) {
				errors.add("title", new ActionError("error.aim.addActivity.titleMissing"));
				saveErrors(request, errors);
				titleFlag = true;				
			} else if (eaForm.getTitle().length() > 255) {
				errors.add("title", new ActionError("error.aim.addActivity.titleTooLong"));
				saveErrors(request, errors);
				titleFlag = true;				
			}
		}
		
		if (eaForm.getStatus().equals(new Long(-1))) {
			errors.add("status", new ActionError("error.aim.addActivity.statusMissing"));
			saveErrors(request, errors);
			statusFlag = true;				
		}
		if (titleFlag == true || statusFlag == true) {
			return mapping.findForward("addActivityStep1");
		}		

		if (eaForm.getActivitySectors() == null || 
				eaForm.getActivitySectors().size() < 1) {
			errors.add("sector",new ActionError("error.aim.addActivity.sectorEmpty"));
			saveErrors(request, errors);
			return mapping.findForward("addActivityStep2");
		}
		
		if (eaForm.getFundingOrganizations() != null &&
		        eaForm.getFundingOrganizations().size() > 0) {
		    Iterator tempItr = eaForm.getFundingOrganizations().iterator();
		    while (tempItr.hasNext()) {
		        FundingOrganization forg = (FundingOrganization) tempItr.next();
		        if (forg.getFundings() == null || forg.getFundings().size() == 0) {
					errors.add("fundings",new ActionError("error.aim.addActivity.fundingsNotEntered"));
					saveErrors(request, errors);
					return mapping.findForward("addActivityStep3");
		        }
		    }
		}
		// end of Modified code

		activity.setAmpId(eaForm.getAmpId());
		activity.setName(eaForm.getTitle());
		if (eaForm.getDescription() == null
				|| eaForm.getDescription().trim().length() == 0) {
			activity.setDescription(new String(" "));
		} else {
			activity.setDescription(eaForm.getDescription());
		}
		if (eaForm.getObjectives() == null
				|| eaForm.getObjectives().trim().length() == 0) {
			activity.setObjective(new String(" "));
		} else {
			activity.setObjective(eaForm.getObjectives());
		}
		if (eaForm.getConditions() == null
				|| eaForm.getConditions().trim().length() == 0) {
			activity.setCondition(" ");
		} else {
			activity.setCondition(eaForm.getConditions());
		}
		
		activity.setProposedApprovalDate(
				DateConversion.getDate(eaForm.getOriginalAppDate()));
		activity.setActualApprovalDate(
				DateConversion.getDate(eaForm.getRevisedAppDate()));
		activity.setProposedStartDate(
				DateConversion.getDate(eaForm.getOriginalStartDate()));
		logger.debug("Setting actual start date as " + eaForm.getRevisedStartDate());
		activity.setActualStartDate(
				DateConversion.getDate(eaForm.getRevisedStartDate()));
		logger.debug("Actual start date = " + activity.getActualStartDate());
		activity.setActualCompletionDate(
				DateConversion.getDate(eaForm.getCurrentCompDate()));
		
		AmpActivityClosingDates closeDate = null;

		Set closeDates = new HashSet();
		if (activity.getClosingDates() == null) {
		    activity.setClosingDates(new HashSet());
		}
		
		if (!(eaForm.isEdit())) {
			closeDate = new AmpActivityClosingDates();
			closeDate.setAmpActivityId(activity);
			closeDate.setClosingDate(DateConversion.getDate(eaForm.getProposedCompDate()));
			closeDate.setComments(" ");
			closeDate.setType(Constants.REVISED);
			activity.getClosingDates().add(closeDate);			    
		}
		
		if (eaForm.getActivityCloseDates() != null) {
			Iterator itr = eaForm.getActivityCloseDates().iterator();
			while (itr.hasNext()) {
				String date = (String) itr.next();
				closeDate = new AmpActivityClosingDates();
				closeDate.setAmpActivityId(activity);
				closeDate.setClosingDate(DateConversion.getDate(date));
				closeDate.setType(Constants.REVISED);
				closeDate.setComments(" ");
				activity.getClosingDates().add(closeDate);
			}
		}
		
		
		if (eaForm.getCurrentCompDate() != null &&
		        eaForm.getCurrentCompDate().trim().length() > 0) {
			closeDate = new AmpActivityClosingDates();
			closeDate.setAmpActivityId(activity);
			closeDate.setClosingDate(DateConversion.getDate(eaForm.getCurrentCompDate()));
			closeDate.setComments(" ");
			closeDate.setType(Constants.CURRENT);
			
			Collection temp = activity.getClosingDates();
			if (!(temp.contains(closeDate))) {
			    activity.getClosingDates().add(closeDate);			    
			}
		}

		activity.setContFirstName(eaForm.getDnrCntFirstName());
		activity.setContLastName(eaForm.getDnrCntLastName());		
		activity.setEmail(eaForm.getDnrCntEmail());
		
		activity.setMofedCntFirstName(eaForm.getMfdCntFirstName());
		activity.setMofedCntLastName(eaForm.getMfdCntLastName());
		activity.setMofedCntEmail(eaForm.getMfdCntEmail());

		activity.setComments(" ");
		
		if (eaForm.getContractors() == null ||
				eaForm.getContractors().trim().length() == 0) {
			activity.setContractors(" ");
		} else {
			activity.setContractors(eaForm.getContractors());
		}

		// set status of an activity
		if (eaForm.getStatus() != null && eaForm.getStatus().intValue() != -1) {
			AmpStatus status = DbUtil.getAmpStatus(eaForm.getStatus());
			if (status != null) {
				activity.setStatus(status);
			}
		}
		// set status reason
		if (eaForm.getStatusReason() != null
				&& eaForm.getStatusReason().trim().length() != 0) {
			activity.setStatusReason(eaForm.getStatusReason().trim());
		} else {
			activity.setStatusReason(" ");
		}

		// set the sectors
		if (eaForm.getActivitySectors() != null) {
			Set sectors = new HashSet();
			if (eaForm.getActivitySectors() != null) {
				Iterator itr = eaForm.getActivitySectors().iterator();
				while (itr.hasNext()) {
					ActivitySector actSect = (ActivitySector) itr.next();
					Long sectorId = null;
					if (actSect.getSubsectorLevel2Id() != null
							&& (!actSect.getSubsectorLevel2Id().equals(
									new Long(-1)))) {
						sectorId = actSect.getSubsectorLevel2Id();
					} else if (actSect.getSubsectorLevel1Id() != null
							&& (!actSect.getSubsectorLevel1Id().equals(
									new Long(-1)))) {
						sectorId = actSect.getSubsectorLevel1Id();
					} else {
						sectorId = actSect.getSectorId();
					}

					if (sectorId != null && (!sectorId.equals(new Long(-1)))) {
						AmpSector sector = DbUtil.getAmpSector(sectorId);
						sectors.add(sector);
					}
				}
			}
			activity.setSectors(sectors);
		}
		
		if (eaForm.getProgram() != null && (!eaForm.getProgram().equals(new Long(-1)))) {
			AmpTheme theme = ProgramUtil.getTheme(eaForm.getProgram());
			if (theme != null) {
				activity.setThemeId(theme);
			}
		}
		if (eaForm.getProgramDescription() != null && 
				eaForm.getProgramDescription().trim().length() != 0) {
			activity.setProgramDescription(eaForm.getProgramDescription());
		} else {
			activity.setProgramDescription(" ");
		}

		// set organizations role
		Set orgRole = new HashSet();

		if (eaForm.getFundingOrganizations() != null) { // funding organizations
			AmpRole role = DbUtil.getAmpRole(Constants.DONOR);
			Iterator itr = eaForm.getFundingOrganizations().iterator();
			while (itr.hasNext()) {
				FundingOrganization fOrg = (FundingOrganization) itr.next();
				AmpOrganisation ampOrg = DbUtil.getOrganisation(fOrg
						.getAmpOrgId());
				AmpOrgRole ampOrgRole = new AmpOrgRole();
				ampOrgRole.setActivity(activity);
				ampOrgRole.setRole(role);
				ampOrgRole.setOrganisation(ampOrg);
				orgRole.add(ampOrgRole);
			}
		}
		if (eaForm.getExecutingAgencies() != null) { // executing agencies
			AmpRole role = DbUtil.getAmpRole(Constants.EXECUTING_AGENCY);
			Iterator itr = eaForm.getExecutingAgencies().iterator();
			while (itr.hasNext()) {
				AmpOrganisation org = (AmpOrganisation) itr.next();
				AmpOrgRole ampOrgRole = new AmpOrgRole();
				ampOrgRole.setActivity(activity);
				ampOrgRole.setRole(role);
				ampOrgRole.setOrganisation(org);
				orgRole.add(ampOrgRole);
			}
		}
		if (eaForm.getImpAgencies() != null) { // implementing agencies
			AmpRole role = DbUtil.getAmpRole(Constants.IMPLEMENTING_AGENCY);
			Iterator itr = eaForm.getImpAgencies().iterator();
			while (itr.hasNext()) {
				AmpOrganisation org = (AmpOrganisation) itr.next();
				AmpOrgRole ampOrgRole = new AmpOrgRole();
				ampOrgRole.setActivity(activity);
				ampOrgRole.setRole(role);
				ampOrgRole.setOrganisation(org);
				orgRole.add(ampOrgRole);
			}
		}
		/*
		if (eaForm.getContractors() != null) { // contractors
			AmpRole role = DbUtil.getAmpRole(Constants.CONTRACTOR);
			Iterator itr = eaForm.getContractors().iterator();
			while (itr.hasNext()) {
				AmpOrganisation org = (AmpOrganisation) itr.next();
				AmpOrgRole ampOrgRole = new AmpOrgRole();
				ampOrgRole.setRole(role);
				ampOrgRole.setOrganisation(org);
				orgRole.add(ampOrgRole);
			}
		}*/
		if (eaForm.getReportingOrgs() != null) { // Reporting Organization
			AmpRole role = DbUtil.getAmpRole(Constants.REPORTING_AGENCY);
			Iterator itr = eaForm.getReportingOrgs().iterator();
			while (itr.hasNext()) {
				AmpOrganisation org = (AmpOrganisation) itr.next();
				AmpOrgRole ampOrgRole = new AmpOrgRole();
				ampOrgRole.setActivity(activity);
				ampOrgRole.setRole(role);
				ampOrgRole.setOrganisation(org);
				orgRole.add(ampOrgRole);
			}
		}
		activity.setOrgrole(orgRole);

		// set locations
		if (eaForm.getSelectedLocs() != null) {
			Set locations = new HashSet();
			Iterator itr = eaForm.getSelectedLocs().iterator();
			while (itr.hasNext()) {
				Location loc = (Location) itr.next();
				AmpLocation ampLoc = DbUtil.getAmpLocation(loc.getCountryId(),
						loc.getRegionId(), loc.getZoneId(), loc.getWoredaId());

				if (ampLoc == null) {
					ampLoc = new AmpLocation();
					ampLoc.setCountry(loc.getCountry());
					ampLoc
							.setDgCountry(DbUtil.getDgCountry(loc
									.getCountryId()));
					ampLoc.setRegion(loc.getRegion());
					ampLoc.setAmpRegion(DbUtil.getAmpRegion(loc.getRegionId()));
					ampLoc.setAmpZone(DbUtil.getAmpZone(loc.getZoneId()));
					ampLoc.setAmpWoreda(DbUtil.getAmpWoreda(loc.getWoredaId()));
					ampLoc.setDescription(new String(" "));
					DbUtil.add(ampLoc);
				}
				locations.add(ampLoc);
			}
			activity.setLocations(locations);
		}

		// set level
		if (eaForm.getLevel() != null) {
			if (eaForm.getLevel().intValue() != -1) {
				AmpLevel level = DbUtil.getAmpLevel(eaForm.getLevel());
				activity.setLevel(level);
			}
		}

		// get cms and user details
		ModuleInstance moduleInstance = RequestUtils.getModuleInstance(request);
		String siteId;
		String instanceId;

		User user = RequestUtils.getUser(request);

		if (moduleInstance.getRealInstance() == null) {
			siteId = moduleInstance.getSite().getSiteId();
			instanceId = moduleInstance.getInstanceName();
		} else {
			siteId = moduleInstance.getRealInstance().getSite().getSiteId();
			instanceId = moduleInstance.getRealInstance().getInstanceName();
		}

		CMS cms = org.digijava.module.cms.util.DbUtil.getCMSItem(siteId,
				instanceId);

		// set documents & links
		Set docs = new HashSet();
		if (eaForm.getDocumentList() != null) {
			Iterator itr = eaForm.getDocumentList().iterator();
			while (itr.hasNext()) {
				CMSContentItem cmsDoc = (CMSContentItem) itr.next();
				CMSContentItem cmsItem = new CMSContentItem(cms);
				cmsItem.setContentType(cmsDoc.getContentType());
				if (cmsDoc.getDescription() == null
						|| cmsDoc.getDescription().trim().length() == 0) {
					cmsItem.setDescription(" ");
				} else {
					cmsItem.setDescription(cmsDoc.getDescription());
				}
				if (cmsDoc.getFile() == null) {
					byte file[] = new byte[1];
					file[0] = 0;
					cmsItem.setFile(file);
				} else {
					cmsItem.setFile(cmsDoc.getFile());
				}
				cmsItem.setIsFile(true);
				cmsItem.setFileName(cmsDoc.getFileName());
				cmsItem.setTitle(cmsDoc.getTitle());
				cmsItem.setAuthorUser(user);
				cmsItem.setPublished(true);
				docs.add(cmsItem);
			}
		}
		if (eaForm.getLinksList() != null) {
			Iterator itr = eaForm.getLinksList().iterator();
			if (itr.hasNext()) {
				CMSContentItem cmsDoc = (CMSContentItem) itr.next();
				CMSContentItem cmsItem = new CMSContentItem(cms);
				cmsItem.setContentType(cmsDoc.getContentType());
				if (cmsDoc.getDescription() == null
						|| cmsDoc.getDescription().trim().length() == 0) {
					cmsItem.setDescription(" ");
				} else {
					cmsItem.setDescription(cmsDoc.getDescription());
				}
				byte file[] = new byte[1];
				file[0] = 0;
				cmsItem.setFile(file);

				cmsItem.setIsFile(false);
				cmsItem.setTitle(cmsDoc.getTitle());
				cmsItem.setAuthorUser(user);
				cmsItem.setUrl(cmsDoc.getUrl());
				cmsItem.setPublished(true);
				docs.add(cmsItem);
			}
		}
		activity.setDocuments(docs);

		// if the activity is being added from a users workspace, associate the
		// activity with the team of the current member.
		if (eaForm.getPageId() == 1 && tm != null) {
			AmpTeam team = TeamUtil.getAmpTeam(tm.getTeamId());
			activity.setTeam(team);
		}

		// set activity internal ids
		Set internalIds = new HashSet();
		if (eaForm.getSelectedOrganizations() != null) {
			OrgProjectId orgProjId[] = eaForm.getSelectedOrganizations();
			for (int i = 0; i < orgProjId.length; i++) {
				AmpActivityInternalId actInternalId = new AmpActivityInternalId();
				logger.info("Setting orgId in Internal id as " + orgProjId[i].getAmpOrgId());
				actInternalId.setOrganisation(DbUtil.getOrganisation(orgProjId[i].getAmpOrgId()));
				actInternalId.setInternalId(orgProjId[i].getProjectId());
				internalIds.add(actInternalId);	
			}
		}
		activity.setInternalIds(internalIds);

		// set components
		Set components = new HashSet();
		logger.info("Setting components from Save Activity");
		if (eaForm.getSelectedComponents() != null) {
			Iterator itr = eaForm.getSelectedComponents().iterator();
			logger.info("Got the iterator");
			while (itr.hasNext()) {
				logger.info("Inside while");
				Components comp = (Components) itr.next();
				AmpComponent ampComponent = new AmpComponent();
				ampComponent.setActivity(activity);
				ampComponent.setAmount(new Double(DecimalToText.getDouble(comp
						.getAmount())));
				ampComponent.setCurrency(DbUtil.getCurrencyByCode(comp
						.getCurrencyCode()));
				if (comp.getDescription() == null
						|| comp.getDescription().trim().length() == 0) {
					ampComponent.setDescription(" ");
				} else {
					ampComponent.setDescription(comp.getDescription());
				}
				ampComponent.setReportingDate(DateConversion.getDate(comp
						.getReportingDate()));
				ampComponent.setTitle(comp.getTitle());

				// set physical progress
				Set phyProgess = new HashSet();
				if (comp.getPhyProgress() != null) {

					Iterator itr1 = comp.getPhyProgress().iterator();
					while (itr1.hasNext()) {
						PhysicalProgress phyProg = (PhysicalProgress) itr1
								.next();
						AmpPhysicalPerformance ampPhyPerf = new AmpPhysicalPerformance();
						if (phyProg.getDescription() == null
								|| phyProg.getDescription().trim().length() == 0) {
							ampPhyPerf.setDescription(new String(" "));
						} else {
							ampPhyPerf.setDescription(phyProg.getDescription());
						}
						ampPhyPerf.setReportingDate(DateConversion
								.getDate(phyProg.getReportingDate()));
						ampPhyPerf.setTitle(phyProg.getTitle());
						ampPhyPerf.setAmpActivityId(activity);
						ampPhyPerf.setComponent(ampComponent);
						ampPhyPerf.setComments(" ");
						phyProgess.add(ampPhyPerf);
					}
				}
				ampComponent.setPhysicalProgress(phyProgess);
				components.add(ampComponent);
			}
		}
		activity.setComponents(components);

		// set funding and funding details
		Set fundings = new HashSet();
		if (eaForm.getFundingOrganizations() != null) {
			Iterator itr1 = eaForm.getFundingOrganizations().iterator();
			while (itr1.hasNext()) {
				FundingOrganization fOrg = (FundingOrganization) itr1.next();

				// add fundings
				if (fOrg.getFundings() != null) {
					Iterator itr2 = fOrg.getFundings().iterator();
					while (itr2.hasNext()) {
						Funding fund = (Funding) itr2.next();
						AmpFunding ampFunding = new AmpFunding();
						ampFunding.setAmpDonorOrgId(DbUtil.getOrganisation(fOrg
								.getAmpOrgId()));
						ampFunding.setFinancingId(fund.getOrgFundingId());
						/*
						if (fund.getSignatureDate() != null)
							ampFunding.setSignatureDate(DateConversion
									.getDate(fund.getSignatureDate()));
						*/
						ampFunding.setModalityId(fund.getModality());
						if (fund.getConditions() != null && fund.getConditions().trim().length() != 0) {
							ampFunding.setConditions(fund.getConditions());
						} else {
							ampFunding.setConditions(new String(" "));
						}
						ampFunding.setComments(new String(" "));
						ampFunding.setAmpTermsAssistId(fund.getAmpTermsAssist());
						ampFunding.setAmpActivityId(activity);

						// add funding details for each funding
						Set fundDeatils = new HashSet();
						if (fund.getFundingDetails() != null) {
							Iterator itr3 = fund.getFundingDetails().iterator();
							while (itr3.hasNext()) {
								FundingDetail fundDet = (FundingDetail) itr3
										.next();
								AmpFundingDetail ampFundDet = new AmpFundingDetail();
								ampFundDet.setTransactionType(new Integer(
										fundDet.getTransactionType()));
								ampFundDet.setAdjustmentType(new Integer(
										fundDet.getAdjustmentType()));
								ampFundDet.setTransactionDate(DateConversion
										.getDate(fundDet.getTransactionDate()));
								ampFundDet.setOrgRoleCode(fundDet
										.getPerspectiveCode());

								Double transAmt = new Double(DecimalToText
										.getDouble(fundDet
												.getTransactionAmount()));
								ampFundDet.setTransactionAmount(transAmt);
								ampFundDet.setAmpCurrencyId(DbUtil
										.getCurrencyByCode(fundDet
												.getCurrencyCode()));
								if (fundDet.getTransactionType() == Constants.EXPENDITURE) {
									ampFundDet.setExpCategory(fundDet.getClassification());
								}
								ampFundDet.setAmpFundingId(ampFunding);
								fundDeatils.add(ampFundDet);
							}
						}
						ampFunding.setFundingDetails(fundDeatils);
						fundings.add(ampFunding);
					}
				}
			}
		}
		activity.setFunding(fundings);
				
		Long field = null;
		if (eaForm.getField() != null)
			field = eaForm.getField().getAmpFieldId();
		if (eaForm.isEdit()) {
			// update an existing activity
			ActivityUtil.saveActivity(activity, eaForm.getActivityId(), true, eaForm.getCommentsCol(), eaForm.isSerializeFlag(), field);
		} else {
			AmpTeamMember teamMember = DbUtil.getAmpTeamMember(tm.getMemberId()) ;
			activity.setActivityCreator(teamMember);
			Calendar cal = Calendar.getInstance();
			activity.setCreatedDate(cal.getTime());			
			// create a new activity
			ActivityUtil.saveActivity(activity, eaForm.getCommentsCol(), eaForm.isSerializeFlag(), field);
			//ObjectPersister.removeObject(getRealPath(tm.getMemberId()));
		}

		eaForm.setStep("1");
		eaForm.setReset(true);
		eaForm.setDocReset(true);
		eaForm.setLocationReset(true);
		eaForm.setOrgPopupReset(true);
		eaForm.setOrgSelReset(true);
		eaForm.setComponentReset(true);
		eaForm.setSectorReset(true);
		eaForm.setPhyProgReset(true);
		// Clearing comment properties
		eaForm.getCommentsCol().clear();
		eaForm.setCommentFlag(false);

		int temp = eaForm.getPageId();
		eaForm.setPageId(-1);
		UpdateDB.updateReportCache(activity.getAmpActivityId());
		eaForm.reset(mapping, request);

		if (session.getAttribute("ampProjects") != null) {
			session.removeAttribute("ampProjects");
		}
	
		if (temp == 0)
			return mapping.findForward("adminHome");
		else if (temp == 1)
			return mapping.findForward("viewMyDesktop");
		else
			return null;
		} catch (Exception e) {
		    e.printStackTrace(System.out);
		}
		return null;
	}
	
	/*
	private String getRealPath(Long val) {
		String fileName =  val.toString()+ ".dat";
		ActionServlet s = getServlet();
		String path = "/WEB-INF/tmp";
		String realPath = s.getServletContext().getRealPath(path);
		File dir = new File(realPath);
		if(dir.mkdir() == true) 
		{
			System.out.println("--- Dir Make ----------");
		}
		else
		{
			System.out.println("---No Dir created ----------");
		}

		fileName = realPath + "\\" + fileName; 
		return fileName;
	}*/		
}
