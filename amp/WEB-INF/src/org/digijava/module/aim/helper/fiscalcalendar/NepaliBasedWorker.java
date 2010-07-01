package org.digijava.module.aim.helper.fiscalcalendar;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.digijava.module.aim.dbentity.AmpFiscalCalendar;

public class NepaliBasedWorker implements ICalendarWorker {

	private DateFormatSymbols dateFormatSymbols = new DateFormatSymbols();

	protected Map<Integer, ComparableMonth> monthCache = new HashMap<Integer, ComparableMonth>();

	private GregorianCalendar internalCalendar = null;

	private NepaliCalendar nepaliCalendar = null;

	private Date internalTime = null;

	private AmpFiscalCalendar fiscalCalendar = null;

	private int fiscalMonth;

	public NepaliBasedWorker(AmpFiscalCalendar fiscalCalendar) {
		this.fiscalCalendar = fiscalCalendar;

	}

	public NepaliBasedWorker(Date date) {
		this.internalTime = date;
		this.nepaliCalendar = new NepaliCalendar(date);
	}

	public NepaliBasedWorker() {
		this.internalTime = new Date();
		this.nepaliCalendar = new NepaliCalendar(internalTime);
	}

	public Date getDate() throws Exception {
		throw new Exception("Not allowed on this calendar");
	}

	public void setTime(Date time) {
		internalTime = time;

		if (this.nepaliCalendar == null) {
			this.nepaliCalendar = new NepaliCalendar(time);
		} else {
			this.nepaliCalendar.setTime(time);
		}

		if (fiscalCalendar != null) {
			int toAdd = -(fiscalCalendar.getStartDayNum() - 1); //
			nepaliCalendar.add(GregorianCalendar.DAY_OF_MONTH, toAdd);

			toAdd = -(fiscalCalendar.getStartMonthNum() - 1); //
			nepaliCalendar.add(GregorianCalendar.MONTH, toAdd);

			nepaliCalendar.add(GregorianCalendar.YEAR, fiscalCalendar
					.getYearOffset());
		}
	}

	public Comparable getMonth() throws Exception {
		return nepaliCalendar.getMonthName();
	}

	public Integer getQuarter() throws Exception {
		return nepaliCalendar.getQuarter();
	}

	public Integer getYear() throws Exception {
		return nepaliCalendar.getYear();
	}

	public Integer getDay() throws Exception {
		return nepaliCalendar.getDay();
	}

	public Integer getMonthNumber() throws Exception {
		return nepaliCalendar.getMonth();
	}

	public Integer getDayOfWeek() throws Exception {
		return nepaliCalendar.getDayOfWeek();
	}

	private void checkSetTimeCalled() throws Exception {
	}

	public Integer getYearDiff(ICalendarWorker worker) throws Exception {
		return this.getYear().intValue() - worker.getYear().intValue();
	}

	public Comparable getFiscalMonth() throws Exception {
		if (!this.fiscalCalendar.getIsFiscal()) {
			return getMonth();
		} else {
			checkSetTimeCalled();
			int monthId = nepaliCalendar.getMonth();
			ComparableMonth cm = monthCache.get(monthId);
			if (cm == null) {
				String monthStr = nepaliCalendar.getMonthName();
				cm = new ComparableMonth(monthId, monthStr);
				monthCache.put(monthId, cm);
			}
			return cm;

		}
	}

	public String getFiscalYear() throws Exception {
		if (this.fiscalCalendar.getIsFiscal()) {
			return "Fiscal Year," + this.getYear() + " - "
					+ (this.getYear() + 1);
		}
		return this.getYear().toString();
	}

}
