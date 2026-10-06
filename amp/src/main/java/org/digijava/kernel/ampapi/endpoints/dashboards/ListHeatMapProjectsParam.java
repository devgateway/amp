package org.digijava.kernel.ampapi.endpoints.dashboards;

/**
 * @author Octavian Ciubotaru
 */
public class ListHeatMapProjectsParam extends HeatMapParameters {

    private Integer offset;

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }
}
