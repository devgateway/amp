package org.digijava.kernel.ampapi.endpoints.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsConstants;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.FeaturesUtil;

public class GisUtil {
	public static final Logger logger = Logger.getLogger(GisUtil.class);

	/***
	 * 
	 * @param d
	 * @return
	 */
	public static String formatDate(Date d) {
		String formatString = FeaturesUtil.getGlobalSettingValue(Constants.GLOBALSETTINGS_DATEFORMAT);
		DateFormat df = new SimpleDateFormat(formatString + "'T'HH:mm'Z'"); 
		TimeZone tz = TimeZone.getTimeZone("UTC");

		df.setTimeZone(tz);
		
		return df.format(d);
	}
}
