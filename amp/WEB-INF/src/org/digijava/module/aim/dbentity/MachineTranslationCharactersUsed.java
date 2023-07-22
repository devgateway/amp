package org.digijava.module.aim.dbentity;

/**
 * @author Octavian Ciubotaru
 */
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name = "machine_translation_characters_used")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MachineTranslationCharactersUsed {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "month", length = 6, nullable = false)
    private String month;

    @Column(name = "used", nullable = false)
    private Integer used;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Integer getUsed() {
        return used;
    }

    public void setUsed(Integer used) {
        this.used = used;
    }
}
