package org.digijava.kernel.ampapi.endpoints.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportEntityType;
import org.dgfoundation.amp.reports.mondrian.MondrianReportFilters;
import org.dgfoundation.amp.reports.mondrian.MondrianReportUtils;
import org.digijava.kernel.ampapi.postgis.util.QueryUtil;

public class FilterUtils {
	protected static Logger logger = Logger.getLogger(FilterUtils.class);

	/**
	 * returns a MondrianReportFilters based on the End point parameter
	 * 
	 * @param filter
	 * @return
	 */
	public static MondrianReportFilters getApiFilter(LinkedHashMap<String, Object> filter) {
		if (filter == null) {
			return null;
		}
		MondrianReportFilters filterRules = new MondrianReportFilters();
		for (String filterName : filter.keySet()) {
			//TODO need to check against ColumsUtils that the filtername is valid
			List<String>ids=getStringFromIntegerArray((List<Integer>)filter.get(filterName));
			filterRules.addFilterRule(MondrianReportUtils.getColumn(filterName, ReportEntityType.ENTITY_TYPE_ACTIVITY), 
					new FilterRule(ids, true, true)); 
			
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
