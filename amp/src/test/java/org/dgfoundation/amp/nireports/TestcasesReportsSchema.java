package org.dgfoundation.amp.nireports;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.dgfoundation.amp.newreports.IdsGeneratorSource;
import org.dgfoundation.amp.newreports.ReportFilters;
import org.dgfoundation.amp.nireports.amp.AmpReportsSchema;
import org.dgfoundation.amp.nireports.schema.NiReportColumn;
import org.dgfoundation.amp.nireports.schema.NiReportMeasure;
import org.dgfoundation.amp.nireports.schema.NiReportsSchema;

import com.google.common.base.Function;

public class TestcasesReportsSchema implements NiReportsSchema {
	public final NiReportsSchema src;
	public final IdsGeneratorSource workspaceFilter;
	
	public TestcasesReportsSchema(NiReportsSchema src, IdsGeneratorSource workspaceFilter) {
		this.src = src;
		this.workspaceFilter = workspaceFilter;
	}

	@Override
	public Map<String, NiReportColumn<? extends Cell>> getColumns() {
		return src.getColumns();
	}

	@Override
	public Map<String, NiReportMeasure> getMeasures() {
		return src.getMeasures();
	}

	@Override
	public NiReportColumn<CategAmountCell> getFundingFetcher() {
		return src.getFundingFetcher();
	}

	@Override
	public Function<ReportFilters, NiFilters> getFiltersConverter() {
		return inputRf -> {
			NiFilters srcFilters = src.getFiltersConverter().apply(inputRf);
			return new NiFilters() {

				@Override public Set<Long> getActivityIds(NiReportsEngine engine) {
					return workspaceFilter.getIds();
				}

				@Override
				public Set<Long> getSelectedIds(NiReportsEngine engine, String columnName) {
					return srcFilters.getSelectedIds(engine, columnName);
				}};
		};
	}

	@Override
	public Function<NiReportsEngine, SchemaSpecificScratchpad> getScratchpadSupplier() {
		return src.getScratchpadSupplier();
	}

	@Override
	public NiCurrency getCurrencyByCode(Optional<String> currencyCode) {
		return src.getCurrencyByCode(currencyCode);
	}
}
