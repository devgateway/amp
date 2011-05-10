/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.sections;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.features.items.AmpContractsItemFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.IPAContract;

/**
 * Contracting section
 * @author aartimon@dginternational.org 
 * @since Feb 7, 2011
 */
public class AmpContractingFormSectionFeature extends AmpFormSectionFeaturePanel {

	public AmpContractingFormSectionFeature(String id, String fmName,
			final IModel<AmpActivityVersion> am) throws Exception {
		super(id, fmName, am);
		final IModel<Set<IPAContract>> setModel = new PropertyModel<Set<IPAContract>>(am, "contracts");
		
		if (setModel.getObject() == null){
			setModel.setObject(new HashSet<IPAContract>());
		}
		
		AbstractReadOnlyModel<List<IPAContract>> listModel = OnePagerUtil 
				.getReadOnlyListModelFromSetModel(setModel);
		
		ListView<IPAContract> list = new ListView<IPAContract>("list", listModel) {
			@Override
			protected void populateItem(final ListItem<IPAContract> item) {
				AmpContractsItemFeaturePanel contractItem = new AmpContractsItemFeaturePanel(
							"item", "Contract Item", item.getModel());
				item.add(contractItem);
				
				AmpDeleteLinkField deleteLinkField = new AmpDeleteLinkField(
						"delete", "Delete Contract Item", new Model<String>("Do you really want to delete this contract?")) {
					@Override
					public void onClick(AjaxRequestTarget target) {
						setModel.getObject().remove(item.getModelObject());
						target.addComponent(AmpContractingFormSectionFeature.this);
						target.appendJavascript(OnePagerConst.getToggleChildrenJS(AmpContractingFormSectionFeature.this));
						//list.removeAll();
					}
				};
				item.add(deleteLinkField);

			}
		};
		
		list.setReuseItems(true);
		add(list);
		

		AmpAjaxLinkField addbutton = new AmpAjaxLinkField("add", "Add Contract", "Add Contract") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				IPAContract comp = new IPAContract();
				setModel.getObject().add(comp);
				target.addComponent(this.getParent());
				target.appendJavascript(OnePagerConst.getToggleChildrenJS(AmpContractingFormSectionFeature.this));
			}
		};
		add(addbutton);
	}

}
