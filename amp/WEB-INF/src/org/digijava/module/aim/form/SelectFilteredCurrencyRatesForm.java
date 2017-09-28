/**
 * 
 */
package org.digijava.module.aim.form;

import java.util.List;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.dbentity.AmpFilteredCurrencyRate;
import org.digijava.module.aim.helper.KeyValue;

/**
 * @author Alex Gartner
 *
 */
public class SelectFilteredCurrencyRatesForm extends ActionForm {
    
    private List<AmpFilteredCurrencyRate> existingFilteredRates;
    private Long [] selectedFilteredRates;
    

    private String toCurrencyCode;
    private String fromCurrencyCode;
    
    private List<KeyValue> existingCurrencies;
    
    public List<AmpFilteredCurrencyRate> getExistingFilteredRates() {
        return existingFilteredRates;
    }
    public void setExistingFilteredRates(
            List<AmpFilteredCurrencyRate> existingFilteredRates) {
        this.existingFilteredRates = existingFilteredRates;
    }
    public Long[] getSelectedFilteredRates() {
        return selectedFilteredRates;
    }
    public void setSelectedFilteredRates(Long[] selectedFilteredRates) {
        this.selectedFilteredRates = selectedFilteredRates;
    }
    public String getToCurrencyCode() {
        return toCurrencyCode;
    }
    public void setToCurrencyCode(String toCurrencyCode) {
        this.toCurrencyCode = toCurrencyCode;
    }
    public String getFromCurrencyCode() {
        return fromCurrencyCode;
    }
    public void setFromCurrencyCode(String fromCurrencyCode) {
        this.fromCurrencyCode = fromCurrencyCode;
    }
    public List<KeyValue> getExistingCurrencies() {
        return existingCurrencies;
    }
    public void setExistingCurrencies(List<KeyValue> existingCurrencies) {
        this.existingCurrencies = existingCurrencies;
    }
    
    
}
