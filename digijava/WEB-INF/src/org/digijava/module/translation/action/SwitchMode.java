/*
 *   SwitchMode.java
 * 	 @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: jan 26, 2004
 * 	 CVS-ID: $Id: SwitchMode.java,v 1.1 2005-07-06 10:34:14 rahul Exp $
 *
 *   This file is part of DiGi project (www.digijava.org).
 *   DiGi is a multi-site portal system written in Java/J2EE.
 *
 *   Confidential and Proprietary, Subject to the Non-Disclosure
 *   Agreement, Version 1.0, between the Development Gateway
 *   Foundation, Inc and the Recipient -- Copyright 2001-2004 Development
 *   Gateway Foundation, Inc.
 *
 *   Unauthorized Disclosure Prohibited.
 *
 *************************************************************************/
package org.digijava.module.translation.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.Constants;
import org.digijava.module.translation.form.ModeSwitchForm;
import org.digijava.kernel.security.DgSecurityManager;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.translator.TranslatorWorker;
import org.apache.log4j.Logger;

public class SwitchMode
    extends Action {

    private static Logger logger = Logger.getLogger(SwitchMode.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        ModeSwitchForm modeSwitchForm = (ModeSwitchForm) form;

        /**
         * @todo Is this verification required?
         */
        if (DgUtil.isLocalTranslatorForSite(request) || DgUtil.isSiteAdministrator(request)) {
            logger.debug("Setting on-site translation mode to " + modeSwitchForm.isTranslationMode());
            TranslatorWorker.setTranslationMode(request, modeSwitchForm.isTranslationMode());
        } else {
            logger.error("You do not have permission to switch to/from on-site translation mode");
        }

        return new ActionForward(modeSwitchForm.getBackUrl(), true);
    }

}