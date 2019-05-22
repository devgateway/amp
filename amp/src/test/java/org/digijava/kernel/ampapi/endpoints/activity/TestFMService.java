package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

/**
 * @author Octavian Ciubotaru
 */
public class TestFMService implements FMService {

    public static final String HIDDEN_FM_PATH = "fm hidden";
    public static final String VISIBLE_FM_PATH = "fm visible";

    private Set<String> disabledPaths;

    public TestFMService() {
        this(ImmutableSet.of(HIDDEN_FM_PATH));
    }

    public TestFMService(Set<String> disabledPaths) {
        this.disabledPaths = disabledPaths;
    }

    @Override
    public boolean isVisible(String fmPath) {
        return !disabledPaths.contains(fmPath);
    }
}
