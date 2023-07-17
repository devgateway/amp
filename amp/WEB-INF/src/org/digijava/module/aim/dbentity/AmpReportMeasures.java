package org.digijava.module.aim.dbentity ;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.serializers.report.AmpReportMeasureSerializer;
import org.digijava.module.aim.annotations.reports.ColumnLike;
import org.digijava.module.aim.annotations.reports.Level;
import org.digijava.module.aim.annotations.reports.Order;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "amp_report_measures")
@JsonSerialize(using = AmpReportMeasureSerializer.class)
public class AmpReportMeasures  implements Serializable, Comparable<AmpReportMeasures>{
    private static Logger logger = Logger.getLogger(AmpReportMeasures.class);


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "measureId")
    private AmpMeasures measure;

    @Column(name = "orderId")
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "levelId")
    private AmpCategoryValue level;

    @Transient
    private static AmpCategoryValue defaultLevel = null;
    
    public AmpReportMeasures() {
        if (defaultLevel == null)
            defaultLevel = CategoryManagerUtil.getAmpCategoryValueFromDb(CategoryConstants.ACTIVITY_LEVEL_KEY, 0l);
        
        level = defaultLevel;
    }
    
    public AmpReportMeasures(AmpMeasures measure, long orderId) {
        this.measure = measure;
        this.orderId = orderId;
    }
    
    public AmpMeasures getMeasure() {
        return measure;
    }
    public void setMeasure(AmpMeasures measure) {
        this.measure = measure;
    }

    
    public Long getOrderId() {
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
    
    public int compareTo(AmpReportMeasures o) {
        try {
            if(orderId==null) return -1;
            if(o==null) return -1;
            if(o.getOrderId()==null)return -1;
            int myOrder = getOrder();
            int oOrder  = ((AmpReportMeasures)o).getOrder();
            return myOrder - oOrder;
        }
        catch (NumberFormatException e) {
            logger.error("NumberFormatException:", e);
            return -1;
        }
    }
    
    @Override
    public boolean equals(Object arg0) {
        try{
            AmpReportMeasures r = (AmpReportMeasures) arg0;
            return compareTo(r) == 0;
        }
        catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        if (orderId != null) {
            return orderId.hashCode();
        } else {
            return super.hashCode();
        }
    }
    
    public Integer getOrder() {
        try{
            if(orderId==null) return new Integer(0);
            return  orderId.intValue();
        }catch (NumberFormatException e) {
            logger.error("NumberFormatException:"+orderId+":", e);
            return 0;
        }       
    }
    
    @Override
    public String toString () {
        return this.measure.getMeasureName() +  " | " + this.orderId;
    }

}
