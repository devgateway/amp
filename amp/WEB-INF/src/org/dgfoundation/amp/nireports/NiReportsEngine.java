package org.dgfoundation.amp.nireports;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.algo.AmpCollections;
import org.dgfoundation.amp.algo.Graph;
import org.dgfoundation.amp.algo.ValueWrapper;
import org.dgfoundation.amp.algo.timing.InclusiveTimer;
import org.dgfoundation.amp.algo.timing.RunNode;
import org.dgfoundation.amp.newreports.IReportEnvironment;
import org.dgfoundation.amp.newreports.ReportCollapsingStrategy;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportWarning;
import org.dgfoundation.amp.nireports.output.*;
import org.dgfoundation.amp.nireports.output.sorting.NiReportSorter;
import org.dgfoundation.amp.nireports.runtime.*;
import org.dgfoundation.amp.nireports.schema.*;
import org.dgfoundation.amp.nireports.schema.NiDimension.Coordinate;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;
import org.digijava.kernel.translator.LocalizableLabel;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * The NiReports engine API-independent entrypoint. A single report should be run per instance <br />
 * No schema-specific code below this point. <br />
 * Code can change its APIs at any point below this point - using the AMP Reports API here is entirely optional <br />
 * 
 * Because instances of this class are the root of the objects tree and are accessible throughtout the schema, its state is generously exposed as <i>public</i> fields.
 * It's a tradeoff between architectural beauty and workable simplicity. 
 * 
 * @author Dolghier Constantin
 *
 */
public class NiReportsEngine implements IdsAcceptorsBuilder {
    
    public static final Logger logger = Logger.getLogger(NiReportsEngine.class);
    
    /**
     * the name of the artificial headers root (see {@link NiHeaderInfo})
     */
    public static final String ROOT_COLUMN_NAME = "RAW";
    public static final LocalizableLabel ROOT_COLUMN_LABEL = new LocalizableLabel(ROOT_COLUMN_NAME);

    /**
     * the name of the premeasure-split measures subtree root (see {@link NiHeaderInfo})
     */
    public static final String FUNDING_COLUMN_NAME = "Funding";
    public static final LocalizableLabel FUNDING_COLUMN_LABEL = new LocalizableLabel(FUNDING_COLUMN_NAME);

    /**
     * the name of the measures- and columns- totals subtree root (see {@link NiHeaderInfo}, {@link VSplitStrategy#getTotalSubcolumnName()}). 
     * see {@link NiColSplitCell#entityType}
     */
    public static final String TOTALS_COLUMN_NAME = "Totals";
    public static final LocalizableLabel TOTALS_COLUMN_LABEL = new LocalizableLabel(TOTALS_COLUMN_NAME);

    /**
     * the type of the {@link #ROOT_COLUMN_NAME} / {@link #FUNDING_COLUMN_NAME} / year group column in the headers output
     * see {@link NiColSplitCell#entityType}  
     */
    public static final String PSEUDOCOLUMN_YEAR = "#date#year";
    
    /**
     * the type of the {@link #ROOT_COLUMN_NAME} / {@link #FUNDING_COLUMN_NAME} / {@link #PSEUDOCOLUMN_YEAR} / quarter group column in the headers output
     * see {@link NiColSplitCell#entityType}
     */ 
    public static final String PSEUDOCOLUMN_QUARTER = "#date#quarter";
    
    /**
     * the type of the {@link #ROOT_COLUMN_NAME} / {@link #FUNDING_COLUMN_NAME} / {@link #PSEUDOCOLUMN_YEAR} / month group column in the headers output
     * see {@link NiColSplitCell#entityType} 
     */     
    public static final String PSEUDOCOLUMN_MONTH = "#date#month";
    
    /**
     * the type of the header column identifying a measure
     * see {@link NiColSplitCell#entityType}
     */
    public static final String PSEUDOCOLUMN_MEASURE = "#ni#measure";

    /**
     * the type of the header column identifying a column
     * see {@link NiColSplitCell#entityType}
     */
    public static final String PSEUDOCOLUMN_COLUMN = "#ni#column";
        
