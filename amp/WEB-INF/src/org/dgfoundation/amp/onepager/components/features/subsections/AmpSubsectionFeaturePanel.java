/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.features.subsections;

import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;

/**
 * @author mpostelnicu@dgateway.org
 * since Nov 4, 2010
 */
public abstract class AmpSubsectionFeaturePanel<T> extends AmpFeaturePanel<T> {

	/**
	 * @param id
	 * @param fmName
	 * @throws Exception
	 */
	public AmpSubsectionFeaturePanel(String id, String fmName) throws Exception {
		super(id, fmName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param fmName
	 * @param model
	 * @throws Exception
	 */
	public AmpSubsectionFeaturePanel(String id, String fmName, IModel<T> model)
			throws Exception {
		super(id, model, fmName);
		// TODO Auto-generated constructor stub
	}



}
