package org.digijava.kernel.ampapi.endpoints.activity;

/**
 * Added to aid testing. Acts as a bridge to static world of FMVisibility.
 *
 * @author Octavian Ciubotaru
 */
public interface FeatureManagerService {

    boolean isVisible(String fmPath);
}
