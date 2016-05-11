package org.dgfoundation.amp.nireports.amp;

import org.dgfoundation.amp.nireports.schema.NiTransactionMeasure;
import org.dgfoundation.amp.nireports.schema.TrivialMeasureBehaviour;

public class TaggedMeasure extends NiTransactionMeasure {

	/**
	 * trivial measure which is split down by a category
	 * @param measureName
	 * @param transactionType
	 * @param adjustmentTypeName
	 * @param directed
	 */
	public TaggedMeasure(String measureName, long transactionType, String adjustmentTypeName, MetaCategory metacat, String pseudocolumnName) {
		super(measureName, 
				cac -> 
					cac.metaInfo.containsMeta(MetaCategory.TRANSACTION_TYPE.category, Long.valueOf(transactionType)) &&
					cac.metaInfo.containsMeta(MetaCategory.ADJUSTMENT_TYPE.category, adjustmentTypeName),
					new TaggedMeasureBehaviour("Total " + measureName, metacat, pseudocolumnName) ,
				AmpReportsSchema.measureDescriptions.get(measureName), false
			);
	}
	
	/**
	 * trivial measure which filters by transactionType only
	 * @param measureName
	 * @param transactionType
	 */
	public TaggedMeasure(String measureName, long transactionType) {
		super(measureName, 
				cac -> cac.metaInfo.containsMeta(MetaCategory.TRANSACTION_TYPE.category, Long.valueOf(transactionType)),
				TrivialMeasureBehaviour.getInstance(),
				AmpReportsSchema.measureDescriptions.get(measureName), false
			);
	}
}
