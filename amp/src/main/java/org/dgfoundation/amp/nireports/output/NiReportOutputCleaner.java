package org.dgfoundation.amp.nireports.output;

import org.dgfoundation.amp.algo.AmpCollections;
import org.dgfoundation.amp.nireports.NiHeaderInfo;
import org.dgfoundation.amp.nireports.output.nicells.NiOutCell;
import org.dgfoundation.amp.nireports.output.nicells.NiSplitCell;
import org.dgfoundation.amp.nireports.runtime.CellColumn;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * visitor which makes the final cleanup of data for the NiReports outputs.
 * Now it cleans up non-header-referenced terminals from trail cells and contents.
 *
 * @author Dolghier Constantin
 *
 */
public class NiReportOutputCleaner implements NiReportDataVisitor<NiReportData> {

    /**
     * leaf output-columns which should be kept
     */
    private final Set<CellColumn> kk;
    private final Set<String> kkNames;

    public NiReportOutputCleaner(NiHeaderInfo headers) {
        this.kk = new HashSet<>(headers.leafColumns);
        this.kkNames = kk.stream().map(c -> c.name).collect(Collectors.toSet());
    }
    
    @Override
    public NiReportData visit(NiColumnReportData crd) {
        return new NiColumnReportData(keepKeys(crd.contents, kk), keepKeys(crd.trailCells, kk), crd.splitter);
    }

    @Override
    public NiReportData visit(NiGroupReportData grd) {
        List<NiReportData> subreports = grd.subreports.stream().map(z -> z.accept(this)).collect(Collectors.toList());
        if (kkNames.contains(grd.splitterColumn)) {
            return new NiGroupReportData(subreports, keepKeys(grd.trailCells, kk), grd.splitter);
        } else {
            return mergeColumnReportData(grd, subreports);
        }
    }
    private NiReportData mergeColumnReportData(NiGroupReportData grd, List<NiReportData> subreports) {
        Map<CellColumn, Map<NiRowId, NiOutCell>> contents = new HashMap<>();
        for (NiReportData subReport : subreports) {
            NiColumnReportData colSubReport = (NiColumnReportData) subReport;
            colSubReport.contents.forEach((c, colContents) -> contents.computeIfAbsent(c, k -> new HashMap<>())
                    .putAll(replaceRowIds(colSubReport.splitter, colContents)));
        }
        return new NiColumnReportData(contents, keepKeys(grd.trailCells, kk), grd.splitter);
    }

    private Map<NiRowId, NiOutCell> replaceRowIds(NiSplitCell splitter, Map<NiRowId, NiOutCell> colContents) {
        return AmpCollections.remap(colContents, row -> row.withSplitter(splitter), Function.identity(), false);
    }

    /**
     * returns a new map containing the filtered entries from a given map which have their keys in a whitelisted set of ok keys
     * @param in
     * @param okKeys
     * @return
     */
    protected<K, V> Map<K, V> keepKeys(Map<K, V> in, Set<K> okKeys) {
        return in.entrySet().stream().filter(z -> okKeys.contains(z.getKey())).collect(Collectors.toMap(z -> z.getKey(), z -> z.getValue()));
    }
}
