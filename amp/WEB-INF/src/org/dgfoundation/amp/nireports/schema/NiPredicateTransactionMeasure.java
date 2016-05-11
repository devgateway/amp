package org.dgfoundation.amp.nireports.schema;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.dgfoundation.amp.newreports.ReportRenderWarning;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.NiReportsEngine;

/**
 * a trivial measure defined as a transaction 
 * @author Dolghier Constantin
 *
 */
public abstract class NiPredicateTransactionMeasure extends NiReportMeasure<CategAmountCell> {
	
	protected final boolean ignoreFilters;
	
	public NiPredicateTransactionMeasure(String measureName, Behaviour<?> behaviour, String description, boolean ignoreFilters) {
		super(measureName, behaviour, description);
		this.ignoreFilters = ignoreFilters;
	}
	
	public NiPredicateTransactionMeasure(String measureName, String description) {
		this(measureName, TrivialMeasureBehaviour.getInstance(), description, false);
	}
	
	public List<CategAmountCell> fetch(List<CategAmountCell> funding) {
		return funding.stream().map(this::processCell).filter(z -> z != null).collect(Collectors.toList());
	}
	
	public abstract CategAmountCell processCell(CategAmountCell src);
	
	@Override
	public List<CategAmountCell> fetch(NiReportsEngine engine) {
		if (ignoreFilters) {
			return fetch(engine.unfilteredFunding);
		} else {
			return fetch(engine.funding);
		}		
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
