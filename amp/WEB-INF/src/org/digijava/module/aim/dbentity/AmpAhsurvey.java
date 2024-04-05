/*
 * Created on 1/03/2006
 *
 * @author akashs
 *
 */
package org.digijava.module.aim.dbentity;

import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.util.Output;

import java.io.Serializable;
import java.util.*;


public class AmpAhsurvey implements Versionable, Serializable, Cloneable, Comparable<AmpAhsurvey> {

    @Interchangeable(fieldTitle = "ID")
    private Long ampAHSurveyId;

    //IATI-check: to be ignored
    
    //private AmpFunding ampFundingId;
    //private Integer surveyYear;
    //point of delivery donor
    private AmpActivityVersion ampActivityId;
    private AmpOrganisation ampDonorOrgId;
    private AmpOrganisation pointOfDeliveryDonor;
//    @Interchangeable(fieldTitle="Responses", importable = true)
    private Set<AmpAhsurveyResponse> responses;
//  @Interchangeable(fieldTitle="Survey Date", importable = true)
    private Date surveyDate;

    public Date getSurveyDate() {
        return surveyDate;
    }

    public void setSurveyDate(Date surveyDate) {
        this.surveyDate = surveyDate;
    }

    /**
     * @return Returns the ampAHSurveyId.
     */
    public Long getAmpAHSurveyId() {
        return ampAHSurveyId;
    }
    /**
     * @param ampAHSurveyId The ampAHSurveyId to set.
     */
    public void setAmpAHSurveyId(Long ampAHSurveyId) {
        this.ampAHSurveyId = ampAHSurveyId;
    }
    /**
     * @return Returns the responses.
     */
    public Set<AmpAhsurveyResponse> getResponses() {
        return responses;
    }
    /**
     * @param responses The responses to set.
     */
    public void setResponses(Set<AmpAhsurveyResponse> responses) {
        this.responses = responses;
    }
    /**
     * @return Returns the ampActivityId.
     */
    public AmpActivityVersion getAmpActivityId() {
        return ampActivityId;
    }
    /**
     * @param ampActivityId The ampActivityId to set.
     */
    public void setAmpActivityId(AmpActivityVersion ampActivityId) {
        this.ampActivityId = ampActivityId;
    }
    /**
     * @return Returns the ampDonorOrgId.
     */
    public AmpOrganisation getAmpDonorOrgId() {
        return ampDonorOrgId;
    }

    public AmpOrganisation getPointOfDeliveryDonor() {
        return pointOfDeliveryDonor;
    }

    /**
     * @param ampDonorOrgId The ampDonorOrgId to set.
     */
    public void setAmpDonorOrgId(AmpOrganisation ampDonorOrgId) {
        this.ampDonorOrgId = ampDonorOrgId;
    }

    public void setPointOfDeliveryDonor(AmpOrganisation pointOfDeliveryDonor) {
        this.pointOfDeliveryDonor = pointOfDeliveryDonor;
    }
    
   
    @Override
    public boolean equalsForVersioning(Object obj) {
        AmpAhsurvey aux = (AmpAhsurvey) obj;
        if (this.ampDonorOrgId.equals(aux.getAmpDonorOrgId())) {
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    public Object getValue() {

        Comparator surveyComparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                AmpAhsurveyResponse aux1 = (AmpAhsurveyResponse) o1;
                AmpAhsurveyResponse aux2 = (AmpAhsurveyResponse) o2;
                return aux1.getAmpQuestionId().getQuestionNumber().compareTo(aux2.getAmpQuestionId().getQuestionNumber());
            }
        };

        String ret = "";
        if (this.pointOfDeliveryDonor != null)
            ret = this.pointOfDeliveryDonor.getAcronym();
        List<AmpAhsurveyResponse> auxList = new ArrayList<AmpAhsurveyResponse>(this.responses);
        Collections.sort(auxList, surveyComparator);
        Iterator<AmpAhsurveyResponse> iter = auxList.iterator();
        while (iter.hasNext()) {
            AmpAhsurveyResponse auxResponse = iter.next();
            ret = ret
                    + ((auxResponse.getResponse() != null && !auxResponse.getResponse().equals("nil")) ? auxResponse
                            .getResponse() : "-");
        }
        return ret;
    }
    
    @Override
    public Output getOutput() {
        Comparator surveyComparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                AmpAhsurveyResponse aux1 = (AmpAhsurveyResponse) o1;
                AmpAhsurveyResponse aux2 = (AmpAhsurveyResponse) o2;
                return aux1.getAmpQuestionId().getAmpQuestionId().compareTo(aux2.getAmpQuestionId().getAmpQuestionId());
            }
        };

        Output out = new Output();
        out.setOutputs(new ArrayList<Output>());
        out.getOutputs().add(new Output(null, new String[] {"Donor" }, new Object[] { this.ampDonorOrgId.getName() }));
        out.getOutputs().add(new Output(null, new String[] {"PoDD" },
                new Object[] { (this.pointOfDeliveryDonor == null? "" : this.pointOfDeliveryDonor.getName()) }));
        out.getOutputs().add(
                new Output(new ArrayList(), new String[] { "Questions" }, new Object[] { "" }));
        Output questions = out.getOutputs().get(out.getOutputs().size() - 1);

        if (this.responses != null) {
            List<AmpAhsurveyResponse> auxList = new ArrayList<AmpAhsurveyResponse>(this.responses);
            Collections.sort(auxList, surveyComparator);
            Iterator<AmpAhsurveyResponse> iter = auxList.iterator();
            while (iter.hasNext()) {
                AmpAhsurveyResponse auxResponse = iter.next();
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
        AmpAhsurvey aux = (AmpAhsurvey) clone();
        aux.ampActivityId = newActivity;
        aux.ampAHSurveyId = null;
        if (aux.responses != null && aux.responses.size() > 0) {
            Set<AmpAhsurveyResponse> responses = new HashSet<AmpAhsurveyResponse>();
            Iterator<AmpAhsurveyResponse> i = aux.responses.iterator();
            while (i.hasNext()) {
                AmpAhsurveyResponse newResp = (AmpAhsurveyResponse) i.next().clone();
                newResp.setAmpAHSurveyId(aux);
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
    public int compareTo(AmpAhsurvey o) {
        if(this.getAmpActivityId()!=null && o.getAmpAHSurveyId()!=null)
            return this.getAmpAHSurveyId().compareTo(o.getAmpAHSurveyId());
        else return -1;
    }
    
}
