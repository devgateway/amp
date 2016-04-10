package org.dgfoundation.amp.nireports.amp;

import java.util.List;

import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.ReportElement;
import org.dgfoundation.amp.nireports.BasicFiltersConverter;
import org.dgfoundation.amp.nireports.NiReportsEngine;

public class AmpFiltersConverter extends BasicFiltersConverter {

	public AmpFiltersConverter(NiReportsEngine engine) {
		super(engine);
	}

	@Override
	protected void processMiscElement(ReportElement repElem, List<FilterRule> rules) {
		// TODO: add non-columns
	}

}
