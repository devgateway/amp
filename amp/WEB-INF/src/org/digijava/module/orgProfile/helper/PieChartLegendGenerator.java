
package org.digijava.module.orgProfile.helper;

import java.text.AttributedString;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.data.general.PieDataset;




public class PieChartLegendGenerator  implements  PieSectionLabelGenerator {
    private int charNumbersBeforeNewLine=25;

    public PieChartLegendGenerator() {}


     public PieChartLegendGenerator(int charNumbersBeforeNewLine) {
            this.charNumbersBeforeNewLine=25;
     }



    public String generateSectionLabel(PieDataset dataset, Comparable key) {
        String result = "";
        int index = 0;
        String newKey = (String) key;
        while (newKey.length() > 0) {
            if (newKey.length() < charNumbersBeforeNewLine) {
                result+=newKey.substring(index);
                break;
            }

            result += newKey.substring(index, charNumbersBeforeNewLine) + '\n';
            newKey = newKey.substring(charNumbersBeforeNewLine);
        }

        return result;
    }

      
    

    public AttributedString generateAttributedSectionLabel(PieDataset dataset, Comparable key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
