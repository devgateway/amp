package org.digijava.kernel.ampapi.endpoints.dashboards.services;

import java.util.List;

/**
 * @author Octavian Ciubotaru
 */
public class AidPredictabilityChartData {

    private String name;

    private String title;

    private String currency;

    private String measure;

    private List<AidPredictabilityAmounts> years;

    private AidPredictabilityAmounts totals;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<AidPredictabilityAmounts> getYears() {
        return years;
    }

    public void setYears(List<AidPredictabilityAmounts> years) {
        this.years = years;
    }

    public AidPredictabilityAmounts getTotals() {
        return totals;
    }

    public void setTotals(AidPredictabilityAmounts totals) {
        this.totals = totals;
    }
}
