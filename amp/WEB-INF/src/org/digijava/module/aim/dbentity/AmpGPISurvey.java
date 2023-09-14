package org.digijava.module.aim.dbentity;

import org.digijava.module.aim.util.Output;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.*;

public class AmpGPISurvey implements Versionable, Serializable, Cloneable, Comparable<AmpGPISurvey> {
    private final static Logger logger = LoggerFactory.getLogger(AmpGPISurvey.class);

    //IATI-check: to be ignored
    private Long ampGPISurveyId;

    private AmpActivityVersion ampActivityId;
//  @Interchangeable(fieldTitle="Responses"/*, descend = true*/)
    private Set<AmpGPISurveyResponse> responses;
//  @Interchangeable(fieldTitle="Survey Date")
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

        Comparator surveyComparator = (o1, o2) -> {
            AmpGPISurveyResponse aux1 = (AmpGPISurveyResponse) o1;
            AmpGPISurveyResponse aux2 = (AmpGPISurveyResponse) o2;
            return aux1.getAmpQuestionId().getQuestionNumber().compareTo(aux2.getAmpQuestionId().getQuestionNumber());
        };

        StringBuilder ret = new StringBuilder();
        List<AmpGPISurveyResponse> auxList = new ArrayList<AmpGPISurveyResponse>(this.responses);
        auxList.sort(surveyComparator);
        for (AmpGPISurveyResponse auxResponse : auxList) {
            ret.append((auxResponse.getResponse() != null && !auxResponse.getResponse().equals("nil")) ? auxResponse.getResponse() : "-");
        }
        return ret.toString();
    }

    @Override
    public Output getOutput() {
        Comparator surveyComparator = (o1, o2) -> {
            AmpGPISurveyResponse aux1 = (AmpGPISurveyResponse) o1;
            AmpGPISurveyResponse aux2 = (AmpGPISurveyResponse) o2;
            return aux1.getAmpQuestionId().getAmpQuestionId().compareTo(aux2.getAmpQuestionId().getAmpQuestionId());
        };

        Output out = new Output();
        out.setOutputs(new ArrayList<>());
        out.getOutputs().add(new Output(new ArrayList(), new String[] { "Questions" }, new Object[] { "" }));
        Output questions = out.getOutputs().get(out.getOutputs().size() - 1);

        if (this.responses != null) {
            List<AmpGPISurveyResponse> auxList = new ArrayList<AmpGPISurveyResponse>(this.responses);
            auxList.sort(surveyComparator);
            for (AmpGPISurveyResponse auxResponse : auxList) {
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
        if (aux.getResponses() != null && aux.getResponses().size() > 0) {
            Set<AmpGPISurveyResponse> responses = new HashSet<>();
            for (AmpGPISurveyResponse respons : aux.getResponses()) {
                AmpGPISurveyResponse newResp = (AmpGPISurveyResponse) respons.clone();
                newResp.setAmpGPISurveyId(aux);
                newResp.setAmpReponseId(null);
                responses.add(newResp);
            }
            aux.setResponses(responses);
        } else {
            aux.setResponses(null);
        }
        logger.info("Merging responses. "+responses);

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
