/*
 * SaveActivity.java
 */

package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.jcr.Node;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.digijava.kernel.user.User;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityClosingDates;
import org.digijava.module.aim.dbentity.AmpActivityInternalId;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpActor;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpComponentFunding;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpIssues;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpMeasure;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpPerspective;
import org.digijava.module.aim.dbentity.AmpPhysicalPerformance;
import org.digijava.module.aim.dbentity.AmpRegion;
import org.digijava.module.aim.dbentity.AmpRegionalFunding;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.EUActivity;
import org.digijava.module.aim.dbentity.EUActivityContribution;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.AmpProject;
import org.digijava.module.aim.helper.CategoryManagerUtil;
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
import org.digijava.module.aim.helper.RelatedLinks;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.AuditLoggerUtil;
import org.digijava.module.aim.util.ComponentsUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DesktopUtil;
import org.digijava.module.aim.util.DocumentUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;

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

		Long actId = null;
		Components tempComp = new Components();

		// if user has not logged in, forward him to the home page
		if (session.getAttribute("currentMember") == null) {
			return mapping.findForward("index");
		}

		session.removeAttribute("report");
		session.removeAttribute("reportMeta");
		session.removeAttribute("forStep9");

		try {

			TeamMember tm = null;
			if (session.getAttribute("currentMember") != null)
				tm = (TeamMember) session.getAttribute("currentMember");

			EditActivityForm eaForm = (EditActivityForm) form;
			AmpActivity activity = new AmpActivity();
			if (activity.getCategories() == null) {
				activity.setCategories( new HashSet() );
			}

			/* Saving categories to AmpActivity */
			CategoryManagerUtil.addCategoryToSet(eaForm.getAccessionInstrument(), activity.getCategories() );
			CategoryManagerUtil.addCategoryToSet(eaForm.getAcChapter(), activity.getCategories() );
			CategoryManagerUtil.addCategoryToSet(eaForm.getStatusId(), activity.getCategories() );
			CategoryManagerUtil.addCategoryToSet(eaForm.getLevelId(), activity.getCategories() );
			/* END - Saving categories to AmpActivity */

            if(eaForm.getProProjCost()==null){
                activity.setFunAmount(null);
                activity.setFunDate(null);
                activity.setCurrencyCode(null);
            }else{
                activity.setFunAmount(eaForm.getProProjCost().getFunAmountAsDouble());
                activity.setFunDate(eaForm.getProProjCost().getFunDate());
                activity.setCurrencyCode(eaForm.getProProjCost().getCurrencyCode());
            }

            if(eaForm.getActPrograms()!=null && eaForm.getActPrograms().size()!=0){
                Set programs=new HashSet();
                //ProgramUtil prg=new ProgramUtil();
                ArrayList prgIds=new ArrayList();
                ArrayList ampThemeLst=null;
                AmpTheme theme=null;

                List themeLst=eaForm.getActPrograms();
                Iterator prgItr=themeLst.listIterator();
                while(prgItr.hasNext()){
                    theme=(AmpTheme)prgItr.next();
                    prgIds.add(theme.getAmpThemeId());
                }

                ampThemeLst = null;
                if(prgIds.size()>0)
                	ampThemeLst=ProgramUtil.getThemesByIds(prgIds);
                if(ampThemeLst!=null){
                    programs.addAll(ampThemeLst);
                    activity.setActivityPrograms(programs);
                }
            }

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
						FundingOrganization fOrg = (FundingOrganization) itr1
								.next();
						if (fOrg.getAmpOrgId().longValue() == eaForm
								.getFundDonor().longValue()) {
							// add fundings
							if (fOrg.getFundings() != null) {
								Iterator itr2 = fOrg.getFundings().iterator();
								while (itr2.hasNext()) {
									Funding fund = (Funding) itr2.next();
									AmpFunding ampFunding = new AmpFunding();
									ampFunding.setAmpFundingId(new Long(fund
											.getFundingId()));

									// add funding details for each funding
									Set fundDeatils = new HashSet();
									if (fund.getFundingDetails() != null) {
										Iterator itr3 = fund
												.getFundingDetails().iterator();
										while (itr3.hasNext()) {
											FundingDetail fundDet = (FundingDetail) itr3
													.next();
											AmpFundingDetail ampFundDet = new AmpFundingDetail();
											ampFundDet
													.setTransactionType(new Integer(
															fundDet
																	.getTransactionType()));
											ampFundDet
													.setPerspectiveId(DbUtil
															.getPerspective(fundDet
																	.getPerspectiveCode()));
											ampFundDet
													.setAdjustmentType(new Integer(
															fundDet
																	.getAdjustmentType()));
											ampFundDet
													.setTransactionDate(DateConversion
															.getDate(fundDet
																	.getTransactionDate()));
											ampFundDet.setOrgRoleCode(fundDet
													.getPerspectiveCode());


											boolean useFixedRate = false;
											if (fundDet.getTransactionType() == Constants.COMMITMENT) {
												if (fundDet.isUseFixedRate() &&
														fundDet.getFixedExchangeRate() > 0) {
													useFixedRate = true;
												}
											}

											if (!useFixedRate) {
												Double transAmt = new Double(
														DecimalToText.getDouble(fundDet.getTransactionAmount()));
												ampFundDet.setTransactionAmount(transAmt);
												ampFundDet.setAmpCurrencyId(
														CurrencyUtil.getCurrencyByCode(fundDet.getCurrencyCode()));
											} else {
												// Use the fixed exchange rate
												double transAmt = DecimalToText.getDouble(fundDet.getTransactionAmount());

												Date trDate = DateConversion.getDate(fundDet.getTransactionDate());
												double frmExRt = CurrencyUtil.getExchangeRate(
														fundDet.getCurrencyCode(),1,trDate);

												double amt = CurrencyWorker.convert1(transAmt, frmExRt,1);
												amt *= fundDet.getFixedExchangeRate();
												ampFundDet.setTransactionAmount(new Double(amt));
												ampFundDet.setFixedExchangeRate(fundDet.getFixedExchangeRate());
												ampFundDet.setAmpCurrencyId(
														CurrencyUtil.getCurrencyByCode(fundDet.getFixedExchangeCurrCode()));
												ampFundDet.setRateCurrencyId(CurrencyUtil.getCurrencyByCode(fundDet.getFixedExchangeCurrCode()));
											}
											ampFundDet.setAmpFundingId(ampFunding);
											if (fundDet.getTransactionType() == Constants.EXPENDITURE) {
												ampFundDet.setExpCategory(
														fundDet.getClassification());
											}
											fundDeatils.add(ampFundDet);
										}
									}
									ampFunding.setFundingDetails(fundDeatils);
									fundings.add(ampFunding);
								}
							}
						}
					}
				}
				ActivityUtil.saveDonorFundingInfo(eaForm.getActivityId(),
						fundings);
			} else {
				// MOFED add/edit

				String toDelete = request.getParameter("delete");

				if (toDelete == null
						|| (!toDelete.trim().equalsIgnoreCase("true"))) {
					if (eaForm.isEditAct() == false) {
						AmpActivity act = ActivityUtil.getActivityByName(eaForm
								.getTitle());
						if (act != null) {
							eaForm.setActivityId(act.getAmpActivityId());
							logger.debug("Activity with the name "
									+ eaForm.getTitle() + " already exist.");
							return mapping.findForward("activityExist");
						}
					}
				} else if (toDelete.trim().equals("true")) {
					eaForm.setEditAct(true);
				} else {
					logger.debug("No duplicate found");
				}

				ActionErrors errors = new ActionErrors();
				boolean titleFlag = false;
				boolean statusFlag = false;

				if (eaForm.getAmpId() == null) {
					/*
					 * The logic for geerating the AMP-ID is as follows:
					 * 1. get default global country code
					 * 2. Get the maximum of the ampActivityId + 1, MAX_NUM
					 * 3. merge them
					 */
					String ampId =
						FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GLOBAL_DEFAULT_COUNTRY).toUpperCase();

			/*		if (eaForm.getFundingOrganizations() != null) {
						if (eaForm.getFundingOrganizations().size() == 1) {
							Iterator itr = eaForm.getFundingOrganizations()
									.iterator();
							if (itr.hasNext()) {
								FundingOrganization fOrg = (FundingOrganization) itr
										.next();
								ampId += "-"
										+ DbUtil.getOrganisation(
												fOrg.getAmpOrgId())
												.getOrgCode();
							}
						}
					}*/
					long maxId = ActivityUtil.getActivityMaxId();
					maxId++;
					ampId += "/" + maxId;
					eaForm.setAmpId(ampId);
				}

				if (eaForm.getTitle() != null) {
					if (eaForm.getTitle().trim().length() == 0) {
						errors.add("title", new ActionError(
								"error.aim.addActivity.titleMissing"));
						saveErrors(request, errors);
						titleFlag = true;
					} else if (eaForm.getTitle().length() > 255) {
						errors.add("title", new ActionError(
								"error.aim.addActivity.titleTooLong"));
						saveErrors(request, errors);
						titleFlag = true;
					}
				}

                Long statId=eaForm.getStatusId();


				if (statId != null && statId.equals(new Long(0))) {
					errors.add("status", new ActionError(
							"error.aim.addActivity.statusMissing"));
					saveErrors(request, errors);
					statusFlag = true;
				}
				if (titleFlag == true || statusFlag == true) {
					eaForm.setStep("1");
					return mapping.findForward("addActivity");
				}

				if (eaForm.getActivitySectors() == null
						|| eaForm.getActivitySectors().size() < 1) {
					errors.add("sector", new ActionError(
							"error.aim.addActivity.sectorEmpty"));
					saveErrors(request, errors);
					eaForm.setStep("2");
					return mapping.findForward("addActivity");
				}

				boolean secPer = false;
				int percent = 0;
				Iterator secPerItr = eaForm.getActivitySectors().iterator();
				while (secPerItr.hasNext()) {
					ActivitySector actSect = (ActivitySector) secPerItr.next();
					if (null == actSect.getSectorPercentage()
							|| "".equals(actSect.getSectorPercentage())) {
						errors.add("sectorPercentageEmpty",
								new ActionError("error.aim.addActivity.sectorPercentageEmpty"));
						logger.debug("sector percentage is empty");
						secPer = true;
					}
					// sector percentage is not a number
					else {
						try {
							percent += actSect.getSectorPercentage().intValue();
						}
						catch (NumberFormatException nex) {
							logger.debug("sector percentage is not a number : " + nex);
							errors.add("sectorPercentageNonNumeric",
									new ActionError("error.aim.addActivity.sectorPercentageNonNumeric"));
							secPer = true;
						}
					}
					if (secPer) {
						saveErrors(request, errors);
						eaForm.setStep("2");
						return mapping.findForward("addActivityStep2");
					}
				}
				// Total sector percentage is not equal to 100%
				if (percent != 100) {
					errors.add("sectorPercentageSumWrong",
							new ActionError("error.aim.addActivity.sectorPercentageSumWrong"));
					saveErrors(request, errors);
					logger.debug("sector percentage is not equal to 100%");
					eaForm.setStep("2");
					return mapping.findForward("addActivityStep2");
				}

				if (eaForm.getFundingOrganizations() != null
						&& eaForm.getFundingOrganizations().size() > 0) {
					Iterator tempItr = eaForm.getFundingOrganizations()
							.iterator();
					while (tempItr.hasNext()) {
						FundingOrganization forg = (FundingOrganization) tempItr
								.next();
						if (forg.getFundings() == null
								|| forg.getFundings().size() == 0) {
							errors
									.add(
											"fundings",
											new ActionError(
													"error.aim.addActivity.fundingsNotEntered"));
							saveErrors(request, errors);
							logger.info(" the funds added is null... please check");
							eaForm.setStep("3");
							return mapping.findForward("addActivity");
						}
					}
				}
				// end of Modified code

                if(eaForm.getProProjCost()==null){
                    activity.setFunAmount(null);
                    activity.setFunDate(null);
                    activity.setCurrencyCode(null);
                }else{
                    activity.setFunAmount(eaForm.getProProjCost().getFunAmountAsDouble());
                    activity.setFunDate(eaForm.getProProjCost().getFunDate());
                    activity.setCurrencyCode(eaForm.getProProjCost().getCurrencyCode());
                }
				activity.setAmpId(eaForm.getAmpId());
				activity.setName(eaForm.getTitle());
				if("unchecked".equals(eaForm.getBudgetCheckbox())==true)
					activity.setBudget(new Boolean(false));
				else activity.setBudget(new Boolean(true));

				/*
				 * tanzania adds
				 */
				activity.setFY(eaForm.getFY());
				activity.setVote(eaForm.getVote());
				activity.setSubVote(eaForm.getSubVote());
				activity.setSubProgram(eaForm.getSubProgram());
				activity.setProjectCode(eaForm.getProjectCode());
				activity.setGovernmentApprovalProcedures(eaForm.getGovernmentApprovalProcedures());

				activity.setGbsSbs(eaForm.getGbsSbs());
				activity.setJointCriteria(eaForm.getJointCriteria());

				if (eaForm.getDescription() == null
						|| eaForm.getDescription().trim().length() == 0) {
					activity.setDescription(new String(" "));
				} else {
					activity.setDescription(eaForm.getDescription());
				}
				if (eaForm.getPurpose() == null
						|| eaForm.getPurpose().trim().length() == 0) {
					activity.setPurpose(new String(" "));
				} else {
					activity.setPurpose(eaForm.getPurpose());
				}
				if (eaForm.getResults() == null
						|| eaForm.getResults().trim().length() == 0) {
					activity.setResults(new String(" "));
				} else {
					activity.setResults(eaForm.getResults());
				}
				if (eaForm.getObjectives() == null
						|| eaForm.getObjectives().trim().length() == 0) {
					activity.setObjective(new String(" "));
				} else {
					activity.setObjective(eaForm.getObjectives());
				}
                if(eaForm.getDocumentSpace() == null
                   || eaForm.getDocumentSpace().trim().length() == 0) {
                    activity.setDocumentSpace(new String(" "));
                } else {
                    activity.setDocumentSpace(eaForm.getDocumentSpace());
                }

				if (eaForm.getConditions() == null
						|| eaForm.getConditions().trim().length() == 0) {
					activity.setCondition(" ");
				} else {
					activity.setCondition(eaForm.getConditions());
				}

				try {
					activity.setLineMinRank(Integer.valueOf(eaForm.getLineMinRank()));
					if (activity.getLineMinRank().intValue() < 1 || activity.getLineMinRank().intValue() > 5) {
						logger.debug("Line Ministry Rank is out of permisible range (1 to 5)");
						activity.setLineMinRank(null);
					}
				}
				catch (NumberFormatException nex) {
					logger.debug("Line Ministry Rank is not a number : " + nex);
					activity.setLineMinRank(null);
				}
				try {
					activity.setPlanMinRank(Integer.valueOf(eaForm.getPlanMinRank()));
					if (activity.getPlanMinRank().intValue() < 1 || activity.getPlanMinRank().intValue() > 5) {
						logger.debug("Plan Ministry Rank is out of permisible range (1 to 5)");
						activity.setPlanMinRank(null);
					}
				}
				catch (NumberFormatException nex) {
					logger.debug("Plan Ministry Rank is not a number : " + nex);
					activity.setPlanMinRank(null);
				}

				activity.setProposedApprovalDate(DateConversion.getDate(eaForm
						.getOriginalAppDate()));
				activity.setActualApprovalDate(DateConversion.getDate(eaForm
						.getRevisedAppDate()));
				activity.setProposedStartDate(DateConversion.getDate(eaForm
						.getOriginalStartDate()));
				activity.setActualStartDate(DateConversion.getDate(eaForm
						.getRevisedStartDate()));
				activity.setActualCompletionDate(DateConversion.getDate(eaForm
						.getCurrentCompDate()));
				activity.setOriginalCompDate(DateConversion.getDate(eaForm
						.getProposedCompDate()));
				activity.setContractingDate(DateConversion.getDate(eaForm
						.getContractingDate()));
				activity.setDisbursmentsDate(DateConversion.getDate(eaForm
						.getDisbursementsDate()));
                activity.setProposedCompletionDate(DateConversion.getDate(eaForm
						.getProposedCompDate()));
				AmpActivityClosingDates closeDate = null;

				if (activity.getClosingDates() == null) {
					activity.setClosingDates(new HashSet());
				}

				if (!(eaForm.isEditAct())) {
					closeDate = new AmpActivityClosingDates();
					closeDate.setAmpActivityId(activity);
					closeDate.setClosingDate(DateConversion.getDate(eaForm
							.getProposedCompDate()));
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

				if (eaForm.getCurrentCompDate() != null
						&& eaForm.getCurrentCompDate().trim().length() > 0) {
					closeDate = new AmpActivityClosingDates();
					closeDate.setAmpActivityId(activity);
					closeDate.setClosingDate(DateConversion.getDate(eaForm
							.getCurrentCompDate()));
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
				activity.setDnrCntTitle(eaForm.getDnrCntTitle());
				activity.setDnrCntOrganization(eaForm.getDnrCntOrganization());
				activity.setDnrCntFaxNumber(eaForm.getDnrCntFaxNumber());
				activity.setDnrCntPhoneNumber(eaForm.getDnrCntPhoneNumber());


				activity.setMofedCntFirstName(eaForm.getMfdCntFirstName());
				activity.setMofedCntLastName(eaForm.getMfdCntLastName());
				activity.setMofedCntEmail(eaForm.getMfdCntEmail());
				activity.setMfdCntTitle(eaForm.getMfdCntTitle());
				activity.setMfdCntOrganization(eaForm.getMfdCntOrganization());
				activity.setMfdCntFaxNumber(eaForm.getMfdCntFaxNumber());
				activity.setMfdCntPhoneNumber(eaForm.getMfdCntPhoneNumber());

				activity.setComments(" ");

				if (eaForm.getContractors() == null
						|| eaForm.getContractors().trim().length() == 0) {
					activity.setContractors(" ");
				} else {
					activity.setContractors(eaForm.getContractors());
				}

				// set status of an activity
//				if (eaForm.getStatusId() != null // TO BE DELETED
//						&& eaForm.getStatus().intValue() != -1) {
//					AmpStatus status = DbUtil.getAmpStatus(eaForm.getStatus());
//					if (status != null) {
//						activity.setStatus(status);
//					}
//				}
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
									&& (!actSect.getSubsectorLevel2Id().equals(new Long(-1)))) {
								sectorId = actSect.getSubsectorLevel2Id();
							} else if (actSect.getSubsectorLevel1Id() != null
									&& (!actSect.getSubsectorLevel1Id().equals(new Long(-1)))) {
								sectorId = actSect.getSubsectorLevel1Id();
							} else {
								sectorId = actSect.getSectorId();
							}
							AmpActivitySector amps = new AmpActivitySector();
							amps.setActivityId(activity);
							if (sectorId != null && (!sectorId.equals(new Long(-1))))
								amps.setSectorId(SectorUtil.getAmpSector(sectorId));
							amps.setSectorPercentage(actSect.getSectorPercentage());
							sectors.add(amps);
						}
					}
					activity.setSectors(sectors);
				}

				if (eaForm.getProgram() != null
						&& (!eaForm.getProgram().equals(new Long(-1)))) {
					AmpTheme theme = ProgramUtil.getThemeObject(eaForm.getProgram());
					if (theme != null) {
						activity.setThemeId(theme);
					}
				}
				if (eaForm.getProgramDescription() != null
						&& eaForm.getProgramDescription().trim().length() != 0) {
					activity.setProgramDescription(eaForm
							.getProgramDescription());
				} else {
					activity.setProgramDescription(" ");
				}

				// set organizations role
				Set orgRole = new HashSet();

				if (eaForm.getFundingOrganizations() != null) { // funding
																// organizations
					AmpRole role = DbUtil.getAmpRole(Constants.DONOR);
					Iterator itr = eaForm.getFundingOrganizations().iterator();
					while (itr.hasNext()) {
						FundingOrganization fOrg = (FundingOrganization) itr
								.next();
						AmpOrganisation ampOrg = DbUtil.getOrganisation(fOrg
								.getAmpOrgId());
						AmpOrgRole ampOrgRole = new AmpOrgRole();
						ampOrgRole.setActivity(activity);
						ampOrgRole.setRole(role);
						ampOrgRole.setOrganisation(ampOrg);
						orgRole.add(ampOrgRole);
					}
				}
				if (eaForm.getExecutingAgencies() != null) { // executing
																// agencies
					AmpRole role = DbUtil
							.getAmpRole(Constants.EXECUTING_AGENCY);
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
					AmpRole role = DbUtil
							.getAmpRole(Constants.IMPLEMENTING_AGENCY);
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
				if (eaForm.getBenAgencies() != null) { // beneficiary agencies
					AmpRole role = DbUtil
							.getAmpRole(Constants.BENEFICIARY_AGENCY);
					Iterator itr = eaForm.getBenAgencies().iterator();
					while (itr.hasNext()) {
						AmpOrganisation org = (AmpOrganisation) itr.next();
						AmpOrgRole ampOrgRole = new AmpOrgRole();
						ampOrgRole.setActivity(activity);
						ampOrgRole.setRole(role);
						ampOrgRole.setOrganisation(org);
						orgRole.add(ampOrgRole);
					}
				}
				if (eaForm.getConAgencies() != null) { // contracting agencies
					AmpRole role = DbUtil
							.getAmpRole(Constants.CONTRACTING_AGENCY);
					Iterator itr = eaForm.getConAgencies().iterator();
					while (itr.hasNext()) {
						AmpOrganisation org = (AmpOrganisation) itr.next();
						AmpOrgRole ampOrgRole = new AmpOrgRole();
						ampOrgRole.setActivity(activity);
						ampOrgRole.setRole(role);
						ampOrgRole.setOrganisation(org);
						orgRole.add(ampOrgRole);
					}
				}
				if (eaForm.getReportingOrgs() != null) { // Reporting
															// Organization
					AmpRole role = DbUtil
							.getAmpRole(Constants.REPORTING_AGENCY);
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
						AmpLocation ampLoc = LocationUtil.getAmpLocation(loc
								.getCountryId(), loc.getRegionId(), loc
								.getZoneId(), loc.getWoredaId());

						if (ampLoc == null) {
							ampLoc = new AmpLocation();
							ampLoc.setCountry(loc.getCountry());
							ampLoc.setDgCountry(DbUtil.getDgCountry(loc
									.getCountryId()));
							ampLoc.setRegion(loc.getRegion());
							ampLoc.setAmpRegion(LocationUtil.getAmpRegion(loc
									.getRegionId()));
							ampLoc.setAmpZone(LocationUtil
									.getAmpZone(loc.getZoneId()));
							ampLoc.setAmpWoreda(LocationUtil.getAmpWoreda(loc
									.getWoredaId()));
							ampLoc.setDescription(new String(" "));
							DbUtil.add(ampLoc);
						}
						locations.add(ampLoc);
					}
					activity.setLocations(locations);
				}

				if(eaForm.getCosts()!=null) {
					Set costs=new HashSet();
					Iterator i=eaForm.getCosts().iterator();
					while (i.hasNext()) {
						EUActivity element = (EUActivity) i.next();
						element.setActivity(activity);
						element.setId(null);
						Iterator ii=element.getContributions().iterator();
						while (ii.hasNext()) {
							EUActivityContribution element2 = (EUActivityContribution) ii.next();
							element2.setId(null);
						}
						costs.add(element);
					}
					activity.setCosts(costs);
				}


