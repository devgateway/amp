package org.digijava.kernel.ampapi.endpoints.reports;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.dbentity.AmpFilterData;
import org.dgfoundation.amp.newreports.*;
import org.dgfoundation.amp.nireports.amp.AmpReportsSchema;
import org.dgfoundation.amp.nireports.schema.NiReportsSchema;
import org.dgfoundation.amp.reports.CachedReportData;
import org.dgfoundation.amp.reports.ReportPaginationUtils;
import org.dgfoundation.amp.reports.converters.AmpReportFiltersConverter;
import org.dgfoundation.amp.reports.converters.AmpReportsToReportSpecification;
import org.dgfoundation.amp.reports.saiku.export.AMPReportExportConstants;
import org.dgfoundation.amp.reports.saiku.export.SaikuReportExportType;
import org.dgfoundation.amp.reports.saiku.export.SaikuReportHtmlRenderer;
import org.dgfoundation.amp.reports.xml.ObjectFactory;
import org.dgfoundation.amp.reports.xml.Report;
import org.dgfoundation.amp.reports.xml.ReportParameter;
import org.dgfoundation.amp.visibility.data.ColumnsVisibility;
import org.dgfoundation.amp.visibility.data.MeasuresVisibility;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponse;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponseService;
import org.digijava.kernel.ampapi.endpoints.errors.ApiRuntimeException;
import org.digijava.kernel.ampapi.endpoints.reports.saiku.QueryModel;
import org.digijava.kernel.ampapi.endpoints.reports.saiku.SaikuBasedQuery;
import org.digijava.kernel.ampapi.endpoints.reports.saiku.SortParam;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsConstants;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.kernel.ampapi.endpoints.util.JSONResult;
import org.digijava.kernel.ampapi.endpoints.util.ReportMetadata;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.*;
import org.digijava.module.translation.util.MultilingualInputFieldValues;
import org.hibernate.Session;
import org.springframework.util.ReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.util.*;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import com.google.gson.Gson;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static org.digijava.kernel.ampapi.endpoints.common.EPConstants.REPORT_TYPE_ID_MAP;
import static org.digijava.kernel.ampapi.endpoints.reports.ReportsUtil.getCachedReportData;

@Path("data")
@Api("data")
public class Reports {

    private static final String IN_MEMORY = "IN_MEMORY";
    private static final String SAVED = "SAVED";

    protected static final Logger logger = Logger.getLogger(Reports.class);

    @Context
    private HttpServletRequest httpRequest;

