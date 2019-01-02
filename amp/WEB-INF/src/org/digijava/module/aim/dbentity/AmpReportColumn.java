package org.digijava.module.aim.dbentity ;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.annotations.reports.ColumnLike;
import org.digijava.module.aim.annotations.reports.Identificator;
import org.digijava.module.aim.annotations.reports.Level;
import org.digijava.module.aim.annotations.reports.Order;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.hibernate.Session;
import org.hibernate.annotations.Index;


public class AmpReportColumn implements Serializable, Comparable<AmpReportColumn>
{
//  @Identificator
//  private Long id;
    
    private static Logger logger = Logger.getLogger(AmpReportColumn.class);
    @ColumnLike
    private AmpColumns column;
    @Order
    private Long  orderId;
    @Level
    private AmpCategoryValue level;
    
    /**
     * whether this column was artificially added by the filter (not a part of the report specification)
     */
    private boolean addedByFilter;
    
//  private boolean generated;
    
    private static AmpCategoryValue defaultLevel = null;
    
    public AmpReportColumn() {
        
        try {
//          Session dbSession = PersistenceManager.getRequestDBSession();
            if ( defaultLevel==null )
                defaultLevel=CategoryManagerUtil.getAmpCategoryValueFromDb(CategoryConstants.ACTIVITY_LEVEL_KEY, (long)0);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage(), e);
        }
        
        
        level   = defaultLevel;
    }

    public AmpColumns getColumn() {
        return column;
    }
    public void setColumn(AmpColumns column) {
        this.column = column;
    }
    public Long  getOrderId() {
        return orderId;
    }
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    public AmpCategoryValue getLevel() {
        return level;
    }
    public void setLevel(AmpCategoryValue level) {
        this.level = level;
    }
    
    @Override
    public int compareTo(AmpReportColumn o) {
        int delta = this.getOrderId().compareTo(o.getOrderId());
        if (delta != 0) return delta;
        return this.getColumn().compareTo(o.getColumn());
    }

    @Override
    public boolean equals(Object arg0) {
        AmpReportColumn other = (AmpReportColumn) arg0;
        int delta = this.compareTo(other);
        return delta == 0;
    }
    
    @Override
    public int hashCode() {
        if (orderId != null) {
            return orderId.hashCode();
        } else {
            return super.hashCode();
        }
    }

    @Override
    public String toString()
    {
        if (column == null)
            return "AmpReportColumn [null]";
        return "ARC: " + column.toString() + "";
    }
    
    public boolean isAddedByFilter(){
        return addedByFilter;
    }
    
    public void setAddedByFilter(boolean addedByFilter){
        this.addedByFilter = addedByFilter;
    }

    
}
