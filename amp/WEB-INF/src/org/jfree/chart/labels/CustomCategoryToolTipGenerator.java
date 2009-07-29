package org.jfree.chart.labels;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.CustomCategoryDataset;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;

/**
 * 
 */
public class CustomCategoryToolTipGenerator
        extends AbstractCategoryItemLabelGenerator
        implements CategoryToolTipGenerator, Serializable {

    /**
     * For serialization.
     */
    private static final long serialVersionUID = -6743801596818710764L;

    /**
     * The default format string.
     */
    public static final String DEFAULT_TOOL_TIP_FORMAT_STRING
            = "({0}, {1}) = {2} \rBaseline: {4} \rCurrent: {5} \rTarget: {7}";

    /**
     * Creates a new generator with a default number formatter.
     */
    public CustomCategoryToolTipGenerator() {
        super(DEFAULT_TOOL_TIP_FORMAT_STRING, NumberFormat.getInstance());
    }

    /**
     * Creates a new generator with the specified number formatter.
     *
     * @param labelFormat the label format string (<code>null</code> not
     *                    permitted).
     * @param formatter   the number formatter (<code>null</code> not permitted).
     */
    public CustomCategoryToolTipGenerator(String labelFormat,
                                          NumberFormat formatter) {
        super(labelFormat, formatter);
    }

    /**
     * Creates a new generator with the specified date formatter.
     *
     * @param labelFormat the label format string (<code>null</code> not
     *                    permitted).
     * @param formatter   the date formatter (<code>null</code> not permitted).
     */
    public CustomCategoryToolTipGenerator(String labelFormat,
                                          DateFormat formatter) {
        super(labelFormat, formatter);
    }

    /**
     * Generates the tool tip text for an item in a dataset.  Note: in the
     * current dataset implementation, each row is a series, and each column
     * contains values for a particular category.
     *
     * @param dataset the dataset (<code>null</code> not permitted).
     * @param row     the row index (zero-based).
     * @param column  the column index (zero-based).
     * @return The tooltip text (possibly <code>null</code>).
     */
    public String generateToolTip(CategoryDataset dataset,
                                  int row, int column) {
        if (dataset == null) {
            throw new IllegalArgumentException("Null 'dataset' argument.");
        }
        String result = null;
        Object[] items = createItemArray(dataset, row, column);

        if (dataset instanceof CustomCategoryDataset) {
            CustomCategoryDataset custDataset = (CustomCategoryDataset) dataset;

            String tip[] = custDataset.getNextTooltip();
            if (tip != null) {
                Object itemsTmp[] = new Object[tip.length + 4];

                for (int i = 0; i < items.length; i++) {
                    itemsTmp[i] = items[i];
                }

                for (int i = 4; i < itemsTmp.length; i++) {
                    itemsTmp[i] = tip[i - 4];
                }
               
                result = MessageFormat.format(getLabelFormat(), itemsTmp);
            } else {
                result = MessageFormat.format(getLabelFormat(), items);
            }

        } else {
            result = MessageFormat.format(getLabelFormat(), items);
        }
        return result;
    }


    /**
     * Tests this generator for equality with an arbitrary object.
     *
     * @param obj the object (<code>null</code> permitted).
     * @return A boolean.
     */
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof StandardCategoryToolTipGenerator)) {
            return false;
        }
        return super.equals(obj);
    }

}
