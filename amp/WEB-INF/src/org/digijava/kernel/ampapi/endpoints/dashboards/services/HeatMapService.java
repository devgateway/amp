/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.dashboards.services;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.newreports.AmountCell;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.newreports.SortingInfo;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.reports.ReportsUtil;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils;
import org.digijava.kernel.ampapi.endpoints.util.DashboardConstants;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.helper.FormatHelper;


/**
 * HeatMap Dashboard Service
 * 
 * @author Nadejda Mandrescu
 */
public class HeatMapService {
    
    private static final Logger LOGGER = Logger.getLogger(HeatMapService.class);
    
    private static final int DEFAULT_X_COUNT = 25;
    private static final int DEFAULT_Y_COUNT = 10;
    private static final BigDecimal HUNDRED = new BigDecimal(100);
    private static final MathContext MCTX = new MathContext(2, RoundingMode.HALF_EVEN);
    
    private JsonBean config;
    private String xCol;
    private String yCol;
    private Integer xCount;
    private Integer yCount;
    
    private DecimalFormat decimalFormatter;
    private String othersTrn;
    
    private ReportSpecification spec;
    private GeneratedReport report;
    private ReportOutputColumn xOutCol;
    private ReportOutputColumn yOutCol;
    private ReportOutputColumn mOutCol;
    
    public HeatMapService(JsonBean config) {
        this.config = config;
    }
    
    public JsonBean buildHeatMap() {
        this.spec = getCustomReportRequest();
        this.report = EndpointUtils.runReport(this.spec);
        
        JsonBean result = new JsonBean();
        result.set("summary", getSummary());
        if (hasData(report)) {
            // init ROCs 
            getXYROC();
            
            // transform data
            transform(result);
        }
        
        return result;
    }
    
    private void transform(JsonBean result) {
        // total as funding amounts
        List<String> yTotalAmounts = new ArrayList<>();
        List<String> xTotalAmounts = new ArrayList<>();
        // storage for funding, then % amounts for matrix, xTotals and yTotals 
        Map<String, Map<String, ReportCell>> data = new LinkedHashMap<>();
        Map<String, BigDecimal> xTotal = new HashMap<>();
        Map<String, BigDecimal> yTotal = new LinkedHashMap<>();
        
        // distribute fundings per sets
        prepareXYResults(yTotalAmounts, yTotal, xTotal, data);
        
        // sort X axis descending (highest first)
        int xTotalCount = xTotal.size();
        int maxSize = xCount == null ? xTotal.size() : xCount;
        xTotal = getTopEntries(xTotal, maxSize, data);
        
        // build matrix and update amounts to %
        JsonBean[][] matrix = calculateMatrixAndPercentages(xTotalAmounts, xTotal, yTotal, data);
        
        // Counts
        result.set("yCount", yTotal.size());
        result.set("yTotalCount", report.reportContents.getChildren().size());
        result.set("xCount", xTotal.size());
        result.set("xTotalCount", xTotalCount);
        // X * Y dataset
        result.set("yDataSet", data.keySet());
        result.set("xDataSet", xTotal.keySet());
        // Amount and Percentage totals for each X, Y dataset
        result.set("yTotals", yTotalAmounts);
        result.set("yPTotals", yTotal.values());
        result.set("xPTotals", xTotal.values());
        result.set("xTotals", xTotalAmounts);
        // matrix with % and display value
        result.set("matrix", matrix);
    }
    
    private void prepareXYResults(List<String> yTotalAmounts, Map<String, BigDecimal> yTotal, 
            Map<String, BigDecimal> xTotal, Map<String, Map<String, ReportCell>> data) {
        int yCountLeft = yCount == null ? report.reportContents.getChildren().size() : yCount;
        boolean buildOthers = yCountLeft < report.reportContents.getChildren().size(); 
        
        // process all to get consistent X & Y totals
        for (ReportArea yArea : report.reportContents.getChildren()) {
            // build Y axis and get row data
            Map<String, ReportCell> row = buildYRow(yArea, yCountLeft--, yTotal, yTotalAmounts, data);
            
            for (ReportArea xArea : yArea.getChildren()) {
                // configure X * Y intersection
                String xValue = xArea.getContents().get(xOutCol).displayedValue;
                ReportCell amountCell = xArea.getContents().get(mOutCol);
                BigDecimal amount = amountCell == null ? BigDecimal.ZERO : (BigDecimal) amountCell.value;
                row.put(xValue, amountCell);
                
                // calculate X totals
                amount = amount.add(xTotal.getOrDefault(xValue, BigDecimal.ZERO));
                xTotal.put(xValue, amount);
            }
        }
        
        if (buildOthers) {
            // only now add formatted Y "Others" summed up amount
            yTotalAmounts.add(decimalFormatter.format(yTotal.get(getOthersTrn())));
                        
        }
    }
    
