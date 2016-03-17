package org.dgfoundation.amp.mondrian;

import java.util.List;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.pagination.PartialReportArea;

public class ReportAreaDescriber {
	
	final List<ReportOutputColumn> leaves;
	public ReportAreaDescriber(List<ReportOutputColumn> leaves) {
		this.leaves = leaves;
	}
	
	public String describeInCode(ReportArea area) {
		return describeInCode(area, 1);
	}
	
	public String describeInCode(ReportArea area, int depth) {
		boolean isPartialReportArea = PartialReportArea.class.isAssignableFrom(area.getClass());
		String testAreaType = isPartialReportArea ? "PaginatedReportAreaForTests" : "ReportAreaForTests"; 
		
		if (area.getChildren() == null) {
			return String.format("%snew %s(%s, %s)", prefixString(depth), testAreaType, describeOwner(area), contentsMap(area));
		}
		
		return String.format("%snew %s(%s)%s%s%s", prefixString(depth), testAreaType,
				describeOwner(area),
				(area.getChildren() != null && area.getChildren().size() > 1) ? ("\n" + prefixString(depth)) : "",
				describeContents(area, depth, isPartialReportArea),
				describeChildren(area, depth));
	}

	public String describeOwner(ReportArea area) {
		if (area.getOwner() == null)
			return "null";
		if (area.getOwner().id > 0)
			return String.format("new AreaOwner(%s)", area.getOwner().id);
		return String.format("new AreaOwner(\"%s\", \"%s\")", area.getOwner().columnName, area.getOwner().debugString);
	}
	
	public String contentsMap(ReportArea area) {
		boolean first = true;
		StringBuilder res = new StringBuilder();
		Collection<ReportOutputColumn> orderedLeaves = leaves == null ? area.getContents().keySet() : leaves;
		
		for (ReportOutputColumn colKey:orderedLeaves) {
			ReportCell colContents = area.getContents().get(colKey);
			//if (colContents != null) {
				res.append(String.format("%s\"%s\", %s", first ? "" : ", ", SimplifiedROCComparator.generateDisplayedName(colKey), colContents == null ? null : "\"" + colContents.displayedValue + "\""));
				first = false;
			//}
		}
		return res.toString();
	}
	
	public String describeContents(ReportArea area, int depth, boolean isPartialReportArea) {
		if (area.getContents() == null) return "";
		StringBuffer res = new StringBuffer(String.format(/*prefixString(depth) + */".withContents(%s)", contentsMap(area)));	
		if (isPartialReportArea) {
			PartialReportArea pArea = (PartialReportArea) area;
			res.append(".withCounts(").append(pArea.getCurrentLeafActivitiesCount()).append(", ")
			.append(pArea.getTotalLeafActivitiesCount()).append(")");
		}
		return res.toString();
	}
		
	public String describeChildren(ReportArea area, int depth) {
		if (area.getChildren() == null) return "";
		StringBuffer res = new StringBuffer("\n" + prefixString(depth) + ".withChildren(");
		for(int i = 0; i < area.getChildren().size(); i++) {
			ReportArea child = area.getChildren().get(i);
			res.append("\n");
			res.append(describeInCode(child, depth + 1));
			if (i != area.getChildren().size() - 1)
				res.append(",");
		}
		res.append(prefixString(depth) + ")");
		return res.toString();
	}
	
	protected String prefixString(int depth) {
		return StringUtils.repeat("  ", depth);
	}
}
