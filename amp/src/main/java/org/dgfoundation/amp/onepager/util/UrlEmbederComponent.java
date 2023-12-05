package org.dgfoundation.amp.onepager.util;

/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 * 
 * @author aartimon@dginternational.org
 * @since Sep 28, 2011
 */

import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnLoadHeaderItem;
import org.apache.wicket.markup.html.basic.Label;

public class UrlEmbederComponent extends Label {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(UrlEmbederComponent.class);

    private String _url;

    public UrlEmbederComponent(String id, final String url) {
        this(id, url, "");
    }
    public UrlEmbederComponent(String id, final String url, final String callBackJs) {
        super(id);
        this._url = url;
        setRenderBodyOnly(false);
        setOutputMarkupId(true);
        final String markupId = this.getMarkupId(true);
        this.add(new Behavior() {
            
            @Override
            public void renderHead(Component component, IHeaderResponse response) {
                super.renderHead(component, response);
                response.render(OnLoadHeaderItem.forScript("$.ajaxSetup({cache: false});$(\"#" + markupId + "\").html(\"\").load(\""+ url +"\", function() { " + callBackJs + " });$.ajaxSetup({cache: true});"));
            }
        });
    }
}
