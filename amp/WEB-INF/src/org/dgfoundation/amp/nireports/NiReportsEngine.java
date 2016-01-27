package org.dgfoundation.amp.nireports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.algo.AmpCollections;
import org.dgfoundation.amp.algo.Graph;
import org.dgfoundation.amp.algo.VivificatingMap;
import org.dgfoundation.amp.algo.timing.InclusiveTimer;
import org.dgfoundation.amp.algo.timing.RunNode;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportWarning;
import org.dgfoundation.amp.nireports.output.NiColumnReportData;
import org.dgfoundation.amp.nireports.output.NiGroupReportData;
import org.dgfoundation.amp.nireports.output.NiReportData;
import org.dgfoundation.amp.nireports.runtime.CachingCalendarConverter;
import org.dgfoundation.amp.nireports.runtime.CellColumn;
import org.dgfoundation.amp.nireports.runtime.Column;
import org.dgfoundation.amp.nireports.runtime.ColumnContents;
import org.dgfoundation.amp.nireports.runtime.ColumnReportData;
import org.dgfoundation.amp.nireports.runtime.GroupColumn;
import org.dgfoundation.amp.nireports.runtime.GroupReportData;
import org.dgfoundation.amp.nireports.runtime.HierarchiesTracker;
import org.dgfoundation.amp.nireports.runtime.PerItemHierarchiesTracker;
import org.dgfoundation.amp.nireports.runtime.IdsAcceptorsBuilder;
import org.dgfoundation.amp.nireports.runtime.NiCell;
import org.dgfoundation.amp.nireports.runtime.ReportData;
import org.dgfoundation.amp.nireports.runtime.ReportDataVisitor;
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
	public static final String FUNDING_COLUMN_NAME = "Funding";
	public static final String TOTALS_COLUMN_NAME = "Totals";
	
	// some of the fields below are public because they are part of the "internal" API and might be used by callbacks from deep inside ComputedMeasures / etc
	
	public final NiReportsSchema schema;
	public CachingCalendarConverter calendar;
	
	final NiFilters filters;
	
	final Map<String, ColumnContents> fetchedColumns = new LinkedHashMap<>();
	final Map<String, ColumnContents> fetchedMeasures = new LinkedHashMap<>();
	final VivificatingMap<Long, Set<ReportWarning>> reportWarnings = new VivificatingMap<>(new HashMap<>(), () -> new HashSet<ReportWarning>());
	
	/**
	 * only available once {@link #createInitialReport()} has exited
	 */
	public NiHeaderInfo headers;
	ReportData rootReportData;
	NiReportData reportOutput;
	
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
	 
	public NiReportData execute() {
		try(SchemaSpecificScratchpad pad = schema.getScratchpadSupplier().apply(this)) {
			this.schemaSpecificScratchpad = pad;
			this.calendar = new CachingCalendarConverter(this.spec.getSettings() != null && this.spec.getSettings().getCalendar() != null ? this.spec.getSettings().getCalendar() : pad.getDefaultCalendar());
			
			this.timer = new InclusiveTimer("Report " + spec.getReportName());
			timer.run("exec", this::runReportAndCleanup);
			RunNode timingInfo = timer.getCurrentState();
			printReportWarnings();
			logger.warn("JsonBean structure of RunNode:" + timingInfo.asJsonBean());
			
			this.reportOutput = this.rootReportData.accept(new ReportDataOutputter());
			return this.reportOutput;
		}
		catch(Exception e) {
			throw AlgoUtils.translateException(e);
		}
	}
	
	/**
	 * TODO: refactor, move to separate file in *.outputs
	 * @author Dolghier Constantin
	 *
	 */
	class ReportDataOutputter implements ReportDataVisitor<NiReportData> {
		
		@Override
		public NiReportData visitLeaf(ColumnReportData crd) {
			Map<CellColumn, Map<Long, Cell>> contents = AmpCollections.remap(crd.getContents(), (cellColumn, columnContents) -> columnContents.flatten(crd.hierarchies, cellColumn.getBehaviour()), null);
			return new NiColumnReportData(contents, crd.trailCells, crd.splitter);
		}

		@Override
		public NiReportData visitGroup(GroupReportData grd, List<NiReportData> visitedChildren) {
			return new NiGroupReportData(visitedChildren, grd.trailCells, grd.splitter);
		}
		
	}
	
	/** writes statistics in the {@link InclusiveTimer} instance */
	protected void writeStatistics() {
		timer.putMetaInNode("calendar_translations", new HashMap<String, Integer>(){{
			put("calls", calendar.getCalls());
			put("noncached", calendar.getNonCachedCalls());
			put("percent_cached", calendar.getCalls() == 0? 0 : 100 - (100 * calendar.getNonCachedCalls() / calendar.getCalls()));
		}});
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
			funding = schema.getFundingFetcher().fetch(this);
			timer.putMetaInNode("cells", funding.size());
			});
		for(NiReportColumn<?> colToFetch:getReportColumns()) {
			timer.run(colToFetch.name, () -> fetchedColumns.put(colToFetch.name, fetchEntity(colToFetch)));
		};
	}
	
	protected ColumnContents fetchEntity(NiReportedEntity<?> colToFetch) throws Exception {
		List<? extends Cell> cells = colToFetch.fetch(this);
		timer.putMetaInNode("cells", cells.size());
		return new ColumnContents(cells.stream().map(z -> new NiCell(z, colToFetch)).collect(Collectors.toList()));
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
		GroupColumn rawData = new GroupColumn("RAW", null, null);
		
		fetchedColumns.forEach((name, contents) -> rawData.addColumn(new CellColumn(name, contents, rawData, schema.getColumns().get(name).getBehaviour()))); // regular columns
		
		rawData.maybeAddColumn(buildFundingColumn(FUNDING_COLUMN_NAME, rawData, this::separateYears));
		rawData.maybeAddColumn(buildFundingColumn(TOTALS_COLUMN_NAME, rawData, Function.identity()));
		
		GroupColumn catData = categorizeData(rawData);
		this.headers = new NiHeaderInfo(catData);
		this.rootReportData = new ColumnReportData(this, null, discoverLeaves(catData), HierarchiesTracker.EMPTY);
	}

	protected Map<CellColumn, ColumnContents> discoverLeaves(GroupColumn gc) {
		Map<CellColumn, ColumnContents> res = new HashMap<>();
		for(CellColumn cc:gc.getLeafColumns())
			res.put(cc, cc.getContents());
		return res;
	}
	
	/**
	 * this function is a semihack - it belongs somewhere in {@link NiReportsSchema}. TODO: refactor when the engine is almost done and we have a clear picture of the necessities
	 * @param fundingColumn
	 * @return
	 */
	protected GroupColumn separateYears(GroupColumn fundingColumn) {
		TimeRange userRequestedRange = TimeRange.forCriteria(spec.getGroupingCriteria());
		Map<String, Behaviour> behaviours = fundingColumn.getSubColumns().stream().collect(Collectors.toMap(z -> z.name, z -> ((CellColumn)z).getBehaviour()));
		
		List<TimeRange> categories = TimeRange.getRange(TimeRange.YEAR, userRequestedRange); // get all the ranges between year and what the report requests
		
		List<NiCell> allCells = new ArrayList<>();
		fundingColumn.forEachCell(cell -> allCells.add(cell));
		
		Column res = new CellColumn(fundingColumn.name, new ColumnContents(allCells), fundingColumn.getParent(), null);
		List<VSplitStrategy> splitCriterias = new ArrayList<>();
		for(TimeRange tr:categories) {
			VSplitStrategy func = cell -> tr.getDateComponentCategorizer().apply((DatedCell) cell.getCell());
			splitCriterias.add(func);
		}
		
		for(VSplitStrategy splitCriteria:splitCriterias)
			res = res.verticallySplitByCategory(splitCriteria);
		
		VSplitStrategy restoreMeasures = VSplitStrategy.build(
			cell -> new ComparableValue<String>(cell.getEntity().getName(), AmpCollections.indexOf(actualMeasures, cell.getEntity().getName())),
			cat -> behaviours.get(cat.getValue()));
		GroupColumn z = res.verticallySplitByCategory(restoreMeasures);
		return z;
	}
	
	protected Column buildFundingColumn(String columnName, GroupColumn parentColumn, Function<GroupColumn, GroupColumn> postprocessor) {
		GroupColumn fundingColumn = new GroupColumn(columnName, null, parentColumn);  // yearly funding
		fetchedMeasures.forEach((name, contents) -> {
			NiReportMeasure<?> meas = schema.getMeasures().get(name);
			if (meas.getBehaviour().getTimeRange() != TimeRange.NONE)
				fundingColumn.addColumn(new CellColumn(name, contents, fundingColumn, meas.getBehaviour()));
		});
		GroupColumn res = postprocessor.apply(fundingColumn);
		return res;
	}
	
	protected GroupColumn categorizeData(GroupColumn fetchedData) {
//		TimeRange userRequestedRange = TimeRange.forCriteria(spec.getGroupingCriteria());
		//fetchedData.contents.getSubColumns())
		return fetchedData;
	}
	
	protected void runComputedMeasures() {
		
	}
	
	protected void createHierarchies() {
		for(String hier: this.actualHierarchies) {
			CellColumn cel = (CellColumn) this.headers.rootColumn.findChildByName(hier);
			NiUtils.failIf(cel == null, () -> String.format("could not find fetched column used for hierarchies: %s", hier));
			if (cel != null) {
				timer.run(hier, () -> {
//					Behaviour beh = schema.getColumns().get(hier).getBehaviour();
					this.rootReportData = this.rootReportData.horizSplit(cel);
				});
			}
		}
	}
	
	protected void createTotals() {
		
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

	@Override
	public IdsAcceptor buildAcceptor(NiDimensionUsage dimUsage, Coordinate coos) {
		DimensionSnapshot snapshot = getDimensionSnapshot(dimUsage.dimension);
		return snapshot.getCachingIdsAcceptor(coos);
	}
}
