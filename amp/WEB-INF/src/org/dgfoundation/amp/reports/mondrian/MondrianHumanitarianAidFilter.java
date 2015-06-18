/**
 * 
 */
package org.dgfoundation.amp.reports.mondrian;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportElement;
import org.digijava.module.aim.util.ActivityUtil;

/**
 * Mondrian DB utility class  
 * @author Constantin Dolghier
 *
 */
public class MondrianHumanitarianAidFilter extends MondrianActivityFilter {	
	
	public MondrianHumanitarianAidFilter() {
		super("humanitarianaid");
	}
	
	@Override public String buildQuery(List<FilterRule> filterElements) {
		List<String> statements = new ArrayList<>();
		if (filterElements == null)
			return null;
		
		for(FilterRule rule:filterElements) {
			Set<String> ids = null;
			
			switch(rule.filterType) {
			
				case RANGE:
					throw new RuntimeException("amp_id range unimplemented!");
				
				case SINGLE_VALUE:
					ids = new HashSet<>(Arrays.asList(valueToBoolean(rule.value)));
					break;
				
				case VALUES:
					if (rule.values != null && rule.values.size() > 0) {
						ids = new HashSet<>();
						for(String value:rule.values)
							ids.add(valueToBoolean(value));
					}
					break;
			
				default:
					throw new RuntimeException("unimplemented type of sql filter type: " + rule.filterType);
			}
			if (ids != null) {
				String statement = String.format("%s %s IN (%s)", this.COLUMN_EXPR, rule.valuesInclusive ? "" : "NOT", Util.toCSStringForIN(ids));
				statements.add(statement);
			}
		}
		return AmpARFilter.mergeStatements(statements, "OR");
	}
	
	@Override protected List<FilterRule> getFilterElements(MondrianReportFilters mrf) {
		return mrf.getFilterRules().get(new ReportElement(new ReportColumn(ColumnConstants.HUMANITARIAN_AID)));
	}
	
	public static String valueToBoolean(String value) {
		if (value.equals("1")) return "true";
		if (value.equals("2")) return "false";
		throw new RuntimeException("unsupported ternary-encoding-integer: " + value);
	}
}
