package org.digijava.module.orgProfile.helper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.AttributedString;
import java.text.NumberFormat;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.general.PieDataset;


public class PieChartCustomLabelGenerator implements  PieSectionLabelGenerator  {
    private NumberFormat percentFormat;

    public  PieChartCustomLabelGenerator() {
        percentFormat= NumberFormat.getPercentInstance();
    }



     public  PieChartCustomLabelGenerator(NumberFormat percentFormat) {
            this.percentFormat=percentFormat;
     }


    public String generateSectionLabel(PieDataset dataset, Comparable key) {
        double total = DatasetUtilities.calculatePieDatasetTotal(dataset);
        String label = "";
        Number value = dataset.getValue(key);
        double percent = 0.0;
        if (value != null) {
            double v = value.doubleValue();
            if (v > 0.0) {
                percent = v / total;
            }
        }
        // don't show percent for sectors which value are less then 6
        BigDecimal bd = new BigDecimal(percent*100).setScale(percentFormat.getMaximumFractionDigits(),percentFormat.getRoundingMode());
        if(bd.doubleValue()>=6){
             label =percentFormat.format(percent);
        }
        return label;

    }

    public AttributedString generateAttributedSectionLabel(PieDataset arg0, Comparable arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
