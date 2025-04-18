package org.digijava.module.help.action;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.help.form.HelpForm;
import org.digijava.module.help.util.HelpUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ViewHelpTopics extends TilesAction {
    
    public ActionForward execute(ComponentContext context, ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HelpForm helpForm=(HelpForm)form;
        String moduleInstance=RequestUtils.getRealModuleInstance(request).getInstanceName();
        helpForm.setTopicTree(HelpUtil.getHelpTopicsTree(RequestUtils.getSite(request), moduleInstance));
        return null;
    }
    
}

