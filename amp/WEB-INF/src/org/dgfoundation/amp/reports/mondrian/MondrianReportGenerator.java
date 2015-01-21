/**
 * 
 */
package org.dgfoundation.amp.reports.mondrian;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.algo.ValueWrapper;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.mondrian.MondrianETL;
import org.dgfoundation.amp.newreports.AmountCell;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.FilterRule.FilterType;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportElement;
import org.dgfoundation.amp.newreports.ReportElement.ElementType;
import org.dgfoundation.amp.newreports.ReportEnvironment;
import org.dgfoundation.amp.newreports.ReportExecutor;
import org.dgfoundation.amp.newreports.ReportFilters;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSettings;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.newreports.SortingInfo;
import org.dgfoundation.amp.newreports.TextCell;
import org.dgfoundation.amp.reports.PartialReportArea;
import org.digijava.kernel.ampapi.exception.AmpApiException;
import org.digijava.kernel.ampapi.mondrian.queries.MDXGenerator;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXAttribute;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXConfig;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXElement;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXFilter;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXMeasure;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXTuple;
import org.digijava.kernel.ampapi.mondrian.util.AmpMondrianSchemaProcessor;
import org.digijava.kernel.ampapi.mondrian.util.Connection;
import org.digijava.kernel.ampapi.mondrian.util.MoConstants;
import org.digijava.kernel.ampapi.mondrian.util.MondrianMapping;
import org.digijava.kernel.ampapi.mondrian.util.MondrianUtils;
import org.digijava.kernel.ampapi.saiku.SaikuGeneratedReport;
import org.digijava.kernel.ampapi.saiku.SaikuReportArea;
import org.digijava.kernel.ampapi.saiku.SaikuReportSorter;
import org.digijava.kernel.ampapi.saiku.util.CellDataSetToAmpHierachies;
import org.digijava.kernel.ampapi.saiku.util.CellDataSetToGeneratedReport;
import org.digijava.kernel.ampapi.saiku.util.SaikuPrintUtils;
import org.digijava.kernel.ampapi.saiku.util.SaikuUtils;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.calendar.util.CalendarUtil;
import org.olap4j.Axis;
import org.olap4j.Cell;
import org.olap4j.CellSet;
import org.olap4j.CellSetAxis;
import org.olap4j.OlapException;
import org.olap4j.Position;
import org.olap4j.metadata.Member;
import org.olap4j.query.SortOrder;
import org.saiku.olap.dto.resultset.AbstractBaseCell;
import org.saiku.olap.dto.resultset.CellDataSet;
import org.saiku.olap.util.OlapResultSetUtil;
import org.saiku.service.olap.totals.TotalNode;
import org.saiku.service.olap.totals.aggregators.TotalAggregator;

/**
 * Generates a report via Mondrian
 * @author Nadejda Mandrescu
 *
 */
public class MondrianReportGenerator implements ReportExecutor {
	protected static final Logger logger = Logger.getLogger(MondrianReportGenerator.class);
	
	//TODO: set to false
	//e.g. skips to throw exceptions until schema def is complete and all mappings are configured based on it
	private static final boolean IS_DEV = true;
	private final Class<? extends ReportAreaImpl> reportAreaType;
	private final boolean printMode;
	
	/*
	// needed for AMP-18330 workaround
	private static final ReportMeasure ALWAYS_PRESENT = new ReportMeasure(MeasureConstants.ALWAYS_PRESENT);
	private SortedSet<Integer> dummyColumnsToRemove = new TreeSet<Integer>();
	*/
	
	private MDXGenerator generator = null;
	
	private List<ReportOutputColumn> leafHeaders = null; //leaf report columns list
	// stores INTERNAL_USE_ID for each row from the CellDataSet, if this feature is required 
	private List<Integer> cellDataSetActivities = null;
	
	private final ReportEnvironment environment;
	
	/**
	 * Mondrian Report Generator
	 * @param reportAreaType - report area type to be used for output generation.
	 * @param printMode - if set to true, then Olap4J CellSet will be printed to the standard output
	 */
	public MondrianReportGenerator(Class<? extends ReportAreaImpl> reportAreaType, ReportEnvironment environment, boolean printMode) {
		this.reportAreaType = reportAreaType;
		this.environment = environment;
		this.printMode = printMode; 
	}
	
	/**
	 * Mondrian Report Generator
	 * @param reportAreaType - report area type to be used for output generation.
	 */
	public MondrianReportGenerator(Class<? extends ReportAreaImpl> reportAreaType, ReportEnvironment environment) {
		this (reportAreaType, environment, false);
	}
	
