package org.digijava.module.widget.action;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.widget.dbentity.AmpSectorOrder;
import org.digijava.module.widget.dbentity.AmpSectorTableWidget;
import org.digijava.module.widget.form.WidgetTeaserForm;
import org.digijava.module.widget.helper.SectorTableHelper;
import org.digijava.module.widget.util.ChartWidgetUtil;
import org.digijava.module.widget.util.SectorTableWidgetUtil;

public class GetSectorDonorAmounts extends Action {

    public static final String ROOT_TAG = "DonorSectorList";
    public static final String SECTOR_TAG = "Sector";

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        WidgetTeaserForm wForm = (WidgetTeaserForm) form;


        response.setContentType("text/xml");
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";


        String widgetId = request.getParameter("widgetId");
        String donorId = request.getParameter("donorId");
        DecimalFormat format=FormatHelper.getDecimalFormat();
        String fiscalCalendarId = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_CALENDAR);
        AmpFiscalCalendar calendar = FiscalCalendarUtil.getAmpFiscalCalendar(Long.parseLong(fiscalCalendarId));
        xml += "<" + ROOT_TAG + ">";
        try {
            AmpSectorTableWidget secTableWidget = SectorTableWidgetUtil.getAmpSectorTableWidget(Long.parseLong(widgetId));
            List<AmpSectorOrder> sectorOrders = new ArrayList<AmpSectorOrder>(secTableWidget.getSectorsColumns());
            Long year = secTableWidget.getDonorYear();
            Date startDate = ChartWidgetUtil.getStartOfYear(year.intValue(), calendar.getStartMonthNum() - 1, calendar.getStartDayNum());
            //we need data including the last day of toYear,this is till the first day of toYear+1
            int MILLISECONDS_IN_DAY = 1000 * 60 * 60 * 24;
            Date endDate = new Date(ChartWidgetUtil.getStartOfYear(year.intValue() + 1, calendar.getStartMonthNum() - 1, calendar.getStartDayNum()).getTime() - MILLISECONDS_IN_DAY);
            double totalAmount = SectorTableWidgetUtil.calculateFunding(new Long[]{Long.parseLong(donorId)}, null, startDate, endDate);
            double exceptOtherAmount =0;
            Iterator<AmpSectorOrder> sectorOrderIter = sectorOrders.iterator();
            while (sectorOrderIter.hasNext()) {
                AmpSectorOrder sectorTableRow = sectorOrderIter.next();
                Long sectorId = sectorTableRow.getSector().getAmpSectorId();
                if (sectorId.equals(SectorTableHelper.TOTAL_ROW_SECTOR_ID) || sectorId.equals(SectorTableHelper.OTHER_ROW_SECTOR_ID) || sectorId.equals(SectorTableHelper.EMPTY_ROW_SECTOR_ID)) {
                    continue;
                }
                Double amount = SectorTableWidgetUtil.calculateFunding(new Long[]{Long.parseLong(donorId)}, new Long[]{sectorId}, startDate, endDate);
                exceptOtherAmount += amount;
                xml += "<" + SECTOR_TAG + " ";
                xml += " id=\"" + "donor_" + widgetId + "_" + sectorId + "\" ";
                xml += " amount=\"" + format.format(amount) + "\" ";
                if (donorId.equals("-1")) {
                    xml += " empty=\"" + true + "\" ";
                } else {
                    xml += " empty=\"" + false + "\" ";
                }
                xml += "/>";


            }
            // others
            xml += "<" + SECTOR_TAG + " ";
            xml += " id=\"" + "donor_" + widgetId + "_" + SectorTableHelper.OTHER_ROW_SECTOR_ID + "\"";
            xml += " amount=\"" + format.format(totalAmount - exceptOtherAmount) + "\" ";
            if (donorId.equals("-1")) {
                xml += " empty=\"" + true + "\" ";
            } else {
                xml += " empty=\"" + false + "\" ";
            }
            xml += "/>";
            //total
            xml += "<" + SECTOR_TAG + " ";
            xml += " id=\"" + "donor_" + widgetId + "_" + SectorTableHelper.TOTAL_ROW_SECTOR_ID + "\" ";
            xml += " amount=\"" + format.format(totalAmount) + "\" ";
            if (donorId.equals("-1")) {
                xml += " empty=\"" + true + "\" ";
            } else {
                xml += " empty=\"" + false + "\" ";
            }
            xml += "/>";

            xml += "</" + ROOT_TAG + "> ";
            response.getWriter().print(xml);
        } catch (Exception e) {
            // TODO handle this exception
            e.printStackTrace();
        }


        return null;
    }
}
