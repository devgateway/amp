package org.digijava.module.aim.helper.fiscalcalendar;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.digijava.module.aim.dbentity.AmpFiscalCalendar;

public class GregorianBasedWorker implements ICalendarWorker {

	private DateFormatSymbols dateFormatSymbols = new DateFormatSymbols();

	protected Map<Integer, ComparableMonth> monthCache = new HashMap<Integer, ComparableMonth>();

	private GregorianCalendar internalCalendar = null;

	private Date internalTime = null;

	private AmpFiscalCalendar fiscalCalendar = null;

	public GregorianBasedWorker(AmpFiscalCalendar fiscalCalendar) {
		this.fiscalCalendar = fiscalCalendar;
	}
	
	public GregorianBasedWorker(Date date) {
		setTime(date);
	}
	public GregorianBasedWorker() {
		
	}
	public Date getDate() throws Exception {

		return internalCalendar.getTime();
	}

	public void setTime(Date time) {
		internalTime = time;
		internalCalendar = new GregorianCalendar();
		internalCalendar.setTime(time);
		if (fiscalCalendar!=null){
		// set offset from fiscal calendar
			
			internalCalendar.add(GregorianCalendar.YEAR, fiscalCalendar.getYearOffset());
			int toAdd = (fiscalCalendar.getStartMonthNum() - 1);
			internalCalendar.add(GregorianCalendar.MONTH, toAdd);
			toAdd = (fiscalCalendar.getStartDayNum() - 1);
			internalCalendar.add(GregorianCalendar.DAY_OF_MONTH, toAdd);
		}
	}

	public Comparable getMonth() throws Exception {
		checkSetTimeCalled();
		int monthId = internalCalendar.get(Calendar.MONTH);
		ComparableMonth cm = monthCache.get(monthId);

		if (cm == null) {
			String monthStr = dateFormatSymbols.getMonths()[monthId];
			cm = new ComparableMonth(monthId, monthStr);
			monthCache.put(monthId, cm);
		}

		return cm;

	}

	public Integer getQuarter() throws Exception {
		checkSetTimeCalled();
		switch (internalCalendar.get(Calendar.MONTH)) {
		case Calendar.JANUARY:
		case Calendar.FEBRUARY:
		case Calendar.MARCH:
			return 1;
		case Calendar.APRIL:
		case Calendar.MAY:
		case Calendar.JUNE:
			return 2;
		case Calendar.JULY:
		case Calendar.AUGUST:
		case Calendar.SEPTEMBER:
			return 3;
		case Calendar.OCTOBER:
		case Calendar.NOVEMBER:
		case Calendar.DECEMBER:
			return 4;
		default:
			return -1;
		}
	}

	public Integer getYear() throws Exception {
		checkSetTimeCalled();
		return internalCalendar.get(Calendar.YEAR);
	}

	private void checkSetTimeCalled() throws Exception {
		if (internalTime == null)
			throw new Exception("Should call to setime first");
	}

	public Integer getYearDiff(ICalendarWorker worker) throws Exception {
		return this.getYear().intValue() - worker.getYear().intValue();
	}
	
}
