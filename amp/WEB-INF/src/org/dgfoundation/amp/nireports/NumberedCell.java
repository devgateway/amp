package org.dgfoundation.amp.nireports;

/**
 * a Cell which has a number. The number can be a percentage or simple amount
 * @author Dolghier Constantin
 *
 */
public interface NumberedCell {
	public MonetaryAmount getAmount();
}
