package org.dgfoundation.amp.reports.mondrian;

import java.util.List;

import org.dgfoundation.amp.newreports.FilterRule;

/**
 * an {@link ActivityFilter} which knows where to fetch data off a {@link MondrianReportFilters} (given that MRF has a number of disconnected Map's)
 * the per-class behaviour is isolated in {@link #getFilterElements(MondrianReportFilters)}
 * @author Constantin Dolghier
 *
 */
public abstract class MondrianActivityFilter extends ActivityFilter {
	
	public MondrianActivityFilter(String columnExpr) {
		super(columnExpr);
	}
	
	/**
	 * returns an "AND xxx"-starting query
	 * @param mrf
	 * @return
	 */
	public String buildQuery(MondrianReportFilters mrf) {
		List<FilterRule> filterElements = getFilterElements(mrf);
		if (filterElements == null)
			return null;
		return buildQuery(filterElements);
	}	

	protected abstract List<FilterRule> getFilterElements(MondrianReportFilters mrf);
}
