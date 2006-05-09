/*
 * SaveActivity.java
 */

package org.digijava.module.aim.action;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityClosingDates;
import org.digijava.module.aim.dbentity.AmpActivityInternalId;
import org.digijava.module.aim.dbentity.AmpActor;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpComponentFunding;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpIssues;
import org.digijava.module.aim.dbentity.AmpLevel;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpMeasure;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpPerspective;
import org.digijava.module.aim.dbentity.AmpPhysicalPerformance;
import org.digijava.module.aim.dbentity.AmpRegion;
import org.digijava.module.aim.dbentity.AmpRegionalFunding;
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
import org.digijava.module.aim.helper.Issues;
import org.digijava.module.aim.helper.Location;
import org.digijava.module.aim.helper.Measures;
import org.digijava.module.aim.helper.OrgProjectId;
import org.digijava.module.aim.helper.PhysicalProgress;
import org.digijava.module.aim.helper.RegionalFunding;
import org.digijava.module.aim.helper.RelatedLinks;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.helper.UpdateDB;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.aim.util.ProgramUtil;

import org.digijava.module.aim.helper.ActivityIndicator;
import org.digijava.module.aim.dbentity.AmpMEIndicatorValue;
import org.digijava.module.aim.dbentity.AmpMECurrValHistory;
import org.digijava.module.aim.util.MEIndicatorsUtil;


/**
 * SaveActivity class creates a 'AmpActivity' object and populate the fields
 * with the values entered by the user and passes this object to the persister
 * class.
 * 
 * @author Priyajith
 */
public class SaveActivity extends Action {

	private static Logger logger = Logger.getLogger(SaveActivity.class);
	
	private ServletContext ampContext = null;

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		HttpSession session = request.getSession();
		ampContext = getServlet().getServletContext();
		
