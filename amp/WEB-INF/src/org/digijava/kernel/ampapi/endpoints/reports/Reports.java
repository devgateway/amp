package org.digijava.kernel.ampapi.endpoints.reports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportEnvironment;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.reports.ReportAreaMultiLinked;
import org.dgfoundation.amp.reports.ReportPaginationCacher;
import org.dgfoundation.amp.reports.ReportPaginationUtils;
import org.dgfoundation.amp.reports.mondrian.MondrianReportFilters;
import org.dgfoundation.amp.reports.mondrian.MondrianReportGenerator;
import org.dgfoundation.amp.reports.mondrian.converters.AmpReportsToReportSpecification;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.kernel.ampapi.endpoints.util.JSONResult;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.endpoints.util.ReportMetadata;
import org.digijava.kernel.ampapi.mondrian.util.MondrianUtils;
import org.digijava.kernel.ampapi.saiku.SaikuGeneratedReport;
import org.digijava.kernel.ampapi.saiku.SaikuReportArea;
import org.digijava.module.aim.action.ReportsFilterPicker;
import org.digijava.module.aim.ar.util.FilterUtil;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpDesktopTabSelection;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.form.ReportsFilterPickerForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.saiku.olap.query2.ThinQuery;
import org.saiku.web.rest.objects.resultset.QueryResult;
import org.saiku.web.rest.util.RestUtil;

/***
 * 
 * @author
 * 
 */

@Path("data")
public class Reports {

	private static final String DEFAULT_CATALOG_NAME = "AMP";
	private static final String DEFAULT_CUBE_NAME = "Donor Funding";
	private static final String DEFAULT_UNIQUE_NAME = "[amp].[AMP].[AMP].[Donor Funding]";
	private static final String DEFAULT_QUERY_NAME = "XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX";
	private static final String DEFAULT_CONNECTION_NAME = "amp";
	private static final String DEFAULT_SCHEMA_NAME = "AMP";

	protected static final Logger logger = Logger.getLogger(Reports.class);

	@Context
	private HttpServletRequest httpRequest;

	@GET
	@Path("/report/{report_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public final JSONResult getReport(@PathParam("report_id") Long reportId) {
		AmpReports ampReport = DbUtil.getAmpReport(reportId);

		// TODO: for now we do not translate other types of reports than Donor
		// Type reports (hide icons for non-donor-type reports?)
		ReportSpecificationImpl spec = null;
		try {
			spec = AmpReportsToReportSpecification.convert(ampReport);
		} catch (AMPException e1) {
			JSONResult result = new JSONResult();
			result.setErrorMessage(e1.getMessage());
			return result;
		}

		JSONResult result = new JSONResult();
		ReportMetadata metadata = new ReportMetadata();
		metadata.setReportSpec(spec);
		metadata.setCatalog(DEFAULT_CATALOG_NAME);
		metadata.setCube(DEFAULT_CUBE_NAME);
		metadata.setUniqueName(DEFAULT_UNIQUE_NAME);
		metadata.setQueryName(DEFAULT_QUERY_NAME);
		metadata.setName(ampReport.getName());
		metadata.setConnection(DEFAULT_CONNECTION_NAME);
		metadata.setSchema(DEFAULT_SCHEMA_NAME);

		result.setReportMetadata(metadata);
		return result;
	}

	@GET
	@Path("/report/{report_id}/result")
	@Produces(MediaType.APPLICATION_JSON)
	public final GeneratedReport getReportResult(@PathParam("report_id") Long reportId) {
		// TODO: for now we do not translate other types of reports than Donor
		// Type reports (hide icons for non-donor-type reports?)
		ReportSpecificationImpl spec = ReportsUtil.getReport(reportId);
		return EndpointUtils.runReport(spec);
	}

