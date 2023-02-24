package org.dgfoundation.amp.nireports;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

import org.dgfoundation.amp.newreports.ReportCollapsingStrategy;
import org.dgfoundation.amp.nireports.output.nicells.NiSplitCell;
import org.dgfoundation.amp.nireports.runtime.CellColumn;
import org.dgfoundation.amp.nireports.runtime.ColumnContents;
import org.dgfoundation.amp.nireports.runtime.ColumnReportData;
import org.dgfoundation.amp.nireports.runtime.GroupReportData;
import org.dgfoundation.amp.nireports.runtime.NiCell;
import org.dgfoundation.amp.nireports.runtime.ReportData;
import org.dgfoundation.amp.nireports.runtime.ReportDataVisitor;

/**
 * a visitor which implements the instructions contained in {@link ReportCollapsingStrategy}.
 * e.g. smashes together hierarchies which are deemed as being equal by the given {@link #strategy}
 * @author Dolghier Constantin
 *
 */
public class ReportHierarchiesCollapser implements ReportDataVisitor<ReportData> {

    
    protected final ReportCollapsingStrategy strategy;
    protected final List<CellColumn> leaves;
    
    public ReportHierarchiesCollapser(ReportCollapsingStrategy strategy, Collection<CellColumn> leaves) {
        this.strategy = strategy;
        this.leaves = Collections.unmodifiableList(new ArrayList<>(leaves));
    }
    
    @Override
    public ReportData visitLeaf(ColumnReportData crd) {
        return crd; // not stashing leafs with the same name
    }

    /**
     * applies the {@link #strategy} to the children of a given {@link GroupReportData} 
     */
    @Override
    public ReportData visitGroup(GroupReportData grd) {
        if (grd.getSubReports().isEmpty() || strategy == ReportCollapsingStrategy.NEVER)
            return grd;
        boolean containsCRDs = grd.getSubReports().get(0) instanceof ColumnReportData; // all the children have the same type
        Map<ReportDataDigest, List<ReportData>> childrenByName = new HashMap<>();
        Function<ReportData, ReportDataDigest> digester = strategy == ReportCollapsingStrategy.ALWAYS ? ALWAYS_DIGESTER : UNKNOWNS_DIGESTER;
        
        for(ReportData subReport:grd.getSubReports()) {
            ReportDataDigest key = digester.apply(subReport);
            childrenByName.computeIfAbsent(key, ignored -> new ArrayList<>()).add(subReport);
        }
        List<ReportData> newChildren = new ArrayList<>();
        for(ReportDataDigest childDigest:childrenByName.keySet()) {
            List<? extends ReportData> children = childrenByName.get(childDigest);
            ReportData newChild = containsCRDs ? collapseCRDs((List<ColumnReportData>) children) : collapseGRDs(strategy, (List<GroupReportData>) children);
            newChildren.add(newChild);
        }
        newChildren.sort((a, b) -> a.splitter.compareTo(b.splitter)); // splitter is always nonnull for children
        return grd.clone(newChildren);
    }
        
    public ColumnReportData collapseCRDs(List<ColumnReportData> children) {
        if (children.size() == 1) // make the common case fast
            return new ColumnReportData(children.get(0).context, children.get(0).splitter, children.get(0).getContents());
        
        Map<CellColumn, ColumnContents> contents = new HashMap<>();
        for(CellColumn leaf:leaves) {
            contents.put(leaf, mergeColumnContents(children.stream().map(child -> child.getContents().get(leaf)).filter(z -> z != null).collect(toList())));
        }
        ColumnReportData res = new ColumnReportData(children.get(0).context, NiSplitCell.merge(children.stream().map(z -> z.splitter)), contents);
        return res;
    }
    
    protected ReportData collapseGRDs(ReportCollapsingStrategy strategy, List<GroupReportData> children) {
        List<ReportData> newChildren = new ArrayList<>();
        for(GroupReportData child:children)
            newChildren.addAll(child.getSubReports());
        GroupReportData grouped = new GroupReportData(children.get(0).context, NiSplitCell.merge(children.stream().map(z -> z.splitter)), newChildren);
        ReportData res = grouped.accept(this);
        return res;
    }
    
    protected static ColumnContents mergeColumnContents(List<ColumnContents> in) {
        Map<Long, List<NiCell>> res = new HashMap<>();
        for(ColumnContents elem:in) {
            for(Long id:elem.data.keySet())
                res.computeIfAbsent(id, ignored -> new ArrayList<>()).addAll(elem.data.get(id));
        }
        return new ColumnContents(res);
    }

    /** a marker interface that an object implements one of the discriminating strategies requested by {@link ReportCollapsingStrategy} */
    interface ReportDataDigest {};
    
    /**
     * a digester corresponding to {@link ReportCollapsingStrategy#UNKNOWNS}
     */
    final static Function<ReportData, ReportDataDigest> UNKNOWNS_DIGESTER = rd -> new UnknownDigest(rd);
    
    /**
     * a digester corresponding to {@link ReportCollapsingStrategy#ALWAYS}
     */ 
    final static Function<ReportData, ReportDataDigest> ALWAYS_DIGESTER = rd -> new AlwaysDigest(rd);
    
    /**
     * a ReportData digest which equals 2 RD's if both of them are undefined OR they have the same id
     * @author Dolghier Constantin
     *
     */
    static class UnknownDigest implements ReportDataDigest {
        final long id;
        final boolean undefined;
        
        public UnknownDigest(ReportData rd) {
            this.id = rd.splitter.entityIds.iterator().next();
            this.undefined = rd.splitter.undefined;
        }
        
        @Override
        public int hashCode() {
            return undefined ? Boolean.hashCode(undefined) : Long.hashCode(id);
        }
        
        @Override
        public boolean equals(Object o) {
            UnknownDigest other = (UnknownDigest) o;
            if (undefined ^ other.undefined)
                return false;
            // both defined or both undefined
            return undefined || (id == other.id);
        }
    }
    
    /**
     * a ReportData digest which equals 2 RD's if both of them are undefined OR they have the same name
     * @author Dolghier Constantin
     *
     */
    static class AlwaysDigest implements ReportDataDigest {
        final String name;
        final boolean undefined;
        
        public AlwaysDigest(ReportData rd) {
            this.name = rd.splitter.getDisplayedValue();
            this.undefined = rd.splitter.undefined;
        }
        
        @Override
        public int hashCode() {
            return undefined? Boolean.hashCode(undefined) : name.hashCode(); 
        }
        
        @Override
        public boolean equals(Object o) {
            AlwaysDigest other = (AlwaysDigest) o;
            if (undefined && other.undefined)
                return true;
            // both defined or both undefined
            return name.equals(other.name);
        }
    }
}
