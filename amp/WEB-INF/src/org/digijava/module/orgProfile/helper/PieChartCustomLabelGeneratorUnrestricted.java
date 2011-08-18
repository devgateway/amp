package org.digijava.module.orgProfile.helper;

import java.text.AttributedString;
import java.text.NumberFormat;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.general.PieDataset;


public class PieChartCustomLabelGeneratorUnrestricted implements  PieSectionLabelGenerator  {
    private NumberFormat percentFormat;

    public  PieChartCustomLabelGeneratorUnrestricted() {
        percentFormat= NumberFormat.getPercentInstance();
    }



     public  PieChartCustomLabelGeneratorUnrestricted(NumberFormat percentFormat) {
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
		label = percentFormat.format(percent);
        return label;

    }

    public AttributedString generateAttributedSectionLabel(PieDataset arg0, Comparable arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
