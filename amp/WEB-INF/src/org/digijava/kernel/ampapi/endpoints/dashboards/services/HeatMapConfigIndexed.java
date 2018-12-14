package org.digijava.kernel.ampapi.endpoints.dashboards.services;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import org.digijava.module.aim.helper.HeatMapConfig;

/**
 * Same as {@link org.digijava.module.aim.helper.HeatMapConfig} but with columns converted to indexes.
 *
 * @author Octavian Ciubotaru
 */
public class HeatMapConfigIndexed {

    @ApiModelProperty(value = "name will be always in English, never translated",
            example = "Fragmentation by Donor and Sector")
    private final String name;
    private final HeatMapConfig.Type type;

    private final List<Integer> xColumns;

    private final List<Integer> yColumns;

    public HeatMapConfigIndexed(String name, HeatMapConfig.Type type, List<Integer> xColumns,
            List<Integer> yColumns) {
        this.name = name;
        this.type = type;
        this.xColumns = xColumns;
        this.yColumns = yColumns;
    }

    public String getName() {
        return name;
    }

    public HeatMapConfig.Type getType() {
        return type;
    }

    @ApiModelProperty(value = "indexes ref of all used columns",
            name = "xColumns")
    public List<Integer> getxColumns() {
        return xColumns;
    }

    @ApiModelProperty(value = "indexes ref of all used columns",
            name = "yColumns")
    public List<Integer> getyColumns() {
        return yColumns;
    }
}
