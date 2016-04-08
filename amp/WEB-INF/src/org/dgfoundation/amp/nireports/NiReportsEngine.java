package org.dgfoundation.amp.nireports;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.algo.AmpCollections;
import org.dgfoundation.amp.algo.Graph;
import org.dgfoundation.amp.algo.VivificatingMap;
import org.dgfoundation.amp.algo.timing.InclusiveTimer;
import org.dgfoundation.amp.newreports.ReportCollapsingStrategy;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportWarning;
import org.dgfoundation.amp.nireports.output.NiReportData;
import org.dgfoundation.amp.nireports.output.NiReportDataOutputter;
import org.dgfoundation.amp.nireports.output.NiReportOutputCleaner;
import org.dgfoundation.amp.nireports.output.NiReportRunResult;
import org.dgfoundation.amp.nireports.output.NiReportVoidnessChecker;
import org.dgfoundation.amp.nireports.output.sorting.NiReportSorter;
import org.dgfoundation.amp.nireports.runtime.CacheHitsCounter;
import org.dgfoundation.amp.nireports.runtime.CachingCalendarConverter;
import org.dgfoundation.amp.nireports.runtime.CellColumn;
import org.dgfoundation.amp.nireports.runtime.Column;
import org.dgfoundation.amp.nireports.runtime.ColumnContents;
import org.dgfoundation.amp.nireports.runtime.ColumnReportData;
import org.dgfoundation.amp.nireports.runtime.ColumnVisitor;
import org.dgfoundation.amp.nireports.runtime.GroupColumn;
import org.dgfoundation.amp.nireports.runtime.IdsAcceptorsBuilder;
import org.dgfoundation.amp.nireports.runtime.NiCell;
import org.dgfoundation.amp.nireports.runtime.HierarchiesTracker;
import org.dgfoundation.amp.nireports.runtime.NiColSplitCell;
import org.dgfoundation.amp.nireports.runtime.PostMeasureVHiersVisitor;
import org.dgfoundation.amp.nireports.runtime.ReportData;
import org.dgfoundation.amp.nireports.runtime.VSplitStrategy;
import org.dgfoundation.amp.nireports.schema.Behaviour;
import org.dgfoundation.amp.nireports.schema.DimensionSnapshot;
import org.dgfoundation.amp.nireports.schema.IdsAcceptor;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.dgfoundation.amp.nireports.schema.NiDimension.Coordinate;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;
import org.dgfoundation.amp.nireports.schema.NiReportColumn;
import org.dgfoundation.amp.nireports.schema.NiReportMeasure;
import org.dgfoundation.amp.nireports.schema.NiReportedEntity;
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
public class NiReportsEngine implements IdsAcceptorsBuilder {
	
	public static final Logger logger = Logger.getLogger(NiReportsEngine.class);
	public static final String ROOT_COLUMN_NAME = "RAW";
	public static final String FUNDING_COLUMN_NAME = "Funding";
	public static final String TOTALS_COLUMN_NAME = "Totals";
	
	public static final String PSEUDOCOLUMN_YEAR = "#date#year";
	public static final String PSEUDOCOLUMN_QUARTER = "#date#quarter";
	public static final String PSEUDOCOLUMN_MONTH = "#date#month";
	public static final String PSEUDOCOLUMN_MEASURE = "#ni#measure";
	public static final String PSEUDOCOLUMN_COLUMN = "#ni#column";
	
	//public final static Set<String> PSEUDOCOLUMNS = new HashSet<>(Arrays.asList(PSEUDOCOLUMN_MONTH, PSEUDOCOLUMN_QUARTER, PSEUDOCOLUMN_YEAR, PSEUDOCOLUMN_MEASURE, PSEUDOCOLUMN_COLUMN));
	
	// some of the fields below are public because they are part of the "internal" API and might be used by callbacks from deep inside ComputedMeasures / etc
	
	public final NiReportsSchema schema;
	public CachingCalendarConverter calendar;
	
	final NiFilters filters;
	
