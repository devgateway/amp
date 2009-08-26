

package org.digijava.module.widget.helper;

import java.io.Serializable;
import org.jfree.chart.urls.PieURLGenerator;
import org.jfree.data.general.PieDataset;


public class DonorSectorPieChartURLGenerator implements PieURLGenerator,Serializable {

    private String prefix;
    private String sectorId="sectorId";

    public DonorSectorPieChartURLGenerator(String url){
        this.prefix=url;
    }

    public String generateURL(PieDataset data, Comparable key, int pieIndex) {
        String url=prefix;
        SectorHelper sector=(SectorHelper)key;
        url += "~" + this.sectorId + "=" + sector.getIds();
        return url;
    }

}
