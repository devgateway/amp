package org.digijava.kernel.ampapi.endpoints.ndd;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportArea;
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
import org.digijava.kernel.ampapi.endpoints.ndd.utils.FlattenProgramRecord;
import org.digijava.kernel.ampapi.endpoints.ndd.utils.FlattenTwoProgramsRecord;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsConstants;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.util.ProgramUtil;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class DashboardService {
    private static Logger logger = Logger.getLogger(DashboardService.class);

    private static Pattern numberPattern = Pattern.compile("\\d{4}");
    private static NDDService nddService = new NDDService();
    public static final Integer COLUMN_0 = 0;
    public static final Integer COLUMN_1 = 1;
    public static final Integer COLUMN_2 = 2;
    public static final Integer COLUMN_3 = 3;
    public static final Integer COLUMN_4 = 4;
    public static final Integer COLUMN_5 = 5;

    private DashboardService() {
    }

    private static GeneratedReport createReport(List<ReportColumn> columns, ReportMeasure measure,
                                                AmpReportFilters filterRules, Map<String, Object> settings,
                                                boolean isSummary) {
        ReportSpecificationImpl spec = new ReportSpecificationImpl("" + Math.random(), ArConstants.DONOR_TYPE);
        spec.setSummaryReport(isSummary);
        spec.setGroupingCriteria(GroupingCriteria.GROUPING_YEARLY);
        spec.setEmptyOutputForUnspecifiedData(true);
        spec.setDisplayEmptyFundingColumns(false);
        spec.setDisplayEmptyFundingRows(false);
        spec.setDisplayEmptyFundingRowsWhenFilteringByTransactionHierarchy(false);

        columns.forEach(spec::addColumn);

        spec.addMeasure(measure);

        if (isSummary) {
            spec.setHierarchies(spec.getColumns());
        } else {
            ((List) columns).remove(columns.size() - 1);
            Set<ReportColumn> hierarchies = new LinkedHashSet<ReportColumn>(columns);
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
     * Given a program get its parent from level @lvl.
     *
     * @param program
     * @param lvl
     * @return
     */
    private static AmpTheme getProgramByLvl(AmpTheme program, int lvl) {
        AmpTheme root = program;
        while (root.getIndlevel() > lvl) {
            root = root.getParentThemeId();
        }
        return root;
    }

    private static String getProgramConstant(String prefix, int index) {
        try {
            Field field = ColumnConstants.class.getField(prefix + index);
            return field.get(null).toString();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            logger.error(e.getLocalizedMessage());
        }
        return null;
    }

    /**
     * Given a @program with level 0 (main root) return the report columns
     * linked to the program in the MultiProgram Configuration Manager (ie: PP, SP, NPO, etc).
     *
     * @param program
     * @return
     */
    private static List<ReportColumn> getColumnsFromProgram(AmpTheme program, int levels) {
        List<ReportColumn> list = new ArrayList<>();
        Set<AmpActivityProgramSettings> programSettings = program.getProgramSettings();
        if (programSettings == null || programSettings.size() != 1) {
            throw new RuntimeException("Cant determine the first column of the report.");
        }
        AmpActivityProgramSettings singleProgramSetting = ((AmpActivityProgramSettings) programSettings.toArray()[0]);
        String prefix = null;
        if (singleProgramSetting.getName().equalsIgnoreCase(ColumnConstants.PRIMARY_PROGRAM)) {
            prefix = "PRIMARY_PROGRAM_LEVEL_";
        } else if (singleProgramSetting.getName().equalsIgnoreCase(ColumnConstants.SECONDARY_PROGRAM)) {
            prefix = "SECONDARY_PROGRAM_LEVEL_";
        } else if (singleProgramSetting.getName().equalsIgnoreCase(ColumnConstants.TERTIARY_PROGRAM)) {
            prefix = "TERTIARY_PROGRAM_LEVEL_";
        } else if (singleProgramSetting.getName().equalsIgnoreCase(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES)
                || singleProgramSetting.getName().equalsIgnoreCase(ProgramUtil.NATIONAL_PLAN_OBJECTIVE)) {
            prefix = "NATIONAL_PLANNING_OBJECTIVES_LEVEL_";
        } else if (singleProgramSetting.getName().equalsIgnoreCase(ProgramUtil.INDIRECT_PRIMARY_PROGRAM)) {
            prefix = "INDIRECT_PRIMARY_PROGRAM_LEVEL_";
        }
        if (prefix != null) {
            for (int i = 1; i <= levels; i++) {
                list.add(new ReportColumn(getProgramConstant(prefix, i)));
            }
            return list;
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
     * Convert a report with N levels of programs (of the same type) and amounts by year to a flat structure with just
     * one program (the lowest level AmpTheme that's not Undefined) and the amounts.
     *
     * @param report
     * @param list
     * @param program
     * @param level
     * @param area
     * @param maxLevels
     */
    private static void flattenOneColumnReport(GeneratedReport report, List<FlattenProgramRecord> list,
                                               AmpTheme program, int level, ReportArea area, int maxLevels) {
        ReportOutputColumn totalCol = report.leafHeaders.get(report.leafHeaders.size() - 1);
        if (area.getChildren() != null && level < maxLevels) {
            for (ReportArea child : area.getChildren()) {
                Map<ReportOutputColumn, ReportCell> content = child.getContents();
                TextCell cell = (TextCell) content.get(report.leafHeaders.get(level));
                AmpTheme auxPrg = getThemeById(cell.entityId);
                if (auxPrg != null
                        && (auxPrg.getIndlevel() < maxLevels || auxPrg.getIndlevel() == maxLevels && maxLevels == 1)) {
                    program = auxPrg;
                } else {
                    if (program != null && level == 0) {
                        // Reset program or we could add an extra row for undefined as last record
                        // but with the previous program name (wrong).
                        program = null;
                    }
                }
                flattenOneColumnReport(report, list, program, level + 1, child, maxLevels);
            }
        } else {
            if (program != null) {
                Map<ReportOutputColumn, ReportCell> content = area.getContents();
                TextCell cell = (TextCell) content.get(report.leafHeaders.get(level - 1));
                AmpTheme auxProg = getThemeById(cell.entityId);
                if (auxProg == null) {
                    auxProg = program;
                }
                BigDecimal amount = ((AmountCell) content.get(totalCol)).extractValue();
                list.add(new FlattenProgramRecord(auxProg, amount, extractAmountsByYear(content)));
            }
        }
    }

    private static void flattenTwoColumnReport(GeneratedReport report, List<FlattenTwoProgramsRecord> list,
                                               AmpTheme leftProgram, AmpTheme rightProgram, int level, ReportArea area,
                                               int srcMaxLevels, int dstMaxLevels) {
        ReportOutputColumn totalCol = report.leafHeaders.get(report.leafHeaders.size() - 1);
        int totalLevels = srcMaxLevels + dstMaxLevels;
        if (area.getChildren() != null && level < srcMaxLevels) {
            for (ReportArea child : area.getChildren()) {
                Map<ReportOutputColumn, ReportCell> content = child.getContents();
                TextCell cell = (TextCell) content.get(report.leafHeaders.get(level));
                AmpTheme auxProg = getThemeById(cell.entityId);
                if (auxProg != null
                        && auxProg.getIndlevel() <= srcMaxLevels
                        && auxProg.getIndlevel() < totalLevels) {
                    leftProgram = auxProg;
                } else {
                    if (leftProgram != null && level == 0) {
                        // Reset program or we could add an extra row for undefined as last record
                        // but with the previous program name (wrong).
                        leftProgram = null;
                    }
                }
                flattenTwoColumnReport(report, list, leftProgram, null, level + 1, child, srcMaxLevels,
                        dstMaxLevels);
            }
        } else if (area.getChildren() != null && level < totalLevels) {
            if (level == srcMaxLevels) {
                rightProgram = null;
            }
            for (ReportArea child : area.getChildren()) {
                Map<ReportOutputColumn, ReportCell> content = child.getContents();
                TextCell cell = (TextCell) content.get(report.leafHeaders.get(level));
                AmpTheme auxProg = getThemeById(cell.entityId);
                if (auxProg != null) {
                    rightProgram = auxProg;
                } else {
                    if (rightProgram != null && level == srcMaxLevels - 1) {
                        // Reset program or we could add an extra row for undefined as last record
                        // but with the previous program name (wrong).
                        rightProgram = null;
                    }
                }
                flattenTwoColumnReport(report, list, leftProgram, rightProgram, level + 1, child, srcMaxLevels,
                        dstMaxLevels);
            }
        } else {
            if (rightProgram != null) {
                Map<ReportOutputColumn, ReportCell> content = area.getContents();
                // TODO: This could be necessary if last level is undefined, please check.
                /* TextCell cell = (TextCell) content.get(report.leafHeaders.get(level - 1));
                AmpTheme auxProg = getThemeById(cell.entityId);
                if (auxProg == null) {
                    auxProg = leftProgram;
                } */
                BigDecimal amount = ((AmountCell) content.get(totalCol)).extractValue();
                list.add(new FlattenTwoProgramsRecord(leftProgram, rightProgram, extractAmountsByYear(content), amount));
            }
        }
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
        if (outerReport.reportContents != null && outerReport.reportContents.getChildren() != null
                && innerReport.reportContents != null && innerReport.reportContents.getChildren() != null) {

            List<FlattenProgramRecord> flatOuterReport = new ArrayList<FlattenProgramRecord>();
            flattenOneColumnReport(outerReport, flatOuterReport, null, 0, outerReport.reportContents,
                    mapping.getSrcProgram().getLevels());

            List<FlattenTwoProgramsRecord> flatInnerReport = new ArrayList<FlattenTwoProgramsRecord>();
            flattenTwoColumnReport(innerReport, flatInnerReport, null, null, 0,
                    innerReport.reportContents, mapping.getSrcProgram().getLevels(),
                    mapping.getDstProgram().getLevels());

            flatOuterReport.forEach(outer -> {
                AtomicBoolean add = new AtomicBoolean();
                add.set(false);
                AmpTheme outerProgram = outer.getProgram();
                NDDSolarChartData nddSolarChartData = new NDDSolarChartData(null, new ArrayList<>());
                if (outerProgram != null) {
                    nddSolarChartData.setDirectProgram(new NDDSolarChartData.ProgramData(outerProgram,
                            outer.getAmount(), outer.getAmountsByYear()));
                    if (nddSolarChartData.getDirectProgram().getProgramLvl1() != null) {
                        add.set(true);
                    }
                } else {
                    logger.debug("Ignore undefined outer program");
                }

                /* Inner ring: try to match the outer program of innerReport with the outer program of outer report */
                flatInnerReport.forEach(inner -> {
                    if (add.get() && inner.getOuterProgram() != null) {
                        if (outerProgram.getAmpThemeId().equals(inner.getOuterProgram().getAmpThemeId())) {
                            if (isIndirect) {
                                if (inner.getInnerProgram() != null) {
                                    nddSolarChartData.getIndirectPrograms()
                                            .add(new NDDSolarChartData.ProgramData(inner.getInnerProgram(),
                                                    inner.getAmount(), inner.getAmountsByYear()));
                                } else {
                                    addFakeProgram(nddSolarChartData, inner);
                                }
                            } else {
                                // Also match with mapping data.
                                if (isMapped(mapping, outerProgram, inner.getInnerProgram())) {
                                    if (inner.getInnerProgram() != null) {
                                        nddSolarChartData.getIndirectPrograms()
                                                .add(new NDDSolarChartData.ProgramData(inner.getInnerProgram(),
                                                        inner.getAmount(), inner.getAmountsByYear()));
                                    } else {
                                        addFakeProgram(nddSolarChartData, inner);
                                    }
                                } else {
                                    addFakeProgram(nddSolarChartData, inner);
                                }
                            }
                        }
                    }
                });

                if (add.get()) {
                    list.add(nddSolarChartData);
                }
            });
        }
        return list;
    }

    private static void addFakeProgram(NDDSolarChartData nddSolarChartData, FlattenTwoProgramsRecord flatRecord) {
        AmpTheme fakeTheme = new AmpTheme();
        fakeTheme.setThemeCode("Undef");
        fakeTheme.setName("Undefined");
        fakeTheme.setAmpThemeId(-1L);
        fakeTheme.setIndlevel(-1);
        fakeTheme.setParentThemeId(flatRecord.getOuterProgram().getParentThemeId());
        addAndMergeUndefinedPrograms(nddSolarChartData, fakeTheme,
                flatRecord.getAmount(), flatRecord.getAmountsByYear());
    }

    /**
     * This function is way faster than ProgramUtil.getTheme().
     *
     * @param id
     * @return
     */
    private static AmpTheme getThemeById(Long id) {
        try {
            if (id > 0) {
                return ProgramUtil.getThemeById(id);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void addAndMergeUndefinedPrograms(NDDSolarChartData nddSolarChartData, AmpTheme ampTheme,
                                                     BigDecimal amount, Map<String, BigDecimal> amountsByYear) {
        NDDSolarChartData.ProgramData programData = new NDDSolarChartData.ProgramData(ampTheme, amount, amountsByYear);
        if (nddSolarChartData.getIndirectPrograms().size() > 0) {
            AtomicBoolean add = new AtomicBoolean(true);
            nddSolarChartData.getIndirectPrograms().forEach(i -> {
                if (i.equals(programData)) {
                    add.set(false);
                    i.setAmount(i.getAmount().add(programData.getAmount()));
                    i.getAmountsByYear().forEach((j, val) -> {
                        programData.getAmountsByYear().forEach((k, val2) -> {
                            if (j.equals(k)) {
                                BigDecimal sum = val.add(val2);
                                i.getAmountsByYear().put(j, sum);
                            }
                        });
                    });
                }
            });
            if (add.get()) {
                nddSolarChartData.getIndirectPrograms().add(programData);
            }
        } else {
            nddSolarChartData.getIndirectPrograms().add(programData);
        }
    }

    private static List<NDDSolarChartData> processOne(final GeneratedReport outerReport) {
        List<NDDSolarChartData> list = new ArrayList<>();
        ReportOutputColumn outerReportProgramColumn = outerReport.leafHeaders.get(0);
        ReportOutputColumn outerReportTotalColumn = outerReport.leafHeaders.get(outerReport.leafHeaders.size() - 1);

        if (outerReport.reportContents != null && outerReport.reportContents.getChildren() != null) {
            outerReport.reportContents.getChildren().stream().forEach(children -> {
                Map<ReportOutputColumn, ReportCell> outerContent = children.getContents();
                NDDSolarChartData nddSolarChartData = new NDDSolarChartData(null, new ArrayList<>());
                AmpTheme direct = getThemeById(((TextCell) outerContent.get(outerReportProgramColumn))
                        .entityId);
                if (direct != null) {
                    BigDecimal amount = ((AmountCell) outerContent.get(outerReportTotalColumn)).extractValue();
                    Map<String, BigDecimal> amountsByYear = extractAmountsByYear(outerContent);
                    NDDSolarChartData.ProgramData programData = new NDDSolarChartData.ProgramData(direct, amount,
                            amountsByYear);
                    // Ignore programs we dont want to show.
                    if (programData.getAmount() != null && programData.getProgramLvl1() != null) {
                        nddSolarChartData.setDirectProgram(programData);
                        list.add(nddSolarChartData);
                    }
                } else {
                    // TODO: implement for undefined row.
                    // System.out.println("To be implemented");
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
                createDetailRecord(children, projectColumn, year, list);
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

        if (report.reportContents != null && report.reportContents.getChildren() != null) {
            report.reportContents.getChildren().forEach(children1 -> {
                Map<ReportOutputColumn, ReportCell> directCol = children1.getContents();
                TextCell directCell = (TextCell) directCol.get(directColumn);
                AmpTheme directProgram = getThemeById(directCell.entityId);
                if (directProgram != null) {
                    children1.getChildren().forEach(children2 -> {
                        Map<ReportOutputColumn, ReportCell> indirectCol = children2.getContents();
                        TextCell indirectProgram = (TextCell) indirectCol.get(indirectColumn);
                        AmpTheme auxProgram = getThemeById(indirectProgram.entityId);
                        if (program != null) {
                            if (auxProgram != null) {
                                if (program.getAmpThemeId().equals(auxProgram.getAmpThemeId())) {
                                    children2.getChildren().forEach(children3 -> {
                                        createDetailRecord(children3, projectColumn, year, list);
                                    });
                                }
                            }
                        } else {
                            // This is for undefined programs.
                            children2.getChildren().forEach(children3 -> {
                                createDetailRecord(children3, projectColumn, year, list);
                            });
                        }
                    });
                }
            });
        }
        return list;
    }

    private static void createDetailRecord(ReportArea children, ReportOutputColumn column, int year,
                                           List<DetailByYear> list) {
        Map<ReportOutputColumn, ReportCell> projectCol = children.getContents();
        TextCell cell = ((TextCell) projectCol.get(column));
        BigDecimal amount = extractAmountsByYear(projectCol).get("" + year);
        if (amount != null) {
            DetailByYear detailRecord = new DetailByYear(cell.entityId,
                    cell.displayedValue, amount);
            list.add(detailRecord);
        }
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
            AmpTheme outerProgram = getThemeById(Long.valueOf(ids.get(0)));
            AmpTheme innerProgram = getThemeById(Long.valueOf(ids.get(1)));

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

            List<ReportColumn> outerColumns = getColumnsFromProgram(outerProgram, mapping.getSrcProgram().getLevels());
            ReportMeasure outerMeasure = getMeasureFromParams(params.getSettings());
            outerReport = createReport(outerColumns, outerMeasure, filters,
                    params.getSettings(), true);

            List<ReportColumn> innerColumns = getColumnsFromProgram(innerProgram, mapping.getDstProgram().getLevels());
            innerColumns.addAll(outerColumns);
            ReportMeasure innerMeasure = outerMeasure;
            innerReport = createReport(innerColumns, outerMeasure, filters,
                    params.getSettings(), true);

            return processTwo(outerReport, innerReport, isIndirect, mapping);
        } else if (ids.size() == 1) {
            AmpTheme outerProgram = getThemeById(Long.valueOf(ids.get(0)));
            List<ReportColumn> outerColumns = getColumnsFromProgram(outerProgram, 3);
            ReportMeasure outerMeasure = getMeasureFromParams(params.getSettings());
            outerReport = createReport(outerColumns, outerMeasure, filters,
                    params.getSettings(), true);
            return processOne(outerReport);
        } else {
            throw new RuntimeException("Error number of ids in settings parameter.");
        }
    }

    private static boolean isMapped(MappingConfiguration mapping, AmpTheme outer, AmpTheme inner) {
        boolean ret = false;
        if (outer != null && inner != null
                && ((ProgramMappingConfiguration) mapping).getProgramMapping().stream().filter(i -> {
            return i.getSrcTheme().getAmpThemeId().equals(outer.getAmpThemeId())
                    && i.getDstTheme().getAmpThemeId().equals(inner.getAmpThemeId());
        }).collect(Collectors.toList()).size() > 0) {
            ret = true;
        }
        return ret;
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
        AmpTheme program = getThemeById(Long.valueOf(programIdString));
        ReportColumn projectTitleColumn = new ReportColumn(ColumnConstants.PROJECT_TITLE);
        if (params.getSettings().get("isShowIndirectDataForActivitiesDetail").toString().equals("true")) {
            MappingConfiguration indirectMapping = nddService.getIndirectProgramMappingConfiguration();
            AmpTheme directProgram = getThemeById(indirectMapping.getSrcProgram().getId());
            List<ReportColumn> outerColumns = getColumnsFromProgram(directProgram, 1);
            ReportMeasure innerMeasure = getMeasureFromParams(params.getSettings());
            AmpTheme rootProgram;
            if (program != null) {
                rootProgram = getProgramByLvl(program, 0);
            } else {
                rootProgram = NDDService.getDstIndirectProgramRoot();
            }
            List<ReportColumn> innerColumns = getColumnsFromProgram(rootProgram, 1);
            List<ReportColumn> columns = outerColumns;
            columns.add(innerColumns.get(0));
            columns.add(projectTitleColumn);
            report = createReport(columns, innerMeasure, filters,
                    params.getSettings(), false);
            return processDetailForIndirectData(report, yearString, program);
        } else {
            boolean dontUseMapping = params.getSettings().get("dontUseMapping").toString().equals("true");
            addFilterFromProgram(program, filters, dontUseMapping);
            ReportMeasure outerMeasure = getMeasureFromParams(params.getSettings());
            List<ReportColumn> columns = new ArrayList<>();
            columns.add(projectTitleColumn);
            report = createReport(columns, outerMeasure, filters, params.getSettings(), true);
            return processDetailForDirectData(report, yearString);
        }
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
        if ((rootProgram.getAmpThemeId().equals(indirectMapping.getDstProgram().getId())
                || rootProgram.getAmpThemeId().equals(indirectMapping.getSrcProgram().getId()))
                && indirectMapping.getDstProgram().isIndirect()) {
            isIndirect = true;
        } else {
            isIndirect = false;
        }
        AmpActivityProgramSettings singleProgramSetting = ((AmpActivityProgramSettings) programSettings.toArray()[0]);
        ReportColumn fromMappingFilterColumn = null;
        if (singleProgramSetting.getName().equalsIgnoreCase(ColumnConstants.PRIMARY_PROGRAM)) {
            if (dontUseMapping) {
                ReportColumn mainFilterColumn;
                if (program.getIndlevel() == 1) {
                    mainFilterColumn = new ReportColumn(ColumnConstants.PRIMARY_PROGRAM_LEVEL_1);
                } else {
                    mainFilterColumn = new ReportColumn(ColumnConstants.PRIMARY_PROGRAM_LEVEL_2);
                }
                filters.addFilterRule(mainFilterColumn, new FilterRule(program.getAmpThemeId().toString(), true));
            } else {
                if (program.getIndlevel() == 1) {
                    fromMappingFilterColumn = new ReportColumn(ColumnConstants.PRIMARY_PROGRAM_LEVEL_1);
                } else {
                    fromMappingFilterColumn = new ReportColumn(ColumnConstants.PRIMARY_PROGRAM_LEVEL_2);
                }
            }
        } else if (singleProgramSetting.getName().equalsIgnoreCase(ColumnConstants.SECONDARY_PROGRAM)) {
            if (dontUseMapping) {
                ReportColumn mainFilterColumn;
                if (program.getIndlevel() == 1) {
                    mainFilterColumn = new ReportColumn(ColumnConstants.SECONDARY_PROGRAM_LEVEL_1);
                } else {
                    mainFilterColumn = new ReportColumn(ColumnConstants.SECONDARY_PROGRAM_LEVEL_2);
                }
                filters.addFilterRule(mainFilterColumn, new FilterRule(program.getAmpThemeId().toString(), true));
            } else {
                if (program.getIndlevel() == 1) {
                    fromMappingFilterColumn = new ReportColumn(ColumnConstants.SECONDARY_PROGRAM_LEVEL_1);
                } else {
                    fromMappingFilterColumn = new ReportColumn(ColumnConstants.SECONDARY_PROGRAM_LEVEL_2);
                }
            }
        } else if (singleProgramSetting.getName().equalsIgnoreCase(ColumnConstants.TERTIARY_PROGRAM)) {
            if (dontUseMapping) {
                ReportColumn mainFilterColumn;
                if (program.getIndlevel() == 1) {
                    mainFilterColumn = new ReportColumn(ColumnConstants.TERTIARY_PROGRAM_LEVEL_1);
                } else {
                    mainFilterColumn = new ReportColumn(ColumnConstants.TERTIARY_PROGRAM_LEVEL_2);
                }
                filters.addFilterRule(mainFilterColumn, new FilterRule(program.getAmpThemeId().toString(), true));

            } else {
                if (program.getIndlevel() == 1) {
                    fromMappingFilterColumn = new ReportColumn(ColumnConstants.TERTIARY_PROGRAM_LEVEL_1);
                } else {
                    fromMappingFilterColumn = new ReportColumn(ColumnConstants.TERTIARY_PROGRAM_LEVEL_2);
                }
            }
        } else if (singleProgramSetting.getName().equalsIgnoreCase(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES)
                || singleProgramSetting.getName().equalsIgnoreCase(ProgramUtil.NATIONAL_PLAN_OBJECTIVE)) {
            if (dontUseMapping) {
                ReportColumn mainFilterColumn;
                if (program.getIndlevel() == 1) {
                    mainFilterColumn = new ReportColumn(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_1);
                } else {
                    mainFilterColumn = new ReportColumn(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_2);
                }
                filters.addFilterRule(mainFilterColumn, new FilterRule(program.getAmpThemeId().toString(), true));
            } else {
                if (program.getIndlevel() == 1) {
                    fromMappingFilterColumn = new ReportColumn(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_1);
                } else {
                    fromMappingFilterColumn = new ReportColumn(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_2);
                }
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
            fromMappingIds.add(program.getAmpThemeId().toString());
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
}
