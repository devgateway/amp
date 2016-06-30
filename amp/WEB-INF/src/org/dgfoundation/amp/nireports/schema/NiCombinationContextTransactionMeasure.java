package org.dgfoundation.amp.nireports.schema;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.dgfoundation.amp.newreports.ReportRenderWarning;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.NiReportsEngine;

/**
 * Measure resulted from combining context measures (NiTransactionContextMeasure). 
 * May combine NiTransactionContextMeasures only. 
 * @author acartaleanu
 *
 */
public class NiCombinationContextTransactionMeasure extends NiReportMeasure<CategAmountCell> {
	
	
	@SuppressWarnings("rawtypes")
	public final Map<NiTransactionContextMeasure, BigDecimal> terms;
	@SuppressWarnings("rawtypes")
	protected final NiTransactionContextMeasure[] measures;
	protected final BigDecimal[] prods;	
	
	
	@SuppressWarnings("rawtypes")
	public NiCombinationContextTransactionMeasure(String name, Map<NiTransactionContextMeasure, BigDecimal> terms, 
			Behaviour<?> behaviour, String description) {
		super(name, behaviour, description);
		this.terms = Collections.unmodifiableMap(new LinkedHashMap<>(terms));
		this.measures =  terms.keySet().toArray(new NiTransactionContextMeasure[0]);
		this.prods = terms.values().toArray(new BigDecimal[0]);
	}

	protected CategAmountCell multiply(CategAmountCell cell, BigDecimal multiplier) {
		if (multiplier == BigDecimal.ONE)
			return cell;
		return cell.multiply(multiplier);
	}
	
	@SuppressWarnings("unchecked")
	public CategAmountCell processCell(CategAmountCell src, Object[] contexts) {
		for(int i = 0; i < measures.length; i++) {
			Object res = measures[i].criterion.apply(contexts[i], src);
			if (Boolean.TRUE.equals((Boolean)res)) {
				CategAmountCell c = multiply(src, prods[i]);
				if (c != null)
					return c;
				break;
			}
		}
		return null;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CategAmountCell> fetch(NiReportsEngine engine) throws Exception {
		Object[] contexts = new Object[measures.length];
		for (int i = 0; i < measures.length; i++ ) {
			contexts[i] = measures[i].contextBuilder.apply(engine);
		}
		return engine.funding.stream().map(cell -> this.processCell(cell, contexts)).filter(z -> z != null).collect(Collectors.toList());
	}

	@Override
	public List<ReportRenderWarning> performCheck() {
		return null;
	}
	
	@Override
	public Set<String> getPrecursorMeasures() {
	    // AMP-23028: per Constantin, feature is incomplete, should remove empty set for now here 
	    return Collections.emptySet();
		//return Arrays.stream(measures).map(z -> z.name).collect(Collectors.toSet());
	}

}
