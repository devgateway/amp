package org.dgfoundation.amp.nireports.amp;

import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Optional;

import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.currencyconvertor.AmpCurrencyConvertor;
import org.dgfoundation.amp.currencyconvertor.CurrencyConvertor;
import org.dgfoundation.amp.newreports.ReportExecutor;
import org.dgfoundation.amp.newreports.ReportFilters;
import org.dgfoundation.amp.nireports.AbstractReportsSchema;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.NiCurrency;
import org.dgfoundation.amp.nireports.NiFilters;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.NiReportsGenerator;
import org.dgfoundation.amp.nireports.SchemaSpecificScratchpad;
import org.dgfoundation.amp.nireports.schema.NiReportColumn;
import org.digijava.module.aim.util.CurrencyUtil;

import com.google.common.base.Function;

/**
 * the big, glorious, immaculate, AMP Reports schema
 * @author Dolghier Constantin
 *
 */
public class AmpReportsSchema extends AbstractReportsSchema {

	private static AmpReportsSchema instance = new AmpReportsSchema();
	
	public static AmpReportsSchema getInstance() {
		return instance;
	}
	
	protected AmpReportsSchema() {
		addTextColumn(ColumnConstants.PROJECT_TITLE, "v_titles");
		addTextColumn(ColumnConstants.TEAM, "v_teams");
		addTextColumn(ColumnConstants.OBJECTIVE, "v_objectives");
		addTextColumn(ColumnConstants.ISSUES, "v_issues");
	}
		
	private AmpReportsSchema addTextColumn(String columnName, String view) {
		return (AmpReportsSchema) addColumn(SimpleTextColumn.fromView(columnName, view));
	}
	
	public final OrganisationsDimension orgsDimension = new OrganisationsDimension("Organisations dimension");
//	public final MathContext mathContext = new MathContext(5, RoundingMode.HALF_EVEN); // banker's rounding
	
	protected final CurrencyConvertor currencyConvertor = AmpCurrencyConvertor.getInstance();
	
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
	
	/**
	 * users entrypoint for running reports using NiReports
	 * @return
	 */
	public static ReportExecutor getExecutor() {
		ReportExecutor res = new NiReportsGenerator(AmpReportsSchema.getInstance());
		return res;
	}

}
