package org.digijava.module.gis.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.gis.util.DbUtil;
import java.util.Calendar;
import org.digijava.module.gis.form.GisRegReportForm;
import org.apache.ecs.xml.XML;
import java.util.Map;
import org.digijava.module.aim.util.FeaturesUtil;
import java.text.DecimalFormat;
import org.apache.ecs.xml.XMLDocument;
import java.text.NumberFormat;
import org.digijava.module.gis.util.FundingData;
import java.util.List;
import java.util.Iterator;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.aim.dbentity.AmpActivity;
import java.math.BigDecimal;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import java.util.HashMap;
import java.util.Date;
import org.digijava.module.gis.dbentity.GisMap;
import java.util.Set;
import java.util.ArrayList;
import org.digijava.module.gis.util.ActivityLocationFunding;


/**
 * GIS Dashboard renderer action.
 * @author Irakli Kobiashvili
 *
 */
public class ShowRegionReport extends Action {

    private NumberFormat formatter = null;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        GisRegReportForm gisRegReportForm = (GisRegReportForm) form;

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

        List secFundings = DbUtil.getSectorFoundings(gisRegReportForm.
                getSectorId());

        String numberFormat = FeaturesUtil.getGlobalSettingValue(
                            "Default Number Format");
            this.formatter = new DecimalFormat(numberFormat);

        Object[] fundingList = getFundingsForLocation(
                gisRegReportForm.getRegCode(),
                secFundings,
                new Integer(gisRegReportForm.getMapLevel()),
                fStartDate.getTime(),
                fEndDate.getTime());

        Map fundingLocationMap = (Map) fundingList[0];
        //FundingData totalFunding = (FundingData) fundingList[1];

        FundingData ammount = (FundingData) fundingLocationMap.
                              get(gisRegReportForm.getRegCode());

        AmpSector selSector = SectorUtil.getAmpSector(gisRegReportForm.
                getSectorId());

        if (selSector != null) {
            gisRegReportForm.setSelSectorName(selSector.getName());
        } else {
            gisRegReportForm.setSelSectorName("All");
        }

        if (ammount != null) {



                gisRegReportForm.setActualCommitmentsStr(formatter.format(ammount.
                        getCommitment().intValue()));
                gisRegReportForm.setActualDisbursementsStr(formatter.format(ammount.
                        getDisbursement().intValue()));
                gisRegReportForm.setActualExpendituresStr(formatter.format(ammount.
                        getExpenditure().intValue()));

            gisRegReportForm.setActivityLocationFundingList(ammount.
                    getActivityLocationFundingList());
        }

