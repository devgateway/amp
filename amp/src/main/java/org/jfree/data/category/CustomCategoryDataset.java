package org.jfree.data.category;

import org.jfree.data.DefaultKeyedValues2D;
import org.jfree.data.UnknownKeyException;
import org.jfree.data.general.AbstractDataset;
import org.jfree.data.general.DatasetChangeEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A custom implementation of the {@link CategoryDataset} interface.
 */
public class CustomCategoryDataset extends AbstractDataset
        implements CategoryDataset, Serializable {

    /**
     * For serialization.
     */
    private static final long serialVersionUID = -8197172347491644622L;

    /**
     * A storage structure for the data.
     */
    private DefaultKeyedValues2D data;

    private List<String[]> toolTips;

    private int vIndex = 0;



    public List<String[]> getToolTips() {
        return toolTips;
    }

    public String[] getToolTips(int col) {
        return toolTips.get(col);
    }

    public String[] getNextTooltip() {
        if(vIndex < toolTips.size()) {
            return toolTips.get(vIndex++);
        }

        vIndex = 0;

        return toolTips.get(vIndex++);
    }

    public void setToolTips(List<String[]> toolTips) {
        this.toolTips = toolTips;
    }

    /**
     * Creates a new (empty) dataset.
     */
    public CustomCategoryDataset() {
        this.data = new DefaultKeyedValues2D();
    }

    /**
     * Returns the number of rows in the table.
     *
     * @return The row count.
     */
    public int getRowCount() {
        return this.data.getRowCount();
    }

    /**
     * Returns the number of columns in the table.
     *
     * @return The column count.
     */
    public int getColumnCount() {
        return this.data.getColumnCount();
    }

    /**
     * Returns a value from the table.
     *
     * @param row    the row index (zero-based).
     * @param column the column index (zero-based).
     * @return The value (possibly <code>null</code>).
     */
    public Number getValue(int row, int column) {
        return this.data.getValue(row, column);
    }

    /**
     * Returns a row key.
     *
     * @param row the row index (zero-based).
     * @return The row key.
     */
    public Comparable getRowKey(int row) {
        return this.data.getRowKey(row);
    }

    /**
     * Returns the row index for a given key.
     *
     * @param key the row key.
     * @return The row index.
     */
    public int getRowIndex(Comparable key) {
        return this.data.getRowIndex(key);
    }

    /**
     * Returns the row keys.
     *
     * @return The keys.
     */
    public List getRowKeys() {
        return this.data.getRowKeys();
    }

    /**
     * Returns a column key.
     *
     * @param column the column index (zero-based).
     * @return The column key.
     */
    public Comparable getColumnKey(int column) {
        return this.data.getColumnKey(column);
    }

    /**
     * Returns the column index for a given key.
     *
     * @param key the column key.
     * @return The column index.
     */
    public int getColumnIndex(Comparable key) {
        return this.data.getColumnIndex(key);
    }

    /**
     * Returns the column keys.
     *
     * @return The keys.
     */
    public List getColumnKeys() {
        return this.data.getColumnKeys();
    }

    /**
     * Returns the value for a pair of keys.
     *
     * @param rowKey    the row key (<code>null</code> not permitted).
     * @param columnKey the column key (<code>null</code> not permitted).
     * @return The value (possibly <code>null</code>).
     * @throws UnknownKeyException if either key is not defined in the dataset.
     */
    public Number getValue(Comparable rowKey, Comparable columnKey) {
        return this.data.getValue(rowKey, columnKey);
    }

    /**
     * Adds a value to the table.  Performs the same function as setValue().
     *
     * @param value     the value.
     * @param rowKey    the row key.
     * @param columnKey the column key.
     */
    public void addValue(Number value, Comparable rowKey,
                         Comparable columnKey) {
        this.data.addValue(value, rowKey, columnKey);
        fireDatasetChanged();
    }

    /**
     * Adds a value to the table.
     *
     * @param value     the value.
     * @param rowKey    the row key.
     * @param columnKey the column key.
     */
    public void addValue(double value, Comparable rowKey,
                         Comparable columnKey) {
        addValue(new Double(value), rowKey, columnKey);
    }


    /**
     * Adds a value to the custom tooltip
     * ( see {@link org.jfree.chart.labels.CustomCategoryToolTipGenerator} class for more information ).
     *
     * @param value the tooltip value.
     */
    public void addCustomTooltipValue(String [] value) {

        if (toolTips == null) {
            toolTips = new ArrayList<String[]>();
        }

        toolTips.add(value);

    }


    /**
     * Adds or updates a value in the table and sends a
     * {@link DatasetChangeEvent} to all registered listeners.
     *
     * @param value     the value (<code>null</code> permitted).
     * @param rowKey    the row key (<code>null</code> not permitted).
     * @param columnKey the column key (<code>null</code> not permitted).
     */
    public void setValue(Number value, Comparable rowKey,
                         Comparable columnKey) {
        this.data.setValue(value, rowKey, columnKey);
        fireDatasetChanged();
    }

    /**
     * Adds or updates a value in the table and sends a
     * {@link DatasetChangeEvent} to all registered listeners.
     *
     * @param value     the value.
     * @param rowKey    the row key (<code>null</code> not permitted).
     * @param columnKey the column key (<code>null</code> not permitted).
     */
    public void setValue(double value, Comparable rowKey,
                         Comparable columnKey) {
        setValue(new Double(value), rowKey, columnKey);
    }

    /**
     * Adds the specified value to an existing value in the dataset (if the
     * existing value is <code>null</code>, it is treated as if it were 0.0).
     *
     * @param value     the value.
     * @param rowKey    the row key (<code>null</code> not permitted).
     * @param columnKey the column key (<code>null</code> not permitted).
     * @throws UnknownKeyException if either key is not defined in the dataset.
     */
    public void incrementValue(double value,
                               Comparable rowKey,
                               Comparable columnKey) {
        double existing = 0.0;
        Number n = getValue(rowKey, columnKey);
        if (n != null) {
            existing = n.doubleValue();
        }
        setValue(existing + value, rowKey, columnKey);
    }

    /**
     * Removes a value from the dataset.
     *
     * @param rowKey    the row key.
     * @param columnKey the column key.
     */
    public void removeValue(Comparable rowKey, Comparable columnKey) {
        this.data.removeValue(rowKey, columnKey);
        fireDatasetChanged();
    }

    /**
     * Removes a row from the dataset.
     *
     * @param rowIndex the row index.
     */
    public void removeRow(int rowIndex) {
        this.data.removeRow(rowIndex);
        fireDatasetChanged();
    }

    /**
     * Removes a row from the dataset.
     *
     * @param rowKey the row key.
     */
    public void removeRow(Comparable rowKey) {
        this.data.removeRow(rowKey);
        fireDatasetChanged();
    }

    /**
     * Removes a column from the dataset.
     *
     * @param columnIndex the column index.
     */
    public void removeColumn(int columnIndex) {
        this.data.removeColumn(columnIndex);
        fireDatasetChanged();
    }

    /**
     * Removes a column from the dataset.
     *
     * @param columnKey the column key.
     */
    public void removeColumn(Comparable columnKey) {
        this.data.removeColumn(columnKey);
        fireDatasetChanged();
    }

    /**
     * Clears all data from the dataset and sends a {@link DatasetChangeEvent}
     * to all registered listeners.
     */
    public void clear() {
        this.data.clear();
        fireDatasetChanged();
    }

    /**
     * Tests if this object is equal to another.
     *
     * @param obj the other object.
     * @return A boolean.
     */
    public boolean equals(Object obj) {

        if (obj == this) {
            return true;
        }

        if (!(obj instanceof CategoryDataset)) {
            return false;
        }

        CategoryDataset that = (CategoryDataset) obj;
        if (!getRowKeys().equals(that.getRowKeys())) {
            return false;
        }

        if (!getColumnKeys().equals(that.getColumnKeys())) {
            return false;
        }

        int rowCount = getRowCount();
        int colCount = getColumnCount();
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < colCount; c++) {
                Number v1 = getValue(r, c);
                Number v2 = that.getValue(r, c);
                if (v1 == null) {
                    if (v2 != null) {
                        return false;
                    }
                } else if (!v1.equals(v2)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns a hash code for the dataset.
     *
     * @return A hash code.
     */
    public int hashCode() {
        return this.data.hashCode();
    }

}
