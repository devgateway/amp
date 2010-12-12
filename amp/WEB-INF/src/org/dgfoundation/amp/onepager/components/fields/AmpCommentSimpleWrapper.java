/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.fields;

import java.util.List;

import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.model.IModel;
import org.digijava.module.aim.dbentity.AmpActivity;

/**
 * @author aartimon@dginternational.org
 * since Oct 19, 2010
 */
public class AmpCommentSimpleWrapper extends AmpFieldPanel {

	public AmpCommentSimpleWrapper(String id, String fmName, IModel<AmpActivity> activityModel) {
		super(id, fmName, true);
		AmpCommentPanel acp = new AmpCommentPanel("comments", fmName, activityModel);
		acp.setOutputMarkupId(true);
		add(acp);
	}

}
