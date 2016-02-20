package org.dgfoundation.amp.nireports.amp;

import org.dgfoundation.amp.nireports.schema.NiTransactionMeasure;
import org.digijava.module.aim.helper.Constants;

public class AmpTrivialMeasure extends NiTransactionMeasure {

	public AmpTrivialMeasure(String measureName, long transactionType, String adjustmentTypeName, boolean directed) {
		super(measureName, 
				cac -> 
					cac.metaInfo.containsMeta(MetaCategory.TRANSACTION_TYPE.category, Long.valueOf(transactionType)) &&
					cac.metaInfo.containsMeta(MetaCategory.ADJUSTMENT_TYPE.category, adjustmentTypeName) &&
					(directed ? (false) : (cac.metaInfo.containsMeta(MetaCategory.SOURCE_ROLE.category, Constants.FUNDING_AGENCY))),
				AmpReportsSchema.measureDescriptions.get(measureName)
			);
	}
}
