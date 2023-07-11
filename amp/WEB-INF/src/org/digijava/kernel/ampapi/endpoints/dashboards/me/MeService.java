package org.digijava.kernel.ampapi.endpoints.dashboards.me;

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
import org.digijava.kernel.ampapi.endpoints.indicator.YearValue;
import org.digijava.kernel.ampapi.endpoints.indicator.manager.MEIndicatorDTO;
import org.digijava.kernel.ampapi.endpoints.indicator.manager.ProgramSchemeDTO;
import org.digijava.kernel.ampapi.endpoints.ndd.utils.DashboardUtils;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.IndicatorTheme;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.util.IndicatorUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.hibernate.Session;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

public class MeService {
    protected static final Logger logger = Logger.getLogger(MeService.class);

    public static List<ProgramSchemeDTO> getProgramConfiguration () {
        List<AmpActivityProgramSettings> settings = ProgramUtil.getAmpActivityProgramSettingsList(true);
        return settings.stream().map(ProgramSchemeDTO::new).collect(Collectors.toList());
    }

    private static String getYearString(Date date) {
        int year = DateConversion.getYear(DateConversion.convertDateToString(date));
        return Integer.toString(year);
    }

    private static int calculateProgress(double base, double actual, double target) {
        if (target == 0) {
            return 0;
        }

        // formula:  [(Current value - Base value) / (Target value - Base value)]*100
        double progressAggregate = (actual - base) / (target - base);
        return Math.round((float) progressAggregate * 100);
    }

    private static IndicatorValues createAggregate (Long programId) {
        IndicatorValues indicatorValues = new IndicatorValues();

        AtomicReference<Double> baseAggregate = new AtomicReference<>(0.0);
        AtomicReference<Double> actualAggregate = new AtomicReference<>(0.0);
        AtomicReference<Double> targetAggregate = new AtomicReference<>(0.0);

        AtomicReference<Date> baseDate = new AtomicReference<>(null);
        AtomicReference<Date> actualDate = new AtomicReference<>(null);
        AtomicReference<Date> targetDate = new AtomicReference<>(null);

        AmpTheme program = ProgramUtil.getThemeById(programId);
        Set<IndicatorTheme> programIndicators =  program.getIndicators();

        programIndicators.forEach(indicator -> {
            indicator.getValues().forEach(value -> {
                int indicatorType = value.getValueType();

                if (indicatorType == AmpIndicatorValue.BASE) {
                    baseAggregate.updateAndGet(v -> v + value.getValue());

                    if (baseDate.get() == null || baseDate.get().before(value.getValueDate())) {
                        baseDate.set(value.getValueDate());
                    }
                } else if (indicatorType == AmpIndicatorValue.ACTUAL) {
                    actualAggregate.updateAndGet(v -> v + value.getValue());

                    if (actualDate.get() == null || actualDate.get().before(value.getValueDate())) {
                        actualDate.set(value.getValueDate());
                    }
                } else if (indicatorType == AmpIndicatorValue.TARGET) {
                    targetAggregate.updateAndGet(v -> v + value.getValue());

                    if (targetDate.get() == null || targetDate.get().before(value.getValueDate())) {
                        targetDate.set(value.getValueDate());
                    }
                }
            });
        });

        // set indicator values
        MeValue baseValue = new MeValue();
        baseValue.setYear(MeService.getYearString(baseDate.get()));
        baseValue.setDetail(baseAggregate.get());
        indicatorValues.setBase(baseValue);

        MeValue actualValue = new MeValue();
        actualValue.setYear(MeService.getYearString(actualDate.get()));
        actualValue.setDetail(actualAggregate.get());
        indicatorValues.setActual(actualValue);

        MeValue targetValue = new MeValue();
        targetValue.setYear(MeService.getYearString(targetDate.get()));
        targetValue.setDetail(targetAggregate.get());
        indicatorValues.setTarget(targetValue);

        return indicatorValues;
    }

