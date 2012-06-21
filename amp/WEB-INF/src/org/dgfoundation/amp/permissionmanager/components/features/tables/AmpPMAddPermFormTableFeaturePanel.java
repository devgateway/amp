/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.tables;

import java.util.List;
import java.util.Set;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.features.tables.AmpFormTableFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpCheckBoxFieldPanel;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.dgfoundation.amp.permissionmanager.components.features.models.AmpPMReadEditWrapper;

/**
 * @author dan
 *
 */
public class AmpPMAddPermFormTableFeaturePanel extends AmpFormTableFeaturePanel {

	public AmpPMAddPermFormTableFeaturePanel(String id, IModel<Set<AmpPMReadEditWrapper>> gatesSetModel, String fmName, boolean hideLeadingNewLine) {
		super(id, gatesSetModel, fmName, hideLeadingNewLine);
		AbstractReadOnlyModel<List<AmpPMReadEditWrapper>> gatesListReadOnlyModel = OnePagerUtil.getReadOnlyListModelFromSetModel(gatesSetModel);
		
		list = new ListView<AmpPMReadEditWrapper>("permGatesList", gatesListReadOnlyModel) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 761176352811271991L;

			@Override
			protected void populateItem(final ListItem<AmpPMReadEditWrapper> item) {
				item.add(new Label("gateName", TranslatorUtil.getTranslation(item.getModelObject().getName())   ));
				
				CheckBox read =	new CheckBox("gateReadFlag", new PropertyModel(item.getModelObject(), "readFlag"));
				read.setOutputMarkupId(true);
				item.add(read);
				CheckBox edit =	new CheckBox("gateEditFlag", new PropertyModel(item.getModelObject(), "editFlag"));
				edit.setOutputMarkupId(true);
				item.add(edit);
			}
		};
		list.setOutputMarkupId(true);
		add(list);
		
	}
	
}
