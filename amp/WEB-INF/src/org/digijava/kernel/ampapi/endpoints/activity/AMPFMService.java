package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.Deque;

import org.digijava.kernel.ampapi.endpoints.activity.visibility.FMVisibility;
import org.digijava.module.aim.annotations.interchange.Interchangeable;

/**
 * @author Octavian Ciubotaru
 */
public class AMPFMService implements FMService {

    @Override
    public boolean isVisible(String fmPath, Deque<Interchangeable> intchStack) {
        return FMVisibility.isVisible(fmPath, intchStack);
    }
}
