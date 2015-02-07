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
public class MondrianDraftFilter extends MondrianActivityFilter {	
	
	public MondrianDraftFilter() {
		super("draft");
	}
	
	@Override public String buildQuery(List<FilterRule> filterElements) {
		List<String> statements = new ArrayList<>();
		if (filterElements == null)
			return null;
		
		for(FilterRule rule:filterElements) {
			String statement = null;
			
			switch(rule.filterType) {
				case RANGE:
					throw new RuntimeException("draft range unimplemented!");
				
				case SINGLE_VALUE:
					statement = buildStatement(Arrays.asList(rule.value), rule.valuesInclusive);
					break;
				
				case VALUES:
					statement = buildStatement(rule.values, rule.valuesInclusive);
					break;
			
				default:
					throw new RuntimeException("unimplemented type of sql filter type: " + rule.filterType);
			}
			if (statement != null)
				statements.add(statement);
		}
		return AmpARFilter.mergeStatements(statements, "OR");
	}
	
	protected String buildStatement(List<String> values, boolean positive) {
		if (values != null && values.size() > 0) {
			Set<String> ids = new HashSet<>();
			for(String value:values)
				ids.add(postprocess(value));
			
			if (ids.isEmpty())
				return positive ? "1=0" : "1=1";
			else
				return String.format("draft %s IN (%s)", positive ? "" : "NOT", Util.toCSStringForIN(ids));
		}
		return null;
	}
	
	protected String postprocess(String input) {
		input = input.toLowerCase();
		if (input.equals("true") || input.equals("false"))
			return input;
			
		throw new RuntimeException("draft filter value not one of 'true', 'false': " + input);
	}
	
	@Override protected List<FilterRule> getFilterElements(MondrianReportFilters mrf) {
		return mrf.getFilterRules().get(new ReportElement(new ReportColumn(ColumnConstants.DRAFT)));
	}
}
