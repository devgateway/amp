/**
 * AmpByAssistTypeAmount.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.digijava.module.aim.helper;

import java.text.DecimalFormat;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Feb 10, 2006
 * 
 */
public class AmpByAssistTypeAmount implements Cloneable {

    
    private static DecimalFormat mf = new DecimalFormat("###,###,###,###,###") ;
    private String fundingTerms;

    private double fundingAmount;

    public AmpByAssistTypeAmount() {
        
    }
    
    public String toString() {
        return mf.format(fundingAmount);
    }
    
    public AmpByAssistTypeAmount(String fundingTerms, double fundingAmount) {
        this.fundingAmount = fundingAmount;
        this.fundingTerms = fundingTerms==null?"Unknown":fundingTerms;
    }
    
    /**
     * @return Returns the fundingAmount.
     */
    public double getFundingAmount() {
        return fundingAmount;
    }

    /**
     * @param fundingAmount
     *            The fundingAmount to set.
     */
    public void setFundingAmount(double fundingAmount) {
        this.fundingAmount = fundingAmount;
    }

    /**
     * @return Returns the fundingTerms.
     */
    public String getFundingTerms() {
        return fundingTerms;
    }

    /**
     * @param fundingTerms
     *            The fundingTerms to set.
     */
    public void setFundingTerms(String fundingTerms) {
        this.fundingTerms = fundingTerms;
    }

    public void addFundingAmount(double fundingAmount) {
        this.fundingAmount += fundingAmount;
    }
    
    public void substractFundingAmount(double fundingAmount) {
        this.fundingAmount -= fundingAmount;
    }

    
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
