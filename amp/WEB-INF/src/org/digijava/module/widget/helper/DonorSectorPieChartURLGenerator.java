

package org.digijava.module.widget.helper;

import java.io.Serializable;
import java.util.List;

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
        List<Long> secIds=sector.getIds();
        for(Long secId: secIds){
              url += "~" + this.sectorId + "=" + secId;
        }
     
        return url;
    }

}
