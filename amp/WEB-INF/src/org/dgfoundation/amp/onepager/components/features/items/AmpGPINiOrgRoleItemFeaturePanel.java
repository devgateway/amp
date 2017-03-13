/**
 * Copyright (c) 2017 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.components.features.items;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections.ComparatorUtils;
import org.apache.commons.collections.comparators.NullComparator;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.sections.AmpDonorFundingFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpGPINiFormSectionFeature;
import org.dgfoundation.amp.onepager.components.fields.AmpAddLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpLinkField;
import org.dgfoundation.amp.onepager.translation.TrnLabel;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpGPINiIndicator;
import org.digijava.module.aim.dbentity.AmpGPINiSurvey;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.gpi.util.GPIUtils;
import org.springframework.util.comparator.NullSafeComparator;

/**
 * @author Viorel Chihai
 * @since Mar 02, 2017
 */
public class AmpGPINiOrgRoleItemFeaturePanel extends AmpFeaturePanel<AmpOrgRole> {

	private static final long serialVersionUID = -1230689136852083970L;

	public AmpGPINiOrgRoleItemFeaturePanel(String id, String fmName, final IModel<AmpOrgRole> donor, final AmpGPINiFormSectionFeature parent) {
		super(id, donor, fmName, true);
		
		setDefaultModel(new CompoundPropertyModel<AmpOrgRole>(donor));

		Label indicatorNameLabel = new Label("organisation.name");
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
			}

			@Override
			public boolean isVisible() {
				return donor.getObject().getGpiNiSurvey() == null;
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
				return donor.getObject().getGpiNiSurvey() != null;
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
			private static final long serialVersionUID = 3706184421459839210L;

			@Override
			public List<AmpGPINiIndicator> getObject() {
				ArrayList<AmpGPINiIndicator> list = new ArrayList<AmpGPINiIndicator>(GPIUtils.getActivityFormGPINiIndicators());
				return list;
			}
		};

		Model<AmpGPINiSurvey> surveyModel = Model.of(donor.getObject().getGpiNiSurvey());
		
		final ListView<AmpGPINiIndicator> indicatorList = new ListView<AmpGPINiIndicator>("listIndicators", listModel) {
			@Override
			protected void populateItem(ListItem<AmpGPINiIndicator> item) {
				AmpGPINiIndicator indicator = item.getModelObject();
				AmpGPINiIndicatorItemFeaturePanel ig = new AmpGPINiIndicatorItemFeaturePanel("indicatorItem", indicator.getName(), item.getModel(), surveyModel);
				item.add(ig);
			}
			
			@Override
			public boolean isVisible() {
				return donor.getObject().getGpiNiSurvey() != null;
			}
		};
		
		add(indicatorList);
	}
	
}
