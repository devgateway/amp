/**
 * Copyright (c) 2017 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.components.features.items;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.sections.AmpGPINiFormSectionFeature;
import org.dgfoundation.amp.onepager.components.fields.AmpAddLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpLinkField;
import org.dgfoundation.amp.onepager.events.GPINiQuestionUpdateEvent;
import org.dgfoundation.amp.onepager.events.UpdateEventBehavior;
import org.dgfoundation.amp.onepager.models.FilteredListModel;
import org.dgfoundation.amp.onepager.translation.TrnLabel;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpGPINiIndicator;
import org.digijava.module.aim.dbentity.AmpGPINiSurvey;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.gpi.util.GPIUtils;

/**
 * @author Viorel Chihai
 * @since Mar 02, 2017
 */
public class AmpGPINiOrgRoleItemFeaturePanel extends AmpFeaturePanel<AmpOrgRole> {

	private static final long serialVersionUID = -1230689136852083970L;
	private static final String INDICATOR_9B_CODE = "9b";

	public AmpGPINiOrgRoleItemFeaturePanel(String id, String fmName, final IModel<AmpOrgRole> donor, 
						IModel<AmpActivityVersion> am, final AmpGPINiFormSectionFeature parent) throws Exception {
		super(id, donor, fmName, true);
		
		PropertyModel<String> orgName = new PropertyModel<String>(donor, "organisation.name");
		
		Label indicatorNameURLLabel = new Label("organisationNameURL", orgName) {
			@Override
			public boolean isVisible() {
				return donor.getObject().getGpiNiSurvey() != null;
			}
		};
		add(indicatorNameURLLabel);
		
		Label indicatorNameLabel = new Label("organisationName", orgName) {
			@Override
			public boolean isVisible() {
				return donor.getObject().getGpiNiSurvey() == null;
			}
		};
		add(indicatorNameLabel);
		
		final AmpLinkField newItem = new AmpAddLinkField("addNewItem", "Add new item") {
			@Override
			protected void onClick(AjaxRequestTarget target) {
				AmpGPINiSurvey as = new AmpGPINiSurvey();
				as.setAmpOrgRole(donor.getObject());
				as.setSurveyDate(new Date());
				as.setResponses(new HashSet<>());
				donor.getObject().setGpiNiSurvey(as);

				target.add(parent);
				target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(parent));
				target.appendJavaScript("$('#" + getDonorToggleId(donor.getObject().getAmpOrgRoleId())  + "').show();");
			}

			@Override
			public boolean isVisible() {
				return super.isVisible() && donor.getObject().getGpiNiSurvey() == null;
			}
		};
		add(newItem);

		AmpLinkField deleteItem = new AmpDeleteLinkField("deleteItem", "Delete Item") {
			@Override
			protected void onClick(AjaxRequestTarget target) {
				donor.getObject().setGpiNiSurvey(null);
				
				target.add(parent);
				target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(parent));
			}

			@Override
			public boolean isVisible() {
				return super.isVisible() && donor.getObject().getGpiNiSurvey() != null;
			}
		};
		add(deleteItem);
		
		Label noSurveyLabel = new TrnLabel("noSurvey", "No survey available") {
			@Override
			public boolean isVisible() {
				return donor.getObject().getGpiNiSurvey() == null;
			}
		};
		add(noSurveyLabel);

		final AbstractReadOnlyModel<List<AmpGPINiIndicator>> listModel = new AbstractReadOnlyModel<List<AmpGPINiIndicator>>() {
			private static final long serialVersionUID = 1L;

			@Override
			public List<AmpGPINiIndicator> getObject() {
				ArrayList<AmpGPINiIndicator> list = new ArrayList<AmpGPINiIndicator>();
				if (donor.getObject().getGpiNiSurvey() != null) {
					list.addAll(GPIUtils.getActivityFormGPINiIndicators());
				}
				return list;
			}
		};
		
		FilteredListModel<AmpGPINiIndicator> filteredListModel = new FilteredListModel<AmpGPINiIndicator>(listModel.getObject()) {
			private static final long serialVersionUID = 1L;

			@Override
			protected boolean accept(AmpGPINiIndicator indicator) {
				
				/**
				 * the indicator 9B should be always visible 
				 
				if (indicator.getCode().equals(INDICATOR_9B_CODE)) {
					return hasDonorConcessionalityLevelFundings(am, donor);
				}
				*/
				
				return true;
			}
		};

		PropertyModel<AmpGPINiSurvey> surveyModel = new PropertyModel<AmpGPINiSurvey>(donor, "gpiNiSurvey");
		
		final ListView<AmpGPINiIndicator> indicatorList = new ListView<AmpGPINiIndicator>("listIndicators", filteredListModel) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<AmpGPINiIndicator> item) {
				AmpGPINiIndicator indicator = item.getModelObject();
				AmpGPINiIndicatorItemFeaturePanel ig = new AmpGPINiIndicatorItemFeaturePanel("indicatorItem", indicator.getName(), item.getModel(), surveyModel);
				item.add(ig);
			}
		};
		
		WebMarkupContainer indicatorPanel = new WebMarkupContainer("indicatorPanel");
		indicatorPanel.add(new AttributeModifier("id", getDonorToggleId(donor.getObject().getAmpOrgRoleId())));
		indicatorPanel.add(indicatorList);
		
		add(indicatorPanel);
	}
	
	/**
	 * Checks if the donor's fundings has concessionality field populated
	 * @param am
	 * @param donorModel
	 * @return
	 */
	private boolean hasDonorConcessionalityLevelFundings(IModel<AmpActivityVersion> am, IModel<AmpOrgRole> donorModel) {
		AmpActivityVersion activity = am.getObject();
		AmpOrgRole donor = donorModel.getObject();
		
		Optional<AmpFunding> fundingWithConcessionalityLevel = activity.getFunding().stream()
			.filter(f -> f.getAmpDonorOrgId().equals(donor.getOrganisation()) && f.getSourceRole().equals(donor.getRole()))
			.filter(f -> f.getConcessionalityLevel() != null)
			.findAny();
		
		return fundingWithConcessionalityLevel.isPresent();
	}
	
	private String getDonorToggleId(Long ampOrgRoleId) {
		return "indicatorPanel_" + ampOrgRoleId;
	}
}
