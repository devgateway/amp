package org.digijava.kernel.ampapi.endpoints.publicportal;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.newreports.SortingInfo;
import org.dgfoundation.amp.reports.ActivityType;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponse;
import org.digijava.kernel.ampapi.endpoints.errors.ApiRuntimeException;
import org.digijava.kernel.ampapi.endpoints.gis.SettingsAndFiltersParameters;
import org.digijava.kernel.ampapi.endpoints.publicportal.dto.PublicTotalsByMeasure;
import org.digijava.kernel.ampapi.endpoints.reports.ReportsUtil;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsConstants;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


/**
 * Public Portal Service
 *
 * @author Nadejda Mandrescu
 */
public class PublicPortalService {
    protected static final Logger logger = Logger.getLogger(PublicPortalService.class);

    /**
     * Retrieves top 'count' projects based on fixed requirements. <br>
     * NOTE: the requirement is fixed at the moment, however we may need to provide some flexibility
     * => JsonBean config can be used for that
     *
     * @param config      - report config
     * @param count       - the maximum number of results to retrieve
     * @param lastUpdated - should order by updated date
     * @return JsonBean object with results
     */
    public static PublicTopData getTopProjects(
            PublicReportFormParameters config, Integer count, Integer months, boolean lastUpdated) {

        Set<String> rawMeasures = new HashSet<>();
        rawMeasures.add(MeasureConstants.ACTUAL_COMMITMENTS);
        rawMeasures.add(MeasureConstants.ACTUAL_DISBURSEMENTS);

        ApiErrorResponse error = ReportsUtil.validateReportConfig(config);
        if (error != null) {
            throw new ApiRuntimeException(error);
        }

        ReportSpecificationImpl spec = EndpointUtils.getReportSpecification(config.getReportType(),
                "PublicPortal_GetTopProjects");

        SettingsUtils.applySettings(spec, config.getSettings(), true);

        applyFilterRules(config, spec, months);
        // configure project types
        ReportsUtil.configureProjectTypes(spec, config.getProjectType());
        // do we need to include empty fundings in case no fundings are detected at all? 
        // normally, with healthy data, they are not visible due to the sorting rule
        spec.setDisplayEmptyFundingRows(true);
        List<String> projectTypeOptions = config.getProjectType();
        boolean isSSCActivitiesTop =
                projectTypeOptions != null && projectTypeOptions.contains(ActivityType.SSC_ACTIVITY.toString());
        Set<String> columnsToIgnore = new HashSet<>();

        if (isSSCActivitiesTop) {
            configureTopSSCProjects(spec, lastUpdated);
            if (lastUpdated) {
                columnsToIgnore.add(MeasureConstants.CUMULATED_SSC_COMMITMENTS);
            }
        } else {
            configureTopStandardProjects(spec, lastUpdated);
        }

        return getPublicReport(count, spec, false, null, columnsToIgnore, rawMeasures);
    }

