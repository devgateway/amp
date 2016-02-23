package org.dgfoundation.amp.nireports.amp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Supplier;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.HeaderCell;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.amp.converters.AmpCellVisitor;
import org.dgfoundation.amp.nireports.output.NiColumnReportData;
import org.dgfoundation.amp.nireports.output.NiGroupReportData;
import org.dgfoundation.amp.nireports.output.NiOutCell;
import org.dgfoundation.amp.nireports.output.NiReportData;
import org.dgfoundation.amp.nireports.output.NiReportOutputBuilder;
import org.dgfoundation.amp.nireports.output.NiReportRunResult;
import org.dgfoundation.amp.nireports.runtime.CellColumn;
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
	
	private final OutputSettings outputSettings;
	private final AmpCellVisitor cellVisitor;
	private boolean isRoot = true;
	
	public AmpNiReportsFormatter(ReportSpecification spec, NiReportRunResult runResult, 
			Supplier<ReportAreaImpl> reportAreaSupplier, OutputSettings outputSettings) {
		this.runResult = runResult;
		this.spec = spec;
		this.reportAreaSupplier = reportAreaSupplier;
		this.outputSettings = outputSettings == null ? defaultOutputSettings(spec) : outputSettings;
		this.cellVisitor = new AmpCellVisitor(spec, 
				runResult.headers != null ? runResult.headers.leafColumns.size() : 0, this.outputSettings);
	}
	
	
	/**
	 * builds an {@link OutputSettings} instance to use in case the user hasn't supplied one 	
	 * @param spec
	 * @return
	 */
	protected OutputSettings defaultOutputSettings(ReportSpecification spec) {
		LinkedHashSet<ReportColumn> columns = new LinkedHashSet<>(spec.getColumns());
		columns.removeAll(spec.getHierarchies());
		Set<String> idsValuesColumns = columns.isEmpty() ? Collections.emptySet() : new HashSet<>(Arrays.asList(columns.iterator().next().getColumnName()));
		return new OutputSettings(idsValuesColumns);
	}
	
	public GeneratedReport format() {
		buildHeaders();
		reportContents = getReportContents(runResult.reportOut);
		return new GeneratedReport(spec, (int) runResult.wallclockTime, null, reportContents, rootHeaders, 
				leafHeaders, generatedHeaders, runResult.timings, runResult.warnings);
	}

	public static NiReportOutputBuilder<GeneratedReport> asOutputBuilder(Supplier<ReportAreaImpl> reportAreaSupplier,
			OutputSettings outputSettings) {
		return (ReportSpecification spec, NiReportRunResult runResult) -> new AmpNiReportsFormatter(spec, runResult, reportAreaSupplier, outputSettings).format();
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
		runResult.headers.leafColumns.forEach(niColumn -> leafHeaders.add(niColumnToROC.get(niColumn)));
	}
	
	protected ReportOutputColumn buildReportOutputColumn(Column niCol) {
		String trnName = getNameTranslation(niCol);
		String trnDescription = getDescriptionTranslation(niCol);
		return new ReportOutputColumn(trnName, niColumnToROC.get(niCol.getParent()), niCol.name, trnDescription, null);
	}
	
	protected String getDescriptionTranslation(Column niCol) {
		String description = niCol.getDescription();
		return description == null ? null : TranslatorWorker.translateText(description);
	}
	
	protected String getNameTranslation(Column niCol) {
		if (niCol.splitCell != null && NiReportsEngine.PSEUDOCOLUMN_YEAR.equals(niCol.splitCell.entityType)) {
			return niCol.name;
		}
		return TranslatorWorker.translateText(niCol.name);
	}
	
	/** Provides {@link ReportArea} structure */
	protected ReportAreaImpl getReportContents(NiReportData niReportData) {
		ReportAreaImpl ra = reportAreaSupplier.get();
		if (isRoot) {
			isRoot = false;
		} else {
			this.cellVisitor.pushRD(niReportData);
		}
		
		runResult.headers.leafColumns.forEach(niCellColumn -> {
			this.cellVisitor.incLevel();
			ReportCell reportCell = convert(niReportData.trailCells.get(niCellColumn), niCellColumn);
			if (reportCell != null) {
				ra.getContents().put(niColumnToROC.get(niCellColumn), reportCell);
			}
		});
		
		if (niReportData instanceof NiColumnReportData) {
			ra.setChildren(getChildren((NiColumnReportData) niReportData));
		} else {
			ra.setChildren(getChildren((NiGroupReportData) niReportData));
		}
		this.cellVisitor.popRD();
		
		return ra;
	}
	
	protected List<ReportArea> getChildren(NiColumnReportData niColumnReportData) {
		if (spec.isSummaryReport())
			return Collections.emptyList();
		this.cellVisitor.setLeaf(true);
		SortedMap<Long, ReportAreaImpl> idReportArea = new TreeMap<Long, ReportAreaImpl>();
		runResult.headers.leafColumns.forEach(niCellColumn -> {
			this.cellVisitor.incLevel();
			niColumnReportData.getIds().forEach(id -> {
				ReportCell reportCell = convert(niColumnReportData.contents.get(niCellColumn).get(id), niCellColumn);
				if (reportCell != null) {
					idReportArea.computeIfAbsent(id, val -> reportAreaSupplier.get()).getContents().put(
							niColumnToROC.get(niCellColumn), reportCell);
				}
			});
		});
		this.cellVisitor.setLeaf(false);
		return new ArrayList<ReportArea>(idReportArea.values());
	}
	
	protected List<ReportArea> getChildren(NiGroupReportData niGroupReportData) {
		List<ReportArea> children = new ArrayList<ReportArea>();
		niGroupReportData.subreports.forEach(subReport -> children.add(getReportContents(subReport)));
		return children;
	}
	
	protected ReportCell convert(NiOutCell cell, CellColumn niCellColumn) {
		if (cell == null)
			return null;
		return cell.accept(cellVisitor, niCellColumn);
	}
	
}
