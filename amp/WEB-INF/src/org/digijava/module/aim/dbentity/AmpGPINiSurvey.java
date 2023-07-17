package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.apache.commons.collections.ComparatorUtils;
import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "AMP_GPI_NI_SURVEY")
public class AmpGPINiSurvey implements Serializable, Cloneable, Comparable<AmpGPINiSurvey> {
    
    private static final long serialVersionUID = -4889980304099658852L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_GPI_NI_SURVEY_seq")
    @SequenceGenerator(name = "AMP_GPI_NI_SURVEY_seq", sequenceName = "AMP_GPI_NI_SURVEY_seq", allocationSize = 1)
    @Column(name = "amp_gpi_ni_survey_id")
    private Long ampGPINiSurveyId;

    @Column(name = "survey_date")
    private Date surveyDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amp_org_role_id", unique = true)
    private AmpOrgRole ampOrgRole;

    @OneToMany(mappedBy = "ampGPINiSurvey", cascade = CascadeType.ALL)
    private Set<AmpGPINiSurveyResponse> responses;


    public Long getAmpGPINiSurveyId() {
        return ampGPINiSurveyId;
    }

    public void setAmpGPINiSurveyId(Long ampGPINiSurveyId) {
        this.ampGPINiSurveyId = ampGPINiSurveyId;
    }

    public Date getSurveyDate() {
        return surveyDate;
    }

    public void setSurveyDate(Date surveyDate) {
        this.surveyDate = surveyDate;
    }

    public AmpOrgRole getAmpOrgRole() {
        return ampOrgRole;
    }

    public void setAmpOrgRole(AmpOrgRole ampOrgRole) {
        this.ampOrgRole = ampOrgRole;
    }

    public Set<AmpGPINiSurveyResponse> getResponses() {
        return responses;
    }

    public void setResponses(Set<AmpGPINiSurveyResponse> responses) {
        this.responses = responses;
    }

    @Override
    public int compareTo(AmpGPINiSurvey o) {
        return ComparatorUtils.nullLowComparator(null).compare(getAmpOrgRole(), o.getAmpOrgRole());
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return super.clone();
    }
    
}
