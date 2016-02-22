package org.dgfoundation.amp.nireports.amp;

import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.schema.NiTransactionMeasure;
import org.dgfoundation.amp.nireports.schema.TrivialMeasureBehaviour;
import org.digijava.module.aim.helper.Constants;

public class AmpTrivialMeasure extends NiTransactionMeasure {

	public AmpTrivialMeasure(String measureName, long transactionType, String adjustmentTypeName, boolean directed) {
		super(measureName, 
				cac -> 
					cac.metaInfo.containsMeta(MetaCategory.TRANSACTION_TYPE.category, Long.valueOf(transactionType)) &&
					cac.metaInfo.containsMeta(MetaCategory.ADJUSTMENT_TYPE.category, adjustmentTypeName) &&
					(directed ? isDirected(cac) : isDonorSourced(cac)),
					directed ? new DirectedMeasureBehaviour() : TrivialMeasureBehaviour.getInstance(),
				AmpReportsSchema.measureDescriptions.get(measureName)
			);
	}
	
	protected static boolean isDonorSourced(CategAmountCell cac) {
		return /*(!cac.metaInfo.hasMetaInfo(MetaCategory.SOURCE_ROLE.category)) || */cac.metaInfo.containsMeta(MetaCategory.SOURCE_ROLE.category, Constants.FUNDING_AGENCY);
	}
	
	protected static boolean isDirected(CategAmountCell cac) {
		return cac.metaInfo.hasMetaInfo(MetaCategory.DIRECTED_TRANSACTION_FLOW.category);
	}
}
