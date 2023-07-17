/**
 * AmpCodeChapitre.java
 * (c) 2009 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Set;

/**
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 *
 */
import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "AMP_CHAPTER")
public class AmpChapter implements Serializable {
    @Id
    @Column(name = "code")
    private String code;

    @Column(name = "year")
    private Integer year;

    @Column(name = "description", length = 255)
    private String description;

    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AmpImputation> imputations;


    
    public AmpChapter() {
        
    }
    public AmpChapter(String code) {
        this.code=code;
    }
    
    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }
    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }
    /**
     * @return the year
     */
    public Integer getYear() {
        return year;
    }
    /**
     * @param year the year to set
     */
    public void setYear(Integer year) {
        this.year = year;
    }
    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * @return the imputations
     */
    public Set<AmpImputation> getImputations() {
        return imputations;
    }
    /**
     * @param imputations the imputations to set
     */
    public void setImputations(Set<AmpImputation> imputations) {
        this.imputations = imputations;
    }

}
