/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.translation;

import java.util.HashMap;
import java.util.Locale;
import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.util.template.JavaScriptTemplate;
import org.apache.wicket.util.template.PackageTextTemplate;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.util.ActivityGatekeeper;
import org.dgfoundation.amp.onepager.web.pages.AmpHeaderFooter;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

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

	public static final String JS_FILE_NAME = "ampAjaxBehavior.js";
	
	
	/**
	 * The respond message called, by the translator component, through AJAX. 
	 */
	@Override
	protected void respond(AjaxRequestTarget target) {
		
		Request request = RequestCycle.get().getRequest();
		String method = request.getRequestParameters().getParameterValue("method").toString();
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
		String key = request.getRequestParameters().getParameterValue("editorKey").toString();
		String message = request.getRequestParameters().getParameterValue("editorVal").toString();
		String pLabelId = request.getRequestParameters().getParameterValue("labelId").toString();
		
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
		
		target.appendJavaScript("updateLabel(\""+pLabelId+"\", \""+message+"\");showLabel(\""+pLabelId+"\");window.status='';");
	}
	
	private void switchTranslatorMode(Request request, AjaxRequestTarget target){
		((AmpAuthWebSession) Session.get()).switchTranslatorMode();
		
		String id = request.getRequestParameters().getParameterValue("activity").toString();
		ActivityGatekeeper.pageModeChange(id);
		
		target.appendJavaScript("window.location.reload()");
	}

	private void switchFMMode(Request request, AjaxRequestTarget target){
		((AmpAuthWebSession) Session.get()).switchFMMode();
		
		String id = request.getRequestParameters().getParameterValue("activity").toString();
		ActivityGatekeeper.pageModeChange(id);
		Component comp = getComponent();
		target.add(comp);
		//target.appendJavaScript("window.location.reload()");
	}
	
	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		super.renderHead(component, response);
		
		CharSequence callBackUrl = getCallbackUrl();
		
		HashMap<String, Object> variables = new HashMap<String, Object>();
		String activityFormOnePager = "false";
		try {
			activityFormOnePager = FeaturesUtil.getGlobalSettingValue(
					GlobalSettingsConstants.ACTIVITY_FORM_ONE_PAGER);
		} catch (Exception ignored) {}
		variables.put("onepagerMode", activityFormOnePager);
		variables.put("onepagerPath", "/" + OnePagerConst.ONEPAGER_URL_PREFIX + "/" + OnePagerConst.ONEPAGER_URL_PARAMETER_ACTIVITY + "/");
		variables.put("callBackUrl", callBackUrl);		
		
		PackageTextTemplate ptt = new PackageTextTemplate(AmpAjaxBehavior.class, JS_FILE_NAME);
		ptt.interpolate(variables);
		JavaScriptTemplate jst = new JavaScriptTemplate(ptt);
		response.renderString(jst.asString());

	}
}
