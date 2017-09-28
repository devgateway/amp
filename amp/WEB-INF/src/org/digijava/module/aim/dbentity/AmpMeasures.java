package org.digijava.module.aim.dbentity ;

import java.io.Serializable;
import java.util.Set;

import org.dgfoundation.amp.ar.ArConstants;
import org.digijava.module.aim.annotations.reports.Identificator;

//but used in reports engine
public class AmpMeasures  implements Serializable, Comparable
{
    @Identificator
    private Long measureId ;
    
    private String measureName ;
    private String aliasName;
    
    /**
     * not used
     */
    private String type;
    private Set reports;
    private String expression;
    private String description;
    
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * not used
     * @return
     */
    public String getType() {
        return type;
    }
    
    /**
     * not used
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }
    
    public String getAliasName() {
        return aliasName;
    }
    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }
    
    public Long getMeasureId() {
        return measureId;
    }
    public void setMeasureId(Long measureId) {
        this.measureId = measureId;
    }
    public String getMeasureName() {
        return measureName;
    }
    public void setMeasureName(String measureName) {
        this.measureName = measureName;
    }

    public Set getReports() {
        return reports;
    }
    public void setReports(Set reports) {
        this.reports = reports;
    }
    
    public int compareTo(Object o) {
        // TODO Auto-generated method stub
        if (!(o instanceof AmpMeasures))
            return -1;
        AmpMeasures auxColumn=(AmpMeasures) o;
        return this.getMeasureName().compareTo(auxColumn.getMeasureName());
    }

    /**
     * can this column be split by hierarchies?
     * @return
     */
    public boolean isSplittable()
    {
        boolean isNotSplittable = getMeasureName().equals(ArConstants.UNDISBURSED_BALANCE) || 
                getMeasureName().equals(ArConstants.TOTAL_COMMITMENTS) || 
                getMeasureName().equals(ArConstants.UNCOMMITTED_BALANCE) ||
                getExpression() != null;
        return !isNotSplittable;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
    
    @Override
    public String toString()
    {
        return this.measureName;
    }
}
