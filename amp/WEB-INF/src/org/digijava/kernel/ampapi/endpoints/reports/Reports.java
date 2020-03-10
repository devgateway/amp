package org.digijava.kernel.ampapi.endpoints.reports;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
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

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
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
import javax.xml.bind.JAXBElement;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.dbentity.AmpFilterData;
import org.dgfoundation.amp.newreports.AmpReportFilters;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportRenderWarning;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.nireports.amp.AmpReportsSchema;
import org.dgfoundation.amp.nireports.schema.NiReportsSchema;
import org.dgfoundation.amp.reports.ReportPaginationUtils;
import org.dgfoundation.amp.reports.converters.AmpReportFiltersConverter;
import org.dgfoundation.amp.reports.mondrian.converters.AmpReportsToReportSpecification;
import org.dgfoundation.amp.reports.saiku.export.AMPReportExportConstants;
import org.dgfoundation.amp.reports.saiku.export.ReportGenerationInfo;
import org.dgfoundation.amp.reports.saiku.export.SaikuReportExportType;
import org.dgfoundation.amp.reports.saiku.export.SaikuReportHtmlRenderer;
import org.dgfoundation.amp.reports.xml.ObjectFactory;
import org.dgfoundation.amp.reports.xml.Report;
import org.dgfoundation.amp.reports.xml.ReportParameter;
import org.dgfoundation.amp.visibility.data.ColumnsVisibility;
import org.dgfoundation.amp.visibility.data.MeasuresVisibility;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponse;
import org.digijava.kernel.ampapi.endpoints.errors.ErrorReportingEndpoint;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsConstants;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.kernel.ampapi.endpoints.util.JSONResult;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.endpoints.util.ReportMetadata;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpContentTranslation;
import org.digijava.module.aim.dbentity.AmpDesktopTabSelection;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.translation.util.MultilingualInputFieldValues;
import org.hibernate.Session;

import mondrian.util.Pair;

/***
 * 
 * @author
 * 
 */

@Path("data")
public class Reports implements ErrorReportingEndpoint {
    
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
        AmpReports ampReport = DbUtil.getAmpReport(reportId);
        if (ampReport == null) {
            ApiErrorResponse.reportError(BAD_REQUEST, ReportErrors.REPORT_NOT_FOUND);
        }
        JSONResult report = getReport(ampReport);
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
    @Path("/report/{report_id}/result")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public final GeneratedReport getReportResult(@PathParam("report_id") Long reportId) {
        ReportSpecificationImpl spec = ReportsUtil.getReport(reportId);
        return EndpointUtils.runReport(spec);
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
     *}</pre>
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
     *
     * @return a HTML with the report preview
     */
    @POST
    @Path("/report/preview")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public final String getReportResult(JsonBean formParams) {
        ReportSpecificationImpl spec = new ReportSpecificationImpl("preview report", ArConstants.DONOR_TYPE);
        String groupingOption = (String) formParams.get("groupingOption");
        ReportsUtil.setGroupingCriteria(spec, groupingOption);
        ReportsUtil.update(spec,formParams);
        SettingsUtils.applySettings(spec, formParams, true);
        FilterUtils.applyFilterRules((Map<String, Object>) formParams.get(EPConstants.FILTERS), spec,null);
        GeneratedReport report = EndpointUtils.runReport(spec);
        SaikuReportHtmlRenderer htmlRenederer = new SaikuReportHtmlRenderer(report);

        return htmlRenederer.renderTable().toString();
    }

