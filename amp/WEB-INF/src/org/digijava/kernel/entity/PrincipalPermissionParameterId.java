package org.digijava.kernel.entity;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class PrincipalPermissionParameterId implements Serializable {

    private PrincipalPermission principalPermission;

    private int index;

    // Constructors, getters, and setters

    public PrincipalPermissionParameterId() {
    }

    public PrincipalPermissionParameterId(PrincipalPermission principalPermission, int index) {
        this.principalPermission = principalPermission;
        this.index = index;
    }

    // Getters and Setters (generated by IDE or manually implemented)

    public PrincipalPermission getPrincipalPermission() {
        return principalPermission;
    }

    public void setPrincipalPermission(PrincipalPermission principalPermission) {
        this.principalPermission = principalPermission;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    // Implement equals() and hashCode() methods (generated by IDE or manually implemented)

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PrincipalPermissionParameterId)) return false;
        PrincipalPermissionParameterId that = (PrincipalPermissionParameterId) o;
        if (getIndex() != that.getIndex()) return false;
        return getPrincipalPermission() != null ? getPrincipalPermission().equals(that.getPrincipalPermission()) : that.getPrincipalPermission() == null;
    }

    @Override
    public int hashCode() {
        int result = getPrincipalPermission() != null ? getPrincipalPermission().hashCode() : 0;
        result = 31 * result + getIndex();
        return result;
    }
}
