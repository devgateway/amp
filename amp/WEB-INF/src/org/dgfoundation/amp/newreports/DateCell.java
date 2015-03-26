package org.dgfoundation.amp.newreports;

import java.text.SimpleDateFormat;
import java.util.Date;

import mondrian.util.Pair;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.algo.AmpCollections;
import org.digijava.kernel.ampapi.mondrian.util.MoConstants;

/**
 * a @link {@link ReportCell} containing a date
 * @author Emanuel Perez, Dolghier Constantin
 *
 */
public final class DateCell extends ReportCell {
	
protected static Logger logger = Logger.getLogger(TextCell.class);
	
	public DateCell(Date value, String displayedValue) {
		super(value, displayedValue);
	}
	
	public static DateCell buildDateCell(String mondrianOut) {
		Pair<Date, String> p = parseMondrianDate(mondrianOut);
		return new DateCell(p.left, p.right);
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
			return new Pair<>(new Date(70, 2, 2), moOut); // could not parse date - at least save the displayed value
		}
	}
	@Override public int compareTo(ReportCell oth) {
		if(value==null && (oth == null || oth.value==null))
			return 0;
		return AmpCollections.nullCompare(value, oth.value);
	}
}
