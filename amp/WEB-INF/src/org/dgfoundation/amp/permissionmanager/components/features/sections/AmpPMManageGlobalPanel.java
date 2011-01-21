/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.permissionmanager.components.features.sections;

import java.util.Set;

import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.components.fields.AmpFieldPanel;
import org.digijava.module.gateperm.core.Permission;

/**
 * 
 * @author dan 
 */

public class AmpPMManageGlobalPanel extends AmpFieldPanel {
	
	
	
	public AmpPMManageGlobalPanel(String id, IModel<Set<Permission>> globalPermissionsModel, String fmName, boolean hideLabel) {
		super(id, globalPermissionsModel, fmName, hideLabel);
		super.setOutputMarkupId(true);
	}

	public AmpPMManageGlobalPanel(String id, IModel model, String fmName) {
		super(id, model, fmName);
	}

	public AmpPMManageGlobalPanel(String id, String fmName, boolean hideLabel) {
		super(id, fmName, hideLabel);
	}

	public AmpPMManageGlobalPanel(String id, String fmName) {
		super(id, fmName);
	}

}
