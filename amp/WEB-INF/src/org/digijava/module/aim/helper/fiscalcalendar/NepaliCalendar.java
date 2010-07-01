package org.digijava.module.aim.helper.fiscalcalendar;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class NepaliCalendar {

	Integer month;
	Integer year;
	Integer day;
	Integer dayOfWeek;

	Integer startEnglishYear = 1944; // 01/01/1944

	Integer startNepaliYear = 2000; // January 1 of 1944 (English) --- > 17 / 9
	// 2000 Nepali DD/MM/YYYY day
	Integer startNepaliMonth = 9;
	Integer startNepaliDayOfMonth = 17 - 1;
	Integer startDayOfWeek = 7 - 1;

	Integer[] months = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
	Integer[] leapMonths = { 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

	public NepaliCalendar() {
		initialize(new GregorianCalendar());
	}

	public NepaliCalendar(Date d) {
		GregorianCalendar c = new GregorianCalendar();
		c.setTime(d);
		initialize(c);
	}

	public NepaliCalendar(GregorianCalendar cal) {
		initialize(cal);
	}

	public void setTime(Date date) {
		GregorianCalendar c = new GregorianCalendar();
		c.setTime(date);
		initialize(c);
	}

	private void initialize(GregorianCalendar calendar) {
		Integer enYear = calendar.get(Calendar.YEAR);
		Integer enMonth = (calendar.get(Calendar.MONTH) + 1);
		Integer enDay = calendar.get(Calendar.DAY_OF_MONTH);
		Integer totalEngDays = 0;

		for (int i = 0; i < (enYear - startEnglishYear); i++) { // 2010 - 1944
			if (isLeap(startEnglishYear + i)) {
				for (int j = 0; j < 12; j++) {
					totalEngDays += leapMonths[j];
				}
			} else {
				for (int j = 0; j < 12; j++) {
					totalEngDays += months[j];
				}
			}
		}
		for (int i = 0; i < (enMonth - 1); i++) {
			if (isLeap(enYear)) {
				totalEngDays += leapMonths[i];
			} else {
				totalEngDays += months[i];
			}

		}
		totalEngDays += enDay;

		int i = 0;
		int j = startNepaliMonth;

		int nDayOfMonth = startNepaliDayOfMonth;
		int nMonth = startNepaliMonth;
		int nYear = startNepaliYear;
		int nDayOfweek = startDayOfWeek;

		while (totalEngDays != 0) {
			Integer totalDaysOfMonth = mapping[i][j];
			nDayOfMonth++; // count the days
			nDayOfweek++; // count the days interms of 7 days

			if (nDayOfMonth > totalDaysOfMonth) {
				nMonth++;
				nDayOfMonth = 1;
				j++;
			}

			if (nDayOfweek > 7)
				nDayOfweek = 1;

			if (nMonth > 12) {
				nYear++;
				nMonth = 1;
			}
			if (j > 12) {
				j = 1;
				i++;
			}
			totalEngDays--;
		}

		this.year = nYear;
		this.month = nMonth;
		this.day = nDayOfMonth;
		this.dayOfWeek = nDayOfweek;

	}

	public void add(int type, int n) {

		if (type == Calendar.DAY_OF_MONTH) {
			if (n > 0)
				addDays(n);
			else
				removeDays(n);
		}

		if (type == Calendar.MONTH) {
			addMonths(n);
		}

		if (type == Calendar.YEAR) {
			this.year += n;
		}

	}

	private void addMonths(int months) {
		int nMonth = this.month -1;
		int nYear = this.year;

		if (months > 0) {
			while (months != 0) {
				nMonth++;
				if (nMonth > 12) {
					nMonth = 1;
					nYear++;
				}
				months--;
			}
		} else {
			while (months != 0) {
				nMonth--;
				if (nMonth < 1) {
					nMonth = 12;
					nYear--;
				}
				months++;
			}
		}

		this.year = nYear;
		this.month = nMonth;

	}

	private void removeDays(int days) {
		int i = getMonthsByYearPosition();
		int j = this.month;
		int nDayOfMonth = this.day - 1;
		int nMonth = this.month;
		int nYear = this.year;
		int nDayOfweek = this.dayOfWeek - 1;

		while (days != 0) {
			Integer totalDaysOfMonth = mapping[i][j];
			nDayOfMonth--;
			nDayOfweek--;

			if (nDayOfMonth < 1) {
				nMonth--;
				j--;
				nDayOfMonth = mapping[i][j];

			}

			if (nDayOfweek < 1)
				nDayOfweek = 7;

			if (nMonth < 1) {
				nYear--;
				nMonth = 12;
			}
			if (j < 1) {
				j = 12;
				i--;
				nDayOfMonth = mapping[i][j];
			}
			days++;
		}

		this.year = nYear;
		this.month = nMonth;
		this.day = nDayOfMonth;
		this.dayOfWeek = nDayOfweek;
	}

	private void addDays(int days) {
		int i = getMonthsByYearPosition();
		int j = this.month;
		int nDayOfMonth = this.day - 1;
		int nMonth = this.month;
		int nYear = this.year;
		int nDayOfweek = this.dayOfWeek - 1;

		while (days != 0) {
			Integer totalDaysOfMonth = mapping[i][j];
			nDayOfMonth++;
			nDayOfweek++;

			if (nDayOfMonth > totalDaysOfMonth) {
				nMonth++;
				nDayOfMonth = 1;
				j++;
			}

			if (nDayOfweek > 7)
				nDayOfweek = 1;

			if (nMonth > 12) {
				nYear++;
				nMonth = 1;
			}
			if (j > 12) {
				j = 1;
				i++;
			}
			days--;
		}

		this.year = nYear;
		this.month = nMonth;
		this.day = nDayOfMonth;
		this.dayOfWeek = nDayOfweek;
	}

	private int getMonthsByYearPosition() {
		for (int i = 0; i < mapping.length; i++) {
			if (mapping[i][0] == this.year) {
				return i;
			}
		}
		return -1;
	}

	public boolean isLeap(Integer year) {
		boolean isLeapYear;
		isLeapYear = (year % 4 == 0);
		isLeapYear = isLeapYear && (year % 100 != 0);
		isLeapYear = isLeapYear || (year % 400 == 0);
		return isLeapYear;
	}

	public String getMonthName() {

		switch (this.month) {
		case 1:
			return "Baishak";

		case 2:
			return "Jestha";

		case 3:
			return "Ashad";

		case 4:
			return "Shrawn";

		case 5:
			return "Bhadra";

		case 6:
			return "Ashwin";

		case 7:
			return "Kartik";

		case 8:
			return "Mangshir";

		case 9:
			return "Poush";

		case 10:
			return "Magh";

		case 11:
			return "Falgun";

		case 12:
			return "Chaitra";
		}
		return "";

	}

	public Integer getQuarter() {
		switch (month) {
		case 1:
		case 2:
		case 3:
			return 1;
		case 4:
		case 5:
		case 6:
			return 2;
		case 7:
		case 8:
		case 9:
			return 3;
		case 10:
		case 11:
		case 12:
			return 4;
		default:
			return -1;
		}

	}

	public static int[][] mapping = {
			{ 2000, 30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31 },
			{ 2001, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30 },
			{ 2002, 31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30 },
			{ 2003, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31 },
			{ 2004, 30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31 },
			{ 2005, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30 },
			{ 2006, 31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30 },
			{ 2007, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31 },
			{ 2008, 31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 29, 31 },
			{ 2009, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30 },
			{ 2010, 31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30 },
			{ 2011, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31 },
			{ 2012, 31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30 },
			{ 2013, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30 },
			{ 2014, 31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30 },
			{ 2015, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31 },
			{ 2016, 31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30 },
			{ 2017, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30 },
			{ 2018, 31, 32, 31, 32, 31, 30, 30, 29, 30, 29, 30, 30 },
			{ 2019, 31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31 },
			{ 2020, 31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30 },
			{ 2021, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30 },
			{ 2022, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30 },
			{ 2023, 31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31 },
			{ 2024, 31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30 },
			{ 2025, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30 },
			{ 2026, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31 },
			{ 2027, 30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31 },
			{ 2028, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30 },
			{ 2029, 31, 31, 32, 31, 32, 30, 30, 29, 30, 29, 30, 30 },
			{ 2030, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31 },
			{ 2031, 30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31 },
			{ 2032, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30 },
			{ 2033, 31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30 },
			{ 2034, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31 },
			{ 2035, 30, 32, 31, 32, 31, 31, 29, 30, 30, 29, 29, 31 },
			{ 2036, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30 },
			{ 2037, 31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30 },
			{ 2038, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31 },
			{ 2039, 31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30 },
			{ 2040, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30 },
			{ 2041, 31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30 },
			{ 2042, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31 },
			{ 2043, 31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30 },
			{ 2044, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30 },
			{ 2045, 31, 32, 31, 32, 31, 30, 30, 29, 30, 29, 30, 30 },
			{ 2046, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31 },
			{ 2047, 31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30 },
			{ 2048, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30 },
			{ 2049, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30 },
			{ 2050, 31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31 },
			{ 2051, 31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30 },
			{ 2052, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30 },
			{ 2053, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30 },
			{ 2054, 31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31 },
			{ 2055, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30 },
			{ 2056, 31, 31, 32, 31, 32, 30, 30, 29, 30, 29, 30, 30 },
			{ 2057, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31 },
			{ 2058, 30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31 },
			{ 2059, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30 },
			{ 2060, 31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30 },
			{ 2061, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31 },
			{ 2062, 30, 32, 31, 32, 31, 31, 29, 30, 29, 30, 29, 31 },
			{ 2063, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30 },
			{ 2064, 31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30 },
			{ 2065, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31 },
			{ 2066, 31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 29, 31 },
			{ 2067, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30 },
			{ 2068, 31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30 },
			{ 2069, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31 },
			{ 2070, 31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30 },
			{ 2071, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30 },
			{ 2072, 31, 32, 31, 32, 31, 30, 30, 29, 30, 29, 30, 30 },
			{ 2073, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31 },
			{ 2074, 31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30 },
			{ 2075, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30 },
			{ 2076, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30 },
			{ 2077, 31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31 },
			{ 2078, 31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30 },
			{ 2079, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30 },
			{ 2080, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30 },
			{ 2081, 31, 31, 32, 32, 31, 30, 30, 30, 29, 30, 30, 30 },
			{ 2082, 30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 30, 30 },
			{ 2083, 31, 31, 32, 31, 31, 30, 30, 30, 29, 30, 30, 30 },
			{ 2084, 31, 31, 32, 31, 31, 30, 30, 30, 29, 30, 30, 30 },
			{ 2085, 31, 32, 31, 32, 30, 31, 30, 30, 29, 30, 30, 30 },
			{ 2086, 30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 30, 30 },
			{ 2087, 31, 31, 32, 31, 31, 31, 30, 30, 29, 30, 30, 30 },
			{ 2088, 30, 31, 32, 32, 30, 31, 30, 30, 29, 30, 30, 30 },
			{ 2089, 30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 30, 30 },
			{ 2090, 30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 30, 30 } };

	public Integer getMonth() {
		return month;
	}

	public Integer getYear() {
		return year;
	}

	public Integer getDay() {
		return day;
	}

	public Integer getDayOfWeek() {
		return dayOfWeek;
	}

}
