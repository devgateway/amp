package org.digijava.module.contentrepository.helper.template;

import org.digijava.module.contentrepository.dbentity.template.*;

/**
 * Constants for Template Document fields
 * @author Dare
 *
 */
public class TemplateConstants {
    public static final String CHECHBOX_FIELD_TYPE="chf";
    public static final String DROPDOWN_FIELD_TYPE="ssf";
    public static final String MULTIBOX_FIELD_TYPE="mbf";
    public static final String MULTISELECT_FIELD_TYPE="msf";
    public static final String STATIC_TEXT_FIELD_TYPE="stf";
    public static final String TEXTAREA_FIELD_TYPE="taf";
    public static final String TEXTBOX_FIELD_TYPE="tbf";
    
    /**
     * Add here all the other classes that extend TemplateField class. 
     */
    public static final Class [] availableFieldClasses =new Class[] {
                                                                CheckboxField.class,
                                                                DropDownField.class,
                                                                MultiboxField.class,
                                                                MultipleSelect.class,
                                                                StaticTextField.class,
                                                                TextAreaField.class,
                                                                TextBoxField.class
                                                                };
    
    public static final String [] availableFieldTypes =new String[] {
                                                        "checkbox",
                                                        "single select",
                                                        "multibox",
                                                        "multiple select",
                                                        "static text",
                                                        "text area",
                                                        "text box"
                                                        };
    
    public static final String DOC_TYPE_PDF ="pdf";
    public static final String DOC_TYPE_WORD ="doc";
    
}
