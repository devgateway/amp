

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
    
public static ProposedProjCost getProposedProjCost(ProposedProjCost ppc,String currCode){
    	
    	ProposedProjCost propProjCost = new ProposedProjCost();
    	propProjCost.setCurrencyCode(ppc.getCurrencyCode());
    	if (propProjCost.getCurrencyCode() != null) {
    			AmpCurrency currency = CurrencyUtil.getCurrencyByCode(propProjCost.getCurrencyCode());
    			if (currency != null) 
    				propProjCost.setCurrencyName(currency.getCurrencyName());
    	}
    	propProjCost.setFunAmount(ppc.getFunAmount());
    	propProjCost.setFunDate(ppc.getFunDate());
    	if(ppc.getCurrencyCode()==null || ppc.getCurrencyCode().equals(currCode)|| ppc.getFunDate()==null)
    		return propProjCost;
    	GregorianCalendar gc;
		try {
			gc = FormatHelper.parseDate(ppc.getFunDate());
		
	    	Date dt = gc.getTime();
			double frmExRt = Util.getExchange(ppc.getCurrencyCode(),new java.sql.Date(dt.getTime()));
			double toExRt = Util.getExchange(currCode,new java.sql.Date(dt.getTime()));
			double amt = CurrencyWorker.convert1(ppc.getFunAmountAsDouble(),frmExRt,toExRt);
			propProjCost.setFunAmount(FormatHelper.formatNumber(amt));
			propProjCost.setCurrencyCode(currCode);
			propProjCost.setCurrencyName(CurrencyUtil.getCurrencyByCode(currCode).getCurrencyName());
		    
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return propProjCost;
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
