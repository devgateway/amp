package org.dgfoundation.amp.onepager.components.fields;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
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
					if (isResponseEmpty(response)) {
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
		}
		
		return false;
	}
}
