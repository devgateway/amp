package org.digijava.kernel.ampapi.endpoints.dashboards.services;

import org.digijava.module.aim.dbentity.AmpColorThreshold;

import java.util.List;

/**
 * @author Octavian Ciubotaru
 */
public class AmpColorThresholdWrapper {

    private List<AmpColorThreshold> amountColors;

    public List<AmpColorThreshold> getAmountColors() {
        return amountColors;
    }

    public void setAmountColors(List<AmpColorThreshold> amountColors) {
        this.amountColors = amountColors;
    }
}
