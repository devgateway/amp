package org.dgfoundation.amp.nireports.schema;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.dgfoundation.amp.newreports.ReportFilters;
import org.dgfoundation.amp.newreports.ReportRenderWarning;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.NiFilters;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.SchemaSpecificScratchpad;

import com.google.common.base.Function;

/**
 * an interface describing the Schema of an API's reporting needs
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
	public Map<String, NiReportMeasure<?>> getMeasures();
	
	/**
	 * returns the fetcher of funding
	 * @return
	 */
	public NiReportColumn<CategAmountCell> getFundingFetcher(NiReportsEngine engine);

	/**
	 * returns a function which exposes the spec's {@link ReportFilters} instance to Ni's NiFilters
	 * @return
	 */
	public NiFilters convertFilters(NiReportsEngine engine);
	
	/**
	 * gets a builder of a schema-specific scratchpad, which will be stored in {@link NiReportsEngine#schemaSpecificScratchpad} at start time
	 * @return
	 */
	public Function<NiReportsEngine, SchemaSpecificScratchpad> getScratchpadSupplier();
	
	public default Map<String, List<ReportRenderWarning>> performColumnChecks(Optional<Set<String>> columns) {
		Set<String> checkedColumns = columns.isPresent() ? columns.get() : getColumns().keySet();
		Map<String, List<ReportRenderWarning>> res = new HashMap<>();
		for(String colName:checkedColumns) {
			res.put(colName, getColumns().get(colName).performCheck());
		}
		return res;
	}
}