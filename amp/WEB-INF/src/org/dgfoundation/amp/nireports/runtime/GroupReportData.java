package org.dgfoundation.amp.nireports.runtime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

import org.dgfoundation.amp.newreports.ReportCollapsingStrategy;
import org.dgfoundation.amp.nireports.NiReportsEngine;

/**
 * a report containing subreports
 * @author Dolghier Constantin
 *
 */
public class GroupReportData extends ReportData {
	protected final List<ReportData> subreports;
	
	public GroupReportData(NiReportsEngine context, NiCell splitter, List<? extends ReportData> subreports) {
		super(context, splitter);
		this.subreports = Collections.unmodifiableList(new ArrayList<ReportData>(subreports));
	}
	
	@Override
	public boolean isLeaf() {
		return false;
	}

	@Override
	public Set<Long> getIds() {
		Set<Long> res = new HashSet<>();
		for(ReportData rd:subreports)
			res.addAll(rd.getIds());
		return res;
	}

	public void addSubReport(ReportData rd) {
		this.subreports.add(rd);
	}
	
	public List<ReportData> getSubReports() {
		return subreports;
	}
	
	@Override
	public GroupReportData horizSplit(CellColumn column) {
		GroupReportData res = new GroupReportData(this.context, this.splitter, 
				subreports.stream().map(z -> z.horizSplit(column)).collect(toList()));
		return res;
	}

	@Override
	public <K> K accept(ReportDataVisitor<K> visitor) {
		List<K> visitedChildren = subreports.stream().map(z -> z.accept(visitor)).collect(toList());
		K res = visitor.visitGroup(this, visitedChildren);
		return res;
	}

	@Override
	public ReportData collapse(ReportCollapsingStrategy strategy) {
		if (subreports.isEmpty() || strategy == ReportCollapsingStrategy.NEVER)
			return this;
		boolean containsCRDs = subreports.get(0) instanceof ColumnReportData; // all the children have the same type
		LinkedHashMap<String, List<ReportData>> childrenByName = new LinkedHashMap<>();
		final String dummyUnknownName = "####dummy####unknown"; // the key by which unknown will go under childrenByName 
		for(ReportData subReport:subreports) {
			String key = subReport.splitter.isUndefinedCell() ? dummyUnknownName : subReport.splitter.getDisplayedValue();
			childrenByName.computeIfAbsent(key, ignored -> new ArrayList<>()).add(subReport);
		}
		List<ReportData> newChildren = new ArrayList<>();
		for(String childName:childrenByName.keySet()) {
			List<? extends ReportData> children = childrenByName.get(childName);
			boolean unknown = childName.equals(dummyUnknownName);
			if (unknown || strategy == ReportCollapsingStrategy.ALWAYS) {
				ReportData newChild = containsCRDs ? collapseCRDs((List<ColumnReportData>) children) : collapseGRDs(strategy, (List<GroupReportData>) children);
				newChildren.add(newChild);
			} else {
				newChildren.addAll(children.stream().map(z -> z.collapse(strategy)).collect(toList()));
			}
		}
		return this.clone(newChildren);
	}
	
	protected ColumnReportData collapseCRDs(List<ColumnReportData> children) {
		List<CellColumn> leaves = context.headers.leafColumns;
		Map<CellColumn, ColumnContents> contents = new HashMap<>();
		for(CellColumn leaf:leaves) {
			contents.put(leaf, mergeColumnContents(children.stream().map(child -> child.contents.get(leaf)).filter(z -> z != null).collect(toList())));
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
		ReportData res = grouped.collapse(strategy);
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
