

package org.digijava.module.aim.helper;

import java.util.Comparator;

import org.digijava.module.aim.dbentity.AmpIndicatorValue;

/**
 *
 * @author medea
 */
public class IndicatorValuesComparator implements Comparator<AmpIndicatorValue> {

    public int compare(AmpIndicatorValue o1, AmpIndicatorValue o2) {
        int compare = 0;
        if (o1.getValueDate() != null && o2.getValueDate() != null) {
            compare = o1.getValueDate().compareTo(o2.getValueDate());
            if (compare == 0) {
                /* If dates are equal we must compare types, 
                 * order should be following:
                 * Base value (2)
                 * Actual value (1)
                 * Target value (0)
                 */
                if (o1.getValueType() > o2.getValueType()) {
                    compare = -1;
                } else {
                    if (o1.getValueType() < o2.getValueType()) {
                        compare = 1;
                    }
                }
            }
        }


        return compare;
    }

  
}
