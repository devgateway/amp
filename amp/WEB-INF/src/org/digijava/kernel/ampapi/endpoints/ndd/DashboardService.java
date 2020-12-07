package org.digijava.kernel.ampapi.endpoints.ndd;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.TextCell;
import org.dgfoundation.amp.newreports.AmountCell;
import org.dgfoundation.amp.newreports.AmpReportFilters;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.gis.SettingsAndFiltersParameters;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsConstants;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.util.ProgramUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DashboardService {

    private static Pattern numberPattern = Pattern.compile("\\d{4}");

    private DashboardService() {
    }

    public static List<NDDSolarChartData> generateDirectIndirectReport(SettingsAndFiltersParameters params) {
        ReportSpecificationImpl spec = new ReportSpecificationImpl("DirectIndirect", ArConstants.DONOR_TYPE);
        spec.setSummaryReport(true);
        spec.setGroupingCriteria(GroupingCriteria.GROUPING_YEARLY);
        spec.setEmptyOutputForUnspecifiedData(true);
        spec.setDisplayEmptyFundingColumns(false);
        spec.setDisplayEmptyFundingRows(false);
        spec.setDisplayEmptyFundingRowsWhenFilteringByTransactionHierarchy(false);

        AmpTheme directProgram = NDDService.getSrcProgramRoot();
        Set<AmpActivityProgramSettings> programSettings = directProgram.getProgramSettings();
        if (programSettings == null || programSettings.size() != 1) {
            throw new RuntimeException("Cant determine the first column of the report.");
        }
        AmpActivityProgramSettings singleProgramSetting = ((AmpActivityProgramSettings) programSettings.toArray()[0]);
        if (singleProgramSetting.getName().equalsIgnoreCase(ColumnConstants.PRIMARY_PROGRAM)) {
            spec.addColumn(new ReportColumn(ColumnConstants.PRIMARY_PROGRAM_LEVEL_3));
        } else if (singleProgramSetting.getName().equalsIgnoreCase(ColumnConstants.SECONDARY_PROGRAM)) {
            spec.addColumn(new ReportColumn(ColumnConstants.SECONDARY_PROGRAM_LEVEL_3));
        } else if (singleProgramSetting.getName().equalsIgnoreCase(ColumnConstants.TERTIARY_PROGRAM)) {
            spec.addColumn(new ReportColumn(ColumnConstants.TERTIARY_PROGRAM_LEVEL_3));
        } else if (singleProgramSetting.getName().equalsIgnoreCase(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES)) {
            spec.addColumn(new ReportColumn(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_3));
        }
        spec.addColumn(new ReportColumn(ColumnConstants.INDIRECT_PRIMARY_PROGRAM_LEVEL_3));
        spec.setHierarchies(spec.getColumns());

        if (params.getSettings() != null && params.getSettings().get(SettingsConstants.FUNDING_TYPE_ID) != null) {
            spec.addMeasure(new ReportMeasure(params.getSettings().get(SettingsConstants.FUNDING_TYPE_ID).toString()));
        } else {
            spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS));
        }

        if (params.getFilters() != null) {
            AmpReportFilters filterRules = FilterUtils.getFilterRules(params.getFilters(), null);
            if (filterRules != null) {
                spec.setFilters(filterRules);
            }
        }

        GeneratedReport report = EndpointUtils.runReport(spec);

        List<NDDSolarChartData> list = new ArrayList<>();
        ReportOutputColumn directColumn = report.leafHeaders.get(0);
        ReportOutputColumn indirectColumn = report.leafHeaders.get(1);
        ReportOutputColumn totalColumn = report.leafHeaders.get(report.leafHeaders.size() - 1);

        if (report.reportContents != null && report.reportContents.getChildren() != null) {
            report.reportContents.getChildren().stream().forEach(children -> {
                if (children.getChildren() != null && children.getChildren().size() > 0) {
                    Map<ReportOutputColumn, ReportCell> content = children.getContents();
                    NDDSolarChartData nddSolarChartData = new NDDSolarChartData(null, new ArrayList<>());
                    AtomicBoolean add = new AtomicBoolean();
                    children.getChildren().forEach(children2 -> {
                        add.set(false);
                        Map<ReportOutputColumn, ReportCell> content2 = children2.getContents();
                        ReportCell programCell = children2.getContents().get(indirectColumn);
                        if (((TextCell) programCell).entityId > -1) {
                            add.set(true);
                            AmpTheme indirect = ProgramUtil.getTheme(((TextCell) programCell).entityId);
                            BigDecimal amount = ((AmountCell) content2.get(totalColumn)).extractValue();
                            Map<String, BigDecimal> amountsByYear = extractAmountsByYear(content2);
                            nddSolarChartData.getIndirectPrograms().add(new NDDSolarChartData.ProgramData(indirect,
                                    amount, amountsByYear));
                        }
                    });
                    if (add.get()) {
                        AmpTheme direct = ProgramUtil.getTheme(((TextCell) content.get(directColumn)).entityId);
                        BigDecimal amount = ((AmountCell) content.get(totalColumn)).extractValue();
                        Map<String, BigDecimal> amountsByYear = extractAmountsByYear(content);
                        nddSolarChartData.setDirectProgram(new NDDSolarChartData.ProgramData(direct, amount,
                                amountsByYear));
                        list.add(nddSolarChartData);
                    }
                }
            });
        }
        return list;
    }

    private static Map<String, BigDecimal> extractAmountsByYear(Map<ReportOutputColumn, ReportCell> content) {
        Map<String, BigDecimal> amountsByYear = new HashMap<>();
        content.entrySet().forEach(entry -> {
            // Ignore columns from report that are not funding by year.
            Matcher m = numberPattern.matcher(entry.getKey().toString());
            if (m.find()) {
                BigDecimal amount = ((AmountCell) entry.getValue()).extractValue();
                if (amount.doubleValue() > 0) {
                    amountsByYear.put(m.group(), amount);
                }
            }
        });
        return amountsByYear;
    }
}
