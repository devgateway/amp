package org.digijava.module.aim.dbentity ;

import java.io.Serializable;

import org.dgfoundation.amp.nireports.NiCurrency;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.annotations.interchange.PossibleValueId;
import org.digijava.module.aim.annotations.interchange.PossibleValueValue;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.Identifiable;
import org.hibernate.Query;

import javax.persistence.*;

@TranslatableClass (displayName = "Currency")
@Entity(name = "amp_currency")
@Table(name = "amp_currency")
public class AmpCurrency implements Serializable, Comparable<AmpCurrency>, Identifiable, NiCurrency
{
    //IATI-check: to not be ignored. obtained from possible values 
    @PossibleValueId
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_currency_seq")
    @SequenceGenerator(name = "amp_currency_seq", sequenceName = "amp_currency_seq", allocationSize = 1)
    @Column(name = "amp_currency_id")
    private Long ampCurrencyId;
    @PossibleValueValue
    @Column(name = "currency_code")
    private String currencyCode;
    @Column(name = "country_name")
    private String countryName;
    @TranslatableField
    @Column(name = "currency_name")
    private String currencyName;
    @Column(name = "active_flag")
    private Integer activeFlag;

    @ManyToOne
    @JoinColumn(name = "country_location_id")
    private AmpCategoryValueLocations countryLocation;

    @ManyToOne
    @JoinColumn(name = "amp_fiscal_cal_id")
    private AmpFiscalCalendar calendar;
    
   @Column(name = "virtual_flag")
    private boolean virtual;
    
    /**
     * @return Returns the activeFlag.
     */
    public Integer getActiveFlag() {
        return activeFlag;
    }
    /**
     * @param activeFlag The activeFlag to set.
     */
    public void setActiveFlag(Integer activeFlag) {
        this.activeFlag = activeFlag;
    }
    /**
     * @return Returns the ampCurrencyId.
     */
    public Long getAmpCurrencyId() {
        return ampCurrencyId;
    }
    /**
     * @param ampCurrencyId The ampCurrencyId to set.
     */
    public void setAmpCurrencyId(Long ampCurrencyId) {
        this.ampCurrencyId = ampCurrencyId;
    }
    
    /**
     * @return the countryLocation
     */
    public AmpCategoryValueLocations getCountryLocation() {
        return countryLocation;
    }
    /**
     * @param countryLocation the countryLocation to set
     */
    public void setCountryLocation(AmpCategoryValueLocations countryLocation) {
        this.countryLocation = countryLocation;
    }
    /**
     * @return Returns the countryName.
     */
    public String getCountryName() {
        return countryName;
    }
    /**
     * @param countryName The countryName to set.
     */
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
    /**
     * @return Returns the currencyCode.
     */
    public String getCurrencyCode() {
        return currencyCode;
    }
    /**
     * @param currencyCode The currencyCode to set.
     */
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
    /**
     * @return Returns the currencyName.
     */
    public String getCurrencyName() {
        return currencyName;
    }
    
    /**
     * @param currencyName The currencyName to set.
     */
    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }
    
    public boolean isVirtual() {
        return this.virtual;
    }
    
    public void setVirtual(Boolean virtual) {
        this.virtual = (virtual != null && virtual);
    }
    
    @Override public boolean equals(Object obj) {
        return this.compareTo((AmpCurrency) obj) == 0;
    }
    
    @Override public int compareTo(AmpCurrency obj) {
        int delta = this.currencyCode.compareTo(obj.getCurrencyCode());
        if (delta != 0) return delta;
        return Long.compare(this.ampCurrencyId, obj.ampCurrencyId);
    }

    @Override public Object getIdentifier() {
        return this.getAmpCurrencyId();
    }
    
    @Override public String toString() {
        return currencyCode;
    }
    
    /**
     * cache of result of calling _is_rate_cache
     */
    Boolean _is_rate_cache = null;
    
    /**
     * Returns true iff currency has at least one active exchange rate against the base currency
     * @param currencyCode currency Code
     * @return boolean
     * @author Irakli Kobiashvili
     */
    public boolean isRate()
    {
        if (_is_rate_cache != null)
            return _is_rate_cache;
        
        try {
            String baseCurrencyCode = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY);
            if (currencyCode.equalsIgnoreCase(baseCurrencyCode)){
                return true;
            }
        
            String queryString = "SELECT COUNT(*) FROM " + AmpCurrencyRate.class.getName() + " f " + 
                                "WHERE (f.fromCurrencyCode = :currencyCode AND f.toCurrencyCode = :baseCurrencyCode) " + 
                                "OR (f.fromCurrencyCode = :baseCurrencyCode AND f.toCurrencyCode = :currencyCode) AND f.exchangeRate IS NOT NULL";
            
            Query q = PersistenceManager.getRequestDBSession().createQuery(queryString);
            q.setString("currencyCode", currencyCode);
            q.setString("baseCurrencyCode", baseCurrencyCode);
            long cnt = PersistenceManager.getLong(q.uniqueResult());
            boolean result = (cnt > 0);
            _is_rate_cache = result;
            return result;
        } catch (Exception ex) {            
            throw new RuntimeException("Error retriving currency exchange rate for "+ currencyCode,ex);
        }
    }

    public boolean isActive() {
        return this.getActiveFlag() != 0;
    }
    /**
     * @return the calendar
     */
    public AmpFiscalCalendar getCalendar() {
        return calendar;
    }
    /**
     * @param calendar the calendar to set
     */
    public void setCalendar(AmpFiscalCalendar calendar) {
        this.calendar = calendar;
    }
    @Override
    public long getId() {
        return ampCurrencyId;
    }
}   
