/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.tables;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpMinSizeCollectionValidationField;
import org.dgfoundation.amp.onepager.components.fields.AmpPercentageCollectionValidatorField;
import org.dgfoundation.amp.onepager.components.fields.AmpPercentageTextField;
import org.dgfoundation.amp.onepager.components.fields.AmpTreeCollectionValidatorField;
import org.dgfoundation.amp.onepager.components.fields.AmpUniqueCollectionValidatorField;
import org.dgfoundation.amp.onepager.models.AbstractAmpAutoCompleteModel;
import org.dgfoundation.amp.onepager.models.AmpSectorSearchModel;
import org.dgfoundation.amp.onepager.util.AmpDividePercentageField;
import org.dgfoundation.amp.onepager.yui.AmpAutocompleteFieldPanel;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.util.AmpAutoCompleteDisplayable;

/**
 * @author mpostelnicu@dgateway.org since Oct 20, 2010
 */
public class AmpSectorsFormTableFeature extends
		AmpFormTableFeaturePanel<AmpActivityVersion, AmpActivitySector> {

	/**
	 * @param id
	 * @param fmName
	 * @param am
	 * @throws Exception
	 */
	public AmpSectorsFormTableFeature(String id, String fmName,
			final IModel<AmpActivityVersion> am,
			final AmpClassificationConfiguration sectorClassification)
			throws Exception {
		super(id, am, fmName, false, true);
		final IModel<Set<AmpActivitySector>> setModel = new PropertyModel<Set<AmpActivitySector>>(
				am, "sectors");
		if (setModel.getObject() == null)
			setModel.setObject(new HashSet<AmpActivitySector>());

		IModel<List<AmpActivitySector>> listModel = new AbstractReadOnlyModel<List<AmpActivitySector>>() {

			@Override
			public List<AmpActivitySector> getObject() {
				// remove sectors with other classification
				ArrayList<AmpActivitySector> ret = new ArrayList<AmpActivitySector>();

				if(setModel.getObject()!=null)
				for (AmpActivitySector ampActivitySector : setModel.getObject()) {
					if (ampActivitySector.getClassificationConfig().getId()
							.equals(sectorClassification.getId()))
						ret.add(ampActivitySector);
				}
				return ret;
			}
		};

		final AmpPercentageCollectionValidatorField<AmpActivitySector> percentageValidationField = new AmpPercentageCollectionValidatorField<AmpActivitySector>(
				"sectorPercentageTotal", listModel, "sectorPercentageTotal") {
			@Override
			public Number getPercentage(AmpActivitySector item) {
				return item.getSectorPercentage();
			}
		};

		add(percentageValidationField);

		final AmpMinSizeCollectionValidationField<AmpActivitySector> minSizeCollectionValidationField = new AmpMinSizeCollectionValidationField<AmpActivitySector>(
				"minSizeSectorsValidator", listModel, "minSizeSectorsValidator");

		add(minSizeCollectionValidationField);
		
		
		final AmpUniqueCollectionValidatorField<AmpActivitySector> uniqueCollectionValidationField = new AmpUniqueCollectionValidatorField<AmpActivitySector>(
				"uniqueSectorsValidator", listModel, "uniqueSectorsValidator") {
			@Override
		 	public Object getIdentifier(AmpActivitySector t) {
				return t.getSectorId().getName();
		 	}	
		};

		add(uniqueCollectionValidationField);
		final AmpTreeCollectionValidatorField<AmpActivitySector> treeCollectionValidationField = new AmpTreeCollectionValidatorField<AmpActivitySector>(
				"treeSectorsValidator", listModel, "treeSectorsValidator") {
					@Override
					public AmpAutoCompleteDisplayable getItem(AmpActivitySector t) {
						return t.getSectorId();
					}			
		};

		add(treeCollectionValidationField);
		 

		list = new ListView<AmpActivitySector>("listSectors", listModel) {

			@Override
			protected void populateItem(final ListItem<AmpActivitySector> item) {
				final MarkupContainer listParent = this.getParent();
				PropertyModel<Double> percModel = new PropertyModel<Double>(
						item.getModel(), "sectorPercentage");

				AmpPercentageTextField percentageField = new AmpPercentageTextField(
						"percentage", percModel, "sectorPercentage",
						percentageValidationField);

				item.add(percentageField);

				item.add(new Label("sectorLabel", item.getModelObject()
						.getSectorId().getName()));

				AmpDeleteLinkField delSector = new AmpDeleteLinkField(
						"delSector", "Delete Sector") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						setModel.getObject().remove(item.getModelObject());
						target.addComponent(listParent);
						list.removeAll();
						percentageValidationField.reloadValidationField(target);
						uniqueCollectionValidationField.reloadValidationField(target);
						minSizeCollectionValidationField.reloadValidationField(target);		
						treeCollectionValidationField.reloadValidationField(target);
					}
				};
				item.add(delSector);

			}
		};
		list.setReuseItems(true);
		add(list);
		
		add(new AmpDividePercentageField<AmpActivitySector>("dividePercentage", "Divide Percentage", "Divide Percentage", setModel, list){
			@Override
			public void setPercentage(AmpActivitySector loc, int val) {
				loc.setSectorPercentage((float) val);
			}

			@Override
			public int getPercentage(AmpActivitySector loc) {
				return (int)((float)loc.getSectorPercentage());
			}

			@Override
			public boolean itemInCollection(AmpActivitySector item) {
				if (item.getClassificationConfig().getId()
						.equals(sectorClassification.getId()))
					return true;
				else
					return false;
			}

		});

		final AmpAutocompleteFieldPanel<AmpSector> searchSectors = new AmpAutocompleteFieldPanel<AmpSector>(
				"searchSectors", "Search " + fmName, AmpSectorSearchModel.class) {
			private static final long serialVersionUID = 1227775244079125152L;

			@Override
			protected String getChoiceValue(AmpSector choice) {
				return choice.getName();
			}

			@Override
			public void onSelect(AjaxRequestTarget target, AmpSector choice) {
				AmpActivitySector activitySector = new AmpActivitySector();
				activitySector.setSectorId(choice);
				activitySector.setActivityId(am.getObject());
				if(list.size()>0)
					activitySector.setSectorPercentage(0f);
				else 
					activitySector.setSectorPercentage(100f); 
				activitySector.setClassificationConfig(sectorClassification);
				setModel.getObject().add(activitySector);
				list.removeAll();
				target.addComponent(list.getParent());
				percentageValidationField.reloadValidationField(target);
				uniqueCollectionValidationField.reloadValidationField(target);
				minSizeCollectionValidationField.reloadValidationField(target);
				treeCollectionValidationField.reloadValidationField(target);
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

		searchSectors.getModelParams().put(
				AmpSectorSearchModel.PARAM.SECTOR_SCHEME,
				sectorClassification.getClassification());
		searchSectors.getModelParams().put(
				AbstractAmpAutoCompleteModel.PARAM.MAX_RESULTS, 0);

		add(searchSectors);
	}

}
