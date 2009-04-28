package org.digijava.module.widget.dbentity;

import java.util.HashSet;
import java.util.Set;
import org.digijava.module.widget.helper.WidgetVisitor;

public class AmpSectorTableWidget extends AmpWidget {

    private Set<AmpSectorOrder> sectorsColumns;
    private Set<AmpSectorTableYear> totalYears;
    private Set<AmpSectorTableYear> percentYears;
    private Set<AmpSectorTableYear> years;

    public Set<AmpSectorTableYear> getYears() {
        return years;
    }

    public void setYears(Set<AmpSectorTableYear> years) {
        this.years = years;
    }

    public Set<AmpSectorTableYear> getPercentYears() {
        return percentYears;
    }

    public void setPercentYears(Set<AmpSectorTableYear> percentYears) {
        this.percentYears = percentYears;
    }

    public Set<AmpSectorTableYear> getTotalYears() {
        return totalYears;
    }

    public void setTotalYears(Set<AmpSectorTableYear> totalYears) {
        this.totalYears = totalYears;
    }

    public Set<AmpSectorOrder> getSectorsColumns() {
        return sectorsColumns;
    }

    public void setSectorsColumns(Set<AmpSectorOrder> sectorsColumns) {
        this.sectorsColumns = sectorsColumns;
    }

    public void addYear(AmpSectorTableYear year) {
        year.setWidget(this);
        if (year.getType().equals(AmpSectorTableYear.PERCENT_TYPE_YEAR)) {
            if (this.percentYears == null) {
                this.percentYears = new HashSet<AmpSectorTableYear>();
            }
            this.percentYears.add(year);
        } else {
            if (this.totalYears == null) {
                this.totalYears = new HashSet<AmpSectorTableYear>();
            }
            this.totalYears.add(year);
        }
    }

    @Override
    public void accept(WidgetVisitor visitor) {
        visitor.visit(this);
    }
}
