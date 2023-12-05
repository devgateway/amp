/**
 * 
 */
package org.digijava.module.aim.util;

/**
 * @author mihai
 * Implements objects that can be identified by an unique Id that can be represented as an object.
 */
public interface LoggerIdentifiable extends Identifiable{
    public Object getObjectType();
    public String getObjectName();
        public String getObjectFilteredName();
}
