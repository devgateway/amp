/*
 * AmpReportsOptions.java
 * Created: 25-Nov-2005
 */

package org.digijava.module.aim.dbentity;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name = "AMP_REPORTS_OPTIONS")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AmpReportsOptions {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_REPORTS_OPTIONS_seq")
    @SequenceGenerator(name = "AMP_REPORTS_OPTIONS_seq", sequenceName = "AMP_REPORTS_OPTIONS_seq", allocationSize = 1)
    @Column(name = "amp_option_id")
    private Long ampOptionId;

    @Column(name = "name")
    private String name;

    @Column(name = "options")
    private Character options;


    public Long getAmpOptionId() {
        return ampOptionId;
    }

    public String getName() {
        return name;
    }

    public char getOptions() {
        return options;
    }

    public void setAmpOptionId(Long ampOptionId) {
        this.ampOptionId = ampOptionId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOptions(char options) {
        this.options = options;
    }
    
}
