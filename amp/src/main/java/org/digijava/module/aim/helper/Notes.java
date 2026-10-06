package org.digijava.module.aim.helper ;

public class Notes
{
    private String description ;
    private Long ampNotesId;
    private int dflag ;
    
    /**
     * @return
     */
    public String getDescription() {
        return description;
    }

    public Long getAmpNotesId()
    {
        return ampNotesId ; 
    }

    /**
     * @return
     */
    public int getDflag() {
        return dflag;
    }

    /**
     * @param string
     */
    public void setDescription(String string) {
        description = string;
    }

    /**
     * @param string
     */
    public void setDflag(int val) {
        dflag = val;
    }

    public void setAmpNotesId(Long id) 
    {
        this.ampNotesId = id ;
    }

}
