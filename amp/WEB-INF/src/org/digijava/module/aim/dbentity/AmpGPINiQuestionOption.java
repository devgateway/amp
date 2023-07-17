package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "AMP_GPI_NI_QUESTION_OPTION")
public class AmpGPINiQuestionOption implements Serializable {
    
    private static final long serialVersionUID = 3456453082099936257L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_GPI_NI_QUESTION_OPTION_seq")
    @SequenceGenerator(name = "AMP_GPI_NI_QUESTION_OPTION_seq", sequenceName = "AMP_GPI_NI_QUESTION_OPTION_seq", allocationSize = 1)
    @Column(name = "amp_gpi_ni_question_option_id")
    private Long ampGPINiQuestionOptionId;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "option_text", columnDefinition = "text")
    private String optionText;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amp_gpi_ni_question_id", nullable = false)
    private AmpGPINiQuestion ampGPINiQuestion;

    public Long getAmpGPINiQuestionOptionId() {
        return ampGPINiQuestionOptionId;
    }

    public void setAmpGPINiQuestionOptionId(Long amapGPINiQuestionOptionId) {
        this.ampGPINiQuestionOptionId = amapGPINiQuestionOptionId;
    }

    public AmpGPINiQuestion getAmpGPINiQuestion() {
        return ampGPINiQuestion;
    }

    public void setAmpGPINiQuestion(AmpGPINiQuestion ampGPINiQuestion) {
        this.ampGPINiQuestion = ampGPINiQuestion;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    
    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
