/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.web.pages;

import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.components.features.sections.AmpStructuresFormSectionFeature;
import org.dgfoundation.amp.onepager.translation.AmpAjaxBehavior;
import org.dgfoundation.amp.onepager.util.UrlEmbederComponent;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

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
        
        AmpAjaxBehavior ampajax = new AmpAjaxBehavior();
        add(ampajax);
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
        add(new UrlEmbederComponent("wHeader", "/showLayout.do?layout=wicketAdminHeaderLayout", "$(\"#switchTranslationMode\").attr('href', 'javascript:wicketSwitchTranslationMode()');$(\"#switchFMMode\").css(\"display\", \"block\");"));
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
        response.render(JavaScriptHeaderItem.forUrl("/TEMPLATE/ampTemplate/js_2/opentip/opentip-jquery2-4-6.js"));
        response.render(JavaScriptHeaderItem.forReference(new PackageResourceReference(AmpStructuresFormSectionFeature.class, "gisPopup.js")));
    }
    
}
