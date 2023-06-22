package org.digijava.kernel.ampapi.endpoints.ndd.me;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.newreports.AmpReportFilters;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.digijava.kernel.ampapi.endpoints.gis.SettingsAndFiltersParameters;
import org.digijava.kernel.ampapi.endpoints.indicator.manager.MEIndicatorDTO;
import org.digijava.kernel.ampapi.endpoints.ndd.utils.DashboardUtils;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.IndicatorActivity;
import org.digijava.module.aim.dbentity.IndicatorTheme;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class MeService {
    protected static final Logger logger = Logger.getLogger(MeService.class);
    
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

    static MeReportDTO generateProgramsByValueReport(SettingsAndFiltersParameters params) {
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

    static List<MEIndicatorDTO> getIndicatorsByProgram(Long programId) {
        List<AmpIndicator> indicators = new ArrayList<>();
        AmpTheme program = ProgramUtil.getThemeById(programId);
        Set<IndicatorTheme> programIndicators =  program.getIndicators();


        programIndicators.forEach(indicator -> {
            indicators.add(indicator.getIndicator());
        });

        return indicators.stream().map(MEIndicatorDTO::new).collect(Collectors.toList());
    }

    static MeReportDTO generateIndicatorsByProgramsReport(SettingsAndFiltersParameters params) {
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

        indicator.getValuesActivity().forEach(valueActivity -> {
            if (valueActivity.getValues() != null)  {
                valueActivity.getValues().forEach(value -> {
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
            }
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

    static List<MEIndicatorDTO> getIndicatorsBySector(Long sectorId) {
        AmpSector sector = SectorUtil.getAmpSector(sectorId);
        return sector.getIndicators().stream().map(MEIndicatorDTO::new).collect(Collectors.toList());
    }
}
