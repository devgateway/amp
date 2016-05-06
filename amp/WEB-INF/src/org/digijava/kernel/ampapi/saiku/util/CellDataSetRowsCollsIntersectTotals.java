/**
 * 
 */
package org.digijava.kernel.ampapi.saiku.util;

import java.util.List;

import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.reports.CustomAmounts;
import org.saiku.olap.dto.resultset.CellDataSet;
import org.saiku.service.olap.totals.aggregators.TotalAggregator;

import clover.org.apache.commons.lang.StringUtils;

/**
 * Manual recalculation for computed measures where automatic detection is not possible,
 * e.g. when having no control over how Saiku CellDataSet totals are calculated and we also have to merge 
 * non-hierachical columns.
 * 
 * @author Nadejda Mandrescu
 */
public class CellDataSetRowsCollsIntersectTotals {
    
    private ReportSpecification spec;
    private CellDataSet cellDataSet;
    private List<ReportOutputColumn> leafHeaders;
    private List<Integer> activities;
    
    private TotalAggregator[][] totals;
    private int colId;
    private int measureColId;
    private int startRowId;
    private int endRowId;
    
    public CellDataSetRowsCollsIntersectTotals(ReportSpecification spec, CellDataSet cellDataSet, 
            List<ReportOutputColumn> leafHeaders, List<Integer> activities) {
        this.spec = spec;
        this.cellDataSet = cellDataSet;
        this.leafHeaders = leafHeaders;
        this.activities = activities;
    }
    
    public Double getActualMeasureIntersectTotal(TotalAggregator[][] totals, int colId, int startRowId, int endRowId) {
        this.measureColId = colId - cellDataSet.getLeftOffset();
        Double totalAmount = totals[0][measureColId].getValue();
        // skip if not merging
        if (startRowId < endRowId && measureColId < cellDataSet.getSelectedMeasures().length) {
            this.totals = totals;
            this.colId = colId;
            this.startRowId = startRowId;
            this.endRowId = endRowId;
            
            String measureName = cellDataSet.getSelectedMeasures()[measureColId].getName();
            List<String> structure = CustomAmounts.PERCENTAGE_AMOUNTS_NUMERATOR_DENOMINATOR.get(measureName);
            if (structure != null && structure.size() == 2) {
                return getPercentageMeasureIntersectTotal(measureName, structure, totalAmount);
            }
        }
        return totalAmount;
    }
    
    private Double getPercentageMeasureIntersectTotal(String measureName, List<String> structure, Double totalAmount) {
        Double numerator = getValue(structure.get(0));
        Double denomiator = getValue(structure.get(1));
        if (numerator == null || denomiator == null || denomiator == 0d ) {
            totalAmount = 0d;
        } else {
            totalAmount = numerator / denomiator * 100;
        }
        return totalAmount;
    }
    
    private Double getValue(String origColumnName) {
        Double value = null;
        if (StringUtils.isNotBlank(origColumnName)) {
            // check measures first
            for (int measureId = 0; measureId < cellDataSet.getSelectedMeasures().length; measureId++) {
                if (origColumnName.equals(cellDataSet.getSelectedMeasures()[measureId].getName()))
                    return totals[0][measureId].getValue();
            }
            // now check for any amount column
            // TODO when adding such columns support
        }
        return value;
    }

}
