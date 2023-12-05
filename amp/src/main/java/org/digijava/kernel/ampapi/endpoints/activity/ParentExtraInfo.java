package org.digijava.kernel.ampapi.endpoints.activity;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * @author Viorel Chihai
 *
 */
public interface ParentExtraInfo {

    @JsonIgnore
    Long getParentId();

}
