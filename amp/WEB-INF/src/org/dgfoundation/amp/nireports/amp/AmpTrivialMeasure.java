package org.dgfoundation.amp.nireports.amp;

import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.schema.NiTransactionMeasure;

import clover.com.google.common.base.Predicate;

public class AmpTrivialMeasure extends NiTransactionMeasure{

	public AmpTrivialMeasure(final String measureName) {
		super(measureName, new Predicate<CategAmountCell>(){

			@Override public boolean apply(CategAmountCell cac) {
				return cac.metaInfo.catEquals(CategAmountCell.TRANSACTION_MEASURE, measureName);
			}
			
		});
	}

}