    @POST
    @Path("/report/custom/paginate")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    /**
     * Generates a custom report.  
     * 
     * @param formParams {@link ReportsUtil#getReportResultByPage form parameters}
     * @return Response JsonBean result for the requested page and pagination information
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
     * Generates a custom xml report.
     *
     * @param reportParameter report parameters ({@link /src/main/resources/schemas/report.xsd})
     * @return Response in xml format result for the report
     */
    @POST
    @Path("/report/custom")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML + ";charset=utf-8")
    public final String getXmlReportResult(ReportParameter reportParameter) {
        return getXmlReportResult(reportParameter, null);
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
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public final JsonBean getReportResultByPage(JsonBean formParams,
            @PathParam("report_id") Long reportId) {
        return ReportsUtil.getReportResultByPage(reportId, formParams);
    }
    
    /**
     * Retrieves report data in XML format for the specified reportId
     *
     * @param reportId report Id
     * @param reportParameter report parameters ({@link /src/main/resources/schemas/report.xsd})
     * @return XML result for the specified reportId
     * @see ApiXMLService#getXmlReport
     */
    @POST
    @Path("/report/{report_id}")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML + ";charset=utf-8")
    public final String getXmlReportResult(ReportParameter reportParameter, @PathParam("report_id") Long reportId) {
        Report xmlReport = ApiXMLService.getXmlReport(reportParameter, reportId);
        ObjectFactory xmlReportObjFactory = new ObjectFactory();
        JAXBElement<Report> report = xmlReportObjFactory.createReport(xmlReport);
        return ApiXMLService.marshallOrTransform(reportParameter.getXsl(), report);
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

        // TODO: normally all extra columns should come from formParams
        List<String> extraColumns = new ArrayList<String>();
        extraColumns.add(ColumnConstants.ACTIVITY_ID);
        extraColumns.add(ColumnConstants.APPROVAL_STATUS);
        extraColumns.add(ColumnConstants.DRAFT);
        //extraColumns.add(ColumnConstants.TEAM_ID);  // TODO: this column never worked in NiReports - is it needed by Tabs now?
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
    public final JsonBean getSaikuReport(JsonBean queryObject, @PathParam("report_id") Long reportId) {

        ReportSpecificationImpl spec = ReportsUtil.getReport(reportId);
        if(spec == null){
            try {
                spec = AmpReportsToReportSpecification.convert(ReportsUtil.getAmpReportFromSession(reportId.intValue()));
            } catch (Exception e) {
                logger.error("Cannot get report from session",e);
                throw new RuntimeException("Cannot restore report from session: " + reportId);
            }
        }

        // AMP-19189 - add columns used for coloring the project title and amp id (but not for summary reports).
        List<String> extraColumns = new ArrayList<String>();
        if (spec.getColumns().size() != spec.getHierarchies().size() && !spec.getMeasures().isEmpty()) {
            extraColumns.add(ColumnConstants.APPROVAL_STATUS);
            extraColumns.add(ColumnConstants.DRAFT);
            queryObject.set(EPConstants.ADD_COLUMNS, extraColumns);
        }

        JsonBean report = ReportsUtil.getReportResultByPage(reportId,
                ReportsUtil.convertSaikuParamsToReports(queryObject));
        
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
        
        report.set("colorSettings", getColorSettings(spec.getColumns()));
        
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
        return getSaikuReport(formParams, new Long(reportToken));
    }   
    
    @POST
    @Path("/saikureport/export/xls/{report_id}")
    @Produces({"application/vnd.ms-excel" })
    public final Response exportXlsSaikuReport(String query, @PathParam("report_id") Long reportId, 
            @DefaultValue("false") @QueryParam ("nireport") Boolean asNiReport) {
        return exportSaikuReport(query, DbUtil.getAmpReport(reportId), AMPReportExportConstants.XLSX, false);
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
        return exportSaikuReport(query, DbUtil.getAmpReport(reportId), AMPReportExportConstants.CSV, false);

    }

    @POST
    @Path("/saikureport/export/csv/run/{report_token}")
    @Produces({"text/csv"})
    public final Response exportCsvSaikuReport(String query, @PathParam("report_token") Integer reportToken,
            @DefaultValue("false") @QueryParam ("nireport") Boolean asNiReport) {

        return exportInMemorySaikuReport(query, reportToken, AMPReportExportConstants.CSV);
    }
    
    @POST
    @Path("/saikureport/export/xml/{report_id}")
    @Produces({"application/xml"})
    public final Response exportXmlSaikuReport(String query, @PathParam("report_id") Long reportId,
            @DefaultValue("false") @QueryParam ("nireport") Boolean asNiReport) {
        return exportSaikuReport(query, DbUtil.getAmpReport(reportId), AMPReportExportConstants.XML, false);

    }

    @POST
    @Path("/saikureport/export/xml/run/{report_token}")
    @Produces({"application/xml"})
    public final Response exportXmlSaikuReport(String query, @PathParam("report_token") Integer reportToken,
            @DefaultValue("false") @QueryParam ("nireport") Boolean asNiReport) {

        return exportInMemorySaikuReport(query, reportToken, AMPReportExportConstants.XML);
    }

    @POST
    @Path("/saikureport/export/pdf/{report_id}")
    @Produces({"application/pdf"})
    public final Response exportPdfSaikuReport(String query, @PathParam("report_id") Long reportId, 
            @DefaultValue("false") @QueryParam ("nireport") Boolean asNiReport) {

        return exportSaikuReport(query, DbUtil.getAmpReport(reportId), AMPReportExportConstants.PDF, false);
    }

    @POST
    @Path("/saikureport/export/pdf/run/{report_token}")
    @Produces({"application/pdf"})
    public final Response exportPdfSaikuReport(String query, @PathParam("report_token") Integer reportToken) {
        return exportInMemorySaikuReport(query, reportToken, AMPReportExportConstants.PDF);
    }
    
    @POST
    @Path("/saikupublicreport/export/pdf/{report_id}")
    @Produces({"application/pdf"})
    public final Response exportPdfSaikuReport(@PathParam("report_id") Long reportId) {
        return exportSaikuPublicReport(DbUtil.getAmpReport(reportId), AMPReportExportConstants.PDF);
    }
    
    @POST
    @Path("/saikupublicreport/export/xls/{report_id}")
    @Produces({"application/vnd.ms-excel"})
    public final Response exportExcelSaikuReport(@PathParam("report_id") Long reportId) {
        return exportSaikuPublicReport(DbUtil.getAmpReport(reportId), AMPReportExportConstants.XLSX);
    }

    private Response exportInMemorySaikuReport(String query, Integer reportToken,String reportType) {
        AmpReports ampReport=ReportsUtil.getAmpReportFromSession(reportToken);
        ampReport.setAmpReportId(reportToken.longValue());
        return exportSaikuReport(query, ReportsUtil.getAmpReportFromSession(reportToken), reportType,true);
    }
    
    public final Response exportSaikuReport(String query, AmpReports ampReport, String type) {
        return exportSaikuReport(query, ampReport, type, false);
    }
    
    public final Response exportSaikuReport(String query, AmpReports ampReport, String type, Boolean isDinamic) {
        return exportSaikuReport(query, type, ampReport, isDinamic);
        
    }

    /**
     * a very very very ugly and hacky function which only exists because of some hacks in Saiku/Mondrian
     * not used in Saiku/NiReports
     * @deprecated
     * @param queryObject
     * @param origReport
     * @param ampReportId
     * @param ampCurrencyCode
     * @return
     * TODO: remove function and code using it
     */
    protected ReportGenerationInfo changeReportCurrencyTo(JsonBean queryObject, 
            ReportGenerationInfo origReport, long ampReportId, String ampCurrencyCode) {
        
        JsonBean newQueryObject = updateCurrency(queryObject, ampCurrencyCode);
        LinkedHashMap<String, Object> newQueryModel = (LinkedHashMap<String, Object>) newQueryObject.get("queryModel");
        
        JsonBean newResult = getSaikuReport(newQueryObject, ampReportId);
        ReportSpecification newReport = origReport.report; 
        
        return new ReportGenerationInfo(newResult, origReport.type, newReport, newQueryModel , String.format(" - %s", ampCurrencyCode));
    }
    
    
    protected GeneratedReport getDualCurrencyReport(JsonBean queryObject, long reportId, String ampCurrencyCode) {
        JsonBean newQueryObject = updateCurrency(queryObject, ampCurrencyCode);
        
        return ReportsUtil.getGeneratedReport(reportId, ReportsUtil.convertSaikuParamsToReports(newQueryObject));
    }

    private JsonBean updateCurrency(JsonBean queryObject, String ampCurrencyCode) {
        JsonBean newQueryObject = queryObject.copy();
        LinkedHashMap<String, Object> newQueryModel = new LinkedHashMap<String, Object>((LinkedHashMap<String, Object>) queryObject.get("queryModel"));
        newQueryObject.set(EPConstants.MD5_TOKEN, Long.toString(Calendar.getInstance().getTimeInMillis()));
        final HashMap<String, Object> newSettings = new LinkedHashMap<>();  // copy the settings
        final Map<String, Object> oldSettings = (Map<String, Object>) newQueryModel.get(EPConstants.SETTINGS);
        if(oldSettings != null) {
            for (final Map.Entry<String, Object> entry : oldSettings.entrySet()) {
                newSettings.put(entry.getKey(), entry.getValue());
            }
        }
        newSettings.put(SettingsConstants.CURRENCY_ID, ampCurrencyCode);
        newQueryModel.put(EPConstants.SETTINGS, newSettings);
        newQueryObject.set("queryModel", newQueryModel);
        
        return newQueryObject;
    }
    
    private Response exportSaikuReport(String query, String type, AmpReports ampReport, Boolean isDinamic) {
        logger.info("Starting export to " + type);
        String decodedQuery = "";
        
        try {
            decodedQuery = java.net.URLDecoder.decode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("error while generating report", e);
            return Response.serverError().build();
        }
        
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
        JsonBean result = getSaikuReport(queryObject, ampReport.getAmpReportId());

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
        
        logger.info("Generate specific export...");
        GeneratedReport generatedReport = ReportsUtil.getGeneratedReport(ampReport.getAmpReportId(),
                ReportsUtil.convertSaikuParamsToReports(queryObject));
        
        return getExportAsResponse(ampReport, type, generatedReport, queryObject);
    }
    
    /** Method used for exporting a public NiReport. 
     * @param ampReport
     * @param type
     * @return Response containing the report data
     */
    private Response exportSaikuPublicReport(AmpReports ampReport, String type) {
        logger.info("Export specific public export...");
        
        GeneratedReport report = EndpointUtils.runReport(AmpReportsToReportSpecification.convert(ampReport));
        
        //TODO: refactoring should be made before 2.12 official release by merging with exportSaikuReport
        return getExportAsResponse(ampReport, type, report, new JsonBean());
    }

    public Response getExportAsResponse(AmpReports ampReport, String type, GeneratedReport report, JsonBean queryObject) {
        String fileName = getExportFileName(ampReport, type);
        try {
            byte[] doc = exportNiReport(report, ampReport.getAmpReportId(), queryObject, type);
            
            if (doc != null) {
                logger.info("Send export data to browser...");

                return Response.ok(doc, MediaType.APPLICATION_OCTET_STREAM)
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
            logger.error(e);
        }

        filename += "." + type;
        
        return filename;
    }
    
    /** Method used for exporting a NiReport. 
     * @param report
     * @param reportId
     * @param queryObject
     * @param type
     * @return
     * @throws Exception
     */
    private byte[] exportNiReport(GeneratedReport report, Long reportId, JsonBean queryObject, String type) throws Exception {
        
        SaikuReportExportType exporter = null;
        GeneratedReport dualReport = null;
        
        switch (type) {
            case AMPReportExportConstants.XLSX: {
                LinkedHashMap<String, Object> queryModel = (LinkedHashMap<String, Object>) queryObject.get("queryModel");
                String styleType = queryModel != null ? (String) queryModel.get(AMPReportExportConstants.EXCEL_TYPE_PARAM) : null;
                if ("plain".equals(styleType)) {
                    exporter = SaikuReportExportType.XLSX_PLAIN;
                } else {
                    exporter = SaikuReportExportType.XLSX;
                }
                            
                String secondCurrencyCode = queryModel != null && queryModel.containsKey("secondCurrency") ? queryModel.get("secondCurrency").toString() : null;
                
                if (secondCurrencyCode != null) {
                    logger.info(String.format("setts 1 = %s, 2 = %s, secondCurrency=%s", queryModel.get("1"), queryModel.get("2"), secondCurrencyCode));
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
    public String saveTab(JsonBean formParams, @PathParam("report_id") Long reportId) {
        String message = null;
        try {
            // Open AmpReport.
            AmpReports report = DbUtil.getAmpReport(reportId);
            // AmpARFilter oldFilters = FilterUtil.buildFilter(null, reportId);
            AmpARFilter newFilters = null;

            // Convert json object back to AmpReportFilters
            Map<String, Object> filterMap = (Map<String, Object>) formParams.get(EPConstants.FILTERS);
            if (filterMap != null) {
                AmpReportFilters reportFilters = FilterUtils.getFilters(filterMap, new AmpReportFilters());

                // Transform back to legacy AmpARFilters.
                AmpReportFiltersConverter converter = new AmpReportFiltersConverter(reportFilters);
                newFilters = converter.buildFilters();
                // converter.mergeWithOldFilters(oldFilters);

                if (formParams.getString("sidx") != null && !formParams.getString("sidx").equals("")) {
                    formParams.set(EPConstants.SORTING, convertJQgridSortingParams(formParams));
                    logger.info(formParams.get(EPConstants.SORTING));
                    newFilters.setSortByAsc(formParams.getString("sord").equals("asc") ? true : false);

                    String columns = "";
                    for (Map map : ((List<Map>) formParams.get(EPConstants.SORTING))) {
                        String column = map.get("columns").toString();
                        column = column.substring(column.indexOf("[") + 1, column.indexOf("]"));
                        columns += ("/" + column);
                    }
                    newFilters.setSortBy(columns);
                }

                if (formParams.get(EPConstants.SETTINGS) != null) {
                    String currency = ((LinkedHashMap<String, Object>) formParams.get(EPConstants.SETTINGS)).get(SettingsConstants.CURRENCY_ID).toString();
                    String calendar = ((LinkedHashMap<String, Object>) formParams.get(EPConstants.SETTINGS)).get(SettingsConstants.CALENDAR_TYPE_ID).toString();
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
            
            Session session = PersistenceManager.getSession();          
            List<Map<String, String>> reportData = (List<Map<String, String>>) formParams.get("reportData");
            boolean emptyDefaultName = true;
            String defaultLang = TLSUtils.getEffectiveLangCode();
            for (Map<String, String> name : reportData) {
                if (defaultLang != null && defaultLang.equals(name.get("lang")) && !"".equals(name.get("name"))) {
                    emptyDefaultName = false;
                }
            }
            if(!emptyDefaultName) {
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
     * a front end for {@link MultilingualInputFieldValues#readTranslationsFromRequest(Class, long, String, String, HttpServletRequest)}
     * co-evolve the two routines!
     * @param reportData
     * @param reportId
     * @return
     */
    private Map<String, AmpContentTranslation> populateContentTranslations (List<Map<String,String>> reportData, long reportId) {
        List<Pair<String, String>> rawData = new ArrayList<>();
        for (Map<String,String> langAndName : reportData) {
            if(StringUtils.isNotEmpty(langAndName.get("name"))) {
                String locale = langAndName.get("lang");
                rawData.add(new Pair<>(locale, langAndName.get("name")));
            }
            
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
    
    public Map<String, Object> getColorSettings(Set<ReportColumn> reportColumns) {
        
        // columns that will be used for coloring and should be hidden in saiku if are not present in report specification
        Set<String> hiddenColumnNames = new HashSet<String>();
        hiddenColumnNames.add(ColumnConstants.APPROVAL_STATUS);
        hiddenColumnNames.add(ColumnConstants.DRAFT);

        for (ReportColumn rc : reportColumns) {
            hiddenColumnNames.remove(rc.getColumnName());
        }

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
//      res.put("CHECK_TIME", delta);
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Class getErrorsClass() {
        return ReportErrors.class;
    }
}
