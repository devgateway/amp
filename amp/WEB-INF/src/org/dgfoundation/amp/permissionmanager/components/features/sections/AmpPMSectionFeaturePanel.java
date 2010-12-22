/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.sections;

import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;

/**
 * @author dan
 *
 */
public class AmpPMSectionFeaturePanel extends AmpFeaturePanel {

	/**
	 * @param id
	 * @param fmName
	 * @throws Exception
	 */
	public AmpPMSectionFeaturePanel(String id, String fmName) throws Exception {
		super(id, fmName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @throws Exception
	 */
	public AmpPMSectionFeaturePanel(String id, IModel model, String fmName)
			throws Exception {
		super(id, model, fmName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @param hideLabel
	 * @throws Exception
	 */
	public AmpPMSectionFeaturePanel(String id, IModel model, String fmName,
			boolean hideLabel) throws Exception {
		super(id, model, fmName, hideLabel);
		// TODO Auto-generated constructor stub
	}

}
