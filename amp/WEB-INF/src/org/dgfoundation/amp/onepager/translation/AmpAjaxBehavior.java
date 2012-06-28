/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.translation;

import java.util.Locale;
import org.apache.log4j.Logger;
import org.apache.wicket.Request;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.util.ActivityGatekeeper;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorWorker;

/**
 * @author aartimon@dginternational.org
 * since Oct 7, 2010
 */
public class AmpAjaxBehavior extends AbstractDefaultAjaxBehavior{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(AmpAjaxBehavior.class);
	
	
	/**
	 * The respond message called, by the translator component, through AJAX. 
	 */
	@Override
	protected void respond(AjaxRequestTarget target) {
	
		Request request = RequestCycle.get().getRequest();
		
		String method = request.getParameter("method");
		if ("translate".compareTo(method.toLowerCase()) == 0){
			translate(request, target);
		}
		else
			if ("switchtranslatormode".compareTo(method.toLowerCase()) == 0){
				switchTranslatorMode(request, target);
			}
			else
				if ("switchfmmode".compareTo(method.toLowerCase()) == 0){
					switchFMMode(request, target);
				}	
		
	}
	
	private void translate(Request request, AjaxRequestTarget target){
		String key = request.getParameter("editorKey");
		String message = request.getParameter("editorVal");
		String pLabelId = request.getParameter("labelId");
		
		AmpAuthWebSession session = (AmpAuthWebSession)Session.get();
		Locale locale = session.getLocale();
		String siteId = String.valueOf(session.getSite().getId());

		TranslatorWorker translatorWorker = TranslatorWorker.getInstance(key);
		Message msg;
		try {
			msg = translatorWorker.getByKey(key, locale.getLanguage(), siteId);
			if (msg != null) {
				msg.setMessage(message);
				msg.setKey(key);
				msg.setCreated(new java.sql.Timestamp(System.currentTimeMillis()));
				msg.setLocale(locale.getLanguage());
				msg.setSiteId(siteId);
				translatorWorker.update(msg);
				
			} else {
				Message newMsg = new Message();
				newMsg.setMessage(message);
				newMsg.setCreated(new java.sql.Timestamp(System.currentTimeMillis()));
				newMsg.setKey(key);
				newMsg.setSiteId(siteId);
				newMsg.setLocale(locale.getLanguage());
				translatorWorker.save(newMsg);
			}
		} catch (WorkerException e1) {
			logger.error("Can't save translation: ", e1);
			message = message + "(not saved due to error!)";
		}
		
		target.appendJavascript("updateLabel(\""+pLabelId+"\", \""+message+"\");showLabel(\""+pLabelId+"\");window.status='';");
	}
	
	private void switchTranslatorMode(Request request, AjaxRequestTarget target){
		((AmpAuthWebSession) Session.get()).switchTranslatorMode();
		
		String id = request.getParameter("activity");
		ActivityGatekeeper.pageModeChange(id);
		
		target.appendJavascript("window.location.reload()");
	}

	private void switchFMMode(Request request, AjaxRequestTarget target){
		((AmpAuthWebSession) Session.get()).switchFMMode();
		
		String id = request.getParameter("activity");
		ActivityGatekeeper.pageModeChange(id);
		
		target.appendJavascript("window.location.reload()");
	}
}
