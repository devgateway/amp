package org.dgfoundation.amp.reports.mondrian;

import java.util.ArrayList;
import java.util.List;

import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportElement;

/**
 * a {@link MondrianActivityFilter} which filters by Activity Status
 * @author Constantin Dolghier
 *
 */
public class MondrianApprovalStatusFilter extends MondrianActivityFilter {
	
	public MondrianApprovalStatusFilter() {
		super("approval_status");
	}

	@Override public String buildQuery(List<FilterRule> filterElements) {
		List<String> statements = new ArrayList<>();
		if (filterElements == null)
			return null;
		
		for(FilterRule rule:filterElements) {
			switch(rule.filterType) {
			
				case RANGE:
					throw new RuntimeException("activity status range unimplemented!");
				
				case SINGLE_VALUE:
					statements.add("1=1 " + AmpARFilter.buildApprovalStatusQuery(Integer.parseInt(rule.value), !rule.valuesInclusive));
					break;
				
				case VALUES:
					if (rule.values != null && rule.values.size() > 0)
						statements.add("1=1 " + AmpARFilter.buildApprovalStatusQuery(rule.values, !rule.valuesInclusive));
					break;
			
				default:
					throw new RuntimeException("unimplemented type of sql filter type: " + rule.filterType);
			}
		}
		return AmpARFilter.mergeStatements(statements, "OR");
	}
	
	@Override protected List<FilterRule> getFilterElements(MondrianReportFilters mrf) {
		return mrf.getFilterRules().get(new ReportElement(new ReportColumn(ColumnConstants.APPROVAL_STATUS)));
	}
}
