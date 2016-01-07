package org.dgfoundation.amp.nireports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.algo.AmpCollections;
import org.dgfoundation.amp.algo.Graph;
import org.dgfoundation.amp.algo.VivificatingMap;
import org.dgfoundation.amp.algo.timing.InclusiveTimer;
import org.dgfoundation.amp.algo.timing.RunNode;
import org.dgfoundation.amp.newreports.CalendarConverter;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportWarning;
import org.dgfoundation.amp.nireports.meta.CategCell;
import org.dgfoundation.amp.nireports.runtime.CellColumn;
import org.dgfoundation.amp.nireports.runtime.Column;
import org.dgfoundation.amp.nireports.runtime.ColumnContents;
import org.dgfoundation.amp.nireports.runtime.ColumnReportData;
import org.dgfoundation.amp.nireports.runtime.GroupColumn;
import org.dgfoundation.amp.nireports.runtime.GroupReportData;
import org.dgfoundation.amp.nireports.schema.DimensionSnapshot;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.dgfoundation.amp.nireports.schema.NiReportColumn;
import org.dgfoundation.amp.nireports.schema.NiReportMeasure;
import org.dgfoundation.amp.nireports.schema.NiReportsSchema;
import org.dgfoundation.amp.nireports.schema.TimeRange;

/**
 * The NiReports engine API-independent entrypoint. A single report should be run per class instance <br />
 * No schema-specific code below this point. <br />
 * Code can change its APIs at any point below this point - using the AMP Reports API here is entirely optional <br />
 * 
 * @author Dolghier Constantin
 *
 */
public class NiReportsEngine {
	
	public static final Logger logger = Logger.getLogger(NiReportsEngine.class);
	public static final String FUNDING_COLUMN_NAME = "Funding";
	public static final String TOTALS_COLUMN_NAME = "Totals";
	
	// some of the fields below are public because they are part of the "internal" API and might be used by callbacks from deep inside ComputedMeasures / etc
	
	public final NiReportsSchema schema;
	public CalendarConverter calendar;
	
	final NiFilters filters;
	
	final Map<String, ColumnContents<?>> fetchedColumns = new LinkedHashMap<>();
	final Map<String, ColumnContents<?>> fetchedMeasures = new LinkedHashMap<>();
	final VivificatingMap<Long, Set<ReportWarning>> reportWarnings = new VivificatingMap<>(new HashMap<>(), () -> new HashSet<ReportWarning>());
	
	ReportData rootReportData;
	public List<CategAmountCell> funding;
	public final ReportSpecification spec;
	
	/**
	 * all the columns which are used in the actually-run report. Some of them might have been added virtually (like for example as a dependency of a measure, filter or an another column)
	 */
	public LinkedHashSet<String> actualColumns;
	
	/**
	 * all the measures which are used in the actually-run report. Some of them might have been added virtually (like for example as a dependency of a measure)
	 */
	public LinkedHashSet<String> actualMeasures;
	
	/**
	 * all the hierarchies which are used in the actually-run report. Some of them might have been added virtually (like for example as a dependency of a filter). <br />
	 * This is fully included in {@link #actualColumns}, else it's a bug
	 */
	public LinkedHashSet<String> actualHierarchies;
	
	/**
	 * do not access directly! use {@link #getDimensionSnapshot(NiDimension)} instead
	 */
	protected Map<NiDimension, DimensionSnapshot> usedDimensions = new HashMap<>();
	

	public InclusiveTimer timer;
	
	/**
	 * schema-specific scratchpad which is not used by the NiReports engine per se, but is made available to the callbacks <br />
	 * 
	 */
	public SchemaSpecificScratchpad schemaSpecificScratchpad;
	
	public NiReportsEngine(NiReportsSchema schema, ReportSpecification reportSpec) {
		this.schema = schema;
		this.spec = reportSpec;
		this.filters = schema.getFiltersConverter().apply(reportSpec.getFilters());
	}
	 
