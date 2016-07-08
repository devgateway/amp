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
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.newreports.SortingInfo;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.reports.ReportsUtil;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils;
import org.digijava.kernel.ampapi.endpoints.util.DashboardConstants;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
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
        Map<String, BigDecimal> yTotal = new HashMap<>();
        
        // distribute fundings per sets
        prepareXYResults(yTotalAmounts, yTotal, xTotal, data);
        
        // sort X axis descending (highest first)
        int maxSize = xCount == null ? xTotal.size() : xCount;
        xTotal = getTopEntries(xTotal, maxSize);
        
        // build matrix and update amounts to %
        JsonBean[][] matrix = calculateMatrixAndPercentages(xTotalAmounts, xTotal, yTotal, data);
        
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
        
        // process all to get consistent X & Y totals
        for (ReportArea yArea : report.reportContents.getChildren()) {
            Map<String, ReportCell> row = null;
            // build Y axis, as much as needed
            if (yCountLeft-- > 0) {
                String yValue = yArea.getContents().get(yOutCol).displayedValue;
                ReportCell yTotCell = yArea.getContents().get(mOutCol);
                yTotalAmounts.add(yTotCell.displayedValue);
                BigDecimal yTotalAmount = yTotCell == null ? BigDecimal.ZERO : (BigDecimal) yTotCell.value; 
                yTotal.put(yValue, yTotalAmount);
                // build row data
                row = data.putIfAbsent(yValue, new HashMap<String, ReportCell>());
                if (row == null)
                    row = data.get(yValue);
            }
            
            for (ReportArea xArea : yArea.getChildren()) {
                // configure X * Y intersection
                String xValue = xArea.getContents().get(xOutCol).displayedValue;
                ReportCell amountCell = xArea.getContents().get(mOutCol);
                BigDecimal amount = amountCell == null ? BigDecimal.ZERO : (BigDecimal) amountCell.value;
                if (row != null)
                    row.put(xValue, amountCell);
                
                // calculate X totals
                amount = amount.add(xTotal.getOrDefault(xValue, BigDecimal.ZERO));
                xTotal.put(xValue, amount);
            }
        }
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
    
    private Map<String, BigDecimal> getTopEntries(Map<String, BigDecimal> map, int maxSize) {
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
        for (Entry<String, BigDecimal> entry : sortedResult) {
            result.put(entry.getKey(), entry.getValue());
            if (--maxSize == 0) break; 
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
        spec.getHierarchies().add(yRepCol);
        
        // also configures Measures - consistent with other Dashboards
        SettingsUtils.applyExtendedSettings(spec, config);
        ReportsUtil.configureFilters(spec, config);
        
        // sort ascending by Y axis (aka Donor Group)
        spec.addSorter(new SortingInfo(yRepCol, true));
        
        this.decimalFormatter = (spec.getSettings() != null && spec.getSettings().getCurrencyFormat() != null) ? 
                spec.getSettings().getCurrencyFormat() : FormatHelper.getDefaultFormat();
        
        return spec;
    }

}
