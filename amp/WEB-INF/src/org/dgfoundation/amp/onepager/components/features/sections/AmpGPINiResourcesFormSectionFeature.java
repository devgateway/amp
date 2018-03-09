package org.dgfoundation.amp.onepager.components.features.sections;

import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.tables.AmpGPINiSurveyResourcesFormTableFeature;
import org.dgfoundation.amp.onepager.components.fields.AmpCollectionValidatorField;
import org.dgfoundation.amp.onepager.components.fields.AmpNewGPINiResourceFieldPanel;
import org.dgfoundation.amp.onepager.helper.TemporaryGPINiDocument;
import org.digijava.module.aim.dbentity.AmpGPINiSurveyResponse;
import org.digijava.module.aim.dbentity.AmpGPINiSurveyResponseDocument;

public class AmpGPINiResourcesFormSectionFeature extends AmpFeaturePanel<AmpGPINiSurveyResponse> {

    private static final long serialVersionUID = 1L;

    public static Logger logger = Logger.getLogger(AmpGPINiResourcesFormSectionFeature.class);

    public AmpGPINiResourcesFormSectionFeature(String id, String fmName,
            final IModel<AmpGPINiSurveyResponse> responseModel, 
            List<AmpCollectionValidatorField<AmpGPINiSurveyResponse, String>> responseValidationFields) 
                    throws Exception {

        super(id, fmName);

        if (responseModel.getObject().getSupportingDocuments() == null) {
            responseModel.getObject().setSupportingDocuments(new HashSet<AmpGPINiSurveyResponseDocument>());
        }

        if (getSession().getMetaData(OnePagerConst.GPI_RESOURCES_NEW_ITEMS) == null) {
            getSession().setMetaData(OnePagerConst.GPI_RESOURCES_NEW_ITEMS, new HashSet<TemporaryGPINiDocument>());
        }

        if (getSession().getMetaData(OnePagerConst.GPI_RESOURCES_DELETED_ITEMS) == null) {
            getSession().setMetaData(OnePagerConst.GPI_RESOURCES_DELETED_ITEMS,
                    new HashSet<AmpGPINiSurveyResponseDocument>());
        }

        final AmpGPINiSurveyResourcesFormTableFeature resourcesList = new AmpGPINiSurveyResourcesFormTableFeature(
                "resourcesList", "Supporting evidence", responseModel, responseValidationFields);
        resourcesList.setOutputMarkupId(true);
        resourcesList.setVisible(true);
        add(resourcesList);

        final AmpNewGPINiResourceFieldPanel newDoc = new AmpNewGPINiResourceFieldPanel("addNewDocument", responseModel,
                "Add New Document", resourcesList, responseValidationFields, false);
        newDoc.setOutputMarkupId(true);
        add(newDoc);

        final AmpNewGPINiResourceFieldPanel newLink = new AmpNewGPINiResourceFieldPanel("addNewWebLink", responseModel,
                "Add New Web Link", resourcesList, responseValidationFields, true);
        newLink.setOutputMarkupId(true);
        add(newLink);
    }

}
