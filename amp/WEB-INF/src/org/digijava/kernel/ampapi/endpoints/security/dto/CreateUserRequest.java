package org.digijava.kernel.ampapi.endpoints.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;


public class CreateUserRequest {
    @JsonProperty("firstName")
    @ApiModelProperty(required = true, example = "ATL")
    private String firstName;

    @JsonProperty("lastName")
    @ApiModelProperty(required = true, example = "ATL")
    private String lastName;

    @JsonProperty("email")
    @ApiModelProperty(required = true, example = "atltest@amp.org")
    private String email;

    @JsonProperty("emailConfirmation")
    @ApiModelProperty(required = true, example = "atltest@amp.org")
    private String emailConfirmation;

    @JsonProperty("password")
    @ApiModelProperty(required = true, example = "abc")
    private String password;

    @JsonProperty("passwordConfirmation")
    @ApiModelProperty(required = true, example = "abc")
    private String passwordConfirmation;

    @JsonProperty("notificationEmailEnabled")
    private Boolean notificationEmailEnabled = false;

    @JsonProperty("notificationEmail")
    @ApiModelProperty(example = "other@amp.org")
    private String notificationEmail;

    @JsonProperty("repeatNotificationEmail")
    @ApiModelProperty(example = "other@amp.org")
    private String repeatNotificationEmail;

    public CreateUserRequest() {
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

    public String getEmailConfirmation() {
        return emailConfirmation;
    }

    public void setEmailConfirmation(String emailConfirmation) {
        this.emailConfirmation = emailConfirmation;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
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
}
