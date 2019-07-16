/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.dashboards.services;

import java.math.BigDecimal;
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
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.newreports.AmountCell;
import org.dgfoundation.amp.newreports.AmpReportFilters;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.newreports.SortingInfo;
import org.dgfoundation.amp.visibility.data.ColumnsVisibility;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.dashboards.DashboardErrors;
import org.digijava.kernel.ampapi.endpoints.errors.ApiEMGroup;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.reports.ReportsUtil;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils;
import org.digijava.kernel.ampapi.endpoints.util.DashboardConstants;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.translator.TranslatorWorker;


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
    private static final BigDecimal ZERO = new BigDecimal(0);
    public static final int SCALE = 6;

    private JsonBean config;
    private Long xId;
    private Long yId;
    private String xCol;
    private String yCol;
    private Integer xCount;
    private Integer yCount;
    
    private DecimalFormat decimalFormatter;
    private String othersTrn;
    private ApiEMGroup errors = new ApiEMGroup();
    private Set<String> visibleColumns; 
    
    private ReportSpecification spec;
    private GeneratedReport report;
    private ReportOutputColumn xOutCol;
    private ReportOutputColumn yOutCol;
    private ReportOutputColumn mOutCol;
    
    public HeatMapService(JsonBean config, Long xId, Long yId) {
        this.config = config;
        this.visibleColumns = ColumnsVisibility.getVisibleColumns();
        this.xId = xId;
        this.yId = yId;
    }

    public HeatMapService(JsonBean config) {
        this.config = config;
        this.visibleColumns = ColumnsVisibility.getVisibleColumns();
    }
    
    public JsonBean buildHeatMap() {
        this.spec = getCustomReportRequest();
        if (spec != null && errors.isEmpty()) {
            this.report = EndpointUtils.runReport(this.spec);
        }
        
        JsonBean result = new JsonBean();
        if (!errors.isEmpty()) {
            result = ApiError.toError(errors.getAllErrors());
        } else if (hasData(report)) {
            result.set("summary", getSummary());
            // init ROCs 
            getXYROC();
            
            // transform data
            transform(result);
        }
        
        return result;
    }

    /**
     * Set filters, settings and columns to the report specification and return a projects list
     * "Others"
     * @return a json with a list of projects.
     */
    public JsonBean buildHeatMapDetail() {
        this.spec = getCustomReportDetailRequest();
        if (spec != null && errors.isEmpty()) {
            this.report = EndpointUtils.runReport(this.spec);
        }
        return DashboardsService.buildPaginateJsonBean(report, DashboardsService.getOffset(this.config));
    }

    private void transform(JsonBean result) {
        // total as funding amounts
        List<String> yTotalAmounts = new ArrayList<>();
        List<String> xTotalAmounts = new ArrayList<>();
        // storage for funding, then % amounts for matrix, xTotals and yTotals 
        Map<String, Map<String, ReportCell>> data = new LinkedHashMap<>();
        Map<String, BigDecimal> xTotal = new HashMap<>();
        Map<String, BigDecimal> yTotal = new LinkedHashMap<>();

        Map<String, Long> yDataSetIds = new LinkedHashMap<>();
        Map<String, Long> xDataSetIds = new HashMap<>();

        // distribute fundings per sets
        prepareXYResults(yTotalAmounts, yTotal, xTotal, data, yDataSetIds, xDataSetIds);
        
        // sort X axis descending (highest first)
        int xTotalCount = xTotal.size();
        int maxSize = xCount == null ? xTotal.size() : xCount;
        xTotal = getTopEntries(xTotal, maxSize, data);
        xDataSetIds = setOrder(xTotal, xDataSetIds);

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
        // X * Y ids
        result.set("yDataSetIds", yDataSetIds.values());
        result.set("xDataSetIds", xDataSetIds.values());
        // Amount and Percentage totals for each X, Y dataset
        result.set("yTotals", yTotalAmounts);
        result.set("yPTotals", yTotal.values());
        result.set("xPTotals", xTotal.values());
        result.set("xTotals", xTotalAmounts);
        // matrix with % and display value
        result.set("matrix", matrix);
        // currency used.
        String currcode = spec.getSettings().getCurrencyCode();
        result.set("currency", currcode);
    }

    private void prepareXYResults(List<String> yTotalAmounts, Map<String, BigDecimal> yTotal,
                                  Map<String, BigDecimal> xTotal, Map<String, Map<String, ReportCell>> data,
                                  Map<String, Long> yDataSetIds, Map<String, Long> xDataSetIds) {
        // how many y full entries left to include, while remaining will go to others
        int yCountLeft = yCount == null ? report.reportContents.getChildren().size() : yCount;
        boolean buildOthers = yCountLeft < report.reportContents.getChildren().size();
        Map<String, BigDecimal> othersYRow = new HashMap<>();
        
        // process all to get consistent X & Y totals
        for (ReportArea yArea : report.reportContents.getChildren()) {
            // build Y axis and get row data
            Map<String, ReportCell> row = buildYRow(yArea, yCountLeft--, yTotal, yTotalAmounts, data);
            boolean isOthersY = yCountLeft < 0;

            if (!isOthersY) {
                yDataSetIds.put(yArea.getContents().get(yOutCol).displayedValue, yArea.getOwner().id);
            }

            for (ReportArea xArea : yArea.getChildren()) {
                // configure X * Y intersection
                String xValue = xArea.getContents().get(xOutCol).displayedValue;
                ReportCell amountCell = xArea.getContents().get(mOutCol);
                BigDecimal amount = amountCell == null ? BigDecimal.ZERO : (BigDecimal) amountCell.value;
                row.put(xValue, amountCell);
                // build "Others" Y row 
                if (isOthersY) {
                    othersYRow.put(xValue, amount.add(othersYRow.getOrDefault(xValue, BigDecimal.ZERO)));
                }
                
                // calculate X totals
                amount = amount.add(xTotal.getOrDefault(xValue, BigDecimal.ZERO));
                xTotal.put(xValue, amount);
                xDataSetIds.put(xValue, xArea.getOwner().id);
            }
        }
        
        if (buildOthers) {
            // only now add formatted Y "Others" summed up amount
            yTotalAmounts.add(decimalFormatter.format(yTotal.get(getOthersTrn())));
            // update other Y row cells formatting
            Map<String, ReportCell> otherYFormattedCells = data.get(getOthersTrn());
            otherYFormattedCells.clear();
            for (Entry<String, BigDecimal> cell : othersYRow.entrySet()) {
                BigDecimal amount = cell.getValue();
                if (!BigDecimal.ZERO.equals(amount)) {
                    otherYFormattedCells.put(cell.getKey(), new AmountCell(amount, decimalFormatter.format(amount)));
                }
            }           
        }
    }
    
    private Map<String, ReportCell> buildYRow(ReportArea yArea, int yCountLeft, Map<String, BigDecimal> yTotal, 
            List<String> yTotalAmounts, Map<String, Map<String, ReportCell>> data) {
        // build Y axis, as much as needed
        boolean buildY = yCountLeft > 0;
        // if no more Y to build, then collect everything remaining under "Others"
        String yValue = buildY ? yArea.getContents().get(yOutCol).displayedValue : getOthersTrn();
        
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
                    if (xTotalEntry.getValue().compareTo(BigDecimal.ZERO) > 0) {
                        percentage = percentage.multiply(HUNDRED).divide(xTotalEntry.getValue(), SCALE, RoundingMode
                                .HALF_EVEN);
                        percentage = percentage.setScale(SCALE, RoundingMode.HALF_EVEN);
                    } else {
                        percentage = null;
                    }

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
                percentage = percentage.multiply(HUNDRED).divide(grandTotal, 6, RoundingMode.HALF_EVEN);
                yTotal.put(entry.getKey(), percentage);
            }
            y++;
        }
        
        // update xTotals
        for (Entry<String, BigDecimal> xTotalEntry : xTotal.entrySet()) {
            xTotalAmounts.add(decimalFormatter.format(xTotalEntry.getValue()));
            // x % total = sum all data for X col / sum all data for X col, per current requirements
            if (xTotalEntry.getValue().compareTo(BigDecimal.ZERO) > 0) {
                xTotalEntry.setValue(HUNDRED);
            } else {
                xTotalEntry.setValue(ZERO);
            }
        }
        
        return matrix;
    }
    
    private void getXYROC() {
        Integer headersSize = report.leafHeaders != null ? report.leafHeaders.size() : null;  
        if (headersSize == null || headersSize != 3) {
            // abnormal issue
            throw new RuntimeException(TranslatorWorker.translateText("Invalid report structure (cannot be processed): "
                    + "leaf headers size = ") + headersSize);
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
        return report != null && !report.isEmpty && report.reportContents != null 
                && report.reportContents.getChildren() != null && report.reportContents.getChildren().size() > 0;
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

    private Map<String, Long> setOrder(Map<String, BigDecimal> map,
                                       Map<String, Long> xDataSetIds) {
        Map<String, Long> result = new LinkedHashMap<String, Long>();
        for (Map.Entry<String, BigDecimal> entry : map.entrySet()) {
            result.put(entry.getKey(),xDataSetIds.get(entry.getKey()));
        }
        return result;
    }

    private ReportSpecification getCustomReportRequest() {
        this.xCol = readXYColumn(DashboardConstants.X_COLUMN);
        this.yCol = readXYColumn(DashboardConstants.Y_COLUMN);
        this.xCount = readXYCount(DashboardConstants.X_COUNT, DEFAULT_X_COUNT);
        this.yCount = readXYCount(DashboardConstants.Y_COUNT, DEFAULT_Y_COUNT);

        String rName = String.format("HeatMap by %s and %s (xCount = %d, yCount = %d)", xCol, yCol, xCount, yCount);
        LOGGER.info(String.format("Generating Chart '%s'%s", rName, errors.isEmpty() ? "" : " - aborted due to errors"));

        // no generation if errors found
        if (!errors.isEmpty())
            return null;

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

        this.decimalFormatter = ReportsUtil.getDecimalFormatOrDefault(spec);

        return spec;
    }

    /**
     * Set filters, settings and columns to the report specification
     * "Others"
     * @return a report specification updated.
     */
    private ReportSpecification getCustomReportDetailRequest() {
        String rName = "";
        this.xCol = FilterUtils.INSTANCE.idFromColumnName(readXYColumn(DashboardConstants.X_COLUMN));
        this.yCol = FilterUtils.INSTANCE.idFromColumnName(readXYColumn(DashboardConstants.Y_COLUMN));
        ReportSpecificationImpl spec = new ReportSpecificationImpl(rName, ArConstants.DONOR_TYPE);

        spec.addColumn(new ReportColumn(ColumnConstants.PROJECT_TITLE));
        spec.addColumn(new ReportColumn(ColumnConstants.ACTIVITY_UPDATED_ON));

        Map<String, Object> filters = null;
        if (this.config != null) {
            filters = (Map<String, Object>) this.config.get(EPConstants.FILTERS);
        }
        if (filters == null) {
            filters = new LinkedHashMap<>();
        }
        filters.putAll(DashboardsService.setFilterId(xId, this.xCol));
        filters.putAll(DashboardsService.setFilterId(yId, this.yCol));
        AmpReportFilters filterRules = FilterUtils.getFilterRules(filters, null);
        if (filterRules != null) {
            spec.setFilters(filterRules);
        }
        spec.addSorter(new SortingInfo(new ReportColumn(ColumnConstants.ACTIVITY_UPDATED_ON), false));

        // also configures Measures - consistent with other Dashboards
        SettingsUtils.applyExtendedSettings(spec, config);
        ReportsUtil.configureFilters(spec, config);

        this.decimalFormatter = ReportsUtil.getDecimalFormatOrDefault(spec);

        return spec;
    }

    private String readXYColumn(String param) {
        String colName = config.getString(param);
        // only if visible, since a generic EP
        if (!visibleColumns.contains(colName)) {
            errors.addApiErrorMessage(DashboardErrors.INVALID_COLUMN, param + " = " + colName);
        }
        return colName;
    }
    
    private Integer readXYCount(String param, Integer defaultValue) {
        Object count = config.get(param);
        if (count == null) {
            count = defaultValue;
        } else if (!Integer.class.isAssignableFrom(count.getClass())) {
            errors.addApiErrorMessage(DashboardErrors.INVALID_NUMBER, param + " = " + count);
            count = null;
        } else if (((Integer) count) < 0 ) {
            count = null; // no limit
        }
        return (Integer) count;
    }
    
    private String getOthersTrn() {
        if (this.othersTrn == null)
            this.othersTrn = TranslatorWorker.translateText(DashboardConstants.OTHERS);
        return this.othersTrn;
    }
    
}
