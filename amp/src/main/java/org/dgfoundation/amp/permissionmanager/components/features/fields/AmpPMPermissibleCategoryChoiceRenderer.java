/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.fields;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;

/**
 * @author dan
 *
 */
public class AmpPMPermissibleCategoryChoiceRenderer extends ChoiceRenderer {

    /**
     * 
     */
    public AmpPMPermissibleCategoryChoiceRenderer() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param displayExpression
     */
    public AmpPMPermissibleCategoryChoiceRenderer(String displayExpression) {
        super(displayExpression);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param displayExpression
     * @param idExpression
     */
    public AmpPMPermissibleCategoryChoiceRenderer(String displayExpression,
            String idExpression) {
        super(displayExpression, idExpression);
        // TODO Auto-generated constructor stub
    }
    @Override
    public Object getDisplayValue(Object object)
    {
        String result = "";
        if(!(object instanceof Class)) return super.getDisplayValue(object);
        else{
            String s = ((Class)object).getSimpleName();
            if("AmpActivityVersion".compareTo(s)==0) result=TranslatorUtil.getTranslation("Activity");
            if("AmpFieldsVisibility".compareTo(s)==0) result= TranslatorUtil.getTranslation("Field");
            if("AmpFeaturesVisibility".compareTo(s)==0) result= TranslatorUtil.getTranslation("Feature");
            if("AmpModulesVisibility".compareTo(s)==0) result= TranslatorUtil.getTranslation("Module");
        }
        return result;
    }

}
