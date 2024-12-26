package org.digijava.kernel.ampapi.endpoints.dashboards.services;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.newreports.AmountCell;
import org.dgfoundation.amp.newreports.AmpReportFilters;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.dto.BaseTargetValue;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiRuntimeException;
import org.digijava.kernel.ampapi.endpoints.gis.SettingsAndFiltersParameters;
import org.digijava.kernel.ampapi.endpoints.indicator.IndicatorYearValues;
import org.digijava.kernel.ampapi.endpoints.indicator.ProgramIndicatorValues;
import org.digijava.kernel.ampapi.endpoints.indicator.YearValue;
import org.digijava.kernel.ampapi.endpoints.indicator.manager.MEIndicatorDTO;
import org.digijava.kernel.ampapi.endpoints.indicator.manager.ProgramSchemeDTO;
import org.digijava.kernel.ampapi.endpoints.indicator.manager.SectorDTO;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpSectorScheme;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.IndicatorActivity;
import org.digijava.module.aim.util.IndicatorUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.hibernate.query.Query;
import org.hibernate.type.LongType;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.time.Year;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;


public class MeService {
    protected static final Logger logger = Logger.getLogger(MeService.class);

    public static List<ProgramSchemeDTO> getProgramConfiguration() {
        List<AmpActivityProgramSettings> settings = ProgramUtil.getAmpActivityProgramSettingsList(true);

        //remove settings with no hierarchy
        settings.removeIf(setting -> setting.getDefaultHierarchy() == null);
        return settings.stream().map(ProgramSchemeDTO::new).collect(Collectors.toList());
    }


    public List<MEIndicatorDTO> getIndicatorsByProgram(Long programId) {
        Set<AmpIndicator> programIndicators = null;

        try {
            programIndicators = IndicatorUtil.getThemeIndicators(programId);
        } catch (Exception e) {
            throw new ApiRuntimeException(NOT_FOUND,
                    ApiError.toError("Program with id " + programId + " does not exist"));
        }


        return programIndicators.stream().map(MEIndicatorDTO::new).collect(Collectors.toList());
    }

    public List<MEIndicatorDTO> getIndicatorsBySector(Long sectorId) {
        AmpSector sector = SectorUtil.getAmpSector(sectorId);
        return sector.getIndicators().stream().map(MEIndicatorDTO::new).collect(Collectors.toList());
    }

    public List<IndicatorYearValues> getIndicatorValuesByProgramId(Long programId,
                                                                   SettingsAndFiltersParameters params) {

        List<AmpIndicator> indicatorsByProgram = getAllAmpIndicators().stream()
                .filter(indicator -> indicator.getProgram() != null)
                .filter(indicator -> indicator.getProgram().getAmpThemeId().equals(programId))
                .collect(Collectors.toList());

        Map<Long, List<YearValue>> indicatorsWithYearValues = getAllIndicatorYearValuesWithActualValues(params);

        return indicatorsByProgram.stream()
                .map(indicator -> getIndicatorYearValues(indicator, indicatorsWithYearValues))
                .collect(Collectors.toList());

    }

    public IndicatorYearValues getIndicatorYearValuesByIndicatorId(Long indicatorId,
                                                                   SettingsAndFiltersParameters params) {
        AmpIndicator existingIndicator = getAllAmpIndicators().stream()
                .filter(indicator -> indicator.getIndicatorId().equals(indicatorId))
                .findFirst()
                .orElse(null);

        int yearsCount = Integer.valueOf(params.getSettings().get("yearCount").toString());

        if (yearsCount < 5) {
            yearsCount = 5;
        }

        if (existingIndicator == null) {
            throw new ApiRuntimeException(NOT_FOUND,
                    ApiError.toError("Indicator with id " + indicatorId + " does not exist"));
        }

        Map<Long, List<YearValue>> indicatorsWithYearValues = getAllIndicatorYearValuesWithActualValues(params);
        Map<Long, BaseTargetValue> baseTargetValues = getBaseTargetValues(params);

        return getIndicatorYearValues(existingIndicator, indicatorsWithYearValues, yearsCount, baseTargetValues);
    }

