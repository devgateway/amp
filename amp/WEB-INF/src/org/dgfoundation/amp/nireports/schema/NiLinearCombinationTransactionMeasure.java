package org.dgfoundation.amp.nireports.schema;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.dgfoundation.amp.newreports.ReportRenderWarning;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.NiUtils;

/**
 * a trivial measure defined as a transaction 
 * @author Dolghier Constantin
 *
 */
public class NiLinearCombinationTransactionMeasure extends NiReportMeasure<CategAmountCell> {
	
	public final Map<NiTransactionMeasure, BigDecimal> terms;
	
	protected final NiTransactionMeasure[] measures;
	protected final BigDecimal[] prods;
	
	public NiLinearCombinationTransactionMeasure(String measureName, Map<NiTransactionMeasure, BigDecimal> terms, Behaviour<?> behaviour, String description) {
		super(measureName, behaviour, description);
		NiUtils.failIf(terms.isEmpty(), () -> String.format("while defining measure %s: you supplied and empty terms list", measureName));
		this.terms = Collections.unmodifiableMap(new HashMap<>(terms));
		this.measures = terms.keySet().toArray(new NiTransactionMeasure[0]);
		this.prods = terms.values().toArray(new BigDecimal[0]);
	}
	
	public NiLinearCombinationTransactionMeasure(String measureName, Map<NiTransactionMeasure, BigDecimal> terms, String description) {
		this(measureName, terms, TrivialMeasureBehaviour.getInstance(), description);
	}
	
	@Override
	public List<CategAmountCell> fetch(NiReportsEngine engine) {
		List<CategAmountCell> res = new ArrayList<>();
		for(CategAmountCell cell:engine.funding) {
			for(int i = 0; i < measures.length; i++) {
				if (measures[i].criterion.test(cell)) {
					CategAmountCell c = multiply(cell, prods[i]);
					if (c != null)
						res.add(c);
					break;
				}
			}
		}
		return res;
	}
	
	protected CategAmountCell multiply(CategAmountCell cell, BigDecimal multiplier) {
		if (multiplier == BigDecimal.ONE)
			return cell;
		return cell.multiply(multiplier);
	}

	/**
	 * trivial measures do not depend on anything
	 */
	@Override
	public Set<String> getPrecursorMeasures() {
		return Collections.emptySet();
	}

	@Override
	public List<ReportRenderWarning> performCheck() {
		return null;
	}
}
