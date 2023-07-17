/*
 *  AmpTeamMemberRoles.java
 *  @Author Priyajith C
 *  Created: 13-Aug-2004
 */

package org.digijava.module.aim.dbentity;

import java.io.Serializable;

import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import javax.persistence.*;

@Entity
@Table(name = "AMP_TEAM_MEMBER_ROLES")
public class AmpTeamMemberRoles implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_team_member_roles_seq_generator")
    @SequenceGenerator(name = "amp_team_member_roles_seq_generator", sequenceName = "AMP_TEAM_MEMBER_ROLES_seq", allocationSize = 1)
    @Column(name = "amp_team_mem_role_id")
    private Long ampTeamMemRoleId;

    @Column(name = "role")
    private String role;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "team_head")
    private Boolean teamHead;

    @Column(name = "approver")
    private boolean approver;
    
    /**
     * @return ampTeamMemRoleId
     */
    public Long getAmpTeamMemRoleId() {
        return ampTeamMemRoleId;
    }

    /**
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return role
     */
    public String getRole() {
        return role;
    }

    /**
     * @param ampTeamMemRoleId
     */
    public void setAmpTeamMemRoleId(Long ampTeamMemRoleId) {
        this.ampTeamMemRoleId = ampTeamMemRoleId;
    }

    /**
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @param role
     */
    public void setRole(String role) {
        this.role = role;
    }


    public Boolean getTeamHead() {
        return teamHead;
    }

    public void setTeamHead(Boolean teamHead) {
        this.teamHead = teamHead;
    }
    
    /**
     * @return the translation key used
     */
    public String getAmpTeamMemberKey () {
        return 
            getAmpTeamMemberKey( this.role );
    }
    
    public static String getAmpTeamMemberKey ( String roleName ) {
        String asciiName    = CategoryManagerUtil.asciiStringFilter(roleName);
        return 
            "aim:AmpTeamMemeberRoleTrnKey:" + asciiName;
    }

    public void setApprover(boolean approver) {
        this.approver = approver;
    }

    public boolean isApprover() {
        return approver;
    }

}
