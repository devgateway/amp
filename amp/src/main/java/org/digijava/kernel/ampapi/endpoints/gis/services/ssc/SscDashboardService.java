package org.digijava.kernel.ampapi.endpoints.gis.services.ssc;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.newreports.AmpReportFilters;
import org.dgfoundation.amp.newreports.DateCell;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.dto.gis.SscDashboardActivity;
import org.digijava.kernel.ampapi.endpoints.dto.gis.SscDashboardObject;
import org.digijava.kernel.ampapi.endpoints.dto.gis.SscDashboardObjectFactory;
import org.digijava.kernel.ampapi.endpoints.dto.gis.SscDashboardResult;
import org.digijava.kernel.ampapi.endpoints.dto.gis.ssc.SscDashboardXlsResult;
import org.digijava.kernel.ampapi.endpoints.dto.gis.ssc.SscDashboardXlsResultRow;
import org.digijava.kernel.ampapi.endpoints.reports.ReportFormParameters;
import org.digijava.kernel.ampapi.endpoints.reports.ReportsUtil;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.module.common.util.DateTimeUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class SscDashboardService {
    private SscDashboardService() {

    }

    public static SscDashboardXlsResult getSscDashboardXlsResult(ReportFormParameters formParameters) {
        SscDashboardXlsResult result = new SscDashboardXlsResult();
        ReportSpecificationImpl spec = new ReportSpecificationImpl("SccXlsExport", ArConstants.DONOR_TYPE);
        generateCommonColumns(spec);
        spec.addColumn(new ReportColumn(ColumnConstants.PROJECT_TITLE));

        AmpReportFilters filterRules = FilterUtils.getFilterRules(formParameters.getFilters(), null);
        if (filterRules != null) {
            spec.setFilters(filterRules);
        }

        List<Integer> years = null;
        if (formParameters.getAdditionalInfo() != null && formParameters.getAdditionalInfo().size() > 0) {
            years = formParameters.getAdditionalInfo().stream().mapToInt(Integer::parseInt).collect(ArrayList::new,
                    ArrayList::add, ArrayList::addAll);
        }
        ReportsUtil.configureSSCWorkspaceFilter(spec, true);
        GeneratedReport report = EndpointUtils.runReport(spec);
        for (ReportArea reportArea : report.reportContents.getChildren()) {
            SscDashboardXlsResultRow r = new SscDashboardXlsResultRow();

            Map<ReportOutputColumn, ReportCell> row = reportArea.getContents();
            Set<ReportOutputColumn> columns = row.keySet();
            Long actualStartDate = null;
            Long proposedStartDate = null;
            for (ReportOutputColumn cell : columns) {
                ReportCell c = row.get(cell);
                if (cell.originalColumnName.equals(ColumnConstants.PROJECT_TITLE)) {
                    r.setActivity(c.displayedValue);
                } else {
                    if (cell.originalColumnName.equals(ColumnConstants.PRIMARY_SECTOR)) {
                        r.setSector(c.displayedValue);
                    } else {
                        if (cell.originalColumnName.equals(ColumnConstants.SSC_MODALITIES)) {
                            r.setModality(c.displayedValue);
                        } else {
                            if (cell.originalColumnName.equals(ColumnConstants.DONOR_COUNTRY)) {
                                r.setCountry(c.displayedValue);
                            } else {

                                if (cell.originalColumnName.equals(ColumnConstants.ACTUAL_START_DATE)) {
                                    actualStartDate = ((DateCell) row.get(cell)).entityId;
                                } else {
                                    if (cell.originalColumnName.equals(ColumnConstants.PROPOSED_START_DATE)) {
                                        proposedStartDate = ((DateCell) row.get(cell)).entityId;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Integer year = getYear(actualStartDate, proposedStartDate);
            if (year == null) {
                continue;
            } else {
                r.setYear(year);
            }
            if (years == null || years.contains(year)) {
                result.getSscDashboardXlsResultRows().add(r);
            }

        }

        result.getSscDashboardXlsResultRows()
                .sort(Comparator.comparing(SscDashboardXlsResultRow::getYear).reversed()
                        .thenComparing(SscDashboardXlsResultRow::getSector));
        return result;
    }

    private static Integer getYear(Long actualStartDate, Long proposedStartDate) {
        if (actualStartDate == null && proposedStartDate == null) {
            return null;
        } else {
            if (actualStartDate == null) {
                return DateTimeUtil.getYearFromJulianNumber(proposedStartDate);
            } else {
                if (proposedStartDate == null) {
                    return DateTimeUtil.getYearFromJulianNumber(actualStartDate);
                } else {
                    if (proposedStartDate < actualStartDate) {
                        return DateTimeUtil.getYearFromJulianNumber(proposedStartDate);
                    } else {
                        return DateTimeUtil.getYearFromJulianNumber(actualStartDate);
                    }
                }
            }
        }
    }

    private static void generateCommonColumns(ReportSpecificationImpl spec) {
        spec.addColumn(new ReportColumn(ColumnConstants.DONOR_COUNTRY));
        spec.addColumn(new ReportColumn(ColumnConstants.PRIMARY_SECTOR));
        spec.addColumn(new ReportColumn(ColumnConstants.SSC_MODALITIES));
        spec.addColumn(new ReportColumn(ColumnConstants.ACTUAL_START_DATE));
        spec.addColumn(new ReportColumn(ColumnConstants.PROPOSED_START_DATE));
    }

    public static SscDashboardResult getSscDashboardResult() {
        SscDashboardResult result = new SscDashboardResult();
        ReportSpecificationImpl spec = new ReportSpecificationImpl("SccDashboard", ArConstants.DONOR_TYPE);
        generateCommonColumns(spec);

        spec.getHierarchies().add(new ReportColumn(ColumnConstants.DONOR_COUNTRY));
        spec.getHierarchies().add(new ReportColumn(ColumnConstants.PRIMARY_SECTOR));
        spec.getHierarchies().add(new ReportColumn(ColumnConstants.SSC_MODALITIES));

        ReportsUtil.configureSSCWorkspaceFilter(spec, true);
        GeneratedReport report = EndpointUtils.runReport(spec);

        if (report != null) {
            for (ReportArea reportArea : report.reportContents.getChildren()) {
                SscDashboardObject countries = SscDashboardObjectFactory.
                        getSscDashboardObject(reportArea.getOwner().columnName, reportArea.getOwner().id);
                List<ReportArea> sectorsReportArea;
                sectorsReportArea = reportArea.getChildren();
                for (ReportArea sectorReportArea : sectorsReportArea) {
                    SscDashboardObject sectors = SscDashboardObjectFactory.
                            getSscDashboardObject(sectorReportArea.getOwner().columnName,
                                    sectorReportArea.getOwner().id);
                    List<ReportArea> modalities = sectorReportArea.getChildren();
                    for (ReportArea modalitiesReportArea : modalities) {
                        SscDashboardObject modality = SscDashboardObjectFactory.
                                getSscDashboardObject(modalitiesReportArea.getOwner().columnName,
                                        modalitiesReportArea.getOwner().id);
                        for (ReportArea activitiesReportArea : modalitiesReportArea.getChildren()) {
                            Long actualStartDate = null;
                            Long proposedStartDate = null;
                            SscDashboardActivity da = new SscDashboardActivity(activitiesReportArea.getOwner().id);
                            Map<ReportOutputColumn, ReportCell> activityRow = activitiesReportArea.getContents();
                            Set<ReportOutputColumn> activityColumns = activityRow.keySet();
                            for (ReportOutputColumn activitiesCls : activityColumns) {
                                DateCell cellActivity = (DateCell) activityRow.get(activitiesCls);
                                if (activitiesCls.originalColumnName.equals(ColumnConstants.ACTUAL_START_DATE)) {
                                    actualStartDate = cellActivity.entityId;
                                } else {
                                    if (activitiesCls.originalColumnName.equals(ColumnConstants.PROPOSED_START_DATE)) {
                                        proposedStartDate = cellActivity.entityId;
                                    }
                                }
                            }
                            Integer year = getYear(actualStartDate, proposedStartDate);
                            if (year == null) {
                                continue;
                            } else {
                                da.setYear(year);
                            }
                            result.getActivitiesId().add(activitiesReportArea.getOwner().id);
                            if (da.getYear() > result.getMostRecentYear()) {
                                result.setMostRecentYear(da.getYear());
                            }
                            modality.getChildren().add(da);
                        }
                        if (modality.getChildren().size() > 0) {
                            sectors.getChildren().add(modality);
                        }
                    }
                    if (sectors.getChildren().size() > 0) {
                        countries.getChildren().add(sectors);
                    }
                }
                if (countries.getChildren().size() > 0) {
                    result.getChildren().add(countries);
                }
            }
        }
        return result;
    }
}
