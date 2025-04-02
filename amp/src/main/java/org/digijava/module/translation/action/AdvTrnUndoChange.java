package org.digijava.module.translation.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.translator.util.TrnUtil;
import org.digijava.module.translation.form.NewAdvancedTrnForm;
import org.digijava.module.translation.util.ListChangesBuffer;

public class AdvTrnUndoChange extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        NewAdvancedTrnForm trnForm = (NewAdvancedTrnForm)form;
        
        ListChangesBuffer<String, Message> buffer = TrnUtil.getBuffer(request.getSession());
        
        String[] changes = trnForm.getUndoChanges();
        if (changes!=null){
            for (String change : changes) {
                buffer.undoOperationFor(change);
            }
        }
        return null;
    }

}
