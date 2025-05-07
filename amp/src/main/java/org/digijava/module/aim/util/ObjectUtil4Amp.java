/**
 * 
 */
package org.digijava.module.aim.util;

/**
 * @author Alex
 *
 */
public class ObjectUtil4Amp {

    public static int nullSafeIgnoreCaseStringCompare(String first,String second, boolean trim) {
        if ( (first == null) != (second == null) ) {
            return (first == null) ? -1 : 1;
        }

        if (first == null && second == null) {
            return 0;
        }

        if (trim)
            return first.trim().compareToIgnoreCase(second.trim());
        else
            return first.compareToIgnoreCase(second);
    }

}
