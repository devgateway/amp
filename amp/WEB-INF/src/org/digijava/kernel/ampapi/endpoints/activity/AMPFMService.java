package org.digijava.kernel.ampapi.endpoints.activity;

import org.digijava.kernel.ampapi.endpoints.activity.visibility.FMVisibility;

/**
 * @author Octavian Ciubotaru
 */
public class AMPFMService implements FMService {

    @Override
    public boolean isVisible(String fmPath) {
        return FMVisibility.isVisible(fmPath);
    }
}
