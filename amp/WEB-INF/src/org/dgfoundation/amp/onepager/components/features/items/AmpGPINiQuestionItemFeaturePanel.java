package org.dgfoundation.amp.onepager.components.features.items;

import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.dgfoundation.amp.onepager.components.features.sections.AmpGPINiResourcesFormSectionFeature;
import org.dgfoundation.amp.onepager.components.fields.AmpCollectionValidatorField;
import org.dgfoundation.amp.onepager.events.GPINiQuestionUpdateEvent;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.dgfoundation.amp.onepager.translation.TrnLabel;
import org.digijava.module.aim.dbentity.AmpGPINiQuestion;
import org.digijava.module.aim.dbentity.AmpGPINiQuestion.GPINiQuestionType;
import org.digijava.module.aim.dbentity.AmpGPINiQuestionOption;
import org.digijava.module.aim.dbentity.AmpGPINiSurvey;
import org.digijava.module.aim.dbentity.AmpGPINiSurveyResponse;

import java.util.*;

/**
 *  Class implementing behavior for GPI Ni Question panel
 * 
 * @author Viorel Chihai
 */
public class AmpGPINiQuestionItemFeaturePanel extends Panel {

    private static final String RESPONSE_NUMBER_ID = "numberInput";
    private static final String RESPONSE_TEXT_ID = "textInput";
    private static final String RESPONSE_OPTIONS_ID = "optionsInput";
    private static final String RESPONSE_SELECT_ID = "optionsSelect";
    private static final String INVALID_INTEGER_MESSAGE = TranslatorUtil.getTranslatedText("Answers may only be "
            + "positive whole numbers");

    protected WebMarkupContainer webLinkFeedbackContainer;
    protected Label webLinkFeedbackLabel;

    private static final long serialVersionUID = 2026765260335404697L;
    protected Set<AmpGPINiSurveyResponse> responses = new HashSet<>();

    protected static Logger logger = Logger.getLogger(AmpGPINiQuestionItemFeaturePanel.class);


    public AmpGPINiQuestionItemFeaturePanel(String id, final IModel<AmpGPINiQuestion> surveyQuestionModel,
                                            final IModel<AmpGPINiSurvey> survey,
                                            List<AmpCollectionValidatorField<AmpGPINiSurveyResponse, String>>
                                                    responseValidationFields) {

        super(id);
        this.setDefaultModel(surveyQuestionModel);
        this.responses = survey.getObject().getResponses();

        Label questionIndex = new Label("questionIndex", new PropertyModel<String>(surveyQuestionModel, "code"));
        add(questionIndex);

        Label questionName = new TrnLabel("questionText", new PropertyModel<String>(surveyQuestionModel,
                "description"));
        add(questionName);

        IModel<AmpGPINiSurveyResponse> responseModel = getResponseModel(surveyQuestionModel, survey);

        GPINiQuestionType type = surveyQuestionModel.getObject().getType();
        if (type.equals(GPINiQuestionType.INTEGER)) {
            TextField<Integer> input = getNumberResponseInput(responseValidationFields, responseModel);
            add(input);
            add(hiddenMarkup(RESPONSE_TEXT_ID), hiddenMarkup(RESPONSE_OPTIONS_ID), hiddenMarkup(RESPONSE_SELECT_ID));
        }
        if (type.equals(GPINiQuestionType.FREE_TEXT)) {
            TextField<String> input = getTextResponseInput(responseValidationFields, responseModel);
            add(input);
            add(hiddenMarkup(RESPONSE_NUMBER_ID), hiddenMarkup(RESPONSE_OPTIONS_ID), hiddenMarkup(RESPONSE_SELECT_ID));
        } else if (type.equals(GPINiQuestionType.MULTIPLE_CHOICE)) {
            RadioChoice<AmpGPINiQuestionOption> answer = getOptionsResponseInput(surveyQuestionModel,
                    responseValidationFields, responseModel);
            add(answer);
            add(hiddenMarkup(RESPONSE_NUMBER_ID), hiddenMarkup(RESPONSE_TEXT_ID), hiddenMarkup(RESPONSE_SELECT_ID));
        } else if (type.equals(GPINiQuestionType.SINGLE_CHOICE)) {
            DropDownChoice<AmpGPINiQuestionOption> choicesInput = getSingleChoiceResponseInput(surveyQuestionModel,
                    responseValidationFields, responseModel);
            add(choicesInput);
            add(hiddenMarkup(RESPONSE_NUMBER_ID), hiddenMarkup(RESPONSE_TEXT_ID), hiddenMarkup(RESPONSE_OPTIONS_ID));
        } else if (type.equals(GPINiQuestionType.LINK)) {
            AmpGPINiResourcesFormSectionFeature resourceContainer = getResourceResponseContainer(responseModel, 
                    responseValidationFields);
            add(resourceContainer);
            add(hiddenMarkup(RESPONSE_NUMBER_ID), hiddenMarkup(RESPONSE_TEXT_ID), hiddenMarkup(RESPONSE_SELECT_ID));
        }

        webLinkFeedbackContainer = new WebMarkupContainer("webLinkFeedbackContainer");
        webLinkFeedbackContainer.setOutputMarkupId(true);
        webLinkFeedbackContainer.setOutputMarkupPlaceholderTag(true);
        webLinkFeedbackContainer.setVisible(false);

        webLinkFeedbackLabel = new Label("webLinkFeedbackLabel", new Model<String>(INVALID_INTEGER_MESSAGE));
        webLinkFeedbackContainer.add(webLinkFeedbackLabel);

        webLinkFeedbackContainer.setVisible(false);

        add(webLinkFeedbackContainer);
    }

