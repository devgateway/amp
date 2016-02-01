package org.dgfoundation.amp.nireports.output;

import java.util.ArrayList;
import java.util.SortedMap;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.NiHeaderInfo;
import org.dgfoundation.amp.nireports.ReportHeadingCell;
import org.dgfoundation.amp.nireports.runtime.CellColumn;
import org.dgfoundation.amp.nireports.runtime.Column;
import org.dgfoundation.amp.nireports.runtime.NiCell;

/**
 * renders the result of running a NiReport to a html string. See {@link NiReportOutputBuilder}
 * @author Dolghier Constantin
 *
 */
public class NiReportHtmlRenderer {
	
	protected static final Logger logger = Logger.getLogger(NiReportHtmlRenderer.class);
	
	final NiReportData report;
	final NiHeaderInfo headers;
	
	public NiReportHtmlRenderer(NiReportData report, NiHeaderInfo headers) {
		this.report = report;
		this.headers = headers;
	}
	
	/**
	 * a formatter which renders the table only (no html shell)
	 * @return
	 */
	public static NiReportOutputBuilder<String> buildNiReportOutputter() {
		return (ReportSpecification spec, NiReportRunResult runResult) -> new NiReportHtmlRenderer(runResult.reportOut, runResult.headers).render();
	}

	/**
	 * a formatter which renders a full html document - basic styles around the output of {@link #buildNiReportFullPageOutputter()}
	 * @return
	 */
	public static NiReportOutputBuilder<String> buildNiReportFullPageOutputter() {
		return NiReportHtmlRenderer::fullPageOutput;//(ReportSpecification spec, NiReportRunResult runResult) -> new NiReportHtmlRenderer(runResult.reportOut, runResult.headers).render();
	}

	public static String fullPageOutput(ReportSpecification spec, NiReportRunResult reportRun) {
		long start = System.currentTimeMillis();
		String renderedReport = new NiReportHtmlRenderer(reportRun.reportOut, reportRun.headers).render();
		long renderTime = System.currentTimeMillis() - start;
		int reportX = reportRun.headers.leafColumns.size();
		int reportY = reportRun.reportOut.getIds().size();
		
		String reportRunTime = String.format("report runtime: %d millies CPU, %d millies wallclock", reportRun.timings.getTotalTime(), reportRun.wallclockTime);
		String reportRenderTime = String.format("report rendertime: %d millies", renderTime);
		String reportSize = String.format("report size Y*X = %d*%d (%d cells)", reportY, reportX, reportY * reportX);
		
		String pageHeader = String.format("<html><head>%s\n%s</head><body> <div style='position: fixed; left: 0; right: 0; top: 0; bottom: 0; z-index: 9999; background-size: cover; background-image: url(/TEMPLATE/ampTemplate/nireports/nickel2.png)'></div>%s", 
				"<link href='/TEMPLATE/ampTemplate/css_2/amp.css' rel='stylesheet' type='text/css'>", 
				"<link href='/TEMPLATE/ampTemplate/nireports/nireports_view.css' rel='stylesheet' type='text/css'>",
				String.format("<div style='padding: 5px; margin: 20px; border: 1px dotted black; border-radius: 7px'>%s\n%s\n%s</div>", 
						String.format("<p style='margin: 10px'>%s</p>", reportRunTime),
						String.format("<p style='margin: 10px'>%s</p>", reportRenderTime),
						String.format("<p style='margin: 10px'>%s</p>", reportSize)));
		
		logger.error(reportRunTime);
		logger.error(reportRenderTime);
		logger.error(reportSize);
		
		return String.format("%s\n%s%s", pageHeader, renderedReport, "</body></html>");

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
		for(int i = 1; i < headers.rasterizedHeaders.size(); i++) {
			SortedMap<Integer, Column> headerRow = headers.rasterizedHeaders.get(i);
			res.append("<tr class='nireport_header'>");
			for(Integer startCol:headerRow.keySet()) {
				ReportHeadingCell rhc = headerRow.get(startCol).getReportHeaderCell();
				res.append(String.format("<td class='nireport_header' rowSpan='%d' colSpan='%d'>%s</td>", rhc.getRowSpan(), rhc.getColSpan(), rhc.getName()));
			}
			res.append("</tr>\n");
		}
		res.append("</thead>");
		return res;
	}
	
	protected StringBuilder renderBody(StringBuilder res, NiReportData element) {
		res.append("<tbody>\n");
		renderReportData(res, element, 0);
		res.append("</tbody>");
		return res;
	}
	
	protected StringBuilder renderReportData(StringBuilder bld, NiReportData element, int level) {
		if (element instanceof NiColumnReportData) {
			renderColumnRD(bld, (NiColumnReportData) element, level);
		} else {
			renderGroupRD(bld, (NiGroupReportData) element, level);
		}
		renderTrailCells(bld, element, level);
		return bld;
	}
	
	protected StringBuilder renderTrailCells(StringBuilder bld, NiReportData elem, int level) {
		boolean isFirstId = true;
		if (level == 0 || !isFirstId)
			bld.append("<tr>");
		int sz = headers.leafColumns.size();
		for(int i = level; i < sz; i++) {
			CellColumn leafHeader = headers.leafColumns.get(i);
			//BigDecimal percentage = crd.hierarchies.calculatePercentage(leafHeader.getBehaviour().getHierarchiesListener());
			NiOutCell cell = elem.trailCells.get(leafHeader);
			String contents = i < headers.nrHierarchies || cell == null ? "" : ensureMaxLen(cell.getDisplayedValue(), 50);
			bld.append("<td class='nireport_data_cell ni_hierarchyLevel");
			bld.append(level + 1);
			bld.append(" ni_trailcell'>");
			bld.append(contents);
			bld.append("</td>");
		}
		bld.append("</tr>\n");
		return bld;
	}
	
	protected StringBuilder renderGroupRD(StringBuilder bld, NiGroupReportData grd, int level) {
		if (level == 0)
			bld.append("<tr>");
		
		for(NiReportData subReport:grd.subreports) {
			bld.append(String.format("<td class='ni_hierarchyCell ni_hierarchyLevel%d' rowspan='%d'>%s</td>", level + 1, subReport.getRowSpan(false), subreportTitle(subReport)));
			renderReportData(bld, subReport, level + 1);
		}
		
		return bld;
	}

	/**
	 * renderer-specific way of naming an undefined subhier name in an output. In the reference renderer it outputs debug data
	 * @param subReport
	 * @return
	 */
	protected String subreportTitle(NiReportData subReport) {
		NiSplitCell cell = subReport.splitter;
		if (cell == null) return "";
		if (cell.undefined) return String.format("Undefined %s: %s", cell.entity.name, cell.entityIds.toString());
		return cell.getDisplayedValue();
	}
	
	protected StringBuilder renderColumnRD(StringBuilder bld, NiColumnReportData crd, int level) {
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
				//BigDecimal percentage = crd.hierarchies.calculatePercentage(leafHeader.getBehaviour().getHierarchiesListener());
				NiOutCell cell = crd.contents.get(leafHeader).get(id);
				String contents = cell == null ? "" : ensureMaxLen(cell.getDisplayedValue(), 50);
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
