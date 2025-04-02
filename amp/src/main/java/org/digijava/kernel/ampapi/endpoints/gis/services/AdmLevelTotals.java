package org.digijava.kernel.ampapi.endpoints.gis.services;

import java.util.List;

/**
 * @author Octavian Ciubotaru
 */
public class AdmLevelTotals {

    private String currency;

    private String numberformat;

    private List<AdmLevelTotal> values;

    public AdmLevelTotals(String currency, String numberformat,
            List<AdmLevelTotal> values) {
        this.currency = currency;
        this.numberformat = numberformat;
        this.values = values;
    }

    public String getCurrency() {
        return currency;
    }

    public String getNumberformat() {
        return numberformat;
    }

    public List<AdmLevelTotal> getValues() {
        return values;
    }
}
