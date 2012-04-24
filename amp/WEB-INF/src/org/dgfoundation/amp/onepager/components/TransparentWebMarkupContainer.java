/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

/**
 * Container that overrides {@link #isTransparentResolver()} and returns true.
 * Thus adding this component will not affect its children tree
 * @see MarkupContainer#isTransparentResolver()
 * @author mpostelnicu@dgateway.org
 * @since Nov 18, 2010
 */
public class TransparentWebMarkupContainer extends WebMarkupContainer {

	/**
	 * @param id
	 */
	public TransparentWebMarkupContainer(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param model
	 */
	public TransparentWebMarkupContainer(String id, IModel<?> model) {
		super(id, model);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean isTransparentResolver() {
		return true;
	}

}
