/**
 * 
 */
package org.dgfoundation.amp.nireports.output;


/**
 * Cell visitor
 * 
 * @author Nadejda Mandrescu
 */
public interface CellVisitor<K> {
	K visit(NiAmountCell cell);
	K visit(NiTextCell cell);
	K visit(NiSplitCell cell);
}