	/**
	 * 1. checks that every column specified in "hierarchies" is also present in "columns"
	 * 2. brings the columns specified as hierarchies to front
	 * 
	 * Somehow hacky, but faster to code than redoing the whole rest of the code to support input in any order. Sorry, Nadia :=)  
	 * 
	 * Nadia's note: I'm glad that you made up your mind to not change the rest of the code :P
	 */
	protected void reorderColumnsByHierarchies(ReportSpecification spec) {
		if (spec.getHierarchies() == null) return; // nothing to do
		LinkedHashSet<ReportColumn> newCols = new LinkedHashSet<>();
		for(ReportColumn hier:spec.getHierarchies()) {
			if (!spec.getColumns().contains(hier))
				throw new RuntimeException("column specified as hierarchy, but not as column: " + hier);
			newCols.add(hier);
		}
		for(ReportColumn col:spec.getColumns()) {
			if (!newCols.contains(col))
				newCols.add(col);
		}
		spec.getColumns().clear();
		spec.getColumns().addAll(newCols);
		
		/**
		 * ugly workaround for AMP-18558 when the report has no hierarchies - a saiku bug which is easier to workaround than fix
		 */
		if (spec.getColumns().isEmpty() && spec.getHierarchies().isEmpty()) {
			ReportColumn constantDummyColumn = new ReportColumn(ColumnConstants.CONSTANT);
			spec.getColumns().add(constantDummyColumn);
			spec.getHierarchies().add(constantDummyColumn);
			((ReportSpecificationImpl) spec).setCalculateRowTotals(false);
			((ReportSpecificationImpl) spec).setCalculateColumnTotals(true);
		}
	}
	
	@Override
	public GeneratedReport executeReport(ReportSpecification spec) throws AMPException {
		try {
			reorderColumnsByHierarchies(spec);
			CellDataSetToGeneratedReport.counts.clear();
			stats = new ReportGenerationStats();
			CellDataSet cellDataSet = generateReportAsSaikuCellDataSet(spec);
			long postprocStart = System.currentTimeMillis();
			
			logger.info("[" + spec.getReportName() + "]" +  "Converting CellDataSet to GeneratedReport...");
			GeneratedReport report = toGeneratedReport(spec, cellDataSet, cellDataSet.runtime);
			logger.info("[" + spec.getReportName() + "]" +  "CellDataSet converted to GeneratedReport.");
			logger.info("[" + spec.getReportName() + "]" +  "Sorting report...");
			if (SaikuReportArea.class.isAssignableFrom(reportAreaType)) {
				report = new SaikuGeneratedReport(
						spec, report.generationTime, report.requestingUser,
						(SaikuReportArea)report.reportContents, cellDataSet, report.rootHeaders, report.leafHeaders, environment);
				SaikuReportSorter.sort(report, environment);
				if (printMode)
					SaikuPrintUtils.print(cellDataSet, spec.getReportName() + "_POST_SORT");
			} else 
				MondrianReportSorter.sort(report, environment);
			logger.info("[" + spec.getReportName() + "]" +  "Report sorted.");
			stats.postproc_time = System.currentTimeMillis() - postprocStart;
			stats.total_time += stats.postproc_time;
			return report;
		}
		catch(Exception e) {
			stats.crashed = true;
			e.printStackTrace();
			throw e;
		}
		finally {
			writeStats();
			tearDown();
		}
	}
	
	void writeStats() {
		if (stats != null) {
			PersistenceManager.getSession().createSQLQuery(
					String.format("INSERT INTO amp_reports_runtime_log (lock_wait_time, mdx_time, total_time, mdx_query, width, height, postproc_time, crashed) VALUES (%d, %d, %d, %s, %d, %d, %d, %s)",
							stats.lock_wait_time, stats.mdx_time, stats.total_time, SQLUtils.stringifyObject(stats.mdx_query),
							stats.width, 
							stats.height,
							stats.postproc_time,
							stats.crashed
							)).executeUpdate();
		}
	}
	
	ReportGenerationStats stats;
	
	/**
	 * Generates a report as a Saiku {@link CellDataSet}, without any translation into our Reports API structures
	 * also sets {@link #stats} to a newly-created instance
	 * @param spec - {@link ReportSpecification}
	 * @return {@link CellDataSet}
	 * @throws AMPException
	 */
	private CellDataSet generateReportAsSaikuCellDataSet(final ReportSpecification spec) throws AMPException {
		//reorderColumnsByHierarchies(spec);
		init(spec);
		addDummyHierarchy(spec);
		reorderColumnsByHierarchies(spec);
		AmpMondrianSchemaProcessor.registerReport(spec, environment);
		CellDataSet cellDataSet = null;
		ValueWrapper<Boolean> forcedOut = new ValueWrapper<Boolean>(false);
		stats.lock_wait_time = MondrianETL.FULL_ETL_LOCK.readLockWithTimeout(7000, forcedOut);

		try {
			long startTime = System.currentTimeMillis();
			//while (System.currentTimeMillis() < startTime + 15000) {};
			//try {Thread.sleep(60000);}catch(Exception e){}
			CellSet cellSet = null;
			String mdxQuery = getMDXQuery(spec);
			stats.mdx_query = mdxQuery;

			if (printMode) System.out.println("[" + spec.getReportName() + "] MDX query: " + mdxQuery);
		
			try {
				cellSet = generator.runQuery(mdxQuery);
			} catch (Exception e) {
				tearDown();
				stats.crashed = true;
				throw new AMPException("Cannot generate Mondrian Report '" + spec.getReportName() +"' : " 
						+ e.getMessage() == null ? e.getClass().getName() : e.getMessage(), e);
			}
		
			stats.mdx_time = System.currentTimeMillis() - startTime;
			if (printMode)
				System.out.println("[" + spec.getReportName() + "] MDX query run time: " + stats.mdx_time);
			else
				logger.info("[" + spec.getReportName() + "] MDX query run time: " + stats.mdx_time);
		
			try {
				cellDataSet = postProcess(spec, cellSet);
				stats.total_time = System.currentTimeMillis() - startTime;
				
				cellDataSet.setRuntime((int) stats.total_time);
				logger.info("CellDataSet for '" + spec.getReportName() + "' report generated within: " + stats.total_time + "ms");
						
				stats.width = cellDataSet == null ? 0 : cellDataSet.getWidth();
				stats.height = cellDataSet == null ? 0 : cellDataSet.getHeight();
			} catch (Exception e) {
				stats.crashed = true;
				throw new AMPException("Cannot generate Mondrian Report '" + spec.getReportName() +"' : " 
						+ e.getMessage() == null ? e.getClass().getName() : e.getMessage(), e);
			} finally {
				if (printMode) {
					if (cellSet != null) // THIS CODE PRINTS DATA BEFORE postprocessing, very useful to see raw Mondrian output to compare against postprocessed data for debug
						MondrianUtils.print(cellSet, spec.getReportName());
//					if (cellDataSet != null) //THIS THING SOMETIMES CRASHES, is not up to dateT
//						SaikuPrintUtils.print(cellDataSet, spec.getReportName() + "_POST");
				}
			}
		}
		finally {
			MondrianETL.FULL_ETL_LOCK.unlockIfStillUsed(forcedOut);
		}
		return cellDataSet;
	}
	
