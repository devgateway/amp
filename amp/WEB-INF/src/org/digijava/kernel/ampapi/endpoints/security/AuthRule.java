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
	/** API Token must be action authorized, no special rule to fulfill */
	TOKEN, 
	/** request must be issued within a selected workspace */
	IN_WORKSPACE,
	/** add activity right must be enabled */
	ADD_ACTIVITY,
	/** edit activity right must be enabled */
	EDIT_ACTIVITY,
	/** view activity right must be enabled */
	VIEW_ACTIVITY,
	/** action authorized, no special rule to fulfill */
	NONE;
	
	@Override
	public String toString() {
		return name();
	}
}
