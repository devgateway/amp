package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.common.util.DateTimeUtil;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "IPA_CONTRACT_AMENDMENT")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class IPAContractAmendment implements Serializable, Cloneable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ipa_contract_amendment_seq_gen")
    @SequenceGenerator(name = "ipa_contract_amendment_seq_gen", sequenceName = "IPA_CONTRACT_AMENDMENT_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "amount")
    private Double amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency")
    private AmpCurrency currency;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ipa_contract_id")
    private IPAContract contract;

    @Column(name = "date_")
    private Date date;

    @Column(name = "reference")
    private String reference;


    /**
     * 
     */
    
    public IPAContract getContract() {
        return contract;
    }

    public void setContract(IPAContract contract) {
        this.contract = contract;
    }

    public String getAmendDate() {
        String amendDate = "";
        if (date != null) {
            amendDate = DateTimeUtil.formatDateForPicker2(date, null);
        }
        return amendDate;
    }

    public void setAmendDate(String date) {
        try {
            if (date != null && "".compareTo(date) != 0)
                this.date = DateTimeUtil.parseDateForPicker(date);
        } catch (Exception ex) {
            Logger.getLogger(IPAContractDisbursement.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
    }

    public String getCurrCode() {
        String currCode = "";
        if (currency != null) {
            currCode = currency.getCurrencyCode();
        }
        return currCode;
    }

    public void setCurrCode(String currCode) {
        currency = CurrencyUtil.getCurrencyByCode(currCode);
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

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return super.clone();
    }
    
    public String getAmoutStr() {
        String amountStr = "";
        if (amount != null) {
            amountStr = BigDecimal.valueOf(amount).toPlainString();
        }
        return amountStr;
    }

    public void setAmoutStr(String amountStr) {
        amount = Double.valueOf(amountStr);
    }
}
