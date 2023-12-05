
package org.digijava.module.aim.form;

import java.util.Collection;

public class UpdateTeamMembersForm extends TeamMemberForm {

    private Long ampTeamId = null;
    private String email = null;
    private String name = null;
    private Long role = null;
    private Collection ampRoles = null;
    private String readPerms = null;
    private String writePerms = null;
    private String deletePerms = null;
    private String permissions = null;
    private Long userId = null;

    public Long getAmpTeamId() {
        return ( this.ampTeamId );
    }

    public void setAmpTeamId( Long ampTeamId ) {
        this.ampTeamId = ampTeamId;
    }

    public String getEmail() {
        return ( this.email );
    }

    public void setEmail( String email ) {
        this.email = email;
    }

    public Long getRole() {
        return ( this.role );
    }

    public void setRole( Long role ) {
        this.role = role;
    }

    public Collection getAmpRoles() {
        return ( this.ampRoles );
    }

    public void setAmpRoles( Collection ampRoles ) {
        this.ampRoles = ampRoles;
    }

    public String getReadPerms() {
        return ( this.readPerms );
    }

    public void setReadPerms( String readPerms ) {
        this.readPerms = readPerms;
    }

    public String getWritePerms() {
        return ( this.writePerms );
    }

    public void setWritePerms( String writePerms ) {
        this.writePerms = writePerms;
    }

    public String getDeletePerms() {
        return ( this.deletePerms );
    }

    public void setDeletePerms( String deletePerms ) {
        this.deletePerms = deletePerms;
    }

    public String getPermissions() {
        return ( this.permissions );
    }

    public void setPermissions( String permissions ) {
        this.permissions = permissions;
    }

    public String getName() {
        return ( this.name );
    }

    public void setName(String name)  {
        this.name = name;
    }   

    public Long getUserId() {
        return (this.userId);
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
