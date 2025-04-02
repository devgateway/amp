package org.digijava.module.translation.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.translator.util.TrnUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.translation.form.NewAdvancedTrnForm;
import org.digijava.module.translation.util.ListChangesBuffer;
import org.digijava.module.translation.util.ListChangesBuffer.Operation;

/**
 * Adds translation as deleted to changes buffer.
 * @author Irakli Kobiashvili
 *
 */
public class AdvTrnDeleteTranslation extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        NewAdvancedTrnForm trnForm = (NewAdvancedTrnForm)form;
        
        String key = trnForm.getAddKey();
        String locale = trnForm.getAddLocale();
        
        if (key != null && locale != null){
            //get buffer
            ListChangesBuffer<String, Message> buffer = TrnUtil.getBuffer(request.getSession());
            Operation operation = buffer.getOperationByKey(key);
            if (operation!=null && operation.equals(Operation.ADD)){
                //just undo cos its only in buffer
                buffer.undoOperationFor(key);
            }else{
                //if not in buffer or in buffer with UPDATE operation
                //then put real message in buffer as DELETED
                Long siteId = RequestUtils.getSite(request).getId();
                //get translation
                Message message = TranslatorWorker.getInstance(key).getByKey(key, locale, siteId);
                //add as deleted
                buffer.operationDelete(message);
            }
            
        }else{
            //TODO do some error response.
        }
        
        return null;
    }

}
