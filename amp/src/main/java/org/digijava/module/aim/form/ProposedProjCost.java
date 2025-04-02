package org.digijava.module.aim.form;
import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.helper.FormatHelper;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class ProposedProjCost extends ActionForm implements Comparable<ProposedProjCost>{

    private String currencyCode;
    private String currencyName;
    private String funAmount;
    private String funDate;
    private String funAmountFormated;

    public String getFunAmount() {
        return funAmount;
    }

    public Double getFunAmountAsDouble() {
        return FormatHelper.parseDouble(funAmount);
    }

    public String getFunDate() {
        return funDate;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setFunDate(String funDate) {
        this.funDate = funDate;
    }

    public void setFunAmount(String funAmount) {
        this.funAmount = funAmount;
    }

    public void setFunAmountAsDouble(Double funAmount) {
            this.funAmount =  FormatHelper.formatNumber(funAmount);
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    /**
     * @return the funAmountFormated
     */
    public String getFunAmountFormated() {
        return FormatHelper.formatNumber(getFunAmountAsDouble());
    }

    /**
     * @param funAmountFormated the funAmountFormated to set
     */
    public void setFunAmountFormated(String funAmountFormated) {
        this.funAmountFormated = FormatHelper.formatNumber(getFunAmountAsDouble());;
    }

    @Override
    public int compareTo(ProposedProjCost o) {
            if (this == o)
            return 0;

        if (this.funDate == null)
            return -1;
        if (o.funDate == null)
            return 1;

        if (Long.parseLong(this.funDate) < Long.parseLong(o.funDate))
            return -1;
        else
            return 1;

    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
        
    }
    
    public String getCurrencyName () {
        return this.currencyName;
    }

}
