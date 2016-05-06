package org.dgfoundation.amp.nireports.amp;

import static java.util.stream.Collectors.toList;
import static org.dgfoundation.amp.algo.AmpCollections.mergePredicates;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.FilterRule.FilterType;
import org.dgfoundation.amp.newreports.ReportElement;
import org.dgfoundation.amp.newreports.ReportElement.ElementType;
import org.dgfoundation.amp.nireports.BasicFiltersConverter;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;
import org.dgfoundation.amp.nireports.schema.NiReportColumn;
import org.dgfoundation.amp.nireports.schema.NiReportsSchema;

/**
 * the AMP filtering rules, used jointly by {@link AmpReportsSchema} and {@link HardcodedReportsTestSchema}
 * @author Dolghier Constantin
 *
 */
public class AmpFiltersConverter extends BasicFiltersConverter {

    final static Map<String, String> DONOR_COLUMNS_TO_PLEDGE_COLUMNS = new HashMap<String, String>() {{
    	put(ColumnConstants.PROJECT_TITLE, ColumnConstants.PLEDGES_TITLES);
        put(ColumnConstants.STATUS, ColumnConstants.PLEDGE_STATUS);
        put(ColumnConstants.MODALITIES, ColumnConstants.PLEDGES_AID_MODALITY);
        
        put(ColumnConstants.DONOR_GROUP, ColumnConstants.PLEDGES_DONOR_GROUP);
        put(ColumnConstants.DONOR_TYPE, ColumnConstants.PLEDGES_DONOR_TYPE);
        
        put(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES, ColumnConstants.PLEDGES_NATIONAL_PLAN_OBJECTIVES);
        
        put(ColumnConstants.PRIMARY_PROGRAM, ColumnConstants.PLEDGES_PROGRAMS);
        put(ColumnConstants.PRIMARY_PROGRAM_LEVEL_1, ColumnConstants.PLEDGES_PROGRAMS);
        put(ColumnConstants.PRIMARY_PROGRAM_LEVEL_2, ColumnConstants.PLEDGES_PROGRAMS_LEVEL_2);
        put(ColumnConstants.PRIMARY_PROGRAM_LEVEL_3, ColumnConstants.PLEDGES_PROGRAMS_LEVEL_3);
        
        put(ColumnConstants.SECONDARY_PROGRAM, ColumnConstants.PLEDGES_SECONDARY_PROGRAMS);
        put(ColumnConstants.SECONDARY_PROGRAM_LEVEL_1, ColumnConstants.PLEDGES_SECONDARY_PROGRAMS);
        put(ColumnConstants.SECONDARY_PROGRAM_LEVEL_2, ColumnConstants.PLEDGES_SECONDARY_PROGRAMS_LEVEL_2);
        put(ColumnConstants.SECONDARY_PROGRAM_LEVEL_3, ColumnConstants.PLEDGES_SECONDARY_PROGRAMS_LEVEL_3);
        
        put(ColumnConstants.TERTIARY_PROGRAM, ColumnConstants.PLEDGES_TERTIARY_PROGRAMS);
        put(ColumnConstants.TERTIARY_PROGRAM_LEVEL_1, ColumnConstants.PLEDGES_TERTIARY_PROGRAMS);
        put(ColumnConstants.TERTIARY_PROGRAM_LEVEL_2, ColumnConstants.PLEDGES_TERTIARY_PROGRAMS_LEVEL_2);
        put(ColumnConstants.TERTIARY_PROGRAM_LEVEL_3, ColumnConstants.PLEDGES_TERTIARY_PROGRAMS_LEVEL_3);

        put(ColumnConstants.PRIMARY_SECTOR, ColumnConstants.PLEDGES_SECTORS);
        put(ColumnConstants.PRIMARY_SECTOR_SUB_SECTOR, ColumnConstants.PLEDGES_SECTORS_SUBSECTORS);
        put(ColumnConstants.PRIMARY_SECTOR_SUB_SUB_SECTOR, ColumnConstants.PLEDGES_SECTORS_SUBSUBSECTORS);
        
        put(ColumnConstants.SECONDARY_SECTOR, ColumnConstants.PLEDGES_SECONDARY_SECTORS);
        put(ColumnConstants.SECONDARY_SECTOR_SUB_SECTOR, ColumnConstants.PLEDGES_SECONDARY_SUBSECTORS);
        put(ColumnConstants.SECONDARY_SECTOR_SUB_SUB_SECTOR, ColumnConstants.PLEDGES_SECONDARY_SUBSUBSECTORS);
        
        put(ColumnConstants.TERTIARY_SECTOR, ColumnConstants.PLEDGES_TERTIARY_SECTORS);
        put(ColumnConstants.TERTIARY_SECTOR_SUB_SECTOR, ColumnConstants.PLEDGES_TERTIARY_SUBSECTORS);
        put(ColumnConstants.TERTIARY_SECTOR_SUB_SUB_SECTOR, ColumnConstants.PLEDGES_TERTIARY_SUBSUBSECTORS);
        
        put(ColumnConstants.TYPE_OF_ASSISTANCE, ColumnConstants.PLEDGES_TYPE_OF_ASSISTANCE);
        
        put(ColumnConstants.ZONE, ColumnConstants.PLEDGES_ZONES);
        put(ColumnConstants.REGION, ColumnConstants.PLEDGES_REGIONS);
        put(ColumnConstants.DISTRICT, ColumnConstants.PLEDGES_DISTRICTS);
        put(ColumnConstants.COUNTRY, ColumnConstants.PLEDGES_COUNTRIES);
    }};

	/**
	 * the dimensions whose {@link NiDimensionUsage} instances are ORed between themselves while filtering (please see the contract for {@link #shouldCollapseDimension(NiDimension)}
	 */
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
				
		if (columnName.equals(ColumnConstants.ACTIVITY_ID)) {
			this.activityIdsPredicate = Optional.of(mergePredicates(rules.stream().map(FilterRule::buildPredicate).collect(toList())));
			return;
		}
		
		if (columnName.equals(ColumnConstants.APPROVAL_STATUS))
			columnName = ColumnConstants.FILTERED_APPROVAL_STATUS;

		columnName = removeIdSuffixIfNeeded(schema, columnName);

		if (this.spec.getReportType() == ArConstants.PLEDGES_TYPE)
	        columnName = DONOR_COLUMNS_TO_PLEDGE_COLUMNS.getOrDefault(columnName, columnName);

		if (schema.getColumns().containsKey(columnName)) {
			super.processColumnElement(columnName, rules);
			return;
		}
				
		// gone till here -> we're going to fail anyway, but using the superclass
		super.processColumnElement(columnName, rules);
	}
	
	public static String removeIdSuffixIfNeeded(NiReportsSchema schema, String columnName) {
		if (columnName.endsWith(" Id")) {
			// cleanup the post-Mondrian mess: if filtering by a "XXX Id" column, treat it as filtering by "XXX", since in Mondrian the "XXX" and "XXX id" columns were distinct entities
			String newColumnName = columnName.substring(0, columnName.length() - 3);
			if (schema.getColumns().containsKey(newColumnName)) {
				return newColumnName;
			}
		}
		
		return columnName;
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

	@Override
	protected boolean shouldIgnoreFilteringColumn(String columnName) {
		if (spec.isAlsoShowPledges() || spec.getReportType() == ArConstants.PLEDGES_TYPE) {
			boolean supported = columnName.startsWith("Pledge") || columnName.equals(ColumnConstants.RELATED_PROJECTS) || DONOR_COLUMNS_TO_PLEDGE_COLUMNS.containsKey(columnName);
			return !supported;
		}
		return false;
	}
}
