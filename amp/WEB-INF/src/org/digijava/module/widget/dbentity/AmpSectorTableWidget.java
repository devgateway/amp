package org.digijava.module.widget.dbentity;

import java.util.HashSet;
import java.util.Set;

import org.digijava.module.widget.helper.WidgetVisitor;

public class AmpSectorTableWidget extends AmpWidget {

	private static final long serialVersionUID = 1L;
	private Set<AmpSectorOrder> sectorsColumns;
    private Set<AmpSectorTableYear> years;
    private Long donorYear;

    public Long getDonorYear() {
        return donorYear;
    }

    public void setDonorYear(Long donorYear) {
        this.donorYear = donorYear;
    }

    public Set<AmpSectorTableYear> getYears() {
        return years;
    }

    public void setYears(Set<AmpSectorTableYear> years) {
        this.years = years;
    }


    public Set<AmpSectorOrder> getSectorsColumns() {
        return sectorsColumns;
    }

    public void setSectorsColumns(Set<AmpSectorOrder> sectorsColumns) {
        this.sectorsColumns = sectorsColumns;
    }

    public void addYear(AmpSectorTableYear year) {
        year.setWidget(this);
        if(this.years==null){
            this.years=new HashSet<AmpSectorTableYear>();
        }
        this.years.add(year);
    }

    @Override
    public void accept(WidgetVisitor visitor) {
        visitor.visit(this);
    }
}
