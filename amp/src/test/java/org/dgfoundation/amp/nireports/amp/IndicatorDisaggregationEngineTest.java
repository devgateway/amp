package org.dgfoundation.amp.nireports.amp;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportEnvBuilder;
import org.dgfoundation.amp.newreports.ReportFiltersImpl;
import org.dgfoundation.amp.newreports.ReportRenderWarning;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.MonetaryAmount;
import org.dgfoundation.amp.nireports.NiFilters;
import org.dgfoundation.amp.nireports.NiPrecisionSetting;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.TextCell;
import org.dgfoundation.amp.nireports.amp.indicators.IndicatorDisaggregationFilter;
import org.dgfoundation.amp.nireports.amp.indicators.IndicatorDisaggregationTextualTokenBehaviour;
import org.dgfoundation.amp.nireports.amp.indicators.IndicatorReportDataOutputter;
import org.dgfoundation.amp.nireports.behaviours.TextualTokenBehaviour;
import org.dgfoundation.amp.nireports.behaviours.TrivialMeasureBehaviour;
import org.dgfoundation.amp.nireports.meta.MetaInfoSet;
import org.dgfoundation.amp.nireports.output.NiColumnReportData;
import org.dgfoundation.amp.nireports.output.NiGroupReportData;
import org.dgfoundation.amp.nireports.output.NiReportData;
import org.dgfoundation.amp.nireports.output.NiReportDataOutputter;
import org.dgfoundation.amp.nireports.output.NiReportRunResult;
import org.dgfoundation.amp.nireports.runtime.CellColumn;
import org.dgfoundation.amp.nireports.schema.Behaviour;
import org.dgfoundation.amp.nireports.schema.DimensionLevel;
import org.dgfoundation.amp.nireports.schema.IdsAcceptor;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.dgfoundation.amp.nireports.schema.NiDimension.Coordinate;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;
import org.dgfoundation.amp.nireports.schema.NiReportColumn;
import org.dgfoundation.amp.nireports.schema.NiReportMeasure;
import org.dgfoundation.amp.nireports.schema.NiReportsSchema;
import org.dgfoundation.amp.nireports.schema.NiTransactionMeasure;
import org.dgfoundation.amp.nireports.schema.SchemaSpecificScratchpad;
import org.dgfoundation.amp.nireports.testcases.ReportsTestScratchpad;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IndicatorDisaggregationEngineTest {
    private static final String PARENT = ColumnConstants.INDICATOR_DISAGGREGATION_LEVEL_0;
    private static final String CHILD = ColumnConstants.INDICATOR_DISAGGREGATION_LEVEL_1;
    private static final String THIRD = ColumnConstants.INDICATOR_DISAGGREGATION_LEVEL_2;
    private static final String NAME = "Indicator name";
    private static final String ACTUAL = "actual";
    private final NiDimension dimension = new NiDimension("test", 1) {
        @Override
        protected List<DimensionLevel> fetchDimension() {
            return Collections.singletonList(new DimensionLevel(0, Collections.emptyMap(),
                    Collections.emptyMap(), true));
        }
    };
    private final LevelColumn indicator = level("indicator");
    private final LevelColumn parent = level("parent");
    private final LevelColumn child = level("child");
    private final LevelColumn third = level("third");
    private final List<LevelColumn> levels = Arrays.asList(parent, child, third);
    private final List<String> levelNames = Arrays.asList(PARENT, CHILD, THIRD);

    @Test
    void filtersAcrossLevelsBeforeProducingRowsAndFooter() {
        NiReportRunResult result = run(Arrays.asList(NAME, PARENT, CHILD, THIRD),
                Collections.singletonList(ACTUAL), Collections.emptyList(), false, "201", "302");

        assertEquals(2, leaves(result.reportOut).stream().mapToInt(leaf -> leaf.orderedIds.size()).sum());
        assertEquals(new HashSet<>(Arrays.asList("22", "33")), values(result.reportOut, ACTUAL));
        assertEquals("55", footer(result.reportOut, ACTUAL));
        assertEquals(new HashSet<>(Arrays.asList("First", "Second")), values(result.reportOut, NAME));
    }

    @Test
    void filtersWhenNoDisaggregationColumnOrMeasureIsDisplayed() {
        NiReportRunResult result = run(Collections.singletonList(NAME), Collections.emptyList(),
                Collections.emptyList(), false, "201");

        assertEquals(Collections.singleton("First"), values(result.reportOut, NAME));
        assertEquals(Collections.singleton(10L), result.reportOut.ids.stream()
                .map(id -> id.id).collect(Collectors.toSet()));
    }

    @Test
    void leavesUnfilteredObservationsAndTotalsUnchanged() {
        NiReportRunResult result = run(Arrays.asList(NAME, PARENT, CHILD),
                Collections.singletonList(ACTUAL), Collections.emptyList(), false);

        assertEquals(5, leaves(result.reportOut).stream().mapToInt(leaf -> leaf.orderedIds.size()).sum());
        assertEquals("117", footer(result.reportOut, ACTUAL));
    }

    @Test
    void filtersAcrossLevelsWhenOnlyOneLevelIsDisplayed() {
        NiReportRunResult result = run(Arrays.asList(NAME, PARENT),
                Collections.singletonList(ACTUAL), Collections.emptyList(), false, "201", "302");

        assertEquals(2, leaves(result.reportOut).stream().mapToInt(leaf -> leaf.orderedIds.size()).sum());
        assertEquals(new HashSet<>(Arrays.asList("101", "102")), values(result.reportOut, PARENT));
        assertEquals("55", footer(result.reportOut, ACTUAL));
    }

    @Test
    void filtersMeasurelessCombinationRows() {
        NiReportRunResult result = run(Arrays.asList(NAME, PARENT, CHILD), Collections.emptyList(),
                Collections.emptyList(), false, "201");

        assertEquals(1, leaves(result.reportOut).stream().mapToInt(leaf -> leaf.orderedIds.size()).sum());
        assertEquals(Collections.singleton("First"), values(result.reportOut, NAME));
        assertEquals(Collections.singleton("101"), values(result.reportOut, PARENT));
        assertEquals(Collections.singleton("201"), values(result.reportOut, CHILD));
    }

    @Test
    void returnsNoRowsWhenNoCombinationMatches() {
        NiReportRunResult result = run(Arrays.asList(NAME, PARENT), Collections.singletonList(ACTUAL),
                Collections.emptyList(), false, "999");

        assertEquals(Collections.emptySet(), result.reportOut.ids);
    }

    @Test
    void filtersSummaryTotals() {
        NiReportRunResult result = run(Arrays.asList(NAME, PARENT), Collections.singletonList(ACTUAL),
                Collections.emptyList(), true, "201", "302");

        assertEquals("55", footer(result.reportOut, ACTUAL));
    }

    @Test
    void filtersCombinationRowsUnderDisaggregationHierarchy() {
        NiReportRunResult result = run(Arrays.asList(NAME, PARENT, CHILD), Collections.singletonList(ACTUAL),
                Collections.singletonList(PARENT), false, "201", "302");

        assertEquals(2, leaves(result.reportOut).stream().mapToInt(leaf -> leaf.orderedIds.size()).sum());
        assertEquals(new HashSet<>(Arrays.asList("22", "33")), values(result.reportOut, ACTUAL));
        assertEquals("55", footer(result.reportOut, ACTUAL));
    }

    @Test
    void rejectsUnsupportedDisaggregationFilterShapes() {
        TestSchema schema = new TestSchema();
        ReportSpecificationImpl spec = new ReportSpecificationImpl("test", ArConstants.INDICATOR_TYPE);
        ReportFiltersImpl filters = new ReportFiltersImpl();
        filters.addFilterRule(new ReportColumn(ColumnConstants.INDICATOR_DISAGGREGATION_FILTER),
                new FilterRule("100", true));
        spec.setFilters(filters);
        NiReportsEngine engine = new NiReportsEngine(schema, spec, ReportEnvBuilder.dummy());

        assertThrows(IllegalArgumentException.class, () -> schema.convertFilters(engine));
    }

    private NiReportRunResult run(List<String> columns, List<String> measures, List<String> hierarchies,
                                  boolean summary, String... selected) {
        ReportSpecificationImpl spec = ReportSpecificationImpl.buildFor("test", columns, measures, hierarchies,
                GroupingCriteria.GROUPING_TOTALS_ONLY, ArConstants.INDICATOR_TYPE);
        spec.setSummaryReport(summary);
        if (selected.length > 0) {
            ReportFiltersImpl filters = new ReportFiltersImpl();
            filters.addFilterRule(new ReportColumn(ColumnConstants.INDICATOR_DISAGGREGATION_FILTER),
                    new FilterRule(Arrays.asList(selected), true));
            spec.setFilters(filters);
        }
        return new NiReportsEngine(new TestSchema(), spec, ReportEnvBuilder.dummy()).execute();
    }

    private List<NiColumnReportData> leaves(NiReportData data) {
        if (data instanceof NiColumnReportData) {
            return Collections.singletonList((NiColumnReportData) data);
        }
        return ((NiGroupReportData) data).subreports.stream().flatMap(child -> leaves(child).stream())
                .collect(Collectors.toList());
    }

    private Set<String> values(NiReportData data, String name) {
        return leaves(data).stream().flatMap(leaf -> leaf.contents.entrySet().stream())
                .filter(entry -> entry.getKey().name.equals(name))
                .flatMap(entry -> entry.getValue().values().stream())
                .map(cell -> cell.getDisplayedValue()).collect(Collectors.toSet());
    }

    private String footer(NiReportData data, String name) {
        return data.trailCells.entrySet().stream().filter(entry -> entry.getKey().name.equals(name))
                .findFirst().get().getValue().getDisplayedValue();
    }

    private LevelColumn level(String name) {
        return dimension.getDimensionUsage(name).getLevelColumn(0);
    }

    private Map<NiDimensionUsage, Coordinate> coordinates(long indicatorId, Long parentId, Long childId, Long thirdId) {
        Map<NiDimensionUsage, Coordinate> coordinates = new HashMap<>();
        coordinates.put(indicator.dimensionUsage, indicator.getCoordinate(indicatorId));
        List<Long> ids = Arrays.asList(parentId, childId, thirdId);
        for (int i = 0; i < levels.size(); i++) {
            if (ids.get(i) != null) {
                coordinates.put(levels.get(i).dimensionUsage, levels.get(i).getCoordinate(ids.get(i)));
            }
        }
        return coordinates;
    }

    private TextCell label(long activity, long indicatorId, long parentId, long childId, long thirdId,
                           LevelColumn level) {
        Map<NiDimensionUsage, Coordinate> coordinates = coordinates(indicatorId, parentId, childId, thirdId);
        long id = coordinates.get(level.dimensionUsage).id;
        return new TextCell(Long.toString(id), activity, id, coordinates, Optional.of(level));
    }

    private CategAmountCell amount(long activity, long indicatorId, Long parentId, Long childId, Long thirdId,
                                   int value) {
        return new CategAmountCell(activity,
                new MonetaryAmount(BigDecimal.valueOf(value), NiPrecisionSetting.IDENTITY_PRECISION_SETTING),
                MetaInfoSet.empty(), coordinates(indicatorId, parentId, childId, thirdId), null);
    }

    private class TestSchema implements NiReportsSchema {
        private final Map<String, NiReportColumn<? extends Cell>> columns = new LinkedHashMap<>();
        private final Map<String, NiReportMeasure<?>> measures = new LinkedHashMap<>();
        private final NiReportColumn<CategAmountCell> funding;

        TestSchema() {
            List<long[]> combinations = Arrays.asList(new long[]{10, 1, 100, 200, 300},
                    new long[]{10, 1, 101, 201, 301}, new long[]{10, 2, 102, 202, 302},
                    new long[]{20, 3, 103, 203, 303});
            columns.put(NAME, new TestColumn<>(NAME, indicator, TextualTokenBehaviour.instance, Arrays.asList(
                    new TextCell("First", 10, 1, Optional.of(indicator)),
                    new TextCell("Second", 10, 2, Optional.of(indicator)),
                    new TextCell("Third", 20, 3, Optional.of(indicator)))));
            for (int i = 0; i < levels.size(); i++) {
                LevelColumn level = levels.get(i);
                List<TextCell> labels = new ArrayList<>();
                combinations.forEach(combo -> labels.add(label(combo[0], combo[1], combo[2], combo[3], combo[4], level)));
                columns.put(levelNames.get(i), new TestColumn<>(levelNames.get(i), level,
                        new IndicatorDisaggregationTextualTokenBehaviour(
                                (engine, cell) -> ((TextCell) cell).text, indicator.dimensionUsage, false) {
                            @Override
                            public Cell filterCell(Map<NiDimensionUsage, IdsAcceptor> acceptors, Cell oldCell,
                                                   Cell splitCell, boolean transactionLevel) {
                                return Behaviour.cellMeetsCoos(acceptors, oldCell, splitCell, transactionLevel)
                                        ? oldCell : null;
                            }
                        }, labels));
            }
            funding = new TestColumn<>("funding", null, TrivialMeasureBehaviour.getInstance(), Arrays.asList(
                    amount(10, 1, 100L, 200L, 300L, 11),
                    amount(10, 1, 101L, 201L, 301L, 22),
                    amount(10, 2, 102L, 202L, 302L, 33),
                    amount(20, 3, 103L, 203L, 303L, 44),
                    amount(10, 1, null, null, null, 7)));
            measures.put(ACTUAL, new NiTransactionMeasure(ACTUAL, cell -> true, null));
        }

        @Override public Map<String, NiReportColumn<? extends Cell>> getColumns() { return columns; }
        @Override public Map<String, NiReportMeasure<?>> getMeasures() { return measures; }
        @Override public NiReportColumn<CategAmountCell> getFundingFetcher(NiReportsEngine engine) { return funding; }
        @Override public SchemaSpecificScratchpad generateScratchpad(NiReportsEngine engine) {
            return new ReportsTestScratchpad(engine);
        }
        @Override public SubDimensions getSubDimensions() { return new SubDimensions(Collections.emptyMap()); }
        @Override public NiFilters convertFilters(NiReportsEngine engine) {
            return new AmpFiltersConverter(engine) {
                @Override protected List<NiDimensionUsage> getDisaggregationLevels() {
                    return levels.stream().map(level -> level.dimensionUsage).collect(Collectors.toList());
                }
            }.buildNiFilters(ignored -> new HashSet<>(Arrays.asList(10L, 20L)));
        }
        @Override public void processFetchedData(NiReportsEngine engine) {
            IndicatorDisaggregationFilter.restrictIndicatorColumns(engine, indicator.dimensionUsage);
        }
        @Override public NiReportDataOutputter getReportDataOutputter(NiReportsEngine engine) {
            return engine.spec.isSummaryReport() ? NiReportsSchema.super.getReportDataOutputter(engine)
                    : new IndicatorReportDataOutputter(engine.headers, engine, indicator.dimensionUsage,
                            levels, levelNames);
        }
    }

    private static class TestColumn<K extends Cell> extends NiReportColumn<K> {
        private final List<K> cells;

        TestColumn(String name, LevelColumn level, Behaviour<?> behaviour, List<K> cells) {
            super(name, level, behaviour, null);
            this.cells = cells;
        }
        @Override public List<K> fetch(NiReportsEngine engine) { return cells; }
        @Override public List<ReportRenderWarning> performCheck() { return Collections.emptyList(); }
    }
}