    public List<ProgramIndicatorValues> getIndicatorYearValuesByIndicatorCountryProgramId(SettingsAndFiltersParameters params) {
        List<ProgramIndicatorValues> programIndicatorValues = new ArrayList<>();
        Map<Long, BaseTargetValue> baseTargetValues = getBaseTargetValues(params);
        int yearsCount = Integer.valueOf(params.getSettings().get("yearCount").toString());

        if (yearsCount < 5) {
            yearsCount = 5;
        }

        // Getting params array of objectives
        List<Integer> objectiveIds = (List<Integer>) params.getFilters().get("national-planning-objectives-level-1");

        for (Integer objectiveId : objectiveIds) {
            Long id = Long.valueOf(objectiveId);
            AmpTheme objective = ProgramUtil.getThemeById(id);
            List<AmpTheme> programSubThemes = new ArrayList<>();
            // Check if the objectives are pillars only
            if (!objective.getIndlevel().equals(1)) {
                continue;
            }
            try {
                programSubThemes = ProgramUtil.getSubThemes(id);
            } catch (DgException e) {
                throw new RuntimeException(e);
            }

            // Clone or create a new instance of params for each programId
            SettingsAndFiltersParameters modifiedParams = cloneWithSingleObjective(params, id);

            ProgramIndicatorValues programValues = new ProgramIndicatorValues(objective.getAmpThemeId(), objective.getName());
            // Get all sub programs from parent theme and create objective/sub programs from it
            Map<Long, List<YearValue>> indicatorsWithYearValues = getAllIndicatorYearValuesWithActualValues2(modifiedParams);
            for (AmpTheme subProgram : programSubThemes) {
                ProgramIndicatorValues subProgramValue = new ProgramIndicatorValues(subProgram.getAmpThemeId(), subProgram.getName());


                List<IndicatorYearValues> indicatorValues = new ArrayList<IndicatorYearValues>();
                for (Map.Entry<Long, List<YearValue>> entry : indicatorsWithYearValues.entrySet()) {
                    // Access the indicator ID (key)
                    Long indicatorId = entry.getKey();
                    AmpIndicator existingIndicator = getIndicatorById(indicatorId);
                    // Check to see it the value is added to the same subProgram
                    if (existingIndicator.getProgram().getAmpThemeId().equals(subProgram.getAmpThemeId())) {
                        IndicatorYearValues singelIndicatorYearValues = getIndicatorYearValues(existingIndicator, indicatorsWithYearValues, yearsCount, baseTargetValues);
                        // Include indicators name
                        singelIndicatorYearValues.setIndicatorName(existingIndicator.getName());
                        indicatorValues.add(singelIndicatorYearValues);
                    }
                }

                // As an update we need to return indicators with also no values and give them values of 0
                addIndicatorsWithNoValues(params, subProgram.getAmpThemeId(), indicatorValues, yearsCount, baseTargetValues);

                subProgramValue.setIndicators(indicatorValues);
                programIndicatorValues.add(subProgramValue);
            }
        }

        return programIndicatorValues;
    }

