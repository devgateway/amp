/**
 * XmlPatchesAction.java
 * (c) 2009 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.digijava.module.xmlpatcher.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.utils.MultiAction;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.xmlpatcher.dbentity.AmpXmlPatch;
import org.digijava.module.xmlpatcher.dbentity.AmpXmlPatchLog;

/**
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
public class XmlPatchesAction extends MultiAction {

    /**
     * 
     */
    public XmlPatchesAction() {
        // TODO Auto-generated constructor stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.dgfoundation.amp.utils.MultiAction#modePrepare(org.apache.struts.
     * action.ActionMapping, org.apache.struts.action.ActionForm,
     * javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward modePrepare(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return modeSelect(mapping, form, request, response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.dgfoundation.amp.utils.MultiAction#modeSelect(org.apache.struts.action
     * .ActionMapping, org.apache.struts.action.ActionForm,
     * javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward modeSelect(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String mode = request.getParameter("mode");
        if ("listPatchLogs".equals(mode))
            return modePatchLogs(mapping, form, request, response);
        if ("patchContents".equals(mode))
            return modePatchContents(mapping, form, request, response);
        if ("logContents".equals(mode))
            return modeLogContents(mapping, form, request, response);
        return modePatchList(mapping, form, request, response);
    }

    private ActionForward modeLogContents(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        String patchLogId = request.getParameter("patchLogId");
        AmpXmlPatchLog log = (AmpXmlPatchLog) DbUtil.getObject(AmpXmlPatchLog.class,
                Long.parseLong(patchLogId));
        request.setAttribute("logContents", log.getLog());
        return mapping.findForward("logContents");
    }

    public ActionForward modePatchContents(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String patchId = request.getParameter("patchId");

        AmpXmlPatch patch = (AmpXmlPatch) DbUtil
                .getObject(AmpXmlPatch.class, patchId);

        String absoluteLocation = this.getServlet().getServletContext()
                .getRealPath("/")
                + patch.getLocation() + patch.getPatchId();

        File f = new File(absoluteLocation);
        if (f.exists()) {
            StringBuilder contents = new StringBuilder();

            try {
                // use buffering, reading one line at a time
                // FileReader always assumes default encoding is OK!

                BufferedReader input = new BufferedReader(new FileReader(f));
                try {
                    String line = null; // not declared within while loop
                    /*
                     * readLine is a bit quirky : it returns the content of a
                     * line MINUS the newline. it returns null only for the END
                     * of the stream. it returns an empty String if two newlines
                     * appear in a row.
                     */
                    while ((line = input.readLine()) != null) {
                        contents.append(line);
                        contents.append(System.getProperty("line.separator"));
                    }
                } finally {
                    input.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            request.setAttribute("patch", patch);
            request.setAttribute("patchContents", contents.toString());
        }
        return mapping.findForward("patchContents");
    }

    public ActionForward modePatchLogs(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String patchId = request.getParameter("patchId");

        AmpXmlPatch patch = (AmpXmlPatch) DbUtil
                .getObject(AmpXmlPatch.class, patchId);
        request.setAttribute("patchLogs", patch.getLogs());

        return mapping.findForward("listPatchLogs");
    }

    public ActionForward modePatchList(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        return mapping.findForward("listPatches");
    }

}
