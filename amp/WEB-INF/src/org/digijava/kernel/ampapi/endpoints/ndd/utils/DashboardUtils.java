package org.digijava.kernel.ampapi.endpoints.ndd.utils;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.AmountCell;
import org.dgfoundation.amp.newreports.AmpReportFilters;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.digijava.kernel.ampapi.endpoints.ndd.IndirectProgramMappingConfiguration;
import org.digijava.kernel.ampapi.endpoints.ndd.MappingConfiguration;
import org.digijava.kernel.ampapi.endpoints.ndd.NDDService;
import org.digijava.kernel.ampapi.endpoints.ndd.NDDSolarChartData;
import org.digijava.kernel.ampapi.endpoints.ndd.ProgramMappingConfiguration;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsConstants;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.util.ProgramUtil;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class DashboardUtils {

    private static Pattern numberPattern = Pattern.compile("\\d{4}");
    private static Logger logger = Logger.getLogger(DashboardUtils.class);
    private static NDDService nddService = new NDDService();

    private DashboardUtils() {
        
    }

    /**
     * Given a program get its parent from level @lvl.
     *
     * @param program
     * @param lvl
     * @return
     */
    public static AmpTheme getProgramByLvl(AmpTheme program, int lvl) {
        AmpTheme root = program;
        while (root.getIndlevel() > lvl) {
            root = root.getParentThemeId();
        }
        return root;
    }

    /**
     * Since we can change the assigned program for PP, NPO, SP, etc then we will have old programs assigned
     * in activities and that will break the donut charts.
     *
     * @param program
     * @param settings
     * @return
     */
    public static boolean isProgramValid(AmpTheme program, Set<AmpActivityProgramSettings> settings) {
        AtomicBoolean ret = new AtomicBoolean(false);
        AmpTheme root = getProgramByLvl(program, 0);
        settings.forEach(i -> {
            root.getProgramSettings().forEach(j -> {
                if (i.getAmpProgramSettingsId().equals(j.getAmpProgramSettingsId())) {
                    ret.set(true);
                }
            });
        });
        return ret.get();
    }

    /**
     * This function is way faster than ProgramUtil.getTheme().
     *
     * @param id
     * @return
     */
    public static AmpTheme getThemeById(Long id) {
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

    /**
     * Return true if 2 programs have been mapped together in the NDD admin.
     *
     * @param mapping
     * @param outer
     * @param inner
     * @return
     */
    public static boolean isMapped(MappingConfiguration mapping, AmpTheme outer, AmpTheme inner) {
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

    /**
     * Return an array of {year | amount} given some report's content data (yearly).
     *
     * @param content
     * @return
     */
    public static Map<String, BigDecimal> extractAmountsByYear(Map<ReportOutputColumn, ReportCell> content) {
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
     * Create a fake "undefined" program and add it to the NDDSolarChartData object.
     *
     * @param nddSolarChartData
     * @param flatRecord
     */
    public static void addFakeProgram(NDDSolarChartData nddSolarChartData, FlattenTwoProgramsRecord flatRecord) {
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
     * Consolidate N fake programs into a single one to simplify the processing in the UI.
     *
     * @param nddSolarChartData
     * @param ampTheme
     * @param amount
     * @param amountsByYear
     */
    private static void addAndMergeUndefinedPrograms(NDDSolarChartData nddSolarChartData, AmpTheme ampTheme,
                                                     BigDecimal amount, Map<String, BigDecimal> amountsByYear) {
        NDDSolarChartData.ProgramData programData = new NDDSolarChartData.ProgramData(ampTheme, amount, amountsByYear);
        if (nddSolarChartData.getIndirectPrograms().size() > 0) {
            AtomicBoolean add = new AtomicBoolean(true);
            for (NDDSolarChartData.ProgramData i : nddSolarChartData.getIndirectPrograms()) {
                if (i.getProgramLvl1().getObjectId() == -1) {
                    add.set(false);
                    i.setAmount(i.getAmount().add(programData.getAmount()));
                    programData.getAmountsByYear().forEach((year, val) -> {
                        if (i.getAmountsByYear().containsKey(year)) {
                            BigDecimal sum = val.add(i.getAmountsByYear().get(year));
                            i.getAmountsByYear().put(year, sum);
                        } else {
                            i.getAmountsByYear().put(year, val);
                        }
                    });
                }
            }
            if (add.get()) {
                nddSolarChartData.getIndirectPrograms().add(programData);
            }
        } else {
            nddSolarChartData.getIndirectPrograms().add(programData);
        }
    }

    /**
     * Use reflection to get column constant fields dynamically.
     *
     * @param prefix
     * @param index
     * @return
     */
    public static String getProgramConstant(String prefix, int index) {
        try {
            Field field = ColumnConstants.class.getField(prefix + index);
            return field.get(null).toString();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            logger.error(e.getLocalizedMessage());
        }
        return null;
    }

    /**
     * Returns true if this program has been mapped as indirect (first tab of NDD).
     *
     * @param rootProgram
     * @return
     */
    public static boolean isIndirect(AmpTheme rootProgram) {
        IndirectProgramMappingConfiguration indirectMapping = nddService.getIndirectProgramMappingConfiguration();
        boolean isIndirect;
        if ((rootProgram.getAmpThemeId().equals(indirectMapping.getDstProgram().getId())
                || rootProgram.getAmpThemeId().equals(indirectMapping.getSrcProgram().getId()))
                && indirectMapping.getDstProgram().isIndirect()) {
            isIndirect = true;
        } else {
            isIndirect = false;
        }
        return isIndirect;
    }

    public static MappingConfiguration getMappingConfig(AmpTheme left, AmpTheme right) {
        MappingConfiguration mapping = null;
        MappingConfiguration indirectMapping = nddService.getIndirectProgramMappingConfiguration();
        MappingConfiguration regularMapping = nddService.getProgramMappingConfiguration();
        if (left.getAmpThemeId().equals(indirectMapping.getSrcProgram().getId())
                && right.getAmpThemeId().equals(indirectMapping.getDstProgram().getId())) {
            mapping = indirectMapping;
        } else {
            mapping = regularMapping;
        }
        return mapping;
    }

    /**
     * Given a @program with level 0 (main root) return the report columns
     * linked to the program in the MultiProgram Configuration Manager (ie: PP, SP, NPO, etc).
     *
     * @param program
     * @return
     */
    public static List<ReportColumn> getColumnsFromProgram(AmpTheme program, int levels) {
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
                list.add(new ReportColumn(DashboardUtils.getProgramConstant(prefix, i)));
            }
            return list;
        }
        return null;
    }

    public static ReportMeasure getMeasureFromParams(Map<String, Object> settings) {
        if (settings != null && settings.get(SettingsConstants.FUNDING_TYPE_ID) != null) {
            return new ReportMeasure(settings.get(SettingsConstants.FUNDING_TYPE_ID).toString());
        } else {
            return new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS);
        }
    }

    public static AmpReportFilters getFiltersFromParams(Map<String, Object> filters) {
        AmpReportFilters filterRules = new AmpReportFilters();
        if (filters != null) {
            filterRules = FilterUtils.getFilterRules(filters, null);
        }
        return filterRules;
    }
}
