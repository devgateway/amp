

package org.digijava.module.widget.helper;

import java.io.Serializable;

import org.jfree.chart.urls.PieURLGenerator;
import org.jfree.data.general.PieDataset;


public class DonorSectorPieChartURLGenerator implements PieURLGenerator,Serializable {

	private static final long serialVersionUID = 1L;
	private String prefix;
    private String sectorId="sectorIds";

    public DonorSectorPieChartURLGenerator(String url){
        this.prefix=url;
    }

    public String generateURL(PieDataset data, Comparable key, int pieIndex) {
        String url=prefix;
        SectorHelper sector=(SectorHelper)key;
        String[] secIds=sector.getIds().split(",");
        for(int i=0;i<secIds.length;i++){
              url += "~" + this.sectorId + "=" + secIds[i];
        }
     
        return url;
    }

}
