package org.dgfoundation.amp.nireports.runtime;

import org.dgfoundation.amp.nireports.ComparableValue;
import org.dgfoundation.amp.nireports.NiUtils;
import org.dgfoundation.amp.nireports.output.nicells.NiOutCell;
import org.dgfoundation.amp.nireports.schema.Behaviour;
import org.dgfoundation.amp.nireports.schema.NiReportedEntity;
import org.digijava.kernel.translator.LocalizableLabel;

import java.util.*;
import java.util.function.Consumer;

import static java.util.Collections.emptyList;

/**
 * a leaf column
 * @author Dolghier Constantin
 *
 */
public class CellColumn extends Column {
    
    public final ColumnContents contents;
    public final Behaviour<?> behaviour;
    public final NiReportedEntity<?> entity;
        
    public CellColumn(String name, LocalizableLabel label, ColumnContents contents, GroupColumn parent, NiReportedEntity<?> entity, NiColSplitCell splitCell) {
        this(name, label, contents, parent, entity, entity.getBehaviour(), splitCell);
    }
    
    public CellColumn(String name, LocalizableLabel label, ColumnContents contents, GroupColumn parent, NiReportedEntity<?> entity, Behaviour<?> behaviour, NiColSplitCell splitCell) {
        super(name, label, parent, splitCell);
        NiUtils.failIf(contents == null, "CellColumn should have a non-null contents");
        this.contents = contents;
        this.behaviour = behaviour;
        this.entity = entity;
    }

    @Override
    public void forEachCell(Consumer<NiCell> acceptor) {
        contents.data.values().forEach(list -> list.forEach(acceptor::accept));
    }

    @Override
    public GroupColumn verticallySplitByCategory(VSplitStrategy strategy, GroupColumn newParent) {
        SortedMap<ComparableValue<String>, List<NiCell>> values = new TreeMap<>();
        this.forEachCell(cell -> values.computeIfAbsent(strategy.categorize(cell), z -> new ArrayList<>()).add(cell));
        ComparableValue<String> totalsSubcolumnCategory = strategy.getTotalSubcolumnName();
        if (totalsSubcolumnCategory != null) {
            values.put(totalsSubcolumnCategory, new ArrayList<>());
            this.forEachCell(cell -> values.get(totalsSubcolumnCategory).add(cell));
        } else if (values.isEmpty() && strategy.getEmptyTotalSubcolumnName() != null) {
            values.put(strategy.getEmptyTotalSubcolumnName(), new ArrayList<>());
        }
            
        GroupColumn res = this.asGroupColumn(null, newParent);
        List<ComparableValue<String>> subColumnNames = strategy.getSubcolumnsNames(values.keySet(), isTotal());
        for(ComparableValue<String> key:subColumnNames) {
            res.addColumn(
                new CellColumn(key.getValue(),
                    new LocalizableLabel(key.getValue()),
                    new ColumnContents(Optional.ofNullable(values.get(key)).orElse(emptyList())),
                    res, 
                    this.entity,
                    strategy.getBehaviour(key, this),
                    strategy.getEntityType() == null ? null : new NiColSplitCell(strategy.getEntityType(), key)));
        };
        return res;
    }

    public ColumnContents getContents() {
        return contents;
    }

    public Behaviour<NiOutCell> getBehaviour() {
        return (Behaviour<NiOutCell>) behaviour; // code ugly as sin because CellColumn has not been parametrized
    }
    
    @Override
    public List<CellColumn> getLeafColumns() {
        return Arrays.asList(this);
    }

    @Override
    public String debugDigest(boolean withContents) {
        String shortDigest = String.format("%s(%d, %s)", this.name, this.contents.countCells(), behaviour == null ? "(no behaviour)" : behaviour.getDebugDigest());
        if (withContents)
            return String.format("%s with contents: %s", shortDigest, contents.toString());
        else
            return shortDigest;
    }
    
    @Override
    public List<Column> getChildrenStartingAtDepth(int depth) {
        if (reportHeaderCell.getStartRow() == depth)
            return Arrays.asList(this);
        else
            return Collections.emptyList();
    }
    
    @Override
    public <K> K accept(ColumnVisitor<K> cv) {
        return cv.visit(this);
    }
    
    public boolean isTotal() {
        return parent.isTotal();
    }
}
