package org.digijava.module.aim.action;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.aim.helper.RegionalFunding;
import org.digijava.module.aim.logic.FundingCalculationsHelper;
import org.digijava.module.aim.util.ActivityUtil;

public class ValidateFundingAmounts extends Action {
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws Exception {
		EditActivityForm eaForm = (EditActivityForm) form;
		String curr=eaForm.getRegFundingPageCurrCode();
		FundingCalculationsHelper cal = new FundingCalculationsHelper();
        FundingCalculationsHelper calReg = new FundingCalculationsHelper();
        
        Collection fundDets = eaForm.getFunding().getFundingDetails();
        Collection<AmpFundingDetail> ampFundDets = ActivityUtil.createAmpFundingDetails(fundDets);
        cal.doCalculations(ampFundDets, curr);
        
        Collection<AmpFundingDetail> regionalFundingDets = new ArrayList<AmpFundingDetail>();
        Collection<FundingDetail> regFunDets=new ArrayList<FundingDetail>();
        if (eaForm.getFunding() != null && eaForm.getFunding().getRegionalFundings() != null) {
            Iterator<RegionalFunding> iterRegFund=eaForm.getFunding().getRegionalFundings().iterator();
            while(iterRegFund.hasNext()){
                //AmpFundingDetail det=new AmpFundingDetail();
                 RegionalFunding  regFdet = iterRegFund.next();
                 if (regFdet.getCommitments() != null && regFdet.getCommitments().size() > 0) {
                    Iterator<FundingDetail> detIter = regFdet.getCommitments().iterator();
                    while (detIter.hasNext()) {
                        FundingDetail regDet = detIter.next();
                        regDet.setTransactionType(Constants.COMMITMENT);
                        regFunDets.add(regDet);
                    }
                }
                
                 if (regFdet.getDisbursements() != null && regFdet.getDisbursements().size() > 0) {
                     Iterator<FundingDetail> detIter = regFdet.getDisbursements().iterator();
                     while (detIter.hasNext()) {
                         FundingDetail regDet = detIter.next();
                         regDet.setTransactionType(Constants.DISBURSEMENT);
                         regFunDets.add(regDet);
                     }
                 }
                 
                 if (regFdet.getExpenditures() != null && regFdet.getExpenditures().size() > 0) {
                     Iterator<FundingDetail> detIter = regFdet.getExpenditures().iterator();
                     while (detIter.hasNext()) {
                         FundingDetail regDet = detIter.next();
                         regDet.setTransactionType(Constants.EXPENDITURE);
                         regFunDets.add(regDet);
                     }
                 }
            }
            regionalFundingDets = ActivityUtil.createAmpFundingDetails(regFunDets);            
        }        
        calReg.doCalculations(regionalFundingDets, curr);
        
        String alertNeeded="";
        double totalActCommitments = cal.getTotActualComm().doubleValue();
        double totalActRegionalCommitments = calReg.getTotActualComm().doubleValue();
        
        if(totalActRegionalCommitments>totalActCommitments){
        	alertNeeded+=TranslatorWorker.translateText("Sum of actual commitments entered on Regional Fudning step is greater then total actual commitments", request);
        	alertNeeded+="\n";
        }
        double totalActDisbursements = cal.getTotActualDisb().doubleValue();
        double totalActRegionalDisbursements = calReg.getTotActualDisb().doubleValue();
        if(totalActRegionalDisbursements>totalActDisbursements){
        	alertNeeded+=TranslatorWorker.translateText("Sum of actual disbursements entered on Regional Fudning step is greater then total actual disbursements", request);
        	alertNeeded+="\n";
        }
        
        double totalActExpenditures = cal.getTotActualExp().doubleValue();
        double totalActRegionalExpenditures = calReg.getTotActualExp().doubleValue();
        if(totalActRegionalExpenditures>totalActExpenditures){
        	alertNeeded+=TranslatorWorker.translateText("Sum of actual expenditures entered on Regional Fudning step is greater then total actual expenditures", request);
        	alertNeeded+="\n";
        }
        
        if(alertNeeded.length()>0){
        	alertNeeded+=TranslatorWorker.translateText("Are you sure you want to save activity ?", request);;
        }else{
         	alertNeeded="No_Alert_Needed";
        }
        
        response.setContentType("text/plain");
        OutputStreamWriter outputStream = new OutputStreamWriter(response.getOutputStream());
        PrintWriter out = new PrintWriter(outputStream, true);
        String xml = alertNeeded;
        out.println(xml);
        out.close();
        outputStream.close();       
       
		return null;
	}
}
