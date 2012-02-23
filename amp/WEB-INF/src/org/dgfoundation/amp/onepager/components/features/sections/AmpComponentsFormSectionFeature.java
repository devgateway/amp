/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.sections;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.ListEditor;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpComponentField;
import org.dgfoundation.amp.onepager.models.PersistentObjectModel;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpComponent;

/**
 * @author aartimon@dginternational.org since Oct 27, 2010
 */
public class AmpComponentsFormSectionFeature extends
		AmpFormSectionFeaturePanel {

	private static final long serialVersionUID = -6654390083784446344L;

	public AmpComponentsFormSectionFeature(String id, String fmName,
			final IModel<AmpActivityVersion> am) throws Exception {
		super(id, fmName, am);
		final PropertyModel<Set<AmpComponent>> setModel=new PropertyModel<Set<AmpComponent>>(am,"components");
		if (setModel.getObject() == null)
			setModel.setObject(new TreeSet<AmpComponent>());
		final ListEditor<AmpComponent> list;

		
		IModel<List<AmpComponent>> listModel = OnePagerUtil.getReadOnlyListModelFromSetModel(setModel); 

		list = new ListEditor<AmpComponent>("list", setModel, new AmpComponent.AmpComponentComparator()) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onPopulateItem(
					org.dgfoundation.amp.onepager.components.ListItem<AmpComponent> comp) {
				AmpComponentField acf = new AmpComponentField("component", am, PersistentObjectModel.getModel(comp.getModelObject()), "Component");
				comp.add(acf);
			}
		};
		add(list);
		
		AmpAjaxLinkField addbutton = new AmpAjaxLinkField("addbutton", "Add Component", "Add Component") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				AmpComponent comp = new AmpComponent();
				list.addItem(comp);
				target.addComponent(this.getParent());
				target.appendJavascript(OnePagerUtil.getToggleChildrenJS(this.getParent()));
			}
		};
		add(addbutton);
	}

}
