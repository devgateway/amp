package org.digijava.kernel.ampapi.endpoints.reports;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
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
import org.dgfoundation.amp.newreports.ReportElement.ElementType;
import org.dgfoundation.amp.newreports.ReportFilters;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.newreports.SortingInfo;
import org.dgfoundation.amp.reports.ActivityType;
import org.dgfoundation.amp.reports.CachedReportData;
import org.dgfoundation.amp.reports.PartialReportArea;
import org.dgfoundation.amp.reports.ReportAreaMultiLinked;
import org.dgfoundation.amp.reports.ReportCacher;
import org.dgfoundation.amp.reports.ReportPaginationUtils;
import org.dgfoundation.amp.reports.mondrian.MondrianReportFilters;
import org.dgfoundation.amp.reports.mondrian.MondrianReportUtils;
import org.dgfoundation.amp.reports.mondrian.converters.AmpReportsToReportSpecification;
import org.dgfoundation.amp.reports.mondrian.converters.MtefConverter;
import org.dgfoundation.amp.utils.BoundedList;
import org.dgfoundation.amp.utils.ConstantsUtil;
import org.dgfoundation.amp.visibility.data.ColumnsVisibility;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsConstants;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.kernel.ampapi.endpoints.util.GisConstants;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.endpoints.util.MaxSizeLinkedHashMap;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.annotations.reports.ColumnLike;
import org.digijava.module.aim.annotations.reports.Identificator;
import org.digijava.module.aim.annotations.reports.Level;
import org.digijava.module.aim.annotations.reports.Order;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpMeasures;
import org.digijava.module.aim.dbentity.AmpReportColumn;
import org.digijava.module.aim.dbentity.AmpReportHierarchy;
import org.digijava.module.aim.dbentity.AmpReportMeasures;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.AdvancedReportUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

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
	 *  "reportType"      : "C"														<br/>
	 *  "projectType"	  : ["S"]													<br/>
	 * } 																			<br/>
	 * 
	 * where: <br>
	 * <dl>
	 * 	 <dt>name</dt>			<dd>mandatory to be provided for custom reports, otherwise is skipped</dd> 
	 *   <dt>page</dt>        	<dd>optional, page number, starting from 1. Use 0 to retrieve only
	 *                        		pagination information, without any records. Default to 0</dd>
	 *   <dt>recordsPerPage</dt> <dd>optional, the number of records per page to return. Default
	 *   					  		will be set to the number configured in AMP. Set it to -1
	 *                        		to get the unlimited records, that will provide all records.</dd>
	 *   <dt>sorting</dt>     	<dd>optional, a list of sorting maps. Each map has 'columns' list 
	 *                        		and 'asc' flag, that is true for sorting ascending. Hierarchical 
	 *                        		sorting will define a column list as a tuple to sort by.</dd>
	 *   <dt>regenerate</dt>	<dd>optional, set to true for all first access and any changes and 
	 *                        		to false for consequent page navigation. Default to true</dd>
	 *   <dt>add_columns</dt> 	<dd>optional, a list of columns names to be added to the
	 *                        		report configuration</dd>
	 *   <dt>add_hierarchies</dt>  <dd>optional, a list of hierarchies to be added to the
	 *                             	report configuration</dd>
	 *   <dt>add_measures</dt> 	<dd>optional, a list of measures to be added to the
	 *                         		report configuration</dd>
	 *   <dt>show_empty_rows</dt> <dd>optional, default false, to show rows with empty measures amounts</dd>
	 *   <dt>show_empty_cols</dt> <dd>optional, default false, to show full column groups (by quarter, year) 
	 *   							with empty measures amounts</dd>
	 *   <dt>rowTotals</dt>    	<dd>optional, flag to request row totals to be build</dd>
	 *   <dt>forceHeaders</dt>  <dd>optional, flag, if the report query returns empty response
     *                    			the list of column headers is populated from the request. Default is false</dd>
	 *   <dt>settings</dt>	   	<dd>Report settings</dd>
	 *   <dt>reportType</dt>	<dd>can be on of "D" (Donor), "C" (Component), "P" (Pledge) report type.
	 *   						Default is "D" if not provided.</dd>
	 *   <dt>projectType</dt>   <dd>an optional list of projects, mainly used to change usual default project
	 *   						types included in the report. The list option per report type:
	 *   						"D" : ["A", "S", "P"], where default is ["A", "S"] if both are reachable
	 *   						"C" : ["A", "S"], where default is ["A", "S"]
	 *   						"P" : ["P"], where default is ["P"]
	 *   						where "A" - standard activity, "S" - SSC Activity, "P" - pledge</dd>
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
				newParams.set("page", ((Integer) queryModel.get("page")));
			}
			if (queryModel.get("recordsPerPage") != null) {
				newParams.set("recordsPerPage", ((Integer) queryModel.get("recordsPerPage")));
			}
			if (queryModel.get("filters") != null) {
				newParams.set("filters", queryModel.get("filters"));
				//newParams.set("page", new Integer("1"));
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
			if (original.get(EPConstants.IS_DYNAMIC) != null) {
				newParams.set(EPConstants.IS_DYNAMIC, true);
			}
		}
		
		if (original.get(EPConstants.ADD_COLUMNS) != null) {
			newParams.set(EPConstants.ADD_COLUMNS, original.get(EPConstants.ADD_COLUMNS));
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
				spec = EndpointUtils.getReportSpecification(formParams, reportName);
			} else {
				if (Boolean.TRUE.equals(formParams.get(EPConstants.IS_DYNAMIC))) {
					try {
						spec = AmpReportsToReportSpecification.convert(ReportsUtil.getAmpReportFromSession(reportId.intValue()));
					} catch (AMPException e) {
						logger.error("Cannot get report from session",e);
						throw new RuntimeException("Cannot restore report from session: " + reportId);
					}
				}else{
					spec = ReportsUtil.getReport(reportId);
				}
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
	 * Updates report specification with the configuration coming from   
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
		
		// update report structure
		addColumns(specImpl, formParams);
		addHierarchies(specImpl, formParams);
		addMeasures(specImpl, formParams);
		
		// update report data presentation
		AmpFiscalCalendar oldCalendar = specImpl.getSettings() == null ? null : specImpl.getSettings().getCalendar(); 
		SettingsUtils.applySettings(specImpl, formParams, false);
		configureFilters(specImpl, formParams, oldCalendar);
		configureSorting(specImpl, formParams);
		
		// update report data
		configureProjectTypes(specImpl, formParams);
		
		// update other settings
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
	
	public static void configureFilters(ReportSpecificationImpl spec, JsonBean formParams, 
			AmpFiscalCalendar oldCalendar) {
		JsonBean filters = new JsonBean();
		LinkedHashMap<String, Object> requestFilters = (LinkedHashMap<String, Object>) formParams.get(EPConstants.FILTERS);
		if (requestFilters != null) {
			filters.any().putAll(requestFilters);
			MondrianReportFilters newFilters = new MondrianReportFilters(spec.getSettings().getCalendar());
			if (spec.getFilters() != null) {
				// TODO: we need calendar + date to be linked in UI as well OR make same form for filters and settings
				// for now, if this is a calendar setting, let's check if any filters still exist and needs to be converted 
				if (spec.getSettings().getCalendar() != oldCalendar
						&& newFilters.getFilterRules().get(new ReportElement(ElementType.DATE)) != null) {
					newFilters.setOldCalendar(oldCalendar);
				}
			}
			
			MondrianReportFilters formFilters = FilterUtils.getFilters(filters, newFilters);
			MondrianReportFilters stickyFilters = copyStickyMtefEntries(spec.getFilters(), formFilters);
			spec.setFilters(stickyFilters);
		}
	}
	
	/**
	 * copies entries which are not enclosed in the Filter widget but should be kept post-filter-application from oldFilters to newFilters<br />
	 * Now this only means MTEF filter entries
	 * @param oldFilters
	 * @param newFilters
	 * @return
	 */
	protected static MondrianReportFilters copyStickyMtefEntries(ReportFilters oldFilters, MondrianReportFilters newFilters) {
		if (oldFilters == null || oldFilters.getFilterRules() == null)
			return newFilters; // no chance of stickies
		
		MondrianReportFilters result = newFilters == null ? new MondrianReportFilters() : newFilters;
		
		boolean somethingAdded = oldFilters.getComputedYear() != null;
		result.setComputedYear(oldFilters.getComputedYear());
		
		// set filters even if they are empty, that means filters are cleared up
		// copy MTEF-hacky entries from old widget to new widget, since these are supposed to be sticky (not present in the filter form)
		for(Entry<ReportElement, List<FilterRule>> elem: oldFilters.getFilterRules().entrySet()) {
			if (MtefConverter.MTEF_DATE_ELEMENT_TYPES.contains(elem.getKey().type)) {
				result.getFilterRules().put(elem.getKey(), elem.getValue());
				somethingAdded = true;
			}
		}
		if (newFilters == null && !somethingAdded)
			return newFilters; // do not alter filters if we did nothing
		
		return result;
	}
	
	/**
	 * Explicitly configures projects that has to be included into the report. 
	 * If nothing specified, then the default project types are used for the given report type.
	 * 
	 * @param spec 
	 * @param formParams
	 */
	public static void configureProjectTypes(ReportSpecificationImpl spec, JsonBean formParams) {
		List<String> projectTypeOptions = (List<String>) formParams.get(EPConstants.PROJECT_TYPE);
		if (projectTypeOptions == null || projectTypeOptions.size() == 0) 
			return;
		// no validation is done here, use "validateReportConfig" before creating report specification
		boolean hasPledge = projectTypeOptions.contains(ActivityType.PLEDGE.toString());
		boolean hasSSCActivity = projectTypeOptions.contains(ActivityType.SSC_ACTIVITY.toString());
		
		switch(spec.getReportType()) {
		case ArConstants.DONOR_TYPE:
			spec.setAlsoShowPledges(hasPledge);
			// no break, following same rule for SSC filters in both Donor & Component reports
		case ArConstants.COMPONENT_TYPE:
			if (hasSSCActivity && projectTypeOptions.size() == 1) {
				configureSSCWorkspaceFilter(spec, true);
			} else if (!hasSSCActivity) {
				configureSSCWorkspaceFilter(spec, false);
			}
			break;
		case ArConstants.PLEDGES_TYPE:
			// only pledges are allowed so far, no special rule
			break;
		}	
	}
	
	/**
	 * Configures SSC workspaces to be the only one selected or excluded from the report
	 * 
	 * @param spec 	report specification
	 * @param add 	has to be configured to true if only SSC workspaces are selected, 
	 * 				and to false if they are excluded  
	 */
	public static void configureSSCWorkspaceFilter(ReportSpecificationImpl spec, boolean add) {
		List<AmpTeam> sscWorkspaces = TeamUtil.getAllSSCWorkspaces();
		if (sscWorkspaces.size() == 0) {
			logger.error("Cannot configure SSC Workspace filter, no SSC Workspace found");
			return;
		}
		List<String> sscWorkspacesIds = new ArrayList<String>(sscWorkspaces.size());
		for (AmpTeam sscWs : sscWorkspaces) {
			sscWorkspacesIds.add(sscWs.getIdentifier().toString());
		}
		MondrianReportFilters filters = (MondrianReportFilters) spec.getFilters();
		if (filters == null) {
			AmpFiscalCalendar calendar = spec.getSettings() == null ? null : spec.getSettings().getCalendar();
			filters = new MondrianReportFilters(calendar);
			spec.setFilters(filters);
		}
		filters.addFilterRule(new ReportColumn(ColumnConstants.TEAM), new FilterRule(sscWorkspacesIds, add));
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
							fundingColumn = new ReportOutputColumn(column, fundingColumn, null, null);
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
	 * @return JsonBean with errors or null if no error
	 */
	public static final JsonBean validateReportConfig(JsonBean formParams,
			boolean isCustom) {
        List<ApiErrorMessage> errors = new ArrayList<ApiErrorMessage>();
		// validate the name
		if (isCustom && StringUtils.isBlank(formParams.getString(EPConstants.REPORT_NAME))) {
            errors.add(ReportErrors.REPORT_NAME_REQUIRED);
		}
		
		// validate the columns
        ApiErrorMessage err = validateList("columns", (List<String>) formParams.get(EPConstants.ADD_COLUMNS),
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
		
		// validate the project types
		err = validateList("project types", (List<String>) formParams.get(EPConstants.PROJECT_TYPE),
				ActivityType.STR_VALUES, false);
		if (err != null) errors.add(err);
		
		// validate the report type
		err = validateReportType(formParams);
		if (err != null) errors.add(err);
		if(errors.size()>0){
			return ApiError.toError(errors);
		}else{
			return null;
		}
	}
	
	private static ApiErrorMessage validateList(String listName, List<String> values,
			Collection<String> allowedValues, boolean isMandatory) {
		if (values == null || values.size() == 0) {
			if (isMandatory) {
                return new ApiErrorMessage(ReportErrors.LIST_NAME_REQUIRED.id,
                        ReportErrors.LIST_NAME_REQUIRED.description, listName);
            }
		} else {
			List<String> copy = new ArrayList<String>(values);
			copy.removeAll(allowedValues);
			if (copy.size() > 0) {
                return new ApiErrorMessage(ReportErrors.LIST_INVALID.id, ReportErrors.LIST_INVALID.description,
                        listName);
            }
		}
		return null;
	}
	
	private static ApiErrorMessage validateReportType(JsonBean config) {
		String reportType = EndpointUtils.getSingleValue(config, 
				EPConstants.REPORT_TYPE, EPConstants.DEFAULT_REPORT_TYPE);
		Integer reportTypeId = EPConstants.REPORT_TYPE_ID_MAP.get(reportType);
		if (reportTypeId == null) {
			return new ApiErrorMessage(ReportErrors.REPORT_TYPE_INVALID, config.getString(EPConstants.REPORT_TYPE));
		}
		List<String> activityTypes = (List<String>) config.get(EPConstants.PROJECT_TYPE);
		if (activityTypes != null) {
			if (activityTypes.size() == 0) {
				return ReportErrors.REPORT_TYPE_REQUIRED;
			}
			if (!EPConstants.REPORT_TYPE_ACTIVITY_MAP.get(reportType).containsAll(activityTypes)) {
				return new ApiErrorMessage(ReportErrors.ACTIVITY_TYPE_LIST_INVALID, activityTypes.toString());
			}
		}
		return null;
	}
	
	private static void setOtherOptions(ReportSpecificationImpl spec, JsonBean formParams) {
		Boolean doRowTotals = (Boolean) formParams.get(EPConstants.DO_ROW_TOTALS);
		if (doRowTotals != null) {
			spec.setCalculateRowTotals(doRowTotals);
		}
		
		Boolean showEmptyRows = (Boolean) formParams.get(EPConstants.SHOW_EMPTY_ROWS);
		if (showEmptyRows != null) {
			spec.setDisplayEmptyFundingRows(showEmptyRows);
		}
		
		Boolean showEmptyColumnGroups = (Boolean) formParams.get(EPConstants.SHOW_EMPTY_COLUMNS);
		if (showEmptyColumnGroups != null) {
			spec.setDisplayEmptyFundingColumns(showEmptyColumnGroups);
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
	
	/**
	 * Last viewed reports are kept track for the user. This method allows to add a report id into
	 * the 'last viewed' report list.
	 * 
	 * @param session, the session where the last viewed reports are kept
	 * @param ampReportId, the id of the report to add as 'last viewed'
	 */
	public static void addLastViewedReport(HttpSession session, Long ampReportId) {
		if (session.getAttribute(Constants.LAST_VIEWED_REPORTS) == null) {
			Comparator<AmpReports> ampReportsComparator = new Comparator<AmpReports>() {
				public int compare(AmpReports a, AmpReports b) {
					return a.getAmpReportId().compareTo(b.getAmpReportId());
				}
			};
			session.setAttribute(Constants.LAST_VIEWED_REPORTS, new BoundedList<AmpReports>(5, ampReportsComparator));
		}

		BoundedList<AmpReports> bList = (BoundedList<AmpReports>) session.getAttribute(Constants.LAST_VIEWED_REPORTS);
		AmpReports report = (AmpReports) PersistenceManager.getSession().get(AmpReports.class, ampReportId);
		if ((report != null) && (!report.getDrilldownTab()))
			bList.add(report);

	}

	public static AmpReports getAmpReportFromSession(Integer reportToken) {
		MaxSizeLinkedHashMap<Integer, AmpReports> reportsList = (MaxSizeLinkedHashMap<Integer, AmpReports>) TLSUtils.getRequest().getSession().getAttribute("reportStack");
		if (reportsList == null) {
			throw new WebApplicationException(Response.Status.NO_CONTENT);
		}
		
		AmpReports ampReport = reportsList.get(reportToken);
		if (ampReport == null) {
			throw new WebApplicationException(Response.Status.NO_CONTENT);
		}
		return ampReport;
	}
	
	/**
	 * Retrieves AmpGlobalSettings from request
	 * @param object
	 * @return AmpReports
	 */
	public static AmpReports getAmpReports(AmpReports ampReport,LinkedHashMap<String, Object> object,HttpServletRequest httpRequest) {
		
        TeamMember teamMember = (TeamMember) httpRequest.getSession().getAttribute(Constants.CURRENT_MEMBER);
        
        AmpTeamMember ampTeamMember = null;
        if (teamMember != null) {
        	ampTeamMember = TeamUtil.getAmpTeamMember(teamMember.getMemberId());
        }
        Collection<AmpColumns> availableCols	= AdvancedReportUtil.getColumnList();
        Collection<AmpMeasures> availableMeas	= AdvancedReportUtil.getMeasureList();

        ampReport.setType(Long.valueOf(String.valueOf(object.get("type"))));
        ampReport.setDrilldownTab(Boolean.valueOf(String.valueOf(object.get("drillDownTab"))));
        String newName = String.valueOf(object.get("name"));        

        boolean updatingPublishedDate = ampReport.getPublicReport() == null || (!ampReport.getPublicReport()); // report was NOT public but is public now
        if (updatingPublishedDate){
           ampReport.setPublishedDate(new Date(System.currentTimeMillis()));
        }

        ampReport.setUpdatedDate( new Date(System.currentTimeMillis()) );
        
        ampReport.setName(newName);
        ampReport.setWorkspaceLinked(Boolean.valueOf(String.valueOf(object.get("workspaceLinked"))) );
        ampReport.setAlsoShowPledges(Boolean.valueOf(String.valueOf(object.get("alsoShowPledges"))) );
        ampReport.setOwnerId(ampTeamMember);
        ampReport.setHideActivities( Boolean.valueOf(String.valueOf(object.get("hideActivities"))) );
        ampReport.setOptions(String.valueOf(object.get("reportPeriod")) );
        ampReport.setReportDescription( String.valueOf(object.get("reportDescription")) );
        ampReport.setPublicReport(Boolean.valueOf(String.valueOf(object.get("publicReport"))));
        ampReport.setReportCategory(CategoryManagerUtil.getAmpCategoryValueFromDb(Long.valueOf(String.valueOf(object.get("reportCategory")))));
		
        ampReport.setAllowEmptyFundingColumns( Boolean.valueOf(String.valueOf(object.get("allowEmptyFundingColumns"))));
        ampReport.setBudgetExporter(Boolean.valueOf(String.valueOf(object.get("budgetExporter"))));

        ampReport.setColumns( new HashSet<AmpReportColumn>() );
        ampReport.setHierarchies( new HashSet<AmpReportHierarchy>() );
        ampReport.setMeasures( new HashSet<AmpReportMeasures>() );

		if (object.get("columns")!=null) {
			ArrayList<String> columns = (ArrayList<String>) object.get("columns");
			int i = 1;
			for (String column : columns) {
				ampReport.getColumns().add(ampReportColumnForColName(column.toString() , i++));
			}
		}
		
		AmpCategoryValue level1	= CategoryManagerUtil.getAmpCategoryValueFromDb( CategoryConstants.ACTIVITY_LEVEL_KEY , 0L);
		if (object.get("hierarchies")!=null) {
			ArrayList<String> hierarchies = (ArrayList<String>) object.get("hierarchies");
			int i = 1;
			for (String hierarchy : hierarchies) {
				ampReport.getHierarchies().add(ampReportHierarchyForColName(hierarchy.toString() , level1));
			}
		}

		if (object.get("measures")!=null) {
			ArrayList<String> measures = (ArrayList<String>) object.get("measures");
			int i = 1;
			for (String measure : measures) {
				ampReport.getMeasures().add(ampReportMeasureForColName(measure.toString() , i++));
			}
		}
				
        if (ampReport.getOwnerId() == null)
            throw new RuntimeException("should not save a report without an ownerId!");

        if (ampReport.getHideActivities() == null)
            throw new RuntimeException("should not save a report with a null hideActivities");

        AdvancedReportUtil.saveReport(ampReport, ampTeamMember.getAmpTeam().getAmpTeamId(), teamMember.getMemberId(), teamMember.getTeamHead() );

		return ampReport;
	}

	private void addFields(Long[] sourceVector, Collection<?> availableFields, Collection container,
			Class<?> reportFieldClass, AmpCategoryValue level) throws Exception {
		if (sourceVector == null)
			return;
		for (int i = 0; i < sourceVector.length; i++) {
			Object reportField = reportFieldClass.newInstance();
			Object[] param1 = new Object[1];
			param1[0] = level;
			invokeSetterForBeanPropertyWithAnnotation(reportField, Level.class, param1);
			// rc.setLevel(level);
			Object[] param2 = new Object[1];
			param2[0] = new Long(i + 1);
			invokeSetterForBeanPropertyWithAnnotation(reportField, Order.class, param2);
			// rc.setOrderId(""+i);

			Iterator<?> iter = availableFields.iterator();
			boolean foundCol = false;
			while (iter.hasNext()) {
				Object field = iter.next();
				if (sourceVector[i].equals(invokeGetterForBeanPropertyWithAnnotation(field, Identificator.class,
						new Object[0]))) {
					Object[] param3 = new Object[1];
					param3[0] = field;
					invokeSetterForBeanPropertyWithAnnotation(reportField, ColumnLike.class, param3);
					foundCol = true;
					break;
				}
			}
			if (foundCol)
				container.add(reportField);
		}
	}

    public static Object invokeGetterForBeanPropertyWithAnnotation (Object beanObj, Class annotationClass, Object [] params ) throws Exception {
        Class myClass		= beanObj.getClass();
        Field[] fields		= myClass.getDeclaredFields();
        for (int i=0; i<fields.length; i++) {
            if ( fields[i].getAnnotation(annotationClass) != null) {
                PropertyDescriptor beanProperty	= new PropertyDescriptor(fields[i].getName(), myClass);
                return beanProperty.getReadMethod().invoke(beanObj, params);
            }
        }
        throw new IntrospectionException("No property was found in bean of class '" + myClass.getCanonicalName() +
                "' with annotation '" + annotationClass.getCanonicalName()
                + "'");
    }
	
    public static void invokeSetterForBeanPropertyWithAnnotation (Object beanObj, Class annotationClass, Object [] params ) throws Exception {
        Class myClass		= beanObj.getClass();
        Field[] fields		= myClass.getDeclaredFields();
        for (int i=0; i<fields.length; i++) {
            if ( fields[i].getAnnotation(annotationClass) != null) {
                PropertyDescriptor beanProperty	= new PropertyDescriptor(fields[i].getName(), myClass);
                beanProperty.getWriteMethod().invoke(beanObj, params);
                return;
            }
        }
        throw new IntrospectionException("No property was found in bean of class '" + myClass.getCanonicalName() +
                "' with annotation '" + annotationClass.getCanonicalName()
                + "'");
    }
    
	public static AmpReportColumn ampReportColumnForColName(String colName, long order) {
		AmpColumns col = (AmpColumns) PersistenceManager.getSession().createQuery("FROM " + AmpColumns.class.getName() + " c WHERE c.columnName=:colName").setString("colName", colName).uniqueResult();
		if (col == null)
			throw new RuntimeException("column with name <" + colName + "> not found!");
		
		AmpReportColumn res = new AmpReportColumn();
		res.setColumn(col);
		res.setOrderId(order);
		return res;
	}
    
	public static AmpReportHierarchy ampReportHierarchyForColName(String colName, AmpCategoryValue level) {
		AmpColumns col = (AmpColumns) PersistenceManager.getSession().createQuery("FROM " + AmpColumns.class.getName() + " c WHERE c.columnName=:colName").setString("colName", colName).uniqueResult();
		if (col == null)
			throw new RuntimeException("column with name <" + colName + "> not found!");
		
		AmpReportHierarchy res = new AmpReportHierarchy();
		res.setColumn(col);
		res.setLevel(level);
		res.setLevelId(level.getId());
		return res;
	}    
	public static AmpReportMeasures ampReportMeasureForColName(String colName, long order) {
		AmpMeasures col = (AmpMeasures) PersistenceManager.getSession().createQuery("FROM " + AmpMeasures.class.getName() + " c WHERE c.type = 'A' and c.measureName=:colName").setString("colName", colName).uniqueResult();
		if (col == null)
			throw new RuntimeException("column with name <" + colName + "> not found!");
		
		AmpReportMeasures res = new AmpReportMeasures();
		res.setMeasure(col);
		res.setOrderId(order);
		return res;
	}
	
}