/**
 * 
 */
package org.digijava.module.aim.dbentity;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.dgfoundation.amp.currency.IRFrequency;
import org.digijava.module.aim.util.Identifiable;

/**
 * Inflation Rates Data Source
 * 
 * @author Nadejda Mandrescu
 */
public class AmpInflationSource implements Serializable, Identifiable {
    
    private static final long serialVersionUID = -6422564054049848996L;
    
    private Long id;
    
    /**
     * Data Source name, translatable
     */
    @NotNull
    private String name;
    
    /**
     * Data Source description, translatable
     */
    private String description;
    
    /**
     * Flags if the current data source was chosen to provide inflation rates
     */
    @NotNull
    private Boolean selected;
    
    /**
     * Inflation Rates frequency selected to be provided via current data source
     */
    private IRFrequency frequency;
    
    /**
     * The currency for which this Data Source provides inflation rates
     */
    @NotNull
    private AmpCurrency currency;
    
    /**
     * Other non-standard settings available for the current data source 
     */
    //DEFLATOR: once we have Settings V2, store any custom settings per data source there, how explicitly
    private String apiToken;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }


    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }


    /**
     * @return the name
     */
    public String getName() {
        return name;
    }


    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }


    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }


    /**
     * @return the selected
     */
    public Boolean getSelected() {
        return selected;
    }


    /**
     * @param selected the selected to set
     */
    public void setSelected(Boolean selected) {
        this.selected = selected;
    }


    /**
     * @return the frequency
     */
    public IRFrequency getFrequency() {
        return frequency;
    }


    /**
     * @param frequency the frequency to set
     */
    public void setFrequency(IRFrequency frequency) {
        this.frequency = frequency;
    }


    /**
     * @return the currency
     */
    public AmpCurrency getCurrency() {
        return currency;
    }


    /**
     * @param currency the currency to set
     */
    public void setCurrency(AmpCurrency currency) {
        this.currency = currency;
    }

    /**
     * @return the apiToken
     */
    public String getApiToken() {
        return apiToken;
    }


    /**
     * @param apiToken the apiToken to set
     */
    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }


    @Override
    public Object getIdentifier() {
        return id;
    }

}
