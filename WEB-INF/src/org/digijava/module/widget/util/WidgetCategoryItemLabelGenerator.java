package org.digijava.module.widget.util;

import java.text.NumberFormat;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.data.category.CategoryDataset;
import org.digijava.module.aim.helper.FormatHelper;

/**
 *
 * @author medea
 */
public class WidgetCategoryItemLabelGenerator extends StandardCategoryItemLabelGenerator {

    public WidgetCategoryItemLabelGenerator() {
        super();
    }

    public WidgetCategoryItemLabelGenerator(String label, NumberFormat formatter) {
        super(label, formatter);
    }

    @Override
    public String generateLabel(CategoryDataset data, int series, int category) {
        String label = "";
        label = super.generateLabel(data, series, category);
        if (FormatHelper.parseDouble(label)==0) {
            label = "";
        }
        return label;
    }
}
