package org.digijava.module.aim.form;

import java.util.Collection;
import java.util.List;
import org.digijava.module.aim.helper.TeamMember;

import org.digijava.module.um.form.UserProfileForm;

public class UserDetailForm extends UserProfileForm
{
    
    private Collection teamMemberDetails;
    private String teamName;
    private String roleName;
    private String address;
    private int count;
        private List<TeamMember> teamMemberTeamHelpers;

    
    /**
     * @return Returns the count.
     */
    public int getCount() {
        return count;
    }
    /**
     * @param count The count to set.
     */
    public void setCount(int count) {
        this.count = count;
    }
    
    
    /**
     * @return Returns the teamMemberDetails.
     */
    public Collection getTeamMemberDetails() {
        return teamMemberDetails;
    }
    /**
     * @param teamMemberDetails The teamMemberDetails to set.
     */
    public void setTeamMemberDetails(Collection teamMemberDetails) {
        this.teamMemberDetails = teamMemberDetails;
    }
    
    /**
     * @return Returns the address.
     */
    public String getAddress() {
        return address;
    }
    /**
     * @param address The address to set.
     */
    public void setAddress(String address) {
        this.address = address;
    }
    /**
     * @return Returns the roleName.
     */
    public String getRoleName() {
        return roleName;
    }
    /**
     * @param roleName The roleName to set.
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    /**
     * @return Returns the teamName.
     */
    public String getTeamName() {
        return teamName;
    }
        /**
     * @param teamName The teamName to set.
     */
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

        public List<TeamMember> getTeamMemberTeamHelpers() {
            return teamMemberTeamHelpers;
        }

        public void setTeamMemberTeamHelpers(List<TeamMember> teamMemberTeamHelpers) {
            this.teamMemberTeamHelpers = teamMemberTeamHelpers;
        }
    
    /**
     * @return Returns the teamMemberDetails.
     */
}
