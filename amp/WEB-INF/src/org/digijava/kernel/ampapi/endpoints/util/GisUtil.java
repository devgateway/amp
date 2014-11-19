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

public class GisUtil {
	public static final Logger logger = Logger.getLogger(GisUtil.class);

	/***
	 * 
	 * @param d
	 * @return
	 */
	public static String formatDate(Date d) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
		TimeZone tz = TimeZone.getTimeZone("UTC");

		df.setTimeZone(tz);
		
		return df.format(d);
	}
	/**
	 * 
	 * @param spec
	 * @param config
	 */
	public static void applySettings(ReportSpecificationImpl spec, JsonBean config) {
		// apply first common settings, i.e. calendar and currency
		EndpointUtils.applySettings(spec, config);

		// now apply GIS custom settings, i.e. selected measures
		if (config.get(EPConstants.SETTINGS) != null) {
			Map<Integer, Object> settings = (Map<Integer, Object>) config.get(EPConstants.SETTINGS);
			List<String> measureOptions = (List<String>) settings.get(SettingsConstants.FUNDING_TYPE_ID);
			if (measureOptions != null)
				for (String measure : measureOptions)
					spec.addMeasure(new ReportMeasure(measure));
		}
	}

}
