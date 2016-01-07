package org.digijava.module.esrigis.action;
/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 * @author Diego Dimunzio
 */

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.utils.MultiAction;

public class DataDispatcher extends MultiAction {
	public ActionForward modePrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return null;
	}

	@Override
	public ActionForward modeSelect(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return null;
	}
}