    @GET
    @Path("/report/{report_id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation("Get report specification")
    public final JSONResult getReport(@PathParam("report_id") Long reportId) {
        AmpReports ampReport = DbUtil.getAmpReport(reportId);
        if (ampReport == null) {
            ApiErrorResponseService.reportError(BAD_REQUEST, ReportErrors.REPORT_NOT_FOUND);
        }
        JSONResult report = getReport(ampReport);
        report.getReportMetadata().setReportType(SAVED);
        report.getReportMetadata().setReportIdentifier(reportId.toString());
        return report;
    }

    @GET
    @Path("/report/run/{report_token}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation(
            value = "Get session report specification",
            notes = "As opposed to GET /data/report/{report_id} which returns persisted reports this operation "
                    + "returns report specification visible only for current session. Used to run reports for "
                    + "anonymous users.")
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
            // AMP-29012: We need to change the Location filter according to include-location-children.
            ReportsUtil.configureIncludeLocationChildrenFilters(spec, spec.isIncludeLocationChildren());
        } catch (Exception e1) {
            logger.error("Failed to convert report.", e1);
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
    @ApiOperation("Generate a html report for diagnostic purposes")
    public String generateRenderedReport(@PathParam("report_id") Long reportId) {
        ReportSpecificationImpl spec = ReportsUtil.getReport(reportId);
        return AmpReportsSchema.getRenderedReport(spec);
    }

    /**
     * Provides a report preview.
     * </br>
     * <dl>
     * The JSON object holds information regarding:
     * <dt><b>groupingOption</b><dd> - the timeframe by which to group funding data in the report
     * <dt><b>add_columns</b><dd> - a list of columns names to be added to the report configuration
     * <dt><b>add_hierarchies</b><dd> - a list of hierarchies to be added to the report configuration
     * <dt><b>add_measures</b><dd> - a list of measures to be added to the report configuration
     * <dt><b>filters</b><dd> - Report filters
     * <dt><b>settings</b><dd> - Report settings
     * </dl></br></br>
     *
     * </br>
     * <h3>Sample Input:</h3><pre>
     * {
     *  "groupingOption": "A",
     *  "add_columns": ["Activity Id",
     *  "Project Title",
     *  "Donor Agency",
     *  "Status",
     *  "AMP ID"],
     *  "add_hierarchies": ["Project Title"],
     *  "add_measures": ["Actual Commitments"],
     *  "filters": {
     *      "date": {
     *          "start": "2010-01-01",
     *          "end": "2015-12-31"
     *      }
     *  },
     *  "settings": {
     *      "funding-type": ["Actual Commitments",
     *      "Actual Disbursements"],
     *      "currency-code": "USD",
     *      "calendar-id": "123",
     *      "year-range": {
     *          "from": "2012",
     *          "to": "2014"
     *      }
     *  }
     * }</pre>
     * <h3>Sample Output:</h3><pre>
     * <table class='nireport_table inside' cellpadding='0' cellspacing='0' width='100%'>
     *   <thead>
     *     <tr class='nireport_header'>
     *       <td class='nireport_header' rowSpan='3' colSpan='1'>Project Title<br /><font class='headermeta'>#ni#column -> Project Title</font></td>
     *       <td class='nireport_header' rowSpan='3' colSpan='1'>Activity Id<br /><font class='headermeta'>#ni#column -> Activity Id</font></td>
     *       <td class='nireport_header' rowSpan='3' colSpan='1'>Donor Agency<br /><font class='headermeta'>#ni#column -> Donor Agency</font></td>
     *       <td class='nireport_header' rowSpan='3' colSpan='1'>Status<br /><font class='headermeta'>#ni#column -> Status</font></td>
     *       <td class='nireport_header' rowSpan='3' colSpan='1'>AMP ID<br /><font class='headermeta'>#ni#column -> AMP ID</font></td>
     *       <td class='nireport_header' rowSpan='2' colSpan='1'>Totals</td>
     *     </tr>
     *     <tr class='nireport_header'></tr>
     *     <tr class='nireport_header'>
     *       <td class='nireport_header' rowSpan='1' colSpan='1'>Actual Commitments<br /><font class='headermeta'>#ni#measure -> Actual Commitments</font></td>
     *     </tr>
     *   </thead>
     *   <tbody>
     *     <tr>
     *       <td class='ni_hierarchyCell ni_hierarchyLevel1' rowspan='2'>2008.2077.9 Sustainable Land Management Program (SLM I) and 2004.2060.4 SUN Program (GIZ)</td>
     *       <td class='nireport_data_cell'>105656</td>
     *       <td class='nireport_data_cell'>Germany</td>
     *       <td class='nireport_data_cell'>Ongoing</td>
     *       <td class='nireport_data_cell'>AMP-100411</td>
     *       <td class='nireport_data_cell'>11497999.622859</td>
     *     </tr>
     *     <td class='nireport_data_cell ni_hierarchyLevel2 ni_trailcell'></td>
     *     <td class='nireport_data_cell ni_hierarchyLevel2 ni_trailcell'></td>
     *     <td class='nireport_data_cell ni_hierarchyLevel2 ni_trailcell'></td>
     *     <td class='nireport_data_cell ni_hierarchyLevel2 ni_trailcell'></td>
     *     <td class='nireport_data_cell ni_hierarchyLevel2 ni_trailcell'>11497999.622859</td>
     *     </tr>
     *     <td class='ni_hierarchyCell ni_hierarchyLevel1' rowspan='2'>2010.9002.6 Technical College in Holeta/ETH (GIZ)</td>
     *     <td class='nireport_data_cell'>103936</td>
     *     <td class='nireport_data_cell'>Germany</td>
     *     <td class='nireport_data_cell'>Ongoing</td>
     *     <td class='nireport_data_cell'>87143122101223</td>
     *     <td class='nireport_data_cell'>1892441.860465</td>
     *     </tr>
     *  ....
     *   </tbody>
     * </table></pre>
     *
     * @param formParams a JSON object with the report's parameters
     * @return a HTML with the report preview
     */
    @POST
    @Path("/report/preview")
    @Produces(MediaType.TEXT_HTML)
    @ApiOperation("Render a report preview in HTML format.")
    public final String getReportResult(
            @ApiParam("a JSON object with the report's parameters") ReportFormParameters formParams) {
        int reportType = ArConstants.DONOR_TYPE;
        if (formParams.getReportType() != null) {
            reportType = REPORT_TYPE_ID_MAP.get(formParams.getReportType());
        }
        ReportSpecificationImpl spec = new ReportSpecificationImpl("preview report", reportType);
        spec.setSummaryReport(Boolean.TRUE.equals(formParams.getSummary()));
        String groupingOption = formParams.getGroupingOption();
        ReportsUtil.setGroupingCriteria(spec, groupingOption);
        ReportsUtil.update(spec, formParams);
        SettingsUtils.applySettings(spec, formParams.getSettings(), true);
        FilterUtils.applyFilterRules(formParams.getFilters(), spec, null);
        GeneratedReport report = EndpointUtils.runReport(spec);
        SaikuReportHtmlRenderer htmlRenderer = new SaikuReportHtmlRenderer(report);

        return htmlRenderer.renderTable().toString();
    }

    @POST
    @Path("/report/scheduler")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation("Render a report preview in HTML format.")
    public final List<ReportsDashboard> getReportScheduler(
            @ApiParam("a JSON object with the report's parameters") ReportFormParameters formParams) {
        int reportType = ArConstants.DONOR_TYPE;
        if (formParams.getReportType() != null) {
            reportType = REPORT_TYPE_ID_MAP.get(formParams.getReportType());
        }
        ReportSpecificationImpl spec = new ReportSpecificationImpl("preview report", reportType);
        spec.setSummaryReport(Boolean.TRUE.equals(formParams.getSummary()));
        String groupingOption = formParams.getGroupingOption();
        ReportsUtil.setGroupingCriteria(spec, groupingOption);
        ReportsUtil.update(spec, formParams);
        SettingsUtils.applySettings(spec, formParams.getSettings(), true);
        FilterUtils.applyFilterRules(formParams.getFilters(), spec, null);
        GeneratedReport report = EndpointUtils.runReport(spec);
        SaikuReportHtmlRenderer htmlRenderer = new SaikuReportHtmlRenderer(report);

        List<ReportsDashboard> ampDashboardFunding = new ArrayList<ReportsDashboard>();
        for (ReportArea child: report.reportContents.getChildren()){
            if(child.getChildren() != null){
                for (ReportArea donorData: child.getChildren()){
                    Map<ReportOutputColumn, ReportCell> contents =donorData.getContents();

                    for (Map.Entry<ReportOutputColumn, ReportCell> content : donorData.getContents().entrySet()) {
                        ReportOutputColumn col = content.getKey();
                        ReportsDashboard fundingReport = new ReportsDashboard();

                        if (col.parentColumn != null && col.parentColumn.originalColumnName != null
                                && !col.parentColumn.originalColumnName.equals("Totals")) {
                            BigDecimal commitment = (BigDecimal) content.getValue().value;
                            fundingReport.setDonorAgency(child.getOwner().debugString);
                            fundingReport.setPillar(donorData.getOwner().debugString);
                            fundingReport.setYear(col.parentColumn.originalColumnName);
                            fundingReport.setActualCommitment(commitment.setScale(2, RoundingMode.HALF_UP));
                            ampDashboardFunding.add(fundingReport);
                        }
                    }
                }
            }
        }

        // Specify the server's endpoint URL
        String serverUrl = "http://localhost:8081/importDonorFunding";
        sendReportsToServer(ampDashboardFunding, serverUrl);
        return ampDashboardFunding;
    }

    public void sendReportsToServer(List<ReportsDashboard> ampDashboardFunding, String serverUrl) {
        try {
            // Create a URL object with the server's endpoint URL
            URL url = new URL(serverUrl);

            // Open a connection to the server
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the HTTP request method to POST
            connection.setRequestMethod("POST");

            // Set the content type of the request
            connection.setRequestProperty("Content-Type", "application/json");

            // Enable input and output streams for the connection
            connection.setDoOutput(true);

            // Convert the ampDashboardFunding to JSON using a JSON library (e.g., Gson)
            Gson gson = new Gson();
            String jsonData = gson.toJson(ampDashboardFunding);

            // Get the output stream of the connection
            try (OutputStream os = connection.getOutputStream()) {
                // Write the JSON data to the output stream
                os.write(jsonData.getBytes("UTF-8"));
            }

            // Get the HTTP response code
            int responseCode = connection.getResponseCode();

            // Check if the request was successful (e.g., HTTP 200 OK)
            if (responseCode == 200) {
                // The data has been successfully sent to the server
                System.out.println("Data sent successfully");
            } else {
                // Handle the error condition (e.g., log an error message)
                System.out.println("Error sending data. HTTP Response Code: " + responseCode);
            }

            // Close the connection
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @GET
    @Path("/report/funding")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation("Render a report preview in HTML format.")
    public final List<?> getReportScheduler() {
        List<AmpOrganisation> donorOrganisations= OrganisationUtil.getAllOrganisations();
        return donorOrganisations;
    }

    /**
     * @see ReportsUtil#getReportResultByPage
     */
    @POST
    @Path("/report/custom/paginate")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation("Generates a custom report.")
    @ApiResponses(@ApiResponse(code = HttpServletResponse.SC_OK, message = "successful operation",
            response = PagedReportResult.class))
    public final Response getCustomReport(ReportFormParameters formParams) {
        ApiErrorResponse result = ReportsUtil.validateReportConfig(formParams, true);
        if (result != null) {
            return Response.ok(result).build(); // FIXME return bad request
        }
        // we need reportId only to store the report result in cache
        Long reportId = (long) formParams.getReportName().hashCode();
        formParams.setCustom(true);
        return Response.ok(getReportResultByPage(formParams, reportId)).build();
    }

    /**
     * @see ReportsUtil#getReportResultByPage
     */
    @POST
    @Path("/report/custom/xls")
    @Produces({"application/vnd.ms-excel"})
    @ApiOperation("Generates a custom report.")
    @ApiResponses(@ApiResponse(code = HttpServletResponse.SC_OK, message = "successful operation",
            response = PagedReportResult.class))
    public final Response getCustomXlsReport(ReportFormParameters formParams) {
        ApiErrorResponse result = ReportsUtil.validateReportConfig(formParams, true);
        if (result != null) {
            throw new ApiRuntimeException(Response.Status.BAD_REQUEST, result);
        }
        Long reportId = (long) formParams.getReportName().hashCode();
        formParams.setCustom(true);
        return generateXlsReport(formParams, reportId);
    }

    @POST
    @Path("/report/custom")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML + ";charset=utf-8")
    @ApiOperation("Generates a custom xml report.")
    public final String getXmlReportResult(ReportParameter reportParameter) {
        return getXmlReportResult(reportParameter, null);
    }

    @POST
    @Path("/report/{report_id}/paginate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation("Retrieves report data for the specified reportId and a given page number")
    public final PagedReportResult getReportResultByPage(ReportFormParameters formParams,
                                                         @PathParam("report_id") Long reportId) {
        return ReportsUtil.getReportResultByPage(reportId, formParams);
    }

    @POST
    @Path("/report/{report_id}")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML + ";charset=utf-8")
    @ApiOperation("Retrieves report data in XML format for the specified reportId")
    public final String getXmlReportResult(ReportParameter reportParameter, @PathParam("report_id") Long reportId) {
        Report xmlReport = ApiXMLService.getXmlReport(reportParameter, reportId);
        ObjectFactory xmlReportObjFactory = new ObjectFactory();
        JAXBElement<Report> report = xmlReportObjFactory.createReport(xmlReport);
        return ApiXMLService.marshallOrTransform(reportParameter.getXsl(), report);
    }

    @POST
    @Path("/report/{report_id}/result/jqGrid")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation("Provides paginated result for tabs.")
    public final PagedReportResult getReportResultForTabGrid(ReportFormParameters formParams,
                                                             @PathParam("report_id") Long reportId) {

        // TODO: normally all extra columns should come from formParams
        List<String> extraColumns = new ArrayList<String>();
        extraColumns.add(ColumnConstants.ACTIVITY_ID);
        extraColumns.add(ColumnConstants.APPROVAL_STATUS);
        extraColumns.add(ColumnConstants.DRAFT);
        //extraColumns.add(ColumnConstants.TEAM_ID);  // TODO: this column never worked in NiReports - is it needed
        // by Tabs now?
        formParams.setAdditionalColumns(extraColumns);

        // Convert jqgrid sorting params into ReportUtils sorting params.
        if (formParams.getSidx() != null) {
            formParams.setSorting(convertJQgridSortingParams(formParams.getSidx(), formParams.getSord()));
        }

        return getReportResultByPage(formParams, reportId);
    }

    @GET
    @Path("/tabs")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation("Get tabs")
    public final List<JSONTab> getTabs() {

        TeamMember tm = (TeamMember) httpRequest.getSession().getAttribute(Constants.CURRENT_MEMBER);
        if (tm != null) {
            AmpTeamMember ampTeamMember = TeamUtil.getAmpTeamMember(tm.getMemberId());
            if (ampTeamMember != null) {
                return getDefaultTabs(ampTeamMember);
            }
        }

        return getPublicTabs();
    }

    private List<JSONTab> getDefaultTabs(AmpTeamMember ampTeamMember) {
        List<JSONTab> tabs = new ArrayList<JSONTab>();

        // Look for the Default Tab and add it visible to the list
        AmpApplicationSettings ampAppSettings = DbUtil.getTeamAppSettings(ampTeamMember.getAmpTeam().getAmpTeamId());
        AmpReports defaultTeamReport = ampAppSettings.getDefaultTeamReport();
        if (defaultTeamReport != null) {
            tabs.add(new JSONTab(defaultTeamReport.getAmpReportId(), true));
        }

        // Get the visible tabs of the currently logged user
        if (ampTeamMember.getDesktopTabSelections() != null && ampTeamMember.getDesktopTabSelections().size() > 0) {
            TreeSet<AmpDesktopTabSelection> sortedSelection =
                    new TreeSet<AmpDesktopTabSelection>(AmpDesktopTabSelection.tabOrderComparator);
            sortedSelection.addAll(ampTeamMember.getDesktopTabSelections());
            for (AmpDesktopTabSelection adts : sortedSelection) {
                AmpReports report = adts.getReport();
                JSONTab tab = new JSONTab(report.getAmpReportId(), true);
                tabs.add(tab);
            }
        }

        // Get the rest of the tabs that aren't visible on first instance
        List<AmpReports> userActiveTabs = TeamUtil.getAllTeamReports(ampTeamMember.getAmpTeam().getAmpTeamId(), true,
                null, null, true,
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

    public enum ExcelType {
        @JsonProperty("styled") STYLED,
        @JsonProperty("plain") PLAIN
    }

    @POST
    @Path("/saikureport/{report_id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation("Generate report")
    public final SaikuPagedReportResult getSaikuReport(
            SaikuBasedQuery queryObject,
            @PathParam("report_id") Long reportId) {

        ReportSpecificationImpl spec = ReportsUtil.getReport(reportId);
        if (spec == null) {
            try {
                spec = AmpReportsToReportSpecification.convert(ReportsUtil.getAmpReportFromSession(reportId.intValue()));
            } catch (Exception e) {
                logger.error("Cannot get report from session", e);
                throw new RuntimeException("Cannot restore report from session: " + reportId);
            }
        }

        // AMP-19189 - add columns used for coloring the project title and amp id (but not for summary reports).
        List<String> extraColumns = new ArrayList<String>();
        if (spec.getColumns().size() != spec.getHierarchies().size() && !spec.getMeasures().isEmpty()) {
            extraColumns.add(ColumnConstants.APPROVAL_STATUS);
            extraColumns.add(ColumnConstants.DRAFT);
            queryObject.setAdditionalColumns(extraColumns);
        }

        PagedReportResult result = ReportsUtil.getReportResultByPage(reportId,
                ReportsUtil.convertSaikuParamsToReports(queryObject));

        SaikuPagedReportResult saikuResult = new SaikuPagedReportResult();
        ReflectionUtils.shallowCopyFieldState(result, saikuResult);

        // Add data needed on Saiku UI.
        // TODO: Make a mayor refactoring on the js code so it doesnt need these extra parameters to work properly.
        Map<String, List<String>> queryProperties = new HashMap<>();
        queryProperties.put("properties", new ArrayList<>());
        saikuResult.setQuery(queryProperties);
        List<String> cellset = new ArrayList<String>();
        cellset.add("dummy");
        saikuResult.setCellset(cellset);

        // Add some missing metadata when running through Rhino.
        saikuResult.setColumns(spec.getColumns());
        saikuResult.setHierarchies(spec.getHierarchies());

        saikuResult.setColorSettings(getColorSettings(spec.getColumns()));

        // In caseIf this is a summarized report without hierarchies then we need to change the word 'constant' for
        // 'Report
        // Totals' (translated).
        saikuResult.setReportTotalsString(TranslatorWorker.translateText("Report Totals"));
        ReportsUtil.addLastViewedReport(httpRequest.getSession(), reportId);

        return saikuResult;
    }

    @POST
    @Path("/saikureport/run/{report_token}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation("Generate session report")
    public final SaikuPagedReportResult getSaikuReport(
            SaikuBasedQuery formParams,
            @PathParam("report_token") String reportToken) {
        //here we fetch the report by reportToken from session session
        formParams.setDinamic(true);
        return getSaikuReport(formParams, new Long(reportToken));
    }

    @POST
    @Path("/saikureport/export/xls/{report_id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces({"application/vnd.ms-excel"})
    @ApiOperation("Generate XLS report")
    public final Response exportXlsSaikuReport(
            @ApiParam("Stringified body parameter as documented in POST /saikureport/{report_id}")
            @DefaultValue("{\"queryModel\": {\"page\": 0,\"recordsPerPage\": 0}}")
            @FormParam("query") SaikuBasedQuery query,
            @DefaultValue("false") @FormParam("isPublic") Boolean isPublic,
            @PathParam("report_id") Long reportId) {
        return exportSaikuReport(query, DbUtil.getAmpReport(reportId), AMPReportExportConstants.XLSX, false, isPublic);
    }

    @POST
    @Path("/saikureport/export/xls/run/{report_token}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces({"application/vnd.ms-excel"})
    @ApiOperation("Generate XLS for a session report")
    public final Response exportXlsSaikuReport(
            @ApiParam("Stringified body parameter as documented in POST /saikureport/{report_id}")
            @DefaultValue("{\"queryModel\": {\"page\": 0,\"recordsPerPage\": 0}}")
            @FormParam("query") SaikuBasedQuery query,
            @PathParam("report_token") Integer reportToken) {
        return exportInMemorySaikuReport(query, reportToken, AMPReportExportConstants.XLSX);
    }

    @POST
    @Path("/saikureport/export/csv/{report_id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces({"text/csv"})
    @ApiOperation("Generate CSV report")
    public final Response exportCsvSaikuReport(
            @ApiParam("Stringified body parameter as documented in POST /saikureport/{report_id}")
            @DefaultValue("{\"queryModel\": {\"page\": 0,\"recordsPerPage\": 0}}")
            @FormParam("query") SaikuBasedQuery query,
            @PathParam("report_id") Long reportId) {
        return exportSaikuReport(query, DbUtil.getAmpReport(reportId), AMPReportExportConstants.CSV, false, false);
    }

    @POST
    @Path("/saikureport/export/csv/run/{report_token}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces({"text/csv"})
    @ApiOperation("Generate CSV for a session report")
    public final Response exportCsvSaikuReport(
            @ApiParam("Stringified body parameter as documented in POST /saikureport/{report_id}")
            @DefaultValue("{\"queryModel\": {\"page\": 0,\"recordsPerPage\": 0}}")
            @FormParam("query") SaikuBasedQuery query,
            @PathParam("report_token") Integer reportToken) {
        return exportInMemorySaikuReport(query, reportToken, AMPReportExportConstants.CSV);
    }

    @POST
    @Path("/saikureport/export/xml/{report_id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces({"application/xml"})
    @ApiOperation("Generate XML report")
    @ApiResponses(@ApiResponse(code = HttpServletResponse.SC_OK, message = "success", response = Report.class))
    public final Response exportXmlSaikuReport(
            @ApiParam("Stringified body parameter as documented in POST /saikureport/{report_id}")
            @DefaultValue("{\"queryModel\": {\"page\": 0,\"recordsPerPage\": 0}}")
            @FormParam("query") SaikuBasedQuery query,
            @PathParam("report_id") Long reportId) {
        return exportSaikuReport(query, DbUtil.getAmpReport(reportId), AMPReportExportConstants.XML, false, false);
    }

    @POST
    @Path("/saikureport/export/xml/run/{report_token}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces({"application/xml"})
    @ApiOperation("Generate XML for a session report")
    @ApiResponses(@ApiResponse(code = HttpServletResponse.SC_OK, message = "success", response = Report.class))
    public final Response exportXmlSaikuReport(
            @ApiParam("Stringified body parameter as documented in POST /saikureport/{report_id}")
            @DefaultValue("{\"queryModel\": {\"page\": 0,\"recordsPerPage\": 0}}")
            @FormParam("query") SaikuBasedQuery query,
            @PathParam("report_token") Integer reportToken) {
        return exportInMemorySaikuReport(query, reportToken, AMPReportExportConstants.XML);
    }

    @POST
    @Path("/saikureport/export/pdf/{report_id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces({"application/pdf"})
    @ApiOperation("Generate PDF report")
    public final Response exportPdfSaikuReport(
            @ApiParam("Stringified body parameter as documented in POST /saikureport/{report_id}")
            @DefaultValue("{\"queryModel\": {\"page\": 0,\"recordsPerPage\": 0}}")
            @FormParam("query") SaikuBasedQuery query,
            @DefaultValue("false") @FormParam("isPublic") Boolean isPublic,
            @PathParam("report_id") Long reportId) {
        return exportSaikuReport(query, DbUtil.getAmpReport(reportId), AMPReportExportConstants.PDF, false, isPublic);
    }

    @POST
    @Path("/saikureport/export/pdf/run/{report_token}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces({"application/pdf"})
    @ApiOperation("Generate PDF for a session report")
    public final Response exportPdfSaikuReport(
            @ApiParam("Stringified body parameter as documented in POST /saikureport/{report_id}")
            @DefaultValue("{\"queryModel\": {\"page\": 0,\"recordsPerPage\": 0}}")
            @FormParam("query") SaikuBasedQuery query,
            @PathParam("report_token") Integer reportToken) {
        return exportInMemorySaikuReport(query, reportToken, AMPReportExportConstants.PDF);
    }

    private Response exportInMemorySaikuReport(SaikuBasedQuery query, Integer reportToken, String reportType) {
        AmpReports ampReport = ReportsUtil.getAmpReportFromSession(reportToken);
        ampReport.setAmpReportId(reportToken.longValue());
        return exportSaikuReport(query, ReportsUtil.getAmpReportFromSession(reportToken), reportType, true, false);
    }

    private GeneratedReport getDualCurrencyReport(SaikuBasedQuery queryObject, long reportId, String ampCurrencyCode) {
        SaikuBasedQuery newQueryObject = updateCurrency(queryObject, ampCurrencyCode);

        return ReportsUtil.getGeneratedReport(reportId, ReportsUtil.convertSaikuParamsToReports(newQueryObject));
    }

    private SaikuBasedQuery updateCurrency(SaikuBasedQuery queryObject, String ampCurrencyCode) {
        SaikuBasedQuery newQueryObject = queryObject.clone();
        QueryModel newQueryModel = queryObject.getQueryModel().clone();
        newQueryObject.setMd5(Long.toString(Calendar.getInstance().getTimeInMillis()));
        final HashMap<String, Object> newSettings = new LinkedHashMap<>();  // copy the settings
        final Map<String, Object> oldSettings = newQueryModel.getSettings();
        if (oldSettings != null) {
            for (final Map.Entry<String, Object> entry : oldSettings.entrySet()) {
                newSettings.put(entry.getKey(), entry.getValue());
            }
        }
        newSettings.put(SettingsConstants.CURRENCY_ID, ampCurrencyCode);
        newQueryModel.setSettings(newSettings);
        newQueryObject.setQueryModel(newQueryModel);

        return newQueryObject;
    }

    private Response exportSaikuReport(SaikuBasedQuery queryObject, AmpReports ampReport, String type,
                                       Boolean isDinamic, Boolean isPublic) {

        logger.info("Starting export to " + type);

        GeneratedReport generatedReport = null;
        if (isPublic) {
            queryObject = new SaikuBasedQuery();
            generatedReport = EndpointUtils.runReport(AmpReportsToReportSpecification.convert(ampReport));
        } else {
            QueryModel queryModel = queryObject.getQueryModel();

            queryModel.setPage(0);
            queryModel.setRecordsPerPage(-1);
            if (isDinamic) {
                queryObject.setDinamic(true);
            }

            generatedReport = ReportsUtil.getGeneratedReport(ampReport.getAmpReportId(),
                    ReportsUtil.convertSaikuParamsToReports(queryObject));
        }


        return getExportAsResponse(ampReport, type, generatedReport, queryObject);
    }

    public Response getExportAsResponse(AmpReports ampReport, String type, GeneratedReport report,
                                        SaikuBasedQuery queryObject) {
        String fileName = getExportFileName(ampReport, type);
        try {
            byte[] doc = exportNiReport(report, ampReport.getAmpReportId(), queryObject, type);

            if (doc != null) {
                logger.info("Send export data to browser...");

                MediaType mediaType = EndpointUtils.getMediaType(type);

                return Response.ok(doc, mediaType)
                        .header("content-disposition", "attachment; filename = " + fileName)
                        .header("content-length", doc.length).build();
            } else {
                logger.error(type + " report export is null");
                return Response.serverError().build();
            }
        } catch (Exception e) {
            logger.error("error while generating report", e);
            return Response.serverError().build();
        }
    }

    public String getExportFileName(AmpReports ampReport, String type) {

        String filename = ampReport != null ? StringUtils.trim(ampReport.getName()) : "export";
        filename = String.format("%s", filename.replaceAll(" ", "_"));

        try {
            filename = URLEncoder.encode(filename, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        }

        filename += "." + type;

        return filename;
    }
    public Response generateXlsReport(ReportFormParameters formParams, Long reportId) {
        CachedReportData cachedReportData = getCachedReportData(reportId, formParams);
        return generateXlsReport(cachedReportData.report, AMPReportExportConstants.XLSX);
    }

    private Response generateXlsReport(GeneratedReport generatedReport, String type) {
        SaikuBasedQuery queryObject = new SaikuBasedQuery();
        AmpReports ampReport = new AmpReports();
        ampReport.setName("sscDataDownload");
        return getExportAsResponse(ampReport, type, generatedReport, queryObject);
    }
    /**
     * Method used for exporting a NiReport.
     *
     * @param report
     * @param reportId
     * @param queryObject
     * @param type
     * @return
     * @throws Exception
     */
    private byte[] exportNiReport(GeneratedReport report, Long reportId, SaikuBasedQuery queryObject,
                                  String type) throws Exception {

        SaikuReportExportType exporter = null;
        GeneratedReport dualReport = null;

        switch (type) {
            case AMPReportExportConstants.XLSX: {
                QueryModel queryModel = queryObject.getQueryModel();
                if (queryObject.getExcelType() == ExcelType.PLAIN) {
                    exporter = SaikuReportExportType.XLSX_PLAIN;
                } else {
                    exporter = SaikuReportExportType.XLSX;
                }

                String secondCurrencyCode = queryModel != null ? queryModel.getSecondCurrency() : null;

                if (secondCurrencyCode != null) {
                    logger.info(String.format("secondCurrency=%s", secondCurrencyCode));
                    dualReport = getDualCurrencyReport(queryObject, reportId, secondCurrencyCode);
                }
                break;
            }
            case AMPReportExportConstants.CSV:
                exporter = SaikuReportExportType.CSV;
                break;
            case AMPReportExportConstants.PDF:
                exporter = SaikuReportExportType.PDF;
                break;
            case AMPReportExportConstants.XML:
                exporter = SaikuReportExportType.XML;
                break;
        }

        return exporter.executor.newInstance().exportReport(report, dualReport);
    }

    @GET
    @Path("/report/columns")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation("Get columns")
    public final Map<String, String> getAllowedColumns() {
        Map<String, String> columnToDisplayName = new HashMap<String, String>();
        Set<String> configurableColumns = ColumnsVisibility.getConfigurableColumns();
        for (String originalColumnName : configurableColumns) {
            columnToDisplayName.put(originalColumnName, TranslatorWorker.translateText(originalColumnName));
        }

        return columnToDisplayName;
    }

    @GET
    @Path("/report/measures")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation("Get measures")
    public final Map<String, String> getAllowedMeasures() {
        Map<String, String> measuresToDisplayName = new HashMap<String, String>();
        Set<String> configurableMeasures = MeasuresVisibility.getConfigurableMeasures();
        for (String originalMeasureName : configurableMeasures) {
            measuresToDisplayName.put(originalMeasureName, TranslatorWorker.translateText(originalMeasureName));
        }

        return measuresToDisplayName;
    }


    @POST
    @Path("/report/saveTab/{report_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Save tab")
    public String saveTab(SaveTabRequest formParams, @PathParam("report_id") Long reportId) {
        String message = null;
        try {
            // Open AmpReport.
            AmpReports report = DbUtil.getAmpReport(reportId);
            // AmpARFilter oldFilters = FilterUtil.buildFilter(null, reportId);
            AmpARFilter newFilters = null;

            // Convert json object back to AmpReportFilters
            Map<String, Object> filterMap = formParams.getFilters();
            if (filterMap != null) {
                AmpReportFilters reportFilters = FilterUtils.getFilters(filterMap, new AmpReportFilters());

                // Transform back to legacy AmpARFilters.
                AmpReportFiltersConverter converter = new AmpReportFiltersConverter(reportFilters);
                newFilters = converter.buildFilters();
                // converter.mergeWithOldFilters(oldFilters);

                String sidx = formParams.getSidx();
                String sord = formParams.getSord();
                if (sidx != null && !sidx.equals("")) {
                    List<SortParam> sortParams = convertJQgridSortingParams(sidx, sord);
                    logger.info(sortParams);
                    newFilters.setSortByAsc(sord.equals("asc"));

                    String columns = "";
                    for (SortParam map : sortParams) {
                        String column = map.getColumns().toString();
                        column = column.substring(column.indexOf("[") + 1, column.indexOf("]"));
                        columns += ("/" + column);
                    }
                    newFilters.setSortBy(columns);
                }

                if (formParams.getSettings() != null) {
                    String currency = formParams.getSettings().get(SettingsConstants.CURRENCY_ID).toString();
                    String calendar = formParams.getSettings().get(SettingsConstants.CALENDAR_TYPE_ID).toString();
                    newFilters.setCurrency(CurrencyUtil.getAmpcurrency(currency));
                    newFilters.setCalendarType(FiscalCalendarUtil.getAmpFiscalCalendar(new Long(calendar)));
                }

                if (formParams.getIncludeLocationChildren() != null) {
                    newFilters.setIncludeLocationChildren(formParams.getIncludeLocationChildren());
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

            Session session = PersistenceManager.getSession();
            List<Map<String, String>> reportData = formParams.getReportData();
            boolean emptyDefaultName = true;
            String defaultLang = TLSUtils.getEffectiveLangCode();
            for (Map<String, String> name : reportData) {
                if (defaultLang != null && defaultLang.equals(name.get("lang")) && !"".equals(name.get("name"))) {
                    emptyDefaultName = false;
                }
            }
            if (!emptyDefaultName) {
                Map<String, AmpContentTranslation> translations = populateContentTranslations(reportData, reportId);
                MultilingualInputFieldValues.serialize(report, "name", session, translations);
                logger.info(report);
            } else {
                message = TranslatorWorker.translateText("Please enter a report name in current language");
            }
        } catch (Exception e) {
            message = e.getLocalizedMessage();
        }
        return message;
    }

    /**
     * a front end for
     * {@link MultilingualInputFieldValues#readTranslationsFromRequest(Class, long, String, String, HttpServletRequest)}
     * co-evolve the two routines!
     *
     * @param reportData
     * @param reportId
     * @return
     */
    private Map<String, AmpContentTranslation> populateContentTranslations(List<Map<String, String>> reportData,
                                                                           long reportId) {
        List<Pair<String, String>> rawData = new ArrayList<>();
        for (Map<String, String> langAndName : reportData) {
            if (StringUtils.isNotEmpty(langAndName.get("name"))) {
                String locale = langAndName.get("lang");
                rawData.add(Pair.of(locale, langAndName.get("name")));
            }

        }
        return MultilingualInputFieldValues.populateContentTranslations(rawData, AmpReports.class, reportId, "name");
    }

    @POST
    @Path("/report/export-to-map/{report_id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation("Save report configuration for current session")
    @ApiResponses(@ApiResponse(code = HttpServletResponse.SC_OK, message = "configuration id"))
    public String exportToMap(
            @ApiParam("report configuration") ReportConfig config,
            @PathParam("report_id") Long reportId) {
        return ReportsUtil.exportToMap(config, reportId);
    }

    public Map<String, Object> getColorSettings(Set<ReportColumn> reportColumns) {

        // columns that will be used for coloring and should be hidden in saiku if are not present in report
        // specification
        Set<String> hiddenColumnNames = new HashSet<String>();
        hiddenColumnNames.add(ColumnConstants.APPROVAL_STATUS);
        hiddenColumnNames.add(ColumnConstants.DRAFT);

        for (ReportColumn rc : reportColumns) {
            hiddenColumnNames.remove(rc.getColumnName());
        }

        Map<String, Object> colorSettings = new HashMap<String, Object>();

        Set<Integer> validatedStatuses = new HashSet<Integer>();
        for (ApprovalStatus s : AmpARFilter.VALIDATED_ACTIVITY_STATUS) {
            validatedStatuses.add(s.getId());
        }

        Set<Integer> unvalidatedStatuses = new HashSet<Integer>();
        for (ApprovalStatus s : AmpARFilter.UNVALIDATED_ACTIVITY_STATUS) {
            unvalidatedStatuses.add(s.getId());
        }

        Map<String, Set<Integer>> activityStatusCodes = new HashMap<String, Set<Integer>>();
        activityStatusCodes.put("validated", validatedStatuses);
        activityStatusCodes.put("unvalidated", unvalidatedStatuses);

        colorSettings.put("hiddenColumnNames", hiddenColumnNames);
        colorSettings.put("activityStatusCodes", activityStatusCodes);

        return colorSettings;
    }

    private List<SortParam> convertJQgridSortingParams(String sidx, String sord) {
        List<SortParam> sorting = new ArrayList<>();
        // Convert jqgrid sorting params into ReportUtils sorting params.
        if (sidx != null) {

            String[] auxColumns = sidx.split(",");
            for (int i = 0; i < auxColumns.length; i++) {
                if (!auxColumns[i].trim().equals("")) {
                    SortParam sort = new SortParam();
                    Boolean asc = true;
                    if (auxColumns[i].contains(" asc") || sord.equals("asc")) {
                        asc = true;
                        auxColumns[i] = auxColumns[i].replace(" asc", "");
                    } else if (auxColumns[i].contains(" desc") || sord.equals("desc")) {
                        asc = false;
                        auxColumns[i] = auxColumns[i].replace(" desc", "");
                    }
                    List<String> listOfColumns = new ArrayList<String>();
                    listOfColumns.add(auxColumns[i].trim());
                    sort.setColumns(listOfColumns);
                    sort.setAsc(asc);

                    // TODO: Testing what happens if we use only the last column
                    // coming from jqgrid (specially on hierarchical reports).
                    //if (i == auxColumns.length - 1) {
                    sorting.add(sort);
                    //}
                }
            }
        }
        return sorting;
    }

    @GET
    @Path("/checkConsistency")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation("Check report engine configuration for consistency")
    public Map<String, List<ReportRenderWarningEx>> checkConsistency() {
        long start = System.currentTimeMillis();
        NiReportsSchema schema = AmpReportsSchema.getInstance();
        Map<String, List<ReportRenderWarning>> warnings = schema.performColumnChecks(Optional.empty());
        Map<String, List<ReportRenderWarningEx>> res = new TreeMap<>();
        for (String colName : warnings.keySet()) {
            List<ReportRenderWarningEx> z = remap(warnings.get(colName));
            if (z != null)
                res.put(colName, z);
        }
        long delta = System.currentTimeMillis() - start;
//      res.put("CHECK_TIME", delta);
        return res;
    }

    protected static List<ReportRenderWarningEx> remap(List<ReportRenderWarning> in) {
        if (in == null)
            return null;
        List<ReportRenderWarningEx> res = new ArrayList<>();
        for (ReportRenderWarning z : in) {
            res.add(new ReportRenderWarningEx(z));
        }
        return res;
    }
}