    public static MeReportDTO generateProgramsByValueReport (
            SettingsAndFiltersParameters params) {
        AmpReportFilters filters = DashboardUtils.getFiltersFromParams(params.getFilters());
        ReportMeasure measures = DashboardUtils.getMeasureFromParams(params.getSettings());

        Long programId = Long.valueOf(params.getSettings().get("programId").toString());

        IndicatorValues indicatorValues = MeService.createAggregate(programId);
        double baseAggregate = indicatorValues.getBase().getDetail();
        double actualAggregate = indicatorValues.getActual().getDetail();
        double targetAggregate = indicatorValues.getTarget().getDetail();

        int progress = MeService.calculateProgress(baseAggregate, actualAggregate, targetAggregate);

        return new MeReportDTO(progress, indicatorValues);
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

    public MeReportDTO generateIndicatorsReport (
            SettingsAndFiltersParameters params) {
        AmpReportFilters filters = DashboardUtils.getFiltersFromParams(params.getFilters());
        ReportMeasure measures = DashboardUtils.getMeasureFromParams(params.getSettings());

        AtomicReference<Double> baseValueAggregate = new AtomicReference<>(0.0);
        AtomicReference<Double> currentValueAggregate = new AtomicReference<>(0.0);
        AtomicReference<Double> targetValueAggregate = new AtomicReference<>(0.0);

        AtomicReference<Date> baseDate = new AtomicReference<>(null);
        AtomicReference<Date> actualDate = new AtomicReference<>(null);
        AtomicReference<Date> targetDate = new AtomicReference<>(null);

        Long indicatorId = Long.valueOf(params.getSettings().get("indicatorId").toString());
        Session session = PersistenceManager.getSession();
        AmpIndicator indicator = (AmpIndicator) session.get(AmpIndicator.class, indicatorId);

        indicator.getValuesTheme().forEach(valueTheme -> {
            valueTheme.getValues().forEach(value -> {
                if (value.getValueType() == AmpIndicatorValue.BASE) {
                    baseValueAggregate.updateAndGet(v -> v + value.getValue());
                    if (baseDate.get() == null || baseDate.get().before(value.getValueDate())) {
                        baseDate.set(value.getValueDate());
                    }
                } else if (value.getValueType()  == AmpIndicatorValue.ACTUAL) {
                    currentValueAggregate.updateAndGet(v -> v + value.getValue());

                    if (actualDate.get() == null || actualDate.get().before(value.getValueDate())) {
                        actualDate.set(value.getValueDate());
                    }
                } else if (value.getValueType()  == AmpIndicatorValue.TARGET) {
                    targetValueAggregate.updateAndGet(v -> v + value.getValue());
                    if (targetDate.get() == null || targetDate.get().before(value.getValueDate())) {
                        targetDate.set(value.getValueDate());
                    }
                }
            });
        });

        IndicatorValues indicatorValues = new IndicatorValues();

        MeValue baseValue = new MeValue();
        baseValue.setYear(MeService.getYearString(baseDate.get()));
        baseValue.setDetail(baseValueAggregate.get());
        indicatorValues.setBase(baseValue);

        MeValue actualValue = new MeValue();
        actualValue.setYear(MeService.getYearString(actualDate.get()));
        actualValue.setDetail(baseValueAggregate.get());
        indicatorValues.setActual(actualValue);

        MeValue targetValue = new MeValue();
        targetValue.setYear(MeService.getYearString(targetDate.get()));
        targetValue.setDetail(targetValueAggregate.get());
        indicatorValues.setTarget(targetValue);

        int progress = MeService.calculateProgress(baseValueAggregate.get(), currentValueAggregate.get(), targetValueAggregate.get());

        return new MeReportDTO(progress, indicatorValues);
    }

    public List<MEIndicatorDTO> getIndicatorsBySector (Long sectorId) {
        AmpSector sector = SectorUtil.getAmpSector(sectorId);
        return sector.getIndicators().stream().map(MEIndicatorDTO::new).collect(Collectors.toList());
    }

    public IndicatorYearValues generateIndicatorProgressReport (Long indicatorId,
            SettingsAndFiltersParameters params) {
        int yearsCount = Integer.valueOf(params.getSettings().get("yearsCount").toString());

        if (yearsCount < 5) {
            yearsCount = 5;
        }

        Session session = PersistenceManager.getSession();
        AmpIndicator indicator = (AmpIndicator) session.get(AmpIndicator.class, indicatorId);
        Map<Long, List<YearValue>> indicatorsWithYearValues = getAllIndicatorYearValuesWithActualValues(params);
        return getIndicatorYearValues(indicator, indicatorsWithYearValues);
    }

    public ProgressReportDTO generateProgramProgressReport (
            SettingsAndFiltersParameters params) {
        AmpReportFilters filters = DashboardUtils.getFiltersFromParams(params.getFilters());
        ReportMeasure measures = DashboardUtils.getMeasureFromParams(params.getSettings());

        Long programId = Long.valueOf(params.getSettings().get("programId").toString());
        int yearsCount = Integer.valueOf(params.getSettings().get("yearsCount").toString());

        if (yearsCount < 5) {
            yearsCount = 5;
        }

        AmpTheme program = ProgramUtil.getThemeById(programId);
        Set<IndicatorTheme> programIndicators =  program.getIndicators();

        ProgressValue baseValue = new ProgressValue();
        ProgressValue actualValue = new ProgressValue();
        ProgressValue targetValue = new ProgressValue();

        programIndicators.forEach(indicator -> {
            indicator.getValues().forEach(indValue -> {
                if (indValue != null) {
                    String year = getYearString(indValue.getValueDate());
                    int valueType = indValue.getValueType();

                    if (valueType == AmpIndicatorValue.BASE) {
                        computeIndValuesPerYear(baseValue, indValue, year);
                    } else if (valueType == AmpIndicatorValue.ACTUAL) {
                        computeIndValuesPerYear(actualValue, indValue, year);
                    } else if (valueType == AmpIndicatorValue.TARGET) {
                        computeIndValuesPerYear(targetValue, indValue, year);
                    }
                }
            });
        });

        return new ProgressReportDTO(baseValue, actualValue, targetValue);
    }

    private static void computeIndValuesPerYear(ProgressValue baseValue,
                                   AmpIndicatorValue indValue, String year) {
        if (baseValue.getAmountsByYear().containsKey(year)) {
            baseValue.getAmountsByYear().put(
                    year, baseValue.getAmountsByYear().get(year) + indValue.getValue());
        } else {
            baseValue.getAmountsByYear().put(year, indValue.getValue());
        }
    }

    public IndicatorYearValues getIndicatorYearValuesByIndicatorId(Long indicatorId,
                                                                   SettingsAndFiltersParameters params) {
        AmpIndicator existingIndicator = getAllAmpIndicators().stream()
                .filter(indicator -> indicator.getIndicatorId().equals(indicatorId))
                .findFirst()
                .orElse(null);

        if (existingIndicator == null) {
            throw new ApiRuntimeException(NOT_FOUND,
                    ApiError.toError("Indicator with id " + indicatorId + " does not exist"));
        }

        Map<Long, List<YearValue>> indicatorsWithYearValues = getAllIndicatorYearValuesWithActualValues(params);
        return getIndicatorYearValues(existingIndicator, indicatorsWithYearValues);
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
}
