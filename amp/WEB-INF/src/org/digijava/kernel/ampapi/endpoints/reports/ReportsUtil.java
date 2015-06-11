package org.digijava.kernel.ampapi.endpoints.reports;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.derby.impl.sql.compile.ModifyColumnNode;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportElement;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.newreports.SortingInfo;
import org.dgfoundation.amp.reports.CachedReportData;
import org.dgfoundation.amp.reports.PartialReportArea;
import org.dgfoundation.amp.reports.ReportAreaMultiLinked;
import org.dgfoundation.amp.reports.ReportCacher;
import org.dgfoundation.amp.reports.ReportPaginationUtils;
import org.dgfoundation.amp.reports.mondrian.MondrianReportFilters;
import org.dgfoundation.amp.reports.mondrian.MondrianReportUtils;
import org.dgfoundation.amp.reports.mondrian.converters.AmpReportsToReportSpecification;
import org.dgfoundation.amp.utils.ConstantsUtil;
import org.dgfoundation.amp.visibility.data.ColumnsVisibility;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsConstants;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.kernel.ampapi.endpoints.util.GisConstants;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.mondrian.util.MoConstants;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.util.DbUtil;

public class ReportsUtil {
	protected static final Logger logger = Logger.getLogger(ReportsUtil.class);

	/**
	 * Retrieves the page for the  result for the specified reportId and a given page number
	 *  
	 * @param reportId    report Id
	 * @param formParams  form parameters, that must be in the following format: 	<br/>
	 * { 																			<br/>
	 * 	"page"            : 1, 														<br/>
	 *  "recordsPerPage"  : 10,														<br/>
	 *  "regenerate"      : true, 													<br/>
	 *  "filters"         : //see filters format defined in Gis, 					<br/>
	 *  "settings"        : //see {@link EndpointUtils#applySettings}                <br/>
	 *  "sorting"         : [ 														<br/>
	 *        {																		<br/>
	 *          "columns" : ["Donor Agency", "Actual Commitments"],					<br/>
	 *          "asc"     : true													<br/>
	 *        },																	<br/>
	 *        {																		<br/>
	 *          "columns" : ["Project Title"],										<br/>
	 *          "asc"     : false													<br/>
	 *        }																		<br/>
	 *       ] 																		<br/>
	 *  																			<br/>
	 *  "add_columns"     : ["Activity Id", "Approval Status"], 					<br/>
	 *  "add_hierarchies" : ["Approval Status"], 									<br/>
	 *  "add_measures"    : ["Custom Measure"], 									<br/>
	 *  "rowTotals"       : true													<br/>
	 *  "projectType"     : see {@link ProjectType}									<br/>
	 * } 																			<br/>
	 * 
	 * where: <br>
	 * <dl>
	 * 	 <dt>name</dt>			<dd>mandatory to be provided for custom reports, otherwise is skipped</dd> 
	 *   <dt>page</dt>        	<dd>optional, page number, starting from 1. Use 0 to retrieve only
	 *                        pagination information, without any records. Default to 0</dd>
	 *   <dt>recordsPerPage</dt> <dd>optional, the number of records per page to return. Default
	 *   					  will be set to the number configured in AMP. Set it to -1
	 *                        to get the unlimited records, that will provide all records.</dd>
	 *   <dt>sorting</dt>     	<dd>optional, a list of sorting maps. Each map has 'columns' list 
	 *                        and 'asc' flag, that is true for sorting ascending. Hierarchical 
	 *                        sorting will define a column list as a tuple to sort by.</dd>
	 *   <dt>regenerate</dt>	<dd>optional, set to true for all first access and any changes and 
	 *                        to false for consequent page navigation. Default to true</dd>
	 *   <dt>add_columns</dt> 	<dd>optional, a list of columns names to be added to the
	 *                        report configuration</dd>
	 *   <dt>add_hierarchies</dt>  <dd>optional, a list of hierarchies to be added to the
	 *                             report configuration</dd>
	 *   <dt>add_measures</dt> 	<dd>optional, a list of measures to be added to the
	 *                         report configuration</dd>
	 *   <dt>rowTotals</dt>    	<dd>optional, flag to request row totals to be build</dd>
     *   <dt>forceHeaders</dt>   <dd>optional, flag, if the report query returns empty response
     *                    the list of column headers is populated from the request. Default is false</dd>
	 *   <dt>settings</dt>	   	<dd>Report settings</dd>
	 * </dl>
	 * @return JsonBean result for the requested page and pagination information
	 */
	public static final JsonBean getReportResultByPage(Long reportId, JsonBean formParams) {
		JsonBean result = new JsonBean();
		
		// read pagination data
		int page = (Integer) EndpointUtils.getSingleValue(formParams, "page", 0);
		int recordsPerPage = EndpointUtils.getSingleValue(formParams, "recordsPerPage", 
				ReportPaginationUtils.getRecordsNumberPerPage());
		int start = (page - 1) * recordsPerPage;
		
		// get the report (from cache if it was cached)
		CachedReportData cachedReportData = getCachedReportData(reportId, formParams);
		ReportAreaMultiLinked[] areas = null;
		if (cachedReportData != null) {
			areas = cachedReportData.areas;
			if (cachedReportData.report != null) {
				result.set("headers", cachedReportData.report.leafHeaders);
			}
		}
		
		// extract data for the requested page
		ReportArea pageArea = null;
		if (recordsPerPage != -1) {
			pageArea = ReportPaginationUtils.getReportArea(areas, start, recordsPerPage);
		} else if (cachedReportData != null && cachedReportData.report !=null) {
			pageArea = cachedReportData.report.reportContents;
		}
		
		int totalPageCount = ReportPaginationUtils.getPageCount(areas, recordsPerPage);
		
		// configure the result
		result.set("page", new JSONReportPage(pageArea, recordsPerPage, page, totalPageCount, 
				(areas != null ? areas.length : 0)));
		result.set(EPConstants.SETTINGS, cachedReportData != null ? 
				SettingsUtils.getReportSettings(cachedReportData.report.spec) : null);
		return result;
	}
	