    private void addIndicatorsWithNoValues(SettingsAndFiltersParameters params, Long programId,
                                           List<IndicatorYearValues> indicatorValues, int yearsCount,
                                           Map<Long, BaseTargetValue> baseTargetValues) {

        List<IndicatorActivity> result = null;
        Session session = PersistenceManager.getRequestDBSession();
        String oql = "from " + AmpIndicator.class.getName() + " i ";
        oql += " where i.program.ampThemeId=:programId";
        Query query = session.createQuery(oql);
        query.setParameter("programId", programId, LongType.INSTANCE);
        List<AmpIndicator> indicators = query.list();
        Map<Long, List<YearValue>> indicatorsWithYearValuesDummy = new HashMap<>();
        indicators.stream().filter(i -> !indicatorValues.stream().anyMatch(indicator -> indicator.getIndicator().equals(i))).collect(Collectors.toList()).forEach(indicator -> {
            IndicatorYearValues singelIndicatorYearValues = getIndicatorYearValues(indicator, indicatorsWithYearValuesDummy, yearsCount, baseTargetValues);
            // Include indicators name
            singelIndicatorYearValues.setIndicatorName(indicator.getName());
            indicatorValues.add(singelIndicatorYearValues);
        });
        Collections.sort(indicatorValues, new Comparator<IndicatorYearValues>() {
            @Override
            public int compare(IndicatorYearValues o1, IndicatorYearValues o2) {
                return extractIndicatorNumber(o1.getIndicatorName()).compareTo(extractIndicatorNumber(o2.getIndicatorName()));
            }

            private String extractIndicatorNumber(String indicatorName) {
                Pattern pattern = Pattern.compile("^(\\d+\\.\\d+\\.\\d+)");
                Matcher matcher = pattern.matcher(indicatorName);
                if (matcher.find()) {
                    return matcher.group(1);
                }
                return "";
            }
        });
    }

    private IndicatorYearValues getIndicatorYearValues(final AmpIndicator indicator,
                                                       final Map<Long, List<YearValue>> indicatorsWithYearValues) {
        BigDecimal baseValue = BigDecimal.ZERO;
        BigDecimal targetValue = BigDecimal.ZERO;

        if (indicator.getBaseValue() != null && indicator.getBaseValue().getValue() != null) {
            baseValue = BigDecimal.valueOf(indicator.getBaseValue().getValue());
        }

        if (indicator.getTargetValue() != null && indicator.getTargetValue().getValue() != null) {
            targetValue = BigDecimal.valueOf(indicator.getTargetValue().getValue());
        }

        List<YearValue> yearValues = indicatorsWithYearValues.get(indicator.getIndicatorId());

        if (yearValues == null) {
            yearValues = Collections.emptyList();
        }

        return new IndicatorYearValues(indicator, baseValue, yearValues, targetValue);
    }

    private IndicatorYearValues getIndicatorYearValues(final AmpIndicator indicator,
                                                       final Map<Long, List<YearValue>> indicatorsWithYearValues,
                                                       final int yearsCount, Map<Long, BaseTargetValue> baseTargetValues) {
        BigDecimal baseValue = BigDecimal.ZERO;
        BigDecimal targetValue = BigDecimal.ZERO;
        BaseTargetValue baseTargetValue = baseTargetValues.get(indicator.getIndicatorId());
        if (baseTargetValue != null) {
            if (baseTargetValue.getBase() != null) {
                baseValue = baseTargetValue.getBase();
            }
            if (baseTargetValue.getTarget() != null) {
                targetValue = baseTargetValue.getTarget();
            }
        }
        List<YearValue> yearValues = indicatorsWithYearValues.get(indicator.getIndicatorId());

        if (yearValues == null) {
            yearValues = Collections.emptyList();
        }

        if (yearValues.size() > yearsCount) {
            yearValues = yearValues.subList(0, yearsCount);
        } else if (yearValues.size() < yearsCount) {
            List<YearValue> newYearValues = new ArrayList<>(yearsCount);
            newYearValues.addAll(yearValues);

            for (int i = yearValues.size(); i < yearsCount; i++) {
                newYearValues.add(new YearValue(Year.now().getValue() - i, BigDecimal.ZERO));
            }

            yearValues = newYearValues;
        }

        return new IndicatorYearValues(indicator, baseValue, yearValues, targetValue);
    }