    // some of the fields below are public because they are part of the "internal" API and might be used by callbacks from deep inside ComputedMeasures / etc
    
    public final NiReportsSchema schema;
    public CachingCalendarConverter calendar;
    
    NiFilters filters;
    
    public final Map<String, ColumnContents> fetchedColumns = new LinkedHashMap<>();
    public final Map<String, ColumnContents> fetchedMeasures = new LinkedHashMap<>();
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
    
    /** the filtered funding */
    public List<CategAmountCell> funding;
    
    /** the unfiltered funding, as returned by the schema */
    public List<CategAmountCell> unfilteredFunding;
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
     * the measures which should be run in the report
     * (e.g. ones requested by the spec + the ones requested by NiReportMeasure.getPrecursorMeasures[value = true])
     */
    public LinkedHashSet<String> reportRunMeasures;

    /**
     * all the hierarchies which are used in the actually-run report. Some of them might have been added virtually (like for example as a dependency of a filter). <br />
     * This is fully included in {@link #actualColumns}, else it's a bug
     */
    public LinkedHashSet<String> actualHierarchies;
            
    public LinkedHashSet<String> orderedColumns;
    
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

    private final IReportEnvironment reportEnvironment;
    
    HierarchiesTracker rootEmptyTracker = HierarchiesTracker.buildEmpty(hiersTrackerCounter);
    
    public NiReportsEngine(NiReportsSchema schema, ReportSpecification reportSpec,
            IReportEnvironment reportEnvironment) {
        this.schema = schema;
        this.spec = reportSpec;
        this.reportEnvironment = reportEnvironment;
        this.yearRangeSettingsPredicate = spec.getSettings() == null ? (z -> true) : spec.getSettings().buildYearSettingsPredicate();
    }

    public NiReportFilterResult executeFilter() {
        try (SchemaSpecificScratchpad pad = schema.generateScratchpad(this)) {
            this.schemaSpecificScratchpad = pad;
            this.timer = new InclusiveTimer("Report " + spec.getReportName());
            this.calendar = pad.buildCalendarConverter();

            timer.run("converting filters", () -> this.filters = schema.convertFilters(this));
            timer.run("fetch activity ids",
                    () -> this.mainIds = Collections.unmodifiableSet(this.filters.getActivityIds()));
            timer.run("apply column filters", this::applyFilters);

            if (filters.getCellPredicates().containsKey(FUNDING_COLUMN_NAME)) {
                timer.run("apply funding filters", this::applyFundingFilters);
            }

            logger.info(String.format("it took %d millies to filter, the breakdown is:\n%s",
                    timer.getWallclockTime(), timer.getCurrentState().asUserString(RunNode.DEFAULT_INDENT)));

            return new NiReportFilterResult(mainIds, timer.getCurrentState(), timer.getWallclockTime(), reportWarnings);
        } catch (Exception e) {
            throw AlgoUtils.translateException(e);
        }
    }

    /**
     * Filter activities by funding.
     */
    private void applyFundingFilters() throws Exception {
        fetchFunding();
        mainIds.retainAll(funding.stream().map(c -> c.activityId).collect(Collectors.toSet()));
    }

    public NiReportRunResult execute() {
        if (spec.getMeasures().isEmpty() && spec.getHierarchies().isEmpty() && spec.isSummaryReport()) {
            throw new RuntimeException("Summary reports without hierarchies and measures are not supported.");
        }
        try (SchemaSpecificScratchpad pad = schema.generateScratchpad(this)) {
            this.schemaSpecificScratchpad = pad;
            this.timer = new InclusiveTimer("Report " + spec.getReportName());
            this.calendar = pad.buildCalendarConverter();

            timer.run("converting filters", () -> this.filters = schema.convertFilters(this));
            timer.run("workspaceFilter",
                    () -> this.mainIds = Collections.unmodifiableSet(this.filters.getActivityIds()));
            timer.run("exec", this::runReportAndCleanup);
            //printReportWarnings();
            NiReportRunResult runResult = new NiReportRunResult(this.reportOutput, timer.getCurrentState(), timer.getWallclockTime(), this.headers, reportWarnings, this.calendar);
//          logger.trace("JsonBean structure of RunNode:" + timingInfo.asJsonBean());
            logger.trace(String.format("it took %d millies to generate report, the breakdown is:\n%s",
                    runResult.wallclockTime, runResult.timings.asUserString(RunNode.DEFAULT_INDENT)));
            return runResult; 
        } catch (Exception e) {
            throw AlgoUtils.translateException(e);
        }
    }
    
