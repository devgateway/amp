package org.digijava.kernel.ampapi.endpoints.dashboards.services;

import java.util.List;

import org.digijava.module.aim.dbentity.AmpColorThreshold;

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
