package org.digijava.module.aim.helper;

import org.apache.log4j.Logger;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpIndicatorRiskRatings;
import org.digijava.module.aim.util.IndicatorUtil;
import org.jfree.chart.urls.PieURLGenerator;
import org.jfree.data.general.PieDataset;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

/**
 * This class is used for generating urls for Dashboard pie graph,
 *  because the words with accents are passed incorrctly to url
 *
 */
public class PieChartURLGenerator implements PieURLGenerator, Serializable {

   private static Logger logger = Logger.getLogger(PieChartURLGenerator.class);

    private String prefix;
    /** Category parameter name to go in each URL */
    private String categoryParameterName = "category";
    /** trsanslation language of the Category parameter  */
    private String languageCode = "en";
    private Long siteId = 3L; // hey, hardcoded database keys! how cute!
    /** The pie index parameter name. */
    private String indexParameterName = "pieIndex";

    public PieChartURLGenerator(String prefix, String categoryParameterName, String languageCode, Long siteId) {
        if (prefix == null) {
            throw new IllegalArgumentException(
                    "Null 'prefix' argument.");
        }
        this.prefix = prefix;
        this.categoryParameterName = categoryParameterName;
        this.languageCode = languageCode;
        this.siteId = siteId;

    }

    public String generateURL(PieDataset dataset, Comparable key, int pieIndex) {
        String url = this.prefix;
        //gets the activity id  from prefix
        String[] urls = prefix.split("ampActivityId=");
        String[] activityIds = urls[1].split("&");
        Long ampActivityId = Long.parseLong(activityIds[0]);
        String keyEng = "";
        try {
            // get risks associated with this activity
            if(ampActivityId!=null && !ampActivityId.equals(new Long(0))){
                Collection<AmpIndicatorRiskRatings> risks = IndicatorUtil.getRisks(ampActivityId);
                Iterator<AmpIndicatorRiskRatings> riskIterator = risks.iterator();
                while (riskIterator.hasNext()) {
                    AmpIndicatorRiskRatings risk = riskIterator.next();
                    String translatedName = TranslatorWorker.translateText(risk.getRatingName(), languageCode, siteId);
                    if (translatedName.equals(key.toString())) {
                        //get risk name in English, this is how we get rid of  the accents  problem
                        keyEng = risk.getRatingName();
                        break;
                    }
                }
                if (url.indexOf("?") > -1) {
                    url += "&amp;" + this.categoryParameterName + "=" + keyEng;
                } else {
                    url += "?" + this.categoryParameterName + "=" + keyEng;
                }
            }           
            
            if (this.indexParameterName != null) {
                url += "&amp;" + this.indexParameterName + "=" + String.valueOf(pieIndex);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }


        return url;

    }
}
