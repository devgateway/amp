package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.Deque;

import org.digijava.kernel.ampapi.endpoints.activity.visibility.FMVisibility;
import org.digijava.module.aim.annotations.interchange.Interchangeable;

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
    public boolean isVisible(String fmPath, Deque<Interchangeable> intchStack) {
        return fmVisibility.isVisible(fmPath, intchStack);
    }

    public FMVisibility getFmVisibility() {
        return fmVisibility;
    }

}