	protected static JsonBean convertSaikuParamsToReports(JsonBean original) {
		JsonBean newParams = new JsonBean();
		LinkedHashMap queryModel = (LinkedHashMap) original.get("queryModel");
		if (queryModel != null) {
			if (queryModel.get("page") != null) {
				// Saiku is 0 based so we add 1.
				newParams.set("page", ((Integer) queryModel.get("page")) + 1);
			}
			if (queryModel.get("recordsPerPage") != null) {
				newParams.set("recordsPerPage", ((Integer) queryModel.get("recordsPerPage")));
			}
			if (queryModel.get("filters") != null) {
				newParams.set("filters", queryModel.get("filters"));
			}
			if (queryModel.get(EPConstants.SETTINGS) != null) {
				newParams.set(EPConstants.SETTINGS, queryModel.get(EPConstants.SETTINGS));
			}
			if (queryModel.get(EPConstants.SORTING) != null) {
				newParams.set(EPConstants.SORTING, queryModel.get(EPConstants.SORTING));
			}
			if (queryModel.get("regenerate") != null) {
				newParams.set("regenerate", queryModel.get("regenerate"));
			}
		}
		return newParams;
	}
	
	private static CachedReportData getCachedReportData(Long reportId, JsonBean formParams) {
		boolean regenerate = mustRegenerate(reportId, formParams);
		boolean resort = formParams.get(EPConstants.SORTING) != null; 
		CachedReportData cachedReportData = null;
		
		// generate the report
		if (regenerate) {
			ReportSpecificationImpl spec = null;
			if (Boolean.TRUE.equals(formParams.get(EPConstants.IS_CUSTOM))) { 
				String reportName = formParams.getString(EPConstants.REPORT_NAME);
				spec = new ReportSpecificationImpl(reportName, ArConstants.DONOR_TYPE);
			} else {
				spec = ReportsUtil.getReport(reportId);
			}
			// add additional requests
			update(spec, formParams);
			// regenerate
			GeneratedReport generatedReport = EndpointUtils.runReport(spec, PartialReportArea.class);
			cachedReportData = ReportPaginationUtils.cacheReportData(reportId, generatedReport);
		} else {
			cachedReportData = ReportCacher.getReportData(reportId);
			if (resort && cachedReportData != null) {
				if (configureSorting((ReportSpecificationImpl)cachedReportData.report.spec, formParams)) {
					// resort only when sorting configuration changed
					try {
						MondrianReportUtils.sort(cachedReportData.report);
						// update cache with resorted data
						cachedReportData = ReportPaginationUtils.cacheReportData(reportId, cachedReportData.report);
					} catch (AMPException e) {
						logger.error(e);
					}
				}
			}
		}
		return cachedReportData;
	}
	
