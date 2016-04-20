package org.dgfoundation.amp.testmodels;

import org.dgfoundation.amp.nireports.schema.NiTransactionMeasure;
import org.digijava.module.aim.helper.Constants;

/**
 * copied from trivial AmpTrivialMeasure
 * @author acartaleanu
 *
 */
public class TrivialTestMeasure extends NiTransactionMeasure {

	public TrivialTestMeasure(String measureName, long transactionType, String adjustmentTypeName, boolean directed) {
		super(measureName, 
				cac -> cac.metaInfo.containsMeta(TestMetaCategory.TRANSACTION_TYPE.category, Long.valueOf(transactionType)) &&
				cac.metaInfo.containsMeta(TestMetaCategory.ADJUSTMENT_TYPE.category, adjustmentTypeName) &&
				(directed ? (false) : (cac.metaInfo.containsMeta(TestMetaCategory.SOURCE_ROLE.category, Constants.FUNDING_AGENCY))),
				null
			);
	}
}
