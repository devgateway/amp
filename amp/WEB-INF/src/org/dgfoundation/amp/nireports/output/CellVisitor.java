/**
 * 
 */
package org.dgfoundation.amp.nireports.output;

import org.dgfoundation.amp.nireports.runtime.CellColumn;


/**
 * {@link NiOutCell} visitor
 * 
 * @author Nadejda Mandrescu
 */
public interface CellVisitor<K> {
	K visit(NiAmountCell cell, CellColumn currentColumn);
	K visit(NiTextCell cell, CellColumn currentColumn);
	K visit(NiSplitCell cell, CellColumn currentColumn);
}
