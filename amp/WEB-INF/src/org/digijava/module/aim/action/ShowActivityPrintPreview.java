/*
 * ShowActivityPrintPreview.java
 * Created : 24-May-2005
 */

package org.digijava.module.aim.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.Util;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpActivityInternalId;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpComments;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpComponentFunding;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpField;
import org.digijava.module.aim.dbentity.AmpFundingAmount;
import org.digijava.module.aim.dbentity.AmpGPISurvey;
import org.digijava.module.aim.dbentity.AmpGPISurveyResponse;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpRegionalFunding;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpStructure;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.form.ProposedProjCost;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.ApplicationSettings;
import org.digijava.module.aim.helper.Components;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.DecimalToText;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.aim.helper.FundingValidator;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.Location;
import org.digijava.module.aim.helper.ManagedDocument;
import org.digijava.module.aim.helper.OrgProjectId;
import org.digijava.module.aim.helper.RegionalFunding;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.ComponentsUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DecimalWraper;
import org.digijava.module.aim.util.DocumentUtil;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.EUActivityUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.IndicatorUtil;
import org.digijava.module.aim.util.LocationUtil.HelperLocationAncestorLocationNamesAsc;
import org.digijava.module.aim.util.MEIndicatorsUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.hibernate.Hibernate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

//import org.digijava.module.aim.helper.Issues;

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
            AmpActivityVersion activity = null;
            try {
                activity = ActivityUtil.loadActivity(actId);
            } catch (DgException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if(activity != null){

                List gpiSurveys = new ArrayList();
                if (activity.getGpiSurvey() != null) {
                    for (AmpGPISurvey survey : activity.getGpiSurvey()) {
                        List<AmpGPISurveyResponse> list = new ArrayList<>(survey.getResponses());
                        Collections.sort(list, new AmpGPISurveyResponse.AmpGPISurveyResponseComparator());
                        gpiSurveys.add(list);
                    }
                    request.setAttribute("gpiSurveys", gpiSurveys);
                }


                //costing
                Collection euActs = EUActivityUtil.getEUActivities(activity.getAmpActivityId());
                request.setAttribute("costs", euActs);
                
                request.setAttribute("actId", actId);               
                int risk = IndicatorUtil.getOverallRisk(actId);
                String riskName = MEIndicatorsUtil.getRiskRatingName(risk);
                String rskColor = MEIndicatorsUtil.getRiskColor(risk);
                request.setAttribute("overallRisk", riskName);
                request.setAttribute("riskColor", rskColor);
                
                // set project costs

                eaForm.getFunding().setProProjCost(getProposedProjectCost(eaForm, activity, AmpFundingAmount.FundingType.PROPOSED));
                eaForm.getFunding().setRevProjCost(getProposedProjectCost(eaForm, activity, AmpFundingAmount.FundingType.REVISED));

                // set title,description and objective
                
                eaForm.getPrograms().setActPrograms(ActivityUtil.getActivityPrograms(activity.getAmpActivityId()));
                if(activity.getName()!=null){
                    eaForm.getIdentification().setTitle(activity.getName().trim());
                }
                if(activity.getDescription()!=null){
                    eaForm.getIdentification().setDescription(activity.getDescription().trim());
                }
                if(activity.getLessonsLearned()!=null){
                    eaForm.getIdentification().setLessonsLearned(activity.getLessonsLearned().trim());
                }
                if(activity.getObjective()!=null){
                 eaForm.getIdentification().setObjectives(activity.getObjective().trim());
               }
                if(activity.getProjectComments()!=null){
                 eaForm.getIdentification().setProjectComments(activity.getProjectComments().trim());
               }
                // fferreyra: Added null checking for field project_impact
                if(activity.getProjectImpact()!=null){
                    eaForm.getIdentification().setProjectImpact(activity.getProjectImpact().trim());
                }

                if (activity.getStatusOtherInfo() != null) {
                    eaForm.getIdentification().setStatusOtherInfo(activity.getStatusOtherInfo().trim());
                }

                if (activity.getProjectCategoryOtherInfo() != null) {
                    eaForm.getIdentification().setProjectCategoryOtherInfo(activity.getProjectCategoryOtherInfo()
                            .trim());
                }

                if (activity.getModalitiesOtherInfo() != null) {
                    eaForm.getIdentification().setModalitiesOtherInfo(activity.getModalitiesOtherInfo().trim());
                }
                
                // fferreyra: Added null checking for field activity_summary
                if(activity.getActivitySummary()!=null){
                    eaForm.getIdentification().setActivitySummary(activity.getActivitySummary().trim());
                }
               
                // fferreyra: Added null checking for field conditionality
                if(activity.getConditionality()!=null){
                    eaForm.getIdentification().setConditionality(activity.getConditionality().trim());
                }
                
                // fferreyra: Added null checking for field project_management
                if(activity.getProjectManagement()!=null){
                    eaForm.getIdentification().setProjectManagement(activity.getProjectManagement().trim());
                }
                
               
                if(activity.getDocumentSpace() == null ||
                   activity.getDocumentSpace().trim().length() == 0) {
                    if(DocumentUtil.isDMEnabled()) {
                        eaForm.getDocuments().setDocumentSpace("aim-document-space-" +
                                                tm.getMemberId() +
                                                "-" + System.currentTimeMillis());
                        Site currentSite = RequestUtils.getSite(request);
                        DocumentUtil.createDocumentSpace(currentSite,
                            eaForm.getDocuments().getDocumentSpace());
                    }
                } else {
                    eaForm.getDocuments().setDocumentSpace(activity.getDocumentSpace().
                                            trim());
                }
                eaForm.getIdentification().setAmpId(activity.getAmpId());

                eaForm.getIdentification().setStatus(DbUtil.getActivityApprovalStatus(new Long(actId)).getDbName());

                String langCode = RequestUtils.getNavigationLanguage(request).getCode();

                if(activity.getStatusReason()!=null){
                    eaForm.getIdentification().setStatusReason(activity.getStatusReason());
                }

                if(null != activity.getLineMinRank())
                    eaForm.getPlanning().setLineMinRank(activity.getLineMinRank().
                                          toString());
                else
                    eaForm.getPlanning().setLineMinRank("-1");
            
                
                eaForm.getPlanning().setActRankCollection(new ArrayList());
                for(int i = 1; i < 6; i++) {
                    eaForm.getPlanning().getActRankCollection().add(new Integer(i));
                }

                eaForm.getIdentification().setCreatedDate(DateConversion.convertDateToLocalizedString(activity.getCreatedDate()));
                eaForm.getIdentification().setFundingSourcesNumber(activity.getFundingSourcesNumber());
                eaForm.getIdentification().setUpdatedDate(DateConversion.convertDateToLocalizedString(activity.getUpdatedDate()));

                eaForm.getPlanning().setOriginalAppDate(DateConversion.convertDateToLocalizedString(activity.getProposedApprovalDate()));
                eaForm.getPlanning().setRevisedAppDate(DateConversion.convertDateToLocalizedString(activity.getActualApprovalDate()));
                eaForm.getPlanning().setOriginalStartDate(DateConversion.convertDateToLocalizedString(activity.getProposedStartDate()));
                eaForm.getPlanning().setOriginalCompDate(DateConversion.convertDateToLocalizedString(activity.getOriginalCompDate()));
                eaForm.getPlanning().setRevisedStartDate(DateConversion.convertDateToLocalizedString(activity.getActualStartDate()));
                eaForm.getPlanning().setCurrentCompDate(DateConversion.convertDateToLocalizedString(activity.getActualCompletionDate()));
                eaForm.getPlanning().setContractingDate(DateConversion.convertDateToLocalizedString(activity.getContractingDate()));
                eaForm.getPlanning().setDisbursementsDate(DateConversion.convertDateToLocalizedString(activity.getDisbursmentsDate()));
                eaForm.getPlanning().setProposedCompDate(DateConversion.convertDateToLocalizedString(activity.getProposedCompletionDate()));
            
                

                if (tm != null && tm.getAppSettings() != null
                        && tm.getAppSettings().getCurrencyId() != null) {
                    String currCode = "";
                    String currName="";
                    AmpCurrency curr = CurrencyUtil.getAmpcurrency(tm
                            .getAppSettings().getCurrencyId());
                    if (curr != null) {
                        currCode = curr.getCurrencyCode();
                        currName = curr.getCurrencyName();
                    }
                    eaForm.setCurrCode(currCode);
                    eaForm.setCurrName(currName);
                }

                // loading organizations and thier project ids.                
                Collection orgProjIdsSet = DbUtil.getActivityInternalId(activity.getAmpActivityId());
                if(orgProjIdsSet != null) {
                    Iterator projIdItr = orgProjIdsSet.iterator();
                    Collection temp = new ArrayList();
                    while(projIdItr.hasNext()) {
                        AmpActivityInternalId actIntId = (
                            AmpActivityInternalId) projIdItr
                            .next();
                        OrgProjectId projId = new OrgProjectId();
                        projId.setId(actIntId.getId());
                        projId.setOrganisation(actIntId.getOrganisation());
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
                        eaForm.getIdentification().setSelectedOrganizations(orgProjectIds);
                    }
                }

                // setting status and modality
                AmpCategoryValue ampCategoryValueForStatus  = 
                    CategoryManagerUtil.getAmpCategoryValueFromListByKey(CategoryConstants.ACTIVITY_STATUS_KEY, activity.getCategories());
                if (ampCategoryValueForStatus != null)
                    eaForm.getIdentification().setStatusId( ampCategoryValueForStatus.getId() );
                
                AmpCategoryValue ampCatValForProjectImplUnit    = 
                    CategoryManagerUtil.getAmpCategoryValueFromListByKey(CategoryConstants.PROJECT_IMPLEMENTING_UNIT_KEY, activity.getCategories());
                if (ampCatValForProjectImplUnit != null)
                    eaForm.getIdentification().setProjectImplUnitId(ampCatValForProjectImplUnit.getId() );
                
                AmpCategoryValue ampCategoryValueForLevel   = 
                    CategoryManagerUtil.getAmpCategoryValueFromListByKey(CategoryConstants.IMPLEMENTATION_LEVEL_KEY, activity.getCategories());
                if(ampCategoryValueForLevel != null)
                    eaForm.getLocation().setLevelId(ampCategoryValueForLevel.getId());

                AmpCategoryValue ampCategoryValueLocationForLevel   = 
                    CategoryManagerUtil.getAmpCategoryValueFromListByKey(CategoryConstants.IMPLEMENTATION_LOCATION_KEY, activity.getCategories());
                if(ampCategoryValueLocationForLevel != null)
                    eaForm.getLocation().setImplemLocationLevel(ampCategoryValueLocationForLevel.getId());

                
                // loading the locations
                List<AmpActivityLocation> ampLocs = ActivityUtil.getActivityLocations(activity.getAmpActivityId());

                if (ampLocs != null && ampLocs.size() > 0) {
                    List<Location> locs = new ArrayList<>();

                    boolean maxLevel = false;
                    for(AmpActivityLocation actLoc:ampLocs){
                        if (actLoc == null)
                            continue;
                        AmpLocation loc=actLoc.getLocation();                               //AMP-2250

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
                        
                        location.setAmpCVLocation( loc.getLocation() );
                        if ( loc.getLocation() != null ){
                            location.setAncestorLocationNames( DynLocationManagerUtil.getParents( loc.getLocation()) );
                            location.setLocationName(loc.getLocation().getName());
                            location.setLocId( loc.getLocation().getId() );
                        }
                          AmpCategoryValueLocations ampCVRegion = DynLocationManagerUtil.getAncestorByLayer(
                                  loc.getLocation(), CategoryConstants.IMPLEMENTATION_LOCATION_ADM_LEVEL_1);
                        if ( ampCVRegion != null ) {
                            if (eaForm.getFunding().getFundingRegions() == null) {
                              eaForm.getFunding()
                                  .setFundingRegions(new ArrayList());
                            }
                            if (!eaForm.getFunding().getFundingRegions().contains(ampCVRegion) ) {
                              eaForm.getFunding().getFundingRegions().add( ampCVRegion );
                        }
}

                        if(actLoc.getLocationPercentage()!=null)
//                            location.setPercent(FormatHelper.formatNumber( actLoc.getLocationPercentage().doubleValue()));
                            location.setPercent(FormatHelper.formatPercentage(actLoc.getLocationPercentage()));
                        locs.add(location);
                      }
                    }
                    if (locs != null) {
                        //String langCode = RequestUtils.getNavigationLanguage(request).getCode();
                        Collections.sort(locs, new HelperLocationAncestorLocationNamesAsc(langCode));
                    }
                    eaForm.getLocation().setSelectedLocs(locs);
                  }

                List<AmpActivitySector> sectors = ActivityUtil.getAmpActivitySectors(activity.getAmpActivityId());

                if (sectors != null && sectors.size() > 0) {
                    List<ActivitySector> activitySectors = new ArrayList<ActivitySector>();
                    for(AmpActivitySector ampActSect:sectors) {
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
                                    actSect.setSectorPercentage(FormatHelper.formatPercentage(ampActSect.getSectorPercentage()));
                                    actSect.setSectorScheme(parent.getAmpSecSchemeId().getSecSchemeName());
                                                                
                                }
                                                       
                                activitySectors.add(actSect);
                            }
                        }
                    }
                    Collections.sort(activitySectors);
                    eaForm.getSectors().setActivitySectors(activitySectors);
                }
                
                if(activity.getProgramDescription()!=null){
                     eaForm.getPrograms().setProgramDescription(activity.getProgramDescription().trim());   
                }
                
                String toCurrCode=null;
                if (tm != null) {
                    toCurrCode = CurrencyUtil.getAmpcurrency(tm.getAppSettings().getCurrencyId()).getCurrencyCode();
                } else {
                    toCurrCode = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY);
                }

                eaForm.getFunding().populateFromFundings(activity, toCurrCode, tm, false); // alternative to activity.getFunding(): DbUtil.getAmpFunding(activity.getAmpActivityId());
        
                ArrayList regFunds = new ArrayList();
                Iterator rItr=null;
                if(activity.getRegionalFundings()!=null) {
                    rItr = activity.getRegionalFundings().iterator();
                    eaForm.getFunding().setRegionTotalDisb(0);
                    while(rItr.hasNext()) {
                        AmpRegionalFunding ampRegFund = (AmpRegionalFunding)
                            rItr
                            .next();

                        double disb = 0;
                        if(ampRegFund.getAdjustmentType().getValue().equals(CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey()) &&
                           ampRegFund.getTransactionType().intValue() == 1)
                            disb = ampRegFund.getTransactionAmount().
                                doubleValue();
               
                        eaForm.getFunding().setRegionTotalDisb(eaForm.getFunding().getRegionTotalDisb() +
                                                  disb);

                        FundingDetail fd = new FundingDetail();
                        fd.setAdjustmentTypeName(ampRegFund.getAdjustmentType());

                        fd.setCurrencyCode(ampRegFund.getCurrency()
                                           .getCurrencyCode());
                        fd.setCurrencyName(ampRegFund.getCurrency()
                                           .getCurrencyName());
                        fd.setTransactionAmount(DecimalToText
                                                .ConvertDecimalToText(
                                                    ampRegFund
                                                    .getTransactionAmount().doubleValue()));
                        fd.setTransactionDate(DateConversion.convertDateToString(ampRegFund.getTransactionDate()));
                        fd.setFiscalYear(DateConversion.convertDateToFiscalYearString(ampRegFund.getTransactionDate()));

                        fd.setTransactionType(ampRegFund.getTransactionType()
                                              .intValue());

                        RegionalFunding regFund = new RegionalFunding();
                        
                        regFund.setRegionId(ampRegFund.getRegionLocation()
                                            .getId());
                        regFund.setRegionName(ampRegFund.getRegionLocation().getName());

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

                eaForm.getFunding().setRegionalFundings(regFunds);

                eaForm.getComponents().setSelectedComponents(null);
                eaForm.getComponents().setCompTotalDisb(0);

                Collection comp = activity.getComponents();
                if (comp != null && comp.size() > 0) {
                    getComponents(comp, activity.getAmpActivityId(), eaForm);
                }
                
                Collection<ManagedDocument> actDocs = DocumentUtil.getDocumentsForActivity(RequestUtils.getSite(request), activity);
                eaForm.getDocuments().setManagedDocumentList(actDocs);
                // loading the related organizations
                List executingAgencies = new ArrayList();
                List impAgencies = new ArrayList();
                List benAgencies = new ArrayList();
                List conAgencies = new ArrayList();
                List reportingOrgs = new ArrayList();
                List sectGroups = new ArrayList();
                List regGroups = new ArrayList();
                List respOrganisations  = new ArrayList<AmpOrganisation>();
                
                eaForm.getAgencies().setExecutingOrgToInfo(new HashMap<String, String>());
                eaForm.getAgencies().setImpOrgToInfo(new HashMap<String, String>());
                eaForm.getAgencies().setBenOrgToInfo(new HashMap<String, String>());
                eaForm.getAgencies().setConOrgToInfo(new HashMap<String, String>());
                eaForm.getAgencies().setRepOrgToInfo(new HashMap<String, String>());
                eaForm.getAgencies().setSectOrgToInfo(new HashMap<String, String>());
                eaForm.getAgencies().setRegOrgToInfo(new HashMap<String, String>());
                eaForm.getAgencies().setRespOrgToInfo(new HashMap<String, String>());
                
                eaForm.getAgencies().setExecutingOrgPercentage(new HashMap<String, String>());
                eaForm.getAgencies().setImpOrgPercentage(new HashMap<String, String>());
                eaForm.getAgencies().setBenOrgPercentage(new HashMap<String, String>());
                eaForm.getAgencies().setConOrgPercentage(new HashMap<String, String>());
                eaForm.getAgencies().setRepOrgPercentage(new HashMap<String, String>());
                eaForm.getAgencies().setSectOrgPercentage(new HashMap<String, String>());
                eaForm.getAgencies().setRegOrgPercentage(new HashMap<String, String>());
                eaForm.getAgencies().setRespOrgPercentage(new HashMap<String, String>());


                List<AmpOrgRole> relOrgs = ActivityUtil.getOrgRole(activity.getAmpActivityId());
                if (relOrgs != null) {
                  AmpRole role = null;
                  AmpOrganisation organisation = null;

                  for(AmpOrgRole orgRole:relOrgs){
                      role = ActivityUtil.getAmpRole(activity.getAmpActivityId(), orgRole.getAmpOrgRoleId());
                      organisation = ActivityUtil.getAmpOrganisation(activity.getAmpActivityId(), orgRole.getAmpOrgRoleId());

                      if (role == null || role.getRoleCode() == null) {
                          continue;
                      }

                      if (role.getRoleCode().equals(Constants.EXECUTING_AGENCY)
                              && (!executingAgencies.contains(organisation))) {
                          executingAgencies.add(organisation);
                          if (orgRole.getAdditionalInfo() != null && orgRole.getAdditionalInfo().length() > 0) {
                              eaForm.getAgencies().getExecutingOrgToInfo().put(organisation.getAmpOrgId().toString(), orgRole.getAdditionalInfo());
                          }
                          if (orgRole.getPercentage() != null) {
                              eaForm.getAgencies().getExecutingOrgPercentage().put(organisation.getAmpOrgId().toString(), orgRole.getPercentage().toString());
                          }
                      } else if (role.getRoleCode().equals(Constants.IMPLEMENTING_AGENCY)
                              && (!impAgencies.contains(organisation))) {
                          impAgencies.add(organisation);
                          if (orgRole.getAdditionalInfo() != null && orgRole.getAdditionalInfo().length() > 0) {
                              eaForm.getAgencies().getImpOrgToInfo().put(organisation.getAmpOrgId().toString(), orgRole.getAdditionalInfo());
                          }
                          if (orgRole.getPercentage() != null) {
                              eaForm.getAgencies().getImpOrgPercentage().put(organisation.getAmpOrgId().toString(), orgRole.getPercentage().toString());
                          }
                      } else if (role.getRoleCode().equals(Constants.BENEFICIARY_AGENCY)
                             && (!benAgencies.contains(organisation))) {
                          benAgencies.add(organisation);
                          if (orgRole.getAdditionalInfo() != null && orgRole.getAdditionalInfo().length() > 0) {
                              eaForm.getAgencies().getBenOrgToInfo().put(organisation.getAmpOrgId().toString(), orgRole.getAdditionalInfo());
                          }
                          if (orgRole.getPercentage() != null) {
                              eaForm.getAgencies().getBenOrgPercentage().put(organisation.getAmpOrgId().toString(), orgRole.getPercentage().toString());
                          }
                      } else if (role.getRoleCode().equals(Constants.CONTRACTING_AGENCY)
                              && (!conAgencies.contains(organisation))) {
                          conAgencies.add(organisation);
                          if (orgRole.getAdditionalInfo() != null && orgRole.getAdditionalInfo().length() > 0) {
                              eaForm.getAgencies().getConOrgToInfo().put(organisation.getAmpOrgId().toString(), orgRole.getAdditionalInfo());
                          }
                          if (orgRole.getPercentage() != null) {
                              eaForm.getAgencies().getConOrgPercentage().put(organisation.getAmpOrgId().toString(), orgRole.getPercentage().toString());
                          }
                      } else if (role.getRoleCode().equals(Constants.REPORTING_AGENCY)
                              && (!reportingOrgs.contains(organisation))) {
                          reportingOrgs.add(organisation);
                          if (orgRole.getAdditionalInfo() != null && orgRole.getAdditionalInfo().length() > 0) {
                              eaForm.getAgencies().getRepOrgToInfo().put(organisation.getAmpOrgId().toString(), orgRole.getAdditionalInfo());
                          }
                          if (orgRole.getPercentage() != null) {
                              eaForm.getAgencies().getRepOrgPercentage().put(organisation.getAmpOrgId().toString(), orgRole.getPercentage().toString());
                          }
                      } else if (role.getRoleCode().equals(Constants.RESPONSIBLE_ORGANISATION)
                              && (!respOrganisations.contains(organisation))) {
                          respOrganisations.add(organisation);
                          if (orgRole.getAdditionalInfo() != null && orgRole.getAdditionalInfo().length() > 0) {
                              eaForm.getAgencies().getRespOrgToInfo().put(organisation.getAmpOrgId().toString(), orgRole.getAdditionalInfo());
                          }
                          if (orgRole.getPercentage() != null) {
                              eaForm.getAgencies().getRespOrgPercentage().put(organisation.getAmpOrgId().toString(), orgRole.getPercentage().toString());
                          }
                      } else if (role.getRoleCode().equals(
                              Constants.SECTOR_GROUP)
                              && (!sectGroups.contains(organisation))) {
                          sectGroups.add(DbUtil.getOrganisation(orgRole.getOrganisation().getAmpOrgId()));
                          if (orgRole.getAdditionalInfo() != null && orgRole.getAdditionalInfo().length() > 0) {
                              eaForm.getAgencies().getSectOrgToInfo().put(organisation.getAmpOrgId().toString(), orgRole.getAdditionalInfo());
                          }
                          if (orgRole.getPercentage() != null) {
                              eaForm.getAgencies().getSectOrgPercentage().put(organisation.getAmpOrgId().toString(), orgRole.getPercentage().toString());
                          }
                      } else if (role.getRoleCode().equals(
                              Constants.REGIONAL_GROUP)
                              && (!regGroups.contains(organisation))) {
                          regGroups.add(organisation);
                          if (orgRole.getAdditionalInfo() != null && orgRole.getAdditionalInfo().length() > 0) {
                              eaForm.getAgencies().getRegOrgToInfo().put(organisation.getAmpOrgId().toString(), orgRole.getAdditionalInfo());
                          }
                          if (orgRole.getPercentage() != null) {
                              eaForm.getAgencies().getRegOrgPercentage().put(organisation.getAmpOrgId().toString(), orgRole.getPercentage().toString());
                          }
                      }

                  }
                }
                
                eaForm.getAgencies().setExecutingAgencies(executingAgencies);
                eaForm.getAgencies().setImpAgencies(impAgencies);
                eaForm.getAgencies().setBenAgencies(benAgencies);
                eaForm.getAgencies().setConAgencies(conAgencies);
                eaForm.getAgencies().setReportingOrgs(reportingOrgs);
                eaForm.getAgencies().setSectGroups(sectGroups);
                eaForm.getAgencies().setRegGroups(regGroups);
                eaForm.getAgencies().setRespOrganisations(respOrganisations);
                //llk here!!
                
                
                
                ArrayList<org.digijava.module.aim.helper.Issues> colIssues = ActivityUtil.getIssues(activity.getAmpActivityId());
                if(colIssues != null && colIssues.size() > 0) {
//                  ArrayList<Issues> issueList = new ArrayList();
//                  for (org.digijava.module.aim.helper.Issues externalIssue : colIssues) {
//                      AmpIssues ampIssue = (AmpIssues) iItr.next();
//                      Issues issue = new Issues();
//                      issue.setId(ampIssue.getAmpIssueId());
//                      issue.setName(ampIssue.getName());
//                      issue.setIssueDate(FormatHelper.formatDate(ampIssue.getIssueDate()));
//                      ArrayList measureList = new ArrayList();
//                      issue.setMeasures(measureList);
//                      issueList.add(issue);
                    
                    eaForm.getIssues().setIssues(colIssues);
                } else {
                    eaForm.getIssues().setIssues(null);
                }
                
                
                
                
                
                
//                eaForm.getIssues().setIssues(new ArrayList<>(ActivityUtil.getIssues(actId)));
//                eaForm.getIssues().setIssues(null);

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
                

               
                
                AmpCategoryValue ampCategoryValue = CategoryManagerUtil.
                getAmpCategoryValueFromList(CategoryConstants.ACCHAPTER_NAME,
                                            activity.getCategories());
                if (ampCategoryValue != null) {
                     eaForm.getIdentification().setAcChapter(ampCategoryValue.getId());
                }
                
                ampCategoryValue = CategoryManagerUtil.getAmpCategoryValueFromList(
                    CategoryConstants.PROCUREMENT_SYSTEM_NAME, activity.getCategories());
                if (ampCategoryValue != null)
                  eaForm.getIdentification().setProcurementSystem(new Long(ampCategoryValue.getId()));

                ampCategoryValue = CategoryManagerUtil.getAmpCategoryValueFromList(
                    CategoryConstants.REPORTING_SYSTEM_NAME, activity.getCategories());
                if (ampCategoryValue != null)
                  eaForm.getIdentification().setReportingSystem(new Long(ampCategoryValue.getId()));

                ampCategoryValue = CategoryManagerUtil.getAmpCategoryValueFromList(
                    CategoryConstants.AUDIT_SYSTEM_NAME, activity.getCategories());
                if (ampCategoryValue != null)
                  eaForm.getIdentification().setAuditSystem(new Long(ampCategoryValue.getId()));

                ampCategoryValue = CategoryManagerUtil.getAmpCategoryValueFromList(
                    CategoryConstants.INSTITUTIONS_NAME, activity.getCategories());
                if (ampCategoryValue != null)
                  eaForm.getIdentification().setInstitutions(new Long(ampCategoryValue.getId()));
                
                ampCategoryValue = CategoryManagerUtil.getAmpCategoryValueFromList(
                    CategoryConstants.ACCESSION_INSTRUMENT_NAME, activity.getCategories());
                if (ampCategoryValue != null) {
                    eaForm.getIdentification().setAccessionInstrument(ampCategoryValue.getId());
                }
                
                ampCategoryValue = CategoryManagerUtil.getAmpCategoryValueFromList(
                        CategoryConstants.PROJECT_CATEGORY_NAME, activity.getCategories());
                    if (ampCategoryValue != null) {
                        eaForm.getIdentification().setProjectCategory(ampCategoryValue.getId());
                    }
                    
                    
                  //load programs by type
                    if(ProgramUtil.getAmpActivityProgramSettingsList()!=null){
                                 List activityNPO=ActivityUtil.getActivityProgramsByProgramType(activity.getAmpActivityId(),ProgramUtil.NATIONAL_PLAN_OBJECTIVE);
                                 List activityPP=ActivityUtil.getActivityProgramsByProgramType(activity.getAmpActivityId(),ProgramUtil.PRIMARY_PROGRAM);
                                 List activitySP=ActivityUtil.getActivityProgramsByProgramType(activity.getAmpActivityId(),ProgramUtil.SECONDARY_PROGRAM);
                                 eaForm.getPrograms().setNationalPlanObjectivePrograms(activityNPO);
                                 eaForm.getPrograms().setPrimaryPrograms(activityPP);
                                 eaForm.getPrograms().setSecondaryPrograms(activitySP);
                                 eaForm.getPrograms().setNationalSetting(ProgramUtil.getAmpActivityProgramSettings(ProgramUtil.NATIONAL_PLAN_OBJECTIVE));
                                 eaForm.getPrograms().setPrimarySetting(ProgramUtil.getAmpActivityProgramSettings(ProgramUtil.PRIMARY_PROGRAM));
                                 eaForm.getPrograms().setSecondarySetting(ProgramUtil.getAmpActivityProgramSettings(ProgramUtil.SECONDARY_PROGRAM));
                      }
                    
                    //get all possible refdoc names from categories
                    Collection<AmpCategoryValue> catValues=CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.REFERENCE_DOCS_KEY,false);

//                  if (catValues!=null && eaForm.getDocuments().getReferenceDocs()==null){
//                      List<ReferenceDoc> refDocs=new ArrayList<ReferenceDoc>();
//                      Collection<AmpActivityReferenceDoc> activityRefDocs=null;
//                      Map<Long, AmpActivityReferenceDoc> categoryRefDocMap=null;
//
//                      if (activity.getAmpActivityId()!=null){
//                          //get list of ref docs for activity
//                          activityRefDocs=ActivityUtil.getReferenceDocumentsFor(activity.getAmpActivityId());
//                          //create map where keys are category value ids.
//                          categoryRefDocMap = AmpCollectionUtils.createMap(
//                                  activityRefDocs,
//                                  new ActivityUtil.CategoryIdRefDocMapBuilder());
//                      }
//                      if(refDocs.size()>0){
//                          ReferenceDoc[] myrefDoc = (ReferenceDoc[]) refDocs.toArray(new ReferenceDoc[0]);
//                          eaForm.getDocuments().setReferenceDocs(myrefDoc);
//                      }
//                      else{
//                          eaForm.getDocuments().setReferenceDocs(null);
//                      }
//
//                  }
                        
                        
                //allComments
                        List<AmpComments> colAux    = null;
                        Collection ampFields            = DbUtil.getAmpFields();
                        HashMap allComments             = new HashMap();
                        
                        if (ampFields!=null) {
                            for (Iterator itAux = ampFields.iterator(); itAux.hasNext(); ) {
                                AmpField field = (AmpField) itAux.next();
                                    colAux = DbUtil.getAllCommentsByField(field.getAmpFieldId(), actId);
                                allComments.put(field.getFieldName(), colAux);
                              }
                        }
                        
                        eaForm.getComments().setAllComments(allComments);
                        //stuctures
                        ArrayList<AmpStructure> res = new ArrayList<AmpStructure>(activity.getStructures());
                        for(AmpStructure struc:res) {
                            Hibernate.initialize(struc.getImages());
                            Hibernate.initialize(struc.getType());
                        }
                        Collections.sort(res);
                        eaForm.setStructures(res);
                        
                 //purpose and results
                if (activity.getPurpose() != null)
                    eaForm.getIdentification().setPurpose(activity.getPurpose().trim());
                  if (activity.getResults() != null)
                    eaForm.getIdentification().setResults(activity.getResults());
                  

                
                if(activity.getFY()!=null)
                    eaForm.getIdentification().setFY(activity.getFY().trim());
                if(activity.getVote()!=null)
                    eaForm.getIdentification().setVote(activity.getVote().trim());
                if(activity.getSubVote()!=null)
                    eaForm.getIdentification().setSubVote(activity.getSubVote().trim());
                if(activity.getSubProgram()!=null)
                    eaForm.getIdentification().setSubProgram(activity.getSubProgram().trim());
                if(activity.getProjectCode()!=null)
                    eaForm.getIdentification().setProjectCode(activity.getProjectCode().trim());
                if(activity.getMinistryCode()!=null)
                    eaForm.getIdentification().setMinistryCode(activity.getMinistryCode().trim());
                
//                eaForm.getIdentification().setGbsSbs(activity.getGbsSbs());
                eaForm.getIdentification().setGovernmentApprovalProcedures(activity.isGovernmentApprovalProcedures());
                eaForm.getIdentification().setJointCriteria(activity.isJointCriteria());
                
                
                
                eaForm.getIdentification().setHumanitarianAid(activity.isHumanitarianAid());
                
                
                
                if(activity.getCrisNumber()!=null)
                    eaForm.getIdentification().setCrisNumber(activity.getCrisNumber().trim());
                
                if(activity.getModifiedBy()!=null){
                    eaForm.getIdentification().setModifiedBy(activity.getModifiedBy());
                }
                if(activity.getActivityCreator() != null) {
                    User usr = activity.getActivityCreator().getUser();
                    if(usr != null) {
                        eaForm.getIdentification().setActAthFirstName(usr.getFirstNames());
                        eaForm.getIdentification().setActAthLastName(usr.getLastName());
                        eaForm.getIdentification().setActAthEmail(usr.getEmail());
                        eaForm.getIdentification().setActAthAgencySource(usr.getOrganizationName());
                    }
                }
            }else{
                eaForm.getIdentification().setAmpId(null);
                eaForm.getIdentification().setTitle(null);
                eaForm.getIdentification().setObjectives(null);
                eaForm.getIdentification().setProjectComments(null);
                eaForm.getIdentification().setDescription(null);
                eaForm.getSectors().setActivitySectors(null);
//                eaForm.setSectorSchemes(null);
                eaForm.getPrograms().setActPrograms(null);
                eaForm.getPrograms().setProgramCollection(null);
                eaForm.getPlanning().setOriginalAppDate(null);
                eaForm.getPlanning().setRevisedAppDate(null);
                eaForm.getPlanning().setOriginalStartDate(null);
                eaForm.getPlanning().setOriginalCompDate(null);
                eaForm.getPlanning().setRevisedStartDate(null);
                eaForm.getPlanning().setCurrentCompDate(null);
                eaForm.getDocuments().setDocumentSpace(null);
                eaForm.getIdentification().setStatusReason(null);
                eaForm.getPlanning().setLineMinRank(null);
                eaForm.getPlanning().setLineMinRank(null);
                eaForm.getIdentification().setCreatedDate(null);
                eaForm.getIdentification().setUpdatedDate(null);
                eaForm.getPlanning().setOriginalAppDate(null);
                eaForm.getPlanning().setRevisedAppDate(null);
                eaForm.getPlanning().setOriginalStartDate(null);
                eaForm.getPlanning().setOriginalCompDate(null);
                eaForm.getPlanning().setRevisedStartDate(null);
                eaForm.getPlanning().setCurrentCompDate(null);
                eaForm.getPlanning().setProposedCompDate(null);
                eaForm.getPrograms().setActPrograms(null);
                eaForm.getFunding().setProProjCost(null);
                eaForm.getFunding().setRevProjCost(null);
                eaForm.getFunding().setRegionalFundings(null);
                eaForm.getComponents().setSelectedComponents(null);
                eaForm.getComponents().setCompTotalDisb(0);
                eaForm.getContactInfo().setDnrCntFirstName(null);
                eaForm.getContactInfo().setDnrCntLastName(null);
                eaForm.getContactInfo().setDnrCntEmail(null);
                eaForm.getContactInfo().setDnrCntTitle(null);
                eaForm.getContactInfo().setDnrCntOrganization(null);
                eaForm.getContactInfo().setDnrCntPhoneNumber(null);
                eaForm.getContactInfo().setDnrCntFaxNumber(null);
                
                eaForm.getContactInfo().setMfdCntFirstName(null);
                eaForm.getContactInfo().setMfdCntLastName(null);
                eaForm.getContactInfo().setMfdCntEmail(null);
                eaForm.getContactInfo().setMfdCntTitle(null);
                eaForm.getContactInfo().setMfdCntOrganization(null);
                eaForm.getContactInfo().setMfdCntPhoneNumber(null);
                eaForm.getContactInfo().setMfdCntFaxNumber(null);
                
                eaForm.getContactInfo().setPrjCoFirstName(null);
                eaForm.getContactInfo().setPrjCoLastName(null);
                eaForm.getContactInfo().setPrjCoEmail(null);
                eaForm.getContactInfo().setPrjCoTitle(null);
                eaForm.getContactInfo().setPrjCoOrganization(null);
                eaForm.getContactInfo().setPrjCoPhoneNumber(null);
                eaForm.getContactInfo().setPrjCoFaxNumber(null);
                
                eaForm.getContactInfo().setSecMiCntFirstName(null);
                eaForm.getContactInfo().setSecMiCntLastName(null);
                eaForm.getContactInfo().setSecMiCntEmail(null);
                eaForm.getContactInfo().setSecMiCntTitle(null);
                eaForm.getContactInfo().setSecMiCntOrganization(null);
                eaForm.getContactInfo().setSecMiCntPhoneNumber(null);
                eaForm.getContactInfo().setSecMiCntFaxNumber(null);
                
                eaForm.getIdentification().setActAthFirstName(null);
                eaForm.getIdentification().setActAthLastName(null);
                eaForm.getIdentification().setActAthEmail(null);
                eaForm.getIdentification().setActAthAgencySource(null);
                eaForm.getIdentification().setModifiedBy(null);
                
                eaForm.getIdentification().setProcurementSystem(null);
                eaForm.getIdentification().setReportingSystem(null);
                eaForm.getIdentification().setAuditSystem(null);
                eaForm.getIdentification().setInstitutions(null);
                eaForm.getIdentification().setAccessionInstrument(null);
                eaForm.getIdentification().setAcChapter(null);
                
                /*
                 * tanzania adds
                 */
                eaForm.getIdentification().setFY(null);
                eaForm.getIdentification().setVote(null);
                eaForm.getIdentification().setSubVote(null);
                eaForm.getIdentification().setSubProgram(null);
                eaForm.getIdentification().setProjectCode(null);
                eaForm.getIdentification().setMinistryCode(null);
//              eaForm.getIdentification().setGbsSbs(null);
                
                eaForm.getIdentification().setGovernmentApprovalProcedures(false);
                eaForm.getIdentification().setJointCriteria(false);               
                eaForm.getIdentification().setCrisNumber(null);
            
     }
    }
        return mapping.findForward("forward");
    }

    private ProposedProjCost getProposedProjectCost(EditActivityForm eaForm, AmpActivityVersion activity, 
            AmpFundingAmount.FundingType funType) {
        ProposedProjCost pg = new ProposedProjCost();
        AmpFundingAmount ppc = activity.getProjectCostByType(funType);
        AmpCurrency ppcCurrency;
        if (ppc != null && ppc.getCurrencyCode() != null) {
            ppcCurrency = CurrencyUtil.getCurrencyByCode(ppc.getCurrencyCode());
        } else {
            ppcCurrency = CurrencyUtil.getCurrencyByCode(eaForm.getCurrCode());
        }
        java.sql.Date ppcDate = ppc != null && ppc.getFunDate() != null ? new java.sql.Date(ppc.getFunDate().getTime()) : null;
        if (ppcDate == null)
            return null;
        double frmExRt = Util.getExchange(ppcCurrency.getCurrencyCode(), ppcDate);
        double toExRt = Util.getExchange(eaForm.getCurrCode(), ppcDate);
        double funAmount = ppc != null && ppc.getFunAmount() != null ? ppc.getFunAmount() : 0; 
        DecimalWraper amt = CurrencyWorker.convertWrapper(funAmount, frmExRt, toExRt, ppcDate);
        pg.setFunAmountAsDouble(amt.doubleValue());
        pg.setCurrencyCode(eaForm.getCurrCode());
        pg.setCurrencyName(eaForm.getCurrName());
        pg.setFunAmount(FormatHelper.formatNumber(amt.doubleValue()));
        pg.setFunDate(FormatHelper.formatDate(ppc == null ? null : ppc.getFunDate()));
        return pg;
    }

    private void getComponents(Collection componets, Long actId, EditActivityForm eaForm) {
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
                    tempComp.getComponentId());
            Iterator cItr = fundingComponentActivity.iterator();
            while (cItr.hasNext()) {
                AmpComponentFunding ampCompFund = (AmpComponentFunding) cItr
                        .next();

                double disb = 0;
                if (ampCompFund.getAdjustmentType().getValue().equals(CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey())
                    && ampCompFund.getTransactionType().intValue() == 1)
                    disb = ampCompFund.getTransactionAmount().doubleValue();

                eaForm.getComponents().setCompTotalDisb(eaForm.getComponents().getCompTotalDisb() + disb);
                FundingDetail fd = new FundingDetail();
                fd.setAdjustmentTypeName(ampCompFund.getAdjustmentType());

                fd.setAmpComponentFundingId(ampCompFund.getAmpComponentFundingId());
                fd.setCurrencyCode(ampCompFund.getCurrency().getCurrencyCode());
                fd.setCurrencyName(ampCompFund.getCurrency().getCurrencyName());
                fd.setTransactionAmount(FormatHelper.formatNumber(ampCompFund.getTransactionAmount().doubleValue()));
                fd.setTransactionDate(DateConversion.convertDateToLocalizedString(ampCompFund.getTransactionDate()));
                fd.setFiscalYear(DateConversion.convertDateToFiscalYearString(ampCompFund.getTransactionDate()));
                fd.setTransactionType(ampCompFund.getTransactionType().intValue());
                fd.setComponentOrganisation(ampCompFund.getReportingOrganization());
                fd.setComponentSecondResponsibleOrganization(ampCompFund.getComponentSecondResponsibleOrganization());
                fd.setComponentTransactionDescription(ampCompFund.getDescription());
                if (fd.getTransactionType() == 0) {
                    tempComp.getCommitments().add(fd);
                } else if (fd.getTransactionType() == 1) {
                    tempComp.getDisbursements().add(fd);
                } else if (fd.getTransactionType() == 2) {
                    tempComp.getExpenditures().add(fd);
                }
            }

            ComponentsUtil.calculateFinanceByYearInfo(tempComp,fundingComponentActivity);
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

        eaForm.getComponents().setSelectedComponents(selectedComponents);
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
  


