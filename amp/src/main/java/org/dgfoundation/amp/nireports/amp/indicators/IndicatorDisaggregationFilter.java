package org.dgfoundation.amp.nireports.amp.indicators;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportElement;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.amp.AmpReportsSchema;
import org.dgfoundation.amp.nireports.runtime.ColumnContents;
import org.dgfoundation.amp.nireports.runtime.NiCell;
import org.dgfoundation.amp.nireports.schema.NiDimension.Coordinate;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class IndicatorDisaggregationFilter {
    private IndicatorDisaggregationFilter() {
    }

    public static boolean isActive(NiReportsEngine engine) {
        if (engine.spec.getReportType() != ArConstants.INDICATOR_TYPE || engine.spec.getFilters() == null) {
            return false;
        }
        FilterRule rule = engine.spec.getFilters().getAllFilterRules().get(
                new ReportElement(new ReportColumn(ColumnConstants.INDICATOR_DISAGGREGATION_FILTER)));
        return rule != null && rule.values != null && !rule.values.isEmpty();
    }

    public static void restrictIndicatorColumns(NiReportsEngine engine) {
        if (!isActive(engine)) {
            return;
        }
        restrictIndicatorColumns(engine, AmpReportsSchema.INDICATOR_DIM_USG);
    }

    public static void restrictIndicatorColumns(NiReportsEngine engine, NiDimensionUsage indicatorDimension) {
        if (!isActive(engine)) {
            return;
        }
        ColumnContents anchor = Objects.requireNonNull(
                engine.fetchedColumns.get(ColumnConstants.INDICATOR_DISAGGREGATION_LEVEL_0),
                "The disaggregation filter could not load its combination data");
        Map<Long, Set<Long>> indicatorsByActivity = new HashMap<>();
        anchor.data.forEach((activityId, cells) -> {
            Set<Long> indicators = indicatorsByActivity.computeIfAbsent(activityId, ignored -> new HashSet<>());
            for (NiCell cell : cells) {
                Coordinate indicator = cell.getCell().getCoordinates().get(indicatorDimension);
                if (indicator != null) {
                    indicators.add(indicator.id);
                }
            }
        });
        engine.fetchedColumns.values().forEach(contents -> contents.data.entrySet().removeIf(entry -> {
            entry.getValue().removeIf(cell -> {
                    Coordinate indicator = cell.getCell().getCoordinates().get(indicatorDimension);
                    return indicator != null
                            && !indicatorsByActivity.getOrDefault(entry.getKey(), Collections.emptySet())
                                    .contains(indicator.id);
                });
            return entry.getValue().isEmpty();
        }));
    }
}
