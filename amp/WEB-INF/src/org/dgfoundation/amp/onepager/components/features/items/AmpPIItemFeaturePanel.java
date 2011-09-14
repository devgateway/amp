/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.components.features.items;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.AbstractSingleSelectChoice;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AbstractAmpAutoCompleteTextField;
import org.dgfoundation.amp.onepager.components.fields.AmpCategorySelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpComboboxFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpIndicatorGroupField;
import org.dgfoundation.amp.onepager.models.AmpOrganisationSearchModel;
import org.dgfoundation.amp.onepager.models.PersistentObjectModel;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.dgfoundation.amp.onepager.translation.TrnLabel;
import org.dgfoundation.amp.onepager.yui.AmpAutocompleteFieldPanel;
import org.digijava.module.aim.dbentity.AmpAhsurvey;
import org.digijava.module.aim.dbentity.AmpAhsurveyIndicator;
import org.digijava.module.aim.dbentity.AmpAhsurveyResponse;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpIndicatorRiskRatings;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.IndicatorActivity;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.MEIndicatorsUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.translation.util.TranslationManager;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * @author aartimon@dginternational.org
 * @since Mar 30, 2011
 */
public class AmpPIItemFeaturePanel extends AmpFeaturePanel<AmpAhsurvey> {
	

	/**
	 * @param id
	 * @param fmName
	 * @throws Exception
	 */
	public AmpPIItemFeaturePanel(String id, String fmName, final IModel<AmpAhsurvey> survey, IModel<AmpOrganisation> surveyOrg){
		super(id, survey, fmName, true);
		if (survey.getObject().getResponses() == null)
			survey.getObject().setResponses(new HashSet<AmpAhsurveyResponse>());

		if (survey.getObject().getPointOfDeliveryDonor() == null)
			survey.getObject().setPointOfDeliveryDonor(survey.getObject().getAmpDonorOrgId());
		Label indicatorNameLabel = new Label("orgName", new PropertyModel<String>(surveyOrg, "name"));
		add(indicatorNameLabel);
	
		final Label pod= new Label("PoD", new PropertyModel(survey, "pointOfDeliveryDonor"));
		pod.setOutputMarkupId(true);
		add(pod);
		
		final AmpAutocompleteFieldPanel<AmpOrganisation> searchOrgs=new AmpAutocompleteFieldPanel<AmpOrganisation>("orgSearch","Search Organizations",AmpOrganisationSearchModel.class) {			
			@Override
			protected String getChoiceValue(AmpOrganisation choice) {
				return choice.getName();
			}

			@Override
			public void onSelect(AjaxRequestTarget target, AmpOrganisation choice) {
				survey.getObject().setPointOfDeliveryDonor(choice);
				target.addComponent(pod);
			}

			@Override
			public Integer getChoiceLevel(AmpOrganisation choice) {
				return null;
			}
		};


		add(searchOrgs);

		final AbstractReadOnlyModel<List<AmpAhsurveyIndicator>> listModel = new AbstractReadOnlyModel<List<AmpAhsurveyIndicator>>() {
			private static final long serialVersionUID = 3706184421459839210L;
			@Override
			public List<AmpAhsurveyIndicator> getObject() {
				ArrayList<AmpAhsurveyIndicator> list = new ArrayList<AmpAhsurveyIndicator>(DbUtil.getAllAhSurveyIndicators());
				Collections.sort(list, new AmpAhsurveyIndicator.AhsurveyIndicatorComparator());
				return list;
			}
		};

		ListView<AmpAhsurveyIndicator> list = new ListView<AmpAhsurveyIndicator>("list", listModel) {
			@Override
			protected void populateItem(final ListItem<AmpAhsurveyIndicator> item) {
				AmpAhsurveyIndicator sv = item.getModelObject();
				
				Label indCode = new Label("indCode", new PropertyModel<String>(sv, "indicatorCode"));
				item.add(indCode);
				Label indName = new TrnLabel("indName", new PropertyModel<String>(sv, "name"));
				item.add(indName);
				
				String code = sv.getIndicatorCode();
				if (code.compareTo("7") == 0){
					String msg = "No question here. This indicator is calculated by the system based on information entered for disbursements for this project/programme";
					Label l = new TrnLabel("qList", new Model(msg));
					item.add(l);
				} else 
					if (code.compareTo("10a") == 0){
						String msg = "No question at the activity level; this indicator is calculated using the Calendar Module";
						Label l = new TrnLabel("qList", new Model(msg));
						item.add(l);
					} else 
						if (code.compareTo("10b") == 0){
							String msg = "No question at the activity level; this indicator is calculated using the Document Management Module";
							Label l = new TrnLabel("qList", new Model(msg));
							item.add(l);
						} else 
							if (code.compareTo("10b") == 0){
								String msg = "No question at the activity level; this indicator is calculated using the Document Management Module";
								Label l = new TrnLabel("qList", new Model(msg));
								item.add(l);
							} else {
								AmpPIQuestionItemFeaturePanel q = new AmpPIQuestionItemFeaturePanel("qList", "PI Questions List", PersistentObjectModel.getModel(sv), survey);
								item.add(q);
							}
			}
		};
		list.setReuseItems(true);
		add(list);
	}

}
