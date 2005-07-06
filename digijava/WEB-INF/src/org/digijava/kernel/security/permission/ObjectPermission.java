package org.digijava.kernel.security.permission;

import java.security.Permission;

import org.digijava.kernel.security.ResourcePermission;
import java.io.Serializable;

public class ObjectPermission
    extends ResourcePermission implements Serializable {

    private static final long serialVersionUID = 1;

    protected Object instanceId;
    protected String className;

    /**
     *
     * <p><b>Impotant:</b> the <code>instanceId</code> object must have
     * <code>equals ( Object obj )</code> method reloaded and working according
         * to the logic appropriate for the object for which ObjectPermission is used.
     *
         * @param className name of the class for which ObjectPermission is used.<p>
     * @param instanceId primary-key object for the instance of the class for
     * which <code>ObjectPermission</code> is used. This InstanceId must be a
         * separate object with a reasonably overriden equals() method (in most cases,
     * though, this id will be Long or String which already have reasonable
     * equals() method).
     *
         * <p><b>Important:</b> If instanceId = null is passed to the ObjectPermission
         * this means that such ObjectPermission grants privilege on all instances of
         * the specified class. This allows granting permission on the whole class of
     * objects and is a useful feature.<p>
     *
     * @param actions comma-delimited list of actions of the permission.
     */
    public ObjectPermission(String className, Object instanceId, String actions) {
        super(className, actions);
        this.instanceId = instanceId;
        this.className = className;
    }

    /**
         * Constructor for an ObjectPermission which grants permission on any instace
     * of a class (class-level permission). In this case instanceId=null.
         * @param className name of the class for which ObjectPermission is used.<p>
     * @param actions comma-delimited list of actions of the permission.
     */
    public ObjectPermission(String className, String actions) {
        this(className, null, actions);
    }

    public ObjectPermission(String className, Object instanceId, int action) {
        super(className, action);
        this.instanceId = instanceId;
        this.className = className;
    }
    public ObjectPermission(String className, Object instanceId, Integer action) {
        this(className, instanceId, action.intValue());
    }

    public ObjectPermission(String className, int action) {
        this(className, null, action);
    }

    /**
     *
     * @param permission
     * @return
     */
    public boolean implies(Permission permission) {
        boolean retVal = false;
        if (permission instanceof ObjectPermission) {
            ObjectPermission otherPerm =
                (ObjectPermission) permission;
            retVal = this.getName().equals(otherPerm.getName()) &&
                ( (this.actionMask | otherPerm.actionMask) ==
                 this.actionMask);

            // ObjectPermission with instanceId = null grants permission
            // on any instance of the class.
            if (this.getInstanceId() != null) {
                if (otherPerm.getInstanceId() == null)
                    return false;

                retVal = retVal && otherPerm.getInstanceId().equals(
                    this.getInstanceId());
            }
        }

        return retVal;
    }

    /**
     *
     * @param obj
     * @return
     */
    public boolean equals(Object obj) {
        boolean retVal = false;

        if (obj instanceof ObjectPermission) {
            ObjectPermission otherPerm = (ObjectPermission) obj;
            retVal = this.getName().equals(otherPerm.getName()) &&
                this.actionMask == otherPerm.actionMask;

            if (otherPerm.getInstanceId() == null ||
                this.getInstanceId() == null)
                return false;

            retVal = retVal && this.getInstanceId().equals(
                otherPerm.getInstanceId());
        }

        return retVal;
    }

    public Object getInstanceId() {
        return this.instanceId;
    }

    public String getClassName() {
        return this.className;
    }

    /**
     * Returns array of objects, which will be persisted into database and then
     * used to reconstruct this permission object. Values are sorted by types
     * and order of constructor's parameter
     * @return array of objects
     */
    public Object[] getParameters() {
        return new Object[] { className, instanceId, new Integer(actionMask)};
    }

}