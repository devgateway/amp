package org.digijava.module.aim.helper ;
import java.util.Collection;


public class TermFund
{
    private String termAssistName;
    private Collection termFundTotal;
    private String totDonorCommAmount;
    private String totDonorDisbAmount;
    private String totDonorUnDisbAmount;
    private String totDonorPlannedDisbAmount;
    private String totDonorExpAmount;

    public String getTermAssistName() {
        return termAssistName;
    }

    public Collection getTermFundTotal() {
        return termFundTotal;
    }

    public void setTermAssistName(String s) {
        termAssistName = s;
    }   
    public void setTermFundTotal(Collection c) {
        termFundTotal = c;
    }

    public String getTotDonorCommAmount() {
        return totDonorCommAmount;
    }

    public String getTotDonorDisbAmount() {
        return totDonorDisbAmount;
    }

    public String getTotDonorUnDisbAmount() {
        return totDonorUnDisbAmount;
    }

    public String getTotDonorPlannedDisbAmount() {
        return totDonorPlannedDisbAmount;
    }

    public String getTotDonorExpAmount() {
        return totDonorExpAmount;
    }

    public void setTotDonorCommAmount(String s) {
        totDonorCommAmount = s;
    }

    public void setTotDonorDisbAmount(String s) {
        totDonorDisbAmount = s;
    }

    public void setTotDonorUnDisbAmount(String s) {
        totDonorUnDisbAmount = s;
    }

    public void setTotDonorPlannedDisbAmount(String s) {
        totDonorPlannedDisbAmount = s;
    }

    public void setTotDonorExpAmount(String s) {
        totDonorExpAmount = s;
    }
    
}
