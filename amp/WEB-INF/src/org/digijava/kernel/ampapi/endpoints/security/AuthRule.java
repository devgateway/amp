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
    /** amp offline global settings must be enabled and client version must be compatible */
    AMP_OFFLINE,
    /** if amp offline user-agent is present in headers check for AMP_OFFLINE. If not, check for other actions */
    AMP_OFFLINE_OPTIONAL,
    /** Current rule: If activity was created in private ws, it can only be access from there **/
    PUBLIC_VIEW_ACTIVITY;
    
    @Override
    public String toString() {
        return name();
    }
}
