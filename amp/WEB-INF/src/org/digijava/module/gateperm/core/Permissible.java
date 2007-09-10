/**
 * Permissible.java
 * (c) 2007 Development Gateway Foundation
 */
package org.digijava.module.gateperm.core;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.gateperm.util.PermissionUtil;

/**
 * Permissible.java
 * TODO description here
 * @author mihai
 * @package org.digijava.module.gateperm.core
 * @since 29.08.2007
 */
public abstract class Permissible implements Identifiable {
	private static Logger logger = Logger.getLogger(Permissible.class);


	/**
	 * Returns a set of actions that this Permissible object implements. An object may not need all possible actions that a permission may allow.
	 * It may only need a subset of them
	 * @return
	 */
	public abstract Set getImplementedActions();
		
	
	
	/**
	 * @return the object category to identify specific permission objects. This is usually a constant stored in Permissible
	 * and it usually represents its Class.
	 */
	public abstract Class getPermissibleCategory();
	
	/**
	 * Gets the list of linked permissions with this object. Queries the permissions to produce
	 * a set of unique allowed actions for this object. 
	 * @param scope a map with the scope of the application (various variables like request, session
	 * or parts of request, session  - currentMember, etc... that are relevant for 
	 * the Gate logic
	 * All the actions that are not implemented by the Permissible object will be retained.
	 * @see getImplementedActions()
	 * @return the collection of unique allowed actions
	 */
	public Collection<String> getAllowedActions(Map scope) {
		Set<String> actions= new TreeSet<String>();
		Collection<Permission> permissionsForObjectOfCategory = PermissionUtil.getPermissionsForObjectOfCategory(this.getIdentifier(), this.getPermissibleCategory().getName());
		Iterator i=permissionsForObjectOfCategory.iterator();
		while (i.hasNext()) {
			Permission element = (Permission) i.next();
			actions.addAll(element.getAllowedActions(scope));
		}
		logger.debug("Actions allowed for object "+this.getIdentifier()+" of type "+this.getClass().getSimpleName()+" are "+actions);
		Set implementedActions = getImplementedActions();
		actions.retainAll(implementedActions);
		return actions;
	}
	

}
