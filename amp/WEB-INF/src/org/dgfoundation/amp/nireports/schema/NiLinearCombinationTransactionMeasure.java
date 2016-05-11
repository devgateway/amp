package org.dgfoundation.amp.nireports.schema;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.NiUtils;

/**
 * a trivial measure defined as a transaction 
 * @author Dolghier Constantin
 *
 */
public class NiLinearCombinationTransactionMeasure extends NiPredicateTransactionMeasure {
	
	public final Map<NiTransactionMeasure, BigDecimal> terms;
	
	protected final NiTransactionMeasure[] measures;
	protected final BigDecimal[] prods;
	
	public NiLinearCombinationTransactionMeasure(String measureName, Map<NiTransactionMeasure, BigDecimal> terms, Behaviour<?> behaviour, String description) {
		super(measureName,  behaviour, description, false);
		NiUtils.failIf(terms.isEmpty(), () -> String.format("while defining measure %s: you supplied an empty terms list", measureName));
		this.terms = Collections.unmodifiableMap(new HashMap<>(terms));
		this.measures = terms.keySet().toArray(new NiTransactionMeasure[0]);
		this.prods = terms.values().toArray(new BigDecimal[0]);
	}
	
	public NiLinearCombinationTransactionMeasure(String measureName, Map<NiTransactionMeasure, BigDecimal> terms, String description) {
		this(measureName, terms, TrivialMeasureBehaviour.getInstance(), description);
	}
	
	@Override
	public CategAmountCell processCell(CategAmountCell src) {
		for(int i = 0; i < measures.length; i++) {
			if (measures[i].criterion.test(src)) {
				CategAmountCell c = multiply(src, prods[i]);
				if (c != null)
					return c;
				break;
			}
		}
		return null;
	}
		
	protected CategAmountCell multiply(CategAmountCell cell, BigDecimal multiplier) {
		if (multiplier == BigDecimal.ONE)
			return cell;
		return cell.multiply(multiplier);
	}
}
