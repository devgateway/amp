package org.dgfoundation.amp.nireports.amp;

import java.time.LocalDate;

import org.dgfoundation.amp.newreports.AmpReportFilters;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.digijava.module.common.util.DateTimeUtil;

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
		LocalDate thisMonthStart = now.withDayOfMonth(1);
		LocalDate previousMonthEndD = thisMonthStart.minusDays(1);
		LocalDate previousMonthStartD = previousMonthEndD.withDayOfMonth(1);
		long previousMonthStart = DateTimeUtil.toJulianDayNumber(previousMonthStartD);
		long previousMonthEnd = DateTimeUtil.toJulianDayNumber(previousMonthEndD);
		return new SelectedYearBlock(selectedYear, selectedYearStart, selectedYearEnd, previousMonthStart, previousMonthEnd, now.getYear());
	}
	
	public static SelectedYearBlock buildFor(ReportSpecification spec, LocalDate now) {
		return buildFor(AmpReportFilters.getReportSelectedYear(spec), now);
	}
	
	public SelectedYearBlock(int selectedYear, long selectedYearStartJulian, long selectedYearEndJulian, long previousMonthStartJulian, long previousMonthEndJulian,
			int currentYear) {
		this.selectedYear = selectedYear;
		this.selectedYearStartJulian = selectedYearStartJulian;
		this.selectedYearEndJulian = selectedYearEndJulian;
		this.previousMonthStartJulian = previousMonthStartJulian;
		this.previousMonthEndJulian = previousMonthEndJulian;
		this.currentYear = currentYear;
		this.currentYearStartJulian = DateTimeUtil.toJulianDayNumber(LocalDate.of(currentYear, 1, 1));
		this.currentYearEndJulian = DateTimeUtil.toJulianDayNumber(LocalDate.of(currentYear, 12, 31));
	}

	@Override
	public String toString() {
		return "SelectedYearBlock [selectedYear=" + selectedYear + ", selectedYearStartJulian=" + selectedYearStartJulian + ", selectedYearEndJulian=" + selectedYearEndJulian + ", previousMonthStartJulian=" + previousMonthStartJulian + ", previousMonthEndJulian=" + previousMonthEndJulian + ", currentYear=" + currentYear + ", currentYearStartJulian=" + currentYearStartJulian + ", currentYearEndJulian=" + currentYearEndJulian + "]";
	}

}
