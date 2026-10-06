/*
 * AmpReportsOptions.java
 * Created: 25-Nov-2005
 */

package org.digijava.module.aim.dbentity;

public class AmpReportsOptions {

    private Long ampOptionId;

    private String name;

    private char options;

    public Long getAmpOptionId() {
        return ampOptionId;
    }

    public String getName() {
        return name;
    }

    public char getOptions() {
        return options;
    }

    public void setAmpOptionId(Long ampOptionId) {
        this.ampOptionId = ampOptionId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOptions(char options) {
        this.options = options;
    }
    
}
