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
		ListChangesBuffer<String, Message> buffer = TrnUtil.getBuffer(request.getSession());
		if (trnForm.getAddKey()!=null && trnForm.getAddLocale()!=null && trnForm.getAddMessage()!=null){
			//Add new translation to buffer
			Message message = new Message();
			message.setKey(trnForm.getAddKey());
			message.setLocale(trnForm.getAddLocale());
			message.setMessage(trnForm.getAddMessage());
			buffer.operationAdd(message);
		}else{
			//TODO do some error response.
		}
		
		return null;
	}

}
