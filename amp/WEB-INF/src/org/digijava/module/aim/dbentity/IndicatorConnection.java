package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Set;

/**
 * Connection Indicator.
 * @author Irakli Kobiashvili
 *
 */
public class IndicatorConnection  extends AbstractAuditLogger implements Serializable, Comparable<IndicatorTheme> {

    private static final long serialVersionUID = 1L;
    
    private Long id;

    /**
     * Indicator. this field is mandatory. It defines indicator in connection with activity, theme or team. 
     */
    private AmpIndicator indicator;

    /**
     * Indicator values.
     */
    protected Set<AmpIndicatorValue> values;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AmpIndicator getIndicator() {
        return indicator;
    }

    public void setIndicator(AmpIndicator indicator) {
        this.indicator = indicator;
    }

    public Set<AmpIndicatorValue> getValues() {
        return values;
    }

    public void setValues(Set<AmpIndicatorValue> values) {
        this.values = values;
    }

    /**
     * Compares by db IDs.
     */
    public int compareTo(IndicatorTheme o) {
        return getId().compareTo(o.getId());
    }


}
