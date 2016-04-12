package org.dgfoundation.amp.nireports.amp;

import java.util.List;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.ReportElement;
import org.dgfoundation.amp.nireports.BasicFiltersConverter;
import org.dgfoundation.amp.nireports.NiReportsEngine;

public class AmpFiltersConverter extends BasicFiltersConverter {

	public AmpFiltersConverter(NiReportsEngine engine) {
		super(engine);
	}

	@Override
	protected void processColumnElement(String columnName, List<FilterRule> rules) {
		if (columnName.equals(ColumnConstants.ARCHIVED))
			return; //TODO: hack so that preexisting testcases are not broken while developing the feature
		if (schema.getColumns().containsKey(columnName) && !schema.getColumns().get(columnName).isTransactionLevelHierarchy())
			return; //TODO: disable all non-supported filters
		if (schema.getColumns().containsKey(columnName)) {
			super.processColumnElement(columnName, rules);
			return;
		}
		if (columnName.endsWith(" Id")) {
			String newColumnName = columnName.substring(0, columnName.length() - 3);
			if (schema.getColumns().containsKey(newColumnName)) {
				super.processColumnElement(newColumnName, rules);
				return;
			}
		}
		// gone till here -> we're going to fail anyway, but using the superclass
		super.processColumnElement(columnName, rules);
	}
	
	@Override
	protected void processMiscElement(ReportElement repElem, List<FilterRule> rules) {
		// TODO: add non-columns
	}

}