	private void init(ReportSpecification spec) {
		if (!Connection.IS_TESTING)
			if(MondrianETL.runETL(false).cacheInvalidated) {
				MondrianReportUtils.flushCache();
			}
		
		MondrianReportUtils.configureDefaults(spec);
		
		if (PartialReportArea.class.isAssignableFrom(reportAreaType)
				/* if there are no leaf entries to be associated with internal use id, 
				 * then we do not need to collect them
				 */
				&& spec.getHierarchies().size() < spec.getColumns().size()) {
			cellDataSetActivities = new ArrayList<Integer>();
		}
	}	
	
	/**
	 * Generates MDX Query string that can be passed to Saiku or any other MDX processor
	 * @param spec - {@link ReportSpecification}
	 * @return mdx string
	 * @throws AMPException
	 */
	public String getMDXQuery(ReportSpecification spec) throws AMPException {
		MDXConfig config = toMDXConfig(spec);
		try {
			generator = new MDXGenerator();
			return generator.getAdvancedOlapQuery(config);
		} catch (AmpApiException e) {
			tearDown();
			throw new RuntimeException("Cannot generate Mondrian Report: ", e);
		} 
	}
	
	/**
	 * Releases the resources
	 */
	public void tearDown() {
		if (generator != null) 
			generator.tearDown();
	}
	
	/**
	 *  Adds a dummy hierarchy by internal id (which is entity id) to group by non-hierarchical columns,
	 *  but only if there are non-hierarchical columns
	 */
	private void addDummyHierarchy(ReportSpecification spec) {
		//if we have more columns than hierarchies, then add the dummy hierarchy to group non-hierarchical columns by it
		if (spec.getHierarchies().size() < spec.getColumns().size()) {
			ReportColumn internalId = new ReportColumn(ColumnConstants.INTERNAL_USE_ID);
			spec.getColumns().add(internalId);
			spec.getHierarchies().add(internalId);
		}
	}
	
	protected MDXConfig toMDXConfig(ReportSpecification spec) throws AMPException {
		MDXConfig config = new MDXConfig();
		config.setCubeName(MoConstants.DEFAULT_CUBE_NAME);
		config.setMdxName(spec.getReportName());
		boolean doHierarchiesTotals = false;//we are moving totals calculation out of MDX. spec.getHierarchies() != null && spec.getHierarchies().size() > 0;
		//totals to be done post generation, because in MDX it take too long
		config.setDoColumnsTotals(false);//we are moving totals calculation out of MDX. columns totals in MDX are equivalent to what we perceive as row totals in standard report
		config.setDoRowTotals(false); //we are moving totals calculation out of MDX. row totals in MDX are equivalent to what we perceive as column totals, e.g. this is for Total Actual Commitments
		config.setColumnsHierarchiesTotals(0); //we are moving subtotals out of MDX.
		config.setRowsHierarchiesTotals(0); //we are moving subtotals out of MDX.
		//add requested columns
		//if (!spec.isSummaryReport())
			for (ReportColumn col:spec.getColumns()) {
				MDXAttribute elem = (MDXAttribute)MondrianMapping.toMDXElement(col);
				if (elem == null) 
					reportError("No mapping found for column name = " + (col==null ? null : col.getColumnName()));
				else 
					config.addRowAttribute(elem);
			}
		//add requested measures
		for (ReportMeasure measure: spec.getMeasures()) {
			MDXMeasure elem = (MDXMeasure)MondrianMapping.toMDXElement(measure);
			if (elem == null) 
				reportError("No mapping found for measure name = " + (measure==null ? null : measure.getMeasureName()) + ", entity type = " + (measure == null ? null : measure.getEntityName()));
			else
				config.addColumnMeasure(elem);
		}
		//add grouping columns for measure
		config.getColumnAttributes().addAll(MondrianMapping.getDateElements(spec.getGroupingCriteria(), spec.getSettings().getCalendar()));
		//add sorting
		configureSortingRules(config, spec, doHierarchiesTotals);
		
		// add empty rows and columns request configuration
		addEmptyColRows(spec, config);
		
		//add filters
		addFilters(spec.getFilters(), config);
		
		//add settings
		addSettings(spec.getSettings(), config);
		
		return config;
	}
	