	final Map<String, ColumnContents> fetchedColumns = new LinkedHashMap<>();
	final Map<String, ColumnContents> fetchedMeasures = new LinkedHashMap<>();
	final SortedMap<Long, SortedSet<ReportWarning>> reportWarnings = new TreeMap<>();
	
	/**
	 * only available once {@link #createInitialReport()} has exited
	 */
	public NiHeaderInfo headers;
	
	/**
	 * updated during {@link #separateYears(GroupColumn)}
	 */
	public int premeasureSplitDepth = 0;
	
	/**
	 * only available once {@link #output()} has exited - contains the full headers, including columns which have been deleted
	 */
	public NiHeaderInfo fullHeaders;

	ReportData rootReportData;
	public NiReportData reportOutput;
	
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
	 * all the measures which are used in the actually-run report, but should not appear in the final report (for example have been added as a dependency of a measure)
	 */
	public LinkedHashSet<String> virtualMeasures;

	/**
	 * all the hierarchies which are used in the actually-run report. Some of them might have been added virtually (like for example as a dependency of a filter). <br />
	 * This is fully included in {@link #actualColumns}, else it's a bug
	 */
	public LinkedHashSet<String> actualHierarchies;
		
	/**
	 * do not access directly! use {@link #getDimensionSnapshot(NiDimension)} instead
	 */
	protected Map<NiDimension, DimensionSnapshot> usedDimensions = new HashMap<>();
	
	/**
	 * a read-only set of the ids used in this report (the so-called 'workspace filter') 
	 */
	protected Set<Long> mainIds;

	public InclusiveTimer timer;
	
	/**
	 * schema-specific scratchpad which is not used by the NiReports engine per se, but is made available to the callbacks <br />
	 * 
	 */
	public SchemaSpecificScratchpad schemaSpecificScratchpad;
	
	public CacheHitsCounter hiersTrackerCounter = new CacheHitsCounter();
	public final Predicate<Long> yearRangeSettingsPredicate;
	
	HierarchiesTracker rootEmptyTracker = HierarchiesTracker.buildEmpty(hiersTrackerCounter);
	
	public NiReportsEngine(NiReportsSchema schema, ReportSpecification reportSpec) {
		this.schema = schema;
		this.spec = reportSpec;
		this.yearRangeSettingsPredicate = spec.getSettings() == null ? (z -> true) : spec.getSettings().buildYearSettingsPredicate();
		this.filters = schema.getFiltersConverter().apply(reportSpec.getFilters());
	}
	 
	public NiReportRunResult execute() {
		try(SchemaSpecificScratchpad pad = schema.getScratchpadSupplier().apply(this)) {
			this.schemaSpecificScratchpad = pad;
			this.calendar = pad.buildCalendarConverter();
			
			this.timer = new InclusiveTimer("Report " + spec.getReportName());
			timer.run("workspaceFilter", () -> this.mainIds = Collections.unmodifiableSet(this.filters.getActivityIds(this)));
			timer.run("exec", this::runReportAndCleanup);
			//printReportWarnings();
			NiReportRunResult runResult = new NiReportRunResult(this.reportOutput, timer.getCurrentState(), timer.getWallclockTime(), this.headers, reportWarnings);
//			logger.warn("JsonBean structure of RunNode:" + timingInfo.asJsonBean());
			logger.warn(String.format("it took %d millies to generate report, the breakdown is:\n%s", runResult.wallclockTime, runResult.timings.asUserString(3)));
			return runResult; 
		}
		catch(Exception e) {
			throw AlgoUtils.translateException(e);
		}
	}
	
	/** writes statistics in the {@link InclusiveTimer} instance */
	protected void writeStatistics() {
		timer.putMetaInNode("calendar_translations", new HashMap<String, Integer>(){{
			put("calls", calendar.getCalls());
			put("noncached", calendar.getNonCachedCalls());
			put("percent_cached", calendar.getCalls() == 0? 0 : 100 - (100 * calendar.getNonCachedCalls() / calendar.getCalls()));
		}});
		timer.putMetaInNode("hierarchies_tracker_stats", hiersTrackerCounter.getStats());
	}
	
