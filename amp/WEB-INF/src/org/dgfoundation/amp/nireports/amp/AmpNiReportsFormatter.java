package org.dgfoundation.amp.nireports.amp;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Iterator;
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
import org.dgfoundation.amp.newreports.TextCell;
import org.dgfoundation.amp.nireports.amp.converters.AmpCellVisitor;
import org.dgfoundation.amp.nireports.output.NiColumnReportData;
import org.dgfoundation.amp.nireports.output.NiGroupReportData;
import org.dgfoundation.amp.nireports.output.NiOutCell;
import org.dgfoundation.amp.nireports.output.NiReportData;
import org.dgfoundation.amp.nireports.output.NiReportOutputBuilder;
import org.dgfoundation.amp.nireports.output.NiReportRunResult;
import org.dgfoundation.amp.nireports.runtime.Column;
import org.digijava.kernel.translator.TranslatorWorker;

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
	private Map<Column, ReportOutputColumn> niColumnToROC = new IdentityHashMap<>();
	
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
		reportContents = getReportContents(runResult.reportOut, 0);
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
				//TODO: until column info is available to clarify if a year column, doing some temporary assumption to not translate columns with numbers
				String trnName = niCol.name.matches("[^0-9]*[0-9]+[^0-9]*") ? niCol.name : 
					TranslatorWorker.translateText(niCol.name);
				ReportOutputColumn roc = niColumnToROC.computeIfAbsent(niCol, val -> 
					new ReportOutputColumn(trnName, niColumnToROC.get(niCol.getParent()), niCol.name, null));
				ampHeaderRow.add(new HeaderCell(niCol.getReportHeaderCell(), roc));
				if (i == 1)
					rootHeaders.add(roc);
			}
			generatedHeaders.add(ampHeaderRow);
		}
		runResult.headers.leafColumns.forEach(niColumn -> leafHeaders.add(niColumnToROC.get(niColumn)));
	}
	
	/** Provides {@link ReportArea} structure */
	protected ReportAreaImpl getReportContents(NiReportData niReportData, int level) {
		ReportAreaImpl ra = reportAreaSupplier.get();
		runResult.headers.leafColumns.forEach(niCellColumn ->
			ra.getContents().put(niColumnToROC.get(niCellColumn), convert(niReportData.trailCells.get(niCellColumn))));
		
		if (niReportData instanceof NiColumnReportData) {
			ra.setChildren(getChildren((NiColumnReportData) niReportData));
		} else {
			ra.setChildren(getChildren((NiGroupReportData) niReportData, level));
		}
		
		fixHierarchyTrailCells(ra, level);
		
		return ra;
	}
	
	protected void fixHierarchyTrailCells(ReportAreaImpl ra, int level) {
		// building trail cells as needed for Reports API, while NiReportData trail cell for hierarchies is empty 
		ReportArea child = ra.getChildren().iterator().next();
		Iterator<Entry<ReportOutputColumn, ReportCell>> iter = ra.getContents().entrySet().iterator();
		if (level == 0 && iter.hasNext()) {
			iter.next().setValue(new TextCell(TranslatorWorker.translateText("Report Totals")));
		}
		for (int i = 0; i < level && iter.hasNext(); i++) {
			Entry<ReportOutputColumn, ReportCell> entry = iter.next();
			String displayedValue = child.getContents().get(entry.getKey()).displayedValue;
			if (i + 1 == level)
				displayedValue += " " + TranslatorWorker.translateText("Totals"); 
			entry.setValue(new TextCell(displayedValue));
		}
	}
	
	protected List<ReportArea> getChildren(NiColumnReportData niColumnReportData) {
		SortedMap<Long, ReportAreaImpl> idReportArea = new TreeMap<Long, ReportAreaImpl>();
		runResult.headers.leafColumns.forEach(niCellColumn ->
			niColumnReportData.getIds().forEach(id ->
				idReportArea.computeIfAbsent(id, val -> reportAreaSupplier.get()).getContents().put(
					niColumnToROC.get(niCellColumn), convert(niColumnReportData.contents.get(niCellColumn).get(id)))));
		return new ArrayList<ReportArea>(idReportArea.values());
	}
	
	protected List<ReportArea> getChildren(NiGroupReportData niGroupReportData, int level) {
		List<ReportArea> children = new ArrayList<ReportArea>();
		niGroupReportData.subreports.forEach(subReport -> children.add(getReportContents(subReport, level + 1)));
		return children;
	}	
	
	protected ReportCell convert(NiOutCell cell) {
		if (cell == null)
			return null;
		return cell.accept(cellVisitor);
	}
	
}
