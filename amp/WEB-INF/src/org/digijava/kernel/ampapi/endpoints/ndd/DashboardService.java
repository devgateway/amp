package org.digijava.kernel.ampapi.endpoints.ndd;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.FilterRule;
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
import org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpIndirectTheme;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.AmpThemeMapping;
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
import java.util.stream.Collectors;

public final class DashboardService {

    private static Pattern numberPattern = Pattern.compile("\\d{4}");
    private static NDDService nddService = new NDDService();

    private DashboardService() {
    }

    private static GeneratedReport createReport(ReportColumn column, ReportMeasure measure,
                                                AmpReportFilters filterRules, Map<String, Object> settings) {
        ReportSpecificationImpl spec = new ReportSpecificationImpl("" + Math.random(), ArConstants.DONOR_TYPE);
        spec.setSummaryReport(true);
        spec.setGroupingCriteria(GroupingCriteria.GROUPING_YEARLY);
        spec.setEmptyOutputForUnspecifiedData(true);
        spec.setDisplayEmptyFundingColumns(false);
        spec.setDisplayEmptyFundingRows(false);
        spec.setDisplayEmptyFundingRowsWhenFilteringByTransactionHierarchy(false);

        spec.addColumn(column);
        spec.addMeasure(measure);
        spec.setHierarchies(spec.getColumns());

        if (filterRules != null) {
            spec.setFilters(filterRules);
        }

        if (settings != null) {
            SettingsUtils.applyExtendedSettings(spec, settings);
        }

        GeneratedReport report = EndpointUtils.runReport(spec);
        return report;
    }

    /**
     * Add the program as a new filter option to the correct category (primary program, secondary program, etc).
     *
     * @param program
     * @param filters
     */
    private static void addFilterFromProgram(final AmpTheme program, AmpReportFilters filters) {
        Set<AmpActivityProgramSettings> programSettings = null;
        if (program.getIndlevel() == 1) {
            programSettings = program.getParentThemeId().getProgramSettings();
        } else {
            programSettings = program.getParentThemeId().getParentThemeId().getProgramSettings();
        }
        if (programSettings == null || programSettings.size() != 1) {
            throw new RuntimeException("Cant determine the filter for the report.");
        }

        boolean isIndirect = false;
        MappingConfiguration mapping = null;
        MappingConfiguration indirectMapping = nddService.getIndirectProgramMappingConfiguration();
        MappingConfiguration regularMapping = nddService.getProgramMappingConfiguration();
        AmpTheme rootProgram = getRootProgram(program);
        if ((rootProgram.getAmpThemeId().equals(indirectMapping.getDstProgram().getId()) ||
                rootProgram.getAmpThemeId().equals(indirectMapping.getSrcProgram().getId()))
                && indirectMapping.getDstProgram().isIndirect()) {
            mapping = indirectMapping;
            isIndirect = true;
        } else {
            mapping = regularMapping;
            isIndirect = false;
        }

        AmpActivityProgramSettings singleProgramSetting = ((AmpActivityProgramSettings) programSettings.toArray()[0]);
        ReportColumn column = null;
        if (singleProgramSetting.getName().equalsIgnoreCase(ColumnConstants.PRIMARY_PROGRAM)) {
            if (program.getIndlevel() == 1) {
                column = new ReportColumn(ColumnConstants.PRIMARY_PROGRAM_LEVEL_1);
            } else {
                column = new ReportColumn(ColumnConstants.PRIMARY_PROGRAM_LEVEL_2);
            }
        } else if (singleProgramSetting.getName().equalsIgnoreCase(ColumnConstants.SECONDARY_PROGRAM)) {
            if (program.getIndlevel() == 1) {
                column = new ReportColumn(ColumnConstants.SECONDARY_PROGRAM_LEVEL_1);
            } else {
                column = new ReportColumn(ColumnConstants.SECONDARY_PROGRAM_LEVEL_2);
            }
        } else if (singleProgramSetting.getName().equalsIgnoreCase(ColumnConstants.TERTIARY_PROGRAM)) {
            if (program.getIndlevel() == 1) {
                column = new ReportColumn(ColumnConstants.TERTIARY_PROGRAM_LEVEL_1);
            } else {
                column = new ReportColumn(ColumnConstants.TERTIARY_PROGRAM_LEVEL_2);
            }
        } else if (singleProgramSetting.getName().equalsIgnoreCase(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES)
                || singleProgramSetting.getName().equalsIgnoreCase(ProgramUtil.NATIONAL_PLAN_OBJECTIVE)) {
            if (program.getIndlevel() == 1) {
                column = new ReportColumn(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_1);
            } else {
                column = new ReportColumn(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_2);
            }
        } else if (singleProgramSetting.getName().equalsIgnoreCase(ProgramUtil.INDIRECT_PRIMARY_PROGRAM)) {
            if (program.getIndlevel() == 1) {
                column = new ReportColumn(ColumnConstants.INDIRECT_PRIMARY_PROGRAM_LEVEL_1);
            } else {
                column = new ReportColumn(ColumnConstants.INDIRECT_PRIMARY_PROGRAM_LEVEL_2);
            }
        }
        filters.addFilterRule(column, new FilterRule(program.getAmpThemeId().toString(), true));
    }

