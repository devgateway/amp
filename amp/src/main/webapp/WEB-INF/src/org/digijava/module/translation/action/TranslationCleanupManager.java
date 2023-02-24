package org.digijava.module.translation.action;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.lucene.LangSupport;
import org.digijava.kernel.lucene.LuceneWorker;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.translation.form.TranslationCleanupForm;
import org.digijava.module.translation.lucene.TrnLuceneModule;

public class TranslationCleanupManager extends Action {
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        TranslationCleanupForm cleanupForm = (TranslationCleanupForm) form;
        if (request.getParameter("reset") != null) {
            cleanupForm.setDeleteBeforeDate(-1);
        } else {
            // get translator worker
            TranslatorWorker worker = TranslatorWorker.getInstance("");
            ServletContext context = request.getSession().getServletContext();
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, -cleanupForm.getDeleteBeforeDate());
            Date date = cal.getTime();
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            boolean recreateLuceneIndex = worker.deleteMessages(sqlDate);
            if (recreateLuceneIndex) {
                LangSupport[] langs = LangSupport.values();
                for (LangSupport lang : langs) {
                    TrnLuceneModule module = new TrnLuceneModule(lang);
                    LuceneWorker.recreateIndext(module, context);
                }
            }
        }
        return mapping.findForward("forward");

    }

}
