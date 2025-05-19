package org.digijava.module.um.form;

import org.apache.struts.action.ActionForm;
import org.digijava.kernel.user.User;
import org.digijava.module.um.dbentity.SuspendLogin;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: flyer
 * Date: 10/1/13
 * Time: 4:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class SuspendLoginManagerForm extends ActionForm {
    private List<SuspendLogin> suspendLoginObjects;
    private SuspendLogin currentObj;
    private Long objId;
    private List<User> allUsers;
    private Long[] suspendedUserIDs;

    public List<SuspendLogin> getSuspendLoginObjects() {
        return suspendLoginObjects;
    }

    public void setSuspendLoginObjects(List<SuspendLogin> suspendLoginObjects) {
        this.suspendLoginObjects = suspendLoginObjects;
    }

    public SuspendLogin getCurrentObj() {
        if (currentObj == null) {
            currentObj = new SuspendLogin();
        }
        return currentObj;
    }

    public void setCurrentObj(SuspendLogin currentObj) {
        this.currentObj = currentObj;
    }

    public Long getObjId() {
        return objId;
    }

    public void setObjId(Long objId) {
        this.objId = objId;
    }

    public List<User> getAllUsers() {
        return allUsers;
    }

    public void setAllUsers(List<User> allUsers) {
        this.allUsers = allUsers;
    }

    public Long[] getSuspendedUserIDs() {
        return suspendedUserIDs;
    }

    public void setSuspendedUserIDs(Long[] suspendedUserIDs) {
        this.suspendedUserIDs = suspendedUserIDs;
    }
}
