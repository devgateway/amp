package org.digijava.module.gis.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.logic.FundingCalculationsHelper;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.gis.dbentity.GisMap;
import org.digijava.module.gis.form.GisRegReportForm;
import org.digijava.module.gis.util.ActivityLocationFunding;
import org.digijava.module.gis.util.DbUtil;
import org.digijava.module.gis.util.FundingData;


/**
 * GIS Dashboard renderer action.
 * @author Irakli Kobiashvili
 *
 */
public class ShowRegionReport extends Action {

	@Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        response.setContentType("text/html");

        TeamMember tm = null;
        Long teamId = null;
        boolean curWorkspaceOnly = request.getParameter("curWorkspaceOnly") != null && request.getParameter("curWorkspaceOnly").equalsIgnoreCase("true");
        if (curWorkspaceOnly) {
            tm = (TeamMember)request.getSession().getAttribute("currentMember");
            if (tm != null) {
                AmpTeam team = TeamUtil.getTeamByName(tm.getTeamName());
                teamId = team.getAmpTeamId();
            }
        }

        GisRegReportForm gisRegReportForm = (GisRegReportForm) form;
        String baseCurr = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY);
        if (baseCurr == null) {
            baseCurr = "USD";
        }
        //currently we are using base currency but in the future we may use value, selected from currency breakdown.
        gisRegReportForm.setSelectedCurrency(baseCurr);
        Calendar fStartDate = null;
        if (gisRegReportForm.getStartYear() != null) {
            fStartDate = Calendar.getInstance();
            fStartDate.set(Integer.parseInt(gisRegReportForm.getStartYear()), 0,
                           1, 0, 0, 0);

        }

        Calendar fEndDate = null;
        if (gisRegReportForm.getEndYear() != null) {
            fEndDate = Calendar.getInstance();
            fEndDate.set(Integer.parseInt(gisRegReportForm.getEndYear()), 11,
                         31, 23, 59, 59);

        }

        String secIdStr = gisRegReportForm.getSectorIdStr();
        Long secId = null;
        Long prgId = null;
        int sectorQueryType = 0;
        if (secIdStr.startsWith("sec_scheme_id_")) {
            sectorQueryType = DbUtil.SELECT_SECTOR_SCHEME;
            secId = new Long(secIdStr.substring(14));
        } else if (secIdStr.startsWith("sec_id_")) {
            sectorQueryType = DbUtil.SELECT_SECTOR;
            secId = new Long(secIdStr.substring(7));
        } else if (secIdStr.startsWith("prj_id_")) {
            sectorQueryType = DbUtil.SELECT_PROGRAM;
            prgId = new Long(secIdStr.substring(7));
        } else {
            sectorQueryType = DbUtil.SELECT_DEFAULT;
            secId = new Long(secIdStr);
        }

        List secFundings;
        if (request.getParameter("donorid")!=null && !request.getParameter("donorid").equalsIgnoreCase("-1")){	
        	Long donorid =new Long(request.getParameter("donorid"));
            if (sectorQueryType != DbUtil.SELECT_PROGRAM) {
        	    secFundings = DbUtil.getSectorFoundingsByDonor(secId,donorid, sectorQueryType);
            } else {
                secFundings = DbUtil.getProgramFoundingsByDonor(prgId, donorid);
            }
        }else{

            if (sectorQueryType != DbUtil.SELECT_PROGRAM) {
                if (request.getParameter("public") != null && request.getParameter("public").equals("true")) {
                    secFundings = DbUtil.getSectorFoundingsPublic(secId, sectorQueryType);
                } else {
        	        secFundings = DbUtil.getSectorFoundings(secId, sectorQueryType, teamId, false);
                }
            } else {
                secFundings = DbUtil.getProgramFoundings(prgId);
            }

        }

        Long regLocId = Long.parseLong(request.getParameter("regLocId"));

        String regionName = DbUtil.getLocationNameById(regLocId);
        gisRegReportForm.setRegName(regionName);

        Object[] fundingList = getFundingsForLocation(
                regLocId,
                secFundings,
                new Integer(gisRegReportForm.getMapLevel()),
                fStartDate.getTime(),
                fEndDate.getTime());

        Map fundingLocationMap = (Map) fundingList[0];
        //FundingData totalFunding = (FundingData) fundingList[1];

        FundingData ammount = (FundingData) fundingLocationMap.
                              get(regLocId);

        Long primarySectorClasId = SectorUtil.getPrimaryConfigClassificationId();






        //AmpSector selSector = SectorUtil.getAmpSector(secId, sectorQueryType);
        String secName = null;
        if (sectorQueryType == DbUtil.SELECT_SECTOR_SCHEME) {
            AmpSectorScheme scheme = SectorUtil.getAmpSectorScheme(secId);
            if (scheme != null) {
                secName = scheme.getSecSchemeName();
            }
        } else {
            AmpSector sec = SectorUtil.getAmpSector(secId);
            if (sec != null) {
                secName = sec.getName();
            }
        }



        if (secName != null) {
            gisRegReportForm.setSelSectorName(secName);
        } else {
            gisRegReportForm.setSelSectorName("All");
        }

        if (ammount != null) {
        		gisRegReportForm.setActualCommitmentsStr(FormatHelper.formatNumber(ammount.getCommitment().doubleValue()));
                gisRegReportForm.setActualDisbursementsStr(FormatHelper.formatNumber(ammount.getDisbursement().doubleValue()));
                gisRegReportForm.setActualExpendituresStr(FormatHelper.formatNumber(ammount.getExpenditure().doubleValue()));

            gisRegReportForm.setActivityLocationFundingList(ammount.
                    getActivityLocationFundingList());
        }


        gisRegReportForm.setPrimarySectorSchemeId(primarySectorClasId);

        return mapping.findForward("forward");
    }

    private Object[] getFundingsForLocation(Long regLocCode,
                                            List activityList, int level,
                                            Date start, Date end) throws
            Exception {

        Map locationFundingMap = new HashMap();
        FundingData totalFundingForSector = new FundingData();
        Long primarySectorClasId = SectorUtil.getPrimaryConfigClassificationId();
        FundingCalculationsHelper fch = new FundingCalculationsHelper();
        String baseCurr	= FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY);
        if ( baseCurr == null ){
            baseCurr	= "USD";
        }

        //Calculate total funding
        if (activityList != null) {
            Iterator<Object[]> actIt = activityList.iterator();
            while (actIt.hasNext()) {
                Object[] actData = actIt.next();
                AmpActivity activity = (AmpActivity) actData[0];

                Set topLevelSectorNames = new HashSet();
                Iterator secIt = activity.getSectors().iterator();
                while (secIt.hasNext()) {
                    AmpSector sec = ((AmpActivitySector) secIt.next()).getSectorId();
                    if (sec.getAmpSecSchemeId().getAmpSecSchemeId().equals(primarySectorClasId)) {
                        topLevelSectorNames.add(getTopLevelSector(sec).getName());
                    }
                }

                Float percentsForSectorSelected = actData[1] != null ? (Float) actData[1] : new Float(0);
                FundingData totalFunding = GetFoundingDetails.
                                           getActivityTotalFundingInBaseCurrency(
                        activity, start, end);

                totalFundingForSector.setCommitment(totalFundingForSector.
                        getCommitment().add(totalFunding.getCommitment()).
                        multiply(new BigDecimal((percentsForSectorSelected /
                                                 100f))));
                totalFundingForSector.setDisbursement(totalFundingForSector.
                        getDisbursement().add(totalFunding.getDisbursement()).
                        multiply(new BigDecimal(percentsForSectorSelected /
                                                100f)));
                totalFundingForSector.setExpenditure(totalFundingForSector.
                        getExpenditure().add(totalFunding.getExpenditure()).
                        multiply(new BigDecimal(percentsForSectorSelected /
                                                100f)));

                FundingData fundingForSector = new FundingData();
                fundingForSector.setCommitment(totalFunding.getCommitment().
                                               multiply(new BigDecimal(
                        percentsForSectorSelected / 100f)));
                fundingForSector.setDisbursement(totalFunding.getDisbursement().
                                                 multiply(new BigDecimal(
                        percentsForSectorSelected / 100f)));
                fundingForSector.setExpenditure(totalFunding.getExpenditure().
                                                multiply(new BigDecimal(
                        percentsForSectorSelected / 100f)));

                Collection locations = activity.getLocations();
                Iterator<AmpActivityLocation> locIt = locations.iterator();

                while (locIt.hasNext()) {
                    AmpActivityLocation loc = locIt.next();



                    if (loc.getLocationPercentage() != null &&
                        loc.getLocationPercentage().floatValue() > 0.0f &&
                        loc.getLocation().getRegionLocation() != null &&
                        (fundingForSector.getCommitment().floatValue() != 0f ||
                         fundingForSector.getDisbursement().floatValue() != 0f ||
                         fundingForSector.getExpenditure().floatValue() != 0f)) {

                        Long regCode = loc.getLocation().
                                             getRegionLocation().getId();
                             if (regCode.equals(regLocCode)) {

                                Set donorOrg = new HashSet();

                                if (activity.getFunding() != null) {
                                    Iterator acFIt = activity.getFunding().iterator();
                                    while (acFIt.hasNext()) {
                                        AmpFunding fnd = (AmpFunding) acFIt.next();
                                        donorOrg.add(fnd.getAmpDonorOrgId().getName());
                                    }
                                }


                        if ((level == GisMap.MAP_LEVEL_REGION &&
                            loc.getLocation().getRegionLocation().
                            getParentCategoryValue().getEncodedValue().equals(
                                "Region")) ||
                                (level == GisMap.MAP_LEVEL_DISTRICT &&
                                   loc.getLocation().getRegionLocation().
                                   getParentCategoryValue().getEncodedValue().
                                   equals("Zone"))) {



                            if (regCode.equals(regLocCode)) {

                                if (locationFundingMap.containsKey(regCode)) {
                                    FundingData existingVal = (FundingData)
                                            locationFundingMap.get(regCode);

                                    FundingData newVal = new FundingData();
                                    newVal.setActivityLocationFundingList(
                                            existingVal.
                                            getActivityLocationFundingList());

                                    newVal.setCommitment(existingVal.
                                                         getCommitment().add(
                                                                 fundingForSector.
                                                                 getCommitment().multiply(new
                                            BigDecimal(loc.
                                                       getLocationPercentage() / 100f))));

                                    newVal.setDisbursement(existingVal.
                                                           getDisbursement().add(
                                                                   fundingForSector.getDisbursement().
                                                                   multiply(new BigDecimal(loc.
                                            getLocationPercentage() / 100f))));

                                    newVal.setExpenditure(existingVal.
                                                          getExpenditure().add(
                                                                  fundingForSector.
                                                                  getExpenditure().multiply(new
                                            BigDecimal(loc.
                                                       getLocationPercentage() / 100f))));

                                    ActivityLocationFunding activityLocationFunding = new ActivityLocationFunding(
                                   		fundingForSector.getCommitment().multiply(new BigDecimal(loc.getLocationPercentage() / 100f)),
                                   		fundingForSector.getDisbursement().multiply(new BigDecimal(loc.getLocationPercentage() / 100f)),
                                   		fundingForSector.getExpenditure().multiply(new BigDecimal(loc.getLocationPercentage() / 100f)),
                                        activity);

                                    activityLocationFunding.setDonorOrgs(donorOrg);
                                    activityLocationFunding.setTopSectors(topLevelSectorNames);



                                    newVal.getActivityLocationFundingList().add(
                                            activityLocationFunding);

                                    if (activityLocationFunding.
                                            getCommitment().intValue()!= 0) {
                                        activityLocationFunding.setFmtCommitment(FormatHelper.formatNumber(
                                        		(activityLocationFunding.getCommitment().doubleValue())));
                                    } else {
                                           activityLocationFunding.setFmtCommitment(null);
                                    }

                                    if (activityLocationFunding.
                                            getDisbursement().intValue() != 0) {
                                        activityLocationFunding.setFmtDisbursement(FormatHelper.formatNumber(
                                                        activityLocationFunding.getDisbursement().doubleValue()));
                                    } else {
                                        activityLocationFunding.setFmtDisbursement(null);
                                    }

                                    if (activityLocationFunding.
                                            getExpenditure().intValue() != 0) {
                                        activityLocationFunding.setFmtExpenditure(FormatHelper.formatNumber(activityLocationFunding.
                                                        getExpenditure().doubleValue()));
                                    } else {
                                        activityLocationFunding.setFmtExpenditure(null);
                                    }
                                    locationFundingMap.put(regCode, newVal);
                                } else {
                                    if (fundingForSector.getCommitment().
                                        floatValue() != 0f ||
                                        fundingForSector.getDisbursement().
                                        floatValue() != 0f ||
                                        fundingForSector.getExpenditure().
                                        floatValue() != 0f) {

                                        FundingData newVal = new FundingData();
                                        newVal.setActivityLocationFundingList(new
                                                ArrayList());

                                        newVal.setCommitment(fundingForSector.
                                                getCommitment().multiply(new
                                                BigDecimal(loc.
                                                getLocationPercentage() / 100f)));
                                        newVal.setDisbursement(fundingForSector.
                                                getDisbursement().multiply(new
                                                BigDecimal(loc.
                                                getLocationPercentage() / 100f)));
                                        newVal.setExpenditure(fundingForSector.
                                                getExpenditure().multiply(new
                                                BigDecimal(loc.
                                                getLocationPercentage() / 100f)));

                                       ActivityLocationFunding activityLocationFunding = new ActivityLocationFunding(
                                    		   fundingForSector.getCommitment().multiply(new BigDecimal(loc.getLocationPercentage() / 100f)),
                                    		   fundingForSector.getDisbursement().multiply(new BigDecimal(loc.getLocationPercentage() / 100f)),
                                    		   fundingForSector.getExpenditure().multiply(new BigDecimal(loc.getLocationPercentage() / 100f)),
                                    		   activity);
                                        activityLocationFunding.setDonorOrgs(donorOrg);
                                        activityLocationFunding.setTopSectors(topLevelSectorNames);
                                        newVal.getActivityLocationFundingList().
                                        add(activityLocationFunding);
                                        
                                        if (activityLocationFunding.getCommitment().intValue() != 0) {
                                        	activityLocationFunding.setFmtCommitment(FormatHelper.formatNumber(activityLocationFunding.
                                        			getCommitment().doubleValue()));
                                        } else {
                                            activityLocationFunding.
                                                    setFmtCommitment(null);
                                        }
                                        if (activityLocationFunding.getDisbursement().intValue() != 0) {
                                            activityLocationFunding.setFmtDisbursement(FormatHelper.formatNumber(
                                            		activityLocationFunding.getDisbursement().doubleValue()));
                                        } else {
                                            activityLocationFunding.
                                                    setFmtDisbursement(null);
                                        }
                                        if (activityLocationFunding.getExpenditure().intValue() != 0) {
                                        	activityLocationFunding.setFmtExpenditure(FormatHelper.
                                        			formatNumber(activityLocationFunding.getExpenditure().doubleValue()));
                                        } else {
                                            activityLocationFunding.
                                                    setFmtExpenditure(null);
                                        }
                                        locationFundingMap.put(regCode, newVal);
                                    }
                                }
                            }
                         }
                        }
                    }
                }

                //Regional funding calculations (Ethiopian issue AMP-9960)
                    if (activity.getRegionalFundings() != null && !activity.getRegionalFundings().isEmpty()) {

                        Set donorOrg = new HashSet();


                        for (Object regFndObj : activity.getRegionalFundings()) {
                            AmpRegionalFunding regFnd = (AmpRegionalFunding) regFndObj;
                            Long regCode = regFnd.getRegionLocation().getId();
                            if (regFnd.getReportingOrganization() != null) {
                                donorOrg.add(regFnd.getReportingOrganization().getName());
                            }

                            if (((level == GisMap.MAP_LEVEL_REGION &&
                                    regFnd.getRegionLocation().getParentCategoryValue().getEncodedValue().equals("Region"))
                                    || (level == GisMap.MAP_LEVEL_DISTRICT &&
                                    regFnd.getRegionLocation().getParentCategoryValue().getEncodedValue().equals("Zone"))) && regCode.equals(regLocCode)) {


                                List regionalFundingCalculations = new ArrayList();
                                AmpFundingDetail forCalculations = new AmpFundingDetail();
                                forCalculations.setAmpCurrencyId(regFnd.getCurrency());
                                forCalculations.setTransactionAmount(regFnd.getTransactionAmount());
                                forCalculations.setTransactionDate(regFnd.getTransactionDate());
                                forCalculations.setTransactionType(regFnd.getTransactionType());
                                regionalFundingCalculations.add(forCalculations);

                                fch.doCalculations(regionalFundingCalculations, baseCurr);

                                if (locationFundingMap.containsKey(regCode)) {


                                    FundingData existingVal = (FundingData) locationFundingMap.get(regCode);

                                    switch (regFnd.getTransactionType()) {
                                        case Constants.COMMITMENT:
                                            existingVal.setCommitment(existingVal.getCommitment().add(fch.getTotActualComm().getValue()));
                                            break;
                                        case Constants.DISBURSEMENT:
                                            existingVal.setDisbursement(existingVal.getDisbursement().add(fch.getTotActualDisb().getValue()));
                                            break;
                                        case Constants.EXPENDITURE:
                                            existingVal.setExpenditure(existingVal.getExpenditure().add(fch.getTotActualExp().getValue()));
                                            break;
                                    }

                                } else {
                                    if (regFnd.getTransactionAmount() > 0) {
                                        FundingData newVal = new FundingData();


                                        switch (regFnd.getTransactionType()) {
                                        case Constants.COMMITMENT:
                                            newVal.setCommitment(fch.getTotActualComm().getValue());
                                            break;
                                        case Constants.DISBURSEMENT:
                                            newVal.setDisbursement(fch.getTotActualDisb().getValue());
                                            break;
                                        case Constants.EXPENDITURE:
                                            newVal.setExpenditure(fch.getTotActualExp().getValue());
                                            break;
                                        }



                                        locationFundingMap.put(regCode, newVal);
                                    }
                                }
                            }
                        }
                        //Set activity
                        FundingData existingVal = (FundingData) locationFundingMap.get(regLocCode);
                        ActivityLocationFunding activityLocationFunding = new ActivityLocationFunding();
                        activityLocationFunding.setActivity(activity);
                        activityLocationFunding.setDonorOrgs(donorOrg);
                        activityLocationFunding.setTopSectors(topLevelSectorNames);

                        activityLocationFunding.setCommitment(existingVal.getCommitment());
                        activityLocationFunding.setExpenditure(existingVal.getExpenditure());
                        activityLocationFunding.setDisbursement(existingVal.getDisbursement());


                        if (activityLocationFunding.getCommitment() != null && activityLocationFunding.getCommitment().intValue() != 0) {
                            activityLocationFunding.setFmtCommitment(FormatHelper.formatNumber(activityLocationFunding.
                                    getCommitment().doubleValue()));
                        } else {
                            activityLocationFunding.
                                    setFmtCommitment(null);
                        }
                        if (activityLocationFunding.getDisbursement() != null && activityLocationFunding.getDisbursement().intValue() != 0) {
                            activityLocationFunding.setFmtDisbursement(FormatHelper.formatNumber(
                                    activityLocationFunding.getDisbursement().doubleValue()));
                        } else {
                            activityLocationFunding.
                                    setFmtDisbursement(null);
                        }
                        if (activityLocationFunding.getExpenditure() != null && activityLocationFunding.getExpenditure().intValue() != 0) {
                            activityLocationFunding.setFmtExpenditure(FormatHelper.
                                    formatNumber(activityLocationFunding.getExpenditure().doubleValue()));
                        } else {
                            activityLocationFunding.
                                    setFmtExpenditure(null);
                        }
                        if (existingVal.getActivityLocationFundingList() == null) {
                            existingVal.setActivityLocationFundingList(new ArrayList());
                        }
                        existingVal.getActivityLocationFundingList().add(activityLocationFunding);


                    }
            }
        }

        Object[] retVal = new Object[2];
        retVal[0] = locationFundingMap;
        retVal[1] = totalFundingForSector;
        return retVal;
    }

    private AmpSector getTopLevelSector(AmpSector sector) {
        AmpSector retVal = sector;
        while (retVal.getParentSectorId() != null) {
           retVal = retVal.getParentSectorId();
        }
        return retVal;
    }

}
