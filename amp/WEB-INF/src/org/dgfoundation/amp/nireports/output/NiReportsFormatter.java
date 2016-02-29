package org.dgfoundation.amp.nireports.output;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.Stack;
import java.util.function.Supplier;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.algo.AmpCollections;
import org.dgfoundation.amp.newreports.AreaOwner;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.HeaderCell;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.nireports.amp.AmpNiReportsFormatter;
import org.dgfoundation.amp.nireports.amp.OutputSettings;
import org.dgfoundation.amp.nireports.runtime.CellColumn;
import org.dgfoundation.amp.nireports.runtime.Column;


/**
 * part of the (NiReportsCore, AmpReportsSchema, Reports API) intersection - translates NiOut output into ReportsAPI output
 * @author Dolghier Constantin
 *
 */
public class NiReportsFormatter implements NiReportDataVisitor<ReportAreaImpl> {
	
	protected static final Logger logger = Logger.getLogger(NiReportsFormatter.class);
	
	public final NiReportRunResult runResult;
	public final ReportSpecification spec;
	public final Supplier<ReportAreaImpl> reportAreaSupplier;
	
	protected ReportAreaImpl reportContents;
	protected List<ReportOutputColumn> leafHeaders;
	protected List<ReportOutputColumn> rootHeaders = new ArrayList<>();
	protected List<List<HeaderCell>> generatedHeaders = new ArrayList<>();
	protected Map<Column, ReportOutputColumn> niColumnToROC = new IdentityHashMap<>();
	protected Stack<NiSplitCell> hiersStack = new Stack<>();
	
	protected final CellVisitor<ReportCell> cellFormatter;
	protected final List<CellColumn> leafColumns;
	
	public NiReportsFormatter(ReportSpecification spec, NiReportRunResult runResult, 
			Supplier<ReportAreaImpl> reportAreaSupplier, CellVisitor<ReportCell> cellFormatter) {
		this.runResult = runResult;
		this.leafColumns = runResult.headers.leafColumns;
		this.spec = spec;
		this.reportAreaSupplier = reportAreaSupplier;
		this.cellFormatter = cellFormatter;
		buildHeaders();
	}
	
	public GeneratedReport format() {
		reportContents = runResult.reportOut.accept(this);
		return new GeneratedReport(spec, (int) runResult.wallclockTime, null, reportContents, rootHeaders, 
				leafHeaders, generatedHeaders, runResult.timings, runResult.warnings);
	}
	
	/** build generated headers and compute ReportOutputColumn's */
	protected void buildHeaders() {
		for (int i = 1; i < runResult.headers.rasterizedHeaders.size(); i++) {
			SortedMap<Integer, Column> niHeaderRow = runResult.headers.rasterizedHeaders.get(i);
			List<HeaderCell> ampHeaderRow = new ArrayList<HeaderCell>();
			for (Entry<Integer, Column> entry : niHeaderRow.entrySet()) {
				Column niCol = entry.getValue();
				ReportOutputColumn roc = niColumnToROC.computeIfAbsent(niCol, this::buildReportOutputColumn);
				ampHeaderRow.add(new HeaderCell(niCol.getReportHeaderCell(), roc));
				if (i == 1)
					rootHeaders.add(roc);
			}
			generatedHeaders.add(ampHeaderRow);
		}
		leafHeaders = AmpCollections.relist(leafColumns, niColumn -> niColumnToROC.get(niColumn));
	}
	
	protected ReportOutputColumn buildReportOutputColumn(Column niCol) {
		return new ReportOutputColumn(niCol.name, niColumnToROC.get(niCol.getParent()), niCol.name, niCol.getDescription(), null);
	}
	
	protected ReportCell convert(NiOutCell cell, CellColumn niCellColumn) {
		if (cell == null)
			return null;
		return cell.accept(cellFormatter, niCellColumn);
	}

	protected ReportAreaImpl renderCrdRow(NiColumnReportData crd, long id) {
		ReportAreaImpl row = reportAreaSupplier.get();
		Map<ReportOutputColumn, ReportCell> rowData = new LinkedHashMap<>();
		for(int i = hiersStack.size(); i < runResult.headers.leafColumns.size(); i++) {
			CellColumn niCellColumn = runResult.headers.leafColumns.get(i);
			ReportCell reportCell = convert(crd.contents.get(niCellColumn).get(id), niCellColumn);
			if (reportCell != null)
				rowData.put(niColumnToROC.get(niCellColumn), reportCell);
		}
		row.setContents(rowData);
		return row;
	}
	
	@Override
	public ReportAreaImpl visit(NiColumnReportData crd) {
		ReportAreaImpl res = reportAreaSupplier.get();
		res.setOwner(toAreaOwner(crd.splitter));
		res.setContents(trailCells(crd));
		if (!spec.isSummaryReport())
			res.setChildren(AmpCollections.relist(crd.getIds(), id -> renderCrdRow(crd, id)));
		return res;
	}

	@Override
	public ReportAreaImpl visit(NiGroupReportData grd) {
		ReportAreaImpl res = reportAreaSupplier.get();
		res.setOwner(toAreaOwner(grd.splitter));
		res.setContents(trailCells(grd));
		List<ReportArea> rchildren = new ArrayList<>();
		for(NiReportData child:grd.subreports) {
			hiersStack.push(child.splitter);
			rchildren.add(child.accept(this));
			hiersStack.pop();
		}
		res.setChildren(rchildren);
		return res;
	}
	
	protected AreaOwner toAreaOwner(NiSplitCell cell) {
		if (cell == null) return null;
		return new AreaOwner(cell.entity.name, convert(cell, null).displayedValue);
	}
	
	protected Map<ReportOutputColumn, ReportCell> trailCells(NiReportData niReportData) {
		Map<ReportOutputColumn, ReportCell> res = new LinkedHashMap<>();
		for(int i = hiersStack.size(); i < leafColumns.size(); i++) {
			CellColumn niCellColumn = leafColumns.get(i);
			ReportCell reportCell = convert(niReportData.trailCells.get(niCellColumn), niCellColumn);
			if (reportCell != null)
				res.put(niColumnToROC.get(niCellColumn), reportCell);
		}
		if (niReportData.splitter != null)
			res.put(niColumnToROC.get(leafColumns.get(hiersStack.size() - 1)), niReportData.splitter.accept(cellFormatter, leafColumns.get(hiersStack.size() - 1)));
		return res;
	}
	
	public List<ReportOutputColumn> getLeafHeaders() {
		return Collections.unmodifiableList(this.leafHeaders);
	}
	
	public static NiReportOutputBuilder<GeneratedReport> asOutputBuilder(CellVisitor<ReportCell> formatter, OutputSettings outputSettings) {
		return (ReportSpecification spec, NiReportRunResult runResult) -> new NiReportsFormatter(spec, runResult, () -> new ReportAreaImpl(), formatter).format();
	}
}
