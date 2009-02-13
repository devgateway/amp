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
        response.setContentType("text/xml");
        OutputStreamWriter outputStream = new OutputStreamWriter(response.getOutputStream());
        PrintWriter out = new PrintWriter(outputStream, true);
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        xml += "<" + "total comm=\"" + FormatHelper.formatNumber(cal.getTotActualComm().doubleValue()) + "\" ";
        xml += "disb=\"" + FormatHelper.formatNumber(cal.getTotActualDisb().doubleValue())+ "\" ";
        xml += "expn=\"" +FormatHelper.formatNumber(cal.getTotActualExp().doubleValue()) + "\" ";
        xml += "curr=\"" + eaForm.getFundingCurrCode() + "\" ";
        xml += "/>";
        out.println(xml);
        out.close();
        // return xml
        outputStream.close();

        return null;

    }
}
