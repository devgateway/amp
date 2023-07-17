package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "AMP_GPI_NI_INDICATOR")
public class AmpGPINiIndicator implements Serializable {

    private static final long serialVersionUID = 2838422344917663439L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_GPI_NI_INDICATOR_seq")
    @SequenceGenerator(name = "AMP_GPI_NI_INDICATOR_seq", sequenceName = "AMP_GPI_NI_INDICATOR_seq", allocationSize = 1)
    @Column(name = "amp_gpi_ni_indicator_id")
    private Long ampGPINiIndicatorId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @OneToMany(mappedBy = "indicator", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<AmpGPINiQuestion> questions;


    public Long getAmpGPINiIndicatorId() {
        return ampGPINiIndicatorId;
    }

    public void setAmpGPINiIndicatorId(Long ampGPINiIndicatorId) {
        this.ampGPINiIndicatorId = ampGPINiIndicatorId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<AmpGPINiQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<AmpGPINiQuestion> questions) {
        this.questions = questions;
    }

}
