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
import org.digijava.kernel.ampapi.endpoints.settings.SettingsConstants;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.util.ProgramUtil;

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

    /**
     * Given a @program with level 0 (main root) return the report columns (one if @onlyLvl3 == true)
     * linked to the program in the MultiProgram Configuration Manager (ie: PP, SP, NPO, etc).
     *
     * @param program
     * @param onlyLvl3
     * @return
     */
    private static List<ReportColumn> getColumnsFromProgram(AmpTheme program, boolean onlyLvl3) {
        List<ReportColumn> list = new ArrayList<>();
        Set<AmpActivityProgramSettings> programSettings = program.getProgramSettings();
        if (programSettings == null || programSettings.size() != 1) {
            throw new RuntimeException("Cant determine the first column of the report.");
        }
        AmpActivityProgramSettings singleProgramSetting = ((AmpActivityProgramSettings) programSettings.toArray()[0]);
        if (singleProgramSetting.getName().equalsIgnoreCase(ColumnConstants.PRIMARY_PROGRAM)) {
            if (!onlyLvl3) {
                list.add(new ReportColumn(ColumnConstants.PRIMARY_PROGRAM_LEVEL_1));
                list.add(new ReportColumn(ColumnConstants.PRIMARY_PROGRAM_LEVEL_2));
            }
            list.add(new ReportColumn(ColumnConstants.PRIMARY_PROGRAM_LEVEL_3));
            return list;
        } else if (singleProgramSetting.getName().equalsIgnoreCase(ColumnConstants.SECONDARY_PROGRAM)) {
            if (!onlyLvl3) {
                list.add(new ReportColumn(ColumnConstants.SECONDARY_PROGRAM_LEVEL_1));
                list.add(new ReportColumn(ColumnConstants.SECONDARY_PROGRAM_LEVEL_2));
            }
            list.add(new ReportColumn(ColumnConstants.SECONDARY_PROGRAM_LEVEL_3));
            return list;
        } else if (singleProgramSetting.getName().equalsIgnoreCase(ColumnConstants.TERTIARY_PROGRAM)) {
            if (!onlyLvl3) {
                list.add(new ReportColumn(ColumnConstants.TERTIARY_PROGRAM_LEVEL_1));
                list.add(new ReportColumn(ColumnConstants.TERTIARY_PROGRAM_LEVEL_2));
            }
            list.add(new ReportColumn(ColumnConstants.TERTIARY_PROGRAM_LEVEL_3));
            return list;
        } else if (singleProgramSetting.getName().equalsIgnoreCase(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES)
                || singleProgramSetting.getName().equalsIgnoreCase(ProgramUtil.NATIONAL_PLAN_OBJECTIVE)) {
            if (!onlyLvl3) {
                list.add(new ReportColumn(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_1));
                list.add(new ReportColumn(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_2));
            }
            list.add(new ReportColumn(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_3));
            return list;
        } else if (singleProgramSetting.getName().equalsIgnoreCase(ProgramUtil.INDIRECT_PRIMARY_PROGRAM)) {
            if (!onlyLvl3) {
                list.add(new ReportColumn(ColumnConstants.INDIRECT_PRIMARY_PROGRAM_LEVEL_1));
                list.add(new ReportColumn(ColumnConstants.INDIRECT_PRIMARY_PROGRAM_LEVEL_2));
            }
            list.add(new ReportColumn(ColumnConstants.INDIRECT_PRIMARY_PROGRAM_LEVEL_3));
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
        ReportOutputColumn orColLvl1 = outerReport.leafHeaders.get(COLUMN_0);
        ReportOutputColumn orColLvl2 = outerReport.leafHeaders.get(COLUMN_1);
        ReportOutputColumn orColLvl3 = outerReport.leafHeaders.get(COLUMN_2);
        ReportOutputColumn irColLvl1 = innerReport.leafHeaders.get(COLUMN_0);
        ReportOutputColumn irColLvl2 = innerReport.leafHeaders.get(COLUMN_1);
        ReportOutputColumn irColLvl3 = innerReport.leafHeaders.get(COLUMN_2);
        ReportOutputColumn irColLvl4 = innerReport.leafHeaders.get(COLUMN_3);
        ReportOutputColumn irColLvl5 = innerReport.leafHeaders.get(COLUMN_4);
        ReportOutputColumn irColLvl6 = innerReport.leafHeaders.get(COLUMN_5);
        ReportOutputColumn orTotalCol = outerReport.leafHeaders.get(outerReport.leafHeaders.size() - 1);
        ReportOutputColumn irTotalCol = innerReport.leafHeaders.get(innerReport.leafHeaders.size() - 1);

        // TODO: Refactoring to allow mapping to levels other than 3.
        if (outerReport.reportContents != null && outerReport.reportContents.getChildren() != null
                && innerReport.reportContents != null && innerReport.reportContents.getChildren() != null) {
            outerReport.reportContents.getChildren().forEach(orChild1 -> {
                Map<ReportOutputColumn, ReportCell> orProgLvl1 = orChild1.getContents();
                AtomicBoolean add = new AtomicBoolean();
                add.set(false);
                /* Outer ring: If the cell is a valid level 3 program then add it to the list, if is undefined then
                 * go up to the level 2 parent and if is a valid program then add it to the list, if not then is a
                 * level 1 program. In all cases the amount is always the one from the level 3 cell. */
                orChild1.getChildren().forEach(orChild2 -> {
                    Map<ReportOutputColumn, ReportCell> orProgLvl2 = orChild2.getContents();
                    orChild2.getChildren().forEach(orChild3 -> {
                        NDDSolarChartData nddSolarChartData = new NDDSolarChartData(null, new ArrayList<>());
                        Map<ReportOutputColumn, ReportCell> orProgLvl3 = orChild3.getContents();
                        TextCell cell = (TextCell) orProgLvl3.get(orColLvl3);
                        AmpTheme outerProgram = getThemeById(cell.entityId);
                        // Go up until we have a valid program.
                        if (outerProgram == null) {
                            cell = (TextCell) orProgLvl2.get(orColLvl2);
                            outerProgram = getThemeById(cell.entityId);
                            if (outerProgram == null) {
                                cell = (TextCell) orProgLvl1.get(orColLvl1);
                                outerProgram = getThemeById(cell.entityId);
                            }
                        }
                        if (outerProgram != null) {
                            BigDecimal amount = ((AmountCell) orProgLvl3.get(orTotalCol)).extractValue();
                            Map<String, BigDecimal> amountsByYear = extractAmountsByYear(orProgLvl3);
                            nddSolarChartData.setDirectProgram(new NDDSolarChartData.ProgramData(outerProgram, amount,
                                    amountsByYear));
                            if (nddSolarChartData.getDirectProgram().getProgramLvl1() != null) {
                                add.set(true);
                            }
                        } else {
                            logger.debug("Ignore undefined outer program");
                        }

                        /* Inner ring: go to the 6th hierarchy level, if is a valid level 3 program and is
                        equal to the current outer program then check if is mapped and add as an indirect program. */
                        AmpTheme finalOuterProgram = outerProgram;
                        innerReport.reportContents.getChildren().forEach(irChild1 -> {
                            Map<ReportOutputColumn, ReportCell> irProgLvl1 = irChild1.getContents();
                            irChild1.getChildren().forEach(irChild2 -> {
                                Map<ReportOutputColumn, ReportCell> irProgLvl2 = irChild2.getContents();
                                irChild2.getChildren().forEach(irChild3 -> {
                                    Map<ReportOutputColumn, ReportCell> irProgLvl3 = irChild3.getContents();
                                    irChild3.getChildren().forEach(irChild4 -> {
                                        Map<ReportOutputColumn, ReportCell> irProgLvl4 = irChild4.getContents();
                                        irChild4.getChildren().forEach(irChild5 -> {
                                            Map<ReportOutputColumn, ReportCell> irProgLvl5 = irChild5.getContents();
                                            irChild5.getChildren().forEach(irChild6 -> {
                                                Map<ReportOutputColumn, ReportCell> irProgLvl6 = irChild6.getContents();
                                                TextCell cellLvl6 = (TextCell) irProgLvl6.get(irColLvl6);
                                                AmpTheme outerPgrmInInnerReport = getThemeById(cellLvl6.entityId);
                                                if (outerPgrmInInnerReport == null) {
                                                    // Go up until we have a valid program.
                                                    cellLvl6 = (TextCell) irProgLvl5.get(irColLvl5);
                                                    outerPgrmInInnerReport = getThemeById(cellLvl6.entityId);
                                                    if (outerPgrmInInnerReport == null) {
                                                        cellLvl6 = (TextCell) irProgLvl4.get(irColLvl4);
                                                        outerPgrmInInnerReport = getThemeById(cellLvl6.entityId);
                                                    }
                                                }
                                                if (finalOuterProgram != null && outerPgrmInInnerReport != null) {
                                                    if (finalOuterProgram.getAmpThemeId()
                                                            .equals(outerPgrmInInnerReport.getAmpThemeId())) {
                                                        AmpTheme innerTheme = getThemeById(((TextCell) irProgLvl3
                                                                .get(irColLvl3)).entityId);
                                                        if (innerTheme == null) {
                                                            innerTheme = getThemeById(((TextCell) irProgLvl2
                                                                    .get(irColLvl2)).entityId);
                                                            if (innerTheme == null) {
                                                                innerTheme = getThemeById(((TextCell) irProgLvl1
                                                                        .get(irColLvl1)).entityId);
                                                            }
                                                        }
                                                        if (innerTheme != null) {
                                                            BigDecimal amountLvl6 = ((AmountCell) irProgLvl6
                                                                    .get(irTotalCol)).extractValue();
                                                            Map<String, BigDecimal> amountsByYearLvl6 =
                                                                    extractAmountsByYear(irProgLvl6);
                                                            nddSolarChartData.getIndirectPrograms()
                                                                    .add(new NDDSolarChartData.ProgramData(innerTheme,
                                                                            amountLvl6, amountsByYearLvl6));
                                                        } else {
                                                            BigDecimal amountLvl6 = ((AmountCell) irProgLvl6
                                                                    .get(irTotalCol)).extractValue();
                                                            Map<String, BigDecimal> amountsByYearLvl6 =
                                                                    extractAmountsByYear(irProgLvl6);
                                                            ReportCell innerCell = irProgLvl1.get(irColLvl1);
                                                            AmpTheme auxTheme = getThemeById(((TextCell) innerCell)
                                                                    .entityId);
                                                            if (auxTheme == null) {
                                                                auxTheme = new AmpTheme();
                                                            }
                                                            AmpTheme fakeTheme = new AmpTheme();
                                                            fakeTheme.setThemeCode("Undef");
                                                            fakeTheme.setName("Undefined");
                                                            fakeTheme.setAmpThemeId(-1L);
                                                            fakeTheme.setIndlevel(-1);
                                                            fakeTheme.setParentThemeId(auxTheme.getParentThemeId());
                                                            addAndMergeUndefinedPrograms(nddSolarChartData, fakeTheme,
                                                                    amountLvl6, amountsByYearLvl6);
                                                        }
                                                    }
                                                }
                                            });
                                        });
                                    });
                                });
                            });
                        });
                        if (add.get()) {
                            list.add(nddSolarChartData);
                        }
                    });
                });
            });
        }
        return list;
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
            List<ReportColumn> outerColumns = getColumnsFromProgram(outerProgram, false);
            ReportMeasure outerMeasure = getMeasureFromParams(params.getSettings());
            outerReport = createReport(outerColumns, outerMeasure, filters,
                    params.getSettings(), true);

            AmpTheme innerProgram = getThemeById(Long.valueOf(ids.get(1)));
            List<ReportColumn> innerColumns = getColumnsFromProgram(innerProgram, false);
            innerColumns.addAll(outerColumns);
            ReportMeasure innerMeasure = outerMeasure;
            innerReport = createReport(innerColumns, innerMeasure, filters,
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
            AmpTheme outerProgram = getThemeById(Long.valueOf(ids.get(0)));
            List<ReportColumn> outerColumns = getColumnsFromProgram(outerProgram, false);
            ReportMeasure outerMeasure = getMeasureFromParams(params.getSettings());
            outerReport = createReport(outerColumns, outerMeasure, filters,
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
        AmpTheme program = getThemeById(Long.valueOf(programIdString));
        ReportColumn projectTitleColumn = new ReportColumn(ColumnConstants.PROJECT_TITLE);
        if (params.getSettings().get("isShowIndirectDataForActivitiesDetail").toString().equals("true")) {
            MappingConfiguration indirectMapping = nddService.getIndirectProgramMappingConfiguration();
            AmpTheme directProgram = getThemeById(indirectMapping.getSrcProgram().getId());
            List<ReportColumn> outerColumns = getColumnsFromProgram(directProgram, false);
            outerColumns.remove(2);
            outerColumns.remove(1);
            ReportMeasure innerMeasure = getMeasureFromParams(params.getSettings());
            AmpTheme rootProgram;
            if (program != null) {
                rootProgram = getProgramByLvl(program, 0);
            } else {
                rootProgram = NDDService.getDstIndirectProgramRoot();
            }
            List<ReportColumn> innerColumns = getColumnsFromProgram(rootProgram, false);
            innerColumns.remove(2);
            innerColumns.remove(1);
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
