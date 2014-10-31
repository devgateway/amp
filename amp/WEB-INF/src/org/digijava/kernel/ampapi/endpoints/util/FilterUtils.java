package org.digijava.kernel.ampapi.endpoints.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.reports.ConstantsUtil;
import org.dgfoundation.amp.reports.mondrian.MondrianReportFilters;
import org.digijava.kernel.ampapi.exception.AmpApiException;
import org.digijava.kernel.ampapi.mondrian.util.MoConstants;

public class FilterUtils {
	protected static Logger logger = Logger.getLogger(FilterUtils.class);
	
	
	public static MondrianReportFilters getApiOtherFilters(Map<String, Object> filter,
			MondrianReportFilters filterRules) {
		try {
			if (filter.get("date") != null) {
				if (filterRules == null) {
					filterRules = new MondrianReportFilters();
				}
				Map<String, Object> date = (LinkedHashMap<String, Object>) filter
						.get("date");
				String start = date.get("start").toString();
				String end = date.get("end").toString();
				SimpleDateFormat sdf = new SimpleDateFormat(
						MoConstants.DATE_FORMAT);
				filterRules.addDateRangeFilterRule(sdf.parse(start),
						sdf.parse(end));
			}
		} catch (AmpApiException | ParseException e) {
			logger.error("cannot process date", e);
		}
		return filterRules;
	}

	/**
	 * returns a MondrianReportFilters based on the End point parameter
	 * 
	 * @param filter
	 * @return
	 */
	public static MondrianReportFilters getApiColumnFilter(LinkedHashMap<String, Object> filter) {
		if (filter == null) {
			return null;
		}
		MondrianReportFilters filterRules = new MondrianReportFilters();
		Set<String> validColumns = ConstantsUtil.getConstantsSet(ColumnConstants.class);
		for (Entry<String, Object> entry : filter.entrySet()) {
			if (validColumns.contains(entry.getKey())) {
				if (entry.getValue() instanceof List) {
					List<String> ids = getStringFromIntegerArray((List<Integer>) filter.get(entry.getKey()));
					filterRules.addFilterRule(new ReportColumn(entry.getKey()),
							new FilterRule(ids, true, true)); 
				} 
			}
		}
		
		return filterRules;
	}
	
	private static List<String>getStringFromIntegerArray(List<Integer>theArray){
		List<String>s=new ArrayList<String>();
		for (Integer intObject : theArray) {
			s.add(intObject.toString());
		}
		return s;
	}
}
