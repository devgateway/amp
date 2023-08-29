package org.dgfoundation.amp.nireports.testcases;

import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportWarning;
import org.dgfoundation.amp.nireports.NiHeaderInfo;
import org.dgfoundation.amp.nireports.amp.OutputSettings;
import org.dgfoundation.amp.nireports.output.*;
import org.dgfoundation.amp.nireports.output.nicells.NiOutCell;
import org.dgfoundation.amp.nireports.runtime.CellColumn;
import org.dgfoundation.amp.nireports.runtime.Column;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;
import java.util.function.BiFunction;

import static org.dgfoundation.amp.algo.AmpCollections.relist;

public class ReportModelGenerator implements NiReportOutputBuilder<NiReportModel> {
    
    @Override
    public NiReportModel buildOutput(ReportSpecification spec, NiReportRunResult reportRun) {
        Map<CellColumn, Integer> colToNr = indexLeaves(reportRun.headers);
        
        DecimalFormatSymbols decSymbols = new DecimalFormatSymbols();
        decSymbols.setDecimalSeparator(',');
        decSymbols.setGroupingSeparator(',');
        CellFormatter cellFormatter = new CellFormatter(spec.getSettings(), 
                new DecimalFormat("###,###,###.##", decSymbols), "dd/MM/yyyy", z -> z, 
                new OutputSettings(null), reportRun.calendar);
        
        NiReportsFormatter formatter = new NiReportsFormatter(spec, reportRun, cellFormatter);
        
        return new NiReportModel(spec.getReportName())
            .withHeaders(digestHeaders(reportRun.headers, colToNr))
            .withWarnings(digestWarnings(reportRun.warnings))
            .withBody(reportRun.reportOut.accept(formatter));
    }

    protected Map<CellColumn, Integer> indexLeaves(NiHeaderInfo headers) {
        Map<CellColumn, Integer> colToNr = new IdentityHashMap<>();
        for(int i = 0; i < headers.leafColumns.size(); i++)
            colToNr.put(headers.leafColumns.get(i), i);
        return colToNr;
    }
    
    protected List<String> digestHeaders(NiHeaderInfo headers, Map<CellColumn, Integer> colToNr) {
        return relist(headers.rasterizedHeaders,
            z -> String.join(";", relist(z.values(), this::digestHeaderCell)));
    }
    
    String digestHeaderCell(Column col) {
        return String.format("(%s: (startRow: %d, rowSpan: %d, totalRowSpan: %d, colStart: %d, colSpan: %d))", col.name,
            col.getReportHeaderCell().getStartRow(), col.getReportHeaderCell().getRowSpan(), col.getReportHeaderCell().getTotalRowSpan(), col.getReportHeaderCell().getStartColumn(), col.getReportHeaderCell().getColSpan());
    }
    
    protected List<String> digestWarnings(SortedMap<Long, SortedSet<ReportWarning>> warnings) {
        return relist(warnings.entrySet(), z -> String.format("%d: %s", z.getKey(), z.getValue().toString()));
    }
    
    /**
     * an imperative Visitor which accumulates rows in a list of strings
     *
     */
    class StringRenderer implements NiReportDataVisitor<List<String>> {

        final List<String> rows = new ArrayList<>();
        final Map<CellColumn, Integer> colToNr;
        final List<CellColumn> leaves;
        final BiFunction<NiOutCell, CellColumn, String> cellFormatter;
        int depth = 0;
        
        public StringRenderer(List<CellColumn> leaves, BiFunction<NiOutCell, CellColumn, String> cellFormatter, Map<CellColumn, Integer> colToNr) {
            this.leaves = leaves;
            this.colToNr = colToNr;
            this.cellFormatter = cellFormatter;
        }
        
        @Override
        public List<String> visit(NiColumnReportData crd) {
            preamble(crd);
            for(NiRowId id:crd.ids) {
                List<String> row = new ArrayList<>();
                for(int i = 0; i < depth; i++)
                    row.add("\t");
                for(int i = depth; i < leaves.size(); i++) {
                    CellColumn cc = leaves.get(i);
                    NiOutCell outCell = crd.contents.get(cc).get(id);
                    String fc = formatCell(cc, outCell);
                    row.add(fc);
                }
                rows.add(String.join("| ", row));
            }
            renderTrails(crd);
            return rows;
        }

        @Override
        public List<String> visit(NiGroupReportData grd) {
            preamble(grd);
            depth ++;
            grd.subreports.forEach(z -> z.accept(this));
            depth --;
            renderTrails(grd);
            return rows;
        }
        
        void preamble(NiReportData nrd) {
            if (nrd.splitter != null)
                rows.add(String.format("%s--> %s %s (ids: %s) <--", prefix(), nrd.getClass().getSimpleName(), nrd.splitter.text, nrd.splitter.entityIds));
        }
        
        void renderTrails(NiReportData nrd) {
            List<String> row = new ArrayList<>();
            for(int i = depth; i < leaves.size(); i++) {
                CellColumn cc = leaves.get(i);
                NiOutCell outCell = nrd.trailCells.get(cc);
                String fc = formatCell(cc, outCell);
                row.add(fc);
            }
            rows.add(String.join("|", row));
        }
        
        String formatCell(CellColumn cc, NiOutCell outCell) {
            return outCell == null ? "\t" : cellFormatter.apply(outCell, cc);
        }
        
        String prefix() {
            StringBuilder bld = new StringBuilder();
            for(int i = 0; i < depth; i++)
                bld.append("\t");
            return bld.toString();
        }
    }
}
