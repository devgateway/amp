package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Date;

import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "AMP_PLEDGES")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AmpPledge implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = -2427620222411238018L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_PLEDGES_seq")
    @SequenceGenerator(name = "AMP_PLEDGES_seq", sequenceName = "AMP_PLEDGES_seq", allocationSize = 1)
    @Column(name = "amp_pledge_id")
    private Long ampPledgeId;

    @Column(name = "date_")
    private Date date;

    @Column(name = "amount")
    private Double amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency", referencedColumnName = "amp_currency_id")
    private AmpCurrency currency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adjustment_type", referencedColumnName = "id")
    private AmpCategoryValue adjustmentType;

    @Column(name = "program")
    private String program;

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
    public Long getAmpPledgeId() {
        return ampPledgeId;
    }
    public void setAmpPledgeId(Long ampPledgeId) {
        this.ampPledgeId = ampPledgeId;
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
    public String getProgram() {
        return program;
    }
    public void setProgram(String program) {
        this.program = program;
    }
    
    


}
