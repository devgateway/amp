/**
 * 
 */
package org.digijava.module.aim.annotations.reports;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Alex Gartner
 * 
 * Properties from AmpARFilter whos getter method are annottated with this Annotations will be ignored when persisting 
 * filters to db.
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnorePersistence {

}
