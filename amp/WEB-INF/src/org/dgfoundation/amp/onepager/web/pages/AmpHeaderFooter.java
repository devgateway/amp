/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.web.pages;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.util.template.TextTemplateHeaderContributor;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.components.features.sections.AmpStructuresFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpSubsectionFeaturePanel;
import org.dgfoundation.amp.onepager.translation.AmpAjaxBehavior;
import org.dgfoundation.amp.onepager.util.UrlEmbederComponent;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.wicketstuff.jquery.JQueryBehavior;

/**
 * @author mpostelnicu@dgateway.org
 * @since Sep 22, 2010
 */
public class AmpHeaderFooter extends WebPage {
	public AmpHeaderFooter() {
		
		Cookie[] cookies = ((WebRequest)getRequestCycle().getRequest()).getCookies();
		if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals("digi_language")) {
                	String languageCode = cookies[i].getValue();
                	Session.get().setLocale(new Locale(languageCode));
                    if (languageCode != null) {
                        break;
                    }
                }
            }
        }
		
		add(new JQueryBehavior());
		AmpAjaxBehavior ampajax = new AmpAjaxBehavior();
		add(ampajax);
		final CharSequence callBackUrl = ampajax.getCallbackUrl();

		IModel variablesModel = new AbstractReadOnlyModel() {
			public Map getObject() {
				Map<String, CharSequence> variables = new HashMap<String, CharSequence>(
						2);
				variables.put("callBackUrl", callBackUrl);
				String activityFormOnePager = FeaturesUtil.getGlobalSettingValue(
						GlobalSettingsConstants.ACTIVITY_FORM_ONE_PAGER);
				variables.put("onepagerMode", activityFormOnePager);
				return variables;
			}
		};

		add(TextTemplateHeaderContributor.forJavaScript(AmpAjaxBehavior.class,
				"translations.js", variablesModel));
		add(JavascriptPackageResource.getHeaderContribution("/ckeditor/ckeditor.js"));
		
		add(JavascriptPackageResource.getHeaderContribution(
				AmpSubsectionFeaturePanel.class, "subsectionSlideToggle.js"));
		add(JavascriptPackageResource.getHeaderContribution(
				AmpStructuresFormSectionFeature.class, "gisPopup.js"));
		
		add(new UrlEmbederComponent("wHeader", "/showLayout.do?layout=wicketHeader", "$(\"#switchTranslationMode\").attr('href', 'javascript:wicketSwitchTranslationMode()');$(\"#switchFMMode\").css(\"display\", \"block\");"));
		add(new UrlEmbederComponent("wFooter", "/showLayout.do?layout=wicketFooter"));
	}
	
	public HttpServletRequest getServletRequest(){
		ServletWebRequest servletWebRequest = (ServletWebRequest) getRequest();
		HttpServletRequest request = servletWebRequest.getHttpServletRequest();
		return request;
	}
	
	public HttpSession getHttpSession(){
		HttpSession session = getServletRequest().getSession();
		return session;
	}
}
