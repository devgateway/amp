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

public class AdvTrnUpdateTranslation extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        NewAdvancedTrnForm trnForm = (NewAdvancedTrnForm) form;
        String key = trnForm.getUpdateKey();
        String newText = trnForm.getUpdateMessage();
        String locale = trnForm.getUpdateLocale();
        Long siteId = RequestUtils.getSite(request).getId();
        ListChangesBuffer<String, Message> buffer = TrnUtil.getBuffer(request
                .getSession());
        Message original = TranslatorWorker.getInstance(key).getByKey(key,locale, siteId);

        // if found and text seems to be changed
        if (original != null && !original.getMessage().equals(newText)) {
            Message editedMessage = new Message();
            editedMessage.setKey(key);
            editedMessage.setLocale(locale);
            editedMessage.setMessage(newText);
            editedMessage.setAmpOffline(original.getAmpOffline());
            editedMessage.setOriginalMessage(original.getOriginalMessage());
            buffer.operationUpdate(editedMessage);
        }

        return null;
    }
}
