package org.digijava.kernel.ampapi.endpoints.dashboards.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModelProperty;
import org.digijava.module.aim.helper.HeatMapConfig;

/**
 * @author Octavian Ciubotaru
 */
public class HeatMapConfigs {

    private List<Column> columns;

    private List<HeatMapConfig> charts;

    @ApiModelProperty("the colors to use for every threshold, "
            + "for values greater or equal than <key>, use <value> color")
    private Map<BigDecimal, String> amountColors;

    public HeatMapConfigs(List<Column> columns, List<HeatMapConfig> charts, Map<BigDecimal, String> amountColors) {
        this.columns = columns;
        this.charts = charts;
        this.amountColors = amountColors;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public List<HeatMapConfig> getCharts() {
        return charts;
    }

    public Map<BigDecimal, String> getAmountColors() {
        return amountColors;
    }
}
