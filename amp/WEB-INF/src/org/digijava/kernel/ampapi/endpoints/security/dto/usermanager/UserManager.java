package org.digijava.kernel.ampapi.endpoints.security.dto.usermanager;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.security.SecurityConstants;

/**
 * Usermanager information
 *
 * @author Denis Mbugua
 */
public class UserManager {
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


    public UserManager(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
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

}
