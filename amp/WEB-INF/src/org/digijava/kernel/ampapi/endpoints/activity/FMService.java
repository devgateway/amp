package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.Deque;

import org.digijava.module.aim.annotations.interchange.Interchangeable;

/**
 * Added to aid testing. Acts as a bridge to static world of FMVisibility.
 *
 * @author Octavian Ciubotaru
 */
public interface FMService {

    boolean isVisible(String fmPath, Deque<Interchangeable> intchStack);
}
