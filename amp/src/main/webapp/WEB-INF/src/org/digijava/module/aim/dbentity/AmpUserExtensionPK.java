package org.digijava.module.aim.dbentity;

import java.io.Serializable;

import org.digijava.kernel.user.User;

/**
 * Primary Key for AmpUserExtension entity.
 * Maybe it is good idea to add team here, and in each team(workspace) user will have different organizations.
 * @author Irakli Kobiashvili
 * @see AmpUserExtension
 *
 */
public class AmpUserExtensionPK implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private User user;

    public AmpUserExtensionPK(){
        
    }

    public AmpUserExtensionPK(User user){
        this.user=user;
    }
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object obj) {
        AmpUserExtensionPK other=(AmpUserExtensionPK)obj;
        return this.user.getId().equals(other.getUser().getId());
    }

    @Override
    public int hashCode() {
        return (this.user!=null && this.user.getId()!=null)?this.user.getId().hashCode():0;
    }
    
    

}
