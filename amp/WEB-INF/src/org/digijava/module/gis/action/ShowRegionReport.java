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

        Object[] fundingList = GetFoundingDetails.getFundingsByLocations(
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

            String numberFormat = FeaturesUtil.getGlobalSettingValue(
                    "Default Number Format");
            NumberFormat formatter = new DecimalFormat(numberFormat);

            gisRegReportForm.setActualCommitmentsStr(formatter.format(ammount.
                    getCommitment().intValue()));

            gisRegReportForm.setActualDisbursementsStr(formatter.format(ammount.
                    getDisbursement().intValue()));

            gisRegReportForm.setActualExpendituresStr(formatter.format(ammount.
                    getExpenditure().intValue()));
        }

        return mapping.findForward("forward");
    }
}
