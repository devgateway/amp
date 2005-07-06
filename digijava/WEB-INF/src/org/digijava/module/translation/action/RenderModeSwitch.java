/*
 *   RenderTeaser.java
 * 	 @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: jan 26, 2004
 * 	 CVS-ID: $Id: RenderModeSwitch.java,v 1.1 2005-07-06 10:34:14 rahul Exp $
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.kernel.Constants;
import org.digijava.kernel.util.DgUtil;
import org.digijava.module.translation.form.ModeSwitchForm;
import org.digijava.kernel.translator.TranslatorWorker;
import org.apache.log4j.Logger;

public class RenderModeSwitch
    extends TilesAction {

    private static Logger logger = Logger.getLogger(RenderModeSwitch.class);

    public ActionForward execute(ComponentContext context,
                                 ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        ModeSwitchForm modeSwitchForm = (ModeSwitchForm) form;

        modeSwitchForm.setTranslationMode(TranslatorWorker.isTranslationMode(request));

        modeSwitchForm.setBackUrl(DgUtil.getFullURL(request));

        return null;
    }

}