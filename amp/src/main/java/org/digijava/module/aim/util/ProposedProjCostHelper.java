

package org.digijava.module.aim.util;

import java.util.Date;
import java.util.GregorianCalendar;

import org.dgfoundation.amp.Util;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.form.ProposedProjCost;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.FormatHelper;

/**
 *
 * @author Medea
 */
public class ProposedProjCostHelper {

    private String currencyCode;
    private Double funAmount;
    private Date funDate;

    public ProposedProjCostHelper(String currencyCode, Double funAmount, Date funDate) {
        this.currencyCode = currencyCode;
        this.funAmount = funAmount;
        this.funDate = funDate;
    }

    ProposedProjCostHelper() {
    }
    

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Double getFunAmount() {
        return funAmount;
    }

    public void setFunAmount(Double funAmount) {
        this.funAmount = funAmount;
    }

    public Date getFunDate() {
        return funDate;
    }

    public void setFunDate(Date funDate) {
        this.funDate = funDate;
    }
}
