/*
 * ShowActivityPrintPreview.java
 * Created : 24-May-2005
 */

package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.util.ActivityUtil;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import org.digijava.module.aim.util.DocumentUtil;
import org.digijava.module.aim.helper.RegionalFunding;
import org.digijava.module.aim.dbentity.AmpCategoryValue;
import org.digijava.module.aim.dbentity.AmpRegionalFunding;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpMeasure;
import java.util.TreeSet;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.helper.CategoryConstants;
import org.digijava.module.aim.helper.CategoryManagerUtil;
import org.digijava.module.aim.helper.Location;
import org.digijava.module.aim.helper.DecimalToText;
import org.digijava.module.aim.helper.FundingValidator;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.dbentity.AmpActivityClosingDates;
import org.digijava.module.aim.dbentity.AmpIssues;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.cms.dbentity.CMSContentItem;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.kernel.request.Site;
import org.digijava.module.aim.helper.Constants;
import java.util.Date;
import org.digijava.module.aim.helper.OrgProjectId;
import org.digijava.module.aim.helper.RelatedLinks;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.helper.Components;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpActor;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.Documents;
import java.util.Collections;
import org.digijava.module.aim.dbentity.AmpComponentFunding;
import org.digijava.module.aim.helper.Measures;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.helper.PhysicalProgress;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.helper.FundingOrganization;
import org.digijava.module.aim.dbentity.AmpActivityInternalId;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.helper.Funding;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.form.ProposedProjCost;
import java.util.Set;
import org.digijava.module.aim.util.ComponentsUtil;
import org.digijava.module.aim.dbentity.AmpPhysicalPerformance;
import org.digijava.module.aim.helper.Issues;
import java.util.Iterator;
import org.digijava.module.aim.helper.TeamMember;
import javax.servlet.http.HttpSession;

