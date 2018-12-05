package org.dgfoundation.amp.reports.converters;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.dgfoundation.amp.newreports.ReportElement;

/**
 * singleton class used for injecting into a ReportSpecificationImpl the data needed for including the MTEF columns
 * as measures in the Mondrian reports implementation
 *
 * @author Dolghier Constantin
 */
public class MtefConverter {
        
    /**
     * the element types which are MTEF filtering elements (those are special and hacky)
     */
    public final static Set<ReportElement.ElementType> MTEF_DATE_ELEMENT_TYPES = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList(ReportElement.ElementType.MTEF_DATE, ReportElement.ElementType.REAL_MTEF_DATE, 
                    ReportElement.ElementType.PIPELINE_MTEF_DATE, ReportElement.ElementType.PROJECTION_MTEF_DATE)));
}
