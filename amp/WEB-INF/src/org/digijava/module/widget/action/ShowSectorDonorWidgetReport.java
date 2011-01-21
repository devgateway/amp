package org.digijava.module.widget.action;


import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.widget.form.SectorDonorWidgetReportForm;
import org.digijava.module.widget.helper.ActivitySectorDonorFunding;
import org.digijava.module.widget.util.ChartWidgetUtil;
import org.digijava.module.widget.util.WidgetUtil;

public class ShowSectorDonorWidgetReport extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws DgException{
        SectorDonorWidgetReportForm cForm = (SectorDonorWidgetReportForm)form;
        Integer fromYear =new Integer(cForm.getStartYear());
        Integer toYear = new Integer(cForm.getEndYear());
        Long[] donorIDs = null;
        Long[] sectorIDs = null;
        String siteId = RequestUtils.getSiteDomain(request).getSite().getId().toString();
        String langCode= RequestUtils.getNavigationLanguage(request).getCode();

        //donors
        if (cForm.getDonorId() != null) {
            donorIDs = new Long[1];
            donorIDs[0] = cForm.getDonorId();
            cForm.setDonorName(DbUtil.getOrganisation(cForm.getDonorId()).getName());
        } else {
            try {
                cForm.setDonorName(TranslatorWorker.translateText("ALL", langCode, siteId));
            } catch (WorkerException ex) {
                new DgException(ex);
            }

        }
         String baseCurr = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY);
        if (baseCurr == null) {
            baseCurr = "USD";
        }
        cForm.setSelectedCurrency(baseCurr);

        // sectors
        sectorIDs=cForm.getSectorIds();
        if(sectorIDs.length==1){
            cForm.setSectorName(SectorUtil.getAmpSector(sectorIDs[0]).getName());
        }
        else{
            try {
                cForm.setSectorName(TranslatorWorker.translateText("Others", langCode, siteId));
            } catch (WorkerException ex) {
                new DgException(ex);
            }
        }

        Long defaultCalendarId = new Long(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_CALENDAR));
        AmpFiscalCalendar calendar = FiscalCalendarUtil.getAmpFiscalCalendar(defaultCalendarId);
        Date fromDate = ChartWidgetUtil.getStartOfYear(fromYear.intValue(), calendar.getStartMonthNum() - 1, calendar.getStartDayNum());
        //we need data including the last day of toYear,this is till the first day of toYear+1
        int MILLISECONDS_IN_DAY = 1000 * 60 * 60 * 24;
        Date toDate = new Date(ChartWidgetUtil.getStartOfYear(toYear.intValue() + 1, calendar.getStartMonthNum() - 1, calendar.getStartDayNum()).getTime() - MILLISECONDS_IN_DAY);
        Collection<ActivitySectorDonorFunding> actSectorDonorFundingInfo=WidgetUtil.getDonorSectorFunding(donorIDs, fromDate, toDate, sectorIDs);
        cForm.setActSectorDonorFundingInfo(actSectorDonorFundingInfo);
        double comm = 0;
        double disb = 0;
        double exp = 0;
        if(actSectorDonorFundingInfo!=null){
            Iterator<ActivitySectorDonorFunding> iter=actSectorDonorFundingInfo.iterator();
            while(iter.hasNext()){
                ActivitySectorDonorFunding funding=iter.next();
                if(funding.getCommitment()!=null){
                     comm+=funding.getCommitment().doubleValue();
                }
               if(funding.getDisbursement()!=null){
                   disb+=funding.getDisbursement().doubleValue();
               }
               if(funding.getExpenditure()!=null){
                   exp+=funding.getExpenditure().doubleValue();
               }
                     
            }
            if (comm != 0) {
                cForm.setActualCommitmentsStr(FormatHelper.formatNumber(comm));
            }

            if (disb != 0) {
                cForm.setActualDisbursementsStr(FormatHelper.formatNumber(disb));
            }
            if (exp != 0) {
                cForm.setActualExpendituresStr(FormatHelper.formatNumber(exp));
            }
            
            
        }

        return mapping.findForward("forward");
    }
}
