package org.digijava.kernel.ampapi.endpoints.security.dto;

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

}
