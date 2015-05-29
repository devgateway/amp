package org.digijava.kernel.ampapi.endpoints.scorecard.model;

import java.util.Calendar;
import java.util.Date;

import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.helper.fiscalcalendar.ICalendarWorker;

import clover.org.apache.log4j.Logger;

public class Quarter {

	private Logger LOGGER = Logger.getLogger(Quarter.class);
	private Integer year;
	private String yearCode;
	private Integer quarterNumber;
	private AmpFiscalCalendar fiscalCalendar;

	public Quarter(AmpFiscalCalendar calendar, Date date) {
		this.fiscalCalendar = calendar;
		ICalendarWorker worker = calendar.getworker();
		worker.setTime(date);
		try {
			this.quarterNumber = worker.getQuarter();
			this.year = worker.getYear();
			this.yearCode = "" + worker.getYear();
			if (calendar.getStartMonthNum() != 1) {
				this.yearCode += "-" + (worker.getYear() + 1);
			}
		} catch (Exception e) {
			LOGGER.warn("Couldn't get qarter for date: " + date);
		}

	}

	public Quarter(AmpFiscalCalendar calendar, Integer quarterNumber, Integer year) {
		this.fiscalCalendar = calendar;
		this.quarterNumber = quarterNumber;
		this.year = year;
		this.yearCode = "" + year;
		if (calendar.getIsFiscal()) {
			this.yearCode += "-" + (year + 1);
		}
	}

	public Integer getQuarterNumber() {
		return quarterNumber;
	}

	public void setQuarterNumber(Integer quarterNumber) {
		this.quarterNumber = quarterNumber;
	}

	@Override
	public String toString() {
		return yearCode + " " + "Q" + quarterNumber;
	}

	public Quarter getPreviousQuarter() {
		Quarter quarter;
		if (quarterNumber > 1) {
			quarter = new Quarter(fiscalCalendar, Integer.valueOf(quarterNumber - 1), year);
		} else {
			// the current object is the first quarter, so the previous quarter
			// is the fourth quarter of last year
			quarter = new Quarter(fiscalCalendar, 4, (year - 1));
		}
		return quarter;
	}

	public Date getQuarterStartDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.DAY_OF_MONTH, fiscalCalendar.getStartDayNum());
		//fiscal Calendar month are 1 based, but Java is 0 based.
		//from the starting month, we add 3 months X number of quarters
		calendar.set(Calendar.MONTH, (fiscalCalendar.getStartMonthNum() - 1) + (quarterNumber - 1) * 3);
		return calendar.getTime();
	}

}
