package org.digijava.kernel.ampapi.endpoints.dashboards.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Octavian Ciubotaru
 */
public class HeatMapConfigs {

    private List<Column> columns;

    private List<HeatMapConfigIndexed> charts;

    @ApiModelProperty("the colors to use for every threshold, "
            + "for values greater or equal than <key>, use <value> color")
    private Map<BigDecimal, String> amountColors;

    public HeatMapConfigs(List<Column> columns, List<HeatMapConfigIndexed> charts,
            Map<BigDecimal, String> amountColors) {
        this.columns = columns;
        this.charts = charts;
        this.amountColors = amountColors;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public List<HeatMapConfigIndexed> getCharts() {
        return charts;
    }

    public Map<BigDecimal, String> getAmountColors() {
        return amountColors;
    }
}
