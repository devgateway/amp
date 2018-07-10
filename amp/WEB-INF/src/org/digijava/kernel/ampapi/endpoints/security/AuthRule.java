/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.security;

/**
 * Usual set of actions that must be authorized in order to execute an API call  
 * 
 * @author Nadejda Mandrescu
 */
public enum AuthRule {
    /** allow execution only for authenticated sessions */
    AUTHENTICATED,
    /** request must be issued within a selected workspace */
    IN_WORKSPACE,
    /** request must be issued by a logged-in admin */
    IN_ADMIN,
    /** add activity right must be enabled */
    ADD_ACTIVITY,
    /** edit activity right must be enabled */
    EDIT_ACTIVITY,
    /** view activity right must be enabled */
    VIEW_ACTIVITY,
    /** amp offline global settings must be enabled */
    AMP_OFFLINE_ENABLED;
    
    @Override
    public String toString() {
        return name();
    }
}