	private void configureSortingRules(MDXConfig config, ReportSpecification spec, boolean doHierarchiesTotals) throws AMPException {
		if (spec.getSorters() == null || spec.getSorters().size() == 0 ) return;
		boolean nonBreakingSort = spec.isCalculateRowTotals() || doHierarchiesTotals;
		
		for (SortingInfo sortInfo : spec.getSorters()) {
			MDXTuple tuple = new MDXTuple();
			if (!sortInfo.isTotals) {//totals sorting will be done during post-processing
				for (Entry<ReportElement, FilterRule> entry : sortInfo.sortByTuple.entrySet()) {
					if (ElementType.ENTITY.equals(entry.getKey().type))
						tuple.add(applySingleFilter(MondrianMapping.toMDXElement(entry.getKey().entity), entry.getValue()));
					else { 
						MDXAttribute mdxAttr = MondrianMapping.getElementByType(entry.getKey().type);
						if (mdxAttr == null) {
							String err = "No mapping found for Element type = " + entry.getKey().type;
							logger.error(err);
							throw new AMPException(err);
						}
						tuple.add(applySingleFilter(mdxAttr, entry.getValue()));
					}	
				}
				config.getSortingOrder().put(tuple, sortInfo.ascending ? 
													(nonBreakingSort ? SortOrder.ASC : SortOrder.BASC) : 
													(nonBreakingSort ? SortOrder.DESC : SortOrder.BDESC));
			}
		}
	}
	
	private MDXElement applySingleFilter(MDXElement elem, FilterRule filter) throws AMPException {
		if (filter == null) return elem;
		if (!FilterType.SINGLE_VALUE.equals(filter.filterType))
			throw new AMPException("Sorting filter must be a single value filter");
		if (elem instanceof MDXMeasure)
			throw new AMPException("Single value sorting filter cannot be applied over measures");
		((MDXAttribute)elem).setValue(filter.value);
		return elem;
	}
	/**
	 * Determines if a ReportElement should be processed through SQL
	 * 
	 * @param element the ReportElement to be analyzed
	 * @return boolean
	 */
	private boolean isProcessedBySQL(ReportElement element) {
		boolean processedBySql = false;
		// at the moment only dates are filtered out, but years, quarters and months are not :(
		// ignore DATE filters, as those are now processed through the SQL  filter

		if (element.type == ElementType.DATE) {
			processedBySql = true;
		}

		// processed through SQL
		else if (element.type == ElementType.ENTITY
				&& (FiltersGroup.FILTER_GROUP.containsKey(element.entity.getEntityName()) || MondrianSQLFilters.FILTER_RULE_CLASSES_MAP
						.containsKey(element.entity.getEntityName()))) {
			processedBySql = true;
		}
		return processedBySql;
	}
	private void addFilters(ReportFilters reportFilter, MDXConfig config) throws AmpApiException {
		if (reportFilter == null) return;
		
		// configures the main filter rules over different criteria type (different hierarchies)
		for(Entry<ReportElement, List<FilterRule>> entry : reportFilter.getFilterRules().entrySet()) {
			ReportElement elem = entry.getKey();
			MDXElement mdxElem = null;
			if (isProcessedBySQL(elem)) {
				continue;
			}
			
			if (elem.type == ElementType.ENTITY) {
				mdxElem = MondrianMapping.toMDXElement(elem.entity);
			} else {
				mdxElem = MondrianMapping.getElementByType(elem.type);
			}
				
			if (mdxElem == null) {
				reportError("Mapping not defined for report element = " + elem);
				if (IS_DEV) continue;
			}
			
			for (FilterRule filter : entry.getValue()) {
				MDXFilter mdxFilter = null;
				
				switch(filter.filterType) {
				case RANGE: mdxFilter = new MDXFilter(filter.min, filter.minInclusive, filter.max, filter.maxInclusive); 
				break;
				case SINGLE_VALUE: mdxFilter = new MDXFilter(filter.value, filter.valuesInclusive);
				break;
				case VALUES: mdxFilter = new MDXFilter(filter.values, filter.valuesInclusive);
				break;
				}
				
				if (MDXAttribute.class.isAssignableFrom(mdxElem.getClass()))
					config.addDataFilter((MDXAttribute)mdxElem, mdxFilter);
				else
					config.addAxisFilter(mdxElem, mdxFilter);
			}
		}
	}
	
