package org.digijava.module.widget.action;

import java.util.ArrayList;
import java.util.Collections;
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
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.widget.dbentity.AmpSectorOrder;
import org.digijava.module.widget.dbentity.AmpSectorTableWidget;
import org.digijava.module.widget.dbentity.AmpSectorTableYear;
import org.digijava.module.widget.form.ShowSectorTableForm;
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
        AmpSectorTableWidget secTableWidget = SectorTableWidgetUtil.getAmpSectorTableWidget(tableForm.getWidgetId());
        List<AmpSectorOrder> sectorOrders = new ArrayList(secTableWidget.getSectorsColumns());
        List<String> years = new ArrayList();
        List<AmpSectorTableYear> totalYears = new ArrayList(secTableWidget.getTotalYears());
        List<AmpSectorTableYear> percentYears = new ArrayList(secTableWidget.getPercentYears());
        List<SectorTableHelper> sectorsInfo = new ArrayList<SectorTableHelper>();
        HashMap<Long, Long> totalForYearExceptOthers = new HashMap<Long, Long>();
        HashMap<Long, Long> totalPercentForYearExceptOthers = new HashMap<Long, Long>();
        HashMap<Long, Long> totalForYear = new HashMap<Long, Long>();
        HashMap<Long, Long> percentTotalForYear = new HashMap<Long, Long>();
        String fiscalCalendarId = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_CALENDAR);
        AmpFiscalCalendar calendar = FiscalCalendarUtil.getAmpFiscalCalendar(Long.parseLong(fiscalCalendarId));

        boolean totalYearsAdded = false;
        boolean percentYearsAdded = false;
        Iterator<AmpSectorOrder> sectorOrderIter = sectorOrders.iterator();
        while (sectorOrderIter.hasNext()) {
            AmpSectorOrder sectorTableRow = sectorOrderIter.next();
            SectorTableHelper sectorTable = new SectorTableHelper();
            sectorTable.setSectorName(sectorTableRow.getSector().getName());
            List<Long> percentValues = new ArrayList<Long>();
            List<Long> totalValues = new ArrayList<Long>();
            Iterator<AmpSectorTableYear> totalYearIter = totalYears.iterator();
            while (totalYearIter.hasNext()) {
                AmpSectorTableYear totalYear = totalYearIter.next();
                Long year = totalYear.getYear();
                Long sectorId = sectorTableRow.getSector().getAmpSectorId();
                Date startDate = ChartWidgetUtil.getStartOfYear(year.intValue(), calendar.getStartMonthNum() - 1, calendar.getStartDayNum());
                //we need data including the last day of toYear,this is till the first day of toYear+1
                int MILLISECONDS_IN_DAY = 1000 * 60 * 60 * 24;
                Date endDate = new Date(ChartWidgetUtil.getStartOfYear(year.intValue() + 1, calendar.getStartMonthNum() - 1, calendar.getStartDayNum()).getTime() - MILLISECONDS_IN_DAY);
                Long amount = SectorTableWidgetUtil.calculateFunding(year, new Long[]{sectorId}, startDate, endDate);
                Long wholeAmount = SectorTableWidgetUtil.calculateFunding(year, null, startDate, endDate);
                if (!totalForYear.containsKey(year)) {
                    totalForYear.put(year, wholeAmount);
                }

                if (!totalYearsAdded) {
                    if (calendar.getIsFiscal()) {
                        years.add(year + "/" + (year + 1));
                    } else {
                        years.add(year + "");
                    }
                }
                totalValues.add(amount);
                if (!totalForYearExceptOthers.containsKey(year)) {
                    totalForYearExceptOthers.put(year, amount);
                } else {
                    Long totalAmount = totalForYearExceptOthers.get(year);
                    totalAmount += amount;
                    totalForYearExceptOthers.put(year, totalAmount);
                }
            }
            totalYearsAdded = true;
            sectorTable.setTotalYearsValue(totalValues);
            Iterator<AmpSectorTableYear> percentYearIter = percentYears.iterator();
            while (percentYearIter.hasNext()) {
                AmpSectorTableYear percentYear = percentYearIter.next();
                Long year = percentYear.getYear();
                Long sectorId = sectorTableRow.getSector().getAmpSectorId();
                Date startDate = ChartWidgetUtil.getStartOfYear(year.intValue(), calendar.getStartMonthNum() - 1, calendar.getStartDayNum());
                //we need data including the last day of toYear,this is till the first day of toYear+1
                int MILLISECONDS_IN_DAY = 1000 * 60 * 60 * 24;
                Date endDate = new Date(ChartWidgetUtil.getStartOfYear(year.intValue() + 1, calendar.getStartMonthNum() - 1, calendar.getStartDayNum()).getTime() - MILLISECONDS_IN_DAY);
                Long amount = SectorTableWidgetUtil.calculateFunding(year, new Long[]{sectorId}, startDate, endDate);
                Long wholeAmount = SectorTableWidgetUtil.calculateFunding(year, null, startDate, endDate);
                if (!percentTotalForYear.containsKey(year)) {
                    Long wholePercent=0l;
                    if (wholeAmount != 0) {
                        wholePercent=100l;
                    }
                    percentTotalForYear.put(year,wholePercent);
                }
                long percent = 0;
                if (wholeAmount != 0) {
                    percent = Math.round(1.0*amount / wholeAmount * 100);
                    percentValues.add(percent);
                } else {
                    percentValues.add(percent);
                }
                if (!percentYearsAdded) {
                    if (calendar.getIsFiscal()) {
                        years.add(year + "/" + (year + 1) + " %");
                    } else {
                        years.add(year + "%");
                    }
                }
                if (!totalPercentForYearExceptOthers.containsKey(year)) {
                    totalPercentForYearExceptOthers.put(year, percent);
                } else {
                    Long totalPercent = totalPercentForYearExceptOthers.get(year);
                    totalPercent += percent;
                    totalPercentForYearExceptOthers.put(year, totalPercent);
                }
            }
            sectorTable.setPercentYearsValue(percentValues);
            percentYearsAdded = true;
            sectorsInfo.add(sectorTable);
        }
        SectorTableHelper otherSectorTableRow = new SectorTableHelper();
        otherSectorTableRow.setSectorName("Others");
        otherSectorTableRow.setTotalYearsValue(new ArrayList());
        otherSectorTableRow.setPercentYearsValue(new ArrayList());
        List totalValuesExceptOthers = new ArrayList(totalForYearExceptOthers.keySet());
        Collections.sort(totalValuesExceptOthers);
        List<Long> totalValues = new ArrayList<Long>();
        List<Long> totalPercentValues = new ArrayList<Long>();
        List totalPercentExceptOthers = new ArrayList(totalPercentForYearExceptOthers.keySet());
        Collections.sort(totalPercentExceptOthers);
        Iterator<Long> totValueIter = totalValuesExceptOthers.iterator();
        while (totValueIter.hasNext()) {
            Long keyYear = totValueIter.next();
            Long wholeValue = totalForYear.get(keyYear);
            Long exceptOthersValue = totalForYearExceptOthers.get(keyYear);
            Long othersValue = wholeValue - exceptOthersValue;
            otherSectorTableRow.getTotalYearsValue().add(othersValue);
            totalValues.add(wholeValue);
        }
        Iterator<Long> percentValueIter = totalPercentExceptOthers.iterator();
        while (percentValueIter.hasNext()) {
            Long keyYear = percentValueIter.next();
            Long wholeValue = percentTotalForYear.get(keyYear);
            Long exceptOthersValue = totalPercentForYearExceptOthers.get(keyYear);
            Long othersValue = wholeValue - exceptOthersValue;
            otherSectorTableRow.getPercentYearsValue().add(othersValue);
            totalPercentValues.add(wholeValue);
        }
        sectorsInfo.add(otherSectorTableRow);
        SectorTableHelper totalSectorTableRow = new SectorTableHelper();
        totalSectorTableRow.setSectorName("Total");
        totalSectorTableRow.setTotalYearsValue(totalValues);
        totalSectorTableRow.setPercentYearsValue(totalPercentValues);
        sectorsInfo.add(totalSectorTableRow);
        tableForm.setYears(years);
        tableForm.setSectorsInfo(sectorsInfo);
        return mapping.findForward("forward");
    }
}
