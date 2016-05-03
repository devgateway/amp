package org.dgfoundation.amp.nireports.schema;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.dgfoundation.amp.newreports.ReportRenderWarning;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.NiReportsEngine;

/**
 * a trivial measure defined as a transaction which is filtered based on context
 * @author Dolghier Constantin
 *
 */
public class NiTransactionContextMeasure<K> extends NiReportMeasure<CategAmountCell> {
	
	public final BiFunction<K, CategAmountCell, Boolean> criterion;
	public final Function<NiReportsEngine, K> contextBuilder;
		
	public NiTransactionContextMeasure(String measureName, Function<NiReportsEngine, K> contextBuilder, BiFunction<K, CategAmountCell, Boolean> criterion, Behaviour<?> behaviour, String description) {
		super(measureName, behaviour, description);
		this.criterion = criterion;
		this.contextBuilder = contextBuilder;
	}
	
	@Override
	public List<CategAmountCell> fetch(NiReportsEngine engine) {
		K context = contextBuilder.apply(engine);
		return engine.funding.stream().filter(cell -> criterion.apply(context, cell)).collect(Collectors.toList());
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