//				// set level // TO BE DELETED
//				if (eaForm.getLevel() != null) {
//					if (eaForm.getLevel().intValue() != -1) {
//						AmpLevel level = DbUtil.getAmpLevel(eaForm.getLevel());
//						activity.setLevel(level);
//					}
//				}

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

				// if the activity is being added from a users workspace,
				// associate the
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
					OrgProjectId orgProjId[] = eaForm
							.getSelectedOrganizations();
					for (int i = 0; i < orgProjId.length; i++) {
						AmpActivityInternalId actInternalId = new AmpActivityInternalId();
						actInternalId.setOrganisation(DbUtil
								.getOrganisation(orgProjId[i].getAmpOrgId()));
						actInternalId
								.setInternalId(orgProjId[i].getProjectId());
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
						AmpComponent ampComp = null;
						Collection col = ComponentsUtil.getComponent(comp.getTitle());
						Iterator it = col.iterator();
						while(it.hasNext())
						{
							ampComp = (AmpComponent)it.next();
							activity.getComponents().add(ampComp);
						}

						//ampComp = DbUtil.getActivityAmpComments(comp.getComponentId());
						// activity.getComponents().add(ampComp);
						/*
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
						*/

						if (comp.getCommitments() != null
								&& comp.getCommitments().size() > 0) {
							Iterator itr2 = comp.getCommitments().iterator();
							while (itr2.hasNext()) {
								AmpComponentFunding ampCompFund = new AmpComponentFunding();
								ampCompFund.setActivity(activity);
								ampCompFund.setTransactionType(new Integer(
										Constants.COMMITMENT));
								FundingDetail fd = (FundingDetail) itr2.next();
								Iterator tmpItr = eaForm.getCurrencies()
										.iterator();
								while (tmpItr.hasNext()) {
									AmpCurrency curr = (AmpCurrency) tmpItr
											.next();
									if (curr.getCurrencyCode().equals(
											fd.getCurrencyCode())) {
										ampCompFund.setCurrency(curr);
										break;
									}
								}
								tmpItr = eaForm.getPerspectives().iterator();
								while (tmpItr.hasNext()) {
									AmpPerspective pers = (AmpPerspective) tmpItr
											.next();
									if (pers.getCode().equals(
											fd.getPerspectiveCode())) {
										ampCompFund.setPerspective(pers);
										break;
									}
								}
								Collection col1 = ActivityUtil.getFundingComponentActivity(ampComp.getAmpComponentId(),eaForm.getActivityId());
								Iterator itr23 = col1.iterator();
								while(itr23.hasNext())
								{
									AmpComponentFunding ampf = (AmpComponentFunding) itr23.next();
									ampCompFund.setAmpComponentFundingId(ampf.getAmpComponentFundingId());

								}
								ampCompFund.setReportingOrganization(null);
								ampCompFund.setTransactionAmount(new Double(
										DecimalToText.getDouble(fd
												.getTransactionAmount())));
								ampCompFund.setTransactionDate(DateConversion
										.getDate(fd.getTransactionDate()));
								ampCompFund.setAdjustmentType(new Integer(fd
										.getAdjustmentType()));
								ampCompFund.setComponent(ampComp);
								HashSet temp = new HashSet();
								temp.add(ampCompFund);
								tempComp.setCommitments(temp);
							}
						}

						if (comp.getDisbursements() != null
								&& comp.getDisbursements().size() > 0) {
							Iterator itr2 = comp.getDisbursements().iterator();
							while (itr2.hasNext()) {
								AmpComponentFunding ampCompFund = new AmpComponentFunding();
								ampCompFund.setActivity(activity);
								ampCompFund.setTransactionType(new Integer(
										Constants.DISBURSEMENT));
								FundingDetail fd = (FundingDetail) itr2.next();
								Iterator tmpItr = eaForm.getCurrencies()
										.iterator();
								while (tmpItr.hasNext()) {
									AmpCurrency curr = (AmpCurrency) tmpItr
											.next();
									if (curr.getCurrencyCode().equals(
											fd.getCurrencyCode())) {
										ampCompFund.setCurrency(curr);
										break;
									}
								}
								tmpItr = eaForm.getPerspectives().iterator();
								while (tmpItr.hasNext()) {
									AmpPerspective pers = (AmpPerspective) tmpItr
											.next();
									if (pers.getCode().equals(
											fd.getPerspectiveCode())) {
										ampCompFund.setPerspective(pers);
										break;
									}
								}
								ampCompFund.setTransactionAmount(new Double(
										DecimalToText.getDouble(fd
												.getTransactionAmount())));
								ampCompFund.setTransactionDate(DateConversion
										.getDate(fd.getTransactionDate()));
								ampCompFund.setAdjustmentType(new Integer(fd
										.getAdjustmentType()));
								ampCompFund.setComponent(ampComp);
								HashSet temp = new HashSet();
								temp.add(ampCompFund);
								tempComp.setDisbursements(temp);
							}
						}

						if (comp.getExpenditures() != null
								&& comp.getExpenditures().size() > 0) {
							Iterator itr2 = comp.getExpenditures().iterator();
							while (itr2.hasNext()) {
								AmpComponentFunding ampCompFund = new AmpComponentFunding();
								ampCompFund.setActivity(activity);
								ampCompFund.setTransactionType(new Integer(
										Constants.EXPENDITURE));
								FundingDetail fd = (FundingDetail) itr2.next();
								Iterator tmpItr = eaForm.getCurrencies()
										.iterator();
								while (tmpItr.hasNext()) {
									AmpCurrency curr = (AmpCurrency) tmpItr
											.next();
									if (curr.getCurrencyCode().equals(
											fd.getCurrencyCode())) {
										ampCompFund.setCurrency(curr);
										break;
									}
								}
								tmpItr = eaForm.getPerspectives().iterator();
								while (tmpItr.hasNext()) {
									AmpPerspective pers = (AmpPerspective) tmpItr
											.next();
									if (pers.getCode().equals(
											fd.getPerspectiveCode())) {
										ampCompFund.setPerspective(pers);
										break;
									}
								}
								ampCompFund.setTransactionAmount(new Double(
										DecimalToText.getDouble(fd
												.getTransactionAmount())));
								ampCompFund.setTransactionDate(DateConversion
										.getDate(fd.getTransactionDate()));
								ampCompFund.setAdjustmentType(new Integer(fd
										.getAdjustmentType()));
								ampCompFund.setComponent(ampComp);
								HashSet temp = new HashSet();
								temp.add(ampCompFund);
								tempComp.setExpenditures(temp);

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
										|| phyProg.getDescription().trim()
												.length() == 0) {
									ampPhyPerf.setDescription(new String(" "));
								} else {
									ampPhyPerf.setDescription(phyProg
											.getDescription());
								}
								ampPhyPerf.setReportingDate(DateConversion
										.getDate(phyProg.getReportingDate()));
								ampPhyPerf.setTitle(phyProg.getTitle());
								ampPhyPerf.setAmpActivityId(activity);
								ampPhyPerf.setComponent(ampComp);
								ampPhyPerf.setComments(" ");
								phyProgess.add(ampPhyPerf);
							}
						}
					}
				}

				// set funding and funding details
				Set fundings = new HashSet();
				if (eaForm.getFundingOrganizations() != null) {
					Iterator itr1 = eaForm.getFundingOrganizations().iterator();
					while (itr1.hasNext()) {
						FundingOrganization fOrg = (FundingOrganization) itr1
								.next();

						// add fundings
						if (fOrg.getFundings() != null) {
							Iterator itr2 = fOrg.getFundings().iterator();
							while (itr2.hasNext()) {
								Funding fund = (Funding) itr2.next();
								AmpFunding ampFunding = new AmpFunding();
								ampFunding.setAmpDonorOrgId(DbUtil
										.getOrganisation(fOrg.getAmpOrgId()));
								ampFunding.setFinancingId(fund
										.getOrgFundingId());
								/*
								 * if (fund.getSignatureDate() != null)
								 * ampFunding.setSignatureDate(DateConversion
								 * .getDate(fund.getSignatureDate()));
								 */
//								ampFunding.setModalityId(fund.getModality());
								ampFunding.setFinancingInstrument(fund.getFinancingInstrument());
								if (fund.getConditions() != null
										&& fund.getConditions().trim().length() != 0) {
									ampFunding.setConditions(fund
											.getConditions());
								} else {
									ampFunding.setConditions(new String(" "));
								}
								ampFunding.setComments(new String(" "));
/*								ampFunding.setAmpTermsAssistId(fund
										.getAmpTermsAssist());*/
								ampFunding.setTypeOfAssistance( fund.getTypeOfAssistance() );
								ampFunding.setAmpActivityId(activity);

								// add funding details for each funding
								Set fundDeatils = new HashSet();
								if (fund.getFundingDetails() != null) {
									Iterator itr3 = fund.getFundingDetails()
											.iterator();
									while (itr3.hasNext()) {
										FundingDetail fundDet = (FundingDetail) itr3
												.next();
										AmpFundingDetail ampFundDet = new AmpFundingDetail();
										ampFundDet
												.setTransactionType(new Integer(
														fundDet
																.getTransactionType()));
										// ampFundDet.setPerspectiveId(DbUtil.getPerspective(Constants.MOFED));
										ampFundDet.setPerspectiveId(DbUtil
												.getPerspective(fundDet
														.getPerspectiveCode()));
										ampFundDet
												.setAdjustmentType(new Integer(
														fundDet
																.getAdjustmentType()));
										ampFundDet
												.setTransactionDate(DateConversion
														.getDate(fundDet
																.getTransactionDate()));
										ampFundDet.setOrgRoleCode(fundDet
												.getPerspectiveCode());

										boolean useFixedRate = false;
										if (fundDet.getTransactionType() == Constants.COMMITMENT) {
											if (fundDet.isUseFixedRate() &&
													fundDet.getFixedExchangeRate() > 0) {
												useFixedRate = true;
											}
										}

										if (!useFixedRate) {
											Double transAmt = new Double(
													DecimalToText.getDouble(fundDet.getTransactionAmount()));
											ampFundDet.setTransactionAmount(transAmt);
											ampFundDet.setAmpCurrencyId(
													CurrencyUtil.getCurrencyByCode(fundDet.getCurrencyCode()));
										} else {
											// Use the fixed exchange rate
											double transAmt = DecimalToText.getDouble(fundDet.getTransactionAmount());

											Date trDate = DateConversion.getDate(fundDet.getTransactionDate());
											double frmExRt = CurrencyUtil.getExchangeRate(
													fundDet.getCurrencyCode(),1,trDate);

											double amt = CurrencyWorker.convert1(transAmt, frmExRt,1);
											amt *= fundDet.getFixedExchangeRate();
											ampFundDet.setTransactionAmount(new Double(amt));
											ampFundDet.setFixedExchangeRate(fundDet.getFixedExchangeRate());
											ampFundDet.setAmpCurrencyId(
													CurrencyUtil.getCurrencyByCode(fundDet.getCurrencyCode()));
											ampFundDet.setRateCurrencyId(CurrencyUtil.getCurrencyByCode(fundDet.getFixedExchangeCurrCode()));
										}
										ampFundDet.setAmpFundingId(ampFunding);
										if (fundDet.getTransactionType() == Constants.EXPENDITURE) {
											ampFundDet.setExpCategory(
													fundDet.getClassification());
										}
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
				if (eaForm.getRegionalFundings() != null
						&& eaForm.getRegionalFundings().size() > 0) {
					Iterator itr1 = eaForm.getRegionalFundings().iterator();
					while (itr1.hasNext()) {
						RegionalFunding regFund = (RegionalFunding) itr1.next();
						if (regFund.getCommitments() != null
								&& regFund.getCommitments().size() > 0) {
							Iterator itr2 = regFund.getCommitments().iterator();
							while (itr2.hasNext()) {
								AmpRegionalFunding ampRegFund = new AmpRegionalFunding();
								ampRegFund.setActivity(activity);
								ampRegFund.setTransactionType(new Integer(
										Constants.COMMITMENT));
								FundingDetail fd = (FundingDetail) itr2.next();
								Iterator tmpItr = eaForm.getCurrencies()
										.iterator();
								while (tmpItr.hasNext()) {
									AmpCurrency curr = (AmpCurrency) tmpItr
											.next();
									if (curr.getCurrencyCode().equals(
											fd.getCurrencyCode())) {
										ampRegFund.setCurrency(curr);
										break;
									}
								}
								tmpItr = eaForm.getPerspectives().iterator();
								while (tmpItr.hasNext()) {
									AmpPerspective pers = (AmpPerspective) tmpItr
											.next();
									if (pers.getCode().equals(
											fd.getPerspectiveCode())) {
										ampRegFund.setPerspective(pers);
										break;
									}
								}
								tmpItr = eaForm.getFundingRegions().iterator();
								while (tmpItr.hasNext()) {
									AmpRegion reg = (AmpRegion) tmpItr.next();
									if (reg.getAmpRegionId().equals(
											regFund.getRegionId())) {
										ampRegFund.setRegion(reg);
										break;
									}
								}
								ampRegFund.setTransactionAmount(new Double(
										DecimalToText.getDouble(fd
												.getTransactionAmount())));
								ampRegFund.setTransactionDate(DateConversion
										.getDate(fd.getTransactionDate()));
								ampRegFund.setAdjustmentType(new Integer(fd
										.getAdjustmentType()));
								regFundings.add(ampRegFund);
							}
						}

						if (regFund.getDisbursements() != null
								&& regFund.getDisbursements().size() > 0) {
							Iterator itr2 = regFund.getDisbursements()
									.iterator();
							while (itr2.hasNext()) {
								AmpRegionalFunding ampRegFund = new AmpRegionalFunding();
								ampRegFund.setActivity(activity);
								ampRegFund.setTransactionType(new Integer(
										Constants.DISBURSEMENT));
								FundingDetail fd = (FundingDetail) itr2.next();
								Iterator tmpItr = eaForm.getCurrencies()
										.iterator();
								while (tmpItr.hasNext()) {
									AmpCurrency curr = (AmpCurrency) tmpItr
											.next();
									if (curr.getCurrencyCode().equals(
											fd.getCurrencyCode())) {
										ampRegFund.setCurrency(curr);
										break;
									}
								}
								tmpItr = eaForm.getPerspectives().iterator();
								while (tmpItr.hasNext()) {
									AmpPerspective pers = (AmpPerspective) tmpItr
											.next();
									if (pers.getCode().equals(
											fd.getPerspectiveCode())) {
										ampRegFund.setPerspective(pers);
										break;
									}
								}
								tmpItr = eaForm.getFundingRegions().iterator();
								while (tmpItr.hasNext()) {
									AmpRegion reg = (AmpRegion) tmpItr.next();
									if (reg.getAmpRegionId().equals(
											regFund.getRegionId())) {
										ampRegFund.setRegion(reg);
										break;
									}
								}
								ampRegFund.setTransactionAmount(new Double(
										DecimalToText.getDouble(fd
												.getTransactionAmount())));
								ampRegFund.setTransactionDate(DateConversion
										.getDate(fd.getTransactionDate()));
								ampRegFund.setAdjustmentType(new Integer(fd
										.getAdjustmentType()));
								regFundings.add(ampRegFund);
							}
						}

						if (regFund.getExpenditures() != null
								&& regFund.getExpenditures().size() > 0) {

							Iterator itr2 = regFund.getExpenditures()
									.iterator();
							while (itr2.hasNext()) {
								AmpRegionalFunding ampRegFund = new AmpRegionalFunding();
								ampRegFund.setActivity(activity);
								ampRegFund.setTransactionType(new Integer(
										Constants.EXPENDITURE));
								FundingDetail fd = (FundingDetail) itr2.next();
								Iterator tmpItr = eaForm.getCurrencies()
										.iterator();
								while (tmpItr.hasNext()) {
									AmpCurrency curr = (AmpCurrency) tmpItr
											.next();
									if (curr.getCurrencyCode().equals(
											fd.getCurrencyCode())) {
										ampRegFund.setCurrency(curr);
										break;
									}
								}
								tmpItr = eaForm.getPerspectives().iterator();
								while (tmpItr.hasNext()) {
									AmpPerspective pers = (AmpPerspective) tmpItr
											.next();
									if (pers.getCode().equals(
											fd.getPerspectiveCode())) {
										ampRegFund.setPerspective(pers);
										break;
									}
								}
								tmpItr = eaForm.getFundingRegions().iterator();
								while (tmpItr.hasNext()) {
									AmpRegion reg = (AmpRegion) tmpItr.next();
									if (reg.getAmpRegionId().equals(
											regFund.getRegionId())) {
										ampRegFund.setRegion(reg);
										break;
									}
								}
								ampRegFund.setTransactionAmount(new Double(
										DecimalToText.getDouble(fd
												.getTransactionAmount())));
								ampRegFund.setTransactionDate(DateConversion
										.getDate(fd.getTransactionDate()));
								ampRegFund.setAdjustmentType(new Integer(fd
										.getAdjustmentType()));
								regFundings.add(ampRegFund);
							}

						}
					}
				}

				// Delete the following code
				Iterator tmp = regFundings.iterator();
				while (tmp.hasNext()) {
					AmpRegionalFunding rf = (AmpRegionalFunding) tmp.next();
					logger.debug("Regional Fundings :" + rf.getAdjustmentType()
							+ " " + rf.getTransactionAmount());
				}

				activity.setRegionalFundings(regFundings);

				if (eaForm.getIssues() != null && eaForm.getIssues().size() > 0) {
					Set issueSet = new HashSet();
					for (int i = 0; i < eaForm.getIssues().size(); i++) {
						Issues issue = (Issues) eaForm.getIssues().get(i);
						AmpIssues ampIssue = new AmpIssues();
						ampIssue.setActivity(activity);
						ampIssue.setName(issue.getName());
						Set measureSet = new HashSet();
						if (issue.getMeasures() != null
								&& issue.getMeasures().size() > 0) {
							for (int j = 0; j < issue.getMeasures().size(); j++) {
								Measures measure = (Measures) issue
										.getMeasures().get(j);
								AmpMeasure ampMeasure = new AmpMeasure();
								ampMeasure.setIssue(ampIssue);
								ampMeasure.setName(measure.getName());
								Set actorSet = new HashSet();
								if (measure.getActors() != null
										&& measure.getActors().size() > 0) {
									for (int k = 0; k < measure.getActors()
											.size(); k++) {
										AmpActor actor = (AmpActor) measure
												.getActors().get(k);
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
                                        activity.setActivityCreator(eaForm.getCreatedBy());
					// update an existing activity
					//request.get
					actId = ActivityUtil.saveActivity(activity, eaForm.getActivityId(),
							true, eaForm.getCommentsCol(), eaForm
									.isSerializeFlag(), field, relatedLinks, tm
									.getMemberId(), eaForm.getIndicatorsME(),tempComp);
					//for logging the activity
					AuditLoggerUtil.logObject(session, request,activity,"update");
						// remove the activity details from the edit activity list
					if (toDelete == null
							|| (!toDelete.trim().equalsIgnoreCase("true"))) {
						String sessId = session.getId();
						synchronized (ampContext) {
							HashMap activityMap = (HashMap) ampContext
									.getAttribute(Constants.EDIT_ACT_LIST);
							activityMap.remove(sessId);
							ArrayList sessList = (ArrayList) ampContext
									.getAttribute(Constants.SESSION_LIST);
							sessList.remove(sessId);
							Collections.sort(sessList);
							ampContext.setAttribute(Constants.EDIT_ACT_LIST,
									activityMap);
							ampContext.setAttribute(Constants.SESSION_LIST,
									sessList);

							HashMap tsList = (HashMap) ampContext
									.getAttribute(Constants.TS_ACT_LIST);
							if (tsList != null) {
								tsList.remove(eaForm.getActivityId());
							}
							ampContext.setAttribute(Constants.TS_ACT_LIST,
									tsList);
							HashMap uList = (HashMap) ampContext
									.getAttribute(Constants.USER_ACT_LIST);
							if (uList != null) {
								uList.remove(tm.getMemberId());
							}
							ampContext.setAttribute(Constants.USER_ACT_LIST,
									uList);

						}
					}
				} else {
					AmpTeamMember teamMember = TeamMemberUtil.getAmpTeamMember(tm
							.getMemberId());
					activity.setActivityCreator(teamMember);
					Calendar cal = Calendar.getInstance();
					activity.setCreatedDate(cal.getTime());
					// Setting approval status of activity
					activity.setApprovalStatus(eaForm.getApprovalStatus());
					// create a new activity

					actId = ActivityUtil.saveActivity(activity,
							eaForm.getCommentsCol(), eaForm.isSerializeFlag(),
							field, relatedLinks, tm.getMemberId(), tempComp);
					//for logging the activity
					 AuditLoggerUtil.logObject(session, request,activity,"add");

				}
			}

            if(DocumentUtil.isDMEnabled()) {
                Site currentSite = RequestUtils.getSite(request);
                Node spaceNode = DocumentUtil.getDocumentSpace(currentSite,
                    activity.getDocumentSpace());
                DocumentUtil.renameNode(spaceNode, activity.getName());
            }
            /**
             * @todo give name to document space
             */
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
//			eaForm.setSectorReset(true);
			eaForm.setPhyProgReset(true);
			// Clearing comment properties
			eaForm.getCommentsCol().clear();
			eaForm.setCommentFlag(false);
			// Clearing approval process properties
			eaForm.setWorkingTeamLeadFlag("no");
			eaForm.setFundingRegions(null);
			eaForm.setRegionalFundings(null);
			eaForm.setLineMinRank(null);
			eaForm.setPlanMinRank(null);

			/* Clearing categories */
			eaForm.setAccessionInstrument(new Long(0));
			eaForm.setAcChapter(new Long(0));
			eaForm.setStatusId(new Long(0));
			/* END - Clearing categories */

			int temp = eaForm.getPageId();
			eaForm.setPageId(-1);
			//UpdateDB.updateReportCache(activity.getAmpActivityId());
			eaForm.reset(mapping, request);

			if (session.getAttribute(Constants.AMP_PROJECTS) != null) {

				Collection col = (Collection) session.getAttribute(
						Constants.AMP_PROJECTS);
				AmpProject project = new AmpProject();
				project.setAmpActivityId(actId);
				col.remove(project);

				Collection actIds = new ArrayList();
				actIds.add(actId);
				Collection dirtyActivities = DesktopUtil.getAmpProjects(actIds);
				Iterator pItr = dirtyActivities.iterator();
				if (pItr.hasNext()) {
					AmpProject proj = (AmpProject) pItr.next();
					col.add(proj);
				}
				session.setAttribute(Constants.AMP_PROJECTS,col);
				session.setAttribute(Constants.DIRTY_ACTIVITY_LIST,dirtyActivities);
				session.setAttribute(Constants.DESKTOP_SETTINGS_CHANGED,new Boolean(true));
			}

			if (tm.getTeamHead()) {
				if (session.getAttribute(Constants.MY_TASKS) != null) {
					session.removeAttribute(Constants.MY_TASKS);
				}
			}

			if (temp == 0)
				return mapping.findForward("adminHome");
			else if (temp == 1) {
				if (surveyFlag) { // forwarding to edit survey action for
									// saving survey responses
					logger.debug("forwarding to edit survey action...");
					return mapping.findForward("saveSurvey");
				} else {
					return mapping.findForward("viewMyDesktop");
				}
			} else {
				logger.info("returning null....");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
            throw new RuntimeException("Save Activity Error",e);

		}
	}

}
