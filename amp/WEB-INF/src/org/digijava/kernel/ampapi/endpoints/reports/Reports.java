package org.digijava.kernel.ampapi.endpoints.reports;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import mondrian.util.Pair;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.dbentity.AmpFilterData;
import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportRenderWarning;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.nireports.amp.AmpReportsSchema;
import org.dgfoundation.amp.nireports.amp.NiReportsGenerator;
import org.dgfoundation.amp.nireports.schema.NiReportsSchema;
import org.dgfoundation.amp.reports.PartialReportArea;
import org.dgfoundation.amp.reports.ReportPaginationUtils;
import org.dgfoundation.amp.reports.mondrian.MondrianReportFilters;
import org.dgfoundation.amp.reports.mondrian.MondrianReportUtils;
import org.dgfoundation.amp.reports.mondrian.converters.AmpReportsToReportSpecification;
import org.dgfoundation.amp.reports.mondrian.converters.MondrianReportFiltersConverter;
import org.dgfoundation.amp.reports.saiku.export.AMPPdfExport;
import org.dgfoundation.amp.reports.saiku.export.AMPReportCsvExport;
import org.dgfoundation.amp.reports.saiku.export.AMPReportExcelExport;
import org.dgfoundation.amp.reports.saiku.export.AMPReportExportConstants;
import org.dgfoundation.amp.reports.saiku.export.ReportGenerationInfo;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsConstants;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.kernel.ampapi.endpoints.util.JSONResult;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.endpoints.util.ReportMetadata;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.action.ReportsFilterPicker;
import org.digijava.module.aim.ar.util.FilterUtil;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpContentTranslation;
import org.digijava.module.aim.dbentity.AmpDesktopTabSelection;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.form.ReportsFilterPickerForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.translation.util.MultilingualInputFieldValues;
import org.hibernate.Session;
import org.saiku.olap.dto.resultset.AbstractBaseCell;
import org.saiku.olap.dto.resultset.CellDataSet;

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

	private static final String IN_MEMORY = "IN_MEMORY";
	private static final String SAVED = "SAVED";

	protected static final Logger logger = Logger.getLogger(Reports.class);

	@Context
	private HttpServletRequest httpRequest;

	@GET
	@Path("/report/{report_id}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public final JSONResult getReport(@PathParam("report_id") Long reportId) {
		JSONResult report = getReport(DbUtil.getAmpReport(reportId));
		report.getReportMetadata().setReportType(SAVED);
		report.getReportMetadata().setReportIdentifier(reportId.toString());
		return report;
	}

	@GET
	@Path("/report/run/{report_token}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public final JSONResult getReport(@PathParam("report_token") Integer reportToken) {
		JSONResult report = getReport(ReportsUtil.getAmpReportFromSession(reportToken));
		report.getReportMetadata().setReportType(IN_MEMORY);
		report.getReportMetadata().setReportIdentifier(reportToken.toString());
		return report;
	}	
	
	private JSONResult getReport(AmpReports ampReport) {
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
		metadata.setSettings(SettingsUtils.getReportSettings(spec));
		metadata.setName(ampReport.getName());
		metadata.setRecordsPerPage(ReportPaginationUtils.getRecordsNumberPerPage());
		
		//Properties that make a Saiku Query. Might be removed later
		metadata.setCatalog(DEFAULT_CATALOG_NAME);
		metadata.setCube(DEFAULT_CUBE_NAME);
		metadata.setUniqueName(DEFAULT_UNIQUE_NAME);
		metadata.setQueryName(DEFAULT_QUERY_NAME);
		metadata.setConnection(DEFAULT_CONNECTION_NAME);
		metadata.setSchema(DEFAULT_SCHEMA_NAME);

		result.setReportMetadata(metadata);
		
		//Translate column names.
		/*Set<ReportColumn> translatedColumns = new LinkedHashSet<ReportColumn>();  
		Iterator<ReportColumn> iCols = metadata.getReportSpec().getColumns().iterator();
		while(iCols.hasNext()) {
			ReportColumn auxCol = iCols.next();
			String translatedName = TranslatorWorker.translateText(auxCol.getEntityName());
			auxCol = new ReportColumn(translatedName, auxCol.getEntityType());
			translatedColumns.add(auxCol);			
		}
		metadata.getReportSpec().setColumns(translatedColumns);
		
		List<ReportMeasure> translatedMeasures = new ArrayList<ReportMeasure>(); 
		Iterator<ReportMeasure> iMs = metadata.getReportSpec().getMeasures().iterator();
		while(iMs.hasNext()) {
			ReportMeasure auxMeasure = iMs.next();
			String translatedName = TranslatorWorker.translateText(auxMeasure.getMeasureName());
			auxMeasure = new ReportMeasure(translatedName, auxMeasure.getEntityType());
			translatedMeasures.add(auxMeasure);
		}
		metadata.getReportSpec().setMeasures(translatedMeasures);*/
		return result;
	}
	
	@GET
	@Path("/nireport/{report_id}")
	@Produces(MediaType.TEXT_HTML + ";charset=utf-8")
	public String generateRenderedReport(@PathParam("report_id") Long reportId) {
		ReportSpecificationImpl spec = ReportsUtil.getReport(reportId);
		return AmpReportsSchema.getRenderedReport(spec);
	}
	
	@GET
	@Path("/nireport-to-amp-reports-api/{report_id}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Deprecated
	//NIREPORTS: temporarily for debugging purpose only
	public GeneratedReport geteneratedReport(@PathParam("report_id") Long reportId) {
		ReportSpecificationImpl spec = ReportsUtil.getReport(reportId);
		NiReportsGenerator generator = new NiReportsGenerator(AmpReportsSchema.getInstance(), PartialReportArea.class);
		return generator.executeReport(spec);
	}
	
	@GET
	@Path("/report/{report_id}/result")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public final GeneratedReport getReportResult(@PathParam("report_id") Long reportId) {
		ReportSpecificationImpl spec = ReportsUtil.getReport(reportId);
		return EndpointUtils.runReport(spec);
	}
	
	@POST
	@Path("/report/custom/paginate")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	/**
	 * Generates a custom report.  
	 * 
	 * @param formParams {@link ReportsUtil#getReportResultByPage form parameters}
	 * @return JsonBean result for the requested page and pagination information
	 * @see ReportsUtil#getReportResultByPage
	 */
	public final JsonBean getCustomReport(JsonBean formParams) {
		JsonBean result = ReportsUtil.validateReportConfig(formParams, true);
		if (result != null) {
			return result;
		}
		// we need reportId only to store the report result in cache
		Long reportId = (long) formParams.getString(EPConstants.REPORT_NAME).hashCode();
		formParams.set(EPConstants.IS_CUSTOM, true);
		return getReportResultByPage(formParams, reportId);
	}
	
	/**
	 * Retrieves report data for the specified reportId and a given page number
	 *  
	 * @param reportId    report Id
	 * @param formParams  {@link ReportsUtil#getReportResultByPage form parameters}
	 * @return JsonBean result for the requested page and pagination information
	 * @see ReportsUtil#getReportResultByPage
	 */
	@POST
	@Path("/report/{report_id}/paginate")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public final JsonBean getReportResultByPage(JsonBean formParams,
			@PathParam("report_id") Long reportId) {
		return ReportsUtil.getReportResultByPage(reportId, formParams, false);
	}
	
	@POST
	@Path("/report/{report_id}/result/jqGrid")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	/**
	 * Provides paginated result for tabs.  
	 * @see {@link getReportResultByPage} for more details
	 */
	public final JsonBean getReportResultForTabGrid(JsonBean formParams, 
			@PathParam("report_id") Long reportId) {
		
		List<String> extraColumns = new ArrayList<String>();
		extraColumns.add(ColumnConstants.ACTIVITY_ID);
		extraColumns.add(ColumnConstants.APPROVAL_STATUS);
		extraColumns.add(ColumnConstants.DRAFT);
		extraColumns.add(ColumnConstants.TEAM_ID);
		formParams.set(EPConstants.ADD_COLUMNS, extraColumns);
		
		// Convert jqgrid sorting params into ReportUtils sorting params.
		if (formParams.getString("sidx") != null) {			
			formParams.set(EPConstants.SORTING, convertJQgridSortingParams(formParams));
		}
		
		// AMP-18516: Fix "page" parameter when is entered manually by user.
		if (formParams.get("page") instanceof String) {
			formParams.set("page", Integer.valueOf(formParams.get("page").toString()));
		}
		
		return getReportResultByPage(formParams, reportId);
	}
	
	@GET
	@Path("/tabs")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
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
			tabs.add(new JSONTab(defaultTeamReport.getAmpReportId(),true));
		}

		// Get the visible tabs of the currently logged user
		if (ampTeamMember.getDesktopTabSelections() != null && ampTeamMember.getDesktopTabSelections().size() > 0) {
			TreeSet<AmpDesktopTabSelection> sortedSelection = new TreeSet<AmpDesktopTabSelection>(AmpDesktopTabSelection.tabOrderComparator);
			sortedSelection.addAll(ampTeamMember.getDesktopTabSelections());
			for (AmpDesktopTabSelection adts:sortedSelection) {
				AmpReports report = adts.getReport();
				JSONTab tab = new JSONTab(report.getAmpReportId(), true);
				tabs.add(tab);
			}
		}

		// Get the rest of the tabs that aren't visible on first instance
		List<AmpReports> userActiveTabs = TeamUtil.getAllTeamReports(ampTeamMember.getAmpTeam().getAmpTeamId(), true, null, null, true,
				ampTeamMember.getAmpTeamMemId(), null, null);
		Iterator<AmpReports> iter = userActiveTabs.iterator();

		while (iter.hasNext()) {
			AmpReports report = iter.next();
			JSONTab tab = new JSONTab(report.getAmpReportId(), false);
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
	
	@POST
	@Path("/saikureport/{report_id}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public final JsonBean getSaikuReport(JsonBean queryObject, @PathParam("report_id") Long reportId, 
			@DefaultValue("false") @QueryParam ("nireport") Boolean asNiReport) {
		//asNiReport = true;  //TODO-Constantin: find a better way to do it :D
		ReportSpecificationImpl spec = ReportsUtil.getReport(reportId);
		if(spec == null){
			try {
				spec = AmpReportsToReportSpecification.convert(ReportsUtil.getAmpReportFromSession(reportId.intValue()));
			} catch (AMPException e) {
				logger.error("Cannot get report from session",e);
				throw new RuntimeException("Cannot restore report from session: " + reportId);
			}
		}
		
		// AMP-19189 - add columns used for coloring the project title and amp id (but not for summary reports).
		List<String> extraColumns = new ArrayList<String>();
		if (spec.getColumns().size() != spec.getHierarchies().size()) {
			extraColumns.add(ColumnConstants.ACTIVITY_ID);
			extraColumns.add(ColumnConstants.APPROVAL_STATUS);
			extraColumns.add(ColumnConstants.DRAFT);
			queryObject.set(EPConstants.ADD_COLUMNS, extraColumns);
		}
		
		JsonBean report = ReportsUtil.getReportResultByPage(reportId, 
				ReportsUtil.convertSaikuParamsToReports(queryObject), asNiReport);
		
		// Add data needed on Saiku UI.
		// TODO: Make a mayor refactoring on the js code so it doesnt need these extra parameters to work properly.
		JsonBean queryProperties = new JsonBean();
		queryProperties.set("properties", new ArrayList<String>());
		report.set("query", queryProperties);
		List<String> cellset = new ArrayList<String>();
		cellset.add("dummy");
		report.set("cellset", cellset);
		
		// Add some missing metadata when running through Rhino.
		report.set("columns", spec.getColumns());
		report.set("hierarchies", spec.getHierarchies());
		
		report.set("colorSettings", getColorSettings());
		
		// In caseIf this is a summarized report without hierarchies then we need to change the word 'constant' for 'Report
        // Totals' (translated).
        report.set("reportTotalsString", TranslatorWorker.translateText("Report Totals"));
        ReportsUtil.addLastViewedReport(httpRequest.getSession(), reportId);
		
		return report;
	}
	
	@POST
	@Path("/saikureport/run/{report_token}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public final JsonBean getSaikuReport(JsonBean formParams, @PathParam("report_token") String reportToken, 
			@DefaultValue("false") @QueryParam ("nireport") Boolean asNiReport) {
		//here we fetch the report by reportToken from session session
		formParams.set(EPConstants.IS_DYNAMIC, true);
		return getSaikuReport(formParams, new Long(reportToken), asNiReport);
	}

	
	
	private CellDataSet getPage(CellDataSet cellDataSet, Object pageParam) {
		//Assuming page is zero indexed
		AbstractBaseCell[][] result = cellDataSet.getCellSetBody();
		if(pageParam != null && result.length > 0) {
			int start = 0, end = 0, page = 0;
			page = (int)pageParam;
			int rpp = ReportPaginationUtils.getRecordsNumberPerPage();
			int pageLimit = rpp == 0 ? 0 : ((result.length + rpp - 1) / rpp - 1);
			page = Math.min(page,  pageLimit);
			start = rpp*page;
			end = (rpp*page+rpp);
			if(end > result.length) 
				end = result.length;
			AbstractBaseCell[][] pageResults = Arrays.copyOfRange(result, start, end);
			//Check hierarchical results that need the data
			//Take first row and check if there are null columns
			if (pageResults.length > 0) {
				AbstractBaseCell[] firstRow = pageResults[0];
				
				for (int i = 0; i < firstRow.length; i++) {
					AbstractBaseCell cell = firstRow[i];
					if(cell.getFormattedValue() == null) {
						//If it's null, get the formatted value from a previous row at the same level
						//from my current row index back
						for(int j = start; j >= 0; j--){
							AbstractBaseCell parentCell = result[j][i];
							if(parentCell.getFormattedValue() != null) {
								cell.setFormattedValue(parentCell.getFormattedValue());
								break;
							}
						}
					}
				}
				
				cellDataSet.setCellSetBody(pageResults);
			}
		}
		
		return cellDataSet;
	}
	@POST
	@Path("/saikureport/export/xls/{report_id}")
	@Produces({"application/vnd.ms-excel" })
	public final Response exportXlsSaikuReport(String query, @PathParam("report_id") Long reportId, 
			@DefaultValue("false") @QueryParam ("nireport") Boolean asNiReport) {
		return exportSaikuReport(query, DbUtil.getAmpReport(reportId), AMPReportExportConstants.XLSX, asNiReport);
	}
	@POST
	@Path("/saikureport/export/xls/run/{report_token}")
	@Produces({"application/vnd.ms-excel" })
	public final Response exportXlsSaikuReport(String query, @PathParam("report_token") Integer reportToken) {
		return exportInMemorySaikuReport(query,reportToken,AMPReportExportConstants.XLSX);
	}	
	@POST
	@Path("/saikureport/export/csv/{report_id}")
	@Produces({"text/csv"})
	public final Response exportCsvSaikuReport(String query, @PathParam("report_id") Long reportId, 
			@DefaultValue("false") @QueryParam ("nireport") Boolean asNiReport) {
		return exportSaikuReport(query, DbUtil.getAmpReport(reportId), AMPReportExportConstants.CSV, asNiReport);

	}
	@POST
	@Path("/saikureport/export/csv/run/{report_token}")
	@Produces({"text/csv"})
	public final Response exportCsvSaikuReport(String query, @PathParam("report_token") Integer reportToken,
			@DefaultValue("false") @QueryParam ("nireport") Boolean asNiReport) {

		return exportInMemorySaikuReport(query, reportToken,AMPReportExportConstants.CSV);
	}
	
	@POST
	@Path("/saikureport/export/pdf/{report_id}")
	@Produces({"application/pdf"})
	public final Response exportPdfSaikuReport(String query, @PathParam("report_id") Long reportId, 
			@DefaultValue("false") @QueryParam ("nireport") Boolean asNiReport) {

		return exportSaikuReport(query, DbUtil.getAmpReport(reportId), AMPReportExportConstants.PDF, asNiReport);
	}

	@POST
	@Path("/saikureport/export/pdf/run/{report_token}")
	@Produces({"application/pdf"})
	public final Response exportPdfSaikuReport(String query, @PathParam("report_token") Integer reportToken) {
		return exportInMemorySaikuReport(query, reportToken,AMPReportExportConstants.PDF);
	}

	private Response exportInMemorySaikuReport(String query, Integer reportToken,String reportType) {
		AmpReports ampReport=ReportsUtil.getAmpReportFromSession(reportToken);
		ampReport.setAmpReportId(reportToken.longValue());
		return exportSaikuReport(query, ReportsUtil.getAmpReportFromSession(reportToken), reportType,true);
	}
	
	public final Response exportSaikuReport(String query, AmpReports ampReport, String type, Boolean asNiReport) {
		return exportSaikuReport(query, ampReport, type, false, asNiReport);
	}
	
	public final Response exportSaikuReport(String query, AmpReports ampReport, String type, Boolean isDinamic, 
			Boolean asNiReport) {
		return exportSaikuReport(query, type, ampReport, isDinamic, asNiReport);
        
	}

	/**
	 * a very very very ugly and hacky function which only exists because the filters / settings API is f...ed up
	 * @param queryObject
	 * @param origReport
	 * @param ampReportId
	 * @param ampCurrencyCode
	 * @return
	 */
	protected ReportGenerationInfo changeReportCurrencyTo(JsonBean queryObject, ReportGenerationInfo origReport, 
			long ampReportId, String ampCurrencyCode, Boolean asNiReport) {
		System.out.println();
		JsonBean newQueryObject = queryObject.copy();
		LinkedHashMap<String, Object> newQueryModel = new LinkedHashMap<String, Object>((LinkedHashMap<String, Object>) queryObject.get("queryModel"));
		newQueryModel.put("regenerate", true);
		if (!newQueryModel.containsKey(EPConstants.SETTINGS))
			newQueryModel.put(EPConstants.SETTINGS, new LinkedHashMap<String, Object>());
		Map<String, Object> settings = (Map<String, Object>) newQueryModel.get(EPConstants.SETTINGS);
		settings.put(SettingsConstants.CURRENCY_ID, ampCurrencyCode);

		newQueryObject.set("queryModel", newQueryModel);
		
		JsonBean newResult = getSaikuReport(newQueryObject, ampReportId, asNiReport);
		ReportSpecification newReport = origReport.report; // sick and tired of shitcode 
		return new ReportGenerationInfo(newResult, origReport.type, newReport, newQueryModel, String.format(" - %s", ampCurrencyCode));
	}
	
	private Response exportSaikuReport(String query, String type, AmpReports ampReport, Boolean asNiReport) {
		return exportSaikuReport(query, type, ampReport, false, asNiReport);
	}
	
	private Response exportSaikuReport(String query, String type, AmpReports ampReport, Boolean isDinamic, 
			Boolean asNiReport) {
		try {
			logger.info("Starting export to " + type);
			String decodedQuery = java.net.URLDecoder.decode(query, "UTF-8");
			decodedQuery = decodedQuery.replace("query=", "");
			JsonBean queryObject = JsonBean.getJsonBeanFromString(decodedQuery);
			LinkedHashMap<String, Object> queryModel = (LinkedHashMap<String, Object>) queryObject.get("queryModel");
			
			queryModel.remove("page");
			queryModel.put("page", 0);
			queryModel.put("recordsPerPage", -1);
			queryModel.put("regenerate", true);
			queryModel.put(AMPReportExportConstants.EXCEL_TYPE_PARAM, queryObject.get(AMPReportExportConstants.EXCEL_TYPE_PARAM));
			if (isDinamic) {
				queryObject.set(EPConstants.IS_DYNAMIC, true);

			}
			logger.info("Obtain report result...");
			JsonBean result = getSaikuReport(queryObject, ampReport.getAmpReportId(), asNiReport);

			byte[] doc = null;
			String filename = "export";

			// We will use report settings to get the DecimalFormat in order to parse the formatted values
			logger.info("Obtain report implementation...");
			ReportSpecification report = null;
			if (!isDinamic) {
				report = ReportsUtil.getReport(ampReport.getAmpReportId());
			} else {
				// if the report is dynamic we need to load it from memory
				report = AmpReportsToReportSpecification
						.convert(ReportsUtil.getAmpReportFromSession(ampReport.getAmpReportId().intValue()));
			}
			
			if (report != null && !StringUtils.isEmpty(report.getReportName())) {
				filename = report.getReportName();
			}
			filename += "." + type;
			filename = filename.replaceAll(" ", "_");

			logger.info("Generate specific export...");
			switch (type) {
			case AMPReportExportConstants.XLSX: {
				ReportGenerationInfo report1 = new ReportGenerationInfo(result, AMPReportExportConstants.XLSX, report, queryModel, "");
				ReportGenerationInfo report2 = null;
				String secondCurrencyCode = queryModel.containsKey("secondCurrency") ? queryModel.get("secondCurrency").toString() : null;
				logger.error(String.format("setts 1 = %s, 2 = %s, secondCurrency=%s", queryModel.get("1"), queryModel.get("2"), secondCurrencyCode));
				if (secondCurrencyCode != null) {
					report2 = changeReportCurrencyTo(queryObject, report1, ampReport.getAmpReportId(), secondCurrencyCode, asNiReport);
				}
				doc = AMPReportExcelExport.generateExcel(report1, report2);
				break;
			}
			case AMPReportExportConstants.CSV: 
				doc = AMPReportCsvExport.generateCSV(result, AMPReportExportConstants.CSV, report, queryModel, ";");
				break;
			
			case AMPReportExportConstants.PDF:
				AMPPdfExport pdf = new AMPPdfExport();
				doc = pdf.pdf(result, AMPReportExportConstants.PDF, report, queryModel);
				break;
			}

			if (doc != null) {
				logger.info("Send export data to browser...");
				return Response.ok(doc, MediaType.APPLICATION_OCTET_STREAM)
						.header("content-disposition", "attachment; filename = " + filename)
						.header("content-length", doc.length).build();
			} else {
				throw new Exception("Empty response while exporting.");
			}
		} catch (Exception e) {
			logger.error("error while generating report", e);
			return Response.serverError().build();
		}
	}

	private JsonBean extractSettings(LinkedHashMap<String, Object> queryModel) {
		JsonBean config = new JsonBean();
		config.set("settings", queryModel.get("settings"));
		return config;
	}

	@GET
	@Path("/report/{report_id}/settings/")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public final Object getSettings(@PathParam("report_id") Long reportId) {
		AmpARFilter arFilter = FilterUtil.buildFilter(null, reportId);
		ReportsFilterPickerForm oldFilterForm = new ReportsFilterPickerForm();
		FilterUtil.populateForm(oldFilterForm, arFilter, reportId);
		ReportsFilterPicker.fillSettingsFormDropdowns(oldFilterForm, arFilter);
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
	
	@GET
	@Path("/report/columns")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public final Map<String, String> getAllowedColumns() {
		Map<String, String> columnToDisplayName = new HashMap<String, String>();
		Set<String> configurableColumns = MondrianReportUtils.getConfigurableColumns();
		for (String originalColumnName : configurableColumns) {
			columnToDisplayName.put(originalColumnName, TranslatorWorker.translateText(originalColumnName));
		}
		
		return columnToDisplayName;
	}
	
	@GET
	@Path("/report/measures")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public final Map<String, String> getAllowedMeasures() {
		Map<String, String> measuresToDisplayName = new HashMap<String, String>();
		Set<String> configurableMeasures = MondrianReportUtils.getConfigurableMeasures();
		for (String originalMeasureName : configurableMeasures) {
			measuresToDisplayName.put(originalMeasureName, TranslatorWorker.translateText(originalMeasureName));
		}
		
		return measuresToDisplayName;
	}
	
	
	@POST
	@Path("/report/saveTab/{report_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String saveTab(JsonBean formParams, @PathParam("report_id") Long reportId) {
		String message = null;
		try {
			// Open AmpReport.
			AmpReports report = DbUtil.getAmpReport(reportId);
			// AmpARFilter oldFilters = FilterUtil.buildFilter(null, reportId);
			AmpARFilter newFilters = null;

			// Convert json object back to the new format: MondrianReportFilters.
			if (formParams.get("filters") != null) {
				JsonBean filters = new JsonBean();
				LinkedHashMap<String, Object> requestFilters = (LinkedHashMap<String, Object>) formParams.get("filters");
				MondrianReportFilters mondrianReportFilters = null;
				if (requestFilters != null) {
					filters.any().putAll(requestFilters);
					mondrianReportFilters = FilterUtils.getFilters(filters,
							MondrianReportUtils.getCurrentUserDefaultFilters(null));

					// Transform back to legacy AmpARFilters.
					MondrianReportFiltersConverter converter = new MondrianReportFiltersConverter(mondrianReportFilters);
					newFilters = converter.buildFilters();
					// converter.mergeWithOldFilters(oldFilters);
					
					if (formParams.getString("sidx") != null && !formParams.getString("sidx").equals("")) {			
						formParams.set(EPConstants.SORTING, convertJQgridSortingParams(formParams));									
						logger.info(formParams.get(EPConstants.SORTING));
						newFilters.setSortByAsc(formParams.getString("sord").equals("asc") ? true : false);
						String column = ((Map) ((List) formParams.get(EPConstants.SORTING)).get(0)).get("columns")
								.toString();
						column = column.substring(column.indexOf("[") + 1, column.indexOf("]"));
						newFilters.setSortBy("/" + column);
					}
					
					if (formParams.get(EPConstants.SETTINGS) != null) {
						String currency = ((LinkedHashMap<String, Object>) formParams.get(EPConstants.SETTINGS)).get("1").toString();
						String calendar = ((LinkedHashMap<String, Object>) formParams.get(EPConstants.SETTINGS)).get("2").toString();
						newFilters.setCurrency(CurrencyUtil.getAmpcurrency(currency));
						newFilters.setCalendarType(FiscalCalendarUtil.getAmpFiscalCalendar(new Long(calendar)));
					}
					
					logger.info(newFilters);

					Set<AmpFilterData> fdSet = AmpFilterData.createFilterDataSet(report, newFilters);
					if (report.getFilterDataSet() == null)
						report.setFilterDataSet(fdSet);
					else {
						report.getFilterDataSet().clear();
						report.getFilterDataSet().addAll(fdSet);
					}
				}
			}
			
			Session session = PersistenceManager.getSession();			
			List<Map<String, String>> reportData = (List<Map<String, String>>) formParams.get("reportData");
			boolean emptyDefaultName = true;
            Locale defaultLang = SiteUtils.getDefaultLanguages(RequestUtils.getSite(httpRequest));
			for (Map<String, String> name : reportData) {
				if (defaultLang != null && defaultLang.getCode().equals(name.get("lang")) && !"".equals(name.get("name"))) {
					emptyDefaultName = false;
				}
			}
			if(!emptyDefaultName) {
				Map<String, AmpContentTranslation> translations = populateContentTranslations(reportData, reportId);			
				MultilingualInputFieldValues.serialize(report, "name", session, translations);
				logger.info(report);
			} else {
				message = TranslatorWorker.translateText("Invalid report name.");
			}
		} catch (Exception e) {
			message = e.getLocalizedMessage();
		}
		return message;
	}
	
	/**
	 * a front end for {@link MultilingualInputFieldValues#readTranslationsFromRequest(Class, long, String, String, HttpServletRequest)}
	 * co-evolve the two routines!
	 * @param reportData
	 * @param reportId
	 * @return
	 */
	private Map<String, AmpContentTranslation> populateContentTranslations (List<Map<String,String>> reportData, long reportId) {
		List<Pair<String, String>> rawData = new ArrayList<>();
		for (Map<String,String> langAndName : reportData) {
			String locale = langAndName.get("lang");
			String translation = langAndName.get("name");
			rawData.add(new Pair<>(locale, translation));
		}
		return MultilingualInputFieldValues.populateContentTranslations(rawData, AmpReports.class, reportId, "name");
	}
	
	@POST
	@Path("/report/export-to-map/{report_id}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	/**
	 * Exports the report to Map
	 * 
	 * @param config current report configuration (settings, filters)
	 * @param reportId report id
	 * @return Api state configuration id 
	 */
	public String exportToMap(JsonBean config, @PathParam("report_id") Long reportId) {
		return ReportsUtil.exportToMap(config, reportId);
	}
	
	public Map<String, Object> getColorSettings() {
		
		Set<String> hiddenColumnNames = new HashSet<String>();
		hiddenColumnNames.add(ColumnConstants.APPROVAL_STATUS);
		hiddenColumnNames.add(ColumnConstants.DRAFT);
		hiddenColumnNames.add(ColumnConstants.ACTIVITY_ID);
		// columns that will be used for coloring and should be hidden in saiku 

		Map<String, Object> colorSettings = new HashMap<String, Object>();
		
		Set<Integer> validatedStatuses = new HashSet<Integer>();
		for (String s : AmpARFilter.validatedActivityStatus) {
			validatedStatuses.add(AmpARFilter.activityStatusToNr.get(s));
		}
		
		Set<Integer> unvalidatedStatuses = new HashSet<Integer>();
		for (String s : AmpARFilter.unvalidatedActivityStatus) {
			unvalidatedStatuses.add(AmpARFilter.activityStatusToNr.get(s));
		}
		
		Map<String, Set<Integer>> activityStatusCodes = new HashMap<String, Set<Integer>>();
		activityStatusCodes.put("validated", validatedStatuses);
		activityStatusCodes.put("unvalidated", unvalidatedStatuses);
		
		colorSettings.put("hiddenColumnNames", hiddenColumnNames);
		colorSettings.put("activityStatusCodes", activityStatusCodes);
		
		return colorSettings;
	}
	
	private List<Map<String, Object>> convertJQgridSortingParams(JsonBean formParams) {
		List<Map<String, Object>> sorting = new ArrayList<Map<String, Object>>();
		// Convert jqgrid sorting params into ReportUtils sorting params.
		if (formParams.getString("sidx") != null) {

			String[] auxColumns = formParams.get("sidx").toString().split(",");
			for (int i = 0; i < auxColumns.length; i++) {
				if (!auxColumns[i].trim().equals("")) {
					Map<String, Object> sort = new HashMap<String, Object>();
					Boolean asc = true;
					if (auxColumns[i].contains(" asc") || formParams.getString("sord").equals("asc")) {
						asc = true;
						auxColumns[i] = auxColumns[i].replace(" asc", "");
					} else if (auxColumns[i].contains(" desc") || formParams.getString("sord").equals("desc")) {
						asc = false;
						auxColumns[i] = auxColumns[i].replace(" desc", "");
					}
					List<String> listOfColumns = new ArrayList<String>();
					listOfColumns.add(auxColumns[i].trim());
					sort.put("columns", listOfColumns);
					sort.put("asc", asc);
					
					// TODO: Testing what happens if we use only the last column
					// coming from jqgrid (specially on hierarchical reports).
					//if (i == auxColumns.length - 1) {
						sorting.add(sort);
					//}
				}
			}
			formParams.set(EPConstants.SORTING, sorting);
		}
		return sorting;
	}
		
	@GET
	@Path("/checkConsistency")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Map<String, List<ReportRenderWarningEx>> checkConsistency() {
		long start = System.currentTimeMillis();
		NiReportsSchema schema = AmpReportsSchema.getInstance();
		Map<String, List<ReportRenderWarning>> warnings = schema.performColumnChecks(Optional.empty());
		Map<String, List<ReportRenderWarningEx>> res = new TreeMap<>();
		for(String colName:warnings.keySet()) {
			List<ReportRenderWarningEx> z = remap(warnings.get(colName));
			if (z != null)
				res.put(colName, z);
		}
		long delta = System.currentTimeMillis() - start;
//		res.put("CHECK_TIME", delta);
		return res;
	}
	
	protected static List<ReportRenderWarningEx> remap(List<ReportRenderWarning> in) {
		if (in == null)
			return null;
		List<ReportRenderWarningEx> res = new ArrayList<>();
		for(ReportRenderWarning z:in)
			res.add(new ReportRenderWarningEx(z));
		return res;
	}
	
}
