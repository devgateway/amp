package org.dgfoundation.amp.newreports;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * class which identifies a path to sort by, like for Example [Primary Sector] or [Measures][2013][Q3][Actual Commitments]
 * @author Dolghier Constantin, Nadejda Mandrescu
 */
public class SortingInfo {
    // one of the possible values for rootPath
    public final static int ROOT_PATH_NONE = 0;
    public final static int ROOT_PATH_FUNDING = 1;
    public final static int ROOT_PATH_TOTALS = 2;
    
    /** sorting ascending or descending */
    public final boolean ascending;
    public final List<String> hierPath;
    
    /** one of ROOT_PATH_XXX constants*/
    public final int rootPath;
    
    public SortingInfo(List<String> hierPath, int rootPath, boolean ascending) {
        this.hierPath = hierPath;
        this.ascending = ascending;
        this.rootPath = rootPath;
    }
    
    /**
     * Configures sorting by a single text column or measure column that is without grouping
     * @param entity - column / measure
     * @param ascending - true if sorting ascending
     */
    public SortingInfo(ReportMeasure entity, boolean ascending) {
        this(Arrays.asList(entity.getMeasureName()), ROOT_PATH_TOTALS, ascending);
    }
    
    /**
     * Configures sorting by a single text column or measure column that is without grouping
     * @param entity - column / measure
     * @param ascending - true if sorting ascending
     * @param isTotals
     */
    public SortingInfo(ReportColumn column, boolean ascending) {
        this(Arrays.asList(column.getColumnName()), ROOT_PATH_NONE, ascending);
    }
    
    public boolean isHierarchySorter(Set<String> allHiers) {
        return this.rootPath == ROOT_PATH_NONE && this.hierPath.size() == 1 && allHiers.contains(this.hierPath.get(0));
    }
    
    public String buildPath(String separator, String fundingName, String totalsName) {
        List<String> toJoin = new ArrayList<>();
        switch(this.rootPath) {
            case ROOT_PATH_FUNDING:
                toJoin.add(fundingName);
                break;
            
            case ROOT_PATH_TOTALS:
                toJoin.add(totalsName);
                break;
            
            default: break;
        }
        toJoin.addAll(this.hierPath);
        return String.join(separator, toJoin);
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof SortingInfo && o != null)
            return this.toString().equals(o.toString());
        return false;
    }
    
    @Override
    public String toString() {
        return "SortingInfo: " + (ascending ? "ASC" : "DESC") + " path = " + hierPath;
    }
}
