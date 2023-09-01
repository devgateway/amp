package org.dgfoundation.amp.onepager.components.features.items;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.OnePagerUtil.SerializablePredicate;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpCollectionValidatorField;
import org.dgfoundation.amp.onepager.components.fields.AmpGPINiDependentQuestionValidatorField;
import org.dgfoundation.amp.onepager.components.fields.AmpGPINiIndicatorValidatorField;
import org.dgfoundation.amp.onepager.events.GPINiQuestionUpdateEvent;
import org.dgfoundation.amp.onepager.events.UpdateEventBehavior;
import org.dgfoundation.amp.onepager.translation.TrnLabel;
import org.digijava.module.aim.dbentity.AmpGPINiIndicator;
import org.digijava.module.aim.dbentity.AmpGPINiQuestion;
import org.digijava.module.aim.dbentity.AmpGPINiSurvey;
import org.digijava.module.aim.dbentity.AmpGPINiSurveyResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The panel of the GPI Ni Survey Indicator
 * 
 * @author Viorel Chihai
 * @since Mar 02, 2017
 */
public class AmpGPINiIndicatorItemFeaturePanel extends AmpFeaturePanel<AmpGPINiIndicator> {

    private static final long serialVersionUID = 3285773837906913394L;
    List<AmpCollectionValidatorField<AmpGPINiSurveyResponse, String>> validationFields = new ArrayList<>();

    public AmpGPINiIndicatorItemFeaturePanel(String id, String fmName, final IModel<AmpGPINiIndicator> indicator,
            final IModel<AmpGPINiSurvey> survey) {
        
        super(id, indicator, fmName, true);

        Label indicatorNameLabel = new TrnLabel("indicatorName", new PropertyModel<String>(indicator, "name"));
        add(indicatorNameLabel);

        AbstractReadOnlyModel<List<AmpGPINiQuestion>> questionListModel = getQuestionListModel(indicator, survey);
        ListView<AmpGPINiQuestion> list = getGPINiQuestionPanelList(survey, questionListModel);
        add(list);
        
        add(UpdateEventBehavior.of(GPINiQuestionUpdateEvent.class));

        IModel<List<AmpGPINiSurveyResponse>> responseListModel = getSurveyResponseListModel(survey);
        addSurveyValidationFields(responseListModel);
    }

    /**
     * @param indicator
     * @param survey
     * @return
     */
    private AbstractReadOnlyModel<List<AmpGPINiQuestion>> getQuestionListModel(
            final IModel<AmpGPINiIndicator> indicator, final IModel<AmpGPINiSurvey> survey) {
        PropertyModel<Set<AmpGPINiQuestion>> indicatorQuestions = 
                new PropertyModel<Set<AmpGPINiQuestion>>(indicator, "questions");
        
        AbstractReadOnlyModel<List<AmpGPINiQuestion>> listModel = OnePagerUtil.getReadOnlyListModelFromSetModel(
                indicatorQuestions, 
                new AmpGPINiQuestion.GPINiQuestionComparator(), 
                isVisible(survey.getObject().getResponses()));
        return listModel;
    }

    /**
     * @param survey
     * @param listModel
     * @return
     */
    private ListView<AmpGPINiQuestion> getGPINiQuestionPanelList(final IModel<AmpGPINiSurvey> survey,
            AbstractReadOnlyModel<List<AmpGPINiQuestion>> listModel) {
        final ListView<AmpGPINiQuestion> list = new ListView<AmpGPINiQuestion>("listQuestions", listModel) {
            
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final ListItem<AmpGPINiQuestion> item) {
                AmpGPINiQuestionItemFeaturePanel questionPanel = new AmpGPINiQuestionItemFeaturePanel("questionItem",
                        item.getModel(), survey, validationFields) {
                    
                    private static final long serialVersionUID = 1L;
                };
                questionPanel.setOutputMarkupId(true);
                questionPanel.setVisibilityAllowed(true);
                item.add(questionPanel);
            }
        };
        return list;
    }
    
    /**
     * @param survey
     * @return
     */
    private IModel<List<AmpGPINiSurveyResponse>> getSurveyResponseListModel(final IModel<AmpGPINiSurvey> survey) {
        IModel<List<AmpGPINiSurveyResponse>> listResponseComponentModel = 
                new AbstractReadOnlyModel<List<AmpGPINiSurveyResponse>>() {
            
            private static final long serialVersionUID = 1L;

            @Override
            public List<AmpGPINiSurveyResponse> getObject() {
                List<AmpGPINiSurveyResponse> responseList = survey.getObject().getResponses().stream()
                    .filter(resp -> responseBelongsToIndicator(resp))
                    .collect(Collectors.toList());
                
                responseList.sort((AmpGPINiSurveyResponse o1, AmpGPINiSurveyResponse o2) 
                            -> o1.getAmpGPINiQuestion().getIndex() - o2.getAmpGPINiQuestion().getIndex());
                
                return responseList;
            }
        };
        return listResponseComponentModel;
    }
    
    /**
     * @param listResponseComponentModel
     */
    private void addSurveyValidationFields(IModel<List<AmpGPINiSurveyResponse>> listResponseComponentModel) {
        AmpGPINiIndicatorValidatorField responsesValidationField = 
                new AmpGPINiIndicatorValidatorField("surveyResponsesValidator", 
                        listResponseComponentModel, "surveyResponsesValidator") {
        };
        add(responsesValidationField);
        
        
        AmpGPINiDependentQuestionValidatorField dependentValidationField = 
                new AmpGPINiDependentQuestionValidatorField("surveyDependentQuestionValidator", 
                        listResponseComponentModel, "surveyDependentQuestionValidator", "7") {
        };
        add(dependentValidationField);
        
        validationFields.add(responsesValidationField);
        validationFields.add(dependentValidationField);
    }
    
    /**
     * 
     * @param responses - list of survey responses in order to check if 10a response is Yes or No
     * @return
     */
    public SerializablePredicate<AmpGPINiQuestion> isVisible(Set<AmpGPINiSurveyResponse> responses) {
        return q -> q.getRequiresDataEntry() && (!q.getCode().equals("10b") || responses.stream()
            .filter(r -> r.getAmpGPINiQuestion().getCode().equals("10a"))
            .findAny()
            .map(r -> r.getQuestionOption())
            .map(o -> o.getDescription())
            .filter(d -> d.equals("Yes"))
            .isPresent());
    }
    
    /**
     * @param indicator
     * @param resp
     * @return
     */
    private boolean responseBelongsToIndicator(AmpGPINiSurveyResponse resp) {
        return resp.getAmpGPINiQuestion().getAmpGPINiIndicator().getAmpGPINiIndicatorId()
                .equals(getModel().getObject().getAmpGPINiIndicatorId());
    }
}
