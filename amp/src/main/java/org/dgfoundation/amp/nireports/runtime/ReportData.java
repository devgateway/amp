package org.dgfoundation.amp.nireports.runtime;

import java.util.List;
import java.util.Set;

import org.dgfoundation.amp.algo.Memoizer;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.output.nicells.NiSplitCell;
import org.dgfoundation.amp.nireports.schema.Behaviour;


/**
 * This class holds a disaggregated report, where all the individual cells, as fetched from the schema,
 * can be found.
 * While the {@link Cell}s are raw, they are enclosed into {@link NiCell} instances to track filtering and percentages;
 * also the cells are distributed across hierarchies.
 * @author Dolghier Constantin
 *
 */
public abstract class ReportData {
    /**TODO: maybe turn it into a reference to {@link IdsAcceptorsBuilder} */
    public final NiReportsEngine context;

    /**
     * the value cell which generated this subreport during horizSplit
     */
    public final NiSplitCell splitter;
    
    /**
     * memoized set of ids
     */
    protected final Memoizer<Set<Long>> _memoIds;

    protected ReportData(NiReportsEngine context, NiSplitCell splitter) {
        this.context = context;         
        this.splitter = splitter;
        this._memoIds = new Memoizer<>(this::_getIds);
    }
    
    public GroupReportData clone(List<? extends ReportData> children) {
        GroupReportData res = new GroupReportData(context, splitter, children);
        return res;
    }
    
    public Set<Long> getIds() {
        return _memoIds.get();
    }
    
    /**
     * recursively split this instance into subinstances. The leaves should generate new leaves, each one of them equaling a given value, governed by the respective leaf's {@link Behaviour}
     * @param column
     * @return
     */
    public abstract GroupReportData horizSplit(CellColumn column);
    public abstract Set<Long> _getIds();
    public abstract boolean isLeaf();
    public abstract<K> K accept(ReportDataVisitor<K> visitor);
        
    @Override
    public String toString() {
        return String.format("%s: %s (id: %s)", this.getClass().getSimpleName(), this.splitter == null ? null : this.splitter.getDisplayedValue(), this.splitter == null ? -1 : this.splitter.entityIds.toString());
    }
}