    public Map<Long, List<YearValue>> getAllIndicatorYearValuesWithActualValues(SettingsAndFiltersParameters params) {
        GeneratedReport generatedReport = runIndicatorReport(params);
        Map<Long, List<YearValue>> data = new HashMap<>();

        List<ReportArea> children = generatedReport.reportContents.getChildren() == null
                ? Collections.emptyList()
                : generatedReport.reportContents.getChildren();

        Map<Long, AmpIndicator> indicatorById = getAllAmpIndicators().stream()
                .collect(Collectors.toMap(AmpIndicator::getIndicatorId, Function.identity()));

        for (ReportArea area : children) {
            AmpIndicator indicator = indicatorById.get(area.getOwner().id);
            List<YearValue> actualValues = new ArrayList<>();

            for (Map.Entry<ReportOutputColumn, ReportCell> entry : area.getContents().entrySet()) {
                ReportOutputColumn col = entry.getKey();

                if (col.parentColumn != null
                        && col.originalColumnName.equals(MeasureConstants.INDICATOR_ACTUAL_VALUE)
                        && col.parentColumn.parentColumn != null
                        && col.parentColumn.parentColumn.originalColumnName.equals(
                        NiReportsEngine.FUNDING_COLUMN_NAME)
                        && col.parentColumn.parentColumn.parentColumn == null) {
                    int year = Integer.parseInt(col.parentColumn.originalColumnName);
                    AmountCell cell = (AmountCell) entry.getValue();
                    BigDecimal actualValue = cell.extractValue();
                    actualValues.add(new YearValue(year, actualValue));
                }
            }
            data.put(indicator.getIndicatorId(), actualValues);
        }

        return data;
    }

    public List<SectorClassificationDTO> getSectorClassification() {
        List<SectorClassificationDTO> sectorClassificationDTOs = new ArrayList<>();
        List<AmpClassificationConfiguration> sectorClassificationConfig = SectorUtil.getAllClassificationConfigs()
                .stream()
                .filter(config -> config.getClassification() != null)
                .collect(Collectors.toList());

        for (AmpClassificationConfiguration config : sectorClassificationConfig) {
            AmpSectorScheme scheme = config.getClassification();
            List<AmpSector> schemeSectors = (List<AmpSector>) SectorUtil.getSectorLevel1(Math.toIntExact(scheme.getAmpSecSchemeId()));
            SectorDTO[] children = schemeSectors.stream().map(SectorDTO::new).toArray(SectorDTO[]::new);

            SectorSchemeDTO schemeDTO = new SectorSchemeDTO(scheme, children);
            sectorClassificationDTOs.add(new SectorClassificationDTO(config, schemeDTO));
        }

        return sectorClassificationDTOs;
    }

    public List<MEIndicatorDTO> getIndicatorsBySectorClassification(Long sectorClassificationId) {
        List<AmpIndicator> indicators = new ArrayList<>();
        AmpClassificationConfiguration sectorClassification = null;

        try {
            sectorClassification = SectorUtil.getClassificationConfigById(sectorClassificationId);
        } catch (DgException e) {
            throw new RuntimeException("Failed to load indicators");
        }


        if (sectorClassification == null) {
            throw new ApiRuntimeException(NOT_FOUND,
                    ApiError.toError("Sector classification with id " + sectorClassificationId + " does not exist"));
        }
        ;

        List<AmpSector> schemeSectors = (List<AmpSector>) SectorUtil.getSectorLevel1(
                Math.toIntExact(sectorClassification.getClassification().getAmpSecSchemeId()));

        for (AmpSector sector : schemeSectors) {
            indicators.addAll(sector.getIndicators());
        }
        ;

        return indicators.stream().map(MEIndicatorDTO::new).collect(Collectors.toList());
    }

    private GeneratedReport runIndicatorReport(SettingsAndFiltersParameters settingsAndFilters) {
        ReportSpecificationImpl
                spec = new ReportSpecificationImpl("indicator-data", ArConstants.INDICATOR_TYPE);

        spec.addColumn(new ReportColumn(ColumnConstants.INDICATOR_NAME));
        spec.getHierarchies().add(new ReportColumn(ColumnConstants.INDICATOR_NAME));
        spec.addMeasure(new ReportMeasure(MeasureConstants.INDICATOR_ACTUAL_VALUE));
        spec.setSummaryReport(true);
        spec.setGroupingCriteria(GroupingCriteria.GROUPING_YEARLY);

        applySettingsAndFilters(settingsAndFilters, spec);

        return EndpointUtils.runReport(spec, ReportAreaImpl.class, null);
    }