    /** writes statistics in the {@link InclusiveTimer} instance */
    protected void writeStatistics() {      
        Map<String, Integer> statsNode = new HashMap<>();
        statsNode.put("calls", calendar.getCalls());
        statsNode.put("noncached", calendar.getNonCachedCalls());
        statsNode.put("percent_cached", calendar.getCalls() == 0 ? 0 : 100 - (100 * calendar.getNonCachedCalls() / calendar.getCalls()));
        timer.putMetaInNode("calendar_translations", statsNode);
        
        timer.putMetaInNode("hierarchies_tracker_stats", hiersTrackerCounter.getStats());
        if (!reportWarnings.isEmpty()) {
            timer.putMetaInNode("warnings", reportWarnings);
        }
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
        timer.run("virtual_hiers", this::createVirtualHierarchies);
        timer.run("hierarchies", this::createHierarchies);
        timer.run("posthierproc", this::postHierarchiesPostprocess);
        timer.run("flatten", this::flatten);
        timer.run("output", this::output);
    }
        
    protected void addRulesIfPresent(Map<NiDimensionUsage, List<IdsAcceptor>> acceptors, LevelColumn lc, boolean positive, Set<Long> ids) {
        if (ids == null)
            return;
        acceptors.computeIfAbsent(lc.dimensionUsage, ignored -> new ArrayList<>()).add(buildAcceptor(lc.dimensionUsage, ids.stream().map(z -> new Coordinate(lc.level, z)).collect(toList())));
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
        Set<String> hiddenHierarchies = spec.getInvisibleHierarchyNames();
        Predicate<Column> isHiddenHierarchy = c -> hiddenHierarchies.contains(c.name);
        Predicate<Column> columnFilter = yearRangeSetting.or(emptyLeaf).or(isHiddenHierarchy);
        this.headers = new NiHeaderInfo(this, pruneHeaders(this.headers.rootColumn, columnFilter), headers.nrHierarchies);
        this.reportOutput = this.reportOutput.accept(new NiReportOutputCleaner(this.headers));
        if (spec.getSorters() != null && !spec.getSorters().isEmpty())
            timer.run("sorting", this::sortOutput);
    }

    protected void sortOutput() {
        this.reportOutput = this.reportOutput.accept(NiReportSorter.buildFor(this));
    }
    
    /**
     * generates a new instance with new {@link NiHeaderInfo#rasterizedHeaders} and {@link NiHeaderInfo#leafColumns}, but sharing the same {@link NiHeaderInfo#rootColumn}
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
        this.reportOutput = this.rootReportData.accept(new NiReportDataOutputter(headers, this));
    }
    
    /**
     * 1. fetches funding
     * 2. fetches the other columns
     * 3. fetches the measures 
     */
    protected void fetchData() {
        timer.run("filtersApply", this::applyFilters);
        timer.run("columns", this::fetchColumns);
        timer.run("measures", this::fetchMeasures);
        if (fundingFiltersIds()) {
            cleanColumnsAccordingToFunding();
        }
        //NiUtils.failIf(this.actualColumns.isEmpty(), "columnless reports not supported");
        NiUtils.failIf(!this.actualColumns.containsAll(this.actualHierarchies), () -> String.format("not all hierarchies (%s) are also specified as columns (%s)", this.actualHierarchies.toString(), this.actualColumns.toString()));
    }
    
    protected void applyFilters() {
        try {
            for(String mandatoryHier:filters.getFilteringColumns()) {
                if (columnSupported(mandatoryHier)) {
                    timer.run(mandatoryHier, () -> fetchedColumns.put(mandatoryHier, fetchEntity(schema.getColumns().get(mandatoryHier), false)));
                } else {
                    addReportWarning(new ReportWarning(String.format("asked to filter by unsupported column <%s>; ignoring", mandatoryHier)));
                }
            }
        }
        catch(Exception e) {
            throw AlgoUtils.translateException(e);
        }
        this.mainIds = filters.getFilteredActivityIds(); // this will use the filtered fetchedColumns
    }
    
