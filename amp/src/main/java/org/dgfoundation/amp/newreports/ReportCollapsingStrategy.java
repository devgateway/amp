package org.dgfoundation.amp.newreports;

/**
 * class which governs the way same-named subreports are collapsed post-hierarchies-building
 * @author Dolghier Constantin
 *
 */
public enum ReportCollapsingStrategy {
    /** never collapse same-named subreports */
    NEVER,
    
    /** only collapse unknowns (regardless of their names) */
    UNKNOWNS,
    
    /** always collapse same-named subreports*/
    ALWAYS;
}
