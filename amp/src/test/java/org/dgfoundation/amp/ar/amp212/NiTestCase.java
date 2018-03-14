package org.dgfoundation.amp.ar.amp212;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.MonetaryAmount;
import org.dgfoundation.amp.nireports.NiUtils;
import org.dgfoundation.amp.nireports.PercentageTextCell;
import org.dgfoundation.amp.nireports.TextCell;
import org.dgfoundation.amp.nireports.TranslatedDate;
import org.dgfoundation.amp.nireports.amp.AmpPrecisionSetting;
import org.dgfoundation.amp.nireports.amp.DirectedMeasureBehaviour;
import org.dgfoundation.amp.nireports.amp.MetaCategory;
import org.dgfoundation.amp.nireports.meta.MetaInfoGenerator;
import org.dgfoundation.amp.nireports.meta.MetaInfoSet;
import org.dgfoundation.amp.nireports.output.nicells.NiSplitCell;
import org.dgfoundation.amp.nireports.runtime.CacheHitsCounter;
import org.dgfoundation.amp.nireports.runtime.CellColumn;
import org.dgfoundation.amp.nireports.runtime.ColumnContents;
import org.dgfoundation.amp.nireports.runtime.ColumnReportData;
import org.dgfoundation.amp.nireports.runtime.DebugOutputReportDataVisitor;
import org.dgfoundation.amp.nireports.runtime.HierarchiesTracker;
import org.dgfoundation.amp.nireports.runtime.NiCell;
import org.dgfoundation.amp.nireports.runtime.ReportData;
import org.dgfoundation.amp.nireports.schema.NiReportedEntity;
import org.dgfoundation.amp.nireports.schema.NiDimension.Coordinate;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;
import org.dgfoundation.amp.nireports.schema.NiReportsSchema;
import org.dgfoundation.amp.testutils.AmpTestCase;

/**
 * a testcase of the inner structures of NiReports
 * @author Dolghier Constantin
 *
 */
public abstract class NiTestCase extends AmpTestCase {
    
    final String TCN = "TotalColumnsName";
    
    final DirectedMeasureBehaviour beh = new DirectedMeasureBehaviour(TCN);
    final MetaInfoGenerator metaInfoGenerator = new MetaInfoGenerator();
    final CacheHitsCounter chc = new CacheHitsCounter();
    final HierarchiesTracker EMPTY_PERCS = HierarchiesTracker.buildEmpty(chc);
    
    final NiReportsSchema schema;
    
    protected NiTestCase(NiReportsSchema schema) {
        this.schema = schema;
    }
    
    protected Cell buildDirectedCell(int amount, String flow) {
        MetaInfoSet metaInfo = new MetaInfoSet(metaInfoGenerator);
        if (flow != null)
            metaInfo.add(MetaCategory.DIRECTED_TRANSACTION_FLOW.category, flow);
        CategAmountCell cell = new CategAmountCell(1l, new MonetaryAmount(BigDecimal.valueOf(amount), new AmpPrecisionSetting()), metaInfo, Collections.emptyMap(), new TranslatedDate(2000, "2000", 1, 1, "January"));
        return cell;
    }
    
    protected Cell buildCell(int amount, MetaInfoSet metaInfo, Map<NiDimensionUsage, Coordinate> coos) {
        CategAmountCell cell = new CategAmountCell(1, new MonetaryAmount(BigDecimal.valueOf(amount), new AmpPrecisionSetting()), metaInfo, coos, new TranslatedDate(2000, "2000", 1, 1, "January"));
        return cell;
    }
    
    protected Cell textCell(String text, long entityId, LevelColumn levelColumn) {
        TextCell cell = new TextCell(text, 10l, entityId, Optional.ofNullable(levelColumn));
        return cell;
    }

    protected Cell textCell(String text, long activityId, long entityId) {
        TextCell cell = new TextCell(text, activityId, entityId, Optional.ofNullable(null));
        return cell;
    }

    protected Cell percentageTextCell(String text, long activityId, long entityId, double percentage, LevelColumn levelColumn) {
        PercentageTextCell cell = new PercentageTextCell(text, activityId, entityId, Optional.ofNullable(levelColumn), BigDecimal.valueOf(percentage));
        return cell;
    }

