package org.dgfoundation.amp.nireports.runtime;

import java.util.function.Function;

import org.dgfoundation.amp.nireports.ComparableValue;
import org.dgfoundation.amp.nireports.schema.Behaviour;

/**
 * an interface which governs the way a column is split vertically:
 * 1. (TODO) generating subclasses
 * 2. mapping individual cells to subclasses
 * 3. specifying the behaviour of generated {@link CellColumn}s
 * @author Dolghier Constanti
 *
 */
public interface VSplitStrategy {
	public ComparableValue<String> categorize(NiCell cell);
	
	public default Behaviour getBehaviour(ComparableValue<String> cat, CellColumn splittedColumn) {
		return splittedColumn.behaviour;
	}
	
	public static VSplitStrategy build(Function<NiCell, ComparableValue<String>> cat, Function<ComparableValue<String>, Behaviour> beh) {
		return new VSplitStrategy() {

			@Override public ComparableValue<String> categorize(NiCell cell) {
				return cat.apply(cell);
			}
			
			@Override public Behaviour getBehaviour(ComparableValue<String> cat, CellColumn splittedColumn) {
				return beh.apply(cat);
			}
		};
	}
}