    private static AmpTheme getRootProgram(AmpTheme program) {
        AmpTheme root = program;
        while(root.getIndlevel() > 0) {
            root = root.getParentThemeId();
        }
        return root;
    }

    private static ReportColumn getColumnFromProgram(AmpTheme program) {
        Set<AmpActivityProgramSettings> programSettings = program.getProgramSettings();
        if (programSettings == null || programSettings.size() != 1) {
            throw new RuntimeException("Cant determine the first column of the report.");
        }
        AmpActivityProgramSettings singleProgramSetting = ((AmpActivityProgramSettings) programSettings.toArray()[0]);
        if (singleProgramSetting.getName().equalsIgnoreCase(ColumnConstants.PRIMARY_PROGRAM)) {
            return new ReportColumn(ColumnConstants.PRIMARY_PROGRAM_LEVEL_3);
        } else if (singleProgramSetting.getName().equalsIgnoreCase(ColumnConstants.SECONDARY_PROGRAM)) {
            return new ReportColumn(ColumnConstants.SECONDARY_PROGRAM_LEVEL_3);
        } else if (singleProgramSetting.getName().equalsIgnoreCase(ColumnConstants.TERTIARY_PROGRAM)) {
            return new ReportColumn(ColumnConstants.TERTIARY_PROGRAM_LEVEL_3);
        } else if (singleProgramSetting.getName().equalsIgnoreCase(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES)
                || singleProgramSetting.getName().equalsIgnoreCase(ProgramUtil.NATIONAL_PLAN_OBJECTIVE)) {
            return new ReportColumn(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_3);
        } else if (singleProgramSetting.getName().equalsIgnoreCase(ProgramUtil.INDIRECT_PRIMARY_PROGRAM)) {
            return new ReportColumn(ColumnConstants.INDIRECT_PRIMARY_PROGRAM_LEVEL_3);
        }
        return null;
    }

