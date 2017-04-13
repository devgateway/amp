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
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.features.sections.AmpGPINiResourcesFormSectionFeature;
import org.dgfoundation.amp.onepager.components.fields.AmpGPINiIndicatorValidatorField;
import org.dgfoundation.amp.onepager.events.GPINiQuestionUpdateEvent;
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
	AmpGPINiQuestion surveyQuestion;
	Set<AmpGPINiSurveyResponse> responses;
	

	public AmpGPINiQuestionItemFeaturePanel(String id, final IModel<AmpGPINiQuestion> surveyQuestionModel, 
			final IModel<AmpGPINiSurvey> survey, AmpGPINiIndicatorValidatorField responsesValidationField) {
		super(id);
		this.setDefaultModel(surveyQuestionModel);
		surveyQuestion = surveyQuestionModel.getObject();
		Label questionIndex = new Label("questionIndex", new PropertyModel<String>(surveyQuestionModel, "code"));
		add(questionIndex);
		Label questionName = new TrnLabel("questionText", new PropertyModel<String>(surveyQuestionModel, "description"));
		add(questionName);
		
		responses = survey.getObject().getResponses();
		
		AmpGPINiSurveyResponse response = null;
		Optional<AmpGPINiSurveyResponse> surveyResponse = responses.stream()
			.filter(r -> r.getAmpGPINiQuestion().getAmpGPINiQuestionId()
					.equals(surveyQuestionModel.getObject().getAmpGPINiQuestionId()))
			.findAny();
		
		if (surveyResponse.isPresent()) {
			response = surveyResponse.get();
		} else {
			response = new AmpGPINiSurveyResponse();
			response.setAmpGPINiSurvey(survey.getObject());
			response.setAmpGPINiQuestion(surveyQuestionModel.getObject());
			responses.add(response);
		}
		
		IModel<AmpGPINiSurveyResponse> responseModel = Model.of(response);
		
		GPINiQuestionType type = surveyQuestionModel.getObject().getType();
		if (type.equals(GPINiQuestionType.INTEGER)) {
			NumberTextField<Long> input = new NumberTextField<Long>("numberInput", 
					new PropertyModel<Long>(responseModel, "integerResponse"));
			add(input);
			input.add(new AjaxFormComponentUpdatingBehavior("onchange") {
				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					target.add(input);
					target.add(responsesValidationField);
				}
			});
			add(hiddenMarkup("textInput"), hiddenMarkup("optionsInput"), hiddenMarkup("optionsSelect"));
		} if (type.equals(GPINiQuestionType.FREE_TEXT)) {
			TextField<String> input = new TextField<String>("answerInput", 
					new PropertyModel<String>(responseModel, "textResponse"));
			add(input);
			input.add(new AjaxFormComponentUpdatingBehavior("onchange") {
				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					target.add(input);
					target.add(responsesValidationField);
				}
			});
			add(hiddenMarkup("numberInput"), hiddenMarkup("optionsInput"), hiddenMarkup("optionsSelect"));
		} else if (type.equals(GPINiQuestionType.MULTIPLE_CHOICE)) {
			List<AmpGPINiQuestionOption> options = new ArrayList<AmpGPINiQuestionOption>();
			options.addAll(surveyQuestionModel.getObject().getOptions());
			Collections.sort(options, 
					(o1, o2) -> o1.getAmpGPINiQuestionOptionId().compareTo(o2.getAmpGPINiQuestionOptionId()));
			
			RadioChoice<AmpGPINiQuestionOption> answer = new RadioChoice<AmpGPINiQuestionOption>("optionsInput", 
					new PropertyModel<AmpGPINiQuestionOption>(responseModel, "questionOption"), 
					options, 
					new ChoiceRenderer<AmpGPINiQuestionOption>("description", "ampGPINiQuestionOptionId"));
			answer.add(new AjaxFormChoiceComponentUpdatingBehavior() {
				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					if (surveyQuestionModel.getObject().getCode().equals("10a")) {
						Optional<AmpGPINiSurveyResponse> response10a = responses.stream()
						.filter(r -> r.getAmpGPINiQuestion().getCode().equals("10b"))
						.findAny();
						
						if (response10a.isPresent()) {
							response10a.get().setQuestionOption(null);
						}
						send(getPage(), Broadcast.BREADTH, new GPINiQuestionUpdateEvent(target));
					} else {
						target.add(answer);
					}
					
					target.add(responsesValidationField);
				}
			});
			add(answer);
			add(hiddenMarkup("numberInput"), hiddenMarkup("textInput"), hiddenMarkup("optionsSelect"));
		} else if (type.equals(GPINiQuestionType.SINGLE_CHOICE)) {
			List<AmpGPINiQuestionOption> options = new ArrayList<AmpGPINiQuestionOption>();
			options.addAll(surveyQuestionModel.getObject().getOptions());
			Collections.sort(options, 
					(o1, o2) -> o1.getAmpGPINiQuestionOptionId().compareTo(o2.getAmpGPINiQuestionOptionId()));
			
			DropDownChoice<AmpGPINiQuestionOption> choices = new DropDownChoice<AmpGPINiQuestionOption>("optionsSelect", 
					new PropertyModel<AmpGPINiQuestionOption>(responseModel, "questionOption"), 
					options, 
					new ChoiceRenderer<AmpGPINiQuestionOption>("description", "ampGPINiQuestionOptionId"));
			choices.add(new AjaxFormComponentUpdatingBehavior("onchange") {
				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					target.add(choices);
					target.add(responsesValidationField);
				}
			});
			add(choices);
			add(hiddenMarkup("numberInput"), hiddenMarkup("textInput"), hiddenMarkup("optionsInput"));
		} else if (type.equals(GPINiQuestionType.LINK)) {
			try {
				final AmpGPINiResourcesFormSectionFeature resourceContainer = 
						new AmpGPINiResourcesFormSectionFeature("optionsInput", "resources", responseModel);
				add(resourceContainer);
			} catch (Exception e) {
				e.printStackTrace();
			}

			add(hiddenMarkup("numberInput"), hiddenMarkup("textInput"), hiddenMarkup("optionsSelect"));
		}
	}

	private Component hiddenMarkup(String containerName) {
		WebMarkupContainer hiddenContainer = new WebMarkupContainer(containerName);
		hiddenContainer.setVisible(false);
		
		return hiddenContainer;
	}
}
