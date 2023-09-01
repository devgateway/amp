package org.digijava.kernel.persistence;

import org.digijava.module.aim.dbentity.AmpCurrency;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Non-persistent implementation of {@code InMemoryManager} which is backed by an in-memory map.
 * <p>
 * Mainly intended for testing purposes, where a full blown persistent system isn't required.
 * <p>
 * Took from Haiti.
 *
 * @author Octavian Ciubotaru
 */
public class InMemoryCurrenciesManager implements InMemoryManager<AmpCurrency> {
    
    private static InMemoryCurrenciesManager instance;
    
    private AmpCurrency usd;
    private AmpCurrency eur;
    
    private final Map<Long, AmpCurrency> currencies = new HashMap<>();
    
    
    public static InMemoryCurrenciesManager getInstance() {
        if (instance == null) {
            instance = new InMemoryCurrenciesManager();
        }
        
        return instance;
    }
    
    private InMemoryCurrenciesManager() {
        usd = new AmpCurrency();
        usd.setAmpCurrencyId(21L);
        usd.setCurrencyCode("USD");
        usd.setCurrencyName("US Dollar");
        
        eur = new AmpCurrency();
        eur.setAmpCurrencyId(10L);
        eur.setCurrencyCode("EUR");
        eur.setCurrencyName("Euro");
        
        currencies.put(usd.getId(), usd);
        currencies.put(eur.getId(), eur);
    }
    
    public AmpCurrency getUsd() {
        return usd;
    }
    
    public AmpCurrency getEur() {
        return eur;
    }
    
    @Override
    public AmpCurrency get(Long id) {
        return currencies.get(id);
    }
    
    public List<AmpCurrency> getAllValues() {
        return new ArrayList<>(currencies.values());
    }
}