	/**
	 * Retrieves the result for the specified reportId and a given page number
	 * 
	 * @param reportId
	 *            - report ID
	 * @param page
	 *            - page number, starting from 1. Use 0 to retrieve only
	 *            pagination information, without any records
	 * @param regenerate
	 *            - set to true for all first access and any changes and to
	 *            false for consequent page navigation
	 * @return ReportArea result for the requested page
	 */
	@POST
	@Path("/report/{report_id}/result/jqGrid")
	@Consumes({ "application/x-www-form-urlencoded,text/plain,text/html" })
	@Produces(MediaType.APPLICATION_JSON)
	@ApiMethod(ui = false, name = "reportresultgrid")
	public final JsonBean getReportResultByPage(@PathParam("report_id") Long reportId, MultivaluedMap<String, String> formParams) {
		JsonBean result = new JsonBean();

		int page = Integer.valueOf(EndpointUtils.getSingleValue(formParams, "page", "0"));
		boolean regenerate = Boolean.valueOf(EndpointUtils.getSingleValue(formParams, "regenerate", "true"));

		List extraColumns = new ArrayList<String>();
		extraColumns.add("Activity Id");
		extraColumns.add("Approval Status");
		formParams.put("add_columns", extraColumns);

		int recordsPerPage = ReportPaginationUtils.getRecordsNumberPerPage();
		int start = (page - 1) * recordsPerPage;
		ReportAreaMultiLinked[] areas = null;
		// extract report areas for pagination
		if (regenerate) {
			ReportSpecificationImpl spec = ReportsUtil.getReport(reportId);
			JsonBean filters = prepareParameters(formParams);
			MondrianReportFilters mondrianReportFilters = FilterUtils.getApiColumnFilter(((LinkedHashMap<String, Object>) filters.any()));
			if (mondrianReportFilters != null) {
				spec.setFilters(mondrianReportFilters);
			}
			ReportsUtil.update(spec, formParams);
			GeneratedReport generatedReport = EndpointUtils.runReport(spec);
			areas = ReportPaginationUtils.cacheReportAreas(reportId, generatedReport);
			result.set("headers", generatedReport.leafHeaders);
		} else
			areas = ReportPaginationCacher.getReportAreas(reportId);

		ReportArea pageArea = ReportPaginationUtils.getReportArea(areas, start, recordsPerPage);
		int totalPageCount = ReportPaginationUtils.getPageCount(areas, recordsPerPage);
		result.set("page", new JSONReportPage(pageArea, recordsPerPage, page, totalPageCount, areas.length));
		return result;
	}

	@GET
	@Path("/tabs")
	@Produces(MediaType.APPLICATION_JSON)
	public final List<JSONTab> getTabs() {

		TeamMember tm = (TeamMember) httpRequest.getSession().getAttribute(Constants.CURRENT_MEMBER);
		AmpTeamMember ampTeamMember = TeamUtil.getAmpTeamMember(tm.getMemberId());
		if (ampTeamMember != null) {
			return getDefaultTabs(ampTeamMember);
		} else {
			return getPublicTabs();
		}
	}

	private List<JSONTab> getDefaultTabs(AmpTeamMember ampTeamMember) {
		List<JSONTab> tabs = new ArrayList<JSONTab>();

		// Look for the Default Tab and add it visible to the list
		AmpApplicationSettings ampAppSettings = DbUtil.getTeamAppSettings(ampTeamMember.getAmpTeam().getAmpTeamId());
		AmpReports defaultTeamReport = ampAppSettings.getDefaultTeamReport();
		if (defaultTeamReport != null) {
			tabs.add(ReportsUtil.convert(defaultTeamReport, true));
		}

		// Get the visible tabs of the currently logged user
		if (ampTeamMember.getDesktopTabSelections() != null && ampTeamMember.getDesktopTabSelections().size() > 0) {
			TreeSet<AmpDesktopTabSelection> sortedSelection = new TreeSet<AmpDesktopTabSelection>(AmpDesktopTabSelection.tabOrderComparator);
			sortedSelection.addAll(ampTeamMember.getDesktopTabSelections());
			Iterator<AmpDesktopTabSelection> iter = sortedSelection.iterator();

			while (iter.hasNext()) {
				AmpReports report = iter.next().getReport();
				JSONTab tab = new JSONTab(report.getAmpReportId(), report.getName(), true);
				tabs.add(tab);
			}
		}

		// Get the rest of the tabs that aren't visible on first instance
		List<AmpReports> userActiveTabs = TeamUtil.getAllTeamReports(ampTeamMember.getAmpTeam().getAmpTeamId(), true, null, null, true,
				ampTeamMember.getAmpTeamMemId(), null, null);
		Iterator<AmpReports> iter = userActiveTabs.iterator();

		while (iter.hasNext()) {
			AmpReports report = iter.next();
			JSONTab tab = new JSONTab(report.getAmpReportId(), report.getName(), false);
			boolean found = false;
			Iterator<JSONTab> iTabs = tabs.iterator();
			while (iTabs.hasNext() && !found) {
				JSONTab auxTab = iTabs.next();
				if (auxTab.getId() == tab.getId()) {
					found = true;
				}
			}
			if (!found) {
				tabs.add(tab);
			}
		}
		// tabs.addAll(userActiveTabs);
		return tabs;
	}