    private Map<String, ReportCell> buildYRow(ReportArea yArea, int yCountLeft, Map<String, BigDecimal> yTotal, 
            List<String> yTotalAmounts, Map<String, Map<String, ReportCell>> data) {
        // build Y axis, as much as needed
        boolean buildY = yCountLeft > 0;
        // if no more Y to build, then collect everything remaining under "Others"
        String yValue = buildY ? yValue = yArea.getContents().get(yOutCol).displayedValue : getOthersTrn();

        ReportCell yTotCell = yArea.getContents().get(mOutCol);
        BigDecimal yTotalAmount = yTotCell == null ? BigDecimal.ZERO : (BigDecimal) yTotCell.value;
        
        if (buildY) {
            // for "Others" will be reformatted once its total is calculated
            yTotalAmounts.add(yTotCell.displayedValue);
        } else {
            // remaining is summed up under "Others"
            yTotalAmount = yTotalAmount.add(yTotal.getOrDefault(yValue, BigDecimal.ZERO));
        }
        yTotal.put(yValue, yTotalAmount);
        
        Map<String, ReportCell> row = data.putIfAbsent(yValue, new HashMap<String, ReportCell>());
        if (row == null) {
            row = data.get(yValue);
        }
        return row;
    }
    
    private JsonBean[][] calculateMatrixAndPercentages(List<String> xTotalAmounts, Map<String, BigDecimal> xTotal,
            Map<String, BigDecimal> yTotal, Map<String, Map<String, ReportCell>> data) {
        BigDecimal grandTotal = (BigDecimal) report.reportContents.getContents().get(mOutCol).value;
        JsonBean[][] matrix = new JsonBean[data.size()][xTotal.size()];
        
        int y = 0;
        for (Entry<String, Map<String, ReportCell>> entry : data.entrySet()) {
            int x = 0;
            Map<String, ReportCell> currentRow = entry.getValue();
            boolean isEmpty = true;
            for (Entry<String, BigDecimal> xTotalEntry : xTotal.entrySet()) {
                ReportCell amountCell = currentRow.get(xTotalEntry.getKey());
                if (amountCell != null && amountCell.value != null) {
                    isEmpty = false;
                    matrix[y][x] = new JsonBean();
                    matrix[y][x].set("dv", amountCell.displayedValue);
                    
                    BigDecimal percentage = (BigDecimal) amountCell.value;
                    percentage = percentage.multiply(HUNDRED).divide(xTotalEntry.getValue(), MCTX);
                    percentage = percentage.setScale(2, RoundingMode.HALF_EVEN);
                    matrix[y][x].set("p", percentage);
                }
                x++;
            }
            // don't set if full empty row
            if (isEmpty)
                matrix[y] = null;
            // now replace with % instead of fundings
            BigDecimal percentage = yTotal.get(entry.getKey());
            if (percentage != null) {
                percentage = percentage.multiply(HUNDRED).divide(grandTotal, MCTX).setScale(2, RoundingMode.HALF_EVEN);
                yTotal.put(entry.getKey(), percentage);
            }
            y++;
        }
        
        // update xTotals
        for (Entry<String, BigDecimal> xTotalEntry : xTotal.entrySet()) {
            xTotalAmounts.add(decimalFormatter.format(xTotalEntry.getValue()));
            // x % total = sum all data for X col / sum all data for X col, per current requirements
            xTotalEntry.setValue(HUNDRED);
        }
        
        return matrix;
    }
    
    private void getXYROC() {
        if (report.leafHeaders == null || report.leafHeaders.size() != 3) {
            // TODO: error reporting
        } else {
            Iterator<ReportOutputColumn> iter = report.leafHeaders.iterator();
            yOutCol = iter.next();
            xOutCol = iter.next();
            mOutCol = iter.next();
        }
    }
    
    private List<String> getSummary() {
        List<String> summary = new ArrayList<String>(spec.getColumnNames());
        summary.addAll(spec.getMeasureNames());
        return summary;
    }
    
    private boolean hasData(GeneratedReport report) {
        return !report.isEmpty && report.reportContents != null && report.reportContents.getChildren() != null
                && report.reportContents.getChildren().size() > 0;
    }
    
