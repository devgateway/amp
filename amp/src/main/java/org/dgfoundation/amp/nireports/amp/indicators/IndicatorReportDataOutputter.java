package org.dgfoundation.amp.nireports.amp.indicators;

import org.dgfoundation.amp.nireports.NiHeaderInfo;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.nireports.output.NiColumnReportData;
import org.dgfoundation.amp.nireports.output.NiReportData;
import org.dgfoundation.amp.nireports.output.NiReportDataOutputter;
import org.dgfoundation.amp.nireports.output.NiRowId;
import org.dgfoundation.amp.nireports.output.nicells.NiOutCell;
import org.dgfoundation.amp.nireports.runtime.CellColumn;
import org.dgfoundation.amp.nireports.runtime.ColumnReportData;
import org.dgfoundation.amp.nireports.runtime.NiCell;
import org.dgfoundation.amp.nireports.runtime.HierarchiesTracker.SplitCellPercentage;
import org.dgfoundation.amp.nireports.schema.NiDimension.Coordinate;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class IndicatorReportDataOutputter extends NiReportDataOutputter {
    private final NiDimensionUsage indicator;
    private final Map<String, LevelColumn> selectedLevels = new LinkedHashMap<>();

    public IndicatorReportDataOutputter(NiHeaderInfo headers, NiReportsEngine engine, NiDimensionUsage indicator,
                                        List<LevelColumn> levels, List<String> names) {
        super(headers, engine);
        this.indicator = indicator;
        Set<String> selected = headers.leafColumns.stream().map(column -> column.name).collect(Collectors.toSet());
        for (int i = 0; i < names.size(); i++) {
            if (selected.contains(names.get(i))) {
                selectedLevels.put(names.get(i), levels.get(i));
            }
        }
    }

    @Override
    public NiReportData visitLeaf(ColumnReportData report) {
        if (selectedLevels.isEmpty()) {
            return super.visitLeaf(report);
        }

        List<NiDimensionUsage> dimensions = new ArrayList<>();
        dimensions.add(indicator);
        selectedLevels.values().forEach(level -> dimensions.add(level.dimensionUsage));
        Map<Long, Set<List<Long>>> rows = new HashMap<>();
        report.getContents().forEach((column, contents) -> {
            contents.data.forEach((activityId, cells) -> {
                for (NiCell cell : cells) {
                    if (selectedLevels.containsKey(column.name) || cell.getCell() instanceof CategAmountCell) {
                        Map<NiDimensionUsage, Coordinate> coordinates = cell.getCell().getCoordinates();
                        if (coordinates.containsKey(indicator)) {
                            List<Long> key = dimensions.stream()
                                    .map(dimension -> coordinates.containsKey(dimension)
                                            ? coordinates.get(dimension).id : ColumnReportData.UNALLOCATED_ID)
                                    .collect(Collectors.toList());
                            rows.computeIfAbsent(activityId, ignored -> new LinkedHashSet<>()).add(key);
                        }
                    }
                }
            });
        });
        if (IndicatorDisaggregationFilter.isActive(engine)) {
            addFilteredCombinationRows(report, dimensions, rows);
        }

        Map<CellColumn, Map<NiRowId, NiOutCell>> output = new HashMap<>();
        report.getContents().forEach((column, contents) -> {
            Map<NiRowId, NiOutCell> values = new HashMap<>();
            contents.data.forEach((activityId, cells) -> {
                Set<List<Long>> keys = rows.get(activityId);
                if (keys == null || keys.isEmpty()) {
                    values.put(new NiRowId(activityId), column.getBehaviour().horizontalReduce(cells, engine));
                } else {
                    for (List<Long> key : keys) {
                        List<NiCell> matching = cells.stream()
                                .filter(cell -> matches(cell, dimensions, key,
                                        selectedLevels.containsKey(column.name)))
                                .collect(Collectors.toList());
                        if (!matching.isEmpty()) {
                            values.put(new NiRowId(activityId).withCoordinates(key),
                                    column.getBehaviour().horizontalReduce(matching, engine));
                        }

                    }
                }
            });
            output.put(column, values);
        });
        return new NiColumnReportData(output, buildTrailCells(report, output), report.splitter);
    }

    private void addFilteredCombinationRows(ColumnReportData report, List<NiDimensionUsage> dimensions,
                                            Map<Long, Set<List<Long>>> rows) {
        engine.fetchedColumns.get(ColumnConstants.INDICATOR_DISAGGREGATION_LEVEL_0).data
                .forEach((activityId, combinations) -> {
                    List<NiCell> leafCells = report.getContents().values().stream()
                            .flatMap(contents -> contents.data.getOrDefault(activityId, java.util.Collections.emptyList())
                                    .stream())
                            .collect(Collectors.toList());
                    for (NiCell combination : combinations) {
                        if (leafCells.stream().noneMatch(cell -> matchesHierarchy(combination, cell))) {
                            continue;
                        }
                        Map<NiDimensionUsage, Coordinate> coordinates = combination.getCell().getCoordinates();
                        List<Long> key = dimensions.stream()
                                .map(dimension -> coordinates.containsKey(dimension)
                                        ? coordinates.get(dimension).id : ColumnReportData.UNALLOCATED_ID)
                                .collect(Collectors.toList());
                        rows.computeIfAbsent(activityId, ignored -> new LinkedHashSet<>()).add(key);
                    }
                });
    }

    private boolean matchesHierarchy(NiCell combination, NiCell leafCell) {
        if (leafCell.getHiersTracker() == null) {
            return true;
        }
        Map<NiDimensionUsage, Coordinate> coordinates = combination.getCell().getCoordinates();
        for (Map.Entry<NiDimensionUsage, SplitCellPercentage> entry :
                leafCell.getHiersTracker().getPercentages().entrySet()) {
            if (!entry.getKey().equals(indicator)
                    && selectedLevels.values().stream().noneMatch(level -> level.dimensionUsage.equals(entry.getKey()))) {
                continue;
            }
            Coordinate coordinate = coordinates.get(entry.getKey());
            long id = coordinate == null ? ColumnReportData.UNALLOCATED_ID : coordinate.id;
            if (id != entry.getValue().id) {
                return false;
            }
        }
        return true;
    }

    private boolean matches(NiCell cell, List<NiDimensionUsage> dimensions, List<Long> key,
                            boolean disaggregationColumn) {
        Map<NiDimensionUsage, Coordinate> coordinates = cell.getCell().getCoordinates();
        for (int i = 0; i < dimensions.size(); i++) {
            Coordinate coordinate = coordinates.get(dimensions.get(i));
            if (coordinate != null && coordinate.id != key.get(i)) {
                return false;
            }
            if (i > 0 && coordinate == null
                    && (disaggregationColumn || cell.getCell() instanceof CategAmountCell)
                    && key.get(i) != ColumnReportData.UNALLOCATED_ID) {
                return false;
            }
        }
        return true;
    }
}
