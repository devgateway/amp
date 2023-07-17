package org.digijava.module.aim.dbentity;

import java.io.Serializable;

/**
 * @author George Kvizhinadze
 *
 */
import javax.persistence.*;

@Entity
@Table(name = "AMP_INDICATOR_SUBGROUP")
public class AmpIndicatorSubgroup implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_INDICATOR_SUBGROUP_seq")
    @SequenceGenerator(name = "AMP_INDICATOR_SUBGROUP_seq", sequenceName = "AMP_INDICATOR_SUBGROUP_seq", allocationSize = 1)
    @Column(name = "amp_indicator_subgroup_id")
    private Long ampIndicatorSubgroupId;

    @Column(name = "subgroup_name")
    private String subgroupName;

    @Column(name = "subgroup_code")
    private String subgroupCode;



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