    public void applySettingsAndFilters(
            SettingsAndFiltersParameters settingsAndFilters,
            ReportSpecificationImpl spec) {

        Map<String, Object> filters = settingsAndFilters.getFilters();
        if (filters == null) {
            filters = new LinkedHashMap<>();
        }
        AmpReportFilters filterRules = FilterUtils.getFilterRules(filters, null);
        if (filterRules != null) {
            spec.setFilters(filterRules);
        }

        SettingsUtils.applySettings(spec, settingsAndFilters.getSettings(), true);
    }

    private List<AmpIndicator> getAllAmpIndicators() {
        try {
            return IndicatorUtil.getAllIndicators();
        } catch (DgException e) {
            throw new RuntimeException("Failed to load indicators");
        }
    }

    private AmpIndicator getIndicatorById(Long indicatorId) {
        try {
            return IndicatorUtil.getIndicator(indicatorId);
        } catch (DgException e) {
            throw new RuntimeException("Failed to load indicator");
        }
    }

    // Helper method to clone the original params and update the "national-planning-objectives-level-2" filter
    private SettingsAndFiltersParameters cloneWithSingleObjective(SettingsAndFiltersParameters originalParams, Long objectiveId) {
        // Implement the cloning or creation of a new instance based on the original
        // This could involve deep copying fields or creating a new instance and manually copying values
        SettingsAndFiltersParameters modifiedParams = new SettingsAndFiltersParameters();

        // Copy settings and other filters as needed
        modifiedParams.setSettings(originalParams.getSettings());

        // Update the "national-planning-objectives-level-2" filter with a single objectiveId
        Map<String, Object> filters = new HashMap<>(originalParams.getFilters());
        filters.put("national-planning-objectives-level-1", Arrays.asList(objectiveId.intValue())); // Convert back to Integer if necessary
        modifiedParams.setFilters(filters);

        return modifiedParams;
    }

    public Map<Long, BaseTargetValue> getBaseTargetValues(SettingsAndFiltersParameters params) {
        List<Long> countriesId = (List<Long>) params.getFilters().get("administrative-level-0");
        List<Long> pillarsId = (List<Long>) params.getFilters().get("national-planning-objectives-level-1");
        List<Long> donorsId = (List<Long>) params.getFilters().get("donor-agency");
        StringBuilder query = new StringBuilder();
        query.append(" select i.indicator_id, iv.value_type, sum(iv.value) ");
        query.append(" FROM amp_indicator_connection ic ");
        query.append(" JOIN amp_activity_location al ON ic.activity_location = al.amp_activity_location_id ");
        query.append(" JOIN amp_indicator_values iv ON ic.id = iv.ind_connect_id ");
        query.append(" join amp_indicator i on i.indicator_id = ic.indicator_id ");
        query.append(" join amp_org_role o on o.activity = ic.activity_id ");
        query.append(" where ic.sub_clazz = 'a' ");
        query.append(" and iv.value_type in (0, 2) ");
        if (pillarsId != null && !pillarsId.isEmpty()) {
            query.append(" and (i.program_id = ");
            query.append(org.dgfoundation.amp.Util.toCSString(pillarsId));
            query.append(" or i.program_id in ");
            query.append(" (select amp_theme_id from amp_theme where parent_theme_id = ");
            query.append(org.dgfoundation.amp.Util.toCSString(pillarsId));
            query.append(")) ");
        }

        if (countriesId != null && !countriesId.isEmpty()) {
            query.append(" AND al.location_id in (");
            query.append(org.dgfoundation.amp.Util.toCSString(countriesId));
            query.append(")");
        }
        query.append(" and o.role = 1 ");
        if (donorsId != null && !donorsId.isEmpty()) {
            query.append(" and o.organisation in(");
            query.append(org.dgfoundation.amp.Util.toCSString(donorsId));

            query.append(" ) ");
        }
        query.append(" group by i.indicator_id, iv.value_type;");
        Map<Long, BaseTargetValue> indicatorBaseTargetValues = new HashMap<>();
        PersistenceManager.getSession().doWork(new Work() {
            public void execute(Connection connection) throws SQLException {
                RsInfo rsi = SQLUtils.rawRunQuery(connection, query.toString(), null);
                ResultSet rs = rsi.rs;
                while (rs.next()) {
                    Long indicatorId = rs.getLong("indicator_id");
                    int valueType = rs.getInt("value_type");
                    BigDecimal value = rs.getBigDecimal("sum");
                    BaseTargetValue baseTargetValue = indicatorBaseTargetValues.get(indicatorId);
                    if (baseTargetValue == null) {
                        baseTargetValue = new BaseTargetValue();
                        indicatorBaseTargetValues.put(indicatorId, baseTargetValue);
                    }
                    if (valueType == 0) {
                        baseTargetValue.setTarget(value);
                    } else {
                        baseTargetValue.setBase(value);
                    }
                }
            }
        });
        return indicatorBaseTargetValues;
    }

