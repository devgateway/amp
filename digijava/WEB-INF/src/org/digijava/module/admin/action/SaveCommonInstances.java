/*
 *   SaveCommonInstances.java
 *   @Author Maka Kharalashvili maka@powerdot.org
 * 	 Created: Jul 7, 2004
 * 	 CVS-ID: $Id: SaveCommonInstances.java,v 1.1 2005-07-06 10:34:10 rahul Exp $
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
package org.digijava.module.admin.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.SiteCache;
import org.digijava.module.admin.form.CommonInstancesForm;
import org.digijava.module.admin.util.DbUtil;

public class SaveCommonInstances
      extends Action {

    private static Logger logger = Logger.getLogger(SaveCommonInstances.class);

    public ActionForward execute(ActionMapping mapping,
				 ActionForm form,
				 javax.servlet.http.HttpServletRequest request,
				 javax.servlet.http.HttpServletResponse
				 response) throws java.lang.Exception {

	if (!DgUtil.isModuleInstanceAdministrator(request)) {
	    return new ActionForward("/admin/index", true);
	}

	CommonInstancesForm formBean = (CommonInstancesForm) form;
	List commonInstances = new ArrayList();

	Iterator iter = formBean.getCommonInstances().iterator();
	while (iter.hasNext()) {
	    CommonInstancesForm.CommonInstanceInfo info = (CommonInstancesForm.
		  CommonInstanceInfo) iter.next();

	    ModuleInstance moduleInstance = new ModuleInstance();

	    moduleInstance.setSite(null);
	    moduleInstance.setPermitted(true);

	    moduleInstance.setInstanceName(info.getInstance());
	    moduleInstance.setModuleInstanceId(info.getId());
	    moduleInstance.setModuleName(info.getModule());
	    moduleInstance.setNumberOfItemsInTeaser(info.getSelectedNumOfItemsInTeaser());

	    commonInstances.add(moduleInstance);
	}

	DbUtil.editCommonInstances(commonInstances);
	SiteCache.getInstance().load();

	return mapping.findForward("forward");
    }
}