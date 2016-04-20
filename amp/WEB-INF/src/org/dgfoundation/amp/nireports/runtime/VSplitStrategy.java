package org.dgfoundation.amp.nireports.runtime;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import org.dgfoundation.amp.nireports.ComparableValue;
import org.dgfoundation.amp.nireports.schema.Behaviour;

/**
 * an interface which governs the way a column is split vertically:
 * 1. (TODO) generating subclasses
 * 2. mapping individual cells to subclasses
 * 3. specifying the behaviour of generated {@link CellColumn}s
 * @author Dolghier Constantin
 *
 */
public interface VSplitStrategy {
	public ComparableValue<String> categorize(NiCell cell);
	
	public default String getEntityType() {
		return null;
	}
	
	public default Behaviour<?> getBehaviour(ComparableValue<String> cat, CellColumn splittedColumn) {
		return splittedColumn.behaviour;
	}

	public default List<ComparableValue<String>> getSubcolumnsNames(Set<ComparableValue<String>> existant) {
		return new ArrayList<>(existant);
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
			public List<ComparableValue<String>> getSubcolumnsNames(Set<ComparableValue<String>> existant) {
				return subColumnNames == null ? VSplitStrategy.super.getSubcolumnsNames(existant) : subColumnNames.apply(existant);
			}
			
			@Override
			public String getEntityType() {
				return entityType;
			}
		};
	}
	
	
	public static VSplitStrategy build(Function<NiCell, ComparableValue<String>> cat, String entityType) {
		return new VSplitStrategy() {
			
			@Override
			public ComparableValue<String> categorize(NiCell cell) {
				return cat.apply(cell);
			}
			
			@Override
			public String getEntityType() {
				return entityType;
			}
		};
	}
}
