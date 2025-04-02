/**
 * Copyright (c) 2014 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.components.features.items;

import java.util.*;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.AmpSearchOrganizationComponent;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.sections.AmpGPIFormSectionFeature;
import org.dgfoundation.amp.onepager.components.fields.*;
import org.dgfoundation.amp.onepager.models.AmpOrganisationSearchModel;
import org.dgfoundation.amp.onepager.models.DateToYearModel;
import org.dgfoundation.amp.onepager.models.PersistentObjectModel;
import org.dgfoundation.amp.onepager.translation.TrnLabel;
import org.dgfoundation.amp.onepager.yui.AmpAutocompleteFieldPanel;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.util.DbUtil;

import java.util.Collections;

/**
 * @author ginchauspe@developmentgateway.org
 * @since Feb 05, 2014
 */
public class AmpGPIItemFeaturePanel extends AmpFeaturePanel<AmpGPISurvey> {

    public AmpGPIItemFeaturePanel(String id, String fmName, final IModel<AmpGPISurvey> survey, final IModel<AmpActivityVersion> am) {
        super(id, survey, fmName, true);
        if (survey.getObject().getResponses() == null) {
            survey.getObject().setResponses(new HashSet<AmpGPISurveyResponse>());
        }

        final AbstractReadOnlyModel<List<AmpGPISurveyIndicator>> listModel = new AbstractReadOnlyModel<List<AmpGPISurveyIndicator>>() {
            private static final long serialVersionUID = 3706182421429839210L;

            @Override
            public List<AmpGPISurveyIndicator> getObject() {
                ArrayList<AmpGPISurveyIndicator> list = new ArrayList<AmpGPISurveyIndicator>(DbUtil.getAllGPISurveyIndicators(true));
                Collections.sort(list, new AmpGPISurveyIndicator.GPISurveyIndicatorComparator());
                return list;
            }
        };

        ListView<AmpGPISurveyIndicator> list = new ListView<AmpGPISurveyIndicator>("list", listModel) {
            @Override
            protected void populateItem(final ListItem<AmpGPISurveyIndicator> item) {
                AmpGPISurveyIndicator sv = item.getModelObject();

                Label indName = new TrnLabel("indName", new PropertyModel<String>(sv, "name"));
                item.add(indName);

                AmpGPIQuestionItemFeaturePanel q = new AmpGPIQuestionItemFeaturePanel("qList", "GPI Questions List", PersistentObjectModel.getModel(sv), survey);
                item.add(q);

            }
        };
        list.setReuseItems(true);
        add(list);
    }

}