    protected void fetchColumns() {
        for(NiReportColumn<?> colToFetch:getReportColumns().stream().filter(z -> !fetchedColumns.containsKey(z)).collect(toList())) {
            timer.run(colToFetch.name, () -> {
                ColumnContents cc = fetchEntity(colToFetch, true);
                if (cc != null)
                    fetchedColumns.put(colToFetch.name, cc);
            });
        }
        fetchFunding();
    }

    private void fetchFunding() {
        timer.run("Funding", () -> {
            unfilteredFunding = schema.fetchEntity(this, schema.getFundingFetcher(this)).stream().filter(z -> this.mainIds.contains(z.activityId)).collect(Collectors.toList());
            funding = selectRelevant(unfilteredFunding, buildCellFilterForColumn(FUNDING_COLUMN_NAME, true));
            timer.putMetaInNode("cells", funding.size());
        });
    }

    /**
     * returns true IFF in this report we should exclude non-transactions-containing activities from the output
     * @return
     */
    protected boolean fundingFiltersIds() {
        return !spec.getMeasures().isEmpty() && // not measureless
                (filters.getCellPredicates().containsKey(FUNDING_COLUMN_NAME) // there is any predicate operating on Funding
                || // or we are filtering on a transaction-level hierarchy and should hide empty rows
                (isHideEmptyFundingRowsWhenFilteringByTransactionHier() && isFilteringOnTransactionLevelHierarchy()));
    }

    private boolean isHideEmptyFundingRowsWhenFilteringByTransactionHier() {
        // hide empty rows if we have hierarchies (because filtering must be equal to hierarchies)
        return !actualHierarchies.isEmpty()
                || // or we have non-hierarchical report and spec say to hide empty rows
                !spec.isDisplayEmptyFundingRowsWhenFilteringByTransactionHierarchy();
    }

    private boolean isFilteringOnTransactionLevelHierarchy() {
        return filters.getFilteringColumns().stream().anyMatch(z -> schema.getColumns().get(z).isTransactionLevelHierarchy());
    }
    
    /**
     * removes any traces of entities containing no funding
     */
    protected void cleanColumnsAccordingToFunding() {
        //Set<Long> idsToKeep = funding.stream().map(cell -> cell.activityId).collect(Collectors.toSet());
        Set<Long> idsToKeep = new HashSet<>();
        reportRunMeasures.stream().map(z -> fetchedMeasures.get(z).data.keySet()).forEach(ids -> idsToKeep.addAll(ids));

        fetchedColumns.forEach((colName, colContents) -> {
            if (!colName.equals(FUNDING_COLUMN_NAME))
                colContents.retainIds(idsToKeep);
        });
    }
    
    /**
     * builds a predicate for filtering the cells of a given column
     * @param columnName
     * @param enable if this is false, the returns predicate will be always-true
     * @return
     */
    @SuppressWarnings("unchecked")
    protected<K extends Cell> Predicate<K> buildCellFilterForColumn(String columnName, boolean enable) {
        Predicate<K> extraCheck = (Predicate<K>) Optional.ofNullable(enable ? filters.getCellPredicates().get(columnName) : null).orElse(z -> true);
        return extraCheck;
    }
    
