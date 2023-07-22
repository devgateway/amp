package org.digijava.module.aim.dbentity;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name = "AMP_GPI_DEFAULT_FILTERS")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class GPIDefaultFilters {

    public final static String GPI_DEFAULT_FILTER_ORG_GROUP = "GPI_DEFAULT_FILTER_ORG_GROUP";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_gpi_default_filters_seq_gen")
    @SequenceGenerator(name = "amp_gpi_default_filters_seq_gen", sequenceName = "AMP_GPI_DEFAULT_FILTERS_seq", allocationSize = 1)
    @Column(name = "id")
    private Long gpiDefaultFiltersId;

    @Column(name = "key")
    private String key;

    @Column(name = "value")
    private String value;


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getGpiDefaultFiltersId() {
        return gpiDefaultFiltersId;
    }

    public void setGpiDefaultFiltersId(Long gpiDefaultFiltersId) {
        this.gpiDefaultFiltersId = gpiDefaultFiltersId;
    }

}