    protected Cell percentageTextCell(String text, long entityId, double percentage, LevelColumn levelColumn) {
        return percentageTextCell(text, 10l, entityId, percentage, levelColumn);
    }

    protected MetaInfoSet buildMetaInfo(Object...z) {
        NiUtils.failIf(z.length % 2 != 0, "please supply an even number of arguments");
        MetaInfoSet metaInfo = new MetaInfoSet(metaInfoGenerator);
        for(int i = 0; i < z.length / 2; i++) {
            MetaCategory mc = (MetaCategory) z[2 * i];
            Object val = z[2 * i + 1];
            NiUtils.failIf(metaInfo.hasMetaInfo(mc.category), () -> String.format("you tried to add cat twice: %s", mc.category));
            metaInfo.add(mc.category, val);
        }
        return metaInfo;
    }
    
    protected Map<NiDimensionUsage, Coordinate> buildCoos(Object...z) {
        NiUtils.failIf(z.length % 2 != 0, "please supply an even number of arguments");
        Map<NiDimensionUsage, Coordinate> res = new HashMap<>();
        for(int i = 0; i < z.length / 2; i++) {
            NiDimensionUsage dimUsg = (NiDimensionUsage) z[2 * i];
            Coordinate coo = (Coordinate) z[2 * i + 1];
            res.put(dimUsg, coo);
        }
        return res;
    }
    
    protected NiCell buildNiCell(int amount, String flow) {
        Cell cell = buildDirectedCell(amount, flow);
        return new NiCell(cell, schema.getMeasures().get(MeasureConstants.ACTUAL_COMMITMENTS), HierarchiesTracker.buildEmpty(chc));
    }
    
    protected NiCell buildMeasureNiCell(Cell cell, String measureName) {
        return new NiCell(cell, schema.getMeasures().get(measureName), HierarchiesTracker.buildEmpty(chc));
    }

    protected NiCell buildColumnNiCell(Cell cell, String columnName) {
        return new NiCell(cell, schema.getColumns().get(columnName), HierarchiesTracker.buildEmpty(chc));
    }
    
    protected Map<String, CellColumn> cachedColumns = Collections.synchronizedMap(new HashMap<>());
    
    protected synchronized CellColumn getCellColumn(String name) {
        NiReportedEntity<?> entity = schema.getColumns().containsKey(name) ? schema.getColumns().get(name) : schema.getMeasures().get(name);
        NiUtils.failIf(entity == null, () -> String.format("unknown entity: %s", name));
        return cachedColumns.computeIfAbsent(name, z -> 
            new CellColumn(z, entity.label, new ColumnContents(Collections.emptyList()), null, entity, null));
    }
    
    /** accepts a list as input (String columnName, List<Cell> contents) */
    protected Map<CellColumn, ColumnContents> buildColumnContents(Object...cc) {
        Map<CellColumn, ColumnContents> res = new HashMap<>();
        for(int i = 0; i < cc.length / 2; i++) {
            String columnName = (String) cc[2 * i];
            List<Cell> contents = (List<Cell>) cc[2 * i + 1];
            CellColumn cellColumn = getCellColumn(columnName);
            res.put(cellColumn, new ColumnContents(contents.stream().map(c -> new NiCell(c, cellColumn.entity, EMPTY_PERCS)).collect(Collectors.toList())));
        }
        return res;
    }
    
    protected NiSplitCell splitCell(String splitterColumn, String splitterText, long splitterId) {
        return new NiSplitCell(schema.getColumns().get(splitterColumn), splitterText, new HashSet<>(Arrays.asList(splitterId)), splitterId < 0);
    }
    
    protected ColumnReportData buildColumnReportData(String splitterColumn, String splitterText, long splitterId, Object...data) {
        ColumnReportData res = new ColumnReportData(null, splitCell(splitterColumn, splitterText, splitterId), buildColumnContents(data));
        return res;
    }
    
    protected String asDebugString(ReportData in) {
        return in.accept(new DebugOutputReportDataVisitor());
    }
    
}
