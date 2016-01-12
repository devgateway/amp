package org.dgfoundation.amp.nireports;

import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;

import org.dgfoundation.amp.nireports.runtime.CellColumn;
import org.dgfoundation.amp.nireports.runtime.Column;
import org.dgfoundation.amp.nireports.runtime.ColumnReportData;
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
		String headers = renderHeaders();
		String body = renderBody(report);
		return String.format("<table class='nireport_table inside' cellpadding='0' cellspacing='0' %s>%s\n%s</table>", "width='100%'", headers, body);
	}
	
	protected String renderHeaders() {
		StringBuilder res = new StringBuilder("<thead>");
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
		return res.toString();
	}
	
	protected String renderBody(ReportData element) {
		StringBuilder res = new StringBuilder("<tbody>\n");
		if (element instanceof ColumnReportData) {
			res.append(renderCRD((ColumnReportData) element));
		} else {
			
		}
		res.append("</tbody>");
		return res.toString();
	}
	
	protected String renderCRD(ColumnReportData crd) {
		StringBuilder bld = new StringBuilder();
		SortedSet<Long> ids = new TreeSet<>(crd.contents.getIds());
		for(Long id:ids) {
			bld.append("<tr>");
			for(CellColumn leafHeader:headers.leafColumns) {
				List<NiCell> cells = leafHeader.getContents().data.get(id);
				String contents = cells == null || cells.isEmpty() ? "" : cells.get(0).getCell().displayedValue;
				bld.append("<td class='nireport_data_cell'>");
				bld.append(contents);
				bld.append("</td>");
			}
			bld.append("</tr>\n");
		}
		return bld.toString();
	}
}
