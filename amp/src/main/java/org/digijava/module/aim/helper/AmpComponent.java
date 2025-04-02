package org.digijava.module.aim.helper ;
import java.util.Collection;

public class AmpComponent
{
    private Long ampComponentId;
    private String title;
    private String name;
    private String objective ;
    private String signatureDate;
    private String plannedCompletionDate;
    private Collection status ;
    private Collection issues ;
    private Collection measures ;
    private String acCommitment;
    private String acExpenditure;
    private String acBalance;
    private Collection responsibleActor;
    //used in selectComponent.jsp
    private String ShortName;
    private String description;
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private int count;


    public Long getAmpComponentId() 
    {
        return ampComponentId;
    }

    public void setAmpComponentId(Long l) 
    {
        ampComponentId = l;
    }

    public String getName() {
        return name;
    }

    public String getObjective() {
        return objective;
    }

    public String getAcCommitment() {
        return acCommitment;
    }

    public String getAcExpenditure() {
        return acExpenditure;
    }

    public String getAcBalance() {
        return acBalance;
    }

    public String getSignatureDate() {
        return signatureDate;
    }

    public String getPlannedCompletionDate() {
        return plannedCompletionDate;
    }

    public Collection getStatus() {
        return status;
    }

    public Collection getIssues() {
        return issues;
    }

    public Collection getMeasures() {
        return measures;
    }

    public Collection getResponsibleActor() {
        return responsibleActor;
    }
    
    
    public void setName(String s) {
        name = s;
    }

    public void setAcCommitment(String s) {
        acCommitment = s;
    }

    public void setAcExpenditure(String s) {
        acExpenditure = s;
    }

    public void setAcBalance(String s) {
        acBalance = s;
    }

    public void setObjective(String s) {
        objective = s;
    }

    public void setResponsibleActor(Collection c) {
        responsibleActor = c;
    }

    public void setStatus(Collection c) {
        status=c;
    }

    public void setIssues(Collection c) {
        issues=c;
    }

    public void setMeasures(Collection c) {
        measures=c;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int i) {
        count = i;
    }
    
    public void setSignatureDate(String s) 
    {
        signatureDate = s;
    }

    public void setPlannedCompletionDate(String s) 
    {
        plannedCompletionDate = s;
    }

    public String getShortName() {
        return ShortName;
    }

    public void setShortName(String shortName) {
        ShortName = shortName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    
}
    
