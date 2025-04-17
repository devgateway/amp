package org.digijava.module.aim.dbentity;

import java.io.Serializable;

/**
 * @author George Kvizhinadze
 *
 */
public class AmpIndicatorSource implements Serializable{
    Long AmpIndicatorSourceId;
    String sourceName;
    public Long getAmpIndicatorSourceId() {
        return AmpIndicatorSourceId;
    }

    public void setAmpIndicatorSourceId(Long AmpIndicatorSourceId) {
        this.AmpIndicatorSourceId = AmpIndicatorSourceId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }
}
