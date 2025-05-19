package org.digijava.module.aim.util.caching;

import org.digijava.module.aim.dbentity.AmpCurrency;

import java.util.List;

/**
 * reset it by overwriting (AmpCaching.currencyCache = new CurrencyCache())
 * @author Dolghier Constantin
 *
 */
public class CurrencyCache 
{
//  public Map<String, Boolean> currencyHasRate;
    public List<AmpCurrency> activeCurrencies;
    
    public CurrencyCache() {reset();}
    
    public void reset()
    {
//      currencyHasRate = new HashMap<String, Boolean>();
        activeCurrencies = null;
    }
}
