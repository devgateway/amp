package org.dgfoundation.amp.nireports;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import org.dgfoundation.amp.nireports.runtime.CellColumn;
import org.dgfoundation.amp.nireports.runtime.Column;
import org.dgfoundation.amp.nireports.runtime.ColumnReportData;
import org.dgfoundation.amp.nireports.runtime.GroupReportData;
import org.dgfoundation.amp.nireports.runtime.NiCell;

/**
 * renders the result of running a NiReport to a string
 * TODO: maybe a generic visitor?
 * @author Dolghier Constantin
 *
 */
public class NiReportRenderer {
	
	final NiReportsEngine engine;
	final ReportData report;
	final NiHeaderInfo headers;
	
	public NiReportRenderer(NiReportsEngine engine, ReportData report) {
		this.engine = engine;
		this.report = report;
		this.headers = engine.headers;
	}
	
	public String render() {
		StringBuilder res = new StringBuilder(String.format("<table class='nireport_table inside' cellpadding='0' cellspacing='0' %s>", "width='100%'"));
		renderHeaders(res);
		res.append("\n");
		renderBody(res, report);
		res.append("</table>");
		return res.toString();
	}
	
	protected StringBuilder renderHeaders(StringBuilder res) {
		res.append("<thead>");
		NiHeaderInfo headers = engine.headers;
		for(int i = 1; i < headers.rasterizedHeaders.size(); i++) {
			SortedMap<Integer, Column> headerRow = headers.rasterizedHeaders.get(i);
			res.append("<tr class='nireport_header'>");
			for(Integer startCol:headerRow.keySet()) {
				ReportHeadingCell rhc = headerRow.get(startCol).getReportHeaderCell();
				res.append(String.format("<td class='nireport_header' rowSpan='%d' colSpan='%d'>%s</td>", rhc.getRowSpan(), rhc.getColSpan(), rhc.name));
			}
			res.append("</tr>\n");
		}
		res.append("</thead>");
		return res;
	}
	
	protected StringBuilder renderBody(StringBuilder res, ReportData element) {
		res.append("<tbody>\n");
		renderReportData(res, element, 0);
		res.append("</tbody>");
		return res;
	}
	
	protected StringBuilder renderReportData(StringBuilder bld, ReportData element, int level) {
		if (element instanceof ColumnReportData) {
			renderColumnRD(bld, (ColumnReportData) element, level);
		} else {
			renderGroupRD(bld, (GroupReportData) element, level);
		}
		return bld;
	}
	
	protected StringBuilder renderGroupRD(StringBuilder bld, GroupReportData grd, int level) {
		if (level == 0)
			bld.append("<tr>");
		
		for(ReportData subReport:grd.getSubReports()) {
			bld.append(String.format("<td class='ni_hierarchyCell ni_hierarchyLevel%d' rowspan='%d'>%s</td>", level + 1, subReport.getRowSpan(false), subReport.splitter.getCell().getDisplayedValue()));
			renderReportData(bld, subReport, level + 1);
		}
		
		return bld;
	}
	
	protected StringBuilder renderColumnRD(StringBuilder bld, ColumnReportData crd, int level) {
		ArrayList<Long> ids = new ArrayList<>(crd.getIds());
		ids.sort(null);
		boolean isFirstId = true;
		for(Long id:ids) {
			if (level == 0 || !isFirstId)
				bld.append("<tr>");
			isFirstId = false;
			int sz = headers.leafColumns.size();
			for(int i = level; i < sz; i++) {
				CellColumn leafHeader = headers.leafColumns.get(i);
				List<NiCell> cells = crd.getContents().get(leafHeader).data.get(id);
				String contents = cells == null || cells.isEmpty() ? "" : ensureMaxLen(leafHeader.getBehaviour().doHorizontalReduce(cells).getDisplayedValue(), 40);
				bld.append("<td class='nireport_data_cell'>");
				bld.append(contents);
				bld.append("</td>");
			}
			bld.append("</tr>\n");
		}
		return bld;
	}
	
	protected String ensureMaxLen(String s, int maxLen) {
		if (s.length() <= maxLen)
			return s;
		return s.substring(0, maxLen - 3) + "...";
	}
}