		// if user has not logged in, forward him to the home page
		if (session.getAttribute("currentMember") == null) {
			return mapping.findForward("index");
		}

		
		try {
		
		TeamMember tm = null;
		if (session.getAttribute("currentMember") != null)
			tm = (TeamMember) session.getAttribute("currentMember");

		EditActivityForm eaForm = (EditActivityForm) form;
		AmpActivity activity = new AmpActivity();

		
		if (eaForm.getPageId() < 0 || eaForm.getPageId() > 1) {
			return mapping.findForward("index");
		}

		if (eaForm.isDonorFlag()) {
			// Donor edit
		
			// set funding and funding details
			Set fundings = new HashSet();
			if (eaForm.getFundingOrganizations() != null) {
				Iterator itr1 = eaForm.getFundingOrganizations().iterator();
				while (itr1.hasNext()) {
					FundingOrganization fOrg = (FundingOrganization) itr1.next();
					//logger.info("fOrg.getAmpOrgId().longValue() :" + fOrg.getAmpOrgId().longValue());
					//logger.info("eaForm.getFundDonor().longValue() :" + eaForm.getFundDonor().longValue());
					if (fOrg.getAmpOrgId().longValue() == eaForm.getFundDonor().longValue()) {
						// add fundings
						if (fOrg.getFundings() != null) {
							//logger.info("fOrg.getFundings() :" + fOrg.getFundings());
							Iterator itr2 = fOrg.getFundings().iterator();
							while (itr2.hasNext()) {
								Funding fund = (Funding) itr2.next();
								AmpFunding ampFunding = new AmpFunding();
								ampFunding.setAmpFundingId(new Long(fund.getFundingId()));

								// add funding details for each funding
								Set fundDeatils = new HashSet();
								if (fund.getFundingDetails() != null) {
									Iterator itr3 = fund.getFundingDetails().iterator();
									while (itr3.hasNext()) {
										FundingDetail fundDet = (FundingDetail) itr3
												.next();
										//logger.info("fundDet.getPerspectiveCode() :" + fundDet.getPerspectiveCode());
										AmpFundingDetail ampFundDet = new AmpFundingDetail();
										ampFundDet.setTransactionType(new Integer(
												fundDet.getTransactionType()));
										ampFundDet.setPerspectiveId(DbUtil.getPerspective(fundDet.getPerspectiveCode()));
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
								//logger.info("Adding " + ampFunding.getAmpFundingId());
								fundings.add(ampFunding);
							}
						}						
					}
				}
			}
			ActivityUtil.saveDonorFundingInfo(eaForm.getActivityId(),fundings);
		} else {
			// MOFED add/edit
			
			String toDelete = request.getParameter("delete");

			if (toDelete == null || (!toDelete.trim().equalsIgnoreCase("true"))) {
				if (eaForm.isEditAct() == false) {
					AmpActivity act = ActivityUtil.getActivityByName(eaForm.getTitle());
					if (act != null) {
						eaForm.setActivityId(act.getAmpActivityId());
						logger.debug("Activity with the name " + eaForm.getTitle() + " already exist.");
						return mapping.findForward("activityExist");
					}
				}
			} else if (toDelete.trim().equals("true")){
				eaForm.setEditAct(true);
			} else {
				logger.debug("No duplicate found");
			}

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
			
			// validate donor fundings
			/*
			
			Collection donorComm = new ArrayList();
			Collection donorDisb = new ArrayList();
			Collection donorExpn = new ArrayList();
			
			if (eaForm.getFundingOrganizations() != null && 
					eaForm.getFundingOrganizations().size() > 0) {
				Iterator itr1 = eaForm.getFundingOrganizations().iterator();
				while (itr1.hasNext()) {
					FundingOrganization forg = (FundingOrganization) itr1.next();
					if (forg.getFundings() != null &&
							forg.getFundings().size() > 0) {
						Iterator itr2 = forg.getFundings().iterator();
						while (itr2.hasNext()) {
							Funding funding = (Funding) itr2.next();
							if (funding.getFundingDetails() != null &&
									funding.getFundingDetails().size() > 0) {
								Iterator itr3 = funding.getFundingDetails().iterator();
								Collection comm = new ArrayList();
								Collection disb = new ArrayList();
								Collection expn = new ArrayList();
								while (itr3.hasNext()) {
									FundingDetail fd = (FundingDetail) itr3.next();
									switch (fd.getTransactionType()) {
									case 0:
										comm.add(fd);
										donorComm.add(fd);
										break;
									case 1:
										disb.add(fd);
										donorDisb.add(fd);
										break;
									case 2:
										expn.add(fd);
										donorExpn.add(fd);
										break;									
									}
								}
								int errIndex = FundingValidator.validateFundings(comm,disb);
								if (errIndex > 0) {
									// invalid entry
									errors.add("fundings",new ActionError("error.aim.addActivity.disbGreaterThanComm"));
									saveErrors(request, errors);
									return mapping.findForward("addActivityStep3");								
								} else {
									errIndex = FundingValidator.validateFundings(disb,expn);
									if (errIndex > 0) {
										// invalid entry
										errors.add("fundings",new ActionError("error.aim.addActivity.expnGreaterThanDisb"));
										saveErrors(request, errors);
										return mapping.findForward("addActivityStep3");
									}
								}
							}
						}
					}
				}
			}
			
			Collection regComm = new ArrayList();
			Collection regDisb = new ArrayList();
			Collection regExpn = new ArrayList();
			
			// validate regional fundings
			if (eaForm.getRegionalFundings() != null && 
					eaForm.getRegionalFundings().size() > 0) {
				Iterator itr1 = eaForm.getRegionalFundings().iterator();
				while (itr1.hasNext()) {
					RegionalFunding rf = (RegionalFunding) itr1.next();
					if (rf.getCommitments() != null)
						regComm.addAll(rf.getCommitments());
					if (rf.getDisbursements() != null)
						regDisb.addAll(rf.getDisbursements());
					if (rf.getExpenditures() != null)
						regExpn.addAll(rf.getExpenditures());
					
					int errIndex = FundingValidator.validateFundings(rf.getCommitments(),rf.getDisbursements());
					if (errIndex > 0) {
						// invalid entry
						errors.add("fundings",new ActionError("error.aim.addActivity.disbGreaterThanComm"));
						saveErrors(request, errors);
						return mapping.findForward("addActivityStep4");								
					} else {
						errIndex = FundingValidator.validateFundings(rf.getDisbursements(),rf.getExpenditures());
						if (errIndex > 0) {
							// invalid entry
							errors.add("fundings",new ActionError("error.aim.addActivity.expnGreaterThanDisb"));
							saveErrors(request, errors);
							return mapping.findForward("addActivityStep4");
						}
					}				
				}
			}		
			
			Collection compComm = new ArrayList();
			Collection compDisb = new ArrayList();
			Collection compExpn = new ArrayList();		
			// validate component fundings
			if (eaForm.getSelectedComponents() != null && 
					eaForm.getSelectedComponents().size() > 0) {
				Iterator itr1 = eaForm.getSelectedComponents().iterator();
				while (itr1.hasNext()) {
					Components comp = (Components) itr1.next();
					if (comp.getCommitments() != null)
						compComm.addAll(comp.getCommitments());
					if (comp.getDisbursements() != null)
						compDisb.addAll(comp.getDisbursements());
					if (comp.getExpenditures() != null)
						compExpn.addAll(comp.getExpenditures());

					int errIndex = FundingValidator.validateFundings(comp.getCommitments(),comp.getDisbursements());
					if (errIndex > 0) {
						// invalid entry
						errors.add("fundings",new ActionError("error.aim.addActivity.disbGreaterThanComm"));
						saveErrors(request, errors);
						return mapping.findForward("addActivityStep5");								
					} else {
						errIndex = FundingValidator.validateFundings(comp.getDisbursements(),comp.getExpenditures());
						if (errIndex > 0) {
							// invalid entry
							errors.add("fundings",new ActionError("error.aim.addActivity.expnGreaterThanDisb"));
							saveErrors(request, errors);
							return mapping.findForward("addActivityStep5");
						}
					}				
				}
			}		
			
			// validate donor commitments againts regional commitments
			int err = FundingValidator.validateFundings(donorComm,regComm);
			if (err > 0) {
				// regional commitments greater than actual donor commitments
				errors.add("fundings",new ActionError("error.aim.addActivity.regCommGreaterThanDonorComm"));
				saveErrors(request, errors);
				return mapping.findForward("addActivityStep4");
			}		
			
			// validate donor disbursements againts regional disbursements
			err = FundingValidator.validateFundings(donorDisb,regDisb);
			if (err > 0) {
				// regional disbursements greater than actual donor disbursements
				errors.add("fundings",new ActionError("error.aim.addActivity.regDisbGreaterThanDonorDisb"));
				saveErrors(request, errors);
				return mapping.findForward("addActivityStep4");
			}		
			
			// validate donor expenditures againts regional expenditures
			err = FundingValidator.validateFundings(donorExpn,regExpn);
			if (err > 0) {
				// regional expenditures greater than actual donor expenditures
				errors.add("fundings",new ActionError("error.aim.addActivity.regExpnGreaterThanDonorExpn"));
				saveErrors(request, errors);
				return mapping.findForward("addActivityStep4");
			}		
			
			// validate donor commitments againts component commitments
			err = FundingValidator.validateFundings(donorComm,compComm);
			if (err > 0) {
				// component commitments greater than actual donor commitments
				errors.add("fundings",new ActionError("error.aim.addActivity.compCommGreaterThanDonorComm"));
				saveErrors(request, errors);
				return mapping.findForward("addActivityStep5");
			}		
			
			// validate donor disbursements againts component disbursements
			err = FundingValidator.validateFundings(donorDisb,compDisb);
			if (err > 0) {
				// component disbursements greater than actual donor disbursements
				errors.add("fundings",new ActionError("error.aim.addActivity.compDisbGreaterThanDonorDisb"));
				saveErrors(request, errors);
				return mapping.findForward("addActivityStep5");
			}		
			
			// validate donor expenditures againts component expenditures
			err = FundingValidator.validateFundings(donorExpn,compExpn);
			if (err > 0) {
				// component expenditures greater than actual donor expenditures
				errors.add("fundings",new ActionError("error.aim.addActivity.compExpnGreaterThanDonorExpn"));
				saveErrors(request, errors);
				return mapping.findForward("addActivityStep5");
			}				
			*/

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
			activity.setActualStartDate(
					DateConversion.getDate(eaForm.getRevisedStartDate()));
			activity.setActualCompletionDate(
					DateConversion.getDate(eaForm.getCurrentCompDate()));
			
			AmpActivityClosingDates closeDate = null;

			if (activity.getClosingDates() == null) {
			    activity.setClosingDates(new HashSet());
			}
			
			if (!(eaForm.isEditAct())) {
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
						ampLoc.setDgCountry(DbUtil.getDgCountry(loc
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

			Collection relatedLinks = new ArrayList();
			if (eaForm.getDocumentList() != null) {
				Iterator itr = eaForm.getDocumentList().iterator();
				while (itr.hasNext()) {
					RelatedLinks rl = (RelatedLinks) itr.next();
					relatedLinks.add(rl);
				}
			}
			if (eaForm.getLinksList() != null) {
				Iterator itr = eaForm.getLinksList().iterator();
				while (itr.hasNext()) {
					RelatedLinks rl = (RelatedLinks) itr.next();
					relatedLinks.add(rl);
				}
			}

			// if the activity is being added from a users workspace, associate the
			// activity with the team of the current member.
			if (tm != null && (eaForm.isEditAct() == false)) {
				AmpTeam team = TeamUtil.getAmpTeam(tm.getTeamId());
				activity.setTeam(team);
			} else {
				activity.setTeam(null);
			}

			// set activity internal ids
			Set internalIds = new HashSet();
			if (eaForm.getSelectedOrganizations() != null) {
				OrgProjectId orgProjId[] = eaForm.getSelectedOrganizations();
				for (int i = 0; i < orgProjId.length; i++) {
					AmpActivityInternalId actInternalId = new AmpActivityInternalId();
					actInternalId.setOrganisation(DbUtil.getOrganisation(orgProjId[i].getAmpOrgId()));
					actInternalId.setInternalId(orgProjId[i].getProjectId());
					internalIds.add(actInternalId);	
				}
			}
			activity.setInternalIds(internalIds);

			// set components
			activity.setComponents(new HashSet());
			if (eaForm.getSelectedComponents() != null) {
				Iterator itr = eaForm.getSelectedComponents().iterator();
				while (itr.hasNext()) {
					Components comp = (Components) itr.next();
					AmpComponent ampComponent = new AmpComponent();
					ampComponent.setActivity(activity);
					if (comp.getDescription() == null
							|| comp.getDescription().trim().length() == 0) {
						ampComponent.setDescription(" ");
					} else {
						ampComponent.setDescription(comp.getDescription());
					}
					ampComponent.setTitle(comp.getTitle());
					ampComponent.setComponentFundings(new HashSet());

					if (comp.getCommitments() != null && comp.getCommitments().size() > 0) {
						Iterator itr2 = comp.getCommitments().iterator();
						while (itr2.hasNext()) {
							AmpComponentFunding ampCompFund = new AmpComponentFunding();
							ampCompFund.setActivity(activity);					
							ampCompFund.setTransactionType(new Integer(Constants.COMMITMENT));						
							FundingDetail fd = (FundingDetail) itr2.next();
							Iterator tmpItr = eaForm.getCurrencies().iterator();
							while (tmpItr.hasNext()) {
								AmpCurrency curr = (AmpCurrency) tmpItr.next();
								if (curr.getCurrencyCode().equals(fd.getCurrencyCode())) {
									ampCompFund.setCurrency(curr);
									break;
								}
							}
							tmpItr = eaForm.getPerspectives().iterator();
							while (tmpItr.hasNext()) {
								AmpPerspective pers = (AmpPerspective) tmpItr.next();
								if (pers.getCode().equals(fd.getPerspectiveCode())) {
									ampCompFund.setPerspective(pers);
									break;
								}
							}
							ampCompFund.setTransactionAmount(new Double(DecimalToText.getDouble(fd.getTransactionAmount())));
							ampCompFund.setTransactionDate(DateConversion.getDate(fd.getTransactionDate()));
							ampCompFund.setAdjustmentType(new Integer(fd.getAdjustmentType()));
							ampCompFund.setComponent(ampComponent);
							ampComponent.getComponentFundings().add(ampCompFund);
						}
					}
					
					if (comp.getDisbursements() != null && comp.getDisbursements().size() > 0) {
						Iterator itr2 = comp.getDisbursements().iterator();
						while (itr2.hasNext()) {
							AmpComponentFunding ampCompFund = new AmpComponentFunding();
							ampCompFund.setActivity(activity);					
							ampCompFund.setTransactionType(new Integer(Constants.DISBURSEMENT));						
							FundingDetail fd = (FundingDetail) itr2.next();
							Iterator tmpItr = eaForm.getCurrencies().iterator();
							while (tmpItr.hasNext()) {
								AmpCurrency curr = (AmpCurrency) tmpItr.next();
								if (curr.getCurrencyCode().equals(fd.getCurrencyCode())) {
									ampCompFund.setCurrency(curr);
									break;
								}
							}
							tmpItr = eaForm.getPerspectives().iterator();
							while (tmpItr.hasNext()) {
								AmpPerspective pers = (AmpPerspective) tmpItr.next();
								if (pers.getCode().equals(fd.getPerspectiveCode())) {
									ampCompFund.setPerspective(pers);
									break;
								}
							}
							ampCompFund.setTransactionAmount(new Double(DecimalToText.getDouble(fd.getTransactionAmount())));
							ampCompFund.setTransactionDate(DateConversion.getDate(fd.getTransactionDate()));
							ampCompFund.setAdjustmentType(new Integer(fd.getAdjustmentType()));
							ampCompFund.setComponent(ampComponent);
							ampComponent.getComponentFundings().add(ampCompFund);
						}
					}				
					
					if (comp.getExpenditures() != null && comp.getExpenditures().size() > 0) {
						Iterator itr2 = comp.getExpenditures().iterator();
						while (itr2.hasNext()) {
							AmpComponentFunding ampCompFund = new AmpComponentFunding();
							ampCompFund.setActivity(activity);					
							ampCompFund.setTransactionType(new Integer(Constants.EXPENDITURE));						
							FundingDetail fd = (FundingDetail) itr2.next();
							Iterator tmpItr = eaForm.getCurrencies().iterator();
							while (tmpItr.hasNext()) {
								AmpCurrency curr = (AmpCurrency) tmpItr.next();
								if (curr.getCurrencyCode().equals(fd.getCurrencyCode())) {
									ampCompFund.setCurrency(curr);
									break;
								}
							}
							tmpItr = eaForm.getPerspectives().iterator();
							while (tmpItr.hasNext()) {
								AmpPerspective pers = (AmpPerspective) tmpItr.next();
								if (pers.getCode().equals(fd.getPerspectiveCode())) {
									ampCompFund.setPerspective(pers);
									break;
								}
							}
							ampCompFund.setTransactionAmount(new Double(DecimalToText.getDouble(fd.getTransactionAmount())));
							ampCompFund.setTransactionDate(DateConversion.getDate(fd.getTransactionDate()));
							ampCompFund.setAdjustmentType(new Integer(fd.getAdjustmentType()));
							ampCompFund.setComponent(ampComponent);
							ampComponent.getComponentFundings().add(ampCompFund);
						}
					}

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
					activity.getComponents().add(ampComponent);
				}
			}

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
									//ampFundDet.setPerspectiveId(DbUtil.getPerspective(Constants.MOFED));
									ampFundDet.setPerspectiveId(DbUtil.getPerspective(fundDet.getPerspectiveCode()));
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
			
			// set Regional fundings
			Set regFundings = new HashSet();
			if (eaForm.getRegionalFundings() != null &&
					eaForm.getRegionalFundings().size() > 0) {
				Iterator itr1 = eaForm.getRegionalFundings().iterator();
				while (itr1.hasNext()) {
					RegionalFunding regFund = (RegionalFunding) itr1.next();
					if (regFund.getCommitments() != null &&
							regFund.getCommitments().size() > 0) {
						Iterator itr2 = regFund.getCommitments().iterator();
						while (itr2.hasNext()) {
							AmpRegionalFunding ampRegFund = new AmpRegionalFunding();
							ampRegFund.setActivity(activity);					
							ampRegFund.setTransactionType(new Integer(Constants.COMMITMENT));						
							FundingDetail fd = (FundingDetail) itr2.next();
							Iterator tmpItr = eaForm.getCurrencies().iterator();
							while (tmpItr.hasNext()) {
								AmpCurrency curr = (AmpCurrency) tmpItr.next();
								if (curr.getCurrencyCode().equals(fd.getCurrencyCode())) {
									ampRegFund.setCurrency(curr);
									break;
								}
							}
							tmpItr = eaForm.getPerspectives().iterator();
							while (tmpItr.hasNext()) {
								AmpPerspective pers = (AmpPerspective) tmpItr.next();
								if (pers.getCode().equals(fd.getPerspectiveCode())) {
									ampRegFund.setPerspective(pers);
									break;
								}
							}
							tmpItr = eaForm.getFundingRegions().iterator();
							while (tmpItr.hasNext()) {
								AmpRegion reg = (AmpRegion) tmpItr.next();
								if (reg.getAmpRegionId().equals(regFund.getRegionId())) {
									ampRegFund.setRegion(reg);
									break;
								}
							}
							ampRegFund.setTransactionAmount(new Double(
									DecimalToText.getDouble(fd.getTransactionAmount())));
							ampRegFund.setTransactionDate(DateConversion.getDate(fd.getTransactionDate()));
							ampRegFund.setAdjustmentType(new Integer(fd.getAdjustmentType()));
							regFundings.add(ampRegFund);						
						}
					}
					
					if (regFund.getDisbursements() != null &&
							regFund.getDisbursements().size() > 0) {
						Iterator itr2 = regFund.getDisbursements().iterator();
						while (itr2.hasNext()) {
							AmpRegionalFunding ampRegFund = new AmpRegionalFunding();
							ampRegFund.setActivity(activity);					
							ampRegFund.setTransactionType(new Integer(Constants.DISBURSEMENT));						
							FundingDetail fd = (FundingDetail) itr2.next();
							Iterator tmpItr = eaForm.getCurrencies().iterator();
							while (tmpItr.hasNext()) {
								AmpCurrency curr = (AmpCurrency) tmpItr.next();
								if (curr.getCurrencyCode().equals(fd.getCurrencyCode())) {
									ampRegFund.setCurrency(curr);
									break;
								}
							}
							tmpItr = eaForm.getPerspectives().iterator();
							while (tmpItr.hasNext()) {
								AmpPerspective pers = (AmpPerspective) tmpItr.next();
								if (pers.getCode().equals(fd.getPerspectiveCode())) {
									ampRegFund.setPerspective(pers);
									break;
								}
							}
							tmpItr = eaForm.getFundingRegions().iterator();
							while (tmpItr.hasNext()) {
								AmpRegion reg = (AmpRegion) tmpItr.next();
								if (reg.getAmpRegionId().equals(regFund.getRegionId())) {
									ampRegFund.setRegion(reg);
									break;
								}
							}
							ampRegFund.setTransactionAmount(new Double(
									DecimalToText.getDouble(fd.getTransactionAmount())));
							ampRegFund.setTransactionDate(DateConversion.getDate(fd.getTransactionDate()));
							ampRegFund.setAdjustmentType(new Integer(fd.getAdjustmentType()));
							regFundings.add(ampRegFund);						
						}
					}				
					
					if (regFund.getExpenditures() != null &&
							regFund.getExpenditures().size() > 0) {
				
						Iterator itr2 = regFund.getExpenditures().iterator();
						while (itr2.hasNext()) {
							AmpRegionalFunding ampRegFund = new AmpRegionalFunding();
							ampRegFund.setActivity(activity);					
							ampRegFund.setTransactionType(new Integer(Constants.EXPENDITURE));						
							FundingDetail fd = (FundingDetail) itr2.next();
							Iterator tmpItr = eaForm.getCurrencies().iterator();
							while (tmpItr.hasNext()) {
								AmpCurrency curr = (AmpCurrency) tmpItr.next();
								if (curr.getCurrencyCode().equals(fd.getCurrencyCode())) {
									ampRegFund.setCurrency(curr);
									break;
								}
							}
							tmpItr = eaForm.getPerspectives().iterator();
							while (tmpItr.hasNext()) {
								AmpPerspective pers = (AmpPerspective) tmpItr.next();
								if (pers.getCode().equals(fd.getPerspectiveCode())) {
									ampRegFund.setPerspective(pers);
									break;
								}
							}
							tmpItr = eaForm.getFundingRegions().iterator();
							while (tmpItr.hasNext()) {
								AmpRegion reg = (AmpRegion) tmpItr.next();
								if (reg.getAmpRegionId().equals(regFund.getRegionId())) {
									ampRegFund.setRegion(reg);
									break;
								}
							}
							ampRegFund.setTransactionAmount(new Double(
									DecimalToText.getDouble(fd.getTransactionAmount())));
							ampRegFund.setTransactionDate(DateConversion.getDate(fd.getTransactionDate()));
							ampRegFund.setAdjustmentType(new Integer(fd.getAdjustmentType()));
							regFundings.add(ampRegFund);						
						}

					}				
				}
			}
			
			// Delete the following code
			Iterator tmp = regFundings.iterator();
			while (tmp.hasNext()) {
				AmpRegionalFunding rf = (AmpRegionalFunding) tmp.next();
				logger.debug("Regional Fundings :" + rf.getAdjustmentType() + " " + rf.getTransactionAmount());
			}
			
			activity.setRegionalFundings(regFundings);
			
			if (eaForm.getIssues() != null &&
					eaForm.getIssues().size() > 0) {
				Set issueSet = new HashSet();
				for (int i = 0;i < eaForm.getIssues().size();i++) {
					Issues issue = (Issues) eaForm.getIssues().get(i);
					AmpIssues ampIssue = new AmpIssues();
					ampIssue.setActivity(activity);
					ampIssue.setName(issue.getName());
					Set measureSet = new HashSet();
					if (issue.getMeasures() != null &&
							issue.getMeasures().size() > 0) {
						for (int j = 0;j < issue.getMeasures().size();j++) {
							Measures measure = (Measures) issue.getMeasures().get(j);
							AmpMeasure ampMeasure = new AmpMeasure();
							ampMeasure.setIssue(ampIssue);
							ampMeasure.setName(measure.getName());
							Set actorSet = new HashSet();
							if (measure.getActors() != null &&
									measure.getActors().size() > 0) {
								for (int k = 0;k < measure.getActors().size();k++) {
									AmpActor actor = (AmpActor) measure.getActors().get(k);
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
					
		Long field = null;
		if (eaForm.getField() != null)
			field = eaForm.getField().getAmpFieldId();
		
		if (eaForm.isEditAct()) {
			// Setting approval status of activity
			activity.setApprovalStatus(eaForm.getApprovalStatus());
			// update an existing activity
			ActivityUtil.saveActivity(activity, eaForm.getActivityId(), true, 
					eaForm.getCommentsCol(), eaForm.isSerializeFlag(), field,
					relatedLinks,tm.getMemberId());

			AmpMEIndicatorValue meIndVal = new AmpMEIndicatorValue();
			AmpMECurrValHistory meCurrVal = new AmpMECurrValHistory();
			
			meCurrVal.setCurrValue(eaForm.getCurrentVal());
			meCurrVal.setCurrValueDate(DateConversion.getDate(eaForm.getCurrentValDate()));
			meIndVal.setAmpMeIndValId(eaForm.getIndicatorValId());
			meCurrVal.setMeIndValue(meIndVal);
			DbUtil.add(meCurrVal);

			ActivityIndicator actInd = new ActivityIndicator();
			actInd.setIndicatorValId(eaForm.getIndicatorValId());
			actInd.setRisk(eaForm.getIndicatorRisk());
			actInd.setComments(eaForm.getComments());

			MEIndicatorsUtil.saveMEIndicatorValues(actInd, 1);
			
			// remove the activity details from the edit activity list
			if (toDelete == null || (!toDelete.trim().equalsIgnoreCase("true"))) {
				String sessId = session.getId();
				synchronized (ampContext) {
					HashMap activityMap = (HashMap) ampContext.getAttribute(Constants.EDIT_ACT_LIST);					
					activityMap.remove(sessId);
					ArrayList sessList = (ArrayList) ampContext.getAttribute(Constants.SESSION_LIST);
				    sessList.remove(sessId);
				    Collections.sort(sessList);
		            ampContext.setAttribute(Constants.EDIT_ACT_LIST,activityMap);		    
				    ampContext.setAttribute(Constants.SESSION_LIST,sessList);
				    
				    HashMap tsList = (HashMap) ampContext.getAttribute(Constants.TS_ACT_LIST);
				    if (tsList != null) {
				    	tsList.remove(eaForm.getActivityId());
				    }
				    ampContext.setAttribute(Constants.TS_ACT_LIST,tsList);
				    HashMap uList = (HashMap) ampContext.getAttribute(Constants.USER_ACT_LIST);
				    if (uList != null) {
				    	uList.remove(tm.getMemberId());
				    }
				    ampContext.setAttribute(Constants.USER_ACT_LIST,uList);
				    
				}				}
			} else {
				AmpTeamMember teamMember = DbUtil.getAmpTeamMember(tm.getMemberId()) ;
				activity.setActivityCreator(teamMember);
				Calendar cal = Calendar.getInstance();
				activity.setCreatedDate(cal.getTime());
				// Setting approval status of activity
				activity.setApprovalStatus(eaForm.getApprovalStatus());
				// create a new activity
				ActivityUtil.saveActivity(activity, eaForm.getCommentsCol(), 
						eaForm.isSerializeFlag(), field,
						relatedLinks,tm.getMemberId());
			}
		}
		

		boolean surveyFlag = eaForm.isDonorFlag();
		
		eaForm.setDonorFlag(false);
		eaForm.setFundDonor(null);
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
		// Clearing approval process properties
		eaForm.setWorkingTeamLeadFlag("no");
		eaForm.setFundingRegions(null);
		eaForm.setRegionalFundings(null);
		
		int temp = eaForm.getPageId();
		eaForm.setPageId(-1);
		UpdateDB.updateReportCache(activity.getAmpActivityId());
		eaForm.reset(mapping, request);
	
		if (session.getAttribute("ampProjects") != null) {
			session.removeAttribute("ampProjects");
		}
	
		if (temp == 0)
			return mapping.findForward("adminHome");
		else if (temp == 1) {
			if (surveyFlag) {	// forwarding to edit survey action for saving survey responses
				logger.debug("forwarding to edit survey action...");
				return mapping.findForward("saveSurvey");
			}
			else
				return mapping.findForward("viewMyDesktop");
		}
		else
			return null;
		} catch (Exception e) {
		    e.printStackTrace(System.out);
		}
		return null;
	}
	
}
