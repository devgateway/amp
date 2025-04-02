package org.dgfoundation.amp.nireports.output;

import java.util.ArrayList;
import java.util.Collections;
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
import org.dgfoundation.amp.newreports.TextCell;
import org.dgfoundation.amp.nireports.amp.OutputSettings;
import org.dgfoundation.amp.nireports.output.nicells.CellVisitor;
import org.dgfoundation.amp.nireports.output.nicells.NiOutCell;
import org.dgfoundation.amp.nireports.output.nicells.NiSplitCell;
import org.dgfoundation.amp.nireports.runtime.CellColumn;
import org.dgfoundation.amp.nireports.runtime.Column;


/**
 * Part of the (NiReportsCore, AmpReportsSchema, Reports API) intersection - translates NiOut output into ReportsAPI output.
 * e.g. turns the {@link NiOutCell} hierarchy into {@link ReportCell} hierarchy and the {@link NiReportData} hierarchy into {@link ReportAreaImpl} hierarchy
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
    
    public NiReportsFormatter(ReportSpecification spec, NiReportRunResult runResult, CellVisitor<ReportCell> cellFormatter) {
        this.runResult = runResult;
        this.leafColumns = runResult.headers.leafColumns;
        this.spec = spec;
        this.reportAreaSupplier = () -> new ReportAreaImpl();
        this.cellFormatter = cellFormatter;
        buildHeaders();
    }
    
    public GeneratedReport format() {
        reportContents = runResult.reportOut.accept(this);
        return new GeneratedReport(spec, (int) runResult.wallclockTime, null, reportContents, rootHeaders, 
                leafHeaders, generatedHeaders, runResult.timings, runResult.warnings, runResult.reportOut.ids.isEmpty());
    }
    
    /** build generated headers and compute ReportOutputColumn's */
    protected void buildHeaders() {
        boolean needToGenerateDummyColumn = spec.isSummaryReport() && spec.getHierarchies().isEmpty();
        for (int i = 1; i < runResult.headers.rasterizedHeaders.size(); i++) {
            SortedMap<Integer, Column> niHeaderRow = runResult.headers.rasterizedHeaders.get(i);
            List<HeaderCell> ampHeaderRow = new ArrayList<HeaderCell>();
            
            if (needToGenerateDummyColumn && i == 1) {
                ReportOutputColumn hdrCol = new ReportOutputColumn("-", null, "-", null, null, null);
                rootHeaders.add(hdrCol);
                ampHeaderRow.add(new HeaderCell(1, runResult.headers.rasterizedHeaders.size() - 1, runResult.headers.rasterizedHeaders.size() - 1, 0, 1, hdrCol));
            }
            
            for (Entry<Integer, Column> entry : niHeaderRow.entrySet()) {
                Column niCol = entry.getValue();
                ReportOutputColumn roc = niColumnToROC.computeIfAbsent(niCol, this::buildReportOutputColumn);
                ampHeaderRow.add(new HeaderCell(niCol.getReportHeaderCell(), roc, needToGenerateDummyColumn ? 1 : 0));
                if (i == 1)
                    rootHeaders.add(roc);
            }
            generatedHeaders.add(ampHeaderRow);
        }
        List<ReportOutputColumn> remappedLeaves = AmpCollections.relist(leafColumns, niColumn -> niColumnToROC.get(niColumn)); 
        leafHeaders = needToGenerateDummyColumn ?
            buildArrayList(rootHeaders.get(0), remappedLeaves) : remappedLeaves;
    }
    
    /**
     * for you LISP lovers - cons :D. Constructs a new list formed by prepending an item to a list
     * @param elem
     * @param list
     * @return
     */
    protected<K> List<K> buildArrayList(K elem, List<K> list) {
        List<K> res = new ArrayList<>();
        res.add(elem);
        res.addAll(list);
        return res;
    }
    
    /**
     * builds a cell to be displayed in lieu of (null) in tabs
     * @param cc
     * @return
     */
    protected ReportCell buildTabsUndefinedCell(CellColumn cc) {
        return new TextCell(String.format("(%s %s)", cc.name, "Unspecified"));
    }
    
    /**
     * builds the "empty cell" field of a column header, 
     * e.g. the value which should be displayed / assumed by client code iff there is no entry for a given (ReportArea, ReportOutputColumn) position in the output
     * @param niCol
     * 
     * Please see {@link ReportOutputColumn#emptyCell}
     * @return
     */
    protected ReportCell buildEmptyCell(Column niCol) {
        if (niCol instanceof CellColumn) {
            CellColumn cc = (CellColumn) niCol;
            NiOutCell niEmptyCell = cc.getBehaviour().getEmptyCell(spec);
            if (niEmptyCell == null) {
                if (spec.isEmptyOutputForUnspecifiedData())
                    return TextCell.EMPTY;
                return buildTabsUndefinedCell(cc);
            } else
                return convert(niEmptyCell, cc);
        } else
            return TextCell.EMPTY;
    }
    
    protected ReportOutputColumn buildReportOutputColumn(Column niCol) {
        ReportCell emptyCell = buildEmptyCell(niCol);
        return new ReportOutputColumn(niCol.name, niColumnToROC.get(niCol.getParent()), niCol.name, null, emptyCell, null);
    }
    
    /**
     * Converts a {@link NiOutCell} to a {@link ReportCell} by using the built-in {@link #cellFormatter}. 
     * This function is a convenience frontend for converting nulls into nulls.
     * @param cell
     * @param niCellColumn
     * @return
     */
    protected ReportCell convert(NiOutCell cell, CellColumn niCellColumn) {
        if (cell == null)
            return null;
        return cell.accept(cellFormatter, niCellColumn);
    }

    /**
     * renders a single row (e.g. the data corresponding to a single entity) in a {@link NiColumnReportData} as a childless {@link ReportAreaImpl}
     * @param crd the leaf holding the entity data
     * @param id the id of the entity to convert
     * @return
     */
    protected ReportAreaImpl renderCrdRow(NiColumnReportData crd, NiRowId id) {
        ReportAreaImpl row = reportAreaSupplier.get();
        row.setOwner(new AreaOwner(id.id));
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
        ReportAreaImpl res = initArea(crd);
        //logger.error("rendering crd with orderedIds: " + crd.orderedIds);
        if (!spec.isSummaryReport())
            res.setChildren(AmpCollections.relist(crd.orderedIds, id -> renderCrdRow(crd, id)));
        return res;
    }

    @Override
    public ReportAreaImpl visit(NiGroupReportData grd) {
        ReportAreaImpl res = initArea(grd);
        List<ReportArea> rchildren = new ArrayList<>();
        for(NiReportData child:grd.subreports) {
            hiersStack.push(child.splitter);
            rchildren.add(child.accept(this));
            hiersStack.pop();
        }
        res.setChildren(rchildren);
        return res;
    }
    
    /**
     * initializes a {@link ReportAreaImpl} given the configured {@link #reportAreaSupplier} and populates the fields which are common to all {@link NiReportData} subclasses
     * @param nrd
     * @return
     */
    protected ReportAreaImpl initArea(NiReportData nrd) {
        ReportAreaImpl res = reportAreaSupplier.get();
        res.setOwner(toAreaOwner(nrd.splitter));
        res.setContents(trailCells(nrd));
        res.setNrEntities(nrd.ids.size());  
        return res;
    }
    
    /**
     * converts a NiReports API {@link NiSplitCell} into its Reports Output API equivalent of {@link AreaOwner}
     * @param cell
     * @return
     */
    protected AreaOwner toAreaOwner(NiSplitCell cell) {
        if (cell == null) return null;
        return new AreaOwner(cell.entity.name, 
                convert(cell, null).displayedValue,
                cell.entityIds == null || cell.entityIds.isEmpty() ? -1 : cell.entityIds.iterator().next()); // choose any of the ids if there is a multitude of them
    }
    
    /**
     * converts the NiReports Output API trail cells into a Reports Output API map
     * @param niReportData
     * @return
     */
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
        return (ReportSpecification spec, NiReportRunResult runResult) -> new NiReportsFormatter(spec, runResult, formatter).format();
    }
}
