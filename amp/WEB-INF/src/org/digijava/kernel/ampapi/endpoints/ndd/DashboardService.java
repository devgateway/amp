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
import java.util.Arrays;
import java.util.HashSet;
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

    private static GeneratedReport createReport(ReportColumn[] columns, ReportMeasure measure,
                                                AmpReportFilters filterRules, Map<String, Object> settings,
                                                boolean isSummary) {
        ReportSpecificationImpl spec = new ReportSpecificationImpl("" + Math.random(), ArConstants.DONOR_TYPE);
        spec.setSummaryReport(isSummary);
        spec.setGroupingCriteria(GroupingCriteria.GROUPING_YEARLY);
        spec.setEmptyOutputForUnspecifiedData(true);
        spec.setDisplayEmptyFundingColumns(false);
        spec.setDisplayEmptyFundingRows(false);
        spec.setDisplayEmptyFundingRowsWhenFilteringByTransactionHierarchy(false);

        Arrays.stream(columns).forEach(reportColumn -> {
            spec.addColumn(reportColumn);
        });

        spec.addMeasure(measure);

        if (isSummary) {
            spec.setHierarchies(spec.getColumns());
        } else {
            ReportColumn[] newArray = Arrays.copyOf(columns, columns.length - 1);
            Set<ReportColumn> hierarchies = new HashSet<ReportColumn>(Arrays.asList(newArray));
            spec.setHierarchies(hierarchies);
        }

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
     * Note: for simplicity we assume "program" can be of level 1 or 2 only.
     *
     * @param program
     * @param filters
     */
    private static void addFilterFromProgram(final AmpTheme program, AmpReportFilters filters, boolean dontUseMapping) {
        Set<AmpActivityProgramSettings> programSettings = null;
        if (program.getIndlevel() == 1) {
            programSettings = program.getParentThemeId().getProgramSettings();
        } else {
            programSettings = program.getParentThemeId().getParentThemeId().getProgramSettings();
        }
        if (programSettings == null || programSettings.size() != 1) {
            throw new RuntimeException("Cant determine the filter for the report.");
        }

        boolean isIndirect;
        IndirectProgramMappingConfiguration indirectMapping = nddService.getIndirectProgramMappingConfiguration();
        ProgramMappingConfiguration regularMapping = nddService.getProgramMappingConfiguration();
        AmpTheme rootProgram = getProgramByLvl(program, 0);
        if ((rootProgram.getAmpThemeId().equals(indirectMapping.getDstProgram().getId()) ||
                rootProgram.getAmpThemeId().equals(indirectMapping.getSrcProgram().getId()))
                && indirectMapping.getDstProgram().isIndirect()) {
            isIndirect = true;
        } else {
            isIndirect = false;
        }
        AmpActivityProgramSettings singleProgramSetting = ((AmpActivityProgramSettings) programSettings.toArray()[0]);
        ReportColumn fromMappingFilterColumn = null;
        if (singleProgramSetting.getName().equalsIgnoreCase(ColumnConstants.PRIMARY_PROGRAM)) {
            fromMappingFilterColumn = new ReportColumn(ColumnConstants.PRIMARY_PROGRAM_LEVEL_3);
        } else if (singleProgramSetting.getName().equalsIgnoreCase(ColumnConstants.SECONDARY_PROGRAM)) {
            fromMappingFilterColumn = new ReportColumn(ColumnConstants.SECONDARY_PROGRAM_LEVEL_3);
        } else if (singleProgramSetting.getName().equalsIgnoreCase(ColumnConstants.TERTIARY_PROGRAM)) {
            fromMappingFilterColumn = new ReportColumn(ColumnConstants.TERTIARY_PROGRAM_LEVEL_3);
        } else if (singleProgramSetting.getName().equalsIgnoreCase(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES)
                || singleProgramSetting.getName().equalsIgnoreCase(ProgramUtil.NATIONAL_PLAN_OBJECTIVE)) {
            if (dontUseMapping) {
                ReportColumn mainFilterColumn = new ReportColumn(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_1);
                filters.addFilterRule(mainFilterColumn, new FilterRule(program.getAmpThemeId().toString(), true));
            } else {
                fromMappingFilterColumn = new ReportColumn(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_3);
            }
        } else if (singleProgramSetting.getName().equalsIgnoreCase(ProgramUtil.INDIRECT_PRIMARY_PROGRAM)) {
            if (program.getIndlevel() == 1) {
                ReportColumn mainFilterColumn = new ReportColumn(ColumnConstants.INDIRECT_PRIMARY_PROGRAM_LEVEL_1);
                filters.addFilterRule(mainFilterColumn, new FilterRule(program.getAmpThemeId().toString(), true));
            } else {
                ReportColumn mainFilterColumn = new ReportColumn(ColumnConstants.INDIRECT_PRIMARY_PROGRAM_LEVEL_2);
                filters.addFilterRule(mainFilterColumn, new FilterRule(program.getAmpThemeId().toString(), true));
            }
        }

        // Add filter by Program with ids from NDD mapping. Only add ids from clicked program.
        List<String> fromMappingIds = new ArrayList<>();
        if (isIndirect) {
            indirectMapping.getProgramMapping().forEach(item -> {
                if (program.getIndlevel() == 1) {
                    if (getProgramByLvl(item.getOldTheme(), 1).getAmpThemeId().equals(program.getAmpThemeId())) {
                        fromMappingIds.add(item.getOldTheme().getAmpThemeId().toString());
                    }
                } else {
                    if (getProgramByLvl(item.getOldTheme(), 1).getAmpThemeId()
                            .equals(getProgramByLvl(program, 1).getAmpThemeId())) {
                        if (getProgramByLvl(item.getOldTheme(), 2).getAmpThemeId().equals(program.getAmpThemeId())) {
                            fromMappingIds.add(item.getOldTheme().getAmpThemeId().toString());
                        }
                    }
                }
            });
        } else {
            regularMapping.getProgramMapping().forEach(item -> {
                if (getProgramByLvl(item.getSrcTheme(), 1).getAmpThemeId().equals(program.getAmpThemeId())) {
                    fromMappingIds.add(item.getSrcTheme().getAmpThemeId().toString());
                }
            });
        }

        if (fromMappingFilterColumn != null) {
            if (fromMappingIds.size() == 0) {
                throw new RuntimeException("Filter ids cant be empty.");
            }
            filters.addFilterRule(fromMappingFilterColumn, new FilterRule(fromMappingIds, true));
        }
    }

    private static AmpTheme getProgramByLvl(AmpTheme program, int lvl) {
        AmpTheme root = program;
        while (root.getIndlevel() > lvl) {
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

    /**
     * Iterate outerReport and innerReport to match 1 direct object with N indirect objects.
     * Since "indirect" programs are hidden on the AF we rely on the NDD mapping to manage them each time an activity
     * is saved, but NDD mapping can change over time and activities could be out of sync so here we will also check
     * with the current NDD mapping to do the mix.
     *
     * @param outerReport
     * @param innerReport
     * @param isIndirect
     * @param mapping
     * @return
     */
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
            outerReport.reportContents.getChildren().forEach(children -> {
                Map<ReportOutputColumn, ReportCell> outerContent = children.getContents();
                NDDSolarChartData nddSolarChartData = new NDDSolarChartData(null, new ArrayList<>());
                AtomicBoolean add = new AtomicBoolean();
                add.set(false);

                innerReport.reportContents.getChildren().forEach(children2 -> {
                    Map<ReportOutputColumn, ReportCell> innerContentFirstColumn = children2.getContents();
                    children2.getChildren().forEach(children3 -> {
                        Map<ReportOutputColumn, ReportCell> innerContentSecondColumn = children3.getContents();
                        if (outerContent.get(outerReportProgramColumn).displayedValue
                                .equals(innerContentSecondColumn.get(outerReportProgramColumn).displayedValue)) {
                            List mapped = getMapped(isIndirect, mapping, outerContent, outerReportProgramColumn);
                            if (mapped.size() == 1) {
                                AmpTheme oldTheme = isIndirect
                                        ? ((AmpIndirectTheme) mapped.get(0)).getOldTheme()
                                        : ((AmpThemeMapping) mapped.get(0)).getSrcTheme();
                                AmpTheme newTheme = isIndirect
                                        ? ((AmpIndirectTheme) mapped.get(0)).getNewTheme()
                                        : ((AmpThemeMapping) mapped.get(0)).getDstTheme();
                                if (innerContentFirstColumn.get(innerReportProgramColumn).displayedValue
                                        .equals(newTheme.getName())
                                        && innerContentSecondColumn.get(outerReportProgramColumn).displayedValue
                                        .equals(oldTheme.getName())) {
                                    add.set(true);
                                    BigDecimal amount = ((AmountCell) innerContentSecondColumn
                                            .get(innerReportTotalColumn)).extractValue();
                                    Map<String, BigDecimal> amountsByYear =
                                            extractAmountsByYear(innerContentSecondColumn);
                                    ReportCell innerCell = innerContentFirstColumn.get(innerReportProgramColumn);
                                    AmpTheme innerTheme = ProgramUtil.getTheme(((TextCell) innerCell).entityId);
                                    nddSolarChartData.getIndirectPrograms()
                                            .add(new NDDSolarChartData.ProgramData(innerTheme, amount, amountsByYear));
                                }
                            }
                        }
                    });
                });

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

    private static List<DetailByYear> processDetailForDirectData(final GeneratedReport report, int year) {
        List<DetailByYear> list = new ArrayList<>();
        ReportOutputColumn projectColumn = report.leafHeaders.get(0);

        if (report.reportContents != null && report.reportContents.getChildren() != null) {
            report.reportContents.getChildren().forEach(children -> {
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

    private static List<DetailByYear> processDetailForIndirectData(final GeneratedReport report, final int year,
                                                                   final AmpTheme program) {
        List<DetailByYear> list = new ArrayList<>();
        ReportOutputColumn directColumn = report.leafHeaders.get(0);
        ReportOutputColumn indirectColumn = report.leafHeaders.get(1);
        ReportOutputColumn projectColumn = report.leafHeaders.get(2);

        MappingConfiguration indirectMapping = nddService.getIndirectProgramMappingConfiguration();
        MappingConfiguration regularMapping = nddService.getProgramMappingConfiguration();

        if (report.reportContents != null && report.reportContents.getChildren() != null) {
            report.reportContents.getChildren().forEach(children -> {
                Map<ReportOutputColumn, ReportCell> contentsCol1 = children.getContents();
                children.getChildren().forEach(children2 -> {
                    Map<ReportOutputColumn, ReportCell> contentsCol2 = children2.getContents();
                    TextCell cellProgram = (TextCell) contentsCol2.get(indirectColumn);
                    AmpTheme auxProgram = ProgramUtil.getTheme(cellProgram.entityId);
                    if (auxProgram != null) {
                        AmpTheme auxRootProgram = getProgramByLvl(auxProgram, 1);
                        if (program.getAmpThemeId().equals(auxRootProgram.getAmpThemeId())) {
                            children2.getChildren().forEach(children3 -> {
                                List mapped = getMapped(true, indirectMapping, contentsCol1, directColumn);
                                if (mapped.size() == 1) {
                                    // TODO: check if line this works for ndd 2nd tab (program mapping).
                                    List mappedForThisProgram = (List) mapped.stream().filter(m -> {
                                        AmpIndirectTheme ampIndirectTheme = (AmpIndirectTheme) m;
                                        return getProgramByLvl(ampIndirectTheme.getNewTheme(), 1).getAmpThemeId()
                                                .equals(program.getAmpThemeId());
                                    }).collect(Collectors.toList());
                                    if (mappedForThisProgram.size() > 0) {
                                        if (mappedForThisProgram.stream().filter(m -> {
                                            AmpIndirectTheme ampIndirectTheme = (AmpIndirectTheme) m;
                                            return ampIndirectTheme.getOldTheme().getName()
                                                    .equals(contentsCol1.get(directColumn).displayedValue);
                                        }).toArray().length > 0) {
                                            Map<ReportOutputColumn, ReportCell> contents3 = children3.getContents();
                                            TextCell cell = ((TextCell) contents3.get(projectColumn));
                                            BigDecimal amount = extractAmountsByYear(contents3).get("" + year);
                                            if (amount != null) {
                                                DetailByYear detailRecord = new DetailByYear(cell.entityId,
                                                        cell.displayedValue, amount);
                                                list.add(detailRecord);
                                            }
                                        }
                                    }
                                }
                            });
                        }
                    }
                });
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
            outerReport = createReport(new ReportColumn[]{outerColumn}, outerMeasure, filters,
                    params.getSettings(), true);

            AmpTheme innerProgram = ProgramUtil.getTheme(Long.valueOf(ids.get(1)));
            ReportColumn innerColumn = getColumnFromProgram(innerProgram);
            ReportMeasure innerMeasure = outerMeasure;
            innerReport = createReport(new ReportColumn[]{innerColumn, outerColumn}, innerMeasure, filters,
                    params.getSettings(), true);

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
            outerReport = createReport(new ReportColumn[]{outerColumn}, outerMeasure, filters,
                    params.getSettings(), true);
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
                // Note: dont compare ">0" because there are negative funding :|
                if (amount.doubleValue() != 0) {
                    amountsByYear.put(m.group(), amount);
                }
            }
        });
        return amountsByYear;
    }

    /**
     * Return detail with list of activities for a given program.
     *
     * @param params
     * @return
     */
    public static List getActivityDetailReport(SettingsAndFiltersParameters params) {
        GeneratedReport report;
        int yearString = Integer.parseInt(params.getSettings().get("year").toString());
        String programIdString = params.getSettings().get("id").toString();
        AmpReportFilters filters = getFiltersFromParams(params.getFilters());
        AmpTheme program = ProgramUtil.getTheme(Long.valueOf(programIdString));
        ReportColumn projectTitleColumn = new ReportColumn(ColumnConstants.PROJECT_TITLE);
        if (params.getSettings().get("isShowIndirectDataForActivitiesDetail").toString().equals("true")) {
            MappingConfiguration indirectMapping = nddService.getIndirectProgramMappingConfiguration();
            AmpTheme directProgram = ProgramUtil.getTheme(indirectMapping.getSrcProgram().getId());
            ReportColumn outerColumn = getColumnFromProgram(directProgram);
            AmpTheme rootProgram = getProgramByLvl(program, 0);
            ReportColumn innerColumn = getColumnFromProgram(rootProgram);
            ReportMeasure innerMeasure = getMeasureFromParams(params.getSettings());
            ReportColumn[] columns = {outerColumn, innerColumn, projectTitleColumn};
            report = createReport(columns, innerMeasure, filters, params.getSettings(), false);
            return processDetailForIndirectData(report, yearString, program);
        } else {
            boolean dontUseMapping = params.getSettings().get("dontUseMapping").toString().equals("true");
            addFilterFromProgram(program, filters, dontUseMapping);
            ReportMeasure outerMeasure = getMeasureFromParams(params.getSettings());
            report = createReport(new ReportColumn[]{projectTitleColumn}, outerMeasure,
                    filters, params.getSettings(), true);
            return processDetailForDirectData(report, yearString);
        }
    }
}
