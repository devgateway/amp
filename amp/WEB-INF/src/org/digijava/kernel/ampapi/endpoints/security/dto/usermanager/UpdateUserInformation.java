package org.digijava.kernel.ampapi.endpoints.security.dto.usermanager;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.security.SecurityConstants;

public class UpdateUserInformation {
    @JsonProperty(EPConstants.ID)
    @ApiModelProperty(required = true)
    private Long id;
    @JsonProperty(SecurityConstants.FIRST_NAME)
    @ApiModelProperty(required = true, example = "ATL")
    private String firstName;

    @JsonProperty(SecurityConstants.LAST_NAME)
    @ApiModelProperty(example = "ATL")
    private String lastName;

    @JsonProperty(SecurityConstants.EMAIL)
    @ApiModelProperty(required = true, example = "atltest@amp.org")
    private String email;

    @JsonProperty("notificationEmailEnabled")
    private Boolean notificationEmailEnabled;

    @JsonProperty("notificationEmail")
    @ApiModelProperty(required = true, example = "other@amp.org")
    private String notificationEmail;

    @JsonProperty("repeatNotificationEmail")
    @ApiModelProperty(required = true, example = "other@amp.org")
    private String repeatNotificationEmail;

    @JsonProperty("address")
    private String address;


//    @JsonProperty("password")
//    @ApiModelProperty(required = true, example = "abc")
//    private String password;
//
//    @JsonProperty("passwordConfirmation")
//    @ApiModelProperty(required = true, example = "abc")
//    private String passwordConfirmation;

    @JsonProperty("countryIso")
    private String countryIso;

    @JsonProperty("languageCode")
    private String languageCode;

    @JsonProperty("organizationId")
    private Long organizationId;

    @JsonProperty("organizationName")
    private String organizationName;

    @JsonProperty("organizationTypeId")
    private Long organizationTypeId;

    @JsonProperty("organizationGroupId")
    private Long organizationGroupId;

    public UpdateUserInformation() {
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

    public String getRepeatNotificationEmail() {
        return repeatNotificationEmail;
    }

    public void setRepeatNotificationEmail(String repeatNotificationEmail) {
        this.repeatNotificationEmail = repeatNotificationEmail;
    }

    public String getCountryIso() {
        return countryIso;
    }

    public void setCountryIso(String countryIso) {
        this.countryIso = countryIso;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public Long getOrganizationTypeId() {
        return organizationTypeId;
    }

    public void setOrganizationTypeId(Long organizationTypeId) {
        this.organizationTypeId = organizationTypeId;
    }

    public Long getOrganizationGroupId() {
        return organizationGroupId;
    }

    public void setOrganizationGroupId(Long organizationGroupId) {
        this.organizationGroupId = organizationGroupId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
