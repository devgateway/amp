package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

/**
 * @author Octavian Ciubotaru
 */
public class TestFMService implements FeatureManagerService {

    public static final String HIDDEN_FM_PATH = "fm hidden";
    public static final String VISIBLE_FM_PATH = "fm visible";

    private Set<String> allowedPaths;
    private Set<String> disabledPaths;

    public TestFMService() {
        this(ImmutableSet.of(), ImmutableSet.of(HIDDEN_FM_PATH));
    }

    /**
     * @param allowedPaths if not empty then only specified paths are allowed, otherwise any path is allowed
     * @param disabledPaths disabled fm paths, always enforced
     */
    public TestFMService(Set<String> allowedPaths, Set<String> disabledPaths) {
        this.allowedPaths = allowedPaths;
        this.disabledPaths = disabledPaths;
    }

    @Override
    public boolean isVisible(String fmPath) {
        return (allowedPaths.isEmpty() || allowedPaths.contains(fmPath))
                && !disabledPaths.contains(fmPath);
    }
}
