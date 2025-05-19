package org.digijava.module.aim.helper;

public class Flag {
       
    private Long cntryId;
    private String cntryName;
    private boolean defaultFlag;
    /**
     * @return Returns the cntryId.
     */
    public Long getCntryId() {
        return cntryId;
    }
    /**
     * @param cntryId The cntryId to set.
     */
    public void setCntryId(Long cntryId) {
        this.cntryId = cntryId;
    }
    /**
     * @return Returns the cntryName.
     */
    public String getCntryName() {
        return cntryName;
    }
    /**
     * @param cntryName The cntryName to set.
     */
    public void setCntryName(String cntryName) {
        this.cntryName = cntryName;
    }
    /**
     * @return Returns the defaultFlag.
     */
    public boolean isDefaultFlag() {
        return defaultFlag;
    }
    /**
     * @param defaultFlag The defaultFlag to set.
     */
    public void setDefaultFlag(boolean defaultFlag) {
        this.defaultFlag = defaultFlag;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (obj instanceof Flag) {
            Flag f = (Flag) obj;
            return (f.getCntryId().longValue() == cntryId.longValue());
        } else throw new ClassCastException();
    }
    
    
}
