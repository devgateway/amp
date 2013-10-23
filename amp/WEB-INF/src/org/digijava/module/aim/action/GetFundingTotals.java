package org.digijava.module.aim.action;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.logic.FundingCalculationsHelper;
import org.digijava.module.aim.util.ActivityUtil;
import java.util.ArrayList;
import java.util.Iterator;
import javax.servlet.http.HttpSession;
import org.digijava.module.aim.helper.Components;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.RegionalFunding;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;

/**
 *
 * @author medea
 */
public class GetFundingTotals extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        //called from regional or component pages
        String isRegCurr=request.getParameter("isRegcurr");
         // called from popup or from step page
        String stepPage=request.getParameter("isStepPage");
        String curr=null;
        HttpSession session = request.getSession();
        TeamMember tm = (TeamMember) session.getAttribute(Constants.CURRENT_MEMBER);
        String defCurr = DbUtil.getTeamAppSettings(tm.getTeamId()).getCurrency().getCurrencyCode();
        EditActivityForm eaForm = (EditActivityForm) form;
         if(isRegCurr.equals("true")){
            curr=eaForm.getRegFundingPageCurrCode();
             if (stepPage.equals("false")) {
                 eaForm.setRegFundingPageCurrCode(defCurr);
             }
        }
         else{
            curr=eaForm.getFundingCurrCode();
            if (stepPage.equals("false")) {
                 eaForm.setFundingCurrCode(defCurr);
             }
         }
        FundingCalculationsHelper cal = new FundingCalculationsHelper();
        FundingCalculationsHelper calComp = new FundingCalculationsHelper();
        FundingCalculationsHelper calReg = new FundingCalculationsHelper();
        Collection fundDets = eaForm.getFunding().getFundingDetails();
        Collection<AmpFundingDetail> ampFundDets = ActivityUtil.createAmpFundingDetails(fundDets);
        cal.doCalculations(ampFundDets, curr, true);
        Collection<AmpFundingDetail> compFundingDets = new ArrayList<AmpFundingDetail>();
        Collection<AmpFundingDetail> regionalFundingDets = new ArrayList<AmpFundingDetail>();
        Collection<FundingDetail> compFunDets = null;
        if (eaForm.getComponents() != null && eaForm.getComponents().getSelectedComponents() != null) {
            Iterator<Components<FundingDetail>> itr = eaForm.getComponents().getSelectedComponents().iterator();
            while (itr.hasNext()) {
                Components<FundingDetail> comp = itr.next();
                if (comp.getDisbursements() != null && comp.getDisbursements().size() > 0) {
                    compFunDets = comp.getDisbursements();
                    Iterator<FundingDetail> detIter = compFunDets.iterator();
                    while (detIter.hasNext()) {
                        FundingDetail det = detIter.next();
                        det.setTransactionType(Constants.DISBURSEMENT);
                    }
                }
                compFundingDets .addAll(ActivityUtil.createAmpFundingDetails(compFunDets));

            }
        }
        Collection<FundingDetail> regFunDets=new ArrayList<FundingDetail>();
        if (eaForm.getFunding() != null && eaForm.getFunding().getRegionalFundings() != null) {
            Iterator<RegionalFunding> iterRegFund=eaForm.getFunding().getRegionalFundings().iterator();
            while(iterRegFund.hasNext()){
                AmpFundingDetail det=new AmpFundingDetail();
                 RegionalFunding  regFdet = iterRegFund.next();
                 if (regFdet.getDisbursements() != null && regFdet.getDisbursements().size() > 0) {
                    Iterator<FundingDetail> detIter = regFdet.getDisbursements().iterator();
                    while (detIter.hasNext()) {
                        FundingDetail regDet = detIter.next();
                        regDet.setTransactionType(Constants.DISBURSEMENT);
                        regFunDets.add(regDet);
                    }
                }
            }
            regionalFundingDets = ActivityUtil.createAmpFundingDetails(regFunDets);
        }
        response.setContentType("text/xml");
        OutputStreamWriter outputStream = new OutputStreamWriter(response.getOutputStream());
        PrintWriter out = new PrintWriter(outputStream, true);
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        xml += "<" + "total comm=\"" + FormatHelper.formatNumber(cal.getTotActualComm().doubleValue()) + "\" ";
        xml += " totalComm=\"" + FormatHelper.formatNumber(cal.getTotalCommitments().doubleValue()) + "\" ";
        xml += "disb=\"" + FormatHelper.formatNumber(cal.getTotActualDisb().doubleValue()) + "\" ";
        xml += "expn=\"" + FormatHelper.formatNumber(cal.getTotActualExp().doubleValue()) + "\" ";
        xml += "curr=\"" + curr + "\" ";
        calComp.doCalculations(compFundingDets, curr, true);
        xml += "comp_disb=\"" + FormatHelper.formatNumber(calComp.getTotActualDisb().doubleValue()) + "\" ";
        calReg.doCalculations(regionalFundingDets, curr, true);
        xml += "regional_disb=\"" + FormatHelper.formatNumber(calReg.getTotActualDisb().doubleValue()) + "\" ";
        xml += "/>";
        out.println(xml);
        out.close();
        // return xml
        outputStream.close();
       
        return null;

    }
}
