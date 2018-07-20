package org.digijava.kernel.ampapi.endpoints.gis.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.AmountsUnits;
import org.dgfoundation.amp.newreports.AmpReportFilters;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.IdentifiedReportCell;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSettingsImpl;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.newreports.SortingInfo;
import org.dgfoundation.amp.newreports.pagination.PaginatedReport;
import org.dgfoundation.amp.nireports.amp.OutputSettings;
import org.dgfoundation.amp.onepager.util.ActivityGatekeeper;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.dto.SimpleJsonBean;
import org.digijava.kernel.ampapi.endpoints.gis.GisFormParameters;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.exception.AmpApiException;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.util.SectorUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ActivityService {
    protected static Logger logger = Logger.getLogger(ActivityService.class);
    private static final Map<String, String> columnHeaders;
    private static final Set<String> columnsToProvide;
     static
        {
         columnHeaders = new HashMap<String, String>();
         columnHeaders.put(ColumnConstants.PROJECT_TITLE,"Title" );
         columnHeaders.put(ColumnConstants.DONOR_AGENCY, "Donor Agency");
         columnHeaders.put(ColumnConstants.EXECUTING_AGENCY, "Executing Agency");
         columnHeaders.put(ColumnConstants.ACTIVITY_UPDATED_ON, "Date");
         
         columnsToProvide = new HashSet<String>();
         columnsToProvide.add(ColumnConstants.AMP_ID);
         columnsToProvide.add(ColumnConstants.PROJECT_TITLE);
         columnsToProvide.add(MeasureConstants.ACTUAL_COMMITMENTS);
         columnsToProvide.add(MeasureConstants.ACTUAL_DISBURSEMENTS);
         columnsToProvide.add(MeasureConstants.PLANNED_COMMITMENTS);
         columnsToProvide.add(MeasureConstants.PLANNED_DISBURSEMENTS);
         columnsToProvide.add(MeasureConstants.BILATERAL_SSC_COMMITMENTS);
         columnsToProvide.add(MeasureConstants.TRIANGULAR_SSC_COMMITMENTS);
        }

    public static JsonBean getActivities(GisFormParameters config, List<String> activitIds, Integer page,
            Integer pageSize) {
        boolean applyFilter=false;
        List<JsonBean> activities=new ArrayList<JsonBean>();
        
        //we check if we have filter by keyword
        LinkedHashMap<String, Object> filters = null;
        if (config != null) {
            filters = (LinkedHashMap<String, Object>) config.getFilters();
            if (activitIds == null) {
                activitIds = new ArrayList<>();
            }
            activitIds.addAll(FilterUtils.applyKeywordSearch(filters));
        }
        
        
        String name= "ActivityList";
        boolean doTotals=true;
        ReportSpecificationImpl spec = new ReportSpecificationImpl(name, ArConstants.DONOR_TYPE);

        spec.addColumn(new ReportColumn(ColumnConstants.AMP_ID));
        spec.addColumn(new ReportColumn(ColumnConstants.PROJECT_TITLE));
        spec.addColumn(new ReportColumn(ColumnConstants.DONOR_AGENCY));
        spec.addColumn(new ReportColumn(ColumnConstants.EXECUTING_AGENCY));
        spec.addColumn(new ReportColumn(ColumnConstants.PRIMARY_SECTOR));
        
        OutputSettings outSettings = new OutputSettings(new HashSet<String>() {{
            add(ColumnConstants.AMP_ID);
            add(ColumnConstants.DONOR_AGENCY);
            add(ColumnConstants.EXECUTING_AGENCY);
            add(ColumnConstants.PRIMARY_SECTOR);
        }});
        
        //for now we are going to return the donor_id as matchesfilters
        
        //then we have to fetch all other matchesfilters outisde mondrian

        // apply default settings
        SettingsUtils.applySettings(spec, config.getSettings(), true);
        // apply custom settings
        SettingsUtils.configureMeasures(spec, config.getSettings());
        
        // AMP-19772: Needed to avoid problems on GIS js. 
        spec.setDisplayEmptyFundingRows(true);
        
        ReportSettingsImpl mrs = (ReportSettingsImpl) spec.getSettings();
        mrs.setUnitsOption(AmountsUnits.AMOUNTS_OPTION_UNITS);

        AmpReportFilters filterRules = FilterUtils.getFilterRules(filters, activitIds);
        if (filterRules != null) {
            GisUtils.configurePerformanceFilter(config, filterRules);
            spec.setFilters(filterRules);
        }
        GeneratedReport report = EndpointUtils.runReport(spec, ReportAreaImpl.class, outSettings);
        //if pagination is requested
        List<ReportArea> ll=null;
        if (page != null && pageSize != null && page >= 0 && pageSize > 0) {
            ReportArea pagedReport = PaginatedReport.getPage(report.reportContents, page, pageSize);
            ll = pagedReport.getChildren();
        }else{ 
            ll = report.reportContents.getChildren();
        }
        Integer count=report.reportContents.getChildren().size();

        for (ReportArea reportArea : ll) {
            JsonBean activity = new JsonBean();
            JsonBean matchesFilters = new JsonBean();
            Map<ReportOutputColumn, ReportCell> row = reportArea.getContents();
            Set<ReportOutputColumn> col = row.keySet();
            for (ReportOutputColumn reportOutputColumn : col) {
                //Filters should be grouped together.
                
                if (columnsToProvide.contains(reportOutputColumn.originalColumnName)) {
                    activity.set(reportOutputColumn.originalColumnName, row.get(reportOutputColumn).value);
                    if (reportOutputColumn.originalColumnName.equals(ColumnConstants.AMP_ID)) {
                        long activityId = ((IdentifiedReportCell) row.get(reportOutputColumn)).entityId;
                        activity.set(ColumnConstants.ACTIVITY_ID, activityId);
                        activity.set("ampUrl", ActivityGatekeeper.buildPreviewUrl(String.valueOf(activityId)));
                    }
                } else {
                    IdentifiedReportCell idReportCell = (IdentifiedReportCell) row.get(reportOutputColumn);
                    Set<Long> ids = idReportCell.entitiesIdsValues == null ? null : idReportCell.entitiesIdsValues.keySet();
                    if (reportOutputColumn.originalColumnName.equals(ColumnConstants.PRIMARY_SECTOR)) {
                        List<SimpleJsonBean> sectors = new ArrayList<>();
                        for (Long id : ids) {
                            AmpSector ampSector = SectorUtil.getAmpSector(id);
                            SimpleJsonBean sector = new SimpleJsonBean();
                            sector.setId(ampSector.getAmpSectorId());
                            sector.setCode(ampSector.getSectorCodeOfficial());
                            sector.setName(ampSector.getName());                        
                            sectors.add(sector);
                        }                       
                        matchesFilters.set(reportOutputColumn.originalColumnName, sectors);
                    } else {
                        matchesFilters.set(reportOutputColumn.originalColumnName, ids);
                    }
                }
            }
            activity.set("matchesFilters", matchesFilters);
            activities.add(activity);
        }
        JsonBean list = new JsonBean();
        list.set("count", count);
        list.set("activities", activities);
        return list;
    }

    /**
     * 
     * @param extraColumns
     * @param pageSize
     * @param config
     * @return
     */

    public static JSONObject getLastUpdatedActivities(List<String> extraColumns, Integer pageSize,
            GisFormParameters config) {
    JSONObject responseJson = new JSONObject();
    if (pageSize == null) {
        pageSize = new Integer(10);
    }
    ReportSpecificationImpl spec = new ReportSpecificationImpl("LastUpdated", ArConstants.DONOR_TYPE);
    spec.addColumn(new ReportColumn(ColumnConstants.ACTIVITY_UPDATED_ON));
    spec.addColumn(new ReportColumn(ColumnConstants.PROJECT_TITLE));
    spec.addColumn(new ReportColumn(ColumnConstants.DONOR_AGENCY));
    for (String columnName : extraColumns) {
        if (!columnName.equals(ColumnConstants.ACTIVITY_UPDATED_ON)
                && !columnName.equals(ColumnConstants.DONOR_AGENCY)
                && !columnName.equals(ColumnConstants.PROJECT_TITLE)) {
        spec.addColumn(new ReportColumn(columnName));

        }
    }

    spec.addMeasure(new ReportMeasure(MeasureConstants.ALWAYS_PRESENT));
    spec.addSorter(new SortingInfo(new ReportColumn(ColumnConstants.ACTIVITY_UPDATED_ON), false));
    
    if (config != null) {
        SettingsUtils.applySettings(spec, config.getSettings(), true);
        FilterUtils.applyFilterRules(config.getFilters(), spec, null);
    }
    GeneratedReport report = EndpointUtils.runReport(spec);
    
    //ReportAreaMultiLinked[] areasDFArray = ReportPaginationUtils.convert(report.reportContents);
    ReportArea pagedReport = PaginatedReport.getPage(report.reportContents, 0, pageSize);
    JSONArray activities = new JSONArray();
    JSONArray headers = new JSONArray();
    
        if (pagedReport != null) {
            List<ReportArea> area = pagedReport.getChildren();

            boolean headerAdded = false;
            for (Iterator<ReportArea> iterator = area.iterator(); iterator.hasNext();) {
                JSONObject activityObj = new JSONObject();
                JSONObject header = new JSONObject();
                ReportArea reportArea = iterator.next();

                Map<ReportOutputColumn, ReportCell> row = reportArea.getContents();
                Set<ReportOutputColumn> col = row.keySet();
                for (ReportOutputColumn reportOutputColumn : col) {

                    if (!reportOutputColumn.originalColumnName.equals(MeasureConstants.ALWAYS_PRESENT)) {
                        activityObj.put(reportOutputColumn.originalColumnName,
                                row.get(reportOutputColumn).displayedValue);
                        String displayName = columnHeaders.get(reportOutputColumn.originalColumnName);
                        if (displayName == null) {
                            displayName = reportOutputColumn.originalColumnName;
                        }
                        displayName = TranslatorWorker.translateText(displayName);
                        header.put(reportOutputColumn.originalColumnName, displayName);
                    }

                }

                if (!headerAdded) {
                    headers.add(header);
                }
                activities.add(activityObj);
                headerAdded = true;
            }
        }
    responseJson.put("activities", activities);
    responseJson.put("headers", headers);
    return responseJson;

    }
}
