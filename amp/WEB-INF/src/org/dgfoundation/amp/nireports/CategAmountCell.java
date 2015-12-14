package org.dgfoundation.amp.nireports;

import org.dgfoundation.amp.nireports.meta.MetaInfoSet;

/**
 * a cell with an amount and an attached metadata
 * @author Dolghier Constantin
 *
 */
public class CategAmountCell extends Cell {
	
	/**
	 * the MetaInfo category of a cell's transaction
	 */
	public final static String TRANSACTION_MEASURE = "measure";

	
	public final MonetaryAmount amount;
	public final MetaInfoSet metaInfo;
	
	public CategAmountCell(long activityId, MonetaryAmount amount, MetaInfoSet metaInfo) {
		super(amount, amount.toString(), activityId, -1);
		this.amount = amount;
		this.metaInfo = metaInfo;
	}
}
