package org.dgfoundation.amp.onepager.components.fields;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.components.features.items.AmpGPINiOrgRoleItemFeaturePanel;
import org.dgfoundation.amp.onepager.helper.TemporaryGPINiDocument;
import org.dgfoundation.amp.onepager.validators.AmpGPINiIndicatorValidator;
import org.digijava.module.aim.dbentity.AmpGPINiQuestion.GPINiQuestionType;
import org.digijava.module.aim.dbentity.AmpGPINiSurveyResponse;
import org.digijava.module.aim.dbentity.AmpGPINiSurveyResponseDocument;

/**
 * This field can be used to validate the GPI Ni responses and show an error
 * message when one of the questions are not populated.
 * 
 * @author Viorel Chihai
 *
 */
public class AmpGPINiIndicatorValidatorField extends AmpCollectionValidatorField<AmpGPINiSurveyResponse, String> {
    private static final long serialVersionUID = 1L;

    /**
     * @param id
     * @param responseComponentInput
     * @param fmName
     */
    public AmpGPINiIndicatorValidatorField(String id,
                                           IModel<? extends Collection<AmpGPINiSurveyResponse>>
                                                   responseComponentInput, String fmName) {
        super(id, responseComponentInput, fmName, new AmpGPINiIndicatorValidator());
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
                boolean hasResponse = false;
                for (AmpGPINiSurveyResponse response : collectionModel.getObject()) {
                    if (isResponseEmpty(response)) {
                        ret.add("Q" + response.getAmpGPINiQuestion().getCode());
                    } else {
                        hasResponse = true;
                    }
                }

                if (hasResponse) {
                    return ret.size() > 0 ? ret.toString() : "";
                } else {
                    return "";
                }
            }
        };

        return model;
    }

    /**
     * Check if the response has a value or not
     *
     * @param response
     * @return true if is empty
     */
    protected boolean isResponseEmpty(AmpGPINiSurveyResponse response) {
        if (response.getAmpGPINiQuestion().getType() != GPINiQuestionType.LINK) {
            // the 10b response can be null when the question 10a has the respons 'No'
            if (response.getAmpGPINiQuestion().getCode().equals("10b")) {
                boolean isDependentResponsePresent = response.getAmpGPINiSurvey().getResponses()
                        .stream()
                        .filter(r -> r.getAmpGPINiQuestion().getCode().equals("10a"))
                        .findAny()
                        .map(r -> r.getQuestionOption())
                        .map(o -> o.getDescription())
                        .filter(d -> d.equals("Yes"))
                        .isPresent();

                return isDependentResponsePresent && response.isEmpty();
            }

            return response.isEmpty();
        } else {
            Set<TemporaryGPINiDocument> allResourcesNewItems =
                    getSession().getMetaData(OnePagerConst.GPI_RESOURCES_NEW_ITEMS);
            Set<TemporaryGPINiDocument> newResponseResourceItems = new HashSet<>();

            newResponseResourceItems = allResourcesNewItems.stream()
                    .filter(item -> response.getAmpGPINiSurvey().getAmpOrgRole().getOrganisation().getAmpOrgId()
                            == item.getSurveyResponse().getAmpGPINiSurvey().getAmpOrgRole().getOrganisation()
                            .getAmpOrgId() && response.getAmpGPINiQuestion().getCode().equals(item
                            .getSurveyResponse().getAmpGPINiQuestion().getCode())
                    )
                    .collect(Collectors.toSet());
            return newResponseResourceItems.size() == 0 && (response.isEmpty() || isEmptyAfterDelete(response));
        }
    }

    private boolean isEmptyAfterDelete(AmpGPINiSurveyResponse response) {
        Set<AmpGPINiSurveyResponseDocument> delItems =
                getSession().getMetaData(OnePagerConst.GPI_RESOURCES_DELETED_ITEMS);

        boolean isEmptyAfterDelete = false;
        if (delItems.size() > 0 && response.getSupportingDocuments().size() > 0) {
            Set<AmpGPINiSurveyResponseDocument> currentDocs = new HashSet<AmpGPINiSurveyResponseDocument>();
            currentDocs.addAll(response.getSupportingDocuments());
            currentDocs.removeAll(delItems);
            isEmptyAfterDelete = (currentDocs.size() <= 0);
        }
        return isEmptyAfterDelete;
    }

    @Override
    protected void onConfigure() {
        AmpGPINiOrgRoleItemFeaturePanel obj = (AmpGPINiOrgRoleItemFeaturePanel) this
                .findParent(AmpGPINiOrgRoleItemFeaturePanel.class);
        obj.putDonorInRequest();
        super.onConfigure();
    }
}
