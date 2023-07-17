package org.digijava.module.aim.dbentity;
import javax.persistence.*;

@Entity
@Table(name = "AMP_COMPONENTS_INDICATORS")
public class AmpComponentsIndicators 
{
    @Id
    @GeneratedValue(generator = "ampComponentsIndicatorsSeq")
    @SequenceGenerator(name = "ampComponentsIndicatorsSeq", sequenceName = "AMP_COMPONENTS_INDICATORS_seq", allocationSize = 1)
    @Column(name = "amp_component_indicator_id")
    private Long ampCompIndId;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    
    /**
     * @return Returns the ampCompIndId.
     */
    public Long getAmpCompIndId() {
        return ampCompIndId;
    }
    /**
     * @param ampCompIndId The ampCompIndId to set.
     */
    public void setAmpCompIndId(Long ampCompIndId) {
        this.ampCompIndId = ampCompIndId;
    }
    /**
     * @return Returns the code.
     */
    public String getCode() {
        return code;
    }
    /**
     * @param code The code to set.
     */
    public void setCode(String code) {
        this.code = code;
    }
    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }
    /**
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
}