    /**
     * @param surveyQuestionModel
     * @param survey
     * @return
     */
    private IModel<AmpGPINiSurveyResponse> getResponseModel(final IModel<AmpGPINiQuestion> surveyQuestionModel,
                                                            final IModel<AmpGPINiSurvey> survey) {
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
        return responseModel;
    }

    /**
     * @param responseValidationFields
     * @param responseModel
     * @return
     */
    private TextField<Integer> getNumberResponseInput(
            List<AmpCollectionValidatorField<AmpGPINiSurveyResponse, String>> responseValidationFields,
            IModel<AmpGPINiSurveyResponse> responseModel) {
        TextField<Integer> input = new TextField<Integer>(RESPONSE_NUMBER_ID,
                new PropertyModel<Integer>(responseModel, "integerResponse"), Integer.class);
        input.add(new IValidator<Integer>() {
            @Override
            public void validate(IValidatable<Integer> validatable) {
                String value = String.valueOf(validatable.getValue());
                webLinkFeedbackContainer.setVisible(false);
                if (!isPositiveInteger(value)) {
                    ValidationError error = new ValidationError();
                    error.addKey("AmpGPINiQuestionNumericValidator");
                    validatable.error(error);
                }
            }

        });

        input.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                input.add(new AttributeModifier("class", ""));
                target.add(input);
                webLinkFeedbackContainer.setVisible(false);
                target.add(webLinkFeedbackContainer);
                responseValidationFields.stream().forEach(r -> r.reloadValidationField(target, false));
            }