    /**
     * Requirement has a fixed output structure:
     * Start Date,
     * Donor(s),
     * Primary Sector(s),
     * Project Title,
     * Actual Commitments,
     * Actual Disbursements.
     *
     * @param spec report specification to configure
     */
    private static void configureTopStandardProjects(ReportSpecificationImpl spec, boolean lastUpdated) {
        spec.addColumn(new ReportColumn(ColumnConstants.ACTUAL_START_DATE));
        spec.addColumn(new ReportColumn(ColumnConstants.ACTIVITY_ID));
        spec.addColumn(new ReportColumn(ColumnConstants.DONOR_AGENCY));
        spec.addColumn(new ReportColumn(ColumnConstants.PRIMARY_SECTOR));
        spec.addColumn(new ReportColumn(ColumnConstants.PROJECT_TITLE));

        spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS));
        spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_DISBURSEMENTS));
        if (lastUpdated) {
            spec.addColumn(new ReportColumn(ColumnConstants.ACTIVITY_UPDATED_ON));
            spec.addSorter(new SortingInfo(new ReportColumn(ColumnConstants.ACTIVITY_UPDATED_ON), false));
        } else {
            spec.addSorter(new SortingInfo(new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS), false));
        }
    }

    /**
     * Requirements has a fixed output structure:
     * Project Title,
     * Component Name
     * Donor Agency
     * Executing Agency
     * Bilateral SSC Commitments
     * Triangular SSC Commitments
     *
     * @param spec
     */
    private static void configureTopSSCProjects(ReportSpecificationImpl spec, boolean lastUpdated) {
        spec.addColumn(new ReportColumn(ColumnConstants.PROJECT_TITLE));
        spec.addColumn(new ReportColumn(ColumnConstants.COMPONENT_NAME));
        spec.addColumn(new ReportColumn(ColumnConstants.DONOR_AGENCY));
        spec.addColumn(new ReportColumn(ColumnConstants.EXECUTING_AGENCY));

        spec.addMeasure(new ReportMeasure(MeasureConstants.BILATERAL_SSC_COMMITMENTS));
        spec.addMeasure(new ReportMeasure(MeasureConstants.TRIANGULAR_SSC_COMMITMENTS));
        // using Cumulated SSC Commitments for sorting order
        if (lastUpdated) {
            spec.addColumn(new ReportColumn(ColumnConstants.ACTIVITY_UPDATED_ON));
            spec.addSorter(new SortingInfo(new ReportColumn(ColumnConstants.ACTIVITY_UPDATED_ON), false));
        } else {
            spec.addMeasure(new ReportMeasure(MeasureConstants.CUMULATED_SSC_COMMITMENTS));
            spec.addSorter(new SortingInfo(new ReportMeasure(MeasureConstants.CUMULATED_SSC_COMMITMENTS), false));
        }
    }


    /**
     * @param config
     * @param count       max amount of records
     * @param months
     * @param fundingType 1 for commitment 2 for disbursements
     * @return
     */
    public static PublicTopData getDonorFunding(PublicReportFormParameters config, Integer count,
                                                Integer months, Integer fundingType, boolean showDonorGroup) {
        String measureName = null;

        ReportSpecificationImpl spec = new ReportSpecificationImpl("PublicPortal_GetDonorFunding",
                ArConstants.DONOR_TYPE);
        if (showDonorGroup) {
            spec.addColumn(new ReportColumn(ColumnConstants.DONOR_GROUP));
        } else {
            spec.addColumn(new ReportColumn(ColumnConstants.DONOR_AGENCY));
        }
        spec.setHierarchies(spec.getColumns());
        if (fundingType == 1) {
            spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS));
            measureName = MeasureConstants.ACTUAL_COMMITMENTS;
            spec.addSorter(new SortingInfo(new ReportMeasure(
                    MeasureConstants.ACTUAL_COMMITMENTS), false));

        } else {
            if (fundingType == 2) {
                measureName = MeasureConstants.ACTUAL_DISBURSEMENTS;
                spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_DISBURSEMENTS));
                spec.addSorter(new SortingInfo(new ReportMeasure(MeasureConstants.ACTUAL_DISBURSEMENTS), false));
            }
        }

        applyFilterRules(config, spec, months);

        SettingsUtils.applySettings(spec, config.getSettings(), true);
        return getPublicReport(count, spec, true, measureName, null, null);
    }

    /**
     * Generates the report and retrieves 'count' results
     *
     * @param count             the number of records to show
     * @param spec              report specification
     * @param calculateSubTotal if we should calculate the subtotal for the measure
     * @param measureName       Measure to use for subTotal -  we may need to receive an array of measures
     * @param columnsToIgnore   a list of columns to not include into the output
     */
    private static PublicTopData getPublicReport(Integer count,
                                                 ReportSpecificationImpl spec, boolean calculateSubTotal,
                                                 String measureName,
                                                 Set<String> columnsToIgnore,
                                                 Set<String> measureAsRawNumber) {
        GeneratedReport report = EndpointUtils.runReport(spec);

        String numberFormat = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.NUMBER_FORMAT);
        String currency = spec.getSettings().getCurrencyCode();

        Map<String, String> headers = new LinkedHashMap<>();
        Map<String, BigDecimal> totals = new LinkedHashMap<>();
        List<Map<String, String>> topData = new ArrayList<>();

        if (report != null
                && report.reportContents != null && report.reportContents.getChildren() != null) {
            // provide header titles by order Id
            Map<String, String> headersToId = new HashMap<>(report.leafHeaders.size());
            Iterator<ReportColumn> colIter = spec.getColumns().iterator();
            for (ReportOutputColumn leafHeader : report.leafHeaders) {
                final String columnName = colIter.hasNext()
                        ? colIter.next().getColumnName() : leafHeader.originalColumnName;
                if (columnsToIgnore != null && columnsToIgnore.contains(columnName))
                    continue;
                final String id = StringUtils.replace(StringUtils.lowerCase(columnName), " ", "-");
                headers.put(id, leafHeader.columnName);
                headersToId.put(leafHeader.columnName, id);
            }
            // provide the top projects data
            if (count != null) {
                count = Math.min(count, report.reportContents.getChildren().size());
            } else {
                count = report.reportContents.getChildren().size();
            }

            BigDecimal total = new BigDecimal(0);
            Iterator<ReportArea> iter = report.reportContents.getChildren().iterator();
            int countCounter = count;
            while (countCounter > 0) {
                ReportArea data = iter.next();
                Map<String, String> rowData = new LinkedHashMap<>();
                if (data.getContents().size() > 1) {
                    for (Entry<ReportOutputColumn, ReportCell> cell : data.getContents().entrySet()) {
                        if (columnsToIgnore == null || !columnsToIgnore.contains(cell.getKey().columnName)) {
                            rowData.put(headersToId.get(cell.getKey().columnName), cell.getValue().displayedValue);
                            if (measureAsRawNumber != null
                                    && measureAsRawNumber.contains(cell.getKey().originalColumnName)) {
                                rowData.put(headersToId.get(cell.getKey().columnName) + "-raw",
                                        cell.getValue().value.toString());
                                if (!headers.containsKey(headersToId.get(cell.getKey().columnName) + "-raw")) {
                                    headers.put(headersToId.get(cell.getKey().columnName) + "-raw",
                                            headersToId.get(cell.getKey().columnName) + "-raw");
                                }
                            }
                            if (calculateSubTotal) {
                                if (cell.getKey().columnName.equals(measureName)) {
                                    total = total.add((BigDecimal) cell.getValue().value);
                                }
                            }
                        }
                    }
                }
                topData.add(rowData);
                countCounter--;
            }
            totals.put("Total " + measureName, total);
        }
        count = count == null ? 0 : count;

        return new PublicTopData(headers, totals, topData, count, numberFormat, currency);
    }

    public static int getActivitiesCount(Map<String, Object> filters, boolean isPledge) {
        ReportSpecificationImpl spec = new ReportSpecificationImpl("PublicPortal_activitiesPledgesCount",
                ArConstants.DONOR_TYPE);
        spec.addColumn(new ReportColumn(ColumnConstants.ACTIVITY_ID));
        if (isPledge) {
            spec.addColumn(new ReportColumn(ColumnConstants.RELATED_PLEDGES));
            spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS));
        }

        applyFilterRules(filters, spec, null);

        spec.setDisplayEmptyFundingRows(true);

        GeneratedReport report = EndpointUtils.runReport(spec);
        int count = 0;

        if (report != null) {
            for (ReportArea reportArea : report.reportContents.getChildren()) {
                Map<ReportOutputColumn, ReportCell> row = reportArea.getContents();
                Set<ReportOutputColumn> col = row.keySet();

                for (ReportOutputColumn reportOutputColumn : col) {
                    String columnValue = row.get(reportOutputColumn).displayedValue;
                    if ((ColumnConstants.RELATED_PLEDGES.equals(reportOutputColumn.originalColumnName) && isPledge)
                            || (ColumnConstants.ACTIVITY_ID.equals(reportOutputColumn.originalColumnName)
                            && !isPledge)) {
                        if (!columnValue.equals("")) {
                            count++;
                        }
                    }
                }

            }
        }
        return count;
    }

    /**
     * Apply or append rules if config is not null.
     * See {@link FilterUtils#applyFilterRules(Map, ReportSpecificationImpl, Integer)}.
     *
     * @param config JsonBean containing filters object
     * @param spec   report specification
     * @param months filter by last N months, may be null
     */
    private static void applyFilterRules(PublicReportFormParameters config, ReportSpecificationImpl spec,
                                         Integer months) {
        if (config != null) {
            FilterUtils.applyFilterRules(config.getFilters(), spec, months);
        }
    }

    private static void applyFilterRules(Map<String, Object> filters, ReportSpecificationImpl spec,
                                         Integer months) {
        if (filters != null) {
            FilterUtils.applyFilterRules(filters, spec, months);
        }
    }

    public static PublicTotalsByMeasure getCountByMeasure(SettingsAndFiltersParameters config) {
        PublicTotalsByMeasure result = new PublicTotalsByMeasure();
        result.setMeasure("Total Activities");
        result.setCount(PublicPortalService.getActivitiesCount(config != null ? config.getFilters() : null, false));
        return result;
    }

    public static PublicTotalsByMeasure getTotalByMeasure(SettingsAndFiltersParameters config) {
        PublicTotalsByMeasure result = new PublicTotalsByMeasure();
        ReportSpecificationImpl spec = new ReportSpecificationImpl("PublicPortal_getTotalByMeasure",
                ArConstants.DONOR_TYPE);
        // applies settings, including funding type as a measure
        SettingsUtils.applyExtendedSettings(spec, config.getSettings());
        ReportsUtil.configureFilters(spec, config.getFilters());

        result.setCurrency((String) config.getSettings().get(SettingsConstants.CURRENCY_ID));
        spec.setSummaryReport(true);
        GeneratedReport report = EndpointUtils.runReport(spec);
        Map<ReportOutputColumn, ReportCell> totals = report.reportContents.getContents();
        totals.entrySet().forEach(c -> {
            result.setTotal((BigDecimal) c.getValue().value);
            result.setMeasure(c.getKey().originalColumnName);
        });
        return result;
    }
} 
