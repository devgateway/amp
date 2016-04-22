package org.dgfoundation.amp.nireports.amp;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.algo.AmpCollections;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.FilterRule.FilterType;
import org.dgfoundation.amp.newreports.ReportElement;
import org.dgfoundation.amp.newreports.ReportElement.ElementType;
import org.dgfoundation.amp.nireports.BasicFiltersConverter;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.dgfoundation.amp.nireports.schema.NiReportColumn;

public class AmpFiltersConverter extends BasicFiltersConverter {

	Set<String> ORED_DIMENSIONS = new HashSet<>(Arrays.asList("locs", "sectors", "progs"));
	
	Set<String> locationColumns = new HashSet<>(Arrays.asList(ColumnConstants.COUNTRY, ColumnConstants.REGION, ColumnConstants.ZONE, ColumnConstants.DISTRICT, ColumnConstants.LOCATION));

	public AmpFiltersConverter(NiReportsEngine engine) {
		super(engine);
	}

	@Override
	protected void processColumnElement(String columnName, List<FilterRule> rules) {
		if (columnName.equals(ColumnConstants.ARCHIVED))
			return; //TODO: hack so that preexisting testcases are not broken while developing the feature
		
		if (columnName.equals(ColumnConstants.DONOR_ID))
			columnName = ColumnConstants.DONOR_AGENCY;
		
		if (columnName.equals(ColumnConstants.DONOR_AGENCY) && (rules == null || (rules.size() == 1 && rules.get(0).filterType == FilterType.VALUES && rules.get(0).values.isEmpty())))
			return; // temporary hack for https://jira.dgfoundation.org/browse/AMP-22602
		
//		if (schema.getColumns().containsKey(columnName) && !schema.getColumns().get(columnName).isTransactionLevelHierarchy())
//			return; //TODO: disable all non-supported filters
		
		if (columnName.equals(ColumnConstants.ACTIVITY_ID)) {
			this.activityIdsPredicate = AmpCollections.mergePredicates(rules.stream().map(FilterRule::buildPredicate).collect(Collectors.toList()));
			return;
		}
		
//		if (locationColumns.contains(columnName)) {
//			LevelColumn lc = schema.getColumns().get(ColumnConstants.REGION).levelColumn.get();
//			for(String cc:locationColumns) {
//				Predicate<Cell> cellPred = cell -> cell.getCoordinates().get(lc.dimensionUsage).id;
//				cellPredicates.computeIfAbsent(cc, ignored -> new ArrayList<>()).addAll(cellPred);
//				filteringColumns.add(cc);
//			}
//		}
		
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
		if (repElem.type == ElementType.DATE) {
			addCellPredicate(NiReportsEngine.FUNDING_COLUMN_NAME, cell -> ((CategAmountCell) cell).amount.getJulianDayCode(), rules);
		}
	}

	@Override
	protected boolean shouldCreateVirtualHierarchy(String columnName) {
		NiReportColumn<?> col = schema.getColumns().get(columnName);
		return col != null && col.getBehaviour().hasPercentages();
	}

	@Override
	protected boolean shouldCollapseDimension(NiDimension dimension) {
		return dimension.depth > 1 && ORED_DIMENSIONS.contains(dimension.name);
	}
}
