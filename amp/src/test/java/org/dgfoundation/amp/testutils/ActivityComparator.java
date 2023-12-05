package org.dgfoundation.amp.testutils;

import org.digijava.module.aim.dbentity.AmpActivityVersion;

import java.util.Comparator;

/**
 * an {@link org.dgfoundation.amp.testutils.ActivityDigest} - mirroring comparator
 * @author simple
 *
 */
public class ActivityComparator implements Comparator<AmpActivityVersion>{
    
    /**
     * please coroborate it with {@link org.dgfoundation.amp.testutils.ActivityDigest#compareTo(ActivityDigest)}
     */
    public int compare(AmpActivityVersion a, AmpActivityVersion b)
    {
        int comp = a.getName().compareTo(b.getName());
        if (comp != 0)
            return comp;
        
        comp = a.getAmpId().compareTo(b.getAmpId());
        if (comp != 0)
            return comp;
        
        return 0;
    }
}
