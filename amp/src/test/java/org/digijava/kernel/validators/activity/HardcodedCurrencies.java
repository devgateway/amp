package org.digijava.kernel.validators.activity;

import org.digijava.module.aim.dbentity.AmpCurrency;

/**
 * @author Octavian Ciubotaru
 */
public class HardcodedCurrencies {

    private AmpCurrency usd;
    private AmpCurrency eur;

    public HardcodedCurrencies() {
        usd = new AmpCurrency();
        usd.setAmpCurrencyId(21L);
        usd.setCurrencyCode("USD");
        usd.setCurrencyName("US Dollar");

        eur = new AmpCurrency();
        eur.setAmpCurrencyId(10L);
        eur.setCurrencyCode("EUR");
        eur.setCurrencyName("Euro");
    }

    public AmpCurrency getUsd() {
        return usd;
    }

    public AmpCurrency getEur() {
        return eur;
    }
}
