package org.digijava.module.aim.helper ;
import java.util.Collection;

public class ProjectTermAssist
{
    private String termAssistName;
    private Collection termAssistFund;
    private String termCommAmount;
    private String termDisbAmount;
    private String termPlannedDisbAmount;
    private String termExpAmount;
    private String termUnDisbAmount;

    
    public String getTermAssistName() 
    {
        return termAssistName;
    }

    public Collection getTermAssistFund() 
    {
        return termAssistFund;
    }

    public String getTermCommAmount() {
        return termCommAmount;
    }

    public String getTermDisbAmount() {
        return termDisbAmount;
    }

    public String getTermPlannedDisbAmount() {
        return termPlannedDisbAmount;
    }

    public String getTermExpAmount() {
        return termExpAmount;
    }

    public String getTermUnDisbAmount() {
        return termUnDisbAmount;
    }

    public void setTermAssistName(String s) {
        termAssistName = s;
    }

    public void setTermAssistFund(Collection c) {
        termAssistFund = c;
    }

    public void setTermCommAmount(String s) {
        termCommAmount = s;
    }

    public void setTermDisbAmount(String s) {
        termDisbAmount = s;
    }

    public void setTermPlannedDisbAmount(String s) {
        termPlannedDisbAmount = s;
    }

    public void setTermExpAmount(String s) {
        termExpAmount = s;
    }

    public void setTermUnDisbAmount(String s) {
        termUnDisbAmount = s;
    }
}
