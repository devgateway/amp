package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Set;

import org.apache.commons.collections.ComparatorUtils;
import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "AMP_GPI_NI_QUESTION")
public class AmpGPINiQuestion implements Serializable {
    
    private static final long serialVersionUID = 6751072241262868712L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_GPI_NI_QUESTION_seq")
    @SequenceGenerator(name = "AMP_GPI_NI_QUESTION_seq", sequenceName = "AMP_GPI_NI_QUESTION_seq", allocationSize = 1)
    @Column(name = "amp_gpi_ni_question_id")
    private Long ampGPINiQuestionId;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private GPINiQuestionType type;

    @Column(name = "index")
    private Integer index;

    @Column(name = "allow_multiple")
    private Boolean allowMultiple;

    @Column(name = "requires_data_entry")
    private Boolean requiresDataEntry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amp_gpi_ni_indicator_id", nullable = false)
    private AmpGPINiIndicator ampGPINiIndicator;

    @OneToMany(mappedBy = "ampGPINiQuestion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<AmpGPINiQuestionOption> options;


    public Long getAmpGPINiQuestionId() {
        return ampGPINiQuestionId;
    }

    public void setAmpGPINiQuestionId(Long ampGPINiQuestionId) {
        this.ampGPINiQuestionId = ampGPINiQuestionId;
    }

    public AmpGPINiIndicator getAmpGPINiIndicator() {
        return ampGPINiIndicator;
    }

    public void setAmpGPINiIndicator(AmpGPINiIndicator ampGPINiIndicator) {
        this.ampGPINiIndicator = ampGPINiIndicator;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GPINiQuestionType getType() {
        return type;
    }

    public void setType(GPINiQuestionType type) {
        this.type = type;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Boolean getAllowMultiple() {
        return allowMultiple;
    }

    public void setAllowMultiple(Boolean allowMultiple) {
        this.allowMultiple = allowMultiple;
    }

    public Boolean getRequiresDataEntry() {
        return requiresDataEntry;
    }

    public void setRequiresDataEntry(Boolean requiresDataEntry) {
        this.requiresDataEntry = requiresDataEntry;
    }

    public Set<AmpGPINiQuestionOption> getOptions() {
        return options;
    }

    public void setOptions(Set<AmpGPINiQuestionOption> options) {
        this.options = options;
    }

    public static class GPINiQuestionComparator implements Comparator<AmpGPINiQuestion>, Serializable {
        private static final long serialVersionUID = 1L;

        @Override
        public int compare(AmpGPINiQuestion arg0, AmpGPINiQuestion arg1) {
            return ComparatorUtils.nullLowComparator(null).compare(arg0.getIndex(), arg1.getIndex());
        }
    }
    
    /**
     * type of responses for the GPI ni question
     */
    public enum GPINiQuestionType {DECIMAL, INTEGER, SINGLE_CHOICE, MULTIPLE_CHOICE, FREE_TEXT, LINK, DOCUMENT, NA}
    
}