public class ShowActivityPrintPreview
    extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {

        EditActivityForm eaForm = (EditActivityForm) form;
        String actId = request.getParameter("activityid");
        if(actId != null) {
            HttpSession session = request.getSession();
            TeamMember tm = (TeamMember) session.getAttribute("currentMember");
            AmpActivity activity = ActivityUtil.getAmpActivity(Long.valueOf(actId));
            if(activity != null){

                // set title,description and objective

                ProposedProjCost pg = new ProposedProjCost();
                pg.setFunAmountAsDouble(activity.getFunAmount());
                pg.setCurrencyCode(activity.getCurrencyCode());
                pg.setFunDate(activity.getFunDate());
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
                eaForm.setTitle(activity.getName().trim());
                eaForm.setDescription(activity.getDescription().trim());
                eaForm.setObjectives(activity.getObjective().trim());
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
                //String actApprovalStatus = DbUtil.getActivityApprovalStatus(id);
                eaForm.setStatus(DbUtil.getActivityApprovalStatus(new Long(Long.parseLong(actId))));
                
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

                eaForm.setProposedCompDate(DateConversion.ConvertDateToString(activity.getProposedCompletionDate()));
                eaForm.setContractors(activity.getContractors().trim());

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

                Collection ampLocs = activity.getLocations();

                if(ampLocs != null && ampLocs.size() > 0) {
                    Collection locs = new TreeSet();

                    Iterator locIter = ampLocs.iterator();
                    boolean maxLevel = false;
                    while(locIter.hasNext()) {
                        AmpLocation loc = (AmpLocation) locIter.next();
                        if(!maxLevel) {
                            if(loc.getAmpWoreda() != null) {
                                impLevel = 3;
                                maxLevel = true;
                            } else if(loc.getAmpZone() != null
                                      && impLevel < 2) {
                                impLevel = 2;
                            } else if(loc.getAmpRegion() != null
                                      && impLevel < 1) {
                                impLevel = 1;
                            }
                        }

                        if(loc != null) {
                            Location location = new Location();
                            location.setLocId(loc.getAmpLocationId());
                            Collection col1 = FeaturesUtil.getDefaultCountryISO();
                            String ISO = null;
                            Iterator itr1 = col1.iterator();
                            while(itr1.hasNext()) {
                                AmpGlobalSettings ampG = (AmpGlobalSettings) itr1.next();
                                ISO = ampG.getGlobalSettingsValue();
                            }

                            //Country cntry = DbUtil.getDgCountry(Constants.COUNTRY_ISO);
                            Country cntry = DbUtil.getDgCountry(ISO);
                            location.setCountryId(cntry.getCountryId());
                            location.setCountry(cntry.getCountryName());
                            if(loc.getAmpRegion() != null) {
                                location.setRegion(loc.getAmpRegion()
                                    .getName());
                                location.setRegionId(loc.getAmpRegion()
                                    .getAmpRegionId());
                                if(eaForm.getFundingRegions() == null) {
                                    eaForm
                                        .setFundingRegions(new ArrayList());
                                }
                                if(eaForm.getFundingRegions().contains(
                                    loc.getAmpRegion()) == false) {
                                    eaForm.getFundingRegions().add(
                                        loc.getAmpRegion());
                                }
                            }
                            if(loc.getAmpZone() != null) {
                                location
                                    .setZone(loc.getAmpZone().getName());
                                location.setZoneId(loc.getAmpZone()
                                    .getAmpZoneId());
                            }
                            if(loc.getAmpWoreda() != null) {
                                location.setWoreda(loc.getAmpWoreda()
                                    .getName());
                                location.setWoredaId(loc.getAmpWoreda()
                                    .getAmpWoredaId());
                            }
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

                if(sectors != null && sectors.size() > 0) {
                    Collection activitySectors = new ArrayList();
                    Iterator sectItr = sectors.iterator();
                    while(sectItr.hasNext()) {
                        AmpActivitySector ampActSect = (AmpActivitySector)
                            sectItr.next();
                        AmpSector sec = ampActSect.getSectorId();
                        if(sec != null) {
                            AmpSector parent = null;
                            AmpSector subsectorLevel1 = null;
                            AmpSector subsectorLevel2 = null;
                            if(sec.getParentSectorId() != null) {
                                if(sec.getParentSectorId().
                                   getParentSectorId() != null) {
                                    subsectorLevel2 = sec;
                                    subsectorLevel1 = sec.getParentSectorId();
                                    parent = sec.getParentSectorId().
                                        getParentSectorId();
                                } else {
                                    subsectorLevel1 = sec;
                                    parent = sec.getParentSectorId();
                                }
                            } else {
                                parent = sec;
                            }
                            ActivitySector actSect = new ActivitySector();
                            if(parent != null) {
                                actSect.setId(parent.getAmpSectorId());
                                actSect.setSectorId(parent.getAmpSectorId());
                                actSect.setSectorName(parent.getName());
                                if(subsectorLevel1 != null) {
                                    actSect.setSubsectorLevel1Id(
                                        subsectorLevel1.getAmpSectorId());
                                    actSect.setSubsectorLevel1Name(
                                        subsectorLevel1.getName());
                                    if(subsectorLevel2 != null) {
                                        actSect.setSubsectorLevel2Id(
                                            subsectorLevel2.getAmpSectorId());
                                        actSect.setSubsectorLevel2Name(
                                            subsectorLevel2.getName());
                                    }
                                }
                                actSect.setSectorPercentage(ampActSect.
                                    getSectorPercentage());
                            }
                            activitySectors.add(actSect);
                        }
                    }
                    eaForm.setActivitySectors(activitySectors);
                }

                if(activity.getThemeId() != null) {
                    eaForm
                        .setProgram(activity.getThemeId()
                                    .getAmpThemeId());
                }
                eaForm.setProgramDescription(activity
                                             .getProgramDescription().trim());

                double totComm = 0;
                double totDisb = 0;
                double totExp = 0;

                ArrayList fundingOrgs = new ArrayList();
                Iterator fundItr = activity.getFunding().iterator();
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
                    Collection fundDetails = ampFunding.getFundingDetails();
                    if(fundDetails != null && fundDetails.size() > 0) {
                        Iterator fundDetItr = fundDetails.iterator();
                        List fundDetail = new ArrayList();

                        long indexId = System.currentTimeMillis();
                        while(fundDetItr.hasNext()) {
                            AmpFundingDetail fundDet = (AmpFundingDetail)
                                fundDetItr
                                .next();
                            FundingDetail fundingDetail = new FundingDetail();
                            fundingDetail.setIndexId(indexId++);
                            int adjType = fundDet.getAdjustmentType()
                                .intValue();
                            fundingDetail.setAdjustmentType(adjType);
                            if(adjType == Constants.PLANNED) {
                                fundingDetail
                                    .setAdjustmentTypeName("Planned");
                            } else if(adjType == Constants.ACTUAL) {
                                fundingDetail
                                    .setAdjustmentTypeName("Actual");
                                Date dt = fundDet.getTransactionDate();
                                double frmExRt = CurrencyUtil.
                                    getExchangeRate(
                                        fundDet.getAmpCurrencyId()
                                        .getCurrencyCode(), 1, dt);
                                String toCurrCode = CurrencyUtil.
                                    getAmpcurrency(
                                        tm.getAppSettings()
                                        .getCurrencyId()).getCurrencyCode();
                                double toExRt = CurrencyUtil.
                                    getExchangeRate(toCurrCode, 1, dt);
                                double amt = CurrencyWorker.convert1(
                                    fundDet.getTransactionAmount()
                                    .doubleValue(), frmExRt,
                                    toExRt);
                                eaForm.setCurrCode(toCurrCode);
                                if(fundDet.getTransactionType().intValue() ==
                                   Constants.COMMITMENT) {
                                    totComm += amt;
                                } else if(fundDet.getTransactionType()
                                          .intValue() ==
                                          Constants.DISBURSEMENT) {
                                    totDisb += amt;
                                } else if(fundDet.getTransactionType()
                                          .intValue() ==
                                          Constants.EXPENDITURE) {
                                    totExp += amt;
                                }
                            }
                            if(fundDet.getTransactionType().intValue() ==
                               Constants.EXPENDITURE) {
                                fundingDetail.setClassification(fundDet
                                    .getExpCategory());
                            }
                            fundingDetail.setCurrencyCode(fundDet
                                .getAmpCurrencyId().getCurrencyCode());
                            fundingDetail.setCurrencyName(fundDet
                                .getAmpCurrencyId().getCountryName());

                            fundingDetail
                                .setTransactionAmount(CurrencyWorker
                                .convert(fundDet
                                         .getTransactionAmount()
                                         .doubleValue(), 1, 1));
                            fundingDetail.setTransactionDate(DateConversion
                                .ConvertDateToString(fundDet
                                .getTransactionDate()));

                            fundingDetail.setPerspectiveCode(fundDet.
                                getPerspectiveId().getCode());
                            fundingDetail.setPerspectiveName(fundDet.
                                getPerspectiveId().getName());

                            /*
                             fundingDetail.setPerspectiveCode(fundDet
                              .getOrgRoleCode());

                             Iterator itr1 = eaForm.getPerspectives()
                              .iterator();
                                     while (itr1.hasNext()) {
                             AmpPerspective pers = (AmpPerspective) itr1
                               .next();
                             if (pers.getCode().equals(
                               fundDet.getOrgRoleCode())) {
                              fundingDetail.setPerspectiveName(pers
                                .getName());
                             }
                                     }
                             */



                            fundingDetail.setTransactionType(fundDet
                                .getTransactionType().intValue());
                            fundDetail.add(fundingDetail);
                        }
                        if(fundDetail != null)
                            Collections.sort(fundDetail,
                                             FundingValidator.dateComp);
                        fund.setFundingDetails(fundDetail);
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
                //logger.info("size = " + fundingOrgs);
                eaForm.setFundingOrganizations(fundingOrgs);
                eaForm.setTotalCommitments(totComm);
                eaForm.setTotalDisbursements(totDisb);
                eaForm.setTotalExpenditures(totExp);

                ArrayList regFunds = new ArrayList();
                Iterator rItr = activity.getRegionalFundings().iterator();

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
                    fd.setPerspectiveCode(ampRegFund.getPerspective()
                                          .getCode());
                    fd.setPerspectiveName(ampRegFund.getPerspective()
                                          .getName());
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
                Collection componets = activity.getComponents();
                if(componets != null && componets.size() > 0) {
                    ArrayList comp = new ArrayList();
                    Iterator compItr = componets.iterator();
                    while(compItr.hasNext()) {
                        AmpComponent temp = (AmpComponent) compItr.next();
                        Components tempComp = new Components();
                        tempComp.setTitle(temp.getTitle());
                        tempComp.setComponentId(temp.getAmpComponentId());
                        if(temp.getDescription() == null) {
                            tempComp.setDescription(" ");
                        } else {
                            tempComp.setDescription(temp.getDescription()
                                .trim());
                        }
                        tempComp.setCommitments(new ArrayList());
                        tempComp.setDisbursements(new ArrayList());
                        tempComp.setExpenditures(new ArrayList());

                        //Iterator cItr = temp.getComponentFundings().iterator();
                        Iterator cItr = ComponentsUtil.getComponentFunding(
                            tempComp.getComponentId()).iterator();
                        while(cItr.hasNext()) {
                            AmpComponentFunding ampCompFund = (
                                AmpComponentFunding) cItr
                                .next();

                            /**
                             * If the funding wasn't created for this activity it should be ignored.
                             */
                            if(ampCompFund.getActivity().getAmpActivityId().longValue() != activity.getAmpActivityId().longValue()) {
                                continue;
                            }
                            double disb = 0;
                            if(ampCompFund.getAdjustmentType().intValue() ==
                               1 &&
                               ampCompFund.getTransactionType().intValue() ==
                               1)
                                disb = ampCompFund.getTransactionAmount().
                                    doubleValue();
                            //if(!ampCompFund.getCurrency().getCurrencyCode().equals("USD")) {
                            //double toRate=1;

                            //	disb/=ARUtil.getExchange(ampCompFund.getCurrency().getCurrencyCode(),new java.sql.Date(ampCompFund.getTransactionDate().getTime()));
                            //}
                            eaForm.setCompTotalDisb(eaForm.getCompTotalDisb() +
                                disb);
                            FundingDetail fd = new FundingDetail();
                            fd.setAdjustmentType(ampCompFund
                                                 .getAdjustmentType().intValue());
                            if(fd.getAdjustmentType() == 1) {
                                fd.setAdjustmentTypeName("Actual");
                            } else if(fd.getAdjustmentType() == 0) {
                                fd.setAdjustmentTypeName("Planned");
                            }
                            fd.setCurrencyCode(ampCompFund.getCurrency()
                                               .getCurrencyCode());
                            fd.setCurrencyName(ampCompFund.getCurrency()
                                               .getCurrencyName());
                            fd.setPerspectiveCode(ampCompFund
                                                  .getPerspective().getCode());
                            fd.setPerspectiveName(ampCompFund
                                                  .getPerspective().getName());
                            fd.setTransactionAmount(DecimalToText
                                .ConvertDecimalToText(ampCompFund
                                .getTransactionAmount()
                                .doubleValue()));
                            fd.setTransactionDate(DateConversion
                                                  .ConvertDateToString(ampCompFund
                                .getTransactionDate()));
                            fd.setTransactionType(ampCompFund
                                                  .getTransactionType().intValue());

                            if(fd.getTransactionType() == 0) {
                                tempComp.getCommitments().add(fd);
                            } else if(fd.getTransactionType() == 1) {
                                tempComp.getDisbursements().add(fd);
                            } else if(fd.getTransactionType() == 2) {
                                tempComp.getExpenditures().add(fd);
                            }
                        }

                        //Collection phyProgess = temp.getPhysicalProgress();
                        Collection phyProgess = ComponentsUtil.
                            getComponentPhysicalProgress(tempComp.
                            getComponentId());
                        if(phyProgess != null && phyProgess.size() > 0) {
                            Collection physicalProgress = new ArrayList();
                            Iterator phyProgItr = phyProgess.iterator();
                            while(phyProgItr.hasNext()) {
                                AmpPhysicalPerformance phyPerf = (
                                    AmpPhysicalPerformance) phyProgItr
                                    .next();
                                PhysicalProgress phyProg = new
                                    PhysicalProgress();
                                phyProg.setPid(phyPerf.getAmpPpId());
                                phyProg.setDescription(phyPerf
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

                    // Sort the funding details based on Transaction date.
                    itr1 = comp.iterator();
                    index = 0;
                    while(itr1.hasNext()) {
                        Components components = (Components) itr1.next();
                        List list = null;
                        if(components.getCommitments() != null) {
                            list = new ArrayList(components
                                                 .getCommitments());
                            Collections.sort(list,
                                             FundingValidator.dateComp);
                        }
                        components.setCommitments(list);
                        list = null;
                        if(components.getDisbursements() != null) {
                            list = new ArrayList(components
                                                 .getDisbursements());
                            Collections.sort(list,
                                             FundingValidator.dateComp);
                        }
                        components.setDisbursements(list);
                        list = null;
                        if(components.getExpenditures() != null) {
                            list = new ArrayList(components
                                                 .getExpenditures());
                            Collections.sort(list,
                                             FundingValidator.dateComp);
                        }
                        components.setExpenditures(list);
                        comp.set(index++, components);
                    }

                    eaForm.setSelectedComponents(comp);
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
                eaForm.setReportingOrgs(new ArrayList());
                Set relOrgs = activity.getOrgrole();
                if(relOrgs != null) {
                    Iterator relOrgsItr = relOrgs.iterator();
                    while(relOrgsItr.hasNext()) {
                        AmpOrgRole orgRole = (AmpOrgRole) relOrgsItr.next();
                        if(orgRole.getRole().getRoleCode().equals(
                            Constants.EXECUTING_AGENCY)
                           && (!eaForm
                               .getExecutingAgencies()
                               .contains(orgRole.getOrganisation()))) {
                            eaForm.getExecutingAgencies().add(
                                orgRole.getOrganisation());
                        } else if(orgRole.getRole().getRoleCode().equals(
                            Constants.IMPLEMENTING_AGENCY)
                                  && (!eaForm.getImpAgencies().contains(
                                      orgRole.getOrganisation()))) {
                            eaForm.getImpAgencies().add(
                                orgRole.getOrganisation());
                        } else if(orgRole.getRole().getRoleCode().equals(
                            Constants.REPORTING_AGENCY)
                                  && (!eaForm.getReportingOrgs().contains(
                                      orgRole.getOrganisation()))) {
                            eaForm.getReportingOrgs().add(
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

                eaForm.setConditions(activity.getCondition().trim());
                
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
                
                
                
                if(activity.getActivityCreator() != null) {
                    User usr = activity.getActivityCreator().getUser();
                    if(usr != null) {
                        eaForm.setActAthFirstName(usr.getFirstNames());
                        eaForm.setActAthLastName(usr.getLastName());
                        eaForm.setActAthEmail(usr.getEmail());
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
				
                eaForm.setConditions(null);
                eaForm.setActAthFirstName(null);
                eaForm.setActAthLastName(null);
                eaForm.setActAthEmail(null);
                
                /*
				 * tanzania adds
				 */
				eaForm.setFY(null);
				eaForm.setVote(null);
				eaForm.setSubVote(null);
				eaForm.setSubProgram(null);
				eaForm.setProjectCode(null);
				eaForm.setGbsSbs(new Integer(0));
				eaForm.setGovernmentApprovalProcedures(false);
				eaForm.setJointCriteria(false);
                
                
            }
        }
        return mapping.findForward("forward");
    }
}
