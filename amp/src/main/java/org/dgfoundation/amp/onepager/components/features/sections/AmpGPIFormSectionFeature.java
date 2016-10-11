/**
 * Copyright (c) 2014 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.sections;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.features.items.AmpGPIItemFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.items.AmpGPIQuestionItemFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.dgfoundation.amp.onepager.helper.GPIFormSectionSurveyComparator;
import org.dgfoundation.amp.onepager.models.PersistentObjectModel;
import org.dgfoundation.amp.onepager.translation.TrnLabel;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.DbUtil;

import java.util.*;

/**
 * GPI section
 * 
 * @author ginchauspe
 * @since Feb 04, 2014
 */
public class AmpGPIFormSectionFeature extends AmpFormSectionFeaturePanel {
	private transient static final Comparator<AmpGPISurvey> SURVEY_COMPARATOR = new GPIFormSectionSurveyComparator();

	public AmpGPIFormSectionFeature(String id, String fmName, final IModel<AmpActivityVersion> am) throws Exception {
		super(id, fmName, am);

		updateSurveySet(am);
		final AbstractReadOnlyModel<List<AmpGPISurvey>> listModel = OnePagerUtil.getReadOnlyListModelFromSetModel(new PropertyModel<Set<AmpGPISurvey>>(am, "gpiSurvey"), SURVEY_COMPARATOR);

		final ListView<AmpGPISurvey> list = new ListView<AmpGPISurvey>("list", listModel) {
			@Override
			protected void populateItem(final ListItem<AmpGPISurvey> item) {
				AmpGPIItemFeaturePanel indicator = new AmpGPIItemFeaturePanel("item", "GPI Item", PersistentObjectModel.getModel(item.getModelObject()), am);
				item.add(indicator);
			}
		};
		// list.setReuseItems(true);
		list.setOutputMarkupId(true);
		add(list);
	}

	private void updateSurveySet(IModel<AmpActivityVersion> am) {
		IModel<Set<AmpGPISurvey>> surveys = new PropertyModel<Set<AmpGPISurvey>>(am, "gpiSurvey");

		if (surveys.getObject() == null) {
			surveys.setObject(new TreeSet<AmpGPISurvey>());
		}
		if (surveys.getObject().size() == 0) {
			AmpGPISurvey as = new AmpGPISurvey();
			as.setAmpActivityId(am.getObject());
			as.setSurveyDate(new Date());
			// as.setAmpDonorOrgId(org);
			surveys.getObject().add(as);
		}

	}

}