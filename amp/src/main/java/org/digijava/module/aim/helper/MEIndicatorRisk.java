package org.digijava.module.aim.helper;

public class MEIndicatorRisk implements Comparable {
    
    private String risk;
    private byte riskRating;
    private int riskCount;
    /**
     * @return Returns the risk.
     */
    public String getRisk() {
        return risk;
    }
    /**
     * @param risk The risk to set.
     */
    public void setRisk(String risk) {
        this.risk = risk;
    }
    /**
     * @return Returns the riskCount.
     */
    public int getRiskCount() {
        return riskCount;
    }
    /**
     * @param riskCount The riskCount to set.
     */
    public void setRiskCount(int riskCount) {
        this.riskCount = riskCount;
    }
    /**
     * @return Returns the riskRating.
     */
    public byte getRiskRating() {
        return riskRating;
    }
    /**
     * @param riskRating The riskRating to set.
     */
    public void setRiskRating(byte riskRating) {
        this.riskRating = riskRating;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (obj instanceof MEIndicatorRisk) {
            MEIndicatorRisk risk = (MEIndicatorRisk) obj;
            return risk.getRisk().equalsIgnoreCase(this.risk);
        } else throw new ClassCastException();
    }
    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object obj) {
        if (obj instanceof MEIndicatorRisk) {
            MEIndicatorRisk risk = (MEIndicatorRisk) obj;
            return (risk.getRiskRating() - riskRating);
        } else throw new ClassCastException();
    }   
}
