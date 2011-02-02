/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.tables;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.features.tables.AmpFormTableFeaturePanel;
import org.dgfoundation.amp.permissionmanager.components.features.models.AmpPMGateWrapper;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.gateperm.core.CompositePermission;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.core.GatePermission;
import org.digijava.module.gateperm.gates.OrgRoleGate;
import org.digijava.module.gateperm.gates.UserLevelGate;

/**
 * @author dan
 *
 */
public class AmpPMAddPermFormTableFeaturePanel extends AmpFormTableFeaturePanel {

	public AmpPMAddPermFormTableFeaturePanel(String id, IModel<Set<AmpPMGateWrapper>> gatesSetModel, String fmName, boolean hideLeadingNewLine) {
		super(id, gatesSetModel, fmName, hideLeadingNewLine);
		AbstractReadOnlyModel<List<AmpPMGateWrapper>> gatesListReadOnlyModel = OnePagerUtil.getReadOnlyListModelFromSetModel(gatesSetModel);
		
				
		list = new ListView<AmpPMGateWrapper>("permGatesList", gatesListReadOnlyModel) {
			@Override
			protected void populateItem(final ListItem<AmpPMGateWrapper> item) {
				item.add(new Label("gateName", item.getModelObject().getName()));
				CheckBox read =	new CheckBox("gateReadFlag", new PropertyModel(item.getModelObject(), "readFlag"));
				read.setOutputMarkupId(true);
				item.add(read);
				CheckBox edit =	new CheckBox("gateEditFlag", new PropertyModel(item.getModelObject(), "editFlag"));
				edit.setOutputMarkupId(true);
				item.add(edit);
				
				System.out.println("read Flag: "+ item.getModelObject().getReadFlag());
				System.out.println("edit Flag: "+ item.getModelObject().getEditFlag());
			}
		};
		list.setReuseItems(true);
		list.setOutputMarkupId(true);
		add(list);
		
	}
	
}
