/**
 * 
 */
package org.dgfoundation.amp.nireports;


/**
 * Cell visitor
 * 
 * @author Nadejda Mandrescu
 */
public interface CellVisitor<K> {
	K visit(AmountCell cell);
	K visit(TextCell cell);
	K visit(PercentageTextCell cell);
	K visit(CategAmountCell cell);
}
