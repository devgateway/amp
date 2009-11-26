package org.digijava.module.widget.dbentity;

import java.util.ArrayList;
import java.util.List;

import org.digijava.module.widget.helper.WidgetVisitor;

public class AmpParisIndicatorTableWidget extends AmpWidget {

	private static final long serialVersionUID = 1L;
	private Long donorGroupYear;
    private List<AmpParisIndicatorBaseTargetValues> parisIndicators;

    public List<AmpParisIndicatorBaseTargetValues> getParisIndicators() {
        return parisIndicators;
    }

    public void setParisIndicators(List<AmpParisIndicatorBaseTargetValues> parisIndicators) {
        this.parisIndicators = parisIndicators;
    }

    public Long getDonorGroupYear() {
        return donorGroupYear;
    }

    public void setDonorGroupYear(Long donorGroupYear) {
        this.donorGroupYear = donorGroupYear;
    }


    public void addParisIndicatorBaseTarget(AmpParisIndicatorBaseTargetValues indicator){
        indicator.setWidget(this);
        if(this.parisIndicators==null){
            this.parisIndicators=new ArrayList<AmpParisIndicatorBaseTargetValues>();
        }
        this.parisIndicators.add(indicator);
    }

    @Override
    public void accept(WidgetVisitor visitor) {
        visitor.visit(this);
    }
}
