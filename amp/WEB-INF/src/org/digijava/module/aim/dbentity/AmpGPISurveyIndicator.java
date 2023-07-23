package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Set;
import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "AMP_GPI_SURVEY_INDICATOR")
public class AmpGPISurveyIndicator implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_GPI_SURVEY_INDICATOR_seq")
    @SequenceGenerator(name = "AMP_GPI_SURVEY_INDICATOR_seq", sequenceName = "AMP_GPI_SURVEY_INDICATOR_seq", allocationSize = 1)
    @Column(name = "amp_indicator_id")
    private Long ampIndicatorId;

    @Column(name = "name")
    private String name;

    @Column(name = "total_question")
    private Integer totalQuestions;

    @Column(name = "indicator_code")
    private String indicatorCode;

    @Column(name = "status")
    private String status;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "show_as_indicator")
    private Boolean showAsIndicator;

    @OneToMany(mappedBy = "ampIndicatorId", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AmpGPISurveyQuestion> questions;

    @OneToMany(mappedBy = "parentIndicator", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AmpGPISurveyIndicatorCalcFormula> calcFormulas;

    public static class GPISurveyIndicatorComparator implements Comparator<AmpGPISurveyIndicator>, Serializable {

        private static final long serialVersionUID = 1L;

        @Override
        public int compare(AmpGPISurveyIndicator arg0, AmpGPISurveyIndicator arg1) {
            if (Boolean.TRUE.equals(arg0.getShowAsIndicator()) && !Boolean.TRUE.equals(arg1.getShowAsIndicator())) {
                return 1;
            } else if (Boolean.TRUE.equals(arg1.getShowAsIndicator()) && !Boolean.TRUE.equals(arg0.getShowAsIndicator())) {
                return -1;
            } else {
                if (arg0.getIndicatorCode() != null && arg1.getIndicatorCode() != null) {
                    return arg0.getIndicatorCode().compareTo(arg1.getIndicatorCode());
                }
                return arg0.hashCode() - arg1.hashCode();
            }
        }
    }

    public String getIndicatorCode() {
        return indicatorCode;
    }

    public void setIndicatorCode(String indicatorCode) {
        this.indicatorCode = indicatorCode;
    }

    public Long getAmpIndicatorId() {
        return ampIndicatorId;
    }

    public void setAmpIndicatorId(Long ampIndicatorId) {
        this.ampIndicatorId = ampIndicatorId;
    }

    public String getName() {
        return name;
    }

    public String getNameTrn() {
        return name.toLowerCase().replaceAll(" ", "").replaceAll("%", "");
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public Set<AmpGPISurveyQuestion> getQuestions() {
        return questions;
    }

    public Set<AmpGPISurveyIndicatorCalcFormula> getCalcFormulas() {
        return calcFormulas;
    }

    public void setQuestions(Set<AmpGPISurveyQuestion> questions) {
        this.questions = questions;
    }

    public void setCalcFormulas(Set<AmpGPISurveyIndicatorCalcFormula> calcFormulas) {
        this.calcFormulas = calcFormulas;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getShowAsIndicator() {
        return showAsIndicator;
    }

    public void setShowAsIndicator(Boolean showAsIndicator) {
        this.showAsIndicator = showAsIndicator;
    }

}
