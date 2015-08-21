package org.dgfoundation.amp.reports.mondrian;

import java.util.ArrayList;
import java.util.List;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.newreports.FilterRule;

/**
 * a class which takes a number of {@link FilterRule} instances and constructs an AND-starting sql fragment which would filter them out
 * understands open/closed ranges, multiple values and negation
 * @author Constantin Dolghier
 *
 */
public class ActivityFilter {
	
	/**
	 * the expression to use on the left side of the queries 
	 */
	public final String COLUMN_EXPR;
	
		
	public ActivityFilter(String columnExpr) {
		this.COLUMN_EXPR = columnExpr;
	}
	
	public String buildQuery(List<FilterRule> filterElements) {
		List<String> statements = new ArrayList<>();
				
		if (filterElements == null)
			return null;
		
		for(FilterRule rule:filterElements) {
			String statement = "";
			switch(rule.filterType) {
			
				case RANGE:
					if (rule.min != null)
						statement = COLUMN_EXPR.concat(rule.minInclusive ? " >= " : " > ").concat(rule.min);
					
					if (rule.min != null && rule.max != null)
						statement += " AND ";
					
					if (rule.max != null)
						statement = statement.concat(COLUMN_EXPR).concat(rule.maxInclusive ? " <= " : " < ").concat(rule.max);					
					break;
				
				case SINGLE_VALUE:
					if (rule.value.equals(FilterRule.NULL_VALUE))
						statement = "COLUMN_EXPR IS NULL";
					else
						statement = COLUMN_EXPR + " = " + rule.value;
					break;
				
				case VALUES:
					if (rule.values != null && rule.values.size() > 0) {
						statement = COLUMN_EXPR.concat(" IN (") + Util.toCSStringForIN(AlgoUtils.collectLongs(rule.values));
					}
			
				default:
					throw new RuntimeException("unimplemented type of sql filter type: " + rule.filterType);
			}
			if (statement != null && !statement.isEmpty()) {
				if (!rule.valuesInclusive)
					statement = "NOT (" + statement + ")";
				statements.add(statement);
			}
		}
		return AmpARFilter.mergeStatements(statements, "OR");
	}	
}
