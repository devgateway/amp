package org.digijava.module.gis.action;

import java.util.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.gis.form.GisRegReportForm;
import org.digijava.module.gis.util.*;


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
        GisRegReportForm gisRegReportForm = (GisRegReportForm) form;
        HttpSession session = request.getSession();

        TeamMember tm = (TeamMember) session.getAttribute("currentMember");
        if(tm==null){
        	gisRegReportForm.setFromPublicView(Boolean.TRUE);
        }
        else{
        	gisRegReportForm.setFromPublicView(Boolean.FALSE);
        }


        boolean isDevinfo = request.getParameter("devInfo")!=null && request.getParameter("devInfo").trim().compareToIgnoreCase("true") == 0;

        if (!isDevinfo) {
            GisFilterForm filterForm = GisUtil.parseFilterRequest(request);
            List <Long> selSecIDs = null;
            if (filterForm.getSelectedSectors() != null) {
                selSecIDs = new ArrayList<Long>(Arrays.asList(filterForm.getSelectedSectors()));
            } else {
                selSecIDs = new ArrayList<Long>();
            }

            if (filterForm.getSelectedSecondarySectors() != null) {
                selSecIDs.addAll(new ArrayList<Long>(Arrays.asList(filterForm.getSelectedSecondarySectors())));
            }
            if (filterForm.getSelectedTertiarySectors() != null) {
                selSecIDs.addAll(new ArrayList<Long>(Arrays.asList(filterForm.getSelectedTertiarySectors())));
            }

            gisRegReportForm.setFilterAllSectors(filterForm.isFilterAllSectors());
            if (!filterForm.isFilterAllSectors()) {
                List<String> secNames = DbUtil.getTopSectorNames(selSecIDs);
                gisRegReportForm.setSelSectorNames(secNames);
            }


            gisRegReportForm.setStartYear(filterForm.getFilterStartYear());
            gisRegReportForm.setEndYear(filterForm.getFilterEndYear());

            Long regLocId = Long.parseLong(request.getParameter("regLocId"));
            String isRegSetStr = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.GIS_FUNDING_TYPE);
            boolean isRegional = (isRegSetStr == null || isRegSetStr.trim().equalsIgnoreCase("donor"))?GisUtil.GIS_DONOR_FUNDINGS:GisUtil.GIS_REGIONAL_FUNDINGS;
            boolean isPublic = request.getParameter("isPublic") == null?false:true;
            Object[] filterResults = RMMapCalculationUtil.getFundingsFilteredForRegReport(filterForm, regLocId, isRegional, isPublic, !gisRegReportForm.getAllSecondarySectors());

            FundingData fndDat = null;
            if (filterResults[0] != null && ((Map)filterResults[0]).size() > 0) {
                fndDat = (FundingData)((Map)filterResults[0]).values().iterator().next();
            } else {
                fndDat = new FundingData();
            }

            gisRegReportForm.setActivityLocationFundingList(fndDat.getActivityLocationFundingList());

            if (fndDat.getCommitment() != null) {
                gisRegReportForm.setActualCommitmentsStr(FormatHelper.formatNumber(fndDat.getCommitment().doubleValue()));
            }

            if (fndDat.getExpenditure() != null) {
                gisRegReportForm.setActualExpendituresStr(FormatHelper.formatNumber(fndDat.getExpenditure().doubleValue()));
            }

            if (fndDat.getDisbursement() != null) {
                gisRegReportForm.setActualDisbursementsStr(FormatHelper.formatNumber(fndDat.getDisbursement().doubleValue()));
            }
            
            if (fndDat.getPlannedDisbursement() != null) {
                gisRegReportForm.setPlannedDisbursementsStr(FormatHelper.formatNumber(fndDat.getPlannedDisbursement().doubleValue()));
            }

            Long teamId = null;
            boolean curWorkspaceOnly = request.getParameter("curWorkspaceOnly") != null && request.getParameter("curWorkspaceOnly").equalsIgnoreCase("true");
            if (curWorkspaceOnly) {
               
                if (tm != null) {
                    AmpTeam team = TeamUtil.getTeamByName(tm.getTeamName());
                    teamId = team.getAmpTeamId();
                }
            }

            gisRegReportForm.setSelectedCurrency(filterForm.getSelectedCurrency());
            String regionName = DbUtil.getLocationNameById(regLocId);
            gisRegReportForm.setRegName(regionName);
        } else {
        //DevInfo mode
            GisFilterForm gisFilterForm = new GisFilterForm();
            String sectorIdStr = request.getParameter("sectorIdStr");
            List <Long> selSecIDs = new ArrayList <Long>();
            if (sectorIdStr != null) {
                selSecIDs.add(new Long(sectorIdStr));
            }
            List<String> secNames = DbUtil.getTopSectorNames(selSecIDs);
            gisRegReportForm.setSelSectorNames(secNames);
            Long[] secArr = new Long[selSecIDs.size()];
            selSecIDs.toArray(secArr);
            gisFilterForm.setSelectedSectors(secArr);

            gisRegReportForm.setStartYear(gisRegReportForm.getStartYear());
            gisRegReportForm.setEndYear(gisRegReportForm.getEndYear());
            gisFilterForm.setFilterStartYear(gisRegReportForm.getStartYear());
            gisFilterForm.setFilterEndYear(gisRegReportForm.getEndYear());


            String baseCurr = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY);
            if (baseCurr == null) {
                baseCurr = "USD";
            }
            gisFilterForm.setSelectedCurrency(baseCurr);

            Long regLocId = Long.parseLong(request.getParameter("regLocId"));
            String isRegSetStr = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.GIS_FUNDING_TYPE);
            boolean isRegional = (isRegSetStr == null || isRegSetStr.trim().equalsIgnoreCase("donor"))?GisUtil.GIS_DONOR_FUNDINGS:GisUtil.GIS_REGIONAL_FUNDINGS;
            boolean isPublic = request.getParameter("isPublic") == null?false:true;
            Object[] filterResults = RMMapCalculationUtil.getFundingsFilteredForRegReport(gisFilterForm, regLocId, isRegional, isPublic, !gisRegReportForm.getAllSecondarySectors());

            FundingData fndDat = null;
            if (filterResults[0] != null && ((Map)filterResults[0]).size() > 0) {
                fndDat = (FundingData)((Map)filterResults[0]).values().iterator().next();
            } else {
                fndDat = new FundingData();
            }



            gisRegReportForm.setActivityLocationFundingList(fndDat.getActivityLocationFundingList());

            if (fndDat.getCommitment() != null) {
                gisRegReportForm.setActualCommitmentsStr(FormatHelper.formatNumber(fndDat.getCommitment().doubleValue()));
            }

            if (fndDat.getExpenditure() != null) {
                gisRegReportForm.setActualExpendituresStr(FormatHelper.formatNumber(fndDat.getExpenditure().doubleValue()));
            }

            if (fndDat.getDisbursement() != null) {
                gisRegReportForm.setActualDisbursementsStr(FormatHelper.formatNumber(fndDat.getDisbursement().doubleValue()));
            }

            if (fndDat.getPlannedDisbursement() != null) {
                gisRegReportForm.setPlannedDisbursementsStr(FormatHelper.formatNumber(fndDat.getPlannedDisbursement().doubleValue()));
            }

            gisRegReportForm.setSelectedCurrency(gisFilterForm.getSelectedCurrency());
            String regionName = DbUtil.getLocationNameById(regLocId);
            gisRegReportForm.setRegName(regionName);
        }
        return mapping.findForward("forward");
    }



}
