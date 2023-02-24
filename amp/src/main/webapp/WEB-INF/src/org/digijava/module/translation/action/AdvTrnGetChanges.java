package org.digijava.module.translation.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.translator.util.TrnUtil;
import org.digijava.module.translation.util.ListChangesBuffer;
import org.digijava.module.translation.util.ListChangesBuffer.ChangedItem;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;

/**
 * Returns HTML for changes list of translation.
 * @author Irakli Kobiashvili
 *
 */
public class AdvTrnGetChanges extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
//      NewAdvancedTrnForm trnForm = (NewAdvancedTrnForm)form;
        String html="<span>"+TranslatorWorker.translateText("no changes")+"</span>"; 
        ListChangesBuffer<String, Message> buffer = TrnUtil.getBuffer(request.getSession());
        
        List<ChangedItem<String, Message>> changes = buffer.listChanges();
        if (changes !=null && changes.size()>0){
            StringBuffer rsp = new StringBuffer("<table class=\"chgangesTable\"><tr>");
            rsp.append("<tr class=\"changesColumnHeader\"><td>");
                        rsp.append(TranslatorWorker.translateText("Undo"));
                        rsp.append("</td><td>");
                        rsp.append(TranslatorWorker.translateText("Oper"));
                        rsp.append("</td><td>");
                        rsp.append(TranslatorWorker.translateText("Text"));
                        rsp.append("</td></tr>");
            for (ChangedItem<String, Message> changedItem : changes) {
                rsp.append("<tr class=\"changesRow\"><td><input type=\"checkbox\" class=\"changedListItem\" value=\"");
                rsp.append(changedItem.getKey());
                rsp.append("\"><td>");
                rsp.append(TranslatorWorker.translateText(changedItem.getOperation().toString()));
                rsp.append("</td><td>");
                rsp.append(changedItem.getElement().getMessage());
                rsp.append("</td></tr>");
            }
            rsp.append("</table>");
            rsp.append("<input id=\"btnUndoSelected\" type=\"button\" value=\"");
                        rsp.append(TranslatorWorker.translateText("Undo selected"));
                        rsp.append("\"/>");
            rsp.append("<input id=\"btnSaveAllChanges\" type=\"button\" value=\"");
                        rsp.append(TranslatorWorker.translateText("Save All Changes"));
                        rsp.append("\"/>");
            
            html = rsp.toString();
        }
        
        response.setContentType("text/html");
        response.getWriter().print(html);
        return null;
    }

}
