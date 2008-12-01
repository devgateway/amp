package org.digijava.module.aim.dbentity;

import java.io.Serializable;

/**
 * @author George Kvizhinadze
 *
 */
public class AmpIndicatorSubgroup implements Serializable{
    Long ampIndicatorSubgroupId;
    String subgroupName;
    String subgroupCode;


    public Long getAmpIndicatorSubgroupId() {
        return ampIndicatorSubgroupId;
    }

    public void setAmpIndicatorSubgroupId(Long ampIndicatorSubgroupId) {
            this.ampIndicatorSubgroupId = ampIndicatorSubgroupId;
    }

    public String getSubgroupName() {
        return subgroupName;
    }

    public void setSubgroupName(String subgroupName) {
        this.subgroupName = subgroupName;
    }

    public String getSubgroupCode() {
        return subgroupCode;
    }

    public void setSubgroupCode(String subgroupCode) {
        this.subgroupCode = subgroupCode;
    }

}
