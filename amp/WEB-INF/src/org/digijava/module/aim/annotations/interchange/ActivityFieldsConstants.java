/**
 * 
 */
package org.digijava.module.aim.annotations.interchange;

/**
 * Stores activity fields related constants used in annotations for reuse
 * @author Nadejda Mandrescu
 */
public class ActivityFieldsConstants {
	public static final String AMP_ACTIVITY_ID = "Internal ID";
	public static final String CREATED_DATE = "Creation Date";
	public static final String UPDATE_DATE = "Update Date";
	public static final String PROJECT_CODE = "Project Code";
	public static final String AMP_ID = "AMP Id";
	public static final String PROJECT_TITLE = "Project Title";
	public static final String IS_DRAFT = "Is Draft";
    public static final String CHANGE_TYPE = "Change Type";

    public static enum ChangeType {

        MANUAL ("Manual"), IMPORT ("Import");

        private String value;

        ChangeType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }

    }
}