	public ReportData execute() {
		try(SchemaSpecificScratchpad pad = schema.getScratchpadSupplier().apply(this)) {
			this.schemaSpecificScratchpad = pad;
			this.calendar = this.spec.getSettings() != null && this.spec.getSettings().getCalendar() != null ? this.spec.getSettings().getCalendar() : pad.getDefaultCalendar();
			
			this.timer = new InclusiveTimer("Report " + spec.getReportName());
			timer.run("exec", this::runReport);
			RunNode timingInfo = timer.getCurrentState();
			printReportWarnings();
			logger.warn(String.format("it took %d millies to generate report, the breakdown is:\n%s", timer.getWallclockTime(), timingInfo.asUserString(3)));
			return rootReportData;
		}
		catch(Exception e) {
			throw AlgoUtils.translateException(e);
		}
	}
	
	/**
	 * overrideable by users
	 */
	protected void runReport() {
		timer.run("fetch", this::fetchData);
		timer.run("init", this::createInitialReport);
		timer.run("hierarchies", this::createHierarchies);
		timer.run("totals", this::createTotals);
	}
	
	/**
	 * 1. fetches funding
	 * 2. fetches the other columns
	 * 3. fetches the measures 
	 */
	protected void fetchData() {
		timer.run("columns", this::fetchColumns);
		timer.run("measures", this::fetchMeasures);
		NiUtils.failIf(this.actualColumns.isEmpty(), "columnless reports not supported");
		NiUtils.failIf(!this.actualColumns.containsAll(this.actualHierarchies), () -> String.format("not all hierarchies (%s) are also specified as columns (%s)", this.actualHierarchies.toString(), this.actualColumns.toString()));
	}
	
	protected void fetchColumns() {
		timer.run("Funding", () -> { 
			funding = schema.getFundingFetcher().fetchColumn(this);
			timer.putMetaInNode("cells", funding.size());
			});
		for(NiReportColumn<?> colToFetch:getReportColumns()) {
			timer.run(colToFetch.name, () -> fetchedColumns.put(colToFetch.name, fetchColumn(colToFetch)));
		};
		//fetchedColumns.values().forEach(col -> logger.error(String.format("the column %s contents is %s", col.name, col.getItems().toString())));
	}
	
	protected ColumnContents<?> fetchColumn(NiReportColumn<?> colToFetch) throws Exception {
		List<? extends Cell> cells = colToFetch.fetchColumn(this);
		timer.putMetaInNode("cells", cells.size());
		return new ColumnContents<>(cells);
	}
	
	protected ColumnContents<?> fetchMeasure(NiReportMeasure<?> colToFetch) throws Exception {
		List<? extends Cell> cells = colToFetch.buildCells(this);
		timer.putMetaInNode("cells", cells.size());
		return new ColumnContents<>(cells);
	}	
	
	/**
	 * builds the dependency graph of the measures and then fetches them in order
	 * 
	 */
	protected void fetchMeasures() {
		this.actualMeasures = new LinkedHashSet<>(spec.getMeasureNames());
		Map<String, NiReportMeasure<?>> measures = schema.getMeasures();
		List<NiReportMeasure<?>> measuresToFetch = new ArrayList<>();
		for(String measName:spec.getMeasureNames()) {
			if (measures.containsKey(measName))
				measuresToFetch.add(measures.get(measName));
			else {
				addReportWarning(new ReportWarning(String.format("measure %s not supported in NiReports", measName)));
				this.actualMeasures.remove(measName);
			}
		}
		Graph<NiReportMeasure<?>> measuresGraph = new Graph<>(measuresToFetch, meas -> meas.getPrecursorMeasures().stream().map(measName -> measures.get(measName)).collect(Collectors.toList()));
		LinkedHashSet<NiReportMeasure<?>> measuresInSortOrder = measuresGraph.sortTopologically();
		for(NiReportMeasure<?> meas:measuresInSortOrder) {
			timer.run(meas.name, () -> fetchedMeasures.put(meas.name, new ColumnContents<>(meas.buildCells(this))));
		}
	}
	
