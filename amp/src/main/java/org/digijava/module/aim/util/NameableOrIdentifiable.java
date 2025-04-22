/**
 * 
 */
package org.digijava.module.aim.util;

/**
 * Implements both {@link Nameable} and {@link Identifiable} interfaces <br>
 * In Java 8 we can avoid this type of interface that is needed for generics and replace with "&lt? extends Nameable & Identifiable&gt"
 * @author Nadejda Mandrescu
 *
 */
public interface NameableOrIdentifiable extends Nameable, Identifiable{
}
