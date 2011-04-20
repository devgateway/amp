package org.digijava.module.translation.action;

import java.util.List;

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
import org.digijava.module.translation.util.ListChangesBuffer;
import org.digijava.module.translation.util.ListChangesBuffer.ChangedItem;
import org.digijava.module.translation.util.ListChangesBuffer.Operation;

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

		//get current site
		String siteId = RequestUtils.getSite(request).getId().toString();
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
			message.setSiteId(siteId);
			
			if (operation.equals(Operation.ADD)){
				worker.save(message);
			}
			if (operation.equals(Operation.UPDATE)){
				worker.update(message);
			}
			if (operation.equals(Operation.DELETE)){
				worker.delete(message);
			}
			buffer.undoOperation(message);
		}
		
		//buffer.fixChnages(new TrnUtil.TrnDb());
		
		return null;
	}

}
