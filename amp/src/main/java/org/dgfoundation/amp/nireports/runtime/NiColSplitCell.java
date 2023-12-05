package org.dgfoundation.amp.nireports.runtime;

import org.dgfoundation.amp.nireports.ComparableValue;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.behaviours.TaggedMeasureBehaviour;

/**
 * info about the cell which has been used to split the parent report
 * @author Dolghier Constantin
 *
 */
public class NiColSplitCell {
    
    /**
     * the entity type used to generate this V-hierarchy. <br />
     * Please see the {@link NiReportsEngine}.PSEUDOCOLUMN_XXX constants for the values supported by the NiReports Core. 
     * The schema can add any number of values here (for example, crosstabs as implemented by {@link TaggedMeasureBehaviour}). 
     */
    public final String entityType;
    
    /**
     * an arbitrary value whose interpretation depends on {@link #entityType}. <br />
     * NiReports Core use its {@link ComparableValue#getValue()} for naming the column, 
     * while its {@link ComparableValue#getComparable()} for deciding the ordering of subcolumns
     */
    public final ComparableValue<String> info;
    
    public NiColSplitCell(String entityType, ComparableValue<String> info) {
        this.entityType = entityType;
        this.info = info;
    }
    
    @Override
    public String toString() {
        return String.format("type: %s, info: %s", entityType, info);
    }
}
