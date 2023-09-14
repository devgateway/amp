/**
 * Copyright (c) 2014 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.components.features.items;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.models.PersistentObjectModel;
import org.dgfoundation.amp.onepager.translation.TrnLabel;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpGPISurvey;
import org.digijava.module.aim.dbentity.AmpGPISurveyIndicator;
import org.digijava.module.aim.dbentity.AmpGPISurveyResponse;
import org.digijava.module.aim.util.DbUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * @author ginchauspe@developmentgateway.org
 * @since Feb 05, 2014
 */
public class AmpGPIItemFeaturePanel extends AmpFeaturePanel<AmpGPISurvey> {

    public AmpGPIItemFeaturePanel(String id, String fmName, final IModel<AmpGPISurvey> survey, final IModel<AmpActivityVersion> am) {
        super(id, survey, fmName, true);
        if (survey.getObject().getResponses() == null) {
            survey.getObject().setResponses(new HashSet<>());
        }

        final AbstractReadOnlyModel<List<AmpGPISurveyIndicator>> listModel = new AbstractReadOnlyModel<List<AmpGPISurveyIndicator>>() {
            private static final long serialVersionUID = 3706182421429839210L;

            @Override
            public List<AmpGPISurveyIndicator> getObject() {
                ArrayList<AmpGPISurveyIndicator> list = new ArrayList<AmpGPISurveyIndicator>(DbUtil.getAllGPISurveyIndicators(true));
                list.sort(new AmpGPISurveyIndicator.GPISurveyIndicatorComparator());
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
