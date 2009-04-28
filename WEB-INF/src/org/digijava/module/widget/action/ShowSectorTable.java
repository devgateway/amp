package org.digijava.module.widget.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.widget.dbentity.AmpSectorOrder;
import org.digijava.module.widget.dbentity.AmpSectorTableWidget;
import org.digijava.module.widget.dbentity.AmpSectorTableYear;
import org.digijava.module.widget.form.ShowSectorTableForm;
import org.digijava.module.widget.helper.OrderHelper;
import org.digijava.module.widget.helper.SectorTableHelper;
import org.digijava.module.widget.util.ChartWidgetUtil;
import org.digijava.module.widget.util.SectorTableWidgetUtil;

public class ShowSectorTable extends Action {

    private static Logger logger = Logger.getLogger(ShowSectorTable.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ShowSectorTableForm tableForm = (ShowSectorTableForm) form;

        // translate headers
        Long siteId = RequestUtils.getSiteDomain(request).getSite().getId();
        String langCode = RequestUtils.getNavigationLanguage(request).getCode();
        String headingUSMil = TranslatorWorker.translateText("US$ millions", langCode, siteId);
        String headingFY = TranslatorWorker.translateText("FY", langCode, siteId);
        String headingPercent = TranslatorWorker.translateText("Of", langCode, siteId);
        String headingOther = TranslatorWorker.translateText("Other", langCode, siteId);
        String headingTotal = TranslatorWorker.translateText("Total", langCode, siteId);

          Comparator<OrderHelper> SENIORITY_ORDER = new Comparator<OrderHelper>() {

                public int compare(OrderHelper ord1, OrderHelper ord2) {
                    return ord1.getOrder().compareTo(ord2.getOrder());
                }
            };



        List<String> years = new ArrayList<String>();
        List<SectorTableHelper> sectorsInfo = new ArrayList<SectorTableHelper>();

        HashMap<String, OrderHelper> totalExceptOthers = new HashMap<String, OrderHelper>();
        HashMap<String, OrderHelper> totalValues  = new HashMap<String, OrderHelper>();

        String fiscalCalendarId = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_CALENDAR);
        AmpFiscalCalendar calendar = FiscalCalendarUtil.getAmpFiscalCalendar(Long.parseLong(fiscalCalendarId));

        AmpSectorTableWidget secTableWidget = SectorTableWidgetUtil.getAmpSectorTableWidget(tableForm.getWidgetId());
        List<AmpSectorTableYear> sectorTableYears = new ArrayList(secTableWidget.getYears());
        List<AmpSectorOrder> sectorOrders = new ArrayList(secTableWidget.getSectorsColumns());
        Iterator<AmpSectorOrder> sectorOrderIter = sectorOrders.iterator();

        SectorTableHelper sectorTableRowOther = new SectorTableHelper();
        sectorTableRowOther.setSectorName(headingOther);

        SectorTableHelper sectorTableRowEmpty = new SectorTableHelper();
        sectorTableRowEmpty.setEmptyRow(true);

        SectorTableHelper sectorTableRowTotal = new SectorTableHelper();
        sectorTableRowTotal.setSectorName(headingTotal);
        sectorTableRowTotal.setApplyStyle(true);