            @Override
            protected void onError(AjaxRequestTarget target, RuntimeException e) {
                input.add(new AttributeModifier("class", "formcomponent invalid inputx"));
                target.add(input);
                webLinkFeedbackContainer.setVisible(true);
                webLinkFeedbackLabel.setDefaultModelObject(INVALID_INTEGER_MESSAGE);
                target.add(webLinkFeedbackContainer);
            }
        });
        return input;
    }

    private boolean isPositiveInteger(String value) {
        int intValue;
        try {
            intValue = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return false;
        }
        return intValue >= 0;
    }

    /**
     * @param responseValidationFields
     * @param responseModel
     * @return
     */
    private TextField<String> getTextResponseInput(
            List<AmpCollectionValidatorField<AmpGPINiSurveyResponse, String>> responseValidationFields,
            IModel<AmpGPINiSurveyResponse> responseModel) {
        TextField<String> input = new TextField<String>("answerInput",
                new PropertyModel<String>(responseModel, "textResponse"));
        input.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.add(input);
                responseValidationFields.stream().forEach(r -> r.reloadValidationField(target, false));
            }
        });
        return input;
    }

    /**
     * @param surveyQuestionModel
     * @param responseValidationFields
     * @param responseModel
     * @return
     */
    private RadioChoice<AmpGPINiQuestionOption> getOptionsResponseInput(
            final IModel<AmpGPINiQuestion> surveyQuestionModel,
            List<AmpCollectionValidatorField<AmpGPINiSurveyResponse, String>> responseValidationFields,
            IModel<AmpGPINiSurveyResponse> responseModel) {
        List<AmpGPINiQuestionOption> options = new ArrayList<AmpGPINiQuestionOption>();
        options.addAll(surveyQuestionModel.getObject().getOptions());
        Collections.sort(options,
                (o1, o2) -> o1.getAmpGPINiQuestionOptionId().compareTo(o2.getAmpGPINiQuestionOptionId()));

        RadioChoice<AmpGPINiQuestionOption> answer = new RadioChoice<AmpGPINiQuestionOption>(RESPONSE_OPTIONS_ID,
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

                responseValidationFields.stream().forEach(r -> r.reloadValidationField(target, false));
            }
        });
        return answer;
    }

    /**
     * @param surveyQuestionModel
     * @param responseValidationFields
     * @param responseModel
     * @return
     */
    private DropDownChoice<AmpGPINiQuestionOption> getSingleChoiceResponseInput(
            final IModel<AmpGPINiQuestion> surveyQuestionModel,
            List<AmpCollectionValidatorField<AmpGPINiSurveyResponse, String>> responseValidationFields,
            IModel<AmpGPINiSurveyResponse> responseModel) {
        List<AmpGPINiQuestionOption> options = new ArrayList<AmpGPINiQuestionOption>();
        options.addAll(surveyQuestionModel.getObject().getOptions());
        Collections.sort(options,
                (o1, o2) -> o1.getAmpGPINiQuestionOptionId().compareTo(o2.getAmpGPINiQuestionOptionId()));

        DropDownChoice<AmpGPINiQuestionOption> choices = new DropDownChoice<AmpGPINiQuestionOption>(RESPONSE_SELECT_ID,
                new PropertyModel<AmpGPINiQuestionOption>(responseModel, "questionOption"),
                options,
                new ChoiceRenderer<AmpGPINiQuestionOption>("description", "ampGPINiQuestionOptionId"));
        choices.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.add(choices);
                responseValidationFields.stream().forEach(r -> r.reloadValidationField(target, false));
            }
        });
        return choices;
    }

    /**
     * @param responseModel
     * @return
     */
    private AmpGPINiResourcesFormSectionFeature getResourceResponseContainer(
            IModel<AmpGPINiSurveyResponse> responseModel, 
            List<AmpCollectionValidatorField<AmpGPINiSurveyResponse, String>> responseValidationFields) {
        AmpGPINiResourcesFormSectionFeature resourceContainer = null;
        try {
            resourceContainer = new AmpGPINiResourcesFormSectionFeature(RESPONSE_OPTIONS_ID, "resources",
                    responseModel, responseValidationFields);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return resourceContainer;
    }

    /**
     * Generate an empty hidden WebMarkupContainer
     *
     * @param containerName
     * @return
     */
    private Component hiddenMarkup(String containerName) {
        WebMarkupContainer hiddenContainer = new WebMarkupContainer(containerName);
        hiddenContainer.setVisible(false);

        return hiddenContainer;
    }
}
