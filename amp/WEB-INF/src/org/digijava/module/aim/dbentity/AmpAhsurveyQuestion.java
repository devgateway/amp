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
import javax.persistence.*;

@Entity
@Table(name = "AMP_AHSURVEY_QUESTION")
public class AmpAhsurveyQuestion implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_ahsurvey_question_seq_generator")
    @SequenceGenerator(name = "amp_ahsurvey_question_seq_generator", sequenceName = "AMP_AHSURVEY_QUESTION_seq", allocationSize = 1)
    @Column(name = "amp_question_id")
    private Long ampQuestionId;

    @Column(name = "question_text")
    private String questionText;

    @ManyToOne( optional = false)
    @JoinColumn(name = "amp_indicator_id")
    private AmpAhsurveyIndicator ampIndicatorId;

    @ManyToOne
    @JoinColumn(name = "parent_question_id")
    private AmpAhsurveyQuestion parentQuestion;

    @Column(name = "question_number")
    private Integer questionNumber;

    @ManyToOne( optional = false)
    @JoinColumn(name = "amp_type_id")
    private AmpAhsurveyQuestionType ampTypeId;

    @Column(name = "status")
    private String status;

    @OneToMany(mappedBy = "parentQuestion",  fetch = FetchType.LAZY)
    private Set<AmpAhsurveyQuestion> questions;
    

    public AmpAhsurveyQuestion getParentQuestion() {
        return parentQuestion;
    }
    public void setParentQuestion(AmpAhsurveyQuestion parentQuestion) {
        this.parentQuestion = parentQuestion;
    }
    public Set<AmpAhsurveyQuestion> getQuestions() {
        return questions;
    }
    public void setQuestions(Set<AmpAhsurveyQuestion> questions) {
        this.questions = questions;
    }
    public static class AhsurveyQuestionComparator implements Comparator<AmpAhsurveyQuestion>, Serializable {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        @Override
        public int compare(AmpAhsurveyQuestion arg0, AmpAhsurveyQuestion arg1) {
            if (arg0.getQuestionNumber() != null && arg1.getQuestionNumber() != null){
                return arg0.getQuestionNumber().compareTo(arg1.getQuestionNumber());
            }
            return arg0.hashCode()-arg1.hashCode();
        }
        
    }
    
    /**
     * @return Returns the ampIndicatorId.
     */
    public AmpAhsurveyIndicator getAmpIndicatorId() {
        return ampIndicatorId;
    }
    /**
     * @param ampIndicatorId The ampIndicatorId to set.
     */
    public void setAmpIndicatorId(AmpAhsurveyIndicator ampIndicatorId) {
        this.ampIndicatorId = ampIndicatorId;
    }
    /**
     * @return Returns the ampQuestionId.
     */
    public Long getAmpQuestionId() {
        return ampQuestionId;
    }
    /**
     * @param ampQuestionId The ampQuestionId to set.
     */
    public void setAmpQuestionId(Long ampQuestionId) {
        this.ampQuestionId = ampQuestionId;
    }
    /**
     * @return Returns the ampTypeId.
     */
    public AmpAhsurveyQuestionType getAmpTypeId() {
        return ampTypeId;
    }
    /**
     * @param ampTypeId The ampTypeId to set.
     */
    public void setAmpTypeId(AmpAhsurveyQuestionType ampTypeId) {
        this.ampTypeId = ampTypeId;
    }
    /**
     * @return Returns the questionNumber.
     */
    public Integer getQuestionNumber() {
        return questionNumber;
    }
    /**
     * @param questionNumber The questionNumber to set.
     */
    public void setQuestionNumber(Integer questionNumber) {
        this.questionNumber = questionNumber;
    }
    /**
     * @return Returns the questionText.
     */
    public String getQuestionText() {
        return questionText;
    }
    /**
     * @param questionText The questionText to set.
     */
    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }
    /**
     * @return Returns the status.
     */
    public String getStatus() {
        return status;
    }
    /**
     * @param status The status to set.
     */
    public void setStatus(String status) {
        this.status = status;
    }

}
