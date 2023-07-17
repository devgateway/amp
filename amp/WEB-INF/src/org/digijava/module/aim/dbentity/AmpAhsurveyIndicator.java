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
@Table(name = "AMP_AHSURVEY_INDICATOR")
public class AmpAhsurveyIndicator implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_indicator_seq_generator")
    @SequenceGenerator(name = "amp_indicator_seq_generator", sequenceName = "AMP_INDICATOR_seq", allocationSize = 1)
    @Column(name = "amp_indicator_id")
    private Long ampIndicatorId;

    @Column(name = "name")
    private String name;

    @Column(name = "total_question")
    private Integer totalQuestions;

    @Column(name = "indicator_number")
    private Integer indicatorNumber;

    @Column(name = "indicator_code")
    private String indicatorCode;

    @Column(name = "status")
    private String status;

    @OneToMany(mappedBy = "ampIndicatorId", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AmpAhsurveyQuestion> questions;

    @OneToMany(mappedBy = "ampIndicatorId", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AmpAhsurveyIndicatorCalcFormula> calcFormulas;


    
    public static class AhsurveyIndicatorComparator implements Comparator<AmpAhsurveyIndicator>, Serializable {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        @Override
        public int compare(AmpAhsurveyIndicator arg0, AmpAhsurveyIndicator arg1) {
            if (arg0.getIndicatorNumber() != null && arg1.getIndicatorNumber() != null){
                return arg0.getIndicatorNumber().compareTo(arg1.getIndicatorNumber());
            }
            return arg0.hashCode()-arg1.hashCode();
        }
        
    }
    
    
    /**
     * @return Returns the indicatorCode.
     */
    public String getIndicatorCode() {
        return indicatorCode;
    }
    /**
     * @param indicatorCode The indicatorCode to set.
     */
    public void setIndicatorCode(String indicatorCode) {
        this.indicatorCode = indicatorCode;
    }
    /**
     * @return Returns the indicatorId.
     */
    public Long getAmpIndicatorId() {
        return ampIndicatorId;
    }
    /**
     * @param indicatorId The indicatorId to set.
     */
    public void setAmpIndicatorId(Long ampIndicatorId) {
        this.ampIndicatorId = ampIndicatorId;
    }
    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    public String getNameTrn() {
        return name.toLowerCase().replaceAll(" ", "").replaceAll("%", "");
    }

    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
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
    /**
     * @return Returns the totalQuestions.
     */
    public Integer getTotalQuestions() {
        return totalQuestions;
    }
    /**
     * @param totalQuestions The totalQuestions to set.
     */
    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }
    /**
     * @return Returns the indicatorNumber.
     */
    public Integer getIndicatorNumber() {
        return indicatorNumber;
    }
    /**
     * @param indicatorNumber The indicatorNumber to set.
     */
    public void setIndicatorNumber(Integer indicatorNumber) {
        this.indicatorNumber = indicatorNumber;
    }
    /**
     * @return Returns the questions.
     */
    public Set<AmpAhsurveyQuestion> getQuestions() {
        return questions;
    }

    public Set<AmpAhsurveyIndicatorCalcFormula> getCalcFormulas() {
        return calcFormulas;
    }

    /**
     * @param questions The questions to set.
     */
    public void setQuestions(Set<AmpAhsurveyQuestion> questions) {
        this.questions = questions;
    }

    public void setCalcFormulas(Set<AmpAhsurveyIndicatorCalcFormula> calcFormulas) {
        this.calcFormulas = calcFormulas;
    }

}