    public Map<Long, List<YearValue>> getAllIndicatorYearValuesWithActualValues2(SettingsAndFiltersParameters params) {
        List<Long> countriesId = (List<Long>) params.getFilters().get("administrative-level-0");
        List<Long> pillarsId = (List<Long>) params.getFilters().get("national-planning-objectives-level-1");
        List<Long> donorsId = (List<Long>) params.getFilters().get("donor-agency");

        StringBuilder query = new StringBuilder();
        query.append("SELECT ic.activity_id AS amp_activity_id, ")
                .append("iv.value, ")
                .append("iv.value_type, ")
                .append("EXTRACT(YEAR FROM iv.value_date) AS value_year, ")
                .append("ic.indicator_id AS me_indicator_id, ")
                .append("al.location_id, ")
                .append("o.organisation ")
                .append("FROM amp_indicator_connection ic ")
                .append("JOIN amp_activity_location al ON ic.activity_location = al.amp_activity_location_id ")
                .append("JOIN amp_indicator_values iv ON ic.id = iv.ind_connect_id ")
                .append("JOIN amp_indicator i ON i.indicator_id = ic.indicator_id ")
                .append("JOIN amp_org_role o ON o.activity = ic.activity_id ")
                .append("WHERE ic.sub_clazz = 'a' ")
                .append("AND (i.program_id IN (")
                .append(org.dgfoundation.amp.Util.toCSString(pillarsId))
                .append(") OR i.program_id IN (")
                .append("SELECT amp_theme_id FROM amp_theme WHERE parent_theme_id IN (")
                .append(org.dgfoundation.amp.Util.toCSString(pillarsId))
                .append("))) ")
                .append("AND o.role = 1 ")
                .append("AND iv.value_type = 1");

        if (countriesId != null && !countriesId.isEmpty()) {
            query.append(" AND al.location_id IN (")
                    .append(org.dgfoundation.amp.Util.toCSString(countriesId))
                    .append(")");
        }

        if (donorsId != null && !donorsId.isEmpty()) {
            query.append(" AND o.organisation IN (")
                    .append(org.dgfoundation.amp.Util.toCSString(donorsId))
                    .append(")");
        }

        Map<Long, List<YearValue>> data = new HashMap<>();

        PersistenceManager.getSession().doWork(new Work() {
            public void execute(Connection connection) throws SQLException {
                RsInfo rsi = SQLUtils.rawRunQuery(connection, query.toString(), null);
                ResultSet rs = rsi.rs;
                while (rs.next()) {
                    Long indicatorId = rs.getLong("me_indicator_id");
                    Long year = rs.getLong("value_year");
                    BigDecimal value = rs.getBigDecimal("value");

                    if (!data.containsKey(indicatorId)) {
                        data.put(indicatorId, new ArrayList<>());
                    }
                    data.get(indicatorId).add(new YearValue(year.intValue(), value));
                }
            }
        });


        return data;
    }
}