	protected void runReportAndCleanup() {
		runReport();
		writeStatistics();
	}
	
	/**
	 * overrideable by users
	 */
	protected void runReport() {
		timer.run("fetch", this::fetchData);
		timer.run("init", this::createInitialReport);
		timer.run("hierarchies", this::createHierarchies);
		timer.run("posthierproc", this::postHierarchiesPostprocess);
		timer.run("flatten", this::flatten);
		timer.run("output", this::output);
	}

	protected boolean isAcceptableYear(int yr) {
		return yearRangeSettingsPredicate.test((long) yr);
	}
	
	/**
	 * returns true iff the said column has no values throughout the report and its behaviour says to delete such columns
	 * @param z
	 * @return
	 */
	protected boolean isEmptyAndDeleteable(CellColumn z) {
		if (!z.getBehaviour().shouldDeleteLeafIfEmpty(z))
			return false;
		return reportOutput.accept(new NiReportVoidnessChecker(z));
	}
	
	/**
	 * does the last-stage postprocessing before exiting, e.g. eliminating undisplayable columns from the output <br />
	 * Right now this is year-range-settings; once computed measures have been implemented this will also be eliminating the unrequested dependencies. Also virtual hierarchies generated by filters will be eliminated here
	 */
	protected void output() {
		this.fullHeaders = headers;
		Predicate<Column> yearRangeSetting = z -> z.splitCell != null && z.splitCell.entityType.equals(PSEUDOCOLUMN_YEAR) && !isAcceptableYear((Integer) z.splitCell.info.comparable);
		Predicate<Column> emptyLeaf = z -> (z instanceof CellColumn) && isEmptyAndDeleteable((CellColumn) z);
		this.headers = new NiHeaderInfo(this, pruneHeaders(this.headers.rootColumn, yearRangeSetting.or(emptyLeaf)), headers.nrHierarchies);
		this.reportOutput = this.reportOutput.accept(new NiReportOutputCleaner(this.headers));
		if (spec.getSorters() != null && !spec.getSorters().isEmpty())
			timer.run("sorting", this::sortOutput);
	}

	protected void sortOutput() {
		this.reportOutput = this.reportOutput.accept(NiReportSorter.buildFor(this));
	}
	
	/**
	 * generates a new instance with new {@link #rasterizedHeaders} and {@link #leafColumns}, but sharing the same {@link #rootColumn}
	 * @param filter
	 * @return
	 */
	protected GroupColumn pruneHeaders(GroupColumn gc, Predicate<Column> filter) {
		List<Column> newColumns = new ArrayList<>();
		for(Column c:gc.getSubColumns()) {
			if (filter.test(c))
				continue;
			if (c instanceof GroupColumn)
				c = pruneHeaders((GroupColumn) c, filter);
			if (c != null)
				newColumns.add(c);
		}
		
		if (newColumns.isEmpty())
			return null;
		
		gc.replaceChildren(newColumns); // ugly, but we want to keep CellColumn references in children
		return gc;
	}

	
	protected void postHierarchiesPostprocess() {
		if (spec.getSubreportsCollapsing() != ReportCollapsingStrategy.NEVER)
			collapseHierarchies();
	}

	protected void collapseHierarchies() {
		timer.run("collapsing", () -> this.rootReportData = this.rootReportData.accept(new ReportHierarchiesCollapser(spec.getSubreportsCollapsing(), this.headers.getLeafColumns())));
	}
	
	protected void flatten() {
		this.reportOutput = this.rootReportData.accept(new NiReportDataOutputter(headers));
	}
	
	/**
	 * 1. fetches funding
	 * 2. fetches the other columns
	 * 3. fetches the measures 
	 */
	protected void fetchData() {
		timer.run("columns", this::fetchColumns);
		timer.run("measures", this::fetchMeasures);
		//NiUtils.failIf(this.actualColumns.isEmpty(), "columnless reports not supported");
		NiUtils.failIf(!this.actualColumns.containsAll(this.actualHierarchies), () -> String.format("not all hierarchies (%s) are also specified as columns (%s)", this.actualHierarchies.toString(), this.actualColumns.toString()));
	}
	
