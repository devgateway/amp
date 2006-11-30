package org.digijava.module.aim.form;
import java.text.*;

import org.apache.struts.action.*;

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
public class ProposedProjCost extends ActionForm{

    private String currencyCode;
    private String funAmount;
    private String funDate;

    public String getFunAmount() {
        return funAmount;
    }

    public Double getFunAmountAsDouble() {
        try {
            DecimalFormat format = new DecimalFormat();
            return new Double(format.parse(funAmount).doubleValue());
        } catch(Exception ex) {
            return null;
        }
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
        try{
            DecimalFormat format = new DecimalFormat();
            this.funAmount = format.format(Double.valueOf(funAmount));
        }catch(Exception ex){
            this.funAmount = null;
        }
    }

    public void setFunAmountAsDouble(Double funAmount) {
        try{
            DecimalFormat format = new DecimalFormat();
            this.funAmount = format.format(funAmount);
        }catch(Exception ex){
            this.funAmount = null;
        }
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

}
