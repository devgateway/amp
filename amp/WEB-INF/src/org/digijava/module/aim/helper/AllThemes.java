package org.digijava.module.aim.helper ;

import java.util.Collection;

public class AllThemes
{
    Long programId;
    String programName;
    Collection allPrgIndicators;
    /**
     * @return Returns the allPrgIndicators.
     */
    public Collection getAllPrgIndicators() {
        return allPrgIndicators;
    }
    /**
     * @param allPrgIndicators The allPrgIndicators to set.
     */
    public void setAllPrgIndicators(Collection allPrgIndicators) {
        this.allPrgIndicators = allPrgIndicators;
    }
    /**
     * @return Returns the programId.
     */
    public Long getProgramId() {
        return programId;
    }
    /**
     * @param programId The programId to set.
     */
    public void setProgramId(Long programId) {
        this.programId = programId;
    }
    /**
     * @return Returns the programName.
     */
    public String getProgramName() {
        return programName;
    }
    /**
     * @param programName The programName to set.
     */
    public void setProgramName(String programName) {
        this.programName = programName;
    }
}
