package org.dgfoundation.amp.onepager.helper;

import org.dgfoundation.amp.onepager.components.fields.AmpGPINiIndicatorValidatorField;

/**
 * This object is used for validation of GPI Ni Survey responses
 * @author Viorel
 *
 * @see AmpGPINiIndicatorValidatorField
 */
public class GPINiResponseComponentInput {

    private String questionCode;
    private String value;

    public String getQuestionCode() {
        return questionCode;
    }

    public void setQuestionCode(String question) {
        this.questionCode = question;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
