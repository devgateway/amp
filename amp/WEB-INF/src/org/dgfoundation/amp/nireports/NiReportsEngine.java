package org.dgfoundation.amp.nireports;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.nireports.schema.NiReportColumn;
import org.dgfoundation.amp.nireports.schema.NiReportsSchema;

/**
 * The NiReports engine API-independent entrypoint
 * No schema-specific code below this point.
 * Code can change its APIs at any point below this point - using the AMP Reports API here is entirely optional
 * 
 * @author Dolghier Constantin
 *
 */
public class NiReportsEngine {
	
	public static final Logger logger = Logger.getLogger(NiReportsEngine.class);
	
	// some of the fields below are public because they are part of the "internal" API and might be used by callbacks from deep inside ComputedMeasures / etc
	
	final NiReportsSchema schema;
	final CurrencyConvertor currencyConvertor;
	final NiFilters filters;
	
	final Map<String, CellColumn> fetchedColumns = new LinkedHashMap<>();
	final Map<String, CellColumn> fetchedMeasures = new LinkedHashMap<>();
	
	GroupReportData rootReportData;
	public List<CategAmountCell> funding;
	final ReportSpecification spec;
	
	public NiReportsEngine(NiReportsSchema schema, CurrencyConvertor currencyConvertor, ReportSpecification reportSpec) {
		this.schema = schema;
		this.currencyConvertor = currencyConvertor;
		this.spec = reportSpec;
		this.filters = schema.getFiltersConverter().apply(reportSpec.getFilters());
	}
	 
	public GroupReportData execute() {
		fetchData();
		createInitialReport();
		categorizeData();
		createHierarchies();
		createTotals();
		return rootReportData;
	}
	
	/**
	 * 1. fetches funding
	 * 2. fetches the other columns
	 * 3. fetches the measures 
	 */
	protected void fetchData() {
		fetchColumns();
		fetchMeasures();
	}
	
	//TODO: migrate to JDK8
	protected void fetchColumns() {
		funding = schema.getFundingFetcher().fetchColumn(filters);
		for(NiReportColumn<?> colToFetch:getReportColumns())
			fetchedColumns.put(colToFetch.name, fetchColumn(colToFetch));
	}
	
	protected CellColumn fetchColumn(NiReportColumn<? extends Cell> colToFetch) {
		List<Cell> cells = (List) colToFetch.fetchColumn(filters);
		return new CellColumn(colToFetch.name, cells, null);
	}
	
	/**
	 * builds the dependency graph of the measures and then fetches them in order
	 * 
	 */
	protected void fetchMeasures() {
		
	}
	
	protected void createInitialReport() {
		
	}
	
	protected void categorizeData() {
		
	}
	
	protected void runComputedMeasures() {
		
	}
	
	protected void createHierarchies() {
		
	}
	
	protected void createTotals() {
		
	}
	
	protected List<NiReportColumn<? extends Cell>> getReportColumns() {
		List<NiReportColumn<? extends Cell>> res = new ArrayList<>();
		for(String columnName:spec.getColumnNames()) {
			NiReportColumn<? extends Cell> col = schema.getColumns().get(columnName);
			if (col == null) {
				logger.error(String.format("column %s not supported in NiReports, ignoring", columnName));
			} else {
				res.add(col);
			}
		}
		return res;
	}
}
