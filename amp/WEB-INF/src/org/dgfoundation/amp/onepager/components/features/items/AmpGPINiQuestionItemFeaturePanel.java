package org.dgfoundation.amp.onepager.components.features.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.models.GPINiOptionAnswerModel;
import org.dgfoundation.amp.onepager.translation.TrnLabel;
import org.digijava.module.aim.dbentity.AmpAhsurveyResponse;
import org.digijava.module.aim.dbentity.AmpGPINiQuestion;
import org.digijava.module.aim.dbentity.AmpGPINiQuestionOption;
import org.digijava.module.aim.dbentity.AmpGPINiSurvey;
import org.digijava.module.aim.dbentity.AmpGPINiSurveyResponse;

/**
 * @author Viorel Chihai
 */
public class AmpGPINiQuestionItemFeaturePanel extends Panel {

	private static final long serialVersionUID = 2026765260335404697L;

	public AmpGPINiQuestionItemFeaturePanel(String id, final IModel<AmpGPINiQuestion> surveyQuestion, final IModel<AmpGPINiSurvey> survey) {
		super(id);
		Label questionIndex = new Label("questionIndex", new PropertyModel<String>(surveyQuestion, "index"));
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
		
		String type = surveyQuestion.getObject().getType();
		if (StringUtils.equals(type, "INTEGER")) {
			TextField<String> input = new TextField<String>("answerInput", new PropertyModel<String>(responseModel, "integerResponse"));
			add(input);
			WebMarkupContainer hidden = new WebMarkupContainer("answerRadio");
			hidden.setVisible(false);
			add(hidden);
		} else if (StringUtils.equals(type, "MULTIPLE_CHOICE")) {
			List<AmpGPINiQuestionOption> options = new ArrayList<AmpGPINiQuestionOption>();
			options.addAll(surveyQuestion.getObject().getOptions());
			Collections.sort(options, (o1, o2) -> o1.getAmpGPINiQuestionOptionId().compareTo(o2.getAmpGPINiQuestionOptionId()));
			List<String> optionsText = options.stream().map(o -> o.getDescription()).collect(Collectors.toList());
			RadioChoice<String> answer = new RadioChoice<String>("answerRadio", new GPINiOptionAnswerModel(new PropertyModel<String>(responseModel, "textResponse")), optionsText);
			add(answer);
			WebMarkupContainer hidden = new WebMarkupContainer("answerInput");
			hidden.setVisible(false);
			add(hidden);
		} else if (StringUtils.equals(type, "LINK") || true) {
			TextField<String> input = new TextField<String>("answerInput", new PropertyModel<String>(responseModel, "textResponse"));
			add(input);
			WebMarkupContainer hidden = new WebMarkupContainer("answerRadio");
			hidden.setVisible(false);
			add(hidden);
		}
	}
}
