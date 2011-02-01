/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.tables;

import java.io.Serializable;
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
import org.digijava.module.gateperm.core.CompositePermission;
import org.digijava.module.gateperm.gates.OrgRoleGate;
import org.digijava.module.gateperm.gates.UserLevelGate;

/**
 * @author dan
 *
 */
public class AmpPMAddPermFormTableFeaturePanel extends AmpFormTableFeaturePanel {

	private Set<AmpPMGateWrapper> gatesSet = null;
	
	public Set<AmpPMGateWrapper> getGatesSet() {
		return gatesSet;
	}

	public void setGatesSet(Set<AmpPMGateWrapper> gatesSet) {
		this.gatesSet = gatesSet;
	}

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @throws Exception
	 */
	public AmpPMAddPermFormTableFeaturePanel(String id, IModel model,String fmName) throws Exception {
		super(id, model, fmName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @param hideLeadingNewLine
	 * @throws Exception
	 */
	
	public AmpPMAddPermFormTableFeaturePanel(String id, IModel<CompositePermission> cpModel, String fmName, boolean hideLeadingNewLine) {
		super(id, cpModel, fmName, hideLeadingNewLine);

		this.gatesSet = generateDefaultGatesList();
		AbstractReadOnlyModel<List<AmpPMGateWrapper>> gatesSetModel = OnePagerUtil.getReadOnlyListModelFromSetModel(new Model((Serializable) gatesSet));
		
		list = new ListView<AmpPMGateWrapper>("permGatesList", gatesSetModel) {

			@Override
			protected void populateItem(final ListItem<AmpPMGateWrapper> item) {
				item.add(new Label("gateName", item.getModelObject().getName()));
				item.add(new CheckBox("gateReadFlag", new PropertyModel(item.getModelObject(), "readFlag")));
				item.add(new CheckBox("gateEditFlag", new PropertyModel(item.getModelObject(), "editFlag")));
			}
		};
		list.setReuseItems(true);
		add(list);
		
	}
	
	private Set<AmpPMGateWrapper> generateDefaultGatesList(){
		Set<AmpPMGateWrapper> gatesList = new TreeSet<AmpPMGateWrapper>();
		
		gatesList.add(new AmpPMGateWrapper(new Long(1),"Everyone", UserLevelGate.PARAM_EVERYONE, UserLevelGate.class, Boolean.FALSE,Boolean.FALSE));
		gatesList.add(new AmpPMGateWrapper(new Long(2),"Guest", UserLevelGate.PARAM_GUEST, UserLevelGate.class, Boolean.FALSE,Boolean.FALSE));
		gatesList.add(new AmpPMGateWrapper(new Long(3),"Owner", UserLevelGate.PARAM_OWNER, UserLevelGate.class, Boolean.FALSE,Boolean.FALSE));
		gatesList.add(new AmpPMGateWrapper(new Long(4),"Beneficiary Agency","BA",OrgRoleGate.class, Boolean.FALSE,Boolean.FALSE));
		gatesList.add(new AmpPMGateWrapper(new Long(5),"Contracting Agency", "CA", OrgRoleGate.class, Boolean.FALSE,Boolean.FALSE));
		gatesList.add(new AmpPMGateWrapper(new Long(6),"Executing Agency", "EA", OrgRoleGate.class, Boolean.FALSE,Boolean.FALSE));
		gatesList.add(new AmpPMGateWrapper(new Long(7),"Funding Agency", "DN", OrgRoleGate.class, Boolean.FALSE,Boolean.FALSE));
		gatesList.add(new AmpPMGateWrapper(new Long(8),"Implementing Agency", "IA", OrgRoleGate.class, Boolean.FALSE,Boolean.FALSE));
		gatesList.add(new AmpPMGateWrapper(new Long(9),"Responsible Agency", "RO", OrgRoleGate.class, Boolean.FALSE,Boolean.FALSE));
		gatesList.add(new AmpPMGateWrapper(new Long(10),"Regional Group", "RG", OrgRoleGate.class, Boolean.FALSE,Boolean.FALSE));
		gatesList.add(new AmpPMGateWrapper(new Long(11),"Sector Group", "SG", OrgRoleGate.class, Boolean.FALSE,Boolean.FALSE));		
		return gatesList;
	}

}