	private List<JSONTab> getPublicTabs() {
		List<JSONTab> tabs = new ArrayList<JSONTab>();
		return tabs;
	}

	/**
	 * Gets the result for the specified reportId for Saiku UI
	 * 
	 * @param reportId
	 *            - AMP report id
	 * @return QueryResult result converted for Saiku for the requested page
	 */

	@GET
	@Path("/saikureport/{report_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public final QueryResult getSaikuReportResult(@PathParam("report_id") Long reportId) {
		AmpReports ampReport = DbUtil.getAmpReport(reportId);
		// MondrianUtils.PRINT_PATH = "/home/simple";
		MondrianReportGenerator generator = new MondrianReportGenerator(SaikuReportArea.class, ReportEnvironment.buildFor(httpRequest),
				MondrianUtils.PRINT_PATH != null);
		SaikuGeneratedReport report = null;
		try {
			ReportSpecificationImpl spec = AmpReportsToReportSpecification.convert(ampReport);
			report = (SaikuGeneratedReport) generator.executeReport(spec);
			System.out.println("[" + spec.getReportName() + "] total report generation duration = " + report.generationTime + "(ms)");
		} catch (Exception e) {
			logger.error("Cannot execute report (" + ampReport + ")", e);
			String error = ExceptionUtils.getRootCauseMessage(e);
			return new QueryResult(error);
		}

		// Adjust width for temporary column
		report.cellDataSet.setWidth(report.cellDataSet.getWidth() - 1);
		// report.cellDataSet.setLeftOffset(report.cellDataSet.getLeftOffset()-1);

		return RestUtil.convert(report.cellDataSet);
	}

	@POST
	@Path("/saikureport/{report_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public final QueryResult getSaiku3ReportResult(@PathParam("report_id") Long reportId) {
		QueryResult result = getSaikuReportResult(reportId);
		result.setQuery(new ThinQuery());
		return result;
	}

	@GET
	@Path("/report/{report_id}/settings/")
	@Produces(MediaType.APPLICATION_JSON)
	public final Object getSettings(@PathParam("report_id") Long reportId) {
		AmpARFilter arFilter = FilterUtil.buildFilter(null, reportId);
		ReportsFilterPickerForm oldFilterForm = new ReportsFilterPickerForm();
		FilterUtil.populateForm(oldFilterForm, arFilter, reportId);
		ReportsFilterPicker.fillSettingsFormDropdowns(oldFilterForm);
		Map<String, Object> settings = new HashMap<String, Object>();
		settings.put("decimalSeparators", oldFilterForm.getAlldecimalSymbols());
		settings.put("decimalSymbols", oldFilterForm.getAlldecimalSymbols());
		settings.put("groupSeparators", oldFilterForm.getAllgroupingseparators());
		settings.put("amountInThousands", oldFilterForm.getAmountinthousands());
		settings.put("selectedDecimalPlaces", oldFilterForm.getCustomDecimalPlaces());
		settings.put("selectedDecimalSeparator", oldFilterForm.getCustomDecimalSymbol());
		settings.put("selectedGroupSeparator", oldFilterForm.getCustomGroupCharacter());
		settings.put("selectedUseGroupingSeparator", oldFilterForm.getCustomUseGrouping());
		// settings.put("calendars", oldFilterForm.getCalendars());
		// settings.put("currencies", oldFilterForm.getCurrencies());
		return settings;
	}

	private JsonBean prepareParameters(MultivaluedMap<String, String> queryParameters) {
		JsonBean jsonBean = new JsonBean();
		Iterator<String> it = queryParameters.keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			// Ignore non filter parameters.
			if (key.indexOf("filters[") == 0) {
				String newKey = key.replace("filters[", "");
				// Find collection for donors, activities, etc (not composed
				// objects).
				if (newKey.indexOf("[]") >= 0) {
					newKey = newKey.replace("][]", "");
					List<Integer> values = new LinkedList<Integer>();
					Iterator<String> iValues = queryParameters.get(key).iterator();
					while (iValues.hasNext()) {
						values.add(Integer.valueOf(iValues.next()));
					}
					jsonBean.set(newKey, values);
				} else {
					// TODO: Implement for filters[Years][endYear]
				}
			}
		}
		return jsonBean;
	}

}
