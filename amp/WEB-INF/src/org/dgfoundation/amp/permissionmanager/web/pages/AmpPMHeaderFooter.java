/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.web.pages;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.apache.wicket.IPageMap;
import org.apache.wicket.PageParameters;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.util.template.TextTemplateHeaderContributor;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.behaviors.JQueryBehavior;
import org.dgfoundation.amp.onepager.translation.AmpAjaxBehavior;

/**
 * @author dan
 *
 */
public class AmpPMHeaderFooter extends WebPage {

	/**
	 * 
	 */
	public AmpPMHeaderFooter() {
		
		List<Cookie> cookies = ((WebRequest)getRequestCycle().getRequest()).getCookies();
		if (cookies != null) {
			Iterator<Cookie> it = cookies.iterator();
			while (it.hasNext()) {
				Cookie cookie = (Cookie) it.next();
				if (cookie.getName().equals("digi_language")) {
                	String languageCode = cookie.getValue();
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
				Map<String, CharSequence> variables = new HashMap<String, CharSequence>(2);
				variables.put("callBackUrl", callBackUrl);
				String OnePager = "false";
				variables.put("onepagerMode", OnePager);
				return variables;
			}
		};
		//add(TextTemplateHeaderContributor.forJavaScript(AmpAjaxBehavior.class,"translations.js", variablesModel));
		
		
		   add(new IndicatingAjaxLink("fmmode", new Model("FM Mode")) {
			    @Override
			    public void onClick(AjaxRequestTarget arg0) {
			     AmpAuthWebSession session = (AmpAuthWebSession) getSession();
			     if (session.isFmMode())
			      session.setFmMode(false);
			     else
			      session.setFmMode(true);
			     setResponsePage(PermissionManager.class);
			    }
			   });
		
	}
}
