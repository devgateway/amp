/*
 * EditActivity.java 
 * Created: Feb 10, 2005
 */

package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityClosingDates;
import org.digijava.module.aim.dbentity.AmpActivityInternalId;
import org.digijava.module.aim.dbentity.AmpActor;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpIssues;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpMeasure;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpPerspective;
import org.digijava.module.aim.dbentity.AmpPhysicalPerformance;
import org.digijava.module.aim.dbentity.AmpRegionalFunding;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.Components;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.CurrencyWorker;
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
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.cms.dbentity.CMSContentItem;

/**
 * EditActivity class loads the activity details of the activity specified in
 * the form bean variable 'activityId' to the EditActivityForm bean instance
 * 
 * @author Priyajith
 */
public class EditActivity extends Action {

	private static Logger logger = Logger.getLogger(EditActivity.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		HttpSession session = request.getSession();
		TeamMember tm = (TeamMember) session.getAttribute("currentMember");
		
		// if user is not logged in, forward him to the home page
		if (session.getAttribute("currentMember") == null)
			return mapping.findForward("index");
		
		EditActivityForm eaForm = (EditActivityForm) form; // form bean instance
		
		try {
		
		// Checking whether the activity is already opened for editing
		// by some other user
		ServletContext ampContext = getServlet().getServletContext();
		HashMap activityMap = (HashMap) ampContext.getAttribute("editActivityList");
		if (activityMap != null && activityMap.containsValue(eaForm.getActivityId())) {
		    // The activity is already opened for editing
		    logger.debug("The activity is already opened by another user");
		    ActionErrors errors = new ActionErrors();
		    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
				"error.aim.activityAlreadyOpenedForEdit"));
		    saveErrors(request, errors);
		    
		    String url = "/aim/viewChannelOverview.do?ampActivityId="+eaForm.getActivityId()+"&tabIndex=0";
		    RequestDispatcher rd = getServlet().getServletContext().
		    	getRequestDispatcher(url);
		    rd.forward(request,response);
		} else {
		    // Edit the activity
		    logger.debug("User given permission to edit the activity");
		    String sessId = session.getId();
		    ArrayList sessList = (ArrayList) ampContext.getAttribute("sessionList");
		    if (sessList == null) {
		        sessList = new ArrayList();
		    }
		    if (activityMap == null) {
		        activityMap = new HashMap();
		    }
		    sessList.add(sessId);
		    Collections.sort(sessList);
		    activityMap.put(sessId,eaForm.getActivityId());
		    ampContext.setAttribute("sessionList",sessList);
            ampContext.setAttribute("editActivityList",activityMap);		    
		}

		
		eaForm.reset(mapping, request);
		eaForm.setEditAct(true);
		
		// Clearing comment properties
		String action = request.getParameter("action");
		logger.debug("action [inside EditActivity] : " + action);
		if (action != null && action.trim().length() != 0) {
			if ("edit".equals(action)) {
				eaForm.getCommentsCol().clear();
				eaForm.setCommentFlag(false);
			}
		}

		// load the activity details
		eaForm.setStep("1");
		eaForm.setReset(false);
		eaForm.setPerspectives(DbUtil.getAmpPerspective());
		
