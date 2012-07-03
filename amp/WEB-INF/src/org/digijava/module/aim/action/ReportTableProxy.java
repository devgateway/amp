package org.digijava.module.aim.action.publicview;

import org.apache.commons.io.IOUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.services.publicview.PublicViewReportCreator;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * Created by IntelliJ IDEA.
 * User: flyer
 * Date: 3/12/12
 * Time: 9:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class ReportTableProxy extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        FileInputStream htmlFileIS = new FileInputStream(PublicViewReportCreator.getTransformationConfigProperty(PublicViewReportCreator.TRANSFORM_CONF_HTML));
        response.setContentType("text/html");

        ServletOutputStream sos = response.getOutputStream();
        IOUtils.copy(htmlFileIS, sos);
        sos.flush();
        sos.close();

        return null;
    }
}
