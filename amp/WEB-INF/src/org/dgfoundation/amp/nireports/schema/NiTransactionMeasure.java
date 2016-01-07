package org.dgfoundation.amp.nireports.schema;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.NiReportsEngine;

/**
 * a trivial measure defined as a transaction 
 * @author Dolghier Constantin
 *
 */
public class NiTransactionMeasure extends NiReportMeasure<CategAmountCell> {
	
	public final Predicate<CategAmountCell> criterion;
	
	public NiTransactionMeasure(String measureName, Predicate<CategAmountCell> criterion) {
		super(measureName, TrivialMeasureBehaviour.instance);
		this.criterion = criterion;
	}
	
	@Override
	public List<CategAmountCell> buildCells(NiReportsEngine engine) {
		return engine.funding.stream().filter(cell -> criterion.test(cell)).collect(Collectors.toList());
	}

	/**
	 * trivial measures do not depend on anything
	 */
	@Override
	public Set<String> getPrecursorMeasures() {
		return Collections.emptySet();
	}
}
