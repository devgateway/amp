/**
 * 
 */
package org.dgfoundation.amp.visibility;

import java.io.Serializable;
import java.util.Comparator;

/**
 * @author dan
 *
 */
public class AmpTreeVisibilityAlphaOrderComparator implements Comparator<Object> , Serializable{

    /**
     * 
     */
    public AmpTreeVisibilityAlphaOrderComparator() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public int compare(Object a, Object b)
    {
        AmpObjectVisibility pairA = (AmpObjectVisibility)a;
        AmpObjectVisibility pairB = (AmpObjectVisibility)b;

        return pairA.getName().compareTo(pairB.getName());
    }

}
