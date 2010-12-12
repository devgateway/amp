/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.tables;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.fields.AbstractAmpAutoCompleteTextField;
import org.dgfoundation.amp.onepager.components.fields.AmpComboboxFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.dgfoundation.amp.onepager.models.AbstractAmpAutoCompleteModel;
import org.dgfoundation.amp.onepager.models.AmpSectorSearchModel;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpSector;

/**
 * @author mpostelnicu@dgateway.org since Oct 20, 2010
 */
public class AmpSectorsFormTableFeature extends
		AmpFormTableFeaturePanel<AmpActivity,AmpActivitySector> {

	/**
	 * @param id
	 * @param fmName
	 * @param am
	 * @throws Exception
	 */
	public AmpSectorsFormTableFeature(String id, String fmName,
			final IModel<AmpActivity> am,
			final AmpClassificationConfiguration sectorClassification)
			throws Exception {
		super(id, am, fmName);
		final IModel<Set<AmpActivitySector>> setModel = new PropertyModel<Set<AmpActivitySector>>(
				am, "sectors");

		IModel<List<AmpActivitySector>> listModel = new AbstractReadOnlyModel<List<AmpActivitySector>>() {

			@Override
			public List<AmpActivitySector> getObject() {
				// remove sectors with other classification
				ArrayList<AmpActivitySector> ret = new ArrayList<AmpActivitySector>();

				for (AmpActivitySector ampActivitySector : setModel.getObject()) {
					if (ampActivitySector.getClassificationConfig().getId()
							.equals(sectorClassification.getId()))
						ret.add(ampActivitySector);
				}
				return ret;
			}
		};

		list = new ListView<AmpActivitySector>("listSectors", listModel) {

			@Override
			protected void populateItem(final ListItem<AmpActivitySector> item) {
				final MarkupContainer listParent = this.getParent();
				item.add(new TextField<String>("percentage",
						new PropertyModel<String>(item.getModel(),
								"sectorPercentage")));
				item.add(new Label("sectorLabel", item.getModelObject()
						.getSectorId().getName()));

				AmpDeleteLinkField delSector = new AmpDeleteLinkField(
						"delSector", "Delete Sector") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						setModel.getObject().remove(item.getModelObject());
						target.addComponent(listParent);
						list.removeAll();
					}
				};
				item.add(delSector);

			}
		};
		list.setReuseItems(true);
		add(list);

		final AbstractAmpAutoCompleteTextField<AmpSector> autoComplete = new AbstractAmpAutoCompleteTextField<AmpSector>(
				AmpSectorSearchModel.class) {
			private static final long serialVersionUID = 1227775244079125152L;

			@Override
			protected String getChoiceValue(AmpSector choice) throws Throwable {
				return choice.getName();
			}

			@Override
			public void onSelect(AjaxRequestTarget target, AmpSector choice) {
				AmpActivitySector activitySector = new AmpActivitySector();
				activitySector.setSectorId(choice);
				activitySector.setActivityId(am.getObject());
				activitySector.setClassificationConfig(sectorClassification);
				setModel.getObject().add(activitySector);
				list.removeAll();
				target.addComponent(list.getParent());
			}

			@Override
			public Integer getChoiceLevel(AmpSector choice) {
				int i = 0;
				AmpSector c = choice;
				while (c.getParentSectorId() != null) {
					i++;
					c = c.getParentSectorId();
				}
				return i;

			}
		};

		autoComplete.getModelParams().put(
				AmpSectorSearchModel.PARAM.SECTOR_SCHEME,
				sectorClassification.getClassification());
		autoComplete.getModelParams().put(
				AbstractAmpAutoCompleteModel.PARAM.MAX_RESULTS, 0);

		final AmpComboboxFieldPanel<AmpSector> searchSectors = new AmpComboboxFieldPanel<AmpSector>(
				"searchSectors", "Search " + fmName, autoComplete);
		add(searchSectors);
	}

}
