package org.dgfoundation.amp.newreports;

import java.text.SimpleDateFormat;
import java.util.Date;

import mondrian.util.Pair;

import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.mondrian.util.MoConstants;

/**
 * a @link {@link ReportCell} containing a date
 * @author Emanuel Perez, Dolghier Constantin
 *
 */
public final class DateCell extends ReportCell {
	
	protected static Logger logger = Logger.getLogger(TextCell.class);
	
	/**
	 * the date written into {@link #value} when the date could not be parsed
	 */
	public final static Date DUMMY_DATE = new Date(70, 2, 2);
	
	public DateCell(Date value, String displayedValue) {
		super(value, displayedValue);
	}
	
	public static DateCell buildDateFromRepOut(String repOut) {
		return new DateCell(parseOutputFormattedDate(repOut), repOut);
	}
	
	/**
	 * parses a Date string as output by Mondrian into the user-defined way of displaying dates
	 * @param moOut
	 * @return
	 */
	public static Pair<Date, String> parseMondrianDate(String moOut) {
		if (moOut == null || moOut.trim().isEmpty() || moOut.equals("#null")) moOut = null;
		if (moOut == null) return new Pair<>(null, "");
		try {
			Date date = new SimpleDateFormat(MoConstants.DATE_FORMAT).parse(moOut);
			return new Pair<>(date, new SimpleDateFormat(MoConstants.DATE_DISPLAY_FORMAT).format(date));
		}
		catch(Exception e) {
			return new Pair<>(DUMMY_DATE, moOut); // could not parse date - at least save the displayed value
		}
	}
	
	/**
	 * parses a Date string as output in the report for displaying purposes
	 * @param repOut
	 * @return
	 */
	public static Date parseOutputFormattedDate(String repOut) {
		if (repOut == null || repOut.trim().isEmpty() || repOut.equals("#null")) repOut = null;
		if (repOut == null) return DUMMY_DATE;
		try {
			return new SimpleDateFormat(MoConstants.DATE_DISPLAY_FORMAT).parse(repOut);
		}
		catch(Exception e) {
			return DUMMY_DATE;
		}
	}
}
