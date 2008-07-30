package org.digijava.module.aim.helper;

import java.sql.Date;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class CalendarHelper {
	private GregorianCalendar gregCalendar;
	private EthiopianCalendar ethcalendar;
	private DateFormatSymbols dateFormatSymbols;
	protected Map<Integer,ComparableMonth> gregorianMonthCache = new HashMap<Integer,ComparableMonth>();
	protected Map<Integer,ComparableMonth> ethiopianMonthCache = new HashMap<Integer,ComparableMonth>();
	public CalendarHelper() {
		gregCalendar = new GregorianCalendar();
		dateFormatSymbols = new DateFormatSymbols();
		ethcalendar= EthiopianCalendar.getEthiopianDate(gregCalendar);
	}
	public void setTime(Date td) {
		getCalendar().setTime(td);
		ethcalendar = EthiopianCalendar.getEthiopianDate(gregCalendar);
	}
	public void setCalendar(GregorianCalendar calendar) {
		this.gregCalendar = calendar;
	}
	public GregorianCalendar getCalendar() {
		return gregCalendar;
	}
	public void setDateFormatSymbols(DateFormatSymbols dfs) {
		this.dateFormatSymbols = dfs;
	}
	public DateFormatSymbols getDateFormatSymbols() {
		return dateFormatSymbols;
	}
	public Comparable getMonth() {
		int monthId = gregCalendar.get(Calendar.MONTH);
		ComparableMonth cm = gregorianMonthCache.get(monthId);
		if (cm == null) {
			String monthStr = dateFormatSymbols.getMonths()[monthId];
			cm = new ComparableMonth(monthId, monthStr);
			gregorianMonthCache.put(monthId, cm);
		}
		return cm;
	}
	
	//FIXED
	//The Java Calendar is zero based!  so JANUARY=0 DECEMBER=11 
	public int getQuarter() {
		switch (gregCalendar.get(Calendar.MONTH)) {
		case 0:
		case 1:
		case 2:
			return 1;
		case 3:
		case 4:
		case 5:
			return 2;
		case 6:
		case 7:
		case 8:
			return 3;
		case 9:
		case 10:
		case 11:
			return 4;
		default:
			return -1;
		}
		
	}
	public int getYear() {
		return gregCalendar.get(Calendar.YEAR);
	}
	public EthiopianCalendar getEthiopianDate() {
		return ethcalendar.getEthiopianDate(gregCalendar);
	}
	public Comparable getEthiopianMonth() {
		int monthId = ethcalendar.ethMonth;
		ComparableMonth cm = ethiopianMonthCache.get(monthId);
		if (cm == null) {
			String monthStr = ethcalendar.ethMonthName;
			cm = new ComparableMonth(monthId, monthStr);
			ethiopianMonthCache.put(monthId, cm);
		}
		return cm;
	}
	public int getEthiopianFiscalYear() {
		// TODO Auto-generated method stub
		return ethcalendar.ethFiscalYear;
	}
	public int getEthiopianYear() {
		return ethcalendar.ethYear;
	}
	public int getEthiopianFiscalQuarter() {
		return ethcalendar.ethFiscalQrt;
	}
	public int getEthiopianQuarter() {
		return ethcalendar.ethQtr;
	}

}

class ComparableMonth implements Comparable<ComparableMonth>{
	private int monthId;
	private String monthStr;

	public ComparableMonth(int monthId, String monthStr) {
		this.monthId = monthId;
		this.monthStr = monthStr;
	}

	public int compareTo(ComparableMonth o) {
		if(monthId < o.monthId)
			return -1;
		if(monthId > o.monthId)
			return 1;
		return 0;
	}
	
	public String toString() {
		return monthStr;
	}
	
	
	/**
	 * Convert the date that comes from the input to gregorian calendar based
	 * on global setting default calendar 
	 * @return
	 */
	public Date defaultToGregorian(Date date){
		return null;	
	}
	
	/**
	 * Convert the date that comes as gregorian to a date based
	 * on global setting default calendar 
	 * @return
	 */
	public Date gregorianToDefault(Date date){
		return null;	
	}
}