		logger.debug("Activity Id = " + eaForm.getActivityId());
		if (eaForm.getActivityId() != null) {
		    AmpActivity activity = ActivityUtil.getAmpActivity(eaForm.getActivityId());

			if (activity != null) {
				// set title,description and objective
				eaForm.setTitle(activity.getName().trim());
				eaForm.setDescription(activity.getDescription().trim());
				eaForm.setObjectives(activity.getObjective().trim());
				eaForm.setAmpId(activity.getAmpId());
				eaForm.setStatusReason(activity.getStatusReason());
				
				eaForm.setCreatedDate(DateConversion.ConvertDateToString(
						activity.getCreatedDate()));

				eaForm.setOriginalAppDate(DateConversion
								.ConvertDateToString(activity
										.getProposedApprovalDate()));
				eaForm.setRevisedAppDate(DateConversion
						.ConvertDateToString(activity.getActualApprovalDate()));
				eaForm.setOriginalStartDate(DateConversion
						.ConvertDateToString(activity.getProposedStartDate()));
				eaForm.setRevisedStartDate(DateConversion
						.ConvertDateToString(activity.getActualStartDate()));
				eaForm.setCurrentCompDate(DateConversion
				        .ConvertDateToString(activity
				                .getActualCompletionDate()));
				
				eaForm.setContractors(activity.getContractors().trim());
				
				Collection col = activity.getClosingDates();				
				Collection dates = new ArrayList();
				if (col != null && col.size() > 0) {
					Iterator itr = col.iterator();
					while (itr.hasNext()) {
						AmpActivityClosingDates cDate = (AmpActivityClosingDates) itr
								.next();
						if (cDate.getType().intValue() == Constants.REVISED.intValue()) {
							dates.add(DateConversion.ConvertDateToString(cDate
									.getClosingDate()));
						}
					}
				}

				eaForm.setActivityCloseDates(dates);

				// loading organizations and thier project ids.
				Set orgProjIdsSet = activity.getInternalIds();
				if (orgProjIdsSet != null) {
					Iterator projIdItr = orgProjIdsSet.iterator();
					Collection temp = new ArrayList();
					while (projIdItr.hasNext()) {
						AmpActivityInternalId actIntId = (AmpActivityInternalId) projIdItr
								.next();
						OrgProjectId projId = new OrgProjectId();
						projId.setAmpOrgId(actIntId.getOrganisation().getAmpOrgId());
						projId.setName(actIntId.getOrganisation().getName());
						projId.setProjectId(actIntId.getInternalId());
						temp.add(projId);
					}
					if (temp != null && temp.size() > 0) {
						OrgProjectId orgProjectIds[] = new OrgProjectId[temp.size()];
						Object arr[] = temp.toArray();
						for (int i = 0; i < arr.length; i++) {
							orgProjectIds[i] = (OrgProjectId) arr[i];
						}
						eaForm.setSelectedOrganizations(orgProjectIds);
					}
				}

				// setting status and modality
				if (activity.getStatus() != null)
					eaForm.setStatus(activity.getStatus().getAmpStatusId());
				if (activity.getLevel() != null)
					eaForm.setLevel(activity.getLevel().getAmpLevelId());


				// loading the locations
				int impLevel = 0;

				Collection ampLocs = activity.getLocations();
				
				if (ampLocs != null && ampLocs.size() > 0) {
					Collection locs = new ArrayList();

					Iterator locIter = ampLocs.iterator();
					boolean maxLevel = false;
					while (locIter.hasNext()) {
						AmpLocation loc = (AmpLocation) locIter.next();
						if (!maxLevel) {
							if (loc.getAmpWoreda() != null) {
								impLevel = 3;
								maxLevel = true;
							} else if (loc.getAmpZone() != null && impLevel < 2) {
								impLevel = 2;
							} else if (loc.getAmpRegion() != null
									&& impLevel < 1) {
								impLevel = 1;
							}
						}

						if (loc != null) {
							Location location = new Location();
							location.setLocId(loc.getAmpLocationId());
							Country cntry = DbUtil
									.getDgCountry(Constants.COUNTRY_ISO);
							location.setCountryId(cntry.getCountryId());
							location.setCountry(cntry.getCountryName());
							if (loc.getAmpRegion() != null) {
								location
										.setRegion(loc.getAmpRegion().getName());
								location.setRegionId(loc.getAmpRegion()
										.getAmpRegionId());
								if (eaForm.getFundingRegions() == null) {
									eaForm.setFundingRegions(new ArrayList());
								}
								if (eaForm.getFundingRegions().contains(loc.getAmpRegion()) == false) {
									eaForm.getFundingRegions().add(loc.getAmpRegion());
								}
							}
							if (loc.getAmpZone() != null) {
								location.setZone(loc.getAmpZone().getName());
								location.setZoneId(loc.getAmpZone()
										.getAmpZoneId());
							}
							if (loc.getAmpWoreda() != null) {
								location
										.setWoreda(loc.getAmpWoreda().getName());
								location.setWoredaId(loc.getAmpWoreda()
										.getAmpWoredaId());
							}
							locs.add(location);
						}
					}
					eaForm.setSelectedLocs(locs);
				}

				switch (impLevel) {
				case 0:
					eaForm.setImplementationLevel("country");
					break;
				case 1:
					eaForm.setImplementationLevel("region");
					break;
				case 2:
					eaForm.setImplementationLevel("zone");
					break;
				case 3:
					eaForm.setImplementationLevel("woreda");
					break;
				default:
					eaForm.setImplementationLevel("country");
				}

				Collection sectors = activity.getSectors();

				if (sectors != null && sectors.size() > 0) {
					Collection activitySectors = new ArrayList();

					Iterator sectItr = sectors.iterator();
					while (sectItr.hasNext()) {
						AmpSector sec = (AmpSector) sectItr.next();
						if (sec != null) {
							AmpSector parent = null;
							AmpSector subsectorLevel1 = null;
							AmpSector subsectorLevel2 = null;
							if (sec.getParentSectorId() != null) {
								if (sec.getParentSectorId().getParentSectorId() != null) {
									subsectorLevel2 = sec;
									subsectorLevel1 = sec.getParentSectorId();
									parent = sec.getParentSectorId()
											.getParentSectorId();
								} else {
									subsectorLevel1 = sec;
									parent = sec.getParentSectorId();
								}
							} else {
								parent = sec;
							}
							ActivitySector actSect = new ActivitySector();
							if (parent != null) {
								actSect.setId(parent.getAmpSectorId());
								actSect.setSectorId(parent.getAmpSectorId());
								actSect.setSectorName(parent.getName());
								if (subsectorLevel1 != null) {
									actSect.setSubsectorLevel1Id(
											subsectorLevel1.getAmpSectorId());
									actSect.setSubsectorLevel1Name(
											subsectorLevel1.getName());
									if (subsectorLevel2 != null) {
										actSect.setSubsectorLevel2Id(
												subsectorLevel2.getAmpSectorId());
										actSect.setSubsectorLevel2Name(
												subsectorLevel2.getName());
									}
								}
							}
							activitySectors.add(actSect);
						}
					}
					eaForm.setActivitySectors(activitySectors);
				}
				
				if (activity.getThemeId() != null) {
					eaForm.setProgram(activity.getThemeId().getAmpThemeId());
				}
				eaForm.setProgramDescription(activity.getProgramDescription().trim());
				
				double totComm = 0;
				double totDisb = 0;
				double totExp = 0;				

				Collection fundingOrgs = new ArrayList();
				Iterator fundItr = activity.getFunding().iterator();
				while (fundItr.hasNext()) {
				    AmpFunding ampFunding = (AmpFunding) fundItr.next();
					AmpOrganisation org = ampFunding.getAmpDonorOrgId();
					FundingOrganization fundOrg = new FundingOrganization();
					fundOrg.setAmpOrgId(org.getAmpOrgId());
					fundOrg.setOrgName(org.getName());				    
					Funding fund = new Funding();
					fund.setAmpTermsAssist(ampFunding
							.getAmpTermsAssistId());
					fund.setFundingId(ampFunding.getAmpFundingId()
							.intValue());
					fund.setOrgFundingId(ampFunding
							.getFinancingId());
					fund.setModality(ampFunding.getModalityId());
					fund.setConditions(ampFunding.getConditions());
					Collection fundDetails = ampFunding.getFundingDetails();	
					Collection funding = new ArrayList();
					if (fundDetails != null
							&& fundDetails.size() > 0) {
						Iterator fundDetItr = fundDetails.iterator();
						Collection fundDetail = new ArrayList();
						
						while (fundDetItr.hasNext()) {
							AmpFundingDetail fundDet = (AmpFundingDetail) fundDetItr.next();
							FundingDetail fundingDetail = new FundingDetail();
							int adjType = fundDet.getAdjustmentType().intValue();
							fundingDetail.setAdjustmentType(adjType);
							if (adjType == Constants.PLANNED) {
								fundingDetail.setAdjustmentTypeName("Planned");
							} else if (adjType == Constants.ACTUAL) {
								fundingDetail.setAdjustmentTypeName("Actual");
								Date dt = fundDet.getTransactionDate();
								double frmExRt = DbUtil.getExchangeRate(
										fundDet.getAmpCurrencyId().getCurrencyCode(),1,dt);
								double toExRt = DbUtil.getExchangeRate(DbUtil.getAmpcurrency(
										tm.getAppSettings().getCurrencyId()).getCurrencyCode(),1,dt);
								double amt = CurrencyWorker.convert1(
										fundDet.getTransactionAmount().doubleValue(),frmExRt,toExRt);
								if (fundDet.getTransactionType().intValue() == Constants.COMMITMENT) {
									totComm += amt;
								} else if (fundDet.getTransactionType().intValue() == Constants.DISBURSEMENT) {
									totDisb += amt;
								} else if (fundDet.getTransactionType().intValue() == Constants.EXPENDITURE) {
									totExp += amt;
								}								
							}
							if (fundDet.getTransactionType().intValue() == Constants.EXPENDITURE) {
								fundingDetail.setClassification(fundDet.getExpCategory());
							}
							fundingDetail.setCurrencyCode(
									fundDet.getAmpCurrencyId().getCurrencyCode());
							fundingDetail.setCurrencyName(
									fundDet.getAmpCurrencyId().getCountryName());

							fundingDetail.setTransactionAmount(
									CurrencyWorker.convert(
											fundDet.getTransactionAmount().doubleValue(),1, 1));
							fundingDetail.setTransactionDate(
									DateConversion.ConvertDateToString(
											fundDet.getTransactionDate()));
							fundingDetail.setPerspectiveCode(fundDet.getOrgRoleCode());
							
							Iterator itr1 = eaForm.getPerspectives().iterator();
							while (itr1.hasNext()) {
								AmpPerspective pers = (AmpPerspective) itr1.next();
								if (pers.getCode().equals(fundDet.getOrgRoleCode())) {
									fundingDetail.setPerspectiveName(pers.getName());
								}
							}
							
							fundingDetail.setTransactionType(
									fundDet.getTransactionType().intValue());
							fundDetail.add(fundingDetail);
						}
						
						fund.setFundingDetails(fundDetail);
						funding.add(fund);
					}
					fundOrg.setFundings(funding);
					fundingOrgs.add(fundOrg);
				}
				eaForm.setFundingOrganizations(fundingOrgs);
				eaForm.setTotalCommitments(totComm);
				eaForm.setTotalDisbursements(totDisb);
				eaForm.setTotalExpenditures(totExp);

				
				ArrayList regFunds = new ArrayList();
				Iterator rItr = activity.getRegionalFundings().iterator();
				
				while (rItr.hasNext()) {
					AmpRegionalFunding ampRegFund = (AmpRegionalFunding) rItr.next();
					FundingDetail fd = new FundingDetail();
					fd.setAdjustmentType(ampRegFund.getAdjustmentType().intValue());
					if (fd.getAdjustmentType() == 1) {
						fd.setAdjustmentTypeName("Actual");
					} else if (fd.getAdjustmentType() == 0) {
						fd.setAdjustmentTypeName("Planned");	
					}
					fd.setCurrencyCode(ampRegFund.getCurrency().getCurrencyCode());
					fd.setCurrencyName(ampRegFund.getCurrency().getCurrencyName());
					fd.setPerspectiveCode(ampRegFund.getPerspective().getCode());
					fd.setPerspectiveName(ampRegFund.getPerspective().getName());
					fd.setTransactionAmount(DecimalToText.ConvertDecimalToText(ampRegFund.getTransactionAmount().doubleValue()));
					fd.setTransactionDate(DateConversion.ConvertDateToString(ampRegFund.getTransactionDate()));
					fd.setTransactionType(ampRegFund.getTransactionType().intValue());
					
					RegionalFunding regFund = new RegionalFunding();
					regFund.setRegionId(ampRegFund.getRegion().getAmpRegionId());
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
					} else if (fd.getTransactionType() == 1) { // disbursements
						if (regFund.getDisbursements() == null) {
							regFund.setDisbursements(new ArrayList());
						}
						regFund.getDisbursements().add(fd);							
					} else if (fd.getTransactionType() == 2) { // expenditures
						if (regFund.getExpenditures() == null) {
							regFund.setExpenditures(new ArrayList());
						}
						regFund.getExpenditures().add(fd);							
					}
					regFunds.set(index,regFund);
				}
				
				eaForm.setRegionalFundings(regFunds);
									    								
				Collection componets = activity.getComponents();
				if (componets != null && componets.size() > 0) {
					Collection comp = new ArrayList();
					Iterator compItr = componets.iterator();
					while (compItr.hasNext()) {
						AmpComponent temp = (AmpComponent) compItr.next();
						Components tempComp = new Components();
						tempComp.setTitle(temp.getTitle());
						tempComp.setAmount(DecimalToText
								.ConvertDecimalToText(temp.getAmount()
										.doubleValue()));
						tempComp.setComponentId(temp.getAmpComponentId());
						if (temp.getCurrency() != null)
							tempComp.setCurrencyCode(temp.getCurrency()
									.getCurrencyCode());
						tempComp.setDescription(temp.getDescription().trim());
						tempComp.setReportingDate(DateConversion
								.ConvertDateToString(temp.getReportingDate()));

						Collection phyProgess = temp.getPhysicalProgress();

						if (phyProgess != null && phyProgess.size() > 0) {
							Collection physicalProgress = new ArrayList();
							Iterator phyProgItr = phyProgess.iterator();
							while (phyProgItr.hasNext()) {
								AmpPhysicalPerformance phyPerf = (AmpPhysicalPerformance) phyProgItr
										.next();
								PhysicalProgress phyProg = new PhysicalProgress();
								phyProg.setPid(phyPerf.getAmpPpId());
								phyProg
										.setDescription(phyPerf
												.getDescription());
								phyProg.setReportingDate(DateConversion
										.ConvertDateToString(phyPerf
												.getReportingDate()));
								phyProg.setTitle(phyPerf.getTitle());
								physicalProgress.add(phyProg);
							}
							tempComp.setPhyProgress(physicalProgress);
						}
						comp.add(tempComp);
					}
					eaForm.setSelectedComponents(comp);
				}

				Collection actDocs = activity.getDocuments();
				if (actDocs != null && actDocs.size() > 0) {
					Collection docsList = new ArrayList();
					Collection linksList = new ArrayList();

					Iterator docItr = actDocs.iterator();
					while (docItr.hasNext()) {
						CMSContentItem cmsItem = (CMSContentItem) docItr.next();
						if (cmsItem.getIsFile()) {
							docsList.add(cmsItem);
						} else {
							linksList.add(cmsItem);
						}
					}
					eaForm.setDocumentList(docsList);
					eaForm.setLinksList(linksList);
				}

				// loading the related organizations
				eaForm.setExecutingAgencies(new ArrayList());
				eaForm.setImpAgencies(new ArrayList());
				eaForm.setReportingOrgs(new ArrayList());
				Set relOrgs = activity.getOrgrole();
				if (relOrgs != null) {
					Iterator relOrgsItr = relOrgs.iterator();
					while (relOrgsItr.hasNext()) {
						AmpOrgRole orgRole = (AmpOrgRole) relOrgsItr.next();
						if (orgRole.getRole().getRoleCode().equals(
								Constants.EXECUTING_AGENCY) && (!eaForm.getExecutingAgencies().contains(orgRole.getOrganisation()))) {
							eaForm.getExecutingAgencies().add(
									orgRole.getOrganisation());
						} else if (orgRole.getRole().getRoleCode().equals(
								Constants.IMPLEMENTING_AGENCY) && (!eaForm.getImpAgencies().contains(orgRole.getOrganisation()))) {
							eaForm.getImpAgencies().add(
									orgRole.getOrganisation());
						} else if (orgRole.getRole().getRoleCode().equals(
								Constants.REPORTING_AGENCY) && (!eaForm.getReportingOrgs().contains(orgRole.getOrganisation()))) {
							eaForm.getReportingOrgs().add(
									orgRole.getOrganisation());
						}
					}
				}
				
				// loading the Issues,Measures and Actors
				if(activity.getIssues() == null) {
					logger.debug("Issues NULL");
				} else {
					logger.debug("Number of issues = " + activity.getIssues().size());
				}
				
				
				if (activity.getIssues() != null &&
						activity.getIssues().size() > 0) {
					ArrayList issueList = new ArrayList();
					Iterator iItr = activity.getIssues().iterator();
					while (iItr.hasNext()) {
						AmpIssues ampIssue = (AmpIssues) iItr.next();
						Issues issue = new Issues();
						issue.setId(ampIssue.getAmpIssueId());
						issue.setName(ampIssue.getName());
						ArrayList measureList = new ArrayList();
						if (ampIssue.getMeasures() != null &&
								ampIssue.getMeasures().size() > 0) {
							Iterator mItr = ampIssue.getMeasures().iterator();
							while (mItr.hasNext()) {
								AmpMeasure ampMeasure = (AmpMeasure) mItr.next();
								Measures measure = new Measures();
								measure.setId(ampMeasure.getAmpMeasureId());
								measure.setName(ampMeasure.getName());
								ArrayList actorList = new ArrayList();
								if (ampMeasure.getActors() != null &&
										ampMeasure.getActors().size() > 0) {
									Iterator aItr = ampMeasure.getActors().iterator();
									while (aItr.hasNext()) {
										AmpActor actor = (AmpActor) aItr.next();
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
				} else {
					eaForm.setIssues(null);
				}

				// loading the contact person details and condition
				eaForm.setDnrCntFirstName(activity.getContFirstName());
				eaForm.setDnrCntLastName(activity.getContLastName());
				eaForm.setDnrCntEmail(activity.getEmail());
				
				eaForm.setMfdCntFirstName(activity.getMofedCntFirstName());
				eaForm.setMfdCntLastName(activity.getMofedCntLastName());
				eaForm.setMfdCntEmail(activity.getMofedCntEmail());
				
				eaForm.setConditions(activity.getCondition().trim());
				
				if (activity.getActivityCreator() != null) {
					User usr = activity.getActivityCreator().getUser();
					if (usr != null) {
						eaForm.setActAthFirstName(usr.getFirstNames());
						eaForm.setActAthLastName(usr.getLastName());
						eaForm.setActAthEmail(usr.getEmail());	
					}
				}
			}
		}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}

		return mapping.findForward("forward");
	}
}
