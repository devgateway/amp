package org.dgfoundation.amp.nireports.behaviours;

import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.MonetaryAmount;
import org.dgfoundation.amp.nireports.NiPrecisionSetting;
import org.dgfoundation.amp.nireports.TextCell;
import org.dgfoundation.amp.nireports.amp.MetaCategory;
import org.dgfoundation.amp.nireports.meta.MetaInfoGenerator;
import org.dgfoundation.amp.nireports.meta.MetaInfoSet;
import org.dgfoundation.amp.nireports.runtime.CacheHitsCounter;
import org.dgfoundation.amp.nireports.runtime.ColumnContents;
import org.dgfoundation.amp.nireports.runtime.ColumnReportData;
import org.dgfoundation.amp.nireports.runtime.HierarchiesTracker;
import org.dgfoundation.amp.nireports.runtime.NiCell;
import org.dgfoundation.amp.nireports.schema.DimensionLevel;
import org.dgfoundation.amp.nireports.schema.IdsAcceptor;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.dgfoundation.amp.nireports.schema.NiDimension.Coordinate;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IndicatorDisaggregationMeasureTest {
    private static final long ACTIVITY = 10L;
    private static final long UNALLOCATED = ColumnReportData.UNALLOCATED_ID;
    private final NiDimension dimension = new NiDimension("test category", 1) {
        @Override
        protected List<DimensionLevel> fetchDimension() {
            return Collections.emptyList();
        }
    };
    private final LevelColumn parent = dimension.getDimensionUsage("parent").getLevelColumn(0);
    private final LevelColumn child = dimension.getDimensionUsage("child").getLevelColumn(0);
    private final LevelColumn third = dimension.getDimensionUsage("third").getLevelColumn(0);
    private final IndicatorMeasureBehaviour behaviour = new IndicatorMeasureBehaviour() {
        @Override
        MetaCategory resolveMetaCategory(NiDimensionUsage usage) {
            if (usage == parent.dimensionUsage) {
                return MetaCategory.CATEGORY_VALUE_ID;
            }
            if (usage == child.dimensionUsage) {
                return MetaCategory.CHILD_CATEGORY_VALUE_ID;
            }
            if (usage == third.dimensionUsage) {
                return MetaCategory.THIRD_CATEGORY_VALUE_ID;
            }
            throw new IllegalArgumentException("Unexpected dimension: " + usage);
        }
    };

    @Test
    void splitsAndAggregatesAllocatedAndLegacyValuesForTheSameActivity() {
        ColumnContents values = new ColumnContents(Arrays.asList(
                amount(10, 100, 200L, null),
                amount(20, 100, 200L, 300L),
                amount(7, 100, 200L, 301L),
                amount(5, 100, null, null)));

        assertAmount(42, values);
        ColumnContents parentValues = split(values, parent, 100);
        ColumnContents childValues = split(parentValues, child, 200);
        assertAmount(10, split(childValues, third, UNALLOCATED));
        assertAmount(20, split(childValues, third, 300));
        assertAmount(7, split(childValues, third, 301));
        ColumnContents missingChild = split(parentValues, child, UNALLOCATED);
        assertAmount(5, split(missingChild, third, UNALLOCATED));
        assertEquals(0, split(missingChild, third, 300).countCells());
    }

    private NiCell amount(int value, long parentId, Long childId, Long thirdId) {
        MetaInfoSet meta = new MetaInfoSet(new MetaInfoGenerator());
        Map<NiDimensionUsage, Coordinate> coordinates = new HashMap<>();
        addDimension(meta, coordinates, parent, MetaCategory.CATEGORY_VALUE_ID, parentId);
        addDimension(meta, coordinates, child, MetaCategory.CHILD_CATEGORY_VALUE_ID, childId);
        addDimension(meta, coordinates, third, MetaCategory.THIRD_CATEGORY_VALUE_ID, thirdId);
        CategAmountCell cell = new CategAmountCell(ACTIVITY,
                new MonetaryAmount(BigDecimal.valueOf(value), NiPrecisionSetting.IDENTITY_PRECISION_SETTING),
                meta, coordinates, null);
        return new NiCell(cell, null, HierarchiesTracker.buildEmpty(new CacheHitsCounter()));
    }

    private void addDimension(MetaInfoSet meta, Map<NiDimensionUsage, Coordinate> coordinates,
                              LevelColumn level, MetaCategory category, Long id) {
        if (id != null) {
            meta.add(category.category, id);
        }
        coordinates.put(level.dimensionUsage, level.getCoordinate(id == null ? UNALLOCATED : id));
    }

    private ColumnContents split(ColumnContents contents, LevelColumn level, long id) {
        Cell splitter = new TextCell("category", ACTIVITY, id, Optional.of(level));
        IdsAcceptor acceptsId = coordinate -> coordinate.id == id;
        return behaviour.horizSplit(contents, Collections.singletonMap(ACTIVITY, splitter),
                Collections.singleton(ACTIVITY), Collections.singletonMap(level.dimensionUsage, acceptsId),
                false, true);
    }

    private void assertAmount(int expected, ColumnContents contents) {
        assertEquals(0, BigDecimal.valueOf(expected)
                .compareTo(behaviour.horizontalReduce(contents.getLinearData(), null).getAmount()));
    }
}
