package org.dgfoundation.amp.onepager.components.features.items;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.extensions.ajax.markup.html.AjaxIndicatorAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.AmpRequiredComponentContainer;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpGPINiIndicatorValidatorField;
import org.dgfoundation.amp.onepager.helper.GPINiResponseComponentInput;
import org.dgfoundation.amp.onepager.translation.TrnLabel;
import org.digijava.module.aim.dbentity.AmpGPINiIndicator;
import org.digijava.module.aim.dbentity.AmpGPINiQuestion;
import org.digijava.module.aim.dbentity.AmpGPINiSurvey;
import org.digijava.module.aim.dbentity.AmpGPINiSurveyResponse;

/**
 * @author Viorel Chihai
 * @since Mar 02, 2017
 */
public class AmpGPINiIndicatorItemFeaturePanel extends AmpFeaturePanel<AmpGPINiIndicator> {

	private static final long serialVersionUID = 3285773837906913394L;
	AmpGPINiIndicatorValidatorField responsesValidationField;

	public AmpGPINiIndicatorItemFeaturePanel(String id, String fmName, final IModel<AmpGPINiIndicator> indicator,
			final IModel<AmpGPINiSurvey> survey) {
		super(id, indicator, fmName, true);

		Label indicatorNameLabel = new TrnLabel("indicatorName", new PropertyModel<String>(indicator, "name"));
		add(indicatorNameLabel);

		final AbstractReadOnlyModel<List<AmpGPINiQuestion>> listModel = new AbstractReadOnlyModel<List<AmpGPINiQuestion>>() {
			private static final long serialVersionUID = 3706184421459839210L;

			@Override
			public List<AmpGPINiQuestion> getObject() {
				Set<AmpGPINiQuestion> questions = (Set<AmpGPINiQuestion>) indicator.getObject().getQuestions();
				List<AmpGPINiQuestion> list = questions.stream().filter(q -> q.getRequiresDataEntry())
						.collect(Collectors.toList());

				Collections.sort(list, new AmpGPINiQuestion.GPINiQuestionComparator());

				return list;
			}
		};

		final ListView<AmpGPINiQuestion> list = new ListView<AmpGPINiQuestion>("listQuestions", listModel) {
			@Override
			protected void populateItem(final ListItem<AmpGPINiQuestion> item) {
				AmpGPINiQuestionItemFeaturePanel questionPanel = new AmpGPINiQuestionItemFeaturePanel("questionItem",
						item.getModel(), survey, responsesValidationField);
				item.add(questionPanel);
			}
		};

		IModel<List<AmpGPINiSurveyResponse>> listResponseComponentModel = 
				new AbstractReadOnlyModel<List<AmpGPINiSurveyResponse>>() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public List<AmpGPINiSurveyResponse> getObject() {
				List<AmpGPINiSurveyResponse> responseList = survey.getObject().getResponses().stream()
					.filter(resp -> responseBelongsToIndicator(resp))
					.collect(Collectors.toList());
				
				responseList.sort((AmpGPINiSurveyResponse o1, AmpGPINiSurveyResponse o2) 
							-> o1.getAmpGPINiQuestion().getIndex() - o2.getAmpGPINiQuestion().getIndex());;
				
				return responseList;
			}
		};

		responsesValidationField = new AmpGPINiIndicatorValidatorField(
				"surveyResponsesValidator", listResponseComponentModel, "surveyResponsesValidator") {
		};
		responsesValidationField.setOutputMarkupId(true);
		add(responsesValidationField);

		list.setReuseItems(true);
		add(list);
	}
	
	/**
	 * @param indicator
	 * @param resp
	 * @return
	 */
	private boolean responseBelongsToIndicator(AmpGPINiSurveyResponse resp) {
		return resp.getAmpGPINiQuestion().getAmpGPINiIndicator().getAmpGPINiIndicatorId().equals(getModel().getObject().getAmpGPINiIndicatorId());
	}
}
