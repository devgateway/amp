/**
 * 
 */
package org.dgfoundation.amp.reports.mondrian;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportElement;

/**
 * Mondrian SQL filter class for boolean fields which are simple columns in amp_activity_version
 * @author Constantin Dolghier
 *
 */
public class MondrianBooleanAAVFieldFilter extends MondrianActivityFilter {	
	
	public final String reportColumnName;
	
	private final Map<String, String> VALUE_TO_SQL_FRAGMENT = new HashMap<String, String>(){{
		put(FilterRule.FALSE_VALUE, "IS FALSE");
		put(FilterRule.TRUE_VALUE, "IS TRUE");
		put(FilterRule.NULL_VALUE, "IS NULL");
	}};
	
	/**
	 * 
	 * @param aavColumnName - the name of the amp_activity_version table column which holds the boolean
	 * @param reportColumnName - the name of the AMP Reports column name
	 */
	public MondrianBooleanAAVFieldFilter(String aavColumnName, String reportColumnName) {
		super(aavColumnName);
		this.reportColumnName = reportColumnName;
	}
	
	@Override public String buildQuery(List<FilterRule> filterElements) {
		List<String> statements = new ArrayList<>();
		if (filterElements == null)
			return null;
		
		for(FilterRule rule:filterElements) {
			Set<String> ids = null;
			
			switch(rule.filterType) {
			
				case RANGE:
					throw new RuntimeException("booleans have no range!");
				
				case SINGLE_VALUE:
					ids = new HashSet<>(Arrays.asList(rule.value));
					break;
				
				case VALUES:
					if (rule.values != null && rule.values.size() > 0) {
						ids = new HashSet<>();
						for(String value:rule.values)
							ids.add(value);
					}
					break;
			
				default:
					throw new RuntimeException("unimplemented type of boolean filter type: " + rule.filterType);
			}
			if (ids != null) {
				StringBuilder valueStatements = new StringBuilder();
				for(String id:ids) {
					String sqlFrag = VALUE_TO_SQL_FRAGMENT.get(id);
					if (sqlFrag == null)
						throw new RuntimeException("unsupported boolean sql filter value: " + id);
					String statement = String.format("%s %s", this.COLUMN_EXPR, sqlFrag);
					valueStatements.append(valueStatements.length() == 0 ? statement : " OR " + statement);
				}
				String statement = String.format("%s(%s)", rule.valuesInclusive ? "" : "NOT ", valueStatements.toString());
				statements.add(statement);
			}
		}
		return AmpARFilter.mergeStatements(statements, "AND");
	}
	
	@Override protected List<FilterRule> getFilterElements(MondrianReportFilters mrf) {
		return mrf.getFilterRules().get(new ReportElement(new ReportColumn(reportColumnName)));
	}
}
