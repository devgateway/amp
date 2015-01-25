/**
 * 
 */
package org.dgfoundation.amp.reports.mondrian;

import java.util.List;

import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.ReportColumn;

/**
 * Mondrian DB utility class  
 * @author Nadejda Mandrescu, Constantin Dolghier (refactoring - split to MondrianSQLFilters)
 *
 */
public class MondrianDateFilters extends MondrianActivityFilter {	
	
	public final String ampColumn;
	public final String aavColumn;
	
	public MondrianDateFilters(String ampColumn, String aavColumn) {
		super(getJulianCodeSql(aavColumn));
		this.ampColumn = ampColumn;
		this.aavColumn = aavColumn;
	}
	
	@Override
	protected List<FilterRule> getFilterElements(MondrianReportFilters mrf) {
		return mrf.getDateFilterRules().get(new ReportColumn(ampColumn));
	}
	
	private static String getJulianCodeSql(String columnName) {
		return String.format("to_char(%s, 'J')::integer", columnName);
	}
}
