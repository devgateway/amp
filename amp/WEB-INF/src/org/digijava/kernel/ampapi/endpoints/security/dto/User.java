/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.security.dto;

import java.util.Set;
import java.util.SortedSet;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.security.SecurityConstants;

/**
 * Basic user information
 * 
 * @author Nadejda Mandrescu
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class User {
    @JsonProperty(EPConstants.ID)
    private Long id;
    
    @JsonProperty(SecurityConstants.FIRST_NAME)
    private String firstName;
    
    @JsonProperty(SecurityConstants.LAST_NAME)
    private String lastName;
    
    @JsonProperty(SecurityConstants.EMAIL)
    private String email;
    
    @JsonProperty(SecurityConstants.PASSWORD_CHANGED_AT)
    private String passwordChangedAt;
    
    @JsonProperty(SecurityConstants.IS_BANNED)
    private boolean isBanned;
    
    @JsonProperty(SecurityConstants.IS_PLEDGER)
    private boolean isPledger;
    
    @JsonProperty(SecurityConstants.IS_ADMIN)
    private boolean isAdmin;
    
    @JsonProperty(SecurityConstants.LANG_ISO2)
    private String langIso2;
    
    @JsonProperty(SecurityConstants.COUNTRY_ISO2)
    private String countryIso2;
    
    @JsonProperty(SecurityConstants.ORG_TYPE_ID)
    private Long orgTypeId;
    @JsonProperty(SecurityConstants.ORG_GROUP_ID)
    private Long orgGroupId;
    @JsonProperty(SecurityConstants.ORG_ID)
    private Long orgId;
    
    @JsonProperty(SecurityConstants.ASSIGNED_ORG_IDS)
    private SortedSet<Long> assignedOrgIds;
    
    @JsonProperty(SecurityConstants.GROUP_KEYS)
    private SortedSet<String> groupKeys;
    
    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }
    
    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }
    
    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }
    
    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * @return the passwordChangedAt
     */
    public String getPasswordChangedAt() {
        return passwordChangedAt;
    }

    /**
     * @param passwordChangedAt the passwordChangedAt to set
     */
    public void setPasswordChangedAt(String passwordChangedAt) {
        this.passwordChangedAt = passwordChangedAt;
    }

    /**
     * @return the isBanned
     */
    public boolean getIsBanned() {
        return isBanned;
    }
    
    /**
     * @param isBanned the isBanned to set
     */
    public void setBanned(boolean isBanned) {
        this.isBanned = isBanned;
    }
    
    /**
     * @return the isPledger
     */
    public boolean getIsPledger() {
        return isPledger;
    }
    
    /**
     * @param isPledger the isPledger to set
     */
    public void setPledger(boolean isPledger) {
        this.isPledger = isPledger;
    }
    
    /**
     * @return the isAdmin
     */
    public boolean getIsAdmin() {
        return isAdmin;
    }
    
    /**
     * @param isAdmin the isAdmin to set
     */
    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
    
    /**
     * @return the langIso2Registration
     */
    public String getLangIso2() {
        return langIso2;
    }
    
    /**
     * @param langIso2 the langIso2 to set
     */
    public void setLangIso2(String langIso2) {
        this.langIso2 = langIso2;
    }
    
    /**
     * @return the countryIso2
     */
    public String getCountryIso2() {
        return countryIso2;
    }
    
    /**
     * @param countryIso2 the countryIso2 to set
     */
    public void setCountryIso2(String countryIso2) {
        this.countryIso2 = countryIso2;
    }
    
    /**
     * @return the orgTypeId
     */
    public Long getOrgTypeId() {
        return orgTypeId;
    }
    
    /**
     * @param orgTypeId the orgTypeId to set
     */
    public void setOrgTypeId(Long orgTypeId) {
        this.orgTypeId = orgTypeId;
    }
    
    /**
     * @return the orgGroupId
     */
    public Long getOrgGroupId() {
        return orgGroupId;
    }
    
    /**
     * @param orgGroupId the orgGroupId to set
     */
    public void setOrgGroupId(Long orgGroupId) {
        this.orgGroupId = orgGroupId;
    }
    
    /**
     * @return the orgId
     */
    public Long getOrgId() {
        return orgId;
    }
    
    /**
     * @param orgId the orgId to set
     */
    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }
    
    /**
     * @return the assignedOrgIds
     */
    public Set<Long> getAssignedOrgIds() {
        return assignedOrgIds;
    }
    
    /**
     * @param assignedOrgIds the assignedOrgIds to set
     */
    public void setAssignedOrgIds(SortedSet<Long> assignedOrgIds) {
        this.assignedOrgIds = assignedOrgIds;
    }

    /**
     * @return the groupKeys
     */
    public Set<String> getGroupKeys() {
        return groupKeys;
    }

    /**
     * @param groupKeys the groupKeys to set
     */
    public void setGroupKeys(SortedSet<String> groupKeys) {
        this.groupKeys = groupKeys;
    }

}
