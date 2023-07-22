package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.*;

import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.util.Output;
import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "AMP_GPI_SURVEY")
public class AmpGPISurvey implements Versionable, Serializable, Cloneable, Comparable<AmpGPISurvey> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_GPISURVEY_seq")
    @SequenceGenerator(name = "AMP_GPISURVEY_seq", sequenceName = "AMP_GPISURVEY_seq", allocationSize = 1)
    @Column(name = "amp_gpisurvey_id")
    private Long ampGPISurveyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amp_activity_id", nullable = false)
    private AmpActivityVersion ampActivityId;

    @OneToMany(mappedBy = "ampResponseId", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AmpGPISurveyResponse> responses = new HashSet<>();

    @Column(name = "survey_date")
    private Date surveyDate;

    public Date getSurveyDate() {
        return surveyDate;
    }

    public void setSurveyDate(Date surveyDate) {
        this.surveyDate = surveyDate;
    }

    public Long getAmpGPISurveyId() {
        return ampGPISurveyId;
    }

    public void setAmpGPISurveyId(Long ampGPISurveyId) {
        this.ampGPISurveyId = ampGPISurveyId;
    }

    public AmpActivityVersion getAmpActivityId() {
        return ampActivityId;
    }

    public void setAmpActivityId(AmpActivityVersion ampActivityId) {
        this.ampActivityId = ampActivityId;
    }

    @Override
    public boolean equalsForVersioning(Object obj) {
        // Since each project can have only 1 GPI survey then we consider them always the same.
        AmpGPISurvey aux = (AmpGPISurvey) obj;
        if (aux != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Object getValue() {

        Comparator surveyComparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                AmpGPISurveyResponse aux1 = (AmpGPISurveyResponse) o1;
                AmpGPISurveyResponse aux2 = (AmpGPISurveyResponse) o2;
                return aux1.getAmpQuestionId().getQuestionNumber().compareTo(aux2.getAmpQuestionId().getQuestionNumber());
            }
        };

        String ret = "";
        List<AmpGPISurveyResponse> auxList = new ArrayList<AmpGPISurveyResponse>(this.responses);
        Collections.sort(auxList, surveyComparator);
        Iterator<AmpGPISurveyResponse> iter = auxList.iterator();
        while (iter.hasNext()) {
            AmpGPISurveyResponse auxResponse = iter.next();
            ret = ret + ((auxResponse.getResponse() != null && !auxResponse.getResponse().equals("nil")) ? auxResponse.getResponse() : "-");
        }
        return ret;
    }

    @Override
    public Output getOutput() {
        Comparator surveyComparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                AmpGPISurveyResponse aux1 = (AmpGPISurveyResponse) o1;
                AmpGPISurveyResponse aux2 = (AmpGPISurveyResponse) o2;
                return aux1.getAmpQuestionId().getAmpQuestionId().compareTo(aux2.getAmpQuestionId().getAmpQuestionId());
            }
        };

        Output out = new Output();
        out.setOutputs(new ArrayList<Output>());
        out.getOutputs().add(new Output(new ArrayList(), new String[] { "Questions" }, new Object[] { "" }));
        Output questions = out.getOutputs().get(out.getOutputs().size() - 1);

        if (this.responses != null) {
            List<AmpGPISurveyResponse> auxList = new ArrayList<AmpGPISurveyResponse>(this.responses);
            Collections.sort(auxList, surveyComparator);
            Iterator<AmpGPISurveyResponse> iter = auxList.iterator();
            while (iter.hasNext()) {
                AmpGPISurveyResponse auxResponse = iter.next();
                Output auxOutResp = new Output();
                auxOutResp.setTitle(auxResponse.getOutput().getTitle());
                auxOutResp.setValue(auxResponse.getOutput().getValue());
                if (auxOutResp.getValue() != null) {
                    questions.getOutputs().add(auxOutResp);
                }
            }
        }
        return out;
    }

    @Override
    public Object prepareMerge(AmpActivityVersion newActivity) throws CloneNotSupportedException {
        AmpGPISurvey aux = (AmpGPISurvey) clone();
        aux.ampActivityId = newActivity;
        aux.ampGPISurveyId = null;
        if (aux.responses != null && aux.responses.size() > 0) {
            Set<AmpGPISurveyResponse> responses = new HashSet<AmpGPISurveyResponse>();
            Iterator<AmpGPISurveyResponse> i = aux.responses.iterator();
            while (i.hasNext()) {
                AmpGPISurveyResponse newResp = (AmpGPISurveyResponse) i.next().clone();
                newResp.setAmpGPISurveyId(aux);
                newResp.setAmpReponseId(null);
                responses.add(newResp);
            }
            aux.responses = responses;
        } else {
            aux.responses = null;
        }
        return aux;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return super.clone();
    }

    @Override
    public int compareTo(AmpGPISurvey o) {
        if (this.getAmpActivityId() != null && o.getAmpGPISurveyId() != null)
            return this.getAmpGPISurveyId().compareTo(o.getAmpGPISurveyId());
        else
            return -1;
    }

    public Set<AmpGPISurveyResponse> getResponses() {
        return responses;
    }

    public void setResponses(Set<AmpGPISurveyResponse> responses) {
        this.responses = responses;
    }
    
}
