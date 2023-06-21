package org.digijava.kernel.ampapi.endpoints.ndd.me;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.newreports.AmpReportFilters;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.digijava.kernel.ampapi.endpoints.gis.SettingsAndFiltersParameters;
import org.digijava.kernel.ampapi.endpoints.ndd.utils.DashboardUtils;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.IndicatorTheme;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.util.ProgramUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class MeService {
    protected static final Logger logger = Logger.getLogger(MeService.class);

    static MeReportDTO generateProgramsByValueReport(SettingsAndFiltersParameters params) {
        AmpReportFilters filters = DashboardUtils.getFiltersFromParams(params.getFilters());
        ReportMeasure measures = DashboardUtils.getMeasureFromParams(params.getSettings());


        Long programId = Long.valueOf(params.getSettings().get("programId").toString());

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

                    // set the last date
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
        int baseYear = DateConversion.getYear(DateConversion.convertDateToString(baseDate.get()));
        baseValue.setYear(Integer.toString(baseYear));
        baseValue.setDetail(baseAggregate.get());
        indicatorValues.setBase(baseValue);

        MeValue actualValue = new MeValue();
        int actualYear = DateConversion.getYear(DateConversion.convertDateToString(actualDate.get()));
        actualValue.setYear(Integer.toString(actualYear));
        actualValue.setDetail(actualAggregate.get());
        indicatorValues.setActual(actualValue);

        MeValue targetValue = new MeValue();
        int targetYear = DateConversion.getYear(DateConversion.convertDateToString(targetDate.get()));
        targetValue.setYear(Integer.toString(targetYear));
        targetValue.setDetail(targetAggregate.get());
        indicatorValues.setTarget(targetValue);

        // formula:  [(Current value - Base value) / (Target value - Base value)]*100
        double progressAggregate = ((actualAggregate.get() - baseAggregate.get()) / (targetAggregate.get() - baseAggregate.get()));
        int progress =  Math.round((float) progressAggregate * 100);

        return new MeReportDTO(progress, indicatorValues);
    }
}
