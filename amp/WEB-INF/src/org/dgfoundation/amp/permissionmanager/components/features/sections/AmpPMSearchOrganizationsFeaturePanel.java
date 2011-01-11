/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.sections;

import java.util.Set;

import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.digijava.kernel.user.User;

/**
 * @author dan
 *
 */
public class AmpPMSearchOrganizationsFeaturePanel extends AmpFeaturePanel {

	/**
	 * @param id
	 * @param fmName
	 * @throws Exception
	 */
	public AmpPMSearchOrganizationsFeaturePanel(String id, String fmName)
			throws Exception {
		super(id, fmName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @throws Exception
	 */
	public AmpPMSearchOrganizationsFeaturePanel(String id, IModel<Set<User>> model,
			String fmName) throws Exception {
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
	public AmpPMSearchOrganizationsFeaturePanel(String id, IModel<Set<User>> model,
			String fmName, boolean hideLabel) throws Exception {
		super(id, model, fmName, hideLabel);
		// TODO Auto-generated constructor stub
		
		
		
		
		
		
	}

}
