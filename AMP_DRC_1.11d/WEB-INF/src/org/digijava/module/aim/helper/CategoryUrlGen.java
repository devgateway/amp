package org.digijava.module.aim.helper;

import org.digijava.module.aim.util.MEIndicatorsUtil;
import org.jfree.chart.urls.CategoryURLGenerator;
import org.jfree.data.category.CategoryDataset;

public class CategoryUrlGen
    implements CategoryURLGenerator {
        private String prefix = "index.html";

        /** Series parameter name to go in each URL */
        private String seriesParameterName = "series";

        /** Category parameter name to go in each URL */
        private String categoryParameterName = "ind";

        public CategoryUrlGen(String prefix) {
                if (prefix == null) {
                        throw new IllegalArgumentException(
                            "Null 'prefix' argument.");
                }
                this.prefix = prefix;
        }
        /** Generates URL  using the id of the indicaror*/
        public String generateURL(CategoryDataset dataset, int series,
                                  int category) {
                String url = this.prefix;
                Comparable categoryKey = dataset.getColumnKey(category);
                boolean firstParameter = url.indexOf("?") == -1;
                url += firstParameter ? "?" : "&amp;";

                //gets the activity id  from prefix
                String[] urls = prefix.split("ampActivityId=");
                String[] activityIds = urls[1].split("&");

                //get indicator id using activity id and category name
                Long meInd = MEIndicatorsUtil.getIndicatorsForActivity(Long.
                    parseLong(activityIds[0]), categoryKey.toString());

                try {
                        url += this.categoryParameterName + "="
                            + meInd;
                }
                catch (Exception e) {
                        url += "&" + this.categoryParameterName + "="
                            + categoryKey.toString();
                }
                return url;
        }

}