	private void addSettings(ReportSettings reportSettings, MDXConfig config) {
		if (reportSettings == null) return;
		/* unfortunately the formatting works a bit differently in Mondrian, e.g.:
		 * 1) original value '19,795,441,979.544' => MDX pattern '# ##0,##' => displayed value '1979544 1,979,544'
		 * 2) original value '162,330' => MDX pattern '#,##0.##' => displayed value '162,330.' (with dot at the end)
		 * so we'll do this as well manually during post process...  
		if (reportSettings.getCurrencyFormat() != null)
			config.setAmountsFormat(reportSettings.getCurrencyFormat().toPattern());
		*/
	}
	
	private void addEmptyColRows(ReportSpecification spec, MDXConfig config) {
		config.setAllowEmptyColumnsData(spec.isDisplayEmptyFundingColumns());
		
		if (spec.isDisplayEmptyFundingColumns() && ReportSpecificationImpl.class.isAssignableFrom(spec.getClass())) {
			ReportSpecificationImpl specImpl = (ReportSpecificationImpl) spec;
			// filter out undefined quarters & months
			try {
				MondrianReportFilters filters = (MondrianReportFilters) spec.getFilters();
				if (filters == null) {
					filters = new MondrianReportFilters();
					specImpl.setFilters(filters);
				}
					
				switch (spec.getGroupingCriteria()) {
				case GROUPING_QUARTERLY:
					filters.addSingleQuarterFilterRule(MoConstants.UNDEFINED_QUARTER_KEY, false);
					break;
				case GROUPING_MONTHLY:
					filters.addSingleMonthFilterRule(MoConstants.UNDEFINED_MONTH_KEY, false);
					break;
				default:
					break;
				}
			} catch (Exception e) {
				logger.error(e);
			}
		}
		config.setAllowEmptyRowsData(spec.isDisplayEmptyFundingRows());
	}
	
	private CellDataSet postProcess(ReportSpecification spec, CellSet cellSet) throws AMPException {		
		CellSetAxis rowAxis = cellSet.getAxes().size() == 2 ? cellSet.getAxes().get(Axis.ROWS.axisOrdinal()) : null;
		CellSetAxis columnAxis = cellSet.getAxes().get(Axis.COLUMNS.axisOrdinal());
				
		logger.info("[" + spec.getReportName() + "]" +  "Starting conversion from Olap4J CellSet to Saiku CellDataSet via Saiku method...");
		CellDataSet cellDataSet = OlapResultSetUtil.cellSet2Matrix(cellSet); // we can also pass a formater to cellSet2Matrix(cellSet, formatter)
		logger.info("[" + spec.getReportName() + "]" +  "Conversion from Olap4J CellSet to Saiku CellDataSet ended.");

		// AMP-18748
		SaikuUtils.cleanupTraceHeadersIfNoData(cellDataSet);
		
		leafHeaders = getOrderedLeafColumnsList(spec, rowAxis, columnAxis);		

		// now cleanup dummy measures, identified during #getOrderedLeafColumnsList
		//SaikuUtils.removeColumns(cellDataSet, dummyColumnsToRemove);
		
		boolean calculateTotalsOnRows = spec.isCalculateRowTotals()
				//enable totals for non-hierarhical columns
				|| spec.getHierarchies().size() < spec.getColumns().size();
		
		if (spec.isCalculateColumnTotals() || calculateTotalsOnRows) {
			try {
				logger.info("[" + spec.getReportName() + "]" +  "Starting totals calculation over the Saiku CellDataSet via Saiku method...");
				SaikuUtils.doTotals(cellDataSet, cellSet, spec.isCalculateColumnTotals(), calculateTotalsOnRows);
				logger.info("[" + spec.getReportName() + "]" +  "Totals over the Saiku CellDataSet ended.");
			} catch (Exception e) {
				logger.error(e.getMessage());
				throw new AMPException(e.getMessage(), e);
			}
		}
		
		applyFilterSetting(spec, cellDataSet);
		
		CellDataSetToAmpHierachies.concatenateNonHierarchicalColumns(
				spec, cellDataSet, leafHeaders, cellDataSetActivities);
		postprocessUndefinedEntries(spec, cellDataSet);
		
		//clear totals if were enabled for non-hierarchical merges
		if (!spec.isCalculateColumnTotals())
			cellDataSet.setColTotalsLists(null);
		if (!spec.isCalculateRowTotals())
			cellDataSet.setRowTotalsLists(null);
		
		// update coordintates after data reshufle
		SaikuUtils.updateCoordinates(cellDataSet);
		
		return cellDataSet;
	}
	
