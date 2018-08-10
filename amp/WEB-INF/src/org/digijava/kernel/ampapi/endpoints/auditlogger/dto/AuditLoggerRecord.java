package org.digijava.kernel.ampapi.endpoints.auditlogger.dto;

import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import org.digijava.kernel.ampapi.endpoints.auditlogger.AmpActivityInternalIdsDefinition;
import org.digijava.kernel.ampapi.endpoints.auditlogger.AuditLogger;
import org.digijava.kernel.ampapi.endpoints.auditlogger.AuditLoggerDefinitions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuditLoggerRecord {
    protected static Logger logger = Logger.getLogger(AuditLoggerRecord.class);

    private String tabletAffected;
    private Set<String> currentValues;
    //private LinkedHashSet previousValues;
    private Long previousAmpActivityId;
    private Long currentAmpActivityId;

    /**
     * U update
     * I Insert
     * D Delete
     */
    private String type;

    public String getTabletAffected() {
        return tabletAffected;
    }

    public void setTabletAffected(String tabletAffected) {
        this.tabletAffected = tabletAffected;
    }

    public Long getPreviousAmpActivityId() {
        return previousAmpActivityId;
    }

    public void setPreviousAmpActivityId(Long previousAmpActivityId) {
        this.previousAmpActivityId = previousAmpActivityId;
    }

    public Long getCurrentAmpActivityId() {
        return currentAmpActivityId;
    }

    public void setCurrentAmpActivityId(Long currentAmpActivityId) {
        this.currentAmpActivityId = currentAmpActivityId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AuditLoggerRecord() {

    }

    public Set<String> getCurrentValues() {
        return currentValues;
    }

    public void setCurrentValues(Set<String> currentValues) {
        this.currentValues = currentValues;
    }


}
