package org.dgfoundation.amp.nireports.runtime;

import java.util.List;

/**
 * an interface used for recursively visiting a {@link ReportData}.
 * please see {@link ReportData#visit(org.dgfoundation.amp.nireports.ReportDataVisitor)}
 * @author Dolghier Constantin
 *
 * @param <K> the output type 
 */
public interface ReportDataVisitor<K> {
	public K visitLeaf(ColumnReportData crd);
	public K visitGroup(GroupReportData grd, List<K> visitedChildren);
}
