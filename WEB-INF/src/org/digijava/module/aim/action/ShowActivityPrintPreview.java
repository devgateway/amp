/*
 * ShowActivityPrintPreview.java
 * Created : 24-May-2005
 */

package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.utils.AmpCollectionUtils;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityClosingDates;
import org.digijava.module.aim.dbentity.AmpActivityComponente;
import org.digijava.module.aim.dbentity.AmpActivityInternalId;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpActivityReferenceDoc;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpActor;
import org.digijava.module.aim.dbentity.AmpCategoryValue;
import org.digijava.module.aim.dbentity.AmpComments;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpComponentFunding;
import org.digijava.module.aim.dbentity.AmpField;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.dbentity.AmpIssues;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpMeasure;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpPhysicalPerformance;
import org.digijava.module.aim.dbentity.AmpRegionalFunding;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.CMSContentItem;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.form.ProposedProjCost;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.CategoryConstants;
import org.digijava.module.aim.helper.CategoryManagerUtil;
import org.digijava.module.aim.helper.Components;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.DecimalToText;
import org.digijava.module.aim.helper.Documents;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.Funding;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.aim.helper.FundingOrganization;
import org.digijava.module.aim.helper.FundingValidator;
import org.digijava.module.aim.helper.Issues;
import org.digijava.module.aim.helper.Location;
import org.digijava.module.aim.helper.Measures;
import org.digijava.module.aim.helper.OrgProjectId;
import org.digijava.module.aim.helper.PhysicalProgress;
import org.digijava.module.aim.helper.ReferenceDoc;
import org.digijava.module.aim.helper.RegionalFunding;
import org.digijava.module.aim.helper.RelatedLinks;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.logic.FundingCalculationsHelper;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.ComponentsUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DocumentUtil;
import org.digijava.module.aim.util.EUActivityUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.IndicatorUtil;
import org.digijava.module.aim.util.MEIndicatorsUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.TeamMemberUtil;

