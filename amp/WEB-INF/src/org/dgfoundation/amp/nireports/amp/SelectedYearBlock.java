package org.dgfoundation.amp.nireports.amp;

import org.dgfoundation.amp.newreports.AmpReportFilters;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.digijava.module.common.util.DateTimeUtil;

import java.time.LocalDate;

/**
 * passive structure holding data regarding the selected year and its timestamps  
 * @author Dolghier Constantin
 *
 */
public class SelectedYearBlock {
    /** the year (Gregorian) */
    public final int selectedYear;
    
    /** the Julian code of the starting day of the year */
    public final long selectedYearStartJulian;
    
    /** the Julian code of the ending day of the year */
    public final long selectedYearEndJulian;

    /** the Julian code of the starting day of the previous year */
    public final long previousYearStartJulian;
    
    /** the Julian code of the ending day of the previous year */
    public final long previousYearEndJulian;    
    
    /** the Julian code of the starting day of the selected month */
    public final long selectedMonthStartJulian;
    
    /** the Julian code of the ending day of the selected month */
    public final long selectedMonthEndJulian;
    
    /** the Julian code of the first day of the previous month */
    public final long previousMonthStartJulian;
    
    /** the Julian code of the last day of the previous month */
    public final long previousMonthEndJulian;
    
    /** the current year */
    public final int currentYear;
    
    /** the Julian code of January the first of the current year */
    public final long currentYearStartJulian;
    
    /** the Julian code of December 31st of the current year */
    public final long currentYearEndJulian;
    
    public static SelectedYearBlock buildFor(int selectedYear, LocalDate now) {
        long selectedYearStart = DateTimeUtil.toJulianDayNumber(LocalDate.ofYearDay(selectedYear, 1));
        long selectedYearEnd = DateTimeUtil.toJulianDayNumber(LocalDate.ofYearDay(selectedYear + 1, 1)) - 1;
        long previousYearStart = DateTimeUtil.toJulianDayNumber(LocalDate.ofYearDay(now.getYear() - 1, 1));
        long previousYearEnd = DateTimeUtil.toJulianDayNumber(LocalDate.ofYearDay(now.getYear(), 1)) - 1;
        
        LocalDate thisMonthStartD = now.withDayOfMonth(1);
        LocalDate thisMonthEndD = now.withDayOfMonth(now.lengthOfMonth());
        LocalDate previousMonthEndD = thisMonthStartD.minusDays(1);
        LocalDate previousMonthStartD = previousMonthEndD.withDayOfMonth(1);
        long thisMonthStart = DateTimeUtil.toJulianDayNumber(thisMonthStartD);
        long thisMonthEnd = DateTimeUtil.toJulianDayNumber(thisMonthEndD);
        long previousMonthStart = DateTimeUtil.toJulianDayNumber(previousMonthStartD);
        long previousMonthEnd = DateTimeUtil.toJulianDayNumber(previousMonthEndD);
        return new SelectedYearBlock(selectedYear, selectedYearStart, selectedYearEnd, previousYearStart, previousYearEnd, 
                thisMonthStart, thisMonthEnd, previousMonthStart, previousMonthEnd, now.getYear());
    }
    
    public static SelectedYearBlock buildFor(ReportSpecification spec, LocalDate now) {
        return buildFor(AmpReportFilters.getReportSelectedYear(spec, now.getYear()), now);
    }
    
    public SelectedYearBlock(int selectedYear, long selectedYearStartJulian, long selectedYearEndJulian, 
            long previousYearStartJulian, long previousYearEndJulian, 
            long selectedMonthStartJulian, long selectedMonthEndJulian, long previousMonthStartJulian, long previousMonthEndJulian,
            int currentYear) {
        this.selectedYear = selectedYear;
        this.selectedYearStartJulian = selectedYearStartJulian;
        this.selectedYearEndJulian = selectedYearEndJulian;
        this.previousYearStartJulian = previousYearStartJulian;
        this.previousYearEndJulian = previousYearEndJulian;
        this.previousMonthStartJulian = previousMonthStartJulian;
        this.previousMonthEndJulian = previousMonthEndJulian;
        this.selectedMonthStartJulian = selectedMonthStartJulian;
        this.selectedMonthEndJulian = selectedMonthEndJulian;
        this.currentYear = currentYear;
        this.currentYearStartJulian = DateTimeUtil.toJulianDayNumber(LocalDate.of(currentYear, 1, 1));
        this.currentYearEndJulian = DateTimeUtil.toJulianDayNumber(LocalDate.of(currentYear, 12, 31));
    }

    @Override
    public String toString() {
        return "SelectedYearBlock [selectedYear=" + selectedYear + ", selectedYearStartJulian=" + selectedYearStartJulian + ", selectedYearEndJulian=" + selectedYearEndJulian + ", previousMonthStartJulian=" + previousMonthStartJulian + ", previousMonthEndJulian=" + previousMonthEndJulian + ", currentYear=" + currentYear + ", currentYearStartJulian=" + currentYearStartJulian + ", currentYearEndJulian=" + currentYearEndJulian + "]";
    }

}