        while (sectorOrderIter.hasNext()) {
            AmpSectorOrder sectorTableRow = sectorOrderIter.next();
            SectorTableHelper sectorTable = new SectorTableHelper();
            sectorTable.setSectorName(sectorTableRow.getSector().getName());
            HashMap<String, OrderHelper> cols = new HashMap<String, OrderHelper>();
            Iterator<AmpSectorTableYear> totalYearIter = sectorTableYears.iterator();
            while (totalYearIter.hasNext()) {
                AmpSectorTableYear sectorTableYear = totalYearIter.next();
                Long year = sectorTableYear.getYear();
                Long sectorId = sectorTableRow.getSector().getAmpSectorId();
                Date startDate = ChartWidgetUtil.getStartOfYear(year.intValue(), calendar.getStartMonthNum() - 1, calendar.getStartDayNum());
                //we need data including the last day of toYear,this is till the first day of toYear+1
                int MILLISECONDS_IN_DAY = 1000 * 60 * 60 * 24;
                Date endDate = new Date(ChartWidgetUtil.getStartOfYear(year.intValue() + 1, calendar.getStartMonthNum() - 1, calendar.getStartDayNum()).getTime() - MILLISECONDS_IN_DAY);
                Long amount = SectorTableWidgetUtil.calculateFunding(year, new Long[]{sectorId}, startDate, endDate);
                Long wholeAmount = SectorTableWidgetUtil.calculateFunding(year, null, startDate, endDate);
                String heading = "";
                if (sectorTableYear.getType().equals(AmpSectorTableYear.TOTAL_TYPE_YEAR)) {
                    if (calendar.getIsFiscal()) {
                        heading = headingFY + " " + year + "/" + (year + 1) + " (" + headingUSMil + ")";
                    } else {
                        heading = year + " (" + headingUSMil + ")";
                    }
                    if (!totalValues.containsKey(year)) {
                        totalValues.put(year.toString(),  new OrderHelper( wholeAmount, sectorTableYear.getOrder(), AmpSectorTableYear.TOTAL_TYPE_YEAR,year.toString()));
                    }
                    OrderHelper totalColumn = new OrderHelper(amount, sectorTableYear.getOrder(), AmpSectorTableYear.TOTAL_TYPE_YEAR, year.toString());
                    cols.put(year.toString(),totalColumn);

                    if (!totalExceptOthers.containsKey(year.toString())) {
                        totalExceptOthers.put(year.toString(),totalColumn);
                    } else {
                        OrderHelper helper = totalExceptOthers.get(year.toString());
                        Long value = helper.getValue();
                        helper.setValue(value + amount);
                        totalExceptOthers.put(year.toString(), helper);
                    }
                } else {

                    if (calendar.getIsFiscal()) {
                        heading = headingFY + " % " + headingPercent + " " + year + "/" + (year + 1);
                    } else {
                        heading = " % " + headingPercent + " " + year;
                    }
                    long percent = 0;
                    if (wholeAmount != 0) {
                        percent = Math.round(1.0 * amount / wholeAmount * 100);
                       
                    }
                     if (!totalValues.containsKey(year+ "%") ) {
                         long wholePercent=0;
                         if (wholeAmount != 0) {
                             wholePercent=100;
                         }
                           totalValues.put(year.toString() + "%", new OrderHelper(wholePercent, sectorTableYear.getOrder(), AmpSectorTableYear.PERCENT_TYPE_YEAR,year.toString() + "%"));
                      }
                    OrderHelper percentColumn = new OrderHelper(percent, sectorTableYear.getOrder(), AmpSectorTableYear.PERCENT_TYPE_YEAR, year.toString() + "%");

                    cols.put(year.toString()+ "%",percentColumn);
                    if (!totalExceptOthers.containsKey(year.toString() + "%")) {
                        totalExceptOthers.put(year.toString() + "%",percentColumn);
                    } else {
                        OrderHelper helper = totalExceptOthers.get(year.toString() + "%");
                        Long value = helper.getValue();
                        helper.setValue(value + percent);
                        totalExceptOthers.put(year.toString() + "%", helper);
                    }
                }
                if (!years.contains(heading)) {
                    years.add(heading);
                }
            }
            List<OrderHelper> orderHelpers = new ArrayList(cols.values());
            Collections.sort(orderHelpers, SENIORITY_ORDER);
            Iterator<OrderHelper> orderHelperIter = orderHelpers.iterator();
            List<String> values = new ArrayList<String>();
            while (orderHelperIter.hasNext()) {
                OrderHelper orderHelper = orderHelperIter.next();
                String value = orderHelper.getValue().toString();
                if (orderHelper.getType().equals(AmpSectorTableYear.PERCENT_TYPE_YEAR)) {
                    value += "%";
                }
                values.add(value);
            }
            sectorTable.setValues(values);
            sectorsInfo.add(sectorTable);
        }
        tableForm.setYears(years);
        List<OrderHelper> totals = new ArrayList(totalExceptOthers.values());
        Collections.sort(totals, SENIORITY_ORDER);
        Iterator<OrderHelper> orderHelperTotal = totals.iterator();
        List<String> values = new ArrayList<String>();
        List<String> otherValues = new ArrayList<String>();
           while (orderHelperTotal.hasNext()) {
            OrderHelper orderHelper = orderHelperTotal.next();
            OrderHelper exceptOtherOrderHelper=totalExceptOthers.get(orderHelper.getYear());
            OrderHelper total=totalValues.get(orderHelper.getYear());
            String otherValue=(total.getValue()-exceptOtherOrderHelper.getValue())+"";
            String value = total.getValue().toString();
            if (orderHelper.getType().equals(AmpSectorTableYear.PERCENT_TYPE_YEAR)) {
                value += "%";
                otherValue+="%";
            }
            values.add(value);
            otherValues.add(otherValue);
        }
        sectorTableRowTotal.setValues(values);
        sectorTableRowOther.setValues(otherValues);
        sectorsInfo.add(sectorTableRowOther);
        sectorsInfo.add(sectorTableRowEmpty);
        sectorsInfo.add(sectorTableRowTotal);
        tableForm.setSectorsInfo(sectorsInfo);

        return mapping.findForward("forward");
    }
}


