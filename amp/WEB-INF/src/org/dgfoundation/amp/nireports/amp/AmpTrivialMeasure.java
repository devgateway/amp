package org.dgfoundation.amp.nireports.amp;

import java.util.function.Predicate;

import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.output.NiAmountCell;
import org.dgfoundation.amp.nireports.schema.Behaviour;
import org.dgfoundation.amp.nireports.schema.NiTransactionMeasure;
import org.dgfoundation.amp.nireports.schema.TrivialMeasureBehaviour;
import org.digijava.module.aim.helper.Constants;

public class AmpTrivialMeasure extends NiTransactionMeasure {

	/**
	 * trivial measure which discerns between directed/nondirected
	 * @param measureName
	 * @param transactionType
	 * @param adjustmentTypeName
	 * @param directed
	 */
	public AmpTrivialMeasure(String measureName, long transactionType, String adjustmentTypeName, boolean directed) {
		this(measureName, transactionType, adjustmentTypeName, directed, cac -> false, false, null);
	}
	
	public AmpTrivialMeasure(String measureName, long transactionType, String adjustmentTypeName, boolean directed, boolean ignoreFilters) {
		this(measureName, transactionType, adjustmentTypeName, directed, cac -> false, ignoreFilters, null);
	}
	
	public AmpTrivialMeasure(String measureName, long transactionType, String adjustmentTypeName,
			boolean directed, boolean ignoreFilters, Behaviour<NiAmountCell> overridingBehaviour) {
		this(measureName, transactionType, adjustmentTypeName, directed, cac -> false, ignoreFilters, overridingBehaviour);
	}
	
	
	public AmpTrivialMeasure(String measureName, long transactionType, String adjustmentTypeName, boolean directed, 
			Predicate<CategAmountCell> or) {
		this(measureName, transactionType, adjustmentTypeName, directed, or, false, null);
		
	}
	
	/**
	 * intoruced because the compiler has problems solving the following:
	 * 	Optional.of(overridingBehaviour).orElseGet(directed ? new DirectedMeasureBehaviour() : TrivialMeasureBehaviour.getInstance()),	
	 * would have looked neat, but doesn't work. Extracted method to make the constructor less cumbersome
	 * @param overridingBehaviour
	 * @param directed
	 * @return
	 */
	private static Behaviour<NiAmountCell> getBehaviour(Behaviour<NiAmountCell> overridingBehaviour, boolean directed) {
		if (overridingBehaviour != null)
			return overridingBehaviour;
		else 
			return directed ? new DirectedMeasureBehaviour() : TrivialMeasureBehaviour.getInstance();
	}
	
	public AmpTrivialMeasure(String measureName, long transactionType, String adjustmentTypeName, boolean directed, 
			Predicate<CategAmountCell> or, boolean ignoreFilters, Behaviour<NiAmountCell> overridingBehaviour) {

		super(measureName, 
				cac -> 
					or.test(cac) || (
							cac.metaInfo.containsMeta(MetaCategory.TRANSACTION_TYPE.category, Long.valueOf(transactionType)) &&
							cac.metaInfo.containsMeta(MetaCategory.ADJUSTMENT_TYPE.category, adjustmentTypeName) &&
							(directed ? isDirected(cac) : isDonorSourced(cac))
						),
					getBehaviour(overridingBehaviour, directed),
				AmpReportsSchema.measureDescriptions.get(measureName),
				ignoreFilters
			);
	}
	
	/**
	 * trivial measure which filters by transactionType only
	 * @param measureName
	 * @param transactionType
	 */
	public AmpTrivialMeasure(String measureName, long transactionType) {
		super(measureName, 
				cac -> cac.metaInfo.containsMeta(MetaCategory.TRANSACTION_TYPE.category, Long.valueOf(transactionType)),
				TrivialMeasureBehaviour.getInstance(),
				AmpReportsSchema.measureDescriptions.get(measureName),
				false
			);
	}
	
	protected static boolean isDonorSourced(CategAmountCell cac) {
		return /*(!cac.metaInfo.hasMetaInfo(MetaCategory.SOURCE_ROLE.category)) || */cac.metaInfo.containsMeta(MetaCategory.SOURCE_ROLE.category, Constants.FUNDING_AGENCY);
	}
	
	protected static boolean isDirected(CategAmountCell cac) {
		return cac.metaInfo.hasMetaInfo(MetaCategory.DIRECTED_TRANSACTION_FLOW.category);
	}
}
