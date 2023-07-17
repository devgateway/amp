/**
 * 
 */
package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.common.util.DateTimeUtil;

/**
 * @author mihai
 *
 */
import javax.persistence.*;

@Entity
@Table(name = "IPA_CONTRACT_DISBURSEMENT")
public class IPAContractDisbursement implements Serializable, Cloneable {
    private static final long serialVersionUID = -4688757182074104911L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ipa_contract_disbursement_seq")
    @SequenceGenerator(name = "ipa_contract_disbursement_seq", sequenceName = "IPA_CONTRACT_DISBURSEMENT_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "adjustment_type")
    private AmpCategoryValue adjustmentType;

    @Column(name = "amount")
    private Double amount;

    @ManyToOne
    @JoinColumn(name = "currency")
    private AmpCurrency currency;

    @ManyToOne
    @JoinColumn(name = "ipa_contract_id")
    private IPAContract contract;

    @Column(name = "date_")
    private Date date;
    


        public IPAContract getContract() {
            return contract;
        }

        public void setContract(IPAContract contract) {
            this.contract = contract;
        }
        
        public String getDisbDate() {
        String disbDate = "";
        if (date != null) {
            disbDate = DateTimeUtil.formatDateForPicker2(date, null);
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