public class ShowActivityPrintPreview
    extends Action {

    @SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {

        EditActivityForm eaForm = (EditActivityForm) form;
        Long actId = eaForm.getActivityId();  
        if(actId==null){
        	actId=new Long(request.getParameter("activityid"));        	
        }
        
             
        if(actId != null) {
            HttpSession session = request.getSession();
            TeamMember tm = (TeamMember) session.getAttribute("currentMember");
            AmpActivity activity = ActivityUtil.getAmpActivity(actId);
            if(activity != null){
            	
            	
                //costing
                Collection euActs = EUActivityUtil.getEUActivities(activity.getAmpActivityId());
      	      	request.setAttribute("costs", euActs);
      	        
      	      	request.setAttribute("actId", actId);     	      	
		        int risk = IndicatorUtil.getOverallRisk(actId);
		        String riskName = MEIndicatorsUtil.getRiskRatingName(risk);
		        String rskColor = MEIndicatorsUtil.getRiskColor(risk);
		        request.setAttribute("overallRisk", riskName);
		        request.setAttribute("riskColor", rskColor);
		        
                // set title,description and objective

            	ProposedProjCost pg = new ProposedProjCost();
                if (activity.getFunAmount() != null){
                	pg.setFunAmountAsDouble(activity.getFunAmount());
                }                  
                pg.setCurrencyCode(activity.getCurrencyCode());
                pg.setFunDate(FormatHelper.formatDate(activity.getFunDate()));
                eaForm.setProProjCost(pg);


                try {
                    List actPrgs = new ArrayList();
                    Set prgSet = activity.getActivityPrograms();
                    if(prgSet != null) {
                        Iterator prgItr = prgSet.iterator();
                        while(prgItr.hasNext()) {
                            actPrgs.add((AmpTheme) prgItr.next());
                        }
                    }
                    eaForm.setActPrograms(actPrgs);
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
                if(activity.getName()!=null){
                	eaForm.setTitle(activity.getName().trim());
                }
                if(activity.getDescription()!=null){
                	eaForm.setDescription(activity.getDescription().trim());
                }
                if(activity.getLessonsLearned()!=null){
                	eaForm.setLessonsLearned(activity.getLessonsLearned().trim());
                }
                if(activity.getObjective()!=null){
                	 eaForm.setObjectives(activity.getObjective().trim());
                }
                // fferreyra: Added null checking for field project_impact
                if(activity.getProjectImpact()!=null){
                	eaForm.setProjectImpact(activity.getProjectImpact().trim());
                }
                
                // fferreyra: Added null checking for field activity_summary
                if(activity.getActivitySummary()!=null){
                	eaForm.setActivitySummary(activity.getActivitySummary().trim());
                }

                // fferreyra: Added null checking for field contracting_arrangements
                if(activity.getContractingArrangements()!=null){
                	eaForm.setContractingArrangements(activity.getContractingArrangements().trim());
                }

                // fferreyra: Added null checking for field cond_seq
                if(activity.getCondSeq()!=null){
                	eaForm.setCondSeq(activity.getCondSeq().trim());
                }
                
                // fferreyra: Added null checking for field linked_activities
                if(activity.getLinkedActivities()!=null){
                	eaForm.setLinkedActivities(activity.getLinkedActivities().trim());
                }
                
                // fferreyra: Added null checking for field conditionality
                if(activity.getConditionality()!=null){
                	eaForm.setConditionality(activity.getConditionality().trim());
                }
                
                // fferreyra: Added null checking for field project_management
                if(activity.getProjectManagement()!=null){
                	eaForm.setProjectManagement(activity.getProjectManagement().trim());
                }
                
                // fferreyra: Added null checking for field contract_details
                if(activity.getContractDetails()!=null){
                	eaForm.setContractDetails(activity.getContractDetails().trim());
                }
                
            	
                
                
               
                if(activity.getDocumentSpace() == null ||
                   activity.getDocumentSpace().trim().length() == 0) {
                    if(DocumentUtil.isDMEnabled()) {
                        eaForm.setDocumentSpace("aim-document-space-" +
                                                tm.getMemberId() +
                                                "-" + System.currentTimeMillis());
                        Site currentSite = RequestUtils.getSite(request);
                        DocumentUtil.createDocumentSpace(currentSite,
                            eaForm.getDocumentSpace());
                    }
                } else {
                    eaForm.setDocumentSpace(activity.getDocumentSpace().
                                            trim());
                }
                eaForm.setAmpId(activity.getAmpId());
//                if (eaForm.getAmpId() == null ) { // if AMP-ID is not generated, generate the AMP-ID
//                    /*
//                     * The logic for geerating the AMP-ID is as follows:
//                     * 1. get default global country code
//                     * 2. Get the maximum of the ampActivityId + 1, MAX_NUM
//                     * 3. merge them
//                     */			    
//                	if(eaForm.getActivityId()!=null){
//                		eaForm.setAmpId(ActivityUtil.generateAmpId(RequestUtils.getUser(request),eaForm.getActivityId()));
//                	}else {
//                		eaForm.setAmpId(ActivityUtil.generateAmpId(RequestUtils.getUser(request),ActivityUtil.getActivityMaxId()+1));
//                	}			
//                  }

                //String actApprovalStatus = DbUtil.getActivityApprovalStatus(id);
                eaForm.setStatus(DbUtil.getActivityApprovalStatus(new Long(actId)));
                
                eaForm.setStatusReason(activity.getStatusReason());

                if(null != activity.getLineMinRank())
                    eaForm.setLineMinRank(activity.getLineMinRank().
                                          toString());
                else
                    eaForm.setLineMinRank("-1");
                if(null != activity.getPlanMinRank())
                    eaForm.setPlanMinRank(activity.getPlanMinRank().
                                          toString());
                else
                    eaForm.setPlanMinRank("-1");
                //eaForm.setActRankCollection(new ArrayList());
                //for(int i = 1; i < 6; i++) {
                //  eaForm.getActRankCollection().add(new Integer(i));
                //}

                eaForm.setCreatedDate(DateConversion
                                      .ConvertDateToString(activity.
                    getCreatedDate()));
                eaForm.setUpdatedDate(DateConversion
                        .ConvertDateToString(activity.
                        		getUpdatedDate()));

                eaForm.setOriginalAppDate(DateConversion
                                          .ConvertDateToString(activity
                    .getProposedApprovalDate()));
                eaForm.setRevisedAppDate(DateConversion
                                         .ConvertDateToString(activity
                    .getActualApprovalDate()));
                eaForm.setOriginalStartDate(DateConversion
                                            .ConvertDateToString(activity
                    .getProposedStartDate()));
                eaForm
                    .setRevisedStartDate(DateConversion
                                         .ConvertDateToString(activity
                    .getActualStartDate()));
                eaForm.setCurrentCompDate(DateConversion
                                          .ConvertDateToString(activity
                    .getActualCompletionDate()));
                eaForm.setContractingDate(DateConversion
                        .ConvertDateToString(activity.
                        		getContractingDate()));
                eaForm.setDisbursementsDate(DateConversion
                        .ConvertDateToString(activity.
                        		getDisbursmentsDate()));

                eaForm.setProposedCompDate(DateConversion.ConvertDateToString(activity.getProposedCompletionDate()));
                if(activity.getContractors()!=null){
                	eaForm.setContractors(activity.getContractors().trim());
                }
                

                Collection col = activity.getClosingDates();
                List dates = new ArrayList();
                if(col != null && col.size() > 0) {
                    Iterator itr = col.iterator();
                    while(itr.hasNext()) {
                        AmpActivityClosingDates cDate = (
                            AmpActivityClosingDates) itr
                            .next();
                        if(cDate.getType().intValue() == Constants.REVISED
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
                if(orgProjIdsSet != null) {
                    Iterator projIdItr = orgProjIdsSet.iterator();
                    Collection temp = new ArrayList();
                    while(projIdItr.hasNext()) {
                        AmpActivityInternalId actIntId = (
                            AmpActivityInternalId) projIdItr
                            .next();
                        OrgProjectId projId = new OrgProjectId();
                        projId.setAmpOrgId(actIntId.getOrganisation()
                                           .getAmpOrgId());
                        projId
                            .setName(actIntId.getOrganisation()
                                     .getName());
                        projId.setProjectId(actIntId.getInternalId());
                        temp.add(projId);
                    }
                    if(temp != null && temp.size() > 0) {
                        OrgProjectId orgProjectIds[] = new OrgProjectId[
                            temp
                            .size()];
                        Object arr[] = temp.toArray();
                        for(int i = 0; i < arr.length; i++) {
                            orgProjectIds[i] = (OrgProjectId) arr[i];
                        }
                        eaForm.setSelectedOrganizations(orgProjectIds);
                    }
                }

                // setting status and modality
                AmpCategoryValue ampCategoryValueForStatus	= 
                	CategoryManagerUtil.getAmpCategoryValueFromListByKey(CategoryConstants.ACTIVITY_STATUS_KEY, activity.getCategories());
                if (ampCategoryValueForStatus != null)
            		eaForm.setStatusId( ampCategoryValueForStatus.getId() );
                
                AmpCategoryValue ampCategoryValueForLevel	= 
                	CategoryManagerUtil.getAmpCategoryValueFromListByKey(CategoryConstants.IMPLEMENTATION_LEVEL_KEY, activity.getCategories());
                if(ampCategoryValueForLevel != null)
                    eaForm.setLevelId(ampCategoryValueForLevel.getId());

                // loading the locations
                int impLevel = 0;

                Collection<AmpActivityLocation> ampLocs = activity.getLocations();

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
                    eaForm.setSelectedLocs(locs);
                  }

                /*switch(impLevel) {
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
                }*/
                
                if (impLevel >= 0) {
                	eaForm.setImplemLocationLevel( 
                			CategoryManagerUtil.getAmpCategoryValueFromDb( CategoryConstants.IMPLEMENTATION_LEVEL_KEY, 
                													new Long(impLevel) ).getId()
                	);
                }
                else
                	eaForm.setImplemLocationLevel( 
                			CategoryManagerUtil.getAmpCategoryValueFromDb( CategoryConstants.IMPLEMENTATION_LEVEL_KEY, 
									new Long(0) ).getId()
                	);
                
                

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
        			eaForm.setActivitySectors(activitySectors);
        		}
        		
                if(activity.getThemeId() != null) {
                    eaForm
                        .setProgram(activity.getThemeId()
                                    .getAmpThemeId());
                }
                if(activity.getProgramDescription()!=null){
                	 eaForm.setProgramDescription(activity.getProgramDescription().trim()); 	
                }
                
                FundingCalculationsHelper calculations=new FundingCalculationsHelper();  

          
                ArrayList fundingOrgs = new ArrayList();
                Iterator fundItr=null;
                if(activity.getFunding()!=null) {
                	fundItr = activity.getFunding().iterator();
                    while(fundItr.hasNext()) {
                        AmpFunding ampFunding = (AmpFunding) fundItr.next();
                        AmpOrganisation org = ampFunding.getAmpDonorOrgId();
                        FundingOrganization fundOrg = new FundingOrganization();
                        fundOrg.setAmpOrgId(org.getAmpOrgId());
                        fundOrg.setOrgName(org.getName());
                        int index = fundingOrgs.indexOf(fundOrg);
                        //logger.info("Getting the index as " + index
                        //	+ " for fundorg " + fundOrg.getOrgName());
                        if(index > -1) {
                            fundOrg = (FundingOrganization) fundingOrgs
                                .get(index);
                        }
                        
                        Funding fund = new Funding();
                        //fund.setAmpTermsAssist(ampFunding.getAmpTermsAssistId());
                        fund.setTypeOfAssistance( ampFunding.getTypeOfAssistance() );
                        fund.setFundingId(ampFunding.getAmpFundingId().
                                          longValue());
                        fund.setOrgFundingId(ampFunding.getFinancingId());
                        //fund.setModality(ampFunding.getModalityId());
                        fund.setFinancingInstrument(ampFunding.getFinancingInstrument());
                        fund.setConditions(ampFunding.getConditions());
                        Collection<AmpFundingDetail> fundDetails = ampFunding.getFundingDetails();
                       
                        if(fundDetails != null && fundDetails.size() > 0) {
                          
                            String toCurrCode=null;
                            if (tm != null)
                                toCurrCode = CurrencyUtil.getAmpcurrency(tm.getAppSettings().getCurrencyId()).getCurrencyCode();

                            calculations.doCalculations(fundDetails, toCurrCode);
    			            
    			            List<FundingDetail> fundDetail = calculations.getFundDetailList();
    			            
                            if(fundDetail != null)
                                Collections.sort(fundDetail,
                                                 FundingValidator.dateComp);
                            fund.setFundingDetails(fundDetail);
                            eaForm.setFundingDetails(fundDetail);
                            // funding.add(fund);
                        }
                        if(fundOrg.getFundings() == null)
                            fundOrg.setFundings(new ArrayList());
                        fundOrg.getFundings().add(fund);

                        if(index > -1) {
                            fundingOrgs.set(index, fundOrg);
                            //	logger
                            //		.info("Setting the fund org obj to the index :"
                            //			+ index);
                        } else {
                            fundingOrgs.add(fundOrg);
                            //	logger.info("Adding new fund org object");
                        }
                    }

                  eaForm.setFundingOrganizations(fundingOrgs);
                  eaForm.setTotalCommitments(calculations.getTotalCommitments().toString());
              	  eaForm.setTotalDisbursements(calculations.getTotActualDisb().toString());
              	  eaForm.setTotalExpenditures(calculations.getTotActualExp().toString());
              	  eaForm.setTotalActualDisbursementsOrders(calculations.getTotActualDisbOrder().toString());
              	  eaForm.setTotalPlannedDisbursements(calculations.getTotPlanDisb().toString());
              	  eaForm.setTotalPlannedCommitments(calculations.getTotPlannedComm().toString());
              	  eaForm.setTotalPlannedExpenditures(calculations.getTotPlannedExp().toString());
              	  eaForm.setTotalPlannedDisbursementsOrders(calculations.getTotPlannedDisbOrder().toString());
              	  eaForm.setUnDisbursementsBalance(calculations.getUnDisbursementsBalance().toString());
                }
                

        

                ArrayList regFunds = new ArrayList();
                Iterator rItr=null;
                if(activity.getRegionalFundings()!=null) {
                	rItr = activity.getRegionalFundings().iterator();
                    eaForm.setRegionTotalDisb(0);
                    while(rItr.hasNext()) {
                        AmpRegionalFunding ampRegFund = (AmpRegionalFunding)
                            rItr
                            .next();

                        double disb = 0;
                        if(ampRegFund.getAdjustmentType().intValue() == 1 &&
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
                        if(fd.getAdjustmentType() == 1) {
                            fd.setAdjustmentTypeName("Actual");
                        } else if(fd.getAdjustmentType() == 0) {
                            fd.setAdjustmentTypeName("Planned");
                        }
                        fd.setCurrencyCode(ampRegFund.getCurrency()
                                           .getCurrencyCode());
                        fd.setCurrencyName(ampRegFund.getCurrency()
                                           .getCurrencyName());
                        fd.setTransactionAmount(DecimalToText
                                                .ConvertDecimalToText(
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

                        if(regFunds.contains(regFund) == false) {
                            regFunds.add(regFund);
                        }

                        int index = regFunds.indexOf(regFund);
                        regFund = (RegionalFunding) regFunds.get(index);
                        if(fd.getTransactionType() == 0) { // commitments
                            if(regFund.getCommitments() == null) {
                                regFund.setCommitments(new ArrayList());
                            }
                            regFund.getCommitments().add(fd);
                        } else if(fd.getTransactionType() == 1) { // disbursements
                            if(regFund.getDisbursements() == null) {
                                regFund.setDisbursements(new ArrayList());
                            }
                            regFund.getDisbursements().add(fd);
                        } else if(fd.getTransactionType() == 2) { // expenditures
                            if(regFund.getExpenditures() == null) {
                                regFund.setExpenditures(new ArrayList());
                            }
                            regFund.getExpenditures().add(fd);
                        }
                        regFunds.set(index, regFund);
                    }
                }
                

       

                // Sort the funding details based on Transaction date.
                Iterator itr1 = regFunds.iterator();
                int index = 0;
                while(itr1.hasNext()) {
                    RegionalFunding regFund = (RegionalFunding) itr1.next();
                    List list = null;
                    if(regFund.getCommitments() != null) {
                        list = new ArrayList(regFund.getCommitments());
                        Collections.sort(list, FundingValidator.dateComp);
                    }
                    regFund.setCommitments(list);
                    list = null;
                    if(regFund.getDisbursements() != null) {
                        list = new ArrayList(regFund.getDisbursements());
                        Collections.sort(list, FundingValidator.dateComp);
                    }
                    regFund.setDisbursements(list);
                    list = null;
                    if(regFund.getExpenditures() != null) {
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
                	getComponents(activity, eaForm);
                }
                

                Collection memLinks = TeamMemberUtil.getMemberLinks(tm.getMemberId());
                Collection actDocs = activity.getDocuments();
                if(actDocs != null && actDocs.size() > 0) {
                    Collection docsList = new ArrayList();
                    Collection linksList = new ArrayList();

                    Iterator docItr = actDocs.iterator();
                    while(docItr.hasNext()) {
                        RelatedLinks rl = new RelatedLinks();

                        CMSContentItem cmsItem = (CMSContentItem) docItr
                            .next();
                        rl.setRelLink(cmsItem);
                        rl.setMember(TeamMemberUtil.getAmpTeamMember(tm
                            .getMemberId()));
                        Iterator tmpItr = memLinks.iterator();
                        while(tmpItr.hasNext()) {
                            Documents doc = (Documents) tmpItr.next();
                            if(doc.getDocId().longValue() == cmsItem
                               .getId()) {
                                rl.setShowInHomePage(true);
                                break;
                            }
                        }

                        if(cmsItem.getIsFile()) {
                            docsList.add(rl);
                        } else {
                            linksList.add(rl);
                        }
                    }
                    eaForm.setDocumentList(docsList);
                    eaForm.setLinksList(linksList);
                }
                Site currentSite = RequestUtils.getSite(request);
                eaForm.setManagedDocumentList(DocumentUtil.getDocumentsForActivity(currentSite, activity));
                // loading the related organizations
                eaForm.setExecutingAgencies(new ArrayList());
                eaForm.setImpAgencies(new ArrayList());
                eaForm.setBenAgencies(new ArrayList());
                eaForm.setConAgencies(new ArrayList());
                eaForm.setReportingOrgs(new ArrayList());
                eaForm.setSectGroups(new ArrayList());
                eaForm.setRegGroups(new ArrayList());

                Set relOrgs = activity.getOrgrole();
                if (relOrgs != null) {
                  Iterator relOrgsItr = relOrgs.iterator();
                  while (relOrgsItr.hasNext()) {
                    AmpOrgRole orgRole = (AmpOrgRole) relOrgsItr.next();
                    if (orgRole.getRole().getRoleCode().equals(
                        Constants.EXECUTING_AGENCY)
                        && (!eaForm
                            .getExecutingAgencies()
                            .contains(orgRole.getOrganisation()))) {
                      eaForm.getExecutingAgencies().add(
                          orgRole.getOrganisation());
                    }
                    else if (orgRole.getRole().getRoleCode().equals(
                        Constants.IMPLEMENTING_AGENCY)
                             && (!eaForm.getImpAgencies().contains(
                                 orgRole.getOrganisation()))) {
                      eaForm.getImpAgencies().add(
                          orgRole.getOrganisation());
                    }
                    else if (orgRole.getRole().getRoleCode().equals(
                        Constants.BENEFICIARY_AGENCY)
                             && (!eaForm.getBenAgencies().contains(
                                 orgRole.getOrganisation()))) {
                      eaForm.getBenAgencies().add(
                          orgRole.getOrganisation());
                    }
                    else if (orgRole.getRole().getRoleCode().equals(
                        Constants.CONTRACTING_AGENCY)
                             && (!eaForm.getConAgencies().contains(
                                 orgRole.getOrganisation()))) {
                      eaForm.getConAgencies().add(
                          orgRole.getOrganisation());
                    }
                    else if (orgRole.getRole().getRoleCode().equals(
                        Constants.REPORTING_AGENCY)
                             && (!eaForm.getReportingOrgs().contains(
                                 orgRole.getOrganisation()))) {
                      eaForm.getReportingOrgs().add(
                          orgRole.getOrganisation());
                    } else if (orgRole.getRole().getRoleCode().equals(
                            Constants.SECTOR_GROUP)
                            && (!eaForm.getSectGroups().contains(
                                orgRole.getOrganisation()))) {
                     eaForm.getSectGroups().add(
                         orgRole.getOrganisation());
                   } else if (orgRole.getRole().getRoleCode().equals(
                           Constants.REGIONAL_GROUP)
                           && (!eaForm.getRegGroups().contains(
                               orgRole.getOrganisation()))) {
                    eaForm.getRegGroups().add(
                        orgRole.getOrganisation());
                  }

                  }
                }

                if(activity.getIssues() != null
                   && activity.getIssues().size() > 0) {
                    ArrayList issueList = new ArrayList();
                    Iterator iItr = activity.getIssues().iterator();
                    while(iItr.hasNext()) {
                        AmpIssues ampIssue = (AmpIssues) iItr.next();
                        Issues issue = new Issues();
                        issue.setId(ampIssue.getAmpIssueId());
                        issue.setName(ampIssue.getName());
                        ArrayList measureList = new ArrayList();
                        if(ampIssue.getMeasures() != null
                           && ampIssue.getMeasures().size() > 0) {
                            Iterator mItr = ampIssue.getMeasures().iterator();
                            while(mItr.hasNext()) {
                                AmpMeasure ampMeasure = (AmpMeasure) mItr.next();
                                Measures measure = new Measures();
                                measure.setId(ampMeasure.getAmpMeasureId());
                                measure.setName(ampMeasure.getName());
                                ArrayList actorList = new ArrayList();
                                if(ampMeasure.getActors() != null
                                   && ampMeasure.getActors().size() > 0) {
                                    Iterator aItr = ampMeasure.getActors().iterator();
                                    while(aItr.hasNext()) {
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
                eaForm.setDnrCntTitle(activity.getDnrCntTitle());
                eaForm.setDnrCntOrganization(activity.getDnrCntOrganization());
                eaForm.setDnrCntPhoneNumber(activity.getDnrCntPhoneNumber());
                eaForm.setDnrCntFaxNumber(activity.getDnrCntFaxNumber());
    
                eaForm.setMfdCntFirstName(activity.getMofedCntFirstName());
                eaForm.setMfdCntLastName(activity.getMofedCntLastName());
                eaForm.setMfdCntEmail(activity.getMofedCntEmail());
                eaForm.setMfdCntTitle(activity.getMfdCntTitle());
                eaForm.setMfdCntOrganization(activity.getMfdCntOrganization());
                eaForm.setMfdCntPhoneNumber(activity.getMfdCntPhoneNumber());
                eaForm.setMfdCntFaxNumber(activity.getMfdCntFaxNumber());
                
                eaForm.setPrjCoFirstName(activity.getPrjCoFirstName());
                eaForm.setPrjCoLastName(activity.getPrjCoLastName());
                eaForm.setPrjCoEmail(activity.getPrjCoEmail());
                eaForm.setPrjCoTitle(activity.getPrjCoTitle());
                eaForm.setPrjCoOrganization(activity.getPrjCoOrganization());
                eaForm.setPrjCoPhoneNumber(activity.getPrjCoPhoneNumber());
                eaForm.setPrjCoFaxNumber(activity.getPrjCoFaxNumber());
                
                eaForm.setSecMiCntFirstName(activity.getSecMiCntFirstName());
                eaForm.setSecMiCntLastName(activity.getSecMiCntLastName());
                eaForm.setSecMiCntEmail(activity.getSecMiCntEmail());
                eaForm.setSecMiCntTitle(activity.getSecMiCntTitle());
                eaForm.setSecMiCntOrganization(activity.getSecMiCntOrganization());
                eaForm.setSecMiCntPhoneNumber(activity.getSecMiCntPhoneNumber());
                eaForm.setSecMiCntFaxNumber(activity.getSecMiCntFaxNumber());
                
                if(activity.getCondition()!=null){
                	 eaForm.setConditions(activity.getCondition().trim());
                }
               
                
                AmpCategoryValue ampCategoryValue = CategoryManagerUtil.
                getAmpCategoryValueFromList(CategoryConstants.ACCHAPTER_NAME,
                                            activity.getCategories());
                if (ampCategoryValue != null) {
                	 eaForm.setAcChapter(ampCategoryValue.getId());
                }
                
                ampCategoryValue = CategoryManagerUtil.getAmpCategoryValueFromList(
                        CategoryConstants.ACCESSION_INSTRUMENT_NAME, activity.getCategories());
                    if (ampCategoryValue != null) {
                    	eaForm.setAccessionInstrument(ampCategoryValue.getId());
                    }
                    
                    
                  //load programs by type
                    if(ProgramUtil.getAmpActivityProgramSettingsList()!=null){
                                 List activityNPO=ActivityUtil.getActivityProgramsByProgramType(activity.getAmpActivityId(),ProgramUtil.NATIONAL_PLAN_OBJECTIVE);
                                 List activityPP=ActivityUtil.getActivityProgramsByProgramType(activity.getAmpActivityId(),ProgramUtil.PRIMARY_PROGRAM);
                                 List activitySP=ActivityUtil.getActivityProgramsByProgramType(activity.getAmpActivityId(),ProgramUtil.SECONDARY_PROGRAM);
                                 eaForm.setNationalPlanObjectivePrograms(activityNPO);
                                 eaForm.setPrimaryPrograms(activityPP);
                                 eaForm.setSecondaryPrograms(activitySP);
                                 eaForm.setNationalSetting(ProgramUtil.getAmpActivityProgramSettings(ProgramUtil.NATIONAL_PLAN_OBJECTIVE));
                                 eaForm.setPrimarySetting(ProgramUtil.getAmpActivityProgramSettings(ProgramUtil.PRIMARY_PROGRAM));
                                 eaForm.setSecondarySetting(ProgramUtil.getAmpActivityProgramSettings(ProgramUtil.SECONDARY_PROGRAM));
                      }
             
                    
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

            			eaForm.setActivityComponentes(activitySectors);
            		}
            		
            		
            		//get all possible refdoc names from categories
                  	Collection<AmpCategoryValue> catValues=CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.REFERENCE_DOCS_KEY,false);

                	if (catValues!=null && eaForm.getReferenceDocs()==null){
                    	List<ReferenceDoc> refDocs=new ArrayList<ReferenceDoc>();
                		Collection<AmpActivityReferenceDoc> activityRefDocs=null;
                		Map<Long, AmpActivityReferenceDoc> categoryRefDocMap=null;

                		if (activity.getAmpActivityId()!=null){
                    		//get list of ref docs for activity
                			activityRefDocs=ActivityUtil.getReferenceDocumentsFor(activity.getAmpActivityId());
                        	//create map where keys are category value ids.
                			categoryRefDocMap = AmpCollectionUtils.createMap(
                					activityRefDocs,
                					new ActivityUtil.CategoryIdRefDocMapBuilder());
                		}
                		eaForm.setReferenceDocs(refDocs);
                	}
                		
                		
                //allComments
                		ArrayList<AmpComments> colAux	= null;
                        Collection ampFields 			= DbUtil.getAmpFields();
                        HashMap allComments 			= new HashMap();
                        
                        if (ampFields!=null) {
                        	for (Iterator itAux = ampFields.iterator(); itAux.hasNext(); ) {
                                AmpField field = (AmpField) itAux.next();
                                	colAux = DbUtil.getAllCommentsByField(field.getAmpFieldId(),
                                                                      actId);
                                allComments.put(field.getFieldName(), colAux);
                              }
                        }
                        
                        eaForm.setAllComments(allComments);
                        
                        
                 //purpose and results
                        if (activity.getPurpose() != null)
                            eaForm.setPurpose(activity.getPurpose().trim());
                          if (activity.getResults() != null)
                            eaForm.setResults(activity.getResults());
                          

                
                /*
                 * tanzania adds
                 */ 
                if(activity.getFY()!=null)
                	eaForm.setFY(activity.getFY().trim());
                if(activity.getVote()!=null)
                	eaForm.setVote(activity.getVote().trim());
                if(activity.getSubVote()!=null)
                	eaForm.setSubVote(activity.getSubVote().trim());
                if(activity.getSubProgram()!=null)
                	eaForm.setSubProgram(activity.getSubProgram().trim());
                if(activity.getProjectCode()!=null)
                	eaForm.setProjectCode(activity.getProjectCode().trim());
                
                eaForm.setGbsSbs(activity.getGbsSbs());
                eaForm.setGovernmentApprovalProcedures(activity.isGovernmentApprovalProcedures());
                eaForm.setJointCriteria(activity.isJointCriteria());
                
                
                if(activity.getUpdatedBy()!=null){
                	eaForm.setUpdatedBy(activity.getUpdatedBy());
                }
                if(activity.getActivityCreator() != null) {
                    User usr = activity.getActivityCreator().getUser();
                    if(usr != null) {
                        eaForm.setActAthFirstName(usr.getFirstNames());
                        eaForm.setActAthLastName(usr.getLastName());
                        eaForm.setActAthEmail(usr.getEmail());
                        eaForm.setActAthAgencySource(usr.getOrganizationName());
                    }
                }
            }else{
                eaForm.setAmpId(null);
                eaForm.setTitle(null);
                eaForm.setObjectives(null);
                eaForm.setDescription(null);
                eaForm.setActivitySectors(null);
//                eaForm.setSectorSchemes(null);
                eaForm.setActPrograms(null);
                eaForm.setProgramCollection(null);
                eaForm.setOriginalAppDate(null);
                eaForm.setRevisedAppDate(null);
                eaForm.setOriginalStartDate(null);
                eaForm.setRevisedStartDate(null);
                eaForm.setCurrentCompDate(null);
                eaForm.setDocumentSpace(null);
                eaForm.setStatusReason(null);
                eaForm.setLineMinRank(null);
                eaForm.setLineMinRank(null);
                eaForm.setPlanMinRank(null);
                eaForm.setPlanMinRank(null);
                eaForm.setCreatedDate(null);
                eaForm.setUpdatedDate(null);
                eaForm.setOriginalAppDate(null);
                eaForm.setRevisedAppDate(null);
                eaForm.setOriginalStartDate(null);
                eaForm.setRevisedStartDate(null);
                eaForm.setCurrentCompDate(null);
                eaForm.setProposedCompDate(null);
                eaForm.setContractors(null);
                eaForm.setActPrograms(null);
                eaForm.setProProjCost(null);
                eaForm.setRegionalFundings(null);
                eaForm.setSelectedComponents(null);
                eaForm.setCompTotalDisb(0);
                eaForm.setDnrCntFirstName(null);
                eaForm.setDnrCntLastName(null);
                eaForm.setDnrCntEmail(null);
                eaForm.setDnrCntTitle(null);
				eaForm.setDnrCntOrganization(null);
				eaForm.setDnrCntPhoneNumber(null);
				eaForm.setDnrCntFaxNumber(null);
				
                eaForm.setMfdCntFirstName(null);
                eaForm.setMfdCntLastName(null);
                eaForm.setMfdCntEmail(null);
                eaForm.setMfdCntTitle(null);
				eaForm.setMfdCntOrganization(null);
				eaForm.setMfdCntPhoneNumber(null);
				eaForm.setMfdCntFaxNumber(null);
				
				eaForm.setPrjCoFirstName(null);
				eaForm.setPrjCoLastName(null);
				eaForm.setPrjCoEmail(null);
				eaForm.setPrjCoTitle(null);
				eaForm.setPrjCoOrganization(null);
				eaForm.setPrjCoPhoneNumber(null);
				eaForm.setPrjCoFaxNumber(null);
				
				eaForm.setSecMiCntFirstName(null);
				eaForm.setSecMiCntLastName(null);
				eaForm.setSecMiCntEmail(null);
				eaForm.setSecMiCntTitle(null);
				eaForm.setSecMiCntOrganization(null);
				eaForm.setSecMiCntPhoneNumber(null);
				eaForm.setSecMiCntFaxNumber(null);
				
                eaForm.setConditions(null);
                eaForm.setActAthFirstName(null);
                eaForm.setActAthLastName(null);
                eaForm.setActAthEmail(null);
                eaForm.setActAthAgencySource(null);
                eaForm.setUpdatedBy(null);
                
                eaForm.setAccessionInstrument(null);
                eaForm.setAcChapter(null);
                
                /*
				 * tanzania adds
				 */
				eaForm.setFY(null);
				eaForm.setVote(null);
				eaForm.setSubVote(null);
				eaForm.setSubProgram(null);
				eaForm.setProjectCode(null);
				eaForm.setGbsSbs(null);
				eaForm.setGovernmentApprovalProcedures(false);
				eaForm.setJointCriteria(false);               
                
				
            
     }
    }
        return mapping.findForward("forward");
    }

	private void getComponents(AmpActivity activity, EditActivityForm eaForm) {
		Collection componets = activity.getComponents();
		List<Components<FundingDetail>> selectedComponents = new ArrayList<Components<FundingDetail>>();
		Iterator compItr = componets.iterator();
		while (compItr.hasNext()) {
			AmpComponent temp = (AmpComponent) compItr.next();
			Components<FundingDetail> tempComp = new Components<FundingDetail>();
			tempComp.setTitle(temp.getTitle());
			tempComp.setComponentId(temp.getAmpComponentId());
			tempComp.setType_Id((temp.getType()!=null)?temp.getType().getType_id():null);
			
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


			Collection<AmpComponentFunding> fundingComponentActivity = ActivityUtil.getFundingComponentActivity(
					tempComp.getComponentId(), activity.getAmpActivityId());
			Iterator cItr = fundingComponentActivity.iterator();
			while (cItr.hasNext()) {
				AmpComponentFunding ampCompFund = (AmpComponentFunding) cItr
						.next();

				double disb = 0;
				if (ampCompFund.getAdjustmentType().intValue() == 1
					&& ampCompFund.getTransactionType().intValue() == 1)
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
				fd.setCurrencyCode(ampCompFund.getCurrency().getCurrencyCode());
				fd.setCurrencyName(ampCompFund.getCurrency().getCurrencyName());
				fd.setTransactionAmount(FormatHelper.formatNumber(ampCompFund.getTransactionAmount().doubleValue()));
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

			ComponentsUtil.calculateFinanceByYearInfo(tempComp,fundingComponentActivity);

			Collection<AmpPhysicalPerformance> phyProgress = ActivityUtil
						.getPhysicalProgressComponentActivity(tempComp.getComponentId(), activity.getAmpActivityId());

			if (phyProgress != null && phyProgress.size() > 0) {
				Collection physicalProgress = new ArrayList();
				Iterator phyProgItr = phyProgress.iterator();
				while (phyProgItr.hasNext()) {
					AmpPhysicalPerformance phyPerf = (AmpPhysicalPerformance) phyProgItr
							.next();
					PhysicalProgress phyProg = new PhysicalProgress();
					phyProg.setPid(phyPerf.getAmpPpId());
					phyProg.setDescription(phyPerf.getDescription());
					phyProg.setReportingDate(DateConversion
							.ConvertDateToString(phyPerf.getReportingDate()));
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
}
  


