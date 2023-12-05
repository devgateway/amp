package org.digijava.module.translation.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.translator.util.TrnUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.translation.form.NewAdvancedTrnForm;
import org.digijava.module.translation.util.ListChangesBuffer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Add translation to buffer.
 * @author Irakli Kobiashvili
 *
 */
public class AdvTrnAddTranslation extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        NewAdvancedTrnForm trnForm = (NewAdvancedTrnForm)form;
        String key = trnForm.getAddKey();
        String text = trnForm.getAddMessage();
        String locale = trnForm.getAddLocale();
        //check mandatory data
        if (locale != null && text != null){
            String originalText = null;
            Site site = RequestUtils.getSite(request);
            ListChangesBuffer<String, Message> buffer = TrnUtil.getBuffer(request.getSession());
            
            if (key==null){
                //if no key was specified then generate new key form text
                key = TranslatorWorker.generateTrnKey(text);
                originalText = text;
            }else{
                if(locale.equals("en")){
                    originalText = text;
                }else{
                    //get translation with same key but English - default one.
                    Message defaultMsg = TranslatorWorker.getInstance(key).getByKey(key, "en", site);
                    if (defaultMsg!=null){
                        //and use its original text
                        originalText = defaultMsg.getOriginalMessage();
                    }
                }
            }
            //Create new translation
            Message message = new Message();
            message.setKey(key);
            message.setLocale(locale);
            message.setSite(site);
            message.setMessage(text);
            message.setOriginalMessage(originalText);
            //Add new translation to buffer
            buffer.operationAdd(message);
        }else{
            throw new AimException("Incorrect request parameters!");
        }
        
        return null;
    }

}