	/**
	 * replace Columns "Undefined" values by ""
	 * Replace Hierarchies' "Undefined" values by <ColumnName>:<Undefined>
	 * hacky, but doing it cleanly (via ETL + schema + MDX generator) would be a huge pain for little gain
	 */
	protected void postprocessUndefinedEntries(ReportSpecification spec, CellDataSet cellDataSet) {
		String translatedUndefined = TranslatorWorker.translateText("Undefined", environment.locale, 3l);
		String translatedUnspecified = TranslatorWorker.translateText("Unspecified", environment.locale, 3l);
		
		for (int rowId = 0; rowId < cellDataSet.getCellSetBody().length; rowId++) {
			AbstractBaseCell[] row = cellDataSet.getCellSetBody()[rowId];
			if (row == null) continue; // who knows, let's be defensive
			
			for(int i = 0; i < row.length; i++) {
				if (row[i] != null && 
						(translatedUndefined.equals(row[i].getFormattedValue()))
						|| ("#null").equals(row[i].getFormattedValue()) // this is for nontranslateable columns
						) {
					boolean isHierarchy = i < spec.getHierarchies().size();
					String newValue = isHierarchy ? 
											String.format("%s: %s", leafHeaders.get(i).columnName, translatedUndefined) :
											spec.isEmptyOutputForUnspecifiedData() ? "" : 
											"(" + leafHeaders.get(i).columnName + " " + translatedUnspecified + ")";

					row[i].setRawValue(newValue);
					row[i].setFormattedValue(newValue);
				}
			}
		}
	}
	
	private void applyFilterSetting(ReportSpecification spec, CellDataSet cellDataSet) throws AMPException {
		if (spec.getSettings() == null || spec.getSettings().getFilterRules() == null) return;
		for (Entry<ReportElement, List<FilterRule>> pair : spec.getSettings().getFilterRules().entrySet()) {
			switch(pair.getKey().type) {
			case YEAR: 
				if (!GroupingCriteria.GROUPING_TOTALS_ONLY.equals(spec.getGroupingCriteria()))
					applyYearRangeSetting(spec, pair.getValue(), cellDataSet);
				break;
			default: throw new AMPException("Not supported: settings behavior over " + pair.getKey().type);
			}
		}
	}
	
	private void applyYearRangeSetting(ReportSpecification spec, List<FilterRule> filters, CellDataSet cellDataSet) throws AMPException {
		if (leafHeaders == null || leafHeaders.size() == 0) return;

		//detect the level where years are displayed
		int level = 0; //this is measures level
		switch(spec.getGroupingCriteria()) {
		case GROUPING_YEARLY : level = 1; break;
		case GROUPING_QUARTERLY : level = 2; break; //i.e. level 0 is for measures, level 1 is for quarters and level 2 is for years
		case GROUPING_MONTHLY : level = 3; break;
		case GROUPING_TOTALS_ONLY : return;  
		}

		List<Integer[]> yearRanges = new ArrayList<Integer[]>();
		Set<Integer> yearSet = new TreeSet<Integer>();
		getYearSettings(filters, yearRanges, yearSet);
		
		Iterator<ReportOutputColumn> iter = leafHeaders.iterator();
		int pos = spec.getColumns().size(); //initial position in the list
		//skip report columns unrelated to years
		while(pos > 0 && iter.hasNext()) {
			iter.next();
			pos--;
		}
		
		//detect the columns that are not in the years ranges or years set
		SortedSet<Integer> leafColumnsNumberToRemove = new TreeSet<Integer>();
		pos = spec.getColumns().size(); //re-init the starting position
		int end = leafHeaders.size() - spec.getMeasures().size();
		while(iter.hasNext() && pos < end) {
			ReportOutputColumn column = iter.next();
			//move to year level header
			int currentLevel = level;
			while(currentLevel > 0 && column!= null) {
				column = column.parentColumn;
				currentLevel--; 
			}
			if (column != null) { //must not be null actually
				int year = CalendarUtil.parseYear(spec.getSettings().getCalendar(), column.columnName);
				// first check if it is in the set
				boolean isAllowed = yearSet.contains(year); 
				if (!isAllowed)
					for (Integer[] range : yearRanges)
						// check if the year is within any [min, max] allowed range from the list of configured ranges
						if (range[0] <= year && year <= range[1]) { 
							isAllowed = true;
							break;
						}
				
				if (!isAllowed) {
					leafColumnsNumberToRemove.add(pos);
					// remove also the leaf header
					iter.remove();
				}
			}
			pos ++;
		}
		
		cellDataSet.setCellSetHeaders(removeYearsToHideCells(cellDataSet.getCellSetHeaders(), leafColumnsNumberToRemove));
		cellDataSet.setCellSetBody(removeYearsToHideCells(cellDataSet.getCellSetBody(), leafColumnsNumberToRemove));
		removeYearsToHideCells(cellDataSet, leafColumnsNumberToRemove);
	}
	
	private AbstractBaseCell[][] removeYearsToHideCells(AbstractBaseCell[][] cellSetHeaders, SortedSet<Integer> leafColumnsNumberToRemove) {
		if (cellSetHeaders.length == 0) return cellSetHeaders; //not the case, but.. 
		AbstractBaseCell[][] newCellSetHeaders = new AbstractBaseCell[cellSetHeaders.length][cellSetHeaders[0].length - leafColumnsNumberToRemove.size()];
		for (int i = 0; i < cellSetHeaders.length; i++) {
			int newJ = 0;
			for (int j = 0; j< cellSetHeaders[i].length; j++) {
				if (!leafColumnsNumberToRemove.contains(j)) {
					newCellSetHeaders[i][newJ++] = cellSetHeaders[i][j];  
				}
			}	
		}
		return newCellSetHeaders;
	}
	
