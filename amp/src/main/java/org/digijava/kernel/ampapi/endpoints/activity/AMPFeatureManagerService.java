package org.digijava.kernel.ampapi.endpoints.activity;

import org.digijava.kernel.ampapi.endpoints.activity.visibility.FMVisibility;

/**
 * @author Octavian Ciubotaru
 */
public class AMPFeatureManagerService implements FeatureManagerService {
    
    private FMVisibility fmVisibility;
    
    public AMPFeatureManagerService() {
        this.fmVisibility = new FMVisibility();
    }
    
    public AMPFeatureManagerService(FMVisibility fmVisibility) {
        this.fmVisibility = fmVisibility;
    }
    
    @Override
    public boolean isVisible(String fmPath) {
        return fmVisibility.isVisible(fmPath);
    }

    public FMVisibility getFmVisibility() {
        return fmVisibility;
    }

}
