package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.Deque;

import org.digijava.module.aim.annotations.interchange.Interchangeable;

/**
 * @author Octavian Ciubotaru
 */
public class TestFMService implements FMService {

    public static final String HIDDEN_FM_PATH = "fm hidden";
    public static final String VISIBLE_FM_PATH = "fm visible";

    @Override
    public boolean isVisible(String fmPath, Deque<Interchangeable> intchStack) {
        return !fmPath.equals(HIDDEN_FM_PATH);
    }
}
