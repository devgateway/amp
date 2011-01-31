/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.sections;

import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;

/**
 * @author dan
 *
 */
public class AmpPMCheckBoxPanel extends AmpComponentPanel {

	/**
	 * @param id
	 * @param fmName
	 * @param fmType
	 */
	public AmpPMCheckBoxPanel(String id, String fmName, AmpFMTypes fmType) {
		super(id, fmName, fmType);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param fmName
	 */
	public AmpPMCheckBoxPanel(String id, String fmName) {
		super(id, fmName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @param fmBehavior
	 */
	public AmpPMCheckBoxPanel(String id, IModel model, String fmName,
			AmpFMTypes fmBehavior) {
		super(id, model, fmName, fmBehavior);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 */
	public AmpPMCheckBoxPanel(String id, IModel model, String fmName) {
		super(id, model, fmName);
		// TODO Auto-generated constructor stub
	}

}
