package org.dgfoundation.amp.onepager.components.fields;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.validators.AmpGPINiDependentQuestionValidator;
import org.digijava.module.aim.dbentity.AmpGPINiQuestion;
import org.digijava.module.aim.dbentity.AmpGPINiSurveyResponse;

/**
 * This field can be used to validate the GPI Survey responses for the fields depending on others
 * E.g.: the response for Q8 should be less than the response of Q9
 * 
 * @author Viorel Chihai
 *
 */
public class AmpGPINiDependentQuestionValidatorField extends AmpCollectionValidatorField<AmpGPINiSurveyResponse, String> {
    
    private static final long serialVersionUID = 1L;

    private static final String INDICATOR_CODE = "1";
    private static final String QUESTIONS_CODE_PATTERN = "[8|9]";
    
    public final static Comparator<AmpGPINiSurveyResponse> RESPONSE_INTEGER_COMPARATOR = 
            Comparator.comparing(AmpGPINiSurveyResponse::getIntegerResponse, 
            Comparator.nullsLast(Comparator.naturalOrder()));
    
    String dependentQuestionCode;

    /**
     * @param id
     * @param responseComponentInput
     * @param fmName
     */
    public AmpGPINiDependentQuestionValidatorField(String id,
            IModel<? extends Collection<AmpGPINiSurveyResponse>> responseComponentInput, String fmName, String dependentQuestionCode) {
        super(id, responseComponentInput, fmName, new AmpGPINiDependentQuestionValidator(dependentQuestionCode));
        
        this.dependentQuestionCode = dependentQuestionCode;
        hiddenContainer.setType(String.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.dgfoundation.amp.onepager.components.fields.
     * AmpCollectionValidatorField#getHiddenContainerModel(org.apache.wicket.
     * model.IModel)
     */
    public IModel<String> getHiddenContainerModel(
            IModel<? extends Collection<AmpGPINiSurveyResponse>> collectionModel) {

        Model<String> model = new Model<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String getObject() {
                List<String> ret = new ArrayList<String>();
                
                Optional<AmpGPINiSurveyResponse> dependentResponse = collectionModel.getObject().stream()
                        .filter(r -> r.getAmpGPINiQuestion().getCode().equals(dependentQuestionCode))
                        .findAny();
                
                if (dependentResponse.isPresent() && dependentResponse.get().getIntegerResponse() != null) {
                    for (AmpGPINiSurveyResponse response : collectionModel.getObject()) {
                        if (hasQuestionDependentValue(response.getAmpGPINiQuestion()) 
                                && RESPONSE_INTEGER_COMPARATOR.compare(dependentResponse.get(), response) < 0) {
                            
                            ret.add("Q" + response.getAmpGPINiQuestion().getCode());
                        }
                    }
                }

                return ret.size() > 0 ? ret.toString() : "";
            }
        };

        return model;
    }

    /**
     * Method used to determine if the question depends on other question
     * 
     * @param question
     * @return
     */
    private boolean hasQuestionDependentValue(AmpGPINiQuestion question) {
        return question.getCode().matches(QUESTIONS_CODE_PATTERN) 
                && question.getAmpGPINiIndicator().getCode().equals(INDICATOR_CODE);
    }
}
