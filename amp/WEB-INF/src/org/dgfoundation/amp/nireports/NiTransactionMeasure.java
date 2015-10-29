package org.dgfoundation.amp.nireports;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import clover.com.google.common.base.Predicate;

/**
 * a trivial measure defined as a transaction 
 * @author Dolghier Constantin
 *
 */
public class NiTransactionMeasure extends NiReportMeasure {
	
	public final Predicate<CategAmountCell> criterion;
	
	public NiTransactionMeasure(String measureName, Predicate<CategAmountCell> criterion) {
		super(measureName);
		this.criterion = criterion;
	}
	
	@Override
	public List<CategAmountCell> buildCells(NiReportContext context) {
		// TODO: replace by java8 awesomeness once ampdev is migrated to j8
		List<CategAmountCell> ret = new ArrayList<>();
		for(CategAmountCell cac:context.funding) {
			if (criterion.apply(cac))
				ret.add(cac);
		}
		return ret;
	}

	/**
	 * trivial measures do not depend on anything
	 */
	@Override
	public Set<String> getPrecursorMeasures() {
		return null;
	}

}
