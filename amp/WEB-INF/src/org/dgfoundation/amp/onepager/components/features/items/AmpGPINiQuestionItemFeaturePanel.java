package org.dgfoundation.amp.onepager.components.features.items;

import java.util.Arrays;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.models.PIYesNoAnswerModel;
import org.dgfoundation.amp.onepager.translation.TrnLabel;
import org.digijava.module.aim.dbentity.AmpGPINiQuestion;
import org.digijava.module.aim.dbentity.AmpGPINiSurveyResponse;

/**
 * @author Viorel Chihai
 */
public class AmpGPINiQuestionItemFeaturePanel extends Panel {

	private static final long serialVersionUID = 2026765260335404697L;

	/**
	 * @param id
	 * @param fmName
	 * @throws Exception
	 */
	public AmpGPINiQuestionItemFeaturePanel(String id, final IModel<AmpGPINiQuestion> surveyQuestion) {
		super(id);
		Label questionIndex = new Label("questionIndex", new PropertyModel<String>(surveyQuestion, "index"));
		add(questionIndex);
		Label questionName = new TrnLabel("questionText", new PropertyModel<String>(surveyQuestion, "description"));
		add(questionName);
		
		AmpGPINiSurveyResponse response = new AmpGPINiSurveyResponse();
		response.setTextResponse("Yes");
		IModel<AmpGPINiSurveyResponse> responseModel = Model.of(response);
		
		final String[] elements = new String[] {"Yes", "No"};
		RadioChoice<String> answer = new RadioChoice<String>("answer", new PIYesNoAnswerModel(new PropertyModel<String>(responseModel, "textResponse")), Arrays.asList(elements));
		answer.setSuffix(" ");
		add(answer);
		
		TextField<String> hidden = new TextField<String>("answerInput");
		hidden.setVisible(false);
		add(hidden);
	}
}
