/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.features.subsections;

import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.components.TransparentWebMarkupContainer;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;

/**
 * @author mpostelnicu@dgateway.org
 * since Nov 4, 2010
 */
public abstract class AmpSubsectionFeaturePanel<T> extends AmpFeaturePanel<T> {

	private TransparentWebMarkupContainer slider;

	/**
	 * @param id
	 * @param fmName
	 * @throws Exception
	 */
	public AmpSubsectionFeaturePanel(String id, String fmName) throws Exception {
		this(id, fmName,null);
		
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
		slider = new TransparentWebMarkupContainer("slider");
		slider.setOutputMarkupId(true);
		add(slider);
	}

	public TransparentWebMarkupContainer getSlider() {
		return slider;
	}



}
