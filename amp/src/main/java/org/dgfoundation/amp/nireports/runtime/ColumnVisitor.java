package org.dgfoundation.amp.nireports.runtime;

/**
 * a visitor for {@link Column}
 * @author Dolghier Constantin
 *
 * @param <K>
 */
public interface ColumnVisitor<K> {
    public K visit(CellColumn cc);
    public K visit(GroupColumn cc);
}
