package org.dgfoundation.amp.newreports;

/**
 * class describing a measure to be used in a report
 * @author Dolghier Constantin
 *
 */
public class ReportMeasure extends NamedTypedEntity {
    
    /**
     * 
     * @param measureName - the name of the measure
     */
    public ReportMeasure(String measureName) {
        super(measureName);
    }
        
    public String getMeasureName() {
        return this.entityName;
    }
}
