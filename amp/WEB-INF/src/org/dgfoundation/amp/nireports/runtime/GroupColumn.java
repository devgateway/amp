package org.dgfoundation.amp.nireports.runtime;

import org.dgfoundation.amp.nireports.NiUtils;
import org.digijava.kernel.translator.LocalizableLabel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * a column with subcolumns. The column has no cells of its own.
 * An instance can be either mutable or immutable, depending on the used constructor. Once frozen, an instance cannot be unfrozen
 * 
 * @author Dolghier Constantin
 *
 */
public class GroupColumn extends Column {
    protected List<Column> subColumns;
    protected boolean mutable;  
    
    protected boolean isTotal = false;
    
    public GroupColumn(String name, LocalizableLabel label, List<Column> subColumns, GroupColumn parent, 
            NiColSplitCell splitCell) {
        this(name, label, subColumns, parent, splitCell, parent == null ? false : parent.isTotal());
    }
    
    /**
     * constructs a mutable or immutable instance of the class, depending on whether a list of subcolumns has been supplied
     * @param name
     * @param subColumns if null, then instance is mutable, else immutable
     * @param parent
     * @param isTotal if column is in totals column
     */
    public GroupColumn(String name, LocalizableLabel label, List<Column> subColumns, GroupColumn parent, 
            NiColSplitCell splitCell, boolean isTotal) {
        
        super(name, label, parent, splitCell);
        if (subColumns != null) {
            subColumns.forEach(z -> NiUtils.failIf(z.getParent() != this, String.format("trying to add a foreing child %s to %s", z.getHierName(), this.getHierName())));
        }
        this.mutable = true;
        this.subColumns = subColumns == null ? new ArrayList<>() : new ArrayList<>(subColumns);
        this.isTotal = isTotal;
    }
    
    /** adds a column, if it is not null */
    public void maybeAddColumn(Column subColumn) {
        if (subColumn != null)
            addColumn(subColumn);
    }
    
    /**
     * adds a subcolumns to this instance, <strong>if it has not been frozen</strong>. <br />
     * Will crash if the instance is frozen
     * 
     * @param subColumn
     */
    public void addColumn(Column subColumn) {
        NiUtils.failIf(!mutable, () -> String.format("%s is immutable, you are not allowed to add columns here"));
        subColumns.add(subColumn);
        NiUtils.failIf(subColumn.getParent() != this, () -> String.format("you attempted to add subcolumn <%s> to GroupColumn<%s> with a different parent", subColumn.debugDigest(false), this.debugDigest(false)));
    }

    /**
     * returns a read-only view of the subcolumns
     * @return
     */
    public List<Column> getSubColumns() {
        return Collections.unmodifiableList(this.subColumns);
    }
    
    public void replaceChildren(List<Column> children) {
        if (!mutable)
            throw new RuntimeException("not allowed to mutate a frozen instance");
        this.subColumns.clear();
        this.subColumns.addAll(children);
    }
    
    /**
     * finds a direct child by name
     * @param childName
     * @return null if nothing found
     */
    public Column findChildByName(String childName) {
        for(Column subCol:subColumns)
            if (subCol.name.equals(childName))
                return subCol;
        return null;
    }

    @Override
    public void forEachCell(Consumer<NiCell> acceptor) {
        for(Column col:subColumns)
            col.forEachCell(acceptor);
    }

    @Override
    public GroupColumn verticallySplitByCategory(VSplitStrategy strategy, GroupColumn newParent) {
        GroupColumn res = asGroupColumn(null, newParent);
        for(Column col:getSubColumns())
            res.addColumn(col.verticallySplitByCategory(strategy, res));
        
        return res;
    }

    @Override
    public List<CellColumn> getLeafColumns() {
        return subColumns.stream().flatMap(z -> z.getLeafColumns().stream()).collect(Collectors.toList());
    }

    @Override
    public String debugDigest(boolean withContents) {
        return String.format("[%s -> %s]", name, getSubColumns().stream().map(z -> z.debugDigest(withContents)).collect(Collectors.toList()));
    }
        
    @Override
    public List<Column> getChildrenStartingAtDepth(int depth) {
        if (reportHeaderCell.getStartRow() == depth)
            return Arrays.asList(this);
        
        if (reportHeaderCell.getStartRow() > depth)
            return Collections.emptyList();
        
        return subColumns.stream().flatMap(z -> z.getChildrenStartingAtDepth(depth).stream()).collect(Collectors.toList());
    }

    @Override
    public <K> K accept(ColumnVisitor<K> cv) {
        return cv.visit(this);
    }
    
    public boolean isTotal() {
        return isTotal;
    }
}
