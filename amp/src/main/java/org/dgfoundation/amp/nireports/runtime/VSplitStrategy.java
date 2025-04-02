package org.dgfoundation.amp.nireports.runtime;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import org.dgfoundation.amp.nireports.ComparableValue;
import org.dgfoundation.amp.nireports.schema.Behaviour;

/**
 * an interface which governs the way a column is split vertically:
 * 1. generating subclasses
 * 2. mapping individual cells to subclasses
 * 3. specifying the behaviour of generated {@link CellColumn}s
 * @author Dolghier Constantin
 *
 */
public interface VSplitStrategy {
    
    /**
     * categorizes a cell
     * @param cell
     */
    public ComparableValue<String> categorize(NiCell cell);
    
    /**
     * @return the entity type produced by this categorizer (for example, YEAR)
     */
    public default String getEntityType() {
        return null;
    }
    
    /**
     * returns the behaviour of the subcolumn (might be different from the behaviour of the parent column) 
     * @param cat the category which would generate a subset
     * @param splittedColumn the column being split
     */
    public default Behaviour<?> getBehaviour(ComparableValue<String> cat, CellColumn splittedColumn) {
        return splittedColumn.behaviour;
    }

    /**
     * returns the list of the subcolumns to be created based on the list of existent categories
     * @param existent
     * @return
     */
    default List<ComparableValue<String>> getSubcolumnsNames(Set<ComparableValue<String>> existent, boolean isTotal) {
        return new ArrayList<>(existent);
    }
    
    /**
     * returns the category name of the Total subcolumn. If returns null, no Total subcolumn exists.
     * A Total subcolumn gets all the cells of the original column. Thus the result of the split would be to have cat1 | cat2 | ... | catn subcolumns + a "Total" subcolumn containing the reunion of the cells in the natural subcategories.
     * By default there is no subtotals column, thus the default implementation of this function returns null
     * @param engine the context
     * @return
     */
    public default ComparableValue<String> getTotalSubcolumnName() {
        return null;
    }
    
    /**
     * AMP-27773
     * returns the category name of the Total Empty subcolumn. If returns null, no Total Empty subcolumn exists.
     * By default there is no subtotal empty column, thus the default implementation of this function returns null
     * It is used for example for currency splitting, when it is needed to put empty total column in order to avoid
     * hiding measures columns without data
     * 
     * @param engine the context
     * @return
     */
    default ComparableValue<String> getEmptyTotalSubcolumnName() {
        return null;
    }

    public static VSplitStrategy build(Function<NiCell, ComparableValue<String>> cat, Function<ComparableValue<String>, Behaviour<?>> beh, Function<Set<ComparableValue<String>>, List<ComparableValue<String>>> subColumnNames, String entityType) {
        return new VSplitStrategy() {

            @Override public ComparableValue<String> categorize(NiCell cell) {
                return cat.apply(cell);
            }
            
            @Override public Behaviour<?> getBehaviour(ComparableValue<String> cat, CellColumn splittedColumn) {
                return beh.apply(cat);
            }
            
            @Override
            public List<ComparableValue<String>> getSubcolumnsNames(Set<ComparableValue<String>> existant, 
                    boolean isTotal) {
                return subColumnNames == null ? VSplitStrategy.super.getSubcolumnsNames(existant, isTotal) 
                        : subColumnNames.apply(existant);
            }
            
            @Override
            public String getEntityType() {
                return entityType;
            }
        };
    }
    
    /**
     * builds a VSplitStrategu which categorizes cells by a callback. Equivalent to calling {@link #build(Function, String, null)}
     * @param cat
     * @param entityType
     */
    public static VSplitStrategy build(Function<NiCell, ComparableValue<String>> cat, String entityType) {
        return build(cat, entityType, null);
    }
    
    /**
     * builds a VSplitStrategy which categorizes cells by a callback, while optionally sporting a "Total" subcolumn
     * @param cat
     * @param entityType
     * @param totalColumnNameSupplier a supplier of the "total" subcolumn name. In case it is null, the strategy will not feature a Total subcolumn
     * @return
     */
    public static VSplitStrategy build(Function<NiCell, ComparableValue<String>> cat, String entityType, Supplier<ComparableValue<String>> totalColumnNameSupplier) {
        return new VSplitStrategy() {
            
            @Override
            public ComparableValue<String> categorize(NiCell cell) {
                return cat.apply(cell);
            }
            
            @Override
            public String getEntityType() {
                return entityType;
            }
            
            @Override
            public ComparableValue<String> getTotalSubcolumnName() {
                return totalColumnNameSupplier == null ? 
                        VSplitStrategy.super.getTotalSubcolumnName() : 
                        totalColumnNameSupplier.get();
            }
        };
    }
}
