package org.dgfoundation.amp.nireports.schema;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import org.dgfoundation.amp.newreports.ReportFilters;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.NiFilters;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.SchemaSpecificScratchpad;

import com.google.common.base.Function;

/**
 * an interface describing the Schema of a reports' implementation
 * @author Constantin Dolghier
 *
 */
public interface NiReportsSchema {
	/**
	 * returns the list of columns which exist in the schema
	 * @return
	 */
	public Map<String, NiReportColumn<? extends Cell>> getColumns();
	
	/**
	 * returns the list of measures which exist in the schema
	 * @return
	 */
	public Map<String, NiReportMeasure> getMeasures();
	
	/**
	 * returns the fetcher of funding
	 * @return
	 */
	public NiReportColumn<CategAmountCell> getFundingFetcher();

	/**
	 * returns a function which exposes the spec's {@link ReportFilters} instance to Ni's NiFilters
	 * @return
	 */
	public Function<ReportFilters, NiFilters> getFiltersConverter();
	
	/**
	 * gets a builder of a schema-specific scratchpad, which will be stored in {@link NiReportsEngine#schemaSpecificScratchpad} at start time
	 * @return
	 */
	public Function<NiReportsEngine, SchemaSpecificScratchpad> getScratchpadSupplier();
}
