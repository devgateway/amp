// Generated by delombok at Mon Mar 24 00:10:06 EET 2014
package org.digijava.module.fundingpledges.dbentity;

import org.dgfoundation.amp.algo.AlgoUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.dbentity.FundingInformationItem;
import org.digijava.module.aim.dbentity.IPAContract;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.common.util.DateTimeUtil;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.aim.dbentity.AmpCurrency;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "AMP_FUNDING_PLEDGES_DETAILS")
public class FundingPledgesDetails implements FundingInformationItem, Identifiable{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_FUNDING_PLEDGES_DETAILS_seq")
    @SequenceGenerator(name = "AMP_FUNDING_PLEDGES_DETAILS_seq", sequenceName = "AMP_FUNDING_PLEDGES_DETAILS_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "amount")
    private Double amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pledge_id")
    private FundingPledges pledgeid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pledge_type")
    private AmpCategoryValue pledgetype;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency")
    private AmpCurrency currency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_of_assistance")
    private AmpCategoryValue typeOfAssistance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aid_modality")
    private AmpCategoryValue aidmodality;

    @Column(name = "year")
    private String fundingYear;

    @Column(name = "dateStart")
    private Date fundingDateStart;

    @Column(name = "dateEnd")
    private Date fundingDateEnd;

    public java.sql.Timestamp getFunding_date() {
        java.sql.Timestamp dateStart = fundingDateStart == null ? null : new Timestamp(fundingDateStart.getTime());
        java.sql.Timestamp yearStamp = fundingYear == null ? null : Timestamp.valueOf(new StringBuffer(getFundingYear()).append("-01-01 00:00:00").toString());
        if (isDateRangeEnabled())
            return dateStart == null ? yearStamp : dateStart; // prioritize dateStart
        
        return yearStamp == null ? dateStart : yearStamp; // prioritize yearStamp
    }
    
    public Object getIdentifier(){
        return this.id;
    }
    
    @Override public Double getTransactionAmount(){
        return FeaturesUtil.applyThousandsForVisibility(this.getAmount());
    }
    
    @Override public Double getAbsoluteTransactionAmount(){
        return this.getAmount();
    }
    
    @Override public AmpCurrency getAmpCurrencyId(){
        return this.getCurrency();
    }
    
    @Override public Date getTransactionDate(){
        int transactionYear = this.fundingYear == null ? 2014 : Integer.parseInt(this.fundingYear);
        return new GregorianCalendar(transactionYear, 1, 1).getTime(); //1st of february
    }
            
    @Override public Date getReportingDate(){
        return getTransactionDate();
    }
    
    @Override public AmpOrganisation getRecipientOrg(){
        return null;
    }
    
    @Override public AmpRole getRecipientRole() {
        return null;
    }
    
    @Override public Integer getTransactionType(){
        return Constants.PLEDGE;
    }
    
    @Override public AmpCategoryValue getAdjustmentType(){
        return CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getAmpCategoryValueFromDB();
    }
    
    @Override public String getDisbOrderId(){
        return null;
    }
    
    @Override public Double getFixedExchangeRate(){
        return null;
    }
    
    @Override public IPAContract getContract(){
        return null;
    }
    
    @Override public String getExpCategory(){
        return null;
    }
    
    @Override public Float getCapitalSpendingPercentage(){
        return null;
    }
    
    @Override public Long getDbId(){
        return this.id;
    }

    
    // trash getters / setters go below
    public int hashCode() {
        return System.identityHashCode(this);
    }
    
    public boolean equals(Object oth) {
        return this == oth;
    }
    
    @java.lang.SuppressWarnings("all")
    public long getId() {
        return this.id;
    }
    
    @java.lang.SuppressWarnings("all")
    public FundingPledges getPledgeid() {
        return this.pledgeid;
    }
    
    @java.lang.SuppressWarnings("all")
    public AmpCategoryValue getPledgetype() {
        return this.pledgetype;
    }
    
    @java.lang.SuppressWarnings("all")
    public AmpCategoryValue getTypeOfAssistance() {
        return this.typeOfAssistance;
    }
    
    @java.lang.SuppressWarnings("all")
    public AmpCategoryValue getAidmodality() {
        return this.aidmodality;
    }
    
    @java.lang.SuppressWarnings("all")
    public Double getAmount() {
        return this.amount;
    }
    
    @java.lang.SuppressWarnings("all")
    public AmpCurrency getCurrency() {
        return this.currency;
    }
    
    @java.lang.SuppressWarnings("all")
    public String getFundingYear() {
        return this.fundingYear;
    }
    
    @java.lang.SuppressWarnings("all")
    public void setId(final long id) {
        this.id = id;
    }
    
    @java.lang.SuppressWarnings("all")
    public void setPledgeid(final FundingPledges pledgeid) {
        this.pledgeid = pledgeid;
    }
    
    @java.lang.SuppressWarnings("all")
    public void setPledgetype(final AmpCategoryValue pledgetype) {
        this.pledgetype = pledgetype;
    }
    
    @java.lang.SuppressWarnings("all")
    public void setTypeOfAssistance(final AmpCategoryValue typeOfAssistance) {
        this.typeOfAssistance = typeOfAssistance;
    }
    
    @java.lang.SuppressWarnings("all")
    public void setAidmodality(final AmpCategoryValue aidmodality) {
        this.aidmodality = aidmodality;
    }
    
    @java.lang.SuppressWarnings("all")
    public void setAmount(final Double amount) {
        this.amount = amount;
    }
    
    @java.lang.SuppressWarnings("all")
    public void setCurrency(final AmpCurrency currency) {
        this.currency = currency;
    }
    
    @java.lang.SuppressWarnings("all")
    public void setFundingYear(final String fundingYear) {
        this.fundingYear = fundingYear;
    }
    
    public Date getFundingDateStart() {
        return fundingDateStart;
    }

    public void setFundingDateStart(Date fundingDateStart) {
        this.fundingDateStart = fundingDateStart;
    }

    public Date getFundingDateEnd() {
        return fundingDateEnd;
    }

    public void setFundingDateEnd(Date fundingDateEnd) {
        this.fundingDateEnd = fundingDateEnd;
    }

    /**
     * constructs a string of the form "undefined", "year" or "yearstart - yearend", describing a pledge detail
     * @param fpd
     * @return
     */
    public String getDatesDescription(){
        return getDatesDescriptionOf(AlgoUtils.getLongFrom(this.getFundingYear()), this.getFundingDateStart(), this.getFundingDateEnd());
    }
    
    /**
     * chooses, based on the 1. available options 2. the {@link #isDateRangeEnabled()} function, what is the representative "dates" string for a detail
     * @param fundingYear
     * @param fundingDateStart
     * @param fundingDateEnd
     * @return
     */
    public static String getDatesDescriptionOf(Long fundingYear, Date fundingDateStart, Date fundingDateEnd){
        String unspecified = TranslatorWorker.translateText("unspecified");
        String year = fundingYear == null ? null : fundingYear.toString();
        
        String dates = (fundingDateStart != null && fundingDateEnd != null) ? 
                DateTimeUtil.formatDate(fundingDateStart) + " - " + DateTimeUtil.formatDate(fundingDateEnd) : null;
        List<String> values = new ArrayList<>(); 
        if (isDateRangeEnabled()){
            values.add(dates);
            values.add(year);
        } else {
            values.add(year);
            values.add(dates);
        }
        values.add(unspecified);
        for(String v:values)
            if (v != null) return v;
        throw new RuntimeException("bug finding a suitable date to display!");
    }

    
    
    public static boolean isDateRangeEnabled(){
        return FeaturesUtil.isVisibleField("Pledge Funding - Year Range");
    }
    
    @Override public String toString(){
        return String.format("%.2f %s", this.getAmount(), this.getCurrency().getCurrencyCode());
    }

    @Override
    public void setTransactionAmount(Double transactionAmount) {
        this.setAmount(transactionAmount);
    }

    @Override
    public Boolean getDisasterResponse() {
        return null;
    }

    @Override
    public String getDisbursementId() {
        return null;
    }

    @Override
    public AmpCategoryValue getExpenditureClass() {
        return null;
    }

    @Override
    public Date getUpdatedDate() {
        // We dont need this for now
        return null;
    }

    @Override
    public Long getCheckSum() {
        // We dont need this for now
        return null;
    }

    @Override
    public void setCheckSum(Long checkSum) {
        // We dont need this for now
        
    }

    @Override
    public void setUpdatedDate(Date dateUpdated) {
        // We dont need this for now
        
    }
}
