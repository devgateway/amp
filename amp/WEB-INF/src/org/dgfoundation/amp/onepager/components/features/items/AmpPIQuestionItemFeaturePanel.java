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
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.AbstractSingleSelectChoice;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpCategorySelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpIndicatorGroupField;
import org.dgfoundation.amp.onepager.models.ActivityBudgetModel;
import org.dgfoundation.amp.onepager.models.PIYesNoAnswerModel;
import org.dgfoundation.amp.onepager.models.PersistentObjectModel;
import org.digijava.module.aim.dbentity.AmpAhsurvey;
import org.digijava.module.aim.dbentity.AmpAhsurveyIndicator;
import org.digijava.module.aim.dbentity.AmpAhsurveyQuestion;
import org.digijava.module.aim.dbentity.AmpAhsurveyResponse;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpIndicatorRiskRatings;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.IndicatorActivity;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.MEIndicatorsUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;

import com.rc.retroweaver.runtime.Arrays;

/**
 * @author aartimon@dginternational.org
 * @since Mar 30, 2011
 */
public class AmpPIQuestionItemFeaturePanel extends AmpFeaturePanel<AmpAhsurveyIndicator> {
	

	/**
	 * @param id
	 * @param fmName
	 * @throws Exception
	 */
	public AmpPIQuestionItemFeaturePanel(String id, String fmName, final IModel<AmpAhsurveyIndicator> surveyIndicator, final IModel<AmpAhsurvey> survey){
		super(id, surveyIndicator, fmName, true);
		
		final AbstractReadOnlyModel<List<AmpAhsurveyQuestion>> listModel = new AbstractReadOnlyModel<List<AmpAhsurveyQuestion>>() {
			private static final long serialVersionUID = 3706184421459839210L;
			@Override
			public List<AmpAhsurveyQuestion> getObject() {
				return new ArrayList<AmpAhsurveyQuestion>(surveyIndicator.getObject().getQuestions());
			}
		};
		
		
		ListView<AmpAhsurveyQuestion> list = new ListView<AmpAhsurveyQuestion>("list", listModel) {
			@Override
			protected void populateItem(final ListItem<AmpAhsurveyQuestion> item) {
				Set<AmpAhsurveyResponse> responses = survey.getObject().getResponses();
				
				AmpAhsurveyResponse response = null;
				Iterator<AmpAhsurveyResponse> it = responses.iterator();
				while (it.hasNext()) {
					AmpAhsurveyResponse rs = (AmpAhsurveyResponse) it
							.next();
					if (rs.getAmpQuestionId().getAmpQuestionId().compareTo(item.getModelObject().getAmpQuestionId()) == 0){
						response = rs;
						break;
					}
				}
				if (response == null){
					response = new AmpAhsurveyResponse();
					response.setAmpAHSurveyId(survey.getObject());
					response.setAmpQuestionId(item.getModelObject());
					responses.add(response);
				}
				
				IModel<AmpAhsurveyResponse> responseModel = PersistentObjectModel.getModel(response);
				
				Label indName = new Label("qtext", new PropertyModel<String>(item.getModelObject(), "questionText"));
				item.add(indName);
				
				String qtype = item.getModelObject().getAmpTypeId().getName();
				if (qtype.compareTo("yes-no") == 0){
					final String[] elements = new String[] {"Yes", "No"};
					RadioChoice<String> answer = new RadioChoice<String>("answer", new PIYesNoAnswerModel(new PropertyModel<String>(responseModel, "response")), Arrays.asList(elements));
					answer.setSuffix(" ");
					item.add(answer);
					
					TextArea hidden = new TextArea("answerInput");
					hidden.setVisible(false);
					item.add(hidden);
					
				} else
					if (qtype.compareTo("calculated") == 0){
						Label l = new Label("answer", "calculated");
						item.add(l);
						TextArea hidden = new TextArea("answerInput");
						hidden.setVisible(false);
						item.add(hidden);
					} else
						if (qtype.compareTo("input") == 0){
							TextArea<String> input = new TextArea<String>("answerInput", new PropertyModel<String>(responseModel, "response"));
							item.add(input);
							WebMarkupContainer hidden = new WebMarkupContainer("answer");
							hidden.setVisible(false);
							item.add(hidden);
						}
			}
		};
		list.setReuseItems(true);
		add(list);
	}
}
