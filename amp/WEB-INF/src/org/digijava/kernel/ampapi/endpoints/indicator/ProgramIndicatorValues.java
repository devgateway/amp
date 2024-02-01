package org.digijava.kernel.ampapi.endpoints.indicator;

import java.util.Set;

public class ProgramIndicatorValues {
    private Long id;
    private String name;
    private Set<IndicatorYearValues> indicators;

    public ProgramIndicatorValues(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<IndicatorYearValues> getIndicators() {
        return indicators;
    }

    public void setIndicators(Set<IndicatorYearValues> indicators) {
        this.indicators = indicators;
    }
}