        return mapping.findForward("forward");
    }

    private Object[] getFundingsForLocation(String locationCode,
                                            List activityList, int level,
                                            Date start, Date end) throws
            Exception {

        Map locationFundingMap = new HashMap();
        FundingData totalFundingForSector = new FundingData();
        //Calculate total funding
        if (activityList != null) {
            Iterator<Object[]> actIt = activityList.iterator();
            while (actIt.hasNext()) {
                Object[] actData = actIt.next();
                AmpActivity activity = (AmpActivity) actData[0];
                Float percentsForSectorSelected = (Float) actData[1];
                FundingData totalFunding = GetFoundingDetails.
                                           getActivityTotalFundingInUSD(
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

                Set locations = activity.getLocations();
                Iterator<AmpActivityLocation> locIt = locations.iterator();

                while (locIt.hasNext()) {
                    AmpActivityLocation loc = locIt.next();



                    if (loc.getLocationPercentage() != null &&
                        loc.getLocationPercentage().floatValue() > 0.0f &&
                        loc.getLocation().getRegionLocation() != null &&
                        (fundingForSector.getCommitment().floatValue() != 0f ||
                         fundingForSector.getDisbursement().floatValue() != 0f ||
                         fundingForSector.getExpenditure().floatValue() != 0f)) {

                        String regCode = loc.getLocation().
                                             getRegionLocation().getName();
                            if (regCode.equals(locationCode)) {

                        if (level == GisMap.MAP_LEVEL_REGION &&
                            loc.getLocation().getRegionLocation().
                            getParentCategoryValue().getEncodedValue().equals(
                                "Region")) {



                            if (regCode.equals(locationCode)) {

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


                                    newVal.getActivityLocationFundingList().add(
                                            activityLocationFunding);

                                    if (activityLocationFunding.
                                            getCommitment().intValue()!= 0) {
                                        activityLocationFunding.setFmtCommitment(
                                                formatter.format(
                                                        activityLocationFunding.
                                                        getCommitment().intValue()));
                                    } else {
                                           activityLocationFunding.setFmtCommitment(null);
                                    }

                                    if (activityLocationFunding.
                                            getDisbursement().intValue() != 0) {
                                        activityLocationFunding.setFmtDisbursement(
                                                formatter.format(
                                                        activityLocationFunding.
                                                        getDisbursement().intValue()));
                                    } else {
                                        activityLocationFunding.setFmtDisbursement(null);
                                    }

                                    if (activityLocationFunding.
                                            getExpenditure().intValue() != 0) {
                                        activityLocationFunding.setFmtExpenditure(
                                                formatter.format(
                                                        activityLocationFunding.
                                                        getExpenditure().intValue()));
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

                                        newVal.getActivityLocationFundingList().
                                                add(activityLocationFunding);

                                        if (activityLocationFunding.
                                                getCommitment().intValue() != 0) {
                                            activityLocationFunding.
                                                    setFmtCommitment(formatter.
                                                                     format(activityLocationFunding.
                                                                            getCommitment().intValue()));
                                        } else {
                                            activityLocationFunding.
                                                    setFmtCommitment(null);
                                        }

                                        if (activityLocationFunding.
                                                getDisbursement().intValue() != 0) {
                                            activityLocationFunding.
                                                    setFmtDisbursement(formatter.
                                                                       format(activityLocationFunding.
                                                                              getDisbursement().intValue()));
                                        } else {
                                            activityLocationFunding.
                                                    setFmtDisbursement(null);
                                        }

                                        if (activityLocationFunding.
                                                getExpenditure().intValue() != 0) {
                                            activityLocationFunding.
                                                    setFmtExpenditure(formatter.
                                                                      format(activityLocationFunding.
                                                                             getExpenditure().intValue()));
                                        } else {
                                            activityLocationFunding.
                                                    setFmtExpenditure(null);
                                        }

                                        locationFundingMap.put(regCode, newVal);
                                    }
                                }
                            }
                        } else if (level == GisMap.MAP_LEVEL_DISTRICT &&
                                   loc.getLocation().getRegionLocation().
                                   getParentCategoryValue().getEncodedValue().
                                   equals("Zone")) {


                            if (regCode.equals(locationCode)) {
                                if (locationFundingMap.containsKey(regCode)) {
                                    FundingData existingVal = (FundingData)
                                            locationFundingMap.get(
                                            regCode);

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

                                    ActivityLocationFunding
                                            activityLocationFunding = new
                                            ActivityLocationFunding(
                                            fundingForSector.getCommitment().
                                            multiply(new BigDecimal(loc.
                                            getLocationPercentage() / 100f)),
                                            fundingForSector.getDisbursement().
                                            multiply(new BigDecimal(loc.
                                            getLocationPercentage() / 100f)),
                                            fundingForSector.getExpenditure().
                                            multiply(new BigDecimal(loc.
                                            getLocationPercentage() / 100f)),
                                            activity);

                                    newVal.getActivityLocationFundingList().add(
                                            activityLocationFunding);

                                    if (activityLocationFunding.
                                            getCommitment().intValue() != 0) {
                                        activityLocationFunding.setFmtCommitment(
                                                formatter.format(
                                                        activityLocationFunding.
                                                        getCommitment().intValue()));
                                    } else {
                                        activityLocationFunding.
                                                setFmtCommitment(null);
                                    }

                                    if (activityLocationFunding.
                                            getDisbursement().intValue() != 0) {
                                        activityLocationFunding.setFmtDisbursement(
                                                formatter.format(
                                                        activityLocationFunding.
                                                        getDisbursement().intValue()));
                                    } else {
                                        activityLocationFunding.
                                                setFmtDisbursement(null);
                                    }

                                    if (activityLocationFunding.
                                            getExpenditure().intValue() != 0) {
                                        activityLocationFunding.setFmtExpenditure(
                                                formatter.format(
                                                        activityLocationFunding.
                                                        getExpenditure().intValue()));
                                    } else {
                                        activityLocationFunding.
                                                setFmtExpenditure(null);
                                    }

                                    locationFundingMap.put(regCode, newVal);
                                } else {
                                    if (fundingForSector.getCommitment().
                                        floatValue() !=
                                        0f ||
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

                                        ActivityLocationFunding
                                                activityLocationFunding = new
                                                ActivityLocationFunding(
                                                fundingForSector.getCommitment().
                                                multiply(new BigDecimal(loc.
                                                getLocationPercentage() / 100f)),
                                                fundingForSector.
                                                getDisbursement().multiply(new
                                                BigDecimal(loc.
                                                getLocationPercentage() / 100f)),
                                                fundingForSector.getExpenditure().
                                                multiply(new BigDecimal(loc.
                                                getLocationPercentage() / 100f)),
                                                activity);

                                        if (activityLocationFunding.
                                                getCommitment().intValue() != 0) {
                                            activityLocationFunding.
                                                    setFmtCommitment(formatter.
                                                                     format(activityLocationFunding.
                                                                            getCommitment().intValue()));
                                        } else {
                                            activityLocationFunding.
                                                    setFmtCommitment(null);
                                        }

                                        if (activityLocationFunding.
                                                getDisbursement().intValue() != 0) {
                                            activityLocationFunding.
                                                    setFmtDisbursement(formatter.
                                                                       format(activityLocationFunding.
                                                                              getDisbursement().intValue()));
                                        } else {
                                            activityLocationFunding.
                                                    setFmtDisbursement(null);
                                        }

                                        if (activityLocationFunding.
                                                getExpenditure().intValue() != 0) {
                                            activityLocationFunding.
                                                    setFmtExpenditure(formatter.
                                                                      format(activityLocationFunding.
                                                                             getExpenditure().intValue()));
                                        } else {
                                            activityLocationFunding.
                                                    setFmtExpenditure(null);
                                        }
                                        newVal.getActivityLocationFundingList().
                                                add(activityLocationFunding);

                                        locationFundingMap.put(regCode, newVal);
                                    }
                                }
                            }
                        }
                        }
                    }
                }
            }
        }

        Object[] retVal = new Object[2];
        retVal[0] = locationFundingMap;
        retVal[1] = totalFundingForSector;
        return retVal;
    }

}
