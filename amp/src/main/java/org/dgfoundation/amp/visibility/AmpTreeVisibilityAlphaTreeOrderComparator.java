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
public class AmpTreeVisibilityAlphaTreeOrderComparator implements Comparator<Object>, Serializable{

    /**
     * 
     */
    public AmpTreeVisibilityAlphaTreeOrderComparator() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public int compare(Object a, Object b)
    {
        AmpTreeVisibility pairA = (AmpTreeVisibility)a;
        AmpTreeVisibility pairB = (AmpTreeVisibility)b;

        return pairA.getRoot().getName().compareTo(pairB.getRoot().getName());
    }
}
