package org.dgfoundation.amp.nireports.amp.indicators;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.MonetaryAmount;
import org.dgfoundation.amp.nireports.NiHeaderInfo;
import org.dgfoundation.amp.nireports.NiPrecisionSetting;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.TextCell;
import org.dgfoundation.amp.nireports.amp.AmpIndicatorMeasure;
import org.dgfoundation.amp.nireports.amp.MetaCategory;
import org.dgfoundation.amp.nireports.amp.OutputSettings;
import org.dgfoundation.amp.nireports.behaviours.IndicatorMeasureBehaviour;
import org.dgfoundation.amp.nireports.behaviours.TextualTokenBehaviour;
import org.dgfoundation.amp.nireports.meta.MetaInfoSet;
import org.dgfoundation.amp.nireports.meta.MetaInfoGenerator;
import org.dgfoundation.amp.nireports.output.NiColumnReportData;
import org.dgfoundation.amp.nireports.output.CellFormatter;
import org.dgfoundation.amp.nireports.output.NiReportRunResult;
import org.dgfoundation.amp.nireports.output.NiReportsFormatter;
import org.dgfoundation.amp.nireports.output.NiRowId;
import org.dgfoundation.amp.nireports.runtime.CacheHitsCounter;
import org.dgfoundation.amp.nireports.runtime.CellColumn;
import org.dgfoundation.amp.nireports.runtime.ColumnContents;
import org.dgfoundation.amp.nireports.runtime.ColumnReportData;
import org.dgfoundation.amp.nireports.runtime.GroupColumn;
import org.dgfoundation.amp.nireports.runtime.HierarchiesTracker;
import org.dgfoundation.amp.nireports.runtime.NiCell;
import org.dgfoundation.amp.nireports.schema.DimensionLevel;
import org.dgfoundation.amp.nireports.schema.Behaviour;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.dgfoundation.amp.nireports.schema.NiDimension.Coordinate;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;
import org.digijava.kernel.translator.LocalizableLabel;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class IndicatorReportDataOutputterTest {
    private static final long ACTIVITY = 10;
    private static final long MISSING = ColumnReportData.UNALLOCATED_ID;
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
    private final NiReportsEngine engine = new NiReportsEngine(null,
            new ReportSpecificationImpl("indicator", ArConstants.INDICATOR_TYPE), null);

    @Test
    void reportsStoredCombinationsAndTheirValuesWithoutHierarchies() {
        GroupColumn root = root();
        CellColumn names = textColumn(root, "indicator", Arrays.asList(
                name(1, "First"), name(2, "Second")));
        CellColumn parents = textColumn(root, "parent", Arrays.asList(
                option(parent, 1, 100, 200, 300, "Female"),
                option(parent, 1, 100, 201, MISSING, "Female"),
                option(parent, 2, 100, 200, 301, "Female")));
        CellColumn children = textColumn(root, "child", Arrays.asList(
                option(child, 1, 100, 200, 300, "Adult"),
                option(child, 1, 100, 201, MISSING, "Youth"),
                option(child, 2, 100, 200, 301, "Adult")));
        CellColumn thirds = textColumn(root, "third", Arrays.asList(
                option(third, 1, 100, 200, 300, "Rural"),
                option(third, 1, 100, 201, MISSING, ""),
                option(third, 2, 100, 200, 301, "Urban")));
        CellColumn actuals = amountColumn(root, "actual", Arrays.asList(
                disaggregationActual(1, 100, 200, 300, 11),
                disaggregationActual(1, 100, 201, MISSING, 22),
                disaggregationActual(2, 100, 200, 301, 33)));

        NiColumnReportData result = output(root);

        assertEquals(3, result.orderedIds.size());
        assertRow(result, names, parents, children, thirds, actuals, 1, 100, 200, 300,
                "First", "Female", "Adult", "Rural", "11");
        assertRow(result, names, parents, children, thirds, actuals, 1, 100, 201, MISSING,
                "First", "Female", "Youth", "", "22");
        assertRow(result, names, parents, children, thirds, actuals, 2, 100, 200, 301,
                "Second", "Female", "Adult", "Urban", "33");
        assertEquals("66", result.trailCells.get(actuals).getDisplayedValue());

        NiHeaderInfo headers = new NiHeaderInfo(engine, root, 0);
        NiReportRunResult run = new NiReportRunResult(result, null, 0, headers, new TreeMap<>(), null);
        CellFormatter formatter = new CellFormatter(null, null, "yyyy-MM-dd", Function.identity(),
                new OutputSettings(Collections.emptySet()), null);
        GeneratedReport generated = new NiReportsFormatter(engine.spec, run, formatter).format();
        Set<String> rows = generated.reportContents.getChildren().stream()
                .map(this::renderedRow).collect(Collectors.toSet());
        assertEquals(3, generated.reportContents.getChildren().size());
        assertEquals(new java.util.HashSet<>(Arrays.asList(
                "First|Female|Adult|Rural|11", "First|Female|Youth||22",
                "Second|Female|Adult|Urban|33")), rows);
    }

    @Test
    void aggregatesUnselectedLevelsWithoutInventingCombinations() {
        GroupColumn root = root();
        CellColumn parents = textColumn(root, "parent", Arrays.asList(
                option(parent, 1, 100, 200, 300, "Female"),
                option(parent, 1, 100, 201, 301, "Female")));
        CellColumn actuals = amountColumn(root, "actual", Arrays.asList(
                amount(1, 100, 200, 300, 11), amount(1, 100, 201, 301, 22)));

        NiColumnReportData result = output(root);
        NiRowId row = new NiRowId(ACTIVITY).withCoordinates(Arrays.asList(1L, 100L));
        assertEquals(1, result.orderedIds.size());
        assertEquals("Female", result.contents.get(parents).get(row).getDisplayedValue());
        assertEquals("33", result.contents.get(actuals).get(row).getDisplayedValue());
    }

    @Test
    void retainsBasicValuesAlongsideDisaggregatedValues() {
        GroupColumn root = root();
        CellColumn names = textColumn(root, "indicator", Collections.singletonList(name(1, "First")));
        CellColumn parents = textColumn(root, "parent",
                Collections.singletonList(option(parent, 1, 100, 200, 300, "Female")));
        CellColumn actuals = amountColumn(root, "actual", Arrays.asList(
                amount(1, 100, 200, 300, 11), amount(1, MISSING, MISSING, MISSING, 7)));

        NiColumnReportData result = output(root);
        NiRowId basic = new NiRowId(ACTIVITY).withCoordinates(Arrays.asList(1L, MISSING));
        assertEquals(2, result.orderedIds.size());
        assertEquals("First", result.contents.get(names).get(basic).getDisplayedValue());
        assertNull(result.contents.get(parents).get(basic));
        assertEquals("7", result.contents.get(actuals).get(basic).getDisplayedValue());
        assertEquals("18", result.trailCells.get(actuals).getDisplayedValue());
    }

    @Test
    void leavesReportsWithoutDisaggregationColumnsUnchanged() {
        GroupColumn root = root();
        CellColumn names = textColumn(root, "indicator", Arrays.asList(name(1, "First"), name(2, "Second")));
        CellColumn actuals = amountColumn(root, "actual", Arrays.asList(
                amount(1, 100, 200, 300, 11), amount(2, 101, 201, 301, 22)));

        NiColumnReportData result = output(root);
        NiRowId row = new NiRowId(ACTIVITY);
        assertEquals(1, result.orderedIds.size());
        assertEquals("First, Second", result.contents.get(names).get(row).getDisplayedValue());
        assertEquals("33", result.contents.get(actuals).get(row).getDisplayedValue());
    }

    @Test
    void filtersDisaggregationLabelsWhenAnExplicitHierarchyIsUsed() {
        IndicatorDisaggregationTextualTokenBehaviour behaviour =
                IndicatorDisaggregationTextualTokenBehaviour.forText(indicator.dimensionUsage, false);
        TextCell female = (TextCell) option(parent, 1, 100, 200, 300, "Female").getCell();
        TextCell male = (TextCell) option(parent, 1, 101, 200, 300, "Male").getCell();
        assertSame(female, behaviour.filterCell(Collections.singletonMap(parent.dimensionUsage,
                coordinate -> coordinate.id == 100), female, female, false));
        assertNull(behaviour.filterCell(Collections.singletonMap(parent.dimensionUsage,
                coordinate -> coordinate.id == 100), male, female, false));
    }

    private void assertRow(NiColumnReportData result, CellColumn name, CellColumn level0, CellColumn level1,
                           CellColumn level2, CellColumn value, long indicatorId, long parentId, long childId,
                           long thirdId, String expectedName, String expected0, String expected1,
                           String expected2, String expectedValue) {
        NiRowId row = new NiRowId(ACTIVITY).withCoordinates(Arrays.asList(indicatorId, parentId, childId, thirdId));
        assertEquals(expectedName, result.contents.get(name).get(row).getDisplayedValue());
        assertEquals(expected0, result.contents.get(level0).get(row).getDisplayedValue());
        assertEquals(expected1, result.contents.get(level1).get(row).getDisplayedValue());
        assertEquals(expected2, result.contents.get(level2).get(row).getDisplayedValue());
        assertEquals(expectedValue, result.contents.get(value).get(row).getDisplayedValue());
    }

    private String renderedRow(ReportArea row) {
        Map<String, String> values = new HashMap<>();
        row.getContents().forEach((column, cell) -> values.put(column.originalColumnName, cell.displayedValue));
        return String.join("|", values.get("indicator"), values.get("parent"), values.get("child"),
                values.get("third"), values.get("actual"));
    }

    private NiColumnReportData output(GroupColumn root) {
        NiHeaderInfo headers = new NiHeaderInfo(engine, root, 0);
        Map<CellColumn, ColumnContents> contents = new HashMap<>();
        headers.leafColumns.forEach(column -> contents.put(column, column.contents));
        ColumnReportData data = new ColumnReportData(engine, null, contents);
        return (NiColumnReportData) new IndicatorReportDataOutputter(headers, engine, indicator.dimensionUsage,
                Arrays.asList(parent, child, third), Arrays.asList("parent", "child", "third")).visitLeaf(data);
    }

    private GroupColumn root() {
        return new GroupColumn(NiReportsEngine.ROOT_COLUMN_NAME, new LocalizableLabel("root"), null, null, null);
    }

    private CellColumn textColumn(GroupColumn root, String name, List<NiCell> cells) {
        Behaviour<?> behaviour = Arrays.asList("parent", "child", "third").contains(name)
                ? IndicatorDisaggregationTextualTokenBehaviour.forText(indicator.dimensionUsage, false)
                : TextualTokenBehaviour.instance;
        CellColumn column = new CellColumn(name, new LocalizableLabel(name), new ColumnContents(cells),
                root, null, behaviour, null);
        root.addColumn(column);
        return column;
    }

    private CellColumn amountColumn(GroupColumn root, String name, List<NiCell> cells) {
        CellColumn column = new CellColumn(name, new LocalizableLabel(name), new ColumnContents(cells),
                root, null, IndicatorMeasureBehaviour.getInstance(), null);
        root.addColumn(column);
        return column;
    }

    private LevelColumn level(String name) {
        return dimension.getDimensionUsage(name).getLevelColumn(0);
    }

    private Map<NiDimensionUsage, Coordinate> coordinates(long indicatorId, long parentId, long childId, long thirdId) {
        Map<NiDimensionUsage, Coordinate> result = new HashMap<>();
        result.put(indicator.dimensionUsage, indicator.getCoordinate(indicatorId));
        result.put(parent.dimensionUsage, parent.getCoordinate(parentId));
        result.put(child.dimensionUsage, child.getCoordinate(childId));
        result.put(third.dimensionUsage, third.getCoordinate(thirdId));
        return result;
    }

    private NiCell option(LevelColumn level, long indicatorId, long parentId, long childId, long thirdId, String text) {
        Map<NiDimensionUsage, Coordinate> coordinates = coordinates(indicatorId, parentId, childId, thirdId);
        long id = coordinates.get(level.dimensionUsage).id;
        return wrap(new TextCell(text, ACTIVITY, id, coordinates, Optional.of(level)));
    }

    private NiCell name(long id, String text) {
        return wrap(new TextCell(text, ACTIVITY, id, Optional.of(indicator)));
    }

    private NiCell amount(long indicatorId, long parentId, long childId, long thirdId, int value) {
        return wrap(new CategAmountCell(ACTIVITY,
                new MonetaryAmount(BigDecimal.valueOf(value), NiPrecisionSetting.IDENTITY_PRECISION_SETTING),
                MetaInfoSet.empty(), coordinates(indicatorId, parentId, childId, thirdId), null));
    }

    private NiCell disaggregationActual(long indicatorId, long parentId, long childId, long thirdId, int value) {
        MetaInfoSet meta = new MetaInfoSet(new MetaInfoGenerator());
        meta.add(MetaCategory.INDICATOR_VALUE_TYPE.category, (long) AmpIndicatorValue.ACTUAL);
        meta.add(MetaCategory.CATEGORY_VALUE_ID.category, parentId);
        CategAmountCell cell = new CategAmountCell(ACTIVITY,
                new MonetaryAmount(BigDecimal.valueOf(value), NiPrecisionSetting.IDENTITY_PRECISION_SETTING),
                meta, coordinates(indicatorId, parentId, childId, thirdId), null);
        AmpIndicatorMeasure measure = new AmpIndicatorMeasure("disaggregation actual", AmpIndicatorValue.ACTUAL,
                true, null);
        CategAmountCell selected = measure.processCell(cell);
        assertNotNull(selected);
        return wrap(selected);
    }

    private NiCell wrap(org.dgfoundation.amp.nireports.Cell cell) {
        return new NiCell(cell, null, HierarchiesTracker.buildEmpty(new CacheHitsCounter()));
    }
}
