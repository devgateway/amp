/**
 * 
 */
package org.dgfoundation.amp.reports.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportOutputColumn;

/**
 * Wrapper that provides some utility methods over Report Area
 * 
 * @author Nadejda Mandrescu
 */
public class ReportAreaContentUtil extends ReportAreaImpl {
	//only for utility usage, for some custom retrieval of the content
	protected Map<String, ReportCell> columnToContent = new HashMap<String, ReportCell>();
	
	@Override
	public void setContents(Map<ReportOutputColumn, ReportCell> contents) {
		super.setContents(contents);
		for (Entry<ReportOutputColumn, ReportCell> entry : contents.entrySet()) {
			if (entry.getKey().parentColumn == null)
				columnToContent.put(entry.getKey().originalColumnName, entry.getValue());
			else
				columnToContent.put(entry.getKey().getHierarchicalName(), entry.getValue());
		}
	}
	
	/**
	 * Utility method for content retrieval for a given originalColumnName. Use it only 
	 * to retrieve some specific column on a particular need. For iteration over the content 
	 * please use {@link #getContents()} and iterate the returned map.
	 * 
	 * @param originalColumnName e.g. "Project Title" or "[2010][Actual Commitments]"
	 * @return ReportCell associated data for the requested column
	 */
	public ReportCell getContent(String originalColumnName) {
		return columnToContent.get(originalColumnName);
	}
}
