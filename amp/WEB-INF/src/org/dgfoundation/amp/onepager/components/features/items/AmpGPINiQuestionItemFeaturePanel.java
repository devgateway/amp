package org.dgfoundation.amp.onepager.components.features.items;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.features.sections.AmpGPINiResourcesFormSectionFeature;
import org.dgfoundation.amp.onepager.components.fields.AmpGPINiIndicatorValidatorField;
import org.dgfoundation.amp.onepager.translation.TrnLabel;
import org.digijava.module.aim.dbentity.AmpGPINiQuestion;
import org.digijava.module.aim.dbentity.AmpGPINiQuestion.GPINiQuestionType;
import org.digijava.module.aim.dbentity.AmpGPINiQuestionOption;
import org.digijava.module.aim.dbentity.AmpGPINiSurvey;
import org.digijava.module.aim.dbentity.AmpGPINiSurveyResponse;

/**
 * @author Viorel Chihai
 */
public class AmpGPINiQuestionItemFeaturePanel extends Panel {

	private static final long serialVersionUID = 2026765260335404697L;

	public AmpGPINiQuestionItemFeaturePanel(String id, final IModel<AmpGPINiQuestion> surveyQuestion, 
			final IModel<AmpGPINiSurvey> survey, AmpGPINiIndicatorValidatorField responsesValidationField) {
		super(id);
		this.setDefaultModel(surveyQuestion);
		Label questionIndex = new Label("questionIndex", new PropertyModel<String>(surveyQuestion, "code"));
		add(questionIndex);
		Label questionName = new TrnLabel("questionText", new PropertyModel<String>(surveyQuestion, "description"));
		add(questionName);
		
		
		Set<AmpGPINiSurveyResponse> responses = survey.getObject().getResponses();
		
		AmpGPINiSurveyResponse response = null;
		Optional<AmpGPINiSurveyResponse> surveyResponse = responses.stream()
			.filter(r -> r.getAmpGPINiQuestion().getAmpGPINiQuestionId().equals(surveyQuestion.getObject().getAmpGPINiQuestionId()))
			.findAny();
		
		if (surveyResponse.isPresent()) {
			response = surveyResponse.get();
		} else {
			response = new AmpGPINiSurveyResponse();
			response.setAmpGPINiSurvey(survey.getObject());
			response.setAmpGPINiQuestion(surveyQuestion.getObject());
			responses.add(response);
		}
		
		IModel<AmpGPINiSurveyResponse> responseModel = Model.of(response);
		
		GPINiQuestionType type = surveyQuestion.getObject().getType();
		if (type.equals(GPINiQuestionType.INTEGER)) {
			NumberTextField<Long> input = new NumberTextField<Long>("numberInput", new PropertyModel<Long>(responseModel, "integerResponse"));
			add(input);
			input.add(new AjaxFormComponentUpdatingBehavior("onchange") {
				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					target.add(input);
					target.add(responsesValidationField);
				}
			});
			add(hiddenMarkupContainer("textInput"), hiddenMarkupContainer("optionsInput"));
		} if (type.equals(GPINiQuestionType.FREE_TEXT)) {
			TextField<String> input = new TextField<String>("answerInput", new PropertyModel<String>(responseModel, "textResponse"));
			add(input);
			input.add(new AjaxFormComponentUpdatingBehavior("onchange") {
				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					target.add(input);
					target.add(responsesValidationField);
				}
			});
			add(hiddenMarkupContainer("numberInput"), hiddenMarkupContainer("optionsInput"));
		} else if (type.equals(GPINiQuestionType.MULTIPLE_CHOICE)) {
			List<AmpGPINiQuestionOption> options = new ArrayList<AmpGPINiQuestionOption>();
			options.addAll(surveyQuestion.getObject().getOptions());
			Collections.sort(options, (o1, o2) -> o1.getAmpGPINiQuestionOptionId().compareTo(o2.getAmpGPINiQuestionOptionId()));
			
			RadioChoice<AmpGPINiQuestionOption> answer = new RadioChoice<AmpGPINiQuestionOption>("optionsInput", 
					new PropertyModel<AmpGPINiQuestionOption>(responseModel, "questionOption"), 
					options, 
					new ChoiceRenderer<AmpGPINiQuestionOption>("description", "ampGPINiQuestionOptionId"));
			answer.add(new AjaxFormChoiceComponentUpdatingBehavior() {
				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					target.add(answer);
					target.add(responsesValidationField);
				}
			});
			add(answer);
			add(hiddenMarkupContainer("numberInput"), hiddenMarkupContainer("textInput"));
		} else if (type.equals(GPINiQuestionType.LINK)) {
			try {
				final AmpGPINiResourcesFormSectionFeature resourceContainer = new AmpGPINiResourcesFormSectionFeature("optionsInput", "resources", responseModel);
				resourceContainer.setOutputMarkupId(true);
				resourceContainer.setVisible(true);
				add(resourceContainer);
			} catch (Exception e) {
				e.printStackTrace();
			}

			add(hiddenMarkupContainer("numberInput"), hiddenMarkupContainer("textInput"));
		}
	}

	private Component hiddenMarkupContainer(String containerName) {
		WebMarkupContainer hiddenContainer = new WebMarkupContainer(containerName);
		hiddenContainer.setVisible(false);
		
		return hiddenContainer;
	}
}
