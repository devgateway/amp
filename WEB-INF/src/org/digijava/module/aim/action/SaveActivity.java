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

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityClosingDates;
import org.digijava.module.aim.dbentity.AmpActivityComponente;
import org.digijava.module.aim.dbentity.AmpActivityDocument;
import org.digijava.module.aim.dbentity.AmpActivityInternalId;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpActivityProgram;
import org.digijava.module.aim.dbentity.AmpActivityReferenceDoc;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpActor;
import org.digijava.module.aim.dbentity.AmpCategoryValue;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpComponentFunding;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFeaturesVisibility;
import org.digijava.module.aim.dbentity.AmpFieldsVisibility;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpFundingMTEFProjection;
import org.digijava.module.aim.dbentity.AmpIssues;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpMeasure;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpPhysicalPerformance;
import org.digijava.module.aim.dbentity.AmpRegion;
import org.digijava.module.aim.dbentity.AmpRegionalFunding;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.EUActivity;
import org.digijava.module.aim.dbentity.EUActivityContribution;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.ActivityDocumentsConstants;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.AmpProject;
import org.digijava.module.aim.helper.CategoryManagerUtil;
import org.digijava.module.aim.helper.Components;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.Funding;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.aim.helper.FundingOrganization;
import org.digijava.module.aim.helper.Issues;
import org.digijava.module.aim.helper.Location;
import org.digijava.module.aim.helper.MTEFProjection;
import org.digijava.module.aim.helper.Measures;
import org.digijava.module.aim.helper.OrgProjectId;
import org.digijava.module.aim.helper.PhysicalProgress;
import org.digijava.module.aim.helper.ReferenceDoc;
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
import org.digijava.module.aim.util.LuceneUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.contentrepository.action.SelectDocumentDM;
import org.digijava.module.contentrepository.helper.DocumentData;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.helper.TemporaryDocumentData;
import org.digijava.module.message.triggers.ActivitySaveTrigger;
import org.digijava.module.message.helper.AmpMessageWorker;
import org.digijava.module.message.triggers.ApprovedActivityTrigger;
import org.digijava.module.message.triggers.NotApprovedActivityTrigger;

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
		Set<Components<AmpComponentFunding>> tempComp = new HashSet<Components<AmpComponentFunding>>();

		// if user has not logged in, forward him to the home page
		if (session.getAttribute("currentMember") == null) {
			return mapping.findForward("index");
		}


		session.removeAttribute("report");
		session.removeAttribute("reportMeta");
		session.removeAttribute("forStep9");
		session.removeAttribute("commentColInSession");

		try {
			ActionErrors errors = new ActionErrors();
			TeamMember tm = null;
			if (session.getAttribute("currentMember") != null)
				tm = (TeamMember) session.getAttribute("currentMember");

			EditActivityForm eaForm = (EditActivityForm) form;
			//if(eaForm!=null) return mapping.findForward("addActivityStepX");

			AmpActivity activity = new AmpActivity();
			if (activity.getCategories() == null) {
				activity.setCategories( new HashSet() );
			}
                        boolean createdAsDraft=false;
                        if(!eaForm.isEditAct()){
                            activity.setCreatedAsDraft(eaForm.getDraft());
                            createdAsDraft=eaForm.getDraft();
                        }
                        else{
                            if(eaForm.getWasDraft()&&!eaForm.getDraft()){
                                activity.setCreatedAsDraft(false);
                                createdAsDraft=false;
                            }
                            else{
                                createdAsDraft=true;
                            }
                        }
			/* Saving categories to AmpActivity */
			CategoryManagerUtil.addCategoryToSet(eaForm.getAccessionInstrument(), activity.getCategories() );
			CategoryManagerUtil.addCategoryToSet(eaForm.getAcChapter(), activity.getCategories() );
			CategoryManagerUtil.addCategoryToSet(eaForm.getStatusId(), activity.getCategories() );
			CategoryManagerUtil.addCategoryToSet(eaForm.getLevelId(), activity.getCategories() );
			CategoryManagerUtil.addCategoryToSet(eaForm.getGbsSbs(), activity.getCategories() );
                        CategoryManagerUtil.addCategoryToSet(eaForm.getImplemLocationLevel(), activity.getCategories() );
                        CategoryManagerUtil.addCategoryToSet(eaForm.getActivityLevel(), activity.getCategories());
			/* END - Saving categories to AmpActivity */

			/* Saving related documents into AmpActivity */
            HashSet<String>UUIDs				= new HashSet<String>();
            Collection<DocumentData> tempDocs	= TemporaryDocumentData.retrieveTemporaryDocDataList(request);
            Iterator<DocumentData> docIter			= tempDocs.iterator();
            while ( docIter.hasNext() ) {
            	TemporaryDocumentData tempDoc	= (TemporaryDocumentData) docIter.next();
            	NodeWrapper nodeWrapper			= tempDoc.saveToRepository(request, eaForm);
            	if ( nodeWrapper != null )
            			UUIDs.add( nodeWrapper.getUuid() );
            }
            if(SelectDocumentDM.getSelectedDocsSet(request, ActivityDocumentsConstants.RELATED_DOCUMENTS, false)!=null) {
            	UUIDs.addAll(SelectDocumentDM.getSelectedDocsSet(request, ActivityDocumentsConstants.RELATED_DOCUMENTS, false));
            }
			if (UUIDs != null && UUIDs.size() >0 ) {
				if ( activity.getActivityDocuments() == null )
						activity.setActivityDocuments(new HashSet<AmpActivityDocument>() );
				else
						activity.getActivityDocuments().clear();
				Iterator<String> iter	= UUIDs.iterator();

				while ( iter.hasNext() ) {
					String uuid					= iter.next();
					AmpActivityDocument doc		= new AmpActivityDocument();
					doc.setUuid(uuid);
					doc.setDocumentType( ActivityDocumentsConstants.RELATED_DOCUMENTS );
					activity.getActivityDocuments().add(doc);
				}
			}
			SelectDocumentDM.clearContentRepositoryHashMap(request);
			/* END -Saving related documents into AmpActivity */

            if(eaForm.getProProjCost()==null){
                activity.setFunAmount(null);
                activity.setFunDate(null);
                activity.setCurrencyCode(null);
            }else{
                activity.setFunAmount(eaForm.getProProjCost().getFunAmountAsDouble());
                //check null for bolivia
                if (eaForm.getProProjCost().getFunDate()!=null){
                    activity.setFunDate(FormatHelper.parseDate(eaForm.getProProjCost().getFunDate()).getTime());
                }
                activity.setCurrencyCode(eaForm.getProProjCost().getCurrencyCode());
            }


          Set programs = new HashSet();
          List activityNPO = eaForm.getNationalPlanObjectivePrograms();
          List activityPP = eaForm.getPrimaryPrograms();
          List activitySP = eaForm.getSecondaryPrograms();
          if (activityNPO != null) {
                  programs.addAll(activityNPO);
          }
          if (activityPP != null) {
                  programs.addAll(activityPP);
          }
          if (activitySP != null) {
                  programs.addAll(activitySP);
          }
          activity.setActPrograms(programs);


			if (eaForm.getPageId() < 0 || eaForm.getPageId() > 1) {
				return mapping.findForward("index");
			}

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


				boolean titleFlag = false;
				boolean statusFlag = false;

//				if (eaForm.getAmpId() == null) {
					/*
					 * The logic for geerating the AMP-ID is as follows:
					 * 1. get default global country code
					 * 2. Get the maximum of the ampActivityId + 1, MAX_NUM
					 * 3. merge them
					 */
//					String ampId =
//						FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GLOBAL_DEFAULT_COUNTRY).toUpperCase();

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
//					long maxId = ActivityUtil.getActivityMaxId();
//					maxId++;
//					ampId += "/" + maxId;
//					eaForm.setAmpId(ampId);
//					User user=RequestUtils.getUser(request);
//					eaForm.setAmpId(ActivityUtil.generateAmpId(user,eaForm.getActivityId()));
//				}

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


                if(isStatusEnabled()){
					if (statId != null && statId.equals(new Long(0))) {
						errors.add("status", new ActionError(
								"error.aim.addActivity.statusMissing"));
						saveErrors(request, errors);
						statusFlag = true;
					}
                }

				if (titleFlag) {
					eaForm.setStep("1");
					return mapping.findForward("addActivity");
				}

                if(eaForm.getDraft()==null || !eaForm.getDraft().booleanValue()){
                    if (statusFlag) {
                        eaForm.setStep("1");
                        return mapping.findForward("addActivity");
                    }
                    eaForm.setPrimarySectorVisible(FeaturesUtil.isVisibleSectors("Primary", ampContext)?"true":"false");
                    eaForm.setSecondarySectorVisible(FeaturesUtil.isVisibleSectors("Secondary", ampContext)?"true":"false");
                    session.setAttribute("Primary Sector", eaForm.getPrimarySectorVisible());
                    session.setAttribute("Secondary Sector", eaForm.getSecondarySectorVisible());
                    if(isSectorEnabled()){
	                    if (eaForm.getActivitySectors() == null
	                        || eaForm.getActivitySectors().size() < 1) {
	                        errors.add("sector", new ActionError(
	                            "error.aim.addActivity.sectorEmpty"));
	                        saveErrors(request, errors);
	                        eaForm.setStep("1");
	                        return mapping.findForward("addActivity");
	                    }
	                    boolean secPer = false;
	                    int percent = 0, primaryPrc=0,secondaryPrc=0;
	                    boolean primary=false;
	                    boolean hasPrimarySectorsAdded=false, hasSecondarySectorsAdded=false;
	                    Iterator<ActivitySector> secPerItr = eaForm.getActivitySectors().iterator();
	                    while (secPerItr.hasNext()) {
	                        ActivitySector actSect = (ActivitySector) secPerItr.next();
	                        AmpClassificationConfiguration config=SectorUtil.getClassificationConfigById(actSect.getConfigId());
	                        if(config.isPrimary()){
	                            primary=true;
	                        }
	                        if("Primary".equals(config.getName())) hasPrimarySectorsAdded=true;
	                        if("Secondary".equals(config.getName())) hasSecondarySectorsAdded=true;
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
	                                if("Primary".equals(config.getName())) primaryPrc+=actSect.getSectorPercentage().intValue();
	                                if("Secondary".equals(config.getName())) secondaryPrc+=actSect.getSectorPercentage().intValue();
	                            	percent += actSect.getSectorPercentage().intValue();
	                            } catch (NumberFormatException nex) {
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
//	                    if(percent!=100 && percent >0)
//	                    {
//	                    	//if("Primary".equals(config))
//	                    	errors.add("sectorPercentageSumWrong", new ActionError("error.aim.addActivity.sectorPercentageSumWrong"));
//	                    	saveErrors(request, errors);
//                            eaForm.setStep("2");
//                            return mapping.findForward("addActivityStep2");
//	                    }
	                    if(primaryPrc!=100 && primaryPrc >0)
	                    {
	                    	//if("Primary".equals(config))
	                    	errors.add("primarySectorPercentageSumWrong", new ActionError("error.aim.addActivity.primarySectorPercentageSumWrong"));
	                    	saveErrors(request, errors);
                            eaForm.setStep("2");
                            return mapping.findForward("addActivityStep2");
	                    }
	                    if(secondaryPrc!=100 && secondaryPrc >0)
	                    {
	                    	//if("Primary".equals(config))
	                    	errors.add("secondarySectorPercentageSumWrong", new ActionError("error.aim.addActivity.secondarySectorPercentageSumWrong"));
	                    	saveErrors(request, errors);
                            eaForm.setStep("2");
                            return mapping.findForward("addActivityStep2");
	                    }
	                    // no primary sectors added
	                    if (isPrimarySectorEnabled() && !hasPrimarySectorsAdded) {
	                        errors.add("noPrimarySectorsAdded",
	                                   new ActionError("error.aim.addActivity.noPrimarySectorsAdded"));
	                        saveErrors(request, errors);
	                        logger.debug("no Primary Sectors Added");
	                        eaForm.setStep("2");
	                        return mapping.findForward("addActivityStep2");
	                    }
	                    if (isSecondarySectorEnabled() && !hasSecondarySectorsAdded) {
	                        errors.add("noSecondarySectorsAdded",
	                                   new ActionError("error.aim.addActivity.noSecondarySectorsAdded"));
	                        saveErrors(request, errors);
	                        logger.debug("no Secondary Sectors Added");
	                        eaForm.setStep("2");
	                        return mapping.findForward("addActivityStep2");
	                    }

                    }

                    if(eaForm.getSelectedLocs() != null && eaForm.getSelectedLocs().size()>0){
    					Iterator<Location> itr = eaForm.getSelectedLocs().iterator();
    					Double totalPercentage = 0d;
    					while (itr.hasNext()) {
    						Location loc = itr.next();
    						Double percentage=FormatHelper.parseDouble(loc.getPercent());
							totalPercentage += percentage;
    					}
    					//Checks if it's 100%
    					
    					if (totalPercentage != 100 && FeaturesUtil.isVisibleField("Regional Percentage", ampContext)) {
	                        errors.add("locationPercentageSumWrong",
	                                   new ActionError("error.aim.addActivity.locationPercentageSumWrong"));
	                        saveErrors(request, errors);
    						return mapping.findForward("addActivityStep2");
    					}
                    }


                    if (eaForm.getNationalPlanObjectivePrograms() != null
    						&& eaForm.getNationalPlanObjectivePrograms().size() > 0) {
                        	Iterator<AmpActivityProgram> npoIt = eaForm.getNationalPlanObjectivePrograms().iterator();
        					Double totalPercentage = 0d;
        					while (npoIt.hasNext()) {
        						AmpActivityProgram activityProgram = npoIt.next();
        						totalPercentage += activityProgram.getProgramPercentage();
        					}
        					if (totalPercentage != 100) {
    	                        errors.add("nationalPlanProgramsPercentageSumWrong",
    	                                   new ActionError("error.aim.addActivity.nationalPlanProgramsPercentageSumWrong"));
    	                        saveErrors(request, errors);
        						return mapping.findForward("addActivityStep2");
        					}
                    }

                    if (eaForm.getPrimaryPrograms()!= null
						&& eaForm.getPrimaryPrograms().size() > 0) {
                    	Iterator<AmpActivityProgram> ppIt = eaForm.getPrimaryPrograms().iterator();
    					Double totalPercentage = 0d;
    					while (ppIt.hasNext()) {
    						AmpActivityProgram activityProgram = ppIt.next();
    						totalPercentage += activityProgram.getProgramPercentage();
    					}
    					if (totalPercentage != 100) {
	                        errors.add("primaryProgramsPercentageSumWrong",
	                                   new ActionError("error.aim.addActivity.primaryProgramsPercentageSumWrong"));
	                        saveErrors(request, errors);
    						return mapping.findForward("addActivityStep2");
    					}
                    }

                    if (eaForm.getSecondaryPrograms()!= null
    						&& eaForm.getSecondaryPrograms().size() > 0) {
                        	Iterator<AmpActivityProgram> spIt = eaForm.getSecondaryPrograms().iterator();
        					Double totalPercentage = 0d;
        					while (spIt.hasNext()) {
        						AmpActivityProgram activityProgram = spIt.next();
        						totalPercentage += activityProgram.getProgramPercentage();
        					}
        					if (totalPercentage != 100) {
    	                        errors.add("secondaryProgramsPercentageSumWrong",
    	                                   new ActionError("error.aim.addActivity.secondaryProgramsPercentageSumWrong"));
    	                        saveErrors(request, errors);
        						return mapping.findForward("addActivityStep2");
        					}
                    }

                }
				// end of Modified code

                if(eaForm.getAhsurvey()!=null) DbUtil.updateSurvey(eaForm.getAhsurvey());
                if(eaForm.getAmpSurveyId()!=null) DbUtil.saveSurveyResponses(eaForm.getAmpSurveyId(), eaForm.getIndicators());

                if(eaForm.getProProjCost()==null){
                    activity.setFunAmount(null);
                    activity.setFunDate(null);
                    activity.setCurrencyCode(null);
                }else{
                    activity.setFunAmount(eaForm.getProProjCost().getFunAmountAsDouble());
                    //check for null on bolivia
                    if(eaForm.getProProjCost().getFunDate()!=null){
                	activity.setFunDate(FormatHelper.parseDate(eaForm.getProProjCost().getFunDate()).getTime());
                    }
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
				activity.setGovAgreementNumber(eaForm.getGovAgreementNumber());


				activity.setJointCriteria(eaForm.getJointCriteria());
				activity.setHumanitarianAid(eaForm.getHumanitarianAid());
				
				if (eaForm.getDescription() == null
						|| eaForm.getDescription().trim().length() == 0) {
					activity.setDescription(new String(" "));
				} else {
					activity.setDescription(eaForm.getDescription());
				}
				if (eaForm.getLessonsLearned() == null
						|| eaForm.getLessonsLearned().trim().length() == 0) {
					activity.setLessonsLearned(new String(" "));
				} else {
					activity.setLessonsLearned(eaForm.getLessonsLearned());
				}

				if (eaForm.getProjectImpact() == null
						|| eaForm.getProjectImpact().trim().length() == 0) {
					activity.setProjectImpact(new String(" "));
				} else {
					activity.setProjectImpact(eaForm.getProjectImpact());
				}

				if (eaForm.getActivitySummary() == null
						|| eaForm.getActivitySummary().trim().length() == 0) {
					activity.setActivitySummary(new String(" "));
				} else {
					activity.setActivitySummary(eaForm.getActivitySummary());
				}

				if (eaForm.getContractingArrangements() == null
						|| eaForm.getContractingArrangements().trim().length() == 0) {
					activity.setContractingArrangements(new String(" "));
				} else {
					activity.setContractingArrangements(eaForm.getContractingArrangements());
				}


				if (eaForm.getCondSeq() == null
						|| eaForm.getCondSeq().trim().length() == 0) {
					activity.setCondSeq(new String(" "));
				} else {
					activity.setCondSeq(eaForm.getCondSeq());
				}


				if (eaForm.getLinkedActivities() == null
						|| eaForm.getLinkedActivities().trim().length() == 0) {
					activity.setLinkedActivities(new String(" "));
				} else {
					activity.setLinkedActivities(eaForm.getLinkedActivities());
				}

				if (eaForm.getConditionality() == null
						|| eaForm.getConditionality().trim().length() == 0) {
					activity.setConditionality(new String(" "));
				} else {
					activity.setConditionality(eaForm.getConditionality());
				}

				if (eaForm.getProjectManagement() == null
						|| eaForm.getProjectManagement().trim().length() == 0) {
					activity.setProjectManagement(new String(" "));
				} else {
					activity.setProjectManagement(eaForm.getProjectManagement());
				}


				if (eaForm.getContractDetails() == null
						|| eaForm.getContractDetails().trim().length() == 0) {
					activity.setContractDetails(new String(" "));
				} else {
					activity.setContractDetails(eaForm.getContractDetails());
				}

				if (eaForm.getEqualOpportunity() == null
						|| eaForm.getEqualOpportunity().trim().length() == 0) {
					activity.setEqualOpportunity(new String(" "));
				} else {
					activity.setEqualOpportunity(eaForm.getEqualOpportunity());
				}

				if (eaForm.getEnvironment() == null
						|| eaForm.getEnvironment().trim().length() == 0) {
					activity.setEnvironment(new String(" "));
				} else {
					activity.setEnvironment(eaForm.getEnvironment());
				}

				if (eaForm.getMinorities() == null
						|| eaForm.getMinorities().trim().length() == 0) {
					activity.setMinorities(new String(" "));
				} else {
					activity.setMinorities(eaForm.getMinorities());
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

				if (eaForm.getActivityCloseDates() != null && eaForm.getActivityCloseDates().size()>0) {
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

				activity.setPrjCoFirstName(eaForm.getPrjCoFirstName());
				activity.setPrjCoLastName(eaForm.getPrjCoLastName());
				activity.setPrjCoEmail(eaForm.getPrjCoEmail());
				activity.setPrjCoTitle(eaForm.getPrjCoTitle());
				activity.setPrjCoOrganization(eaForm.getPrjCoOrganization());
				activity.setPrjCoPhoneNumber(eaForm.getPrjCoPhoneNumber());
				activity.setPrjCoFaxNumber(eaForm.getPrjCoFaxNumber());

				activity.setSecMiCntFirstName(eaForm.getSecMiCntFirstName());
				activity.setSecMiCntLastName(eaForm.getSecMiCntLastName());
				activity.setSecMiCntEmail(eaForm.getSecMiCntEmail());
				activity.setSecMiCntTitle(eaForm.getSecMiCntTitle());
				activity.setSecMiCntOrganization(eaForm.getSecMiCntOrganization());
				activity.setSecMiCntPhoneNumber(eaForm.getSecMiCntPhoneNumber());
				activity.setSecMiCntFaxNumber(eaForm.getSecMiCntFaxNumber());

                activity.setDraft(eaForm.getDraft());

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

				//save reference docs
				//Collection<AmpActivityReferenceDoc> activityRefDocs=ActivityUtil.getReferenceDocumentsFor(eaForm.getActivityId());
	        	//create map where keys are category value ids.
	        	//Map<Long, AmpActivityReferenceDoc> categoryRefDocMap=
	        	//	new ActivityUtil.CategoryIdRefDocMapBuilder().createMap(activityRefDocs);

				List formRefDocs=eaForm.getReferenceDocs();
	        	Set<AmpActivityReferenceDoc> resultRefDocs=new HashSet<AmpActivityReferenceDoc>();
	        	if(formRefDocs!=null && !formRefDocs.isEmpty())
				for (Iterator refIter = formRefDocs.iterator(); refIter.hasNext();) {
					ReferenceDoc refDoc = (ReferenceDoc) refIter.next();
					if(ArrayUtils.contains(eaForm.getAllReferenceDocNameIds(), refDoc.getCategoryValueId())){
						AmpActivityReferenceDoc dbRefDoc=null;//categoryRefDocMap.get(refDoc.getCategoryValueId());
						if (refDoc.getChecked() == true){
							dbRefDoc=new AmpActivityReferenceDoc();
							dbRefDoc.setCreated(new Date());
							AmpCategoryValue catVal=CategoryManagerUtil.getAmpCategoryValueFromDb(refDoc.getCategoryValueId());
							dbRefDoc.setCategoryValue(catVal);
							dbRefDoc.setActivity(activity);
						//}
						dbRefDoc.setActivity(activity);
						dbRefDoc.setComment(refDoc.getComment());
						dbRefDoc.setLastEdited(new Date());
						resultRefDocs.add(dbRefDoc);

					}else{
						dbRefDoc=null;
						resultRefDocs.add(dbRefDoc);
						}
					}
				}
				activity.setReferenceDocs(resultRefDocs);


				// set the sectors
				if (eaForm.getActivitySectors() != null && eaForm.getActivitySectors().size()>0) {
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
                                                        amps.setClassificationConfig(SectorUtil.getClassificationConfigById(actSect.getConfigId()));
							sectors.add(amps);
						}
					}
					activity.setSectors(sectors);
				}

				// set the sectors
				if (eaForm.getActivityComponentes() != null) {
					Set componentes = new HashSet();
					if (eaForm.getActivityComponentes() != null && eaForm.getActivityComponentes().size()>0) {
						Iterator itr = eaForm.getActivityComponentes().iterator();
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
							AmpActivityComponente ampc = new AmpActivityComponente();
							ampc.setActivity(activity);
							if (sectorId != null && (!sectorId.equals(new Long(-1))))
								ampc.setSector(SectorUtil.getAmpSector(sectorId));
							ampc.setPercentage(new Float(actSect.getSectorPercentage()));
							componentes.add(ampc);
						}
					}
					activity.setComponentes(componentes);
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
				if (eaForm.getFundingOrganizations() != null && eaForm.getFundingOrganizations().size()>0) { // funding

																// organizations
					AmpRole role = DbUtil.getAmpRole(Constants.FUNDING_AGENCY);
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
				if (eaForm.getExecutingAgencies() != null && eaForm.getExecutingAgencies().size()>0) { // executing
																// agencies
					AmpRole role = DbUtil
							.getAmpRole(Constants.EXECUTING_AGENCY);
					Iterator itr = eaForm.getExecutingAgencies().iterator();
					while (itr.hasNext()) {
						AmpOrganisation tmp= (AmpOrganisation) itr.next();
						AmpOrgRole ampOrgRole=new AmpOrgRole();
						ampOrgRole.setActivity(activity);
						ampOrgRole.setRole(role);
						ampOrgRole.setOrganisation(tmp);
						orgRole.add(ampOrgRole);
					}
				}
				if (eaForm.getImpAgencies() != null && eaForm.getImpAgencies().size()>0) { // implementing agencies
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
				if (eaForm.getBenAgencies() != null && eaForm.getBenAgencies().size()>0) { // beneficiary agencies
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
				if (eaForm.getConAgencies() != null && eaForm.getConAgencies().size()>0) { // contracting agencies
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
				if (eaForm.getRegGroups() != null && eaForm.getRegGroups().size()>0) { // regional groups
					AmpRole role = DbUtil
							.getAmpRole(Constants.REGIONAL_GROUP);
					Iterator itr = eaForm.getRegGroups().iterator();
					while (itr.hasNext()) {
						AmpOrganisation org = (AmpOrganisation) itr.next();
						AmpOrgRole ampOrgRole = new AmpOrgRole();
						ampOrgRole.setActivity(activity);
						ampOrgRole.setRole(role);
						ampOrgRole.setOrganisation(org);
						orgRole.add(ampOrgRole);
					}
				}
				if (eaForm.getSectGroups() != null && eaForm.getSectGroups().size()>0) { // sector groups
					AmpRole role = DbUtil
							.getAmpRole(Constants.SECTOR_GROUP);
					Iterator itr = eaForm.getSectGroups().iterator();
					while (itr.hasNext()) {
						AmpOrganisation org = (AmpOrganisation) itr.next();
						AmpOrgRole ampOrgRole = new AmpOrgRole();
						ampOrgRole.setActivity(activity);
						ampOrgRole.setRole(role);
						ampOrgRole.setOrganisation(org);
						orgRole.add(ampOrgRole);
					}
				}
				if (eaForm.getReportingOrgs() != null && eaForm.getReportingOrgs().size()>0) { // Reporting
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
				if (eaForm.getRespOrganisations() != null && eaForm.getRespOrganisations().size()>0) { // Responsible Organisation
					AmpRole role = DbUtil
							.getAmpRole(Constants.RESPONSIBLE_ORGANISATION);
					Iterator itr = eaForm.getRespOrganisations().iterator();
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
				if (eaForm.getSelectedLocs() != null && eaForm.getSelectedLocs().size()>0) {
					Set<AmpActivityLocation> locations = new HashSet<AmpActivityLocation>();
					Iterator<Location> itr = eaForm.getSelectedLocs().iterator();
					while (itr.hasNext()) {
						Location loc = itr.next();
                                                String countryIso=loc.getNewCountryId();
						AmpLocation ampLoc = LocationUtil.getAmpLocation(countryIso, loc.getRegionId(), loc.getZoneId(), loc.getWoredaId());

						if (ampLoc == null) {
							ampLoc = new AmpLocation();
							ampLoc.setCountry(loc.getCountry());
                                                        if(countryIso!=null){
							ampLoc.setDgCountry(DbUtil.getDgCountry(loc.getNewCountryId()));
                                                        }
							ampLoc.setRegion(loc.getRegion());
							ampLoc.setAmpRegion(LocationUtil.getAmpRegion(loc.getRegionId()));
							ampLoc.setAmpZone(LocationUtil.getAmpZone(loc.getZoneId()));
							ampLoc.setAmpWoreda(LocationUtil.getAmpWoreda(loc.getWoredaId()));
							ampLoc.setDescription(new String(" "));
							DbUtil.add(ampLoc);
						}

						//AMP-2250
						AmpActivityLocation actLoc=new AmpActivityLocation();
						actLoc.setActivity(activity);//activity);
						actLoc.getActivity().setAmpActivityId(eaForm.getActivityId());
						actLoc.setLocation(ampLoc);
						Double percent=FormatHelper.parseDouble(loc.getPercent());
                                                if(percent==null){
						percent=new Double(0);
                                                }
                                                actLoc.setLocationPercentage(percent.floatValue());
						locations.add(actLoc);
						//locations.add(ampLoc);
						//AMP-2250
					}
					activity.setLocations(locations);
				}
				if(eaForm.getSelectedLocs() != null && eaForm.getSelectedLocs().size() == 1){
					Iterator<Location> itr = eaForm.getSelectedLocs().iterator();
					while (itr.hasNext()) {
						Location loc = itr.next();
						if(loc.getRegion().equals("") && eaForm.getRegionalFundings()!=null){
							eaForm.getRegionalFundings().clear();
				 	      }
					 }
				}else if (eaForm.getSelectedLocs()==null || eaForm.getSelectedLocs().size() == 0){
					if (eaForm.getRegionalFundings()!=null) eaForm.getRegionalFundings().clear();
				}


				if(eaForm.getCosts()!=null && eaForm.getCosts().size()>0) {
					Set costs=new HashSet();
					Iterator i=eaForm.getCosts().iterator();
					while (i.hasNext()) {
						EUActivity element = (EUActivity) i.next();
						element.setActivity(activity);
						element.setId(null);
						if(element.getContributions()!=null){
							Iterator ii=element.getContributions().iterator();
							while (ii.hasNext()) {
								EUActivityContribution element2 = (EUActivityContribution) ii.next();
								element2.setId(null);
							}
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
				if (eaForm.getDocumentList() != null && eaForm.getDocumentList() .size()>0) {
					Iterator itr = eaForm.getDocumentList().iterator();
					while (itr.hasNext()) {
						RelatedLinks rl = (RelatedLinks) itr.next();
						relatedLinks.add(rl);
					}
				}
				if (eaForm.getLinksList() != null && eaForm.getLinksList().size()>0) {
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
						if(orgProjId[i] != null){
							actInternalId.setAmpActivity(activity);
						actInternalId.setOrganisation(DbUtil
								.getOrganisation(orgProjId[i].getOrganisation().getAmpOrgId()));
						actInternalId
								.setInternalId(orgProjId[i].getProjectId());
						internalIds.add(actInternalId);
					}
				}
			}
				//activity.getInternalIds().clear();
				activity.setInternalIds(internalIds);

				//set components
				proccessComponents(tempComp, eaForm, activity);

				//set funding and funding details
				Set fundings = new HashSet();
				if (eaForm.getFundingOrganizations() != null && eaForm.getFundingOrganizations().size()>0) {
					Iterator itr1 = eaForm.getFundingOrganizations().iterator();
					while (itr1.hasNext()) {
						FundingOrganization fOrg = (FundingOrganization) itr1.next();
						//add fundings
						if (fOrg.getFundings() != null && fOrg.getFundings().size()>0) {
							Iterator itr2 = fOrg.getFundings().iterator();
							while (itr2.hasNext()) {
								Funding fund = (Funding) itr2.next();
								AmpFunding ampFunding = new AmpFunding();
								ampFunding.setActive(fOrg.getFundingActive());
								if("unchecked".equals(fOrg.getDelegatedCooperationString()) || fOrg.getDelegatedCooperation()==null)
									ampFunding.setDelegatedCooperation(false);
								else
									ampFunding.setDelegatedCooperation(true);
								if("unchecked".equals(fOrg.getDelegatedPartnerString()) || fOrg.getDelegatedPartner()==null)
									ampFunding.setDelegatedPartner(false);
								else
									ampFunding.setDelegatedPartner(true);

								ampFunding.setAmpDonorOrgId(DbUtil.getOrganisation(fOrg.getAmpOrgId()));
								ampFunding.setFinancingId(fund.getOrgFundingId());
								/*
								 * if (fund.getSignatureDate() != null)
								 * ampFunding.setSignatureDate(DateConversion
								 * .getDate(fund.getSignatureDate()));
								 */
								//ampFunding.setModalityId(fund.getModality());
								ampFunding.setFinancingInstrument(fund.getFinancingInstrument());
								if (fund.getConditions() != null
										&& fund.getConditions().trim().length() != 0) {
									ampFunding.setConditions(fund.getConditions());
								} else {
									ampFunding.setConditions(new String(" "));
								}
								ampFunding.setComments(new String(" "));
								/*ampFunding.setAmpTermsAssistId(fund.getAmpTermsAssist());*/
								ampFunding.setTypeOfAssistance( fund.getTypeOfAssistance() );
								ampFunding.setAmpActivityId(activity);

								// add funding details for each funding
								Set fundDeatils = new HashSet();
								if (fund.getFundingDetails() != null) {
									Iterator itr3 = fund.getFundingDetails().iterator();
									while (itr3.hasNext()) {
										FundingDetail fundDet = (FundingDetail) itr3.next();
										AmpFundingDetail ampFundDet = new AmpFundingDetail();
										ampFundDet.setTransactionType(new Integer(fundDet.getTransactionType()));
										// ampFundDet.setPerspectiveId(DbUtil.getPerspective(Constants.MOFED));
										ampFundDet.setAdjustmentType(new Integer(fundDet.getAdjustmentType()));
										ampFundDet.setTransactionDate(DateConversion.getDate(fundDet
																.getTransactionDate()));
										boolean useFixedRate = false;
										if (fundDet.getTransactionType() == Constants.COMMITMENT) {
											if (fundDet.isUseFixedRate()
													&& fundDet.getFixedExchangeRate().doubleValue() > 0
													&& fundDet.getFixedExchangeRate().doubleValue() != 1) {
												useFixedRate = true;
											}
										}

										if (!useFixedRate) {
											Double transAmt = new Double(
													FormatHelper.parseDouble(fundDet.getTransactionAmount()));
											ampFundDet.setTransactionAmount(transAmt);
											ampFundDet.setAmpCurrencyId(CurrencyUtil.getCurrencyByCode(fundDet.getCurrencyCode()));
											ampFundDet.setFixedExchangeRate(null);
										} else {
											// Use the fixed exchange rate
											double transAmt = FormatHelper.parseDouble(fundDet.getTransactionAmount());
											Date trDate = DateConversion.getDate(fundDet.getTransactionDate());
											// double frmExRt =
											// CurrencyUtil.getExchangeRate(fundDet.getCurrencyCode(),1,trDate);
											// double amt =
											// CurrencyWorker.convert1(transAmt,
											// frmExRt,1);
											// amt *=
											// fundDet.getFixedExchangeRate();
											ampFundDet.setTransactionAmount(new Double(transAmt));
											ampFundDet.setFixedExchangeRate(fundDet.getFixedExchangeRate());
											ampFundDet.setAmpCurrencyId(CurrencyUtil.getCurrencyByCode(fundDet
																	.getCurrencyCode()));
										}
										ampFundDet.setAmpFundingId(ampFunding);
										if (fundDet.getTransactionType() == Constants.EXPENDITURE) {
											ampFundDet.setExpCategory(fundDet.getClassification());
										}
										ampFundDet.setDisbOrderId(fundDet.getDisbOrderId());
										ampFundDet.setContract(fundDet.getContract());
										fundDeatils.add(ampFundDet);
									}
								}
								ampFunding.setFundingDetails(fundDeatils);
								this.saveMTEFProjections(fund, ampFunding);
								fundings.add(ampFunding);
							}
						}
						//AMP-2947 Funding amount should not be mandatory when adding a Funding Agency
						else{
							Funding fund = new Funding();
							AmpFunding ampFunding = new AmpFunding();
							ampFunding.setActive(fOrg.getFundingActive());
							if("unchecked".equals(fOrg.getDelegatedCooperationString()) || fOrg.getDelegatedCooperation()==null)
								ampFunding.setDelegatedCooperation(false);
							else
								ampFunding.setDelegatedCooperation(true);
							if("unchecked".equals(fOrg.getDelegatedPartnerString()) || fOrg.getDelegatedPartner()==null)
								ampFunding.setDelegatedPartner(false);
							else
								ampFunding.setDelegatedPartner(true);

							ampFunding.setAmpDonorOrgId(DbUtil.getOrganisation(fOrg.getAmpOrgId()));
							ampFunding.setFinancingId(fund.getOrgFundingId());

							ampFunding.setFinancingInstrument(fund.getFinancingInstrument());
							if (fund.getConditions() != null
									&& fund.getConditions().trim().length() != 0) {
								ampFunding.setConditions(fund.getConditions());
							} else {
								ampFunding.setConditions(new String(" "));
							}
							ampFunding.setComments(new String(" "));
							ampFunding.setTypeOfAssistance( fund.getTypeOfAssistance() );
							ampFunding.setAmpActivityId(activity);
							this.saveMTEFProjections(fund, ampFunding);
							fundings.add(ampFunding);
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
								Iterator tmpItr=null;
								if(eaForm.getCurrencies()!=null){
									tmpItr = eaForm.getCurrencies()
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
								}

								boolean regionFlag=false;

								if(eaForm.getFundingRegions()!=null && eaForm.getFundingRegions().size()>0){
									tmpItr = eaForm.getFundingRegions().iterator();
									while (tmpItr.hasNext()) {
										AmpRegion reg = (AmpRegion) tmpItr.next();
										if (reg.getAmpRegionId().equals(
												regFund.getRegionId())) {
											ampRegFund.setRegion(reg);
                                                                                        regionFlag=true;
											break;
										}
									}
								}
								ampRegFund.setTransactionAmount(new Double(
									FormatHelper.parseDouble(fd
												.getTransactionAmount())));
								ampRegFund.setTransactionDate(DateConversion
										.getDate(fd.getTransactionDate()));
								ampRegFund.setAdjustmentType(new Integer(fd
										.getAdjustmentType()));
                                                                if(regionFlag){
								regFundings.add(ampRegFund);
                                                                }
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
								Iterator tmpItr=null;
								if(eaForm.getCurrencies()!=null){
									 tmpItr = eaForm.getCurrencies()
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
								}
                                                                boolean regionFlag=false;
								if(eaForm.getFundingRegions()!=null && eaForm.getFundingRegions().size()>0){
									tmpItr = eaForm.getFundingRegions().iterator();
									while (tmpItr.hasNext()) {
										AmpRegion reg = (AmpRegion) tmpItr.next();
										if (reg.getAmpRegionId().equals(
												regFund.getRegionId())) {
											ampRegFund.setRegion(reg);
                                                                                        regionFlag=true;
											break;
										}
									}
								}

								ampRegFund.setTransactionAmount(new Double(
									FormatHelper.parseDouble(fd
												.getTransactionAmount())));
								ampRegFund.setTransactionDate(DateConversion
										.getDate(fd.getTransactionDate()));
								ampRegFund.setAdjustmentType(new Integer(fd
										.getAdjustmentType()));
                                                                if(regionFlag){
								regFundings.add(ampRegFund);
                                                                }
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
								Iterator tmpItr=null;
								if(eaForm.getCurrencies()!=null){
									tmpItr = eaForm.getCurrencies()
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
								}
                                                                boolean regionFlag=false;
								if( eaForm.getFundingRegions()!=null && eaForm.getFundingRegions().size()>0){
									tmpItr = eaForm.getFundingRegions().iterator();
									while (tmpItr.hasNext()) {
										AmpRegion reg = (AmpRegion) tmpItr.next();
										if (reg.getAmpRegionId().equals(
												regFund.getRegionId())) {
											ampRegFund.setRegion(reg);
                                                                                        regionFlag=true;
											break;
										}
									}
								}

								ampRegFund.setTransactionAmount(new Double(
									FormatHelper.parseDouble(fd
												.getTransactionAmount())));
								ampRegFund.setTransactionDate(DateConversion
										.getDate(fd.getTransactionDate()));
								ampRegFund.setAdjustmentType(new Integer(fd
										.getAdjustmentType()));
                                                                if(regionFlag){
								regFundings.add(ampRegFund);
                                                                }
							}

						}
					}
				}

				// Delete the following code
				if(regFundings!=null && regFundings.size() >0){
					Iterator tmp = regFundings.iterator();
					while (tmp.hasNext()) {
						AmpRegionalFunding rf = (AmpRegionalFunding) tmp.next();
						logger.debug("Regional Fundings :" + rf.getAdjustmentType()
								+ " " + rf.getTransactionAmount());
					}
				}


				activity.setRegionalFundings(regFundings);

				if (eaForm.getIssues() != null && eaForm.getIssues().size() > 0) {
					Set issueSet = new HashSet();
					for (int i = 0; i < eaForm.getIssues().size(); i++) {
						Issues issue = (Issues) eaForm.getIssues().get(i);
						AmpIssues ampIssue = new AmpIssues();
						ampIssue.setActivity(activity);
						ampIssue.setName(issue.getName());
						if (issue.getIssueDate()!=null){
							ampIssue.setIssueDate(FormatHelper.parseDate(issue.getIssueDate()).getTime());
						}
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

				//this fields are used to determine receivers of approvals(Messaging System)
				String oldActivityApprovalStatus="";
				String editedActivityApprovalStatus="";

				if (eaForm.isEditAct()) {
					//AmpActivity act = ActivityUtil.getActivityByName(eaForm.getTitle());
					// Setting approval status of activity
					activity.setApprovalStatus(eaForm.getApprovalStatus());
					activity.setApprovalDate(eaForm.getApprovalDate());
					activity.setApprovedBy(eaForm.getApprovedBy());
				
					
					//AMP-3464
					//if an approved activity is edited and the appsettins is set to newOnly then the activity
					//doesn't need to be approved again!
					AmpActivity aAct = ActivityUtil.getAmpActivity(eaForm.getActivityId());
					oldActivityApprovalStatus=aAct.getApprovalStatus();
					if( Constants.STARTED_STATUS.equals(aAct.getApprovalStatus()) ){
                        activity.setApprovalStatus(Constants.STARTED_STATUS);
                    }
//
//					if("newOnly".equals(tm.getAppSettings().getValidation()) &&
//                       Constants.APPROVED_STATUS.equals(aAct.getApprovalStatus())){
//                        activity.setApprovalStatus(Constants.APPROVED_STATUS);
//                    }
					//AMP-3464
					String s=DbUtil.getTeamAppSettingsMemberNotNull(aAct.getTeam().getAmpTeamId()).getValidation();
					if("newOnly".equals(DbUtil.getTeamAppSettingsMemberNotNull(aAct.getTeam().getAmpTeamId()).getValidation()) && Constants.APPROVED_STATUS.equals(aAct.getApprovalStatus()))
						{
	                        activity.setApprovalStatus(Constants.APPROVED_STATUS);
	                    }

					if(tm.getTeamHead()){
                        activity.setApprovalStatus(Constants.APPROVED_STATUS);
                    }
					if(eaForm.getDraft()==true){
						if(tm.getTeamHead()){
                            activity.setApprovalStatus(Constants.APPROVED_STATUS);
                        }else{
                            activity.setApprovalStatus(aAct.getApprovalStatus());
                        }
					}

					activity.setActivityCreator(eaForm.getCreatedBy());
                    List<String> auditTrail = AuditLoggerUtil.generateLogs(
												activity, eaForm.getActivityId());
					// update an existing activity
					actId = ActivityUtil.saveActivity(activity, eaForm.getActivityId(),
							true, eaForm.getCommentsCol(), eaForm
									.isSerializeFlag(), field, relatedLinks, tm
									.getMemberId(), eaForm.getIndicatorsME(),tempComp,eaForm.getContracts());
					//update lucene index
					LuceneUtil.addUpdateActivity(request, true, actId);
					//for logging the activity
					AuditLoggerUtil.logActivityUpdate(session, request,
												activity, auditTrail);

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
					actId = ActivityUtil.saveActivity(activity, null, false, eaForm.getCommentsCol(), eaForm.isSerializeFlag(),
	                        field, relatedLinks,tm.getMemberId() , eaForm.getIndicatorsME(), tempComp, eaForm.getContracts());
					//update lucene index
					LuceneUtil.addUpdateActivity(request, false, actId);
					//for logging the activity
					AuditLoggerUtil.logObject(session, request,activity,"add");
				}

			//If we're adding an activity, create system/admin message
			if(!createdAsDraft) {
				ActivitySaveTrigger ast=new ActivitySaveTrigger(activity);
			}
                     
            boolean needApproval=false;
            boolean approved=false;
            //this field is used to define if "activity approved" approval has to be created
            boolean needNewAppForApproved=true;

            AmpActivity myActivity=ActivityUtil.loadActivity(actId);
            editedActivityApprovalStatus=myActivity.getApprovalStatus();

            if(eaForm.isEditAct()){
                if(tm.getTeamHead()){
                	/**
                	 * we have two cases: team leader approves activity or edits already approved one. if so(second situation), then no messages should be
                	 * created. If activity that team leader edited was not approved,this means that team leader now approved it and we need to send message to
                	 * creator/updater of activity to let him know his activity was approved.
                	 */
                	if(oldActivityApprovalStatus.equals(org.digijava.module.aim.helper.Constants.APPROVED_STATUS)){
                		needNewAppForApproved=false;
                	}
                    needApproval = false;
                    approved=true;
                }else if("newOnly".equals(tm.getAppSettings().getValidation())){
                	needNewAppForApproved=false;
                    approved=true;
                    needApproval = false;
                }else{
                    needApproval = true;
                    approved=false;
                }
            }else{
                if(tm.getTeamHead()){
                	needNewAppForApproved=false;
                    needApproval=false;
                    approved=true;
                }else{
                    approved=false;
                    needApproval=true;
                }
            }

            /**
             * I am doing this,because activity field holds old value of updatedBy and myActivity field holds new one.
             * If team leader approved activity,then myActivity has updatedBy=teamLeader and activity has previous updater if he/she exists.
             * So If updater exists message  should be sent to him, not team leader.
             * But if someone(not team leader) edited activity, then message should be sent to him.
             */
            if(tm.getTeamHead()){
            	myActivity=activity;
            	myActivity.setUpdatedBy(eaForm.getUpdatedBy());
            }

            //if workspace has no manager, then there is no need to approve any activity.
            AmpTeamMember teamMem=TeamMemberUtil.getAmpTeamMember(tm.getMemberId());
            if(teamMem.getAmpTeam().getTeamLead()!=null){
            	//check whether Activity is approved or needs Approval
            	if(approved && needNewAppForApproved&&!myActivity.getDraft()){
                    new ApprovedActivityTrigger(myActivity);
                }
                if(needApproval&&!myActivity.getDraft()){
                    new NotApprovedActivityTrigger(myActivity);
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
            boolean surveyFlag = false;

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
                        eaForm.setTeamLead(false);

			/* Clearing categories */
			eaForm.setAccessionInstrument(new Long(0));
			eaForm.setAcChapter(new Long(0));
			eaForm.setStatusId(new Long(0));
			eaForm.setGbsSbs(new Long(0));
            eaForm.setDraft(null);
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
				if(dirtyActivities!=null && dirtyActivities.size()>0){
					Iterator pItr = dirtyActivities.iterator();
					if (pItr.hasNext()) {
						AmpProject proj = (AmpProject) pItr.next();
						col.add(proj);
					}
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


	private boolean isSectorEnabled() {
 	    ServletContext ampContext = getServlet().getServletContext();
	    AmpTreeVisibility ampTreeVisibility=(AmpTreeVisibility) ampContext.getAttribute("ampTreeVisibility");
		AmpTemplatesVisibility currentTemplate=(AmpTemplatesVisibility) ampTreeVisibility.getRoot();
		if(currentTemplate!=null)
			if(currentTemplate.getFeatures()!=null)
				for(Iterator it=currentTemplate.getFeatures().iterator();it.hasNext();)
				{
					AmpFeaturesVisibility feature=(AmpFeaturesVisibility) it.next();
					if(feature.getName().compareTo("Sectors")==0)
					{
						return true;
					}

				}
		return false;
	}

	private boolean isStatusEnabled() {
 	   ServletContext ampContext = getServlet().getServletContext();

	   AmpTreeVisibility ampTreeVisibility=(AmpTreeVisibility) ampContext.getAttribute("ampTreeVisibility");

		AmpTemplatesVisibility currentTemplate=(AmpTemplatesVisibility) ampTreeVisibility.getRoot();
		if(currentTemplate!=null)
			if(currentTemplate.getFeatures()!=null)
				for(Iterator it=currentTemplate.getFeatures().iterator();it.hasNext();)
				{
					AmpFeaturesVisibility feature=(AmpFeaturesVisibility) it.next();
					if(feature.getName().compareTo("Status")==0)
					{
						return true;
					}

				}
		return false;
	}

	/**
	 * @param tempComp
	 * @param eaForm
	 * @param activity
	 */
	private void proccessComponents(Collection<Components<AmpComponentFunding>> tempComps, EditActivityForm eaForm, AmpActivity activity) {
		activity.setComponents(new HashSet());
		if (eaForm.getSelectedComponents() != null) {
			Iterator<Components<FundingDetail>> itr = eaForm.getSelectedComponents().iterator();
			while (itr.hasNext()) {
				Components<FundingDetail> comp = itr.next();
				Components<AmpComponentFunding> tempComp = new Components<AmpComponentFunding>();

				AmpComponent ampComp = null;
				Collection col = ComponentsUtil.getComponent(comp.getTitle());
				Iterator it = col.iterator();
				if(it.hasNext()){
					ampComp = (AmpComponent)it.next();
					activity.getComponents().add(ampComp);
				}

				if (comp.getCommitments() != null
						&& comp.getCommitments().size() > 0) {
					HashSet<AmpComponentFunding> temp = new HashSet<AmpComponentFunding>();
					Iterator<FundingDetail> commitmentsIter = comp.getCommitments().iterator();
					while (commitmentsIter.hasNext()) {
						FundingDetail fd = commitmentsIter.next();
						AmpComponentFunding ampCompFund = new AmpComponentFunding();
						ampCompFund.setActivity(activity);
						ampCompFund.setTransactionType(new Integer(
								Constants.COMMITMENT));
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

						ampCompFund.setAmpComponentFundingId(fd.getAmpComponentFundingId());
						ampCompFund.setReportingOrganization(null);
							ampCompFund.setTransactionAmount(FormatHelper.parseDouble(fd.getTransactionAmount()));

						ampCompFund.setTransactionDate(DateConversion
								.getDate(fd.getTransactionDate()));
						ampCompFund.setAdjustmentType(new Integer(fd
								.getAdjustmentType()));
						ampCompFund.setComponent(ampComp);
						temp.add(ampCompFund);
					}
					tempComp.setCommitments(temp);
				}


				if (comp.getDisbursements() != null
						&& comp.getDisbursements().size() > 0) {
					HashSet<AmpComponentFunding> temp = new HashSet<AmpComponentFunding>();
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

						ampCompFund.setAmpComponentFundingId(fd.getAmpComponentFundingId());

							ampCompFund.setTransactionAmount(new Double(
									FormatHelper.parseDouble(fd
											.getTransactionAmount())));

						ampCompFund.setTransactionDate(DateConversion
								.getDate(fd.getTransactionDate()));
						ampCompFund.setAdjustmentType(new Integer(fd
								.getAdjustmentType()));
						ampCompFund.setComponent(ampComp);
						temp.add(ampCompFund);
					}
					tempComp.setDisbursements(temp);
				}

				if (comp.getExpenditures() != null
						&& comp.getExpenditures().size() > 0) {
					HashSet<AmpComponentFunding> temp = new HashSet<AmpComponentFunding>();
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

						ampCompFund.setAmpComponentFundingId(fd.getAmpComponentFundingId());
						ampCompFund.setTransactionAmount(new Double(FormatHelper.parseDouble(fd.getTransactionAmount())));

						ampCompFund.setTransactionDate(DateConversion
								.getDate(fd.getTransactionDate()));
						ampCompFund.setAdjustmentType(new Integer(fd
								.getAdjustmentType()));
						ampCompFund.setComponent(ampComp);
						temp.add(ampCompFund);
					}
					tempComp.setExpenditures(temp);
				}

				// set physical progress

				if (comp.getPhyProgress() != null) {
					Set phyProgess = new HashSet();
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
                                                if(!phyProg.isNewProgress()){
                                                  ampPhyPerf.setAmpPpId(phyProg.
                                                      getPid());
                                                }
						ampPhyPerf.setReportingDate(DateConversion
								.getDate(phyProg.getReportingDate()));
						ampPhyPerf.setTitle(phyProg.getTitle());
						ampPhyPerf.setAmpActivityId(activity);
						ampPhyPerf.setComponent(ampComp);
						ampPhyPerf.setComments(" ");
						phyProgess.add(ampPhyPerf);
					}
					tempComp.setPhyProgress(phyProgess);
				}
                                tempComp.setComponentId(comp.getComponentId());
                                tempComp.setType_Id(comp.getType_Id());

                                tempComps.add(tempComp);
			}
		}
	}

	private void saveMTEFProjections (Funding fund, AmpFunding ampFunding) {
		if ( ampFunding.getMtefProjections() != null ) {
			ampFunding.getMtefProjections().clear();
		}
		else
			ampFunding.setMtefProjections(new HashSet<AmpFundingMTEFProjection> ());
		if(fund.getMtefProjections()!=null)
		{
			Iterator mtefItr=fund.getMtefProjections().iterator();
			while (mtefItr.hasNext())
			{
				MTEFProjection mtef=(MTEFProjection)mtefItr.next();
				AmpFundingMTEFProjection ampmtef=new AmpFundingMTEFProjection();
				ampmtef.setAmount(FormatHelper.parseDouble(mtef.getAmount()));

				ampmtef.setAmpFunding(ampFunding);
				ampmtef.setAmpCurrency(CurrencyUtil.getCurrencyByCode(mtef.getCurrencyCode()));
				ampmtef.setProjected( CategoryManagerUtil.getAmpCategoryValueFromDb(mtef.getProjected()) );
				ampmtef.setProjectionDate( DateConversion.getDate(mtef.getProjectionDate()) );

				ampFunding.getMtefProjections().add(ampmtef);
			}
		}
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
	  private boolean isSecondarySectorEnabled() {
	 	    ServletContext ampContext = getServlet().getServletContext();
		    AmpTreeVisibility ampTreeVisibility=(AmpTreeVisibility) ampContext.getAttribute("ampTreeVisibility");
			AmpTemplatesVisibility currentTemplate=(AmpTemplatesVisibility) ampTreeVisibility.getRoot();
			if(currentTemplate!=null)
				if(currentTemplate.getFeatures()!=null)
					for(Iterator it=currentTemplate.getFields().iterator();it.hasNext();)
					{
						AmpFieldsVisibility field=(AmpFieldsVisibility) it.next();
						if(field.getName().compareTo("Secondary Sector")==0)
						{
							return true;
						}

					}
			return false;
	  }

}
