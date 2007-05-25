package org.digijava.module.digifeed.utils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;



public class PublisherDateFormatter {
	private static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd");
	private static SimpleDateFormat valueDateFormat = new SimpleDateFormat(
	"EEE MMM d yyyy");
	public static Date parse(String date) throws ParseException {
		return dateFormat.parse(date);
	}

	public static String print(Date date) {
		String result = null;
		if (date != null) {
			result = dateFormat.format(date);
		}
		return result;
	}
	
	public static String valuePrint(Date date) {
		String result = null;
		if (date != null) {
			result = valueDateFormat.format(date);
		}
		return result;
	}
}

