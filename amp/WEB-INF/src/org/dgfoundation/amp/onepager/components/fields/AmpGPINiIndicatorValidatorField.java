package org.dgfoundation.amp.onepager.components.fields;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.helper.GPINiResponseComponentInput;
import org.dgfoundation.amp.onepager.validators.AmpGPINiIndicatorValidator;
import org.digijava.module.aim.dbentity.AmpGPINiQuestion.GPINiQuestionType;
import org.digijava.module.aim.dbentity.AmpGPINiSurveyResponse;

/**
 * This field can be used to validate the GPI Ni responses and show an error
 * message when one of them is not populated It checks the responses form
 * component using the converted input
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
			IModel<? extends Collection<AmpGPINiSurveyResponse>> responseComponentInput, String fmName) {
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
				for (AmpGPINiSurveyResponse response : collectionModel.getObject()) {
					if (response.getAmpGPINiQuestion().getType() != GPINiQuestionType.LINK && response.isEmpty()) {
						ret.add("Q" + response.getAmpGPINiQuestion().getCode());
					}
				}

				if (ret.size() > 0)
					return ret.toString();

				return "";
			}
		};

		return model;
	}
}
