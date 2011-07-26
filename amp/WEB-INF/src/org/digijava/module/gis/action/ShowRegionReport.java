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
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.gis.dbentity.GisMap;
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

        GisFilterForm filterForm = GisUtil.parseFilterRequest(request);

        gisRegReportForm.setStartYear(filterForm.getFilterStartYear());
        gisRegReportForm.setEndYear(filterForm.getFilterEndYear());

        Long regLocId = Long.parseLong(request.getParameter("regLocId"));

        Object[] filterResults = RMMapCalculationUtil.getFundingsFilteredForRegReport(filterForm, regLocId);

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


        /*
        String baseCurr = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY);
        if (baseCurr == null) {
            baseCurr = "USD";
        }
        //currently we are using base currency but in the future we may use value, selected from currency breakdown.
        gisRegReportForm.setSelectedCurrency(baseCurr);
        */


        gisRegReportForm.setSelectedCurrency(filterForm.getSelectedCurrency());





        String regionName = DbUtil.getLocationNameById(regLocId);
        gisRegReportForm.setRegName(regionName);




        return mapping.findForward("forward");
    }



}
