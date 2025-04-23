/**
 * 
 */
package org.dgfoundation.amp.reports;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity types
 * 
 * @author Nadejda Mandrescu
 */
public enum ActivityType {
    /** Standard activities */
    ACTIVITY("A"), 
    /** South-South Cooperation activities */
    SSC_ACTIVITY("S"),
    /** Pledges */
    PLEDGE("P");
    
    public static final List<String> STR_VALUES = getStringValues();
    
    private String value;
    ActivityType(String value) {
        this.value = value;
    }
    
    @Override
    public String toString(){
        return this.value;
    }
    
    /*
    public static ActivityType getValueOf(String value) {
        ActivityType.
        for (ActivityType typeName : ActivityType.values()) {
            if (typeName.toString().equals(value)) {
                return typeName;
            }
        }
        return null;
    }*/
    
    private static List<String> getStringValues() {
        List<String> strValues = new ArrayList<String>();
        for (ActivityType type : ActivityType.values()) {
            strValues.add(type.value);
        }
        return strValues;
    }
}
