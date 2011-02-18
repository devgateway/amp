/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.sections;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.features.items.AmpMEItemFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AbstractAmpAutoCompleteTextField;
import org.dgfoundation.amp.onepager.components.fields.AmpComboboxFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.dgfoundation.amp.onepager.models.AmpMEIndicatorSearchModel;
import org.dgfoundation.amp.onepager.models.PersistentObjectModel;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.IndicatorActivity;

/**
 * M&E section
 * @author aartimon@dginternational.org 
 * @since Feb 10, 2011
 */
public class AmpMEFormSectionFeature extends AmpFormSectionFeaturePanel {

	public AmpMEFormSectionFeature(String id, String fmName,
			final IModel<AmpActivity> am) throws Exception {
		super(id, fmName, am);
		final IModel<Set<IndicatorActivity>> setModel = new PropertyModel<Set<IndicatorActivity>>(am, "indicators");
		
		if (setModel.getObject() == null){
			setModel.setObject(new HashSet<IndicatorActivity>());
		}
		AbstractReadOnlyModel<List<IndicatorActivity>> listModel = OnePagerUtil 
				.getReadOnlyListModelFromSetModel(setModel);
		
		final ListView<IndicatorActivity> list = new ListView<IndicatorActivity>("list", listModel) {
			@Override
			protected void populateItem(final ListItem<IndicatorActivity> item) {
				AmpMEItemFeaturePanel indicator = new AmpMEItemFeaturePanel("item", "ME Item", new PersistentObjectModel<IndicatorActivity>(item.getModelObject()));
				item.add(indicator);
				AmpDeleteLinkField deleteLinkField = new AmpDeleteLinkField(
						"delete", "Delete ME Item", new Model<String>("Do you really want to delete this indicator?")) {
					@Override
					public void onClick(AjaxRequestTarget target) {
						setModel.getObject().remove(item.getModelObject());
						target.addComponent(AmpMEFormSectionFeature.this);
						target.appendJavascript(OnePagerConst.getToggleChildrenJS(AmpMEFormSectionFeature.this));
					}
				};
				item.add(deleteLinkField);
			}
		};
		
		list.setReuseItems(true);
		add(list);
		
		final AbstractAmpAutoCompleteTextField<AmpIndicator> autoComplete = new AbstractAmpAutoCompleteTextField<AmpIndicator>(
				AmpMEIndicatorSearchModel.class) {
			private static final long serialVersionUID = 1227775244079125152L;

			@Override
			protected String getChoiceValue(AmpIndicator choice) throws Throwable {
				return choice.getName();
			}

			@Override
			public void onSelect(AjaxRequestTarget target, AmpIndicator choice) {
				IndicatorActivity ia = new IndicatorActivity();
				ia.setActivity(am.getObject());
				ia.setIndicator(choice);
				setModel.getObject().add(ia);
				target.addComponent(list.getParent());
				target.appendJavascript(OnePagerConst.getToggleChildrenJS(AmpMEFormSectionFeature.this));
			}

			@Override
			public Integer getChoiceLevel(AmpIndicator choice) {
				return 0;
			}
		};

		final AmpComboboxFieldPanel<AmpIndicator> searchSectors = new AmpComboboxFieldPanel<AmpIndicator>(
				"search", "Search " + fmName, autoComplete);
		add(searchSectors);
	}

}
