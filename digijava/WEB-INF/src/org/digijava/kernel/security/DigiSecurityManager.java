package org.digijava.kernel.security;

import java.security.AccessControlException;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;
import java.security.Principal;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.security.auth.Subject;

import org.digijava.kernel.exception.DgException;
import java.util.*;
import org.digijava.kernel.security.principal.GroupPrincipal;
import org.digijava.kernel.security.principal.UserPrincipal;
import net.sf.hibernate.Session;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.user.User;
import org.digijava.kernel.user.Group;
import org.apache.log4j.Logger;
import org.digijava.kernel.util.UserUtils;

public class DigiSecurityManager {
    private static Logger logger = Logger.getLogger(DigiSecurityManager.class);

    /**
     *
     */
    public DigiSecurityManager() {
    }

    private static DigiPolicy getDigiPolicy() {
	Policy currentPolicy = (DigiPolicy) Policy.getPolicy();

	if (! (currentPolicy instanceof DigiPolicy)) {
	    throw new RuntimeException(
		  "DigiPolicy policy is not installed in system");
	}

	DigiPolicy policy = (DigiPolicy) currentPolicy;
	return policy;
    }

    /**
     * @param principal
     * @param permission
     */
    public static void grantPermission(Principal principal,
				       Permission permission) throws
	  DgException {
	DigiPolicy policy = getDigiPolicy();
	policy.grant(principal, permission);
    }

    public static void grantPermission(User user,
				       Permission permission) throws
	  DgException {
	DigiPolicy policy = getDigiPolicy();
	UserPrincipal userPrincipal = new UserPrincipal(user);

	grantPermission(userPrincipal, permission);
    }

    public static void grantPermission(Group group,
				       Permission permission) throws
	  DgException {
	DigiPolicy policy = getDigiPolicy();
	GroupPrincipal groupPrincipal = new GroupPrincipal(group);

	grantPermission(groupPrincipal, permission);
    }

    public static void revokePermission(Principal principal,
					Permission permission) throws
	  DgException {
	DigiPolicy policy = getDigiPolicy();
	policy.revoke(principal, permission);
    }

    public static void revokePermission(User user,
					Permission permission) throws
	  DgException {
	UserPrincipal userPrincipal = new UserPrincipal(user);

	revokePermission(userPrincipal, permission);
    }

    public static void revokePermission(Group group,
					Permission permission) throws
	  DgException {
	GroupPrincipal groupPrincipal = new GroupPrincipal(group);

	revokePermission(groupPrincipal, permission);
    }

    /**
     * Attach principal to the subject
     * @param subject Subject
     * @param principal Principal
     * @todo rewrite this method to work with DB
     */
    public static void attachPrincipal(Subject subject, Principal principal) {
	throw new UnsupportedOperationException(
	      "attachPrincipal() is not implemented");
	/*
		 subject.getPrincipals().add(principal);
		 Long userId = null;
	       Iterator iter = subject.getPrincipals(UserPrincipal.class).iterator();
		 while (iter.hasNext()) {
	    UserPrincipal item = (UserPrincipal)iter.next();
	    userId = new Long(item.getUserId());
		 }
		 if (userId == null) {
return;
		 }
		 logger.debug("Attaching principals to user #" + userId);
	 */
	/**
	 * @todo implement
	 */
	/*
	     Set subjects = (Set)principalToSubjects.get(principal);
	     if (subjects == null) {
	  subjects = new HashSet();
	     }
	     subjects.add(subject);
	     principalToSubjects.put(principal, subjects);
	 */
    }

    /**
     * Detach principal from the subject
     * @param subject Subject
     * @param principal Principal
     * @todo rewrite this method to work with DB
     */
    public static void detachPrincipal(Subject subject, Principal principal) {
	throw new UnsupportedOperationException(
	      "detachPrincipal() is not implemented");
	//subject.getPrincipals().remove(principal);
	/**
	 * @todo implement
	 */

	/*
	     Set subjects = (Set)principalToSubjects.get(principal);
	     if (subjects != null) {
	  subjects.remove(subject);
	     }
	 */

    }

    public static boolean checkPermission(Principal principal,
					  Permission permission) {

	Set principals = new HashSet();
	principals.add(principal);
	final Subject subject = new Subject(true, principals, new HashSet(),
					    new HashSet());

	return checkPermission(subject, permission);
    }

    /**
     *
     * @param subject
     * @param permission
     * @return
     */
    public static boolean checkPermission(Subject subject,
					  Permission permission) {

	final Permission localPerm = permission;
	try {
	    Subject.doAsPrivileged(subject, new PrivilegedExceptionAction() {
		public Object run() {
		    final SecurityManager sm;

		    if (System.getSecurityManager() == null) {
			sm = new SecurityManager();
		    }
		    else {
			sm = System.getSecurityManager();
		    }
		    sm.checkPermission(localPerm);
		    return null;
		}
	    }

	    , null);
	    return true;
	}
	catch (AccessControlException ace) {
        if (logger.isDebugEnabled()) {
            logger.debug("Subject " + subject +" got AccessControlException for " + permission);
        }
	    return false;
	}
	catch (PrivilegedActionException pae) {
        if (logger.isDebugEnabled()) {
            logger.debug("Subject " + subject +" got PrivilegedActionException for " + permission);
        }
	    return false;
	}
    }

    public static Set getPrincipals(Permission permission) {
	return getPrincipals(permission, null);
    }

    public static Set getPrincipals(Permission permission, Class principalClass) {
	HashSet principals = new HashSet();
	Map privileges = getDigiPolicy().getPrivileges();

	Iterator iter = privileges.entrySet().iterator();
	while (iter.hasNext()) {
	    Map.Entry item = (Map.Entry) iter.next();
	    Principal principal = (Principal) item.getKey();

	    if (principalClass == null ||
		(principalClass != null &&
		 principalClass.isAssignableFrom(principal.getClass()))) {
		Permissions permissions = (Permissions) item.getValue();

		if (permissions.implies(permission)) {
		    principals.add(principal);
		}
	    }
	}

	return principals;
    }

