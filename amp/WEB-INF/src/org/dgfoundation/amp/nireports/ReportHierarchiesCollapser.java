package org.dgfoundation.amp.nireports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

import org.dgfoundation.amp.newreports.ReportCollapsingStrategy;
import org.dgfoundation.amp.nireports.runtime.CellColumn;
import org.dgfoundation.amp.nireports.runtime.ColumnContents;
import org.dgfoundation.amp.nireports.runtime.ColumnReportData;
import org.dgfoundation.amp.nireports.runtime.GroupReportData;
import org.dgfoundation.amp.nireports.runtime.NiCell;
import org.dgfoundation.amp.nireports.runtime.ReportData;
import org.dgfoundation.amp.nireports.runtime.ReportDataVisitor;

/**
 * a visitor which implements the instructions contained in {@link ReportCollapsingStrategy}, e.g. smashes together hierarchies with the same name but different underlying IDs
 * @author Dolghier Constantin
 *
 */
public class ReportHierarchiesCollapser implements ReportDataVisitor<ReportData> {

	protected final ReportCollapsingStrategy strategy;
	
	public ReportHierarchiesCollapser(ReportCollapsingStrategy strategy) {
		this.strategy = strategy;
	}
	
	@Override
	public ReportData visitLeaf(ColumnReportData crd) {
		return crd; // not stashing leafs with the same name
	}

	@Override
	public ReportData visitGroup(GroupReportData grd) {
		if (grd.getSubReports().isEmpty() || strategy == ReportCollapsingStrategy.NEVER)
			return grd;
		boolean containsCRDs = grd.getSubReports().get(0) instanceof ColumnReportData; // all the children have the same type
		LinkedHashMap<String, List<ReportData>> childrenByName = new LinkedHashMap<>();
		final String dummyUnknownName = "####dummy####unknown"; // the key by which unknown will go under childrenByName 
		for(ReportData subReport:grd.getSubReports()) {
			String key = subReport.splitter.undefined ? dummyUnknownName : subReport.splitter.getDisplayedValue();
			childrenByName.computeIfAbsent(key, ignored -> new ArrayList<>()).add(subReport);
		}
		List<ReportData> newChildren = new ArrayList<>();
		for(String childName:childrenByName.keySet()) {
			List<? extends ReportData> children = childrenByName.get(childName);
			boolean unknown = childName.equals(dummyUnknownName);
			if (unknown || strategy == ReportCollapsingStrategy.ALWAYS) {
				ReportData newChild = containsCRDs ? collapseCRDs(grd.context.headers, (List<ColumnReportData>) children) : collapseGRDs(strategy, (List<GroupReportData>) children);
				newChildren.add(newChild);
			} else {
				newChildren.addAll(children.stream().map(z -> z.accept(this)).collect(toList()));
			}
		}
		return grd.clone(newChildren);
	}
		
	protected ColumnReportData collapseCRDs(NiHeaderInfo headers, List<ColumnReportData> children) {
		List<CellColumn> leaves = headers.leafColumns;
		Map<CellColumn, ColumnContents> contents = new HashMap<>();
		for(CellColumn leaf:leaves) {
			contents.put(leaf, mergeColumnContents(children.stream().map(child -> child.getContents().get(leaf)).filter(z -> z != null).collect(toList())));
		}
		//TODO: all the ids except one will be lost - ATM the datastructures do not allow holding multiple IDs
		//NiCell splitter = children.get(0).splitter; 
		ColumnReportData res = new ColumnReportData(children.get(0).context, children.get(0).splitter, contents);
		return res;
	}
	
	protected ReportData collapseGRDs(ReportCollapsingStrategy strategy, List<GroupReportData> children) {
		List<ReportData> newChildren = new ArrayList<>();
		for(GroupReportData child:children)
			newChildren.addAll(child.getSubReports());
		GroupReportData grouped = new GroupReportData(children.get(0).context, children.get(0).splitter, newChildren);
		ReportData res = grouped.accept(this);
		return res;
	}
	
	protected ColumnContents mergeColumnContents(List<ColumnContents> in) {
		Map<Long, List<NiCell>> res = new HashMap<>();
		for(ColumnContents elem:in) {
			for(Long id:elem.data.keySet())
				res.computeIfAbsent(id, ignored -> new ArrayList<>()).addAll(elem.data.get(id));
		}
		return new ColumnContents(res);
	}

}
