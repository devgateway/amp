package org.dgfoundation.amp.onepager.util;

/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 * 
 * @author aartimon@dginternational.org
 * @since Sep 28, 2011
 */

import org.apache.log4j.Logger;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;

public class UrlEmbederComponent extends Label {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(UrlEmbederComponent.class);

	private String _url;
	
	public UrlEmbederComponent(String id, final String url) {
		super(id);
		this._url = url;
		setRenderBodyOnly(false);
		setOutputMarkupId(true);
		final String markupId = this.getMarkupId(true);
		this.add(new AbstractBehavior() {
			
			@Override
			public void renderHead(IHeaderResponse response) {
				super.renderHead(response);
				response.renderOnLoadJavascript("$(\"#" + markupId + "\").html(\"\").load(\""+ url +"\");");
			}
		});
	}
}
