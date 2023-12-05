/**
 * AmpImputation.java
 * (c) 2009 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.digijava.module.aim.dbentity;

import java.io.Serializable;

/**
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 *
 */
public class AmpImputation implements Serializable {

    private String code;
    private String description;
    private AmpChapter chapter;
    
    public AmpImputation() {
        
    }
    
    /**
     * @param impCode
     */
    public AmpImputation(String impCode) {
        this.code=impCode;
    }
    
    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }
    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }
    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the chapter
     */
    public AmpChapter getChapter() {
        return chapter;
    }

    /**
     * @param chapter the chapter to set
     */
    public void setChapter(AmpChapter chapter) {
        this.chapter = chapter;
    }
}