	protected void fetchColumns() {
		timer.run("Funding", () -> { 
			funding = selectRelevant(schema.getFundingFetcher().fetch(this));
			timer.putMetaInNode("cells", funding.size());
			});
		for(NiReportColumn<?> colToFetch:getReportColumns()) {
			timer.run(colToFetch.name, () -> {
				ColumnContents cc = fetchEntity(colToFetch);
				if (cc != null)
					fetchedColumns.put(colToFetch.name, cc);
			});
		};
	}
	
	protected ColumnContents fetchEntity(NiReportedEntity<?> colToFetch) throws Exception {
		try {
			List<? extends Cell> cells = selectRelevant(colToFetch.fetch(this));
			timer.putMetaInNode("cells", cells.size());
			return new ColumnContents(cells.stream().map(z -> new NiCell(z, colToFetch, rootEmptyTracker)).collect(Collectors.toList()));
		}
		catch(Exception e) {
			timer.putMetaInNode("error", e.getMessage());
			logger.error(e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * filters out the relevant part of the supplied input
	 * TODO: when filters are implemented, filter on all dimensions
	 * @param in
	 * @return
	 */
	protected<K extends Cell> List<K> selectRelevant(List<K> in) {
		timer.putMetaInNode("fetched_raw", in.size());
		Set<Long> ids = this.filters.getActivityIds(this);
		if (ids == null) return in;
		List<K> res = in.stream().filter(z -> ids.contains(z.activityId)).collect(Collectors.toList());
		return res;
	}
	
	/**
	 * builds the dependency graph of the measures and then fetches them in order
	 * 
	 */
	protected void fetchMeasures() {
		LinkedHashSet<NiReportMeasure<?>> measuresInSortOrder = getReportMeasures();
		for(NiReportMeasure<?> meas:measuresInSortOrder) {
			timer.run(meas.name, () -> fetchedMeasures.put(meas.name, fetchEntity(meas)));
		}
	}
	
	protected void createInitialReport() {
		//ColumnReportData fetchedData = new ColumnReportData(this);
		GroupColumn rawData = new GroupColumn(ROOT_COLUMN_NAME, null, null, null);
		
		fetchedColumns.forEach((name, contents) -> rawData.addColumn(new CellColumn(name, contents, rawData, schema.getColumns().get(name), new NiColSplitCell(PSEUDOCOLUMN_COLUMN, new ComparableValue<String>(name, name))))); // regular columns
		
		TimeRange userRequestedRange = TimeRange.forCriteria(spec.getGroupingCriteria());
		if (userRequestedRange != TimeRange.NONE)
			rawData.maybeAddColumn(buildFundingColumn(FUNDING_COLUMN_NAME, rawData, this::separateYears));
		rawData.maybeAddColumn(buildTotalsColumn(TOTALS_COLUMN_NAME, rawData));
		
		GroupColumn catData = applyPostMeasureVerticalHierarchies(rawData);
		this.headers = new NiHeaderInfo(this, catData, this.actualHierarchies.size());
		this.rootReportData = new ColumnReportData(this, null, AmpCollections.map(catData.getLeafColumns(), cc -> cc.getContents()));
	}
	
	/**
	 * this function is a semihack - it belongs somewhere in {@link NiReportsSchema}. TODO: refactor when the engine is almost done and we have a clear picture of the necessities
	 * @param fundingColumn
	 * @return
	 */
	protected GroupColumn separateYears(GroupColumn fundingColumn) {
		TimeRange userRequestedRange = TimeRange.forCriteria(spec.getGroupingCriteria());
		Map<String, Behaviour<?>> behaviours = fundingColumn.getSubColumns().stream().collect(Collectors.toMap(z -> z.name, z -> ((CellColumn)z).getBehaviour()));
		
		//List<TimeRange> categories = TimeRange.getRange(TimeRange.YEAR, userRequestedRange); // get all the ranges between year and what the report requests
		
		List<NiCell> allCells = new ArrayList<>();
		fundingColumn.forEachCell(cell -> allCells.add(cell));
		
		Column res = new CellColumn(fundingColumn.name, new ColumnContents(allCells), fundingColumn.getParent(), null, null, fundingColumn.splitCell);
		List<VSplitStrategy> splitCriterias = new ArrayList<>();
		splitCriterias.add(TimeRange.YEAR.asVSplitStrategy());
		if (userRequestedRange != TimeRange.YEAR)
			splitCriterias.add(userRequestedRange.asVSplitStrategy());
//		for(TimeRange tr:categories) {
//			splitCriterias.add(tr.asVSplitStrategy());
//		}
		
		for(VSplitStrategy splitCriteria:splitCriterias)
			res = res.verticallySplitByCategory(splitCriteria, fundingColumn.getParent());
		
		this.premeasureSplitDepth = splitCriterias.size();
		
		VSplitStrategy restoreMeasures = VSplitStrategy.build(
			cell -> new ComparableValue<String>(cell.getEntity().getName(), AmpCollections.indexOf(actualMeasures, cell.getEntity().getName())),
			cat -> behaviours.get(cat.getValue()),
			/*spec.isDisplayEmptyFundingColumns() ? null : */z -> getAsComparable(actualMeasures),
			PSEUDOCOLUMN_MEASURE);
		GroupColumn z = res.verticallySplitByCategory(restoreMeasures, fundingColumn.getParent());
		return z;
	}
	
	protected GroupColumn applyPostMeasureVerticalHierarchies(GroupColumn rawData) {
		return (GroupColumn) rawData.accept(new PostMeasureVHiersVisitor(this));
	}
	
	/**
	 * gets a set as a list of ComparableValue(elem, index-of-elem-in-set)
	 * @return
	 */
	protected<K> List<ComparableValue<K>> getAsComparable(Set<K> in) {
		List<ComparableValue<K>> res = new ArrayList<>();
		int i = 0;
		for(K item:in) {
			res.add(new ComparableValue<K>(item, i));
			i ++;
		}
		return res;
	}
	
	protected Column buildFundingColumn(String columnName, GroupColumn parentColumn, Function<GroupColumn, GroupColumn> postprocessor) {
		GroupColumn fundingColumn = new GroupColumn(columnName, null, parentColumn, null);
		fetchedMeasures.forEach((name, contents) -> {
			NiReportMeasure<?> meas = schema.getMeasures().get(name);
			if (meas.getBehaviour().getTimeRange() != TimeRange.NONE)
				fundingColumn.addColumn(new CellColumn(name, contents, fundingColumn, meas, meas.getBehaviour(), new NiColSplitCell(PSEUDOCOLUMN_MEASURE, new ComparableValue<String>(name, name))));
		});
		GroupColumn res = postprocessor.apply(fundingColumn);
		return res;
	}
	
	/**
	 * builds the initial "totals" column
	 * @param columnName
	 * @param parentColumn
	 * @return
	 */
	protected Column buildTotalsColumn(String columnName, GroupColumn parentColumn) {
		LinkedHashMap<NiReportedEntity<?>, ColumnContents> fetchedEntities = new LinkedHashMap<>();
		
		// step1: collect fetched entitities in a unified map, measures after columns
		fetchedMeasures.forEach((name, contents) -> {fetchedEntities.put(schema.getMeasures().get(name), contents);});
		fetchedColumns.forEach((name, contents) -> {fetchedEntities.put(schema.getColumns().get(name), contents);});
		
		//step2: collect/generate the totals cells
		LinkedHashMap<String, ImmutablePair<NiReportedEntity<?>, ColumnContents>> totalsColumnsContents = new LinkedHashMap<>();
		fetchedEntities.forEach((entity, contents) -> {
			ImmutablePair<String, ColumnContents> measTotals = entity.getBehaviour().getTotalCells(this, entity, contents);  //meas.getBehaviour().getCellsFor
			if (measTotals != null)
				totalsColumnsContents.computeIfAbsent(measTotals.k, z -> new ImmutablePair<>(entity, new ColumnContents(Collections.emptyMap()))).v.add(measTotals.v);
		});

		//step3: create the Ni columns based on the collected cells
		GroupColumn totalsColumn = new GroupColumn(columnName, null, parentColumn, null);
		totalsColumnsContents.forEach((name, cont) -> {
			totalsColumn.addColumn(new CellColumn(name, cont.v, totalsColumn, cont.k, cont.k.getBehaviour(), new NiColSplitCell(PSEUDOCOLUMN_MEASURE, new ComparableValue<String>(name, name))));
		});
		
		return totalsColumn;
	}
	
	protected void runComputedMeasures() {
		
	}
	
	protected void createHierarchies() {
		for(String hier: this.actualHierarchies) {
			CellColumn cel = (CellColumn) this.headers.rootColumn.findChildByName(hier);
			NiUtils.failIf(cel == null, () -> String.format("could not find fetched column used for hierarchies: %s", hier));
			if (cel != null) {
				timer.run(hier, () -> {
					this.rootReportData = this.rootReportData.horizSplit(cel);
				});
			}
		}
	}
		
	/**
	 * <strong>HAS A SIDE EFFECT</strong>: fills {@link #actualHierarchies} and {@link #actualColumns} <br />
	 * returns a list of the supported columns of the ones mandated by the report. This function will become an one-liner when NiReports will become the reference reporting engine
	 * @return
	 */
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

	/**
	 * <strong>HAS A SIDE EFFECT</strong>: fills {@link #actualMeasures} and {@link #virtualMeasures} <br />
	 * returns a list of the support measures of the ones mandated by the report. This function will become an one-liner when NiReports will become the reference reporting engine
	 * @return
	 */
	protected LinkedHashSet<NiReportMeasure<? extends Cell>> getReportMeasures() {
		Map<String, NiReportMeasure<?>> measures = schema.getMeasures();
		Set<String> specifiedMeasures = spec.getMeasureNames();
		List<NiReportMeasure<?>> supportedMeasures = new ArrayList<>();
		for(String measName:specifiedMeasures) {
			if (measures.containsKey(measName))
				supportedMeasures.add(measures.get(measName));
			else {
				addReportWarning(new ReportWarning(String.format("measure %s not supported in NiReports", measName)));
			}
		}
		Graph<NiReportMeasure<?>> measuresGraph = new Graph<>(supportedMeasures, meas -> meas.getPrecursorMeasures().stream().map(measName -> measures.get(measName)).collect(Collectors.toList()));
		LinkedHashSet<NiReportMeasure<?>> allRunMeasures = measuresGraph.sortTopologically();
		this.actualMeasures = new LinkedHashSet<>(allRunMeasures.stream().map(z -> z.getName()).collect(Collectors.toList()));
		this.virtualMeasures = new LinkedHashSet<>(actualMeasures.stream().filter(z -> specifiedMeasures.contains(z)).collect(Collectors.toList()));
		return allRunMeasures;
	}
	
	protected void addReportWarning(ReportWarning warning) {
		reportWarnings.computeIfAbsent(warning.entityId, ignored -> new TreeSet<>()).add(warning);
	}
	
	protected void printReportWarnings() {
		if (!reportWarnings.isEmpty())
			logger.error("report render warnings: " + reportWarnings.toString());
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

	/**
	 * returns the "workspace ids", potentially reduced after the per-report filters have been applied
	 * TODO: specify that after filters have been implemented
	 * @return
	 */
	public Set<Long> getMainIds() {
		return mainIds;
	}
	
	@Override
	public IdsAcceptor buildAcceptor(NiDimensionUsage dimUsage, List<Coordinate> coos) {
		DimensionSnapshot snapshot = getDimensionSnapshot(dimUsage.dimension);
		return snapshot.getCachingIdsAcceptor(coos);
	}
}
