package org.digijava.module.translation.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.lucene.LuceneWorker;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.translator.util.TrnUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.translation.util.ListChangesBuffer;
import org.digijava.module.translation.util.ListChangesBuffer.ChangedItem;
import org.digijava.module.translation.util.ListChangesBuffer.Operation;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Executes buffered actions.
 * NOTE: last two imports were added to make code easier for reading.
 * @author Irakli Kobiashvili
 *
 */
public class AdvTrnSaveChanges extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ServletContext context = request.getSession().getServletContext();
        //get current site
        Site site = RequestUtils.getSite(request);
        //get buffer
        ListChangesBuffer<String, Message> buffer = TrnUtil.getBuffer(request.getSession());
        //get changes list from buffer
        List<ChangedItem<String, Message>> changes = buffer.listChanges();
        if (changes==null || changes.size()==0) return null;
        //get translator worker
        TranslatorWorker worker = TranslatorWorker.getInstance("");
        //iterate changes
        for (ChangedItem<String, Message> change : changes) {
            Message message = change.getElement();
            Operation operation = change.getOperation();
            message.setSite(site);
            String suffix =  message.getLocale();
            
            if (operation.equals(Operation.ADD)){
                worker.save(message);
                LuceneWorker.addItemToIndex(message, context,suffix);
            }
            if (operation.equals(Operation.UPDATE)){
                worker.update(message);
                LuceneWorker.deleteItemFromIndex(message, context,suffix);
                LuceneWorker.addItemToIndex(message, context,suffix);
            }
            if (operation.equals(Operation.DELETE)){
                worker.delete(message);
                LuceneWorker.deleteItemFromIndex(message, context,suffix);
            }
            buffer.undoOperation(message);
        }
        
        //buffer.fixChnages(new TrnUtil.TrnDb());
        
        return null;
    }

}
