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
import javax.persistence.*;

@Entity
@Table(name = "AMP_INFLATION_SOURCE")
public class AmpInflationSource implements Serializable, Identifiable {
    
    private static final long serialVersionUID = -6422564054049848996L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_INFLATION_SOURCE_seq")
    @SequenceGenerator(name = "AMP_INFLATION_SOURCE_seq", sequenceName = "AMP_INFLATION_SOURCE_seq", allocationSize = 1)
    @Column(name = "amp_inflation_source_id")
    private Long id;

    @Column(name = "name", nullable = false)
    @NotNull

    private String name;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "selected", nullable = false)
    @NotNull

    private Boolean selected = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "frequency", length = 1)
    private IRFrequency frequency;

    @ManyToOne
    @JoinColumn(name = "amp_currency_id", nullable = false)
    @NotNull
    private AmpCurrency currency;

    @Column(name = "api_token")
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
