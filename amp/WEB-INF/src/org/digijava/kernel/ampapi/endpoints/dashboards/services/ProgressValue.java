package org.digijava.kernel.ampapi.endpoints.dashboards.services;

import java.util.HashMap;
import java.util.Map;

public class ProgressValue {
    private Map<String, Double> amountsByYear = new HashMap<>();

    public Map<String, Double> getAmountsByYear() { return amountsByYear; }
    public void setAmountsByYear(Map<String, Double> value) { this.amountsByYear = value; }
}
