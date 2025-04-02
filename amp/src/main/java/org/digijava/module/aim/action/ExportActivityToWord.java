package org.digijava.module.aim.action;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.FeaturesUtil;

public class ExportActivityToWord extends Action {
    private static Logger logger = Logger.getLogger(ExportActivityToWord.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        EditActivityForm myForm=(EditActivityForm)form;

        if (!FeaturesUtil.showEditableExportFormats()) {
            return mapping.findForward("index");
        }
        
        Long actId=null;
        AmpActivityVersion activity=null;
        if (request.getParameter("activityid") != null) {
            actId = new Long(request.getParameter("activityid"));
        }

        response.setContentType("application/msword");
        response.setHeader("content-disposition", "inline;filename=activity.doc");
        XWPFDocument doc = null;

        try {
            activity = ActivityUtil.loadActivity(actId);

            if (activity != null) {
                doc = new ExportActivityToWordBuilder(activity, myForm, request).render();
            }

            ServletOutputStream out = response.getOutputStream();
            doc.write(out);
            out.flush();

        } catch (IOException e) {
            handleExportException("File data write exception", e);
        } catch (Exception e) {
            handleExportException("Exception", e);
        }

        return null;
    }

    private void handleExportException(String message, Exception e) {
        logger.error(message, e);
    }

}