	protected void createInitialReport() {
		ColumnReportData fetchedData = new ColumnReportData(this);
		
		fetchedColumns.forEach((name, contents) -> fetchedData.contents.addColumn(new CellColumn<>(name, contents, null, schema.getColumns().get(name).getBehaviour()))); // regular columns
		
		fetchedData.contents.maybeAddColumn(buildFundingColumn(FUNDING_COLUMN_NAME, this::separateYears));
		fetchedData.contents.maybeAddColumn(buildFundingColumn(TOTALS_COLUMN_NAME, Function.identity()));
		
		timer.run("categorize", () -> this.rootReportData = this.categorizeData(fetchedData));
	}

	protected GroupColumn separateYears(GroupColumn fundingColumn) {
		TimeRange userRequestedRange = TimeRange.forCriteria(spec.getGroupingCriteria());
		List<TimeRange> categories = TimeRange.getRange(TimeRange.YEAR, userRequestedRange);
		GroupColumn res = fundingColumn;
		for(TimeRange tr:categories) {
			Function<Cell, ComparableValue<String>> func = (cell -> tr.getDateComponentCategorizer().apply((DatedCell) cell));
			res = res.verticallySplitByCategory(func);
		}
		return res;
	}
	
	protected Column buildFundingColumn(String columnName, Function<GroupColumn, GroupColumn> postprocessor) {
		GroupColumn fundingColumn = new GroupColumn(columnName, null, null);  // yearly funding
		fetchedMeasures.forEach((name, contents) -> {
			NiReportMeasure<?> meas = schema.getMeasures().get(name);
			if (meas.getBehaviour().getTimeRange() != TimeRange.NONE)
				fundingColumn.addColumn(new CellColumn<>(name, contents, fundingColumn, meas.getBehaviour()));
		});
		GroupColumn res = postprocessor.apply(fundingColumn);
		return res;
	}
	
	protected ReportData categorizeData(ColumnReportData fetchedData) {
//		TimeRange userRequestedRange = TimeRange.forCriteria(spec.getGroupingCriteria());
		//fetchedData.contents.getSubColumns())
		return fetchedData;
	}
	
	protected void runComputedMeasures() {
		
	}
	
	protected void createHierarchies() {
		
	}
	
	protected void createTotals() {
		
	}
	
	protected List<NiReportColumn<? extends Cell>> getReportColumns() {
		List<NiReportColumn<? extends Cell>> res = new ArrayList<>();
		this.actualHierarchies = new LinkedHashSet<>(spec.getHierarchyNames());
		this.actualColumns = new LinkedHashSet<>(spec.getColumnNames());
		for(String columnName:AmpCollections.union(spec.getHierarchyNames(), spec.getColumnNames())) {
			NiReportColumn<? extends Cell> col = schema.getColumns().get(columnName);
			if (col == null) {
				addReportWarning(new ReportWarning(String.format("column %s not supported in NiReports", columnName)));
				this.actualHierarchies.remove(columnName);
				this.actualColumns.remove(columnName);
			} else {
				res.add(col);
			}
		}
		return res;
	}
	
	protected void addReportWarning(ReportWarning warning) {
		reportWarnings.getOrCreate(warning.entityId).add(warning);
	}
	
	protected void printReportWarnings() {
		if (!reportWarnings.isEmpty())
			logger.error(reportWarnings.toString());
	}
	
	public synchronized DimensionSnapshot getDimensionSnapshot(NiDimension dimension) {
		if (!usedDimensions.containsKey(dimension)) {
			timer.run("fetch_dimension", () -> {
				timer.run(dimension.name, () -> usedDimensions.put(dimension, dimension.getDimensionData()));
			});
		}
		return usedDimensions.get(dimension);
	}
	
	public NiFilters getFilters() {
		return filters;
	}
	
}