	/**
	 * Retrieves report configuration for the specified report id 
	 * @param reportId - report id to provide the specification for 
	 * @return ReportSpecification object
	 * @throws AMPException
	 */
	public static ReportSpecificationImpl getReport(Long reportId) {
		AmpReports ampReport = DbUtil.getAmpReport(reportId);
		try {
			if (ampReport != null)
				return AmpReportsToReportSpecification.convert(ampReport);
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}
	
	/**
	 * Updates report specification with the configuration comming from   
	 * @param spec - the specification that will be updated
	 * @param formParams
	 * @return the updated spec
	 */
	public static ReportSpecification update(ReportSpecification spec, 
			JsonBean formParams) {
		if (spec == null || formParams == null) return spec;
		if (!(spec instanceof ReportSpecificationImpl)) {
			logger.error("Unsupported request for "  + spec.getClass());
			return spec;
		}
		ReportSpecificationImpl specImpl = (ReportSpecificationImpl) spec;
		
		//update report structure
		addColumns(specImpl, formParams);
		addHierarchies(specImpl, formParams);
		addMeasures(specImpl, formParams);
		
		//update report data presentation
		configureFilters(specImpl, formParams);
		configureSorting(specImpl, formParams);
		SettingsUtils.applySettings(specImpl, formParams);
		
		//update other settings
		setOtherOptions(specImpl, formParams);
		
		return spec;
	}
	
	private static void addColumns(ReportSpecification spec, JsonBean formParams) {
		//adding new columns if not present
		if (formParams.get(EPConstants.ADD_COLUMNS) != null) {
			List<String> columns = (List<String>) formParams.get(EPConstants.ADD_COLUMNS);
			for (String columnName : columns) {
				ReportColumn column = new ReportColumn(columnName);
				if (!spec.getColumns().contains(column)) {
					spec.getColumns().add(column);
				}
			}
		}
	}
	
	private static void addHierarchies(ReportSpecification spec, JsonBean formParams) {
		//adding new hierarchies if not present
		if (formParams.get(EPConstants.ADD_HIERARCHIES) != null) {
			List<String> hierarchies = (List<String>) formParams.get(EPConstants.ADD_HIERARCHIES);
			List<ReportColumn> existingColumns = new ArrayList<ReportColumn>();
			existingColumns.addAll(spec.getColumns());
			for (String columnName : hierarchies) {
				ReportColumn column = new ReportColumn(columnName);
				if (!spec.getHierarchies().contains(column)) {
					//add as a column if not present 
					if (!existingColumns.contains(column)) {
						existingColumns.add(spec.getHierarchies().size(), column);
					}
					spec.getHierarchies().add(column);
				}
			}
			spec.getColumns().clear();
			spec.getColumns().addAll(existingColumns);
		}
	}
	
	private static void addMeasures(ReportSpecification spec, JsonBean formParams) {
		//add new measures if not present
		if (formParams.get(EPConstants.ADD_MEASURES) != null) {
			List<String> measures = (List<String>) formParams.get(EPConstants.ADD_MEASURES);
			for (String measureName : measures) {
				ReportMeasure measure = new ReportMeasure(measureName);
				if (!spec.getMeasures().contains(measure)) {
					spec.getMeasures().add(measure);		}
			}
		}
	}
	
	private static void configureFilters(ReportSpecificationImpl spec, JsonBean formParams) {
		JsonBean filters = new JsonBean();
		LinkedHashMap<String, Object> requestFilters = (LinkedHashMap<String, Object>) formParams.get("filters");
		MondrianReportFilters mondrianReportFilters = null;
		if (requestFilters != null) {
			filters.any().putAll(requestFilters);
			mondrianReportFilters = FilterUtils.getFilters(filters);
			// set filters even if they are empty, that means filters are cleared up
			((ReportSpecificationImpl)spec).setFilters(mondrianReportFilters);
		}
	}
	
	/**
	 * Retrieves sorting request
	 * @param spec
	 * @param formParams
	 * @return true if sorting configuration changed
	 */
	public static boolean configureSorting(ReportSpecificationImpl spec, JsonBean formParams) {
		List<SortingInfo> newSorters = new ArrayList<SortingInfo>();
		
		if (formParams.get(EPConstants.SORTING) != null) {
			List<Map<String, Object>> sortingConfig = 
					(List<Map<String, Object>>)formParams.get(EPConstants.SORTING);
			Set<String> validColumns = ConstantsUtil.getConstantsSet(ColumnConstants.class);
			Set<String> validMeasures = ConstantsUtil.getConstantsSet(MeasureConstants.class);
			
			for (Map<String, Object> sort : sortingConfig) {
				LinkedHashMap<ReportElement, FilterRule> sortByTuple = new LinkedHashMap<ReportElement, FilterRule>();
				boolean isTotals = true;
				
				List<String> columns = (List<String>)sort.get("columns");
				Boolean asc = (Boolean)sort.get("asc");
				
				List<String> errors = new ArrayList<String>();
				if (columns == null)
					errors.add("columns = null");
				if (asc == null)
					errors.add("sorting order is not specified, asc = null");
				if (errors.size() == 0) {
					boolean hasReportColumn = false;
					ReportOutputColumn fundingColumn = null;
					for (Iterator<String> iter = columns.iterator(); iter.hasNext(); ) {
						String column = iter.next();
						if (validColumns.contains(column)) {
							hasReportColumn = true;
							SortingInfo.addEntityToSorting(sortByTuple, new ReportColumn(column));
						} else if (validMeasures.contains(column)) {
							// no totals sorting if 1st entry in tuple is funding column
							if (sortByTuple.size() == 0 && fundingColumn != null)
								isTotals = false;
							fundingColumn = addFundingColumnToSorting(spec, sortByTuple, fundingColumn);
							SortingInfo.addEntityToSorting(sortByTuple, new ReportMeasure(column));
						} else {
							if (GroupingCriteria.GROUPING_TOTALS_ONLY.equals(spec.getGroupingCriteria())) {
								errors.add("Invalid column name = " + column);
								break;
							}
							fundingColumn = new ReportOutputColumn(column, fundingColumn, null);
						}
					}
					if (fundingColumn != null)
						errors.add("funding column not followed by a measure");
					// if there is only 1 entry on the tuple that is a simple column, then no totals sorting  
					if (hasReportColumn && sortByTuple.size() == 1)
						isTotals = false;
				}
					
				if (errors.size() > 0)
					logger.error("Ignoring invalid sorting request: " + errors);
				else
					newSorters.add(new SortingInfo(sortByTuple, asc, isTotals));
					
			}
		}
		
		// check if sorting config indeed changed
		boolean sortingChanged = !MondrianReportUtils.equals(newSorters, spec.getSorters());
		if (sortingChanged)
			spec.setSorters(newSorters);
		return sortingChanged;
	}
	
	private static ReportOutputColumn addFundingColumnToSorting(ReportSpecification spec, 
			LinkedHashMap<ReportElement, FilterRule> sortByTuple, ReportOutputColumn roc) {
		if (roc != null) {
			if (GroupingCriteria.GROUPING_MONTHLY.equals(spec.getGroupingCriteria())) {
				SortingInfo.addMonthToSorting(sortByTuple, roc.columnName);
				roc = roc.parentColumn;
			} else if (GroupingCriteria.GROUPING_QUARTERLY.equals(spec.getGroupingCriteria())) {
				SortingInfo.addQuarterToSorting(sortByTuple, roc.columnName);
				roc = roc.parentColumn;
			}
			// both for GROUPING_YEARLY & previous groupings
			SortingInfo.addYearToSorting(sortByTuple, roc.columnName);
		}
		// null to clear the reference
		return null;
	}
	
	/**
	 * Validates the report config data provide via formParams
	 * 
	 * @param formParams input parameters used 
	 * @return a list of errors
	 */
	public static final List<String> validateReportConfig(JsonBean formParams,
			boolean isCustom) {
		List<String> errors = new ArrayList<String>();
		// validate the name
		if (isCustom && StringUtils.isBlank(formParams.getString(EPConstants.REPORT_NAME)))
			errors.add("report name not specified");
		
		// validate the columns
		String err = validateList("columns", (List<String>) formParams.get(EPConstants.ADD_COLUMNS),
				MondrianReportUtils.getConfigurableColumns(), isCustom);
		if (err != null) errors.add(err);
		
		// validate the measures
		err = validateList("measures", (List<String>) formParams.get(EPConstants.ADD_MEASURES),
				MondrianReportUtils.getConfigurableMeasures(), isCustom);
		if (err != null) errors.add(err);
		
		// validate the hierarchies
		err = validateList("hierarchies", (List<String>) formParams.get(EPConstants.ADD_HIERARCHIES),
				(List<String>) formParams.get(EPConstants.ADD_COLUMNS), false);
		if (err != null) errors.add(err);
		
		return errors;
	}
	
	private static String validateList(String listName, List<String> values, 
			Collection<String> allowedValues, boolean isCustom) {
		if (values == null || values.size() == 0) {
			if (isCustom)
				return "no " + listName + " are specified";
		} else {
			List<String> copy = new ArrayList<String>(values);
			copy.removeAll(allowedValues);
			if (copy.size() > 0)
				return "not allowed / invalid " + listName + " provided = " + copy.toString();
		}
		return null;
	}
	
	private static void setOtherOptions(ReportSpecificationImpl spec, JsonBean formParams) {
		Boolean doRowTotals = (Boolean) formParams.get(EPConstants.DO_ROW_TOTALS);
		if (doRowTotals != null) {
            spec.setCalculateRowTotals(doRowTotals);
        }

        Boolean forceHeaders = (Boolean) formParams.get(EPConstants.FORCE_HEADERS);
        if (forceHeaders != null) {
            spec.setPopulateReportHeadersIfEmpty(forceHeaders);
        }
	}
	
	/**
	 * Verifies report must be (re)generated during pagination request
	 * 
	 * @param reportId     report Id
	 * @param formParams   json request parameters
	 * @return true if the report must be (re)generated 
	 */
	public static boolean mustRegenerate(Long reportId, JsonBean formParams) {
		boolean regenerate = (Boolean) EndpointUtils.getSingleValue(formParams, EPConstants.REGENERATE, Boolean.TRUE);
		
		// check if we need to force the regeneration, due to session timeout
		if (!regenerate && ReportCacher.getReportData(reportId) == null)
				regenerate = true;
		
		// TODO: add here any other automatic detections for mandatory regenerate 
		
		return regenerate;
	}
	
	/**
	 * Exports current report configuration to the map
	 * 
	 * @param config
	 * @param exportId
	 * @return
	 */
	public static String exportToMap(final JsonBean config, final Long reportId) {
		ReportSpecificationImpl spec = getReport(reportId);
		if (spec == null)
			return null;
		
		// detect layers view as a highest hierarchy from location columns
		String layersView = null;
		Set<String> orderedLocations = new LinkedHashSet<String>(
				Arrays.asList(ColumnConstants.COUNTRY, ColumnConstants.REGION, ColumnConstants.ZONE, ColumnConstants.DISTRICT, ColumnConstants.LOCATION));
		for (ReportColumn column : spec.getHierarchies()) {
			if (orderedLocations.contains(column.getColumnName())) {
				layersView = column.getColumnName();
				break;
			}
		}
		
		if (layersView == null) {
			// configure the default, that is the 1st sub-national level, e.g. Region if it is visible
			orderedLocations.remove(ColumnConstants.COUNTRY);
			Set<String> visibleColumns = ColumnsVisibility.getVisibleColumns();
			for (String defaultOption : orderedLocations) {
				if (visibleColumns.contains(defaultOption)) {
					layersView = defaultOption;
					break;
				}
			}
		}
		config.set(EPConstants.API_STATE_LAYERS_VIEW, layersView);
		
		// update the settings based on Measures
		Map<String, String> settings = (Map<String, String>) config.get(EPConstants.SETTINGS);
		// must be not null! but just in case something gets broken
		if (settings == null) {
			logger.error("No settings are provided - please fix!");
			settings = new HashMap<String, String>();
			config.set(EPConstants.SETTINGS, settings);
		}
		
		// set default funding type
		String fundingType = SettingsConstants.DEFAULT_FUNDING_TYPE_ID;
		
		/*
		 * NOTE: Requirement is still undefined clearly if any priority and which priority to be used,
		 * so configuring meanwhile the definition list priority from FUNDING_TYPES,
		 * because it seems some export mapping is still desired, though not clarified yet which one
		 */
		// get first measure that is defined in FUNDING_TYPES
		// => first found has highest priority to consider as the default option
		MEASURE_TEST: for (String measureName : GisConstants.FUNDING_TYPES) {
			for (ReportMeasure measure : spec.getMeasures()) {
				if (measureName.equals(measure.getMeasureName())) {
					fundingType = measureName;
					break MEASURE_TEST;
				}
			}
		}
		
		settings.put(SettingsConstants.FUNDING_TYPE_ID, fundingType);
		
		// we need to stringify the final config
		JSONObject jObject = new JSONObject();
		jObject.accumulateAll(config.any());
		
		// configure api state json 
		JsonBean apiState = new JsonBean();
		apiState.set(EPConstants.API_STATE_TITLE, spec.getReportName());
		apiState.set(EPConstants.API_STATE_DESCRIPTION, EPConstants.API_STATE_REPORT_EXPORT_DESCRIPTION);
		apiState.set(EPConstants.API_STATE_BLOB, jObject.toString());
		
		// Saving the export to the user session.
		// Will there be any need to keep multiple states for the same report export?
		TLSUtils.getRequest().getSession().setAttribute(EPConstants.API_STATE_REPORT_EXPORT + reportId, apiState);
		
		return reportId.toString();
	}
	
	/**
	 * Provides the saved api state for the given reportConfigId (at the moment = reportId)
	 * 
	 * @param reportConfigId
	 * @return JsonBean with saved Api state
	 */
	public static JsonBean getApiState(String reportConfigId) {
		// TODO: can we safely remove it from session afterwards? 
		return (JsonBean) TLSUtils.getRequest().getSession()
				.getAttribute(EPConstants.API_STATE_REPORT_EXPORT + reportConfigId);
	}
	
}