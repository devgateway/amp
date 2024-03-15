/*
 * Created on 06.01.2004
 * Local Projects Database
 * Mihai Postelnicu (mihai@ro-gateway.org)
 * (c) 2003 Development Gateway Foundation
 */
package org.dgfoundation.amp.utils;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author Mihai Postelnicu ;
    This abstract class can be used to create ActionS that use multiple forwards to process
 * (especially) forms. The developer has to implement the subcontroller (the modeSelect method) that will
 * make the forward decision. This is how the process works : <br>
 *  - the modePrepare() method is invoked. Because this method will be executed whatever the mode, it's a
 * good place to put object initialisations that are shared by all modes. Also, global verifiers can be
 * posted here. <br>
 *  - before ending the modePrepare method, the servlet can call the modeSelect() method which will choose
 * the appropriate mode to be processed and will invoke the method associated with that mode <br>
 *  - the method will execute and, after processing, will return the forward, that will be returned to the
 * main Struts controller<p>
 * @version 0.2
 */
public abstract class MultiAction extends Action {

    private static final Logger logger = Logger.getLogger(MultiAction.class);

    /**
     * @param mapping object list to be passed to other modes
     * @param form the form (if available) to be passed to other modes
     * @param request
     * @param response
     * @return the forward to be passed to execute() method.
     * This overrides execute() and implements exception catching and some global properties
     * @throws Exception this will be caught in the main execute method
     */
    public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        try {
        return modePrepare(mapping, form, request, response);
        } catch(Exception e) {
            logger.error(e.getMessage(), e);
            return mapping.findForward("error");
        }
    }

    /**
     * @param mapping object list to be passed to other modes
     * @param form the form (if available) to be passed to other modes
     * @param request
     * @param response
     * @return the forward to be passwd to execute() method.
     * This is the default mode that will be implemented in the subclass
     * @throws Exception this will be caught in the main execute method
     */
    public abstract ActionForward modePrepare(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception;

    /**
     * @param mapping object list to be passed to other modes
     * @param form the form (if available) to be passed to other modes
     * @param request
     * @param response
     * @return the forward to be passwd to execute() method.
     * This is returned by the current mode, after execution.
     * Further mode selection is done within this method implementation.
     * @throws Exception this will be caught in the main execute method
     */
    public abstract ActionForward modeSelect(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception;
}