    private static ReportMeasure getMeasureFromParams(Map<String, Object> settings) {
        if (settings != null && settings.get(SettingsConstants.FUNDING_TYPE_ID) != null) {
            return new ReportMeasure(settings.get(SettingsConstants.FUNDING_TYPE_ID).toString());
        } else {
            return new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS);
        }
    }

    private static AmpReportFilters getFiltersFromParams(Map<String, Object> filters) {
        AmpReportFilters filterRules = new AmpReportFilters();
        if (filters != null) {
            filterRules = FilterUtils.getFilterRules(filters, null);
        }
        return filterRules;
    }

    private static List<NDDSolarChartData> processTwo(final GeneratedReport outerReport,
                                                      final GeneratedReport innerReport,
                                                      final boolean isIndirect,
                                                      final MappingConfiguration mapping) {
        List<NDDSolarChartData> list = new ArrayList<>();
        ReportOutputColumn outerReportProgramColumn = outerReport.leafHeaders.get(0);
        ReportOutputColumn innerReportProgramColumn = innerReport.leafHeaders.get(0);
        ReportOutputColumn outerReportTotalColumn = outerReport.leafHeaders.get(outerReport.leafHeaders.size() - 1);
        ReportOutputColumn innerReportTotalColumn = innerReport.leafHeaders.get(innerReport.leafHeaders.size() - 1);

        if (outerReport.reportContents != null && outerReport.reportContents.getChildren() != null
                && innerReport.reportContents != null && innerReport.reportContents.getChildren() != null) {
            outerReport.reportContents.getChildren().stream().forEach(children -> {
                Map<ReportOutputColumn, ReportCell> outerContent = children.getContents();
                NDDSolarChartData nddSolarChartData = new NDDSolarChartData(null, new ArrayList<>());
                AtomicBoolean add = new AtomicBoolean();
                add.set(false);

                List mapped = getMapped(isIndirect, mapping, outerContent, outerReportProgramColumn);
                if (mapped.size() > 0) {
                    mapped.forEach(m -> {
                        AmpTheme newTheme = isIndirect
                                ? ((AmpIndirectTheme) m).getNewTheme()
                                : ((AmpThemeMapping) m).getDstTheme();
                        innerReport.reportContents.getChildren().stream().forEach(children2 -> {
                            Map<ReportOutputColumn, ReportCell> innerContent = children2.getContents();
                            ReportCell innerCell = innerContent.get(innerReportProgramColumn);
                            AmpTheme innerTheme = ProgramUtil.getTheme(((TextCell) innerCell).entityId);
                            if (innerTheme != null && newTheme.getAmpThemeId().equals(innerTheme.getAmpThemeId())) {
                                add.set(true);
                                BigDecimal amount = ((AmountCell) innerContent.get(innerReportTotalColumn))
                                        .extractValue();
                                Map<String, BigDecimal> amountsByYear = extractAmountsByYear(innerContent);
                                nddSolarChartData.getIndirectPrograms()
                                        .add(new NDDSolarChartData.ProgramData(innerTheme, amount, amountsByYear));
                            }
                        });
                    });
                } else {
                    // TODO: add not mapped outer as undefined only if the outer itself is not an undefined row.
                    System.out.println("To be implemented");
                }

                if (add.get()) {
                    AmpTheme direct = ProgramUtil.getTheme(((TextCell) outerContent.get(outerReportProgramColumn))
                            .entityId);
                    BigDecimal amount = ((AmountCell) outerContent.get(outerReportTotalColumn)).extractValue();
                    Map<String, BigDecimal> amountsByYear = extractAmountsByYear(outerContent);
                    nddSolarChartData.setDirectProgram(new NDDSolarChartData.ProgramData(direct, amount,
                            amountsByYear));
                    list.add(nddSolarChartData);
                }
            });
        }
        return list;
    }

    private static List<NDDSolarChartData> processOne(final GeneratedReport outerReport) {
        List<NDDSolarChartData> list = new ArrayList<>();
        ReportOutputColumn outerReportProgramColumn = outerReport.leafHeaders.get(0);
        ReportOutputColumn outerReportTotalColumn = outerReport.leafHeaders.get(outerReport.leafHeaders.size() - 1);

        if (outerReport.reportContents != null && outerReport.reportContents.getChildren() != null) {
            outerReport.reportContents.getChildren().stream().forEach(children -> {
                Map<ReportOutputColumn, ReportCell> outerContent = children.getContents();
                NDDSolarChartData nddSolarChartData = new NDDSolarChartData(null, new ArrayList<>());
                AmpTheme direct = ProgramUtil.getTheme(((TextCell) outerContent.get(outerReportProgramColumn))
                        .entityId);
                if (direct != null) {
                    BigDecimal amount = ((AmountCell) outerContent.get(outerReportTotalColumn)).extractValue();
                    Map<String, BigDecimal> amountsByYear = extractAmountsByYear(outerContent);
                    NDDSolarChartData.ProgramData programData = new NDDSolarChartData.ProgramData(direct, amount,
                            amountsByYear);
                    // Ignore programs we dont want to show.
                    if (programData.getAmount() != null) {
                        nddSolarChartData.setDirectProgram(programData);
                        list.add(nddSolarChartData);
                    }
                } else {
                    // TODO: implement for undefined row.
                    System.out.println("To be implemented");
                }
            });
        }
        return list;
    }

    private static List<DetailByYear> processDetail(final GeneratedReport report, int year) {
        List<DetailByYear> list = new ArrayList<>();
        ReportOutputColumn projectColumn = report.leafHeaders.get(0);

        if (report.reportContents != null && report.reportContents.getChildren() != null) {
            report.reportContents.getChildren().stream().forEach(children -> {
                Map<ReportOutputColumn, ReportCell> contents = children.getContents();
                TextCell cell = ((TextCell) contents.get(projectColumn));
                BigDecimal amount = extractAmountsByYear(contents).get("" + year);
                if (amount != null) {
                    DetailByYear detailRecord = new DetailByYear(cell.entityId, cell.displayedValue, amount);
                    list.add(detailRecord);
                }
            });
        }
        return list;
    }

    public static List<NDDSolarChartData> generateDirectIndirectReport(SettingsAndFiltersParameters params) {
        List<NDDSolarChartData> list = new ArrayList<>();
        MappingConfiguration mapping;
        boolean isIndirect = false;

        GeneratedReport outerReport;
        GeneratedReport innerReport;

        List<String> ids = (ArrayList<String>) params.getSettings().get("programIds");
        if (ids == null) {
            return list;
        }
        AmpReportFilters filters = getFiltersFromParams(params.getFilters());
        if (ids.size() == 2) {
            AmpTheme outerProgram = ProgramUtil.getTheme(Long.valueOf(ids.get(0)));
            ReportColumn outerColumn = getColumnFromProgram(outerProgram);
            ReportMeasure outerMeasure = getMeasureFromParams(params.getSettings());
            outerReport = createReport(outerColumn, outerMeasure, filters, params.getSettings());

            AmpTheme innerProgram = ProgramUtil.getTheme(Long.valueOf(ids.get(1)));
            ReportColumn innerColumn = getColumnFromProgram(innerProgram);
            ReportMeasure innerMeasure = outerMeasure;
            innerReport = createReport(innerColumn, innerMeasure, filters, params.getSettings());

            // TODO: maybe do a "normalization" here to get the common programMapping.
            MappingConfiguration indirectMapping = nddService.getIndirectProgramMappingConfiguration();
            MappingConfiguration regularMapping = nddService.getProgramMappingConfiguration();
            if (innerProgram.getAmpThemeId().equals(indirectMapping.getDstProgram().getId())
                    && indirectMapping.getDstProgram().isIndirect()) {
                mapping = indirectMapping;
                isIndirect = true;
            } else {
                mapping = regularMapping;
            }
            return processTwo(outerReport, innerReport, isIndirect, mapping);
        } else if (ids.size() == 1) {
            AmpTheme outerProgram = ProgramUtil.getTheme(Long.valueOf(ids.get(0)));
            ReportColumn outerColumn = getColumnFromProgram(outerProgram);
            ReportMeasure outerMeasure = getMeasureFromParams(params.getSettings());
            outerReport = createReport(outerColumn, outerMeasure, filters, params.getSettings());
            return processOne(outerReport);
        } else {
            throw new RuntimeException("Error number of ids in settings parameter.");
        }
    }

    private static List getMapped(boolean isIndirect, MappingConfiguration mapping, Map<ReportOutputColumn,
            ReportCell> outerContent, ReportOutputColumn outerReportProgramColumn) {
        List mapped;
        if (isIndirect) {
            mapped = ((IndirectProgramMappingConfiguration) mapping).getProgramMapping().stream()
                    .filter(i -> {
                        return i.getOldTheme().getName()
                                .equalsIgnoreCase(outerContent.get(outerReportProgramColumn).displayedValue);
                    }).collect(Collectors.toList());
        } else {
            mapped = ((ProgramMappingConfiguration) mapping).getProgramMapping().stream().filter(i -> {
                return i.getSrcTheme().getName().equalsIgnoreCase(outerContent.get(outerReportProgramColumn)
                        .displayedValue);
            }).collect(Collectors.toList());
        }
        return mapped;
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

    public static List getActivityDetailReport(SettingsAndFiltersParameters params) {
        GeneratedReport report;
        int yearString = Integer.parseInt(params.getSettings().get("year").toString());
        String programIdString = params.getSettings().get("id").toString();
        AmpReportFilters filters = getFiltersFromParams(params.getFilters());
        AmpTheme program = ProgramUtil.getTheme(Long.valueOf(programIdString));
        addFilterFromProgram(program, filters);
        ReportColumn projectTitleColumn = new ReportColumn(ColumnConstants.PROJECT_TITLE);
        ReportMeasure outerMeasure = getMeasureFromParams(params.getSettings());
        report = createReport(projectTitleColumn, outerMeasure, filters, params.getSettings());
        return processDetail(report, yearString);
    }
}