    public static PermissionCollection getPermissions(Principal principal) {
	return (PermissionCollection) getDigiPolicy().getPrivileges().get(
	      principal);
    }

    public static void removePrincipals(Set principals) throws DgException {
	getDigiPolicy().removePrincipals(principals);
    }

    public static Set getUsers(Permission permission) throws DgException {
	TreeSet result = new TreeSet(new Comparator() {
	    public int compare(Object o1, Object o2) {
		return ( (User) o1).getId().compareTo( ( (User) o2).getId());
	    }
	}
	);
	Set principals = getDigiPolicy().getPrincipals(permission);
	if (principals == null) {
	    logger.debug("No principals found for " + permission);
	    return new HashSet();
	}
	/**
	 * @todo take into account query size limitation in SQL
	 */
	StringBuffer groupsBuffer = new StringBuffer(512);
	StringBuffer usersBuffer = new StringBuffer(512);
	boolean firstUser = true;
	boolean firstGroup = true;

	Iterator iter = principals.iterator();
	while (iter.hasNext()) {
	    Principal item = (Principal) iter.next();

	    if (item instanceof GroupPrincipal) {
		GroupPrincipal gp = (GroupPrincipal) item;
		if (!firstGroup) {
		    groupsBuffer.append(',');
		}
		else {
		    firstGroup = false;

		}
		groupsBuffer.append(gp.getGroupId());
	    }
	    else
	    if (item instanceof UserPrincipal) {
		UserPrincipal up = (UserPrincipal) item;
		if (!firstUser) {
		    usersBuffer.append(',');
		}
		else {
		    firstUser = false;
		}
		usersBuffer.append(up.getUserId());
	    }

	}
	if (! (firstUser && firstGroup)) {
	    Session session = null;
	    String userQuery = null;
	    String groupQuery = null;
	    if (!firstUser) {
		userQuery = "from " + User.class.getName() +
		      " u where u.id in (" +
		      usersBuffer.toString() + ")";
	    }
	    if (!firstGroup) {
		groupQuery = "from " + Group.class.getName() +
		      " g where g.id in (" + groupsBuffer.toString() + ")";

	    }
	    try {
		session = PersistenceManager.getSession();

		if (userQuery != null) {
		    result.addAll(session.find(userQuery));
		}
		if (groupQuery != null) {
		    List list = session.find(groupQuery);
		    iter = list.iterator();
		    while (iter.hasNext()) {
			Group group = (Group) iter.next();

			if (group.getUsers() != null) {
			    result.addAll(group.getUsers());
			}
		    }
		}
	    }
	    catch (Exception ex) {
		throw new DgException("Unabla to get user list", ex);
	    }
	    finally {
		if (session != null) {
		    try {
			PersistenceManager.releaseSession(session);
		    }
		    catch (Exception ex) {
		    }
		}
	    }

	}
	return result;
    }

    /**
     * @deprecated do not use this method.
     * @param principal
     * @param newPermissions
     * @throws DgException
     */
    public static void setPrincipalPermissions(Principal principal,
					       Collection newPermissions) throws
	  DgException {
	HashSet classes = new HashSet();
	Iterator iter = newPermissions.iterator();
	while (iter.hasNext()) {
	    Object item = (Object) iter.next();
	    classes.add(item.getClass());
	}

	Class permissionClasses[] = new Class[classes.size()];
	int i = 0;
	iter = classes.iterator();
	while (iter.hasNext()) {
	    Class item = (Class) iter.next();
	    permissionClasses[i] = item;
	    i++;
	}
	setPrincipalPermissions(principal, newPermissions, permissionClasses);
    }

    public static void setPrincipalPermissions(Principal principal,
					       Collection newPermissions, Class[] permissionClassesArr) throws
	  DgException {
	PermissionCollection permissions = getPermissions(principal);

	HashSet toDelete = new HashSet();
	HashSet toAdd = new HashSet(newPermissions);
	HashSet permissionClasses = new HashSet();

	for (int i = 0; i < permissionClassesArr.length; i++) {
	    permissionClasses.add(permissionClassesArr[i]);
	}

	if (permissions != null) {
	    Enumeration enumElements = permissions.elements();
	    while (enumElements.hasMoreElements()) {
		Permission item = (Permission) enumElements.nextElement();
		if (toAdd.contains(item)) {
		    toAdd.remove(item);
		}
		else {
		    if (permissionClasses.contains(item.getClass())) {
			toDelete.add(item);
		    }
		}
	    }
	}

	Iterator iter = toAdd.iterator();
	while (iter.hasNext()) {
	    Permission item = (Permission) iter.next();
	    grantPermission(principal, item);
	}

	iter = toDelete.iterator();
	while (iter.hasNext()) {
	    Permission item = (Permission) iter.next();
		revokePermission(principal, item);
	}
    }

    public static void printPermissions(Principal principal) {
	Permissions ps =
	      (Permissions) getDigiPolicy().getPrivileges().get(principal);

	printPermissions(ps);
    }

    public static void printPermissions(PermissionCollection ps) {
	if (ps != null) {
	    Enumeration e = ps.elements();

	    logger.debug("====================");
	    while (e.hasMoreElements()) {
		Permission p = (ResourcePermission) e.nextElement();
		logger.debug(p.getName() + "  == " + p.getClass().getName() +
				   " -- " +
				   ( (ResourcePermission) p).getActionMask());
	    }
	    logger.debug("<====================>");
	}
	else {
	}

    }

}