	private void removeYearsToHideCells(CellDataSet cellDataSet, SortedSet<Integer> leafColumnsNumberToRemove) {
		//navigate through the totals list and remember the totals only for the columns we display
		if (cellDataSet.getRowTotalsLists() == null)
			return;
		for(List<TotalNode> totalLists : cellDataSet.getRowTotalsLists()) {
			for(TotalNode totalNode : totalLists) {
				if (totalNode.getTotalGroups() != null && totalNode.getTotalGroups().length > 0) {
					int offset = cellDataSet.getLeftOffset();
					for (int i = 0; i < totalNode.getTotalGroups().length; i++) {
						int newJ  = 0; 
						TotalAggregator[] newLine = new TotalAggregator[totalNode.getTotalGroups()[i].length - leafColumnsNumberToRemove.size()];
						for (int j = 0; j< totalNode.getTotalGroups()[i].length; j++) {
							if ( !leafColumnsNumberToRemove.contains(j+offset) ) {
								newLine[newJ ++ ] = totalNode.getTotalGroups()[i][j];
							} 
						}
						totalNode.getTotalGroups()[i] = newLine; 
					}
				}
			}
		}
	}
	
	private void getYearSettings(List<FilterRule> filters, List<Integer[]> yearRanges, Set<Integer> yearSet) {
		//build the list of ranges and selective set of years
		for(FilterRule rule : filters) {
			switch(rule.filterType) {
			case RANGE :
				Integer min = rule.min == null ? Integer.MIN_VALUE : Integer.parseInt(rule.min);
				Integer max = rule.max == null ? Integer.MAX_VALUE : Integer.parseInt(rule.max);
				yearRanges.add(new Integer[]{min, max});
				break;
			case SINGLE_VALUE : 
				yearSet.add(Integer.parseInt(rule.value)); 
				break;
			case VALUES : 
				for (String value : rule.values) {
					yearSet.add(Integer.parseInt(value));
				}
				break;
			}
		} 
	}
	
	private GeneratedReport toGeneratedReport(ReportSpecification spec, CellDataSet cellDataSet, int duration) throws AMPException {
		long start = System.currentTimeMillis();
		CellDataSetToGeneratedReport translator = new CellDataSetToGeneratedReport(
				spec, cellDataSet, leafHeaders, cellDataSetActivities);
		ReportAreaImpl root = translator.transformTo(reportAreaType);
		GeneratedReport genRep = new GeneratedReport(spec, duration + (int)(System.currentTimeMillis() - start), null, root, getRootHeaders(leafHeaders), leafHeaders); 
		return genRep;
	}
	
	@Deprecated
	private GeneratedReport toGeneratedReport(ReportSpecification spec, CellSet cellSet, int duration) throws AMPException {
		CellSetAxis rowAxis = cellSet.getAxes().get(Axis.ROWS.axisOrdinal());
		CellSetAxis columnAxis = cellSet.getAxes().get(Axis.COLUMNS.axisOrdinal());
		ReportAreaImpl root = MondrianReportUtils.getNewReportArea(reportAreaType);
		
		if (rowAxis.getPositionCount() > 0 && columnAxis.getPositionCount() > 0 ) {
			/* Build Report Areas */
			// stack of current group of children
			Deque<List<ReportArea>> stack = new ArrayDeque<List<ReportArea>>();
			int maxStackSize = 1 + (spec.isCalculateRowTotals() ? rowAxis.getAxisMetaData().getHierarchies().size()  : 0); 
			refillStack(stack, maxStackSize); //prepare the stack
			
			int cellOrdinal = 0; //initial position of row data from the cellSet
			boolean wasAreaEnd = false; 
			
			for (Position rowPos : rowAxis.getPositions()) {
				int columnPos = 0;
				boolean areaEnd = false; 
				ReportAreaImpl reportArea = MondrianReportUtils.getNewReportArea(reportAreaType);
				Map<ReportOutputColumn, ReportCell> contents = new LinkedHashMap<ReportOutputColumn, ReportCell>();
				
				for (Member member : rowPos.getMembers()) {
					TextCell text = new TextCell(member.getName());
					contents.put(leafHeaders.get(columnPos++), text);
					areaEnd = areaEnd || member.isAll();
				}
				
				for (Position colPos : columnAxis.getPositions()) {
					Cell cell = cellSet.getCell(cellOrdinal++);
					if (cell.getValue() instanceof OlapException) {
						logger.error("Unexpected cell error: " + MondrianUtils.getOlapExceptionMessage((OlapException)cell.getValue()));
					} else {
						if (cell.getValue() != null) {
							AmountCell amount = new AmountCell(new BigDecimal(cell.getValue().toString()), null);
							contents.put(leafHeaders.get(columnPos++), amount);
						} else 
							columnPos++; //skip column
					} 
				}
				
				reportArea.setContents(contents);
				
				if (areaEnd) {
					reportArea.setChildren(stack.pop());
				} else if (wasAreaEnd) { 
					//was an area end but now is simple content => refill with new children lists to the stack 
					refillStack(stack, maxStackSize);
				}
				stack.peek().add(reportArea);
				wasAreaEnd = areaEnd;
			}
			root.setChildren(stack.pop());
		}
			
		//we should have requesting user already configure in spec - spec must have all required data
		GeneratedReport genRep = new GeneratedReport(spec, duration, null, root, getRootHeaders(leafHeaders), leafHeaders); 
		return genRep;
	}
	
