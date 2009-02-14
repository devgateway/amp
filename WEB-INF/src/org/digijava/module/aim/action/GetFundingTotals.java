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
import org.digijava.module.aim.helper.Components;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.aim.helper.Constants;

/**
 *
 * @author medea
 */
public class GetFundingTotals extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        EditActivityForm eaForm = (EditActivityForm) form;
        FundingCalculationsHelper cal = new FundingCalculationsHelper();
        Collection fundDets = eaForm.getFunding().getFundingDetails();
        Collection<AmpFundingDetail> ampFundDets = ActivityUtil.createAmpFundingDetails(fundDets);
        cal.doCalculations(ampFundDets, eaForm.getFundingCurrCode());
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
                compFundingDets = ActivityUtil.createAmpFundingDetails(compFunDets);

            }
        }
        if (eaForm.getFunding() != null && eaForm.getFunding().getRegionalFundings() != null) {
            regionalFundingDets = ActivityUtil.createAmpFundingDetails(eaForm.getFunding().getRegionalFundings());
        }
        response.setContentType("text/xml");
        OutputStreamWriter outputStream = new OutputStreamWriter(response.getOutputStream());
        PrintWriter out = new PrintWriter(outputStream, true);
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        xml += "<" + "total comm=\"" + FormatHelper.formatNumber(cal.getTotActualComm().doubleValue()) + "\" ";
        xml += " totalComm=\"" + FormatHelper.formatNumber(cal.getTotalCommitments().doubleValue()) + "\" ";
        xml += "disb=\"" + FormatHelper.formatNumber(cal.getTotActualDisb().doubleValue()) + "\" ";
        xml += "expn=\"" + FormatHelper.formatNumber(cal.getTotActualExp().doubleValue()) + "\" ";
        xml += "curr=\"" + eaForm.getFundingCurrCode() + "\" ";
        cal.doCalculations(compFundingDets, eaForm.getFundingCurrCode());
        xml += "comp_disb=\"" + FormatHelper.formatNumber(cal.getTotActualDisb().doubleValue()) + "\" ";
        cal.doCalculations(regionalFundingDets, eaForm.getFundingCurrCode());
        xml += "regional_disb=\"" + FormatHelper.formatNumber(cal.getTotActualDisb().doubleValue()) + "\" ";
        xml += "/>";
        out.println(xml);
        out.close();
        // return xml
        outputStream.close();

        return null;

    }
}