    /**
     * Get top entries and collect anything remaining under "Others"
     * @param map
     * @param maxSize
     * @param data
     * @return top entries and "Others"
     */
    private Map<String, BigDecimal> getTopEntries(Map<String, BigDecimal> map, int maxSize, 
            Map<String, Map<String, ReportCell>> data) {
        if (maxSize < 1)
            return Collections.emptyMap();
        // TODO: once we fix the possibility to use Lambda expressions in REST API, then we can simplify do this:
        /*
        map = map.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .limit(maxSize).collect(
                Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        */
        // until then:
        
        Map<String, BigDecimal> result = new LinkedHashMap<String, BigDecimal>();
        SortedSet<Entry<String, BigDecimal>> sortedResult = new TreeSet<Entry<String, BigDecimal>>(
                new Comparator<Entry<String, BigDecimal>>() {
            @Override
            public int compare(Entry<String, BigDecimal> o1, Entry<String, BigDecimal> o2) {
                if (o1 == null || o1.getValue() == null) {
                    if (o2 == null || o2.getValue() == null)
                        return 0;
                    return 1;
                } else {
                    return -o1.getValue().compareTo(o2 == null ? null : o2.getValue());
                }
            }
        });
        sortedResult.addAll(map.entrySet());
        BigDecimal xOthers = null;
        for (Entry<String, BigDecimal> entry : sortedResult) {
            if (maxSize-- > 0) {
                result.put(entry.getKey(), entry.getValue());
            } else {
                // anything cut off should go to "Others"
                xOthers = xOthers == null ? entry.getValue() : xOthers.add(entry.getValue());
            }
        }
        // configure "Others" data if present 
        if (xOthers != null) {
            // set "Others" xTotals
            result.put(getOthersTrn(), xOthers);
            // now merge also Y intersections for "X Others"
            Set<String> othersXSet = map.keySet();
            othersXSet.removeAll(result.keySet());
            for (Map<String, ReportCell> row : data.values()) {
                BigDecimal otherXCell = null;
                // note: again, iterating due to current Jersey and Lambda issue 
                for (String otherX : othersXSet) {
                    ReportCell amountCell = row.get(otherX);
                    if (amountCell != null && amountCell.displayedValue != null) {
                        BigDecimal otherToAdd = (BigDecimal) amountCell.value;
                        otherXCell = otherXCell == null ? otherToAdd : otherXCell.add(otherToAdd);
                    }
                }
                if (otherXCell != null)
                    row.put(getOthersTrn(), new AmountCell(otherXCell, decimalFormatter.format(otherXCell)));
            }

        }
        return result;
    }
    
    private ReportSpecification getCustomReportRequest() {
        // TODO add validation
        this.xCol = config.getString(DashboardConstants.X_COLUMN);
        this.yCol = config.getString(DashboardConstants.Y_COLUMN);
        this.xCount = EndpointUtils.getSingleValue(config, DashboardConstants.X_COUNT, DEFAULT_X_COUNT);
        if (xCount < 0) xCount = null;
        this.yCount = EndpointUtils.getSingleValue(config, DashboardConstants.Y_COUNT, DEFAULT_Y_COUNT);
        if (yCount < 0) yCount = null;
        
        String rName = String.format("HeatMap by %s and %s (xCount = %d, yCount = %d)", xCol, yCol, xCount, yCount);
        LOGGER.info(String.format("Generating Chart '%s'", rName));
        
        ReportSpecificationImpl spec = new ReportSpecificationImpl(rName, ArConstants.DONOR_TYPE);
        ReportColumn yRepCol = new ReportColumn(yCol); 
        spec.addColumn(yRepCol);
        spec.addColumn(new ReportColumn(xCol));
        spec.getHierarchies().addAll(spec.getColumns());
        
        // also configures Measures - consistent with other Dashboards
        SettingsUtils.applyExtendedSettings(spec, config);
        ReportsUtil.configureFilters(spec, config);
        
        // sort ascending by Y axis (aka Donor Group)
        spec.addSorter(new SortingInfo(yRepCol, true));
        
        this.decimalFormatter = (spec.getSettings() != null && spec.getSettings().getCurrencyFormat() != null) ? 
                spec.getSettings().getCurrencyFormat() : FormatHelper.getDefaultFormat();
        
        return spec;
    }
    
    private String getOthersTrn() {
        if (this.othersTrn == null)
            this.othersTrn = TranslatorWorker.translateText(DashboardConstants.OTHERS);
        return this.othersTrn;
    }
    
}
