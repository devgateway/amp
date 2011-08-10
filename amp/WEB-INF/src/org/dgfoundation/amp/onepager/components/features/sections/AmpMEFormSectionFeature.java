/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.sections;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
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
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpComboboxFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpDatePickerFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpTextAreaFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.models.AbstractAmpAutoCompleteModel;
import org.dgfoundation.amp.onepager.models.AmpMEIndicatorSearchModel;
import org.dgfoundation.amp.onepager.models.AmpOrganisationSearchModel;
import org.dgfoundation.amp.onepager.models.AmpSectorSearchModel;
import org.dgfoundation.amp.onepager.models.PersistentObjectModel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.onepager.yui.AmpAutocompleteFieldPanel;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.IndicatorActivity;
import org.digijava.module.aim.util.SectorUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * M&E section
 * @author aartimon@dginternational.org 
 * @since Feb 10, 2011
 */
public class AmpMEFormSectionFeature extends AmpFormSectionFeaturePanel {
	private final ListView<IndicatorActivity> list;
	public AmpMEFormSectionFeature(String id, String fmName,
			final IModel<AmpActivityVersion> am) throws Exception {
		super(id, fmName, am);
		this.fmType = AmpFMTypes.MODULE;
		
		//final IModel<Set<IndicatorActivity>> setModel = new PropertyModel<Set<IndicatorActivity>>(am, "indicators");
		
		if (am.getObject().getIndicators() == null){
			am.getObject().setIndicators(new HashSet<IndicatorActivity>());
		}
		final AbstractReadOnlyModel<List<IndicatorActivity>> listModel = OnePagerUtil 
				.getReadOnlyListModelFromSetModel(new PropertyModel(am, "indicators"));
		
		list = new ListView<IndicatorActivity>("list", listModel) {
			@Override
			protected void populateItem(final ListItem<IndicatorActivity> item) {
				AmpMEItemFeaturePanel indicator = new AmpMEItemFeaturePanel("item", "ME Item", item.getModel(), PersistentObjectModel.getModel(item.getModelObject().getIndicator()), new PropertyModel(item.getModel(), "values"));
				item.add(indicator);
				AmpDeleteLinkField deleteLinkField = new AmpDeleteLinkField(
						"delete", "Delete ME Item", new Model<String>("Do you really want to delete this indicator?")) {
					@Override
					public void onClick(AjaxRequestTarget target) {
						am.getObject().getIndicators().remove(item.getModelObject());
						//setModel.getObject().remove(item.getModelObject());
						list.removeAll();
						target.addComponent(AmpMEFormSectionFeature.this);
						target.appendJavascript(OnePagerConst.getToggleChildrenJS(AmpMEFormSectionFeature.this));
					}
				};
				item.add(deleteLinkField);
			}
		};
		list.setReuseItems(true);
		add(list);
		
		final AmpAutocompleteFieldPanel<AmpIndicator> searchIndicators=new AmpAutocompleteFieldPanel<AmpIndicator>("search","Search Indicators",AmpMEIndicatorSearchModel.class) {			
			
			private static final long serialVersionUID = 1227775244079125152L;

			@Override
			protected String getChoiceValue(AmpIndicator choice) {
				return choice.getName();
			}

			@Override
			public void onSelect(AjaxRequestTarget target, AmpIndicator choice) {
				IndicatorActivity ia = new IndicatorActivity();
				ia.setActivity(am.getObject());
				ia.setIndicator(choice);
				am.getObject().getIndicators().add(ia);
				//setModel.getObject().add(ia);
				list.removeAll();
				target.addComponent(list.getParent());
				target.appendJavascript(OnePagerConst.getToggleChildrenJS(AmpMEFormSectionFeature.this));
			}

			@Override
			public Integer getChoiceLevel(AmpIndicator choice) {
				return 0;
			}
		};

		add(searchIndicators);

		final IModel<AmpIndicator> newInd = getNewIndicatorModel();
		
		add(new AmpTextFieldPanel<String>("indName", new PropertyModel<String>(newInd, "name"), "Name", AmpFMTypes.FEATURE));
		add(new AmpTextAreaFieldPanel<String>("indDesc", new PropertyModel<String>(newInd, "description"), "Description", false, AmpFMTypes.FEATURE));
		add(new AmpTextFieldPanel<String>("indCode", new PropertyModel<String>(newInd, "code"), "Code", AmpFMTypes.FEATURE));
		AmpDatePickerFieldPanel datePicker = new AmpDatePickerFieldPanel("indDate", new PropertyModel<Date>(newInd, "creationDate"), "Creation Date");
		datePicker.setEnabled(false);
		add(datePicker);
		
		ArrayList<String> typeCol = new ArrayList<String>();
		typeCol.add("A"); typeCol.add("D");
		
		ChoiceRenderer cr = new ChoiceRenderer(){
			@Override
			public Object getDisplayValue(Object object) {
				String s = (String)object;
				
				if (s.compareTo("A") == 0)
					return "ascending";
				else
					return "descending";
			}
		};
		add(new DropDownChoice("indType", new PropertyModel(newInd, "type"), typeCol, cr));
		
		
		
		
		
		final AmpClassificationConfiguration sectorClassification = SectorUtil.getPrimaryConfigClassification();
		final IModel<Set<AmpSector>> sectorSetModel = new PropertyModel<Set<AmpSector>>(
				newInd, "sectors");

		IModel<List<AmpSector>> sectorListModel = new AbstractReadOnlyModel<List<AmpSector>>() {

			@Override
			public List<AmpSector> getObject() {
				ArrayList<AmpSector> ret = new ArrayList<AmpSector>();
				ret.addAll(sectorSetModel.getObject());
				return ret;
			}
		};

		ListView<AmpSector> sectorList = new ListView<AmpSector>("listSectors", sectorListModel) {
			@Override
			protected void populateItem(final ListItem<AmpSector> item) {
				final MarkupContainer listParent = this.getParent();
				item.add(new Label("sectorLabel", item.getModelObject().getName()));

				AmpDeleteLinkField delSector = new AmpDeleteLinkField(
						"delSector", "Delete Sector") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						sectorSetModel.getObject().remove(item.getModelObject());
						target.addComponent(listParent);
						list.removeAll();
					}
				};
				item.add(delSector);

			}
		};
		list.setReuseItems(true);
		add(sectorList);

		final AmpAutocompleteFieldPanel<AmpSector> searchSectors=new AmpAutocompleteFieldPanel<AmpSector>("searchSectors", "Search " + fmName,AmpSectorSearchModel.class) {			

			private static final long serialVersionUID = 1227775244079125152L;

			@Override
			protected String getChoiceValue(AmpSector choice){
				return choice.getName();
			}

			@Override
			public void onSelect(AjaxRequestTarget target, AmpSector choice) {
				sectorSetModel.getObject().add(choice);
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

		searchSectors.getModelParams().put(AmpSectorSearchModel.PARAM.SECTOR_SCHEME,	sectorClassification.getClassification());
		searchSectors.getModelParams().put(AbstractAmpAutoCompleteModel.PARAM.MAX_RESULTS, 0);

		add(searchSectors);
		
		AmpAjaxLinkField addIndicator = new AmpAjaxLinkField("addIndicator", "Add Indicator", "Add Indicator", AmpFMTypes.FEATURE) {
			@Override
			public void onClick(AjaxRequestTarget target) {
				try {
					Session session = PersistenceManager.getSession();
					Transaction tr = session.beginTransaction();
					session.save(newInd.getObject());
					tr.commit();
					
					IndicatorActivity ia = new IndicatorActivity();
					ia.setActivity(am.getObject());
					ia.setIndicator(newInd.getObject());
					am.getObject().getIndicators().add(ia);
					//setModel.getObject().add(ia);
					
					newInd.setObject(getNewIndicator());
					
					target.addComponent(list.getParent());
					target.appendJavascript(OnePagerConst.getToggleChildrenJS(AmpMEFormSectionFeature.this));
				} catch (Exception e) {
					logger.error(e);
				}
			}
		};
		add(addIndicator);
	}
	
	private AmpIndicator getNewIndicator() {
		AmpIndicator newInd = new AmpIndicator();
		newInd.setSectors(new HashSet<AmpSector>());
		newInd.setCreationDate(new Date());
		return newInd;
	}

	private IModel<AmpIndicator> getNewIndicatorModel() {
		return new Model(getNewIndicator());
	}

}
