package org.digijava.kernel.translator.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.wicket.ajax.json.JSONObject;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.translator.form.AjaxTranslatorForm;
import org.digijava.kernel.translator.form.TranslatorForm;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.I18NHelper;
import org.digijava.kernel.util.RequestUtils;

/**
 * Use this class with translationUtils.js to translate texts inside js files
 * through Ajax calls.
 */
public class AjaxTranslator extends Action {

    private static Logger logger = I18NHelper.getKernelLogger(AjaxTranslator.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        AjaxTranslatorForm tForm = (AjaxTranslatorForm) form;
        String translatedText = "";
        try {
            if (tForm.getOriginalText() != null && tForm.getOriginalText().trim() != "") {
                translatedText = TranslatorWorker.translateText(tForm.getOriginalText());
            }
            response.setContentType("text/json");
            PrintWriter pw = response.getWriter();
            JSONObject json = new JSONObject();
            json.put("text", translatedText);
            pw.write(json.toString());
            pw.flush();
            pw.close();
            //logger.info(tForm.getOriginalText() + "-" + translatedText);
        } catch (Exception e) {
            logger.error(Level.ERROR, e);
        }
        return null;
    }
}
