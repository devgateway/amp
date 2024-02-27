package org.digijava.kernel.ampapi.endpoints.dashboards.services;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
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
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiRuntimeException;
import org.digijava.kernel.ampapi.endpoints.gis.SettingsAndFiltersParameters;
import org.digijava.kernel.ampapi.endpoints.indicator.IndicatorYearValues;
import org.digijava.kernel.ampapi.endpoints.indicator.ProgramIndicatorValues;
import org.digijava.kernel.ampapi.endpoints.indicator.YearValue;
import org.digijava.kernel.ampapi.endpoints.indicator.manager.MEIndicatorDTO;
import org.digijava.kernel.ampapi.endpoints.indicator.manager.ProgramSchemeDTO;
import org.digijava.kernel.ampapi.endpoints.ndd.utils.DashboardUtils;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.util.IndicatorUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.hibernate.Session;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static org.digijava.module.aim.util.IndicatorUtil.findIndicatorConnectionByLocationCategoryValue;

public class MeService {
    protected static final Logger logger = Logger.getLogger(MeService.class);

    public static List<ProgramSchemeDTO> getProgramConfiguration () {
        List<AmpActivityProgramSettings> settings = ProgramUtil.getAmpActivityProgramSettingsList(true);

        //remove settings with no hierarchy
        settings.removeIf(setting -> setting.getDefaultHierarchy() == null);
        return settings.stream().map(ProgramSchemeDTO::new).collect(Collectors.toList());
    }


    public List<MEIndicatorDTO> getIndicatorsByProgram (Long programId) {
        List<AmpIndicator> indicators = new ArrayList<>();
        AmpTheme program = ProgramUtil.getThemeById(programId);

        Set<IndicatorTheme> programIndicators =  program.getIndicators();


        programIndicators.forEach(indicator -> {
            indicators.add(indicator.getIndicator());
        });

        return indicators.stream().map(MEIndicatorDTO::new).collect(Collectors.toList());
    }

    public List<MEIndicatorDTO> getIndicatorsBySector (Long sectorId) {
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
        return getIndicatorYearValues(existingIndicator, indicatorsWithYearValues, yearsCount);
    }

    public List<ProgramIndicatorValues> getIndicatorYearValuesByIndicatorCountryProgramId(SettingsAndFiltersParameters params) {
        List<ProgramIndicatorValues> programIndicatorValues = new ArrayList<ProgramIndicatorValues>();

        int yearsCount = Integer.valueOf(params.getSettings().get("yearCount").toString());

        if (yearsCount < 5) {
            yearsCount = 5;
        }

        // Getting params array of objectives
        List<Integer> objectiveIds = (List<Integer>) params.getFilters().get("national-planning-objectives-level-2");

        for (Integer objectiveId : objectiveIds) {
            Long id = Long.valueOf(objectiveId);
            AmpTheme objective = ProgramUtil.getThemeById(id);
            ProgramIndicatorValues programValues = new ProgramIndicatorValues(objective.getAmpThemeId(), objective.getName());

            // Clone or create a new instance of params for each objectiveId
            SettingsAndFiltersParameters modifiedParams = cloneWithSingleObjective(params, id);

            Map<Long, List<YearValue>> indicatorsWithYearValues = getAllIndicatorYearValuesWithActualValues(modifiedParams);

            List<IndicatorYearValues> indicatorValues = new ArrayList<IndicatorYearValues>();

            for (Map.Entry<Long, List<YearValue>> entry : indicatorsWithYearValues.entrySet()) {
                // Access the indicator ID (key)
                Long indicatorId = entry.getKey();
                AmpIndicator existingIndicator = getIndicatorById(indicatorId);
                IndicatorYearValues singelIndicatorYearValues = getIndicatorYearValues(existingIndicator, indicatorsWithYearValues, yearsCount);
                // Include indicators name
                singelIndicatorYearValues.setIndicatorName(existingIndicator.getName());
                indicatorValues.add(singelIndicatorYearValues);

            }
            // As an update we need to return indicators with also no values and give them values of 0
            addIndicatorsWithNoValues(params, id, indicatorValues, yearsCount);
            programValues.setIndicators(indicatorValues);
            programIndicatorValues.add(programValues);
        }

        return programIndicatorValues;
    }

    private void addIndicatorsWithNoValues(SettingsAndFiltersParameters params, Long id, List<IndicatorYearValues> indicatorValues, int yearsCount){
        // Get the first element of the location category value id, NOTE here we are taking the first element
        // since the api sends one location id in the array at a time
        List<Integer> locationIds = (List<Integer>) params.getFilters().get("administrative-level-0");
        if (locationIds != null && !locationIds.isEmpty()) {
            Long locationId = Long.valueOf(locationIds.get(0));
            List<IndicatorActivity> locationIndicatorConnections = new ArrayList<>();
            try {
                locationIndicatorConnections = findIndicatorConnectionByLocationCategoryValue(locationId);
            } catch (DgException e) {
                throw new RuntimeException(e);
            }

            // Filter indicators that contains the same objective
            List<IndicatorActivity> filteredIndicatorsList = locationIndicatorConnections.stream()
                    // Filter the list based on the nested ampThemeId matching the objectiveId
                    .filter(indicatorActivity -> indicatorActivity.getActivity() != null && indicatorActivity.getActivity().getActPrograms() != null)
                    .filter(indicatorActivity -> indicatorActivity.getActivity().getActPrograms().stream()
                            .anyMatch(ampActivityProgram -> ampActivityProgram.getProgram() != null && id.equals(ampActivityProgram.getProgram().getAmpThemeId())))
                    .collect(Collectors.toList());

            for(IndicatorActivity filteredIndicator :filteredIndicatorsList){
                if(!indicatorValues.stream()
                        .anyMatch(indicator -> indicator.getIndicator().equals(filteredIndicator.getIndicator()))) {

                    Map<Long, List<YearValue>> indicatorsWithYearValuesDummy = new HashMap<>();

                    IndicatorYearValues singelIndicatorYearValues = getIndicatorYearValues(filteredIndicator.getIndicator(), indicatorsWithYearValuesDummy, yearsCount);
                    // Include indicators name
                    singelIndicatorYearValues.setIndicatorName(filteredIndicator.getIndicator().getName());
                    indicatorValues.add(singelIndicatorYearValues);
                }
            }
        }
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
                                                       final Map<Long, List<YearValue>> indicatorsWithYearValues, final int yearsCount) {
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

        if (yearValues.size() > yearsCount) {
            yearValues = yearValues.subList(0, yearsCount);
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

    private void applySettingsAndFilters(
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

    private AmpIndicator getIndicatorById(Long indicatorId){
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
        filters.put("national-planning-objectives-level-2", Arrays.asList(objectiveId.intValue())); // Convert back to Integer if necessary
        modifiedParams.setFilters(filters);

        return modifiedParams;
    }

}
