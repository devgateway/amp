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

import org.apache.log4j.Logger;
import org.apache.wicket.Session;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.behaviors.DocumentReadyBehavior;
import org.dgfoundation.amp.onepager.components.features.sections.AmpStructuresFormSectionFeature;
import org.dgfoundation.amp.onepager.translation.AmpAjaxBehavior;
import org.dgfoundation.amp.onepager.util.UrlEmbederComponent;
import org.digijava.kernel.entity.UserLangPreferences;
import org.digijava.kernel.entity.UserPreferencesPK;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteCache;
import org.hibernate.ObjectNotFoundException;

/**
 * @author mpostelnicu@dgateway.org
 * @since Sep 22, 2010
 */
public class AmpHeaderFooter extends WebPage {
    private static Logger logger = Logger.getLogger(AmpHeaderFooter.class);

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
                	org.digijava.kernel.entity.Locale newLocale = new org.digijava.kernel.entity.Locale();
                	newLocale.setCode(languageCode);
                	//TLSUtils.forceLocaleUpdate(newLocale);
                    if (languageCode != null) {
                    	localeSet = true;
                        break;
                    }
                }
			}

            if (!localeSet){
            	Session.get().setLocale(new Locale(TranslatorWorker.getDefaultLocalCode()));
            	org.digijava.kernel.entity.Locale newLocale = new org.digijava.kernel.entity.Locale();
            	newLocale.setCode(TranslatorWorker.getDefaultLocalCode());
            	//TLSUtils.forceLocaleUpdate(newLocale);
            }
        }
		
		add(new DocumentReadyBehavior());
		AmpAjaxBehavior ampajax = new AmpAjaxBehavior();
		add(ampajax);
		
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
		response.render(JavaScriptHeaderItem.forUrl("/ckeditor/ckeditor.js"));
		response.render(JavaScriptHeaderItem.forReference(new PackageResourceReference(AmpStructuresFormSectionFeature.class, "gisPopup.js")));
	}
}