    protected<K extends Cell> ColumnContents fetchEntity(NiReportedEntity<K> colToFetch, boolean applyFilterPercentages) throws Exception {
        try {
            List<K> rawCells = schema.fetchEntity(this, colToFetch);
            List<K> cells = (colToFetch instanceof NiReportColumn) ? selectRelevant(rawCells, buildCellFilterForColumn(colToFetch.name, true)) : rawCells;
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
    protected<K extends Cell> List<K> selectRelevant(List<K> in, Predicate<K> extraCheck) {
        timer.putMetaInNode("fetched_raw", in.size());
        Set<Long> ids = this.mainIds;
        if (ids == null) return in;
//      Predicate<K> cellFilter = z -> ids.contains(z.activityId);
//      if (alsoFilter)
//          cellFilter = cellFilter.and(this::cellMatchesFilters);
        List<K> res = new ArrayList<>();
        for(K cell:in) {
            if (ids.contains(cell.activityId) && extraCheck.test(cell) && this.cellMatchesFilters(cell))
                res.add(cell);
        }
        return res;
    }
    
    protected<K extends Cell> boolean cellMatchesFilters(K cell) {
        for(Map.Entry<NiDimensionUsage, Predicate<Coordinate>> filterItem:filters.getProcessedFilters().entrySet()) {
            Coordinate cellCoo = cell.getCoordinates().get(filterItem.getKey());
            if (cellCoo == null) 
                continue; // cell is indifferent to this dimension
            if (!filterItem.getValue().test(cellCoo)) {
                //TODO: treat nulls differently as part of the (undefined / unexistant) filtering API
                return false;
            }
        }
        return true;
    }
    
    /**
     * builds the dependency graph of the measures and then fetches them in order
     * 
     */
    protected void fetchMeasures() {
        LinkedHashSet<NiReportMeasure<?>> measuresInSortOrder = getReportMeasures();
        for(NiReportMeasure<?> meas:measuresInSortOrder) {
            timer.run(meas.name, () -> fetchedMeasures.put(meas.name, fetchEntity(meas, true)));
        }
    }
    
    protected void createInitialReport() {
        //ColumnReportData fetchedData = new ColumnReportData(this);
        GroupColumn rawData = new GroupColumn(ROOT_COLUMN_NAME, ROOT_COLUMN_LABEL, null, null, null);
        
        for(String name:orderedColumns) {
            ColumnContents contents = fetchedColumns.get(name);
            NiReportColumn<? extends Cell> column = schema.getColumns().get(name);
            rawData.addColumn(new CellColumn(name, column.label, contents, rawData, column, new NiColSplitCell(PSEUDOCOLUMN_COLUMN, new ComparableValue<String>(name, name)))); // regular columns
        }
        
        TimeRange userRequestedRange = TimeRange.forCriteria(spec.getGroupingCriteria());
        if (userRequestedRange != TimeRange.NONE)
            rawData.maybeAddColumn(buildFundingColumn(FUNDING_COLUMN_NAME, FUNDING_COLUMN_LABEL, rawData, this::separateYears));
        rawData.maybeAddColumn(buildTotalsColumn(TOTALS_COLUMN_NAME, TOTALS_COLUMN_LABEL, rawData));
        
        GroupColumn catData = applyPostMeasureVerticalHierarchies(rawData);
        this.headers = new NiHeaderInfo(this, catData, this.actualHierarchies.size());
        this.rootReportData = new ColumnReportData(this, null, AmpCollections.map(catData.getLeafColumns(), cc -> cc.getContents())); //mark here: this.reportWarnings
    }
    
    /**
     * runs and then collapses the virtual hierarchies mandated by filtering
     */
    protected void createVirtualHierarchies() {
        ValueWrapper<ColumnReportData> root = new ValueWrapper<>((ColumnReportData) this.rootReportData);
        for(String vhier:filters.getMandatoryHiers())
            if (!this.actualHierarchies.contains(vhier)) 
                timer.run(vhier, () -> {
                    NiReportColumn<?> col = schema.getColumns().get(vhier);
                    GroupReportData grd = root.value.horizSplit(fetchedColumns.get(vhier), fetchedColumns.get(vhier), col.getBehaviour(), col, true);
                    if (grd.getSubReports().isEmpty())
                        root.value.getContents().values().forEach(cc -> cc.data.clear());
                    else
                        root.value = new ReportHierarchiesCollapser(ReportCollapsingStrategy.ALWAYS, this.headers.getLeafColumns()).collapseCRDs(AmpCollections.relist(grd.getSubReports(), z -> (ColumnReportData) z));
                });
        this.rootReportData = ((ColumnReportData) this.rootReportData).replaceContents(root.value.getContents());
    }
    
    /**
     * splits the funding part by time dimension, depending on the spec (years, quarters, months)
     * @param fundingColumn
     * @return
     */
    protected GroupColumn separateYears(GroupColumn fundingColumn) {
        TimeRange userRequestedRange = TimeRange.forCriteria(spec.getGroupingCriteria());
        Map<String, Behaviour<?>> behaviours = fundingColumn.getSubColumns().stream().collect(Collectors.toMap(z -> z.name, z -> ((CellColumn)z).getBehaviour()));
        
        //List<TimeRange> categories = TimeRange.getRange(TimeRange.YEAR, userRequestedRange); // get all the ranges between year and what the report requests
        
        List<NiCell> allCells = new ArrayList<>();
        fundingColumn.forEachCell(cell -> allCells.add(cell));
        
        Column res = new CellColumn(fundingColumn.name, fundingColumn.label, new ColumnContents(allCells), fundingColumn.getParent(), null, null, fundingColumn.splitCell);
        List<VSplitStrategy> splitCriterias = new ArrayList<>();
        splitCriterias.add(TimeRange.YEAR.asVSplitStrategy(this));
        if (userRequestedRange != TimeRange.YEAR)
            splitCriterias.add(userRequestedRange.asVSplitStrategy(this));
//      for(TimeRange tr:categories) {
//          splitCriterias.add(tr.asVSplitStrategy());
//      }
        
        for(VSplitStrategy splitCriteria:splitCriterias)
            res = res.verticallySplitByCategory(splitCriteria, fundingColumn.getParent());
        
        this.premeasureSplitDepth = splitCriterias.size();
        List<String> relevantMeasures = reportRunMeasures.stream().filter(zz -> behaviours.containsKey(zz) && behaviours.get(zz).getTimeRange() != TimeRange.NONE).collect(toList());
        
        VSplitStrategy restoreMeasures = VSplitStrategy.build(
            cell -> new ComparableValue<String>(cell.getEntity().getName(), AmpCollections.indexOf(relevantMeasures, cell.getEntity().getName())),
            cat -> behaviours.get(cat.getValue()),
            /*spec.isDisplayEmptyFundingColumns() ? null : */z -> getAsComparable(relevantMeasures),
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
    protected<K> List<ComparableValue<K>> getAsComparable(Collection<K> in) {
        List<ComparableValue<K>> res = new ArrayList<>();
        int i = 0;
        for(K item:in) {
            res.add(new ComparableValue<K>(item, i));
            i ++;
        }
        return res;
    }
    
    protected Column buildFundingColumn(String columnName, LocalizableLabel columnLabel, GroupColumn parentColumn, Function<GroupColumn, GroupColumn> postprocessor) {
        GroupColumn fundingColumn = new GroupColumn(columnName, columnLabel, null, parentColumn, null);
        reportRunMeasures.forEach(name -> {
            NiReportMeasure<?> meas = schema.getMeasures().get(name);
            if (meas.getBehaviour().getTimeRange() != TimeRange.NONE)
                fundingColumn.addColumn(new CellColumn(name, meas.label, fetchedMeasures.get(name), fundingColumn, meas, meas.getBehaviour(), new NiColSplitCell(PSEUDOCOLUMN_MEASURE, new ComparableValue<String>(name, name))));
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
    protected Column buildTotalsColumn(String columnName, LocalizableLabel columnLabel, GroupColumn parentColumn) {
        LinkedHashMap<NiReportedEntity<?>, ColumnContents> fetchedEntities = new LinkedHashMap<>();
        
        // step1: collect fetched entities in a unified map, measures after columns
        reportRunMeasures.forEach(name -> fetchedEntities.put(schema.getMeasures().get(name), fetchedMeasures.get(name)));
        orderedColumns.forEach(name -> fetchedEntities.put(schema.getColumns().get(name), fetchedColumns.get(name)));
        
        //step2: collect/generate the totals cells
        LinkedHashMap<String, ImmutablePair<NiReportedEntity<?>, ColumnContents>> totalsColumnsContents = new LinkedHashMap<>();
        fetchedEntities.forEach((entity, contents) -> {
            ImmutablePair<String, ColumnContents> measTotals = entity.getBehaviour().getTotalCells(this, entity, contents);  //meas.getBehaviour().getCellsFor
            if (measTotals != null)
                totalsColumnsContents.computeIfAbsent(measTotals.k, z -> new ImmutablePair<>(entity, new ColumnContents(Collections.emptyMap()))).v.add(measTotals.v);
        });

        //step3: create the Ni columns based on the collected cells
        GroupColumn totalsColumn = new GroupColumn(columnName, columnLabel, null, parentColumn, null, true);
        totalsColumnsContents.forEach((name, cont) -> {
            totalsColumn.addColumn(new CellColumn(name, new LocalizableLabel(name), cont.v, totalsColumn, cont.k, cont.k.getBehaviour(), new NiColSplitCell(PSEUDOCOLUMN_MEASURE, new ComparableValue<String>(name, name))));
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
        
    protected boolean columnSupported(String columnName) {
        return schema.getColumns().containsKey(columnName);
    }
    
    /**
     * <strong>HAS A SIDE EFFECT</strong>: fills {@link #actualHierarchies} and {@link #actualColumns} <br />
     * returns a list of the supported columns of the ones mandated by the report. This function will become an one-liner when NiReports will become the reference reporting engine
     * @return
     */
    protected List<NiReportColumn<? extends Cell>> getReportColumns() {
        List<NiReportColumn<? extends Cell>> res = new ArrayList<>();
        this.actualHierarchies = new LinkedHashSet<>(spec.getHierarchyNames()); //AmpCollections.prefixUnion(filters.getMandatoryHierarchies(), spec.getHierarchyNames());
        this.actualHierarchies.addAll(spec.getInvisibleHierarchyNames());
        this.actualColumns = new LinkedHashSet<>(spec.getColumnNames()); //AmpCollections.prefixUnion(filters.getMandatoryHierarchies(), spec.getColumnNames());
        this.orderedColumns = new LinkedHashSet<>();
        //this.virtualHierarchies = AmpCollections.difference(filters.getMandatoryHierarchies(), spec.getHierarchyNames());
        for(String columnName:AmpCollections.union(this.actualHierarchies, this.actualColumns)) {
            NiReportColumn<? extends Cell> col = schema.getColumns().get(columnName);
            if (col == null) {
                addReportWarning(new ReportWarning(String.format("column \"%s\" not supported in NiReports", columnName)));
                this.actualHierarchies.remove(columnName);
                this.actualColumns.remove(columnName);
            } else if (spec.isSummaryReport() && (!actualHierarchies.contains(columnName)) && (!col.getKeptInSummaryReports())) {
                this.actualColumns.remove(columnName);
            } else {
                res.add(col);
                this.orderedColumns.add(columnName);
            }
        }
        return res;
    }

    /**
     * <strong>HAS A SIDE EFFECT</strong>: fills {@link #actualMeasures} and {@link #reportRunMeasures} <br />
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
                addReportWarning(new ReportWarning(String.format("measure \"%s\" not supported in NiReports", measName)));
            }
        }
        Graph<NiReportMeasure<?>> measuresGraph = new Graph<>(supportedMeasures, meas -> AmpCollections.relist(meas.getPrecursorMeasures().keySet(), measName -> measures.get(measName)));
        LinkedHashSet<NiReportMeasure<?>> allRunMeasures = measuresGraph.sortTopologically();
        
        Set<String> measuresToRun = new HashSet<>(spec.getMeasureNames());
        allRunMeasures.forEach(meas -> {
            meas.getPrecursorMeasures().forEach((depMeas, toRun) -> {
                if (toRun)
                    measuresToRun.add(depMeas);
            });
        });
        this.actualMeasures = new LinkedHashSet<>(AmpCollections.relist(allRunMeasures, NiReportMeasure::getName));
        this.reportRunMeasures = new LinkedHashSet<>(actualMeasures.stream().filter(measuresToRun::contains).collect(toList()));
        return allRunMeasures;
    }
    
    public void addReportWarning(ReportWarning warning) {
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

    public IReportEnvironment getReportEnvironment() {
        return reportEnvironment;
    }
}
