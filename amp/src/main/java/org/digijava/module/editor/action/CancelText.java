package org.digijava.module.editor.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.editor.form.EditorForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CancelText extends Action{
@Override
public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)
        throws Exception {
    
    EditorForm formBean = (EditorForm) form;
    return new ActionForward((formBean.getReturnUrl() == null ? "/" : formBean.getReturnUrl()), true);
    }
}
