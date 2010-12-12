/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.translation;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.model.Model;

/**
 * @author aartimon@dginternational.org
 * since Oct 4, 2010
 */
public class LabelTranslatorBehaviour extends AttributeAppender {

	private static final long serialVersionUID = 1L;
	
	private CharSequence key;

	public LabelTranslatorBehaviour() {
		super("onclick", new Model("spawnEditBox(this.id)"), ";");
		
	}
	
}
