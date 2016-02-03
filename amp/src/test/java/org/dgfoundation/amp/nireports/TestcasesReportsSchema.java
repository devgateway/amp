package org.dgfoundation.amp.nireports;

import java.util.Set;

import org.dgfoundation.amp.newreports.IdsGeneratorSource;
import org.dgfoundation.amp.newreports.ReportFilters;
import org.dgfoundation.amp.nireports.amp.AmpReportsSchema;


import com.google.common.base.Function;

public class TestcasesReportsSchema extends AmpReportsSchema {
	public static IdsGeneratorSource workspaceFilter;
	public final static TestcasesReportsSchema instance = new TestcasesReportsSchema(); 
	
	private TestcasesReportsSchema() {
	}

	@Override
	public Function<ReportFilters, NiFilters> getFiltersConverter() {
		return inputRf -> {
			NiFilters srcFilters = super.getFiltersConverter().apply(inputRf);
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
}
