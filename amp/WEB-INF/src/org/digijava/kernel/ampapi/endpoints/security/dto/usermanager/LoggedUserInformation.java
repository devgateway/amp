package org.digijava.kernel.ampapi.endpoints.security.dto.usermanager;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.security.SecurityConstants;

/**
 * Loggedin user for editing
 *
 * @author Denis Mbugua
 */
public class LoggedUserInformation {
    @JsonProperty(EPConstants.ID)
    private Long id;
    @JsonProperty(SecurityConstants.FIRST_NAME)
    @ApiModelProperty(example = "ATL")
    private String firstName;

    @JsonProperty(SecurityConstants.LAST_NAME)
    @ApiModelProperty(example = "ATL")
    private String lastName;

    @JsonProperty(SecurityConstants.EMAIL)
    @ApiModelProperty(example = "atltest@amp.org")
    private String email;
    @JsonProperty(SecurityConstants.IS_BANNED)
    private boolean isBanned;

    @JsonProperty("notificationEmailEnabled")
    private Boolean notificationEmailEnabled;

    @JsonProperty("notificationEmail")
    @ApiModelProperty(example = "other@amp.org")
    private String notificationEmail;

    @JsonProperty("address")
    private String address;

    @JsonProperty("countryIso")
    private String countryIso;

    @JsonProperty("organizationName")
    private String organizationName;

    @JsonProperty("organizationTypeId")
    private Long organizationTypeId;

    @JsonProperty("languageCode")
    private String languageCode;

    @JsonProperty("organizationGroupId")
    private Long organizationGroupId;

    public LoggedUserInformation(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public LoggedUserInformation() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserManager{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        this.isBanned = banned;
    }

    public Boolean getNotificationEmailEnabled() {
        return notificationEmailEnabled;
    }

    public void setNotificationEmailEnabled(Boolean notificationEmailEnabled) {
        this.notificationEmailEnabled = notificationEmailEnabled;
    }

    public String getNotificationEmail() {
        return notificationEmail;
    }

    public void setNotificationEmail(String notificationEmail) {
        this.notificationEmail = notificationEmail;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return countryIso;
    }

    public void setCountry(String country) {
        this.countryIso = country;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public Long getOrganizationTypeId() {
        return organizationTypeId;
    }

    public void setOrganizationTypeId(Long organizationTypeId) {
        this.organizationTypeId = organizationTypeId;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public Long getOrganizationGroupId() {
        return organizationGroupId;
    }

    public void setOrganizationGroupId(Long organizationGroupId) {
        this.organizationGroupId = organizationGroupId;
    }
}
