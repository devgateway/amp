package org.digijava.module.gis.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.gis.form.AdditionalGraphicsForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: flyer
 * Date: 9/30/11
 * Time: 8:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class ShowAdditionalGraphics extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        AdditionalGraphicsForm additionalGraphicsForm = (AdditionalGraphicsForm) form;
        String action = (additionalGraphicsForm.getActionType() != null && !additionalGraphicsForm.getActionType().isEmpty()) ?
                additionalGraphicsForm.getActionType() : "unspecified";

        if (action.equalsIgnoreCase("legendGradient")) {

        }

        return null;
    }
}
