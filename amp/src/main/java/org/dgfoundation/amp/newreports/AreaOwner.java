package org.dgfoundation.amp.newreports;

/**
 * the split cell which has generated a subreport, for a GroupReportData / ColumnReportData. The activityId which is the master of the row, for a leaf 
 * TODO: if there is real need for it, make the field rich
 * @author Dolghier Constantin
 *
 */
public class AreaOwner {
    /**
     * the {@link #columnName} and {@link #debugString} of a leaf ReportArea's owner
     */
    public final static String LEAF = "##LEAF##";
    
    public final String columnName;
    public final String debugString;
    
    /**
     * the activityId of the row, if this is a leaf
     */
    public final long id;

    /**
     * builds an {@link AreaOwner} for a CRD / GRD 
     */
    public AreaOwner(String columnName, String debugString, long id) {
        this.columnName = columnName;
        this.debugString = debugString;
        this.id = id;
    }

    /**
     * builds an {@link AreaOwner} for a CRD / GRD 
     */
    public AreaOwner(String columnName, String debugString) {
        this(columnName, debugString, -1);
    }
    
    public AreaOwner(long id) {
        if (id <= 0)
            throw new RuntimeException("leaf AreaOwner should have nonnull ids");
        this.columnName = LEAF;
        this.debugString = LEAF;
        this.id = id;
    }
    
    @Override
    public boolean equals(Object oth) {
        if (!(oth instanceof AreaOwner))
            return false;
        AreaOwner other = (AreaOwner) oth;
        return this.columnName.equals(other.columnName) && this.debugString.equals(other.debugString) && (this.id == other.id);
    }
    
    @Override
    public int hashCode() {
        return this.columnName.hashCode() + 19 * this.debugString.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("colName: %s, debugString: %s, id: %d", columnName, debugString, id);
    }
}
