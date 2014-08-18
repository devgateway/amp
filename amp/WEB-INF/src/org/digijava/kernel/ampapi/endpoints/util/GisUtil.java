package org.digijava.kernel.ampapi.endpoints.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class GisUtil {
	public static String formatDate(Date d) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
		TimeZone tz = TimeZone.getTimeZone("UTC");

		df.setTimeZone(tz);
		return df.format(d);
	}
}
