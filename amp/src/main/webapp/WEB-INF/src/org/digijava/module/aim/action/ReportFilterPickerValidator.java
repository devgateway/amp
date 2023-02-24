package org.digijava.module.aim.action;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.ReportsFilterPickerForm;
import org.digijava.module.aim.helper.FormatHelper;

public class ReportFilterPickerValidator extends Action {
    private static Logger logger = Logger
            .getLogger(ReportFilterPickerValidator.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response)
            throws java.lang.Exception {
        ReportsFilterPickerForm filterForm = (ReportsFilterPickerForm) form;
        boolean isValid = true;
        try
        {
            if (FormatHelper.parseDate(filterForm.getToDate()).before(
                FormatHelper.parseDate(filterForm.getFromDate()))) {
                isValid = false;
            }
        }
        catch(Exception e)
        {
            logger.error("error parsing filter date", e);
            isValid = false;
        }
        response.setContentType("text/xml");
        StringBuilder result = new StringBuilder();
        result.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        result.append("<result valid='");
        result.append(isValid);
        result.append("'/>");
        OutputStreamWriter outputStream = new OutputStreamWriter(
                response.getOutputStream());
        PrintWriter out = new PrintWriter(outputStream, true);
        out.println(result);
        out.close();
        return null;
    }
}