	/**
	 * See https://jira.dgfoundation.org/browse/AMP-18656 for the details
	 * If the report query returns empty response the list of column headers is populated from the request
	 * 
	 * @param reportColumns - target 
	 * @param spec - the report specification
	 */
	private void addStaticColumnHeaders(List<ReportOutputColumn> reportColumns, ReportSpecification spec) {
		if (reportColumns != null && spec.getColumns() != null) {
			for (ReportColumn reportColumn : spec.getColumns()) {
				String originalColumnName = reportColumn.getColumnName();
				ReportOutputColumn reportOutputColumn = ReportOutputColumn.buildTranslated(originalColumnName, environment.locale, null);
				reportColumns.add(reportOutputColumn);
			}
		}
	}
	
	private List<ReportOutputColumn> getOrderedLeafColumnsList(ReportSpecification spec, CellSetAxis rowAxis, CellSetAxis columnAxis) {
		//<fully qualified column name, ReportOutputColumn instance>, where fully qualified means with all parents name: /root/root-child/root-grandchild/...
		Map<String, ReportOutputColumn> reportColumnsByFullName = new LinkedHashMap<String,ReportOutputColumn>();
		List<ReportOutputColumn> reportColumns = new ArrayList<ReportOutputColumn>(); //leaf report columns list

		//build the list of available columns
		if (rowAxis != null && rowAxis.getPositionCount() > 0 ) {
			for (Member textColumn : rowAxis.getPositions().get(0).getMembers()) {
				ReportOutputColumn reportColumn = new ReportOutputColumn(textColumn.getLevel().getCaption(), null, 
						MondrianMapping.fromFullNameToColumnName.get(textColumn.getLevel().getUniqueName()));
				reportColumns.add(reportColumn);
			}
		} else if (spec.isPopulateReportHeadersIfEmpty()) {
			addStaticColumnHeaders(reportColumns, spec);
		}
		
		int colIdx = reportColumns.size();
		
		LinkedHashSet<String> outputtedMeasures = new LinkedHashSet<>(); // the set of the measures for which saiku generated an output (and we are supposed to generate totals)
		//int measuresLeafPos = columnAxis.getAxisMetaData().getHierarchies().size();
		if (columnAxis.getPositions() != null)
			for (Position position : columnAxis.getPositions()) {
				String fullColumnName = "";
				for (Member measureColumn : position.getMembers()) {
					ReportOutputColumn parent = reportColumnsByFullName.get(fullColumnName);
					fullColumnName += "/" +  measureColumn.getName();
					ReportOutputColumn reportColumn = reportColumnsByFullName.get(fullColumnName);
					if (reportColumn == null) {
						reportColumn = new ReportOutputColumn(measureColumn.getCaption(), parent, measureColumn.getName());
						reportColumnsByFullName.put(fullColumnName, reportColumn);
					}
					if (measureColumn.getDepth() == 0) { //lowest depth ==0 => this is leaf column
						/*
						if (MeasureConstants.ALWAYS_PRESENT.equals(reportColumn.originalColumnName)) {
							dummyColumnsToRemove.add(colIdx);
						} else {
						*/
							if (!outputtedMeasures.contains(measureColumn.getName()))
								outputtedMeasures.add(measureColumn.getName());
							reportColumns.add(reportColumn);
						//}
					}
				}
				colIdx ++;
			}
		//add measures total columns
		if (spec.isCalculateColumnTotals() && !GroupingCriteria.GROUPING_TOTALS_ONLY.equals(spec.getGroupingCriteria())) {
			ReportOutputColumn totalMeasuresColumn = ReportOutputColumn.buildTranslated(MoConstants.TOTAL_MEASURES, environment.locale, null);
			for (String measureName : outputtedMeasures)
				reportColumns.add(ReportOutputColumn.buildTranslated(measureName, environment.locale, totalMeasuresColumn));
		}
		return reportColumns;
	}
	
	private void refillStack(Deque<List<ReportArea>> stack, int maxSize) {
		for (int i = stack.size(); i < maxSize; i++) {
			stack.push(new ArrayList<ReportArea>()); 
		}
	}
	
	private List<ReportOutputColumn> getRootHeaders(List<ReportOutputColumn> leafHeaders) {
		if (leafHeaders ==null || leafHeaders.size() == 0) return null;
		Set<ReportOutputColumn> rootHeaders = new LinkedHashSet<ReportOutputColumn>();
		for (ReportOutputColumn leaf : leafHeaders) {
			while(leaf.parentColumn != null) {
				leaf = leaf.parentColumn;
			}
			rootHeaders.add(leaf);
		}
		return Arrays.asList(rootHeaders.toArray(new ReportOutputColumn[1]));
	}
	
	private void reportError(String error) throws AmpApiException {
		if (IS_DEV)
			logger.error(error);
		else
			throw new AmpApiException(error);
	}
}

	