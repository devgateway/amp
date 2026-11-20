package org.dgfoundation.amp.nireports.amp;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportFiltersImpl;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.MonetaryAmount;
import org.dgfoundation.amp.nireports.NiPrecisionSetting;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.TextCell;
import org.dgfoundation.amp.nireports.amp.indicators.IndicatorDisaggregationFilter;
import org.dgfoundation.amp.nireports.meta.MetaInfoSet;
import org.dgfoundation.amp.nireports.runtime.ColumnContents;
import org.dgfoundation.amp.nireports.runtime.NiCell;
import org.dgfoundation.amp.nireports.schema.NiDimension.Coordinate;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.dgfoundation.amp.nireports.schema.DimensionLevel;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IndicatorDisaggregationFilterTest {
    private final NiDimension dimension = new NiDimension("test", 1) {
        @Override
        protected List<DimensionLevel> fetchDimension() {
            return Collections.emptyList();
        }
    };
    private final LevelColumn indicator = level("indicator");
    private final LevelColumn parent = level("parent");
    private final LevelColumn child = level("child");
    private final LevelColumn third = level("third");
    private final List<NiDimensionUsage> levels =
            Arrays.asList(parent.dimensionUsage, child.dimensionUsage, third.dimensionUsage);

    @Test
    void matchesAnySelectedOptionAtAnyPosition() {
        Set<Long> selected = new java.util.HashSet<>(Arrays.asList(100L, 201L, 302L));
        assertTrue(AmpFiltersConverter.matchesDisaggregation(amount(100L, null, null), selected, levels));
        assertTrue(AmpFiltersConverter.matchesDisaggregation(amount(101L, 201L, null), selected, levels));
        assertTrue(AmpFiltersConverter.matchesDisaggregation(amount(101L, 202L, 302L), selected, levels));
        assertFalse(AmpFiltersConverter.matchesDisaggregation(amount(101L, 202L, 303L), selected, levels));
        assertFalse(AmpFiltersConverter.matchesDisaggregation(amount(null, null, null), selected, levels));
        assertTrue(AmpFiltersConverter.matchesDisaggregation(
                new TextCell("activity", 1, 1, Optional.empty()), selected, levels));
    }

    @Test
    void retainsOnlyIndicatorsWithMatchingCombinationsWithinEachActivity() {
        ReportSpecificationImpl spec = new ReportSpecificationImpl("indicator", ArConstants.INDICATOR_TYPE);
        ReportFiltersImpl filters = new ReportFiltersImpl();
        filters.addFilterRule(new ReportColumn(ColumnConstants.INDICATOR_DISAGGREGATION_FILTER),
                new FilterRule(Collections.singletonList("100"), true));
        spec.setFilters(filters);
        NiReportsEngine engine = new NiReportsEngine(null, spec, null);
        engine.fetchedColumns.put(ColumnConstants.INDICATOR_DISAGGREGATION_LEVEL_0,
                new ColumnContents(Collections.singletonList(wrap(indicatorCell(10, 1)))));
        engine.fetchedColumns.put("names", new ColumnContents(Arrays.asList(
                wrap(indicatorCell(10, 1)), wrap(indicatorCell(10, 2)), wrap(indicatorCell(20, 1)))));
        engine.fetchedColumns.put("activity", new ColumnContents(
                Collections.singletonList(wrap(new TextCell("Activity", 10, 10, Optional.empty())))));

        IndicatorDisaggregationFilter.restrictIndicatorColumns(engine, indicator.dimensionUsage);

        assertEquals(1, engine.fetchedColumns.get("names").countCells());
        assertTrue(engine.fetchedColumns.get("names").data.containsKey(10L));
        assertFalse(engine.fetchedColumns.get("names").data.containsKey(20L));
        assertEquals(1, engine.fetchedColumns.get("activity").countCells());
    }

    private CategAmountCell amount(Long parent, Long child, Long third) {
        Map<NiDimensionUsage, Coordinate> coordinates = new HashMap<>();
        add(coordinates, this.parent, parent);
        add(coordinates, this.child, child);
        add(coordinates, this.third, third);
        return new CategAmountCell(1,
                new MonetaryAmount(BigDecimal.ONE, NiPrecisionSetting.IDENTITY_PRECISION_SETTING),
                MetaInfoSet.empty(), coordinates, null);
    }

    private void add(Map<NiDimensionUsage, Coordinate> coordinates,
                     org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn level, Long id) {
        if (id != null) {
            coordinates.put(level.dimensionUsage, level.getCoordinate(id));
        }
    }

    private TextCell indicatorCell(long activity, long indicator) {
        return new TextCell("indicator " + indicator, activity, indicator,
                Optional.of(this.indicator));
    }

    private LevelColumn level(String name) {
        return dimension.getDimensionUsage(name).getLevelColumn(0);
    }

    private NiCell wrap(org.dgfoundation.amp.nireports.Cell cell) {
        return new NiCell(cell, null, null);
    }
}
