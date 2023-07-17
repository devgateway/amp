/*
 * Created on 1/03/2006
 * 
 * @author akashs
 * 
 */
package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Set;

import org.digijava.module.aim.annotations.interchange.Interchangeable;
import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "AMP_GPI_SURVEY_QUESTION")
public class AmpGPISurveyQuestion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_GPI_SURVEY_QUESTION_seq")
    @SequenceGenerator(name = "AMP_GPI_SURVEY_QUESTION_seq", sequenceName = "AMP_GPI_SURVEY_QUESTION_seq", allocationSize = 1)
    @Column(name = "amp_question_id")
    private Long ampQuestionId;

    @Column(name = "question_text")
    private String questionText;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "amp_indicator_id", nullable = false)
    private AmpGPISurveyIndicator ampIndicatorId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_question_id")
    private AmpGPISurveyQuestion parentQuestion;

    @Column(name = "question_number")
    private Integer questionNumber;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "amp_type_id", nullable = false)
    private AmpGPISurveyQuestionType ampTypeId;

    @Column(name = "status")
    private String status;

    @OneToMany(mappedBy = "parentQuestion", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AmpGPISurveyQuestion> questions;


    public AmpGPISurveyQuestion getParentQuestion() {
        return parentQuestion;
    }

    public void setParentQuestion(AmpGPISurveyQuestion parentQuestion) {
        this.parentQuestion = parentQuestion;
    }

    public Set<AmpGPISurveyQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<AmpGPISurveyQuestion> questions) {
        this.questions = questions;
    }

    public static class GPISurveyQuestionComparator implements Comparator<AmpGPISurveyQuestion>, Serializable {

        private static final long serialVersionUID = 1L;

        @Override
        public int compare(AmpGPISurveyQuestion arg0, AmpGPISurveyQuestion arg1) {
            if (arg0.getQuestionNumber() != null && arg1.getQuestionNumber() != null) {
                return arg0.getQuestionNumber().compareTo(arg1.getQuestionNumber());
            }
            return arg0.hashCode() - arg1.hashCode();
        }

    }

    public AmpGPISurveyIndicator getAmpIndicatorId() {
        return ampIndicatorId;
    }

    public void setAmpIndicatorId(AmpGPISurveyIndicator ampIndicatorId) {
        this.ampIndicatorId = ampIndicatorId;
    }

    public Long getAmpQuestionId() {
        return ampQuestionId;
    }

    public void setAmpQuestionId(Long ampQuestionId) {
        this.ampQuestionId = ampQuestionId;
    }

    public AmpGPISurveyQuestionType getAmpTypeId() {
        return ampTypeId;
    }

    public void setAmpTypeId(AmpGPISurveyQuestionType ampTypeId) {
        this.ampTypeId = ampTypeId;
    }

    public Integer getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(Integer questionNumber) {
        this.questionNumber = questionNumber;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
