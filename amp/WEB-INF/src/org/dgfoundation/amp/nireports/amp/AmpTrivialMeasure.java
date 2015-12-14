package org.dgfoundation.amp.nireports.amp;

import org.dgfoundation.amp.nireports.schema.NiTransactionMeasure;

public class AmpTrivialMeasure extends NiTransactionMeasure {

	public AmpTrivialMeasure(String measureName, long transactionType, String adjustmentTypeName) {
		super(measureName, cac -> cac.metaInfo.containsMeta(MetaCategory.TRANSACTION_TYPE.category, Long.valueOf(transactionType)) &&
				cac.metaInfo.containsMeta(MetaCategory.ADJUSTMENT_TYPE.category, adjustmentTypeName));
	}
}
