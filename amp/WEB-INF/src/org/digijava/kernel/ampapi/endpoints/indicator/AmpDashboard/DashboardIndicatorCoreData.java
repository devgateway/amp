package org.digijava.kernel.ampapi.endpoints.indicator.AmpDashboard;

import java.util.List;

public class DashboardIndicatorCoreData {
    private String country;
    private String donor;
    private String pillar;
    private List<DashboardCoreIndicatorValue> values;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDonor() {
        return donor;
    }

    public void setDonor(String donor) {
        this.donor = donor;
    }

    public String getPillar() {
        return pillar;
    }

    public void setPillar(String pillar) {
        this.pillar = pillar;
    }

    public List<DashboardCoreIndicatorValue> getValues() {
        return values;
    }

    public void setValues(List<DashboardCoreIndicatorValue> values) {
        this.values = values;
    }
}
