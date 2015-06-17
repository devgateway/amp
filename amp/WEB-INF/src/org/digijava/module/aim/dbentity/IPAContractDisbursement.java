/**
 * 
 */
package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.common.util.DateTimeUtil;

/**
 * @author mihai
 *
 */
public class IPAContractDisbursement implements Serializable, Cloneable {
	private static final long serialVersionUID = -4688757182074104911L;
	@Interchangeable(fieldTitle="ID")
	private Long id;
	@Interchangeable(fieldTitle="Adjustment Type",fmPath="/Activity Form/Contracts/Contract Item/Contract Disbursements/Adjustment Type")
	private AmpCategoryValue adjustmentType;
	@Interchangeable(fieldTitle="Amount",fmPath="/Activity Form/Contracts/Contract Item/Contract Disbursements/Amount")
	private Double amount;
	@Interchangeable(fieldTitle="Currency",fmPath="/Activity Form/Contracts/Contract Item/Contract Disbursements/Currency")
	private AmpCurrency currency;
	@Interchangeable(fieldTitle="Date",fmPath="/Activity Form/Contracts/Contract Item/Contract Disbursements/Transaction Date")
	private Date date;
	@Interchangeable(fieldTitle="Contract", recursive=true)
        private IPAContract contract;

        public IPAContract getContract() {
            return contract;
        }

        public void setContract(IPAContract contract) {
            this.contract = contract;
        }
        
        public String getDisbDate() {
        String disbDate = "";
        try {
            if (date != null) {
                disbDate = DateTimeUtil.parseDateForPicker2(date, null);
            }
        } catch (Exception ex) {
            Logger.getLogger(IPAContractDisbursement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return disbDate;
    }
        
         public void setDisbDate(String date){
        try {
            if(date!=null && "".compareTo(date)!=0) this.date = DateTimeUtil.parseDateForPicker(date);
        } catch (Exception ex) {
            Logger.getLogger(IPAContractDisbursement.class.getName()).log(Level.SEVERE, null, ex);
        }
        }

        public String getCurrCode() {
            String currCode="";
            if(currency!=null){
                currCode=currency.getCurrencyCode();
            }
            return currCode;
        }

        public void setCurrCode(String currCode) {
           currency= CurrencyUtil.getCurrencyByCode(currCode);
        }
	public AmpCategoryValue getAdjustmentType() {
		return adjustmentType;
	}
	public void setAdjustmentType(AmpCategoryValue adjustmentType) {
		this.adjustmentType = adjustmentType;
	}
	public Double getAmount() {
		return FeaturesUtil.applyThousandsForVisibility(amount);
	}
	public void setAmount(Double amount) {
		this.amount = FeaturesUtil.applyThousandsForEntry(amount);
	}
	public AmpCurrency getCurrency() {
		return currency;
	}
	public void setCurrency(AmpCurrency currency) {
		this.currency = currency;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
}
