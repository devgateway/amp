package org.dgfoundation.amp.nireports.amp;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.newreports.ReportFilters;
import org.dgfoundation.amp.nireports.AbstractReportsSchema;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.NiFilters;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.SchemaSpecificScratchpad;
import org.dgfoundation.amp.nireports.schema.NiReportColumn;

import com.google.common.base.Function;

public class AmpReportsSchema extends AbstractReportsSchema {

	private static AmpReportsSchema instance = buildInstance();
	
	public static AmpReportsSchema getInstance() {
		return instance;
	}
	
	private static AmpReportsSchema buildInstance() {
		AmpReportsSchema result = new AmpReportsSchema();
		result
			.addTextColumn(ColumnConstants.PROJECT_TITLE, "v_titles")
			.addTextColumn(ColumnConstants.TEAM, "v_teams")
			.addTextColumn(ColumnConstants.OBJECTIVE, "v_objectives")
			.addTextColumn(ColumnConstants.ISSUES, "v_issues");
		return result;
	}
	
	private AmpReportsSchema addTextColumn(String columnName, String view) {
		return (AmpReportsSchema) addColumn(SimpleTextColumn.fromView(columnName, view));
	}
	
	@Override
	public NiReportColumn<CategAmountCell> getFundingFetcher() {
		return new AmpFundingColumn();
	}

	@Override
	public Function<ReportFilters, NiFilters> getFiltersConverter() {
		return (ReportFilters rf) -> new AmpNiFilters();
	}

	@Override
	public Function<NiReportsEngine, SchemaSpecificScratchpad> getScratchpadSupplier() {
		return engine -> new AmpReportsScratchpad(engine);
	}
}
