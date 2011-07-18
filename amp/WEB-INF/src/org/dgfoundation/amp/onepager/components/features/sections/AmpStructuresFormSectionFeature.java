/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.sections;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpStructureField;
import org.dgfoundation.amp.onepager.models.PersistentObjectModel;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpStructure;

/**
 * @author fferreyra@developmentgateway.org since May 16, 2011
 */
public class AmpStructuresFormSectionFeature extends
		AmpFormSectionFeaturePanel {

	private static final long serialVersionUID = -6654390083754446344L;

	public AmpStructuresFormSectionFeature(String id, String fmName,
			final IModel<AmpActivityVersion> am) throws Exception {
		super(id, fmName, am);
		final PropertyModel<Set<AmpStructure>> setModel=new PropertyModel<Set<AmpStructure>>(am,"structures");
		if (setModel.getObject() == null)
			setModel.setObject(new HashSet());
		final ListView<AmpStructure> list;

		
		IModel<List<AmpStructure>> listModel = new AbstractReadOnlyModel<List<AmpStructure>>() {
			private static final long serialVersionUID = 3706184421459839220L;

			@Override
			public ArrayList<AmpStructure> getObject() {
				return new ArrayList<AmpStructure>(setModel.getObject());
			}
		};
		
		list = new ListView<AmpStructure>("list", listModel) {
			
			@Override
			protected void populateItem(ListItem<AmpStructure> stru) {
				AmpStructureField acf = new AmpStructureField("structure", am, PersistentObjectModel.getModel(stru.getModelObject()), "Structure");
				stru.add(acf);
			}
		};
		add(list);
		
		AmpAjaxLinkField addbutton = new AmpAjaxLinkField("addbutton", "Add Structure", "Add Structure") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				AmpStructure stru = new AmpStructure();
				setModel.getObject().add(stru);
				target.addComponent(this.getParent());
				target.appendJavascript(OnePagerConst.getToggleChildrenJS(this.getParent()));
				list.removeAll();
			}
		};
		add(addbutton);
	}

}
