package org.dgfoundation.amp.nireports.amp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Supplier;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.HeaderCell;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.nireports.amp.converters.AmpCellVisitor;
import org.dgfoundation.amp.nireports.output.NiColumnReportData;
import org.dgfoundation.amp.nireports.output.NiGroupReportData;
import org.dgfoundation.amp.nireports.output.NiOutCell;
import org.dgfoundation.amp.nireports.output.NiReportData;
import org.dgfoundation.amp.nireports.output.NiReportOutputBuilder;
import org.dgfoundation.amp.nireports.output.NiReportRunResult;
import org.dgfoundation.amp.nireports.runtime.Column;

/**
 * part of the (NiReportsCore, AmpReportsSchema, Reports API) intersection - formats the output
 * @author Dolghier Constantin
 *
 */
public class AmpNiReportsFormatter {
	
	protected static final Logger logger = Logger.getLogger(AmpNiReportsFormatter.class);
	
	public final NiReportRunResult runResult;
	public final ReportSpecification spec;
	public final Supplier<ReportAreaImpl> reportAreaSupplier;
	
	private ReportAreaImpl reportContents = null;
	private List<ReportOutputColumn> leafHeaders = new ArrayList<>();
	private List<ReportOutputColumn> rootHeaders = new ArrayList<>();
	private List<List<HeaderCell>> generatedHeaders = new ArrayList<>();
	private Map<Column, ReportOutputColumn> niColumnToROC = new HashMap<>();
	
	private final AmpCellVisitor cellVisitor;
	
	public AmpNiReportsFormatter(ReportSpecification spec, NiReportRunResult runResult, 
			Supplier<ReportAreaImpl> reportAreaSupplier) {
		this.runResult = runResult;
		this.spec = spec;
		this.reportAreaSupplier = reportAreaSupplier;
		this.cellVisitor = new AmpCellVisitor(spec);
	}
	
	public GeneratedReport format() {
		buildHeaders();
		reportContents = getReportContents(runResult.reportOut);
		return new GeneratedReport(spec, (int) runResult.wallclockTime, null, reportContents, rootHeaders, 
				leafHeaders, generatedHeaders, runResult.timings);
	}

	public static NiReportOutputBuilder<GeneratedReport> asOutputBuilder(Supplier<ReportAreaImpl> reportAreaSupplier) {
		return (ReportSpecification spec, NiReportRunResult runResult) -> 
			new AmpNiReportsFormatter(spec, runResult, reportAreaSupplier).format();
	}
	
	protected void buildHeaders() {
		// build generated headers and computer ReportOutputColumn
		for (int i = 1; i < runResult.headers.rasterizedHeaders.size(); i++) {
			SortedMap<Integer, Column> niHeaderRow = runResult.headers.rasterizedHeaders.get(i);
			List<HeaderCell> ampHeaderRow = new ArrayList<HeaderCell>();
			for (Entry<Integer, Column> entry : niHeaderRow.entrySet()) {
				Column niCol = entry.getValue();
				ReportOutputColumn roc = niColumnToROC.computeIfAbsent(niCol, val -> 
					new ReportOutputColumn(niCol.name, niColumnToROC.get(niCol.getParent()), niCol.name, null));
				ampHeaderRow.add(new HeaderCell(niCol.getReportHeaderCell(), roc));
				if (i == 1)
					rootHeaders.add(roc);
			}
			generatedHeaders.add(ampHeaderRow);
		}
		runResult.headers.leafColumns.forEach(niColumn -> leafHeaders.add(niColumnToROC.get(niColumn)));
	}
	
	/** Provides {@link ReportArea} structure */
	protected ReportAreaImpl getReportContents(NiReportData niReportData) {
		ReportAreaImpl ra = reportAreaSupplier.get();
		runResult.headers.leafColumns.forEach(niCellColumn ->
			ra.getContents().put(niColumnToROC.get(niCellColumn), convert(niReportData.trailCells.get(niCellColumn))));
		
		if (niReportData instanceof NiColumnReportData) {
			ra.setChildren(getChildren((NiColumnReportData) niReportData));
		} else {
			ra.setChildren(getChildren((NiGroupReportData) niReportData));
		}
		return ra;
	}
	
	protected List<ReportArea> getChildren(NiColumnReportData niColumnReportData) {
		SortedMap<Long, ReportAreaImpl> idReportArea = new TreeMap<Long, ReportAreaImpl>();
		runResult.headers.leafColumns.forEach(niCellColumn ->
			niColumnReportData.getIds().forEach(id ->
				idReportArea.computeIfAbsent(id, val -> reportAreaSupplier.get()).getContents().put(
					niColumnToROC.get(niCellColumn), convert(niColumnReportData.contents.get(niCellColumn).get(id)))));
		return new ArrayList<ReportArea>(idReportArea.values());
	}
	
	protected List<ReportArea> getChildren(NiGroupReportData niGroupReportData) {
		List<ReportArea> children = new ArrayList<ReportArea>();
		niGroupReportData.subreports.forEach(subReport -> children.add(getReportContents(subReport)));
		return children;
	}	
	
	protected ReportCell convert(NiOutCell cell) {
		if (cell == null) 
			return new org.dgfoundation.amp.newreports.TextCell(""); //TODO: should return null for the sake of sparse output
		return cell.accept(cellVisitor);
	}
	
}
