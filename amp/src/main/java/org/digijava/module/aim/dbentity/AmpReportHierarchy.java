package org.digijava.module.aim.dbentity ;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.digijava.kernel.ampapi.endpoints.serializers.report.AmpReportHierarchySerializer;
import org.digijava.module.aim.annotations.reports.ColumnLike;
import org.digijava.module.aim.annotations.reports.Level;
import org.digijava.module.aim.annotations.reports.Order;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

import java.io.Serializable;


@JsonSerialize(using = AmpReportHierarchySerializer.class)
public class AmpReportHierarchy implements Serializable, Comparable
{
    @ColumnLike
    private AmpColumns column;
    @Order
    private Long levelId;
    @Level
    private AmpCategoryValue level;
    
    private static AmpCategoryValue defaultLevel = null;
    
    public AmpReportHierarchy(){
        if ( defaultLevel==null )
            defaultLevel=CategoryManagerUtil.getAmpCategoryValueFromDb(CategoryConstants.ACTIVITY_LEVEL_KEY, (long)0);
        level   = defaultLevel;
    }

    public AmpColumns getColumn() {
        return column;
    }
    public void setColumn(AmpColumns column) {
        this.column = column;
    }
    public Long getLevelId() {
        return levelId;
    }
    public void setLevelId(Long levelId) {
        this.levelId = levelId;
    }
    public AmpCategoryValue getLevel() {
        return level;
    }
    public void setLevel(AmpCategoryValue level) {
        this.level = level;
    }
    
    public int compareTo(Object o) {
        try {
            Long myOrder    = levelId;
            Long oOrder =  ((AmpReportHierarchy)o).getLevelId() ;
            return myOrder.intValue()-oOrder.intValue();
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
            return -1;
        }
    }
    
    @Override
    public boolean equals(Object arg0) {
        return compareTo(arg0)==0;
    }

    @Override
    public int hashCode() {
        if (levelId != null) {
            return levelId.hashCode();
        } else {
            return super.hashCode();
        }
    }
    
    @Override
    public String toString()
    {
        return String.format("hierarchy on %s", column);
    }
}
