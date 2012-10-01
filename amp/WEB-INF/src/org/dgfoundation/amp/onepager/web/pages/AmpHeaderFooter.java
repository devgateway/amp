/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.web.pages;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.dgfoundation.amp.onepager.behaviors.DocumentReadyBehavior;
import org.dgfoundation.amp.onepager.behaviors.JQueryBehavior;
import org.dgfoundation.amp.onepager.components.features.sections.AmpStructuresFormSectionFeature;
import org.dgfoundation.amp.onepager.translation.AmpAjaxBehavior;
import org.dgfoundation.amp.onepager.util.UrlEmbederComponent;
import org.digijava.kernel.translator.TranslatorWorker;

/**
 * @author mpostelnicu@dgateway.org
 * @since Sep 22, 2010
 */
public class AmpHeaderFooter extends WebPage {
	public AmpHeaderFooter() {
		List<Cookie> cookies = ((WebRequest)getRequestCycle().getRequest()).getCookies();
		if (cookies != null) {
			boolean localeSet = false;
			Iterator<Cookie> it = cookies.iterator();
			while (it.hasNext()) {
				Cookie cookie = (Cookie) it.next();
				if (cookie.getName().equals("digi_language")) {
                	String languageCode = cookie.getValue();
                	Session.get().setLocale(new Locale(languageCode));
                    if (languageCode != null) {
                    	localeSet = true;
                        break;
                    }
                }
			}
            if (!localeSet)
            	Session.get().setLocale(new Locale(TranslatorWorker.getDefaultLocalCode()));
        }
		
		add(new JQueryBehavior());
		add(new DocumentReadyBehavior());
		AmpAjaxBehavior ampajax = new AmpAjaxBehavior();
		add(ampajax);
		
		//TODO:1.5
		/*
		final CharSequence callBackUrl = ampajax.getCallbackUrl();

		IModel variablesModel = new AbstractReadOnlyModel() {
			public Map getObject() {
				Map<String, CharSequence> variables = new HashMap<String, CharSequence>(
						2);
				variables.put("callBackUrl", callBackUrl);
				String activityFormOnePager = "false";
				try {
					activityFormOnePager = FeaturesUtil.getGlobalSettingValue(
							GlobalSettingsConstants.ACTIVITY_FORM_ONE_PAGER);
				} catch (Exception ignored) {}
				variables.put("onepagerMode", activityFormOnePager);
				variables.put("onepagerPath", "/" + OnePagerConst.ONEPAGER_URL_PREFIX + "/" + OnePagerConst.ONEPAGER_URL_PARAMETER_ACTIVITY + "/");
				return variables;
			}
		};
		add(TextTemplateHeaderContributor.forJavaScript(AmpAjaxBehavior.class,
				"translations.js", variablesModel));
		*/
		add(new UrlEmbederComponent("wHeader", "/showLayout.do?layout=wicketHeader", "$(\"#switchTranslationMode\").attr('href', 'javascript:wicketSwitchTranslationMode()');$(\"#switchFMMode\").css(\"display\", \"block\");"));
		add(new UrlEmbederComponent("wFooter", "/showLayout.do?layout=wicketFooter"));
	}
	
	public HttpServletRequest getServletRequest(){
		ServletWebRequest servletWebRequest = (ServletWebRequest) getRequest();
		HttpServletRequest request = servletWebRequest.getContainerRequest();
		return request;
	}
	
	public HttpSession getHttpSession(){
		HttpSession session = getServletRequest().getSession();
		return session;
	}
	
	@Override
	protected void setHeaders(WebResponse response) {
		super.setHeaders(response);
		response.setHeader("Cache-Control", "no-cache, max-age=0, must-revalidate, no-store");
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		//TODO:1.5
		//response.renderJavaScriptReference(new PackageResourceReference("/ckeditor/ckeditor.js"));
		response.renderJavaScriptReference("/ckeditor/ckeditor.js");
		response.renderJavaScriptReference(new PackageResourceReference(AmpStructuresFormSectionFeature.class, "gisPopup.js"));
	}
}
