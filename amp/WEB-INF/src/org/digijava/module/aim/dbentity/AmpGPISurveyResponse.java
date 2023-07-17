package org.digijava.module.aim.dbentity;

import org.digijava.module.aim.util.Output;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import javax.persistence.*;

@Entity
@Table(name = "AMP_GPI_SURVEY_RESPONSE")
public class AmpGPISurveyResponse implements Versionable, Cloneable, Serializable {

    //IATI-check: to be ignored
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_GPI_SURVEY_RESPONSE_seq")
    @SequenceGenerator(name = "AMP_GPI_SURVEY_RESPONSE_seq", sequenceName = "AMP_GPI_SURVEY_RESPONSE_seq", allocationSize = 1)
    @Column(name = "amp_response_id")
    private Long ampResponseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amp_gpisurvey_id", nullable = false)
    private AmpGPISurvey ampGPISurveyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amp_question_id", nullable = false)
    private AmpGPISurveyQuestion ampQuestionId;

    @Column(name = "response")
    private String response;

    @Column(name = "references_col")
    private String references;


    private Long ampReponseId;


    public String getReferences() {
        return references;
    }

    public void setReferences(String references) {
        this.references = references;
    }

    public AmpGPISurveyQuestion getAmpQuestionId() {
        return ampQuestionId;
    }

    public void setAmpQuestionId(AmpGPISurveyQuestion ampQuestionId) {
        this.ampQuestionId = ampQuestionId;
    }

    public Long getAmpReponseId() {
        return ampReponseId;
    }

    public void setAmpReponseId(Long ampReponseId) {
        this.ampReponseId = ampReponseId;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public AmpGPISurvey getAmpGPISurveyId() {
        return ampGPISurveyId;
    }

    public void setAmpGPISurveyId(AmpGPISurvey ampGPISurveyId) {
        this.ampGPISurveyId = ampGPISurveyId;
    }

    public static class AmpGPISurveyResponseComparator implements Comparator<AmpGPISurveyResponse>, Serializable {

        private static final long serialVersionUID = 1L;

        @Override
        public int compare(AmpGPISurveyResponse arg0, AmpGPISurveyResponse arg1) {
            if (arg0.getAmpQuestionId() != null && arg1.getAmpQuestionId() != null) {
                return arg0.getAmpQuestionId().getQuestionNumber().compareTo(arg1.getAmpQuestionId().getQuestionNumber());
            }
            return arg0.hashCode() - arg1.hashCode();
        }

    }

    @Override
    public boolean equalsForVersioning(Object obj) {
        AmpGPISurveyResponse auxResponse = (AmpGPISurveyResponse) obj;
        String val1 = this.response != null ? this.response : "";
        String val2 = auxResponse.getResponse() != null ? auxResponse.getResponse() : "";
        if (this.ampQuestionId.equals(auxResponse.getAmpQuestionId()) && val1.equals(val2)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Object getValue() {
        return response;
    }

    @Override
    public Output getOutput() {
        Output out = new Output();
        out.setTitle(new String[] { " Q", this.ampQuestionId.getQuestionNumber().toString(), ": " });
        out.setOutputs(new ArrayList<Output>());
        /*
         * if (this.response != null && !this.response.equals("nil")) {
         * out.getOutputs().add(new Output(null, new String[] {
         * "Response:&nbsp;" }, new Object[] { this.getResponse() })); }
         */
        out.setValue(new Object[] { this.getResponse() != null ? this.getResponse() : "" });
        return out;
    }

    @Override
    public Object prepareMerge(AmpActivityVersion newActivity) {
        // TODO Auto-generated method stub
        return this;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return super.clone();
    }
}
