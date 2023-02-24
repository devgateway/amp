package org.dgfoundation.amp.nireports.schema;

import java.util.Arrays;

import org.dgfoundation.amp.nireports.runtime.ColumnReportData;

/**
 * NiReports' hardcoded boolean dimension, used for yes/no/undefined columns
 * @author Dolghier Constantin
 *
 */
public class BooleanDimension extends ConstantNiDimension {
    
    public BooleanDimension(String name, long yesValue, long noValue) {
        super(name, 1, Arrays.asList(Arrays.asList(yesValue), Arrays.asList(noValue), 
                Arrays.asList(ColumnReportData.UNALLOCATED_ID)));
    }
}
