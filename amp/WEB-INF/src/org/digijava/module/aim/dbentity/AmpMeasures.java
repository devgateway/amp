package org.digijava.module.aim.dbentity ;

import java.io.Serializable;
import java.util.Set;

import org.dgfoundation.amp.ar.ArConstants;
import org.digijava.module.aim.annotations.reports.Identificator;
import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "AMP_MEASURES")
//but used in reports engine
public class AmpMeasures  implements Serializable, Comparable
{

    @Id
    @Identificator

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_MEASURES_seq_generator")
    @SequenceGenerator(name = "AMP_MEASURES_seq_generator", sequenceName = "AMP_MEASURES_seq", allocationSize = 1)
    @Column(name = "measureId")
    private Long measureId;

    @Column(name = "measureName")
    private String measureName;

    @Column(name = "aliasName")
    private String aliasName;

    @Column(name = "type")
    private String type;

    @Column(name = "expression")
    private String expression;

    @Column(name = "description")
    private String description;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "AMP_REPORT_MEASURES",
            joinColumns = @JoinColumn(name = "measureId"),
            inverseJoinColumns = @JoinColumn(name = "amp_report_id"))
    private Set<AmpReports> reports;


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
