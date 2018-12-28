package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.Deque;

import org.digijava.module.aim.annotations.interchange.Interchangeable;

/**
 * @author Octavian Ciubotaru
 */
public class TestFMService implements FMService {

    @Override
    public boolean isVisible(String fmPath, Deque<Interchangeable> intchStack) {
        return !fmPath.equals("fm hidden");
    }
}
